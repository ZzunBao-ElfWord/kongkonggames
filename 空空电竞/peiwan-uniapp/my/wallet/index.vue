<template>
	<view>
		<view class="flex text-center text-white text-lg" v-if="Huizhang || IdStatus == 2 || zhiRate != 0 ">
			<!-- <view class="title_btn flex-sub" @click="cut(1)" :style="{backgroundColor:title_color==1?bgCol2: ''}">我的余额
			</view>
			<view class="title_btn flex-sub" @click="cut(2)" :style="{backgroundColor:title_color==2?bgCol2: ''}">我的收益
			</view> -->
			<view class="title_btn flex-sub" v-for="(item,index) in tablist" :key="index" @click="cut(item.id)"
				:style="{backgroundColor:title_color==item.id?bgCol2: ''}">{{item.name}}
			</view>
		</view>
		<view v-if="title_color==1" class="padding">
			<view class=" radius bg">
				<view class=" u-flex u-p-l-30 u-p-t-30 u-p-b-30">
					<view class="u-m-r-20">
						<u-avatar :src="avatar" size="80"></u-avatar>
					</view>
					<view class="u-flex-1 ">
						<view class="u-font-16 text-white text-bold flex align-center justify-between">

							<view class="">
								{{money}}币
							</view>
							<view @click="goNav('/my/wallet/mymoneydetail')" class="flex align-center"
								style="margin-right: 20rpx;font-weight: normal;font-size: 24rpx;">
								钱包明细
								<u-icon name="arrow-right" color="#ffffff" size="28"></u-icon>
							</view>
						</view>
						<view class="u-font-14 u-m-t-10 u-tips-color" style="color: #A1A2B2;">钻石用于订单支付，会员开通</view>
					</view>
				</view>
			</view>
			<view class="margin-top radius bg flex justify-between flex-wrap padding-lr padding-bottom">
				<view v-for="(item,index) in wallet" :key='index'
					class="flex justify-between padding-sm radius margin-top"
					style="background-color: #362f5b; width: 46%;border: 1px solid #3B3D55;"
					@click="Tabactive(index,item.payClassifyId)" :class="tabIndex == index?'active':''">
					<view class="flex">
						<image src="../static/jinbi.png" style="width: 38rpx;height: 38rpx;"></image>
						<text class="margin-left-sm">{{item.money+item.giveMoney}}</text>
					</view>
					<view>
						¥{{item.price}}
					</view>
				</view>
			</view>

			<!-- 	<view class="bg  radius margin-top">
				<view style="padding:30upx;">
					<view class="margin-bottom">自定义充值<text class="text-sm margin-left-xs"
							style="color: #1676FE;">*其他充值金额必须大于100的整数</text></view>
					<view class="flex align-center padding-sm radius" style="background: #3B3D55;">
						<view style="width: 88%;" class="flex align-center">
							<view>￥</view>
							<input type="number" placeholder="请输入充值的金额" v-model="payPrice"
								@input="zuanNumber(payPrice)" />
						</view>
						<view class=" flex align-center">
							<image src="../static/jinbi.png" style="width: 38rpx;height: 38rpx;"></image>
							<view class="margin-left-xs">{{total}}</view>
						</view>

					</view>
				</view>
			</view> -->

			<view v-if="isOpenKfPay == '否'" class="bg margin-top padding-lr radius">
				<view style="padding: 0 20upx;">
					<view class="" style="padding-top: 20rpx;font-size: 24rpx;color: #AC75FE;">
						*充值后不支持退款，柚币仅用于购买下单消费
					</view>
					<view style="display: flex;height: 100upx;align-items: center;padding: 20upx 0;"
						v-for="(item,index) in openLists" :key='index'>
						<image :src="item.image" style="width: 55upx;height: 55upx;border-radius: 50upx;"></image>
						<view style="font-size: 30upx;color: #FFFFFF;margin-left: 20upx;width: 70%;">{{item.text}}
							<text v-if="item.id==3">
								（{{teamMoney}}元）
							</text>
						</view>
						<radio-group name="openWay" style="margin-left: 20upx;" @tap='selectWay(item)'>
							<label class="tui-radio">
								<radio color="#AC75FE" :checked="openWay === item.id ? true : false" />
							</label>
						</radio-group>
					</view>
				</view>
			</view>
			<view class="xieyi flex align-center justify-center">
				<view class="xieyi-box flex align-center">
					<u-checkbox active-color="#AC75FE" shape="circle" v-model="checked"></u-checkbox>
					<view @click="checked = !checked" class="xieyi-box-txt flex align-center">
						我已阅读并同意
						<text @click="goUserPayXiYi()">《用户充值协议》</text>
					</view>

				</view>
			</view>
			<view class="btn" v-if="isOpenKfPay == '否'" @click="pay()">确认充值</view>
			<view class="btn" v-if="isOpenKfPay == '是'" @click="goChat()">联系客服充值</view>

		</view>
		<view v-if="title_color==2" class="padding">
			<view class="bg radius text-white padding">
				<view class="text-lg">可提现余额</view>
				<view class="flex margin-top-sm">
					<!-- <view class="text-xxl"></view> -->
					<view class="text-xxl">{{price}}元</view>
				</view>
			</view>
			<view class="bg radius text-white padding margin-top">
				<view class="text-lg margin-bottom">提现余额</view>
				<input type="number" placeholder="请输入提现余额" v-model="moneyNum">
			</view>
			<view style="display: flex;flex-direction: row;flex-wrap: wrap;margin-top: 20upx;"
				v-if="zhifubaoTiXian && weixinTiXian">
				<view style="display: flex;flex-direction: row;">
					<view class="zhifuSlect_btn" @tap="zhifuSelectTap(1)" v-if="zhifubaoTiXian"
						:style="zhifuSelect == 1 ? 'border: 1px solid #AC75FE;' : 'border: 1px solid #c8c8c8;'">
						<image src="../static/zhifubao.png"></image>
						<view style="margin-left:10upx">支付宝</view>
					</view>
					<view class="zhifuSlect_btn" @tap="zhifuSelectTap(2)" v-if="weixinTiXian"
						:style="zhifuSelect == 2 ? 'border: 1px solid #AC75FE;' : 'border: 1px solid #c8c8c8;'">
						<image src="../static/icon_weixin.png"></image>
						<view style="margin-left:10upx">微信</view>
					</view>
				</view>
			</view>
			<view class="text-center text-grey margin-top">{{placeholder}}</view>
			<view class="btn" @click="cashMoney">立即提现</view>



			<u-popup v-model="show" mode="center" :closeable="true" close-icon-pos="top-right" close-icon="close-circle"
				close-icon-size="50" border-radius="20" width="80%" @close="closeWxImag">
				<view class="padding">
					<view class="text-center text-white text-lg text-bold text-black">添加微信</view>
					<view class="text-center padding-top-sm padding-bottom-lg" style="color: red;">请提交微信收款二维码
					</view>
					<view style="width: 80%;margin: 0 auto;">
						<view class="margin-top-sm " @click="addImages"
							style="border: 4rpx solid #010101;border-radius: 16rpx;overflow: hidden;">
							<image v-if="!wxImg" src="../../static/erweima.png"></image>
							<image v-else :src="wxImg"></image>
						</view>
					</view>
					<view class="text-center margin-top-sm " @click="SaveWxImg"
						style="border-radius: 10rpx;background-color: #AC75FE;color: #FFFFFF;height: 80rpx;line-height: 80rpx;">
						保存</view>
				</view>
			</u-popup>
		</view>


		<view class="flex justify-around margin-top text-white padding-bottom-xl">
			<!-- IdStatus==2 && -->
			<view @click="goNav('/my/wallet/zhifubao')" v-if="title_color==2">提现账号</view>
			<!-- &&IdStatus==2 -->
			<view v-if="title_color==2 && weixinTiXian" @click="goWxImg()">微信收款码</view>
			<!-- <view @click="goNav('/my/wallet/mymoneydetail')" v-if="title_color == 1">钱包明细</view> -->
			<view @click="goNav('/my/wallet/cashList')" v-if="title_color == 2">提现记录</view>
			<view @click="goNav('/my/wallet/getMoneyList')" v-if="title_color == 2">收益明细</view>
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"
			:title='title'></popup>
		<popup v-if="popupSM" :show="popupSM" @closeT="popupSM = false" @sureT="sureSM" :content="contentSM"></popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import configdata from '@/common/config.js';
	export default {
		components: {
			popup
		},
		data() {
			return {
				enableWechatAppPay: false, // 安卓当前构建全局关闭微信支付；恢复后需重新打包并提交审核
				checked: false, //是否同意用户充值协议
				popupSM: false,
				contentSM: '请补充实名认证信息后再来操作吧！',
				checkSM: true,
				popupshow: false,
				content: '',
				title: '',


				tablist: [{
					name: '我的充值',
					id: 1
				}, {
					name: '我的收益',
					id: 2
				}],
				wxImg: '',
				mywxImg: '',
				WxTXFangShi: 1,
				IdStatus: 0,
				maxTiXian: 0,
				show: false,
				weixinTiXian: false,
				zhifubaoTiXian: false,
				zhifuSelect: 1,
				title_color: 1,
				YaoqingShangJin: false,
				bgCol1: '#1E1F31',
				bgCol2: '#352f53',
				money: 0,
				price: 0,
				avatar: '',
				Profit: 0,
				openLists: [{
					image: '../static/zhifubao.png',
					text: '支付宝',
					id: 1
				}],
				openWay: 1,
				wallet: [],
				moneyNum: null,
				thisSelect: {
					id: 1,
					num: '5',
					price: '5',
					isSelect: true
				},
				charge: 0, //提现手续费
				maxMoney: 0, //最高提现额度
				minMoney: 0, //最低提现额度
				ratio: '', //柚币比例
				placeholder: '',
				payClassifyId: '',
				tabIndex: 0,
				bili: '',
				total: 0,
				payPrice: '',
				Huizhang: false,
				zhiRate: 0,
				teamMoney: 0,
				isOpenKfPay: '否', //是否开启联系客服充值
				SelKeFu: '2',
			}
		},
		onLoad(option) {
			this.SelKeFu = this.$queue.getData('SelKeFu');
			this.isOpenKfPay = uni.getStorageSync('isOpenKfPay')
			this.title_color = option.index
			this.avatar = uni.getStorageSync('avatar')
			this.Huizhang = uni.getStorageSync("Huizhang")
			this.zhiRate = uni.getStorageSync('zhiRate')

			// #ifdef APP-PLUS
			this.openLists = [{
				image: '../../static/images/my/zhifubao.png',
				text: '支付宝',
				id: 1
			}, {
				image: '../../static/images/my/myMoneyIcon.png',
				text: '收益余额',
				id: 3
			}];
			if (this.enableWechatAppPay) {
				this.openLists.splice(1, 0, {
					image: '../../static/images/my/icon_weixin.png',
					text: '微信',
					id: 2
				});
			}
			this.openWay = 1;
			// #endif

			// #ifdef MP-WEIXIN
			this.openLists = [{
				image: '../../static/share/icon_weixin.png',
				text: '微信',
				id: 2
			}, {
				image: '../../static/images/my/myMoneyIcon.png',
				text: '收益余额',
				id: 3
			}];
			this.openWay = 2;
			// #endif

			// #ifdef H5
			let target = navigator.userAgent.toLowerCase();
			let isWeixin = (/micromessenger/.test(target)) ? true : false;
			if (isWeixin) {
				this.openLists = [{
					image: '../../static/share/icon_weixin.png',
					text: '微信',
					id: 2
				}, {
					image: '../../static/images/my/myMoneyIcon.png',
					text: '收益余额',
					id: 3
				}];
				this.openWay = 2;
			} else {
				this.openLists = [{
					image: '../../static/images/my/zhifubao.png',
					text: '支付宝',
					id: 1
				}, {
					image: '../../static/share/icon_weixin.png',
					text: '微信',
					id: 2
				}, {
					image: '../../static/images/my/myMoneyIcon.png',
					text: '收益余额',
					id: 3
				}];
				this.openWay = 1;
			}
			// 安卓项目的浏览器预览与原生包保持一致，统一隐藏微信支付。
			if (!this.enableWechatAppPay) {
				this.openLists = this.openLists.filter(item => item.id !== 2);
				if (this.openWay === 2) {
					this.openWay = this.openLists.length ? this.openLists[0].id : 1;
				}
			}
			// #endif

			this.$Request.getT('/app/common/type/244').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.WxTXFangShi = res.data.value;
					}
				}
			});

			//充值比例1元等于多少柚币
			this.$Request.getT('/app/common/type/142').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.bili = res.data.value;
					}
				}
			});

			//是否开启支付宝提现
			this.$Request.getT('/app/common/type/245').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						if (res.data.value === '是') {
							this.zhifubaoTiXian = true;
							this.zhifuSelect = 1;
						} else {
							this.zhifubaoTiXian = false;
							if (!this.weixinTiXian) {
								// this.zhifuSelect = -1;
							}
						}
					}
				}
			});

			// #ifndef APP-PLUS
			//是否开启微信提现
			this.$Request.getT('/app/common/type/243').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.weixinTiXian = true;
					} else {
						this.weixinTiXian = false;
						this.zhifuSelect = 1;
					}
				}
			});
			// #endif
			this.getUserInfo()
		},
		onShow() {
			let YaoqingShangJin = this.$queue.getData('YaoqingShangJin');
			if (YaoqingShangJin && YaoqingShangJin != 0) {
				this.YaoqingShangJin = true;
			} else {
				this.YaoqingShangJin = false;
			}
			this.getMoney()
			this.getCharge()
			this.getMinMoney()
			this.getMaxMoney()
			this.getRatio()
			this.getRenZheng()
			this.geiPrice()
		},
		methods: {
			goChat() {
				if (this.SelKeFu === '1') { //企业客服
					// #ifdef MP-WEIXIN
					let that = this
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
					let url = this.$queue.getData('SelKeFuLink');
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
				} else if (this.SelKeFu === '2') { //系统客服
					let token = this.$queue.getData('token');
					if (token) {
						uni.navigateTo({
							url: '/my/setting/chat'
						});
					} else {
						uni.navigateTo({
							url: '/pages/public/login'
						})
					}
				} else { //客服二维码
					uni.navigateTo({
						url: '/my/setting/kefu'
					})
				}
			},
			getTeamMoney() {
				this.$Request.get("/app/userMoney/selectMyMoney").then(res => {
					if (res.code == 0 && res.data) {
						// console.log(res.data.money)

						if (res.data.earningsMoney && res.data.earningsMoney > 0) {
							this.teamMoney = res.data.earningsMoney
						} else {
							this.teamMoney = 0
						}
					}
				});
			},
			//去用户充值协议
			goUserPayXiYi() {
				uni.navigateTo({
					url: '/my/setting/userPayXiYi'
				})
			},
			// 获取充值分类  价格
			geiPrice() {
				this.$Request.get("/app/payClassify/selectPayClassifyList").then(res => {
					if (res.code == 0) {
						uni.hideLoading();
						this.wallet = res.data
						this.payClassifyId = res.data[0].payClassifyId
					}
				});
			},
			// 自定义价格 计算钻石数量
			zuanNumber(payPrice) {

				this.total = (payPrice * this.bili).toFixed(2)
			},
			//检查是否已经认证陪玩官
			getRenZheng() {
				this.$Request.get("/app/userCertification/queryInsert").then(res => {
					if (res.code == 0) {
						if (res.data) {
							if (res.data.name && res.data.idNumber) {
								this.checkSM = true;
							} else {
								this.checkSM = false;
							}
							if (res.data.status != 1) {
								this.checkSM = false;
							}
						} else {
							this.checkSM = false;
						}
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
			closeWxImag() {
				this.show = false;
			},
			goWxImg() {
				this.show = true;
			},
			SaveWxImg() {
				let that = this;
				this.$queue.showLoading('上传中...')
				if (this.wxImg == '') {
					uni.hideLoading();
					this.$queue.showToast('请上传微信二维码！')
					return;
				}

				let data = {
					wxImg: that.wxImg
				}
				that.$Request.postJson('/app/user/updateUser', data).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						that.show = false;
						that.$queue.showToast('绑定成功！')
						that.$Request.getT('/app/user/selectUserById').then(res => {
							if (res.code == 0) {
								that.mywxImg = res.data.wxImg ? res.data.wxImg : '';
							}
						});
					} else {
						that.$queue.showToast(res.msg)
					}
				});
			},
			// 图片上传
			addImages() {
				let that = this;
				uni.chooseImage({
					count: 1,
					sourceType: ['album', 'camera'],
					success: res => {
						for (let i = 0; i < 1; i++) {
							that.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								url: that.config("APIHOST") + '/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									that.wxImg = JSON.parse(uploadFileRes.data).data
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
			zhifuSelectTap(index) {
				this.zhifuSelect = index;
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(des => {
					if (des.code == 0) {
						this.mywxImg = des.data.wxImg ? des.data.wxImg : '';
						this.wxImg = des.data.wxImg ? des.data.wxImg : '';
						this.avatar = des.data.avatar ? des.data.avatar : '../../static/logo.png'
						uni.setStorageSync('avatar', des.data.avatar)

					}
				});
			},
			// 提现手续费
			getCharge() {
				this.$Request.get("/app/common/type/152").then(res => {
					if (res.code == 0) {
						this.charge = parseFloat(res.data.value)
						this.placeholder = '手续费:' + parseFloat(this.charge * 100).toFixed(
								0) + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
					}
				});
			},
			// 最低提现额度
			getMinMoney() {
				this.$Request.get("/app/common/type/112").then(res => {
					if (res.code == 0) {
						this.minMoney = parseFloat(res.data.value)
						this.placeholder = '手续费:' + parseFloat(this.charge * 100).toFixed(
								0) + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
					}
				});
			},
			// 最高提现额度
			getMaxMoney() {
				this.$Request.get("/app/common/type/153").then(res => {
					if (res.code == 0) {
						this.maxMoney = parseFloat(res.data.value)
						this.placeholder = '手续费:' + parseFloat(this.charge * 100).toFixed(
								0) + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
					}
				});
			},
			// 柚币比例
			getRatio() {
				this.$Request.get("/app/common/type/154").then(res => {
					if (res.code == 0) {
						this.ratio = res.data.value
						this.placeholder = '手续费:' + parseFloat(this.charge * 100).toFixed(
								0) + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
						this.wallet.forEach(re => {
							re.num = re.price * res.data.value
						})
					}
				});
			},
			cut(e) {
				this.title_color = e
			},
			goNav(url) {
				uni.navigateTo({
					url
				})
			},
			Tabactive(index, payClassifyId) {
				this.tabIndex = index
				this.payClassifyId = payClassifyId
				// this.pay(1)
			},
			// 我的余额
			getMoney() {
				this.$Request.get("/app/userMoney/selectMyMoney").then(res => {
					if (res.code == 0 && res.data) {
						this.getTeamMoney()
						this.money = res.data.money
						if (res.data.earningsMoney && res.data.earningsMoney > 0) {
							this.price = res.data.earningsMoney
						}
					}
				});
			},
			selectWay: function(item) {
				// #ifdef APP-PLUS
				if (item.id === 2 && !this.enableWechatAppPay) return;
				// #endif
				this.openWay = item.id;
			},

			pay() {
				// #ifdef APP-PLUS
				if (this.openWay === 2 && !this.enableWechatAppPay) {
					this.openWay = 1;
					uni.showToast({
						title: '当前版本暂不支持微信支付',
						icon: 'none'
					});
					return;
				}
				// #endif
				if (!this.checked) {
					uni.showToast({
						title: '请阅读并同意用户充值协议',
						icon: 'none'
					})
					return
				}
				// let data
				// if (e == 1) {
				let data = {
					payClassifyId: this.payClassifyId
				}

				// } else if (e == 2) {
				// 	if (parseInt(this.payPrice) < 100) {
				// 		uni.showToast({
				// 			title: '请输入大于等于100的整数',
				// 			icon: 'none'
				// 		})
				// 		return
				// 	}
				// 	if (!/^\d+$/.test(this.payPrice)) {
				// 		uni.showToast({
				// 			icon: 'none',
				// 			title: '请输入正确金额,不能包含中文,英文,特殊字符和小数'
				// 		});
				// 		return;
				// 	}
				// 	data = {
				// 		money: this.payPrice
				// 	}
				// }
				if (this.openWay == 3) {
					this.$Request.post('/app/userMoney/inviteMoneyConvertMoney', data).then(res => {
						if (res.code == 0) {
							uni.hideLoading()
							uni.showToast({
								title: '支付成功',
								icon: 'success'
							})
							this.getMoney()
						} else {
							uni.hideLoading()
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				} else {
					// 获取订单编号
					this.$Request.post("/app/appOrder/insertOrder", data).then(res => {
						console.log(res, '********')
						uni.showLoading({
							title: '支付中...'
						});
						if (res.code == 0) {
							// this.Profit = res.data
							if (this.openWay == 2) { //微信支付
								// #ifdef APP-PLUS
								// 微信APP支付 根据订单id获取支付信息
								this.$Request.post("/app/wxPay/payAppOrder", {
									id: res.data.id
								}).then(ret => {
									this.isCheckPay(ret.code, 'wxpay', JSON.stringify(ret.data));
								});
								// #endif

								// #ifdef MP-WEIXIN
								// 微信小程序支付
								this.$Request.post('/app/wxPay/wxPayJsApiOrder', {
									orderId: res.data.id
								}).then(ret => {
									uni.hideLoading()
									uni.requestPayment({
										provider: 'wxpay',
										timeStamp: ret.data.timestamp,
										nonceStr: ret.data.noncestr,
										package: ret.data.package,
										signType: ret.data.signType,
										paySign: ret.data.sign,
										success: function(suc) {
											console.log('success:' + JSON.stringify(suc));
											uni.showToast({
												title: '支付成功',
												icon: 'success'
											})
											setTimeout(d => {
												uni.navigateBack(1)
											}, 1000);
										},
										fail: function(err) {
											console.log('fail:' + JSON.stringify(err));
											uni.showToast({
												title: '支付失败',
												icon: 'none'
											})
										}
									});
								});
								// #endif

								// #ifdef H5
								let ua = navigator.userAgent.toLowerCase();
								if (ua.indexOf('micromessenger') !== -1) {
									this.$Request.post('/app/wxPay/wxPayMpOrder?orderId=' + res.data.id).then(
										res => {
											if (res.code == 0) {
												this.callPay(res.data);
											} else {
												uni.showToast({
													icon: 'none',
													title: '支付失败!'
												});
											}
										});
								} else {
									this.$Request.post('/app/wxPay/wxPayH5Order?orderId=' + res.data.id).then(
										ret => {
											if (ret.code == 0) {
												const urlArr = window.location.href;
												const hostUrl = urlArr.split("/");
												const callBack = hostUrl[0] + "//" + hostUrl[2] + "/";
												const url = '&redirect_url=' + callBack +
													'my/wallet/index?index=1';
												window.location = ret.data.mweb_url + url
											} else {
												uni.showToast({
													icon: 'none',
													title: '支付失败!'
												});
											}
										});
								}
								// #endif

							} else if (this.openWay == 1) { //支付宝支付
								// #ifdef APP-PLUS
								// APP支付宝支付
								this.$Request.post("/app/aliPay/payApp", {
									id: res.data.id
								}).then(ret => {
									console.log(ret)
									this.isCheckPay(ret.code, 'alipay', ret.data);
								});
								// #endif

								// #ifdef H5
								this.$Request.post('/app/aliPay/payOrder?id=' + res.data.id).then(
									ret => {
										if (ret.code === 0) {
											const div = document.createElement('div')
											div.innerHTML = ret.data //此处form就是后台返回接收到的数据
											document.body.appendChild(div)
											document.forms[0].submit()
										} else {
											uni.showToast({
												icon: 'none',
												title: '支付失败!'
											});
										}
									});
								// #endif
							}
						}
					});
				}
			},
			callPay: function(response) {
				if (typeof WeixinJSBridge === "undefined") {
					if (document.addEventListener) {
						document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady(response), false);
					} else if (document.attachEvent) {
						document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady(response));
						document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady(response));
					}
				} else {
					this.onBridgeReady(response);
				}
			},
			onBridgeReady: function(response) {
				let that = this;
				if (!response.package) {
					return;
				}
				console.log(response, '++++++++')
				WeixinJSBridge.invoke(
					'getBrandWCPayRequest', {
						"appId": response.appid, //公众号名称，由商户传入
						"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
						"nonceStr": response.noncestr, //随机串
						"package": response.package,
						"signType": response.signType, //微信签名方式：
						"paySign": response.sign //微信签名
					},
					function(res) {
						console.log(res, '/*-/*-/*-')
						if (res.err_msg === "get_brand_wcpay_request:ok") {
							// 使用以上方式判断前端返回,微信团队郑重提示：
							//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
							uni.showLoading({
								title: '支付成功'
							});
							setTimeout(function() {
								uni.hideLoading();
							}, 1000);
						} else {
							uni.hideLoading();
						}
						WeixinJSBridge.log(response.err_msg);
					}
				);
			},
			isCheckPay(status, name, order) {
				if (status == 0) {
					this.setPayment(name, order);
				} else {
					uni.hideLoading();
					uni.showToast({
						title: '支付信息有误',
						icon: 'none'
					});
				}
			},
			setPayment(name, order) {
				console.log('*-*-*')
				uni.requestPayment({
					provider: name,
					orderInfo: order, //微信、支付宝订单数据
					success: function(res) {
						console.log(res)
						uni.hideLoading();
						uni.showLoading({
							title: '支付成功'
						});
					},
					fail: function(err) {
						console.log(err)
						uni.hideLoading();
					},
					complete() {
						uni.hideLoading();
					}
				});
			},
			sureSM() {
				this.popupSM = false;
				if (this.IdStatus == 1) {
					this.popupSM = false;
				} else if (this.IdStatus == 3) {
					this.popupSM = false;
					uni.navigateTo({
						url: '/my/renzheng/index?name=wallet'
					});
				} else {
					uni.navigateTo({
						url: '/my/renzheng/index?name=wallet'
					});
				}
			},
			// 提现
			cashMoney() {
				if (!this.checkSM) {
					if (this.IdStatus == 1) {
						this.contentSM = '当前认证审核中，请通过后才来操作吧！';
					} else if (this.IdStatus == 3) {
						this.contentSM = '当前认证被拒绝，请重新提交通过后才来操作吧！';
					}
					this.popupSM = true;
					return;
				}
				if (!this.moneyNum) {
					uni.showToast({
						icon: 'none',
						title: '请输入提现金额'
					})
					return
				}
				if (this.moneyNum > this.price * 1) {
					uni.showToast({
						icon: 'none',
						title: '您的余额不足'
					})
					return
				}
				if (this.moneyNum < this.minMoney || this.moneyNum > this.maxMoney * 1) {
					uni.showToast({
						icon: 'none',
						title: '请输入正确的金额'
					})
					return
				}
				// if (this.moneyNum*1+this.charge*this.moneyNum > this.money*1) {
				// 	uni.showToast({
				// 		icon: 'none',
				// 		title: '您的手续费不足'
				// 	})
				// 	return
				// }
				// let typeSelect = 1;
				// if (this.zhifuSelect == 2) {
				// 	// #ifdef H5
				// 	typeSelect = 3;
				// 	// #endif

				// 	// #ifdef MP-WEIXIN
				// 	typeSelect = 2;
				// 	// #endif
				// }
				this.sp = (this.moneyNum * this.charge).toFixed(2)
				this.content = '本次提现' + this.moneyNum + '元，服务费' + this.sp + '元，是否确认提现？'
				this.title = "提现申请提示"
				this.popupshow = true

			},
			sureT() {
				let that = this
				let typeSelect = 1;
				if (that.zhifuSelect == 2) {
					// #ifdef H5
					typeSelect = 3;
					// #endif

					// #ifdef MP-WEIXIN
					typeSelect = 2;
					// #endif
				}
				if (that.zhifuSelect == 1) {
					let zhiFuBao = uni.getStorageSync('zhiFuBao')
					let zhiFuBaoName = uni.getStorageSync('zhiFuBaoName')
					if (!zhiFuBao && !zhiFuBaoName) {
						uni.navigateTo({
							url: '/my/wallet/zhifubao'
						})
						that.popupshow = false
						return
					}
					that.sp = (that.moneyNum * that.charge).toFixed(2)
					that.$Request.get("/app/cash/cashMoney", {
						money: that.moneyNum,
						classify: typeSelect
					}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								icon: 'none',
								title: res.msg
							})
							that.moneyNum = null
							that.popupshow = false
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg
							})
						}
						that.getMoney()
					});
				} else if (that.zhifuSelect == 2) {
					if (that.WxTXFangShi == 2) {
						if (that.mywxImg) {

							that.sp = (that.moneyNum * that.charge).toFixed(2)
							that.$queue.showLoading("提现中...");
							that.$Request.get("/app/cash/cashMoney", {
								money: that.moneyNum,
								classify: typeSelect
							}).then(res => {
								uni.hideLoading();
								if (res.code == 0) {
									uni.showToast({
										icon: 'none',
										title: res.msg
									})
									that.moneyNum = null
									that.popupshow = false
								} else {
									uni.showToast({
										icon: 'none',
										title: res.msg
									})
								}
								that.getMoney()
							});
						} else {
							that.show = true;
							that.popupshow = false
						}
					} else {
						that.$queue.showLoading("提现中...");
						that.$Request.get("/app/cash/cashMoney", {
							money: that.moneyNum,
							classify: typeSelect
						}).then(res => {
							uni.hideLoading();
							if (res.code == 0) {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
								that.moneyNum = null
								that.popupshow = false
							} else {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
							}
							that.getMoney()
						});
					}
				}
			}
		}
	}
</script>

<style lang="scss">
	::v-deep .u-checkbox__label {
		display: none;
	}

	.xieyi {
		width: 100%;
		height: auto;
		margin-top: 40rpx;

		.xieyi-box {
			width: 686rpx;
			height: 100%;

			.xieyi-box-txt {
				margin-left: 10rpx;

				text {
					color: #AC75FE;
				}
			}

		}
	}

	.active {
		border: 1px solid #AC75FE !important;
		background: #AC75FE !important;
	}

	.zhifuSlect_btn {
		justify-content: center;
		border-radius: 15upx;
		padding: 20upx;
		margin-left: 20upx;
		display: flex;
		background-color: #1E1F31;
		width: 320upx;
		align-items: center;
		height: 80upx;

		image {
			width: 50upx;
			height: 50upx;
			border-radius: 50upx;
		}
	}

	.title_btn {
		height: 78upx;
		line-height: 78upx;
		// background-color: #1A1728;
		background-color: #1A1728;
	}

	.btn {
		width: 100%;
		height: 88upx;
		background: #AC75FE;
		border-radius: 44upx;
		text-align: center;
		line-height: 88upx;
		margin-top: 40upx;
		font-size: 28upx;
		color: #FFF;
	}
</style>
