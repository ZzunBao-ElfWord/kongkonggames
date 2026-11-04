<template>
	<view class="release" v-if="shangxianSelect != '否'">
		<!-- <view class="release_name">
			分享你的奇思妙想吧
		</view> -->
		<textarea placeholder="分享你的奇思妙想吧" class="release_name" v-model="content"></textarea>

		<view class="margin-lr-sm">
			<view class="text-df margin-top">上传视频</view>
			<view class="flex align-center justify-center margin-top-sm radius"
				style="background: linear-gradient(90deg, rgba(243, 134, 189, 0.1) 0%, rgba(164, 112, 248, 0.1) 100%); width: 100%;height: 320rpx;position: relative;">
				<view style="text-align: center;margin: 0rpx auto 0;" @click="addImages()" v-if="!videoUrl">
					<image src="../static/photo.png" mode="widthFix" style="width: 73rpx;"></image>
					<view class="text-sm text-white margin-top-sm">上传视频(视频在30MB以内)</view>
				</view>
				<!-- @longpress="delData(videoUrl)" -->
				<view v-else>
					<view style="z-index: 9;position: absolute;top: -15rpx;right: -15rpx;" @click="delData()">
						<u-icon name="close-circle-fill" color="#F3A6FF" size="50rpx"></u-icon>
					</view>
					<video :src="videoUrl" style="width: 100%;height: 320rpx;"></video>
				</view>
				<!-- <video v-else :src="videoUrl" style="width: 100%;height: 320rpx;"></video> -->
			</view>
		</view>

		<view class="release_image">
			<view class="flex" style="overflow: hidden;flex-wrap: wrap;">
				<view v-if="imageList.length">
					<view class="margin-top flex margin-right-sm flex-wrap">
						<view class="flex" v-for="(item,index) in imageList" :key="index"
							style="width: 200rpx;height: 200rpx;margin-right: 5rpx;position: relative;margin-bottom: 10rpx;">
							<image :src="item" style="width: 100%;height: 100%;"></image>
							<view style="z-index: 9;position: absolute;top: -15rpx;right: -15rpx;"
								@click="removeImg(index)">
								<u-icon name="close-circle-fill" color="#F3A6FF" size="50rpx"></u-icon>
							</view>
						</view>
					</view>
				</view>
				<view class="margin-top" @click="addImage(1)" v-if="imageList.length<=8" style="">
					<view style="width: 200rpx;height: 200rpx;" class="flex justify-center align-center">
						<image class="del-image" src="https://h5.dingjianpp.com/paizhao.png">
						</image>
					</view>
				</view>
			</view>

		</view>

		<view class="release_btn" @tap="save" v-if="!talkId">我要发布</view>
		<view class="release_btn" @tap="bianji" v-else>我要发布</view>
	</view>
</template>

<script>
	import configdata from '@/common/config.js';
	export default {
		data() {
			return {
				shangxianSelect: '否',
				talkId: '',
				content: '',
				imageList: [],
				videoUrl: '',
				imageLists: [],
				count: 0,
				createTime: '',
				videoUrl: '',
			}
		},
		onLoad(d) {
			console.error(d)
			// #ifdef MP-WEIXIN
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			// #endif
			// #ifndef MP-WEIXIN
			this.shangxianSelect = '是';
			// #endif
			if (this.shangxianSelect === '否') {
				uni.setNavigationBarTitle({
					title: '设置'
				})
			} else {
				uni.setNavigationBarTitle({
					title: '发布动态'
				})
			}
			this.talkId = d.talkId;
			if (d.talkId) {
				this.getCont()
			}
		},
		methods: {
			removeImg(index) {
				this.imageList.splice(index, 1)
			},
			getCont() {
				let data = {
					talkId: this.talkId
				}
				this.$Request.getT('/app/gambit/selectById', data).then(res => {
					if (res.status == 0) {
						this.content = res.data.content
						this.videoUrl = res.data.videoUrl
						// console.error(res.data.img)
						if (res.data.img) {
							this.imageList = res.data.img.split(',')
							console.error(this.imageList)
						}
						this.createTime = res.data.createTime
					} else {
						uni.hideLoading();
						this.$queue.showToast(res.msg);
					}
				});
			},
			bianji() {
				this.imageLists = this.imageList
				this.imageLists = this.imageLists.toString()
				this.$queue.showLoading('提交中...');
				if (this.$queue.getChatSearchKeys(this.content)) {
					uni.showToast({
						title: "输入内容带有非法关键字请重新输入",
						mask: false,
						duration: 1500,
						icon: "none"
					});
					this.$Request.postT('/app/risk/insertRisk?riskType=2&content=' + this.content).then(res => {

					})
					return;
				}
				if (!this.videoUrl && this.imageList.length == 0) {
					this.$queue.showToast('请添加图片或者视频!');
					return;
				}
				if (this.videoUrl && this.imageList.length != 0) {
					this.$queue.showToast('图片或者视频只能发布一个!');
					return;
				}
				let userId = this.$queue.getData('userId');
				let data = {
					userId: userId,
					content: this.content,
					img: this.imageLists,
					videoUrl: this.videoUrl,
					talkId: this.talkId,
					createTime: this.createTime
				}

				// let userId = this.$queue.getData('userId');
				// let data = {
				// 	userId: userId,
				// 	content: this.content,
				// 	img: this.imageLists,
				// 	videoUrl: this.videoUrl,
				// 	talkId: this.talkId,
				// 	createTime: this.createTime
				// }
				this.$Request.postJson('/app/gambit/updateTalk', data).then(res => {
					if (res.status == 0) {
						uni.hideLoading();
						this.$queue.showToast('修改成功');
						setTimeout(d => {
							uni.navigateBack();
						}, 1000)
					} else {
						uni.hideLoading();
						this.$queue.showToast(res.msg);
					}
				});
			},
			save() {
				console.log(this.content)
				let that = this;
				// let images = '';
				this.imageLists = this.imageList
				this.imageLists = this.imageLists.toString()
				this.$queue.showLoading('提交中...');

				if (this.content === '') {
					uni.hideLoading();
					this.$queue.showToast('请输入内容信息!');
					return;
				}
				if (this.$queue.getChatSearchKeys(this.content)) {
					uni.showToast({
						title: "输入内容带有非法关键字请重新输入",
						mask: false,
						duration: 1500,
						icon: "none"
					});
					this.$Request.postT('/app/risk/insertRisk?riskType=2&content=' + this.content).then(res => {

					})
					return;
				}
				// if (this.imageList.length == 0) {
				// 	uni.hideLoading();
				// 	this.$queue.showToast('请添加图片!');
				// 	return;
				// } else {
				// 	for (var i = 0; i < this.imageList.length; i++) {
				// 		if (i === 0) {
				// 			images = this.imageList[i];
				// 		} else {
				// 			images = images + ',' + this.imageList[i];
				// 		}
				// 	}
				// }
				if (!this.videoUrl && this.imageList.length == 0) {
					this.$queue.showToast('请添加图片或者视频!');
					return;
				}
				if (this.videoUrl && this.imageList.length != 0) {
					this.$queue.showToast('图片或者视频只能发布一个!');
					return;
				}
				let userId = this.$queue.getData('userId');
				let data = {
					userId: userId,
					content: this.content,
					img: this.imageLists,
					videoUrl: this.videoUrl
				}
				// let userId = this.$queue.getData('userId');
				// let data = {
				// 	userId: userId,
				// 	content: this.content,
				// 	img: images
				// }
				this.$Request.postJson('/app/gambit/saveTalk', data).then(res => {
					if (res.status == 0) {
						uni.hideLoading();
						this.$queue.showToast('发布成功');
						// setTimeout(d => {
						// 	uni.navigateBack();
						// }, 1000)
						uni.redirectTo({
							url: '/package/pages/detail/dongtai'
						})
					} else {
						uni.hideLoading();
						this.$queue.showToast(res.msg);
					}
				});
			},
			// 删除
			delData() {
				this.videoUrl = ''
			},
			// 视频上传
			addImages(e) {
				let that = this
				uni.chooseVideo({
					count: 1,
					sourceType: ['album', 'camera'],
					success: res => {
						console.log(res)
						// for (let i = 0; i < res.tempFilePath.length; i++) {
						that.$queue.showLoading("上传中...");
						uni.uploadFile({ // 上传接口
							url: that.config("APIHOST1") + '/alioss/upload', //真实的接口地址
							filePath: res.tempFilePath,
							name: 'file',
							success: (uploadFileRes) => {
								console.log(JSON.parse(uploadFileRes.data).data)
								that.videoUrl = JSON.parse(uploadFileRes.data).data
								// that.form.videoUrl = JSON.parse(uploadFileRes.data).data
								// console.log(that.videoUrl)
								uni.hideLoading();
							}
						});
						// }
					}
				})
			},
			// 图片上传
			addImage(e) {
				if (e == 1) {
					var num = this.imageList.length
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
								url: that.config("APIHOST1") + '/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									console.log(JSON.parse(uploadFileRes.data).data)
									that.imageList.push(JSON.parse(uploadFileRes.data).data)
									console.log(that.imageList)
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
			}
		}
	}
</script>

<style>
	page {
		background: #111224
	}

	.release {
		width: 100%;
		position: relative;
	}

	.release_name {
		width: 95%;
		color: #999999;
		background: linear-gradient(90deg, rgba(243, 134, 189, 0.1) 0%, rgba(164, 112, 248, 0.1) 100%);
		padding: 20upx;
		margin: 0 auto;
		height: 300rpx;
		margin: 20rpx;
		letter-spacing: 2rpx;
		border-radius: 15upx;
		font-size: 28rpx;
	}

	.release_image {
		margin: 20rpx;
		width: 90%;
		/* margin: 0 auto; */
	}

	.release_image image {
		width: 100px;
		height: 100px;
	}

	.release_btn {
		width: 90%;
		height: 50px;
		margin: 0 auto;
		background: #ac75fe;
		color: white;
		text-align: center;
		line-height: 50px;
		margin-top: 40px;
	}
</style>
