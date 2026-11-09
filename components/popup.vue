<template>
	<view class="page-box">
		<u-popup v-model="show" mode="center" :closeable="showClose" :mask-close-able="maskCloseAble" border-radius="20"
			@close="close" width="580upx" bgColor="linear-gradient(23deg, #1C1C22 0%, #313035 89%)">
			<view class="padding">
				<view class="text-center text-lg text-bold ">{{titles}}</view>
				<view class="textMoney" :style="custTE">
					{{content}}
					<text v-if="isStartTime">
						({{timeNum}}秒后自动拒绝)
					</text>
				</view>
				<view class="flex align-center justify-center margin-top-xl" style="margin-bottom: 20rpx;">
					<view class="quxiao" @click="closeT()">{{closeName}}</view>
					<view class="sure" @click="sureT()">{{confirmName}}</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		props: {
			custTE: {
				type: String,
				default: ''
			},
			/**
			 * 取消按钮名称
			 */
			closeName: {
				type: String,
				default: '取消'
			},
			/**
			 * 确定按钮名称
			 */
			confirmName: {
				type: String,
				default: '确定'
			},
			/**
			 * 是否启用点击遮罩关闭弹窗(重载i-popup的mask-close-able属性)
			 */
			maskCloseAble: {
				type: Boolean,
				default: true,
			},
			/**
			 * 是否启用关闭图标(重载i-popup的closeable属性)
			 */
			showClose: {
				type: Boolean,
				default: false,
			},
			/**
			 * 弹框提示
			 */
			titles: {
				type: String,
				default: '提示'
			},
			/**
			 * 显示状态
			 */
			show: {
				type: Boolean,
				default: false
			},
			/**
			 * 显示内容
			 */
			content: {
				type: String,
				default: ''
			},
			//是否显示倒计时
			isStartTime: {
				type: Boolean,
				default: false
			}
		},
		data() {
			return {
				timer: null, //定时器
				timeNum: 30,
			}
		},
		created() {
			//如果开启了倒计时
			if (this.isStartTime) {
				console.log('走这里了1')
				if (this.timer) { //如果已经有定时器了，先清除一下定时器
					clearInterval(this.timer)
					this.timer = null
					this.timeNum = 30
				}
				this.timer = setInterval(() => {
					// console.log('走这里了')
					this.timeNum--;
					if (this.timeNum == 0) { //倒计时结束
						clearInterval(this.timer)
						this.timer = null
						this.closeT()
					}
				}, 1000)

			}
		},
		destroyed() {
			if (this.timer) { //如果已经有定时器了，先清除一下定时器
				clearInterval(this.timer)
				this.timer = null
				this.timeNum = 30
			}
		},
		methods: {
			sureT() {
				this.$emit('sureT', {
					name: '确认'
				})
			},
			closeT() {
				this.$emit('closeT', {
					name: '取消'
				})
			},
			close() {
				this.$emit('close', {
					name: '取消'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.textMoney {
		width: 445rpx;
		text-align: center;
		margin: 20rpx auto;
		margin-top: 40rpx;
		color: #A1A2B2;
	}

	.quxiao {
		width: 220rpx;
		height: 70rpx;
		// background: rgba(171, 116, 255, 0.3);
		background-color: #D9A800;
		text-align: center;
		line-height: 70rpx;
		// background: #FBCA0C;
		// opacity: 0.3;
		border-radius: 35px;
	}

	.sure {
		width: 220rpx;
		height: 70rpx;
		// background: linear-gradient(90deg, #D9A800 0%, #FBCA0C 100%);
		background: #FBCA0C;
		text-align: center;
		line-height: 70rpx;
		border-radius: 35px;
		margin-left: 30rpx;
	}
</style>