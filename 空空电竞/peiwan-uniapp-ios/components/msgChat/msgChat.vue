<template>
	<scroll-view scroll-y="true" :scroll-top="scrollTop">
		<view style="width: 100%;padding-bottom: 220rpx;padding-top: 245rpx;" @touchstart="hideDrawer">
			<view style="display: flex;flex-direction: column;" v-for="(item,index) in ListItem">
				<view style="margin-top: 15rpx;width: 100%;text-align: center;font-size: 26rpx;color: #999999;"
					v-if="item.createTime">
					{{item.createTime}}
				</view>
				<view v-if="item.userId && userId != item.userId" style="width: 83%;margin-right: 15%;">
					<view class="chat-listitem" style="float: left;margin-left: 10rpx;">
						<view>
							<image @tap="goGuanZhu(item)" :src="item.avatar ? item.avatar : '/static/logo.png'"
								class="chat-listitem-image"></image>
						</view>
						<view v-if="item.content && item.messageType === 1" @longpress="copy(item.content)"
							class="chat-listitem-text" style="margin-left: 20rpx;">{{item.content}}</view>
						<image @tap="viewImg(item.content)" v-if="item.content && item.messageType === 2"
							:src="item.content" style="height: 200rpx;width: 200rpx;margin-left: 20rpx;"></image>
						<image class="chat-listitem-text" @tap="viewImg(item.content)"
							v-if="item.content && item.messageType === 4"
							:src="'https://pw.xianmxkj.com/emoji/' +item.content"
							style="height: 80rpx;width: 80rpx;margin-left: 20rpx;"></image>


						<view v-if="item.content && item.messageType === 3" class="chat-listitem-text bubble voice"
							:class="playMsgid == index?'play':''"
							style="margin-left: 20rpx;display: flex;align-items: center;"
							@tap="playVoice(item.content,index)">
							<image v-if="playMsgid != index" src="../../static/pw/laba.png"
								style="width: 35upx;height: 40upx;margin-right: 10rpx;"></image>
							<image v-if="playMsgid == index" src="../../static/pw/labagif.gif"
								style="width: 35upx;height: 40upx;margin-right: 10rpx;"></image>

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
						<image class="chat-listitem-text" @tap="viewImg(item.content)"
							v-if="item.content && item.messageType === 4"
							:src="'https://pw.xianmxkj.com/emoji/' +item.content"
							style="height: 80rpx;width: 80rpx;margin-right: 20rpx;">
						</image>

						<view v-if="item.content && item.messageType === 3" class="chat-listitem-text bubble voice"
							:class="playMsgid == index?'play':''"
							style="margin-right: 20rpx;display: flex;align-items: center;"
							@tap="playVoice(item.content,index)">
							<view class="length">{{item.voiceLength ? item.voiceLength : 0}}</view>
							<image v-if="playMsgid != index" src="../../static/pw/labaleft.png"
								style="width: 35upx;height: 40upx;margin-left: 10rpx;"></image>
							<image v-if="playMsgid == index" src="../../static/pw/labaleft.png"
								style="width: 35upx;height: 40upx;margin-left: 10rpx;"></image>

						</view>
						<view>
							<image :src="imageUrl" class="chat-listitem-image"></image>
						</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 抽屉栏 -->
		<view class="popup-layer" :class="popupLayerClass" @touchmove.stop.prevent="discard">
			<!-- 表情 -->
			<swiper class="emoji-swiper" indicator-active-color="#cccccc" :class="{hidden:hideEmoji}"
				indicator-dots="true" duration="150">
				<swiper-item v-for="(page1,pid) in emojiList" :key="pid">
					<view v-for="(em,eid) in page1" :key="eid" @tap="setChatSave(4,em.url)">
						<!-- <image src="../../static/emoji/100.gif" mode="widthFix"></image> -->
						<image mode="widthFix" :src="'https://pw.xianmxkj.com/emoji/'+em.url"></image>
					</view>
				</swiper-item>
			</swiper>
			<!-- 更多功能 相册-拍照-红包 -->
			<view class="more-layer" :class="{hidden:hideMore}">
				<view class="list">
					<!-- 上传图片 -->
					<view>
						<view class="box" @click="chooseImage(['album'])">
							<view class="icon tupian2" style="color: #000000;"></view>
						</view>
						<view style="width: 100%;text-align: center;">上传图片</view>
					</view>
					<!-- 常用语 -->
					<view>
						<view class="box" @tap="showChangYongSelect">
							<view class="icon jianpan" style="color: #000000;"></view>
						</view>
						<view style="width: 100%;text-align: center;">常用语句</view>
					</view>
					<!-- 添加常用语或自动回复内容 -->
					<view>
						<view class="box" @tap="goAdd">
							<view class="icon add" style="color: #000000;"></view>
						</view>
						<view style="width: 100%;text-align: center;">添加语句</view>
					</view>
					<view>
						<view class="box" @tap="goHistory">
							<view class="icon chehui" style="color: #000000;"></view>
						</view>
						<view style="width: 100%;text-align: center;">聊天记录</view>
					</view>
					<!-- <view>
						<view class="box" @tap="goHistory">
							<view class="icon chehui" style="color: #000000;"></view>
						</view>
						<view style="width: 100%;text-align: center;">聊天记录</view>
					</view> -->
					<!-- <view class="box" @tap="goHistory">
						<view class="icon other-voice" style="color: #000000;"></view>
					</view> -->
					<!-- <view class="box" @tap="goHistory">
						<view class="icon to" style="color: #000000;"></view>
					</view> -->
					<!-- <view class="box" @tap="handRedEnvelopes"><view class="icon hongbao"></view></view> -->
				</view>
			</view>
		</view>
		<!-- 底部输入栏 -->
		<view class="input-box" :class="popupLayerClass" @touchmove.stop.prevent="discard">
			<view class="flex">
				<view class="textbox" :style="isVoice ? 'width:100%' : ''">
					<view class="voice-mode" :class="[isVoice?'':'hidden',recording?'recording':'']"
						@touchstart="voiceBegin" @touchmove.stop.prevent="voiceIng" @touchend="voiceEnd"
						@touchcancel="voiceCancel">{{voiceTis}}</view>
					<view class="text-mode" :class="isVoice?'hidden':''">
						<view class="box">
							<input style="color: #FFFFFF;width: 100%;" auto-height="true" @confirm='setChatSave(1)'
								v-model="content" @focus="textareaFocus" />
						</view>

					</view>
				</view>
				<!-- #ifndef H5 -->
				<!-- 				<view class="more" @tap="showMore">
					<view class="icon add"></view>
				</view> -->
				<!-- #endif -->
				<view class="send" :class="isVoice?'hidden':''" @tap='setChatSave(1)'>
					<view class="btn">发送</view>
				</view>
			</view>
			<view class="action flex justify-between align-center">
				<!-- H5下不能录音，输入栏布局改动一下 -->
				<!-- #ifndef H5 -->
				<view class="voice">
					<view class="icon" :class="isVoice?'jianpan':'yuyin'" @tap="switchVoice"></view>
				</view>
				<!-- #endif -->
				<view class="em" @tap="chooseEmoji">
					<view class="icon biaoqing"></view>
				</view>
				<view class="lw" @tap="showGift">
					<image style="width: 60rpx;height: 60rpx;" src="../../static/images/index/dashang.png" mode="">
					</image>
				</view>
				<view class="more" @tap="showMore">
					<view class="icon add"></view>
				</view>

			</view>
		</view>
		<!-- 输入框下面的标签 -->

		<!-- 录音UI效果 -->
		<view class="record" :class="recording?'':'hidden'">
			<view class="ing" :class="willStop?'hidden':''">
				<view class="icon luyin2"></view>
			</view>
			<view class="cancel" :class="willStop?'':'hidden'">
				<view class="icon chehui"></view>
			</view>
			<view class="tis" :class="willStop?'change':''">{{recordTis}}</view>
		</view>
		<!-- 礼物栏 -->
		<u-popup v-model="show" mode="bottom" border-radius="14" :closeable="true">
			<view class="popupbox">
				<view class="padding-top text-bold text-lg">快来给Ta送个小礼物吧~</view>
				<view class="flex justify-between align-center" style="margin-top: 40rpx;">
					<view class="flex align-center">
						<image src="../../static/images/index/jinbi.png" style="width: 35upx;height: 36upx;">
						</image>
						<view class="margin-left-xs">{{money}}</view>
					</view>
					<view class="popupbtn" @click="goNav('/my/wallet/index?index=1')">充值</view>
				</view>
				<!-- 分类轮播 -->
				<view class="category" v-if="giftList.length>0">
					<view class="box">
						<swiper class="swiper" duration="300" :style="{ height: categoryHeight }"
							@change="categoryChange">
							<swiper-item v-for="(nav, index5) in giftList" :key="index5">
								<view class="category-list">
									<view class="icon popbox" v-for="(item,ind) in nav" :key="ind"
										@click="binddaS(item)" :class="TabCurr == item.giftId?'active':''">
										<image mode="widthFix" :src="item.giftImg" style="height: 90upx;width: 90upx">
										</image>
										<view class="text-lg" :class="TabCurr == item.giftId?'actives':''">
											{{ item.giftName }}
										</view>
										<view class="text-sm flex align-center justify-center margin-top-xs">
											<view>{{item.giftMoney}}</view>
											<image style="margin:0upx 6upx;width: 20rpx;height: 20rpx;"
												src="../../static/images/index/jinbi.png"></image>
										</view>
										<view v-if="TabCurr == item.giftId" class="zengsong"
											@click="giveGift(item.giftId)">赠送
										</view>
									</view>
								</view>
							</swiper-item>
						</swiper>
					</view>
				</view>
			</view>
		</u-popup>
		<u-select v-model="showChangYong" title="常用词语" mode="single-column" :list="changYongList" @confirm="selConfirm">
		</u-select>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content1">
		</popup>
	</scroll-view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import configdata from '../../common/config.js';
	export default {
		components: {
			popup
		},
		props: {
			//名称
			byNickName: {
				type: String,
				default: ''
			},
			//对方的userid
			byUserId: {
				type: String || Number,
				default: ''
			},
			//聊天室id
			chatConversationId: {
				type: Number,
				default: 0,
			}

		},
		data() {
			return {
				popupshow: false,
				content1: '',
				popupType: 0,
				copyContent: '',


				money: 0,
				currentPageindex: 0,
				categoryHeight: '300rpx',
				recordLength: 0,
				initPoint: {
					identifier: 0,
					Y: 0
				},
				showChangYong: false,
				changYongList: [],
				//播放语音相关参数
				AUDIO: uni.createInnerAudioContext(),
				playMsgid: null,
				VoiceTimer: null,


				//文字消息
				textMsg: '',
				//消息列表
				isHistoryLoading: false,
				scrollAnimation: false,
				scrollTop: 99999,
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


				//表情定义
				hideEmoji: true,
				emojiList: [
					[{
						"url": "100.gif",
						alt: "[微笑]"
					}, {
						"url": "101.gif",
						alt: "[伤心]"
					}, {
						"url": "102.gif",
						alt: "[美女]"
					}, {
						"url": "103.gif",
						alt: "[发呆]"
					}, {
						"url": "104.gif",
						alt: "[墨镜]"
					}, {
						"url": "105.gif",
						alt: "[哭]"
					}, {
						"url": "106.gif",
						alt: "[羞]"
					}, {
						"url": "107.gif",
						alt: "[哑]"
					}, {
						"url": "108.gif",
						alt: "[睡]"
					}, {
						"url": "109.gif",
						alt: "[哭]"
					}, {
						"url": "110.gif",
						alt: "[囧]"
					}, {
						"url": "111.gif",
						alt: "[怒]"
					}, {
						"url": "112.gif",
						alt: "[调皮]"
					}, {
						"url": "113.gif",
						alt: "[笑]"
					}, {
						"url": "114.gif",
						alt: "[惊讶]"
					}, {
						"url": "115.gif",
						alt: "[难过]"
					}, {
						"url": "116.gif",
						alt: "[酷]"
					}, {
						"url": "117.gif",
						alt: "[汗]"
					}, {
						"url": "118.gif",
						alt: "[抓狂]"
					}, {
						"url": "119.gif",
						alt: "[吐]"
					}, {
						"url": "120.gif",
						alt: "[笑]"
					}, {
						"url": "121.gif",
						alt: "[快乐]"
					}, {
						"url": "122.gif",
						alt: "[奇]"
					}, {
						"url": "123.gif",
						alt: "[傲]"
					}],
					[{
						"url": "124.gif",
						alt: "[饿]"
					}, {
						"url": "125.gif",
						alt: "[累]"
					}, {
						"url": "126.gif",
						alt: "[吓]"
					}, {
						"url": "127.gif",
						alt: "[汗]"
					}, {
						"url": "128.gif",
						alt: "[高兴]"
					}, {
						"url": "129.gif",
						alt: "[闲]"
					}, {
						"url": "130.gif",
						alt: "[努力]"
					}, {
						"url": "131.gif",
						alt: "[骂]"
					}, {
						"url": "132.gif",
						alt: "[疑问]"
					}, {
						"url": "133.gif",
						alt: "[秘密]"
					}, {
						"url": "134.gif",
						alt: "[乱]"
					}, {
						"url": "135.gif",
						alt: "[疯]"
					}, {
						"url": "136.gif",
						alt: "[哀]"
					}, {
						"url": "137.gif",
						alt: "[鬼]"
					}, {
						"url": "138.gif",
						alt: "[打击]"
					}, {
						"url": "139.gif",
						alt: "[bye]"
					}, {
						"url": "140.gif",
						alt: "[汗]"
					}, {
						"url": "141.gif",
						alt: "[抠]"
					}, {
						"url": "142.gif",
						alt: "[鼓掌]"
					}, {
						"url": "143.gif",
						alt: "[糟糕]"
					}, {
						"url": "144.gif",
						alt: "[恶搞]"
					}, {
						"url": "145.gif",
						alt: "[什么]"
					}, {
						"url": "146.gif",
						alt: "[什么]"
					}, {
						"url": "147.gif",
						alt: "[累]"
					}],
					[{
						"url": "148.gif",
						alt: "[看]"
					}, {
						"url": "149.gif",
						alt: "[难过]"
					}, {
						"url": "150.gif",
						alt: "[难过]"
					}, {
						"url": "151.gif",
						alt: "[坏]"
					}, {
						"url": "152.gif",
						alt: "[亲]"
					}, {
						"url": "153.gif",
						alt: "[吓]"
					}, {
						"url": "154.gif",
						alt: "[可怜]"
					}, {
						"url": "155.gif",
						alt: "[刀]"
					}, {
						"url": "156.gif",
						alt: "[水果]"
					}, {
						"url": "157.gif",
						alt: "[酒]"
					}, {
						"url": "158.gif",
						alt: "[篮球]"
					}, {
						"url": "159.gif",
						alt: "[乒乓]"
					}, {
						"url": "160.gif",
						alt: "[咖啡]"
					}, {
						"url": "161.gif",
						alt: "[美食]"
					}, {
						"url": "162.gif",
						alt: "[动物]"
					}, {
						"url": "163.gif",
						alt: "[鲜花]"
					}, {
						"url": "164.gif",
						alt: "[枯]"
					}, {
						"url": "165.gif",
						alt: "[唇]"
					}, {
						"url": "166.gif",
						alt: "[爱]"
					}, {
						"url": "167.gif",
						alt: "[分手]"
					}, {
						"url": "168.gif",
						alt: "[生日]"
					}, {
						"url": "169.gif",
						alt: "[电]"
					}, {
						"url": "170.gif",
						alt: "[炸弹]"
					}, {
						"url": "171.gif",
						alt: "[刀子]"
					}],
					[{
						"url": "172.gif",
						alt: "[足球]"
					}, {
						"url": "173.gif",
						alt: "[瓢虫]"
					}, {
						"url": "174.gif",
						alt: "[翔]"
					}, {
						"url": "175.gif",
						alt: "[月亮]"
					}, {
						"url": "176.gif",
						alt: "[太阳]"
					}, {
						"url": "177.gif",
						alt: "[礼物]"
					}, {
						"url": "178.gif",
						alt: "[抱抱]"
					}, {
						"url": "179.gif",
						alt: "[拇指]"
					}, {
						"url": "180.gif",
						alt: "[贬低]"
					}, {
						"url": "181.gif",
						alt: "[握手]"
					}, {
						"url": "182.gif",
						alt: "[剪刀手]"
					}, {
						"url": "183.gif",
						alt: "[抱拳]"
					}, {
						"url": "184.gif",
						alt: "[勾引]"
					}, {
						"url": "185.gif",
						alt: "[拳头]"
					}, {
						"url": "186.gif",
						alt: "[小拇指]"
					}, {
						"url": "187.gif",
						alt: "[拇指八]"
					}, {
						"url": "188.gif",
						alt: "[食指]"
					}, {
						"url": "189.gif",
						alt: "[ok]"
					}, {
						"url": "190.gif",
						alt: "[情侣]"
					}, {
						"url": "191.gif",
						alt: "[爱心]"
					}, {
						"url": "192.gif",
						alt: "[蹦哒]"
					}, {
						"url": "193.gif",
						alt: "[颤抖]"
					}, {
						"url": "194.gif",
						alt: "[怄气]"
					}, {
						"url": "195.gif",
						alt: "[跳舞]"
					}],
					[{
						"url": "196.gif",
						alt: "[发呆]"
					}, {
						"url": "197.gif",
						alt: "[背着]"
					}, {
						"url": "198.gif",
						alt: "[伸手]"
					}, {
						"url": "199.gif",
						alt: "[耍帅]"
					}, {
						"url": "200.png",
						alt: "[微笑]"
					}, {
						"url": "201.png",
						alt: "[生病]"
					}, {
						"url": "202.png",
						alt: "[哭泣]"
					}, {
						"url": "203.png",
						alt: "[吐舌]"
					}, {
						"url": "204.png",
						alt: "[迷糊]"
					}, {
						"url": "205.png",
						alt: "[瞪眼]"
					}, {
						"url": "206.png",
						alt: "[恐怖]"
					}, {
						"url": "207.png",
						alt: "[忧愁]"
					}, {
						"url": "208.png",
						alt: "[眨眉]"
					}, {
						"url": "209.png",
						alt: "[闭眼]"
					}, {
						"url": "210.png",
						alt: "[鄙视]"
					}, {
						"url": "211.png",
						alt: "[阴暗]"
					}, {
						"url": "212.png",
						alt: "[小鬼]"
					}, {
						"url": "213.png",
						alt: "[礼物]"
					}, {
						"url": "214.png",
						alt: "[拜佛]"
					}, {
						"url": "215.png",
						alt: "[力量]"
					}, {
						"url": "216.png",
						alt: "[金钱]"
					}, {
						"url": "217.png",
						alt: "[蛋糕]"
					}, {
						"url": "218.png",
						alt: "[彩带]"
					}, {
						"url": "219.png",
						alt: "[礼物]"
					}, ]
				],
				connected: false,
				connecting: false,
				msg: false,
				type4: [],
				listRight: {
					chat: {
						userHead: ""
					},
					content: "",
					messageType: 1,
					type: 1
				},
				content: '',
				chatId: '',
				type: 1,
				ListItem: [],
				index: 0,
				page: 1,
				size: 100,
				imageUrl: '/static/logo.png',
				userId: 0,
				countDown: '',
				giftList: [],
				TabCurr: 0,
				show: false,
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
		//聊天组件销毁后关闭scoket
		destoryed() {
			uni.closeSocket()
			this.AUDIO.stop();
			this.playMsgid = null;
			uni.hideLoading()
		},
		//组件创建后
		created(d) {
			this.getChangYong();
			this.getMoney()
			this.userId = this.$queue.getData('userId');
			this.connect();
			this.getgift()
			this.getUserInfo();
			if (this.chatConversationId) {
				this.getTimeOrListItem1();
			} else {
				this.insertChat();
			}
			if (this.byNickName) {
				uni.setNavigationBarTitle({
					title: this.byNickName
				})
			}
			let image_url = this.$queue.getData('avatar');
			if (image_url && image_url !== 'undefined') {
				this.imageUrl = image_url;
			} else {
				this.imageUrl = '/static/logo.png';
			}
			// this.emojiList.forEach(d =>{
			// 	d.forEach(e =>{
			// 		console.log('/static/emoji/' + e.url)
			// 	});
			// });
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
		methods: {
			sureT() {
				this.popupshow = false
				if (this.popupType == 0) {
					uni.navigateBack();
				} else if (this.popupType == 1) {
					let that = this
					uni.setClipboardData({
						data: that.copyContent,
						success: r => {
							that.$queue.showToast('复制成功');
						}
					});
				} else if (this.popupType == 3) {

				}


			},
			getUserInfo() {
				this.$Request.getT("/app/user/selectUserByUserId?userId=" + this.byUserId).then(res => {
					if (res.code == 0) {
						if (res.data.status && res.data.status == 2) {
							this.content1 = '该用户违反平台规则已被封号，请勿转账，谨防被骗！'
							this.popupshow = true


						} else {
							if (res.data.risk && res.data.risk == 1) {
								this.content1 = '该账号是风险账号，请勿转账，谨防被骗！'
								this.popupshow = true


							}
						}
					}
				});
			},
			// 我的余额
			getMoney() {
				this.$Request.get("/app/userMoney/selectMyMoney").then(res => {
					if (res.code == 0 && res.data) {
						console.log(res.data.money)
						this.money = res.data.money
					}
				});
			},
			// 传进数组和指定个数，进行拆分
			chunk: function(array, size) {
				//获取数组的长度，如果你传入的不是数组，那么获取到的就是undefined
				const length = array.length
				//判断不是数组，或者size没有设置，size小于1，就返回空数组
				if (!length || !size || size < 1) {
					return []
				}
				//核心部分
				let index = 0 //用来表示切割元素的范围start
				let resIndex = 0 //用来递增表示输出数组的下标

				//根据length和size算出输出数组的长度，并且创建它。
				let result = new Array(Math.ceil(length / size))
				//进行循环
				while (index < length) {
					//循环过程中设置result[0]和result[1]的值。该值根据array.slice切割得到。
					result[resIndex++] = array.slice(index, (index += size))
				}
				//输出新数组
				return result
			},
			// 获取礼物列表
			getgift() {
				this.$Request.getT("/app/gift/selectGiftList", {
					status: 1
				}).then(res => {
					if (res.code == 0) {
						if (res.data.length > 0) {
							var datanew = this.chunk(res.data, 8)
							this.giftList = datanew;
							console.log('giftList的数据', datanew)
							if (res.data.length > 4) {
								this.categoryHeight = "480rpx"
							} else {
								this.categoryHeight = "240rpx"
							}
						} else {
							var datanew = this.chunk(this.giftLists, 8)
							this.navlist = datanew;
						}

						// this.giftList = res.data
					}
				});
			},
			// 打赏礼物
			binddaS(item) {
				this.TabCurr = item.giftId
			},
			giveGift(giftId) {
				console.log(this.byUserId)
				let data = {
					giftId: giftId,
					userId: this.byUserId
				}
				this.$Request.postJson('/app/gift/sendGift', data).then(res => {
					if (res.code == 0) {
						this.getMoney()
						this.$queue.showToast('打赏成功！');
						setTimeout(d => {
							uni.hideLoading();
							// this.show = false
						}, 1000);
					} else {
						uni.hideLoading();
						this.$queue.showToast(res.msg)
					}
				});
			},
			goNav(e) {
				uni.navigateTo({
					url: e
				})
			},
			//送礼物
			showGift() {
				this.show = !this.show
			},
			showChangYongSelect() {
				this.showMore();
				this.showChangYong = !this.showChangYong;
			},
			selConfirm(e) {
				// this.form.game = e[0].label
				// this.form.gameId = e[0].value
				this.content = e[0].label;
				this.setChatSave(1);
			},
			getChangYong() {
				this.$Request.getT('/app/message/selectMessageByUserId?page=1&limit=100&state=20')
					.then(res => {
						if (res.code == 0) {
							this.changYongList = [];
							res.data.list.forEach(d => {
								let data = {
									label: d.content,
									value: d.id
								}
								this.changYongList.push(data);
							});
						}
					});
			},
			goGuanZhu(item) {
				this.hideDrawer();
				//去关注页面
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + item.userId
				});
				// uni.navigateTo({
				// 	url: '/package/pages/task/followUser?userId=' + item.userId
				// });
			},
			//添加常用语/自动回复
			goAdd() {
				this.showMore();
				uni.navigateTo({
					url: '/pages/msg/addmsg'
				});
			},
			goHistory() {
				this.showMore();
				uni.navigateTo({
					url: '/pages/msg/chathistory?chatConversationId=' + this.chatConversationId
				});
			},
			insertChat() {
				let userId = this.$queue.getData('userId');
				let data = {
					focusedUserId: this.byUserId,
					userId: userId
				}
				this.$Request.postJson('/appChat/insertChatConversation', data).then(res => {
					if (res.code == 0 && res.data) {
						this.chatConversationId = res.data.chatConversationId;
						this.getTimeOrListItem1();
					}
				});
			},
			//更多功能(点击+弹出)
			showMore() {
				this.isVoice = false;
				this.hideEmoji = true;
				if (this.hideMore) {
					this.hideMore = false;
					this.openDrawer();
				} else {
					this.hideDrawer();
				}
			},
			// 打开抽屉
			openDrawer() {
				this.popupLayerClass = 'showLayer';
			},
			// 隐藏抽屉
			hideDrawer() {
				this.popupLayerClass = '';
				setTimeout(() => {
					this.hideMore = true;
					this.hideEmoji = true;
				}, 150);
			},
			// 选择表情
			chooseEmoji() {
				this.hideMore = true;
				if (this.hideEmoji) {
					this.hideEmoji = false;
					this.openDrawer();
				} else {
					this.hideDrawer();
				}
			},
			//添加表情
			addEmoji(em) {
				this.textMsg += em.alt;
			},
			//获取焦点，如果不是选表情ing,则关闭抽屉
			textareaFocus() {
				if (this.popupLayerClass == 'showLayer' && this.hideMore == false) {
					this.hideDrawer();
				}
			},
			//替换表情符号为图片
			replaceEmoji(str) {
				let replacedStr = str.replace(/\[([^(\]|\[)]*)\]/g, (item, index) => {
					console.log("item: " + item);
					for (let i = 0; i < this.emojiList.length; i++) {
						let row = this.emojiList[i];
						for (let j = 0; j < row.length; j++) {
							let EM = row[j];
							if (EM.alt == item) {
								//在线表情路径，图文混排必须使用网络路径，请上传一份表情到你的服务器后再替换此路径 
								//比如你上传服务器后，你的100.gif路径为https://www.xxx.com/emoji/100.gif 则替换onlinePath填写为https://www.xxx.com/emoji/
								let onlinePath = 'https://pw.xianmxkj.com/emoji/'
								let imgstr = '<img src="' + onlinePath + this.onlineEmoji[EM.url] + '">';
								console.log("imgstr: " + imgstr);
								return imgstr;
							}
						}
					}
				});
				return '<div style="display: flex;align-items: center;word-wrap:break-word;">' + replacedStr + '</div>';
			},
			// 播放语音
			playVoice(msg, index) {
				this.playMsgid = index;
				this.AUDIO.src = msg;
				this.$nextTick(function() {
					this.AUDIO.play();
				});
			},
			// 录音开始
			voiceBegin(e) {
				if (e.touches.length > 1) {
					return;
				}
				this.initPoint.Y = e.touches[0].clientY;
				this.initPoint.identifier = e.touches[0].identifier;
				this.RECORDER.start({
					format: "mp3"
				}); //录音开始,
			},
			//录音开始UI效果
			recordBegin(e) {
				this.recording = true;
				this.voiceTis = '松开 结束';
				this.recordLength = 0;
				this.recordTimer = setInterval(() => {
					this.recordLength++;
				}, 1000)
			},
			// 录音被打断
			voiceCancel() {
				this.recording = false;
				this.voiceTis = '按住 说话';
				this.recordTis = '手指上滑 取消发送'
				this.willStop = true; //不发送录音
				this.RECORDER.stop(); //录音结束
			},
			// 录音中(判断是否触发上滑取消发送)
			voiceIng(e) {
				if (!this.recording) {
					return;
				}
				let touche = e.touches[0];
				//上滑一个导航栏的高度触发上滑取消发送
				if (this.initPoint.Y - touche.clientY >= uni.upx2px(100)) {
					this.willStop = true;
					this.recordTis = '松开手指 取消发送'
				} else {
					this.willStop = false;
					this.recordTis = '手指上滑 取消发送'
				}
			},
			// 结束录音
			voiceEnd(e) {
				if (!this.recording) {
					return;
				}
				this.recording = false;
				this.voiceTis = '按住 说话';
				this.recordTis = '手指上滑 取消发送'
				this.RECORDER.stop(); //录音结束
			},
			//录音结束(回调文件)
			recordEnd(e) {
				clearInterval(this.recordTimer);
				if (!this.willStop) {
					this.$queue.showLoading('发送中...')
					console.log("e: " + JSON.stringify(e));
					let msg = {
						length: 0,
						url: e.tempFilePath
					}
					let min = parseInt(this.recordLength / 60);
					let sec = this.recordLength % 60;
					min = min < 10 ? '0' + min : min;
					sec = sec < 10 ? '0' + sec : sec;
					msg.length = min + ':' + sec;
					console.log('msg.length___:' + msg.length)
					uni.uploadFile({ // 上传接口
						url: this.config("APIHOST1") + '/alioss/upload', //真实的接口地址
						filePath: e.tempFilePath,
						name: 'file',
						success: (uploadFileRes) => {
							uni.hideLoading();
							this.content = JSON.parse(uploadFileRes.data).data;
							console.log("语音：" + this.content)
							this.setChatSave(3, msg.length);
							uni.hideLoading();
						}
					});
				} else {
					console.log('取消发送录音');
				}
				this.willStop = false;
			},
			// 切换语音/文字输入
			switchVoice() {
				this.hideDrawer();
				this.isVoice = this.isVoice ? false : true;
			},
			copy(content) {
				let that = this
				that.copyContent = content
				that.popupType = 1
				that.content1 = '确认要复制此文字吗？'
				that.popupshow = true
				// that.$Pmessage({
				// 	showModel: true,
				// 	title: '温馨提示',
				// 	content: '确认要复制此文字吗？',
				// 	maskcloseAble: false,
				// 	cancelText: '我知道了',
				// 	confirmText: '我知道了',
				// }, {
				// 	emitBtn:()=> { //确认
				// 		console.log('用户点击确认');
				// 	uni.setClipboardData({
				// 		data: content,
				// 		success: r => {
				// 			that.$queue.showToast('复制成功');
				// 		}
				// 	});
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
			connect() {
				let userId = this.$queue.getData('userId');
				console.log(userId)
				if (this.connected || this.connecting) {
					that.popupType = 3
					that.content1 = '正在连接或者已经连接，请勿重复连接'
					that.popupshow = true

					// this.$Pmessage({
					// 	showModel: true,
					// 	title: '提示',
					// 	content: '正在连接或者已经连接，请勿重复连接',
					// 	maskcloseAble: false
					// }, {
					// 	emitBtn: () => { //确认
					// 		console.log('用户点击确认');

					// 	},
					// 	emitClose: () => { //取消
					// 		console.log('用户点击取消');

					// 	},
					// });

					return false
				}
				this.connecting = true
				uni.showLoading({
					title: '连接中...'
				})
				uni.connectSocket({
					// url: 'wss://h5.youxb.icu/wss/pwyl-api/chatSocket/' + userId,
					url: this.config("WSHOST1") + userId,
					// url: 'ws://192.168.1.17:8893/pwyl-api/chatSocket/' + userId,
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
				uni.onSocketOpen((res) => {
					this.connecting = false
					this.connected = true
					uni.hideLoading()
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
					// 		console.log('用户点击确认');

					// 	},
					// 	emitClose: () => { //取消
					// 		console.log('用户点击取消');
					// 	},
					// });
					console.log('onError', err);
				})
				uni.onSocketMessage((res) => {
					let resData = JSON.parse(res.data);
					console.log('onMessage', resData.messageType)
					if (resData && resData.messageType == 10) {
						this.popupType = 1
						this.content1 = '' + resData.content
						this.popupshow = true
						// this.$Pmessage({
						// 	showModel: true,
						// 	title: '提示',
						// 	content: '' + resData.content,
						// 	maskcloseAble: false,
						// 	cancelText: '我知道了',
						// 	confirmText: '我知道了',
						// }, {
						// 	emitBtn: () => { //确认
						// 		console.log('用户点击确认');
						// 		uni.navigateBack();
						// 	},
						// 	emitClose: () => { //取消
						// 		console.log('用户点击取消');

						// 	},
						// });

					} else {
						this.getTimeOrListItem1();
						console.log('onMessage', resData)
					}
					// this.getTimeOrListItem1();
					// console.log('onMessage', res)
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
				let userId = this.$queue.getData('userId')
				this.$Request.getT('/app/chat/selectChatContent?page=' + this.page + '&limit=' + this.size +
					'&chatConversationId=' + this.chatConversationId + '&userId=' + userId).then(
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
								// if (!d.chat.userHead) {
								// 	d.chat.userHead = '/static/logo.png';
								// }
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
							// setTimeout(() => {
							// 	uni.pageScrollTo({
							// 		scrollTop: 99999,
							// 		duration: 0
							// 	});
							// }, 50);
						}
						uni.hideLoading();
					});
			},
			setChatSave(type, url) {
				//type:1文字 2图片
				if (type === 1 && this.content == '') {
					this.$queue.showToast('请输入聊天内容');
					return;
				}
				if (this.$queue.getChatSearchKeys(this.content)) {
					uni.showToast({
						title: "输入内容带有非法关键字请重新输入",
						mask: false,
						duration: 1500,
						icon: "none"
					});
					this.$Request.postT('/app/risk/insertRisk?riskType=5&content=' + this.content).then(res => {

					})
					return;
				}
				// if (type === 1 && this.$queue.getChatSearchKeys(this.content) != -1) {
				// 	uni.showToast({
				// 		title: "输入内容带有非法关键字请重新输入",
				// 		mask: false,
				// 		duration: 1500,
				// 		icon: "none"
				// 	});
				// 	return;
				// }

				let userId = this.$queue.getData('userId');
				if (type === 4) {
					this.content = url;
				}
				let data = {};
				if (type == 3) {
					data = {
						userId: this.byUserId,
						content: this.content,
						voiceLength: url,
						messageType: type,
						chatConversationId: this.chatConversationId
					}
				} else {
					data = {
						userId: this.byUserId,
						content: this.content,
						messageType: type,
						chatConversationId: this.chatConversationId
					}
				}

				data = JSON.stringify(data);
				let that = this;
				uni.sendSocketMessage({
					data: data,
					success(res) {
						let image_url = that.$queue.getData('image_url');
						if (!image_url) {
							image_url = '/static/logo.png';
						}
						let data = {
							chat: {
								userHead: image_url
							},
							content: that.content,
							type: type,
							sendType: 1
						}
						that.ListItem.push(data);
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
					sourceType: ['album'],
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
	@import "./css/style.scss";


	page {
		background: #111224;
		color: #FFFFFF;
	}

	.chat-listitem {
		display: flex;
		margin-top: 20rpx;
		padding: 10rpx;
	}

	.chat-listitem-text {
		color: #000000;
		background: #FFFFFF;
		margin-top: 6rpx;
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

	.popupbox {
		background: #291B3D;
		padding: 0upx 20upx;
	}

	.popupbtn {
		background: #ac75fe;
		color: #FFFFFF;
		border-radius: 8upx;
		padding: 15upx 45upx;
		/* margin-top: 40upx; */
	}

	.popbox {
		/* width: 25%; */
		width: 170rpx;
		height: 200rpx;
		text-align: center;
		margin-bottom: 30rpx;
	}

	.active {
		background: #32224a;
		border-radius: 15upx;
	}

	.zengsong {
		width: 100%;
		/* height: 56px; */
		background: #523E80;
		border-radius: 0px 0px 15upx 15upx;
		padding: 10upx 0;
		margin: 24rpx 0px;
		font-size: 30rpx;
	}

	.actives {
		display: none;
	}

	.scroll-view_H {
		width: 100%;
		white-space: nowrap;
	}

	.scroll-view-item_H {
		display: inline-flex;
		width: 100%;
		height: 300upx;
	}

	.category {
		width: 100%;

		.box {
			width: 100%;
			border-radius: 20upx;
			// background-color: #ffffff;

			.dots {
				display: flex;
				justify-content: center;
				height: 15upx;
				width: 100%;

				view {
					width: 30upx;
					height: 5upx;
					background-color: rgba(0, 0, 0, 0.2);

					&.active {
						background-color: #ff570a;
					}
				}
			}

			.swiper {
				width: 100%;
				padding: 10upx 0;

				.uni-swiper-dot {
					width: 20upx;
				}

				.category-list {
					width: 100%;
					height: auto;
					display: flex;
					justify-content: flex-start;
					padding: 10upx 0;
					flex-flow: wrap;

					.icon {

						image {
							width: 70%;
							height: 13.3vw;
						}

						view {
							// width: 100%;
							// display: flex;
							// justify-content: center;
						}

						.remen,
						.lijian {
							width: 60upx;
							height: 30upx;
							position: absolute;
							top: 0;
							right: 0;
						}
					}
				}
			}
		}
	}
</style>