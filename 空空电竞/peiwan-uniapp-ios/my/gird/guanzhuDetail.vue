<template>
	<view class="">
		<view class="padding-lr u-skeleton-fillet padding-tb " v-if="order.userCertification">
			<swiper class="swiper" :autoplay="true" interval="5000" duration="500" :circular="true"
				style="width: 100%;height: 300rpx;">
				<swiper-item v-for="(item,index) in order.userCertification.skillImg" :key='index'
					@click="saveImg(order.userCertification.skillImg,index)">
					<image :src="item" mode="aspectFill" style="width: 710rpx;height: 300rpx;border-radius: 24rpx;">
					</image>
				</swiper-item>
			</swiper>
		</view>

		<view class="padding-lr padding-tb bg">
			<view class="flex" v-if="order.user">
				<view class="margin-lr-xs" style="position: relative;">
					<image :src="order.user.avatar ? order.user.avatar: '/static/logo.png'"
						style="width: 160upx;height: 160upx;border-radius: 8rpx;"></image>
					<!-- #ifdef APP || H5 -->
					<view v-if="order.user.isRoom == 1" class="yuliao flex align-center">
						<image src="../../static/images/index/yuliaosic.png" mode=""></image>
						语聊中
					</view>
					<!-- #endif -->
				</view>
				<view class="margin-left-sm" style="width: 100%;">
					<view class="flex justify-between">
						<view class="u-font-16  text-bold" style="display: flex;align-items: center;">
							{{order.user.userName}}
							<image src="../../static/images/index/tousu.png" @click="goTousu()"
								v-if="myId != order.user.userId"
								style="width:40rpx;height: 40rpx;margin-left: 20rpx;margin-top: 6upx;"></image>
						</view>
						<view v-if="myId != order.user.userId">
							<view @click="insert()" style="color: #AC75FE;">
								<view v-if="!isFollow" class="flex align-center" style="font-size: 27rpx;">
									<image src="../../static/images/index/guanzhu.png"
										style="width: 30rpx;height: 30rpx;">
									</image>
									关注
								</view>
								<view v-else class="flex align-center" style="font-size: 27rpx;">
									<image src="../../static/images/index/guanzhu1.png"
										style="width: 30rpx;height: 30rpx;">
									</image>
									已关注
								</view>
							</view>
						</view>
					</view>
					<view class="margin-top-sm flex align-center">
						<view v-if="order.user.sex == 1" class="  margin-right-xs"
							style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 8upx;color: #6EE3FB;border-radius: 50rpx;">
							<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
							{{order.user.age?order.user.age:0}}
						</view>
						<view v-if="order.sex == 2" class="  margin-right-xs"
							style="display: inline-block;background: linear-gradient(-90deg, #4F2B4B, #6735b1);padding: 2upx 8upx;color: #FB85FC;border-radius: 50rpx;">
							<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
							{{order.user.age?order.user.age:0}}
						</view>
						<!-- <view v-if="order.user.sex == 1" class="radius text-white margin-right-xs"
							style="display: inline-block;background: linear-gradient(0deg, #51bdfb 0%, #5c80f2 100%);padding: 2upx 8upx;">
							<u-icon name="man" color="#ffffff" size="24"></u-icon>
							{{order.user.age?order.user.age:0}}
						</view>
						<view v-if="order.user.sex == 2" class="radius text-white margin-right-xs"
							style="display: inline-block;background: linear-gradient(0deg, #FD8882 0%, #F07284 100%);padding: 2upx 8upx;">
							<u-icon name="woman" color="#ffffff" size="24"></u-icon>
							{{order.user.age?order.user.age:0}}
						</view> -->
						<view class="flex text-sm">
							<view class=" titbox" style="margin-right: 13rpx;" v-if="order.score">
								评分{{(order.score).toFixed(2) }}</view>
							<view class="titbox" v-if="order.ordersCount">{{order.ordersCount}}人下单</view>
						</view>
						<view class="chats" v-if="isFollow" @click="goMsg">
							联系TA
						</view>
					</view>

				</view>
			</view>
			<view style="width: 100%;height: 1rpx;background: #2D2F52;margin: 30upx 0upx;"></view>
			<view class="text-df text-bold" v-if="order.userCertification">个人简介</view>
			<view class="text-df margin-top-xs" style="color: #BABBCB;" v-if="order.userCertification">
				{{order.userCertification.details?order.userCertification.details:'暂无数据'}}
			</view>
		</view>

		<view class="" v-if="order.userCertification&&order.userCertification.idNumber">
			<scroll-view id="tab-bar" scroll-with-animation :scroll-x="true" :show-scrollbar="false"
				style="background: #211D33;">
				<view style="display: flex;">
					<view :key='index' v-for="(tab, index) in tabBars" :id="tab.id" :data-current="index"
						@tap="tabClick(index)" class=" flex-sub text-center" style="position: relative;">
						<view class="tui-tab-item-title" :class="{ 'tui-tab-item-title-active': tabIndex == index }">
							{{ tab.name }}
						</view>
						<view :class="{ 'tui-tab-item-title-actives': tabIndex == index }"></view>
					</view>
				</view>
			</scroll-view>
		</view>

		<!-- 评价 -->
		<view v-if="tabIndex == 0" class=" bg padding-lr padding-top-sm" style="color: #8C8DA6;">
			<view class="pinglun">
				<view style="flex:3">
					<view class="flex">
						<view class="text-lg text-bold text-white">
							<image
								src="https://pw.xianmxkj.com/file/uploadPath/2023/03/21/2c5a169516106ba081c7926b19c66c02.png"
								style="width: 186rpx;height: 59rpx;"></image>
						</view>
						<view class="flex align-center margin-left-sm">
							<view>
								<u-rate :count="count" v-model="order.score" active-color="#B578FA"
									inactive-icon="star-fill" inactive-color="#9e9ca8" :disabled="true"></u-rate>
							</view>
							<view v-if="order.score" style="color: #B578FA;font-size: 40rpx;margin-left: 12rpx;">
								{{(order.score).toFixed(2)}}
							</view>
							<!-- <u-icon v-for="ite in item.score" :key='ite' color="#B578FA" name="star-fill"></u-icon> -->
						</view>
					</view>
					<view class="flex justify-between margin-top-sm" style="padding-right: 60upx;">
						<view>技术水平</view>
						<view>服务态度</view>
						<view>响应速度</view>
					</view>
				</view>
				<view style="width: 1rpx;height: auto;background: #eee5ff;"></view>
				<view class="flex-sub text-center">
					<view style="font-size: 52upx;color: #B578FA;">{{order.ordersCount}}</view>
					<view>累计接单</view>
				</view>
			</view>
			<!-- <view style="width: 100%;height: 1rpx;background: #2D2F52;margin: 30upx 0upx;"></view> -->
			<view class="text-df text-bold text-white margin-top">用户评价</view>
			<view class="margin-tb-sm padding-bottom" v-for="(item, index) in commentList" :key='index'>
				<view class="flex justify-between">
					<!-- <u-avatar :src="item.avatar" size="48" ></u-avatar> -->
					<image :src="item.avatar?item.avatar:'/static/logo.png'" mode=""
						style="width: 55upx;height: 55upx;border-radius: 50rpx;"></image>
					<view class="flex-sub margin-left-xs" style="line-height: 46upx;">{{item.userName}}</view>
					<view class="flex align-center">
						<view>
							<u-icon v-for="ite in item.score" :key='ite' color="#B578FA" name="star-fill"></u-icon>
						</view>
					</view>
				</view>
				<view class="margin-top-sm text-white">{{item.content}}</view>
			</view>
			<view class="margin-tb-sm padding-bottom margin-top" v-if="commentList.length==0">暂无数据</view>
		</view>
		<!-- 动态 -->
		<view v-if="tabIndex == 2">
			<Head :list="orderList" @click="clickItem"></Head>
		</view>
		<!-- 服务列表 -->
		<view class="padding-lr-sm padding-bottom padding-top-sm" v-if="tabIndex == 1">
			<view class="bgbox u-skeleton-fillet" v-for="(item,index) in orderList" :key='index' @click="goOrder(item)">
				<view style="margin-top: -30rpx;position: relative;bottom: -6rpx;">
					<image :src="item.avatar?item.avatar: '/static/logo.png'"
						style="width: 230rpx;height: 260rpx;border-radius: 24rpx"></image>
					<view class="yuyinImg" @click.stop="playVoice(item.voiceIntroduce,item.isPlay,index)"
						v-if="item.voiceIntroduce">
						<view class="flex align-center box">
							<!-- 暂停 -->
							<image v-if="item.isPlay==false" src="../../static/jc-record/index.png" mode=""
								style="width: 40upx;height: 40upx;"></image>
							<!-- 播放 -->
							<image v-else src="../../static/jc-record/bof.png" mode=""
								style="width: 40upx;height: 40upx;"></image>

							<image src="../../static/jc-record/yuyin.png" mode=""
								style="width: 84upx;height: 32upx;margin: 0upx 10upx;"></image>
							<view v-if="item.sec">{{item.sec}}<text>"</text></view>
						</view>
					</view>
				</view>
				<view class="flex-sub margin-left text-white flex flex-direction justify-between padding-top-sm">
					<view class="flex align-center justify-between">
						<view class="flex" style="align-items: end;">
							<view class="margin-right-xs" style="display: inline-block;">
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
						<view class="margin-right" v-if="item.commentScore">评分{{(item.commentScore).toFixed(2)}}</view>
						<view class="margin-right" v-else>暂无评分</view>
						<view v-if="item.count">{{item.count}}人已下单</view>
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
						<view class="text-sm" style="color: #FBEA74;"><text
								style="font-size: 36rpx;font-weight: 600;">{{isVip? item.memberMoney :item.money }}</text><text
								style=""></text>币/{{item.unit}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<empty v-if="orderList.length == 0&&tabIndex==1"></empty>
		<empty v-if="orderList.length == 0&&tabIndex==2"></empty>
	</view>

</template>

<script>
	import Head from '../../components/Head.vue'
	import empty from '../../components/empty.vue'
	export default {
		components: {
			Head,
			empty
		},
		data() {
			return {
				userId: '',
				tabBars: [{
					name: '评价',
					id: 'pinglun',
					state: '0'
				}, {
					name: '技能',
					id: 'hot',
					state: '1'
				}, {
					name: '动态',
					id: 'yule',
					state: '2'
				}],
				tabIndex: 1,
				sex: 0,
				age: 0,
				orderList: [],
				followCoun: 0,
				avatar: '',
				userName: '',
				updateTime: '',
				isFollow: false,
				isVip: false,
				ByUserId: '',
				type: 1,
				page: 1,
				limit: 10,
				swiperList: [{
					imageUrl: '../static/bg.png'
				}, {
					imageUrl: '../static/bg.png'
				}],
				count: 5,
				value: 4,
				commentList: [],
				id: '',
				order: [],
				myId: '',
				isSafetyMoney: '',
				isPlay: false,
				AUDIO: uni.createInnerAudioContext(),
				musicIndex: '',
				renzheng: '',
				byNickName: ""
			}
		},
		onShareAppMessage(res) {
			return {
				path: '/my/gird/guanzhuDetail?invitation=' + this.invitationCode + '&userId=' + this
					.ByUserId, //这是为了传参   onload(data){let id=data.id;} 
				title: this.order.user.userName,
				imageUrl: this.order.userCertification.skillImg[0]
			}
		},
		/*
		 * uniapp微信小程序分享页面到微信朋友圈
		 */
		onShareTimeline(res) {
			return {
				path: '/my/gird/guanzhuDetail?invitation=' + this.invitationCode + '&userId=' + this
					.ByUserId, //这是为了传参   onload(data){let id=data.id;} 
				title: this.order.user.userName,
				imageUrl: this.order.userCertification.skillImg[0]
			}
		},
		onLoad(e) {
			this.userId = this.$queue.getData('userId');
			this.myId = uni.getStorageSync('userId')
			this.id = e.id
			this.yueSelect = this.$queue.getData('yueSelect');
			this.isSafetyMoney = uni.getStorageSync('isSafetyMoney')

			this.getRenZheng();
			this.getIsVip();
			if (e.userId) {
				this.ByUserId = e.userId;
				this.getByUserIdDetail();
				// this.getOrderComment()

				// this.selectFollow();
				this.getTaskList();
				this.selectFollow()
			}


			//语音自然播放结束
			this.AUDIO.onEnded((res) => {
				let musicIndexs = this.musicIndex;
				if (musicIndexs != '') {
					this.orderList[musicIndexs].isPlay = false;
				} else {
					this.orderList[0].isPlay = false;
				}
			});
		},
		onShow() {

		},
		onHide() {
			this.AUDIO.stop();
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.orderList[musicIndexs].isPlay = false;
			} else if (musicIndexs == 0) {
				this.orderList[0].isPlay = false;
			}
		},
		methods: {
			getRenZheng() {
				this.$Request.get("/app/userCertification/queryInsert").then(res => {
					if (res.code == 0) {
						// 0审核中 1通过 2拒绝 
						if (res.data == null) {
							this.renzheng = 0 //未实名
						} else if (res.data.status == 0) {
							this.renzheng = 1 //审核中
						} else if (res.data.status == 1) {
							this.renzheng = 2 //已实名
						} else if (res.data.status == 2) {
							this.renzheng = 3 //已拒绝
						}
					}
				});
			},
			// 播放语音
			playVoice(voiceIntroduce, isPlay, index) {
				this.isPlay = this.orderList[index].isPlay
				this.AUDIO.src = voiceIntroduce;
				if (this.isPlay == true) {
					this.AUDIO.stop();

					this.orderList[index].isPlay = false
				} else {
					let musicIndexs = this.musicIndex;
					if (musicIndexs != '') {
						this.orderList[musicIndexs].isPlay = false;
					} else if (musicIndexs == 0) {
						this.orderList[0].isPlay = false;
					}
					this.musicIndex = index;
					this.AUDIO.stop();

					this.AUDIO.play();
					this.orderList[index].isPlay = true
				}
				// this.isPlay = !this.isPlay;
			},
			// 获取用户所有评价
			getOrderComment() {
				this.$Request.get("/app/takingComment/selectCommentByOrderTakingUserId", {
					userId: this.ByUserId,
					page: this.page,
					limit: this.limit
				}).then(res => {
					if (res.code == 0) {
						// this.commentList = res.data.list;
						if (this.page == 1) {
							this.commentList = [];
						}
						res.data.list.forEach(d => {
							this.commentList.push(d);
						});
						// this.commentList = [...this.commentList, ...res.data.list]
					}
					uni.stopPullDownRefresh();
				});
			},
			clickItem: function(options) {
				console.log(options)
				if (options.index == 0) {
					uni.navigateTo({
						url: '/package/pages/detail/listDetails?talkId=' + this.orderList[options.id].talkId
					});
				} else if (options.index == 3) {
					this.saveLove(this.orderList[options.id].talkId);
				}
			},
			saveLove(talkId) {
				let userId = this.$queue.getData('userId');
				let data = {
					talkId: talkId,
					userId: userId
				}
				this.$Request.postJson('/app/gambit/saveGoodTalk', data).then(res => {
					if (res.status == 0) {
						this.page = 1;
						this.getTaskList();
					} else {
						this.$queue.showToast(res.msg);
					}
				});
			},
			tabClick(e) {
				this.tabIndex = e;
				this.page = 1;
				this.getTaskList();
			},
			goTousu() {
				let token = this.$queue.getData('token');
				if (token) {
					uni.navigateTo({
						url: '/my/order/complain?byUserId=' + this.ByUserId + '&platform=2&platformId='
					});
				} else {
					this.goLoginInfo();
				}

			},
			goMsg() {
				let token = this.$queue.getData('token');
				if (token) {
					let data = {
						userId: this.myId,
						focusedUserId: this.ByUserId
					}
					this.$Request.postJson('/app/chat/insertChatConversation', data).then(res => {
						if (res.code == 0) {
							uni.navigateTo({
								url: '/pages/msg/chat?chatConversationId=' + res.data.chatConversationId +
									'&byUserId=' + this.ByUserId + '&byNickName=' + this.byNickName
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

			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
						this.isVip = res.data
						uni.setStorageSync('isVIP', res.data)
					}
				});
			},
			// 是否关注
			selectFollow() {
				this.$Request.get("/app/userFollow/selectFollowUser", {
					followUserId: this.ByUserId
				}).then(res => {
					if (res.data == true) {
						this.isFollow = true
					} else {
						this.isFollow = false
					}
				});
			},
			goOrder(e) {
				let token = this.$queue.getData('token');
				if (token) {
					uni.navigateTo({
						url: '/pages/index/game/order?id=' + e.id
					});
				} else {
					this.goLoginInfo();
				}

			},
			insert() {
				let that = this
				let token = this.$queue.getData('token');
				if (token) {
					let data = {
						followUserId: this.ByUserId
					}
					that.$Request.get("/app/userFollow/insert", data).then(res => {
						console.log(res)
						if (res.code == 0) {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
							this.selectFollow();
							this.getByUserIdDetail();
						}
					});
				} else {
					this.goLoginInfo();
				}

			},
			getTaskList() {
				if (this.tabIndex == 0) {
					this.getOrderComment()
				} else if (this.tabIndex == 1) {
					this.$Request.getT('/app/orderTaking/selectUserOrderTaking?page=' + this.page + '&limit=' + this
						.limit +
						'&userId=' + this.ByUserId).then(res => {
						if (res.code == 0) {
							if (this.page == 1) {
								this.orderList = [];
							}
							res.data.list.forEach(d => {
								d.isPlay = false
								if (d.label) {
									d.label = d.label.split(',')
								}
								this.orderList.push(d);

							});
							this.loading = false;
							uni.stopPullDownRefresh();
						}
					});
				} else if (this.tabIndex == 2) {
					let userId = this.$queue.getData('userId');
					this.$Request.getT('/app/gambit/selectTalkPage?page=' + (this.page - 1) + '&size=' + this.limit +
						'&byUserId=' +
						userId + '&userId=' + this.ByUserId + '&followUserId=' + 0).then(res => {
						if (res.status == 0) {
							if ((this.page - 1) == 0 || res.status) {
								this.orderList = [];
							}
							res.data.list.forEach(d => {
								if (d.img) {
									d.img = d.img.split(',')
								}
								if (d.isWangZhe) {
									d.isWangZhe = d.isWangZhe.split(',')
								}
								this.orderList.push(d);
							})
							this.loading = false;
							uni.stopPullDownRefresh();
						}
						uni.hideLoading();
					});
				}
			},
			//查询用户详情
			getByUserIdDetail() {
				this.$Request.getT('/app/user/selectUserDetailsByUserId?userId=' + this.ByUserId).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						uni.setNavigationBarTitle({
							title: res.data.user.userName
						})
						this.byNickName = res.data.user.userName
						this.order = res.data;
						if (res.data.userCertification) {
							if (res.data.userCertification.skillImg) {
								this.order.userCertification.skillImg = res.data.userCertification.skillImg.split(
									',')
							}
						}

					}

				});
			},
			// 查看图片
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
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/login'
				});
			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getTaskList();
			this.getOrderComment()
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getTaskList();
			this.getOrderComment()
		},
	}
</script>

<style lang="less">
	page {
		background-color: #1A1728;
	}

	.yuliao {
		position: absolute;
		top: 10rpx;
		left: 10rpx;
		background-color: rgba(0, 0, 0, 0.5);
		padding: 4rpx;
		border-radius: 40rpx;
		font-size: 22rpx;
		font-weight: 500;

		image {
			width: 35rpx;
			height: 35rpx;
			border-radius: 50%;
			margin-right: 4rpx;
		}
	}

	.bg {
		background: #211D33;
	}

	.bgs {
		background-color: #282A47;
	}

	.tui-tabs {
		flex: 1;
		flex-direction: column;
		overflow: hidden;
		background-color: #1E1F31;
		/* #ifdef MP-ALIPAY || MP-BAIDU */
		height: 100vh;
		/* #endif */
	}

	.tui-tab-item-title {
		color: #FFFFFF;
		font-size: 30rpx;
		height: 80rpx;
		line-height: 80rpx;
		flex-wrap: nowrap;
		white-space: nowrap;
	}

	.tui-tab-item-title-active {
		color: #AC75FE;
		font-size: 32upx;
		font-weight: bold;
		text-align: center;
	}

	.tui-tab-item-title-actives {
		width: 40upx;
		height: 8upx;
		background: #AC75FE;
		border-radius: 4upx;
		position: absolute;
		left: 105rpx;
		bottom: 10rpx;
	}

	.titbox {
		background: #e0caff;
		color: #AC75FE;
		padding: 6rpx 15rpx;
		border-radius: 7rpx;
	}

	.jin {
		background: 1789FD;
		color: #fff;
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

	.head_box {
		width: 100% !important;
		margin: 0 !important;
		border-radius: 0 !important;
	}

	.chats {
		background: #AC75FE;
		color: #fff;
		display: inline-block;
		border-radius: 24rpx;
		padding: 9rpx 33rpx;
		/* margin-top: 20rpx' */
		font-size: 24rpx;
		margin-left: 10rpx;
	}

	.pinglun {
		// background: #291b3d;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/1118a7b01f1c09f652baa505da9994fd.png');
		background-repeat: no-repeat;
		background-size: 100% 100%;
		color: #DFC9FF;
		// border: 1rpx solid;
		border-radius: 24rpx;
		padding: 30rpx 20rpx;
		display: flex;
	}

	.gambox {
		width: fit-content;
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
			padding-right: 20rpx;
			max-width: 100rpx;
			// width: 110rpx;
			margin-left: 10rpx;
			font-size: 24rpx;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			overflow: hidden;

		}
	}
</style>