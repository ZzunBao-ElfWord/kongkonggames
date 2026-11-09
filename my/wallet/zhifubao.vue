<template>
	<view class="container" v-if="shangxianSelect != '否'">
		<list-cell title="收款人姓名" type="text" placeholder="请输入支付宝收款人姓名" v-model="zhiFuBaoName"></list-cell>

		<list-cell title="支付宝账号" type="text" placeholder="请输入要绑定的支付宝账号" v-model="zhiFuBao"></list-cell>

        <wButton text="绑定账户" :rotate="logining" @click.native="toLogin()"></wButton>
	    <view style="padding: 32upx 64upx;font-size: 24upx;color: #999999;">提示：请正确填写收款人的支付宝账户和真实的收款人姓名，否则将无法正常收款</view>
	</view>
</template>

<script>
	import listCell from '@/components/com-input';
	import wButton from '../components/watch-login/watch-button.vue'; //button
	export default {
		components: {
			listCell,
			wButton
		},
		data() {
			return {
				zhiFuBao: '',
				zhiFuBaoName: '',
				logining: false,
				shangxianSelect:'否'
			}
		},
		onLoad() {
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			let userId = this.$queue.getData("userId");
			if (userId) {
				// this.$Request.getT("/app/cash/userinfo/" + userId).then(res => {
				// 	if (res.status === 0) {
				// 		if (res.data.zhiFuBao) {
				// 			this.zhiFuBao = res.data.zhiFuBao;
				// 		}
				// 		if (res.data.zhiFuBaoName) {
				// 			this.zhiFuBaoName = res.data.zhiFuBaoName;
				// 		}
				// 	}
				// });
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						if (res.data.zhiFuBao) {
							this.zhiFuBao = res.data.zhiFuBao;
						}
						if (res.data.zhiFuBaoName) {
							this.zhiFuBaoName = res.data.zhiFuBaoName;
						}
					}
				});
			}

		},
		methods: {
			inputChange(e) {
				const key = e.currentTarget.dataset.key;
				this[key] = e.detail.value;
			},
			navBack() {
				uni.navigateBack();
			},

			toLogin() {
				let userId = this.$queue.getData("userId");
				let token = uni.getStorageSync("token");
				const {
					zhiFuBao,
					zhiFuBaoName
				} = this;
				if (!zhiFuBao) {
					this.$queue.showToast("请设置收款人支付宝账号");
				} else if (!zhiFuBaoName) {
					this.$queue.showToast("请设置收款人姓名");
				} else {
					this.$queue.showLoading("修改中...");
					let data = {
						zhiFuBao: this.zhiFuBao,
						zhiFuBaoName: this.zhiFuBaoName
					}
					this.$Request.postJson('/app/user/updateUser',data).then(res => {
							if (res.code === 0) {
								uni.setStorageSync('zhiFuBao',zhiFuBao)
								uni.setStorageSync('zhiFuBaoName',zhiFuBaoName)
								uni.showToast({
									title:'修改成功',
									icon:'none',
									complete() {
										setTimeout(function() {
											uni.navigateBack();
										},1000)
									}
								})
							} else {
								this.$queue.showToast(res.msg)
							}
							uni.hideLoading();
						});
				}
			},
		},

	}
</script>

<style lang='scss'>
	page {
		background: #101014;
	}

	.container {
		padding-top: 32upx;
		position: relative;
		width: 100%;
		height: 100%;
		overflow: hidden;
		background: #101014;
	}

	.confirm-btn {
		width: 300px;
		height: 42px;
		line-height: 42px;
		border-radius: 30px;
		margin-top: 70upx;
		background: #e10a07;
		color: #fff;
		font-size: 32rpx;
	
		&:after {
			border-radius: 60px;
		}
	}

</style>
