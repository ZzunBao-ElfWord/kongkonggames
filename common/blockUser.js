const BLOCK_KEY = 'blockedUserIds'

export function getBlockedUserIds() {
	try {
		return (uni.getStorageSync(BLOCK_KEY) || []).map(item => String(item))
	} catch (e) {
		return []
	}
}

export function isUserBlocked(userId) {
	if (!userId) return false
	return getBlockedUserIds().indexOf(String(userId)) !== -1
}

export function blockUser(userId) {
	if (!userId) return false
	const ids = getBlockedUserIds()
	if (ids.indexOf(String(userId)) === -1) {
		ids.push(String(userId))
		uni.setStorageSync(BLOCK_KEY, ids)
	}
	return true
}

export function unblockUser(userId) {
	if (!userId) return false
	const ids = getBlockedUserIds().filter(item => item !== String(userId))
	uni.setStorageSync(BLOCK_KEY, ids)
	return true
}
