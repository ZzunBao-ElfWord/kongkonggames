<template>
	<view>
		<view class="">
			<view class="margin-lr margin-top" style="position: relative;border-radius: 10upx;overflow: hidden;">
				<image src="../static/vip.png" style="width: 100%;height: 250rpx;"></image>
				<view class=" u-flex u-p-l-30 u-p-t-80 u-p-b-30" style="position: absolute;top: 0;width: 100%;">
					<view class="u-m-r-20">
						<u-avatar :src="avatar" size="100"></u-avatar>
					</view>
					<view class="u-flex-1 ">
						<view class="u-font-18 text-white text-bold">{{userName}}</view>

						<view class="text-26 u-m-t-10 u-tips-color" style="color: #FFF;" v-if="isVip==1 && isVipEnd">
							到期时间:{{isVipEnd}}
						</view>
						<view class="u-font-14 u-m-t-10 u-tips-color" style="color: #FFF;" v-else>您目前还未开通会员
						</view>
					</view>
				</view>
			</view>
			<view class="flex margin-top-sm flex-wrap padding-lr-sm">
				<view v-for="(item,index) in vipList" :key='index' @click="select(item)" class=""
					style="display: inline-block;width: 216rpx;height: 250rpx;margin: 10rpx 10rpx;">
					<view class="text-center flex flex-direction justify-between padding-tb radius"
						:class="selNum == item.id?'active': ''"
						style="color: #dfc5a7;width: 216rpx;height: 250rpx;background:#291B3D;border: 1px; ">
						<view class="text-bold">{{item.vipName}}</view>
						<view class="text-bold">￥<text class="text-xxl">{{item.cashMoney}}</text></view>
						<view class="text-sm">立即购买</view>
					</view>
				</view>
			</view>
			<view class="pay-way-box margin-lr margin-top-sm">
				<view class="pay-way-label">支付方式</view>
				<view class="pay-way-value">
					<image src="../static/zhifubao.png" class="pay-way-icon"></image>
					<text>支付宝</text>
					<text class="pay-way-check">✓</text>
				</view>
			</view>
			<view class="vipbox">
				<view class="text-center text-xl text-bold " style="color:#dfc5a7;">会员特权</view>
				<view class="flex  flex-wrap">
					<view v-for="(item,index) in MemberList" :key="index"
						style="width: 33%;text-align: center;margin-top: 34upx;">
						<image :src="item.memberImg" mode="" style="margin: 0 auto;height: 45rpx;width: 45rpx;"></image>
						<view class="grid-text margin-top-sm" style="color: #dfc5a7;">{{item.memberName}}</view>
					</view>
				</view>
			</view>
		</view>
		<view style="height: 110rpx;"></view>
		<view class="flex justify-between cu-bar foot bg padding-lr">
			<view style="color: #AC75FE;">
				应付：￥<text style="font-size: 38upx;margin-top: 8upx;margin-right: 2upx;">{{price}}</text>
			</view>
			<view class="">
				<u-button :custom-style="customStyle" @click="pay()" :loading="paying" shape="circle"
					:hair-line="false">{{isVip==1?'立即续费':'立即开通'}}</u-button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				paying: false,
				cashPriceMap: {
					'0': 28,
					'1': 68,
					'2': 168
				},
				customStyle: {
					width: '250rpx',
					color: '#FFFFFF',
					background: "#AC75FE",
					border: 0,
					fontWeight: '700'
				},
				gridData: [{
						title: '专享折上折',
						image: '../static/1.png'
					},
					{
						title: '特权礼物',
						image: '../static/2.png'
					},
					{
						title: '身份标识',
						image: '../static/3.png'
					},
					{
						title: '超值专享券',
						image: '../static/4.png'
					},
					{
						title: '商家特权',
						image: '../static/5.png'
					},
					{
						title: '定制挂件',
						image: '../static/6.png'
					}
				],
				avatar: '',
				userName: '匿名',
				vipList: [],
				selNum: '',
				price: 0,
				MemberList: [],
				isVip: 1,
				isVipEnd: ''
			}
		},
		onLoad() {
			this.avatar = uni.getStorageSync('avatar')
			this.userName = uni.getStorageSync('userName')
			this.isVip = 1
			this.getVipList()
			this.getMemberList()
			this.getIsVip()
			this.getUserInfo()
		},
		methods: {
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.avatar = res.data.avatar ? res.data.avatar : '../../static/logo.png'

						uni.setStorageSync('avatar', res.data.avatar)
					}
				});
			},
			//获取VIP列表
			getVipList() {
				this.$Request.get('/app/VipDetails/selectVipDetails').then(res => {
					if (res.code == 0) {
						this.vipList = (res.data || []).map(item => Object.assign({}, item, {
							cashMoney: this.getVipCashPrice(item)
						}))
						if (this.vipList.length) {
							this.price = this.vipList[0].cashMoney
							this.selNum = this.vipList[0].id
						}
					}
				})
			},
			// 获取特权列表
			getMemberList() {
				this.$Request.get('/app/member/selectMemberList').then(res => {
					if (res.code == 0) {
						this.MemberList = res.data
					}
				})
			},
			getIsVip() {
				this.$Request.get("/app/UserVip/selectUserVip").then(res => {
					if (res.code == 0) {
						if (res.data && res.data.isVip) {
							this.isVip = res.data.isVip
						} else {
							this.isVip = -1
						}

						this.isVipEnd = res.data.endTime
					}
				});
			},
			getVipCashPrice(vip) {
				return this.cashPriceMap[String(vip.vipNameType)] || 0
			},
			select(e) {
				this.selNum = e.id
				this.price = e.cashMoney
			},
			pay() {
				if (!this.selNum) {
					uni.showToast({
						title: '请选择开通会员类型？',
						icon: 'none'
					})
					return
				}
				if (this.paying) return
				this.paying = true
				uni.showLoading({ title: '正在创建订单...' })
				this.$Request.get('/app/orders/generateOrder', {
					id: this.selNum,
					type: '2'
				}).then(res => {
					if (!res || res.code != 0 || !res.data || !res.data.ordersId) {
						throw new Error((res && res.msg) || '创建会员订单失败')
					}
					return this.startAlipay(res.data.ordersId)
				}).catch(err => {
					uni.hideLoading()
					this.paying = false
					uni.showToast({ title: err.message || '支付失败', icon: 'none' })
				})
			},
			startAlipay(ordersId) {
				let supported = false
				// #ifdef APP-PLUS
				supported = true
				return this.$Request.post('/app/aliPay/payVipApp', { ordersId }).then(ret => {
					if (!ret || ret.code != 0 || !ret.data) {
						throw new Error((ret && ret.msg) || '获取支付宝支付信息失败')
					}
					uni.hideLoading()
					return new Promise((resolve, reject) => {
						uni.requestPayment({
							provider: 'alipay',
							orderInfo: ret.data,
							success: resolve,
							fail: reject
						})
					}).then(() => {
						this.paymentSucceeded()
					})
				})
				// #endif

				// #ifdef H5
				supported = true
				return this.$Request.post('/app/aliPay/payVipOrder', { ordersId }).then(ret => {
					if (!ret || ret.code != 0 || !ret.data) {
						throw new Error((ret && ret.msg) || '获取支付宝支付信息失败')
					}
					const div = document.createElement('div')
					div.innerHTML = ret.data
					document.body.appendChild(div)
					div.querySelector('form').submit()
				})
				// #endif

				if (!supported) {
					return Promise.reject(new Error('当前平台暂不支持支付宝会员支付'))
				}
			},
			paymentSucceeded() {
				uni.hideLoading()
				this.paying = false
				uni.showToast({ title: '支付成功，会员正在到账', icon: 'success' })
				setTimeout(() => {
					this.getIsVip()
				}, 1500)
			}
		},
		computed: {

		}
	}
</script>

<style>
	.bg {
		background-color: #291B3D;
	}

	.btn {
		width: 100%;
		height: 88upx;
		/* background: linear-gradient(0deg, #af8262 0%, #cab49c 100%); */
		background: #AC75FE;
		border-radius: 44upx;
		text-align: center;
		line-height: 88upx;
		margin-top: 40upx;
		font-size: 34upx;
		font-weight: 600;
		color: #FFFFFF;
	}

	.active {
		/* border: 1px solid #cab49c !important; */
		background: linear-gradient(-90deg, #552775, #28183e) !important;
		border: 1rpx solid #ac75fe !important;
		/* border-radius: ; */
	}

	.vipbox {
		padding: 30rpx 0;
		border-radius: 24rpx;
		margin: 30rpx 30rpx;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/22/4954b77fd8b51a4e80da4f1c3d6d267e.png');
		background-size: 100% 100%;
		background-repeat: no-repeat;
	}

	.pay-way-box {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 24rpx 28rpx;
		border-radius: 16rpx;
		background: #291B3D;
		color: #FFFFFF;
	}

	.pay-way-label {
		font-size: 28rpx;
		color: #BCB2D4;
	}

	.pay-way-value {
		display: flex;
		align-items: center;
		font-size: 28rpx;
	}

	.pay-way-icon {
		width: 44rpx;
		height: 44rpx;
		margin-right: 12rpx;
	}

	.pay-way-check {
		display: inline-flex;
		align-items: center;
		justify-content: center;
		width: 38rpx;
		height: 38rpx;
		margin-left: 18rpx;
		border-radius: 50%;
		background: #AC75FE;
		color: #FFFFFF;
		font-size: 24rpx;
	}
</style>
