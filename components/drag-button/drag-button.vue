<template>
	<view>
		<view id="_drag_button" class="drag" :style="'left: ' + left + 'px; top:' + top + 'px;'"
			@touchstart="touchstart" @touchmove.stop.prevent="touchmove" @touchend="touchend"
			@click.stop.prevent="click" :class="{transition: isDock && !isMove }">
			<view class="images">

				<image class="avatar" v-if="isMyRoom" :src="roomInfo.avatar" mode=""></image>
				<image class="avatar" v-else :src="startRoom.avatar" mode=""></image>
				<image class="playIcon" :src="require('./play.png')" mode=""></image>
				<!-- 关闭按钮 -->
				<image @click.stop.prevent="closeDrag" class="closeIcon" :src="require('./closeIcon.png')" mode="">
				</image>
			</view>

		</view>
		<popup v-if="popupshowcl" :maskCloseAble="false" :show="popupshowcl" closeName="取消" confirmName="关闭"
			@closeT="popupshowcl = false" @sureT="closeCl" :content="contentcl"></popup>
	</view>
</template>

<script>
	// #ifdef H5
	import TRTC from 'trtc-sdk-v5';
	// // 创建trtc对象
	const trtc = TRTC.create();
	// #endif
	import configdata from '../../common/config.js'
	import popup from '@/components/popup.vue'
	export default {
		components: {
			popup
		},
		name: 'drag-button',
		props: {
			isDock: {
				type: Boolean,
				default: false
			},
			existTabBar: {
				type: Boolean,
				default: false
			},
			//当前正在播放房间的信息
			startRoom: {
				type: Object,
				default: {}
			},
		},
		data() {
			return {
				contentcl: '确认关闭房间吗？关闭后你的房间将被解散',
				popupshowcl: false, //是否重连弹框
				isShow: true, //是否显示悬浮
				top: 0,
				left: 0,
				width: 0,
				height: 0,
				offsetWidth: 0,
				offsetHeight: 0,
				windowWidth: 0,
				windowHeight: 0,
				isMove: true,
				edge: 10,
				text: '按钮',
				roomInfo: {}, //房间信息
				isMyRoom: false, //是否是我的房间 true:是 false：不是
				isLoding: false, //是否正在关闭
			}
		},
		mounted() {
			this.getUserId()
			const sys = uni.getSystemInfoSync();
			this.windowWidth = sys.windowWidth;
			this.windowHeight = sys.windowHeight;

			// #ifdef APP-PLUS
			this.existTabBar && (this.windowHeight -= 50);
			// #endif
			if (sys.windowTop) {
				this.windowHeight += sys.windowTop;
			}
			const query = uni.createSelectorQuery().in(this);
			query.select('#_drag_button').boundingClientRect(data => {
				this.width = data.width;
				this.height = data.height;
				this.offsetWidth = data.width / 2;
				this.offsetHeight = data.height / 2;
				this.left = this.windowWidth - this.width - this.edge;
				this.top = this.windowHeight - this.height - this.edge;
			}).exec();
		},
		methods: {
			getUserId() {
				this.$Request.getT('/app/user/selectUserById').then(res => {
					if (res.code == 0) {
						console.log(res, '个人信息ssssss')
						if (Number(res.data.isRoom) === 1) { //有正在直播的房间
							// 存储房间信息
							this.roomInfo = {
								strRoomId: res.data.roomNo,
								roomId: res.data.roomId,
								userId: res.data.userId,
								avatar: res.data.avatar
							}
							this.isMyRoom = true
						} else {
							this.isMyRoom = false
						}

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//退出房间并隐藏悬浮窗
			closeDrag() {
				if (this.isMyRoom) { //我自己的房间退房
					console.log('4444444444')
					// this.closeCl()
					this.popupshowcl = true
				} else { //别人的房间退房
					console.log('555555555555')
					this.closeCl2()
				}

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
			//别人的房间
			closeCl2() {
				// 关闭房间
				let that = this
				that.isLoding = true
				let userId = uni.getStorageSync('userId')
				uni.closeSocket()
				uni.showLoading({
					title: '关闭中...'
				})
				// 连接scoket
				uni.connectSocket({
					url: that.config("APIHOST5") +
						`/roomChatSocket/${userId}/${that.startRoom.roomId}`,
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
						uni.hideLoading()
						// 这里是接口调用失败的回调，不是连接失败的回调，请注意
					}
				})
				//连接打开
				uni.onSocketOpen((res) => {
					let data = {
						roomId: that.startRoom.roomId,
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
									that.isLoding = false
									//关闭scoket连接
									uni.closeSocket()
									//退出trtc房间
									// #ifdef APP
									that.$trtcCloud.exitRoom();
									// #endif
									// #ifdef H5
									trtc.exitRoom();
									trtc.destroy();
									// #endif
									that.$emit('closeDrag');
									uni.hideLoading()
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
			// 我自己的房间
			closeCl() {
				// 关闭房间
				let that = this
				that.isLoding = true
				uni.closeSocket()
				uni.showLoading({
					title: '关闭中...'
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
						uni.hideLoading()
						// 这里是接口调用失败的回调，不是连接失败的回调，请注意
					}
				})
				//连接打开
				uni.onSocketOpen((res) => {
					let data = {
						roomId: that.roomInfo.roomId,
						status: 2
					}
					console.log('走这里了吗？')
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
			//服务端解散房间
			dismissRoom() {
				let data = {
					roomNo: this.roomInfo.strRoomId
				}
				this.$Request.getT('/app/room/dismissRoom', data).then(res => {
					if (res.code == 0) {
						//退出trtc房间
						// #ifdef APP
						this.$trtcCloud.exitRoom();
						// #endif
						// #ifdef H5
						trtc.exitRoom();
						trtc.destroy();
						// #endif
						this.$emit('closeDrag');
						that.isLoding = true
						uni.hideLoading()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			click() {
				//退出trtc房间
				// #ifdef APP
				this.$trtcCloud.exitRoom();
				// #endif
				// #ifdef H5
				trtc.exitRoom();
				trtc.destroy();
				// #endif
				//关闭scoket连接
				uni.closeSocket()
				if (this.isMyRoom) { //是我的房间
					// #ifdef H5
					uni.navigateTo({
						url: '/my/voice/voiceH5?strRoomId=' + this.roomInfo.strRoomId + '&role=TRTCRoleAnchor' +
							'&roomId=' +
							this.roomInfo.roomId + '&isBack=true'
					})
					// #endif
					// #ifdef APP
					uni.navigateTo({
						url: '/my/voice/voice?strRoomId=' + this.roomInfo.strRoomId + '&role=TRTCRoleAnchor' +
							'&roomId=' +
							this.roomInfo.roomId + '&isBack=true'
					})
					// #endif

				} else { //不是我的房间
					//在进入不是我的房间的时候判断一下，该房间是否存在
					let data = {
						roomId: this.startRoom.roomId
					}
					this.$Request.getT('/app/room/selectRoomDetails', data).then(res => {
						if (res.code == 0) {
							if (res.data && res.data.status == 1) {
								// #ifdef H5
								uni.navigateTo({
									url: '/my/voice/voiceH5?strRoomId=' + this.startRoom.strRoomId +
										'&role=TRTCRoleAudience' +
										'&roomId=' +
										this.startRoom.roomId + '&isBack=true'
								})
								// #endif
								// #ifdef APP
								uni.navigateTo({
									url: '/my/voice/voice?strRoomId=' + this.startRoom.strRoomId +
										'&role=TRTCRoleAudience' +
										'&roomId=' +
										this.startRoom.roomId + '&isBack=true'
								})
								// #endif

							} else {
								//清除一下房间缓存
								uni.removeStorageSync('startRoom')
								uni.showToast({
									title: '房间不存在，请重试',
									icon: 'none'
								})
							}
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})

				}
				this.$emit('closeDrag');
				// this.$emit('btnClick');
			},
			touchstart(e) {
				// this.$emit('btnTouchstart');
			},
			touchmove(e) {
				// 单指触摸
				if (e.touches.length !== 1) {
					return false;
				}

				this.isMove = true;

				this.left = e.touches[0].clientX - this.offsetWidth;

				let clientY = e.touches[0].clientY - this.offsetHeight;
				// #ifdef H5
				clientY += this.height;
				// #endif
				let edgeBottom = this.windowHeight - this.height - this.edge;

				// 上下触及边界
				if (clientY < this.edge) {
					this.top = this.edge;
				} else if (clientY > edgeBottom) {
					this.top = edgeBottom;
				} else {
					this.top = clientY
				}

			},
			touchend(e) {
				if (this.isDock) {
					let edgeRigth = this.windowWidth - this.width - this.edge;

					if (this.left < this.windowWidth / 2 - this.offsetWidth) {
						this.left = this.edge;
					} else {
						this.left = edgeRigth;
					}

				}

				this.isMove = false;

				// this.$emit('btnTouchend');
			},
		}
	}
</script>

<style lang="scss">
	.drag {
		background-image: url('./dragBg.png');
		background-repeat: no-repeat;
		background-size: 100% 100%;
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		position: fixed;
		z-index: 999999;

		&.transition {
			transition: left .3s ease, top .3s ease;
		}
	}

	.images {
		width: 100%;
		height: 100%;
		position: relative;
	}

	.avatar {
		width: 85rpx;
		height: 85rpx;
		border-radius: 50%;
		position: absolute;
		top: 42%;
		left: 50%;
		transform: translate(-50%, -50%);
	}

	.playIcon {
		width: 38rpx;
		height: 36rpx;
		position: absolute;
		top: 42%;
		left: 50%;
		transform: translate(-50%, -50%);
	}

	.closeIcon {
		width: 28rpx;
		height: 28rpx;
		position: absolute;
		top: 0;
		right: -10rpx;
	}
</style>