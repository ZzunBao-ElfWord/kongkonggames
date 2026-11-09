<template>
	<view>
		<textarea v-model="content" class="text-white text-df flex-sub" placeholder="请输入信息内容" maxlength="100"
			placeholder-style="text-align: left;"
			style="height: 300upx;width: 95%;margin: 20rpx;text-align: left;background: #101014;padding: 20rpx;border-radius: 15rpx;"></textarea>

		<view class="padding-lr-sm" v-if="type == 0">
			<u-button :custom-style="customStyle1" :hair-line="false" @click="save()">
				添加
			</u-button>
		</view>
		<view class="padding-lr-sm" style="margin-top: 100rpx;display: flex;" v-if="type == 1">
			<u-button style="width: 48%;margin-right: 20rpx;" :custom-style="customStyle2" :hair-line="false"
				@click="deleteMsg()">
				删除
			</u-button>
			<u-button style="width: 48%;" :custom-style="customStyle1" :hair-line="false" @click="updateMsg()">
				保存
			</u-button>
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content1">
		</popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	export default {
		components: {
			popup
		},
		data() {
			return {
				popupshow: false,
				content1: '',
				popupType: 0,


				customStyle1: {
					backgroundColor: "#FBCA0C",
					border: 0,
					color: "#FFF",
					height: '98rpx'
				},
				customStyle2: {
					backgroundColor: "#ffffff",
					border: 0,
					color: "#000",
					height: '98rpx'
				},
				id: '',
				type1: 0, //0,常用语，1，自动回复
				type: 0, //0，添加  1，编辑
				content: ''
			}
		},
		onLoad(d) {
			this.type = d.type;
			if (d.type == 1) {
				uni.setNavigationBarTitle({
					title: '编辑信息'
				})
			}
			this.type1 = d.type1;
			if (this.type1 == 1) {
				this.getDetailMsg();
			}
			if (d.id) {
				this.id = d.id;
				this.getDetailMsg();
			}
		},
		methods: {
			sureT() {
				let userId = this.$queue.getData('userId');
				let that = this
				if (that.popupType == 0) {
					if (that.type1 == 0) {
						that.$Request.postT('/app/message/deleteMessage?id=' + that.id).then(res => {
							if (res.code == 0) {
								uni.hideLoading();
								that.$queue.showToast('删除成功！')
								that.content = '';
								that.popupshow = false
								setTimeout(d => {
									uni.navigateBack();
								}, 1000);
							} else {
								uni.hideLoading();
								that.$queue.showToast(res.msg)
							}
						});
					} else {
						let data = {
							msg: ''
						}
						that.$Request.postJson('/app/user/updateUser', data).then(res => {
							if (res.code == 0) {
								uni.hideLoading();
								that.$queue.showToast('删除成功！')
								that.content = '';
								that.popupshow = false
								setTimeout(d => {
									uni.navigateBack();
								}, 1000);
							} else {
								uni.hideLoading();
								that.$queue.showToast(res.msg)
							}
						});
					}
				} else if (that.popupType == 1) {
					let data = {
						state: 20,
						userId: userId,
						content: that.content,
						id: that.id
					}
					if (that.type1 == 0) {
						that.$Request.postJson('/app/message/updateMessage', data).then(res => {
							if (res.code == 0) {
								uni.hideLoading();
								that.$queue.showToast('修改成功！')
								that.content = '';
								that.popupshow = false
								setTimeout(d => {
									uni.navigateBack();
								}, 1000);
							} else {
								uni.hideLoading();
								that.$queue.showToast(res.msg)
							}
						});
					} else {
						let data = {
							msg: that.content
						}
						that.$Request.postJson('/app/user/updateUser', data).then(res => {
							if (res.code == 0) {
								uni.hideLoading();
								that.$queue.showToast('修改成功！')
								that.content = '';
								that.popupshow = false
								setTimeout(d => {
									uni.navigateBack();
								}, 1000);
							} else {
								uni.hideLoading();
								that.$queue.showToast(res.msg)
							}
						});
					}
				}

			},
			deleteMsg() {
				this.content1 = '您确认要删除当前信息吗？'
				this.popupType = 0
				this.popupshow = true

				// that.$Pmessage({
				// 	showModel: true,
				// 	title: '温馨提示',
				// 	content: '您确认要删除当前信息吗？',
				// 	maskcloseAble: false
				// }, {
				// 	emitBtn:()=> { //确认
				// 		console.log('用户点击确认');

				// 	},
				// 	emitClose:()=> { //取消
				// 		console.log('用户点击取消');
				// 	},
				// });

			},
			updateMsg() {
				this.content1 = '您确认要修改当前信息吗？'
				this.popupType = 1
				this.popupshow = true



			},
			getDetailMsg() {
				if (this.type1 == 1) {
					this.$Request.get("/app/user/selectUserById").then(res => {
						if (res.code == 0) {
							this.content = res.data.msg ? res.data.msg : '';
						}
					});
				} else {
					this.$Request.getT('/app/message/selectMessageById?id=' + this.id).then(res => {
						if (res.code == 0) {
							this.content = res.data.content;
						}
					});
				}
			},
			save() {
				let that = this;
				this.$queue.showLoading('上传中...')
				if (this.content == '') {
					uni.hideLoading();
					this.$queue.showToast('请输入信息内容！')
					return;
				}
				// 添加常用语  分类传个20吧  state ，再传userId   content内容应该就行
				let userId = this.$queue.getData('userId');
				let data = {
					state: 20,
					userId: userId,
					content: this.content
				}

				if (this.type1 == 0) {
					this.$Request.postJson('/app/message/insertMessage', data).then(res => {
						if (res.code == 0) {
							uni.hideLoading();
							this.$queue.showToast('添加成功！')
							this.content = '';
							setTimeout(d => {
								uni.navigateBack();
							}, 1000);
						} else {
							uni.hideLoading();
							this.$queue.showToast(res.msg)
						}
					});
				} else {
					let data = {
						msg: this.content
					}
					this.$Request.postJson('/app/user/updateUser', data).then(res => {
						if (res.code == 0) {
							uni.hideLoading();
							this.$queue.showToast('添加成功！')
							this.content = '';
							setTimeout(d => {
								uni.navigateBack();
							}, 1000);
						} else {
							uni.hideLoading();
							this.$queue.showToast(res.msg)
						}
					});
				}
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #050506;
	}

	.bg {
		background-color: #101014;
	}

	::v-deep .textMoney {
		text-align: center;
	}
</style>