<template name="Head">
	<view class="head">
		<view class="head_box">
			<view>
				<view class="head_left" @tap="goFollowUser(item.userId)">
					<view style="width: 80%;display: flex;">
						<view class="head_img" @tap.stop="goGuanZhuDetail(item.userId)">
							<image :src="item.imageUrl ? item.imageUrl : '/static/logo.png'"></image>
						</view>
						<view style="margin-left: 20rpx;">
							<view class="head_name">{{item.nickName}}</view>
							<view class="head_data">{{item.createTime}}</view>
						</view>
					</view>
					<view v-if="item.userId != userId" style="width: 20%;text-align: right;margin-left: 20rpx;"
						@tap="goTousu(item.userId)">
						<image src="../../../static/images/index/tousu.png"
							style="width: 40rpx;height: 40rpx;margin-top: 25upx;"></image>
					</view>
				</view>

				<view class="head_conten">{{item.content}}</view>
				<view v-if="item.videoUrl" class="margin-lr margin-top-sm">
					<video
						id="talk-detail-video"
						:src="getVideoPreviewSrc(item.videoUrl)"
						:poster="getVideoPoster(item)"
						:initial-time="0.1"
						:controls="true"
						:show-center-play-btn="true"
						:show-play-btn="true"
						:show-fullscreen-btn="true"
						object-fit="cover"
						class="talk-video"
						@loadedmetadata="prepareVideoPreview">
					</video>
				</view>
				<view v-if="!item.videoUrl" class="head_image">
					<image @tap="selectImage(item.img,index)" v-for="(imgs,index) in item.img" :src="imgs"
						:class="item.img.length>1?'img2':'img1'" mode="aspectFit"></image>
				</view>
			</view>
			<!-- 分享 评论 点赞  -->
			<view class="user_mk">
				<view class="mk1" @tap="saveLove(item.talkId)">
					<view class="mk_image">
						<image src="../../../static/pw/xin.png"></image>
						<!-- <image src="../../../static/pw/xin.png" v-if="item.goodTalkByUserCount != 1"></image> -->
						<!-- <image src="../../../static/pw/xinon.png" v-if="item.goodTalkByUserCount == 1"></image> -->
					</view>
					<view class="mk_num">{{item.goodTalkCount ? item.goodTalkCount : 0}}</view>
				</view>
				<view class="mk2">
					<view class="mk_image">
						<image src="../../../static/pw/msg.png"></image>
						<!-- <image src="../../../static/pw/msg.png" v-if="item.commentByUserCount < 1"></image>
						<image src="../../../static/pw/msgon.png" v-if="item.commentByUserCount > 0"></image> -->
					</view>
					<view class="mk_num">{{item.commentCount ? item.commentCount : 0}}</view>
				</view>
				<view class="mk3" @tap="hide" v-if="false">
					<view class="mk_image">
						<image src="../../../static/pw/shang.png"></image>
						<!-- <image src="../../../static/pw/shang.png" v-if="item.rewardByUserCount < 1"></image>
						<image src="../../../static/pw/shangon.png" v-if="item.rewardByUserCount > 0"></image> -->
					</view>
					<view class="mk_num">{{item.rewardCount ? item.rewardCount : 0}}</view>
				</view>
			</view>
			<view style="display: flex;margin-left: 30rpx;align-items: center;" v-if="false">
				<view style="margin-left: 10rpx;color: #F5D876;">{{item.rewardCount ? item.rewardCount : 0}}人已打赏</view>
			</view>
			<view class=" " style="color: red;padding-left:30rpx" v-if="item.status==3&&item.auditContent">
				拒绝理由：{{item.auditContent}}
			</view>
			<!-- 用户评论 -->
			<view class="user_pl">
				<view class="pl" v-for="ite in list">
					<view class="pl_name">
						{{ite.nickName}}：
					</view>
					<view class="pl_name">{{ite.content}}</view>
				</view>
			</view>

			<!-- 展开更多 -->
			<!-- <view class="gengduo" v-for="item in item.gengduo" :key="item.id">
				{{item.name}}
				<image :src="item.icon"></image>
			</view> -->
			<!-- 加载更多提示 -->
			<!-- <view class="s-col is-col-24" style="margin-top: 30rpx;" v-if="item">
				<load-more :loadingType="loadingType" :contentText="contentText"></load-more>
			</view> -->
		</view>

		<!-- <tui-modal :show="modal" @cancel="hide" title="打赏用户" :moneyMessage="moneyMessage" place="请输入打赏金额"
			:button="button" @click="handleClick" :moneyType="moneyType" :content="moneycontent">
		</tui-modal> -->
		<!-- 礼物弹框 -->
		<u-popup v-model="show" mode="bottom" border-radius="14" :closeable="true">
			<view class="popupbox">
				<view class="padding-top text-bold text-lg popupboxbgs">快来给Ta送个小礼物吧~</view>
				<view class="flex justify-between align-center" style="margin-top: 20rpx;padding: 0 30rpx;">
					<view class="flex align-center">
						<image src="../../../static/images/index/jinbi.png" style="width: 38upx;height: 38upx;">
						</image>
						<view class="margin-left-xs">{{money}}</view>
					</view>
					<view class="popupbtn" @click="goNavs('/my/wallet/index?index=1')">充值</view>
				</view>
				<!-- 分类轮播 -->
				<view class="category" v-if="giftList.length>0">
					<view class="box">
						<swiper class="swiper" duration="300" :style="{ height: categoryHeight }">
							<swiper-item v-for="(nav, index5) in giftList" :key="index5">
								<view class="category-list flex align-center justify-around">
									<view class="iconss popbox" v-for="(item,ind) in nav" :key="ind"
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
									<view class="icon popbox" style="height: 0;"></view>
									<view class="icon popbox" style="height: 0;"></view>
									<view class="icon popbox" style="height: 0;"></view>
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
		<!-- 底部聊天输入框 -->
		<view class="input-box">
			<view style="display: flex;margin-top: 15rpx;width: 100%;">
				<input confirm-type="send" @confirm='setChatSave' type="text" v-model="content"
					style="width: 75%;height: 70rpx;background: #1C1C22;color: #FFFFFF;margin: 4rpx 10rpx 0;border-radius: 10rpx;padding-left: 10rpx;" />
				<view class="save" @tap='setChatSave'>发送</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { blockUser } from '@/common/blockUser'
	export default {
		data() {
			return {
				currentPageindex: 0,
				categoryHeight: '300rpx',
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
				content: '',
				userId: '',
				item: {},
				list: [],
				reward: [],
				talkId: 0,
				page: 0,
				size: 20,
				loadingType: 0,
				scrollTop: false,
				contentText: {
					contentdown: '上拉显示更多',
					contentrefresh: '正在加载...',
					contentnomore: '没有更多评论了'
				},
				show: false,
				money: 0,
				giftList: [],
				TabCurr: 0,
				scrollTop: 0,
				old: {
					scrollTop: 0
				}
			}
		},
		onLoad(d) {
			this.userId = this.$queue.getData('userId');
			this.talkId = d.talkId;
			console.log(this.talkId)
			this.getRenZheng();
			this.getListDetail();
			this.getPingLun();
			this.getDaShang();
		},
		onShow() {
			this.getgift()
			this.getMoney()
		},
		onShareAppMessage(res) {
			return {
				path: '/package/pages/details/listDetails?talkId=' + this.talkId, //这是为了传参   onload(data){let id=data.id;} 
				title: this.item.content,
				imageUrl: this.item.img[0]
			}
		},
		methods: {
			getVideoPoster(item) {
				if (!item) return '';
				if (item.videoCover) return item.videoCover;
				if (item.coverUrl) return item.coverUrl;
				if (item.poster) return item.poster;
				if (Array.isArray(item.img) && item.img.length) return item.img[0];
				return '';
			},
			getVideoPreviewSrc(url) {
				if (!url || url.indexOf('#t=') !== -1) return url;
				return url + '#t=0.1';
			},
			prepareVideoPreview() {
				if (this.getVideoPoster(this.item)) return;
				this.$nextTick(() => {
					const videoContext = uni.createVideoContext('talk-detail-video', this);
					if (videoContext && videoContext.seek) videoContext.seek(0.1);
				});
			},
			goTousu(userId) {
				const that = this
				uni.showActionSheet({
					itemList: ['举报该动态', '拉黑该用户'],
					success(res) {
						if (res.tapIndex === 0) {
							uni.navigateTo({
								url: '/my/order/complain?byUserId=' + userId + '&platform=3&platformId=' + that.talkId
							})
							return
						}
						if (res.tapIndex === 1) {
							uni.showModal({
								title: '拉黑提示',
								content: '拉黑后你将不再看到该用户发布的动态，确定继续吗？',
								success(modal) {
									if (!modal.confirm) return
									blockUser(userId)
									uni.showToast({
										title: '已拉黑该用户',
										icon: 'none'
									})
								}
							})
						}
					}
				})
			},
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
			goFollowUser(userId) {
				let token = this.$queue.getData('token');
				if (token) {
					//去关注页面
					uni.navigateTo({
						url: '/package/pages/task/followUser?userId=' + userId
					});

				} else {
					this.goLoginInfo();
				}
			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/login'
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
			goNavs(e) {
				uni.navigateTo({
					url: e
				})
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
				let data = {
					giftId: giftId,
					userId: this.item.userId,
					talkId: this.talkId
				}
				this.$Request.postJson('/app/gift/sendGift', data).then(res => {
					if (res.code == 0) {
						this.getMoney()
						this.$queue.showToast('打赏成功！');

						setTimeout(d => {
							this.getDaShang()
							this.getPingLun()
							this.getListDetail()
							uni.hideLoading();
							// this.show = false
						}, 1000);
					} else {
						uni.hideLoading();
						this.$queue.showToast(res.msg)
					}
				});

			},
			hide() {
				let userId = this.$queue.getData('userId');
				if (this.item.userId != userId) {
					this.show = true
				} else {
					this.$queue.showToast('自己不能打赏自己！');
				}
			},
			selectImage(image, index) {
				let _this = this;
				let imgsArray = image;
				// imgsArray[0] = image;
				uni.previewImage({
					current: index,
					urls: imgsArray
				});
			},
			saveShare() {
				this.$Request.getT('/app/gambit/addShare?talkId=' + this.talkId).then(res => {
					if (res.status == 0) {
						this.getListDetail();
					}
				});
			},
			setChatSave() {
				// if (this.renzheng != 2) {
				// 	uni.showModal({
				// 		title: '温馨提示',
				// 		content: '您还没有进行实名认证，请认证完成之后再来操作吧！',
				// 		showCancel: true,
				// 		cancelText: '取消',
				// 		confirmText: '确认',
				// 		success: res => {
				// 			if (res.confirm) {
				// 				uni.navigateTo({
				// 					url: "/my/renzheng/index"
				// 				});
				// 			}
				// 		}
				// 	});
				// 	return;
				// }
				if (this.content === '') {
					this.$queue.showToast('评论不能为空！');
					return;
				}
				if (this.$queue.getChatSearchKeys(this.content)) {
					uni.showToast({
						title: "输入内容带有非法关键字请重新输入",
						mask: false,
						duration: 1500,
						icon: "none"
					});
					this.$Request.postT('/app/risk/insertRisk?riskType=3&content=' + this.content).then(res => {

					})
					return;
				}
				let userId = this.$queue.getData('userId');
				let data = {
					talkId: this.talkId,
					userId: userId,
					content: this.content
				}
				this.$Request.postJson('/app/gambit/saveComment', data).then(res => {
					if (res.status == 0) {
						this.page = 0;
						this.content = '';
						this.getListDetail();
						this.getPingLun();
					} else {
						this.$queue.showToast(res.msg);
					}
				});
			},
			saveLove(talkId) {
				// if (this.renzheng != 2) {
				// 	this.$Pmessage({
				// 		showModel: true,
				// 		title: '温馨提示',
				// 		content: '您还没有进行实名认证，请认证完成之后再来操作吧！',
				// 		maskcloseAble: false
				// 	}, {
				// 		emitBtn:()=> { //确认
				// 			uni.navigateTo({
				// 				url: "/my/renzheng/index"
				// 			});
				// 		},
				// 		emitClose:()=> { //取消
				// 			console.log('用户点击取消');
				// 		},
				// 	});
				// 	return;
				// }
				let userId = this.$queue.getData('userId');
				let data = {
					talkId: talkId,
					userId: userId
				}
				this.$Request.postJson('/app/gambit/saveGoodTalk', data).then(res => {
					if (res.status == 0) {
						this.getListDetail();
					} else {
						this.$queue.showToast(res.msg);
					}
				});
			},
			getDaShang() {
				let userId = this.$queue.getData('userId');
				this.$Request.getT('/app/gambit/selectTalkRewardPage?page=0&size=5&talkId=' + this
					.talkId).then(res => {
					if (res.status === 0) {
						this.reward = res.data.content;
					}
				});
			},
			getPingLun() {
				this.loadingType = 1;
				// uni.showLoading({
				// 	title: '加载中...'
				// });
				let userId = this.$queue.getData('userId');
				this.$Request.getT('/app/gambit/selectCommentPage?page=' + this.page + '&size=' + this.size + '&talkId=' +
					this
					.talkId).then(res => {
					if (res.status === 0) {
						if (this.page === 0 || res.status) {
							this.list = [];
						}
						res.data.content.forEach(d => {
							this.list.push(d);
						});
						if (res.data.content.length === this.size) {
							this.loadingType = 0;
						} else {
							this.loadingType = 3;
						}
					} else {
						this.loadingType = 2;
					}
					uni.hideLoading();
				});
			},
			getListDetail() {
				uni.showLoading({
					title: '加载中...'
				});
				let userId = this.$queue.getData('userId');
				this.$Request.getT('/app/gambit/selectTalkByTalkId?talkId=' + this.talkId + '&byUserId=' + userId).then(
					res => {
						if (res.status == 0) {
							if (res.data.img) {
								res.data.img = res.data.img.split(',');
							}
							this.item = res.data;
						}
						uni.hideLoading();
					});
			},
			topScrollTap: function() {
				uni.pageScrollTo({
					scrollTop: 0,
					duration: 300
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
			goGuanZhuDetail(userId) {
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + userId
				});

			}
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getPingLun();
		},
		onPullDownRefresh: function() {
			this.page = 0;
			this.getPingLun();
		}
	}
</script>

<style lang="scss">
	page {
		background: #050506;
	}

	.head {
		width: 100%;
		margin-bottom: 50rpx;
	}

	.head_box {
		// width: 100%;
		// background: #372853;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/22/9762a6ea960f4016d8a0bbe43a14b3dd.png');
		background-size: 100% 100%;
		background-repeat: no-repeat;
		padding-top: 20rpx;
		margin-bottom: 200rpx;
		border-radius: 24rpx;
		margin: 0 30rpx;
	}

	.head_left {
		width: 90%;
		margin: 0 auto;
		display: flex;
	}

	.head_img image {
		width: 90rpx;
		height: 90rpx;
		border-radius: 100rpx;
	}

	.head_name {
		margin-top: 10rpx;
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: bold;
	}

	.head_data {
		margin-top: 15rpx;
		color: #F5D876;
		font-size: 24rpx;
	}

	.head_conten {
		width: 90%;
		margin: 0 auto;
		letter-spacing: 2rpx;
		font-size: 28rpx;
		margin-top: 20rpx;
		color: #FFFFFF;
	}

	.head_image {
		width: 90%;
		margin: 0 auto;
		margin-top: 20rpx;
	}

	.talk-video {
		width: 100%;
		height: 320rpx;
		border-radius: 10rpx;
		background-color: #101014;
	}

	.img1 {
		width: 420rpx;
		height: 320rpx;
	}

	.img2 {
		width: 202rpx;
		height: 202rpx;
		margin-top: 15rpx;
		margin-right: 5rpx;
		margin-left: 15rpx;
	}

	/* 分享 评论 点赞 */
	.user_mk {
		width: 90%;
		margin: 0 auto;
		display: flex;
		margin-top: 20rpx;
	}

	.mk1 {
		flex: 1;
		height: 80rpx;
		display: flex;
		justify-content: left;
		align-items: center;
	}

	.mk2 {
		flex: 1;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.mk3 {
		flex: 1;
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}

	.mk_image {
		width: 50rpx;
		height: 50rpx;
	}

	.mk_image image {
		width: 40rpx;
		height: 40rpx;
	}

	.mk_num {
		margin-top: -8rpx;
		color: #999999;
		margin-left: 10rpx;
	}

	/* 用户评论 */
	.user_pl {
		width: 100%;
		margin-top: 20rpx;
		padding-bottom: 30rpx;
	}

	.pl_name {
		font-size: 24rpx;
		font-family: PingFang SC Bold, PingFang SC Bold-Bold;
		font-weight: 700;
		text-align: left;
		color: #F5D876;
		line-height: 32rpx;
	}

	button::after {
		border: none;
	}

	.pl {
		width: 90%;
		margin: 0 auto;
		display: flex;
		justify-content: left;
		align-items: center;
		height: 50rpx;
		font-size: 24rpx;
		color: #F5D876;
	}

	.gengduo {
		height: 80rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 20rpx;
		color: #F5D876;
		letter-spacing: 2rpx;
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
		// background-color: #101014;
		// background: #372853;
		padding: 5rpx;
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

	.gengduo image {
		width: 16rpx;
		height: 10rpx;
	}

	.popupbox {
		background: linear-gradient(to right bottom, #1C1C22, #101014);
	}

	.popupbtn {
		background: linear-gradient(-90deg, #FBCA0C 0%, #D9A800 100%);
		color: #0A0A0C;
		border-radius: 8upx;
		padding: 15upx 45upx;
		/* margin-top: 40upx; */
	}

	.popbox {
		/* width: 25%; */
		width: 160rpx;
		height: 200rpx;
		text-align: center;
		margin-bottom: 30rpx;
	}

	.popupboxbgs {
		padding: 20rpx;
		padding-bottom: 16rpx;
		border-radius: 14rpx 14rpx 0 0;
		background: linear-gradient(110deg, rgba(255, 142, 206, 0.1), rgba(171, 116, 255, 0.1));

	}

	.active {
		background: linear-gradient(to right bottom, rgba(243, 134, 189, 0.1), rgba(164, 112, 248, 0.1));
		// background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/1118a7b01f1c09f652baa505da9994fd.png');
		// background-repeat: no-repeat;
		// background-size: 100% 100%;
		border-radius: 15upx;
	}


	.zengsong {
		width: 100%;
		/* height: 56px; */
		background: linear-gradient(-90deg, #FBCA0C 0%, #D9A800 100%);
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
					padding: 10upx 0;
					flex-flow: wrap;

					.iconss {

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
