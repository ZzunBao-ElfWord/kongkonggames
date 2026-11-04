<template>
	<view>
		<view class="margin-lr padding radius margin-top-sm" style="background: #291B3D;">
			<view class="flex" v-if="order.game">
				<image :src="order.game.gameImg" style="width: 76rpx;height: 76rpx;border-radius: 10rpx;"></image>
				<view class="margin-left-xs flex align-center">
					<view class="margin-right-sm text-lg text-bold">{{order.game.gameName}}</view>
					<view class="radius margin-right-xs text-sm" v-if="order.sex == 0"
						style="background: #AC75FE;padding:2rpx 8rpx 2rpx 8rpx;color: #FFFFFF;">
						不限
					</view>
					<view v-if="order.sex == 1" class="  margin-right-xs "
						style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 12upx;color: #6EE3FB;border-radius: 50rpx;">
						<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
						<text class="text-sm">男生</text>
					</view>
					<view v-if="order.sex == 2" class="  margin-right-xs"
						style="display: inline-block;background: linear-gradient(-90deg, #4F2B4B, #6735b1);padding: 2upx 12upx;color: #FB85FC;border-radius: 50rpx;">
						<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
						<text class="text-sm">女生</text>
					</view>

				</view>
			</view>
			<!-- <view class="flex margin-tb-sm" v-if="order.serviceType">
				<view style="color: #B8B8CC;">服务类型：</view>
				<view style="color: #FFFFFF;">{{order.serviceType}}</view>
			</view> -->
			<view class="flex margin-top" v-if="order.myLevel||order.myArea">
				<view class="flex align-center" style="color: #FFFFFF;">
					<text style="color: #FFFFFF;" class="margin-right-xs" v-if="order.myLevel">玩家段位</text>/
					<text style="color: #FFFFFF;" class="margin-lr-xs" v-if="order.myArea">大区：</text>
				</view>
				<view style="color: #FFFFFF;width: 50%;">
					<text v-if="order.myLevel">{{order.myLevel}} / </text>
					<text v-if="order.myArea"> {{order.myArea}}</text>
				</view>
			</view>
			<!-- <view class="flex margin-top-sm" v-if="order.sex ">
				<view style="color: #FFFFFF;">性别要求：</view>
				<view class="text-white" v-if="order.sex == 0">不限</view>
				<view class="text-white" v-if="order.sex == 1">男</view>
				<view class="text-white" v-if="order.sex ==2">女</view>
			</view> -->
			<view class="flex margin-top-sm" v-if="order.skill">
				<view style="color: #FFFFFF;">技能水平：</view>
				<view class="text-white">{{order.skill}}</view>
			</view>
			<view class="flex margin-tb-sm padding-top-xs" v-if="order.orderLevel">
				<view style="color: #FFFFFF;">期望段位：</view>
				<view style="color: #FFFFFF;">{{order.orderLevel}}</view>
			</view>
			<view class="flex margin-tb-sm" v-if="order.location||order.hero">
				<view style="color: #FFFFFF;">期望TA：</view>
				<view style="color: #FFFFFF;">{{order.location}} / {{order.hero}} </view>
			</view>
			<view class="flex margin-top-sm" v-if="order.money">
				<text style="color: #FFFFFF;">价格要求：</text>
				<view class="text-white">{{order.money}}
					<image src="../../static/share/icon_QQkongjian.png"
						style="width: 20rpx;height: 20rpx;margin: 0rpx 5rpx;"></image>/局
				</view>
			</view>
			<view class="flex margin-top-sm" v-if="order.num">
				<view style="color: #FFFFFF;">局数要求：</view>
				<view class="text-white">{{order.num}}</view>
			</view>
			<view class="flex margin-top-sm" v-if="order.orderTakingTime">
				<view style="color: #FFFFFF;">期望时间：</view>
				<view class="text-white">{{order.orderTakingTime}}</view>
			</view>
			<view class="margin-top-sm flex">
				<view style="color: #FFFFFF;" class="text-df">其他要求：</view>
				<view class="text-sm ">{{order.remark?order.remark:'暂无其他要求'}}</view>
			</view>
		</view>
		<view class="margin-lr padding radius margin-top-sm" style="background: #291B3D;">
			<!-- #ifndef H5 -->
			<view class="text-white">
				<!-- 	<u-form :model="form" ref="uForm" label-position="top" :label-style='lableStyle'>
					<u-form-item label="语音介绍" :border-bottom='false'>
						<textarea class="text-grey" disabled style="height: 150rpx;"
							placeholder="录制一段自我介绍的语音(介绍自己擅长位置，英雄，游戏成就 等皆可)"></textarea>
					</u-form-item>
				</u-form> -->
				<view>
					<view>语音介绍</view>
					<view>录制一段自我介绍的语音(介绍自己擅长位置，英雄，游戏成就 等皆可提升老板下单量)</view>
				</view>
				<jsfun-record :maxTime="60" :minTime="5" @okClick="saveRecord" @start='select' v-if="status == 1">
					<view class="flex justify-between text-center margin-top">
						<view class="padding-top">
							<!-- <image style="width: 120rpx;height: 120rpx;" src="../static/reset.png"></image> -->
						</view>
						<view>
							<view>
								<image style="width: 184rpx;height: 184rpx;" src="../static/luyin.png"></image>
							</view>
						</view>
						<view class="padding-top">
							<!-- <image style="width: 120rpx;height: 120rpx;" src="../static/confirm.png"></image> -->
						</view>
					</view>
					<view class="flex justify-between text-center">
						<view style="width: 120rpx;"></view>

						<view style="width: 184rpx;">点击录制</view>

						<view style="width: 120rpx;"></view>
					</view>
				</jsfun-record>
				<view class="flex justify-between" v-if="status == 2">
					<view class="u-rela" style="width: 360rpx;" @click="playVoice">
						<image src="../static/15.png" style="width: 360rpx;height: 78rpx;"></image>
						<view class="flex justify-between u-abso"
							style="width: 326rpx;height: 58rpx;line-height: 58rpx;top:10rpx;left: 0;right: 0;margin: auto;">
							<image v-if="isPlay" src="../static/stop1.png" style="width: 58rpx;height: 58rpx;"></image>
							<image v-else src="../static/play1.png" style="width: 58rpx;height: 58rpx;"></image>
							<image src="../static/yinpin.png" style="width: 185rpx;height: 58rpx;"></image>
							<view>{{recordLength?recordLength+'S':'未知'}}</view>
						</view>
					</view>
					<u-icon @click="clearAudio()" name="close" size="40"></u-icon>
				</view>
			</view>
			<!-- #endif -->
			<view class="">
				<view class="text-df text-bold">文字介绍</view>
				<view>
					<u-input v-model="content3" type="textarea" placeholder="如：绍自己擅长位置，英雄，游戏成就 等皆可可提升老板下单量哦"
						height="300" maxlength="300" @input="confirmDetails" />
				</view>
			</view>
		</view>

		<!-- dingw -->
		<view class="margin-lr flex justify-between margin-top" style="margin-bottom: 120upx;">
			<view class="btn" @click="cancel()">确认抢单</view>
			<view class="btn " @click="back()">取消抢单</view>
		</view>


		<!-- 抢单弹框 -->
		<u-popup v-model="show" mode="center" width="560rpx" border-radius="14" :closeable="true"
			close-icon="close-circle" close-icon-size="48" close-icon-color="#ffffff" @close="qiangdan">
			<view class="text-center" style="padding-top:40rpx;background: #291B3D;">
				<view class="text-df text-bold">已提交抢单</view>
				<view class="popuptit">
					您已成功提交抢单！可在“我的抢单” 查看该订单，抢单成功后可在 “我的接单”查看该订单。
				</view>
				<view class="btn1" @click="qiangdan()">我已阅读并了解</view>
			</view>
		</u-popup>
		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"
			:title="title"></popup>
	</view>
</template>

<script>
	import jsfunRecord from '../components/jc-record/jc-record.vue'
	import popup from '@/components/popup.vue'
	import configdata from '@/common/config.js';
	export default {
		components: {
			jsfunRecord,
			popup
		},
		data() {
			return {
				popupshow: false,
				content: '',
				title: '',

				show: false,
				fastOrderId: '',
				order: [],
				status: 1,
				//录音相关参数
				// #ifndef H5
				//H5不能录音
				// #endif

				recording: false,
				willStop: false,

				recordLength: 0, //录音时长
				content3: '', //接单文字 
				voice: '', //接单语音
				sec: '', //语音秒数
				AUDIO: uni.createInnerAudioContext(),
			}
		},
		onLoad(option) {
			this.fastOrderId = option.fastOrderId
			if (this.fastOrderId) {
				this.getOrderDet()
			}
		},
		onShow() {

		},
		onReady() {
			this.AUDIO.onEnded(function(res) {
				this.isPlay = false;
			});
		},
		onUnload() {
			if (this.isPlay) {
				this.AUDIO.stop();
			}
			this.isPlay = !this.isPlay;
		},
		methods: {
			playVoice() {
				console.log(this.isPlay)
				this.AUDIO.src = this.form.voiceIntroduce;
				if (this.isPlay) {
					this.AUDIO.stop();
				} else {
					this.AUDIO.play();
				}
				this.isPlay = !this.isPlay;
			},
			//订单详情
			getOrderDet() {
				this.$queue.showLoading("加载中...");
				let data = {
					fastOrderId: this.fastOrderId
				}
				this.$Request.get('/app/fastOrder/selectFastOrderDetails', data).then(res => {
					if (res.code == 0) {
						this.order = res.data
						if (this.order.templateType == 2) {
							if (this.order.serviceType) {
								let serviceType = ''
								serviceType = this.order.serviceType.split('~')
								this.order.serviceType = serviceType[0]
							}
						}
					} else {
						uni.hideLoading();
						this.$queue.showToast(res.msg)
					}
					uni.stopPullDownRefresh();
					uni.hideLoading();
				});
			},
			//取消抢单
			back() {
				uni.navigateBack()
			},
			//跳转抢单
			qiangdan() {
				uni.redirectTo({
					url: '/package/pages/play/robOrder'
				})
			},
			sureT() {
				let that = this
				let data = {
					fastOrderId: that.fastOrderId,
					content: that.content3,
					voice: that.voice,
					sec: that.sec
				}
				that.$Request.postJson("/app/fastOrder/insertFastOrderTake", data).then(
					ret => {
						if (ret.code == 0) {
							that.popupshow = false
							that.show = true
						} else {
							uni.showToast({
								title: ret.msg,
								icon: 'none'
							})
						}
					});
			},
			//确认抢单
			cancel() {
				this.title = '抢单提示'
				this.content = '确认抢单吗?'
				this.popupshow = true
				// let that = this
				// let data = {
				// 	fastOrderId: that.fastOrderId,
				// 	content: that.content,
				// 	voice: that.voice,
				// 	sec: that.sec
				// }
				// that.$Pmessage({
				// 	showModel: true,
				// 	title: '抢单提示',
				// 	content: '确认抢单吗?',
				// 	maskcloseAble: false
				// }, {
				// 	emitBtn: () => { //确认
				// 		console.log('用户点击确认');
				// 		that.$Request.postJson("/app/fastOrder/insertFastOrderTake", data).then(
				// 			ret => {
				// 				if (ret.code == 0) {
				// 					that.show = true
				// 				} else {
				// 					uni.showToast({
				// 						title: ret.msg,
				// 						icon: 'none'
				// 					})
				// 				}
				// 			});
				// 	},
				// 	emitClose: () => { //取消
				// 		console.log('用户点击取消');
				// 	},
				// });
			},
			select(e) {
				this.status = e
			},
			saveRecord(recordPath) {
				console.log("===音频文件地址：" + recordPath + "===")
				let that = this
				uni.showLoading({
					title: '录音上传中...'
				})
				// console.log(this.config("APIHOST1") + '/alioss/uploadMusic')
				uni.uploadFile({
					url: that.config("APIHOST1") + '/alioss/uploadMusic', //仅为示例，非真实的接口地址
					filePath: recordPath,
					name: 'file',
					success: (uploadFileRes) => {
						console.log(JSON.parse(uploadFileRes.data))
						that.voice = JSON.parse(uploadFileRes.data).data.url
						that.sec = JSON.parse(uploadFileRes.data).data.sec
						that.recordLength = JSON.parse(uploadFileRes.data).data.sec
						uni.hideLoading()
					},
					fail: (err) => {
						console.log(err)
						uni.hideLoading()
					}
				});
			},
			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
					if (name2.length > 1) {
						info = configdata[name2[0]][name2[1]] || null;
					} else {
						info = configdata[name] || null;
					}
					if (info == null) {
						let web_config = cache.get("web_config");
						if (web_config) {
							if (name2.length > 1) {
								info = web_config[name2[0]][name2[1]] || null;
							} else {
								info = web_config[name] || null;
							}
						}
					}
				}
				return info;
			},
			// 清除录音
			clearAudio() {
				this.status = 1
				this.recordLength = 0
				this.form.voiceIntroduce = ''
			},
		}
	}
</script>

<style>
	::v-deep .textMoney {
		text-align: center;
	}

	page {
		background-color: #1A1728;
	}

	.round {
		background: #582D4D;
		padding: 1px 8px 3px 6px;
		color: #FF659A;
		border-radius: 30rpx;
	}

	.dingw {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 80rpx;
		z-index: 9;
	}

	.btn {
		background: #AC75FE;
		border-radius: 16upx;
		/* flex: 1; */
		width: 48%;
		padding: 25rpx 0rpx;
		font-weight: bold;
		text-align: center;
		font-size: 32upx;
		color: #fff;
	}

	.u-mode-center-box {
		background-color: #111224 !important;
	}

	.popuptit {
		width: 306upx;
		margin: 30rpx auto;
		font-size: 28upx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #B8B8CC;
	}

	.btn1 {
		background: #AC75FE;
		color: #fff;
		/*width: 55upx;
		margin: 0 auto; */
		padding: 15rpx 60rpx;
		display: inline-block;
		border-radius: 33rpx;
		margin-top: 10rpx;
		margin-bottom: 75rpx;
	}

	.u-input__textarea {
		color: #FFFFFF !important;
	}
</style>
