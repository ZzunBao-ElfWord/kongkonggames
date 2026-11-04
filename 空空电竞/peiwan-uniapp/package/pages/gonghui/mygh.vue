<template>
	<view>
		<view class="view1 flex align-center justify-around" style="border-radius: 20upx;" @click="goTixian">
			<view class="text-center" >
				<view style="font-size: 40rpx;color: ##1789FD;">{{ sumMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">总收益</view>
			</view>
			<view class="text-center">
				<view style="font-size: 40rpx;color: ##1789FD;">{{ ordersMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">订单收益</view>
			</view>
			<view class="text-center">
				<view style="font-size: 40rpx;color: ##1789FD;">{{ giftMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">礼物收益</view>
			</view>
		</view>

		<view class="view2">
			<view class="padding" style="display: flex;flex-direction: row;">
				<view style="width: 25%;">邀请码</view>

				<view style="width: 45%;text-align: center;">昵称</view>
				<view style="width: 30%;text-align: right;">奖励</view>
			</view>
			<view class="flex justify-between align-center padding" v-for="(item, index) in list" :key="index">
				<view class="flex align-center" style="width: 25%;" @click.stop="copyClick(item.invitationCode)">
					<view style="font-size: 28upx;margin-left: 15upx;margin-top: 6upx;">{{item.invitationCode}}
					</view>
					<image src="../static/copy.png" style="width:24upx;height:24upx;margin-left:10upx;" ></image>
				</view>

				<view style="width: 45%;display: flex;flex-direction: row;align-items: center;">
					<view class="text-center" style="font-size: 28upx;width: 90%;overflow: hidden;">{{ item.userName }}
						<!-- <text v-if="item.isAuthentication==1" style="font-size: 22upx;color:#fff;background: #525363;margin-left:10upx;border-radius: 33upx;padding: 5upx 15upx;">已实名</text> -->
					</view>
				</view>
				<view style="width: 30%;text-align: center;display: flex;justify-content: center;align-items: center;"
					@click="bindMoneylist(item.consortiaId,item.userId)">
					<view style="font-size: 32upx;color: #FBEA74;">{{ item.money ? item.money : 0 }}</view>
					<image src="../../../static/images/index/jinbi.png" style="width: 38rpx;height: 38rpx;margin-left: 10rpx;"></image>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				total: 0,
				first: 0,
				second: 0,
				list: [],
				page: 1,
				limit: 20,
				count: 0,
				tabIndex: 1,
				checkReZhiShu: '否',
				checkReTuanZhang: '否',
				checkReFeiZhiShu: '否',
				scrollTop: false,


				sumMoney: 0,
				ordersMoney: 0,
				giftMoney: 0,
				Huizhang: false
			}
		},
		onLoad() {
			this.$queue.showLoading("加载中...");
			this.getTeamMoney()
			this.getList();
			// if (option.Huizhang) {
			// 	this.Huizhang = option.Huizhang
			// }
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		methods: {
			copyClick(copy) {
				uni.setClipboardData({
					data: copy,
					success: function(res) {
						uni.getClipboardData({
							success: function(res) {
								uni.showToast({
									title: "复制成功",
									icon: 'none',
								});
							},
						});
					},
				});
			},
			bindMoneylist(consortiaId, userId) {
				// console.log(consortiaId)

				uni.navigateTo({
					url: '/package/pages/gonghui/mysylist?consortiaId=' + consortiaId + '&userId=' + userId
				})
			},
			changeList(e) {
				this.page = 1
				this.type = e
				this.getList()
			},
			getTeamMoney() {
				let consortiaId = this.$queue.getData('consortiaId');
				let data = {
					consortiaId: consortiaId,
				}
				this.$Request.getT('/app/consortia/selectConsortiaMoney', data).then(res => {
					if (res.code == 0) {
						this.sumMoney = res.data.sumMoney
						this.ordersMoney = res.data.ordersMoney
						this.giftMoney = res.data.giftMoney

					}
				});
			},
			getList() {
				let consortiaId = this.$queue.getData('consortiaId');
				let userId = this.$queue.getData('userId');
				let data = {
					page: this.page,
					limit: this.limit,
					consortiaId: consortiaId,
				}
				this.$Request.getT('/app/consortia/selectConsortiaPersonMoneyList', data).then(res => {
					if (res.code == 0 && res.data) {
						if (this.page == 1) {
							this.list = res.data.records
						} else {
							this.list = [...this.list, ...res.data.records]
						}
						this.count = res.data.totalCount
					}
					uni.hideLoading();
					uni.stopPullDownRefresh();
				});
			},
			goTixian() {
				uni.navigateTo({
					url: '/my/wallet/index?index=2'
				})
			}
		},
		onReachBottom: function() {
			if (this.list.length == this.count) {
				uni.showToast({
					title: '已经到底了',
					icon: 'none'
				})
			} else {
				this.page = this.page + 1;
				this.getList();
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getList();
		}
	}
</script>

<style lang="less">
	page {
		background-color: #1A1728;
	}

	.view1 {
		background-color: #291B3D;
		width: 93%;
		height: 300upx;
		margin: 30rpx auto;
		// margin-left: 26upx;
		// border-radius: 20upx;
		// margin-top: 20upx;
		// box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
	}

	.view2 {
		background-color: #291B3D;
		width: 93%;
		margin-left: 26upx;
		border-radius: 20upx;
		margin-top: 20upx;
		margin-bottom: 20upx;
		// box-shadow: rgba(183, 183, 183, 0.3) 1px 1px 10px 1px;
		border-radius: 20upx;
		margin-bottom: 50upx;
	}

	.yaoqing_view {
		width: 95%;
		display: flex;
		position: fixed;
		bottom: 100rpx;
		justify-content: center;
	}

	.yaoqing_btn {
		width: 520rpx;
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
		background: #1789FD;
		color: #FFFFFF;
		border-radius: 50rpx;
	}

	.tui-tab-item-title {
		// color: #ffffff;
		font-size: 30rpx;
		height: 80rpx;
		line-height: 80rpx;
		flex-wrap: nowrap;
		white-space: nowrap;
	}

	.tui-tab-item-title-active {
		border-bottom: 1px solid #5E81F9;
		color: #5E81F9;
		font-size: 32upx;
		font-weight: bold;
		border-bottom-width: 6upx;
		text-align: center;
	}

	.item {
		background: #f5f5f5;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
	}
</style>
