<template>
	<view>
		<!-- #ifdef H5 -->
		<ren-dropdown-filter :filterData='filterData' :border="false" :defaultIndex='defaultIndex' @onSelected='change'
			class="u-skeleton-rect" style="width: 100%;position: fixed;top: 80upx;z-index: 999;">
		</ren-dropdown-filter>
		<!-- #endif -->
		<!-- #ifndef H5 -->
		<ren-dropdown-filter :filterData='filterData' :border="false" :defaultIndex='defaultIndex' @onSelected='change'
			class="u-skeleton-rect" style="width: 100%;position: fixed;top: 0;z-index: 999;">
		</ren-dropdown-filter>
		<!-- #endif -->
		<view class="margin-lr-sm margin-top-sm " style="margin-top: 120upx;" v-if="dataList.length">
			<view class="bgbox" @click="goOrder(item)" v-for="(item,index) in dataList" :key='index'>
				<view style="margin-top: -30rpx;position: relative;bottom: -6rpx;">
					<image :src="item.avatar?item.avatar: '../../../static/logo.png'" mode="aspectFill"
						style="width: 230rpx;height: 260rpx;border-radius: 24rpx"></image>
					<view class="yuyinImg" @click.stop="playVoice(item.voiceIntroduce,item.isPlay,index)"
						v-if="item.voiceIntroduce">
						<view class="flex align-center box">
							<!-- 暂停 -->
							<image v-if="item.isPlay==false" src="../../../static/jc-record/index.png" mode=""
								style="width: 40upx;height: 40upx;"></image>
							<!-- 播放 -->
							<image v-else src="../../../static/jc-record/bof.png" mode=""
								style="width: 40upx;height: 40upx;"></image>
							<image src="../../../static/jc-record/yuyin.png" mode=""
								style="width: 84upx;height: 32upx;margin: 0upx 10upx;"></image>
							<view v-if="item.sec">{{item.sec}}<text>"</text></view>
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
							<view class="mytit">{{item.myLevel}}</view>
						</view>
						<view style="color: #FBEA74;">
							<text class="text-lg text-bold">{{isVip? item.memberMoney :item.money}}
							</text>币/{{item.unit}}
						</view>
					</view>
				</view>
			</view>
		</view>

		<empty v-if="!dataList.length"></empty>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	import RenDropdownFilter from '@/components/ren-dropdown-filter/ren-dropdown-filter.vue'
	export default {
		components: {
			RenDropdownFilter,
			empty
		},
		data() {
			return {
				juliSelect: '否',
				value1: '',
				value2: '',
				value3: '',
				defaultIndex: [0, 0, 0],
				filterData: [
					[{
							label: '智能排序',
							value: '',
						},
						{
							label: '距离优先',
							value: 3,
						},
						{
							label: '人气优先',
							value: 2,
						},
						{
							label: '同城优先',
							value: 1,
						}
					],
					[{
							label: '不限性别',
							value: '',
						},
						{
							label: '限男生',
							value: 1,
						},
						{
							label: '限女生',
							value: 2,
						}
					],
					[{
							label: '不限价格',
							value: '',
						},
						{
							label: '从高到低',
							value: 'desc',
						},
						{
							label: '从低到高',
							value: 'asc',
						}
					],
				],
				page: 1,
				limit: 10,
				dataList: [],
				isVip: false,
				myId: '',
				AUDIO: uni.createInnerAudioContext(),
				musicIndex: '',
			}
		},
		onLoad() {
			this.juliSelect = this.$queue.getData('juliSelect');
			this.isVip = uni.getStorageSync('isVIP') ? uni.getStorageSync('isVIP') : false
			this.myId = uni.getStorageSync('userId')
			this.getDataList()

			let that = this;
			//语音自然播放结束
			that.AUDIO.onEnded((res) => {
				console.log("this.musicIndex___:" + that.musicIndex)
				let musicIndexs = that.musicIndex;
				if (musicIndexs != '') {
					that.dataList[musicIndexs].isPlay = false;
				} else {
					that.dataList[0].isPlay = false;
				}
			});

		},
		onUnload() {
			this.AUDIO.stop();
			console.log("this.musicIndex___:" + this.musicIndex)
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.dataList[musicIndexs].isPlay = false;
			}
		},
		onHide() {
			this.AUDIO.stop();
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.dataList[musicIndexs].isPlay = false;
			}
		},
		methods: {
			// 播放语音
			playVoice(voiceIntroduce, isPlay, index) {
				this.isPlay = this.dataList[index].isPlay
				this.AUDIO.src = voiceIntroduce;
				if (this.isPlay == true) {
					this.AUDIO.stop();
					this.dataList[index].isPlay = false
				} else {
					let musicIndexs = this.musicIndex;
					if (musicIndexs != '') {
						this.dataList[musicIndexs].isPlay = false;
					} else if (musicIndexs == 0) {
						this.dataList[0].isPlay = false;
					}
					this.musicIndex = index;
					this.AUDIO.stop();
					this.AUDIO.play();
					this.dataList[index].isPlay = true
				}
				// this.isPlay = !this.isPlay;
			},
			// 筛选
			change(e) {
				this.value1 = e[0][0].value
				this.value2 = e[1][0].value
				this.value3 = e[2][0].value
				this.page = 1;
				this.getDataList();
			},
			setMorKm(m) {
				var n = ''
				if (m) {
					if (m >= 1000) {
						n = (m / 1000).toFixed(0) + 'km'
					} else {
						n = parseInt(m) + 'm'
					}
				} else {
					n = '0m'
				}
				return n
			},
			getDataList() {
				let data = {
					page: this.page,
					limit: this.limit,
					condition: this.value1, //智能优选
					sex: this.value2, //不限男女
					by: this.value3, //价格
					latitude: uni.getStorageSync('latitude') ? uni.getStorageSync('latitude') : '',
					longitude: uni.getStorageSync('longitude') ? uni.getStorageSync('longitude') : '',
				}
				if (this.userId) {
					this.$Request.get("/app/orderTaking/queryTaking", data).then(res => {
						if (res.code == 0) {

							if (this.page == 1) {
								this.dataList = [];
							}
							res.data.list.forEach(d => {
								d.isPlay = false
								if (d.label) {
									d.label = d.label.split(',')
								}
								d.distance = this.setMorKm(d.distance);
								this.dataList.push(d);
							});
						}
						uni.stopPullDownRefresh();
					});
				} else {
					this.$Request.get("/app/orderTaking/queryTakings", data).then(res => {
						if (res.code == 0) {
							if (this.page == 1) {
								this.dataList = [];
							}
							res.data.list.forEach(d => {
								d.isPlay = false
								if (d.label) {
									d.label = d.label.split(',')
								}
								d.distance = this.setMorKm(d.distance);
								this.dataList.push(d);
							});
						}
						uni.stopPullDownRefresh();
					});
				}
			},

			// 跳转订单
			goOrder(e) {
				let token = uni.getStorageSync('token')
				if (token) {
					uni.navigateTo({
						url: '/pages/index/game/order?id=' + e.id
					});
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getDataList();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getDataList();
		},
	}
</script>

<style lang="less">
	page {
		background-color: #111224;
	}

	.bg {
		background-color: #291B3D;
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
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;

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
