<template>
	<view class="container">
		<view class="wrapper">
			<view class="input-content">
				<view class="cu-form-group"
					style="border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px">
					<view class="title text-black">用户名</view>
					<input :value="userName" placeholder="请输入用户名" data-key="userName" @input="inputChange" />
				</view>
				<view class="cu-form-group"
					style="border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px">
					<view class="title text-black">手机号</view>
					<input type="number" :value="phone" placeholder="请输入手机号" maxlength="11" data-key="phone"
						@input="inputChange" />
				</view>
				<view class="cu-form-group padding-right-xs"
					style="border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px">
					<text class="title text-black">验证码</text>
					<input type="number" :value="code" placeholder="请输入验证码" maxlength="6" data-key="code"
						@input="inputChange" @confirm="toLogin" />
					<button class="send-msg" @click="sendMsg" :disabled="sending">{{ sendTime }}</button>
				</view>

				<view class="cu-form-group"
					style="border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px">
					<text class="title text-black">设置密码</text>
					<input type="password" :value="password" placeholder="请设置密码" placeholder-class="input-empty"
						maxlength="20" minlength="6" data-key="password" @input="inputChange" @confirm="toLogin" />
				</view>
				<view class="cu-form-group"
					style="border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px"
					v-if="required == '是'">
					<text class="title text-black">邀请码</text>
					<input type="" maxlength="6" :value="invitation" placeholder="请填写邀请码(必填)" data-key="invitation"
						@input="inputChange" @confirm="toLogin" />
				</view>
				<view class="cu-form-group"
					style="border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px"
					v-if="required == '否'">
					<text class="title text-black">邀请码</text>
					<input type="" maxlength="6" :value="invitation" placeholder="请填写邀请码(选填)" data-key="invitation"
						@input="inputChange" @confirm="toLogin" />
				</view>
			</view>
			<button class="confirm-btn" @click="toLogin">立即注册</button>
			<view class="footer">
				<text @tap="isShowAgree" class="cuIcon"
					:class="showAgree ? 'cuIcon-radiobox' : 'cuIcon-round'">同意</text>
				<!-- 协议地址 -->
				<navigator url="/my/setting/mimi" open-type="navigate" @tap.stop="showAgree = false">《隐私政策》</navigator>
				和
				<navigator url="/my/setting/xieyi" open-type="navigate" @tap.stop="showAgree = false">《用户服务协议》</navigator>
			</view>
		</view>
		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"
			:title="title">
		</popup>
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
				title: '',

				userName: '',
				showAgree: false,
				code: '',
				phone: '',
				password: '',
				required: '否',
				sending: false,
				sendTime: '获取验证码',
				count: 60,
				relation: "",
				state: '',
				invitation: '',
				platform: ''
			};
		},
		onLoad() {

			// #ifdef APP-PLUS
			this.platform = 'app'
			//#endif
			//#ifdef H5
			this.platform = 'H5'
			// #endif 
		},
		onShow() {
			this.showAgree = false;

			let invitation = this.$queue.getData('inviterCode') ? this.$queue.getData('inviterCode') : '';
			console.log(invitation, 'invitationinvitation')
			if (invitation) {
				this.invitation = invitation
			} else {

				// 注册邀请码必填
				this.$Request.getT('/app/common/type/3').then(res => {
					if (res.code == 0 && res.data) {
						this.required = res.data.value;

						if (this.required == '是') {
							//查询官方邀请码
							this.$Request.getT('/app/common/type/88').then(ret => {
								if (ret.code == 0 && ret.data) {
									this.invitation = ret.data.value;
									// console.log(this.required,this.required == '是','2555')
								}
							});
						}
					}
				});

			}




		},
		methods: {
			sureT() {
				this.popupshow = false
			},
			// 获取新用户优惠券数量
			newMa() {
				this.$Request.getT('/app/common/type/119').then(res => {
					if (res.code == 0) {
						this.amount = res.data.value;
					}
				});
			},
			isShowAgree() {
				//是否选择协议
				this.showAgree = !this.showAgree;
			},
			sendMsg() {
				const {
					phone
				} = this;
				if (!phone) {
					this.$queue.showToast('请输入手机号');
				} else if (phone.length !== 11) {
					this.$queue.showToast('请输入正确的手机号');
				} else {
					this.$queue.showLoading('正在发送验证码...');
					this.$Request.getT('/app/Login/sendMsg/' + phone + '/1').then(res => {
						if (res.code === 0) {
							this.sending = true;
							this.$queue.showToast('验证码发送成功请注意查收');
							this.countDown();
							uni.hideLoading();
						} else {
							uni.hideLoading();
							this.title = '短信发送失败'
							this.content = res.msg ? res.msg : '请一分钟后再获取验证码'
							this.popupshow = true


						}
					});
				}
			},
			countDown() {
				const {
					count
				} = this;
				if (count === 1) {
					this.count = 60;
					this.sending = false;
					this.sendTime = '获取验证码';
				} else {
					this.count = count - 1;
					this.sending = true;
					this.sendTime = count - 1 + '秒后重新获取';
					setTimeout(this.countDown.bind(this), 1000);
				}
			},
			inputChange(e) {
				const key = e.currentTarget.dataset.key;
				this[key] = e.detail.value;
			},
			navBack() {
				uni.navigateBack();
			},

			navTo(url) {
				uni.navigateTo({
					url
				});
			},
			toLogin() {
				const {
					userName,
					phone,
					password,
					code,
					showAgree,
					invitation,
				} = this;
				if (!userName) {
					this.$queue.showToast('请输入用户名');
				} else if (!phone) {
					this.$queue.showToast('请输入手机号');
				} else if (!code) {
					this.$queue.showToast('请输入验证码');
				} else if (!password) {
					this.$queue.showToast('请设置密码');
				} else if (password.length < 6) {
					this.$queue.showToast('密码位数必须大于六位');
				} else if (!showAgree) {
					this.$queue.showToast('请先同意《协议》');
				} else if (!invitation && this.required == '是') {
					this.$queue.showToast('请填写邀请码');
				} else if (!invitation && this.required == '否') {
					this.invitation = '';
					this.logining = true;
					this.$queue.showLoading('注册中...');
					this.$Request.post(`/app/Login/registApp`, {
						userName: userName,
						password: password,
						phone: phone,
						openId: this.$queue.getData('openid') ? this.$queue.getData('openid') : '',
						invitation: this.invitation,
						platform: this.platform,
						msg: code
					}).then(res => {
						if (res.code === 0) {
							this.$queue.showToast('注册成功');
							this.$queue.setData("invitationCode", res.user.invitationCode);
							this.$queue.setData("token", res.token);
							this.$queue.setData('userId', res.user.userId);
							this.$queue.setData('userName', res.user.userName);
							this.$queue.setData('phone', res.user.phone);
							this.$queue.setData('avatar', res.user.avatar ? res.user.avatar :
								'../../static/logo.png');
							this.getUserInfo()
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/index/index'
								})
							}, 1000)
						} else {
							uni.hideLoading();
							this.title = '注册失败'
							this.content = res.msg
							this.popupshow = true

						}
					});
				} else {
					this.logining = true;
					this.$queue.showLoading('注册中...');
					this.$Request.post(`/app/Login/registApp?msg=${code}`, {
						userName: userName,
						password: password,
						phone: phone,
						openId: this.$queue.getData('openid') ? this.$queue.getData('openid') : '',
						invitation: this.invitation,
						platform: this.platform
					}).then(res => {
						if (res.code === 0) {
							this.$queue.setData("token", res.token);
							this.$queue.setData('userId', res.user.userId);
							this.$queue.setData('userName', res.user.userName);
							this.$queue.setData('phone', res.user.phone);
							this.$queue.setData('avatar', res.user.avatar ? res.user.avatar :
								'../../static/logo.png');
							this.$queue.setData("invitationCode", res.user.invitationCode);
							this.$queue.setData("inviterCode", res.user.inviterCode);
							this.getUserInfo()
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/index/index'
								})
							}, 1000)
						} else {
							this.title = '注册失败'
							this.content = res.msg
							this.popupshow = true

						}
						uni.hideLoading();
					});
				}
			},
			//获取用户信息
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						uni.hideLoading()
						this.$queue.setData('avatar', res.data.avatar ? res.data.avatar : '../../static/logo.png');
						this.$queue.setData('userId', res.data.userId);
						this.$queue.setData('userName', res.data.userName);
						this.$queue.setData('phone', res.data.phone);
						this.$queue.setData("invitationCode", res.user.invitationCode);
						this.$queue.setData("inviterCode", res.user.inviterCode);

						this.userName = res.data.userName
						this.invitationCode = res.data.invitationCode
						uni.setStorageSync('invitationCode', res.data.invitationCode)
						setTimeout(function() {
							uni.switchTab({
								url: '/pages/index/index'
							})
						}, 1000)
					} else {
						this.title = '登录失败'
						this.content = res.msg
						this.popupshow = true

						this.$queue.logout();
					}
				});
			}
		}
	};
</script>

<style lang="scss">
	page {
		height: 100%;
		background: #1A1728 !important;
	}

	.footer {
		padding-left: 140upx;
		margin-top: 32upx;
		text-align: center;
		display: flex;
		color: #FFFFFF;
	}

	.send-msg {
		border-radius: 30px;
		color: white;
		height: 30px;
		font-size: 10px;
		line-height: 30px;
		background: #ac75fe;
	}


	.container {
		padding-top: 32upx;
		position: relative;
		width: 100%;
		height: 100%;
		overflow: hidden;
		background: #1A1728 !important;
	}

	.wrapper {
		position: relative;
		z-index: 90;
		background: #1A1728;
		padding-bottom: 20px;
	}


	.input-content {
		padding: 32upx 80upx;
	}


	.confirm-btn {
		width: 600upx;
		height: 80upx;
		line-height: 80upx;
		border-radius: 60upx;
		margin-top: 32upx;
		background: #ac75fe;

		color: #fff;
		font-size: 32upx;

		&:after {
			border-radius: 60px;
		}
	}
</style>
