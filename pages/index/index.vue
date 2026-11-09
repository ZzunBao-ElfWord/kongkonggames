<template>
	<view>
		<mescroll-body :sticky="true" ref="mescrollRef" @init="mescrollInit" @down="downCallback" @up="upCallback">
			<view>
				<view class="head">
					<view class=" searchx_box" @click.stop="goSearch">
						<view class="flex justify-between margin-right-sm" @tap.stop="goSelectCity"
							style="line-height: 68rpx;">
							<image src="../../static/images/index/place.png"
								style="width: 27rpx;height: 37rpx;margin-top:15rpx"></image>
							<view class="localName text-white margin-left-sm">{{city}}</view>
						</view>
						<u-search class="flex-sub" placeholder="搜索你想要的电竞教练" shape="round" disabled :show-action="false"
							height="76" :animation="true" bg-color="#1C1C22" search-icon-color="#A1A2B2"
							placeholder-color="#A1A2B2">
						</u-search>
					</view>
				</view>
				<!-- #ifdef H5 -->
				<view class="padding-lr" style="margin-top: 170rpx;">
				<!-- #endif -->
					<!-- #ifdef MP-WEIXIN -->
					<view class="padding-lr padding-top-xs" style="margin-top: 250rpx;">
					<!-- #endif -->
						<!-- #ifdef APP -->
						<view class="padding-lr padding-top-xs" style="margin-top: 200rpx;">
						<!-- #endif -->
							<swiper class="screen-swiper" style="height: 200rpx;" :circular="true" :autoplay="true"
								interval="5000" duration="800">
								<swiper-item v-for="(item,index) in swiperList" :key="index" @click="goNav(item.url)">
									<image :src="item.imageUrl"
										style="width: 100%;;height: 200rpx;border-radius: 24upx;">
									</image>
								</swiper-item>
							</swiper>
						</view>

					</view>
					<view class="margin-top-xs" style="color: #BFBFCB;">
						<u-grid :col="5" :border="false">
					<u-grid-item bg-color="#050506" v-for="(item,index) in gridData" :key='index'
						@tap="goGridItem(item)">
								<image :src="item.imageUrl" style="width: 80rpx;height: 80rpx;">
								</image>
								<view class="text-white margin-top-xs text-sm">{{item.name}}</view>
							</u-grid-item>
						</u-grid>
					</view>
					<view class="flex margin-lr justify-between margin-top-xs" v-if="bannerList.length!=0">
						<view @click="getPaihangbang(bannerList[0].url)">
							<image :src="bannerList[0].imageUrl" style="width: 339rpx;height: 166rpx;"></image>
						</view>
						<view @click="getFastOrder(bannerList[1].url)">
							<image :src="bannerList[1].imageUrl" style="width:339rpx ;height: 166rpx;"></image>
						</view>
					</view>
					<!-- 萌新推荐 -->
					<view class="fubg" v-if="lowTaking.length">
						<view class="headtop">
							<view class="flex align-center" style="height: 87rpx;">
								<text
									style="font-size: 36rpx;font-weight: bold;color: #FBCA0C;letter-spacing: 2rpx;">明星教练</text>
							</view>
							<view class="title_right" @tap="goLowTaking()">
								更多
								<image class="margin-left-sm" style="width: 14rpx;height: 24rpx;display: inline-block;"
									src="../../static/images/index/right.png"></image>
							</view>
						</view>
						<view class="headbot" style="">
							<view v-for="(item,index) in lowTaking" :key='index' @click="goOrder(item)"
								style="width: 200rpx;">
								<view class="u-relative">
									<image style="width: 200rpx;height: 200rpx;border-radius: 20rpx;"
										:src="item.avatar?item.avatar: '../../static/logo.png'">
									</image>
								</view>
								<view class="margin-top-xs text-white text-df text-cut" style="height: 40rpx;">
									{{item.userName}}
								</view>
								<view class="margin-top-xs flex align-center" style="color: #FBCA0C;">
									<image style="display: inline-block;margin-right: 6upx;width: 30rpx;height: 30rpx;"
										src="../../static/images/index/jinbi.png"></image>
									<text class=" ">{{isVip? item.memberMoney :item.money}}币</text>/局
								</view>
							</view>
						</view>
					</view>
					<!-- 房间推荐 -->
					<!-- #ifdef APP || H5 -->
					<view class="fubg"
						style="background: #101014;border: 1rpx solid #2A2A32;border-radius: 24rpx;padding: 20rpx 0;"
						v-if="roomList.length">
						<view class="headtop">
							<image src="../../static/images/index/yltj.png"
								style="width: 210rpx;height: 55rpx;margin-left: 20rpx;margin-top: 30rpx;">
							</image>
							<view class="title_right" @tap="goNav('/my/roomList/roomList')">
								更多
								<image class="margin-left-sm" style="width: 14rpx;height: 24rpx;display: inline-block;"
									src="../../static/images/index/right.png"></image>
							</view>
						</view>
						<view class="roomList">
							<scroll-view class="roomLists" scroll-x="true">
								<view class="roomLists-item" v-for="(item,index) in roomList" :key="index"
									@click="goRoom(item)">
									<image :src="item.roomImg?item.roomImg: '../../static/logo.png'" mode="aspectFill">
									</image>
									<view class="roomLists-item-txt">
										{{item.roomName}}
									</view>
								</view>
							</scroll-view>
						</view>
					</view>
					<!-- #endif -->

					<view class="margin-top-xs">
						<ren-dropdown-filter :filterData='filterData' :border="false" :defaultIndex='defaultIndex'
							@onSelected='change'>
						</ren-dropdown-filter>
					</view>
					<view class="margin-lr-sm" v-if="orderList.length">
						<view class="bgbox" v-for="(item,index) in orderList" :key='index' @click="goOrder(item)">
							<view style="margin-top: -30rpx;position: relative;bottom: -6rpx;">
								<image @click.stop="item.isRoom == 1?goRoom(item):''"
									:src="item.avatar?item.avatar: '../../static/logo.png'" mode="aspectFill"
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
								<!-- #ifdef APP || H5 -->
								<view v-if="item.isRoom == 1" class="yuliao flex align-center">
									<image src="../../static/images/index/yuliaosic.png" mode=""></image>
									语聊中
								</view>
								<!-- #endif -->

							</view>
							<view class="flex-sub margin-left padding-top-sm flex flex-direction justify-between order-card-info">
								<view class="flex align-center justify-between">
									<view class="flex align-center ">
										<view class="margin-right-xs text-lg text-bold text-cut"
											style="display: inline-block;">
											{{item.userName}}
										</view>
										<view v-if="false" class="  margin-right-xs"
											style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 12upx;color: #6EE3FB;border-radius: 50rpx;">
											<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
											{{item.age?item.age:0}}
										</view>
										<view v-if="false" class="  margin-right-xs"
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
										<!-- <view class="text-sm" v-if="item.city" style="color: #FFFFFF;display: inline-block;">
									{{item.city}} {{juliSelect === '是' ? item.distance : ''}}
								</view> -->
									</view>

								</view>
								<view class="flex" v-if="item.label ">
									<view v-for="(ite,ind) in item.label" :key="ind">
										<image :src="ite" style="width: 98rpx;height: auto;margin-right: 5rpx;"
											mode="widthFix">
										</image>
									</view>
								</view>
								<view class="flex text-sm order-card-meta" style="color: #FFFFFF;">
									<view class="flex order-card-score">
										<view class="margin-right" v-if="item.commentScore">
											评分{{(item.commentScore).toFixed(2)}}
										</view>
										<view class="margin-right" v-else>暂无评分</view>
										<view v-if="item.count">{{item.count}}人已下单</view>
									</view>
									<view class="order-card-price" style="color: #FBCA0C;">
										<text class="text-lg text-bold">{{isVip? item.memberMoney :item.money }}</text>币/局
									</view>
								</view>
								<view class="flex align-center justify-between order-card-footer">
									<view class="gambox">
										<view class="gamebox">
											<image :src="item.gameImg" mode="aspectFill" style="">
											</image>
											<view class="level">{{item.gameName}}</view>
										</view>
										<view class="mytit">{{item.myLevel}}</view>
									</view>
								</view>
							</view>
						</view>
					</view>
					<empty v-if="orderList.length == 0"></empty>
		</mescroll-body>
		<!-- 青少年模式弹窗 -->
		<u-popup v-model="qingshow" mode="center" width="551rpx" bg-color="transparent"
			:mask-close-able="false">
			<view class="teen-mode-dialog">
				<image class="teen-mode-image"
					src="https://pw.xianmxkj.com/file/uploadPath/2023/11/24/54d953a59e236f79a87dd516d2316703.png">
				</image>
				<view class="teen-mode-title">青少年模式</view>
				<view class="teen-mode-content">
					为呵护未成年人健康成长，平台推出青少年模式。开启后，部分功能将受到限制，请由监护人设置监护密码。
				</view>
				<view class="flex align-center justify-between">
					<view class="btnn" @click="gomoshi()">去开启</view>
					<view class="btnns" @tap="gbQs()">我知道了</view>
				</view>
			</view>
		</u-popup>
		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
		<popup v-if="popupshowsq" :show="popupshowsq" @closeT="popupshowsq = false" @sureT="suresq"
			:content="contentsq"></popup>
		<popup v-if="popupshowcl" :maskCloseAble="false" :show="popupshowcl" closeName="取消" confirmName="进入"
			@closeT="popupshowcl = false" @sureT="surecl" :content="contentcl"></popup>
		<!-- #ifdef APP || H5 -->
		<!-- 语聊房悬浮窗口 -->
		<drag-button v-if="isShowDrag" :startRoom="startRoom" @closeDrag="closeDrag" :isDock="true"
			:existTabBar="true" />
		<!-- #endif -->
		<!-- 新人红包 -->
		<couponTipsCom @closeCop="closeCop" @getMyNewCoupon="getMyNewCoupon" v-if="showCoupon" :list="couponList"
			:showCoupon="showCoupon" />

	</view>
</template>

<script>
	// #ifdef H5
	import TRTC from 'trtc-sdk-v5';
	// // 创建trtc对象
	const trtc = TRTC.create();
	// #endif
	import MescrollMixin from "@/components/mescroll-uni/components/mescroll-uni/mescroll-mixins.js";
	import mescrollBody from "@/components/mescroll-uni/components/mescroll-body/mescroll-body.vue";
	import meTabs from "@/components/mescroll-uni/me-tabs/me-tabs.vue";
	import empty from '@/components/empty.vue'
	import popup from '@/components/popup.vue'
	import RenDropdownFilter from '@/components/ren-dropdown-filter/ren-dropdown-filter.vue'
	//导入权限判断
	// #ifdef APP
	import permision from '@/js_sdk/wa-permission/permission.js'
	// #endif
	import configdata from '../../common/config.js'
	import dragButton from "@/components/drag-button/drag-button.vue";
	import couponTipsCom from '../../components/couponTipsCom/couponTipsCom.vue'
	export default {
		mixins: [MescrollMixin], // 使用mixin
		components: {
			mescrollBody,
			meTabs,
			empty,
			RenDropdownFilter,
			popup,
			dragButton,
			couponTipsCom
		},
		data() {
			return {
				showCoupon: false, //是否显示新人优惠券
				couponList: [], //新人优惠券列表
				qingshow: false,
				isShowDrag: false,
				contentcl: '你当前有正在直播的房间，进入新房间会解散当前房间，是否进入新房间？',
				popupshowcl: false, //是否重连弹框
				popupshowsq: false,
				contentsq: '未获取麦克风权限，请前往设置开启麦克风权限',
				popupshow: false,
				content: '',
				popupType: 0,
				juliSelect: '否',
				shangxianSelect: '否',
				defaultSelected: [],
				tabIndex: 0, // tab下标
				tabData: [{
					createTime: "",
					gameName: '推荐',
					gameImg: "",
					id: 0,
					status: 0,
					updateTime: "",
				}],
				IdStatus: 0,
				yueSelect: '否',
				swiperList: [],
				bannerList: [],
				gridData: [],
				lowTaking: [],

				value1: '',
				value2: '',
				value3: '',

				defaultIndex: [0, 0],
				filterData: [
					[{
							label: '智能排序',
							value: '',
						},
						{
							label: '距离优先',
							value: 3,
						},
						{
							label: '人气优先',
							value: 2,
						},
						{
							label: '同城优先',
							value: 1,
						}
					],
					[{
							label: '不限价格',
							value: '',
						},
						{
							label: '从高到低',
							value: 'desc',
						},
						{
							label: '从低到高',
							value: 'asc',
						}
					],
				],

				city: '请选择城市',
				latitude: '',
				longitude: '',
				orderList: [],
				token: '',
				isVip: false,
				myId: uni.getStorageSync('userId') ? uni.getStorageSync('userId') : '',
				showModal: true,
				backgroundImage: '',
				tuiguang: '',
				invitationCode: '',
				arr: [],
				myFabu: '',
				fabuNumber: '',
				fabuPrice: '',
				yuewanFabu: '否',
				yuewanImage: '',
				invitationCode: "",
				tuiguang: '快来和我一起打游戏吧!',
				bgImg: '',
				isPlay: false,
				AUDIO: uni.createInnerAudioContext(),
				musicIndex: '',
				roomList: [], //推荐语聊房
				roomInfo: {}, //房间信息
				startRoom: {}, //当前正在播放房间的信息
				itemRoom: {}, //点击的房间信息
			}
		},
		onShareAppMessage(res) {
			return {
				path: '/pages/index/index',
				title: this.tuiguang,
				imageUrl: this.backgroundImage
			}
		},
		/*
		 * uniapp微信小程序分享页面到微信朋友圈
		 */
		onShareTimeline(res) {
			return {
				path: '/pages/index/index',
				title: this.tuiguang,
				imageUrl: this.backgroundImage
			}
		},
		onLoad(e) {
			this.invitationCode = this.$queue.getData('invitationCode');
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			this.yuewanFabu = this.$queue.getData('yuewanFabu')
			this.yuewanImage = this.$queue.getData('yuewanImage')
			let that = this
			that.$Request.getT('/app/banner/selectBannerList?state=-1&classify=5').then(res => {
				if (res.code === 0) {
					that.backgroundImage = res.data[0].imageUrl;
				}
			});
			that.$Request.getT('/app/common/type/116').then(res => {
				if (res.code === 0) {
					if (res.data && res.data.value) {
						that.tuiguang = res.data.value;
					}
				}
			});
			that.$Request.getT('/app/common/type/308').then(res => { //快速派单图
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.yuewanImage = res.data.value
					}
				}
			});
			that.getClassfly()

			if (that.myId) {
				that.$Request.getT('/app/common/type/235').then(res => { //新订单通知
					if (res.code == 0) {
						if (res.data && res.data.value) {
							that.arr.push(res.data.value)
						}
					}
				})
				that.$Request.getT('/app/common/type/236').then(res => { //微信退款
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
				this.$Request.getT('/app/common/type/116').then(res => { //分享文字
					if (res.code === 0) {
						if (res.data && res.data.value) {
							this.tuiguang = res.data.value;
						}
					}
				});
				this.$Request.getT('/app/common/type/313').then(res => { //分享图片配置 
					if (res.code === 0) {
						if (res.data && res.data.value) {
							this.bgImg = res.data.value;
						}
					}
				});
			}

			uni.getLocation({
				type: 'gcj02',
				geocode: true, //设置该参数为true可直接获取经纬度及城市信息
				success: function(res) {
					console.log(res, '地理位置')
					that.latitude = res.latitude
					that.longitude = res.longitude
					uni.setStorageSync('latitude', res.latitude)
					uni.setStorageSync('longitude', res.longitude)

					// #ifdef APP-PLUS
					that.city = res.address.city
					uni.setStorageSync('city', res.address.city)
					let data = {
						num: 1,
						size: 10
					}
					that.getData(data)
					// #endif

					// #ifdef H5
					that.selectCity(that.longitude, that.latitude);
					// #endif

					// #ifdef MP-WEIXIN
					// that.selectCity(that.longitude, that.latitude);
					uni.request({
						url: 'https://apis.map.qq.com/ws/geocoder/v1/?location=' + that.latitude +
							',' + that.longitude + '&key=REDACTED_CONFIGURE_LOCALLY',
						success(re) {
							if (re.statusCode === 200) {
								let citydata = re.data.result.address_component.city
								console.log("获取城市名称成功", citydata)
								that.city = citydata ? citydata : '未知'
								uni.setStorageSync('city', citydata)
								let data = {
									num: 1,
									size: 10
								}
								that.getData(data)
							} else {
								console.log("获取信息失败，请重试！")
							}
						}
					});
					// #endif
				},
				fail: function() {
					console.log('获取地址失败')
				}
			})
			//语音自然播放结束
			that.AUDIO.onEnded((res) => {
				console.log("this.musicIndex___:" + that.musicIndex)
				let musicIndexs = that.musicIndex;
				if (musicIndexs != '') {
					that.orderList[musicIndexs].isPlay = false;
				} else {
					that.orderList[0].isPlay = false;
				}
			});
		},
		onShow() {
			let that = this
			that.startRoom = {}
			// #ifdef APP || H5
			// #ifdef APP
			//检测麦克风权限
			this.getAudioPermision()
			// #endif
			//获取语聊房间
			this.getRoomList()
			if (uni.getStorageSync('token')) {
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
				this.getUserId()
			} else {
				this.roomInfo = {
					strRoomId: '',
					roomId: '',
					userId: ''
				}
			}
			// #endif
			this.yuewanImage = this.$queue.getData('yuewanImage')
			this.juliSelect = this.$queue.getData('juliSelect');
			this.yueSelect = this.$queue.getData('yueSelect');
			this.fabuNumber = this.$queue.getData('fabuNumber');
			this.yuewanFabu = this.$queue.getData('yuewanFabu')
			this.fabuPrice = this.$queue.getData('fabuPrice')
			that.city = uni.getStorageSync('city') ? uni.getStorageSync('city') : '请选择城市'
			that.getBannerList()
			that.getbannerList1()
			that.getGrid()
			that.getLowTaking()
			that.getUserFabu()
			that.token = uni.getStorageSync('token')
			if (uni.getStorageSync('token')) {
				that.getUserInfo();
				that.getIsVip()
				that.getRenZheng();
			}
			that.myId = uni.getStorageSync('userId')
			console.log(that.showModal, '------', that.myId)
			// #ifdef MP-WEIXIN
			//订阅
			if (that.myId) {
				if (this.showModal) {
					setTimeout(d => {
						this.openMsg()
					}, 2000)
				}
			}
			// #endif

		},
		onHide() {
			this.AUDIO.stop();
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.orderList[musicIndexs].isPlay = false;
			}
		},
		methods: {
			closeCop() {
				this.showCoupon = false
			},
			//领取优惠券
			getMyNewCoupon() {
				uni.showLoading({
					title: '领取中'
				})
				this.$Request.post('/app/selfCouponUser/getNewUserCoupon').then(res => {
					if (res.status == 0) {
						uni.hideLoading()
						this.showCoupon = false
						uni.showToast({
							title: '领取成功'
						})
					} else {
						uni.hideLoading()
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//获取新人优惠券列表
			getNewCouponList() {
				this.$Request.getT('/app/common/type/818').then(res => {
					if (res.code == 0) {
						if (res.data && res.data.couponList) {
							if (res.data.couponList.length > 0) {
								this.couponList = res.data.couponList
								this.showCoupon = true
							}

						}
					}
				});
			},
			gbQs() {
				// 先在本机记录用户已阅读，避免网络或旧版后端异常导致弹窗无法关闭。
				this.qingshow = false;
				uni.setStorageSync('teenModeNoticeAcknowledged', true);
				this.$Request.postT('/app/user/iKnow').then(res => {
					if (!res || res.code != 0) {
						console.warn('同步青少年模式提示状态失败', res && res.msg);
					}
				}).catch(err => {
					console.warn('同步青少年模式提示状态失败', err);
				});
			},
			gomoshi() {
				//跳转青少年模式
				uni.navigateTo({
					url: '/package/pages/paihangbang/moshi'
				})
			},
			//重置房间信息
			resetting() {
				uni.removeStorageSync('startRoom')
				this.itemRoom = {}
				this.startRoom = {}
				this.roomInfo = {}
			},
			//关闭悬浮
			closeDrag() {
				this.isShowDrag = false
				uni.removeStorageSync('startRoom')
				this.getRoomList()
			},
			//获取用户信息
			getUserId() {
				this.$Request.getT('/app/user/selectUserById').then(res => {
					if (res.code == 0) {
						if (Number(res.data.isRoom) === 1) { //有正在直播的房间
							uni.removeStorageSync('startRoom')
							// 存储房间信息
							this.roomInfo = {
								strRoomId: res.data.roomNo,
								roomId: res.data.roomId,
								userId: res.data.userId
							}
							this.isShowDrag = true
						} else {
							this.roomInfo = {
								strRoomId: '',
								roomId: '',
								userId: ''
							}
						}

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//服务端解散房间
			dismissRoom() {
				let data = {
					roomNo: this.roomInfo.strRoomId
				}
				let that = this
				this.$Request.getT('/app/room/dismissRoom', data).then(res => {
					if (res.code == 0) {
						//关闭弹框
						this.popupshowcl = false
						this.isShowDrag = false
						//退出trtc房间
						// #ifdef APP
						this.$trtcCloud.exitRoom();
						uni.navigateTo({
							url: '/my/voice/voice?strRoomId=' + that.itemRoom.roomNo +
								'&role=TRTCRoleAudience' +
								'&roomId=' +
								that.itemRoom.roomId,
							success: function(res) {
								that.resetting()
							}
						})
						// #endif
						// #ifdef H5
						trtc.exitRoom();
						trtc.destroy();
						uni.navigateTo({
							url: '/my/voice/voiceH5?strRoomId=' + that.itemRoom.roomNo +
								'&role=TRTCRoleAudience' +
								'&roomId=' +
								that.itemRoom.roomId,
							success: function(res) {
								that.resetting()
							}
						})
						// #endif


						uni.hideLoading()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//取消重连
			closeCl() {
				// 关闭房间
				let that = this
				uni.showLoading({
					title: '解散房间中...'
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
									uni.hideLoading()
									//关闭scoket连接
									uni.closeSocket()
									//调用一下服务端解散房间的接口
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
			//重连跳转
			surecl() {
				this.closeCl()
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
			//跳转设置
			suresq() {
				this.popupshowsq = false
				permision.gotoAppPermissionSetting()
			},
			//获取麦克风权限
			async getAudioPermision() {
				let status = permision.isIOS ? await permision.judgeIosPermission("record") : await permision
					.requestAndroidPermission("android.permission.RECORD_AUDIO")
				if (status === null || status === 1 || status == true) { //已经同意授权
					console.log('获取到权限了')
				} else { //未授权的
					this.popupshowsq = true
				}
			},
			//退出直播房间
			outRoom(item, roomId) {
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
									// #ifdef APP
									that.$trtcCloud.exitRoom();
									uni.navigateTo({
										url: '/my/voice/voice?strRoomId=' + item.roomNo +
											'&role=TRTCRoleAudience' +
											'&roomId=' +
											item.roomId
									})
									// #endif
									// #ifdef H5
									trtc.exitRoom();
									trtc.destroy();
									uni.navigateTo({
										url: '/my/voice/voiceH5?strRoomId=' + item.roomNo +
											'&role=TRTCRoleAudience' +
											'&roomId=' +
											item.roomId
									})
									// #endif

									uni.removeStorageSync('startRoom')
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
			//判断当前要进入的房间自己是否是黑名单
			isBlackRoom(roomId) {
				return new Promise((resolve, reject) => {
					let data = {
						roomId: roomId
					}
					this.$Request.postJson('/app/room/insertRoomUser', data).then(res => {
						// console.log(res)
						if (res.code == 0) {
							if (res.data) {
								if (res.data.status == 3) {
									resolve(3);
								} else {
									resolve(1);
								}
							} else {
								resolve(1);
							}
						} else {
							resolve(2);
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				})
			},
			//进入房间
			async goRoom(item) {
				// #ifdef APP || H5
				if (uni.getStorageSync('token')) {
					//如果有没有退出的房间，先退出房间在进入
					if (this.isShowDrag) { //当前进入的有房间
						// 退出当前房间
						if (this.roomInfo.roomId) {
							//进入的是自己的房间，也直接进
							if (item.roomId == this.roomInfo.roomId) {
								// #ifdef APP
								uni.navigateTo({
									url: '/my/voice/voice?strRoomId=' + item.roomNo +
										'&role=TRTCRoleAudience' +
										'&roomId=' +
										item.roomId + '&isBack=true'
								})
								// #endif
								// #ifdef H5
								uni.navigateTo({
									url: '/my/voice/voiceH5?strRoomId=' + item.roomNo +
										'&role=TRTCRoleAudience' +
										'&roomId=' +
										item.roomId + '&isBack=true'
								})
								// #endif

							} else { //自己有直播的房间，则提示先解散再进入
								let returnStatus = await this.isBlackRoom(item.roomId)
								if (returnStatus === 1) {
									this.itemRoom = item
									this.popupshowcl = true
								} else if (returnStatus === 3) {
									uni.showToast({
										title: '已被主持封禁',
										icon: 'none'
									})
								}
							}
						} else { //别人的房间（自己没开房间，先退出再进入）
							let data = {
								roomId: item.roomId
							}
							this.$Request.getT('/app/room/selectRoomDetails', data).then(async res => {
								if (res.code == 0) {
									if (res.data && res.data.status == 1) {
										let returnStatus = await this.isBlackRoom(item.roomId)
										if (returnStatus === 1) {
											this.outRoom(item, this.startRoom.roomId)
										} else if (returnStatus === 3) {
											uni.showToast({
												title: '已被主持封禁',
												icon: 'none'
											})
										}
									} else {
										uni.showToast({
											title: '房间不存在，请重试',
											icon: 'none'
										})
										//刷新一下房间列表
										this.getRoomList()
									}
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							})

						}
					} else { //当前没有进入房间，直接进
						//进入房间之前先判断一下该房间的状态
						let data = {
							roomId: item.roomId
						}
						this.$Request.getT('/app/room/selectRoomDetails', data).then(async res => {
							if (res.code == 0) {
								if (res.data && res.data.status == 1) {
									let returnStatus = await this.isBlackRoom(item.roomId)
									if (returnStatus === 1) {
										// #ifdef APP
										uni.navigateTo({
											url: '/my/voice/voice?strRoomId=' + item.roomNo +
												'&role=TRTCRoleAudience' +
												'&roomId=' +
												item.roomId
										})
										// #endif
										// #ifdef H5
										uni.navigateTo({
											url: '/my/voice/voiceH5?strRoomId=' + item.roomNo +
												'&role=TRTCRoleAudience' +
												'&roomId=' +
												item.roomId
										})
										// #endif

									} else if (returnStatus === 3) {
										uni.showToast({
											title: '已被主持封禁',
											icon: 'none'
										})
									}
								} else {
									uni.showToast({
										title: '房间不存在，请重试',
										icon: 'none'
									})
									//刷新一下房间列表
									this.getRoomList()
								}
							} else {
								uni.showToast({
									title: res.msg,
									icon: 'none'
								})
							}
						})
					}

				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
				// #endif

			},
			getRoomList() {
				let data = {
					page: 1,
					limit: 5,
					status: 1
				}
				this.$Request.getT('/app/room/selectRoomList', data).then(res => {
					if (res.code == 0) {
						this.roomList = res.data.list
					}
				})
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
			getUserFabu() {
				this.$Request.get("/app/fastOrder/selectMyFastCount").then(res => {
					if (res.code == 0) {
						this.myFabu = res.data
						uni.setStorageSync("myFabu", res.data)
					}
				});
			},
			sureT() {
				this.popupshow = false
				if (this.popupType == 0) {
					uni.navigateTo({
						url: '/package/pages/matching/matching'
					})
				} else if (this.popupType == 1) {
					let that = this
					wx.requestSubscribeMessage({
						tmplIds: that.arr,
						success(re) {
							console.log(JSON.stringify(re), '++++++++++++++')
							var datas = JSON.stringify(re);
							if (datas.indexOf("accept") != -1) {
								console.log(re)
								// uni.setStorageSync('sendMsg', true)
							}
						},
						fail: (res) => {
							console.log(res)
						}
					})
					that.showModal = false
				}

			},
			//判断用户是否已发布过订单
			getFastOrder() {
				let token = this.$queue.getData('token');
				if (token) {
					this.$Request.get("/app/fastOrder/selectFastOrder").then(res => {
						if (res.code == 0) {
							if (res.data != null) {
								uni.navigateTo({
									url: '/package/pages/matching/orderDeatil'
								})
							} else {
								if (this.yuewanFabu == '是') {
									if (this.myFabu < this.fabuNumber) {
										var num = this.fabuNumber - this.myFabu;
										this.content = '每日免费次数还剩余：' + num + '次，是否继续发布',
											this.popupType = 0
										this.popupshow = true

										return
									}
									if (this.myFabu >= this.fabuNumber) {
										this.content = '今日免费发布次数已完，发布需要支付' + this.fabuPrice + '柚币,是否继续发布',
											this.popupType = 0
										this.popupshow = true

										return
									}
								} else {
									uni.navigateTo({
										url: '/package/pages/matching/matching'
									})
								}
							}
						}
					});

				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}

			},
			getPaihangbang(url) {
				let token = this.$queue.getData('token');
				if (token) {
					uni.navigateTo({
						url: url
					})
					// uni.navigateTo({
					// 	url: '/package/pages/paihangbang/index'
					// })
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.invitationCode = res.data.invitationCode
						if (!res.data.isNewUser) {
							this.getNewCouponList()
						}
						if (!res.data.babyModel && !uni.getStorageSync('teenModeNoticeAcknowledged')) {
							this.qingshow = true;
						} else if (res.data.babyModel == 1) {
							uni.reLaunch({
								url: '/package/pages/paihangbang/moshi'
							})
						}
					}
				});
			},
			//检测是否为陪玩官
			getRenZheng() {
				this.$Request.get("/app/userCertification/queryInsert").then(res => {
					if (res.code == 0) {
						// 0审核中 1通过 2拒绝 
						if (res.data == null) {
							this.IdStatus = 0 //未实名
						} else if (res.data.status == 0) {
							this.IdStatus = 1 //审核中
						} else if (res.data.status == 1) {
							this.IdStatus = 2 //已实名
						} else if (res.data.status == 2) {
							this.IdStatus = 3 //已拒绝
						}
					}
				});
			},
			selectCity(longitude, latitude) {
				this.$Request.get('/app/Login/selectCity?lat=' + latitude + '&lng=' + longitude).then(res => {
					if (res.code == 0) {
						this.city = res.data ? res.data : '未知'
						uni.setStorageSync('city', res.data)
						let data = {
							num: 1,
							size: 10
						}
						this.getData(data)
					}
				});
			},
			// 开启订阅消息
			openMsg() {
				console.log('订阅消息')
				var that = this
				uni.getSetting({
					withSubscriptions: true, //是否获取用户订阅消息的订阅状态，默认false不返回
					success(ret) {
						console.log(ret.subscriptionsSetting, '------------------')
						// if (ret.subscriptionsSetting.itemSettings && Object.keys(ret.subscriptionsSetting.itemSettings).length == 2) {
						if (ret.subscriptionsSetting.itemSettings) {
							uni.setStorageSync('sendMsg', true)
							uni.openSetting({ // 打开设置页 
								success(rea) {
									console.log(rea.authSetting)
								}
							});
						} else { // 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
							console.log(99999)
							uni.setStorageSync('sendMsg', false)
							that.content = '为了更好的体验,请绑定消息推送'
							that.popupType = 1
							that.popupshow = true

						}
					}
				})
			},
			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
						this.isVip = res.data
						uni.setStorageSync('isVIP', res.data)
					}
				});
			},
			/*下拉刷新的回调 */
			downCallback() {
				// #ifdef APP || H5
				//获取语聊房间
				this.getRoomList()
				// #endif
				// 这里加载你想下拉刷新的数据, 比如刷新轮播数据
				// loadSwiper();
				// 下拉刷新的回调,默认重置上拉加载列表为第一页 (自动执行 page.num=1, 再触发upCallback方法 )
				// this.$refs.uDropdown.close();
				this.mescroll.resetUpScroll()
			},
			/*上拉加载的回调: 其中page.num:当前页 从1开始, page.size:每页数据条数,默认10 */
			upCallback(page) {
				// console.log(page,'*************')
				this.getData(page)
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
			getData(page) {
				let curTab = this.tabData[this.tabIndex].id
				let num
				if (this.tabIndex == 0) {
					num = 0
				} else {
					num = 2
				}
				let data = {
					id: curTab,
					page: page.num,
					limit: page.size,
					isRecommend: num,
					condition: this.value1, //智能优选
					by: this.value3, //价格
					latitude: uni.getStorageSync('latitude') ? uni.getStorageSync('latitude') : '',
					longitude: uni.getStorageSync('longitude') ? uni.getStorageSync('longitude') : '',
					city: this.city
				}
				// console.log(data)
				if (this.token) {
					this.$Request.get("/app/orderTaking/queryTaking", data).then(res => {
						this.mescroll.endBySize(res.data.list.length, res.data.list)
						if (res.code == 0) {
							if (page.num == 1) {
								this.orderList = [];
							}
							res.data.list.forEach(d => {
								d.isPlay = false
								if (d.label) {
									d.label = d.label.split(',')
								}
								d.distance = this.setMorKm(d.distance);
								this.orderList.push(d);
							});
						}
						this.mescroll.endSuccess(res.data.list.length); // 隐藏加载状态栏
					}).catch(() => {
						//联网失败, 结束加载
						this.mescroll.endErr();
					});
				} else {
					this.$Request.get("/app/orderTaking/queryTakings", data).then(res => {
						this.mescroll.endBySize(res.data.list.length, res.data.list)
						if (res.code == 0) {

							if (page.num == 1) {
								this.orderList = [];
							}
							res.data.list.forEach(d => {
								d.isPlay = false
								if (d.label) {
									d.label = d.label.split(',')
								}
								d.distance = this.setMorKm(d.distance);
								this.orderList.push(d);
							});
						}
						this.mescroll.endSuccess(res.data.list.length); // 隐藏加载状态栏
					}).catch(() => {
						//联网失败, 结束加载
						this.mescroll.endErr();
					});
				}
				// this.getClassfly()
				// this.getBannerList()
				// this.getbannerList1()
				// this.getGrid()
				// this.getLowTaking()

			},
			// 切换菜单
			tabChange() {
				if (uni.getStorageSync('sendMsg')) {
					console.log('授权+1')
					wx.requestSubscribeMessage({
						tmplIds: this.arr,
						success(re) {
							console.log(JSON.stringify(re), 111111111111)
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
				this.defaultIndex = [0, 0, 0]
				// this.$refs.uDropdown.close();
				// this.orderList = []; // 置空列表,显示加载进度条
				this.mescroll.resetUpScroll()
			},
			// 获取游戏类型
			getClassfly() {
				return this.$Request.get("/app/appGame/queryGameName").then(res => {
					if (res.code == 0 && Array.isArray(res.data)) {
						this.tabData = [{
							createTime: "",
							gameName: '推荐',
							gameImg: "",
							id: 0,
							status: 0,
							updateTime: "",
						}]
						this.tabData = [...this.tabData, ...res.data]
						return res.data
					}
					return []
				});
			},
			//获取轮播图
			getBannerList() {
				this.$Request.get("/app/banner/selectBannerList", {
					classify: 1
				}).then(res => {
					if (res.code == 0) {
						this.swiperList = res.data
					}
				});
			},
			//获取排行榜  约玩
			getbannerList1() {
				this.$Request.get("/app/banner/selectBannerList", {
					classify: 7
				}).then(res => {
					if (res.code == 0) {
						this.bannerList = res.data
					}
				});
			},
			// 获取金刚区分类
			getGrid() {
				this.$Request.get("/app/banner/selectBannerList", {
					classify: 2
				}).then(res => {
					if (res.code == 0) {
						this.gridData = res.data
					}
				});
			},
			// 乐享低价
			getLowTaking() {
				this.$Request.get("/app/orderTaking/queryLowTaking").then(res => {
					if (res.code == 0) {
						this.lowTaking = res.data
					}
				});
			},
			// 筛选
			change(e) {
				this.value1 = e[0][0].value
				this.value3 = e[1][0].value
				this.page = 1;
				this.mescroll.resetUpScroll()
			},
			// 选择城市
			goSelectCity() {
				uni.navigateTo({
					url: '/pages/index/citys/citys'
				});
			},
			// 乐享低价
			goLowTaking() {
				uni.navigateTo({
					url: '/pages/index/game/lowTaking'
				});
			},
			// 首页金刚区的旧配置链接可能使用过期的 gameId，按当前游戏目录跳转。
			goGridItem(item) {
				if (!item || !item.url) return
				if (item.url.indexOf('/pages/index/game/gameList') === -1) {
					this.goNav(item.url)
					return
				}
				const aliases = { '穿越火箭': '穿越火线' }
				const name = aliases[item.name] || item.name || item.describes
				const openGame = games => {
					const game = games.find(entry => entry.gameName === name)
					if (!game) {
						uni.showToast({ title: '该分类暂无内容', icon: 'none' })
						return
					}
					this.goNav('/pages/index/game/gameList?gameId=' + game.id + '&name=' + game.gameName)
				}
				const games = this.tabData.filter(entry => entry.id !== 0)
				if (games.some(entry => entry.gameName === name)) {
					openGame(games)
					return
				}
				this.getClassfly().then(openGame).catch(() => {
					uni.showToast({ title: '分类加载失败，请重试', icon: 'none' })
				})
			},
			// 跳转游戏列表
			goNav(url) {
				if (uni.getStorageSync('sendMsg')) {
					console.log('授权+1')
					wx.requestSubscribeMessage({
						tmplIds: this.arr,
						success(re) {
							console.log(JSON.stringify(re), 111111111111)
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
			},
			// 跳转搜索
			goSearch() {
				if (uni.getStorageSync('sendMsg')) {
					console.log('授权+1')
					wx.requestSubscribeMessage({
						tmplIds: this.arr,
						success(re) {
							console.log(JSON.stringify(re), 111111111111)
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
				uni.navigateTo({
					url: '/pages/index/search/index'
				});
			},
			// 跳转订单
			goOrder(e) {
				console.log('授权', uni.getStorageSync('sendMsg'))
				if (uni.getStorageSync('sendMsg')) {
					console.log('授权+1')
					wx.requestSubscribeMessage({
						tmplIds: this.arr,
						success(re) {
							console.log(JSON.stringify(re), 111111111111)
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
				if (this.token) {
					uni.navigateTo({
						url: '/pages/index/game/order?id=' + e.id + '&fenX=0'
					});
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}

			},
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #050506;
	}

	.roomList {
		width: 100%;
		height: auto;
		margin-top: 76rpx;

		.roomLists {
			width: calc(100% - 30rpx);
			margin-left: 30rpx;
			white-space: nowrap;
		}

		.roomLists-item {
			display: inline-block;
			margin-right: 20rpx;

			image {
				width: 160rpx;
				height: 160rpx;
				border-radius: 16rpx;
			}

			.roomLists-item-txt {
				width: 160rpx;
				font-weight: 500;
				margin-top: 8rpx;
				font-weight: 500;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
				-o-text-overflow: ellipsis;

			}
		}
	}

	.head {
		// #28183e
		// background: linear-gradient(to left, #552775 20%,#552775 30%,#28183e 50%);
		// background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/20/c3e50814c6bb43cdeee6e046c93eae77.png');
		background: linear-gradient(180deg, #1C1C22 0%, #050506 100%);
		background-size: 100% 100%;
		position: fixed;
		// border-radius: 0 0 50rpx 50rpx;
		left: 0;
		right: 0;
		z-index: 999;
		/* #ifdef H5 */
		top: 0;
		padding-top: 30rpx;
		height: 170rpx;
		/* #endif */
		/* #ifdef MP-WEIXIN */
		top: 0;
		height: 250rpx;
		padding-top: 140rpx;
		// margin-top: ;
		/* #endif */
		/* #ifdef APP */
		top: 0;
		height: 190rpx;
		padding-top: 80rpx;
		/* #endif */
	}

	.searchx_box {

		// margin-top: 120rpx;
		// height: 200rpx;

		// background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/c14daf6709e386c8236908f1f61db8ef.png');
		padding: 20rpx 30rpx;
		display: flex;

	}

	.fubgs {}

	.fubg {
		background: #101014;
		border: 1rpx solid #2A2A32;
		border-radius: 24rpx;
		background-size: 100% 100%;
		margin: 25rpx 25rpx 0 25rpx;
		padding: 40rpx 30rpx 30rpx;
		position: relative;

		// margin-lr-sm padding-sm radius 
		.headtop {
			display: flex;
			justify-content: space-between;
			align-items: center;
			color: #FFFFFF;
			padding-bottom: 20rpx;
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			z-index: 99;

			.title_right {
				margin-right: 30rpx;
				margin-top: 10rpx;
			}
		}

		.headbot {
			overflow: hidden;
			display: flex;
			justify-content: space-between;
			padding-top: 70rpx;
		}
	}


	.bgbox {
		background-color: #101014;
		display: flex;
		justify-content: space-between;
		margin-top: 60rpx;
		padding: 0 20rpx 30rpx 20rpx;
		border-radius: 24upx;
		position: relative;
	}


	// .sticky-tabs {
	// 	z-index: 990;
	// 	position: sticky;
	// 	top: var(--window-top);
	// 	// background-color: #fff;
	// }


	/* // 使用mescroll-uni,则top为0 */
	// .mescroll-uni,
	// ::v-deep .mescroll-uni {
	// 	.sticky-tabs {
	// 		top: 0;
	// 	}
	// }

	// .demo-tip {
	// 	padding: 18upx;
	// 	font-size: 24upx;
	// 	text-align: center;
	// }

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

	.yuyinImg .box {
		width: 100%;
		text-align: center;
		justify-content: space-between;
		padding: 0 15rpx;
	}

	.order-card-info,
	.order-card-footer {
		min-width: 0;
	}

	.order-card-meta {
		min-width: 0;
		align-items: center;
		justify-content: space-between;
		gap: 8rpx;
	}

	.order-card-score {
		min-width: 0;
		overflow: hidden;
		white-space: nowrap;
	}

	.order-card-footer {
		gap: 8rpx;
	}

	.order-card-price {
		flex: none;
		white-space: nowrap;
	}

	.gambox {
		flex: 1 1 0;
		width: auto;
		min-width: 0;
		height: 43rpx;
		overflow: hidden;
		white-space: nowrap;
		line-height: 39upx;
		background: linear-gradient(-90deg, #221A06 0%, #4A3808 100%);
		border-radius: 24rpx 0 0 24rpx;
		display: flex;
		align-items: center;

		.gamebox {
			display: flex;
			align-items: center;
			flex: 0 1 auto;
			min-width: 0;
			max-width: calc(100% - 50rpx);
			overflow: hidden;
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
				left: 0;
			}

			.level {
				margin-left: 47rpx;
				font-size: 24rpx;
				min-width: 0;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;

			}
		}

		.mytit {
			flex: 1 1 50rpx;
			min-width: 0;
			padding-right: 10rpx;
			max-width: 180rpx;
			margin-left: 4rpx;
			font-size: 24rpx;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
		}
	}

	.teen-mode-dialog {
		position: relative;
		z-index: 1;
		box-sizing: border-box;
		width: 551rpx;
		padding: 50rpx 40rpx;
		border-radius: 20rpx;
		background-color: #1C1C22;
		background-image: linear-gradient(23deg, #1C1C22 0%, #313035 89%);
		text-align: center;
		overflow: hidden;
	}

	.teen-mode-image {
		width: 192rpx;
		height: 174rpx;
	}

	.teen-mode-title {
		margin-top: 16rpx;
		color: #FFFFFF;
		font-size: 30rpx;
		font-weight: 700;
	}

	.teen-mode-content {
		width: 467rpx;
		margin: 24rpx auto 40rpx;
		color: #A1A2B2;
		font-size: 26rpx;
		line-height: 40rpx;
		text-align: left;
	}
	.btnn {
		width: 220rpx;
		height: 70rpx;
		background: #D9A800;
		border-radius: 35rpx;
		text-align: center;
		line-height: 70rpx;

	}

	.btnns {
		width: 220rpx;
		height: 70rpx;
		background: #FBCA0C;
		border-radius: 35rpx;
		text-align: center;
		line-height: 70rpx;
	}
</style>

