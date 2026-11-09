<template>
	<view v-if="shangxianSelect != '否'" style="padding-bottom: 50rpx;">
		<view class="padding">
			<view class="margin-bottom text-red" v-if="form.remek">拒绝原因：{{form.remek}}</view>
			<view class="text-white padding bg radius" v-if="shimingSelect != '否'">
				<u-form :model="form" ref="uForm" label-position="top" :label-style='lableStyle'>
					<u-form-item label="真实姓名" :border-bottom='false'>
						<u-input :disabled="disabled" placeholder="请输入真实姓名" v-model="form.name" />
					</u-form-item>
					<u-form-item label="证件号码" :border-bottom='false'>
						<u-input :disabled="disabled" placeholder="请输入身份证号码" v-model="form.idNumber" />
					</u-form-item>
				</u-form>
			</view>
			<view class="text-white padding bg radius margin-top" v-if="shimingSelect != '否'">
				<view class="text-lg">上传身份证正面</view>
				<view class="margin-top"
					style="border: 2rpx dashed #4A3808; width: 100%;height: 320rpx;position: relative;">
					<view style="text-align: center;margin: 80rpx auto 0;" @click="addImages(1)" v-if="!form.front">
						<image src="../static/photo.png" mode="widthFix" style="width: 73rpx;"></image>
						<view class="text-sm text-gray margin-top-sm">添加身份证正面</view>
					</view>
					<image @click="disabled? '':addImages(1)" v-else :src="form.front"
						style="width: 100%;height: 320rpx;"></image>
				</view>
			</view>
			<view class="text-white padding bg radius margin-top" v-if="shimingSelect != '否'">
				<view class="text-lg">上传身份证反面</view>
				<view class="margin-top"
					style="border: 2rpx dashed #4A3808; width: 100%;height: 320rpx;position: relative;">
					<view style="text-align: center;margin: 80rpx auto 0;" @click="addImages(2)" v-if="!form.back">
						<image src="../static/photo.png" mode="widthFix" style="width: 73rpx;"></image>
						<view class="text-sm text-gray margin-top-sm">添加身份证反面</view>
					</view>
					<image @click="disabled? '':addImages(2)" v-else :src="form.back"
						style="width: 100%;height: 320rpx;"></image>
				</view>
			</view>

			<view class="margin-top">
				<view class="text-lg">技术水平证明截图<text class="text-df">（包含清晰ID和当前段位，最多九张）</text></view>
				<view class="flex" style="overflow: hidden;flex-wrap: wrap;">
					<view v-if="skillImg.length">
						<view class="margin-top flex margin-right-sm flex-wrap">
							<view class="flex" v-for="(item,index) in skillImg" :key="index"
								style="width: 200rpx;height: 200rpx;margin-right: 5rpx;position: relative;margin-bottom: 10rpx;">
								<image :src="item" style="width: 100%;height: 100%;"></image>
								<view style="z-index: 9;position: absolute;top: -15rpx;right: -15rpx;"
									@click="removeImg(index,1)">
									<u-icon name="close-circle-fill" color="#FBCA0C" size="50rpx"></u-icon>
								</view>
							</view>
						</view>
					</view>
					<view class="margin-top" @click="addImage(1)" v-if="skillImg.length<=8">
						<view style="width: 200rpx;height: 200rpx;background: #101014;"
							class="flex justify-center align-center">
							<view>
								<view class="text-center">
									<image src="../static/photo.png" style="width: 65rpx;height: 55rpx;">
									</image>
								</view>
								<view class="text-center ">添加图片</view>
							</view>
						</view>
					</view>
				</view>
			</view>

			<view class="text-lg margin-top">生活照<text class="text-df">（最多九张）</text></view>
			<view class="flex" style="overflow: hidden;flex-wrap: wrap;">
				<view v-if="lifeImg.length">
					<view class="margin-top flex margin-right-sm flex-wrap">
						<view class="flex" v-for="(item,index) in lifeImg" :key="index"
							style="width: 200rpx;height: 200rpx;margin-right: 5rpx;position: relative;margin-bottom: 10rpx;">
							<image :src="item" style="width: 100%;height: 100%;"></image>
							<view style="z-index: 9;position: absolute;top: -15rpx;right: -15rpx;"
								@click="removeImg(index,2)">
								<u-icon name="close-circle-fill" color="#FBCA0C" size="50rpx"></u-icon>
							</view>
						</view>
					</view>
				</view>
				<view class="margin-top" @click="addImage(2)" v-if="lifeImg.length<=8">
					<view style="width: 200rpx;height: 200rpx;background: #101014;"
						class="flex justify-center align-center">
						<view>
							<view class="text-center">
								<image src="../static/photo.png" style="width: 65rpx;height: 55rpx;">
								</image>
							</view>
							<view class="text-center ">添加图片</view>
						</view>
					</view>
				</view>
			</view>

			<view class="margin-top">
				<view class="text-lg">个人简介</view>
				<view class="padding-lr-sm padding-tb-xs margin-top-sm"
					style="background: #101014;border-radius: 10upx;">
					<u-input v-model="form.details" type="textarea" placeholder="请介绍您的技能水平、技术特长、个人风格等（至少50 个字）"
						height="180" maxlength="300" />
				</view>
			</view>
		</view>
		<view class="padding-lr-lg flex align-center" style="padding: 0rpx 30rpx;">
			<u-checkbox active-color="#FBCA0C" shape="circle" v-model="checked"></u-checkbox>
			<view @click="checked = !checked" class="text-sm flex align-center flex-wrap"
				style="color: #A1A2B2;width: 600rpx;margin-left: 10rpx;">
				同意并遵守
				<view style="color: #FBCA0C;" @click.stop="goNav('/my/setting/pingtaiXieYi?name=平台入驻服务协议&id=821')">
					《平台入驻服务协议》</view>
				<view style="color: #FBCA0C;" @click.stop="goNav('/my/setting/pingtaiXieYi?name=平台行为守则&id=822')">
					《平台行为守则》
				</view>
				<view style="color: #FBCA0C;" @click.stop="goNav('/my/setting/pingtaiXieYi?name=平台服务规范&id=823')">
					《平台服务规范》</view>

			</view>
		</view>
		<view class="margin-lr">
			<u-button v-if="!disabled" @click="submit" class="margin-top" :custom-style="customStyle" shape="circle"
				:hair-line="false">提交认证</u-button>
		</view>
	</view>
	</view>
</template>

<script>
	import configdata from '../../common/config.js';
	export default {
		data() {
			return {
				checked: false,
				shangxianSelect: '否',
				shimingSelect: '否',
				form: {
					name: '',
					idNumber: '',
					front: '',
					back: '',
					remek: '',
					details: '',
					lifePhoto: '', //生活照
					skillImg: '' //技能照
				},
				skillImg: [],
				lifeImg: [],
				disabled: false,
				lableStyle: {
					color: '#FFFFFF',
					fontSize: '32upx'
				},
				customStyle: {
					backgroundColor: '#FBCA0C',
					color: '#FFFFFF',
					border: 0
				},
				customStyle1: {
					backgroundColor: '#ccc',
					color: '#FFFFFF',
					border: 0
				},
				count: '',
				nameWallet: ''
			}
		},
		onLoad(d) {
			if (d.name) {
				if (d.name === 'wallet') {
					this.shimingSelect = '是';
					this.nameWallet = d.name;
				} else {
					this.shimingSelect = this.$queue.getData('shimingSelect');
				}
			} else {
				this.shimingSelect = this.$queue.getData('shimingSelect');
			}
			// #ifdef MP-WEIXIN
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			// #endif
			// #ifndef MP-WEIXIN
			this.shangxianSelect = '是';
			// #endif
			this.getUserInfo()
		},
		methods: {
			// 协议配置
			goNav(e) {
				uni.navigateTo({
					url: e
				})
			},
			// 上传主页图删除
			removeImg(index, ind) {
				if (ind == 1) {
					this.skillImg.splice(index, 1)
				} else if (ind == 2) {
					this.lifeImg.splice(index, 1)
				}
			},
			submit() {
				// console.log(this.form)
				this.form.skillImg = this.skillImg
				this.form.skillImg = this.form.skillImg.toString()
				this.form.lifePhoto = this.lifeImg
				this.form.lifePhoto = this.form.lifePhoto.toString()
				if (this.shimingSelect === '是') {
					if (!this.form.name) {
						uni.showToast({
							title: '请输入真实姓名',
							icon: 'none',
							duration: 1000
						})
						return;
					}
					if (!this.form.idNumber) {
						uni.showToast({
							title: '请输入身份证号',
							icon: 'none',
							duration: 1000
						})
						return;
					}
					let regX =
						/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
					if (!regX.test(this.form.idNumber)) {
						uni.showToast({
							title: '请输入正确的身份证号',
							icon: 'none',
							duration: 1000
						})
						return;
					}
					if (!this.form.front) {
						uni.showToast({
							title: '请上传身份证正面',
							icon: 'none',
							duration: 1000
						})
						return;
					}
					if (!this.form.back) {
						uni.showToast({
							title: '请上传身份证反面',
							icon: 'none',
							duration: 1000
						})
						return;
					}
				}
				if (!this.form.skillImg) {
					uni.showToast({
						title: '请上传技术水平截图',
						icon: 'none',
						duration: 1000
					})
					return;
				}
				if (!this.form.lifePhoto) {
					uni.showToast({
						title: '请上传生活照',
						icon: 'none',
						duration: 1000
					})
					return;
				}
				if (!this.form.details) {
					uni.showToast({
						title: '请填写个人简介',
						icon: 'none',
						duration: 1000
					})
					return;
				}
				if (!this.checked) {
					uni.showToast({
						title: '请同意并阅读协议',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (this.nameWallet === '') {
					this.$Request.post("/app/userCertification/insert", this.form).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: '认证提交成功！',
								icon: 'none'
							})
							setTimeout(function() {
								uni.navigateBack()
							}, 1000)
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					});
				} else if (this.nameWallet === 'wallet') {
					this.$Request.postJson("/app/userCertification/updateUserCertification", this.form).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: '认证提交成功！',
								icon: 'none'
							})
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/my/index'
								})
							}, 1000)
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					});
				}
			},
			getUserInfo() {
				this.$Request.get("/app/userCertification/queryInsert").then(res => {
					if (res.code == 0) {
						if (res.data) {
							this.form.name = res.data.name ? res.data.name : ''
							this.form.idNumber = res.data.idNumber ? res.data.idNumber : ''
							this.form.front = res.data.front ? res.data.front : ''
							this.form.back = res.data.back ? res.data.back : ''

							if (this.form.name && this.form.idNumber) {
								this.shimingSelect = '是';
							}

							if (res.data.lifePhoto) {
								this.lifeImg = res.data.lifePhoto.split(',')
							}
							if (res.data.skillImg) {
								this.skillImg = res.data.skillImg.split(',')
							}
							this.form.details = res.data.details

							// this.shimingSelect = '是';
							// this.nameWallet = d.name;

							if (res.data.status == 1) {
								if (this.nameWallet === 'wallet') {
									this.disabled = false
								} else {
									this.disabled = true
								}
							}
							if (res.data.status == 2) {
								this.form.remek = res.data.remek ? res.data.remek : ''
								this.shimingSelect = '是';
								this.nameWallet = 'wallet';
							}
						} else {
							this.nameWallet = '';
						}
					}
				});
			},
			// 图片上传
			addImage(e) {
				if (e == 1) {
					var num = this.skillImg.length
					this.count = 9 - num
				} else if (e == 2) {
					var num = this.lifeImg.length
					this.count = 9 - num
				}
				let that = this
				uni.chooseImage({
					count: this.count,
					sourceType: ['album', 'camera'],
					success: res => {
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							that.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								// url: 'https://pw.xianmxkj.com/pwyl-api/alioss/upload',
								url: this.config("APIHOST1") + '/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									console.log(JSON.parse(uploadFileRes.data).data)
									if (e == 1) {
										that.skillImg.push(JSON.parse(uploadFileRes.data).data)
									} else if (e == 2) {
										that.lifeImg.push(JSON.parse(uploadFileRes.data).data)
									}
									uni.hideLoading();
								}
							});
						}
					}
				})
			},
			addImages(e) {
				uni.chooseImage({
					count: 1,
					sourceType: ['album', 'camera'],
					success: res => {
						for (let i = 0; i < 1; i++) {
							this.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								// url: 'https://pw.xianmxkj.com/pwyl-api/alioss/upload',
								url: this.config("APIHOST1") + '/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									if (e == 1) {
										this.form.front = JSON.parse(uploadFileRes.data).data
									} else {
										this.form.back = JSON.parse(uploadFileRes.data).data
									}

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
		}
	}
</script>

<style>
	::v-deep .u-checkbox__label {
		display: none;
	}

	.bg {
		background-color: #101014;
	}

	.u-input__textarea {
		color: #ffffff !important;
	}
</style>