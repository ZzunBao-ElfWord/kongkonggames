<template>
	<view class="content">

		<view class="view1" style="border-radius: 20upx;margin-bottom: 20upx;">
			<view style="display: flex;">
				<view style="margin: 70upx 0 0 20upx;height: 100upx; width: 300upx; text-align: center;">
					<view style="font-size: 40upx;">{{ teamCount?teamCount:0 }}</view>
					<view style="font-size: 28upx;margin-left: 20upx;">邀请好友（人）</view>
				</view>
				<view style="margin: 70upx 0 0 75upx;height: 100upx; width: 300upx;text-align: center;" @click="goDet">
					<view style="font-size: 40upx;">{{ teamMoney?teamMoney:0 }}</view>
					<view style="font-size: 28upx;">我的收益</view>
				</view>
			</view>
			<view style="margin: 20rpx 30rpx;font-size: 28rpx;color: #AC75FE;" v-if="YaoqingShangJin!= 0">
				*邀请好友可得{{YaoqingShangJin}}柚币</view>
			<view style="margin: 20rpx 30rpx;font-size: 28rpx;color: #AC75FE;" v-if="yijiyongjin">
				*可得一级好友消费奖励的{{yijiyongjin*100}}%</view>
			<view style="margin: 20rpx 30rpx;font-size: 28rpx;color: #AC75FE;" v-if="erjiyongjin">
				*可得二级好友消费奖励的{{erjiyongjin*100}}%</view>

			<button class="yaoqing_btn" @click="goYao">邀请好友</button>
		</view>
		<view class="navbar">
			<view class="nav-item" :class="{current: type == 1}" @click="changeList(1)">
				一级( {{oneTeamCount}} )
			</view>
			<view class="nav-item" :class="{current: type == 2}" @click="changeList(2)">
				二级( {{twoTeamCount}} )
			</view>
		</view>
		<view class="view2" style="">
			<view style="display: flex;flex-direction: row;padding: 20upx;">
				<view style="width: 15%;">编号</view>
				<view style="width: 20%;">头像</view>
				<view style="width: 40%;">昵称</view>
				<view style="width: 35%;text-align: center;">时间</view>
			</view>
			<view class="flex justify-between align-center padding" v-for="(item, index) in list" :key="index"
				style="margin-bottom: 16upx;">
				<view style="width: 15%;">
					<view style="font-size: 28upx;margin-left: 15upx;margin-top: 6upx;">{{ index + 1 }}
					</view>
				</view>
				<view style="width: 20%;">
					<image :src="item.avatar?item.avatar:'../../static/logo.png'" class="round"
						style="width: 50rpx;height: 50rpx;"></image>
				</view>
				<view style="width: 40%;display: flex;flex-direction: row;align-items: center;">
					<view style="font-size: 28upx;width: 90%;overflow: hidden;">{{ item.userName }}</view>
				</view>
				<view style="width: 35%;text-align: center;">
					<view style="font-size: 28upx;">
						{{ item.createTime }}
					</view>
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
				YaoqingShangJin: 0,
				setting: 1,
				list: [],
				page: 1,
				limit: 20,
				type: 1,
				status: 0,
				teamCount: 0,
				teamMoney: 0,
				isFenxiao: '否',
				oneTeamCount: 0,
				twoTeamCount: 0,
				yijiyongjin: '',
				erjiyongjin: '',
				currentTab: 0,
				tabsnva: [{
					loadingType: ''
				}],
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
			};
		},
		onLoad() {
			uni.showLoading({
				title: '加载中...'
			});
			let YaoqingShangJin = this.$queue.getData('YaoqingShangJin');
			if (YaoqingShangJin && YaoqingShangJin != 0) {
				this.YaoqingShangJin = YaoqingShangJin;
			}
			// this.getSetting();
			this.getUserInfo();
			this.getMoney();
			this.getTeamMoney()
		},
		methods: {
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.yijiyongjin = res.data.zhiRate ? res.data.zhiRate : 0;
						this.erjiyongjin = res.data.feiRate ? res.data.feiRate : 0;
						uni.setStorageSync('avatar', res.data.avatar)
						uni.setStorageSync('invitationCode', res.data.invitationCode)
						uni.setStorageSync('zhiFuBao', res.data.zhiFuBao)
						uni.setStorageSync('zhiFuBaoName', res.data.zhiFuBaoName)
					}
				});
			},
			getTeamMoney() {
				let data = {
					// type: this.type
				}
				this.$Request.getT('/app/orders/selectTeamStatistics', data).then(res => {
					if (res.code == 0) {
						this.teamCount = res.data.teamCount
						this.teamMoney = res.data.teamMoney
						this.oneTeamCount = res.data.oneTeamCount
						this.twoTeamCount = res.data.twoTeamCount

					}
				});
			},
			getSetting() {
				this.$Request.getT('/common/type/91').then(res => {
					if (res.code == 0) {
						if (res.data && res.data.value) {
							this.setting = res.data.value
						}
					}
				});
			},
			goYao() {
				uni.navigateTo({
					url: '/pages/my/invitationUser'
				});
			},
			changeList(zhishu) {
				this.page = 1
				this.type = zhishu

				this.getMoney()
			},
			changeTab(e) {
				console.log('eeeeeeeee', e)
				this.currentTab = e.target.current;

				if (this.currentTab == 0) {
					this.type = 1
					this.getMoney()
				} else if (this.currentTab == 1) {
					this.type = 2
					this.getMoney()
				}
			},
			getMoney() {
				this.loadingType = 1;
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
			goDet() {
				uni.navigateTo({
					url: '/my/team/earnings'
				})
			}
		},
		onPullDownRefresh: function() {
			this.page = 0;
			this.limit = 0;
			this.list = [];
		},
		onReachBottom() {
			this.page = this.page + 1;
			this.getMoney()

		},
	};
</script>

<style lang="scss">
	@import '../../static/css/index.css';

	.view1 {
		background-color: #291B3D;
		width: 93%;
		// height: 300upx;
		margin-left: 26upx;
		border-radius: 20upx;
		margin-top: 20upx;
		padding-bottom: 32upx;
	}

	.view2 {
		background-color: #291B3D;
		width: 93%;
		// height: 100%;
		border-top-left-radius: 20upx;
		border-top-right-radius: 20upx;

		margin-left: 26upx;
		margin-top: 20upx;
		border-radius: 20upx;
		margin-bottom: 50upx;
		padding-bottom: 20rpx

	}

	.navbar {
		display: flex;
		height: 40px;
		border-radius: 20upx;
		// box-shadow: 0 1px 5px rgba(0, 0, 0, .06);
		position: relative;
		z-index: 10;
		margin: 0 24rpx;
		border-radius: 20rpx;
		overflow: hidden;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;

			position: relative;

			&.current {

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

	.yaoqing_btn {
		// width: 80%;

		line-height: 80upx;
		margin-top: 30upx;
		height: 85upx;
		color: #FFFFFF;
		background: #AC75FE;
		margin-left: 32upx;
		margin-right: 32upx;

		background-size: 100%;
	}
</style>
