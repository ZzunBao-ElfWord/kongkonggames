<template>
	<view class="text-white">
		<view class="head">
			<view class=" u-flex u-p-l-30 u-p-t-30 u-p-b-30">
				<view class="u-m-r-10">
					<image :src="avatar?avatar:'../../../static/logo.png'"
						style="width: 100rpx;height: 100rpx;border-radius: 100rpx;"
						@click="goNav('/pages/my/userinfo')">
					</image>
					<!-- <view class="lovip" @click.stop="goNav('/my/renzheng/index')"
						v-if="status == 2 && yueSelect != '否'">
						<image src="../../../static/images/my/shim.png" style="width: 100%;height: 100%;"></image>
					</view> -->
				</view>
				<view class="flex align-center justify-between" style="width: 85%;position: relative;top: -10rpx;">
					<view class="u-flex-1 u-m-l-10 text-white">

						<view class="  text-bold flex align-center">
							<view class="text-30">{{userName}}</view>
							<image v-if="onLine" @tap="updateState"
								src="https://pw.xianmxkj.com/file/uploadPath/2023/03/23/53659d8d3996af8fe86d4ae6b1afe910.png"
								class="sxx_view"></image>
							<image v-if="!onLine" @tap="updateState"
								src="https://pw.xianmxkj.com/file/uploadPath/2023/03/23/fbd000b6f593f28b145de4f638a8b2e6.png"
								class="sxx_view"></image>
							<!-- <view v-if="status == 0 && yueSelect != '否'" class="label  "
								@click.stop="goNav('/my/renzheng/index')" style="">
								未认证
							</view>
							<view v-if="status == 1 && yueSelect != '否'" class="label"
								@click.stop="goNav('/my/renzheng/index')">
								审核中
							</view>
							<view v-if="status == 2 && yueSelect != '否'" class="label"
								@click.stop="goNav('/my/renzheng/index')">
								已认证
							</view>
							<view v-if="status == 3 && yueSelect != '否'" class="label"
								@click.stop="goNav('/my/renzheng/index')">
								已拒绝
							</view> -->
						</view>
						<view class="text-sm">ID：{{invitationCode}}</view>
					</view>
					<view class="flex align-center margin-right justify-end"
						@click="goNav('/my/gird/guanzhuDetail?userId='+userId+'&name=我的主页')" style="width: 30%;">
						<view class="margin-right-xs">主页中心</view>
						<image src="../../../static/images/index/right.png" style="width: 11upx;height: 20upx;"></image>
					</view>
				</view>
			</view>

			<view class="margin-lr bgs">
				<view class="flex justify-between align-center ">
					<view>
						<view class="">我的钱包</view>
						<view class="margin-top-xs" style="font-size:38upx;font-weight: bold;">{{money}}</view>
					</view>
					<view class="look" @click="goNav('/my/wallet/index?index=2')">
						<image
							src="https://pw.xianmxkj.com/file/uploadPath/2023/03/21/5d67f4f7149e6b9a8ace1453ed3b1dd9.png">
						</image>
					</view>

				</view>
			</view>

		</view>

		<view class="flex align-center justify-between margin-lr box" v-if="mylist.length!=0">
			<view @click="goNav(mylist[0].url)">
				<image :src="mylist[0].imageUrl" style="width: 333rpx;height:166rpx;"></image>
			</view>
			<view @click="goNav(mylist[1].url)">
				<image :src="mylist[1].imageUrl" style="width: 333rpx;height:166rpx;"></image>
			</view>
		</view>



		<!-- <view class="bgbox1 flex align-center justify-around">
			<view class="flex " @click="goNav('/my/takeOrder/index')">
				<view class="margin-right-sm">
					<view class="text-lg text-bold margin-bottom-xs">我的接单</view>
					<view style="color: #D4D4D4;font-size: 24upx;">查看接单更容易</view>
				</view>
				<image src="../../../static/images/my/jiadan.png" style="width: 89upx;height: 89upx;"></image>
			</view>
			<view style="width: 1rpx;height: 90upx;background: #E6E6E6;"></view>
			<view class="flex " @click="goNav('/my/publish/index')">
				<view class="margin-right-sm">
					<view class="text-lg text-bold margin-bottom-xs">我的服务</view>
					<view style="color: #D4D4D4;font-size: 24upx;">查看接单更容易</view>
				</view>
				<image src="../../../static/images/my/qiangdan.png" style="width: 89upx;height: 89upx;"></image>
			</view>
		</view> -->
		<view class="peibox">
			<!-- #ifdef APP || H5 -->
			<view class="flex padding-lr padding-tb justify-between align-center" @click="creatRoomFun()">
				<view class="flex align-center">
					<image src="../../../static/images/my/createRoom.png" style="width: 49rpx;height: 57rpx;"></image>
					<view class="flex-sub margin-left text-df">创建房间</view>
				</view>
				<image src="../../../static/images/index/right.png" style="width: 15rpx;height: 30rpx;"></image>
			</view>
			<!-- #endif -->
			<view class="flex padding-lr padding-tb justify-between align-center"
				@click="goNav('/package/pages/play/order')">
				<view class="flex align-center">
					<image src="../../../static/images/my/qiangdanchi.png" style="width: 49rpx;height: 57rpx;"></image>
					<view class="flex-sub margin-left text-df">抢单池</view>
				</view>
				<image src="../../../static/images/index/right.png" style="width: 15rpx;height: 30rpx;"></image>
			</view>
			<view class="flex padding-lr padding-tb justify-between align-center"
				@click="goNav('/package/pages/play/robOrder')">
				<view class="flex align-center">
					<image src="../../../static/images/my/qiangdans.png" style="width: 55rpx;height: 57rpx;"></image>
					<view class="flex-sub margin-left text-df">我的抢单</view>
				</view>
				<image src="../../../static/images/index/right.png" style="width: 15rpx;height: 30rpx;"></image>
			</view>
			<view class="flex padding-lr padding-tb justify-between align-center" @click="goGonghui()">
				<view class="flex align-center">
					<image src="../../../static/images/my/gonghui1.png" style="width: 60rpx;height:57rpx;">
					</image>
					<view class="flex-sub margin-left text-df">我的公会</view>
				</view>
				<image src="../../../static/images/index/right.png" style="width: 15rpx;height: 30rpx;"></image>
			</view>
			<!-- <view class="flex padding-lr padding-tb justify-between align-center" @click="goGonghui()" v-if="!Huizhang">
				<view class="flex align-center">
					<image src="../../../static/images/my/gonghui1.png" style="width: 70rpx;height: 70rpx;">
					</image>
					<view class="flex-sub margin-left text-df">我的公会</view>
				</view>
				<image src="../../../static/images/index/right.png" style="width: 15rpx;height: 30rpx;"></image>
			</view> -->
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
		<!-- 创建语聊房 -->
		<u-popup :scrollY="false" :mask="false" @close="closeRoom" v-model="createRoom" mode="bottom" border-radius="24"
			:closeable="true">
			<view class="createRoom">
				<view class="createRoom-title">
					创建房间
				</view>
				<view class="createRoom-item flex align-center">
					<text>房间类型：</text>
					<u-radio-group active-color="#AC75FE" v-model="createRoomInfo.roomType">
						<u-radio :name="1">
							<text style="color: #ffffff;">娱乐</text>
						</u-radio>
						<u-radio :name="2">
							<text style="color: #ffffff;">陪玩</text>
						</u-radio>
					</u-radio-group>
				</view>
				<view v-if="createRoomInfo.roomType!=1" class="createRoom-item flex align-center justify-between">
					<text>游戏类型：</text>
					<u-input @click="getGame()" height="80" disabled v-model="createRoomInfo.gameName"
						class="createRoom-item-inp" placeholder="请选择游戏类型" type="text" :border="false" />
					<u-icon class="arrow-right" name="arrow-right" color="#DCDFE6" size="28"></u-icon>
				</view>
				<view class="createRoom-item flex align-center justify-between">
					<text>房间名称：</text>
					<u-input height="80" v-model="createRoomInfo.roomName" class="createRoom-item-inp"
						placeholder="请输入房间名称" type="text" :border="false" />
				</view>
				<view class="createRoom-item flex align-center justify-between">
					<text>房间公告：</text>
					<u-input class="createRoom-item-inp" v-model="createRoomInfo.announcement" placeholder="请输入房间公告"
						type="textarea" :border="false" />
				</view>
				<view class="createRoom-item flex align-center justify-between">
					<text>房间玩法：</text>
					<u-input class="createRoom-item-inp" v-model="createRoomInfo.playingMethod" placeholder="请输入房间房间玩法"
						type="textarea" :border="false" />
				</view>
				<view class="createRoom-item flex align-center">
					<text>封面图片：</text>
				</view>
				<view class="createRoom-item" style="padding-left: 20rpx;padding-top: 0;">
					<shmily-drag-image :list.sync="sendImgList" :number="1"></shmily-drag-image>
				</view>
				<view class="createRoom-item flex align-center justify-between">
					<view @click="createRoom = false" class="createRoom-item-l flex align-center justify-center">
						取消
					</view>
					<view @click="createRoomFun()" class="createRoom-item-r flex align-center justify-center">
						创建房间
					</view>
				</view>

			</view>

		</u-popup>
		<!-- 游戏类型选择 -->
		<u-popup :scrollY="false" v-model="showGame" mode="bottom" border-radius="14" :closeable="true">
			<view class="gameView">
				<view class="gameView-title flex align-center justify-center">
					选择游戏分类
				</view>
				<view class="gameView-list">
					<scroll-view scroll-y="true" style="width: 100%;height: 100%;">
						<view class="flex align-center flex-wrap">
							<view @click="selectGame(item)" class="gameView-list-item" v-for="(item,index) in gameList"
								:key="index">
								{{item.gameName}}
							</view>
						</view>

					</scroll-view>

				</view>
			</view>

		</u-popup>
		<popup v-if="popupshowcl" :maskCloseAble="false" :show="popupshowcl" closeName="解散房间" confirmName="重新进入"
			@closeT="closeCl" @sureT="surecl" :content="contentcl"></popup>
	</view>
</template>

<script>
	// #ifdef H5
	import TRTC from 'trtc-sdk-v5';
	// // 创建trtc对象
	const trtc = TRTC.create();
	// #endif
	import shmilyDragImage from '../../../my/components/shmily-drag-image/shmily-drag-image.vue'
	import configdata from '../../../common/config.js'
	import popup from '@/components/popup.vue'
	export default {
		components: {
			shmilyDragImage,
			popup
		},
		data() {
			return {
				contentcl: '你当前有正在直播的房间，是否重新进入房间？',
				popupshowcl: false, //是否重连弹框
				showGame: false,
				gameList: [],
				sendImgList: [],
				createRoomInfo: {
					roomNo: '',
					roomName: '',
					roomImg: '',
					userId: uni.getStorageSync('userId'),
					announcement: '',
					playingMethod: '',
					roomType: 1,
					gameName: '',
					gameId: ''
				},
				createRoom: false,
				popupshow: false,
				content: '',

				userId: '',
				yueSelect: '否',
				invitationCode: '',
				money: '',
				userName: '匿名',
				avatar: '../../../static/logo.png',
				status: -1,
				safetyMoney: 0,
				isSafetyMoney: '',
				Huizhang: false,
				consortiaId: '',
				onLine: false,
				mylist: [],
				roomInfo: {
					isRoom: 0
				}, //房间信息
				isCreatRoomIng: false, //是否正在创建房间
			}
		},
		onLoad() {
			this.yueSelect = this.$queue.getData('yueSelect');
			this.invitationCode = this.$queue.getData('invitationCode');
			this.getMoney()
			this.getBannerList()
		},
		onShow() {
			this.userId = uni.getStorageSync('userId')

			if (this.userId) {
				this.isLogin = false
				this.getUserInfo()
				this.getMoney()
				this.getRenZheng()
			} else {
				this.isLogin = true
				this.userName = '匿名'
				this.safetyMoney = 0
				this.avatar = '../../static/logo.png'
			}
		},
		methods: {
			//服务端解散房间
			dismissRoom() {
				let data = {
					roomNo: this.roomInfo.strRoomId
				}
				this.$Request.getT('/app/room/dismissRoom', data).then(res => {
					if (res.code == 0) {
						//关闭弹框
						this.popupshowcl = false
						uni.hideLoading()
						uni.showToast({
							title: '房间已解散'
						})
						//清理一下缓存
						uni.removeStorageSync('startRoom')
						//重新调用一下个人信息
						this.getUserInfo()
						this.roomInfo = {
							strRoomId: '',
							roomId: '',
							userId: '',
							isRoom: 0
						}
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
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
			//取消重连
			closeCl() {
				// 关闭房间
				let that = this
				uni.showLoading({
					title: '解散房间中...'
				})
				let userId = uni.getStorageSync('token')
				// 连接scoket
				uni.connectSocket({
					url: that.config("APIHOST5") +
						`/roomChatSocket/${userId}/${that.roomInfo.roomId}`,
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
									//关闭scoket连接
									uni.closeSocket()
									//调用一下服务端解散房间的接口
									that.dismissRoom()
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
			//重连跳转
			surecl() {
				this.popupshowcl = false
				// #ifdef H5
				uni.navigateTo({
					url: '/my/voice/voiceH5?strRoomId=' + this.roomInfo.strRoomId + '&role=TRTCRoleAnchor' +
						'&roomId=' +
						this.roomInfo.roomId
				})
				// #endif
				// #ifdef APP
				uni.navigateTo({
					url: '/my/voice/voice?strRoomId=' + this.roomInfo.strRoomId + '&role=TRTCRoleAnchor' +
						'&roomId=' +
						this.roomInfo.roomId
				})
				// #endif
			},
			//创建房间
			creatRoomFun() {
				//判断是否有已经进入的房间//或者正在直播的房间
				if (uni.getStorageSync('startRoom') || Number(this.roomInfo.isRoom) === 1) {
					//如果我有正在直播的房间
					if (Number(this.roomInfo.isRoom) === 1) {
						this.popupshowcl = true
					} else { //直接退出房间
						let startRoom = uni.getStorageSync('startRoom')
						this.outRoom(startRoom.roomId)
					}
				} else { //没有进入的房间，也没有正在直播的房间
					this.createRoom = true
				}



				// //自己当前有正在直播的房间，则打开弹窗提示
				// if (Number(this.roomInfo.isRoom) === 1) {
				// 	// 打开房间提示弹框
				// 	console.log('1111111')
				// 	this.popupshowcl = true
				// } else { //当前自己没有正在直播的房间，直接打开出创建房间的弹窗
				// 	console.log('2222222')
				// 	this.createRoom = true
				// }
			},
			//退出房间（别人的房间）
			outRoom(roomId) {
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
									trtc.exitRoom();
									// that.$trtcCloud.exitRoom();
									uni.removeStorageSync('startRoom')
									//退出房间之后打开创建房间弹窗
									that.createRoom = true
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
			//创建房间
			createRoomFun() {
				let that = this
				if (this.createRoomInfo.roomType != 1) {
					if (!this.createRoomInfo.gameId) {
						uni.showToast({
							title: '请选择游戏分类',
							icon: 'none'
						})
						return
					}
				}
				if (!this.createRoomInfo.roomName) {
					uni.showToast({
						title: '请输入房间名称',
						icon: 'none'
					})
					return
				}
				if (!this.createRoomInfo.announcement) {
					uni.showToast({
						title: '请输入房间公告',
						icon: 'none'
					})
					return
				}
				if (!this.createRoomInfo.playingMethod) {
					uni.showToast({
						title: '请输入房间玩法',
						icon: 'none'
					})
					return
				}
				if (this.sendImgList.length == 0) {
					uni.showToast({
						title: '请上传封面图片',
						icon: 'none'
					})
					return
				}
				let data = {
					roomName: this.createRoomInfo.roomName,
					roomImg: this.sendImgList[0],
					userId: uni.getStorageSync('userId'),
					announcement: this.createRoomInfo.announcement,
					playingMethod: this.createRoomInfo.playingMethod,
					roomType: this.createRoomInfo.roomType,
					gameId: this.createRoomInfo.gameId
				}
				if (this.isCreatRoomIng) {
					return
				}
				uni.showLoading({
					title: '创建中...'
				})
				this.isCreatRoomIng = true
				this.$Request.postJson('/app/room/insertRoom', data).then(res => {
					if (res.code == 0) {
						uni.hideLoading(res.data.roomId, '获取的roomid')
						// #ifdef H5
						uni.navigateTo({
							url: '/my/voice/voiceH5?strRoomId=' + res.data.roomNo +
								'&role=TRTCRoleAnchor&roomId=' + res.data.roomId,
							complete() {
								that.createRoom = false
								that.isCreatRoomIng = false
							}
						})
						// #endif
						// #ifdef APP
						uni.navigateTo({
							url: '/my/voice/voice?strRoomId=' + res.data.roomNo +
								'&role=TRTCRoleAnchor&roomId=' + res.data.roomId,
							complete() {
								that.createRoom = false
								that.isCreatRoomIng = false
							}
						})
						// #endif

					} else {
						this.isCreatRoomIng = false
						uni.hideLoading()
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//选择游戏分类
			selectGame(item) {
				this.createRoomInfo.gameName = item.gameName
				this.createRoomInfo.gameId = item.id
				this.showGame = false
			},
			getGame() {
				this.$Request.get('/app/appGame/queryGameName').then(res => {
					if (res.code == 0) {
						this.gameList = res.data
						if (this.gameList.length > 0) {
							this.showGame = true
						}
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//关闭弹窗并清空数据
			closeRoom() {
				this.sendImgList = []
				this.createRoomInfo = {
					roomNo: '',
					roomName: '',
					roomImg: '',
					userId: uni.getStorageSync('userId'),
					announcement: '',
					playingMethod: '',
					roomType: 1,
					gameName: '',
					gameId: '',
				}
			},
			//获取技能  接单
			getBannerList() {
				this.$Request.get("/app/banner/selectBannerList", {
					classify: 9
				}).then(res => {
					if (res.code == 0) {
						this.mylist = res.data
					}
				});
			},
			updateState() {
				this.$queue.showLoading('切换中...')
				let onLine = 1;
				if (this.onLine) {
					onLine = 2;
				}
				this.$Request.postT('/app/user/updateOnLine?onLine=' + onLine).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						this.$queue.showToast('切换成功！');
						this.getUserInfo()
					} else {
						this.$queue.showToast(res.msg);
					}
				});
			},
			//是否会长
			getUserTd() {
				let data = {
					consortiaId: this.consortiaId
				}
				this.$Request.get("/app/consortia/selectConsortiaById", data).then(res => {
					if (res.code == 0) {
						let userId = res.data.userId
						let myUserId = this.$queue.getData('userId');
						if (myUserId == userId) {
							this.Huizhang = true
						} else {
							this.Huizhang = false
						}
						uni.setStorageSync("Huizhang", this.Huizhang)
					}
				});

			},
			//跳转公会
			goGonghui() {
				uni.navigateTo({
					url: '/package/pages/play/gonghui'
				})
			},
			getRenZheng() {
				this.$Request.get("/app/userCertification/queryInsert").then(res => {
					if (res.code == 0) {
						// 0审核中 1通过 2拒绝 
						if (res.data == null) {
							this.status = 0 //未实名
						} else if (res.data.status == 0) {
							this.status = 1 //审核中
						} else if (res.data.status == 1) {
							this.status = 2 //已实名
						} else if (res.data.status == 2) {
							this.status = 3 //已拒绝
						}
					}
				});
			},
			// 我的余额
			getMoney() {
				this.$Request.get("/app/userMoney/selectMyMoney").then(res => {
					if (res.code == 0 && res.data) {
						this.money = res.data.money
					}
				});
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						// #ifdef APP
						if (Number(res.data.isRoom) === 1) { //有正在直播的房间
							// 存储房间信息
							this.roomInfo = {
								strRoomId: res.data.roomNo,
								roomId: res.data.roomId,
								userId: res.data.userId,
								isRoom: 1
							}
						}

						// #endif
						this.consortiaId = res.data.consortiaId
						if (this.consortiaId) {
							this.getUserTd()
						}
						if (res.data.onLine && res.data.onLine == 1) {
							this.onLine = true;
						} else {
							this.onLine = false;
						}
						this.userName = res.data.userName
						// this.isSafetyMoney = res.data.isSafetyMoney
						this.invitationCode = res.data.invitationCode
						this.avatar = res.data.avatar ? res.data.avatar : '../../static/logo.png'

					}
				});
			},
			sureT() {
				uni.navigateTo({
					url: '/pages/public/login'
				})
				this.popupshow = false
			},
			goNav(e) {
				if (this.userId) {
					uni.navigateTo({
						url: e
					})
				} else {
					this.content = '您还未登录,请先登录'
					this.popupshow = true

				}
			},
		}
	}
</script>

<style lang="less">
	page {
		background: #111224;
	}

	.gameView {
		width: 100%;
		height: 700rpx;
		background-color: #1A1728;
		color: #FFFFFF;

		.gameView-title {
			width: 100%;
			height: 88rpx;
			text-align: center;
		}

		.gameView-list {
			width: calc(100% - 60rpx);
			height: calc(700rpx - 88rpx - 60rpx);
			margin: auto;
			margin-top: 30rpx;

			.gameView-list-item {
				padding: 20rpx 40rpx;
				background-color: #622a89;
				color: #FFFFFF;
				margin-bottom: 20rpx;
				margin-right: 20rpx;
				border-radius: 40rpx;
			}
		}
	}

	.createRoom {
		width: 100%;
		height: auto;
		color: #FFFFFF;
		background: #111224;
		padding-bottom: 40rpx;
	}

	.createRoom-title {
		width: 100%;
		text-align: center;
		font-size: 30rpx;
		font-weight: bold;
		padding-top: 30rpx;
	}

	.createRoom-item {
		padding: 30rpx 40rpx 0 40rpx;
		position: relative;

		.arrow-right {
			position: absolute;
			right: 60rpx;
		}

		.createRoom-item-inp {
			background: linear-gradient(90deg, #2A1D3F 0%, #2A1C3D 100%);
			padding: 0 20rpx !important;
			border-radius: 18rpx;
		}

		// background: #291B3D;
		.createRoom-item-uploud {
			width: 200rpx;
			height: 200rpx;
			border-radius: 12rpx;

			image {
				width: 100%;
				height: 100%;
				border-radius: 18rpx;
			}
		}

		.createRoom-item-l {
			width: 40%;
			height: 80rpx;
			border: 1px solid #AC75FE;
			border-radius: 44rpx;
			color: #AC75FE;
			margin-left: 40rpx;
		}

		.createRoom-item-r {
			width: 40%;
			height: 80rpx;
			border: 1px solid #AC75FE;
			background: #AC75FE;
			color: #FFFFFF;
			border-radius: 44rpx;
			margin-right: 40rpx;
		}
	}

	.head {

		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/20/c3e50814c6bb43cdeee6e046c93eae77.png');
		background-size: 100% 100%;
		/* #ifdef H5 */
		padding: 50rpx 0 20rpx 0;
		// height: 380rpx;
		/* #endif */

		/* #ifdef MP-WEIXIN */
		height: 320rpx;
		padding: 0rpx 0;
		/* #endif */

		/* #ifdef APP */
		// height: 320rpx;
		// padding-top: 40rpx
		padding: 40rpx 0 20rpx 0;
		/* #endif */
	}


	.bgs {
		color: #FFFFFF;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/20/6eeeba5bc2623b57ca015560779e0e5a.png');
		background-size: 100% 100%;
		background-repeat: no-repeat;
		/* background: #272842; */
		border-radius: 12upx;
		/* padding:0 20rpx; */
		padding: 20rpx;
	}

	.bgbox1 {
		background: #272842;
		padding: 40upx 30upx;
		border-radius: 24upx;
		margin: 30upx;
	}

	.btn {
		color: #FFFFFF;
		background: #2087fe;
		border-radius: 10upx;
		font-size: 28upx;
		padding: 14upx 25upx;
	}

	.look {

		/* width: 150upx;
		height: 60upx;
		text-align: center;
		line-height: 60upx;
		color: #FFFFFF;
		background: #2087fe;
		border-radius: 10upx; */
		image {
			width: 130rpx;
			height: 64rpx;
		}
	}

	.box {
		/* #ifdef APP */
		margin-top: 40rpx;
		/* #endif */
	}

	.lovip {
		width: 43rpx;
		height: 35rpx;
		position: relative;
		top: -38rpx;
		left: 26rpx;
	}

	.peibox {
		margin: 20rpx 30rpx;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/9cc004b3119efe39d9b66b942735de67.png');
		background-size: 100% 100%;
	}

	.label {
		display: inline-block;
		background-color: #6D709A;
		color: #FFFFFF;
		padding: 2upx 16upx;
		font-weight: 400;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/ac89e556a1a91a0c82456a588791c1bf.png');
		background-size: 100% 100%;
		border-radius: 48rpx;
		margin-left: 30rpx;
		font-size: 26rpx;

	}

	.sxx_view {
		width: 98rpx;
		height: 40rpx;
		margin-left: 20rpx;
	}
</style>
