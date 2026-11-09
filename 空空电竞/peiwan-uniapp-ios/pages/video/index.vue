<template>
	<view class="video-page" :class="{ 'video-page-post': activeTopTab === '帖子' }">
		<view class="top-category-bar" :class="{ 'top-category-post': activeTopTab === '帖子' }">
			<view v-for="tab in topTabs" :key="tab" class="top-category-item" @click.stop="selectTopTab(tab)">
				<text class="top-category-text" :class="{ 'top-category-active': activeTopTab === tab }">{{ tab }}</text>
				<view v-if="activeTopTab === tab" class="top-category-line"></view>
			</view>
			<image v-if="activeTopTab === '帖子'" class="top-post-publish" src="../../static/images/my/dtfb.png" mode="aspectFit" @click.stop="publishPost"></image>
		</view>
		<!-- iOS 的 video/cover-view 是原生层，不走 DOM 层级，销毁也是异步的。
		     弹层打开或切到「帖子」时先把 stageAlive 置 false 一次性卸载整个原生舞台，
		     给原生层时间真正从屏幕移除后再显示普通 view 内容，
		     避免头像、评论、礼物等 cover-view 残留在列表或弹层之上并拦截点击。 -->
		<view v-if="stageAlive && currentVideo && !panelVisible" class="video-stage" :style="videoViewportStyle">
			<video
				:key="currentVideo.id"
				id="video-current"
				class="video-player"
				:src="currentVideo.src"
				:controls="false"
				:loop="true"
				:muted="muted"
				:autoplay="true"
				:show-center-play-btn="false"
				:show-fullscreen-btn="false"
				:show-play-btn="false"
				:show-progress="false"
				:enable-progress-gesture="false"
				:object-fit="currentVideo.objectFit"
				@loadedmetadata="onVideoMetadata($event, currentIndex)"
				@error="onVideoError"
			/>

			<cover-view
				class="video-gesture-layer"
				@touchstart="onVideoTouchStart($event, currentIndex)"
				@touchmove.stop="onVideoTouchMove"
				@touchend="onVideoTouchEnd($event, currentIndex)"
				@touchcancel="onVideoTouchCancel"
				@click="onVideoGestureTap(currentIndex)"
			></cover-view>

			<cover-view class="room-card" @click.stop="openAuthor(currentVideo)"></cover-view>
			<cover-image class="room-avatar" :src="currentVideo.avatar || '/static/logo.png'" mode="aspectFill" @click.stop="openAuthor(currentVideo)"></cover-image>
			<cover-view class="room-name" @click.stop="openAuthor(currentVideo)">{{ currentVideo.author }}</cover-view>
			<cover-view class="room-level" @click.stop="openAuthor(currentVideo)">动态发布者</cover-view>
			<cover-view class="room-status" @click.stop="openAuthor(currentVideo)">{{ currentVideo.createTime }}</cover-view>

			<cover-image class="side-avatar" :src="currentVideo.avatar || '/static/logo.png'" mode="aspectFill" @click.stop="openAuthor(currentVideo)"></cover-image>
			<cover-view v-if="!currentVideo.followed && !currentVideo.isSelf" class="follow-badge" @click.stop="toggleFollow(currentIndex)">+</cover-view>

			<cover-image class="like-icon side-like" :src="currentVideo.liked ? '../../static/images/video-heart-active.png' : '../../static/images/video-heart.png'" mode="aspectFit" @click.stop="toggleLike(currentIndex)"></cover-image>
			<cover-view class="action-count side-like-count" @click.stop="toggleLike(currentIndex)">{{ currentVideo.likeCount }}</cover-view>

			<cover-image class="comment-icon side-comment" src="../../static/images/video-message.png" mode="aspectFit" @click.stop="openComments"></cover-image>
			<cover-view class="action-count side-comment-count" @click.stop="openComments">{{ currentVideo.commentCount }}</cover-view>

			<cover-image class="gift-icon side-gift" src="../../static/images/video-gift.png" mode="aspectFit" @click.stop="openGifts"></cover-image>
			<cover-view class="action-label side-gift-label" @click.stop="openGifts">礼物</cover-view>

			<cover-view class="order-button" @click.stop="goToOrder(currentVideo)">{{ orderingUserId === currentVideo.userId ? '查询中…' : '去下单' }}</cover-view>
			<cover-view class="video-title">{{ currentVideo.author }}</cover-view>
			<cover-view class="author-badge">达人</cover-view>
			<cover-view class="online-badge">在线</cover-view>
			<cover-view class="video-description">{{ currentVideo.description }}</cover-view>
		</view>
		<view v-if="activeTopTab === '视频' && !videoList.length" class="video-empty">
			<text>{{ videoLoading ? '视频加载中…' : '暂无视频动态' }}</text>
		</view>
		<post-feed v-if="activeTopTab === '帖子'" ref="postFeed" class="post-feed-host" />

		<view v-if="panelVisible" class="panel-mask" @click="closePanel">
			<view class="bottom-panel" :class="{ 'comment-panel': panelType === 'comments' }" @click.stop="noop">
				<view class="panel-header">
					<text class="panel-title">{{ panelType === 'comments' ? '评论 ' + (currentVideo ? currentVideo.commentCount : 0) : '快来给Ta送个小礼物吧~' }}</text>
					<text class="panel-close" @click="closePanel">×</text>
				</view>
				<view v-if="panelType === 'comments'" class="panel-content">
					<scroll-view scroll-y class="comment-list" @scrolltolower="loadComments(false)">
						<view v-if="commentLoading && !comments.length" class="comment-state">评论加载中…</view>
						<view v-else-if="!comments.length" class="comment-state">暂无评论，快来说点什么吧</view>
						<view v-for="(comment, commentIndex) in comments" :key="comment.commentId || commentIndex" class="comment-item">
							<image class="comment-avatar" :src="comment.imageUrl || '/static/logo.png'" mode="aspectFill" />
							<view class="comment-body">
								<view class="comment-meta">
									<text class="comment-name">{{ comment.nickName || '神秘用户' }}</text>
									<text class="comment-time">{{ comment.createTime || '' }}</text>
								</view>
								<text class="comment-text">{{ comment.content }}</text>
							</view>
						</view>
						<view v-if="commentLoading && comments.length" class="comment-more">加载中…</view>
					</scroll-view>
					<view class="comment-input-row">
						<input v-model="commentText" class="comment-input" confirm-type="send" placeholder="说点什么…" @confirm="sendComment" />
						<text class="send-button" :class="{ 'send-button-disabled': commentSending }" @click="sendComment">{{ commentSending ? '发送中' : '发送' }}</text>
					</view>
				</view>
				<view v-else class="gift-panel-content">
					<view class="gift-balance-row">
						<view class="gift-balance"><image src="../../static/images/index/jinbi.png" mode="aspectFit" />{{ giftBalance }}</view>
						<view class="gift-recharge" @click="recharge">充值</view>
					</view>
					<scroll-view scroll-y class="gift-scroll">
						<view v-if="giftLoading" class="gift-state">礼物加载中…</view>
						<view v-else-if="!gifts.length" class="gift-state">暂无可用礼物</view>
						<view v-else class="gift-list">
							<view v-for="gift in gifts" :key="gift.giftId" class="gift-item" :class="{ 'gift-item-selected': selectedGiftId === gift.giftId }" @click="selectGift(gift)">
								<image class="gift-image" :src="gift.giftImg" mode="aspectFit" />
								<text class="gift-name">{{ gift.giftName }}</text>
								<view class="gift-price">{{ gift.giftMoney }}<image src="../../static/images/index/jinbi.png" mode="aspectFit" /></view>
							</view>
						</view>
					</scroll-view>
					<view v-if="!currentVideoRecipientId" class="gift-notice">当前演示视频尚未关联发布者，暂不能送礼</view>
					<view class="gift-send" :class="{ 'gift-send-disabled': !selectedGiftId || !currentVideoRecipientId || giftSending }" @click="sendGift">{{ giftSending ? '赠送中…' : '赠送' }}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import PostFeed from '../index/post-feed.vue'

	export default {
		components: { PostFeed },
		data() {
			return {
				currentIndex: 0,
				playing: true,
				// 原生舞台（video + cover-view）是否挂载；切 Tab 时先卸载再切换
				stageAlive: true,
				videoTouchStartX: 0,
				videoTouchStartY: 0,
				videoTouchMoved: false,
				videoTouchWasSwipe: false,
				videoViewportTop: 0,
				videoViewportHeight: 0,

				// 浏览器通常禁止带声音自动播放，因此默认静音
				muted: true,
				panelVisible: false,
				panelType: '',
				commentText: '',
				comments: [],
				commentLoading: false,
				commentSending: false,
				commentPage: 0,
				commentSize: 20,
				commentHasMore: true,
				gifts: [],
				giftBalance: 0,
				giftLoading: false,
				giftSending: false,
				selectedGiftId: null,
				videoLoading: false,
				videoPage: 0,
				videoSize: 20,
				videoHasMore: true,
				orderingUserId: null,
				topTabs: ['视频', '帖子'],
				activeTopTab: '视频',

				videoList: []
			}
		},

		computed: {
			videoViewportStyle() {
				if (!this.videoViewportHeight) return {}
				return {
					top: this.videoViewportTop + 'px',
					height: this.videoViewportHeight + 'px'
				}
			},
			currentVideo() {
				return this.videoList[this.currentIndex] || null
			},
			currentVideoRecipientId() {
				const video = this.currentVideo
				return video && video.userId ? video.userId : null
			}
		},

		onReady() {
			this.updateVideoViewport()
			setTimeout(() => {
				if (this.activeTopTab === '视频') this.playVideo(0)
			}, 300)
		},

		onShow() {
			this.loadVideos(true)
			setTimeout(() => {
				if (this.activeTopTab === '视频') this.playVideo(this.currentIndex)
				else if (this.$refs.postFeed) this.$refs.postFeed.refreshPosts()
			}, 300)
		},

		onHide() {
			this.pauseAllVideos()
		},

		onUnload() {
			this.pauseAllVideos()
		},

		onResize() {
			this.updateVideoViewport()
		},

		methods: {
			noop() {},
			updateVideoViewport() {
				this.$nextTick(() => {
					const systemInfo = uni.getSystemInfoSync()
					const windowHeight = Number(systemInfo.windowHeight || 0)
					uni.createSelectorQuery().in(this).select('.top-category-bar').boundingClientRect(rect => {
						const fallbackTop = Number(systemInfo.statusBarHeight || 0) + 44
						const top = rect && Number(rect.bottom) > 0 ? Number(rect.bottom) : fallbackTop
						this.videoViewportTop = Math.round(top)
						this.videoViewportHeight = Math.max(240, Math.round(windowHeight - top))
					}).exec()
				})
			},
			publishPost() {
				if (this.$refs.postFeed) this.$refs.postFeed.bindfb()
			},
			selectTopTab(tab) {
				if (tab === this.activeTopTab) return
				this.panelVisible = false
				if (tab === '帖子') {
					this.pauseAllVideos()
					// 先卸载原生舞台，等原生层从屏幕移除后再切 Tab，
					// 否则 cover-view 会残留在帖子列表之上
					this.stageAlive = false
					setTimeout(() => {
						if (this.activeTopTab !== '帖子') this.activeTopTab = '帖子'
					}, 100)
				} else {
					this.activeTopTab = '视频'
					if (!this.videoList.length) this.loadVideos(true)
					this.$nextTick(() => {
						this.stageAlive = true
						this.$nextTick(() => this.playVideo(this.currentIndex))
					})
				}
			},
			openAuthor(item) {
				if (!item.userId) return
				uni.navigateTo({ url: '/my/gird/guanzhuDetail?userId=' + item.userId })
			},
			goToOrder(item) {
				if (!item.userId || this.orderingUserId) return
				if (item.orderTakingId) {
					uni.navigateTo({ url: '/pages/index/game/order?id=' + item.orderTakingId })
					return
				}
				this.orderingUserId = item.userId
				this.$Request.getT('/app/orderTaking/selectUserOrderTaking', {
					page: 1,
					limit: 1,
					userId: item.userId
				}).then(res => {
					const list = res.code === 0 && res.data && Array.isArray(res.data.list) ? res.data.list : []
					if (!list.length) {
						uni.showToast({ title: '该用户暂未发布可下单技能', icon: 'none' })
						return
					}
					this.$set(item, 'orderTakingId', list[0].id)
					uni.navigateTo({ url: '/pages/index/game/order?id=' + list[0].id })
				}).catch(() => {
					uni.showToast({ title: '技能信息加载失败', icon: 'none' })
				}).finally(() => { this.orderingUserId = null })
			},
			toggleFollow(index) {
				const item = this.videoList[index]
				if (!this.requireLogin() || item.isSelf) return
				this.$Request.get('/app/userFollow/insert', { followUserId: item.userId }).then(res => {
					if (res.code !== 0) return uni.showToast({ title: res.msg || '操作失败', icon: 'none' })
					const followed = !item.followed
					this.videoList.forEach((video, videoIndex) => {
						if (String(video.userId) === String(item.userId)) this.$set(this.videoList[videoIndex], 'followed', followed)
					})
					uni.showToast({ title: res.msg || (followed ? '关注成功' : '取消关注成功'), icon: 'none' })
				})
			},
			toggleLike(index) {
				const item = this.videoList[index]
				if (!this.requireLogin()) return
				this.$Request.postJson('/app/gambit/saveGoodTalk', { talkId: item.id, userId: this.$queue.getData('userId') }).then(res => {
					if (res.status !== 0) return uni.showToast({ title: res.msg || '点赞失败', icon: 'none' })
					this.$set(item, 'liked', !item.liked)
					this.$set(item, 'likeCount', item.likeCount + (item.liked ? 1 : -1))
					if (item.liked) this.playLikeAnimation(item)
				})
			},
			playLikeAnimation(item) {
					this.$set(item, 'likeAnimating', false)
					this.$nextTick(() => {
						this.$set(item, 'likeAnimating', true)
						setTimeout(() => this.$set(item, 'likeAnimating', false), 650)
					})
			},
			openComments() {
				if (this.panelVisible) return
				this.pauseVideo(this.currentIndex)
				this.playing = false
				this.panelType = 'comments'
				this.panelVisible = true
				this.loadComments(true)
			},
			loadComments(reset) {
				if (this.commentLoading || !this.currentVideo) return
				if (reset) {
					this.commentPage = 0
					this.commentHasMore = true
					this.comments = []
				}
				if (!this.commentHasMore) return
				this.commentLoading = true
				this.$Request.getT('/app/gambit/selectCommentPage', {
					page: this.commentPage,
					size: this.commentSize,
					talkId: this.currentVideo.id
				}).then(res => {
					const records = res.status === 0 && res.data && Array.isArray(res.data.content) ? res.data.content : []
					this.comments = this.comments.concat(records)
					this.commentHasMore = records.length === this.commentSize
					this.commentPage += 1
				}).catch(() => {
					uni.showToast({ title: '评论加载失败', icon: 'none' })
				}).finally(() => { this.commentLoading = false })
			},
			openGifts() {
				if (this.panelVisible) return
				this.pauseVideo(this.currentIndex)
				this.playing = false
				this.panelType = 'gifts'
				this.panelVisible = true
				this.loadGifts()
				if (this.$queue.getData('token')) this.loadGiftBalance()
			},
			loadGifts() {
				this.giftLoading = true
				this.$Request.getT('/app/gift/selectGiftList', { status: 1 }).then(res => {
					this.gifts = res.code === 0 && Array.isArray(res.data) ? res.data : []
				}).catch(() => {
					this.gifts = []
					uni.showToast({ title: '礼物加载失败', icon: 'none' })
				}).finally(() => { this.giftLoading = false })
			},
			loadGiftBalance() {
				this.$Request.get('/app/userMoney/selectMyMoney').then(res => {
					if (res.code === 0 && res.data) this.giftBalance = res.data.money
				})
			},
			recharge() {
				if (!this.$queue.getData('token')) {
					uni.navigateTo({ url: '/pages/public/login' })
					return
				}
				uni.navigateTo({ url: '/my/wallet/index?index=1' })
			},
			closePanel() {
				this.panelVisible = false
				// 等 iOS 重新创建原生 video 视图后再播放，避免 createVideoContext 调用过早。
				this.$nextTick(() => setTimeout(() => this.playVideo(this.currentIndex), 100))
			},
			sendComment() {
				if (!this.commentText.trim() || this.commentSending) return
				if (!this.requireLogin()) return
				if (this.$queue.getChatSearchKeys(this.commentText)) {
					uni.showToast({ title: '评论包含非法关键字，请重新输入', icon: 'none' })
					return
				}
				const item = this.videoList[this.currentIndex]
				this.commentSending = true
				this.$Request.postJson('/app/gambit/saveComment', {
					talkId: item.id,
					userId: this.$queue.getData('userId'),
					content: this.commentText.trim()
				}).then(res => {
					if (res.status !== 0) return uni.showToast({ title: res.msg || '评论失败', icon: 'none' })
					item.commentCount += 1
					this.commentText = ''
					uni.showToast({ title: '评论成功', icon: 'none' })
					this.loadComments(true)
				}).catch(() => {
					uni.showToast({ title: '评论失败，请稍后重试', icon: 'none' })
				}).finally(() => { this.commentSending = false })
			},
			requireLogin() {
				if (this.$queue.getData('token')) return true
				uni.navigateTo({ url: '/pages/public/login' })
				return false
			},
			loadVideos(reset) {
				if (this.videoLoading) return
				if (reset) {
					this.videoPage = 0
					this.videoHasMore = true
					this.videoList = []
					this.currentIndex = 0
				}
				if (!this.videoHasMore) return
				this.videoLoading = true
				const viewerId = this.$queue.getData('userId') || ''
				this.$Request.getT('/app/gambit/selectTalkPage', {
					page: this.videoPage,
					size: this.videoSize,
					byUserId: viewerId,
					followUserId: 0,
					videoOnly: 1
				}).then(res => {
					if (res.status !== 0 || !res.data || !Array.isArray(res.data.list)) {
						this.videoHasMore = false
						return
					}
					const records = res.data.list
					const videos = records.filter(item => item.videoUrl).map(item => this.mapTalkVideo(item, viewerId))
					this.videoList = this.videoList.concat(videos)
					this.videoHasMore = records.length === this.videoSize
					this.videoPage += 1
					this.$nextTick(() => {
						if (this.activeTopTab === '视频' && this.videoList.length) this.playVideo(this.currentIndex)
					})
				}).catch(() => {
					uni.showToast({ title: '视频动态加载失败', icon: 'none' })
				}).finally(() => { this.videoLoading = false })
			},
			mapTalkVideo(item, viewerId) {
				return {
					id: item.talkId,
					userId: item.userId,
					src: item.videoUrl,
					author: item.nickName || '神秘用户',
					avatar: item.imageUrl || '/static/logo.png',
					createTime: item.createTime || '',
					description: item.content || '',
					followed: Number(item.followedCount || 0) > 0,
					isSelf: viewerId && String(viewerId) === String(item.userId),
					liked: Number(item.goodTalkByUserCount || 0) > 0,
					likeCount: Number(item.goodTalkCount || 0),
					commentCount: Number(item.commentCount || 0),
					orderTakingId: null,
					objectFit: 'cover',
					orientation: '',
					likeAnimating: false
				}
			},
			selectGift(gift) {
				this.selectedGiftId = gift.giftId
			},
			sendGift() {
				if (!this.selectedGiftId || this.giftSending) return
				if (!this.$queue.getData('token')) {
					uni.navigateTo({ url: '/pages/public/login' })
					return
				}
				const userId = this.currentVideoRecipientId
				if (!userId) {
					uni.showToast({ title: '当前视频尚未关联发布者', icon: 'none' })
					return
				}
				if (String(userId) === String(this.$queue.getData('userId'))) {
					uni.showToast({ title: '不能给自己送礼', icon: 'none' })
					return
				}
				this.giftSending = true
				this.$Request.postJson('/app/gift/sendGift', { giftId: this.selectedGiftId, userId }).then(res => {
					if (res.code === 0) {
						uni.showToast({ title: '赠送成功', icon: 'none' })
						this.loadGiftBalance()
					} else {
						uni.showToast({ title: res.msg || '赠送失败', icon: 'none' })
					}
				}).catch(() => {
					uni.showToast({ title: '赠送失败，请稍后重试', icon: 'none' })
				}).finally(() => { this.giftSending = false })
			},

			getVideoContext() {
				return uni.createVideoContext('video-current', this)
			},

			playVideo(index) {
				if (this.activeTopTab !== '视频' || !this.videoList[index]) return
				this.getVideoContext().play()
				this.playing = true
			},

			pauseVideo(index) {
				if (index < 0 || !this.videoList[index]) return

				this.getVideoContext().pause()
			},

			pauseAllVideos() {
				if (this.currentVideo) this.getVideoContext().pause()
				this.playing = false
			},

			onVideoTouchStart(event, index) {
				if (index !== this.currentIndex) return
				const touch = event && event.touches && event.touches[0]
				if (!touch) return
				this.videoTouchStartX = Number(touch.clientX || touch.pageX || touch.screenX || 0)
				this.videoTouchStartY = Number(touch.clientY || touch.pageY || touch.screenY || 0)
				this.videoTouchMoved = false
				this.videoTouchWasSwipe = false
			},

			onVideoTouchMove() {
				this.videoTouchMoved = true
			},

			onVideoTouchEnd(event, index) {
				if (index !== this.currentIndex) return
				const touch = event && ((event.changedTouches && event.changedTouches[0]) || (event.touches && event.touches[0]))
				if (!touch) return
				const endX = Number(touch.clientX || touch.pageX || touch.screenX || 0)
				const endY = Number(touch.clientY || touch.pageY || touch.screenY || 0)
				const deltaX = endX - this.videoTouchStartX
				const deltaY = endY - this.videoTouchStartY
				const isVerticalSwipe = Math.abs(deltaY) >= 38 && Math.abs(deltaY) > Math.abs(deltaX) * 1.1
				this.videoTouchWasSwipe = isVerticalSwipe
				if (!isVerticalSwipe) return

				const nextIndex = deltaY < 0 ? index + 1 : index - 1
				if (nextIndex < 0) return
				if (nextIndex >= this.videoList.length) {
					if (this.videoHasMore) this.loadVideos(false)
					return
				}
				this.switchVideo(nextIndex)
			},

			onVideoTouchCancel() {
				this.videoTouchMoved = false
				this.videoTouchWasSwipe = false
			},

			onVideoGestureTap(index) {
				if (this.videoTouchWasSwipe) {
					this.videoTouchWasSwipe = false
					return
				}
				this.togglePlay(index)
			},

			switchVideo(nextIndex) {
				const previousIndex = this.currentIndex
				if (nextIndex === previousIndex || !this.videoList[nextIndex]) return
				this.pauseVideo(previousIndex)
				this.currentIndex = nextIndex
				if (nextIndex >= this.videoList.length - 2) this.loadVideos(false)
				this.$nextTick(() => this.playVideo(nextIndex))
			},

			togglePlay(index) {
				if (index !== this.currentIndex) return

				const context = this.getVideoContext()

				if (this.playing) {
					context.pause()
					this.playing = false
				} else {
					context.play()
					this.playing = true
				}
			},

			toggleMuted() {
				this.muted = !this.muted

				this.$nextTick(() => {
					this.playVideo(this.currentIndex)
				})
			},

			onVideoMetadata(event, index) {
				const detail = event && event.detail ? event.detail : {}
				const width = Number(detail.width || detail.videoWidth || 0)
				const height = Number(detail.height || detail.videoHeight || 0)
				if (!width || !height || !this.videoList[index]) return
				const landscape = width > height
				this.$set(this.videoList[index], 'orientation', landscape ? 'landscape' : 'portrait')
				// iOS 对包含旋转信息的视频可能返回未旋转的宽高。短视频页统一 cover，
				// 避免竖屏视频被误判为横屏后出现左右黑边。
				this.$set(this.videoList[index], 'objectFit', 'cover')
			},

			onVideoError(event) {
				console.error('视频播放失败：', event)
			}
		}
	}
</script>

<style scoped>
	.video-page {
		position: fixed;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		overflow: hidden;
		background: #000;
	}
	.video-page-post {
		background: #151633;
	}
	.video-empty { position: absolute; top: calc(88rpx + env(safe-area-inset-top)); right: 0; bottom: 0; left: 0; display: flex; align-items: center; justify-content: center; color: rgba(255,255,255,.62); font-size: 28rpx; background: #000; }

	.video-stage {
		position: absolute;
		top: calc(88rpx + env(safe-area-inset-top));
		right: 0;
		left: 0;
		width: 100%;
		height: calc(100vh - 88rpx - env(safe-area-inset-top));
		overflow: hidden;
		background: #000;
	}

	.video-player {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: #000;
	}

	.video-gesture-layer {
		position: absolute;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		z-index: 10;
		background: rgba(0, 0, 0, 0.001);
	}

	.top-category-bar {
		position: absolute;
		left: 0;
		top: 0;
		z-index: 10001;
		width: 100%;
		height: calc(88rpx + env(safe-area-inset-top));
		padding-top: env(safe-area-inset-top);
		box-sizing: border-box;
		display: flex;
		align-items: center;
		justify-content: center;
		background: linear-gradient(180deg, #291b3d 0%, #1b1938 100%);
	}
	.top-category-post {
		left: 0;
		top: 0;
		width: 100%;
		height: calc(88rpx + env(safe-area-inset-top));
		padding-top: env(safe-area-inset-top);
		background: transparent;
	}
	.top-post-publish {
		position: absolute;
		top: calc(9rpx + env(safe-area-inset-top));
		right: 24rpx;
		width: 70rpx;
		height: 70rpx;
	}
	.post-feed-host { position: absolute; inset: 0; z-index: 40; }
	.top-category-item { position: relative; height: 68rpx; padding: 0 28rpx; display: flex; align-items: center; justify-content: center; }
	.top-category-text { color: rgba(255,255,255,.72); font-size: 28rpx; text-shadow: 0 2px 6px rgba(0,0,0,.65); }
	.top-category-active { color: #fff; font-weight: bold; }
	.top-category-line { position: absolute; left: 50%; bottom: 0; width: 38rpx; height: 6rpx; margin-left: -19rpx; border-radius: 3rpx; background: #b45cff; }

	.room-card { position: absolute; left: 24rpx; top: 20rpx; z-index: 20; width: 350rpx; height: 96rpx; border: 1px solid rgba(184,102,255,.45); border-radius: 22rpx; background: rgba(20,17,50,.68); }
	.room-avatar { position: absolute; left: 38rpx; top: 36rpx; z-index: 30; width: 60rpx; height: 60rpx; border: 2rpx solid #b866ff; border-radius: 50%; background: #fff; }
	.room-name { position: absolute; left: 112rpx; top: 27rpx; z-index: 30; width: 240rpx; height: 28rpx; color: #fff; font-size: 22rpx; line-height: 28rpx; }
	.room-level { position: absolute; left: 112rpx; top: 53rpx; z-index: 30; width: 220rpx; height: 24rpx; color: #ccc; font-size: 18rpx; line-height: 24rpx; }
	.room-status { position: absolute; left: 112rpx; top: 76rpx; z-index: 30; width: 230rpx; height: 24rpx; color: #eee; font-size: 17rpx; line-height: 24rpx; }

	.side-avatar { position: absolute; right: 21rpx; bottom: 330rpx; z-index: 30; width: 72rpx; height: 72rpx; border: 3rpx solid #fff; border-radius: 50%; background: #fff; }

	.follow-badge {
		position: absolute;
		right: 41rpx;
		bottom: 318rpx;
		z-index: 40;
		width: 32rpx;
		height: 32rpx;
		border-radius: 50%;
		background: #ff416c;
		color: #fff;
		font-size: 28rpx;
		line-height: 32rpx;
		font-weight: bold;
		text-align: center;
	}

	.like-icon {
		width: 62rpx;
		height: 62rpx;
	}
	.side-like { position: absolute; right: 26rpx; bottom: 235rpx; z-index: 30; }
	.side-like-count { right: 18rpx; bottom: 210rpx; }

	.comment-icon {
		width: 58rpx;
		height: 58rpx;
	}
	.side-comment { position: absolute; right: 28rpx; bottom: 135rpx; z-index: 30; }
	.side-comment-count { right: 18rpx; bottom: 110rpx; }

	.action-count {
		position: absolute;
		z-index: 30;
		width: 78rpx;
		height: 24rpx;
		color: #fff;
		font-size: 21rpx;
		line-height: 24rpx;
		font-weight: 500;
		text-align: center;
	}

	.action-label {
		position: absolute;
		z-index: 30;
		width: 78rpx;
		height: 26rpx;
		color: #fff;
		font-size: 21rpx;
		line-height: 26rpx;
		text-align: center;
	}

	.gift-icon {
		width: 68rpx;
		height: 68rpx;
	}
	.side-gift { position: absolute; right: 23rpx; bottom: 35rpx; z-index: 30; }
	.side-gift-label { right: 18rpx; bottom: 7rpx; }

	.order-button { position: absolute; left: 30rpx; bottom: 112rpx; z-index: 30; width: 104rpx; height: 42rpx; border-radius: 8rpx; background: #c23cba; color: #fff; font-size: 21rpx; line-height: 42rpx; text-align: center; }

	.video-title {
		position: absolute;
		left: 30rpx;
		bottom: 67rpx;
		z-index: 30;
		width: 120rpx;
		height: 38rpx;
		color: #fff;
		font-size: 30rpx;
		line-height: 38rpx;
		font-weight: bold;
	}
	.author-badge, .online-badge { position: absolute; bottom: 72rpx; z-index: 30; width: 54rpx; height: 27rpx; border-radius: 14rpx; color: #fff; font-size: 17rpx; line-height: 27rpx; text-align: center; }
	.author-badge { left: 152rpx; background: #a855f7; }
	.online-badge { left: 214rpx; background: #ec4899; }

	.video-description {
		position: absolute;
		left: 30rpx;
		right: 130rpx;
		bottom: 24rpx;
		z-index: 30;
		height: 34rpx;
		color: #fff;
		font-size: 23rpx;
		line-height: 34rpx;
	}

	.panel-mask {
		position: fixed;
		top: calc(88rpx + env(safe-area-inset-top));
		right: 0;
		bottom: calc(100rpx + env(safe-area-inset-bottom));
		left: 0;
		z-index: 20000;
		background: rgba(0,0,0,.45);
		display: flex;
		align-items: flex-end;
	}
	.bottom-panel { width: 100%; min-height: 420rpx; padding: 28rpx 30rpx calc(36rpx + env(safe-area-inset-bottom)); border-radius: 30rpx 30rpx 0 0; background: #17152b; box-sizing: border-box; }
	.panel-header { display: flex; align-items: center; justify-content: space-between; }
	.panel-title { color: #fff; font-size: 32rpx; font-weight: bold; }
	.panel-close { color: #aaa; font-size: 50rpx; }
	.comment-panel { height: 72vh; display: flex; flex-direction: column; }
	.panel-content { min-height: 0; margin-top: 24rpx; flex: 1; display: flex; flex-direction: column; }
	.comment-list { min-height: 0; flex: 1; height: 0; }
	.comment-state { padding: 120rpx 0; color: #aaa; font-size: 26rpx; text-align: center; }
	.comment-item { display: flex; padding: 18rpx 0; border-bottom: 1rpx solid rgba(255,255,255,.08); }
	.comment-avatar { width: 68rpx; height: 68rpx; flex-shrink: 0; border-radius: 50%; background: #292642; }
	.comment-body { min-width: 0; margin-left: 18rpx; flex: 1; }
	.comment-meta { display: flex; align-items: center; justify-content: space-between; }
	.comment-name { color: #cba8ff; font-size: 24rpx; }
	.comment-time { margin-left: 16rpx; color: #77738a; font-size: 19rpx; }
	.comment-text { margin-top: 8rpx; color: #fff; font-size: 26rpx; line-height: 38rpx; word-break: break-all; }
	.comment-more { padding: 18rpx 0; color: #77738a; font-size: 22rpx; text-align: center; }
	.comment-input-row { padding-top: 22rpx; display: flex; align-items: center; }
	.comment-input { flex: 1; height: 72rpx; padding: 0 24rpx; border-radius: 36rpx; color: #fff; background: #292642; }
	.send-button { margin-left: 20rpx; color: #b866ff; font-size: 28rpx; }
	.send-button-disabled { opacity: .5; }
	.gift-panel-content { margin-top: 32rpx; }
	.gift-balance-row { display: flex; justify-content: space-between; align-items: center; }
	.gift-balance { display: flex; align-items: center; color: #fff; font-size: 28rpx; }
	.gift-balance image { width: 38rpx; height: 38rpx; margin-right: 8rpx; }
	.gift-recharge { padding: 12rpx 38rpx; border-radius: 8rpx; background: #a96cf9; color: #fff; font-size: 26rpx; }
	.gift-scroll { height: 430rpx; margin-top: 20rpx; }
	.gift-list { display: flex; flex-wrap: wrap; }
	.gift-item { width: 25%; min-height: 196rpx; box-sizing: border-box; padding: 12rpx 4rpx; border: 2rpx solid transparent; border-radius: 12rpx; display: flex; flex-direction: column; align-items: center; }
	.gift-item-selected { border-color: #ad70ff; background: rgba(173,112,255,.16); }
	.gift-image { width: 90rpx; height: 90rpx; }
	.gift-name { margin-top: 5rpx; color: #fff; font-size: 22rpx; text-align: center; }
	.gift-price { display: flex; align-items: center; margin-top: 4rpx; color: #fff; font-size: 20rpx; }
	.gift-price image { width: 20rpx; height: 20rpx; margin-left: 4rpx; }
	.gift-state { padding: 100rpx 0; text-align: center; color: #aaa; font-size: 26rpx; }
	.gift-notice { margin: 8rpx 0 14rpx; color: #b9adc9; font-size: 22rpx; text-align: center; }
	.gift-send { width: 100%; padding: 16rpx 0; border-radius: 10rpx; background: #a96cf9; color: #fff; font-size: 26rpx; text-align: center; }
	.gift-send-disabled { opacity: .48; }
</style>

