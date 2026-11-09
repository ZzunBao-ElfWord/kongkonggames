<template>
	<!-- #ifdef APP -->
	<view style="padding-bottom: 20upx;">
	<!-- #endif -->
		<!-- #ifndef APP -->
		<view style="padding-bottom: 80upx;">
		<!-- #endif -->
			<view class="text-sm padding-top-sm margin-lr" style="color: #AC75FE;">{{fabuTitle}}</view>
			<view class="box">
				<view>
					<view class="text-df text-bold flex align-center">游戏分类</view>
					<view class="flex align-center justify-between margin-top-sm" v-if="gameImg" @click="gameShow=true">
						<view v-if="gameImg">
							<image :src="gameImg" style="width: 76upx;height: 76upx;border-radius: 8upx;"
								mode="aspectFill">
							</image>
						</view>
						<view class="flex align-center  ">
							<view class="margin-right-sm">{{gameName}}</view>
							<image src="../../../static/images/index/right.png" style="width: 8upx;height: 18upx;">
							</image>
						</view>
					</view>
					<view class="flex align-center justify-end margin-top-sm" v-else @click="gameShow=true">
						<view class="margin-right-sm">请选择游戏分类</view>
						<image src="../../../static/images/index/right.png" style="width: 8upx;height: 18upx;">
						</image>
					</view>
					<u-select v-model="gameShow" :list="gameList" @confirm="confirmGame"></u-select>
				</view>
				<view style="width: 650upx;height: 1upx;background: #2B2D44;margin: 30upx auto;">
				</view>
				<view class="flex align-center justify-between">
					<view class="text-df text-bold">性别要求</view>
					<view class="flex align-center" @click="sexShow = true">
						<view class="margin-right-sm">{{sex?sex:'请选择'}}</view>
						<image src="../../../static/images/index/right.png" style="width: 14upx;height: 24upx;"></image>
						<u-select v-model="sexShow" :list="sexList" @confirm="confirmSex"></u-select>
					</view>
				</view>
				<view style="width: 650upx;height: 1upx;background: #2B2D44;margin: 30upx auto;"></view>
				<view class="flex align-center justify-between margin-top-sm">
					<view class="text-df text-bold">单价区间</view>
					<view class="flex align-center">
						<view class="maxbox">
							<input v-model="minMoney" type="number" :clearable="false" placeholder="0" />
						</view>
						<view class="margin-lr-sm">
							-
						</view>
						<view class="maxbox">
							<input v-model="maxMoney" type="number" :clearable="false" placeholder="0" />
						</view>
					</view>
				</view>

			</view>

			<view class="box" style="margin-bottom: 200rpx;">
				<view class="flex align-center justify-between">
					<view class="text-df text-bold">期望段位</view>
					<view class="text-right">
						<u-input v-model="form.orderLevel" placeholder="请填写期望段位(选填)" :clearable="false" />
					</view>
				</view>
				<view style="width: 650upx;height: 1upx;background: #2B2D44;margin: 30upx auto;"></view>
				<view class="flex align-center justify-between">
					<view class="text-df text-bold">我的段位</view>
					<view class="text-right">
						<u-input v-model="form.myLevel" placeholder="请填写玩家当前段位(选填)" :clearable="false" />
					</view>
				</view>
				<view style="width: 650upx;height: 1upx;background: #2B2D44;margin: 30upx auto;"></view>
				<view class="flex align-center justify-between">
					<view class="text-df text-bold">期望大区</view>
					<view class="text-right">
						<u-input v-model="form.myArea" placeholder="请填写期望大区(选填)" :clearable="false" />
					</view>
				</view>
				<view style="width: 650upx;height: 1upx;background: #2B2D44;margin: 30upx auto;">
				</view>
				<view class="flex align-center justify-between margin-top-sm">
					<view class="text-df text-bold">期望局数</view>
					<view>
						<u-number-box v-model="num" @change="valChange" :min="1" bg-color="#362f5b"></u-number-box>
					</view>
				</view>
				<view style="width: 650upx;height: 1upx;background: #2B2D44;margin: 30upx auto;"></view>
				<view class="flex align-center justify-between">
					<view class="text-df text-bold">期望时间</view>
					<view class="flex align-center" @click="showTime = true">
						<view class="margin-right-sm">{{orderTakingTime?orderTakingTime:'请选择期望时间'}}</view>
						<image src="../../../static/images/index/right.png" style="width: 14upx;height: 24upx;"></image>
						<u-picker v-model="showTime" mode="time" :params="params" @confirm="getTime"></u-picker>
					</view>
				</view>
				<view style="width: 650upx;height: 1upx;background: #2B2D44;margin: 30upx auto;"></view>
				<view style="z-index: 9;">
					<view class="text-df text-bold">其他要求</view>
					<view>
						<u-input v-model="remark" type="textarea" placeholder="如：双排/三排/五排/匹配 来个国服等" height="300"
							maxlength="300" @input="confirmDetails" inputAlign="left" />
					</view>
				</view>
			</view>

			<view class="btn margin-lr " @click="setOrder">发布快速派单</view>
			<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content">
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

				//游戏分类
				gameId: '',
				gameName: '',
				gameImg: '',
				gameShow: false,
				//性别要求
				sex: '',
				sexShow: false,
				sexList: [{
						value: '0',
						label: '不限'
					}, {
						value: '1',
						label: '男'
					},
					{
						value: '2',
						label: '女'
					}
				],
				//单价区间
				moneyTitle: '',
				maxMoney: '50',
				minMoney: '0',

				//局数
				num: 1,
				numTitle: '',
				//派单时间
				orderTakingTime: '',
				params: {
					year: true,
					month: true,
					day: true,
					hour: true,
					minute: true,
					second: true
				},
				TimeTitle: '',
				showTime: false,
				//其他要求
				remark: '',
				remarkTitle: '',
				fabuTitle: '',

				form: {
					longitude: '',
					latitude: '',
					city: '',
					//游戏分类
					gameId: '',
					//服务类型
					// serviceType: '',
					//性别要求
					sex: '',
					//单价区间
					money: '',
					//技能水平
					// skill: '',
					//擅长位置
					// location: '',
					//擅长英雄
					// hero: '',
					//包上分段位
					orderLevel: '',
					//玩家段位
					myLevel: '',
					//玩家系统
					mySystem: '',
					//玩家大区
					myArea: '',
					//局数
					num: '1',
					//派单时间
					orderTakingTime: '',
					// voiceIntroduce: '',
					unit: '单',
					// isWangZhe: '',
					remark: "",
					yuewanFabu: '否',
					fabuPrice: 0,
					myFabu: '',
					fabuNumber: ''
				},
				gameList: []
			}
		},
		onLoad(option) {
			// console.log(option)
			this.fabuTitle = uni.getStorageSync('fabuTitle')
			this.myFabu = uni.getStorageSync("myFabu")
			this.fabuNumber = this.$queue.getData('fabuNumber');
			this.gameId = option.id
			this.gameName = option.gameName
			this.gameImg = option.gameImg
			this.form.gameId = option.id
			this.form.city = uni.getStorageSync('city')
			this.form.latitude = uni.getStorageSync('latitude')
			this.form.longitude = uni.getStorageSync('longitude')
			this.yuewanFabu = this.$queue.getData('yuewanFabu')
			this.fabuPrice = this.$queue.getData('fabuPrice')
			// if (this.gameId) {
			// 	console.log(this.gameId)
			// 	// this.getGameType()
			// }
			this.getAllType()
		},
		onShow() {
			// this.game = uni.getStorageSync('game')
			// if (this.game) {
			// 	this.getGameType()
			// }

		},
		methods: {
			confirmGame(option) {
				console.log(option)
				this.gameId = option[0].value
				this.gameName = option[0].label
				this.gameImg = option[0].image
				this.form.gameId = option[0].value
			},
			getAllType() {
				this.$Request.getT('/app/appGame/queryGameName').then(res => {
					console.log(res)
					if (res.code == 0) {
						// this.gameList = res.data
						let gameList = res.data
						var arr = []
						for (let i = 0; i < gameList.length; i++) {
							var data = {}
							data.label = gameList[i].gameName
							data.value = gameList[i].id
							data.image = gameList[i].gameImg
							arr.push(data)
						}
						this.gameList = arr

					}
				});
			},
			//其他要求
			confirmDetails(e) {
				// console.log(e)
				this.remark = e
				this.form.remark = e
			},

			//性别要求
			confirmSex(e) {
				this.sex = e[0].label
				this.form.sex = e[0].value
			},

			// 获取时间
			getTime(e) {
				// console.log(e)
				this.orderTakingTime = e.year + '-' + e.month + '-' + e.day + ' ' + e.hour + ':' + e.minute + ':' + e
					.second
				this.form.orderTakingTime = e.year + '-' + e.month + '-' + e.day + ' ' + e.hour + ':' + e.minute + ':' + e
					.second
			},
			valChange(e) {
				// console.log('当前值为: ' + e.value)
				this.form.num = e.value
			},
			// 跳转详情
			goDetail() {
				uni.navigateTo({
					url: '/package/pages/matching/orderDeatil'
				})
				// this.$refs.gameShow.open()
			},

			// 创建派单需求
			setOrder() {
				// this.qdshow = true
				this.form.money = this.minMoney + '~' + this.maxMoney
				if (!this.gameName) {
					uni.showToast({
						title: '请选择游戏分类',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.sex) {
					uni.showToast({
						title: '请选择性别要求',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.minMoney) {
					uni.showToast({
						title: '请填写最低单价',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (this.minMoney < 0) {
					uni.showToast({
						title: '最低单价不能低于0',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.maxMoney) {
					uni.showToast({
						title: '请填写最高单价',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (this.maxMoney <= 0) {
					uni.showToast({
						title: '最高单价需要大于0',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.form.money) {
					uni.showToast({
						title: '请填写单价区间',
						icon: 'none',
						duration: 1000
					})
					return
				}

				// if (!this.form.skill) {
				// 	uni.showToast({
				// 		title: '请填写技能水平',
				// 		icon: 'none',
				// 		duration: 1000
				// 	})
				// 	return
				// }

				// if (!this.form.location) {
				// 	uni.showToast({
				// 		title: '请填写期望TA擅长的位置',
				// 		icon: 'none',
				// 		duration: 1000
				// 	})
				// 	return
				// }
				// if (!this.form.hero) {
				// 	uni.showToast({
				// 		title: '请填写期望TA擅长的英雄',
				// 		icon: 'none',
				// 		duration: 1000
				// 	})
				// 	return
				// }

				// if (!this.form.orderLevel) {
				// 	uni.showToast({
				// 		title: '请填写期望上分段位',
				// 		icon: 'none',
				// 		duration: 1000
				// 	})
				// 	return
				// }
				// if (!this.form.myLevel) {
				// 	uni.showToast({
				// 		title: '请填写玩家当前段位',
				// 		icon: 'none',
				// 		duration: 1000
				// 	})
				// 	return
				// }

				// if (!this.form.mySystem) {
				// 	uni.showToast({
				// 		title: '请填写玩家系统',
				// 		icon: 'none',
				// 		duration: 1000
				// 	})
				// 	return
				// }
				// if (!this.form.myArea) {
				// 	uni.showToast({
				// 		title: '请填写玩家大区',
				// 		icon: 'none',
				// 		duration: 1000
				// 	})
				// 	return
				// }
				if (!this.form.orderTakingTime) {
					uni.showToast({
						title: '请选择期望时间',
						icon: 'none',
						duration: 1000
					})
					return
				}
				// if (!this.form.remark) {
				// 	uni.showToast({
				// 		title: '请填写其他要求' ,
				// 		icon: 'none',
				// 		duration: 1000
				// 	})
				// 	return
				// }

				var num = this.fabuNumber - this.myFabu;
				console.log(num, '发布次数')
				if (this.yuewanFabu == '是' && num <= 0) {
					this.form.fastPrice = this.fabuPrice
					this.content = '发布需要支付' + this.fabuPrice + '柚币，是否继续发布'
					this.popupshow = true
				} else {
					this.$Request.postJson("/app/fastOrder/insertFastOrder", this.form).then(res => {
						if (res.code == 0) {
							uni.redirectTo({
								url: '/my/yuewanOrder/index'
							})
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					});
				}

			},
			sureT() {
				let that = this
				that.$Request.postJson("/app/fastOrder/insertFastOrder", that.form).then(
					res => {
						if (res.code == 0) {
							that.popupshow = false
							uni.redirectTo({
								url: '/my/yuewanOrder/index'
							})
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					});
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

	.uni-textarea-textarea {
		text-align: left !important;
		color: #FFFFFF !important;
	}

	.uni-textarea-placeholder {
		text-align: left !important;
	}

	.uni-scroll-view {
		background: #1E1F31 !important;
	}

	.box {
		background-color: #291B3D;
		border-radius: 16upx;
		margin: 30upx 30upx;
		padding: 40upx 30upx;
	}

	.maxbox {
		width: 140upx;
		background: #362f5b;
		/* height: 48upx; */
		/* line-height: 48upx; */
		text-align: center;
		display: flex;
		align-items: center;
		height: 64rpx;
		border-radius: 7rpx;
	}

	.btn {
		background: #AC75FE;
		color: #fff;
		padding: 30upx 10upx;
		/* margin: 30upx; */
		position: fixed;
		bottom: 30upx;
		left: 0;
		right: 0;
		z-index: 99;
		text-align: center;
		border-radius: 12upx;
		font-size: 31upx;
		font-weight: 600;
	}



	.waiting {
		width: 500rpx;
		height: 555rpx;
		background: #1789FD;
		color: #fff;
		/* border: 2rpx solid #fff; */
		border-radius: 8rpx;
	}

	.popuptit {
		width: 490upx;
		margin: 30rpx auto;
		font-size: 28upx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #B8B8CC;
	}

	.btn1 {
		background: #1789FD;
		color: #fff;
		/*width: 55upx;
		margin: 0 auto; */
		padding: 15rpx 60rpx;
		display: inline-block;
		border-radius: 33rpx;
		margin-top: 10rpx;
	}

	.u-mode-center-box {
		background-color: #111224 !important;
	}

	.u-drawer-bottom {
		background-color: #111224 !important;
	}

	.u-content {
		background-color: #1E1F31 !important;
	}

	.u-input {
		background-color: #291B3D !important;
		/* text-align: right !important; */
	}

	.searchs {
		background: #1E1F31;
		padding: 14rpx 40rpx;
		border-radius: 30rpx;
		margin-left: 20rpx;
	}

	.u-select__header {
		background: #ffffff !important;
	}

	.u-icon-disabled.data-v-a54b45c0 {
		color: #FFFFFF !important;
		background: #27283C !important;
	}
</style>
