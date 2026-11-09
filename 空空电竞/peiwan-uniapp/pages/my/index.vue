<template>
	<view class="text-white">
		<view class="head">
			<view class=" u-flex u-p-l-30 u-p-t-30 ">
				<view class="u-m-r-10" style="position: relative;">
					<image :src="avatar" style="width: 100rpx;height: 100rpx;border-radius: 100rpx;"
						@click="goNav('/pages/my/userinfo')"></image>
					<!-- <view class="lovip" v-if="isVip">
						<image src="../../static/images/my/vip.png" style="width: 100%;height: 100%;"></image>
					</view> -->
				</view>
				<view class="u-flex-1 u-m-l-10 text-white" v-if="!isLogin" style="position: relative;top: 0rpx;">
					<view class="u-font-18  text-bold">{{userName}}
						<view v-if="status == 0 && yueSelect != '否'" class="label  "
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
						</view>
					</view>
					<view style="margin-top: 8upx;" class="u-font-13 u-tips-color"
						@click="goNav('/pages/me/vip/index')">
						ID：{{invitationCode}}</view>
				</view>
				<view v-else class="text-xl u-p-l-20 text-bold" @click="goLogin('/pages/public/login')">
					登录
				</view>
				<!-- <view v-if="!isLogin" class="" @click="goNav('/pages/my/invitationUser')">
				<image src="../../static/images/my/yaoqing.png" style="width: 163rpx;height: 58rpx;" mode=""></image>
			</view> -->
			</view>

			<view class="flex justify-around padding-tb">
				<view style="text-align: center;" @click="goNav('/my/gird/guanzhu?name=我的粉丝&type=1')">
					<view class="text-xxl">{{fans}}</view>
					粉丝
				</view>
				<view style="text-align: center;" @click="goNav('/my/gird/guanzhu?name=我的关注&type=2')">
					<view class="text-xxl">{{follow}}</view>
					关注
				</view>
				<view style="text-align: center;" @click="goNav('/my/gird/visitor')">
					<view class="text-xxl">{{visitor}}</view>
					访客
				</view>
				<view style="text-align: center;" @click="goNav('/my/gird/browse')">
					<view class="text-xxl">{{browse}}</view>
					足迹
				</view>
			</view>

			<view class=" padding-lr" style="position: relative;" v-if="huiyuanSelect != '否'">
				<image src="../../static/images/my/bg.png" style="width: 100%;height: 110rpx;" mode=""></image>
				<view class="flex justify-between  margin-lr padding-tb-sm radius"
					style="position: absolute;top: 0;width: 640rpx;">
					<image src="@/static/images/my/huiyuan.png" style="width: 58rpx;height:54rpx;margin-top: 12rpx;">
					</image>
					<view class="flex-sub margin-left-sm flex align-center"
						style="line-height: 74rpx;margin-top:-2rpx;">
						<image
							src="https://pw.xianmxkj.com/file/uploadPath/2023/03/20/ef9638f97d300bedfb794a7986cf4c09.png"
							style="width: 86rpx;height: 27rpx;"></image>
						<view class="line"></view>
						<view class=" ">开通享受专属福利</view>
					</view>
					<view v-if="!isVip" class="btn-bg" @click="goNav('/my/vip/index')">去开通</view>
					<view v-if="isVip" class="btn-bg" @click="goNav('/my/vip/index')">已开通</view>
				</view>
			</view>

		</view>



		<!-- <view class="bgbox flex align-center justify-between" @click="goNavs('/package/pages/play/index')"
			v-if="shangxianSelect !='否'">
			<view class="peiwan_center">
				<image src="../../static/images/my/peiwanzhongxin.png"></image>
				陪玩中心
			</view>
			<image src="../../static/images/my/right.png" style="width: 15rpx;height: 30rpx;"></image>
		</view> -->

		<!-- <view class="bgbox1 flex align-center justify-between padding-lr-sm padding-top">
			<view class=" text-center image_tu" @click="goNav('/my/order/index?index=1')">
				<image src="../../static/images/my/dfh.png"></image>
				<view>待付款</view>
				<view class="borderRa" v-if="orderCount.count0">{{orderCount.count0}}</view>
			</view>
			<view class=" text-center image_tu" @click="goNav('/my/order/index?index=2')">
				<image src="../../static/images/my/dfw.png"></image>
				<view>待服务</view>
				<view class="borderRa" v-if="orderCount.count1">{{orderCount.count1}}</view>
			</view>
			<view class=" image_tu text-center" @click="goNav('/my/order/index?index=3')">
				<image src="../../static/images/my/jxz.png"></image>
				<view>进行中</view>
				<view class="borderRa" v-if="orderCount.count4">{{orderCount.count4}}</view>
			</view>
			<view class=" image_tu text-center" @click="goNav('/my/order/index?index=4')">
				<image src="../../static/images/my/ywc.png"></image>
				<view>已完成</view>
			</view>
		</view> -->
		<view class="margin-top-sm">
			<view class="flex margin-lr justify-between" v-if="shangxianSelect != '否' && mylist.length >= 3">
				<view @click="goNavs(mylist[0].url)">
					<image :src="mylist[0].imageUrl" style="width: 337rpx;height: 260rpx;"></image>
				</view>
				<view>
					<view @click="goNav(mylist[1].url)">
						<image :src="mylist[1].imageUrl" style="width: 337rpx;height: 125rpx;"></image>
					</view>
					<view style="margin-top:5rpx;" @click="goNav(mylist[2].url)">
						<image :src="mylist[2].imageUrl" style="width: 337rpx;height: 125rpx;"></image>
					</view>
				</view>
			</view>
			<view class="my-entry-grid margin-lr" v-else-if="shangxianSelect != '否'">
				<view class="my-entry-card my-entry-play" @click="goNavs('/package/pages/play/index')">
					<view class="my-entry-title">陪玩中心</view>
					<view class="my-entry-subtitle">在线寻找你的玩伴</view>
					<image src="../../static/images/my/peiwanzhongxin.png" class="my-entry-play-icon"></image>
				</view>
				<view class="my-entry-side">
					<view class="my-entry-card my-entry-small my-entry-order" @click="goNav('/my/order/index?index=1')">
						<view class="my-entry-title-small">我的订单</view>
						<image src="../../static/images/my/yuewan.png" class="my-entry-small-icon"></image>
					</view>
					<view class="my-entry-card my-entry-small my-entry-wallet" @click="goNav('/my/wallet/index?index=1')">
						<view class="my-entry-title-small">我的钱包</view>
						<image src="../../static/images/my/ye.png" class="my-entry-small-icon"></image>
					</view>
				</view>
			</view>
		</view>
		<view class="bgbox2 ">
			<!-- <view class="text-lg text-bold padding-lr padding-top">常用功能</view> -->

			<view class=" flex align-center  flex-wrap ">
				<view class=" image_tu text-center" @click="goNav('/pages/my/invitationUser')" v-if="yueSelect != '否'">
					<image src="../../static/images/my/yao.png"></image>
					<view>邀请好友</view>
				</view>
				<view class=" text-center image_tu" @click="goNav('/package/pages/detail/dongtai')"
					v-if="yueSelect != '否'">
					<image src="../../static/images/my/dongtai.png"></image>
					<view>我的动态</view>
				</view>
				<view class=" text-center image_tu" @click="goNav('/package/pages/myComment/myComment')">
					<image src="../../static/images/my/myPinlun.png"></image>
					<view>我的评论</view>
				</view>
				<view class=" image_tu text-center" @click="goNav('/my/yuewanOrder/index')">
					<image src="../../static/images/my/yuewan.png"></image>
					<view>我的派单</view>
				</view>
				<view class=" text-center image_tu" @click="goNav('/my/gird/gift')" v-if="shangxianSelect !='否'">
					<image src="../../static/images/my/gift.png"></image>
					<view>礼物中心</view>
				</view>


				<!-- <view class=" text-center image_tu" @click="goNav('/my/wallet/index?index=2')"
					v-if="yueSelect != '否' && Huizhang || zhiRate != 0 || status == 2">
					<image src="../../static/images/my/cz.png"></image>
					<view>我的收益</view>
				</view>
				<view class=" text-center image_tu" @click="goNav('/my/wallet/index?index=1')"
					v-if="yueSelect != '否'&&yueSelect != '否'">
					<image src="../../static/images/my/ye.png"></image>
					<view>我的余额</view>
				</view> -->
				<view class=" image_tu text-center" @click="goNav('/package/pages/detail/youhuijuan')"
					v-if="yhqSelect != '否'">
					<image src="../../static/images/my/youhuijuan.png"></image>
					<view>我的优惠券</view>
				</view>

				<view class=" image_tu text-center" @click="goNav('/package/pages/gonghui/index')"
					v-if="Huizhang&&yueSelect != '否'">
					<image src="../../static/images/my/gonghui.png"></image>
					<view>公会中心</view>
				</view>
				<view class=" image_tu text-center" @click="goNav('/package/pages/paihangbang/moshi')"
					v-if="shangxianSelect != '否'">
					<image src="../../static/images/my/qngqing.png"></image>
					<view>青少年模式</view>
				</view>
				<view class=" image_tu text-center" @click="goNav('/my/team/team')">
					<image src="../../static/images/my/team.png"></image>
					<view>我的团队</view>
				</view>
				<view class=" text-center image_tu" @click="goNav('/package/pages/detail/goreport')"
					v-if="yueSelect != '否'">
					<image src="../../static/images/my/tousu.png"></image>
					<view>我的投诉</view>
				</view>

				<view class=" text-center image_tu" @click="goNav('/package/pages/feedbackIndex/feedbackIndex')">
					<image src="../../static/images/my/help.png"></image>
					<view>帮助中心</view>
				</view>
				<view class=" text-center image_tu" @click="goNav('/my/setting/index')">
					<image src="../../static/images/my/setting.png"></image>
					<view>设置中心</view>
				</view>
			</view>
		</view>
		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
		<!-- #ifdef APP || H5 -->
		<!-- 语聊房悬浮窗口 -->
		<drag-button v-if="isShowDrag" :startRoom="startRoom" @closeDrag="closeDrag" :isDock="true"
			:existTabBar="true" />
		<!-- #endif -->
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import configdata from '../../common/config.js'
	import dragButton from "@/components/drag-button/drag-button.vue";
	export default {
		components: {
			popup,
			dragButton
		},
		data() {
			return {
				isShowDrag: false,
				popupshow: false,
				content: '',
				zhiRate: 0,
				teamSelect: '否',
				yhqSelect: '否',
				YaoqingShangJin: false,
				huiyuanSelect: '否',
				yueSelect: '否',
				shangxianSelect: '否',
				avatar: '../../static/logo.png',
				isLogin: true,
				userName: '匿名',
				status: -1,
				browse: 0, //浏览数
				fans: 0, //粉丝数
				follow: 0, //关注数
				visitor: 0, //访客数
				userId: '',
				isVip: false,
				invitationCode: '', //邀请码
				list: [],
				orderCount: [],
				consortiaId: '',
				arr: [],
				Huizhang: false,

				mylist: [],
				startRoom: {}, //当前正在播放房间的信息
			}
		},
		onLoad() {
			this.orderCount = uni.getStorageSync('orderCount')
			this.teamSelect = this.$queue.getData('teamSelect');
			this.yhqSelect = this.$queue.getData('yhqSelect');
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			this.huiyuanSelect = this.$queue.getData('huiyuanSelect');
			this.yueSelect = this.$queue.getData('yueSelect');
			let that = this;
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
				//判断是否是从其他房间返回的
				try {
					let dataInfo = uni.getStorageSync('startRoom')
					if (dataInfo.strRoomId) {
						that.startRoom = dataInfo
						that.isShowDrag = true
						console.log(that.isShowDrag, '111111111111111111111')
					} else {
						that.startRoom = {}
						that.isShowDrag = false
					}
				} catch (e) {
					//TODO handle the exception
					that.startRoom = {}
				}
			}
			// #endif

			this.popupshowcl = false
			this.consortiaId = this.$queue.getData('consortiaId');
			let YaoqingShangJin = this.$queue.getData('YaoqingShangJin');
			if (YaoqingShangJin && YaoqingShangJin != 0) {
				this.YaoqingShangJin = true;
			} else {
				this.YaoqingShangJin = false;
			}
			this.userId = uni.getStorageSync('userId')
			this.orderCount = uni.getStorageSync('orderCount')
			// 三个主入口属于公共页面内容，未登录时也应显示；点击后再执行登录校验。
			this.getBannerList()
			if (this.userId) {
				this.isLogin = false
				this.getUserInfo()
				this.getRenZheng()
				this.getAmount()
				this.getIsVip()

				// this.$Request.getT('/app/orders/selectMyOrdersCount').then(res => {
				// 	if (res.code === 0) {
				// 		this.orderCount = res.data
				// 		uni.setStorageSync('orderCount', this.orderCount)

				// 	}
				// });
			} else {
				this.orderCount = []
				this.isLogin = true
				this.isVip = false
				this.userName = '匿名'
				this.browse = 0
				this.fans = 0
				this.follow = 0
				this.visitor = 0
				this.avatar = '../../static/logo.png'
			}

		},
		methods: {
			//关闭悬浮
			closeDrag() {
				this.isShowDrag = false
				uni.removeStorageSync('startRoom')
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
			//获取陪玩中心  订单  资金
			getBannerList() {
				this.$Request.get("/app/banner/selectBannerList", {
					classify: 8
				}).then(res => {
					if (res.code == 0) {
						this.mylist = res.data
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
							console.log('我是会长：' + '少时诵诗书')
							this.Huizhang = true
						} else {
							console.log('我不是会长：' + '少时诵诗书')
							this.Huizhang = false
						}
						uni.setStorageSync("Huizhang", this.Huizhang)
					}
				});

			},
			goNavs(e) {
				if (this.userId) {
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
					if (this.status == 2) {
						uni.navigateTo({
							url: e
						})
					} else {
						uni.navigateTo({
							url: '/my/renzheng/index'
						})
					}
				} else {
					this.content = '您还未登录,请先登录'
					this.popupshow = true
					// this.$Pmessage({
					// 	showModel: true,
					// 	title: '提示',
					// 	content: '您还未登录,请先登录',
					// 	maskcloseAble: false
					// }, {
					// 	emitBtn:()=> { //确认
					// 		console.log('用户点击确认');
					// 		uni.navigateTo({
					// 			url: '/pages/public/login'
					// 		})
					// 	},
					// 	emitClose:()=> { //取消
					// 		console.log('用户点击取消');
					// 	},
					// });

				}
			},
			sureT() {
				uni.navigateTo({
					url: '/pages/public/login'
				})
				this.popupshow = false
			},
			goNav(e) {
				if (this.userId) {
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
					uni.navigateTo({
						url: e
					})
				} else {
					this.content = '您还未登录,请先登录'
					this.popupshow = true
					// this.$Pmessage({
					// 	showModel: true,
					// 	title: '提示',
					// 	content: '您还未登录,请先登录',
					// 	maskcloseAble: false
					// }, {
					// 	emitBtn:()=> { //确认
					// 		console.log('用户点击确认');
					// 		uni.navigateTo({
					// 			url: '/pages/public/login'
					// 		})
					// 	},
					// 	emitClose:()=> { //取消
					// 		console.log('用户点击取消');
					// 	},
					// });
				}
			},
			goLogin(e) {
				uni.navigateTo({
					url: e
				})
			},
			getAmount() {
				this.$Request.get("/app/userBrowse/selectAmount").then(res => {
					if (res.code == 0) {
						this.browse = res.data.browse
						this.fans = res.data.fans
						this.follow = res.data.follow
						this.visitor = res.data.visitor
					}
				});
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						// #ifdef APP || H5
						if (Number(res.data.isRoom) === 1) { //有正在直播的房间
							uni.removeStorageSync('startRoom')
							this.isShowDrag = true
						}
						// #endif
						this.zhiRate = res.data.zhiRate ? res.data.zhiRate : 0;
						uni.setStorageSync('zhiRate', res.data.zhiRate)
						this.consortiaId = res.data.consortiaId
						if (this.consortiaId) {

							this.getUserTd()
						}
						uni.setStorageSync('consortiaId', res.data.consortiaId)
						this.userName = res.data.userName
						this.invitationCode = res.data.invitationCode
						this.avatar = res.data.avatar ? res.data.avatar : '../../static/logo.png'

						uni.setStorageSync('avatar', res.data.avatar)
						uni.setStorageSync('invitationCode', res.data.invitationCode)
						uni.setStorageSync('zhiFuBao', res.data.zhiFuBao)
						uni.setStorageSync('zhiFuBaoName', res.data.zhiFuBaoName)
						if (res.data.babyModel == 1) {
							uni.reLaunch({
								url: '/package/pages/paihangbang/moshi'
							})
						}
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
			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
						this.isVip = res.data
						uni.setStorageSync('isVIP', res.data)
					}
				});
			}

		}
	}
</script>

<style lang="scss">
	.head {

		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/20/c3e50814c6bb43cdeee6e046c93eae77.png');
		background-size: 100% 100%;
		/* #ifdef H5 */
		// height: 468rpx;
		padding: 0rpx 0;
		/* #endif */
		/* #ifndef H5 */
		// height: 430rpx;
		padding: 0rpx 0;
		/* #endif */
	}



	.bgbox {
		background: #272842;
		padding: 20upx 30upx;
		border-radius: 24upx;
		margin: 30upx;
	}

	.bgbox1 {
		background: #272842;

		border-radius: 24upx;
		margin: 30upx;
	}

	.bgbox2 {
		// background: #291B3D;
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/22/4954b77fd8b51a4e80da4f1c3d6d267e.png');
		background-size: 100% 100%;
		background-repeat: no-repeat;
		// padding: 40upx 30upx;
		border-radius: 24upx;
		margin: 30upx;
		padding-top: 40rpx;

	}

	.peiwan_center {
		display: flex;
		align-items: center;
		font-size: 32upx;
		font-weight: bold;
	}

	.peiwan_center image {
		width: 75upx;
		height: 75upx;
		margin-right: 13upx;
	}

	.image_tu {
		width: 25%;
		margin-bottom: 30rpx;
		position: relative;
	}

	.image_tu image {
		width: 60upx;
		height: 60upx;
		margin-bottom: 5upx;
	}

	.camera {
		width: 54px;
		height: 44px;

		&:active {
			background-color: #ededed;
		}
	}

	.btn-bg {
		text-align: center;
		margin-top: 14rpx;
		color: #603C1E;
		width: 128rpx;
		height: 48rpx;
		line-height: 48rpx;
		border-radius: 24rpx 24rpx 24rpx 24rpx;
		background: linear-gradient(-90deg, #e0a252ff, #fbdcb4ff);

	}

	.lovip {
		width: 100rpx;
		height: 35rpx;
		position: absolute;
		top: 68rpx;
	}

	.borderRa {
		position: absolute;
		top: 0rpx;
		right: 30rpx;
		background: red;
		border-radius: 60rpx;
		/* padding: 2rpx 10rpx; */
		width: 35rpx;
		text-align: center;

	}

	.line {
		width: 4rpx;
		height: 20rpx;
		border-radius: 2rpx 2rpx 2rpx 2rpx;
		background: #d3b89aff;
		margin: 0 15rpx;
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

	.my-entry-grid {
		display: flex;
		justify-content: space-between;
		height: 260rpx;
	}

	.my-entry-card {
		position: relative;
		overflow: hidden;
		box-sizing: border-box;
		color: #fff;
		border: 1rpx solid rgba(255, 255, 255, 0.12);
	}

	.my-entry-play {
		width: 337rpx;
		height: 260rpx;
		padding: 26rpx 22rpx;
		border-radius: 20rpx;
		background: linear-gradient(145deg, #7135a3 0%, #4b176f 100%);
	}

	.my-entry-side {
		width: 337rpx;
	}

	.my-entry-small {
		width: 337rpx;
		height: 125rpx;
		padding: 24rpx 20rpx;
		border-radius: 16rpx;
	}

	.my-entry-order {
		background: linear-gradient(135deg, #392d72 0%, #25346d 100%);
	}

	.my-entry-wallet {
		margin-top: 10rpx;
		background: linear-gradient(135deg, #5a235f 0%, #7d294f 100%);
	}

	.my-entry-title {
		position: relative;
		z-index: 2;
		font-size: 38rpx;
		font-weight: 700;
	}

	.my-entry-subtitle {
		position: relative;
		z-index: 2;
		margin-top: 10rpx;
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.8);
	}

	.my-entry-title-small {
		position: relative;
		z-index: 2;
		font-size: 28rpx;
		font-weight: 700;
	}

	.my-entry-play-icon {
		position: absolute;
		right: 34rpx;
		bottom: 30rpx;
		width: 130rpx;
		height: 130rpx;
		opacity: 0.95;
	}

	.my-entry-small-icon {
		position: absolute;
		right: 22rpx;
		top: 30rpx;
		width: 68rpx;
		height: 68rpx;
	}
</style>
