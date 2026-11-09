<template>
	<view class="u-skeleton" style="padding-bottom: 200rpx;">
		<view class="padding">
			<view class="padding bg radius u-skeleton-fillet">
				<view class=" u-flex">
					<view class="u-m-r-10">
						<u-avatar :src="order.avatar" mode="square" size="100"></u-avatar>
					</view>
					<view class="u-flex-1 text-white margin-left-xs">
						<view class="u-font-16  text-bold">{{order.userName}}</view>
						<view class="u-font-14 margin-top-sm u-tips-color flex justify-between">
							<view class="" style="color: #FBEA74;">
								{{isVip? order.memberMoney :order.money}}币/{{order.unit}}
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="padding bg margin-top radius u-skeleton-fillet">
				<view class="flex justify-between  margin-top">
					<view class="" style="color: #FFFFFF;width: 140upx;">技能服务</view>
					<view class="text-white">{{order.gameName}}</view>
				</view>
				<view class="flex justify-between  margin-top">
					<view class="" style="color: #FFFFFF;width: 140upx;">购买数量</view>
					<u-number-box v-model="value" :min='1' bg-color="#362f5b" color="#fff" @change="valChange">
					</u-number-box>
				</view>
				<view class="flex justify-between  margin-top" v-if="yhqSelect != '否'">
					<view class="" style="color: #FFFFFF;width: 140upx;">优惠券</view>
					<view class="text-white" @tap="getYhQList">{{couponName}}</view>
				</view>
				<view class="flex justify-between  margin-top">
					<view class="" style="color: #FFFFFF;">单价</view>
					<view class="text-white">{{order.money}}币/{{order.unit}}</view>
				</view>
				<view class="flex justify-between  margin-top" v-if="isVip">
					<view class="" style="color: #FFFFFF;">会员价</view>
					<view class="text-white">{{order.memberMoney}}币/{{order.unit}}</view>
				</view>
			</view>
			<view class="padding bg margin-top radius u-skeleton-fillet">
				<view class="flex justify-between">
					<view class="" style="color: #FFFFFF;margin-right: 20upx;">备注说明</view>
					<textarea v-model="remark" class="text-white text-df flex-sub" placeholder="简单描述下您的要求"
						placeholder-style="text-align: right;" style="height: 200upx;text-align: right;"></textarea>
				</view>
			</view>
		</view>
		<view class="margin-lr">
			<u-checkbox-group>
				<u-checkbox @change="checkboxChange" v-model="check" name=1 shape="circle" active-color="#ac75fe">
					<view class="text-sm flex align-center flex-wrap" style="color: #5F6285;">
						本人已成年，已知晓禁止未成年人下单
					</view>
				</u-checkbox>
			</u-checkbox-group>
		</view>
		<uni-popup ref="popusLingJuan" type="bottom">
			<view class="bg"
				style="width: 100%;height: 1000rpx;border-top-left-radius: 20upx;border-top-right-radius: 20upx;padding: 40upx;z-index: 999;">
				<view style="width: 100%;text-align: center;font-size: 32rpx;color: #ac75fe;">优惠券明细</view>
				<scroll-view scroll-y="true" style="height: 900rpx;">
					<view style="width: 100%;text-align: center;" v-for='(item,index) in CouponIssueList' :key='index'>
						<view class="padding-tb-sm"
							style="background: #362f5b;width: 100%;border-radius: 10rpx;margin-top: 20rpx;">
							<view class="flex align-center " style="color: #FFFFFF;">
								<view
									style="margin-left: 0rpx;font-size: 40rpx;color: #FFFFFF;font-weight: 600;width: 150rpx;">
									<text style="font-size: 20upx;">￥</text>{{item.lessMoney}}
								</view>
								<view style="margin-left: 20rpx;text-align: left;width: 300rpx;">
									<view class="text-26">{{item.couponName}}(满{{item.minMoney}}可用)</view>
									<view class="margin-top-xs text-sm">截止：{{item.failureTime}}</view>
								</view>
								<view
									style="height: 105rpx;width: 2rpx;background: #FFFFFF;margin-left: 20rpx;margin-top: 15rpx;">
								</view>
								<view
									style="color: #ac75fe;line-height: 130rpx;height: 130rpx;width: 145rpx;font-weight: 600;"
									@tap='lingJuan(item)'>立即使用</view>
							</view>
						</view>
					</view>
				</scroll-view>
			</view>
		</uni-popup>
		<view class="tabber">
			<view>
				<!-- 实付：<text style="font-size: 38upx;margin-top: 8upx;">{{price}}币</text> -->
				实付：<text style="font-size: 38upx;margin-top: 8upx;">{{orderMoney}}币</text>
			</view>
			<view class="">
				<u-button :custom-style="customStyle" @click="pay()" shape="circle" :hair-line="false"
					hover-class="none" :disabled="disabled">立即支付</u-button>
			</view>
		</view>
		<u-skeleton :loading="loading" :animation="true" elColor='#1E1F31' bgColor='#111224'></u-skeleton>

		<popup v-if="popupshow" :show="popupshow" @closeT="colse" @sureT="sureT" :content="content" @close="colse">
		</popup>
		<popup v-if="popupshow1" :show="popupshow1" @closeT="colse" @sureT="sureTs" :content="content" @close="colse"
			:confirmName="confirmName"></popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import uPopu from '../../../components/uni-popup/uni-popup.vue';
	export default {
		components: {
			uPopu,
			popup
		},
		data() {
			return {
				popupshow1: false,
				popupshow: false,
				content: '',
				payType: 0,
				confirmName: '确定',


				yhqSelect: '否',
				orderMoney1: 0,
				orderMoney: '',
				myOrderMoney: 0, //订单原始价格
				CouponIssueList: [],
				couponName: '请选择优惠券',
				couponUserId: 0,
				lessMoney: 0,
				loading: true, // 是否显示骨架屏组件
				customStyle: {
					width: '340upx',
					color: '#FFFFFF',
					background: "#ac75fe",
					border: 0
				},
				page: 1,
				size: 15,
				value: 1,
				remark: '',
				id: '',
				order: {},
				isVip: false,
				fastOrderId: 0,
				fastOrderTakeId: '',
				check: false,
				disabled: false
			}
		},
		onLoad(option) {
			this.yhqSelect = this.$queue.getData('yhqSelect');
			console.log(option)

			this.id = option.id
			if (option.fastOrderId) {
				this.fastOrderId = option.fastOrderId
				this.fastOrderTakeId = option.fastOrderTakeId
			} else {
				this.fastOrderId = 0
				this.fastOrderTakeId = ''
			}

			if (option.num) {
				this.value = Number(option.num)
			}
			this.getIsVip()

			// this.isVip = uni.getStorageSync('isVIP')
		},
		onShow() {
			this.payType = 0
		},
		methods: {
			colse() {
				this.payType = 0
				this.popupshow = false
				this.popupshow1 = false
			},
			lingJuan(item) {
				var money = item.lessMoney;
				if (parseFloat(this.myOrderMoney) >= parseFloat(money)) {
					this.couponUserId = item.couponUserId;
					this.couponName = item.couponName + '-' + item.lessMoney + '币';
					this.lessMoney = item.lessMoney;
					this.orderMoney = this.numFilter(this.myOrderMoney * this.value - item.lessMoney);
					this.$refs.popusLingJuan.close();
				} else {
					this.$queue.showToast('您不满足使用此劵的资格');
				}
			},
			numFilter(value) {
				// 截取当前数据到小数点后三位
				let tempVal = parseFloat(value).toFixed(2)
				// let realVal = tempVal.substring(0, tempVal.length - 1)
				return tempVal
			},
			getYhQList() {
				this.loadQuan();
				this.$refs.popusLingJuan.open();
			},
			loadQuan() {
				let userId = this.$queue.getData('userId');
				this.$Request.getT('/app/selfCouponUser/userList?page=0&size=50&userId=' + userId).then(res => {
					if (res.status === 0) {
						this.CouponIssueList = res.data.content
					}
				});

			},
			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
						this.isVip = res.data
						this.getDet()
					}
				});
			},
			checkboxChange(e) {

			},
			// 详情
			getDet() {
				this.$Request.get("/app/orderTaking/queryTakingDetails", {
					id: this.id
				}).then(res => {
					if (res.code == 0) {
						let orderMoney = this.isVip ? res.data.memberMoney : res.data.money
						this.orderMoney = parseFloat(orderMoney * this.value).toFixed(2)
						this.myOrderMoney = parseFloat(orderMoney * this.value).toFixed(2)
						this.orderMoney1 = orderMoney
						// let price = this.isVip ? this.order.memberMoney : this.order.money
						// console.log(price * 1 * this.value)
						// return (price * this.value).toFixed(2)
						this.order = res.data
						this.loading = false;
					} else {
						this.payType = 0
						this.content = res.msg
						this.popupshow = true

					}
				});
			},
			valChange(e) {
				this.value = e.value
				// this.couponName = '请选择优惠券';
				// this.couponUserId = 0;
				if (this.couponUserId != 0) {
					this.orderMoney = ((this.myOrderMoney * this.value) - this.lessMoney).toFixed(2);
				} else {
					this.orderMoney = (this.myOrderMoney * this.value).toFixed(2)
				}
			},
			pay() {
				if (!this.check) {
					uni.showToast({
						title: '请勾选本人已成年，已知晓禁止未成年人下单',
						icon: 'none'
					})
					return
				}
				this.payType = 1
				this.popupType = false
				this.content = '确认支付' + this.orderMoney + '柚币吗?'
				this.popupshow = true

			},
			sureTs() {
				uni.navigateTo({
					url: '/my/wallet/index?index=1'
				})
				this.popupshow1 = false

			},
			sureT() {
				let that = this
				uni.showLoading({
					title: '支付中'
				})
				// that.disabled = true
				that.popupshow = false
				console.log(that.payType, 'that.popupType')
				// return
				if (that.payType == 1) {
					let data
					if (that.fastOrderId == 0) {
						data = {
							id: that.order.id,
							type: 1,
							orderNumber: that.value,
							remarks: that.remark,
							couponId: that.couponUserId,
						}

					} else {
						data = {
							id: that.order.id,
							type: 1,
							orderNumber: that.value,
							remarks: that.remark,
							couponId: that.couponUserId,
							fastOrderId: that.fastOrderId,
							fastOrderTakeId: that.fastOrderTakeId
						}
					}
					that.$Request.get("/app/orders/generateOrder", data).then(ret => {
						if (ret.code == 0) {
							that.$Request.post("/app/orders/payMoney", {
								ordersId: ret.data.ordersId,
							}).then(res => {
								uni.hideLoading();
								if (res.code == 0) {
									uni.showToast({
										title: '支付成功'
									})
									setTimeout(function() {
										uni.redirectTo({
											url: '/my/order/index'
										})
									}, 1000)

								} else {
									// uni.showToast({
									// 	title: res.msg,
									// 	icon: 'none'
									// })
									that.content = res.msg
									that.popupshow1 = true
									that.confirmName = '去充值'
								}
							});
						}

					});

				} else if (that.payType == 0) {
					uni.navigateBack()
					that.popupshow = false
				}

			}
		},
		computed: {
			price() {
				let price = this.isVip ? this.order.memberMoney : this.order.money
				console.log(price * 1 * this.value)
				return (price * this.value).toFixed(2)
			}
		}
	}
</script>

<style>
	textarea::-webkit-input-placeholder {
		color: red;

	}

	::v-deep .textMoney {
		text-align: center;
	}

	.tabber {
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: space-between;
		background-color: #291B3D;
		padding: 25rpx 30rpx;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 9;

		/* text-white flex justify-between cu-bar foot bg padding-lr */
	}
</style>
