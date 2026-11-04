<template>
	<view class="content">

		<view class="view1" style="border-radius: 20upx;margin-bottom: 20upx;">
			<view style="display: flex;">
				<view style="margin: 70upx 0 0 20upx;height: 100upx; width: 300upx; text-align: center;">
					<view style="font-size: 40upx;color: #AC75FE;">{{ count?count:0 }}</view>
					<view style="font-size: 28upx;margin-left: 20upx;">邀请好友（人）</view>
				</view>
				<view style="margin: 70upx 0 0 75upx;height: 100upx; width: 300upx;text-align: center;" @click="goDet">
					<view style="font-size: 40upx;color: #AC75FE;">{{ sumMoney?sumMoney:0 }}</view>
					<view style="font-size: 28upx;">我的收益</view>
				</view>
			</view>

			<view class="flex align-center justify-between margin-lr margin-top-sm">
				<view style="font-size: 28rpx;color: #AC75FE;">
					*可得礼物赠送奖励的{{consortiaGiftRate*100}}%</view>
				<view style="font-size: 28rpx;color: #AC75FE;">
					*可得服务订单奖励的{{consortiaOrdersRate*100}}%</view>
			</view>

			<button class="yaoqing_btn" @click="goMygonghuo()" v-if="Huizhang">公会管理</button>
		</view>


		<view class="view2" v-if="list.length!=0">
			<view style="display: flex;flex-direction: row;" class="padding">
				<view style="width: 25%;">邀请码</view>
				<view style="width: 40%;text-align: center;">昵称</view>
				<view style="width: 30%;text-align: center;">时间</view>
				<view style="width: 15%;"></view>
			</view>
			<view class="flex justify-between align-center padding-lr-sm padding-bottom-sm"
				v-for="(item, index) in list" :key="index" style="margin-bottom: 16upx">
				<view class="flex align-center" style="width: 25%;" @click.stop="copyClick(item.invitationCode)">
					<view style="font-size: 28upx;margin-left: 15upx;margin-top: 6upx;">{{item.invitationCode}}
					</view>
					<image src="../static/copy.png" style="width:24upx;height:24upx;margin-left:10upx;"></image>
				</view>
				<view style="width: 40%;display: flex;flex-direction: row;align-items: center;"
					@tap="goGuanZhuDetail(item.userId)">
					<view class="text-center"
						style="font-size: 28upx;width: 90%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
						{{ item.userName }}
						<!-- <text v-if="item.isAuthentication==1"
							style="font-size: 22upx;color:#fff;background: #525363;margin-left:10upx;border-radius: 33upx;padding: 5upx 15upx;">已实名</text> -->
					</view>
				</view>
				<view style="width: 30%;text-align: center;">
					<view style="font-size: 28upx;">
						{{ item.createTime }}
					</view>
				</view>
				<view style="width: 15%;" v-if="Huizhang">
					<view class="btn" @click="submit(item.userId)" v-if="huizUserId!=item.userId">踢出</view>
				</view>
			</view>
			<empty v-if="list.length==0" content="暂无数据"></empty>
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"
			:title="title"></popup>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	import popup from '@/components/popup.vue'
	export default {
		components: {
			popup,
			empty
		},
		data() {
			return {
				popupshow: false,
				content: '',
				id: '',
				title: '',

				setting: 1,
				list: [],
				page: 1,
				limit: 20,
				consortiaId: '',
				status: 0,
				count: 0,
				sumMoney: 0,
				isFenxiao: '否',
				oneTeamCount: 0,
				twoTeamCount: 0,
				consortiaOrdersRate: '',
				consortiaGiftRate: '',
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
				consortiaId: '',
				Huizhang: false,
				huizUserId: ''
			};
		},
		onLoad() {
			uni.showLoading({
				title: '加载中...'
			});
			// this.getSetting();
			// this.getUserInfo();
			// this.getTeamMoney()
		},
		onShow() {
			this.getUserInfo();
			this.getTeamMoney()
		},
		methods: {
			goGuanZhuDetail(userId) {
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + userId + '&id=' + this.id
				});

			},
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
			//是否会长
			getUserTd() {
				let data = {
					consortiaId: this.consortiaId
				}
				this.$Request.get("/app/consortia/selectConsortiaById", data).then(res => {
					if (res.code == 0) {
						this.huizUserId = res.data.userId
						let userId = res.data.userId
						let myUserId = this.$queue.getData('userId');
						if (myUserId == userId) {
							console.log('我是会长：' + '少时诵诗书')
							this.Huizhang = true
						} else {
							console.log('我不是会长：' + '少时诵诗书')
							this.Huizhang = false
						}
						uni.setStorageSync("Huizhang", this.Huizhang)
					}
				});

			},
			sureT() {
				let that = this
				let data = {
					userId: that.id,
				}
				that.$Request.postT("/app/consortia/outConsortiaPerson", data).then(ret => {
					if (ret.code == 0) {
						that.popupshow = false
						that.page = 1
						that.getMoney();
						uni.showToast({
							title: '踢出成功！',
							icon: 'none'
						})
					} else {
						uni.showToast({
							title: ret.msg,
							icon: "none"
						})
					}

				});
			},
			//踢出
			submit(userId) {
				this.id = userId
				this.title = '温馨提示'
				this.popupshow = true
				this.content = '确定将此陪陪踢出公会吗？踢出您将无法获取陪陪带来的收益！'

			},
			//我的公会
			goMygonghuo() {
				uni.navigateTo({
					url: '/package/pages/gonghui/myglList'
				})
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.consortiaId = res.data.consortiaId
						this.getUserTd()
						this.page = 1
						this.getMoney();
						this.consortiaOrdersRate = res.data.consortiaOrdersRate ? res.data.consortiaOrdersRate : 0;
						this.consortiaGiftRate = res.data.consortiaGiftRate ? res.data.consortiaGiftRate : 0;
					}
				});
			},
			getTeamMoney() {
				let consortiaId = this.$queue.getData('consortiaId');
				let data = {
					consortiaId: consortiaId,
				}
				this.$Request.getT('/app/consortia/selectConsortiaData', data).then(res => {
					if (res.code == 0) {
						this.sumMoney = res.data.sumMoney
						this.count = res.data.count

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
			getMoney() {
				this.loadingType = 1;
				uni.showLoading({
					title: '加载中...'
				});
				let data = {
					page: this.page,
					limit: this.limit,
					consortiaId: this.consortiaId,
				}
				this.$Request.getT('/app/consortia/selectConsortiaPersonList', data).then(res => {
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
					url: '/package/pages/gonghui/mygh?Huizhang=' + this.Huizhang
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
	@import '../../../static/css/index.css';

	page {
		background-color: #1A1728;
	}

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
		margin: 60rpx auto;
		border-radius: 20upx;
		/* #ifdef H5 */
		padding: 30upx;
		/* #endif */
	}

	.navbar {
		display: flex;
		height: 40px;
		border-radius: 20upx;
		box-shadow: 0 1px 5px rgba(0, 0, 0, .06);
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

				background-color: #1E1F31;

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
		color: #fff;
		background: #AC75FE;
		margin-left: 32upx;
		margin-right: 32upx;

		background-size: 100%;
	}

	.btn {
		color: #fff;
		background: #AC75FE;
		border-radius: 10rpx;
		padding: 15rpx 0rpx;
		text-align: center;
		margin-left: 15upx;
	}
</style>
