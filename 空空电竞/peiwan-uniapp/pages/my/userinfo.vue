<template>
	<view>
		<view class="usermain">
			<view class="usermain-item flex justify-between">
				<view>头像</view>
				<!-- #ifdef MP-WEIXIN -->
				<view>
					<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
						<image style="width: 111rpx;height: 111rpx;border-radius: 50%;" :src="avatar"></image>
					</button>
				</view>
				<!-- 	<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar" style="background: #111224;">
					<image :src="avatar?avatar:'../../static/logo.png'" mode=""
						style="width: 111rpx;height: 111rpx;border-radius: 50%;">
					</image>
				</button> -->
				<!-- #endif -->
				<!-- #ifndef MP-WEIXIN -->
				<view @click="uploadImg()">
					<image :src="avatar?avatar:'../../static/logo.png'" mode=""
						style="width: 111rpx;height: 111rpx;border-radius: 50%;">
					</image>
				</view>
				<!-- #endif -->
			</view>
			<view class="usermain-item item-padding ">
				<view>用户名</view>
				<view>
					<view class="cu-form-group">
						<input type="nickname" maxlength="8" v-model="userName" placeholder="请输入用户名" />
					</view>
				</view>
			</view>
			<view class="usermain-item item-padding ">
				<view>年龄</view>
				<view>
					<view class="cu-form-group">
						<input v-model="age" />
					</view>
				</view>
			</view>
			<!-- <view class="usermain-item item-padding">
				<view  >姓名</view>
				<view class="cu-form-group">
					<input    v-model="realName" placeholder="请填写您的真实姓名" />
				</view>
			</view> -->

			<view class="usermain-item item-padding ">
				<view>手机</view>
				<view>
					<view class="cu-form-group">
						<input disabled v-model="phone" placeholder="请输入联系电话" />
					</view>
				</view>
			</view>
			<view class="usermain-item item-padding ">
				<view>性别</view>
				<view>
					<view class="cu-form-group">
						<u-radio-group v-model="sex">
							<u-radio shape="circle" :name="1" active-color="#AC75FE">男</u-radio>
							<u-radio shape="circle" :name="2" active-color="#AC75FE">女</u-radio>
						</u-radio-group>
					</view>
				</view>
			</view>
		</view>
		<view class="footer-btn">
			<view class="usermain-btn" @click="messagebtn()">保存</view>
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
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
				content: '',


				phone: '',
				avatar: '../../static/logo.png',
				userName: '',
				nickName: '',
				userId: '',
				realName: '',
				weChatId: "",
				password: '',
				platform: '',
				createTime: '',
				money: '',
				jiFen: '',
				status: '',
				zhiFuBao: '',
				zhiFuBaoName: '',
				sex: 1,
				age: 0
			};
		},
		onLoad(e) {
			this.getUserInfo()
			// this.avatar = uni.getStorageSync('avatar')
		},
		methods: {
			//微信填写能力获取头像
			onChooseAvatar(e) {
				// console.log(e,'eeeeeee')
				// return
				let that = this;
				let token = uni.getStorageSync('token');
				uni.showLoading({
					title: '上传中...'
				});
				let userId = uni.getStorageSync('userId');
				uni.uploadFile({
					url: that.config("APIHOST1") + '/alioss/upload', //真实的接口地址
					filePath: e.detail.avatarUrl,
					header: {
						token: token
					},
					name: 'file',
					success: uploadFileRes => {
						let url = JSON.parse(uploadFileRes.data);
						that.avatar = url.data
						uni.hideLoading();
					}
				});
			},
			goMyAddress() {
				uni.navigateTo({
					url: '../jifen/myaddress'
				});
			},
			uploadImg() {
				// let token = uni.getStorageSync('token')
				// if (!token) {
				// 	this.goLoginInfo();
				// 	return;
				// }
				let that = this;
				var url = null;
				uni.showActionSheet({
					// itemList按钮的文字接受的是数组
					itemList: ["查看头像", "从相册选择图片"],
					success(e) {
						var index = e.tapIndex
						if (index === 0) {
							// 用户点击了预览当前图片
							// 可以自己实现当前头像链接的读取
							let url = that.avatar;
							let arr = []
							arr.push(url)
							uni.previewImage({
								// 预览功能图片也必须是数组的
								urls: arr
							})
						} else if (index === 1) {
							uni.chooseImage({
								count: 1, //默认9
								sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
								sourceType: ['album'], //从相册选择
								success: function(res) {
									uni.showLoading({
										title: '上传中...'
									});
									let token = uni.getStorageSync('token');
									let userId = uni.getStorageSync('userId');
									uni.uploadFile({
										url: that.config("APIHOST1") +
											'/alioss/upload', //真实的接口地址
										filePath: res.tempFilePaths[0],
										header: {
											token: token
										},
										name: 'file',
										success: uploadFileRes => {
											url = JSON.parse(uploadFileRes.data);

											that.avatar = url.data
											uni.hideLoading();
										}
									});
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
			getUserInfo() {
				let userId = uni.getStorageSync('userId')
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.$queue.setData('avatar', res.data.avatar);
						this.$queue.setData('userId', res.data.userId);
						this.$queue.setData('userName', res.data.userName);
						this.$queue.setData('phone', res.data.phone);
						this.$queue.setData('age', res.data.age);
						this.sex = res.data.sex
						this.age = res.data.age
						this.phone = res.data.phone;
						this.avatar = res.data.avatar;
						this.userName = res.data.userName;
						if (this.userName == null) {
							this.userName = res.data.nickName;
						} else {
							this.userName = res.data.userName;
						}
					}
					uni.hideLoading();
				});


			},
			// 保存
			messagebtn() {
				console.log(this.userName, 'that.userName')
				if (!this.userName) {
					// this.$queue.showToast('用户名不能为空');
					uni.showToast({
						title: "用户名不能为空",
						icon: "none"
					})
				} else if (!this.phone) {
					// this.$queue.showToast('用户名不能为空');
					uni.showToast({
						title: "联系电话不能为空",
						icon: "none"
					})
				} else {
					this.content = '确定保存信息？'
					this.popupshow = true

					// that.$Pmessage({
					// 	showModel: true,
					// 	title: '温馨提示',
					// 	content: '确定保存信息',
					// 	maskcloseAble: false
					// }, {
					// 	emitBtn:()=> { //确认

					// 	},
					// 	emitClose:()=> { //取消
					// 		console.log('用户点击取消');
					// 	},
					// });

				}
			},
			sureT() {
				let that = this
				let data = {
					userName: that.userName,
					avatar: that.avatar,
					phone: that.phone,
					sex: that.sex,
					age: that.age
				}
				// alert(data.userName)
				// return
				that.$Request.postJson("/app/user/updateUser", data).then(res => {
					if (res.code === 0) {
						that.popupshow = true
						uni.showToast({
							title: '保存成功',
							icon: "none"
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					} else {
						uni.showToast({
							title: res.msg,
							icon: "none"
						})
					}
				})
			}
		},
		// userphone(){
		// 	uni.navigateTo({
		// 		url:'/pages/my/userphone'
		// 	})
		// }

	};
</script>

<style>
	page {
		/* background: #1c1b20; */
	}


	button::after {
		border: none;
		background-color: none;
	}

	button {
		position: relative;
		display: block;
		margin-left: auto;
		margin-right: auto;
		padding-left: 0px;
		padding-right: 0px;
		box-sizing: border-box;
		text-decoration: none;
		line-height: 1.35;
		overflow: hidden;
		color: #666666;
		/* background-color: #fff; */
		background-color: rgba(255, 255, 255, 0) !important;
		width: 100%;
		height: 100%;
	}


	.usermain {
		background: #111224;
		color: #fff;
	}

	.usermain-item {
		display: flex;
		align-items: center;
		margin: 0 40rpx;
		padding: 10rpx 0;
		justify-content: space-between;
		border-bottom: 1rpx solid #1E1F2E;
		/* border-bottom: 2rpx solid #f2f2f2; */
	}

	.usermain-item.item-padding {
		/* padding: 0; */
	}

	.cu-form-group {
		padding: 0;
		background: #111224;
		text-align: right;
	}

	.cu-form-group input {
		background: #111224;
		font-size: 28rpx;
		color: #fff;

	}

	.footer-btn {
		margin-top: 150rpx;
	}

	.footer-btn .usermain-btn {
		color: #FFFFFF;
		background: #AC75FE;
		text-align: center;
		width: 450rpx;
		height: 80rpx;
		font-size: 28rpx;
		line-height: 80rpx;
		margin: 0 auto;
		border-radius: 40rpx;
	}

	::v-deep .textMoney {
		text-align: center;
	}
</style>