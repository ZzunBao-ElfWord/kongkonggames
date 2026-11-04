<template>
	<view>
		<view class="bg radius padding margin u-skeleton-fillet">
			<view class="flex">
				<image :src="order.game.gameImg" style="width: 76upx;height: 76upx;border-radius: 12rpx;"></image>
				<view class="flex margin-left-sm align-center">
					<view class="text-lg text-bold margin-right-xs">{{order.game.gameName}}</view>
					<view class="radius margin-right-xs" v-if="order.sex == 0"
						style="background: #AC75FE;padding:2px 8px 2px 8px;color: #FFFFFF;">
						不限
					</view>
					<view v-if="order.sex == 1" class="  margin-right-xs"
						style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 8upx;color: #6EE3FB;border-radius: 50rpx;">
						<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
						男生
					</view>
					<view v-if="order.sex == 2" class="  margin-right-xs"
						style="display: inline-block;background: linear-gradient(-90deg, #4F2B4B, #6735b1);padding: 2upx 8upx;color: #FB85FC;border-radius: 50rpx;">
						<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
						女生
					</view>

					<!-- <view class="radius margin-right-xs" v-if="order.sex == 0"
						style="background: #f2f2f2;padding:2px 8px 5px 8px;color: #000000;">
						不限
					</view> -->
					<!-- <view v-if="order.sex == 1" class="radius text-white margin-right-xs"
						style="display: inline-block;background: linear-gradient(0deg, #51bdfb 0%, #5c80f2 100%);padding: 2upx 8upx;">
						<u-icon name="man" color="#ffffff" size="24"></u-icon>
						男生
					</view>
					<view v-if="order.sex == 2" class="radius text-white margin-right-xs"
						style="display: inline-block;background: linear-gradient(0deg, #FD8882 0%, #F07284 100%);padding: 2upx 8upx;">
						<u-icon name="woman" color="#ffffff" size="24"></u-icon>
						女生
					</view> -->
				</view>
			</view>
			<view style="color: #FFFFFF;">
				<!-- 	<view class="flex margin-tb-sm">
					<view>服务类型：</view>
					<view style="color: #FFFFFF;">{{order.serviceType}}</view>
				</view> -->
				<view class="flex margin-tb-sm" v-if="order.myLevel||order.myArea">
					<view class="flex align-center" style="color: #FFFFFF;">
						<text style="color: #FFFFFF;" class="margin-right-xs" v-if="order.myLevel">玩家段位 /</text>
						<text style="color: #FFFFFF;" class="margin-lr-xs" v-if="order.myArea">大区：</text>
					</view>
					<view style="color: #FFFFFF;width: 50%;">{{order.myLevel}} / {{order.myArea}}</view>
				</view>
				<!-- <view class="flex margin-tb-sm" v-if="order.sex">
					<view>性别：</view>
					<view style="color: #FFFFFF;" v-if="order.sex ==2">女</view>
					<view style="color: #FFFFFF;" v-if="order.sex ==0">不限</view>
					<view style="color: #FFFFFF;" v-if="order.sex ==1">男</view>
				</view> -->
				<!-- <view class="flex margin-tb-sm" v-if="order.skill">
					<view>技能水平要求：</view>
					<view style="color: #FFFFFF;">{{order.skill}}</view>
				</view> -->
				<view class="flex margin-tb-sm" v-if="order.orderLevel">
					<view>期望段位：</view>
					<view style="color: #FFFFFF;">{{order.orderLevel}}</view>
				</view>

				<view class="flex margin-top" v-if="order.money">
					<view>价格要求：</view>
					<view class="flex align-center" style="color: #FFFFFF;">{{order.money}}
						<image style="display: inline-block;margin:0upx 6upx;width: 20rpx;height: 20rpx;"
							src="../../../static/images/index/jinbi.png"></image>
					</view>
				</view>
				<view class="flex margin-tb-sm" v-if="order.num">
					<view>局数要求：</view>
					<view style="color: #FFFFFF;">{{order.num}}</view>
				</view>
				<view class="flex margin-tb-sm" v-if="order.location||order.hero">
					<view>期望TA：</view>
					<view style="color: #FFFFFF;">{{order.location}} / {{order.hero}} </view>
				</view>
				<view class="flex margin-tb-sm" v-if="order.orderTakingTime">
					<view>期望时间：</view>
					<view style="color: #FFFFFF;">{{order.orderTakingTime}}</view>
				</view>
				<view class="flex ">
					<view class="text-df text-bold">其他要求：</view>
					<view class="text-sm">{{order.remark?order.remark:'暂无其他要求'}}</view>
				</view>
			</view>
		</view>
		<u-skeleton :loading="loading" :animation="true" elColor='#1E1F31' bgColor='#111224'></u-skeleton>
		<!-- <view class="margin-lr flex justify-between dingw">
			<!-- <view class="btn" @click="show = true">确认抢单</view> -->
		<!-- <view class="btn" @click="deteOrder()">取消抢单</view> -->
	</view>

	<!-- 抢单弹框 -->
	<!-- <u-popup v-model="show" mode="center" width="520rpx" height="420rpx" border-radius="14" closeable="true"
			close-icon="close-circle" close-icon-size="48" close-icon-color="#ffffff">
			<view class="text-center" style="margin-top: 75rpx;">
				<view class="text-lg text-bold">已提交抢单</view>
				<view class="popuptit">您已成功提交抢单！可在“我的抢单” 查看该订单，抢单成功后可在 “我的接单”查看该订单。</view>
				<view class="btn1">确认</view>
			</view>
		</u-popup> -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				loading: true, // 是否显示骨架屏组件
				show: true,
				fastOrderId: '',
				order: []
			}
		},
		onLoad(option) {
			this.$queue.showLoading("加载中...");
			this.fastOrderId = option.fastOrderId
			if (this.fastOrderId) {
				this.getFastOrder()
			}
		},
		onShow() {

		},
		methods: {
			//订单详情
			getFastOrder() {
				let data = {
					fastOrderId: this.fastOrderId
				}
				this.$Request.get("/app/fastOrder/selectFastOrderDetails", data).then(res => {
					if (res.code == 0) {
						this.order = res.data
						if (this.order.templateType == 2) {
							if (this.order.serviceType) {
								let serviceType = ''
								serviceType = this.order.serviceType.split('~')
								this.order.serviceType = serviceType[0]
							}
						}
						this.loading = false
					}
					uni.stopPullDownRefresh();
					uni.hideLoading();
				});
			},
			//取消抢单
			// deteOrder() {
			// 	let that = this
			// 	let data = {
			// 		fastOrderId: that.fastOrderId
			// 	}
			// 	that.$Pmessage({
			// 		showModel: true,
			// 		title: '抢单提示',
			// 		content: '确认抢单吗?',
			// 		maskcloseAble: false
			// 	}, {
			// 		emitBtn:()=> { //确认
			// 			that.$Request.postJson("/app/fastOrder/cancelFastOrderTake", data).then(
			// 				ret => {
			// 					if (ret.code == 0) {
			// 						// that.show = true
			// 						uni.navigateBack()
			// 					} else {
			// 						uni.showToast({
			// 							title: res.msg,
			// 							icon: 'none'
			// 						})
			// 					}
			// 				});
			// 		},
			// 		emitClose:()=> { //取消
			// 			console.log('用户点击取消');
			// 		},
			// 	});
			// }
		}
	}
</script>

<style>
	page {
		background: #1A1728;
	}

	.bg {
		background: #291B3D;
	}

	.dingw {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 80rpx;
		z-index: 9;
	}

	.btn {
		background: #AC75FE;
		color: #fff;
		border-radius: 16upx;
		/* flex: 1; */
		padding: 25rpx 105rpx;
		font-weight: bold;
		font-size: 32upx;
		width: 100%;
		text-align: center;

	}

	.u-mode-center-box {
		background-color: #111224 !important;
	}

	.popuptit {
		width: 395rpx;
		margin: 30rpx auto;
		font-size: 28upx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #B8B8CC;
	}

	.btn1 {
		background: #AC75FE;
		color: #fff;
		/*width: 55upx;
		margin: 0 auto; */
		padding: 15rpx 60rpx;
		display: inline-block;
		border-radius: 33rpx;
		margin-top: 10rpx;
	}
</style>
