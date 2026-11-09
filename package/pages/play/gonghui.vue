<template>
	<view class="container ">
		<view style="color: red;" class="margin-lr margin-top-sm" v-if="jujueContent">拒绝理由：{{jujueContent}}</view>
		<view class="earnest_money bg margin-lr margin-top flex align-center justify-center">
			<view class="text-center">
				<view v-if="consortia.length==0">
					<view class="text-lg text-bold margin-top-sm">暂未加入公会</view>
				</view>
				<view v-else>
					<image :src="consortia.consortiaImg?consortia.consortiaImg:'../../../static/logo.png'"
						mode="aspectFill" style="width: 90rpx;height: 90rpx;border-radius:80upx"></image>
					<view class="text-lg text-bold margin-top-xs">{{consortia.consortiaName}}</view>
					<view class="margin-top-xs">ID:{{consortia.consortiaNo}}</view>
				</view>
			</view>
			<view class="earnest_left" @click="getshenqing(1)" v-if="consortiaId==null">入会申请</view>
			<view class="earnest_right" @click="getshenqing1(2)" v-if="consortiaId!=null&&!Huizhang">退会申请</view>
		</view>
		<view class="rule bg margin-lr margin-top padding-lr ">
			<view class="flex align-center">
				<text class="text-lg text-bold margin-right-sm">公会规则</text>
				<text class="text-sm">公会遇到问题，</text>
				<text class="text-sm" style="color: #FBCA0C;" @click="goChat">请联系客服</text>
			</view>

			<view v-if="consortia.length!=0">

				<viwe style="color: #FFFFFF;font-size: 28upx;">{{consortia.consortiaContent}}</viwe>
			</view>
			<view v-if="consortia.length==0">
				<view class="text-lg text-bold margin-tb" v-if="content">{{ruhui}}</view>
				<view style="color: #FFFFFF;font-size: 28upx;" v-html="content"></view>
				<view class="text-lg text-bold margin-tb" v-if="tuikian">{{tuihui}}</view>
				<view style="color: #FFFFFF;font-size: 28upx;" v-html="tuikian"></view>
			</view>

		</view>


		<!-- 公会加入弹框 -->
		<u-popup v-model="show" mode="center" width="640rpx" height="400rpx" border-radius="14" :closeable="true"
			close-icon="close-circle" close-icon-size="50" close-icon-color="#CCCCCC">
			<view style="padding-top: 30rpx;">
				<view class="text-xl text-bold text-center">公会申请</view>
				<view style="padding-top: 70upx;">
					<view class="margin-lr padding-lr-sm" style="background: #101014;">
						<u-input v-model="value" type="number" placeholder="请输入公会号" maxlength="4" />
					</view>
					<view class="sure" @click="submit()">申请加入</view>
				</view>
			</view>
		</u-popup>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content1"
			:title="title"></popup>
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
				content1: '',
				title: '',

				money: 0.00,
				tit: 'FMPW-VIP',
				ruhui: '',
				content: '',
				tuihui: '',
				tuikian: '',
				renzheng: 0,
				show: false,
				safetyMoney: 0,
				userId: '',
				value: '',
				avatar: '',
				consortiaId: '',
				consortia: [],
				jujueContent: '',
				Huizhang: false,

				SelKeFu: '2',
			}
		},
		onLoad() {
			this.jiaoNa() // 公会加入
			this.TuiKuan() // 公会退出
			this.getUserInfo()

			this.userId = uni.getStorageSync('userId')

		},
		onShow() {
			this.Huizhang = uni.getStorageSync('Huizhang')
			this.SelKeFu = this.$queue.getData('SelKeFu');
		},
		methods: {
			getjujue(e) {
				let that = this
				let classify;
				if (e == null || e == '' || e == 'null') {
					classify = 1
				} else {
					classify = 2
				}
				let data = {
					classify: classify
				}
				that.$Request.getT('/app/consortia/selectOldConsortiaApplyByUserId', data).then(res => {
					if (res.code === 0 && res.data) {
						this.jujueContent = res.data.content
					}
				});
			},
			getshenqing(e) {
				let that = this
				let data = {
					classify: e
				}
				that.$Request.getT('/app/consortia/selectOldConsortiaApplyByUserId', data).then(res => {
					if (res.code === 0) {
						if (res.data == null) {
							that.show = true
						} else {
							if (res.data.status == 1) {
								uni.showToast({
									title: '入会申请已提交，审核中',
									icon: 'none'
								})
							} else if (res.data.status == 3) {
								that.show = true
							} else if (res.data.status == 2) {
								that.show = true
							}
						}

					}
				});
			},

			getshenqing1(e) {
				let that = this
				let data = {
					classify: e
				}
				that.$Request.getT('/app/consortia/selectOldConsortiaApplyByUserId', data).then(res => {
					if (res.code === 0) {
						if (res.data == null) {
							that.TuiSubmit()
						} else {
							if (res.data.consortiaId == that.consortiaId) {
								if (res.data.status == 1) {
									uni.showToast({
										title: '退会申请已提交，审核中...',
										icon: 'none'
									})
								} else if (res.data.status == 3) {
									that.TuiSubmit()
									// uni.showModal({
									// 	title: '温馨提示',
									// 	content: '退出公会申请已拒绝，拒绝内容：' + res.data.content,
									// 	success: function(ret) {
									// 		if (ret.confirm) {
									// 			console.log('用户点击确定');
									// 			that.TuiSubmit()
									// 		} else if (ret.cancel) {
									// 			console.log('用户点击取消');
									// 		}
									// 	}
									// });
								} else if (res.data.status == 2) {

									that.TuiSubmit()
								}
							} else {
								that.TuiSubmit()
							}

						}

					} else {
						that.show = false
						that.$queue.showToast(res.msg)

					}
				});
			},
			// 入会申请弹框
			join(e) {
				// this.getshenqing(e)
				this.show = true

			},
			goChat() {
				let that = this;
				if (that.SelKeFu === '1') {
					// #ifdef MP-WEIXIN
					try {
						wx.openCustomerServiceChat({
							extInfo: {
								url: that.$queue.getData('SelKeFuLink')
							},
							corpId: that.$queue.getData('SelKeFuAppId'),
							success(res) {},
							fail(res) {
								console.error(res)
							}
						})
					} catch (error) {
						console.error("catchcatch" + error)
						uni.showToast({
							title: '请更新至微信最新版本'
						});
					}
					// #endif
					// #ifndef MP-WEIXIN
					let url = that.$queue.getData('SelKeFuLink');
					if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
						uni.navigateTo({
							url
						});
					} else {
						//#ifndef H5
						uni.navigateTo({
							url: '/pages/index/webView?url=' + url
						});
						//#endif
						//#ifdef H5
						window.location.href = url;
						//#endif
					}
					// #endif
				} else if (that.SelKeFu === '2') {
					let token = that.$queue.getData('token');
					if (token) {
						uni.navigateTo({
							url: '/my/setting/chat'
						});
					} else {
						that.goLoginInfo();
					}
				} else {
					uni.navigateTo({
						url: '/my/setting/kefu'
					})
				}

			},
			copyOrder(tit) {
				let that = this;
				uni.setClipboardData({
					data: tit,
					success: function() {
						that.$queue.showToast('复制成功')
					}
				})
			},

			// 公会加入规则
			jiaoNa() {
				this.$Request.getT('/app/common/type/604').then(res => {
					if (res.code === 0 && res.data) {
						this.ruhui = res.data.min
						this.content = res.data.value;
					}
				});
			},
			//公会退出规则
			TuiKuan() {
				this.$Request.getT('/app/common/type/605').then(res => {
					if (res.code === 0 && res.data) {
						this.tuihui = res.data.min
						this.tuikian = res.data.value;
					}
				});
			},
			//加入公会
			submit() {
				if (!this.value) {
					uni.showToast({
						title: '请输入公会编号',
						icon: 'none'
					})
					return
				}
				let data = {
					userId: this.userId,
					classify: 1, //申请 1  退出2
					consortiaNo: this.value
				}
				this.$Request.postJson("/app/consortia/insertConsortiaApply", data).then(res => {
					if (res.code == 0) {
						this.show = false
						this.getUserInfo()
						uni.showToast({
							title: '公会加入申请已提交，请等待通过。',
							icon: 'none'
						})

						// uni.navigateBack()

					} else {
						// this.show = false
						this.$queue.showToast(res.msg)

					}
				});
			},
			sureT() {
				let that = this
				let data = {
					userId: that.userId,
					classify: 2, //申请 1  退出2
					consortiaId: that.consortiaId
				}
				that.$Request.postJson("/app/consortia/insertConsortiaApply", data).then(ret => {
					if (ret.code == 0) {
						that.getUserInfo()
						that.popupshow = false
						uni.showToast({
							title: '公会退出申请已提交，请等待通过。',
							icon: 'none'
						})
					} else {
						uni.showToast({
							title: ret.msg,
							icon: "none"
						})
					}

				});
			},
			//退出公会
			TuiSubmit() {
				this.title = '温馨提示'
				this.content1 = '确定退出公会？退出公会您将无法享受公会权益！'
				this.popupshow = true

			},
			// 查询公会详情
			getgonghuiDetail() {
				let data = {
					consortiaId: this.consortiaId
				}
				this.$Request.get("/app/consortia/selectConsortiaById", data).then(res => {
					if (res.code == 0) {
						this.consortia = res.data

					}
				});
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.consortiaId = res.data.consortiaId
						if (this.consortiaId) {
							this.getgonghuiDetail()
						}
						this.getjujue(this.consortiaId)
						uni.setStorageSync('consortiaId', res.data.consortiaId)

					}
				});
			},

		}
	}
</script>

<style lang="less">
	::v-deep .textMoney {
		text-align: center;
	}

	.earnest_money {
		width: 690rpx;
		height: 310rpx;
		position: relative;
		border-radius: 24rpx;
	}

	.earnest_left {
		width: 180rpx;
		height: 64rpx;
		color: #0A0A0C;
		background: #FBCA0C;
		border-radius: 0rpx 32rpx 32rpx 0rpx;
		position: absolute;
		left: 0;
		font-weight: 500;
		font-size: 28rpx;
		line-height: 64rpx;
		text-align: center;
	}

	.earnest_right {
		width: 180rpx;
		height: 64rpx;
		color: #0A0A0C;
		background: #FBCA0C;
		border-radius: 32rpx 0 0 32rpx;
		position: absolute;
		right: 0;
		// font-weight: bold;
		font-size: 28rpx;
		line-height: 64rpx;
		text-align: center;
	}

	.rule {
		width: 690rpx;
		// height: 559rpx;
		padding: 30rpx;
		border-radius: 24rpx;
	}

	.titetext {
		color: #A1A2B2;
		font-size: 20upx;
		width: 60%;
		margin: 80px auto;
		text-align: center;
	}

	.sure {
		width: 90%;
		height: 80rpx;
		color: #0A0A0C;
		background: #FBCA0C;
		border-radius: 46rpx;
		text-align: center;
		line-height: 80rpx;
		margin: 80rpx auto;
		letter-spacing: 2rpx;
	}

	.u-mode-center-box {

		background: #050506 !important;
	}
</style>