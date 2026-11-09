<template>
	<view>
		<!-- #ifdef MP-WEIXIN -->
		<view style="padding-bottom: 70rpx;" v-if="list.length>0">
			<view style="margin:20rpx 20rpx" v-for='(item,index) in list' :key='index'>
				<view class="bgImgaes">
				<!-- #endif -->

					<!-- #ifndef MP-WEIXIN -->
					<view style="">
						<view style="margin:20rpx 20rpx;" v-for='(item,index) in list' :key='index'>
							<view class="bgImgaes">
							<!-- #endif -->
								<view class="flex " style="width: 100%;padding: 10rpx 0;">
									<view style="width: 70%;height: 165rpx;margin-left: 30rpx;">
										<view
											style="font-size: 28rpx;color: #FFFFFF;overflow: hidden;margin-top: 20upx;white-space: nowrap;text-overflow: ellipsis;">
											{{item.couponName}}
										</view>
										<view style="font-size: 24rpx;color: #d5d5d5;margin-top: 40rpx;">
											有效期至{{item.failureTime}}</view>
									</view>
									<view style="text-align: center;width: 30%;margin-top: 60rpx;text-align: center;">
										<view style="color: #FBEA74;font-size:38rpx;"><text
												style="font-size: 22rpx;"></text>{{item.lessMoney}}币</view>
									</view>
								</view>

								<view class="flex align-center "
									style="margin-left: 30rpx;margin-top: 3rpx;padding-bottom: 20rpx;">
									<view style="width: 75%;font-size: 22rpx;color: #FFFFFF;" v-if="item.minMoney>0">
										满{{item.minMoney}}可用
									</view>
									<view style="width: 75%;font-size: 22rpx;color: #FFFFFF;" v-else>无门槛使用
									</view>
									<view
										style="background: #AC75FE;color: #FFFFFF;font-size: 22rpx;border-radius: 55rpx;padding:10rpx 25rpx 10rpx 25rpx;text-align: center;"
										@tap='shiyong(item)'>立即使用</view>
								</view>
								<!-- 	<view class="flex align-center"  style="margin-left: 10rpx;margin-top: 5rpx;">
										<view style="width:75%;font-size: 22rpx;margin-top: 5rpx;">报名抵柚币专用。</view>
										<view
											style="background: #AC75FE;margin-top: 5rpx;color: #FFFFFF;font-size: 22rpx;border-radius: 55rpx;padding:10rpx 25rpx 10rpx 25rpx;text-align: center;"
											@tap='shiyong(item)'>立即使用</view>
									</view> -->
							</view>
						</view>
					</view>
					<!-- 悬浮上拉 -->
					<view class="scroll_top" @tap="topScrollTap" v-bind:class="[scrollTop ? 'active' : '','']"
						style="bottom: 56px;">
						<text class="iconfont icon-shangla"></text>
					</view>

					<!-- 加载更多提示 -->
					<view class="s-col is-col-24" v-if="list.length > 0">
						<load-more :loadingType="loadingType" :contentText="contentText"></load-more>
					</view>
					<!-- 加载更多提示 -->
					<!-- 加载更多提示 -->
					<!-- #ifdef APP-PLUS -->
					<empty v-if="list.length === 0" content="您当前没有优惠券哦~" style="margin-top: 80rpx;" show="false">
					</empty>
					<!-- #endif -->
					<!-- #ifndef APP-PLUS -->
					<empty v-if="list.length === 0" content='您当前没有优惠券哦' style="margin-top: 80rpx;"></empty>
					<!-- #endif -->
				</view>
</template>

<script>
	import empty from '../../../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				page: 0,
				size: 10,
				loadingType: 0,
				scrollTop: false,
				tabIndex: 1,
				bj1: 'https://pw.xianmxkj.com/youhuiquan.png',
				list: [],
				contentText: {
					contentdown: '上拉显示更多',
					contentrefresh: '正在加载...',
					contentnomore: '没有更多劵了'
				}
			};
		},
		onShow() {
			this.getYouHuiJuanList('', this.tabIndex);
		},
		methods: {
			getCouponIssueList() {
				let userId = this.$queue.getData('userId');
				this.$Request.getT('/app/selfCouponIssue/useList?goodsId=&userId=' + userId).then(res => {
					if (res.status === 0) {
						res.data.forEach(d => {
							this.CouponIssueList.push(d);
						});
					}
				});
			},
			shiyong(item) {
				uni.switchTab({
					url: '/pages/index/index'
				})
			},
			getYouHuiJuanList(type, status) {
				this.loadingType = 1;
				uni.showLoading({
					title: '加载中...'
				});
				let userId = this.$queue.getData('userId');
				this.$Request.getT('/app/selfCouponUser/userList?page=' + this.page + '&size=' + this.size + '&userId=' +
					userId +
					'&goodsId=&type=' + status).then(res => {
					if (res.status === 0) {
						if (this.page === 0 || res.status) {
							this.list = [];
						}
						res.data.content.forEach(d => {
							// if (!d.coupon.goodsImages) {
							// 	d.coupon.goodsImages =
							// 		'https://h5.jiudiansai.com/img/20210119/1dc6276309584b6d9b91adf6d1fb4a34.png';
							// }
							this.list.push(d);
						});
						if (res.data.content.length === this.size) {
							this.loadingType = 0;
						} else {
							this.loadingType = 3;
						}
						uni.hideLoading();
					} else {
						this.loadingType = 2;
					}
					uni.hideLoading();
					if (type === 'Refresh') {
						uni.stopPullDownRefresh(); // 停止刷新
					}
				});
			},
			tabSlect(item) {
				this.tabIndex = item.id;
				this.page = 0;
				this.getYouHuiJuanList('', this.tabIndex);
			},
			topScrollTap: function() {
				uni.pageScrollTo({
					scrollTop: 0,
					duration: 300
				});
			}
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getYouHuiJuanList('', this.tabIndex);
		},
		onPullDownRefresh: function() {
			this.page = 0;
			this.getYouHuiJuanList('Refresh', this.tabIndex);
		}
	};
</script>

<style>
	@import '../../../static/css/index.css';

	page {
		background: #1e1f31;
		padding-bottom: 120rpx;
	}

	.bgImgaes {
		/* #ifdef MP-WEIXIN */
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/22/5ba4050eb7d10775c8ed6f58313413d6.png');
		background-size: 100%;
		border-radius: 10rpx;
		/* height: 260rpx; */
		/* margin-top: 10rpx; */
		/* #endif */

		/* #ifndef MP-WEIXIN */
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/22/5ba4050eb7d10775c8ed6f58313413d6.png');
		background-size: 100%;
		border-radius: 10rpx;
		/* height: 260rpx; */
		/* margin-top: 10rpx; */

		/* #endif */

	}

	.tui-tabs {
		flex: 1;
		flex-direction: column;
		overflow: hidden;
		background-color: #fafafa;
		/* #ifdef MP-ALIPAY || MP-BAIDU */
		height: 100vh;
		/* #endif */
	}

	.tui-scroll-h {
		width: 750rpx;
		height: 80rpx;
		background-color: #ffffff;
		flex-direction: row;
		/* #ifndef APP-PLUS */
		white-space: nowrap;
		/* #endif */
		/* #ifdef H5 */
		position: fixed;
		top: 44px;
		left: 0;
		z-index: 999;
		/* #endif */
	}

	.tui-line-h {
		/* #ifdef APP-PLUS */
		height: 1rpx;
		background-color: #cccccc;
		/* #endif */
		position: relative;
	}

	/* #ifndef APP-PLUS*/
	.tui-line-h::after {
		content: '';
		position: absolute;
		border-bottom: 1rpx solid #cccccc;
		-webkit-transform: scaleY(0.5);
		transform: scaleY(0.5);
		bottom: 0;
		right: 0;
		left: 0;
	}

	/* #endif */
	.tui-tab-item {
		/* #ifndef APP-PLUS */
		display: flex;
		/* #endif */
		// flex-wrap: nowrap;
		// padding-left: 34rpx;
		// padding-right: 34rpx;
	}

	.tui-tab-item-title {
		color: #555;
		font-size: 30rpx;
		height: 80rpx;
		line-height: 80rpx;
		flex-wrap: nowrap;
		/* #ifndef APP-PLUS */
		white-space: nowrap;
		/* #endif */
	}

	.tui-tab-item-title-active {
		color: #5E81F9;
		font-size: 32upx;
		font-weight: bold;
		border-bottom: 1rpx solid #5E81F9;
		border-bottom-width: 5upx;
		text-align: center;
	}

	.limapboxqing2 {

		font-size: 28upx;
		color: #333333;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}
</style>
