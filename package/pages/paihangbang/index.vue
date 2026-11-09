<template>
	<view>
		<view class="part1">
			<view class="flex">
				<image src="https://pw.xianmxkj.com/file/uploadPath/2023/03/22/001756405e50d46613cda94c7e1e5350.png"
					class="bg-back" @click="goback()" :style="{top:searchBarTop + searchBarHeight - 25 + 'px'}">
				</image>
				<!-- #ifdef H5 -->
				<view
					style="width: 520rpx;margin-top: 46rpx;display: flex;justify-content: center;margin-left: 130rpx;">
				<!-- #endif -->
					<!-- #ifndef H5 -->
					<view
						style="width: 520rpx;margin-top: 150rpx;display: flex;justify-content: center;margin-left: 130rpx;">
					<!-- #endif -->
						<scroll-view id="tab-bar" scroll-with-animation class="tui-scroll-h" :scroll-x="true"
							:show-scrollbar="false">
							<view
								style="display: flex;background: linear-gradient(90deg, #101014 1%, #221A06 33%, #4A3808 65%, #2C2613 100%);border-radius: 50rpx;">
								<view :key='index' v-for="(tab, index) in tabBars" :id="tab.id" :data-current="index"
									@tap="tabClick(index)">
									<view class="tui-tab-item-title"
										:class="{ 'tui-tab-item-title-active': tabIndex == index }">{{ tab.name }}
									</view>
								</view>
							</view>
						</scroll-view>
					</view>
				</view>
				<view class="part1_sw" v-if="rankNumList.length > 0">
					<view class="part2">
						<view class="part_one" v-if="rankNumList.length > 0">
							<view style="position: relative;">
								<view>
									<image
										:src="rankNumList[0].avatar ? rankNumList[0].avatar : '../../../static/logo.png'"
										style="width: 173rpx;height: 173rpx;border-radius: 80rpx;"></image>
								</view>
								<view style="position: absolute;top: 90rpx;left: -16rpx;">
									<image
										src="https://pw.xianmxkj.com/file/uploadPath/2023/03/21/cb2f87ce17558e76f12797cab3b08110.png"
										style="width: 199rpx;height: 100rpx;"></image>
								</view>
								<view
									style="width: 200rpx;text-align: center;;position: absolute;top: 190rpx;left: -20rpx;">
									<view class="text-cut">{{rankNumList[0].userName}}</view>
									<view v-if="tabIndex == 0">{{rankNumList[0].ordersCount}}接单</view>
									<view class="flex align-center text-center" v-else
										style="color: #FBCA0C;width: 200rpx;justify-content: center;">
										<view>{{rankNumList[0].money}}</view>
										<image
											style="display: inline-block;margin-left: 6upx;width: 30rpx;height: 30rpx;"
											src="../../../static/images/index/jinbi.png"></image>
									</view>
								</view>
							</view>

						</view>

						<view class="part_two" v-if="rankNumList.length > 1">
							<view style="position: relative;">
								<view>
									<image
										:src="rankNumList[1].avatar ? rankNumList[1].avatar : '../../../static/logo.png'"
										style="width: 130rpx;height: 130rpx;border-radius: 80rpx;"></image>
								</view>
								<view style="position: absolute;top: 90rpx;left: -16rpx;">
									<image
										src="https://pw.xianmxkj.com/file/uploadPath/2023/03/21/a4fec6ec0d7e21d0f6facccef3c3cc54.png"
										style="width: 152rpx;height: 54rpx;"></image>
								</view>
								<view
									style="width: 200rpx;text-align: center;;position: absolute;top: 150rpx;left: -40rpx;">
									<view class="text-cut">{{rankNumList[1].userName}}</view>
									<view v-if="tabIndex == 0">{{rankNumList[1].ordersCount}}接单</view>
									<view class="flex align-center text-center" v-else
										style="color: #FBCA0C;width: 200rpx;justify-content: center;">
										<view>{{rankNumList[1].money}}</view>
										<image
											style="display: inline-block;margin-left: 6upx;width: 30rpx;height: 30rpx;"
											src="../../../static/images/index/jinbi.png"></image>
									</view>
								</view>
							</view>
						</view>

						<view class="part_three" v-if="rankNumList.length > 2">
							<view style="position: relative;">
								<view>
									<image
										:src="rankNumList[2].avatar ? rankNumList[2].avatar : '../../../static/logo.png'"
										style="width: 130rpx;height: 130rpx;border-radius: 80rpx;"></image>
								</view>
								<view style="position: absolute;top: 90rpx;left: -16rpx;">
									<image
										src="https://pw.xianmxkj.com/file/uploadPath/2023/03/21/d1bdc6f44004d6a1576e5fb7af6a6c40.png"
										style="width: 152rpx;height: 54rpx;"></image>
								</view>
								<view
									style="width: 200rpx;text-align: center;;position: absolute;top: 150rpx;left: -40rpx;">
									<view class="text-cut">{{rankNumList[2].userName}}</view>
									<view v-if="tabIndex == 0">{{rankNumList[2].ordersCount}}接单
									</view>
									<view class="flex align-center text-center" v-else
										style="color: #FBCA0C;width: 200rpx;justify-content: center;">
										<view>{{rankNumList[2].money}}</view>
										<image
											style="display: inline-block;margin-left: 6upx;width: 30rpx;height: 30rpx;"
											src="../../../static/images/index/jinbi.png"></image>
									</view>
								</view>
							</view>
						</view>
					</view>
					<view class="listbox" v-if="rankNumDetailList.length > 0">
						<view class="" v-for="(item,index) in rankNumDetailList" @tap="goGuanZhuDetail(item.userId)">
							<view class="flex align-center padding-tb-sm">
								<view class="number" v-if="index < 9">0{{index + 1}}</view>
								<view class="number" v-else>{{index + 1}}</view>
								<view class="padding-lr ">
									<image :src="item.avatar ? item.avatar : '../../../static/logo.png'"
										style="width: 100rpx;height: 100rpx;border-radius: 28rpx;border: 1rpx solid #FFD255;">
									</image>
								</view>
								<view class="flex align-center justify-between" style="width: 80%;">
									<view>
										<view class="titlename">{{item.userName}}</view>
										<view v-if="false"
											style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 12upx;color: #6EE3FB;border-radius: 50rpx;">
											<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
											{{item.age?item.age:0}}
										</view>
										<view v-if="false"
											style="display: inline-block;background: linear-gradient(-90deg, #4F2B4B, #6735b1);padding: 2upx 12upx;color: #FB85FC;border-radius: 50rpx;">
											<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
											{{item.age?item.age:0}}
										</view>
									</view>
									<view class="text-26" v-if="tabIndex == 0"><text
											class="jushu">{{item.ordersCount?item.ordersCount:'0'}}</text>接单量</view>
									<view class="flex align-center text-center" style="color: #FBCA0C;" v-else>
										<view>{{item.money?item.money:'0'}}</view>
										<image
											style="display: inline-block;margin-left: 6upx;width: 30rpx;height: 30rpx;"
											src="../../../static/images/index/jinbi.png"></image>
									</view>

								</view>
							</view>
						</view>
					</view>
				</view>

			</view>
		</view>
</template>

<script>
	export default {
		data() {
			return {
				tabIndex: 0,
				tabBars: [{
						name: '接单榜',
						id: 'jd',
						state: '-1'
					}, {
						name: '贡献榜',
						id: 'lb',
						state: '1'
					}
				],
				rankNumList: [],
				rankNumDetailList: [],
				page: 1,
				limit: 10,
				scrollTop: false,
				searchBarTop: 70, //搜索栏的外边框高度，单位px
				searchBarHeight: 0, //搜索栏的高度，单位px
			}
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getNumDetailList();
		},
		onLoad() {
			this.getNumList();
			this.getNumDetailList();
		},
		created() {
			// #ifdef MP-WEIXIN
			let topHeight = uni.getSystemInfoSync().statusBarHeight
			let menuButtonInfo = uni.getMenuButtonBoundingClientRect();
			this.searchBarTop = topHeight;
			this.searchBarHeight = menuButtonInfo.height;
			// #endif
		},
		methods: {
			tabClick(e) {
				let token = this.$queue.getData('token');
				if (!token) {
					this.goLoginInfo();
					return;
				}
				this.tabIndex = e;
				this.page = 1;
				this.getNumList();
				this.getNumDetailList();
			},
			goback() {
				uni.navigateBack()
			},
			goGuanZhuDetail(userId) {
				let token = this.$queue.getData('token');
				if (token) {
					uni.navigateTo({
						url: '/my/gird/guanzhuDetail?userId=' + userId
					});
				} else {
					this.goLoginInfo();
				}

			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/login'
				});
			},
			getNumList() {
				this.$queue.showLoading('加载中...')
				if (this.tabIndex == 0) {
					this.$Request.getT('/app/user/selectUserByOrdersCount?page=1&limit=3').then(res => {
						if (res.code == 0) {
							if (res.data && res.data.list && res.data.list.length > 0) {
								this.rankNumList = res.data.list;
							}
							uni.hideLoading();
						}
					});
				} else if (this.tabIndex == 1) {
					this.$Request.getT('/app/orders/selectUserMoneyOrdersList?page=1&limit=3').then(res => {
						if (res.code == 0) {
							if (res.data && res.data.list && res.data.list.length > 0) {
								this.rankNumList = res.data.list;
							}
							uni.hideLoading();
						}
					});
				}
			},
			getNumDetailList() {
				this.$queue.showLoading('加载中...')
				if (this.tabIndex == 0) {
					this.$Request.getT('/app/user/selectUserByOrdersCount?page=' + this.page + '&limit=' + this.limit)
						.then(
							res => {
								if (res.code === 0) {
									if (this.page == 1) {
										this.rankNumDetailList = res.data.list
									} else {
										res.data.list.forEach(d => {
											this.rankNumDetailList.push(d);
										});
									}
								}
								uni.hideLoading();
								uni.stopPullDownRefresh();
							});
				} else if (this.tabIndex == 1) {
					this.$Request.getT('/app/orders/selectUserMoneyOrdersList?page=' + this.page + '&limit=' + this.limit)
						.then(
							res => {
								if (res.code === 0) {
									if (this.page == 1) {
										this.rankNumDetailList = res.data.list
									} else {
										res.data.list.forEach(d => {
											this.rankNumDetailList.push(d);
										});
									}
								}
								uni.hideLoading();
								uni.stopPullDownRefresh();
							});
				}
			}
		}

	}
</script>

<style lang="less">
	.part1 {
		width: 100%;
		height: 100vh;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/07/24/fb66f6273075d88e80073cf1943f3957.png');
		background-size: 100% 100vh;
		position: relative;
		// background-repeat: no-repeat;

		// .fabhui {
		// 	position: absolute;
		// 	top: 55rpx;
		// 	left: 40rpx;
		// 	z-index: 99;
		// }
		.bg-back {
			width: 50rpx;
			height: 50rpx;
			position: fixed;

			left: 30rpx;
			z-index: 999;
		}

		/* image{
			width: 100%;
			height: 100vh;
		} */
		.part1_sw {
			position: absolute;
			/* #ifdef H5 */
			top: 220rpx;
			/* #endif */
			/* #ifndef H5 */
			top: 280rpx;
			/* #endif */
			left: 0;
			right: 0;
			z-index: 9;

			.part2 {
				width: 677rpx;
				height: 312rpx;
				margin: 0 auto;
				/* #ifdef H5 */
				background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/28/b3415bc856d0e9550bbc3a1a92d536e6.png');
				/* #endif */
				/* #ifndef H5 */
				background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/28/b3415bc856d0e9550bbc3a1a92d536e6.png');
				/* #endif */
				background-size: 100% 100%;
				position: relative;

				.part_one {
					position: absolute;
					top: 30rpx;
					left: 255rpx;
				}

				.part_two {
					position: absolute;
					top: 60rpx;
					left: 49rpx;
				}

				.part_three {
					position: absolute;
					top: 60rpx;
					right: 49rpx;
				}
			}
		}

		.listbox {
			border-radius: 24rpx;
			background: #212037ff;
			padding: 30rpx;
			margin: 0 30rpx;

			.number {
				font-size: 30rpx;
				font-weight: bold;
			}

			.titlename {
				margin-bottom: 10rpx;
			}

			.jushu {
				font-size: 30rpx;
				font-weight: bold;
			}
		}
	}

	.tui-scroll-h {
		width: 550rpx;
		height: 75rpx;
		// margin-left: 30rpx;
		// background-color: #101014;
		flex-direction: row;
		/* #ifdef APP-PLUS */
		margin-top: 30rpx;
		/* #endif */
		/* #ifdef H5 */
		margin-top: 30rpx;
		/* #endif */
		/* #ifdef MP-WEIXIN */
		margin-top: 20rpx;
		/* #endif */
	}

	.tui-tab-item-title {
		width: 180rpx;
		height: 75rpx;
		line-height: 75rpx;
		border-radius: 37.5rpx 37.5rpx 37.5rpx 37.5rpx;
		// background: #ffffff;
		text-align: center;
		color: #FFFFFF;
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
</style>