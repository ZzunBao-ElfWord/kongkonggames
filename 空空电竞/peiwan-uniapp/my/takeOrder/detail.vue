<template>
	<view class="padding">
		<view class="bg padding radius">
			<view class=" u-flex " @click="goNav('')" v-if="order.ordersUser">
				<view class="u-m-r-10">
					<u-avatar :src="order.ordersUser.avatar" size="68"></u-avatar>
				</view>
				<view class="u-flex-1 text-white">
					<view class="text-lg  text-bold">{{order.ordersUser.userName}}</view>
				</view>
				<view>
					<!-- <image src="../static/right.png" style="width: 16rpx;height: 28rpx;" ></image> -->
				</view>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;width: 120upx;">技能服务</view>
				<view class="text-white">{{order.game.gameName}}</view>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;width: 120upx;">购买数量</view>
				<view class="text-white">×{{order.orderNumber}}</view>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;width: 120upx;">单价</view>
				<view class="text-white">{{order.orderTaking?order.orderTaking.money:''}}
					<image src="../static/jinbi.png" style="width: 27rpx;height: 27rpx;margin: 0rpx 5rpx;">
					</image>/{{order.orderTaking?order.orderTaking.unit:''}}
				</view>
			</view>
			<view class="flex justify-between margin-top" v-if="order.couponMoney">
				<view style="color: #FFFFFF;width: 120upx;">优惠券</view>
				<view class="text-white">-￥{{order.couponMoney}}</view>
			</view>
			<!-- <view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;width: 120upx;">会员优惠</view>
				<view class="text-white">{{youhui}}</view>
			</view> -->
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;margin-right: 20upx;">备注说明</view>
				<textarea class="text-white flex-sub" style="height: 100upx;" v-model="order.remarks"></textarea>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;" @click="goMsg(order)">
					<u-icon name="chat" size="30" color="#AC75FE" class="margin-right-sm"></u-icon>联系TA
				</view>
				<view class="text-white">实付：<text class="text-lg text-bold">{{order.payMoney}}</text>
					<image src="../static/jinbi.png" style="width: 27rpx;height: 27rpx;margin: 0rpx 5rpx;">
					</image>
				</view>
			</view>
		</view>
		<view class="bg padding radius margin-top">
			<view class="text-lg text-white">订单信息</view>
			<view class="flex justify-between margin-top-xl">
				<view style="color: #FFFFFF;width: 120upx;">订单编号</view>
				<view class="text-white flex align-center">{{order.ordersNo}}
					<image src="../static/copy.png" style="width: 45rpx;height: 45rpx;margin-left: 5upx;"
						@click.stop="copyClick(order.ordersNo)"></image>
				</view>
			</view>
			<view class="flex justify-between margin-top">
				<view style="color: #FFFFFF;width: 120upx;">下单时间</view>
				<view class="text-white">{{order.createTime}}</view>
			</view>
		</view>
		<!-- 	<view class="flex justify-between margin-top-xl" >
			<u-button @click="cancelOrder(order,3)" class="margin-top" :custom-style="customStyle" :hair-line="false" v-if="order.state == 1">取消订单
			</u-button>
			<u-button @click="cancelOrder(order,4)" class="margin-top" :custom-style="customStyle2" :hair-line="false" v-if="order.state == 1">开始服务
			</u-button>
			<u-button v-if="order.state == 4" :custom-style="customStyle" shape="circle"
				@click="cancelOrder(item,2)">完成订单</u-button>
		</view> -->
		<!-- 支付弹框 -->
		<!-- 	<u-popup v-model="showpay" mode="bottom" :closeable="closeable" border-radius="14" close-icon="close-circle"
			close-icon-color="#ffffff" close-icon-size="48">
			<view class="popup_pay">
				<view class="text-bold padding-top margin-lr" style="font-size: 38upx;">支付方式</view>
				<view class="margin-top margin-lr margin-bottom-sm">
					<view class="flex align-center justify-between padding-tb-sm" style="height: 100upx;"
						v-for="(item,index) in openList" :key='index'>
						<image :src="item.image" style="width: 44upx;height: 44upx;">
						</image>
						<view class="text-white flex align-end" style="width: 70%;">
							<view class="text-lg text-bold margin-right-xs">{{item.text}}</view>
							<view v-if="item.tit" class="text-sm">{{item.tit}}</view>
						</view>
						<radio-group name="openWay" style="margin-left: 45upx;" @tap.stop='selectWay(item)'>
							<label class="tui-radio">
								<radio :checked="openWay === item.id ? true : false" color="#D0B391" />
							</label>
						</radio-group>
					</view>
				</view>
				<view class="pay_btn" @click="pay()">确认支付</view>
			</view>
		</u-popup> -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				openList: [],
				openWay: 1,
				closeable: true,
				showpay: false,
				customStyle: {
					backgroundColor: '#27283C',
					color: '#FFFFFF',
					border: 0,
					width: "330upx",
					margin: 0
				},
				customStyle2: {
					backgroundColor: '#2087FE',
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
				youhui: 0,
				userId: ''
			}
		},
		onLoad(e) {
			this.userId = uni.getStorageSync('userId')
			this.isTrue = e.isTrue
			if (this.isTrue) {
				uni.setNavigationBarTitle({
					title: '订单详情'
				})
			}
			this.id = e.id
			this.getOrder()
			// #ifdef MP-WEIXIN
			this.openList = [{
					image: '../../static/images/my/icon_weixin.png',
					text: '微信',
					id: 2
				}, {
					image: '../../static/images/index/shuijing.png',
					text: '水晶支付',
					id: 1,
					tit: '*当前余额不足点击去充值'
				}],
				this.openWay = 1;
			// #endif
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
			//支付选择
			selectWay: function(item) {
				console.log(item.id)
				this.openWay = item.id;
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
					} else {
						uni.showToast({
							title: res.msg,
							icon: "none"
						})
					}
				})
			},
			// Orderpay() {
			// 	this.showpay = true
			// },
			// 支付订单
			// pay() {
			// 	let that = this
			// 	if (that.openWay == 1) { //水晶支付
			// 		that.showpay = false
			// 		that.$Request.post("/app/orders/payMoney", {
			// 			ordersId: that.order.ordersId,
			// 		}).then(res => {
			// 			if (res.code == 0) {
			// 				uni.showToast({
			// 					title: '支付成功'
			// 				})
			// 				setTimeout(function() {
			// 					uni.navigateBack()
			// 				}, 1000)
			// 			} else {
			// 				uni.showToast({
			// 					title: res.msg,
			// 					icon: 'none'
			// 				})
			// 			}
			// 		});

			// 	} else if (that.openWay == 2) { //微信支付
			// 		that.showpay = false
			// 		that.$Request.postT("/app/wxPay/wxPayOrders", {
			// 			ordersId: that.order.ordersId,
			// 			type: 3
			// 		}).then(ret => {
			// 			console.log('------------', ret)
			// 			if (ret.code == 0) {
			// 				console.log('------------')
			// 				uni.requestPayment({
			// 					provider: 'wxpay',
			// 					timeStamp: ret.data.timestamp,
			// 					nonceStr: ret.data.noncestr,
			// 					package: ret.data.package,
			// 					signType: ret.data.signType,
			// 					paySign: ret.data.sign,
			// 					success: function(rea) {
			// 						console.log(rea)
			// 						uni.showToast({
			// 							title: '支付成功',
			// 							icon: 'none'
			// 						});
			// 						uni.navigateBack()
			// 					},
			// 					fail: function(err) {
			// 						that.$queue.showToast(
			// 							'支付失败');
			// 					}
			// 				});

			// 			} else {
			// 				uni.showToast({
			// 					title: res.msg,
			// 					icon: 'none'
			// 				})
			// 			}
			// 		});
			// 	}

			// },
			// 取消订单
			// cancelOrder(e, status) {
			// 	let that = this
			// 	let content = ''
			// 	if (status == 3) {
			// 		content = '确认取消订单吗？'
			// 	} else if (status == 2) {
			// 		content = '确定订单已经完成吗？如果未完成，客户投诉将采取封号处理'
			// 	} else if (status == 4) {
			// 		content = '确认开始服务订单吗？'
			// 	} else if (status == 5) {
			// 		content = '确认取消订单吗？'
			// 	}
			// 	that.$Pmessage({
			// 		showModel: true,
			// 		title: '提示',
			// 		content: content,
			// 		maskcloseAble: false
			// 	}, {
			// 		emitBtn:()=> { //确认
			// 			//检查订单状态
			// 			that.$Request.get('/app/orders/queryOrders', {
			// 				id: e.ordersId
			// 			}).then(res => {
			// 				if (res.code == 0) {
			// 					let order = res.data
			// 					switch (order.state) {
			// 						case '1': //待服务
			// 							that.changeOrderState(e.ordersId, status);
			// 							break;
			// 						case '2': //完成了
			// 							that.$queue.showToast('很抱歉订单已完成啦，无法操作')
			// 							that.mescroll.resetUpScroll()
			// 							break;
			// 						case '3': //取消了
			// 							that.$queue.showToast('很抱歉订单已被取消，无法操作')
			// 							// that.changeOrderState(e.ordersId, status);
			// 							that.mescroll.resetUpScroll()
			// 							break;
			// 						case '4': //进行中
			// 							// that.$queue.showToast('很抱歉订单已被取消，无法操作')
			// 							that.changeOrderState(e.ordersId, status);
			// 							// that.mescroll.resetUpScroll()
			// 							break;
			// 						default:
			// 							that.mescroll.resetUpScroll()
			// 							break;
			// 					}

			// 				}
			// 			})
			// 		},
			// 		emitClose:()=> { //取消
			// 			console.log('用户点击取消');
			// 			that.mescroll.resetUpScroll()
			// 		},
			// 	});

			// },
			//统一处理订单状态
			// changeOrderState(id, status) {
			// 	let that = this;
			// 	let data = {
			// 		id,
			// 		status
			// 	}
			// 	that.$Request.get('/app/orders/cancelOrder', data).then(res => {
			// 		if (res.code == 0) {
			// 			// that.mescroll.resetUpScroll()
			// 			uni.navigateBack()
			// 		} else {
			// 			that.$queue.showToast(res.msg)
			// 		}
			// 	})
			// },
			// 取消订单
			// cancelOrder(e) {
			// 	let data = {
			// 		id: e.ordersId,
			// 		status: '3'
			// 	}
			// 	this.$Request.get('/app/orders/cancelOrder', data).then(res => {
			// 		if (res.code == 0) {
			// 			uni.showToast({
			// 				title: '取消成功',
			// 				icon: 'none'
			// 			})
			// 			setTimeout(function() {
			// 				uni.navigateBack()
			// 			}, 1000)
			// 		} else {
			// 			uni.navigateBack()
			// 		}
			// 	})
			// },
			goMsg(options) {
				console.log(options)
				let data = {
					userId: this.userId,
					focusedUserId: options.userId
				}
				this.$Request.postJson('/app/chat/insertChatConversation', data).then(res => {
					if (res.code == 0) {
						let id = this.userId == res.data.userId ? res.data.focusedUserId : this.userId
						uni.navigateTo({
							// url: '/pages/msg/im?chatConversationId=' + res.data.chatConversationId +
							// 	'&byUserId=' + id
							url: '/pages/msg/chat?chatConversationId=' + res.data.chatConversationId +
								'&byUserId=' + id + '&byNickName=' + options.ordersUser.userName
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
		background-color: #291B3D;
	}

	.popup_pay {
		width: 100%;
		padding-bottom: 45rpx;
	}

	.pay_btn {
		width: 90%;
		margin: 0 auto;
		text-align: center;
		/* background: linear-gradient(94deg, #E6CCA7 0%, #D0B391 100%); */
		background-color: #AC75FE;
		height: 80rpx;
		border-radius: 16rpx;
		color: #FFFFFF;
		line-height: 80rpx;

	}

	.u-drawer-bottom {
		background-color: #2F3154 !important;
	}
</style>