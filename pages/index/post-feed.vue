<template>
	<scroll-view class="post-feed-scroll" scroll-y :refresher-enabled="true" :refresher-triggered="refreshing" @refresherrefresh="refreshPosts" @scrolltolower="loadMore">
	<view class="content">
		<view class="headtop" v-if="shangxianSelect!='否'">
			<view class="tui-tabs">
				<scroll-view id="tab-bar" scroll-with-animation class="tui-scroll-h" :scroll-x="true"
					:show-scrollbar="false">
					<view style="display: flex;">
						<view :key='index' v-for="(tab, index) in tabBars" :id="tab.id" :data-current="index"
							@tap="tabClick(index)">
							<view class="tui-tab-item-title"
								:class="{ 'tui-tab-item-title-active': tabIndex == index }">{{ tab.name }}</view>
						</view>
					</view>
				</scroll-view>
			</view>

		</view>

		<view class="subh" v-if="bannerList.length > 0 && shangxianSelect!='否'">
			<swiper class="swiper-container" :autoplay="true" :interval="2000" :circular="true" :indicator-dots="false"
				style="height: 240upx;">
				<swiper-item class="swiper-wrapper" style="" v-for="(item, index3) in bannerList" :key="index3"
					@tap='toNavList(item.url)'>
					<image lazy-load='true' fade-show='true' :src="item.imageUrl" v-if="item"
						style="width: 100%;height: 240upx;border-radius: 24rpx;"></image>
				</swiper-item>
			</swiper>
		</view>
		<view class="" style="margin-top: 40rpx;" v-if="shangxianSelect!='是'" v-for="(item,index) in 10" :key="index">
			<swiper class="swiper-container" :autoplay="true" :interval="2000" :circular="true" :indicator-dots="false"
				style="height: 240upx;">
				<swiper-item class="swiper-wrapper" style="" v-for="(item, index3) in bannerList" :key="index3"
					@tap='toNavList(item.url)'>
					<image lazy-load='true' fade-show='true' :src="item.imageUrl" v-if="item"
						style="width: 100%;height: 240upx;border-radius: 24rpx;"></image>
				</swiper-item>
			</swiper>
		</view>

		<view v-if="shangxianSelect!='否'">
			<Head :list="list" @click="clickItem"></Head>
		</view>
		<view class="part3" v-if="shangxianSelect != '否'" @click="bindfb">
			<image src="../../static/images/my/dtfb.png"></image>
		</view>

		<!-- 悬浮上拉 -->
		<!-- <view class="scroll_top" @tap="topScrollTap" v-bind:class="[scrollTop ? 'active' : '', '']"
				style="bottom: 56px;"><text class="iconfont icon-shangla"></text></view> -->

		<!-- 加载更多提示 -->
		<!-- <view class="s-col is-col-24">
			<load-more :status="status" :contentText="contentText"></load-more>
		</view> -->
		<!-- 加载更多提示 -->
		<empty v-if="list.length === 0" content="暂无数据" show="false"></empty>
	</view>
</scroll-view>
</template>

<script>
	import Head from '../../components/Head.vue'
	import empty from '../../components/empty.vue'
	export default {
		components: {
			empty,
			Head
		},
		data() {
			return {
				shangxianSelect: '否',
				renzheng: 0,
				tabIndex: 0,
				tabBars: [{
						name: '全部',
						id: 'hot',
						state: '-1'
					},
					{
						name: '关注',
						id: 'yule',
						state: '1'
					}
				],
				gambitId: 0,
				title: '',
				content: '',
				contentImg: '',
				bannerList: [],
				list: [],
				followUserId: 0,
				isEnable: '否',
				scrollTop: false,
				page: 0,
				refreshing: false,
				isLoading: false,
				hasMore: true,
				size: 10,
				status: 0,
				scrollTop: false,
				contentText: {
					contentdown: '上拉显示更多',
					contentrefresh: '正在加载...',
					contentnomore: '没有更多数据了'
				}

			}
		},
		mounted() {
			uni.showLoading({
				title: '加载中...'
			});
			let a = this.$queue.getData("isEnable")
			if (a) {
				this.isEnable = a;
			}

			this.initializeFeed();
		},
		methods: {
			initializeFeed() {
			// #ifdef MP-WEIXIN
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			// #endif
			// #ifndef MP-WEIXIN
			this.shangxianSelect = '是';
			// #endif
			this.page = 0;
			this.getList('');
			this.getSelectBanner();
			this.getRenZheng();
			},
			refreshPosts() {
				if (this.isLoading) return;
				this.refreshing = true;
				this.page = 0;
				this.hasMore = true;
				this.getList('refresh');
				this.getSelectBanner();
			},
			loadMore() {
				if (this.isLoading || !this.hasMore) return;
				this.page += 1;
				this.getList('');
			},
			getRenZheng() {
				this.$Request.get("/app/userCertification/queryInsert").then(res => {
					if (res.code == 0) {
						// 0审核中 1通过 2拒绝
						if (res.data == null) {
							this.renzheng = 0 //未实名
						} else if (res.data.status == 0) {
							this.renzheng = 1 //审核中
						} else if (res.data.status == 1) {
							this.renzheng = 2 //已实名
						} else if (res.data.status == 2) {
							this.renzheng = 3 //已拒绝
						}
					}
				});
			},
			/**
			 * @param {Object} url
			 */
			toNavList: function(url) {
				if (!url) return;
				let token = this.$queue.getData('token');
				if (token) {
					if (url.indexOf('/pages/') !== -1) {
						uni.navigateTo({
							url
						});
					} else {
						//#ifndef H5
						uni.navigateTo({
							url: '/pages/index/webview?url=' + url
						});
						//#endif
						//#ifdef H5
						window.location.href = url;
						//#endif
					}
				} else {
					this.goLoginInfo();
				}
			},
			getSelectBanner() {
				this.$Request.getT('/app/banner/selectBannerList?classify=6&state=1').then(res => {
					if (res.code == 0) {
						this.bannerList = res.data;
					}
				});
			},
			topScrollTap: function() {
				uni.pageScrollTo({
					scrollTop: 0,
					duration: 300
				});
			},
			tabClick(e) {
				let token = this.$queue.getData('token');
				if (!token) {
					this.goLoginInfo();
					return;
				}
				this.tabIndex = e;
				this.page = 0;
				this.getList('');
			},
			clickItem: function(options) {
				let token = this.$queue.getData('token');
				if (!token) {
					this.goLoginInfo();
					return;
				}
				console.log(options)
				if (options.index == 0) {
					uni.navigateTo({
						url: '/package/pages/detail/listDetails?talkId=' + this.list[options.id].talkId
					});
				} else if (options.index == 1) {
					let token = this.$queue.getData('token');
					if (token) {
						//去关注页面
						uni.navigateTo({
							url: '/my/gird/guanzhuDetail?userId=' + this.list[options.id].userId
						});

					} else {
						this.goLoginInfo();
					}
				} else if (options.index == 2) {
					// this.getMessageList2();
				} else if (options.index == 3) {
					this.saveLove(this.list[options.id].talkId);
				}
			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/login'
				});
			},
			saveLove(talkId) {
				// if (this.renzheng != 2) {
				// 	uni.showModal({
				// 		title: '温馨提示',
				// 		content: '您还未认证陪玩官,请先认证',
				// 		showCancel: true,
				// 		cancelText: '取消',
				// 		confirmText: '确认',
				// 		success: res => {
				// 			if (res.confirm) {
				// 				uni.navigateTo({
				// 					url: "/my/renzheng/index"
				// 				});
				// 			}
				// 		}
				// 	});
				// 	return;
				// }
				let userId = this.$queue.getData('userId');
				let data = {
					talkId: talkId,
					userId: userId
				}
				this.$Request.postJson('/app/gambit/saveGoodTalk', data).then(res => {
					if (res.status == 0) {
						this.page = 0;
						this.getList('');
					} else {
						this.$queue.showToast(res.msg);
					}
				});
			},
			getList(type) {
				if (this.isLoading) return;
				this.isLoading = true;
				this.loadingType = 'loading';

				let userId = this.$queue.getData('userId') ? this.$queue.getData('userId') : '';
				let followUserId = 0;
				if (this.tabIndex != 0) {
					followUserId = userId;
				}
				this.$Request.getT('/app/gambit/selectTalkPage?page=' + this.page + '&size=' + this.size + '&byUserId=' +
					userId + '&followUserId=' + followUserId).then(res => {
					if (res.status == 0) {
						if (this.page == 0 || res.status) {
							this.list = [];
						}
						res.data.list.forEach(d => {
							if (d.img) {
								d.img = d.img.split(',');
							}
							this.list.push(d);
						})
						console.log(this.list, 'this.listthis.list')
						this.hasMore = res.data.list.length === this.size;
						if (this.hasMore) {
							this.loadingType = 0;
						} else {
							this.loadingType = 'more';
						}
					} else {
						this.loadingType = 2;
					}
					uni.hideLoading();
					console.log(this.list)
					this.isLoading = false;
					if (type === 'refresh') this.refreshing = false;
				}).catch(() => {
					this.isLoading = false;
					this.refreshing = false;
					uni.hideLoading();
				});
			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/login'
				});
			},
			bindfb() {
				let token = this.$queue.getData('token');
				if (token) {
					// if (this.renzheng != 2) {
					// 	uni.showModal({
					// 		title: '温馨提示',
					// 		content: '您还未认证陪玩官,请先认证',
					// 		showCancel: true,
					// 		cancelText: '取消',
					// 		confirmText: '确认',
					// 		success: res => {
					// 			if (res.confirm) {
					// 				uni.navigateTo({
					// 					url: "/my/renzheng/index"
					// 				});
					// 			}
					// 		}
					// 	});
					// 	return;
					// }
					uni.navigateTo({
						url: '/package/pages/releaseone/releaseone'
					})
				} else {
					this.goLoginInfo();
				}
			}
		}

	}
</script>

<style lang="scss">
	// @import '../../static/less/index.less';
	@import '../../static/css/index.css';

	.post-feed-scroll {
		width: 100%;
		height: 100%;
		background: #050506;
	}
	.post-feed-scroll .content {
		box-sizing: border-box;
		min-height: 100%;
		padding-top: calc(246rpx + env(safe-area-inset-top));
		padding-bottom: 220rpx;
	}

	.headtop {
		background: linear-gradient(180deg, #1C1C22 0%, #050506 100%);
		background-size: 100% 100%;
		background-repeat: no-repeat;
		position: fixed;
		/* #ifdef H5 */
		// padding: 30rpx 0;
		top: 0;
		padding-top: 30rpx;
		padding-left: 30rpx;
		height: 230rpx;
		/* #endif */
		/* #ifdef MP-WEIXIN */
		top: 0;
		height: 260rpx;
		padding-top: 60rpx;
		padding-left: 30rpx;
		/* #endif */
		/* #ifdef APP */
		top: 0;
		height: 280rpx;
		padding-top: 90rpx;
		padding-left: 30rpx;
		/* #endif */
		z-index: 9999;
	}

	.tui-tabs {
		width: 100%;
		// flex: 1;
		// flex-direction: column;
		// overflow: hidden;
		// background-color: #101014;
		/* #ifdef MP-ALIPAY || MP-BAIDU */
		// height: 100vh;
		/* #endif */
	}

	.tui-tab-item-title {
		width: 180rpx;
		height: 75rpx;
		line-height: 75rpx;
		border-radius: 37.5rpx 37.5rpx 37.5rpx 37.5rpx;
		background: #ffffff;
		text-align: center;
		color: #333333;
		margin-right: 25rpx;
		// font-size: 30rpx;
		// height: 80rpx;
		// line-height: 80rpx;
		// flex-wrap: nowrap;
		// white-space: nowrap;
	}

	.tui-tab-item-title-active {
		width: 180rpx;
		height: 75rpx;
		border-radius: 37.5rpx 37.5rpx 37.5rpx 37.5rpx;
		background: linear-gradient(-132deg, #D9A800, #FBCA0C);
		// border: 1rpx solid #e8deff;

		color: #0A0A0C;
		// font-size: 32upx;
		// font-weight: bold;
		text-align: center;
	}

	.swiper-container {
		// height: 240upx;
		transform: translateY(0);
		overflow: hidden;
		overflow: hidden !important;
		/* 兼容IOS，否则在swiper组件内的布局都不受border-radius和overflow的约束 */
		transform: translateY(0) !important;

		position: relative;
		/* #ifdef APP-PLUS */
		top: 0upx;
		/* #endif */
		/* #ifdef H5 */
		top: 0upx;
		/* #endif */
		/* #ifdef MP-WEIXIN */
		top: 0upx;
		/* #endif */
		right: 0upx;
		left: 0;
		margin: auto;
	}

	.tui-scroll-h {
		width: 750rpx;
		height: 75rpx;
		// margin-left: 30rpx;

		// background-color: #101014;
		flex-direction: row;
		/* #ifndef APP-PLUS */
		// white-space: nowrap;
		// margin-top: 30rpx;
		/* #endif */
		/* #ifdef APP-PLUS */
		margin-top: 30rpx;
		// position: fixed;
		// top: 0px;
		// left: 0;
		// z-index: 999;
		/* #endif */
		/* #ifdef H5 */
		margin-top: 30rpx;
		// position: fixed;
		// top: 80rpx;
		// left: 0;
		// z-index: 999;
		/* #endif */
		/* #ifdef MP-WEIXIN */
		margin-top: 20rpx;
		// position: fixed;
		// top: 0;
		// left: 0;
		// z-index: 999;
		/* #endif */
	}

	.content {
		width: 100%;
	}

	.part1 {
		position: relative;
	}

	.image_mengceng {
		width: 100%;
		height: 314rpx;
		opacity: 0.3;
		background: #000000;
		position: absolute;
		top: 0;
	}

	.content_bg {
		width: 100%;
		height: 314rpx;
	}

	.content_bg image {
		width: 100%;
		height: 314rpx;
	}

	.content_title {
		position: absolute;
		bottom: 25px;
		left: 20px;
		color: white;
	}

	.title {
		font-size: 38rpx;
		margin-bottom: 20rpx;
	}

	.content_title text {
		font-size: 24rpx;
		font-family: PingFang SC Medium, PingFang SC Medium-Medium;
		font-weight: 500;
		text-align: left;
		color: #ffffff;
	}

	.part3 {
		position: fixed;
		bottom: 150rpx;
		right: 30px;
		z-index: 9999;
	}

	.part3 image {
		width: 172rpx;
		height: 172rpx;
	}

	.subh {
		background: #050506;
		/* #ifdef H5 */
		margin: 240rpx 30rpx 0 30rpx;
		/* #endif */
		/* #ifdef MP-WEIXIN */
		margin: 270rpx 30rpx 0 30rpx;
		/* #endif */
		/* #ifdef APP */
		margin: 300rpx 30rpx 0 30rpx;
		/* #endif */
	}

	/* 帖子页与顶部「视频 / 帖子」导航共用同一层级。 */
	.post-feed-scroll .headtop {
		left: 0;
		right: 0;
		top: 0;
		width: 100%;
		height: calc(218rpx + env(safe-area-inset-top));
		padding: calc(136rpx + env(safe-area-inset-top)) 0 0 52rpx;
		box-sizing: border-box;
		background: linear-gradient(180deg, #101014 0, #101014 calc(88rpx + env(safe-area-inset-top)), #050506 100%);
		z-index: 80;
	}
	.post-feed-scroll .tui-scroll-h {
		width: 100%;
		height: 80rpx;
		margin-top: 0;
	}
	.post-feed-scroll .tui-tab-item-title,
	.post-feed-scroll .tui-tab-item-title-active {
		width: 240rpx;
		height: 80rpx;
		line-height: 80rpx;
		margin-right: 24rpx;
		border-radius: 40rpx;
		font-size: 32rpx;
	}
	.post-feed-scroll .tui-tab-item-title-active {
		background: linear-gradient(100deg, #FBCA0C 0%, #D9A800 100%);
		color: #0A0A0C;
	}
	.post-feed-scroll .subh {
		margin: 0 28rpx 28rpx;
		border-radius: 20rpx;
		background: transparent;
		overflow: hidden;
	}
	.post-feed-scroll .swiper-container {
		border-radius: 20rpx;
	}
	.post-feed-scroll .head {
		background: transparent;
	}
	.post-feed-scroll .page-box {
		height: 510rpx;
		margin-top: 85rpx;
	}
	.post-feed-scroll .page-box .centre {
		height: 260rpx;
	}
	.post-feed-scroll .page-box .centre image {
		width: 210rpx;
		height: 190rpx;
		margin-bottom: 0;
	}
	.post-feed-scroll .page-box .tips {
		margin-top: 12rpx;
		color: rgba(119, 116, 145, 0.33);
		font-size: 30rpx;
		font-weight: 400;
	}
	.post-feed-scroll .part3 {
		right: 24rpx;
		bottom: calc(300rpx + env(safe-area-inset-bottom));
		z-index: 90;
	}
	.post-feed-scroll .part3 image {
		width: 148rpx;
		height: 148rpx;
	}
</style>
