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
						<view class="text-bold text-xxl">{{getVipDisplayPrice(item)}}</view>
						<view class="text-sm">立即购买</view>
					</view>
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
			<view class="apple-payment-note">
				会员购买由 Apple App Store 安全处理，付款成功后将直接开通或延长会员。
			</view>
		</view>
		<view style="height: 110rpx;"></view>
		<view class="flex justify-between cu-bar foot bg padding-lr">
			<view style="color: #AC75FE;">
				应付：<text style="font-size: 38upx;margin-top: 8upx;margin-right: 2upx;">{{selectedPrice}}</text>
			</view>
			<view class="">
				<u-button :custom-style="customStyle" @click="payAppleMembership()" :loading="iapLoading" shape="circle"
					:hair-line="false">{{isVip==1?'立即续费':'立即开通'}}</u-button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
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
				MemberList: [],
				isVip: -1,
				isVipEnd: '',
				iapChannel: null,
				iapReady: false,
				iapLoading: false,
				iapRestoring: false,
				iapProductId: '',
				appleProducts: {},
				membershipProductIds: {
					'0': '0001',
					'1': '0002',
					'2': '0003'
				},
				membershipFallbackPrices: {
					'0': '¥28',
					'1': '¥68',
					'2': '¥168'
				}
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
			// #ifdef APP-PLUS
			if (plus.os.name === 'iOS') {
				this.initAppleIap().then(() => this.restoreAppleMembershipTransactions()).catch(err => {
					console.error('初始化 Apple 会员商品失败', err)
				})
			}
			// #endif
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
					if (res.code == 0 && Array.isArray(res.data) && res.data.length) {
						this.vipList = res.data
						this.selNum = this.vipList[0].id
						this.iapProductId = this.getMembershipProductId(this.vipList[0])
					}
				})
			},
			// 获取特权列表
			getMemberList() {
				this.$Request.get('/app/member/selectMemberList').then(res => {
					if (res.code == 0) {
						const hiddenMemberNames = ['团队特权', '专属奖励']
						this.MemberList = (Array.isArray(res.data) ? res.data : []).filter(item =>
							hiddenMemberNames.indexOf(item.memberName) === -1)
					}
				})
			},
			getIsVip() {
				return this.$Request.get("/app/UserVip/selectUserVip").then(res => {
					if (res.code == 0) {
						const vip = res.data || {}
						if (vip.isVip) {
							this.isVip = vip.isVip
						} else {
							this.isVip = -1
						}
						this.isVipEnd = vip.endTime || ''
					}
				});
			},
			select(e) {
				this.selNum = e.id
				this.iapProductId = this.getMembershipProductId(e)
			},
			getMembershipProductId(vip) {
				if (!vip && !this.selNum) return ''
				const current = vip || this.vipList.find(item => item.id === this.selNum)
				return current ? (this.membershipProductIds[String(current.vipNameType)] || '') : ''
			},
			getVipDisplayPrice(vip) {
				// DCloud 在部分 iOS 版本中返回的是美元价格档位，不能直接加人民币符号。
				// 页面价格与 App Store Connect 中的中国区实际售价保持一致。
				return this.membershipFallbackPrices[String(vip.vipNameType)] || '--'
			},
			initAppleIap() {
				// #ifdef APP-PLUS
				if (plus.os.name !== 'iOS') return Promise.reject(new Error('Apple IAP 仅支持 iOS'))
				if (this.iapReady && this.iapChannel) return Promise.resolve(this.iapChannel)
				return new Promise((resolve, reject) => {
					uni.getProvider({
						service: 'payment',
						success: res => {
							const providers = res.providers || res.provider || []
							const channel = providers.find(item => item && item.id === 'appleiap')
							if (!channel) {
								reject(new Error('当前安装包未包含 Apple IAP 支付模块'))
								return
							}
							this.iapChannel = channel
							const productIds = Object.keys(this.membershipProductIds).map(key => this.membershipProductIds[key])
							channel.requestProduct(productIds, products => {
								if (!products || !products.length) {
									reject(new Error('未从 App Store 获取到会员商品'))
									return
								}
								const productMap = {}
								products.forEach(product => {
									const productId = product.productid || product.productIdentifier
									if (productId) productMap[productId] = product
								})
								this.appleProducts = productMap
								this.iapReady = true
								resolve(channel)
							}, err => reject(new Error((err && err.errMsg) || '获取 Apple 会员商品失败')))
						},
						fail: err => reject(new Error((err && err.errMsg) || '无法获取 Apple 支付通道'))
					})
				})
				// #endif
				// #ifndef APP-PLUS
				return Promise.reject(new Error('请在 iPhone 或 iPad 安装包中购买会员'))
				// #endif
			},
			requestSelectedAppleProduct(productId) {
				return new Promise((resolve, reject) => {
					if (!this.iapChannel) {
						reject(new Error('Apple 支付通道尚未初始化'))
						return
					}
					this.iapChannel.requestProduct([productId], products => {
						const product = (products || []).find(item =>
							(item.productid || item.productIdentifier) === productId)
						if (!product) {
							reject(new Error('Apple 商品 ' + productId + ' 尚未生效，请稍后重试'))
							return
						}
						this.appleProducts = Object.assign({}, this.appleProducts, {
							[productId]: product
						})
						resolve(product)
					}, err => {
						reject(new Error((err && err.errMsg) || ('无法获取 Apple 商品 ' + productId)))
					})
				})
			},
			finishAppleTransaction(transaction) {
				return new Promise((resolve, reject) => {
					this.iapChannel.finishTransaction(transaction, resolve, reject)
				})
			},
			getTransactionProductId(transaction) {
				return (transaction.payment && transaction.payment.productid) ||
					transaction.productIdentifier || this.iapProductId
			},
			verifyAppleMembershipTransaction(transaction) {
				return this.$Request.postJson('/app/appleIap/verifyMembership', {
					productId: this.getTransactionProductId(transaction),
					transactionId: transaction.transactionIdentifier,
					receiptData: transaction.transactionReceipt
				}).then(res => {
					if (!res || res.code !== 0) {
						throw new Error((res && res.msg) || '服务器验证 Apple 会员订单失败')
					}
					return this.finishAppleTransaction(transaction).then(() => res.data)
				})
			},
			restoreAppleMembershipTransactions() {
				if (!this.iapChannel || this.iapRestoring) return
				this.iapRestoring = true
				const productIds = Object.keys(this.membershipProductIds).map(key => this.membershipProductIds[key])
				this.iapChannel.restoreCompletedTransactions({
					manualFinishTransaction: true,
					username: String(uni.getStorageSync('userId') || '')
				}, transactions => {
					const pending = (transactions || []).filter(item =>
						String(item.transactionState) === '1' && productIds.indexOf(this.getTransactionProductId(item)) !== -1)
					pending.reduce((chain, transaction) => {
						return chain.then(() => this.verifyAppleMembershipTransaction(transaction)).catch(err => {
							console.error('Apple 会员补单失败', err)
						})
					}, Promise.resolve()).then(() => {
						if (pending.length) this.getIsVip()
						this.iapRestoring = false
					})
				}, err => {
					console.error('检查 Apple 会员未完成订单失败', err)
					this.iapRestoring = false
				})
			},
			async payAppleMembership() {
				if (!this.selNum) {
					uni.showToast({
						title: '请选择会员类型',
						icon: 'none'
					})
					return
				}
				this.iapProductId = this.getMembershipProductId()
				if (!this.iapProductId) {
					uni.showToast({ title: '该会员类型尚未配置 Apple 商品', icon: 'none' })
					return
				}
				if (this.iapLoading) return
				// #ifndef APP-PLUS
				uni.showToast({ title: '请在 iPhone 或 iPad 安装包中购买会员', icon: 'none' })
				return
				// #endif
				// #ifdef APP-PLUS
				if (plus.os.name !== 'iOS') {
					uni.showToast({ title: '当前设备不支持 Apple IAP', icon: 'none' })
					return
				}
				this.iapLoading = true
				uni.showLoading({ title: '正在连接 App Store...' })
				try {
					await this.initAppleIap()
					await this.requestSelectedAppleProduct(this.iapProductId)
					const transaction = await new Promise((resolve, reject) => {
						uni.requestPayment({
							provider: 'appleiap',
							orderInfo: {
								productid: this.iapProductId,
								username: String(uni.getStorageSync('userId') || ''),
								quantity: 1,
								manualFinishTransaction: true
							},
							success: resolve,
							fail: reject
						})
					})
					await this.verifyAppleMembershipTransaction(transaction)
					this.isVip = 1
					await this.getIsVip()
					uni.showToast({ title: '会员开通成功', icon: 'success' })
				} catch (err) {
					const cancelled = err && (err.errCode === 2 || err.code === 2)
					if (!cancelled) {
						uni.showToast({ title: (err && (err.message || err.errMsg)) || 'Apple 会员购买失败', icon: 'none', duration: 3000 })
					}
				} finally {
					this.iapLoading = false
					uni.hideLoading()
				}
				// #endif
			}
		},
		computed: {
			selectedPrice() {
				const vip = this.vipList.find(item => item.id === this.selNum)
				return vip ? this.getVipDisplayPrice(vip) : '--'
			}
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

	.apple-payment-note {
		margin: 0 30rpx 30rpx;
		padding: 22rpx 24rpx;
		border-radius: 16rpx;
		background: #291B3D;
		color: #BCB2D4;
		font-size: 24rpx;
		line-height: 38rpx;
	}
</style>




