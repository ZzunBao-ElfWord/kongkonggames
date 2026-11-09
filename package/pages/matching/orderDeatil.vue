<template>
	<view style="padding-bottom: 40upx;">
		<view class="box u-skeleton-fillet">
			<view class="flex ">
				<image :src="userInfo.avatar" style="width: 78upx;height: 78upx;border-radius: 50rpx;"></image>
				<view class="margin-left-sm">
					<view class="flex">
						<view class="margin-right-xs text-lg">
							{{userInfo.userName}}
						</view>
						<!-- <view class="flex align-center vipdj" @click="goNav('/my/vip/index')">
							<image src="@/static/images/my/huiyuan.png" style="width: 35rpx;height: 30rpx;"></image>
							<view>{{userInfo.member?userInfo.member:'1'}}</view>
						</view> -->
					</view>
					<view class="flex text-sm margin-top-xs">
						<view class="">ID:{{userInfo.invitationCode}}</view>
					</view>
				</view>
			</view>
			<view class="margin-top" style="color: #FFFFFF;font-size: 24upx;">
				<!-- <view class="flex margin-tb-sm" v-if="order.serviceType">
					<view>服务类型：</view>
					<view style="color: #FFFFFF;">{{order.serviceType}}</view>
				</view> -->
				<view class="flex margin-tb-sm" v-if="order.myArea">
					<view>大区：</view>
					<view style="color: #FFFFFF;">{{order.myArea}}</view>
				</view>
				<view class="flex margin-tb-sm" v-if="order.skill">
					<view>技能水平：</view>
					<view style="color: #FFFFFF;">{{order.skill}}</view>
				</view>
				<view class="flex margin-tb-sm padding-top-xs" v-if="order.orderLevel">
					<view>期望段位：</view>
					<view style="color: #FFFFFF;">{{order.orderLevel}}</view>
				</view>

				<view class="flex margin-top-sm" v-if="order.money">
					<view>价格：</view>
					<view class="flex align-center" style="color: #FFFFFF;">{{order.money}}
					</view>
				</view>
				<view class="flex align-center margin-tb-sm" v-if="order.num">
					<view>局数：</view>
					<view style="color: #FFFFFF;">{{order.num}}</view>
				</view>
				<view class="flex margin-tb-sm" v-if="order.location">
					<view>期望TA：</view>
					<view style="color: #FFFFFF;">{{order.location}} / {{order.hero}} </view>
				</view>
				<view class="flex margin-tb-sm" v-if="order.orderTakingTime">
					<view>期望时间：</view>
					<view style="color: #FFFFFF;">{{order.orderTakingTime}}</view>
				</view>
				<view class="flex align-center padding-top-xs">
					<view class="text-df text-bold ">其他要求：</view>
					<view class="text-sm ">{{order.remark?order.remark:'暂无其他要求'}}</view>
				</view>
			</view>
		</view>
		<!-- <view class="suiji" v-if="order.status!=3&&order.status!=2" @click="countDown()">推荐一位顶尖教练</view> -->
		<view class="margin-lr text-lg text-bold text-center" v-if="orderList.length>0">在线可接单教练</view>
		<!-- <view class="flex justify-around margin-top">
			<view v-for="(item,index) in list" :key="index" class="tab" :class="tabIndex==index?'active':''"
				@click="bindTAB(index)">
				{{item.name}}
			</view>
		</view> -->
		<view class="margin-top margin-lr" v-if="orderList">
			<view class="bgbox u-skeleton-fillet" v-for="(item,index) in orderList" :key='index' @click="goOrder(item)">
				<view style="margin-top: -30rpx;position: relative;bottom: -6rpx;">
					<image :src="item.avatar?item.avatar: '/static/logo.png'"
						style="width: 230rpx;height: 260rpx;border-radius: 24rpx"></image>
					<view class="yuyinImg" @click.stop="playVoice(item.fastVoice,item.isPlay,index)"
						v-if="item.fastVoice">
						<view class="flex align-center boxs">
							<!-- 暂停 -->
							<image v-if="item.isPlay==false" src="../../../static/jc-record/index.png" mode=""
								style="width: 40upx;height: 40upx;"></image>
							<!-- 播放 -->
							<image v-else src="../../../static/jc-record/bof.png" mode=""
								style="width: 40upx;height: 40upx;"></image>

							<image src="../../../static/jc-record/yuyin.png" mode=""
								style="width: 84upx;height: 32upx;margin: 0upx 10upx;"></image>
							<view v-if="item.fastSec">{{item.fastSec}}<text>"</text></view>
						</view>
					</view>
				</view>
				<view class="flex-sub margin-left-sm text-white flex flex-direction justify-between padding-top-sm">
					<view class="flex align-center justify-between">
						<view class="flex" style="align-items: end;">
							<view class="margin-right-xs" style="display: inline-block;">
								{{item.userName}}
							</view>
							<view v-if="false" class="  margin-right-xs"
								style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 12upx;color: #6EE3FB;border-radius: 50rpx;">
								<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
								{{item.age?item.age:0}}
							</view>
							<view v-if="false" class="  margin-right-xs"
								style="display: inline-block;background: linear-gradient(-90deg, #4F2B4B, #6735b1);padding: 2upx 12upx;color: #FB85FC;border-radius: 50rpx;">
								<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
								{{item.age?item.age:0}}
							</view>
						</view>
						<view class="">
							<view class="text-sm" v-if="item.onLine==1">
								<view
									style="width: 10upx;height: 10upx;background: #1AD566;border-radius: 10upx;display: inline-block;margin: 0 8upx 4upx 0;">
								</view>
								在线
							</view>
							<view class="text-sm" v-else>
								<view
									style="width: 10upx;height: 10upx;background: #999999;border-radius: 10upx;display: inline-block;margin: 0 8upx 4upx 0;">
								</view>
								离线
							</view>
						</view>

					</view>
					<view class="flex" v-if="item.label ">
						<view v-for="(ite,ind) in item.label" :key="ind">
							<image :src="ite" style="width: 98rpx;height: auto;margin-right: 5rpx;" mode="widthFix">
							</image>
						</view>
					</view>
					<view class="flex text-sm" style="color: #FFFFFF;">
						<view class="margin-right" v-if="item.orderScore">评分{{(item.orderScore).toFixed(2)}}</view>

						<view class="margin-right" v-else>暂无评分</view>
						<view v-if="item.count">服务{{item.count}}人</view>
					</view>

					<view class="flex align-center justify-between">
						<view class="gambox">
							<view class="gamebox">
								<image :src="item.gameImg" mode="aspectFill" style="">
								</image>
								<view class="level">{{item.gameName}}</view>
							</view>
							<view class="margin-left-xs text-sm">{{item.myLevel}}</view>
						</view>
						<view class="text-sm" style="color: #FBCA0C;"><text
								style="font-size: 36rpx;font-weight: 600;">{{isVip? item.memberMoney :item.money }}</text><text></text>币/局
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="orderBtn" v-if="order.status!=3&&order.status!=2" @click="cancelFast()">取消派单</view>
		<!-- 随机匹配 -->
		<u-popup v-model="show" mode="center" border-radius="14" width="500rpx">
			<view class="padding text-center margin-tb">
				<view class="paidan">
					<image :src="gameOrder.avatar" style="width: 100upx;height: 100upx;" @click="goNav()"
						v-if="gameOrder"></image>
					<image src="../../../static/logo.png" style="width: 100upx;height: 100upx;" v-else></image>
				</view>
				<view class="text-center padding-tb-sm" v-if="gameOrder.userName">
					<view>{{gameOrder.userName}}(秒接单)</view>
					<view class="margin-top-sm">系统匹配成功，祝您玩的愉快</view>
				</view>
				<view class="text-center padding-tb-sm" v-else>
					<view><text style="font-size: 48upx;">{{count}}</text>秒</view>
					<view>{{sendTime}}</view>
				</view>
				<view class="quxiaobtn" @click="cancel()" v-if="gameOrder">取消派单</view>
				<view class="quxiaobtn" @click="cancel()" v-else>取消预约</view>
			</view>
		</u-popup>

		<!-- 取消快速派单须知 -->
		<!-- <u-popup v-model="qdshow" mode="center" width="560rpx" border-radius="14" closeable="true"
			close-icon="close-circle" close-icon-size="48" close-icon-color="#ffffff">
			<view class="text-center" style="padding-top:40rpx;background: #050506;">
				<view class="text-df text-bold">取消快速派单须知</view>
				<view class="popuptit">
					玩家通过“快速派单”成交付款的订单，如遇特殊情况需取消退款，付款后三分钟之内免费取消退款；
					付款后超过三分钟取消退款，平台需收取20%的手续费补偿抢单教练。
					单日恶意连续超过三次取消退款的玩家，将冻结此玩家账号并不予退款。
				</view>
				<view class="btn1" @click="cancelFast()">我已阅读并了解</view>
			</view>
		</u-popup> -->
		<u-skeleton :loading="loading" :animation="true" elColor='#101014' bgColor='#050506'></u-skeleton>
		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import empty from '@/components/empty.vue'
	let time
	export default {
		components: {
			empty,
			popup
		},
		data() {
			return {
				popupshow: false,
				content: '',

				loading: true, // 是否显示骨架屏组件
				qdshow: false,
				userInfo: [],
				order: [],
				orderList: [],
				show: false,
				game: [],
				isVip: false,
				city: '',
				count: 60,
				sending: false,
				sendTime: '系统正在匹配，请您耐心等待',

				gameOrder: [],
				member: '1',

				isPlay: false,
				AUDIO: uni.createInnerAudioContext(),
				musicIndex: '',
				fastOrderId: '',
				fastMoney: '',
				checkPlay: false,
			}
		},
		onLoad(option) {
			uni.showLoading({
				title: '加载中......',
				icon: 'none'
			})
			this.city = uni.getStorageSync('city')
			this.fastOrderId = option.fastOrderId
			this.getIsVip()
			this.getUserInfo()
			this.getFastOrder()


			let that = this
			let time = setInterval(function() {
				that.getOrder()
			}, 2000)

			//语音自然播放结束
			this.AUDIO.onEnded((res) => {
				let musicIndexs = this.musicIndex;
				if (musicIndexs != '') {
					this.orderList[musicIndexs].isPlay = false;
				} else if (musicIndexs == 0) {
					this.orderList[0].isPlay = false;
				}
				this.checkPlay = false;
			});
		},
		onShow() {
			this.getFastOrder()
		},
		onHide() {
			this.show = false
			this.AUDIO.stop();
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.orderList[musicIndexs].isPlay = false;
			} else if (musicIndexs == 0) {
				this.orderList[0].isPlay = false;
			}
		},
		methods: {
			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
						this.isVip = res.data
						uni.setStorageSync('isVIP', res.data)
					}
				});
			},
			// 播放语音
			playVoice(voiceIntroduce, isPlay, index) {
				this.checkPlay = true;

				this.isPlay = this.orderList[index].isPlay

				this.AUDIO.src = voiceIntroduce;
				if (this.isPlay == true) {
					this.AUDIO.stop();
					this.orderList[index].isPlay = false

				} else {
					let musicIndexs = this.musicIndex;

					if (musicIndexs != '') {
						this.orderList[musicIndexs].isPlay = false;
					} else if (musicIndexs == 0) {
						this.orderList[0].isPlay = false;
					}
					this.musicIndex = index;
					this.AUDIO.stop();

					this.AUDIO.play();
					this.orderList[index].isPlay = true
				}
				// this.isPlay = !this.isPlay;
			},
			goNav(e) {
				uni.navigateTo({
					url: '/pages/index/game/orderDet?id=' + this.gameOrder.id + '&fastOrderId=' + this
						.fastOrderId +
						'&fastOrderTakeId=' + this.gameOrder.fastOrderTakeId + '&classType=1'
				})
				clearInterval(time)
			},

			countDown() {
				if (this.count == 0) {
					this.count = 60
				}
				// this.count = 60
				this.setTimes()

			},
			setTimes() {
				var that = this
				setTimeout(function() {
					that.matchIng()
				}, 5000)

				if (that.count > 0) {
					that.show = true
					time = setTimeout(function() {
						that.count = that.count - 1
						console.log(that.count, '--')
						if (that.count > 0) {
							that.countDown()
						} else {
							that.show = false
							uni.showToast({
								title: '暂时没有匹配成功',
								duration: 1000,
								icon: 'none'
							});
							clearInterval(time)
						}

					}, 1000)
				} else {
					that.show = false
					that.count = 0
					uni.showToast({
						title: '暂时没有匹配成功',
						duration: 1000,
						icon: 'none'
					});
					clearInterval(time)
				}
			},
			cancel() {
				this.show = false
				this.count = 0

			},
			//获取发布订单
			getFastOrder() {
				uni.showLoading({
					title: '加载中......',
					icon: 'none'
				})
				if (this.fastOrderId) {
					let data = {
						fastOrderId: this.fastOrderId
					}
					this.$Request.get("/app/fastOrder/selectFastOrderDetails", data).then(res => {
						if (res.code == 0) {
							this.order = res.data
							this.fastMoney = res.data.fastMoney
							this.fastOrderId = res.data.fastOrderId
							this.getOrder()
							this.loading = false
						}
						uni.stopPullDownRefresh();
						uni.hideLoading();

					});
				} else {
					this.$Request.get("/app/fastOrder/selectFastOrder").then(res => {
						if (res.code == 0) {
							this.order = res.data
							this.fastMoney = res.data.fastMoney
							this.fastOrderId = res.data.fastOrderId
							this.getOrder()
							this.loading = false
						}
						uni.stopPullDownRefresh();
						uni.hideLoading();

					});
				}

			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.userInfo = res.data
					}
				});
			},
			// 在线可接单列表
			getOrder() {
				if (this.checkPlay) {
					return;
				}
				let data = {
					fastOrderId: this.order.fastOrderId
				}
				this.$Request.get("/app/fastOrder/selectFastOrderTakeList", data).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						this.orderList = []
						res.data.forEach(d => {
							d.isPlay = false
							if (d.label) {
								d.label = d.label.split(',')
							}
							this.orderList.push(d);
						});

					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});

					}
				});
			},
			// 跳转订单
			goOrder(e) {
				console.log(e.id, '--------------', this.fastOrderId)
				uni.navigateTo({
					url: '/pages/index/game/order?id=' + e.id + '&fastOrderId=' + this.fastOrderId +
						'&fastOrderTakeId=' + e.fastOrderTakeId + '&classType=1' + '&fastContent=' + e.fastContent
				});
			},
			// 自动匹配
			matchIng() {
				let data = {
					fastOrderId: this.order.fastOrderId
				}
				this.$Request.get("/app/fastOrder/selectFastOrderTakeLists", data).then(res => {
					if (res.code == 0) {
						this.gameOrder = res.data
						if (!res.data) {
							// clearInterval(time)
						}

					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
						clearInterval(time)
					}
				});
			},
			sureT() {
				let that = this
				that.$Request.post("/app/fastOrder/cancelFastOrder", {
					fastOrderId: that.order.fastOrderId,
				}).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '取消成功'
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
			//取消派单
			cancelFast() {
				let that = this
				var money = that.fastMoney
				if (!money) {
					money = 0;
				}
				if (money > 0 && that.orderList.length >= 1) {
					that.content = '当前已有教练接单，取消订单不退款'
					that.popupshow = true

				} else {
					that.content = '是否确认取消发布？'
					that.popupshow = true

				}

			}
		},

	}
</script>

<style lang="less">
	::v-deep .textMoney {
		text-align: center;
	}

	page {
		background: #050506;
	}

	.box {
		background: #101014;
		border-radius: 8upx;
		margin: 30upx 30upx;
		padding: 30upx 25upx;
	}

	.diand {
		width: 14upx;
		height: 14upx;
		background: #06E673;
		border-radius: 50%;
	}

	.suiji {
		/* width: 218upx; */
		/* width: max-content; */
		/* height: 59upx; */
		margin: 20upx 30upx;
		padding: 20upx 20upx;
		background: linear-gradient(90deg, #E6CCA7 0%, #DBB78D 100%);
		color: #6C4C26;
		border-radius: 16upx;
		text-align: center;
		line-height: 59upx;
		margin-top: 30upx;
		font-size: 32upx;
		font-weight: bold;
	}

	.tab {
		width: 200upx;
		height: 68upx;
		background: #101014;
		border-radius: 34upx;
		text-align: center;
		line-height: 68upx;
	}

	.active {
		background: #FBCA0C;
		color: #0A0A0C;
	}

	.orderBtn {
		background: #FBCA0C;
		color: #0A0A0C;
		border-radius: 16upx;
		margin: 60upx 30upx;
		padding: 30upx 10upx;
		text-align: center;
		font-weight: bold;
		font-size: 32rpx;
		/* position: fixed;
		bottom: 30upx;
		left: 0;
		right: 0;
		z-index: 99; */
	}


	.paidan {
		width: 264upx;
		height: 264upx;
		/* background-image: url('../../../static/images/index/popbg.png'); */
		background-size: 100% 100%;
		background-repeat: no-repeat;
		display: flex;
		align-items: center;
		justify-content: center;
		margin: 0 auto;
	}

	.paidan image {
		border-radius: 8upx;
	}

	.quxiaobtn {
		background: #FBCA0C;
		color: #0A0A0C;
		border-radius: 10upx;
		display: inline-flex;
		padding: 19rpx 90rpx;
	}

	.page-box {
		height: 35vh !important;
	}

	.vipdj {
		background: #E5CBA6;
		display: inline-flex;
		color: #604320;
		border-radius: 53rpx;
		padding: 0rpx 15rpx;
		font-size: 22rpx;
		/* margin-left: 30rpx; */
	}

	.bgbox {
		background-color: #101014;
		display: flex;
		justify-content: space-between;
		margin-top: 60rpx;
		padding: 0 20rpx 30rpx 20rpx;
		border-radius: 24upx;
		position: relative;
	}

	.yuyinImg {
		width: 230upx;
		height: 60upx;
		text-align: center;
		line-height: 60upx;
		// background: #FFFFFF;
		// border-radius: 16upx;
		color: #FFFFFF;
		border-radius: 0rpx 0rpx 24rpx 24rpx;
		background: #2C2613;
		margin-top: -10rpx;
		position: absolute;
		bottom: 0;
		left: 0rpx;
		right: 0;
	}

	.yuyinImg .boxs {
		width: 100%;
		text-align: center;
		justify-content: space-between;
		padding: 0 15rpx;
	}

	/* .u-drawer__scroll-view {
		background: #050506;
	} */

	.popuptit {
		width: 490upx;
		margin: 30rpx auto;
		font-size: 28upx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #B8B8CC;
	}

	.btn1 {
		background: #FBCA0C;
		color: #0A0A0C;
		/*width: 55upx;
		margin: 0 auto; */
		padding: 15rpx 60rpx;
		display: inline-block;
		border-radius: 33rpx;
		margin-top: 10rpx;
		margin-bottom: 75rpx;
	}

	.gambox {
		width: 65%;
		line-height: 39upx;
		background: linear-gradient(-90deg, #221A06 0%, #4A3808 100%);
		border-radius: 24rpx 0 0 24rpx;
		display: flex;
		align-items: center;

		.gamebox {
			display: flex;
			align-items: center;
			background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/22/9c9581055662f648645300a662645892.png');
			background-size: 100% 100%;
			background-repeat: no-repeat;
			padding-right: 20rpx;
			border-radius: 24rpx 0 0 24rpx;
			position: relative;
			font-size: 24rpx;

			image {
				width: 47rpx;
				height: 43rpx;
				border-radius: 50%;
				position: absolute;
				top: 0;
				left: -10rpx;
			}

			.level {
				margin-left: 44rpx;
				font-size: 24rpx;
			}
		}
	}
</style>