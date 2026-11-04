<template>
	<view>
		<!-- 新人优惠券弹窗 -->
		<u-popup v-model="showCoupons" :isShowBgImage="false" @close="closeCop" mode="center">
			<view class="showCoupon">
				<view class="showCoupon-center flex align-center justify-center">
					<view class="showCoupon-centers">
						<view class="showCoupon-centers-title flex align-center justify-center">
							<view class="showCoupon-centers-title-l flex align-center">
								<text></text>
								<text></text>
								<text></text>
							</view>
							<view class="showCoupon-centers-title-name">
								恭喜你获得新人优惠券
							</view>
							<view class="showCoupon-centers-title-r flex align-center">
								<text></text>
								<text></text>
								<text></text>
							</view>
						</view>
						<view class="showCoupon-centers-list">
							<scroll-view scroll-y="true" style="width: 100%;height: 100%;padding-bottom: 80rpx;">
								<view class="showCoupon-centers-list-item" v-for="(item,index) in list" :key="index">
									<image src="../../static/images/index/compBg.png" mode=""></image>
									<view class="showCoupon-centers-list-items flex justify-between">
										<view class="showCoupon-centers-list-items-l">
											<view class="showCoupon-centers-list-items-l-top">
												<view class="showCoupon-centers-list-items-l-topt flex align-center">
													{{item.couponName}}
												</view>
												<!-- <view class="showCoupon-centers-list-items-l-topb">
													有效期至2022-03-18
												</view> -->
											</view>
											<view class="showCoupon-centers-list-items-l-j">
												{{item.validDay}}天内有效
											</view>
										</view>
										<view
											class="showCoupon-centers-list-items-l-r flex align-center justify-center">
											<text>{{item.lessMoney}}</text>币
										</view>
									</view>
								</view>
							</scroll-view>
						</view>
					</view>
				</view>
				<view class="showCoupon-bottom"></view>
				<view class="showCoupon-btn flex ">
					<view class="showCoupon-btnss flex">
						<image src="../../static/images/index/bottomBgRadus.png" mode=""></image>
						<view @click="getMyNewCoupon()" class="showCoupon-btns flex align-center justify-center">
							立即领取
						</view>
					</view>

				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		name: "couponTipsCom",
		props: {
			showCoupon: {
				type: Boolean,
				default: false
			},
			//优惠券列表
			list: {
				type: Array,
				default: []
			}
		},
		data() {
			return {
				showCoupons: false,
			};
		},
		mounted() {
			this.showCoupons = this.showCoupon
		},
		methods: {
			//关闭优惠券
			closeCop() {
				this.$emit('closeCop')
			},
			//领取优惠券
			getMyNewCoupon() {
				this.$emit('getMyNewCoupon')
			},
		}
	}
</script>

<style lang="scss" scoped>
	.showCoupon {
		width: 534rpx;
		height: 700rpx;
		position: relative;

		.showCoupon-center {
			width: 492rpx;
			height: 560rpx;
			background: linear-gradient(0deg, #F8E6C7 0%, #F8EEDB 100%);
			border-radius: 32rpx 32rpx 32rpx 32rpx;
			position: absolute;
			top: 0;
			z-index: 2;
			left: 50%;
			transform: translate(-50%, 0);

			.showCoupon-centers {
				width: 472rpx;
				height: calc(100% - 20rpx);
				border: 1px solid #EDE0C4;
				border-radius: 32rpx;
				background: linear-gradient(0deg, #F9EBD4 0%, #FAF5EC 100%);
			}

			.showCoupon-centers-title {
				width: 100%;
				margin-top: 30rpx;

				.showCoupon-centers-title-l {
					text {
						display: inline-block;
					}

					text:nth-of-type(1) {
						width: 6rpx;
						height: 6rpx;
						border-radius: 50%;
						background-color: #F3C847;
						margin-right: 6rpx;
					}

					text:nth-of-type(2) {
						width: 8rpx;
						height: 8rpx;
						border-radius: 50%;
						background-color: #F3C847;
						margin-right: 6rpx;
					}

					text:nth-of-type(3) {
						width: 10rpx;
						height: 10rpx;
						border-radius: 50%;
						background-color: #F3C847;
						margin-right: 10rpx;
					}
				}

				.showCoupon-centers-title-name {
					color: #764B26;
					font-weight: bold;
				}

				.showCoupon-centers-title-r {
					text {
						display: inline-block;
					}

					text:nth-of-type(1) {
						width: 10rpx;
						height: 10rpx;
						border-radius: 50%;
						background-color: #F3C847;
						margin-left: 10rpx;
					}

					text:nth-of-type(2) {
						width: 8rpx;
						height: 8rpx;
						border-radius: 50%;
						background-color: #F3C847;
						margin-left: 6rpx;
					}

					text:nth-of-type(3) {
						width: 6rpx;
						height: 6rpx;
						border-radius: 50%;
						background-color: #F3C847;
						margin-left: 6rpx;
					}




				}
			}

			.showCoupon-centers-list {
				width: 430rpx;
				height: 500rpx;
				margin: 0 auto;
				margin-top: 30rpx;

				.showCoupon-centers-list-item {
					width: 100%;
					height: 160rpx;
					position: relative;
					margin-bottom: 10rpx;

					image {
						width: 100%;
						height: 160rpx;
					}

					.showCoupon-centers-list-items {
						width: 100%;
						height: 100%;
						position: absolute;
						top: 0;
						left: 0;

						.showCoupon-centers-list-items-l {
							width: calc(100% - 120rpx);
							padding: 0 16rpx;

							.showCoupon-centers-list-items-l-topt {
								width: 100%;
								height: 70rpx;
								color: #000000;

								font-weight: bold;
								margin-top: 14rpx;
								font-size: 26rpx;
								overflow: hidden;
								text-overflow: ellipsis;
								-webkit-line-clamp: 2;
								// display: -webkit-box;
								// -webkit-box-orient: vertical;
							}

							.showCoupon-centers-list-items-l-topb {
								color: #999999;
								font-weight: 400;
								font-size: 20rpx;
							}

							.showCoupon-centers-list-items-l-j {
								color: #999999;
								font-size: 24rpx;
								font-weight: 400;
								margin-top: 26rpx;
							}

						}

						.showCoupon-centers-list-items-l-r {
							width: 120rpx;
							font-size: 20rpx;
							color: #FD3C44;
							text-align: center;
							font-weight: 500;

							text {
								font-size: 30rpx;

							}
						}
					}
				}
			}
		}

		.showCoupon-bottom {
			width: 100%;
			height: 576rpx;
			background: linear-gradient(0deg, #D04331 0%, #E16443 100%);
			border-radius: 32rpx;
			position: absolute;
			bottom: 0;
			z-index: 1;
		}

		.showCoupon-btn {
			width: 100%;
			height: 200rpx;
			position: absolute;
			bottom: 0;
			z-index: 3;

			.showCoupon-btnss {
				width: 100%;
				height: 100%;
				position: relative;

				image {
					width: 100%;
					height: 100%;
				}
			}

			.showCoupon-btns {
				width: 360rpx;
				height: 80rpx;
				border-radius: 40rpx;
				background: linear-gradient(0deg, #EFB95E 0%, #FFDC95 100%);
				color: #D64E37;
				font-size: 28rpx;
				font-weight: bold;
				position: absolute;
				bottom: 40rpx;
				left: 50%;
				transform: translate(-50%, 0);
			}
		}

		// background-color: red;
	}
</style>