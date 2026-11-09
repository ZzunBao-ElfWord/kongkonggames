const PURPOSES = {
	location: {
		title: '位置权限使用说明',
		content: '用于在你主动使用“定位当前城市”时获取所在城市，并展示同城服务及距离信息。我们不会在应用启动时申请或持续获取位置。',
		permissions: ['android.permission.ACCESS_FINE_LOCATION', 'android.permission.ACCESS_COARSE_LOCATION']
	},
	microphone: {
		title: '麦克风权限使用说明',
		content: '用于你主动进入语音互动、上麦或发送语音消息时采集声音。未获得授权时，浏览、文字聊天等功能仍可正常使用。',
		permissions: ['android.permission.RECORD_AUDIO']
	},
	cameraMedia: {
		title: '相机和照片权限使用说明',
		content: '用于你主动拍摄或选择头像、动态、聊天及反馈所需的图片或视频。仅处理你主动选择的内容。',
		permissions: ['android.permission.CAMERA']
	},
	storage: {
		title: '照片和视频权限使用说明',
		content: '用于你主动从相册选择、上传或保存图片和视频。仅处理你主动选择的内容，不会自行读取其他文件。',
		permissions: []
	}
}

function isAndroidApp() {
	// #ifdef APP-PLUS
	return typeof plus !== 'undefined' && plus.os && String(plus.os.name).toLowerCase() === 'android'
	// #endif
	return false
}

function permissionsGranted(permissions) {
	if (!permissions || !permissions.length || !isAndroidApp()) return false
	try {
		const main = plus.android.runtimeMainActivity()
		return permissions.some(permission => main.checkSelfPermission(permission) === 0)
	} catch (error) {
		return false
	}
}

export function confirmPermissionPurpose(type) {
	const purpose = PURPOSES[type]
	if (!purpose || !isAndroidApp() || permissionsGranted(purpose.permissions)) {
		return Promise.resolve(true)
	}
	return new Promise(resolve => {
		uni.showModal({
			title: purpose.title,
			content: purpose.content,
			confirmText: '继续',
			cancelText: '暂不使用',
			success: result => resolve(Boolean(result.confirm)),
			fail: () => resolve(false)
		})
	})
}

export function installAndroidPermissionPurposeGuards() {
	// main.js 运行时 plus 可能尚未就绪；等待原生环境后再安装，而不是在启动时申请权限。
	// #ifdef APP-PLUS
	if (typeof plus === 'undefined' && typeof document !== 'undefined') {
		document.addEventListener('plusready', installAndroidPermissionPurposeGuards, { once: true })
		return
	}
	// #endif
	if (!isAndroidApp() || uni.__permissionPurposeInstalled) return
	uni.__permissionPurposeInstalled = true

	const rawChooseImage = uni.chooseImage && uni.chooseImage.bind(uni)
	if (rawChooseImage) {
		uni.chooseImage = options => {
			const sourceType = options && Array.isArray(options.sourceType) ? options.sourceType : []
			const type = sourceType.length === 1 && sourceType[0] === 'album' ? 'storage' : 'cameraMedia'
			confirmPermissionPurpose(type).then(allowed => {
				if (allowed) rawChooseImage(options || {})
				else if (options && typeof options.fail === 'function') options.fail({ errMsg: 'chooseImage:fail user cancelled permission purpose' })
			})
		}
	}

	const rawChooseVideo = uni.chooseVideo && uni.chooseVideo.bind(uni)
	if (rawChooseVideo) {
		uni.chooseVideo = options => {
			const sourceType = options && Array.isArray(options.sourceType) ? options.sourceType : []
			const type = sourceType.length === 1 && sourceType[0] === 'album' ? 'storage' : 'cameraMedia'
			confirmPermissionPurpose(type).then(allowed => {
				if (allowed) rawChooseVideo(options || {})
				else if (options && typeof options.fail === 'function') options.fail({ errMsg: 'chooseVideo:fail user cancelled permission purpose' })
			})
		}
	}

	const rawGetLocation = uni.getLocation && uni.getLocation.bind(uni)
	if (rawGetLocation) {
		uni.getLocation = options => {
			confirmPermissionPurpose('location').then(allowed => {
				if (allowed) rawGetLocation(options || {})
				else if (options && typeof options.fail === 'function') options.fail({ errMsg: 'getLocation:fail user cancelled permission purpose' })
			})
		}
	}

	// 不要在应用启动时调用 getRecorderManager。部分 Android 运行环境会在
	// 创建录音管理器时触发麦克风权限申请，导致用户拒绝后重启仍再次弹窗。
	// 仅当用户进入录音功能、业务代码主动获取录音管理器时才创建并包装它。
	const rawGetRecorderManager = uni.getRecorderManager && uni.getRecorderManager.bind(uni)
	if (rawGetRecorderManager) {
		let recorderManager = null
		let startToken = 0
		const listeners = []
		const facade = {}
		// 获取管理器或注册回调不会触碰原生录音模块；用户确认用途后才创建。
		for (const event of ['Start', 'Stop', 'Pause', 'Resume', 'Error', 'FrameRecorded', 'InterruptionBegin', 'InterruptionEnd']) {
			facade['on' + event] = callback => {
				if (recorderManager) {
					if (typeof recorderManager['on' + event] === 'function') recorderManager['on' + event](callback)
				} else listeners.push([event, callback])
			}
		}
		facade.start = options => {
			const token = ++startToken
			return confirmPermissionPurpose('microphone').then(allowed => {
				if (!allowed || token !== startToken) return false
				if (!recorderManager) {
					recorderManager = rawGetRecorderManager()
					for (const [event, callback] of listeners) {
						if (typeof recorderManager['on' + event] === 'function') recorderManager['on' + event](callback)
					}
					listeners.length = 0
				}
				return recorderManager.start(options || {})
			})
		}
		for (const method of ['stop', 'pause', 'resume']) {
			facade[method] = (...args) => {
				// 松手或页面退出会取消尚未确认的录音，不会在权限弹窗关闭后突然录音。
				if (method !== 'resume') ++startToken
				if (recorderManager && typeof recorderManager[method] === 'function') return recorderManager[method](...args)
			}
		}
		uni.getRecorderManager = () => facade
	}
}
