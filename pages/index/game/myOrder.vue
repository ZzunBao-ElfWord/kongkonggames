<template>
	<view>
		<view class="u-skeleton">
			<!-- <image class="u-skeleton-fillet"
				style="display: inline-block;margin-right: 6upx;width: 710rpx;height: 320rpx;border-radius: 10rpx;"
				:src="order.homepageImg" @tap="previewPhoto(order.homepageImg)" mode="aspectFill"></image>-->
			<view class="u-skeleton-fillet padding-lr bg">
				<swiper class="swiper" :autoplay="true" interval="5000" duration="500" :circular="true"
					style="width: 100%;height: 340rpx;border-radius: 24rpx;">
					<swiper-item v-for="(item,index) in order.homepageImg" :key='index'
						@click="saveImg(order.homepageImg,index)">
						<image :src="item" mode="aspectFill" style="width: 710rpx;height: 340rpx;border-radius: 24rpx;">
						</image>
					</swiper-item>
				</swiper>
			</view>
			<view class="bg u-skeleton-fillet padding">
				<view class="jinbix padding-lr">
					<view style="margin-top: -30rpx;position: relative;bottom: -6rpx;"
						@click="goGuanZhuDetail(order.userId)">
						<image :src="order.avatar?order.avatar: '../../../static/logo.png'" mode="aspectFill"
							style="width: 230rpx;height: 260rpx;border-radius: 24rpx"></image>
						<view class="yuyinImg" @click.stop="playVoice()" v-if="order.voiceIntroduce">
							<view class="flex align-center box">
								<!-- 暂停 -->
								<image v-if="!isPlay" src="../../../static/jc-record/index.png" mode=""
									style="width: 40upx;height: 40upx;"></image>
								<!-- 播放 -->
								<image v-else src="../../../static/jc-record/bof.png" mode=""
									style="width: 40upx;height: 40upx;"></image>

								<image src="../../../static/jc-record/yuyin.png" mode=""
									style="width: 84upx;height: 32upx;margin: 0upx 10upx;"></image>
								<view>{{order.sec?order.sec:'0'}}<text>"</text></view>
							</view>
						</view>
					</view>
					<view class="flex-sub margin-left padding-top-sm flex flex-direction justify-between">
						<view class="flex align-center justify-between">
							<view class="flex align-center ">
								<view class="margin-right-xs text-lg text-bold text-cut" style="display: inline-block;">
									{{order.userName}}
								</view>

							</view>
							<view class="">
								<view class="text-sm" v-if="order.city" style="color: #FFFFFF;display: inline-block;">
									<!-- <view style="width: 10upx;height: 10upx;background: #1AD566;border-radius: 10upx;display: inline-block;margin: 0 8upx 4upx 0;"></view> -->
									{{order.city}} {{juliSelect === '是' ? order.distance : ''}}
								</view>
							</view>

						</view>
						<view class="flex">
							<view v-if="false" class="flex align-center  margin-right-xs padding-lr-xs"
								style="background:linear-gradient(-90deg, #174C6D, #33355F);color: #6EE3FB;border-radius: 50rpx;height: 40rpx;">
								<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
								{{order.age?order.age:0}}
							</view>
							<view v-if="false" class="flex align-center  margin-right-xs padding-lr-xs"
								style="background: linear-gradient(-90deg, #4F2B4B, #6735b1);color: #FB85FC;border-radius: 50rpx;height: 40rpx;">
								<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
								{{order.age?order.age:0}}
							</view>
							<view v-for="(ite,ind) in order.label" :key="ind">
								<image :src="ite" style="width: 98rpx;height: auto;margin-right: 5rpx;" mode="widthFix">
								</image>
							</view>
						</view>
						<view class="flex text-sm" style="color: #FFFFFF;">
							<view class="margin-right" v-if="order.orderScore">评分{{order.orderScore?(order.orderScore).toFixed(2):''}}</view>
							<view class="margin-right" v-else>暂无评分</view>
							<view v-if="order.count">{{order.count}}人已下单</view>
						</view>
						<view class="flex align-center justify-between">
							<view class="gambox">
								<view class="gamebox">
									<image :src="order.gameImg" mode="aspectFill" style="">
									</image>
									<view class="level">{{order.gameName}}</view>
								</view>
								<view class="mytit">{{order.myLevel}}</view>
							</view>
							<view style="color: #FBCA0C;">
								<text
									class="text-lg text-bold">{{isVip? order.memberMoney :order.money }}</text>币/{{order.unit}}
							</view>
						</view>
					</view>
				</view>
				<view>
					<view class="flex margin-top-sm">
						<text style="color: #FFFFFF;">接单游戏：</text>
						<view class="text-white">{{order.gameName}}</view>
					</view>
					<view class="flex margin-top-sm" v-if="order.myLevel">
						<view style="color: #FFFFFF;">我的段位：</view>
						<view class="text-white">{{order.myLevel}}</view>
					</view>
					<view class="flex margin-top-sm" v-if="order.orderTakingArea">
						<view style="color: #FFFFFF;">可接大区：</view>
						<view class="text-white">{{order.orderTakingArea}}</view>
					</view>
					<view class="flex margin-top-sm" v-if="order.orderTakingTime">
						<view style="color: #FFFFFF;">接单时间：</view>
						<view class="text-white">{{order.orderTakingTime}}</view>
					</view>
					<view class="flex margin-top-sm" v-if="order.orderLevel">
						<view style="color: #FFFFFF;">可接段位：</view>
						<view class="text-white">{{order.orderLevel}}</view>
					</view>

					<view class="flex margin-top">
						<!-- #ifdef APP -->
						<view style="color: #FFFFFF;width: 170rpx;">自我介绍：</view>
						<!-- #endif -->
						<!-- #ifndef APP -->
						<view style="color: #FFFFFF;width: 180rpx;">自我介绍：</view>
						<!-- #endif -->
						<view class="text-white" style="width: 90%;">{{order.details?order.details:'暂无其他要求'}}</view>
					</view>
				
				</view>
			</view>



			<view class="margin-top-sm padding bg text-white  u-skeleton-fillet" style="border-radius: 24upx;">
				<view class="pinglun">
					<view style="flex:3">
						<view class="flex">
							<view class="text-lg text-bold text-white">
								<image
									src="https://pw.xianmxkj.com/file/uploadPath/2023/03/21/2c5a169516106ba081c7926b19c66c02.png"
									style="width: 186rpx;height: 59rpx;"></image>
							</view>
							<view class="flex align-center margin-left-sm">
								<u-rate :count="count" v-model="order.commentScore" active-color="#FBCA0C"
									inactive-icon="star-fill" inactive-color="#9e9ca8" :disabled="true"></u-rate>
								<view class="margin-left-xs" style="font-size: 38upx;color: #FBCA0C;"
									v-if="order.commentScore">{{order.commentScore}}.0</view>
								<!-- <u-icon v-for="ite in item.score" :key='ite' color="#D0B391" name="star-fill"></u-icon> -->
							</view>
						</view>
						<view class="flex justify-between margin-top-sm" style="padding-right: 60upx;">
							<view>技术水平</view>
							<view>服务态度</view>
							<view>响应速度</view>
						</view>
					</view>
					<view style="width: 1rpx;height: auto;background:#2A2A32;"></view>
					<view class="flex-sub text-center">
						<view style="font-size: 52upx;color: #FBCA0C;">{{order.ordersCount}}</view>
						<view>累计接单</view>
					</view>
				</view>
				<!-- <view style="width: 100%;height: 1rpx;background: #2D2F52;margin: 30upx 0upx;"></view> -->
				<view class="text-df text-bold text-white margin-top">用户评价</view>
				<view>
					<view class="margin-tb-sm padding-bottom" v-for="(item, index) in commentList" :key='index'>
						<view class="flex justify-between">
							<!-- <u-avatar :src="item.avatar" size="48"></u-avatar> -->
							<image :src="item.avatar" mode="" style="width: 55rpx;height: 55rpx;border-radius: 50rpx;">
							</image>
							<view class="flex-sub margin-left-xs" style="line-height: 46upx;color: #8C8DA6;">
								{{item.userName}}
							</view>
							<view class="flex">
								<u-icon v-for="ite in item.score" :key='ite' color="#FBCA0C" name="star-fill"></u-icon>
							</view>
						</view>
						<view class="margin-top-sm text-white">{{item.content}}</view>
					</view>
				</view>
				<view v-if="commentList.length==0" class="margin-top-sm">暂无评价</view>
			</view>
			<view style="height: 100rpx;"></view>
		</view>
		<!-- <tui-modal :show="modal" @cancel="goDaShang" title="打赏用户" :moneyMessage="moneyMessage" place="请输入打赏柚币数"
			:button="button" @click="handleClick" :moneyType="moneyType" :content="moneycontent">
		</tui-modal> -->
		<view class="text-white flex justify-between cu-bar foot bg padding-tb-xs" v-if="myId != order.userId">
			<!-- <view @click="follow" class="padding-lr" style="width: 120rpx;">
				<image src="../../../static/images/index/guanzhu.png" v-if="!isFollow"
					style="width: 49rpx;height: 43rpx;"></image>
				<image src="../../../static/images/index/guanzhu1.png" v-else style="width: 49rpx;height: 43rpx;">
				</image>
				<view style="font-size: 22upx;margin-top: 8upx;">关注</view>
			</view> -->
			<view @click="goMsg" v-if="shangxianSelect != '否'" class="padding-lr"
				style="width: 140rpx;text-align: center;">
				<image src="../../../static/images/index/im.png" style="width: 55rpx;height: 55rpx;"></image>
				<view style="font-size: 22upx;margin-top: 8upx;">聊天</view>
			</view>
			<view @click="goTousu(order.userId)" class="padding-lr" style="width: 120rpx;">
				<image src="../../../static/images/index/tousu.png" style="width: 55rpx;height: 55rpx;"></image>
				<view style="font-size: 22upx;margin-top: 8upx;">投诉</view>
			</view>
			<view @click="goDaShang" class="padding-lr" style="width: 120rpx;" v-if="false">
				<image src="../../../static/images/index/dashang.png" style="width: 55rpx;height: 55rpx;"></image>
				<view style="font-size: 22upx;margin-top: 8upx;">礼物</view>
			</view>
			<view class="flex-sub margin-right">
				<u-button :custom-style="customStyle" @click="goNav()" shape="circle" :hair-line="false"
					hover-class="none">立即下单</u-button>
			</view>
		</view>
		<u-skeleton :loading="loading" :animation="true" elColor='#101014' bgColor='#050506'></u-skeleton>
		<!-- 礼物弹框 -->
		<u-popup v-model="show" mode="bottom" border-radius="14" :closeable="true">
			<view class="popupbox">
				<view class="padding-top text-bold text-lg">快来给Ta送个小礼物吧~</view>
				<view class="flex justify-between align-center" style="margin-top: 40rpx;">
					<view class="flex align-center">
						<image src="../../../static/images/index/jinbi.png" style="width: 38upx;height: 38upx;">
						</image>
						<view class="margin-left-xs">{{money}}</view>
					</view>
					<view class="popupbtn" @click="goNavs('/my/wallet/index')">充值</view>
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
										<view :class="TabCurr == item.giftId?'actives':''">{{ item.giftName }}</view>
										<view class="text-sm flex align-center justify-center margin-top-xs">
											<view>{{item.giftMoney}}</view>
											<image style="margin:0upx 6upx;width: 20rpx;height: 20rpx;"
												src="../../../static/images/index/jinbi.png"></image>
										</view>
										<view v-if="TabCurr == item.giftId" class="zengsong"
											@click="giveGift(item.giftId)">赠送
										</view>
									</view>
								</view>
							</swiper-item>
						</swiper>
						<!-- <view class="dots">
							<view v-for="(page, pageindex) in navlist" :key="pageindex"
								:class="{ active: pageindex == currentPageindex }"></view>
						</view> -->
					</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import tuiModal from "@/components/tui-modal/tui-modal.vue"
	export default {
		components: {
			tuiModal
		},
		data() {
			return {
				currentPageindex: 0,
				categoryHeight: '300rpx',
				juliSelect: '否',
				// modal: false,
				// moneyType: false,
				// moneycontent: '',
				// moneyMessage: '请输入打赏金额',
				// button: [{
				// 		text: '确定',
				// 		type: 'red'
				// 	},
				// 	{
				// 		text: '取消',
				// 		type: 'black'
				// 	}
				// ],
				shangxianSelect: '否',
				loading: true, // 是否显示骨架屏组件
				customStyle: {
					width: '380upx',
					color: '#FFFFFF',
					background: "#FBCA0C",
					border: 0
				},
				id: '',
				orderDet: {},
				page: 1,
				limit: 10,
				order: {},
				commentList: [],
				isFollow: false,
				myId: '',
				isVip: false,
				AUDIO: uni.createInnerAudioContext(),
				isPlay: false,
				dashangFeilv: 0,
				dashangZuidi: 0,
				dashangZuiGao: 0,
				fenX: 0,
				token: '',
				shangjia: '',
				show: false,
				money: 0,
				giftList: [],
				TabCurr: 0,
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				fastOrderId: 0,
				fastOrderTakeId: 0,
				fastContent: '',
				orderTakingId: '',
				count: 5,
				tuiguang:''
			}
		},
		onShareAppMessage(res) {
			return {
				path: '/pages/index/game/order?invitation=' + this.invitationCode + '&id=' + this.id +
					'&fenX=1', //这是为了传参   onload(data){let id=data.id;} 
				title: this.tuiguang,
				imageUrl: this.order.homepageImg[0]
			}
		},
		/*
		 * uniapp微信小程序分享页面到微信朋友圈
		 */
		onShareTimeline(res) {
			return {
				path: '/pages/index/game/order?invitation=' + this.invitationCode + '&id=' + this.id +
					'&fenX=1', //这是为了传参   onload(data){let id=data.id;} 
				title: this.tuiguang,
				imageUrl: this.order.homepageImg[0]
			}
		},
		onLoad(option) {
			console.log(option)
			this.id = option.id
			this.fenX = option.fenX
			if (option.fastOrderId) {
				this.fastOrderId = option.fastOrderId
				this.fastOrderTakeId = option.fastOrderTakeId
				this.fastContent = option.fastContent
			}
			this.$Request.getT('/app/common/type/116').then(res => { //分享文字
				if (res.code === 0) {
					if (res.data && res.data.value) {
						this.tuiguang = res.data.value;
					}
				}
			});
			//语音自然播放结束
			this.AUDIO.onEnded((res) => {
				this.isPlay = !this.isPlay;
			});

		},
		onShow() {
			this.juliSelect = this.$queue.getData('juliSelect');
			this.dashangZuidi = this.$queue.getData('dashangZuidi');
			this.dashangZuiGao = this.$queue.getData('dashangZuiGao');
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			this.token = this.$queue.getData('token');
			this.getDet()
			this.getOrderComment()
			this.getgift()
			this.getMoney()
			this.myId = uni.getStorageSync('userId')
			this.isVip = uni.getStorageSync('isVIP')
		},
		onReady() {
			this.AUDIO.onEnded(function(res) {
				this.isPlay = false;
			});
		},
		onUnload() {
			if (this.isPlay) {
				this.AUDIO.stop();
			}
			this.isPlay = !this.isPlay;
		},
		methods: {
			goTousu(userId) {
				if (this.token) {
					uni.navigateTo({
						url: '/my/order/complain?byUserId=' + userId + '&platform=4&platformId=' + this.id
					});
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}

			},
			goGuanZhuDetail(userId) {
				if (this.token) {
					uni.navigateTo({
						url: '/my/gird/guanzhuDetail?userId=' + userId
					});
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}

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
							this.giftList = datanew;
						}

						// this.giftList = res.data
					}
				});
			},
			// 打赏礼物
			binddaS(item) {
				this.TabCurr = item.giftId
			},
			scroll: function(e) {
				console.log(e)
				this.old.scrollTop = e.detail.scrollTop
			},
			giveGift(giftId) {
				console.log(this.order.userId)
				let data = {
					giftId: giftId,
					userId: this.order.userId,
					orderTakingId: this.orderTakingId

				}
				this.$Request.postJson('/app/gift/sendGift', data).then(res => {
					if (res.code == 0) {
						this.getMoney()
						this.$queue.showToast('打赏成功！');
						this.getDet()
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
			goDaShang() {
				if (this.token) {
					let userId = this.$queue.getData('userId');
					if (this.order.userId != userId) {
						// this.modal = !this.modal;
						this.show = true
					} else {
						this.$queue.showToast('自己不能打赏自己！');
					}
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
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
			// 详情
			getDet() {
				if (this.fenX == 1) {
					this.$Request.get("/app/orderTaking/queryTakingDetailss", {
						id: this.id,
						latitude: uni.getStorageSync('latitude') ? uni.getStorageSync('latitude') : '',
						longitude: uni.getStorageSync('longitude') ? uni.getStorageSync('longitude') : '',
					}).then(res => {
						if (res.code == 0) {
							this.loading = false
							if (res.data.distance) {
								res.data.distance = this.setMorKm(res.data.distance);
							}
							this.order = res.data
							this.orderTakingId = res.data.id
							this.shangjia = res.data.status
							this.order.homepageImg = res.data.homepageImg.split(',')
							this.order.label = res.data.label.split(',')
							this.selectFollow()
						} else {

						}
					});
				} else {
					this.$Request.get("/app/orderTaking/queryTakingDetails", {
						id: this.id,
						latitude: uni.getStorageSync('latitude') ? uni.getStorageSync('latitude') : '',
						longitude: uni.getStorageSync('longitude') ? uni.getStorageSync('longitude') : '',
					}).then(res => {
						if (res.code == 0) {
							this.loading = false
							if (res.data.distance) {
								res.data.distance = this.setMorKm(res.data.distance);
							}
							if (res.data.label) {
								res.data.label = res.data.label.split(',')
							}
							this.order = res.data
							this.orderTakingId = res.data.id
							this.shangjia = res.data.status
							this.order.homepageImg = res.data.homepageImg.split(',')
							this.selectFollow()
						} else {

						}
					});
				}

			},
			// 评论
			getOrderComment() {
				this.$Request.get("/app/takingComment/selectOrderTakingComment", {
					id: this.id,
					page: this.page,
					limit: this.limit
				}).then(res => {
					if (res.code == 0) {
						this.commentList = res.data.list;
						// this.commentList = [...this.commentList, ...res.data.list]
					}
				});
			},
			// 是否关注
			selectFollow() {
				this.$Request.get("/app/userFollow/selectFollowUser", {
					followUserId: this.order.userId
				}).then(res => {
					if (res.data == true) {
						this.isFollow = true
					} else {
						this.isFollow = false
					}
					this.loading = false;
				});

			},
			playVoice() {
				console.log(this.isPlay)
				if (this.order.voiceIntroduce) {
					this.AUDIO.src = this.order.voiceIntroduce;
				} else {
					this.AUDIO.src = this.order.fastVoice;
				}
				if (this.isPlay) {
					this.AUDIO.stop();
				} else {
					this.AUDIO.play();
				}
				this.isPlay = !this.isPlay;
			},
			goNav() {
				console.error('11111111111', this.fastOrderTakeId, this.fastOrderId)
				if (this.token) {
					if (this.shangjia == 0) {
						uni.navigateTo({
							url: "/pages/index/game/orderDet?id=" + this.id + '&fastOrderTakeId=' + this
								.fastOrderTakeId + '&fastOrderId=' + this.fastOrderId
						})
					} else {
						uni.showToast({
							title:'服务已下架',
							icon:'none',
							duration:3000
						})
					}
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
			},
			goNavs(e) {
				if (this.token) {
					uni.navigateTo({
						url: e
					})
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
			},
			goMsg() {
				if (this.token) {
					let data = {
						userId: this.myId,
						focusedUserId: this.order.userId
					}
					this.$Request.postJson('/app/chat/insertChatConversation ', data).then(res => {
						if (res.code == 0) {
							let id = this.order.userId == res.data.userId ? res.data.focusedUserId : this.order
								.userId
							uni.navigateTo({
								url: '/pages/msg/chat?chatConversationId=' + res.data.chatConversationId +
									'&byUserId=' + id + '&byNickName=' + this.order.userName
							})
						} else {
							this.$queue.showToast(res.msg);
						}
					})
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}

			},
			// 关注
			follow() {
				let that = this
				if (that.token) {
					that.$Request.get("/app/userFollow/insert", {
						followUserId: that.order.userId
					}).then(res => {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						setTimeout(function() {
							that.selectFollow()
						}, 500)
					});
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}

			}, // 查看图片
			saveImg(imgs, index) {
				// console.log(imgs)
				let that = this;
				let imgArr = imgs
				// imgArr.push(imgs);
				// //预览图片
				uni.previewImage({
					urls: imgArr,
					current: imgArr[index]
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
			}

		}

	}
</script>

<style lang="scss">
	.bg {
		background: #101014;
	}

	.jinbix {
		// background: #101014;
		display: flex;
		// border: 1rpx solid;
		margin-top: 30rpx;
		padding-bottom: 30rpx;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/1118a7b01f1c09f652baa505da9994fd.png');
		background-repeat: no-repeat;
		background-size: 100% 100%;
		// border-image: linear-gradient(to right, red, #578aef) 4;
		// border-image: linear-gradient(#d39cff, #ffffff, #cd8fff) 1;
		border-radius: 24rpx;
		// background-color: ;
		// background-image: linear-gradient(#d39cff, #ffffff, #cd8fff);
	}

	.popupbox {
		background: #101014;
		padding: 0upx 20upx;
	}

	.popupbtn {
		background: #FBCA0C;
		color: #0A0A0C;
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
		background: #1C1C22;
		// background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/1118a7b01f1c09f652baa505da9994fd.png');
		// background-repeat: no-repeat;
		// background-size: 100% 100%;
		border-radius: 15upx;
	}

	.zengsong {
		width: 100%;
		/* height: 56px; */
		background: #2C2613;
		border-radius: 0px 0px 15upx 15upx;
		padding: 10upx 0;
		margin: 24rpx 0px;

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

	.yuyinImg {
		width: 230upx;
		height: 60upx;
		text-align: center;
		line-height: 60upx;
		// background: #FFFFFF;
		// border-radius: 16upx;
		color: #FFFFFF;
		border-radius: 0rpx 0rpx 24rpx 24rpx;
		background: #2C2613;
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

	.pinglun {
		// background: #101014;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/1118a7b01f1c09f652baa505da9994fd.png');
		background-repeat: no-repeat;
		background-size: 100% 100%;
		color: #F5D876;
		// border: 1rpx solid;
		border-radius: 24rpx;
		padding: 30rpx 20rpx;
		display: flex;
	}

	.gambox {
		width: 70%;
		line-height: 39upx;
		background: linear-gradient(-90deg, #221A06 0%, #4A3808 100%);
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
		.mytit{
			width:90rpx;
			margin-left:10rpx;
			font-size:24rpx;
			display: -webkit-box;
			-webkit-box-orient: vertical;    
			-webkit-line-clamp: 1;    
			overflow: hidden;
			
		}
	}
</style>
