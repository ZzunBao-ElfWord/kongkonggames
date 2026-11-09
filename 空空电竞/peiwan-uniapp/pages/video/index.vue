<template>
	<view class="video-page" :class="{ 'video-page-post': activeTopTab === '帖子' }">
		<view class="top-category-bar" :class="{ 'top-category-post': activeTopTab === '帖子' }">
			<view v-for="tab in topTabs" :key="tab" class="top-category-item" @click.stop="selectTopTab(tab)">
				<text class="top-category-text" :class="{ 'top-category-active': activeTopTab === tab }">{{ tab }}</text>
				<view v-if="activeTopTab === tab" class="top-category-line"></view>
			</view>
			<image v-if="activeTopTab === '帖子'" class="top-post-publish" src="../../static/images/my/dtfb.png" mode="aspectFit" @click.stop="publishPost"></image>
		</view>
		<!-- #ifndef APP-PLUS -->
		<swiper v-if="activeTopTab === '视频' && videoList.length && !panelVisible"
			class="video-swiper"
			vertical
			:current="currentIndex"
			@change="onSwiperChange"
		>
			<swiper-item
				v-for="(item, index) in videoList"
				:key="item.id"
			>
				<view class="video-item">
					<video
						:id="'video-' + index"
						class="video-player"
						:src="item.src"
						:controls="false"
						:loop="true"
						:muted="muted"
						:autoplay="index === currentIndex"
						:show-center-play-btn="false"
						:show-fullscreen-btn="false"
						:enable-progress-gesture="false"
						:vslide-gesture="false"
						:object-fit="item.objectFit"
						@loadedmetadata="onVideoMetadata($event, index)"
						@error="onVideoError"
					/>

					<block v-if="index === currentIndex">
					<cover-view
						class="video-gesture-layer"
						@touchstart.stop="onVideoTouchStart($event, index)"
						@touchmove.stop.prevent="onVideoTouchMove($event, index)"
						@touchend.stop="onVideoTouchEnd($event, index)"
					></cover-view>
					<!-- Android 的 video 是原生层，普通 view/image 会被视频盖住。
					     悬浮交互必须使用 cover-view/cover-image，并尽量保持扁平结构。 -->
					<cover-view class="room-card" @click.stop="openAuthor(item)"></cover-view>
					<cover-image class="room-avatar" :src="item.avatar || '/static/logo.png'" mode="aspectFill" @click.stop="openAuthor(item)"></cover-image>
					<cover-view class="room-name" @click.stop="openAuthor(item)">{{ item.author }}</cover-view>
					<cover-view class="room-level" @click.stop="openAuthor(item)">动态发布者</cover-view>
					<cover-view class="room-status" @click.stop="openAuthor(item)">{{ item.createTime }}</cover-view>

					<cover-image class="side-avatar" :src="item.avatar || '/static/logo.png'" mode="aspectFill" @click.stop="openAuthor(item)"></cover-image>
					<cover-view v-if="!item.followed && !item.isSelf" class="follow-badge" @click.stop="toggleFollow(index)">+</cover-view>

					<cover-image class="like-icon side-like" :class="{ 'like-icon-animate': item.likeAnimating }" :src="item.liked ? '../../static/images/video-heart-active.png' : '../../static/images/video-heart.png'" mode="aspectFit" @click.stop="toggleLike(index)"></cover-image>
					<cover-view class="action-count side-like-count" @click.stop="toggleLike(index)">{{ item.likeCount }}</cover-view>

					<cover-image class="comment-icon side-comment" src="../../static/images/video-message.png" mode="aspectFit" @click.stop="openComments"></cover-image>
					<cover-view class="action-count side-comment-count" @click.stop="openComments">{{ item.commentCount }}</cover-view>

					<cover-image class="gift-icon side-gift" src="../../static/images/video-gift.png" mode="aspectFit" @click.stop="openGifts"></cover-image>
					<cover-view class="action-label side-gift-label" @click.stop="openGifts">礼物</cover-view>

					<cover-view class="order-button" @click.stop="goToOrder(item)">{{ orderingUserId === item.userId ? '查询中…' : '去下单' }}</cover-view>
					<cover-view class="video-title">{{ item.author }}</cover-view>
					<cover-view class="author-badge">达人</cover-view>
					<cover-view class="online-badge">在线</cover-view>
					<cover-view class="video-description">{{ item.description }}</cover-view>
					</block>

				</view>
			</swiper-item>
		</swiper>
		<!-- #endif -->
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
	import { getPostVideoPreview } from '../../utils/post-video-preview.js'

	export default {
		components: { PostFeed },
		data() {
			return {
				currentIndex: 0,
				playing: true,

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
				pageActive: false,
				videoTouchStartX: 0,
				videoTouchStartY: 0,
				videoTouchLastX: 0,
				videoTouchLastY: 0,

				videoList: []
			}
		},

		computed: {
			currentVideo() {
				return this.videoList[this.currentIndex] || null
			},
			currentVideoRecipientId() {
				const video = this.currentVideo
				return video && video.userId ? video.userId : null
			}
		},

		watch: {
			videoList: { deep: true, handler() { this.syncNativeStage() } },
			currentIndex() { this.syncNativeStage() },
			activeTopTab() { this.syncNativeStage() },
			panelVisible() { this.syncNativeStage() }
		},
		onLoad() {
			// #ifdef APP-PLUS
			this._nativeReadyHandler = () => this.syncNativeStage()
			this._nativeActionHandler = event => this.onNativeAction(event)
			uni.$on('video-stage:ready', this._nativeReadyHandler)
			uni.$on('video-stage:action', this._nativeActionHandler)
			// #endif
		},
		onReady() {
			this.syncNativeStage()
			setTimeout(() => {
				if (this.activeTopTab === '视频') this.playVideo(0)
			}, 300)
		},

		onShow() {
			this.pageActive = true
			this.syncNativeStage()
			this.loadVideos(true)
			setTimeout(() => {
				if (this.activeTopTab === '视频') this.playVideo(this.currentIndex)
				else if (this.$refs.postFeed) this.$refs.postFeed.refreshPosts()
			}, 300)
		},

		onHide() {
			this.pageActive = false
			this.syncNativeStage()
			this.pauseAllVideos()
		},

		onUnload() {
			this.pageActive = false
			this.syncNativeStage()
			this.pauseAllVideos()
			// #ifdef APP-PLUS
			uni.$off('video-stage:ready', this._nativeReadyHandler)
			uni.$off('video-stage:action', this._nativeActionHandler)
			// #endif
		},

		methods: {
			prepareVideoDimensions(index) {
				// #ifdef APP-PLUS
				const item = this.videoList[index]
				if (!item || item.videoWidth || (this._sizingVideos && this._sizingVideos.has(item.src))) return
				if (!this._sizingVideos) this._sizingVideos = new Set()
				this._sizingVideos.add(item.src)
				getPostVideoPreview(item.src).then(size => {
					if (!(size.width > 0 && size.height > 0)) return
					this.$set(item, 'videoWidth', size.width)
					this.$set(item, 'videoHeight', size.height)
				}).catch(() => {}).finally(() => this._sizingVideos.delete(item.src))
				// #endif
			},
			syncNativeStage() {
				// #ifdef APP-PLUS
				const token = this._nativeSyncToken = (this._nativeSyncToken || 0) + 1
				const stage = uni.getSubNVueById('video-native-stage')
				if (!stage) return
				const visible = this.pageActive && this.activeTopTab === '视频' && !this.panelVisible && this.videoList.length > 0
				if (!visible) {
					this._nativeStageShown = false
					this._nativeLatestState = null
					uni.$emit('video-stage:state', { visible: false })
					stage.hide('none', 0)
					return
				}
				this.$nextTick(() => {
					if (token !== this._nativeSyncToken) return
					uni.createSelectorQuery().in(this).select('.top-category-bar').boundingClientRect(rect => {
						if (token !== this._nativeSyncToken || !this.pageActive || this.panelVisible || this.activeTopTab !== '视频') return
						const info = uni.getSystemInfoSync()
						// 原生子窗体首次加载时 DOM 查询可能尚未返回；按顶部栏的实际 CSS 提供回退。
						const top = rect ? rect.bottom : uni.upx2px(88) + ((info.safeAreaInsets && info.safeAreaInsets.top) || 0)
						const state = { visible: true, videos: this.videoList, index: this.currentIndex, playing: this.playing, muted: this.muted,
							width: info.windowWidth, height: Math.max(1, info.windowHeight - top), headerHeight: top, statusBarHeight: info.statusBarHeight || 0 }
						this._nativeLatestState = state
						if (!this._nativeStageShown) {
							this._nativeStageShown = true
							stage.setStyle({ top: '0px', bottom: '0px' })
							stage.show('none', 0, () => {
								if (this._nativeStageShown && this._nativeLatestState) uni.$emit('video-stage:state', this._nativeLatestState)
							})
						}
						uni.$emit('video-stage:state', state)
					}).exec()
				})
				// #endif
			},
			onNativeAction(event) {
				if (!this.pageActive || !event || this.panelVisible) return
				if (event.type === 'posts') {
					this.selectTopTab('帖子')
					return
				}
				const index = Number(event.index)
				if (!Number.isInteger(index) || !this.videoList[index]) return
				if (event.type === 'change') {
					this.onSwiperChange({ detail: { current: index } })
					return
				}
				if (index !== this.currentIndex) return
				const item = this.videoList[index]
				const actions = {
					toggle: () => this.togglePlay(index), author: () => this.openAuthor(item),
					follow: () => this.toggleFollow(index), like: () => this.toggleLike(index),
					comments: () => this.openComments(), gifts: () => this.openGifts(), order: () => this.goToOrder(item)
				}
				if (actions[event.type]) actions[event.type]()
			},
			noop() {},
			publishPost() {
				if (this.$refs.postFeed) this.$refs.postFeed.bindfb()
			},
			selectTopTab(tab) {
				if (tab === this.activeTopTab) return
				this.panelVisible = false
				if (tab === '帖子') {
					this.pauseAllVideos()
					this.activeTopTab = '帖子'
				} else {
					this.activeTopTab = '视频'
					this.loadVideos(true)
					this.$nextTick(() => this.playVideo(this.currentIndex))
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
				this.pauseVideo(this.currentIndex)
				this.panelType = 'comments'
				this.panelVisible = true
				this.syncNativeStage()
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
				this.pauseVideo(this.currentIndex)
				this.panelType = 'gifts'
				this.panelVisible = true
				this.syncNativeStage()
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
				this.$nextTick(() => this.playVideo(this.currentIndex))
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
					objectFit: 'contain',
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

			getVideoContext(index) {
				return uni.createVideoContext('video-' + index, this)
			},

			playVideo(index) {
				if (!this.pageActive || this.panelVisible || this.activeTopTab !== '视频' || !this.videoList[index]) return
				// #ifdef APP-PLUS
				this.prepareVideoDimensions(index)
				this.prepareVideoDimensions(index + 1)
				this.playing = true
				this.syncNativeStage()
				return
				// #endif
				this.getVideoContext(index).play()
				this.playing = true
			},

			pauseVideo(index) {
				if (index < 0 || !this.videoList[index]) return
				// #ifdef APP-PLUS
				uni.$emit('video-stage:pause', { index })
				return
				// #endif

				this.getVideoContext(index).pause()
			},

			pauseAllVideos() {
				this.videoList.forEach((item, index) => {
					this.pauseVideo(index)
				})

				this.playing = false
			},

			onSwiperChange(event) {
				const nextIndex = Number(event.detail.current)
				const previousIndex = this.currentIndex

				if (nextIndex === previousIndex) return

				this.pauseVideo(previousIndex)
				this.currentIndex = nextIndex
				if (nextIndex >= this.videoList.length - 2) this.loadVideos(false)

				this.$nextTick(() => {
					this.playVideo(nextIndex)
				})
			},

			onVideoTouchStart(event, index) {
				if (index !== this.currentIndex) return
				const touch = event && event.touches && event.touches[0]
				if (!touch) return
				this.videoTouchStartX = Number(touch.clientX || touch.pageX || 0)
				this.videoTouchStartY = Number(touch.clientY || touch.pageY || 0)
				this.videoTouchLastX = this.videoTouchStartX
				this.videoTouchLastY = this.videoTouchStartY
			},

			onVideoTouchMove(event, index) {
				if (index !== this.currentIndex) return
				const touch = event && event.touches && event.touches[0]
				if (!touch) return
				this.videoTouchLastX = Number(touch.clientX || touch.pageX || this.videoTouchLastX)
				this.videoTouchLastY = Number(touch.clientY || touch.pageY || this.videoTouchLastY)
			},

			onVideoTouchEnd(event, index) {
				if (index !== this.currentIndex) return
				const touch = event && event.changedTouches && event.changedTouches[0]
				const endX = touch ? Number(touch.clientX || touch.pageX || this.videoTouchLastX) : this.videoTouchLastX
				const endY = touch ? Number(touch.clientY || touch.pageY || this.videoTouchLastY) : this.videoTouchLastY
				const deltaX = endX - this.videoTouchStartX
				const deltaY = endY - this.videoTouchStartY

				if (Math.abs(deltaY) >= 80 && Math.abs(deltaY) > Math.abs(deltaX)) {
					const nextIndex = deltaY < 0 ? index + 1 : index - 1
					if (nextIndex >= 0 && nextIndex < this.videoList.length) {
						this.pauseVideo(index)
						this.currentIndex = nextIndex
						if (nextIndex >= this.videoList.length - 2) this.loadVideos(false)
						this.$nextTick(() => this.playVideo(nextIndex))
					}
					return
				}

				if (Math.abs(deltaX) < 18 && Math.abs(deltaY) < 18) this.togglePlay(index)
			},

			togglePlay(index) {
				if (index !== this.currentIndex) return
				// #ifdef APP-PLUS
				this.playing = !this.playing
				this.syncNativeStage()
				return
				// #endif

				const context = this.getVideoContext(index)

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
				// 横屏完整显示并留黑，竖屏保持原有铺满效果。
				this.$set(this.videoList[index], 'objectFit', landscape ? 'contain' : 'cover')
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
	.video-empty { position: absolute; top: calc(88rpx + env(safe-area-inset-top)); right: 0; bottom: calc(100rpx + env(safe-area-inset-bottom)); left: 0; display: flex; align-items: center; justify-content: center; color: rgba(255,255,255,.62); font-size: 28rpx; background: #000; }

	.video-swiper {
		position: absolute;
		top: calc(88rpx + env(safe-area-inset-top));
		right: 0;
		bottom: calc(100rpx + env(safe-area-inset-bottom));
		left: 0;
		width: 100%;
		height: auto;
		background: #000;
	}

	/* #ifdef APP-PLUS */
	/* App 页面可用高度已经排除了原生 tabBar，不能再次预留 100rpx。 */
	.video-swiper,
	.video-empty {
		bottom: 0;
	}
	/* #endif */

	.video-item {
		position: relative;
		width: 100%;
		height: 100%;
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
		/* Android App 的原生 video 会吞掉外层 swiper 的触摸事件。
		 * 禁止视频层接收指针事件，让上下滑动交给 swiper 处理。 */
		pointer-events: none;
	}

	.video-gesture-layer {
		position: absolute;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		z-index: 15;
		/* Android 原生层命中测试需要一个近乎透明的背景色。 */
		background: rgba(0, 0, 0, 0.001);
		pointer-events: auto;
		touch-action: none;
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
	.post-feed-host { position: absolute; top: calc(88rpx + env(safe-area-inset-top)); right: 0; bottom: calc(100rpx + env(safe-area-inset-bottom)); left: 0; z-index: 40; }
	/* #ifdef APP-PLUS */
	.post-feed-host { bottom: 0; }
	/* #endif */
	.top-category-item { position: relative; height: 68rpx; padding: 0 28rpx; display: flex; align-items: center; justify-content: center; }
	.top-category-text { color: rgba(255,255,255,.72); font-size: 28rpx; text-shadow: 0 2px 6px rgba(0,0,0,.65); }
	.top-category-active { color: #fff; font-weight: bold; }
	.top-category-line { position: absolute; left: 50%; bottom: 0; width: 38rpx; height: 6rpx; margin-left: -19rpx; border-radius: 3rpx; background: #b45cff; }

	.room-card {
		position: absolute;
		left: 28rpx;
		top: 24rpx;
		z-index: 25;
		max-width: 390rpx;
		padding: 16rpx 20rpx;
		border: 1px solid rgba(184,102,255,.35);
		border-radius: 26rpx;
		background: rgba(20,17,50,.58);
		display: flex;
		align-items: center;
		box-sizing: border-box;
		backdrop-filter: blur(8px);
	}
	.room-avatar { width: 66rpx; height: 66rpx; flex-shrink: 0; border: 2rpx solid #b866ff; border-radius: 50%; background: #fff; }
	.room-content { margin-left: 14rpx; display: flex; flex-direction: column; }
	.room-name { color: #fff; font-size: 24rpx; }
	.room-level { margin-top: 3rpx; color: #ccc; font-size: 19rpx; }
	.room-status { margin-top: 8rpx; color: #eee; font-size: 19rpx; }

	.video-info {
		position: absolute;
		left: 30rpx;
		right: 160rpx;
		bottom: 18rpx;
		z-index: 10;
		display: flex;
		flex-direction: column;
		color: #fff;
	}

	.video-tags-row { align-self: flex-start; display: flex; align-items: center; }
	.order-button { padding: 8rpx 18rpx; border-radius: 8rpx; background: linear-gradient(90deg, #a838ef 0%, #ed4a9b 100%); color: #fff; font-size: 22rpx; box-shadow: 0 2rpx 10rpx rgba(201,63,193,.38); }
	.author-row { margin-top: 14rpx; display: flex; align-items: center; }
	.author-badge, .online-badge { margin-left: 10rpx; padding: 3rpx 10rpx; border-radius: 14rpx; color: #fff; font-size: 18rpx; }
	.author-badge { background: #a855f7; }
	.online-badge { background: #ec4899; }

	.action-bar {
		position: absolute;
		right: 24rpx;
		bottom: 78rpx;
		z-index: 20;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.avatar-action {
		position: relative;
		width: 96rpx;
		height: 112rpx;
		margin-bottom: 12rpx;
	}

	.avatar-image {
		width: 88rpx;
		height: 88rpx;
		margin-left: 4rpx;
		border: 4rpx solid #fff;
		border-radius: 50%;
		background: #fff;
	}

	.follow-badge {
		position: absolute;
		left: 29rpx;
		bottom: 0;
		width: 38rpx;
		height: 38rpx;
		border-radius: 50%;
		background: #ff416c;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.follow-symbol {
		color: #fff;
		font-size: 34rpx;
		line-height: 36rpx;
		font-weight: bold;
	}

	.action-item {
		width: 96rpx;
		margin-top: 12rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.like-icon {
		width: 76rpx;
		height: 76rpx;
		filter: drop-shadow(0 2px 6px rgba(0, 0, 0, 0.55));
	}

	.like-effect-box { position: relative; width: 88rpx; height: 80rpx; display: flex; align-items: center; justify-content: center; }
	.like-icon-animate { animation: like-pop .65s ease-out; }
	.like-particles { position: absolute; left: 50%; top: 50%; width: 1rpx; height: 1rpx; pointer-events: none; }
	.like-particle { position: absolute; color: #ff5a86; font-size: 14rpx; opacity: 0; animation: particle-burst .65s ease-out forwards; }
	.like-particle-1 { --particle-x: -42rpx; --particle-y: -34rpx; }
	.like-particle-2 { --particle-x: 0rpx; --particle-y: -52rpx; }
	.like-particle-3 { --particle-x: 42rpx; --particle-y: -34rpx; }
	.like-particle-4 { --particle-x: -44rpx; --particle-y: 24rpx; }
	.like-particle-5 { --particle-x: 0rpx; --particle-y: 48rpx; }
	.like-particle-6 { --particle-x: 44rpx; --particle-y: 24rpx; }
	@keyframes like-pop {
		0% { transform: scale(1); }
		30% { transform: scale(.72) rotate(-10deg); }
		60% { transform: scale(1.45) rotate(8deg); }
		82% { transform: scale(.92); }
		100% { transform: scale(1); }
	}
	@keyframes particle-burst {
		0% { opacity: 1; transform: translate(-50%, -50%) scale(.4); }
		70% { opacity: 1; }
		100% { opacity: 0; transform: translate(calc(-50% + var(--particle-x)), calc(-50% + var(--particle-y))) scale(1); }
	}

	.comment-icon {
		width: 68rpx;
		height: 68rpx;
		filter: drop-shadow(0 2px 6px rgba(0, 0, 0, 0.55));
	}

	.action-count {
		margin-top: 6rpx;
		color: #fff;
		font-size: 24rpx;
		font-weight: 500;
		text-align: center;
		text-shadow: 0 2px 6px rgba(0, 0, 0, 0.8);
	}

	.action-label {
		margin-top: 6rpx;
		color: #fff;
		font-size: 24rpx;
		text-align: center;
		text-shadow: 0 2px 6px rgba(0, 0, 0, 0.8);
	}

	.gift-icon {
		width: 92rpx;
		height: 92rpx;
	}

	.video-title {
		color: #fff;
		font-size: 32rpx;
		font-weight: bold;
		text-shadow: 0 2px 6px rgba(0, 0, 0, 0.8);
	}

	.video-description {
		margin-top: 12rpx;
		color: #fff;
		font-size: 24rpx;
		text-shadow: 0 2px 6px rgba(0, 0, 0, 0.8);
	}

	/* 原生 video 上方的 cover 组件使用扁平绝对定位，
	   确保 Android 真机和模拟器都能正确显示并响应点击。 */
	.room-card { position: absolute; left: 24rpx; top: 20rpx; z-index: 20; width: 350rpx; height: 96rpx; max-width: none; padding: 0; border: 1px solid rgba(184,102,255,.45); border-radius: 22rpx; background: rgba(20,17,50,.68); display: block; }
	.room-avatar { position: absolute; left: 38rpx; top: 36rpx; z-index: 30; width: 60rpx; height: 60rpx; border: 2rpx solid #b866ff; border-radius: 50%; background: #fff; }
	.room-name { position: absolute; left: 112rpx; top: 27rpx; z-index: 30; width: 240rpx; height: 28rpx; margin: 0; color: #fff; font-size: 22rpx; line-height: 28rpx; }
	.room-level { position: absolute; left: 112rpx; top: 53rpx; z-index: 30; width: 220rpx; height: 24rpx; margin: 0; color: #ccc; font-size: 18rpx; line-height: 24rpx; }
	.room-status { position: absolute; left: 112rpx; top: 76rpx; z-index: 30; width: 230rpx; height: 24rpx; margin: 0; color: #eee; font-size: 17rpx; line-height: 24rpx; }

	.side-avatar { position: absolute; right: 21rpx; bottom: 330rpx; z-index: 30; width: 72rpx; height: 72rpx; border: 3rpx solid #fff; border-radius: 50%; background: #fff; }
	.follow-badge { position: absolute; left: auto; right: 41rpx; bottom: 318rpx; z-index: 40; width: 32rpx; height: 32rpx; border-radius: 50%; background: #ff416c; color: #fff; font-size: 28rpx; line-height: 32rpx; font-weight: bold; text-align: center; display: block; }

	.like-icon { width: 62rpx; height: 62rpx; filter: none; }
	.side-like { position: absolute; right: 26rpx; bottom: 235rpx; z-index: 30; }
	.side-like-count { right: 18rpx; bottom: 210rpx; }
	.comment-icon { width: 58rpx; height: 58rpx; filter: none; }
	.side-comment { position: absolute; right: 28rpx; bottom: 135rpx; z-index: 30; }
	.side-comment-count { right: 18rpx; bottom: 110rpx; }
	.action-count { position: absolute; z-index: 30; width: 78rpx; height: 24rpx; margin-top: 0; color: #fff; font-size: 21rpx; line-height: 24rpx; font-weight: 500; text-align: center; }
	.action-label { position: absolute; z-index: 30; width: 78rpx; height: 26rpx; margin-top: 0; color: #fff; font-size: 21rpx; line-height: 26rpx; text-align: center; }
	.gift-icon { width: 68rpx; height: 68rpx; }
	.side-gift { position: absolute; right: 23rpx; bottom: 35rpx; z-index: 30; }
	.side-gift-label { right: 18rpx; bottom: 7rpx; }

	.order-button { position: absolute; left: 30rpx; bottom: 112rpx; z-index: 30; width: 104rpx; height: 42rpx; padding: 0; border-radius: 8rpx; background: #c23cba; color: #fff; font-size: 21rpx; line-height: 42rpx; text-align: center; }
	.video-title { position: absolute; left: 30rpx; bottom: 67rpx; z-index: 30; width: 120rpx; height: 38rpx; color: #fff; font-size: 30rpx; line-height: 38rpx; font-weight: bold; }
	.author-badge, .online-badge { position: absolute; bottom: 72rpx; z-index: 30; width: 54rpx; height: 27rpx; margin-left: 0; padding: 0; border-radius: 14rpx; color: #fff; font-size: 17rpx; line-height: 27rpx; text-align: center; }
	.author-badge { left: 152rpx; background: #a855f7; }
	.online-badge { left: 214rpx; background: #ec4899; }
	.video-description { position: absolute; left: 30rpx; right: 130rpx; bottom: 24rpx; z-index: 30; height: 34rpx; margin-top: 0; color: #fff; font-size: 23rpx; line-height: 34rpx; }

	.panel-mask { position: fixed; inset: 0; z-index: 20000; background: rgba(0,0,0,.45); display: flex; align-items: flex-end; }
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
