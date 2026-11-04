<template>
	<view class="">
		<view class=" margin-lr">
			<view class="flex padding-tb-sm padding-lr-sm bg" @click="goGf(6)" style="border-radius: 24rpx;">
				<view>
					<image style="width: 80rpx;height: 80rpx;border-radius: 80rpx;"
						src="https://h5.dingjianpp.com/file/uploadPath/2023/02/08/59d114a7ff34a5fba68afcb1ebbc3bea.png">
					</image>
				</view>
				<view class="flex-sub margin-left-sm" style="margin-top:9rpx;">
					<view class="flex justify-between">
						<view class="text-white text-bold">官方公告</view>
						<view v-if="gfMessageCount>0" style="position: relative;">
							<u-badge :offset="[54,0]" type="error" :count="gfMessageCount"></u-badge>
						</view>

					</view>
					<view style="margin-top: 10rpx;">
						<!-- <view class="text-grey">{{item.content}}</view> -->
						<view class="" style="color: #D7BCFF;">您收到一条新的官方消息~</view>
					</view>
				</view>
			</view>
		</view>
		<view class="margin-top-sm margin-lr" v-if="status==2">
			<view class="flex padding-tb-sm padding-lr-sm bg" @click="goQDC()" style="border-radius: 24rpx;">
				<view>
					<image style="width: 80rpx;height: 80rpx;border-radius: 80rpx;"
						src="https://h5.dingjianpp.com/file/uploadPath/2023/02/08/ce27cadf4e74258874d43fc3eefe6015.png">
					</image>
				</view>
				<view class="flex-sub margin-left-sm" style="margin-top:9rpx;">
					<view class="flex justify-between">
						<view class="text-white text-bold">抢单池提示</view>
						<view v-if="qdcMessageCount>0" style="position: relative;">
							<u-badge :offset="[54,0]" type="error" :count="qdcMessageCount"></u-badge>
						</view>
					</view>
					<view style="margin-top: 10rpx;">
						<view class="" style="color: #D7BCFF;">没有订单？快来抢单吧！</view>
					</view>
				</view>
			</view>
		</view>

		<view v-if="msgList.length" class="margin-top-sm margin-lr">
			<view class="flex padding-tb  padding-lr-sm bg" @click="goMsg" v-for="(item,index) in msgList"
				style="border-radius: 24rpx;" :key='index'>
				<view>
					<image style="width: 80rpx;height: 80rpx;border-radius: 80rpx;"
						src="../../static/images/msg/msg.png"></image>
				</view>
				<view class="flex-sub margin-left-sm">
					<view class="flex justify-between">
						<view class="text-white">{{item.title?item.title: '系统消息'}}</view>
						<view v-if="messageCount>0" style="position: relative;">
							<u-badge :offset="[54,0]" type="error" :count="messageCount"></u-badge>
						</view>
					</view>
					<view style="margin-top: 10rpx;">
						<view style="color: #D7BCFF;">{{item.content}}</view>
					</view>
				</view>
			</view>
		</view>
		<view class="margin-top-sm margin-lr" @tap="goChat">
			<view class="flex padding-tb  padding-lr-sm bg " style="border-radius: 24rpx;">
				<view>
					<image style="width: 80rpx;height: 80rpx;border-radius: 80rpx;" src="../../static/logo.png"></image>
				</view>
				<view class="flex-sub margin-left-sm">
					<view class="flex justify-between">
						<view class="text-white">在线客服</view>
						<view style="position: relative;" v-if="adminmessageCount>0">
							<u-badge :offset="[56,0]" type="error" :count="adminmessageCount"></u-badge>
						</view>
					</view>
					<view style="margin-top: 10rpx;">
						<view style="color: #D7BCFF;">有问题请联系在线客服~</view>
					</view>
				</view>
			</view>
		</view>

		<view v-if="chatList.length" class="margin-top-sm  content  margin-lr ">
			<view class="padding-lr-sm bg" style="margin-top: 14rpx;border-radius: 24rpx;" @click="goIM(item)"
				v-for="(item,index) in chatList" :key='index'>
				<view class="flex padding-tb " v-if="userId == item.userId">
					<view>
						<u-image shape="circle" width='80rpx' height="80rpx"
							:src="item.byAvatar ? item.byAvatar : '/static/logo.png'"></u-image>
					</view>
					<view class="flex-sub margin-left-sm">
						<view class="flex justify-between">
							<view class="text-white text-30 text-cut" style="width: 260rpx;">{{item.byUserName}}</view>
							<view style="color: #D7BCFF;" class="text-26" v-if="item.messageTime">{{item.messageTime}}
							</view>
						</view>
						<view class="flex justify-between" style="padding-top: 10rpx;">
							<view v-if="item.messageType == 1"
								style="width: 500rpx;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #D7BCFF;">
								{{item.content}}
							</view>
							<view style="color: #D7BCFF;" v-if="item.messageType == 2">[图片]</view>
							<view style="color: #D7BCFF;" v-if="item.messageType == 3">[语音]</view>
							<view style="color: #D7BCFF;" v-if="item.messageType == 4">[表情]</view>
							<view style="position: relative;" v-if="item.contentCount">
								<u-badge :offset="[8,0]" type="error" :count="item.contentCount"></u-badge>
							</view>
						</view>
					</view>
				</view>
				<view class="flex padding-tb" v-else>
					<view>
						<u-image shape="circle" width='80rpx' height="80rpx"
							:src="item.avatar ? item.avatar : '/static/logo.png'"></u-image>
					</view>
					<view class="flex-sub margin-left-sm">
						<view class="flex justify-between">
							<view class="text-white text-30 text-cut" style="width: 260rpx;">{{item.userName}}</view>
							<view class="text-26" style="color: #D7BCFF;" v-if="item.messageTime">{{item.messageTime}}
							</view>
						</view>
						<view class="flex justify-between">
							<view
								style="width: 500rpx;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #D7BCFF;"
								v-if="item.messageType == 1">{{item.content}}</view>
							<view style="color: #D7BCFF;" v-if="item.messageType == 2">[图片]</view>
							<view style="color: #D7BCFF;" v-if="item.messageType == 3">[语音]</view>
							<view style="color: #D7BCFF;" v-if="item.messageType == 4">[表情]</view>
							<view style="position: relative;" v-if="item.contentCount">
								<u-badge :offset="[8,0]" type="error" :count="item.contentCount"></u-badge>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<empty v-if="!chatList.length && !msgList.length" content='暂无消息'></empty>

		<uni-popup ref="isPhonePopup" type="center" :mask-click="false">
			<view class=""
				style="width: 500rpx;height: 300rpx;background:linear-gradient(23deg, #37225D 0%, #313035 89%);border-radius: 24rpx;">
				<view class=""
					style="width: 100%;text-align: center;font-size: 32rpx;padding-top: 20rpx;font-weight: bold;">
					请授权手机号
				</view>
				<view class="" style="width: 100%;margin-top: 40rpx;text-align: center;">
					绑定手机号才可以使用全部功能
				</view>
				<view class="flex justify-center"
					style="width: 100%;margin-top: 60rpx;padding-bottom: 20rpx;height: 80rpx;">
					<button @click="qvxiaophone"
						style="width: 40%;border-radius: 100rpx;font-size: 24rpx;color: #FFFFFF;background: rgba(171, 116, 255, 0.3);box-sizing: border-box;"
						class="bottom flex justify-center align-center">
						取消
					</button>
					<button
						style="background: linear-gradient(90deg, #F386BD 0%, #A470F8 100%);border-radius: 100rpx;color: #FFFFFF;width: 40%;font-size: 24rpx;"
						class="bottom flex justify-center align-center" open-type="getPhoneNumber"
						@getphonenumber="getPhoneNumber">
						授权手机号
					</button>
				</view>

			</view>
		</uni-popup>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"
			:title="title"></popup>
		<!-- #ifdef APP || H5 -->
		<!-- 语聊房悬浮窗口 -->
		<drag-button v-if="isShowDrag" :startRoom="startRoom" @closeDrag="closeDrag" :isDock="true"
			:existTabBar="true" />
		<!-- #endif -->
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import empty from '../../components/empty.vue'
	import dragButton from "@/components/drag-button/drag-button.vue";
	export default {
		components: {
			empty,
			popup,
			dragButton
		},
		data() {
			return {
				isShowDrag: false,
				popupshow: false,
				content: '',
				title: '',

				SelKeFu: '2',
				page: 1,
				limit: 100,
				chatList: [],
				userId: '',
				msgList: [],
				time: '',
				messageCount: 0,
				// adminmessageCount:0,
				adminmessageCount: 0,
				arr: [],
				myarr: [],
				gfMessageCount: 0,
				qdcMessageCount: 0,
				status: -1,
				qdcSel: false,
				phone: '',
				startRoom: {}, //当前正在播放房间的信息
			}
		},
		onLoad() {
			this.getChatList()
			this.SelKeFu = this.$queue.getData('SelKeFu');
			let that = this;
			that.$Request.getT('/app/common/type/728').then(res => { //抢单提醒 728
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.myarr.push(res.data.value)
					}
				}
			})
			that.$Request.getT('/app/common/type/235').then(res => { //报名成功通知
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.arr.push(res.data.value)
					}
				}
			})
			that.$Request.getT('/app/common/type/236').then(res => { //活动即将开始提醒
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.arr.push(res.data.value)
					}
				}
			})
			that.$Request.getT('/app/common/type/240').then(res => { //消息推送
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.arr.push(res.data.value)
					}
				}
			})
		},
		onShow() {
			let that = this
			that.startRoom = {}
			// #ifdef APP || H5
			if (uni.getStorageSync('token')) {
				console.log(uni.getStorageSync('startRoom'), '22222222')
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
				that.getUserId()
			} else {
				that.isShowDrag = false
			}
			// #endif
			that.userId = uni.getStorageSync('userId')
			if (that.userId) {
				that.getUserInfo()
				that.getRenZheng()
				that.getRenZheng()
				that.time = setInterval(function() {
					that.getChatList()
					that.getMsgList()
					that.$nextTick(function() {
						that.messageCount = uni.getStorageSync('messageCount')
						that.adminmessageCount = uni.getStorageSync('adminmessageCount')
						that.gfMessageCount = uni.getStorageSync('systemMessageCount')
						that.qdcMessageCount = uni.getStorageSync('yueMessageCount')
					})

				}, 1000)

			} else {
				that.status = -1;
				that.qdcSel = false;
				that.chatList = []
				that.msgList = []
				that.gfList = []

			}
		},
		onHide() {
			clearInterval(this.time)
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
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						if (res.data.phone) {
							this.phone = res.data.phone;
						} else {
							this.phone = '';
						}
						uni.setStorageSync('consortiaId', res.data.consortiaId)
						uni.setStorageSync('isSafetyMoney', res.data.isSafetyMoney)
						uni.setStorageSync('avatar', res.data.avatar)
						uni.setStorageSync('invitationCode', res.data.invitationCode)
						uni.setStorageSync('zhiFuBao', res.data.zhiFuBao)
						uni.setStorageSync('zhiFuBaoName', res.data.zhiFuBaoName)
					}
				});
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
			//取消授权手机号
			qvxiaophone() {
				this.$refs.isPhonePopup.close();
			},
			sureT() {
				this.popupshow = false
			},
			//更新手机号
			upPhone(phone) {
				this.$Request.postT("/app/Login/wxBindPhone", {
					phone: phone,
				}).then(res => {
					if (res.code === 0) {
						uni.showToast({
							title: '更新成功'
						})
						this.$refs.isPhonePopup.close();
						uni.setStorageSync('token', res.token)
						uni.setStorageSync('userId', res.user.userId)
						this.getUserInfo();
						this.getRenZheng();
						this.getChatList()
						this.getMsgList()

					} else {
						uni.hideLoading();
						this.title = '更新失败'
						this.content = res.msg
						this.popupshow = true

						// this.$Pmessage({
						// 	showModel: true,
						// 	title: '更新失败',
						// 	content: res.msg,
						// 	maskcloseAble: false
						// }, {
						// 	emitBtn:()=> { //确认
						// 		console.log('用户点击确认');

						// 	},
						// 	emitClose:()=> { //取消
						// 		console.log('用户点击取消');
						// 	},
						// });

					}
				});
			},
			//授权成功后换取手机号
			setPhoneByInsert(decryptData, iv, session_key) {
				this.$queue.showLoading('更新中...');
				let data = {
					decryptData: decryptData,
					key: session_key,
					iv: iv
				};
				this.$Request.postJson('/app/Login/selectPhone', data).then(res => {
					if (res.code === 0) {
						uni.hideLoading();
						this.upPhone(res.data.phoneNumber)
					} else {
						uni.hideLoading();
						this.$queue.showToast(res.msg);
					}
				});
			},
			//手机号授权
			getPhoneNumber: function(e) {
				let _this = this
				console.log(e);
				if (e.detail.errMsg == 'getPhoneNumber:fail user deny') {
					console.log('用户拒绝提供手机号');
				} else {
					console.log('用户同意提供手机号');
					uni.login({
						provider: 'weixin',
						success: function(loginRes) {
							let code = loginRes.code;
							let data = {
								code: code
							};
							_this.$Request.getT("/app/Login/wxLogin", data).then(res => {
								if (res.code === 0) {
									_this.setPhoneByInsert(e.detail.encryptedData, e.detail.iv, res
										.data.session_key);
								}
							});
						},
					});

				}
			},
			goQDC() {
				// #ifdef MP-WEIXIN
				if (uni.getStorageSync('sendMsg')) {
					wx.requestSubscribeMessage({
						tmplIds: this.arr,
						success(re) {
							var datas = JSON.stringify(re);
							if (datas.indexOf("accept") != -1) {
								console.log(re)
							}
						},
						fail: (res) => {
							console.log(res)
						}
					})
				}
				// #endif
				// #ifdef MP-WEIXIN
				if (uni.getStorageSync('sendMyMsg')) {
					wx.requestSubscribeMessage({
						tmplIds: this.myarr,
						success(re) {
							var datas = JSON.stringify(re);
							if (datas.indexOf("accept") != -1) {
								console.log(re)
							}
						},
						fail: (res) => {
							console.log(res)
						}
					})
				}
				// #endif
				let token = this.$queue.getData('token');
				if (token) {
					if (!this.phone) {
						this.$refs.isPhonePopup.open();
						return;
					}
					if (this.status == 2) {

						let data = {
							page: 1,
							limit: 1,
							state: 7
						}
						this.$Request.getT('/app/message/selectMessageByUserId', data).then(res => {
							if (res.code === 0) {

							}
						});
						uni.navigateTo({
							url: '/package/pages/play/order'
						})
					}
				} else {
					this.goLoginInfo();
				}
			},
			goGf(type) {
				// #ifdef MP-WEIXIN
				if (uni.getStorageSync('sendMsg')) {
					wx.requestSubscribeMessage({
						tmplIds: this.arr,
						success(re) {
							var datas = JSON.stringify(re);
							if (datas.indexOf("accept") != -1) {
								console.log(re)
							}
						},
						fail: (res) => {
							console.log(res)
						}
					})
				}
				// #endif
				// #ifdef MP-WEIXIN
				if (uni.getStorageSync('sendMyMsg')) {
					wx.requestSubscribeMessage({
						tmplIds: this.myarr,
						success(re) {
							var datas = JSON.stringify(re);
							if (datas.indexOf("accept") != -1) {
								console.log(re)
							}
						},
						fail: (res) => {
							console.log(res)
						}
					})
				}
				// #endif
				let token = this.$queue.getData('token');
				if (token) {
					uni.navigateTo({
						url: '/pages/msg/gonggao?type=' + type
					})
				} else {
					this.goLoginInfo();
				}
			},
			// 在线客服
			goChat() {
				let that = this
				if (that.SelKeFu === '1') {
					// #ifdef MP-WEIXIN
					try {
						wx.openCustomerServiceChat({
							extInfo: {
								url: that.$queue.getData('SelKeFuLink')
							},
							corpId: that.$queue.getData('SelKeFuAppId'),
							success(res) {},
							fail(res) {
								console.error(res)
							}
						})
					} catch (error) {
						console.error("catchcatch" + error)
						uni.showToast({
							title: '请更新至微信最新版本'
						});
					}
					// #endif
					// #ifndef MP-WEIXIN
					let url = that.$queue.getData('SelKeFuLink');
					if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
						uni.navigateTo({
							url
						});
					} else {
						//#ifndef H5
						uni.navigateTo({
							url: '/pages/index/webView?url=' + url
						});
						//#endif
						//#ifdef H5
						window.location.href = url;
						//#endif
					}
					// #endif
				} else if (that.SelKeFu === '2') {
					let token = that.$queue.getData('token');
					if (token) {
						uni.navigateTo({
							url: '/my/setting/chat'
						});
					} else {
						that.goLoginInfo();
					}
				} else {
					uni.navigateTo({
						url: '/my/setting/kefu'
					})
				}
			},
			getChatList() {
				this.$Request.getMsg("/app/chat/selectChatConversationPage", {
					page: this.page,
					limit: this.limit
				}).then(res => {
					if (res.code == 0) {
						this.chatList = res.data.list
					}
				});
			},
			getMsgList() {
				this.$Request.getMsg("/app/message/selectMessageByUserIdLimit1").then(res => {
					if (res.code == 0) {
						this.msgList = res.data.list
					}
				});
			},
			goIM(e) {
				let userId = this.$queue.getData('userId');
				if (e.userId == userId) {
					userId = e.byUserId
				} else {
					userId = e.userId
				}
				let byNickName = ''
				if (e.userId == userId) {
					byNickName = e.userName
				} else {
					byNickName = e.byUserName
				}
				// uni.navigateTo({
				// 	url: '/pages/msg/im?chatConversationId='+e.chatConversationId+'&byUserId=' + userId
				// })
				let token = this.$queue.getData('token');
				if (token) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendMsg')) {
						wx.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) {
								if (datas.indexOf("accept") != -1) {
									console.log(re)
								}
							},
							fail: (res) => {
								console.log(res)
							}
						})
					}
					// #endif
					uni.navigateTo({
						url: '/pages/msg/chat?chatConversationId=' + e.chatConversationId + '&byUserId=' + userId +
							'&byNickName=' + byNickName
					})
				} else {
					this.goLoginInfo();
				}
			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/login'
				})
			},
			goMsg() {
				let token = this.$queue.getData('token');
				if (token) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendMsg')) {
						wx.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) {
								if (datas.indexOf("accept") != -1) {
									console.log(re)
								}
							},
							fail: (res) => {
								console.log(res)
							}
						})
					}
					// #endif
					uni.navigateTo({
						url: '/pages/msg/message'
					})
				} else {
					this.goLoginInfo();
				}
			}
		}
	}
</script>

<style>
	page {
		background-color: #1A1728;
	}

	.bg {
		background-color: #291B3D;
	}
</style>
