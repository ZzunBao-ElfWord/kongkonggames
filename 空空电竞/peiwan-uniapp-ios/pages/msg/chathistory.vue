<template>
	<view class="task">
		<!-- 搜索板块 -->
		<view class="index-header">
			<view class="">
				<!-- 搜索板块 -->
				<view class="tui-header">
					<view class="tui-search" style="height: 60upx; width: 600upx;">
						<input type="text" class="text-search" v-model="search" placeholder="请输入搜索内容" />
					</view>
					<view @tap='rtBubble' style="color: #FFFFFF; font-size: 15px;">搜索</view>
					<view></view>
				</view>
			</view>
		</view>
		<view style="width: 100%;padding-bottom: 140rpx;background: #111224;color: #FFFFFF;margin-top: 80rpx;">
			<view style="display: flex;flex-direction: column;" v-for="(item,index) in ListItem">
				<view style="margin-top: 15rpx;width: 100%;text-align: center;font-size: 26rpx;color: #999999;">
					{{item.createTime}}
				</view>
				<view v-if="item.userId && userId != item.userId" style="width: 83%;margin-right: 15%;">
					<view class="chat-listitem" style="float: left;margin-left: 10rpx;">
						<view>
							<image @tap="goGuanZhu(item)" :src="item.imageUrl ? item.imageUrl : '/static/logo.png'"
								class="chat-listitem-image"></image>
						</view>
						<view v-if="item.content && item.messageType === 1" class="chat-listitem-text"
							style="margin-left: 20rpx;">{{item.content}}</view>
						<image @tap="viewImg(item.content)" v-if="item.content && item.messageType === 2"
							:src="item.content" style="height: 200rpx;width: 200rpx;margin-left: 20rpx;"></image>
						<view class="chat-listitem-text" style="background: transparent;padding: 0;"
							v-if="item.content && item.messageType === 4">
							<image @tap="viewImg(item.content)" :src="'https://pw.xianmxkj.com/emoji/' +item.content"
								style="height: 80rpx;width: 80rpx;margin-left: 20rpx;"></image>
						</view>


						<view v-if="item.content && item.messageType === 3" class="chat-listitem-text bubble voice"
							:class="playMsgid == index?'play':''"
							style="margin-left: 20rpx;display: flex;align-items: center;"
							@tap="playVoice(item.content,index)">
							<image v-if="playMsgid != index" src="../../static/pw/laba.png"
								style="width: 35upx;height: 40upx;margin-right: 10rpx;"></image>
							<image v-if="playMsgid == index" src="../../static/pw/labagif.gif"
								style="width: 35upx;height: 40upx;margin-right: 10rpx;"></image>
							<!-- <view class="icon other-voice"></view> -->
							<view class="length">{{item.voiceLength ? item.voiceLength : 0}}</view>
						</view>


					</view>
				</view>

				<view v-if="item.userId && userId == item.userId" style="width: 83%;margin-left: 15%;">
					<view class="chat-listitem" style="float: right;">
						<view v-if="item.content && item.messageType === 1" @longpress="copy(item.content)"
							class="chat-listitem-text" style="margin-right: 20rpx;">{{item.content}}</view>
						<image @tap="viewImg(item.content)" v-if="item.content && item.messageType === 2"
							:src="item.content" style="height: 200rpx;width: 200rpx;margin-right: 20rpx;"></image>
						<view class="chat-listitem-text" style="background: transparent;padding: 0;"
							v-if="item.content && item.messageType === 4">
							<image @tap="viewImg(item.content)" :src="'https://pw.xianmxkj.com/emoji/' +item.content"
								style="height: 80rpx;width: 80rpx;margin-right: 20rpx;">
							</image>
						</view>
						<view v-if="item.content && item.messageType === 3" class="chat-listitem-text bubble voice"
							:class="playMsgid == index?'play':''"
							style="margin-right: 20rpx;display: flex;align-items: center;"
							@tap="playVoice(item.content,index)">
							<view class="length">{{item.voiceLength ? item.voiceLength : 0}}</view>
							<image v-if="playMsgid != index" src="../../static/pw/labaleft.png"
								style="width: 35upx;height: 40upx;margin-left: 10rpx;"></image>
							<image v-if="playMsgid == index" src="../../static/pw/labaleft.png"
								style="width: 35upx;height: 40upx;margin-left: 10rpx;"></image>
							<!-- <view class="icon other-voice"></view> -->
						</view>
						<view>
							<image :src="imageUrl" class="chat-listitem-image"></image>
						</view>
					</view>
				</view>
			</view>
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content1">
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
				content1: '',
				copyContent: '',

				recordLength: 0,
				initPoint: {
					identifier: 0,
					Y: 0
				},
				//播放语音相关参数
				AUDIO: uni.createInnerAudioContext(),
				playMsgid: null,
				VoiceTimer: null,


				//文字消息
				textMsg: '',
				//消息列表
				isHistoryLoading: false,
				scrollAnimation: false,
				scrollTop: 0,
				scrollToView: '',
				msgList: [],
				msgImgList: [],
				myuid: 0,

				//录音相关参数
				// #ifndef H5
				//H5不能录音
				RECORDER: uni.getRecorderManager(),
				// #endif
				isVoice: false,
				voiceTis: '按住 说话',
				recordTis: "手指上滑 取消发送",
				recording: false,
				willStop: false,
				initPoint: {
					identifier: 0,
					Y: 0
				},
				recordTimer: null,
				recordLength: 0,

				//播放语音相关参数
				AUDIO: uni.createInnerAudioContext(),
				playMsgid: null,
				VoiceTimer: null,
				// 抽屉参数
				popupLayerClass: '',
				// more参数
				hideMore: true,

				page: 1,
				size: 100,
				byUserId: 0,
				playMsgid: 0,
				imageUrl: '/static/logo.png',
				chatConversationId: 0,
				userId: 0,
				ListItem: [],
				search: ''
			};
		},
		onLoad(d) {
			this.userId = this.$queue.getData('userId');
			this.chatConversationId = d.chatConversationId;
			this.getTimeOrListItem1();

			let image_url = this.$queue.getData('avatar');
			if (image_url && image_url !== 'undefined') {
				this.imageUrl = image_url;
			} else {
				this.imageUrl = '/static/logo.png';
			}

			//语音自然播放结束
			this.AUDIO.onEnded((res) => {
				this.playMsgid = null;
			});
			// #ifndef H5
			//录音开始事件
			this.RECORDER.onStart((e) => {
				this.recordBegin(e);
			})
			//录音结束事件
			this.RECORDER.onStop((e) => {
				this.recordEnd(e);
			})
			// #endif
		},
		onBackPress() {
			this.AUDIO.stop();
			this.playMsgid = null;
		},
		onHide() {
			this.AUDIO.stop();
			this.playMsgid = null;
		},
		methods: {
			goGuanZhu(item) {
				//去关注页面
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + item.userId
				});
				// uni.navigateTo({
				// 	url: '/package/pages/task/followUser?userId=' + item.userId
				// });
			},
			// 播放语音
			playVoice(msg, index) {
				this.playMsgid = index;
				this.AUDIO.src = msg;
				this.$nextTick(function() {
					this.AUDIO.play();
				});
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
			getTimeOrListItem1() {
				this.$queue.showLoading('加载中...');
				let userId = this.$queue.getData('userId')
				this.$Request.getT('/app/chat/selectChatContent?page=' + this.page + '&limit=' + this.size +
						'&chatConversationId=' + this.chatConversationId + '&userId=' + userId + '&content=' + this.search)
					.then(
						res => {
							this.ListItem = [];
							if (res.data) {
								var time = '';
								res.data.list.forEach(d => {
									d.createTime = this.getDateDiff(d.createTime);
									if (d.createTime === time) {
										d.createTime = '';
									} else {
										time = d.createTime;
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
								this.ListItem.reverse();
							}
							uni.hideLoading();
						});
			},
			sureT() {
				let that = this
				uni.setClipboardData({
					data: that.copyContent,
					success: r => {
						that.$queue.showToast('复制成功');
					}
				});
			},
			copy(content) {
				let that = this
				that.copyContent = content
				that.content1 = '确认要复制此文字吗？'
				that.popupshow = true
				// that.$Pmessage({
				// 	showModel: true,
				// 	title: '温馨提示',
				// 	content: '确认要复制此文字吗？',
				// 	maskcloseAble: false
				// }, {
				// 	emitBtn:()=> { //确认
				// 		console.log('用户点击确认');
				// 		uni.setClipboardData({
				// 			data: content,
				// 			success: r => {
				// 				that.$queue.showToast('复制成功');
				// 			}
				// 		});
				// 	},
				// 	emitClose:()=> { //取消
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
			navBack() {
				uni.navigateBack();
			},
			rtBubble() {
				this.getTimeOrListItem1();
			}
		}
	};
</script>

<style lang="scss">
	@import '../../static/less/index.less';
	@import '../../static/css/index.css';
	@import "../msg/css/style.scss";

	page {
		background: #111224;
		color: #FFFFFF;
	}

	::v-deep .textMoney {
		text-align: center;
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
		background: #FFFFFF;
		margin: 5rpx 10rpx 0;
		line-height: 70rpx;
	}

	.text-search {
		margin-left: 20upx;
		font-size: 14px;
		height: 60upx;
		line-height: 60upx;
		width: 95%;
		color: #FFFFFF;
	}

	.item-text-title {
		color: #000000;
		font-size: 15px;
		font-weight: bold;
	}

	.item {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
	}

	.item-text {
		margin-left: 20upx;
		border-radius: 50upx;
		margin: 10upx;
		border: 1px solid #dee0de;
		padding: 5upx;
		padding-left: 20upx;
		padding-right: 20upx;
		font-size: 13px;
		color: #555555;
	}
</style>