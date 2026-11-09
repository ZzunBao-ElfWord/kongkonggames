<template>
	<view class="content">
		<view class="headtop" v-if="shangxianSelect!='否'">
			<view class="">
				<image src="https://pw.xianmxkj.com/file/uploadPath/2023/03/21/75825755ef293345ee6960bf32b4b5db.png"
					style="width: 146rpx;height: 79rpx;"></image>
			</view>
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
		onLoad(d) {
			uni.showLoading({
				title: '加载中...'
			});
			let a = this.$queue.getData("isEnable")
			if (a) {
				this.isEnable = a;
			}

			uni.$on('click', (val) => {
				console.log("val______:" + val)
			});
		},
		onShow() {
			// #ifdef MP-WEIXIN
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			// #endif
			// #ifndef MP-WEIXIN
			this.shangxianSelect = '是';
			// #endif
			if (this.shangxianSelect != '否') {
				uni.setNavigationBarTitle({
					title: '动态'
				})
			} else {
				uni.setNavigationBarTitle({
					title: '帮助中心'
				})
			}
			this.page = 0;
			this.getList('');
			this.getSelectBanner();
			this.getRenZheng();
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		methods: {
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
						if (res.data.list.length === this.size) {
							this.loadingType = 0;
						} else {
							this.loadingType = 'more';
						}
					} else {
						this.loadingType = 2;
					}
					uni.hideLoading();
					console.log(this.list)
					if (type === 'refresh') {
						uni.stopPullDownRefresh();
					}
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
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getList('');
		},
		onPullDownRefresh: function() {
			this.page = 0;
			this.getList('refresh');
			this.getSelectBanner();
		}
	}
</script>

<style lang="scss">
	// @import '../../static/less/index.less';
	@import '../../static/css/index.css';

	page {
		background-color: #1A1728;
	}

	.headtop {
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/c14daf6709e386c8236908f1f61db8ef.png');
		// background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/20/c3e50814c6bb43cdeee6e046c93eae77.png');
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
		// background-color: #1E1F31;
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
		background: linear-gradient(-132deg, #3e2b55, #ac75fe);
		// border: 1rpx solid #e8deff;

		color: #FFFFFF;
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

		// background-color: #1E1F31;
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
		background: #1A1728;
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
</style>