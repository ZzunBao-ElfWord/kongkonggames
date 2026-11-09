<template>
	<view class="text-white padding-lr">
		<view class="flex  padding-tb" @click="goNav('/pages/public/pwd')">
			<view class="flex-sub text-df" style="line-height: 50upx;">修改密码</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb">
			<view class="flex-sub text-df" style="line-height: 50upx;">消息通知</view>
			<u-switch v-model="checked" @change="change()" active-color="#AC75FE"></u-switch>
			<!-- <image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image> -->
		</view>

		<view class="flex  padding-tb" @click="goNav('/my/feedback/index')">
			<view class="flex-sub text-df" style="line-height: 50upx;">意见反馈</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/logOff')">
			<view class="flex-sub text-df" style="line-height: 50upx;">注销账号</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/xieyi')">
			<view class="flex-sub text-df" style="line-height: 50upx;">用户协议</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/mimi')">
			<view class="flex-sub text-df" style="line-height: 50upx;">隐私政策</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/about')">
			<view class="flex-sub text-df" style="line-height: 50upx;">关于我们</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/pingtaiXieYi?name=用户注销协议&id=826')">
			<view class="flex-sub text-df" style="line-height: 50upx;">用户注销协议</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/userPayXiYi')">
			<view class="flex-sub text-df" style="line-height: 50upx;">用户充值协议</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/pingtaiXieYi?name=平台入驻服务协议&id=821')">
			<view class="flex-sub text-df" style="line-height: 50upx;">平台入驻服务协议</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/pingtaiXieYi?name=平台行为守则&id=822')">
			<view class="flex-sub text-df" style="line-height: 50upx;">平台行为守则</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/pingtaiXieYi?name=平台服务规范&id=823')">
			<view class="flex-sub text-df" style="line-height: 50upx;">平台服务规范</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/pingtaiXieYi?name=游戏上分服务保障&id=824')">
			<view class="flex-sub text-df" style="line-height: 50upx;">游戏上分服务保障</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view>
		<view class="btn" @click="goOut">退出登录</view>
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

				checked: true,
			}
		},
		onLoad() {
			this.getUserInfo()
		},
		methods: {
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						console.log(res.data.isSendMsg)
						this.checked = res.data.isSendMsg == 1 || res.data.isSendMsg == null ? true : false
					}
				});
			},
			change(status) {
				// console.log(status);
				this.$Request.post('/app/user/updateSendMsg', {
					isSendMsg: status ? 1 : 2
				}).then(res => {
					if (res.code == 0) {

					}
				});
			},
			goNav(e) {
				uni.navigateTo({
					url: e
				})
			},
			sureT() {
				this.popupshow = false
				uni.removeStorageSync('userName')
				uni.removeStorageSync('avatar')
				uni.removeStorageSync('userId')
				uni.removeStorageSync('token')
				uni.removeStorageSync('phone')
				uni.removeStorageSync('zhiFuBaoName')
				uni.removeStorageSync('zhiFuBao')
				uni.removeStorageSync('invitationCode')
				uni.removeStorageSync('unionId')
				uni.removeStorageSync('openId')
				uni.removeStorageSync('isVIP')
				uni.removeStorageSync('weixinPhone')
				uni.showToast({
					title: '退出成功！',
					icon: 'none'
				})
				setTimeout(function() {
					uni.navigateBack()
				}, 1000)
			},
			goOut() {
				this.content = '确定退出登录吗？'
				this.popupshow = true

			}
		}

	}
</script>

<style>
	::v-deep .textMoney {
		text-align: center;
	}

	page {
		background: #1A1728;
	}

	.btn {
		width: 100%;
		height: 80upx;
		background: #AC75FE;
		border-radius: 6upx;
		text-align: center;
		line-height: 80upx;
		margin-top: 40upx;
		font-size: 34upx;
		color: #fff;
	}
</style>