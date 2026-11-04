<template>
	<view style="padding-bottom: 20rpx;">

		<u-sticky h5-nav-height="0" :offset-top="0" bg-color="#1A1728">
			<!-- 搜索 -->
			<view class="search flex align-center justify-center">
				<view class="search-box">
					<u-search search-icon-color="#909399" color="#ffffff" bg-color="#35324e" :show-action="false"
						placeholder="请输入主播名称或房间号" v-model="search" @search="searchList"></u-search>
				</view>
			</view>
			<!-- 分类 -->
			<view class="type flex align-center justify-center">
				<view class="type-box">
					<!-- 一级分类 -->
					<view class="type-box-one flex align-center justify-between">
						<view class="type-box-one-s flex align-center">
							<view @click="change(index)" :class="typeCurr == index?'activeType':''"
								class="type-box-one-s-item flex flex-wrap justify-center"
								v-for="(item,index) in typeList" :key="index">
								<view class="">
									{{item.name}}
								</view>
								<image v-if="typeCurr == index" style="margin-top: -16rpx;"
									src="../../static/images/my/tabsDown.png" mode="">
								</image>
							</view>
						</view>
						<image class="type-box-one-i" src="../../static/images/my/tabsIcon.png" mode=""></image>
					</view>
					<!-- 二级分类 -->
					<view v-if="typeCurr == 1" class="type-box-er" style="margin-top: 30rpx;">
						<scroll-view class="type-box-er" scroll-x="true">
							<view @click="selectTab(index)" :class="tbasCur == index?'tbaActive':''"
								class="type-box-er-item" v-for="(item,index) in tabs" :key="index">
								{{item.gameName}}
							</view>
						</scroll-view>

					</view>
				</view>
			</view>
		</u-sticky>
		<!-- 房间列表 -->
		<!-- <view class="list flex align-center justify-center">
			<view class="list-box flex flex-wrap justify-between">
				<view class="list-box-item" v-for="(item,index) in roomList" :key="index">
					<view class="list-box-item-img">
						<image :src="item.roomImg?item.roomImg:'../../static/logo.png'" mode="aspectFill"></image>
						<view class="list-box-item-img-name">
							{{item.userName}}
						</view>
					</view>
					<view class="list-box-item-name">
						{{item.roomName}}
					</view>
				</view>

			</view>
		</view> -->
		<view class="lists flex align-center justify-center" @click="goRoom(item)" v-for="(item,index) in roomList"
			:key="index">
			<view class="lists-box flex align-center justify-center">
				<view class="lists-boxs flex justify-between">
					<image :src="item.roomImg" mode="aspectFill"></image>
					<view class="lists-boxs-r flex flex-wrap">
						<view class="lists-boxs-r-title">
							{{item.roomName}}
						</view>
						<view class="" style="width: 100%;">
							<view class="lists-boxs-r-tips">
								{{item.roomType==1?'娱乐':item.gameName}}
							</view>
						</view>

						<view class="lists-boxs-r-btom flex align-center justify-between">
							<view class="lists-boxs-r-btom-l">
								<image v-if="item.userEntityList.length>0 && ind<5"
									v-for="(ite,ind) in item.userEntityList" :key="ind" :src="ite.avatar" mode="">
								</image>
							</view>
							<view class="lists-boxs-r-btom-r flex align-center">
								<image src="../../static/images/my/roomXh.png" mode=""></image>
								{{item.counts}}人在线
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<empty v-if="roomList.length == 0"></empty>
		<popup v-if="popupshowcl" :maskCloseAble="false" :show="popupshowcl" closeName="取消" confirmName="进入"
			@closeT="popupshowcl = false" @sureT="surecl" :content="contentcl"></popup>
		<!-- #ifdef APP || H5 -->
		<!-- 语聊房悬浮窗口 -->
		<drag-button v-if="isShowDrag" :startRoom="startRoom" @closeDrag="closeDrag" :isDock="true"
			:existTabBar="true" />
		<!-- #endif -->
	</view>
</template>

<script>
	// #ifdef H5
	import TRTC from 'trtc-sdk-v5';
	// // 创建trtc对象
	const trtc = TRTC.create();
	// #endif
	import empty from '../../components/empty.vue'
	import popup from '@/components/popup.vue'
	import dragButton from "@/components/drag-button/drag-button.vue";
	import configdata from '../../common/config.js'
	export default {
		components: {
			empty,
			popup,
			dragButton
		},
		data() {
			return {
				isShowDrag: false,
				contentcl: '你当前有正在直播的房间，进入新房间会解散当前房间，是否进入新房间？',
				popupshowcl: false, //是否重连弹框
				typeCurr: 0,
				typeList: [{
						name: '娱乐',
						status: 0
					},
					{
						name: '陪玩',
						status: 1
					}
				], //一级分类
				tbasCur: 0,
				tabs: [], //二级分类
				page: 1,
				pages: 1,
				limit: 10,
				roomList: [], //房间列表
				search: '', //搜索内容
				roomInfo: {}, //房间信息
				startRoom: {}, //当前正在播放房间的信息
				itemRoom: {}, //点击的房间信息
			}
		},
		onShow() {
			//获取语聊房间
			this.getList()
			this.getGame()
			let that = this
			that.startRoom = {}
			// #ifdef APP || H5
			if (uni.getStorageSync('token')) {
				//判断是否是从其他房间返回的
				try {
					let dataInfo = uni.getStorageSync('startRoom')
					if (dataInfo.strRoomId) {
						that.startRoom = dataInfo
						that.isShowDrag = true
					} else {
						that.startRoom = {}
						that.isShowDrag = false
					}
				} catch (e) {
					//TODO handle the exception
					that.startRoom = {}
				}
				this.getUserId()
			} else {
				this.roomInfo = {
					strRoomId: '',
					roomId: '',
					userId: ''
				}
			}
			// #endif

		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.getList()
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getList()
		},
		methods: {
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
			dismissRoom() {
				let data = {
					roomNo: this.roomInfo.strRoomId
				}
				let that = this
				this.$Request.getT('/app/room/dismissRoom', data).then(res => {
					if (res.code == 0) {
						//关闭弹框
						this.popupshowcl = false
						this.isShowDrag = false
						//退出trtc房间
						// #ifdef APP
						this.$trtcCloud.exitRoom();
						uni.navigateTo({
							url: '/my/voice/voice?strRoomId=' + that.itemRoom.roomNo +
								'&role=TRTCRoleAudience' +
								'&roomId=' +
								that.itemRoom.roomId,
							success: function(res) {
								that.resetting()
							}
						})
						// #endif
						// #ifdef H5
						trtc.exitRoom();
						trtc.destroy();
						uni.navigateTo({
							url: '/my/voice/voiceH5?strRoomId=' + that.itemRoom.roomNo +
								'&role=TRTCRoleAudience' +
								'&roomId=' +
								that.itemRoom.roomId,
							success: function(res) {
								that.resetting()
							}
						})
						// #endif
						uni.hideLoading()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			closeCl() {
				// 关闭房间
				let that = this
				uni.showLoading({
					title: '解散房间中...'
				})
				// 连接scoket
				uni.connectSocket({
					url: that.config("APIHOST5") +
						`/roomChatSocket/${that.roomInfo.userId}/${that.roomInfo.roomId}`,
					data() {
						return {
							msg: 'Hello'
						}
					},
					header: {
						'content-type': 'application/json'
					},
					method: 'GET',
					success(res) {
						// 这里是接口调用成功的回调，不是连接成功的回调，请注意
					},
					fail(err) {
						// 这里是接口调用失败的回调，不是连接失败的回调，请注意
					}
				})
				//连接打开
				uni.onSocketOpen((res) => {
					let data = {
						roomId: that.roomInfo.roomId,
						status: 2
					}
					that.$Request.postJson('/app/room/updateRoomUser', data).then(res => {
						if (res.code == 0) {
							let upData = {
								type: 16,
							}
							upData = JSON.stringify(upData);
							uni.sendSocketMessage({
								data: upData,
								success(res) {
									uni.hideLoading()
									//关闭scoket连接
									uni.closeSocket()
									//调用一下服务端解散房间的接口
									that.dismissRoom()
								},
								fail(err) {
									uni.hideLoading()
									// console.log(err);
								}
							})
						} else {
							uni.hideLoading()
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				})
			},
			//重连跳转
			surecl() {
				this.closeCl()
			},
			//关闭悬浮
			closeDrag() {
				this.isShowDrag = false
				uni.removeStorageSync('startRoom')
				this.page = 1
				this.getList()
			},
			//获取用户信息
			getUserId() {
				this.$Request.getT('/app/user/selectUserById').then(res => {
					if (res.code == 0) {
						if (Number(res.data.isRoom) === 1) { //有正在直播的房间
							uni.removeStorageSync('startRoom')
							// 存储房间信息
							this.roomInfo = {
								strRoomId: res.data.roomNo,
								roomId: res.data.roomId,
								userId: res.data.userId
							}
							this.isShowDrag = true
						} else {
							this.roomInfo = {
								strRoomId: '',
								roomId: '',
								userId: ''
							}
						}

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//进行搜索
			searchList() {
				this.page = 1
				this.getList()
			},
			//获取游戏分类
			getGame() {
				this.$Request.get('/app/appGame/queryGameName').then(res => {
					if (res.code == 0) {
						this.tabs = res.data
					}
				})
			},
			//选择二级分类
			selectTab(index) {
				this.tbasCur = index
				this.page = 1
				this.getList()
			},
			//选择一级分类
			change(index) {
				this.typeCurr = index
				this.page = 1
				this.getList()
			},
			//退出直播房间
			outRoom(item, roomId) {
				// 关闭房间
				let that = this
				let userId = uni.getStorageSync('userId')
				// 连接scoket
				uni.connectSocket({
					url: that.config("APIHOST5") +
						`/roomChatSocket/${userId}/${roomId}`,
					data() {
						return {
							msg: 'Hello'
						}
					},
					header: {
						'content-type': 'application/json'
					},
					method: 'GET',
					success(res) {
						// 这里是接口调用成功的回调，不是连接成功的回调，请注意
					},
					fail(err) {
						// 这里是接口调用失败的回调，不是连接失败的回调，请注意
					}
				})
				//连接打开
				uni.onSocketOpen((res) => {
					let data = {
						roomId: roomId,
						status: 2
					}
					that.$Request.postJson('/app/room/updateRoomUser', data).then(res => {
						if (res.code == 0) {
							let upData = {
								type: 14,
							}
							upData = JSON.stringify(upData);
							uni.sendSocketMessage({
								data: upData,
								success(res) {
									//关闭scoket连接
									uni.closeSocket()
									//退出trtc房间
									// #ifdef APP
									that.$trtcCloud.exitRoom();
									uni.navigateTo({
										url: '/my/voice/voice?strRoomId=' + item.roomNo +
											'&role=TRTCRoleAudience' +
											'&roomId=' +
											item.roomId
									})
									// #endif
									// #ifdef H5
									trtc.exitRoom();
									trtc.destroy();
									uni.navigateTo({
										url: '/my/voice/voiceH5?strRoomId=' + item.roomNo +
											'&role=TRTCRoleAudience' +
											'&roomId=' +
											item.roomId
									})
									// #endif

									uni.removeStorageSync('startRoom')
								},
								fail(err) {
									uni.hideLoading()
								}
							})
						} else {
							uni.hideLoading()
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				})
			},
			//判断当前要进入的房间自己是否是黑名单
			isBlackRoom(roomId) {
				return new Promise((resolve, reject) => {
					let data = {
						roomId: roomId
					}
					this.$Request.postJson('/app/room/insertRoomUser', data).then(res => {
						// console.log(res)
						if (res.code == 0) {
							if (res.data) {
								if (res.data.status == 3) {
									resolve(3);
								} else {
									resolve(1);
								}
							} else {
								resolve(1);
							}
						} else {
							resolve(2);
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				})
			},
			//进入房间
			async goRoom(item) {
				// #ifdef APP || H5
				if (uni.getStorageSync('token')) {
					//如果有没有退出的房间，先退出房间在进入
					if (this.isShowDrag) {
						// 退出当前房间
						if (this.roomInfo.roomId) { //自己开的房间
							if (item.roomId == this.roomInfo.roomId) {
								// #ifdef APP
								uni.navigateTo({
									url: '/my/voice/voice?strRoomId=' + item.roomNo +
										'&role=TRTCRoleAudience' +
										'&roomId=' +
										item.roomId + '&isBack=true'
								})
								// #endif
								// #ifdef H5
								uni.navigateTo({
									url: '/my/voice/voiceH5?strRoomId=' + item.roomNo +
										'&role=TRTCRoleAudience' +
										'&roomId=' +
										item.roomId + '&isBack=true'
								})
								// #endif

							} else {
								let returnStatus = await this.isBlackRoom(item.roomId)
								if (returnStatus === 1) {
									this.itemRoom = item
									this.popupshowcl = true
								} else if (returnStatus === 3) {
									uni.showToast({
										title: '已被主持封禁',
										icon: 'none'
									})
								}
							}
						} else { //别人的房间
							let data = {
								roomId: item.roomId
							}
							this.$Request.getT('/app/room/selectRoomDetails', data).then(async res => {
								if (res.code == 0) {
									if (res.data && res.data.status == 1) {
										let returnStatus = await this.isBlackRoom(item.roomId)
										if (returnStatus === 1) {
											this.outRoom(item, this.startRoom.roomId)
										} else if (returnStatus === 3) {
											uni.showToast({
												title: '已被主持封禁',
												icon: 'none'
											})
										}
									} else {
										uni.showToast({
											title: '房间不存在，请重试',
											icon: 'none'
										})
										//刷新一下房间列表
										this.page = 1
										this.getList()
									}
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							})
						}
					} else {
						//进入房间之前先判断一下该房间的状态
						let data = {
							roomId: item.roomId
						}
						this.$Request.getT('/app/room/selectRoomDetails', data).then(async res => {
							if (res.code == 0) {
								if (res.data && res.data.status == 1) {
									let returnStatus = await this.isBlackRoom(item.roomId)
									if (returnStatus === 1) {
										// #ifdef APP
										uni.navigateTo({
											url: '/my/voice/voice?strRoomId=' + item.roomNo +
												'&role=TRTCRoleAudience' +
												'&roomId=' +
												item.roomId
										})
										// #endif
										// #ifdef H5
										uni.navigateTo({
											url: '/my/voice/voiceH5?strRoomId=' + item.roomNo +
												'&role=TRTCRoleAudience' +
												'&roomId=' +
												item.roomId
										})
										// #endif

									} else if (returnStatus === 3) {
										uni.showToast({
											title: '已被主持封禁',
											icon: 'none'
										})
									}

								} else {
									uni.showToast({
										title: '房间不存在，请重试',
										icon: 'none'
									})
									//刷新一下房间列表
									this.page = 1
									this.getList()
								}
							} else {
								uni.showToast({
									title: res.msg,
									icon: 'none'
								})
							}
						})
					}
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
				// #endif
			},
			//房间列表
			getList() {
				let data = {
					page: this.page,
					limit: this.limit,
					status: 1,
					roomType: Number(this.typeCurr) + 1,
					search: this.search
				}
				if (this.typeCurr == 1) {
					data.gameId = this.tabs[this.tbasCur].id
				}
				this.$Request.getT('/app/room/selectRoomList', data).then(res => {
					uni.hideLoading()
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.pages = res.data.totalPage
						if (this.page == 1) {
							this.roomList = res.data.list
						} else {
							this.roomList = [...this.roomList, ...res.data.list]
						}
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	.activeType {
		color: #ac75fe !important;
		font-weight: bold;
	}

	.tbaActive {
		background: linear-gradient(-132deg, #3e2b55, #ac75fe) !important;
		font-weight: bold;
	}

	.search {
		width: 100%;
		height: auto;
		background-color: #1A1728;

		.search-box {
			width: 686rpx;
			height: 100%;
			padding: 20rpx 0;
		}
	}

	.type {
		width: 100%;
		height: auto;
		background-color: #1A1728;
		padding-bottom: 20rpx;

		.type-box {
			width: 686rpx;
			height: 100%;
		}

		.type-box-one {
			width: 100%;
			// margin: 0 auto;
		}

		.type-box-one-s-item {
			margin-right: 100rpx;
			font-size: 36rpx;
			width: 100rpx;
			height: 80rpx;

			image {
				width: 40rpx;
				height: 16rpx;
				margin-top: 0;
			}
		}

		.type-box-one-i {
			width: 60rpx;
			height: 70rpx;
		}

		.type-box-er {
			width: 100%;
			white-space: nowrap;
		}

		.type-box-er-item {
			display: inline-block;
			margin-right: 20rpx;
			padding: 14rpx 34rpx;
			border-radius: 40rpx;
			background-color: #363342;
			font-size: 28rpx;
		}


	}

	.list {
		margin-top: 20rpx;
		width: 100%;
		height: auto;

		.list-box {
			width: 686rpx;
			height: 100%;
		}

		.list-box-item {
			width: calc(100% / 2 - 4rpx);
			height: auto;
			margin-bottom: 20rpx;
			border-radius: 14rpx;
			background-color: #291B3D;

			.list-box-item-img {
				width: 100%;
				height: 200rpx;
				border-radius: 14rpx 14rpx 0 0;
				position: relative;

				image {
					width: 100%;
					height: 100%;
					border-radius: 14rpx 14rpx 0 0;
				}

				.list-box-item-img-name {
					position: absolute;
					bottom: 10rpx;
					left: 10rpx;
					font-size: 24rpx;
				}
			}

			.list-box-item-name {
				width: 100%;
				padding: 20rpx;
				overflow: hidden; //超出的文本隐藏
				text-overflow: ellipsis; //溢出用省略号显示
				white-space: nowrap; // 默认不换行；
			}
		}
	}

	.lists {
		width: 100%;
		height: 192rpx;
		margin-bottom: 20rpx;

		.lists-box {
			width: 686rpx;
			height: 100%;
			background-color: #201E38;
			border-radius: 32rpx;
		}

		.lists-boxs {
			width: calc(100% - 40rpx);
			height: calc(100% - 40rpx);

			image {
				width: 150rpx;
				height: 150rpx;
				border-radius: 22rpx;
			}

			.lists-boxs-r {
				width: calc(100% - 170rpx);
				height: 100%;
			}

			.lists-boxs-r-title {
				width: 100%;
				font-size: 32rpx;
				overflow: hidden; //超出的文本隐藏
				text-overflow: ellipsis; //溢出用省略号显示
				white-space: nowrap; // 默认不换行；
			}

			.lists-boxs-r-tips {
				width: fit-content;
				padding: 2rpx 14rpx;
				font-size: 24rpx;
				color: #FFADB3;
				border: 1px solid #DDB2B2;
				background: linear-gradient(90deg, #212238 0%, #80505F 100%);
				border-radius: 10rpx;
			}

			.lists-boxs-r-btom {
				width: 100%;
			}

			.lists-boxs-r-btom-l {
				image:nth-of-type(1) {
					margin-left: 0;
				}

				image {
					width: 40rpx;
					height: 40rpx;
					border-radius: 50%;
					margin-left: -10rpx;
				}
			}

			.lists-boxs-r-btom-r {
				image {
					width: 40rpx;
					height: 40rpx;
					margin-right: 6rpx;
				}
			}
		}
	}
</style>