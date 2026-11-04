<template>
	<view>
		<view class="text-center" style="margin: 60rpx auto;">
			<image src="https://pw.xianmxkj.com/file/uploadPath/2023/11/24/54d953a59e236f79a87dd516d2316703.png"
				style="width: 210rpx;height: 190rpx;"></image>
			<view class="text-lg text-bold margin-top-sm" v-if="babyModel">青少年模式未开启</view>
			<view class="text-lg text-bold margin-top-sm" v-if="!babyModel">青少年模式已开启</view>
		</view>
		<view v-if="qsnMsg" class="box" v-html="qsnMsg"></view>

		<view class="btn" v-if="babyModel" @tap="qingshow = true">开启青少年模式</view>
		<view class="btn" v-if="!babyModel" @tap="qingshow = true">关闭青少年模式</view>

		<u-popup v-model="qingshow" mode="center" width="551rpx"
			bgColor="linear-gradient(23deg, #37225D 0%, #313035 89%);">
			<view class="text-center" style="padding: 48rpx 30rpx;">
				<view class="title">{{mimaMsg}}</view>
				<view class="margin-top-xl">
					<VcodeInput :sum="4" ref="VcodeInput" @vcodeInput="vcodeInput"></VcodeInput>
				</view>
				<view class="btn1" @tap="savePwd">确定</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import VcodeInput from '../../components/vcode-input/vcode-input.vue'
	export default {
		components: {
			VcodeInput
		},
		data() {
			return {
				keyPwd: '',
				mimaMsg: '设置密码',
				babyModel: '',
				qsnMsg: '',
				qingshow: false,
				value: ''
			}
		},
		onLoad() {
			this.getUserInfo();
			this.$Request.getT('/app/common/type/724').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.qsnMsg = res.data.value;
					}
				}
			});
		},
		onBackPress() {
			if (this.babyModel) {
				return;
			}
		},
		methods: {
			vcodeInput(e) {
				this.keyPwd = e;
				if (this.keyPwd.length == 4) {
					this.savePwd();
				}
			},
			savePwd() {
				if (this.keyPwd.length != 4) {
					this.$queue.showToast('请设置四位数密码！');
					return;
				}
				if (this.babyModel) {
					this.$Request.postT('/app/user/setBabyPwd?babyPwd=' + this.keyPwd).then(res => {
						if (res.code == 0) {
							this.$queue.showToast('青少年模式已开启！');
							setTimeout(d => {
								this.getUserInfo();
								uni.reLaunch({
									url: '/package/pages/paihangbang/moshi'
								})
							}, 2000);
						} else {
							this.$queue.showToast(res.msg);
						}
						this.keyPwd = '';
					});
				} else {
					this.$Request.postT('/app/user/closeBaby?babyPwd=' + this.keyPwd).then(res => {
						if (res.code == 0) {
							this.$queue.showToast('已关闭青少年模式！');
							setTimeout(d => {
								uni.switchTab({
									url: '/pages/index/index'
								})
								this.getUserInfo();
							}, 2000);
						} else {
							this.$queue.showToast(res.msg);
						}
						this.keyPwd = '';
					});
				}
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.keyPwd = '';
						if (!res.data.babyModel || res.data.babyModel == 2) {
							this.mimaMsg = '设置密码';
							this.babyModel = true;
						} else {
							this.mimaMsg = '输入密码';
							this.babyModel = false;
						}
					}
				});
			},
		}
	}
</script>

<style lang="less">
	.u-mode-center-box {
		border-radius: 28rpx !important;
	}

	.box {
		background: linear-gradient(90deg, rgba(234, 134, 189, 0.3) 0%, rgba(162, 112, 248, 0.3) 100%);
		border-radius: 24rpx;
		margin: 90rpx 32rpx;
		padding: 30rpx 40rpx;
	}

	.btn {
		width: 686rpx;
		margin: 0 auto;
		height: 100rpx;
		background: linear-gradient(90deg, #F386BD 0%, #A470F8 100%);
		border-radius: 50rpx;
		text-align: center;
		line-height: 100rpx;
		font-family: PingFang SC;
		font-weight: bold;
		font-size: 30rpx;
	}

	.btn1 {
		width: 400rpx;
		height: 70rpx;
		background: linear-gradient(90deg, #F386BD 0%, #A470F8 100%);
		border-radius: 35rpx;
		text-align: center;
		line-height: 70rpx;
		margin: 40rpx auto;
	}

	.title {
		font-size: 36rpx;
		font-weight: 800;
	}
</style>