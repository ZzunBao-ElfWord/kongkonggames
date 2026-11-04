// 帖子列表不挂载原生 video：只在可见时提取封面，避免原生层穿透滚动卡片。
const pending = new Map()
let queue = Promise.resolve()

function hashUrl(url) {
	let hash = 2166136261
	for (let i = 0; i < url.length; i++) hash = Math.imul(hash ^ url.charCodeAt(i), 16777619)
	return (hash >>> 0).toString(16)
}

function exists(path) {
	return new Promise(resolve => plus.io.resolveLocalFileSystemURL(path, () => resolve(true), () => resolve(false)))
}

async function extract(url) {
	// #ifdef APP-PLUS
	if (plus.os.name !== 'Android') return {}
	const key = hashUrl(url)
	const relative = '_doc/post-video-previews/' + key + '.jpg'
	const storageKey = 'post-video-preview:' + key
	if (await exists(relative)) {
		const size = await new Promise(resolve => uni.getImageInfo({ src: relative, success: resolve, fail: () => resolve({}) }))
		return { poster: relative, duration: Number(uni.getStorageSync(storageKey)) || 0, width: size.width, height: size.height }
	}
	const file = await new Promise((resolve, reject) => {
		uni.downloadFile({ url, success: result => result.statusCode === 200 ? resolve(result.tempFilePath) : reject(new Error('Video download failed')), fail: reject })
	})
	const directory = plus.io.convertLocalFileSystemURL('_doc/post-video-previews/')
	const folder = plus.android.newObject('java.io.File', directory)
	plus.android.invoke(folder, 'mkdirs')
	const Retriever = plus.android.importClass('android.media.MediaMetadataRetriever')
	const Format = plus.android.importClass('android.graphics.Bitmap$CompressFormat')
	const retriever = new Retriever()
	let bitmap
	let stream
	try {
		retriever.setDataSource(plus.io.convertLocalFileSystemURL(file))
		const duration = Number(retriever.extractMetadata(9)) / 1000 || 0
		bitmap = retriever.getFrameAtTime(100000, 2)
		if (!bitmap) throw new Error('Video has no preview frame')
		stream = plus.android.newObject('java.io.FileOutputStream', plus.io.convertLocalFileSystemURL(relative))
		if (!plus.android.invoke(bitmap, 'compress', Format.JPEG, 82, stream)) throw new Error('Cannot save preview frame')
		plus.android.invoke(stream, 'flush')
		uni.setStorageSync(storageKey, duration)
		return { poster: relative, duration, width: plus.android.invoke(bitmap, 'getWidth'), height: plus.android.invoke(bitmap, 'getHeight') }
	} finally {
		if (stream) plus.android.invoke(stream, 'close')
		if (bitmap) plus.android.invoke(bitmap, 'recycle')
		retriever.release()
	}
	// #endif
	return {}
}

export function getPostVideoPreview(url) {
	if (!url) return Promise.resolve({})
	if (pending.has(url)) return pending.get(url)
	const task = queue.then(() => extract(url))
	queue = task.catch(() => {})
	pending.set(url, task)
	task.catch(() => pending.delete(url))
	return task
}
