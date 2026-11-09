<template>
	<view>
		<view class=" margin-top-sm">
			<view class="margin-lr padding radius bg margin-bottom" v-for="(item,index) in order" :key="index">
				<view class="flex justify-between">
					<view class="text-lg text-bold" >{{item.gameName}} <text v-if="item.myArea">- {{item.myArea}}</text></view>
					
					<view class="flex text-sm align-center">
						<view class="text-xl" style="color: #FBCA0C;">
							{{item.money}}
							<image src="../../../static/images/index/jinbi.png"
								style="width: 22rpx;height: 23rpx;margin: 0rpx 10rpx;">
							</image>
						</view>/局
					</view>
				</view>
				<view class="flex align-center margin-top-sm">
					<view v-if="false" class="  margin-right-xs"
						style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 12upx;color: #6EE3FB;border-radius: 50rpx;">
						<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
						男生
					</view>
					<view v-if="false" class="  margin-right-xs"
						style="display: inline-block;background: linear-gradient(-90deg, #4F2B4B, #6735b1);padding: 2upx 12upx;color: #FB85FC;border-radius: 50rpx;">
						<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
						女生
					</view>
					<view class="radius margin-right-xs" v-if="item.sex == 0"
						style="background: #FBCA0C;padding:2px 8px 2px 8px;color: #0A0A0C;">
						不限
					</view>
					<!-- <view v-if="false" class="radius text-white margin-right-xs"
						style="display: inline-block;background: linear-gradient(0deg, #51bdfb 0%, #5c80f2 100%);padding: 2upx 8upx;">
						<u-icon name="man" color="#ffffff" size="24"></u-icon>
						男生
					</view>
					<view v-if="false" class="radius text-white margin-right-xs"
						style="display: inline-block;background: linear-gradient(0deg, #FD8882 0%, #F07284 100%);padding: 2upx 8upx;">
						<u-icon name="woman" color="#ffffff" size="24"></u-icon>
						女生
					</view>
					<view class="radius margin-right-xs" v-if="item.sex == 0"
						style="background: #f2f2f2;padding:2px 8px 5px 8px;color: #000000;">
						不限
					</view> -->
					<view class="titBox" v-if="item.myLevel"> 玩家段位 {{item.myLevel}}</view>
					
				</view>
				<view class="margin-top" v-if="item.orderLevel">
					期望TA：{{item.orderLevel}}</view>
				<view class="flex justify-between align-end">
					<view>期望时间：{{item.orderTakingTime}}</view>
					<view class="btn" @click="goDetail(item)">抢单</view>
				</view>
			</view>
			<empty v-if="order.length == 0"></empty>
		</view>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				
				page: 1,
				limit: 10,
				count: '',
				order: [],

			}
		},
		onLoad() {
			this.$queue.showLoading("加载中...");
			this.getOrderList()


		},
		onShow() {
			let that = this
			setInterval(function() {
				that.getOrderList()
			}, 5000)
		},
		methods: {
			goDetail(e) {
				console.log(e)
				uni.navigateTo({
					url: '/my/publish/orderDetail?fastOrderId=' + e.fastOrderId
				})
				
			},
			getOrderList() {
				let data = {
					page: this.page,
					limit: this.limit,
					
				}
				this.$Request.get('/app/fastOrder/selectFastOrderTake', data).then(res => {
					if (res.code == 0) {
						if (this.page == 1) {
							this.order = res.data.list
						} else {
							res.data.list.forEach(d => {
								this.order.push(d);
							});
						}
						this.count = res.data.totalCount;

					} else {
						uni.hideLoading();
						this.$queue.showToast(res.msg)
					}
					uni.stopPullDownRefresh();
					uni.hideLoading();
				});
			}
		},
		onReachBottom: function() {
			if (this.order.length == this.count) {
				uni.showToast({
					title: '已经到底了',
					icon: 'none'
				})
			} else {
				this.page = this.page + 1;
				this.getOrderList();
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getOrderList();
		}
	}
</script>

<style>
	page {
		background: #050506;
	}

	.bg {
		background: #101014;
	}

	
	.tabOrder {
		width: 50%;
		text-align: center;
		padding: 25rpx 0rpx;
	}

	.active {
		background: #1C1C22;
		padding: 25rpx 0rpx;
	}

	.titBox {
		background: linear-gradient(90deg, #FBCA0C 0%, #D9A800 100%);
		color: #0A0A0C;
		border-radius: 8upx;
		padding: 7rpx 20rpx;
	}

	.btn {
		background: #FBCA0C;
		color: #0A0A0C;
		padding: 10rpx 40rpx;
		border-radius: 42rpx;
	}
</style>
