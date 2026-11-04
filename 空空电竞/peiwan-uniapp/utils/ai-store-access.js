// 只保护 OpenElf 入口，不影响其他轮播图或普通外链。
export function isAiStoreUrl(value) {
	if (typeof value !== 'string') return false
	let target = value.trim()
	if (target.indexOf('/pages/index/webView?') === 0) {
		const match = target.match(/[?&]url=([^&]*)/)
		if (!match) return false
		try { target = decodeURIComponent(match[1]) } catch (error) { return false }
	}
	return /^https?:\/\/(?:[a-z0-9-]+\.)*openelfai\.com(?::\d+)?(?:[/?#]|$)/i.test(target)
}

export async function ensureAiStoreLogin(request, replace = false) {
	const token = uni.getStorageSync('token')
	const goLogin = () => {
		const navigate = replace ? uni.redirectTo : uni.navigateTo
		navigate({ url: '/pages/public/login' })
		return false
	}
	if (!token) return goLogin()
	try {
		// 登录是否有效以服务器为准，不把本地存在 token 当成已验证登录。
		const result = await request.get('/app/user/selectUserById')
		if (String(uni.getStorageSync('token')) !== String(token)) return goLogin()
		if (result && Number(result.code) === 0 && result.data && result.data.userId) return true
		if (result && Number(result.code) === 401) return goLogin()
		uni.showToast({ title: '暂时无法验证登录，请稍后重试', icon: 'none' })
		return false
	} catch (error) {
		uni.showToast({ title: '网络异常，暂时无法验证登录', icon: 'none' })
		return false
	}
}
