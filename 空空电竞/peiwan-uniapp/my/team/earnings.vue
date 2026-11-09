<template>
	<view>
		<view class="view1" style="border-radius: 20upx;display: flex;flex-direction: row;" @click="goTixian">
			<view style="margin: 120upx 0 0 60upx;height: 100upx; width: 150upx;text-align: center;">
				<view style="font-size: 40rpx;color: #AC75FE;">{{ teamMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">总收益</view>
			</view>
			<view style="margin: 120upx 0 0 60upx;height: 100upx; width: 150upx;text-align: center;">
				<view style="font-size: 40rpx;color: #AC75FE;">{{ oneTeamMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">一级收益</view>
			</view>
			<view style="margin: 120upx 0 0 60upx;height: 100upx; width: 150upx;text-align: center;">
				<view style="font-size: 40rpx;color: #AC75FE;">{{ twoTeamMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">二级收益</view>
			</view>
		</view>
		<view class="navbar">
			<view v-for="(item, index) in tabList" :key="index" class="nav-item" :class="{current: type == item.type}"
				@click="changeList(item.type)">
				{{item.text}}
			</view>
		</view>
		<view class="view2"
			style="border-radius: 20upx;margin-bottom: 50upx;">
			<view style="display: flex;flex-direction: row;padding: 20upx;">
				<view style="width: 15%;">编号</view>
				<view style="width: 20%;">头像</view>
				<view style="width: 45%;">昵称</view>
				<view style="width: 30%;text-align: center;">奖励</view>
			</view>
			<view class="flex justify-between align-center padding" v-for="(item, index) in list" :key="index">
				<view style="width: 15%;">
					<view style="font-size: 28upx;margin-left: 15upx;margin-top: 6upx;">{{ index + 1 }}
					</view>
				</view>
				<view style="width: 20%;">
					<image :src="item.avatar?item.avatar:'../../static/logo.png'" class="round"
						style="width: 50rpx;height: 50rpx;"></image>
				</view>
				<view style="width: 45%;display: flex;flex-direction: row;align-items: center;">
					<view style="font-size: 28upx;width: 90%;overflow: hidden;">{{ item.userName }}</view>
				</view>
				<view style="width: 30%;text-align: center;display: flex;justify-content: center;align-items: center;" @click.stop="goList(item.userId)">
					<view style="font-size: 32upx;color: #FBEA74;">{{ item.money ? item.money : 0 }}</view>
					<image src="../static/jinbi.png" style="width: 38rpx;height: 38rpx;margin-left: 10rpx;"></image>
				</view>
			</view>
			<empty v-if="list.length==0" content="暂无数据"></empty>
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
				tabIndex: 1,
				checkReZhiShu: '否',
				checkReTuanZhang: '否',
				checkReFeiZhiShu: '否',
				scrollTop: false,
				tabList: [{
					state: 'zs',
					text: '一级',
					type: 1,
					number: 0
				}, {
					state: 'fzs',
					text: '二级',
					type: 2,
					number: 0
				}],
				type: 1,

				teamMoney: 0,
				oneTeamMoney: 0,
				twoTeamMoney: 0
			}
		},
		onLoad() {
			
			this.$queue.showLoading("加载中...");
			this.getTeamMoney()
			this.getList();
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		methods: {
			goList(userId){
				uni.navigateTo({
					url:'/my/team/mysylist?byUserId='+userId
				})
			},
			changeList(e) {
				this.page = 1
				this.type = e

				this.getList()
			},
			getTeamMoney() {
				let data = {
					// type: this.type
				}
				this.$Request.getT('/app/orders/selectTeamStatistics', data).then(res => {
					if (res.code == 0) {

						this.teamMoney = res.data.teamMoney
						this.oneTeamMoney = res.data.oneTeamMoney
						this.twoTeamMoney = res.data.twoTeamMoney

					}
				});
			},
			getList() {
				uni.showLoading({
					title: '加载中...'
				});
				let data = {
					page: this.page,
					limit: this.limit,
					type: this.type,
				}
				this.$Request.getT('/app/orders/selectTeamUserList', data).then(res => {
					if (res.code == 0 && res.data) {
						if (this.page == 1) {
							this.list = res.data.list
						} else {
							this.list = [...this.list, ...res.data.list]
						}
					}
					uni.hideLoading();
				});
			},
			goTixian() {
				uni.navigateTo({
					url: '/my/wallet/index?index=2'
				})
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getList();
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
		margin-left: 26upx;
		border-radius: 20upx;
		margin-top: 20upx;
		box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
	}

	.view2 {
		background-color: #291B3D;
		width: 93%;
		margin-left: 26upx;
		border-radius: 20upx;
		margin-top: 20upx;
		margin-bottom: 20upx;
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
		background: #AC75FE;
		// color: #FFFFFF;
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
		border-bottom: 1px solid #AC75FE;
		color: #AC75FE;
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

	.navbar {
		display: flex;
		height: 40px;
		border-radius: 20upx;
		box-shadow: 0 1px 5px rgba(0, 0, 0, .06);
		position: relative;
		z-index: 10;
		margin: 20upx 24upx 0;
		border-radius: 16upx;
		overflow: hidden;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;
			// color: $font-color-dark;
			position: relative;

			&.current {
				// color: $base-color;
				background-color: #291B3D;

				&:after {
					content: '';
					position: absolute;
					left: 50%;
					bottom: 0;
					transform: translateX(-50%);
					width: 44px;
					height: 0;
					// border-bottom: 2px solid $base-color;
				}
			}
		}
	}
</style>
