<template>
	<view class="padding">
		<view class="bg padding radius">
			<view class=" u-flex " @click="goNav('')" v-if="order.user">
				<view class="u-m-r-10">
					<u-avatar :src="order.user.avatar" size="68"></u-avatar>
				</view>
				<view class="u-flex-1 text-white">
					<view class="u-font-14  text-bold">{{order.user.userName}}</view>
				</view>
				<view>
					<!-- <image src="../static/right.png" style="width: 16rpx;height: 28rpx;" ></image> -->
				</view>
			</view>
			<view class="flex justify-between margin-top" v-if="order.game.gameName">
				<view style="color: #FFFFFF;width: 120upx;">技能服务</view>
				<view class="text-white">{{order.game.gameName}}</view>
			</view>
			<view class="flex justify-between margin-top" v-if="order.orderNumber">
				<view style="color: #FFFFFF;width: 120upx;">购买数量</view>
				<view class="text-white">×{{order.orderNumber}}</view>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;width: 120upx;">单价</view>
				<view class="text-white">
					{{order.orderTaking?order.orderTaking.money:''}}币/{{order.orderTaking?order.orderTaking.unit:''}}
				</view>
			</view>
			<view class="flex justify-between margin-top" v-if="order.couponMoney">
				<view style="color: #FFFFFF;width: 120upx;">优惠券</view>
				<view class="text-white">-￥{{order.couponMoney}}</view>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;width: 120upx;">会员优惠</view>
				<view class="text-white">{{youhui}}</view>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;margin-right: 20upx;">备注说明</view>
				<textarea class="text-white flex-sub" style="height: 100upx;" v-model="order.remarks"></textarea>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;" @click="goMsg">
					<u-icon name="chat" size="30" color="#FBCA0C" class="margin-right-sm"></u-icon>联系TA
				</view>
				<view class="text-white">实付：<text class="text-lg text-bold">{{order.payMoney}}币</text></view>
			</view>
		</view>
		<view class="bg padding radius margin-top">
			<view class="text-lg text-white">订单信息</view>
			<view class="flex justify-between margin-top-xl">
				<view style="color: #FFFFFF;width: 120upx;">订单编号</view>
				<view class="text-white  flex align-center">{{order.ordersNo}}
					<image src="../static/copy.png" style="width: 45rpx;height: 45rpx;margin-left: 5upx;"
						@click.stop="copyClick(order.ordersNo)"></image>
				</view>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;width: 120upx;">下单时间</view>
				<view class="text-white">{{order.createTime}}

				</view>
			</view>
		</view>
		<view class="flex justify-between margin-top-xl" v-if="isTrue == 0">
			<u-button @click="cancelOrder(order)" class="margin-top" :custom-style="customStyle" :hair-line="false"
				hover-class="none">取消订单
			</u-button>
			<u-button @click="pay" class="margin-top" :custom-style="customStyle2" :hair-line="false"
				hover-class="none">立即支付</u-button>
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	export default {
		components: {
			popup
		},
		data() {
			return {
				popupshow: false,
				content: '',
				// id: '',

				customStyle: {
					backgroundColor: '#1C1C22',
					color: '#FFFFFF',
					border: 0,
					width: "330upx",
					margin: 0
				},
				customStyle2: {
					backgroundColor: '#FBCA0C',
					color: '#FFFFFF',
					border: 0,
					width: "330upx",
					margin: 0
				},
				id: '',
				order: {
					user: {},
					game: {}
				},
				isTrue: 0,
				youhui: 0
			}
		},
		onLoad(e) {
			this.isTrue = e.isTrue
			if (this.isTrue) {
				uni.setNavigationBarTitle({
					title: '订单详情'
				})
			}
			this.id = e.id
			this.getOrder()
		},
		methods: {
			copyClick(copy) {
				uni.setClipboardData({
					data: copy,
					success: function(res) {
						uni.getClipboardData({
							success: function(res) {
								uni.showToast({
									title: "复制成功",
									icon: 'none',
								});
							},
						});
					},
				});
			},
			goNav(e) {
				uni.navigateTo({
					url: e
				})
			},
			getOrder() {
				let data = {
					id: this.id
				}
				this.$Request.get('/app/orders/queryOrders', data).then(res => {
					if (res.code == 0) {
						this.order = res.data
						this.youhui = (this.order.orderTaking.money * this.order.orderNumber - this.order
							.payMoney * 1).toFixed(2)
					}
				})
			},
			sureT() {
				let that = this
				that.$Request.post("/app/orders/payMoney", {
					ordersId: that.order.ordersId,
				}).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '支付成功'
						})
						that.popupshow = false
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
			// 支付订单
			pay() {
				this.content = '确认支付' + this.order.payMoney + '柚币吗?'
				this.popupshow = true

			},
			// 取消订单
			cancelOrder(e) {
				let data = {
					id: e.ordersId,
					status: '3'
				}
				this.$Request.get('/app/orders/cancelOrder', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '取消成功',
							icon: 'none'
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					} else {
						uni.navigateBack()
					}
				})
			},
			goMsg() {
				let data = {
					userId: uni.getStorageSync('userId'),
					focusedUserId: this.order.user.userId
				}
				this.$Request.postJson('/app/chat/insertChatConversation ', data).then(res => {
					if (res.code == 0) {
						let id = this.order.user.userId == res.data.userId ? res.data.focusedUserId : this.order
							.user.userId
						uni.navigateTo({
							// url: '/pages/msg/im?chatConversationId=' + res.data.chatConversationId +
							// 	'&byUserId=' + id
							url: '/pages/msg/chat?chatConversationId=' + res.data.chatConversationId +
								'&byUserId=' + id + '&byNickName=' + this.order.user.userName
						})
					} else {
						this.$queue.showToast(res.msg);
					}
				})
			},
		}
	}
</script>

<style>
	.bg {
		background-color: #101014;
	}

	::v-deep .textMoney {
		text-align: center;
	}
</style>
