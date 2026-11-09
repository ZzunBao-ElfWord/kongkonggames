<template>
	<view>
		<view style="width: 100%;padding-bottom: 140rpx;">
			<view style="display: flex;flex-direction: column;" v-for="(item,index) in ListItem" :key='index'>
				<view style="margin-top: 15rpx;width: 100%;text-align: center;font-size: 26rpx;color: #999999;">
					{{item.createTime}}
				</view>
				<view v-if="item.sendType === 2" style="width: 83%;margin-right: 15%;">
					<view class="chat-listitem" style="float: left;margin-left: 10rpx;">
						<view>
							<image src="../../static/logo.png" class="chat-listitem-image"></image>
						</view>
						<view v-if="item.content && item.type === 1" class="chat-listitem-text"
							style="margin-left: 20rpx;">{{item.content}}</view>
						<image @tap="viewImg(item.content)" v-if="item.content && item.type === 2" :src="item.content"
							style="height: 200rpx;width: 200rpx;margin-left: 20rpx;"></image>
					</view>
				</view>

				<view v-if="item.sendType === 1" style="width: 83%;margin-left: 15%;">
					<view class="chat-listitem" style="float: right;">
						<view v-if="item.content && item.type === 1" @longpress="copy(item.content)"
							class="chat-listitem-text" style="margin-right: 20rpx;">{{item.content}}</view>
						<view v-if="item.content && item.type === 4" @click="goShop(item.content[3])"
							style="width: 400rpx;background: #FFFFFF;height: max-content;margin-right: 20rpx;margin-top: 10rpx;border-radius: 20rpx;">
							<image :src="item.content[0]" class="chat-listitem-image-type4"
								style="width: 400rpx;height: 350rpx;"></image>
							<view style="padding: 10rpx;padding-bottom: 20rpx;">
								<view style="color: #ed5732;font-size: 34rpx;"><text style="font-size: 22rpx;">￥ </text>
									{{item.content[2]}}
								</view>
								<view
									style="overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;width: 100%;height:  75upx;">
									{{item.content[1]}}
								</view>
							</view>
						</view>
						<view v-if="item.content && item.type === 3"
							style="width: 500rpx;background: #FFFFFF;height: max-content;margin-right: 20rpx;margin-top: 10rpx;border-radius: 20rpx;padding: 15rpx 10rpx;">
							<view style="color: #000000;font-weight: 600;margin-left: 10rpx;">你正在咨询的订单</view>
							<view style="display: flex;">
								<image :src="item.content[0]" class="chat-listitem-image-type4"
									style="margin-left: 7rpx;margin-top: 20rpx;width: 110rpx;height: 110rpx;"></image>
								<view
									style="margin-top: 15rpx;padding: 10rpx 0rpx 5rpx 10rpx;width: 75%;background: #f5f5f5;margin-left: 10rpx;">
									<view
										style="font-size: 28rpx;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;height:  75upx;width: 100%;">
										{{item.content[1]}}
									</view>
									<view style="color: #ed5732;font-size: 28rpx;"><text style="font-size: 22rpx;">￥
										</text>{{item.content[5]}}
									</view>
								</view>
							</view>
							<view style="color: #999999;margin-top: 10rpx;margin-left: 12rpx;">
								<view>订单编号：{{item.content[3]}}</view>
								<view style="margin-top: 10rpx;">创建时间：{{item.content[4]}}</view>
							</view>
							<view
								style="float: right;margin-right: 10rpx;margin-top: 15rpx;background: #F9221D;color: #FFFFFF;border-radius: 50rpx;width: 150rpx;height: 50rpx;font-size: 24rpx;text-align: center;line-height: 50rpx;"
								@click="goDingdanDetail(item.content[2])">查看</view>
						</view>
						<image @tap="viewImg(item.content)" v-if="item.content && item.type === 2" :src="item.content"
							style="height: 200rpx;width: 200rpx;margin-right: 20rpx;"></image>
						<view>
							<image v-if="item.chat.userHead" :src="item.chat.userHead" class="chat-listitem-image">
							</image>
							<image v-else src="../../static/logo.png" class="chat-listitem-image"></image>
						</view>
					</view>
				</view>
				<!-- <view v-if="item.sendType === 4" style="width: 83%;margin-left: 15%;">
					<view class="chat-listitem" style="float: right;">
						<view style="height: max-content;">
							<image :src="type4[0]" mode=""></image>
						</view>
						<image @tap="viewImg(item.content)" v-if="item.content && item.type === 2" :src="item.content" style="height: 200rpx;width: 170rpx;margin-right: 20rpx;"></image>
						<view>
							<image :src="item.chat.userHead" class="chat-listitem-image"></image>
						</view>
					</view>
				</view> -->
			</view>
		</view>
		<view v-if="ShopState"
			style="width: 95%;margin-left: 20rpx;height: 150upx;position: fixed;bottom: 120upx;z-index: 99;background-color: #FFFFFF;border-radius: 20rpx;">
			<view style="display: flex;width: 100%;color: #000000;padding: 20rpx;">
				<image :src="Shopimage" style="width: 110rpx;height: 110rpx;"></image>
				<view style="margin-left: 20rpx;width: 400rpx;">
					<view
						style="font-size: 34rpx;color: #000000;overflow: hidden;text-overflow: ellipsis;flex-wrap: nowrap;white-space: nowrap;width: 98%;">
						{{ShopTitle}}
					</view>
					<view style="margin-top: 20rpx;color: #ed5732;font-size: 34rpx;">￥{{Shopmoney}}</view>
				</view>
				<view style="text-align: right;">
					<image @click="ShopClose" src="../../static/images/msg/close.png"
						style="width: 30rpx;height: 30rpx;"></image>
					<view
						style="margin-top: 20rpx;background: #F9221D;color: #FFFFFF;border-radius: 50rpx;width: 150rpx;height: 50rpx;font-size: 24rpx;text-align: center;line-height: 50rpx;"
						@click="goMaijia">发送给商家</view>
				</view>
			</view>
		</view>
		<view v-if="orderState"
			style="width: 95%;margin-left: 20rpx;height: 150upx;position: fixed;bottom: 120upx;z-index: 99;background-color: #FFFFFF;border-radius: 20rpx;">
			<view style="display: flex;width: 100%;color: #000000;padding: 20rpx;">
				<image :src="orderimage" style="width: 110rpx;height: 110rpx;"></image>
				<view style="margin-left: 20rpx;width: 400rpx;">
					<view
						style="font-size: 34rpx;color: #000000;overflow: hidden;text-overflow: ellipsis;flex-wrap: nowrap;white-space: nowrap;width: 98%;">
						你可能想咨询该订单</view>
					<view style="margin-top: 20rpx;color: #ed5732;font-size: 34rpx;">￥{{ordermoney}}</view>
				</view>
				<view style="text-align: right;">
					<image @click="orderClose" src="../../static/images/msg/close.png"
						style="width: 30rpx;height: 30rpx;"></image>
					<view
						style="margin-top: 20rpx;background: #F9221D;color: #FFFFFF;border-radius: 50rpx;width: 150rpx;height: 50rpx;font-size: 24rpx;text-align: center;line-height: 50rpx;"
						@click="goDingdan">发送订单</view>
				</view>
			</view>
		</view>
		<!-- 底部聊天输入框 -->
		<view class="input-box">
			<view class="justify-between padding-lr"
				style="display: flex;margin-top: 15rpx;width: 100%;padding-top: 4upx;">
				<!-- <image src="../../static/images/msg/add.png" @click="chooseImage(['album'])"
					style="width: 60rpx;height: 60rpx;margin-top: 8rpx;margin-right: 12rpx;color: #FFFFFF;"></image> -->
				<view class="more" style="align-items: flex-start;margin-top: 10upx;" @click="chooseImage(['album'])">
					<view class="icon add"></view>
				</view>
				<input confirm-type="send" @confirm='setChatSave(1)' type="text" v-model="content"
					style="width: 72%;height: 70rpx;background: #2A2A32;margin: 4rpx 10rpx 0;border-radius: 70rpx;padding-left: 10rpx;color: #fff;" />
				<view class="save" @tap='setChatSave(1)'>发送</view>
			</view>
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content1">
		</popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import configdata from '../../common/config.js';
	export default {
		components: {
			popup
		},
		data() {
			return {
				popupshow: false,
				content1: '',
				copyContent: '',
				popupType: 0,

				connected: false,
				connecting: false,
				msg: false,
				type4: [],
				listRight: {
					chat: {
						userHead: ""
					},
					content: "",
					sendType: 1,
					type: 1
				},
				content: '',
				chatId: '',
				type: 1,
				ListItem: [],
				ShopState: false,
				ShopordersId: '',
				Shopimage: '',
				Shopmoney: '',
				ShopTitle: '',
				orderState: false,
				ordersId: '',
				orderimage: '',
				orderNum: '',
				ordermoney: '',
				orderTitle: '',
				orderCreateTime: '',
				className: '',
				Shopsales: '',
				hand: 1,
				index: 0,
				page: 0,
				size: 1000,
				countDown: ''
			};
		},
		computed: {
			showMsg() {
				if (this.connected) {
					if (this.msg) {
						return '收到消息：' + this.msg
					} else {
						return '等待接收消息'
					}
				} else {
					return '尚未连接'
				}
			}
		},
		onUnload() {
			uni.closeSocket()
			uni.hideLoading()
		},
		onLoad(d) {
			if (d.className) {
				this.className = d.className;
				if (d.className === 'shop') {
					this.ShopState = true;
					this.ShopordersId = d.ordersId;
					this.Shopimage = d.image;
					this.Shopmoney = d.money;
					this.Shopsales = d.sales;
					this.ShopTitle = d.title;
				} else if (d.className === 'order') {
					this.orderState = true;
					this.ordersId = d.id;
					this.orderimage = d.image;
					this.ordermoney = d.money;
					this.orderTitle = d.title;
					this.orderNum = d.orderNum;
					this.orderCreateTime = d.createTime;
				}
			}
			this.getChatSave();
			this.connect();
		},
		onShow() {
			if (this.connected || this.connecting) {

			} else {
				this.connect();
			}
		},
		onHide() {
			uni.closeSocket()
		},
		methods: {
			sureT() {
				this.popupshow = false
				if (this.popupType == 1) {
					let that = this
					uni.setClipboardData({
						data: that.copyContent,
						success: r => {
							this.$queue.showToast('复制成功');
						}
					});
				} else if (this.popupType == 0) {
					uni.navigateBack()
				} else if (this.popupType == 3) {

				}
			},
			copy(content) {
				this.copyContent = content
				this.popupType = 1
				this.content1 = '确认要复制此文字吗?'
				this.popupshow = true
				// this.$Pmessage({
				// 	showModel: true,
				// 	title: '温馨提示',
				// 	content: '确认要复制此文字吗？',
				// 	maskcloseAble: false
				// }, {
				// 	emitBtn: () => { //确认
				// 		uni.setClipboardData({
				// 			data: content,
				// 			success: r => {
				// 				this.$queue.showToast('复制成功');
				// 			}
				// 		});
				// 	},
				// 	emitClose: () => { //取消
				// 		console.log('用户点击取消');
				// 	},
				// });

			},
			h5Copy(content) {
				if (!document.queryCommandSupported('copy')) {
					// 不支持
					return false
				}
				let textarea = document.createElement("textarea")
				textarea.value = content
				textarea.readOnly = "readOnly"
				document.body.appendChild(textarea)
				textarea.select() // 选择对象
				textarea.setSelectionRange(0, content.length) //核心
				let result = document.execCommand("copy") // 执行浏览器复制命令
				textarea.remove()
				return result
			},
			getDateDiff(data) {
				// 传进来的data必须是日期格式，不能是时间戳
				//var str = data;
				//将字符串转换成时间格式
				var timePublish = new Date(data);
				var timeNow = new Date();
				var minute = 1000 * 60;
				var hour = minute * 60;
				var day = hour * 24;
				var month = day * 30;
				var result = "2";

				var diffValue = timeNow - timePublish;
				var diffMonth = diffValue / month;
				var diffWeek = diffValue / (7 * day);
				var diffDay = diffValue / day;
				var diffHour = diffValue / hour;
				var diffMinute = diffValue / minute;


				if (diffMonth > 3) {
					result = timePublish.getFullYear() + "-";
					result += timePublish.getMonth() + "-";
					result += timePublish.getDate();
				} else if (diffMonth > 1) { //月
					result = data.substring(0, 10);
				} else if (diffWeek > 1) { //周
					result = data.substring(0, 10);
				} else if (diffDay > 1) { //天
					result = data.substring(0, 10);
				} else if (diffHour > 1) { //小时
					result = parseInt(diffHour) + "小时前";
				} else if (diffMinute > 1) { //分钟
					result = parseInt(diffMinute) + "分钟前";
				} else {
					result = "刚刚";
				}
				return result;
			},
			goDingdanDetail(id) {
				uni.navigateTo({
					url: '../member/orderdetail?id=' + id
				});
			},
			goShop(ordersId) {
				uni.navigateTo({
					url: './commoditydetail?ordersId=' + ordersId
				});
			},
			ShopClose() {
				this.ShopState = false;
			},
			orderClose() {
				this.orderState = false;
			},
			goDingdan() {
				this.orderState = false;
				this.setChatSave(3);
			},
			goMaijia() {
				this.ShopState = false;
				this.setChatSave(4);
			},
			connect() {
				let userId = this.$queue.getData('userId');
				if (this.connected || this.connecting) {
					this.popupType = 3
					this.content1 = '正在连接或者已经连接，请勿重复连接'
					this.popupshow = true
					// this.$Pmessage({
					// 	showModel: true,
					// 	title: '温馨提示',
					// 	content: '正在连接或者已经连接，请勿重复连接',
					// 	maskcloseAble: false
					// }, {
					// 	emitBtn: () => { //确认

					// 	},
					// 	emitClose: () => { //取消
					// 		console.log('用户点击取消');
					// 	},
					// });

					return false
				}
				let token = uni.getStorageSync('token')
				this.connecting = true
				uni.showLoading({
					title: '连接中...'
				})
				uni.connectSocket({
					// url: 'wss://game.shengqianxiong.com.cn/wss/websocket/' + userId,
					// url: 'ws://192.168.1.17:8180/pwyl-api/websocket/' + userId,
					url: this.config("WSHOST") + userId,
					data() {
						return {
							msg: 'Hello'
						}
					},
					header: {
						'content-type': 'application/json',
						'token': token
					},
					method: 'GET',
					success(res) {
						// 这里是接口调用成功的回调，不是连接成功的回调，请注意
					},
					fail(err) {
						// 这里是接口调用失败的回调，不是连接失败的回调，请注意
					}
				})
				uni.onSocketOpen((res) => {
					this.connecting = false
					this.connected = true
					uni.hideLoading()
					// uni.showToast({
					// 	icon: 'none',
					// 	title: '连接成功'
					// })
					console.log('onOpen', res);
				})
				uni.onSocketError((err) => {
					this.connecting = false
					this.connected = false
					uni.hideLoading()
					this.popupType = 3
					this.content1 = '网络较差，请稍后再试'
					this.popupshow = true
					// this.$Pmessage({
					// 	showModel: true,
					// 	title: '提示',
					// 	content: '网络较差，请稍后再试',
					// 	maskcloseAble: false
					// }, {
					// 	emitBtn: () => { //确认
					// 	},
					// 	emitClose: () => { //取消
					// 		console.log('用户点击取消');
					// 	},
					// });
				})
				uni.onSocketMessage((res) => {
					// let that = this;
					// let datas = JSON.parse(res.data)
					// let data = {
					// 	chat: {
					// 		userHead: '../../static/logo.png'
					// 	},
					// 	content: datas.content,
					// 	type: datas.type,
					// 	sendType: datas.sendType
					// }
					// that.ListItem.push(data);
					this.getTimeOrListItem1();
					console.log('onMessage', res)
				})
				uni.onSocketClose((res) => {
					this.connected = false
					this.startRecive = false
					this.msg = false
					console.log('onClose', res)
				})
			},
			close() {
				uni.closeSocket()
			},
			getTimeOrListItem1() {
				this.$Request.getT('/app/chats/list?chatId=' + this.chatId).then(
					res => {
						this.ListItem = [];
						if (res.data) {
							var time = '';
							res.data.forEach(d => {
								d.createTime = this.getDateDiff(d.createTime);
								if (d.createTime === time) {
									d.createTime = '';
								} else {
									time = d.createTime;
								}
								if (!d.chat.userHead) {
									// d.chat.userHead = '../../static/logo.png';
									let avatar = this.$queue.getData('avatar');
									d.chat.userHead = avatar
								}
								if (d.type === 4) {
									let data = d.content.split(',');
									d.content = data;
								}
								if (d.type === 3) {
									let data = d.content.split(',');
									d.content = data;
								}
								this.ListItem.push(d);
							});
							setTimeout(() => {
								uni.pageScrollTo({
									scrollTop: 99999,
									duration: 0
								});
							}, 50);
						}
						uni.hideLoading();
					});
			},
			getChatSave() {
				let userId = this.$queue.getData('userId');
				let phone = this.$queue.getData('phone');
				let userName = this.$queue.getData('userName');
				if (!phone) {
					phone = this.$queue.getData('userName');
				}
				let avatar = this.$queue.getData('avatar');
				let data = {
					userId: userId,
					userHead: avatar,
					userName: userName,
					storeId: '0',
					storeHead: '空空电竞',
					storeName: ''
				}
				this.$Request.postJson('/app/chats/save', data).then(res => {
					if (res.status === 0) {
						this.chatId = res.data.chatId;
						uni.showLoading({
							title: '加载中...'
						});
						this.getTimeOrListItem1();
					}
				});
			},
			setChatSave(type) {
				//type:1文字 2图片
				if (type === 1 && this.content == '') {
					this.$queue.showToast('请输入聊天内容');
					return;
				}
				if (this.chatId == '' || this.chatId == undefined) {
					this.$queue.showToast('网络较差，请稍后再试');
					return;
				}
				let userId = this.$queue.getData('userId');
				if (type === 4) {
					this.content = this.Shopimage + ',' + this.ShopTitle + ',' + this.Shopmoney + ',' + this.ShopordersId;
				}
				if (type === 3) {
					this.content = this.orderimage + ',' + this.orderTitle + ',' + this.ordersId + ',' + this.orderNum +
						',' + this.orderCreateTime +
						',' + this.ordermoney
				}
				let data = {
					userId: userId,
					content: this.content,
					chatId: this.chatId,
					type: type,
					storeId: '0',
					sendType: '1'
				}
				data = JSON.stringify(data);
				let that = this;
				uni.sendSocketMessage({
					data: data,
					success(res) {
						let avatar = that.$queue.getData('avatar');
						if (!avatar) {
							avatar = '../../static/logo.png';
						}
						// let data = {
						// 	chat: {
						// 		userHead: avatar
						// 	},
						// 	content: that.content,
						// 	type: type,
						// 	sendType: 1
						// }
						// that.ListItem.push(data);
						setTimeout(() => {
							that.getTimeOrListItem1();
						}, 50);
						console.log(that.content);
					},
					fail(err) {
						console.log(err);
					}
				})
				this.content = '';
			},
			//发送图片
			chooseImage(sourceType) {

				uni.chooseImage({
					count: 1,
					sourceType: ['album', 'camera'],
					success: res => {
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							this.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								url: this.config("APIHOST1") + '/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									this.content = JSON.parse(uploadFileRes.data).data;
									this.setChatSave(2);
									uni.hideLoading();
								}
							});
						}
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
			//查看大图
			viewImg(item) {
				let imgsArray = [];
				imgsArray[0] = item;
				uni.previewImage({
					current: 0,
					urls: imgsArray
				});
			},
		},
	};
</script>

<style lang="scss">
	@import "../../pages/msg/css/style.scss";

	page {
		background: #1c1b20;
	}

	::v-deep .textMoney {
		text-align: center;
	}

	.input-box {
		position: fixed;
		bottom: 0;
		left: 0;
		height: 100rpx;
		width: 100%;
		display: flex;
		box-sizing: content-box;
		z-index: 999;
		/* background-color: #ececec; */
		/* padding: 0 5rpx; */
	}

	.chat-listitem {
		display: flex;
		margin-top: 20rpx;
		padding: 10rpx;
	}

	.chat-listitem-text {
		color: #000000;
		background: #FFFFFF;
		margin-top: 10rpx;
		width: fit-content;
		padding: 15rpx;
		font-size: 30rpx;
		height: max-content;
		word-wrap: break-word;
		word-break: break-all;
		border-radius: 10rpx;
	}

	.chat-listitem-image-type4 {
		color: #000000;
		background: #FFFFFF;
		width: fit-content;
		font-size: 30rpx;
		height: max-content;
		word-wrap: break-word;
		word-break: break-all;
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;
	}

	.chat-listitem-image {
		margin-top: 5rpx;
		width: 75rpx;
		height: 75rpx;
		border-radius: 5rpx;
	}

	.save {
		width: 130rpx;
		text-align: center;
		border-radius: 10rpx;
		height: 70rpx;
		color: #0A0A0C;
		background: #FBCA0C;
		margin: 5rpx 10rpx 0;
		line-height: 70rpx;
	}
</style>