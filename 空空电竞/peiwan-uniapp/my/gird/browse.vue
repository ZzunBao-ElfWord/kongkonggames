<template>
	<view class="margin-lr-sm">
		<view v-if="dataList.length != 0" class="bgbox" @click="goOrder(item)" @longpress="delData(item)"
			v-for="(item,index) in dataList" :key="index">
			<view style="margin-top: -30rpx;position: relative;bottom: -6rpx;">
				<image :src="item.avatar?item.avatar: '../../static/logo.png'"
					style="width: 230rpx;height: 260rpx;border-radius: 24rpx"></image>
				<view class="yuyinImg"
					@click.stop="playVoice(item.orderTaking.voiceIntroduce,item.orderTaking.isPlay,index)"
					v-if="item.orderTaking&&item.orderTaking.voiceIntroduce">
					<view class="flex align-center box">
						<!-- 暂停 -->
						<image v-if="item.orderTaking.isPlay==false" src="../../static/jc-record/index.png" mode=""
							style="width: 40upx;height: 40upx;"></image>
						<!-- 播放 -->
						<image v-else src="../../static/jc-record/bof.png" mode="" style="width: 40upx;height: 40upx;">
						</image>

						<image src="../../static/jc-record/yuyin.png" mode=""
							style="width: 84upx;height: 32upx;margin: 0upx 10upx;"></image>
						<view v-if="item.orderTaking.sec">{{item.orderTaking.sec}}<text>"</text></view>
					</view>
				</view>
			</view>
			<view class="flex-sub margin-left text-white flex flex-direction justify-between padding-top-sm">
				<view class="flex justify-between">
					<view class="flex align-center ">
						<view class="margin-right-xs text-lg text-bold" style="display: inline-block;">
							{{item.userName}}
						</view>
						<view v-if="item.sex == 1" class="  margin-right-xs"
							style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 12upx;color: #6EE3FB;border-radius: 50rpx;">
							<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
							{{item.age?item.age:0}}
						</view>
						<view v-if="item.sex == 2" class="  margin-right-xs"
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
				<view class="flex" v-if="item.orderTaking">
					<view v-for="(ite,ind) in item.orderTaking.label" :key="ind">
						<image :src="ite" style="width: 98rpx;height: auto;margin-right: 5rpx;" mode="widthFix">
						</image>
					</view>
				</view>

				<view class="flex text-sm" style="color: #FFFFFF;">
					<view class="margin-right" v-if="item.orderTaking && item.orderTaking.orderScore">
						评分{{(item.orderTaking.orderScore).toFixed(2)}}</view>
					<view class="margin-right" v-else>暂无评分</view>
					<view v-if="item.orderTaking && item.orderTaking.count">{{item.orderTaking.count}}人已下单</view>
				</view>
				<view class="flex justify-between">
					<view class="gambox" v-if="item.orderTaking&&item.orderTaking.game">
						<view class="gamebox">
							<image :src="item.orderTaking.game.gameImg" mode="aspectFill" style="">
							</image>
							<view class="level">{{item.orderTaking.game.gameName}}</view>
						</view>
						<view class="mytit">{{item.orderTaking.myLevel}}</view>
					</view>
					<view style="color: #FBEA74;" v-if="item.orderTaking">
						<text class="text-lg text-bold">{{isVip?item.orderTaking.memberMoney:item.orderTaking.money}}
						</text> 币/{{item.orderTaking.unit}}
					</view>
				</view>
			</view>
		</view>

		<empty v-if="dataList.length == 0"></empty>
		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import empty from '../../components/empty.vue'
	export default {
		components: {
			empty,
			popup
		},
		data() {
			return {
				popupshow: false,
				content: '',
				id: '',

				shangxianSelect: '是',
				dataList: [],
				page: 1,
				limit: 10,
				myId: '',
				isVip: false,
				AUDIO: uni.createInnerAudioContext(),
				musicIndex: '',
				isPlay: false
			}
		},
		onLoad(e) {
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			this.$queue.showLoading("加载中...");
			this.myId = uni.getStorageSync('userId')
			this.isVip = uni.getStorageSync('isVIP') ? uni.getStorageSync('isVIP') : false
			this.getBrowseList() //语音自然播放结束
			this.AUDIO.onEnded((res) => {
				let musicIndexs = this.musicIndex;
				if (musicIndexs != '') {
					this.dataList[musicIndexs].orderTaking.isPlay = false;
				} else {
					this.dataList[0].orderTaking.isPlay = false;
				}
			});
		},
		onHide() {
			this.AUDIO.stop();
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.dataList[musicIndexs].orderTaking.isPlay = false;
			} else if (musicIndexs == 0) {
				this.dataList[0].orderTaking.isPlay = false;
			}
		},
		methods: {
			// 播放语音
			playVoice(voiceIntroduce, isPlay, index) {
				this.isPlay = this.dataList[index].orderTaking.isPlay
				this.AUDIO.src = voiceIntroduce;
				if (this.isPlay == true) {
					this.AUDIO.stop();
					this.dataList[index].orderTaking.isPlay = false
				} else {
					let musicIndexs = this.musicIndex;
					if (musicIndexs != '') {
						this.dataList[musicIndexs].orderTaking.isPlay = false;
					} else if (musicIndexs == 0) {
						this.dataList[0].orderTaking.isPlay = false;
					}
					this.musicIndex = index;
					this.AUDIO.stop();

					this.AUDIO.play();
					this.dataList[index].orderTaking.isPlay = true
				}
				// this.isPlay = !this.isPlay;
			},
			// 足迹
			getBrowseList() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$Request.get("/app/userBrowse/myBrowse", data).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						if (this.page == 1) {
							this.dataList = [];
						}
						res.data.list.forEach(d => {
							// d.distance = this.setMorKm(d.distance);
							if (d.orderTaking) {
								d.orderTaking.isPlay = false
								if (d.orderTaking.label) {
									d.orderTaking.label = d.orderTaking.label.split(',')
								} else {
									d.orderTaking.label = []
								}
							}
							this.dataList.push(d);
						});
					} else {
						console.log(res.msg)
					}
					uni.stopPullDownRefresh();
				})
			},
			// 跳转订单
			goOrder(e) {
				uni.navigateTo({
					url: '/pages/index/game/order?id=' + e.orderTaking.id
				});
			},
			sureT() {
				let that = this
				let data = {
					id: that.id
				}
				that.$Request.post("/app/userBrowse/deleteMyBrowse", data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '删除成功！',
							icon: 'none'
						})
						that.page = 1
						that.getBrowseList()
						that.popupshow = false
					}
				})
			},
			// 删除
			delData(e) {
				this.id = e.id
				this.content = '确定删除吗？'
				this.popupshow = true

			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getBrowseList()
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getBrowseList()
		},
	}
</script>

<style lang="less">
	::v-deep .textMoney {
		text-align: center;
	}

	.bgbox {
		background-color: #291B3D;
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
		background: #523E80;
		margin-top: -10rpx;
		position: absolute;
		bottom: 0;
		left: 0rpx;
		right: 0;
	}

	.yuyinImg .box {
		width: 100%;
		text-align: center;
		justify-content: space-between;
		padding: 0 15rpx;
	}

	.gambox {
		width: 68%;
		line-height: 39upx;
		background: linear-gradient(-90deg, #261d44 0%, #58287b 100%);
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

		.mytit {
			width: 110rpx;
			margin-left: 10rpx;
			font-size: 24rpx;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			overflow: hidden;

		}
	}
</style>