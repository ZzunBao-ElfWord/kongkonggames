<template>
	<view>
		<view class="margin-lr ">
			<view class=" radius margin-top-sm bg" v-for="(item,index) in order" :key="index">
				<view class="padding flex justify-between" style="color: #FBCA0C;">
					<view>
						<view v-if="item.status==1">进行中</view>
						<view v-if="item.status==2">匹配成功</view>
						<view v-if="item.status==3">已取消</view>
					</view>
					<view>{{item.createTime}}</view>
				</view>
				<view style="width: 650upx;height: 1upx;background: #2A2A32;margin: 0upx auto;"></view>
				<view class="padding">
					<view class="flex justify-between">
						<view class="text-lg text-bold">{{item.gameName}}<text
								v-if="item.myArea">-{{item.myArea}}</text></view>

						<view class="flex text-sm align-center">
							<view class="text-xl" style="color:#FBCA0C;">{{item.money}}</text>
								<image src="../../../static/images/index/jinbi.png"
									style="width: 22rpx;height: 23rpx;margin: 0rpx 10rpx;">
							</view>
						</view>
					</view>
					<view class="flex align-center margin-top-sm">
						<view class="radius margin-right-xs text-sm" v-if="item.sex == 0"
							style="background: #FBCA0C;padding:2rpx 8rpx 2rpx 8rpx;color: #0A0A0C;">
							不限
						</view>
						<view v-if="false" class="  margin-right-xs "
							style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 12upx;color: #6EE3FB;border-radius: 50rpx;">
							<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
							<text class="text-sm">男生</text>
						</view>
						<view v-if="false" class="  margin-right-xs"
							style="display: inline-block;background: linear-gradient(-90deg, #4F2B4B, #6735b1);padding: 2upx 12upx;color: #FB85FC;border-radius: 50rpx;">
							<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
							<text class="text-sm">女生</text>
						</view>
						<view class="titBox" v-if="item.myLevel">玩家段位：{{item.myLevel}} </view>

					</view>
					<view class="margin-top" v-if="item.orderLevel">期望TA：{{item.orderLevel}}</view>

					<view class="flex justify-between align-end">
						<view>期望时间：{{item.orderTakingTime}}</view>
						<view class="btn" @click="goDetail(item)">详情</view>
					</view>
				</view>
			</view>
			<empty v-if="order.length === 0" content="暂无数据" show="false"></empty>
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
				show: false,
				page: 1,
				limit: 10,
				count: '',
				// status: 0,
				order: []
			}
		},
		onLoad() {
			this.$queue.showLoading("加载中...");
			this.getOrderDet()
		},
		onShow() {

		},
		methods: {
			//跳转详情
			goDetail(e) {
				uni.navigateTo({
					url: '/package/pages/play/robDetail?fastOrderId=' + e.fastOrderId
				})
			},
			getOrderDet() {
				let data = {
					page: this.page,
					limit: this.limit,
					status: 0
				}
				this.$Request.get('/app/fastOrder/selectFastOrderTakeListByUserId', data).then(res => {
					if (res.code === 0) {
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
				this.getOrderDet();
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getOrderDet();
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

	.titBox {
		background: linear-gradient(90deg, #FBCA0C 0%, #D9A800 100%);
		color: #0A0A0C;
		border-radius: 8upx;
		padding: 4rpx 20rpx;
		font-size: 26rpx;
	}

	.btn {
		background: #FBCA0C;
		color: #0A0A0C;
		padding: 10rpx 40rpx;
		border-radius: 42rpx;
	}
</style>
