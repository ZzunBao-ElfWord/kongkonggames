<template>
	<view class="page">
		<!-- #ifdef MP-WEIXIN -->
		<view class="" style="font-size: 24rpx;color: red;padding: 20rpx 0;padding-left: 20rpx;">
			*此投诉为本小程序自有投诉渠道，非微信官方投诉渠道
		</view>
		<!-- #endif -->
		<view class="feedback-title">
			<text>投诉内容</text>
		</view>
		<view class="feedback-body"><textarea placeholder="请输入投诉内容..." v-model="sendDate.content"
				class="feedback-textare" /></view>
		<!-- <view class="feedback-title"><text>联系方式</text></view>
		<view class="feedback-body"><input class="feedback-input" v-model="sendDate.contact" placeholder="方便我们联系你" /> -->
		<view class="feedback-title">
			<text>投诉图片</text>
		</view>
		<view class="release_image">
			<shmily-drag-image :list.sync="imageList" :number="6"></shmily-drag-image>
		</view>
		<button class="feedback-submit" @tap="send">提交投诉</button>
	</view>
</template>

<script>
	import shmilyDragImage from '../../my/components/shmily-drag-image/shmily-drag-image.vue'
	export default {
		components: {
			shmilyDragImage
		},
		data() {
			return {
				msgContents: ['界面显示错乱', '启动缓慢，卡出翔了', 'UI无法直视，丑哭了', '偶发性崩溃'],
				stars: [1, 2, 3, 4, 5],
				imageList: [],
				byUserId: '',
				platform: 1,
				platformId: '',
				sendDate: {
					content: '',
					contact: ''
				}
			};
		},
		onLoad(e) {
			if (e.byUserId) {
				this.byUserId = e.byUserId;
			}
			if (e.platform) {
				this.platform = e.platform;
			}
			if (e.platformId) {
				this.platformId = e.platformId;
			}
		},
		methods: {
			send() {
				//发送反馈
				let images = '';
				let userId = this.$queue.getData('userId');
				if (!this.sendDate.content) {
					uni.showToast({
						icon: 'none',
						title: '请输入投诉内容'
					});
					return;
				}
				if (this.$queue.getChatSearchKeys(this.sendDate.content)) {
					// uni.showToast({
					// 	title: "输入内容带有非法关键字请重新输入",
					// 	mask: false,
					// 	duration: 1500,
					// 	icon: "none"
					// });
					this.$Request.postT('/app/risk/insertRisk?riskType=6&content=' + this.sendDate.content + '&byUserId=' +
						this.byUserId).then(res => {

					})
					// return;
				}
				if (this.imageList.length == 0) {
					uni.hideLoading();
					this.$queue.showToast('请添加图片!');
					return;
				} else {
					for (var i = 0; i < this.imageList.length; i++) {
						if (i === 0) {
							images = this.imageList[i];
						} else {
							images = images + ',' + this.imageList[i];
						}
					}
				}
				this.$queue.showLoading('加载中...');
				this.$Request.postJson('/app/message/insertMessage', {
					content: this.sendDate.content,
					image: images,
					state: 3,
					platform: this.platform,
					userId: userId,
					byUserId: this.byUserId,
					platformId: this.platformId
				}).then(res => {
					if (res.code === 0) {
						this.$queue.showToast('投诉成功，等待平台处理！')
						setTimeout(function() {
							uni.navigateBack();
						}, 2000);
					} else {
						uni.hideLoading();
						uni.showToast({
							title: res.msg,
							icon: 'none',
							duration: 2000
						})
					}
				});
			}
		}
	};
</script>

<style>
	@font-face {
		font-family: uniicons;
		font-weight: normal;
		font-style: normal;
		src: url('https://img-cdn-qiniu.dcloud.net.cn/fonts/uni.ttf') format('truetype');
	}

	text {
		color: #FFFFFF;
	}

	page {
		background-color: #111224 !important;
	}

	view {
		font-size: 28upx;
	}

	.release_image {
		margin: 20rpx;
		width: 95%;
		/* margin: 0 auto; */
	}

	/*问题反馈*/
	.feedback-title {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding: 20upx;
		color: #8f8f94;
		font-size: 28upx;
	}

	.feedback-star-view.feedback-title {
		justify-content: flex-start;
		margin: 0;
	}

	.feedback-body {
		font-size: 32upx;
		padding: 16upx;
		margin: 16upx;
		border-radius: 16upx;
		background: #291B3D;
		color: #FFF;
	}

	.feedback-textare {
		height: 200upx;
		font-size: 30upx;
		line-height: 50upx;
		width: 100%;
		box-sizing: border-box;
		padding: 10upx 10upx 0;
	}

	.feedback-input {
		font-size: 32upx;
		height: 60upx;
		padding: 15upx 20upx;
		line-height: 60upx;
	}


	.feedback-submit {
		background: #AC75FE;
		color: #ffffff;
		margin: 20upx;
		margin-top: 32upx;
	}
</style>