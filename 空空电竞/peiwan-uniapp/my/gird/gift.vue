<template>
	<view>
		<!-- 顶部tab -->
		<view class="headtop padding-bottom-sm">
			<u-tabs :list="list" :is-scroll="false" :current="current" @change="change" active-color="#B578FA"></u-tabs>
		</view>

		<view style="margin-top: 110upx;" v-if="giftList.length>0">
			<view v-for="(item,index) in giftList" :key="index">
				<view class="box">
					<view class="flex align-center">
						<image :src="item.giftImg" style="width: 90upx;" mode="widthFix"></image>
						<view class="margin-left-sm" style="width: 100%;">
							<view class="flex align-center justify-between">
								<view>{{item.giftName}}</view>
								<!-- <view class="" style="color: #B578FA;font-size: 30upx;">
									￥<text style="font-size: 48upx;">{{item.giftMoney}}</text>
								</view> -->
							</view>
							<view v-if="current == 0"><text style="color: #B578FA;"
									@click="goGuanZhuDetail(item.sendUserId)">{{item.sendUserName}}</text>赠送了礼物{{item.giftName}}
							</view>
							<view v-else>给<text style="color: #B578FA;"
									@click="goGuanZhuDetail(item.userId)">{{item.userName}}</text>赠送了礼物{{item.giftName}}
							</view>
						</view>
					</view>
					<view style="width: 100%;height: 1rpx;border: 2rpx dashed #111224;margin: 30upx 0upx;"></view>
					<view>赠送时间：{{item.createTime}}</view>
				</view>
			</view>
		</view>

		<empty v-if="giftList.length === 0" content="暂无记录" show="false"></empty>
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
				list: [{
					name: '收到礼物'
				}, {
					name: '送出礼物'
				}],
				current: 0,
				page: 1,
				limit: 10,
				giftList: [],
				count: 0
			}
		},
		onLoad() {
			this.$queue.showLoading("加载中...");
			this.getgiftList();
		},
		methods: {
			change(index) {
				this.current = index;
				this.getgiftList();
			},
			// 查询自己收到的礼物 
			getgiftList() {
				if (this.current == 0) {
					this.$queue.showLoading("加载中...");
					let data = {
						page: this.page,
						limit: this.limit
					}
					this.$Request.getT("/app/gift/selectGiftUser", data).then(res => {
						if (res.code === 0 && res.data) {
							if (this.page == 1) {
								this.giftList = res.data.list
							} else {
								res.data.list.forEach(d => {
									this.giftList.push(d);
								});
							}
							this.count = res.data.totalCount;
						}
						uni.stopPullDownRefresh();
						uni.hideLoading();
					});
				} else if (this.current == 1) {
					this.$queue.showLoading("加载中...");
					let data = {
						page: this.page,
						limit: this.limit
					}
					this.$Request.getT("/app/gift/selectGiftSendUser", data).then(res => {
						if (res.code === 0 && res.data) {
							this.totalCount = res.data.totalCount
							if (this.page == 1) {
								this.giftList = res.data.list
							} else {
								res.data.list.forEach(d => {
									this.giftList.push(d);
								});
							}
							this.count = res.data.totalCount;
						}
						uni.stopPullDownRefresh();
						uni.hideLoading();
					});
				}

			},
			goGuanZhuDetail(userId) {
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + userId
				});
			},
		},
		onReachBottom: function() {
			if (this.giftList.length == this.count) {
				uni.showToast({
					title: '已经到底了',
					icon: 'none'
				})
			} else {
				this.page = this.page + 1;
				this.getgiftList();
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getgiftList();
		}
	}
</script>

<style>
	page {
		background: #111224;
	}
.bg {
		background-color: #291B3D;
	}
	.u-tab-item {
		color: #ffffff !important;
	}

	.u-tabs {
		background: #1E1F31 !important;
	}


	.box {
		background: #291B3D;
		margin: 30upx 20upx;
		padding: 30upx;

		border-radius: 16upx;
	}

	.headtop {
		position: fixed;
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		/* #ifdef H5 */
		top: 84upx;
		/* #endif */
		left: 0;
		right: 0;
		z-index: 999;
	}
</style>
