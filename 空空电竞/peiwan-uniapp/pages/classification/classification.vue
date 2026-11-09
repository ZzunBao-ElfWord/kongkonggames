<template>
	<view class="content">
		<view class="header">
			<view class="header-box">
				<view class="header-box-search" @click="goNav('/pages/index/search/index')">
					<u-icon name="search" color="#FFFFFF" size="36"></u-icon>
					<text style="font-size: 26upx;color: #FFFFFF;">搜索你想要的陪陪</text>
				</view>
			</view>
		</view>
		<view class="menu">
			<!-- 左侧菜单 -->
			<view class="menuLeft">
				<scroll-view scroll-y="true" class="menuLeft-scroll">
					<view :class="current==index?'active':''" @click="qeihuan(item.id,index)"
						class="menuLeft-scroll-item" v-for="(item,index) in tabsList" :key="index">
						{{item.gameName}}
					</view>
				</scroll-view>
			</view>
			<!-- 右侧内容 -->
			<view class="menuRight">
				<view class="menuRight-search">
					<rendropdownfilters :filterData='filterData' :border="false" :defaultIndex='defaultIndex'
						@onSelected='change' class="u-skeleton-rect">
					</rendropdownfilters>
				</view>
				<scroll-view scroll-y="true" class="menuRight-scroll" @scrolltolower="reachbottom">
					<view class="flex justify-between margin-bottom-sm bg margin-sm u-skeleton-fillet category-card"
						v-for="(item,index) in tabsListRight" :key='index' @click="goOrder(item)"
						style="border-radius: 24upx;">
						<view style="position: relative;">
							<image class="category-card-avatar" :src="item.avatar?item.avatar: '../../static/logo.png'"
								style="border-radius: 20rpx;"></image>
							<view class="yuyinImg" @click.stop="playVoice(item.voiceIntroduce,item.isPlay,index)"
								v-if="item.voiceIntroduce">
								<view class="flex align-center box">
									<!-- 暂停 -->
									<image v-if="item.isPlay==false" src="../../static/jc-record/index.png" mode=""
										style="width: 34upx;height: 34upx;"></image>
									<!-- 播放 -->
									<image v-else src="../../static/jc-record/bof.png" mode=""
										style="width: 34upx;height: 34upx;"></image>

									<image src="../../static/jc-record/yuyin.png" mode=""
										style="width: 24upx;height: 24upx;margin: 0upx 10upx;"></image>
									<view v-if="item.sec">{{item.sec}}<text>"</text></view>
								</view>
							</view>
						</view>
						<view class="flex-sub margin-left-sm text-white flex flex-direction justify-between category-card-content">
							<view class="flex justify-between category-card-header">
								<view class="flex align-center category-card-profile">
									<view class="margin-right-xs text-30 text-bold text-cut category-card-name">
										{{item.userName}}
									</view>
									<view v-if="item.sex == 1" class="category-card-age"
										style="background:linear-gradient(-90deg, #174C6D, #33355F);padding: 2upx 12upx;color: #6EE3FB;border-radius: 50rpx;">
										<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
										{{item.age?item.age:0}}
									</view>
									<view v-if="item.sex == 2" class="category-card-age"
										style="background: linear-gradient(-90deg, #4F2B4B, #6735b1);padding: 2upx 12upx;color: #FB85FC;border-radius: 50rpx;">
										<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
										{{item.age?item.age:0}}
									</view>
									<!-- <view v-if="item.sex == 1" class="round"
									style="display: inline-block;background-color:#3A2FED; color: #ffffff;padding: 2upx 8upx;">
									<u-icon name="man" color="#ffffff" size="24"></u-icon>
									{{item.age?item.age:0}}
								</view>
								<view v-if="item.sex == 2" class="round"
									style="display: inline-block;background-color: #FF164A; color: #ffffff;padding: 2upx 8upx;">
									<u-icon name="woman" color="#ffffff" size="24"></u-icon>
									{{item.age?item.age:0}}
								</view> -->
								</view>


							</view>
							<view class="game-tag">
								<view class="game-tag-game">
									<image class="game-tag-icon" :src="item.gameImg" mode="aspectFill"></image>
									<view class="game-tag-name">{{item.gameName}}</view>
								</view>
								<view v-if="item.myLevel" class="game-tag-level">{{item.myLevel}}</view>
							</view>
							<view class="flex text-sm" style="color: #FFFFFF;">
								<view class="margin-right" v-if="item.orderScore">评分{{(item.orderScore).toFixed(2)}}
								</view>

								<view v-if="item.count">{{item.count}}人已下单</view>
							</view>
							<view class="flex align-center justify-between category-card-footer">
								<view class="category-card-price" style="color: #FBEA74;"><text
										class="">{{isVip? item.memberMoney :item.money }}币</text>/{{item.unit}}
								</view>
								<view class="category-card-status">
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
						</view>
					</view>
					<u-loadmore v-if="tabsListRight.length==10" :status="status" :loadmoreText="loadingText"
						:loadingText="loadmoreText" :nomoreText="nomoreText" />
					<empty v-if="tabsListRight.length == 0"></empty>
				</scroll-view>
			</view>
		</view>
		<!-- #ifdef APP || H5 -->
		<!-- 语聊房悬浮窗口 -->
		<drag-button v-if="isShowDrag" :startRoom="startRoom" @closeDrag="closeDrag" :isDock="true"
			:existTabBar="true" />
		<!-- #endif -->
	</view>
</template>

<script>
	import rendropdownfilters from '../../components/ren-dropdown-filters/ren-dropdown-filter.vue'
	import empty from '@/components/empty.vue'
	import dragButton from "@/components/drag-button/drag-button.vue";
	export default {
		components: {
			rendropdownfilters,
			empty,
			dragButton
		},
		data() {
			return {
				isShowDrag: false,
				status: 'loadmore',
				loadingText: '努力加载中',
				loadmoreText: '轻轻上拉',
				nomoreText: '实在没有了',
				current: 0,
				isVip: false,
				defaultIndex: [0, 0, 0],
				filterData: [
					[{
							label: '智能优选',
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
							label: '同城',
							value: 1,
						}
					],
					[{
							label: '性别',
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
							label: '价格',
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
				tabsList: [],
				tabsListRight: [],
				idHome: '',
				token: '',
				page: 1,
				limit: 10,
				gameName: '',
				value1: '',
				value2: '',
				value3: '',
				userId: '',
				juliSelect: '',
				musicIndex: '',
				isPlay: false,
				AUDIO: uni.createInnerAudioContext(),
				startRoom: {}, //当前正在播放房间的信息
			}
		},
		onLoad() {
			this.getClassfly();

			let that = this;
			//语音自然播放结束
			that.AUDIO.onEnded((res) => {
				console.log("this.musicIndex___:" + that.musicIndex)
				let musicIndexs = that.musicIndex;
				if (musicIndexs != '') {
					that.tabsListRight[musicIndexs].isPlay = false;
				} else {
					that.tabsListRight[0].isPlay = false;
				}
			});

		},
		onHide() {
			this.AUDIO.stop();
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.tabsListRight[musicIndexs].isPlay = false;
			}
		},
		onShow() {
			let _this = this
			_this.startRoom = {}
			// #ifdef APP || H5
			if (uni.getStorageSync('token')) {
				//判断是否是从其他房间返回的
				try {
					let dataInfo = uni.getStorageSync('startRoom')
					if (dataInfo.strRoomId) {
						_this.startRoom = dataInfo
						_this.isShowDrag = true
					} else {
						_this.startRoom = {}
						_this.isShowDrag = false
					}
				} catch (e) {
					//TODO handle the exception
					_this.startRoom = {}
				}
				_this.getUserId()
			}
			// #endif
			_this.token = uni.getStorageSync('token')
			_this.juliSelect = this.$queue.getData('juliSelect');
			_this.userId = uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
			if (uni.getStorageSync('token')) {
				_this.getIsVip()
			}
		},
		methods: {
			//关闭悬浮
			closeDrag() {
				this.isShowDrag = false
				uni.removeStorageSync('startRoom')
			},
			getUserId() {
				this.$Request.getT('/app/user/selectUserById').then(res => {
					if (res.code == 0) {
						if (Number(res.data.isRoom) === 1) { //有正在直播的房间
							uni.removeStorageSync('startRoom')
							this.isShowDrag = true
						}

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			// 播放语音
			playVoice(voiceIntroduce, isPlay, index) {
				this.isPlay = this.tabsListRight[index].isPlay
				this.AUDIO.src = voiceIntroduce;
				if (this.isPlay == true) {
					this.AUDIO.stop();

					this.tabsListRight[index].isPlay = false
				} else {
					let musicIndexs = this.musicIndex;
					if (musicIndexs != '') {
						this.tabsListRight[musicIndexs].isPlay = false;
					} else if (musicIndexs == 0) {
						this.tabsListRight[0].isPlay = false;
					}
					this.musicIndex = index;
					this.AUDIO.stop();

					this.AUDIO.play();
					this.tabsListRight[index].isPlay = true
				}
				// this.isPlay = !this.isPlay;
			},
			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
						this.isVip = res.data
						uni.setStorageSync('isVIP', res.data)
					}
				});
			},
			//跳转至搜索
			goNav(url) {
				uni.navigateTo({
					url
				})
			},
			//触底
			reachbottom() {
				// uni.showToast({
				// 	title: '已经触底了',
				// 	icon: 'none'
				// })
				//若页面数据等于10则加载下一页
				if (this.tabsListRight.length == 10) {
					this.page += 1
					this.status = 'loading'
					this.getLists(this.gameName, 2)
				} else if (this.tabsListRight.length < 10) {
					this.status = 'nomore'
				}
			},
			// 跳转订单
			goOrder(e) {
				if (this.token) {
					uni.navigateTo({
						url: '/pages/index/game/order?id=' + e.id
					});
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
			},
			// 获取类型
			getClassfly() {
				let _this = this
				this.$Request.get("/app/appGame/queryGameName").then(res => {
					if (res.code == 0) {
						console.log(res.data, '11111111111111111')
						_this.tabsList = res.data
						_this.gameName = res.data[0].id
						// _this.idHome
						_this.getLists(res.data[0].id, 1)
					}
				});
			},
			//根据类型获取商品
			getLists(value, type) {
				let data = {
					id: value,
					page: this.page,
					limit: this.limit,
					condition: this.value1, //智能优选
					sex: this.value2, //不限性别
					by: this.value3, //价格
					latitude: uni.getStorageSync('latitude'),
					longitude: uni.getStorageSync('longitude'),
					city: uni.getStorageSync('city')
				}
				let _this = this
				this.$Request.get("/app/orderTaking/queryTakings", data).then(res => {
					if (res.code == 0) {
						uni.hideLoading()
						console.log(res.data.list, '222222222')
						if (type == 1) {
							// _this.tabsListRight = res.data.list
							_this.tabsListRight = [];
							res.data.list.forEach(d => {
								d.isPlay = false
								_this.tabsListRight.push(d);
							});
						} else {
							// _this.tabsListRight = [..._this.tabsListRight, ...res.data.list]
							res.data.list.forEach(d => {
								d.isPlay = false
								_this.tabsListRight.push(d);
							});
						}
					}
				});
			},
			// 左侧筛选
			qeihuan(gameName, index) {
				uni.showLoading({
					title: '加载中'
				})
				this.AUDIO.stop();
				let musicIndexs = this.musicIndex;
				if (musicIndexs != '') {
					this.tabsListRight[musicIndexs].isPlay = false;
				}
				this.musicIndex = 0;
				this.current = index
				this.page = 1
				this.filterData = [
					[{
							label: '智能优选',
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
							label: '同城',
							value: 1,
						}
					],
					[{
							label: '销量',
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
					[{
							label: '价格',
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
				];
				this.value1 = ''
				this.value2 = ''
				this.value3 = ''
				this.gameName = gameName
				this.getLists(gameName, 1)


			},
			// 筛选
			change(e) {
				// console.log(e[0][0], '+++++++++')
				// console.log(e,'888888888888888888888888')
				this.page = 1
				this.value1 = e[0][0].value
				this.value2 = e[1][0].value
				this.value3 = e[2][0].value


				this.getLists(this.gameName, 1)
			},
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #1A1728;
	}

	.bg {
		background-color: #291B3D;
	}

	.category-card {
		padding: 25rpx 20rpx;
	}

	.category-card-avatar {
		width: 180rpx;
		height: 180rpx;
	}

	.category-card-content {
		min-width: 0;
	}

	.category-card-header,
	.category-card-profile {
		min-width: 0;
		width: 100%;
	}

	.category-card-name {
		flex: 1 1 auto;
		min-width: 0;
		max-width: none;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.category-card-age {
		display: flex;
		align-items: center;
		flex: none;
		margin-left: auto;
		white-space: nowrap;
	}

	.category-card-footer {
		min-width: 0;
		gap: 8rpx;
	}

	.category-card-price,
	.category-card-status {
		flex: none;
		white-space: nowrap;
	}

	@media screen and (max-width: 360px) {
		.category-card {
			padding: 20rpx 12rpx;
		}

		.category-card-avatar {
			width: 140rpx;
			height: 140rpx;
		}

		.category-card .yuyinImg {
			top: 96rpx;
			left: 10rpx;
		}
	}

	.game-tag {
		box-sizing: border-box;
		width: 100%;
		max-width: 260rpx;
		min-width: 0;
		height: 40rpx;
		display: flex;
		align-items: center;
		border-radius: 20rpx;
		background: linear-gradient(-90deg, #261d44 0%, #58287b 100%);
		color: #fff;
		font-size: 24rpx;
		white-space: nowrap;
		overflow: hidden;
	}

	.game-tag-game {
		box-sizing: border-box;
		display: flex;
		align-items: center;
		flex: 0 1 auto;
		min-width: 0;
		max-width: calc(100% - 42rpx);
		height: 100%;
		padding-right: 10rpx;
		border-radius: 20rpx 0 0 20rpx;
		background: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/22/9c9581055662f648645300a662645892.png') center / 100% 100% no-repeat;
		overflow: hidden;
	}

	.game-tag-icon {
		width: 40rpx;
		height: 40rpx;
		flex: 0 0 40rpx;
		border-radius: 50%;
		margin-right: 6rpx;
	}

	.game-tag-name,
	.game-tag-level {
		min-width: 0;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.game-tag-name {
		flex: 0 1 auto;
	}

	.game-tag-level {
		flex: 1 1 auto;
		margin-left: 8rpx;
		padding-right: 8rpx;
	}

	.content {
		width: 100%;
		// height: calc(100vh - 88rpx);
		height: 100vh;
		// background-color: #111224;
	}

	.header {
		width: 100%;
		height: 88rpx;
		background-color: #1A1728;
		display: flex;
		justify-content: center;
		align-items: center;

		.header-box {
			width: 686rpx;
			height: 60rpx;

			.header-box-search {

				width: 100%;
				height: 100%;
				// background-color: rgb(247, 247, 247);
				background: #291B3D;
				border-radius: 10rpx;
				display: flex;
				align-items: center;
				padding-left: 30rpx;

				text {
					margin-left: 10rpx;
					font-size: 24rpx;
					color: #666666;
				}
			}
		}
	}

	.active {
		font-weight: bold;
		background-color: #AC75FE;
	}

	.menu {
		width: 100%;
		height: calc(100vh - 88rpx);
		display: flex;

		.menuLeft {
			// width: 220rpx;
			width: 30%;
			height: 100%;

			.menuLeft-scroll {
				width: 100%;
				height: 100%;
				background-color: #291B3D;
			}

			.menuLeft-scroll-item {
				width: 100%;
				height: 110rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				color: #FFFFFF;
				font-size: 28rpx;
				// font-weight: 500;
			}
		}

		.menuRight {
			// width: 530rpx;
			width: 70%;
			height: 100%;
			position: relative;

			// background-color: yellow;
			.menuRight-search {
				color: #FFFFFF;
				width: 100%;
				height: 92rpx;
				position: absolute;
				top: 0;
				left: 0;
				z-index: 30;
				background: #1A1728;
				overflow: visible;
			}

			.menuRight-scroll {
				width: 100%;
				height: calc(100% - 92rpx);
				position: absolute;
				top: 92rpx;

				.menuRight-scroll-item {
					width: 100%;
					height: 240rpx;
					display: flex;
					justify-content: center;
					flex-wrap: wrap;
					margin-bottom: 20rpx;

					.menuRight-scroll-item-box {
						width: 90%;
						height: 100%;
						background-color: #ffffff;
						border-radius: 8rpx;
						display: flex;

						align-items: center;

						.menuRight-scroll-item-box-l {
							width: 200rpx;
							height: 200rpx;
							margin-left: 21rpx;

							image {
								width: 100%;
								height: 100%;
							}
						}

						.menuRight-scroll-item-box-r {
							width: calc(100% - 237rpx);
							height: 200rpx;
							margin-left: 16rpx;
							display: flex;
							flex-wrap: wrap;

							// view {
							// 	// margin-top: 16rpx;
							// 	width: 100%;
							// }

							// view:nth-of-type(1) {
							// 	color: #1E1F31;
							// 	font-size: 28rpx;
							// 	font-weight: bold;
							// }

							// view:nth-of-type(2) {
							// 	color: #999999;
							// 	font-size: 24rpx;
							// 	font-weight: 400;
							// }

							// view:nth-of-type(3) {
							// 	color: #999999;
							// 	font-size: 20rpx;
							// 	font-weight: 400;
							// }

							// view:nth-of-type(4) {
							// 	margin-top: 15rpx;
							// 	color: #000000;
							// 	font-weight: 800;

							// 	span:nth-of-type(1) {
							// 		font-size: 24rpx;
							// 	}

							// 	span:nth-of-type(2) {
							// 		font-size: 32rpx;
							// 	}

							// 	span:nth-of-type(3) {
							// 		font-size: 24rpx;
							// 	}
							// }
						}
					}
				}
			}
		}
	}

	.filter-wrapper {
		width: auto !important;
	}

	.navs {
		background-color: #111224 !important;
		// color: #8b9aae !important;
	}

	.yuyinImg {
		width: 130upx;
		height: 44upx;
		text-align: center;
		line-height: 44upx;
		background: #FFFFFF;
		border-radius: 16upx;
		color: #000000;
		position: absolute;
		top: 120rpx;
		left: 15rpx;
	}

	.yuyinImg .box {
		width: 100%;
		text-align: center;
		justify-content: center;
	}
</style>
