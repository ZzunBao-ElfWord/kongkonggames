<template>
	<view>
		<view class="navbar">
			<view v-for="(item, index) in tabList" :key="index" class="nav-item" :class="{current: type == item.type}"
				@click="changeList(item.type)">
				{{item.text}}
			</view>
		</view>

		<!-- #ifndef H5 -->
		<view style="text-align: left;padding-bottom: 10rpx;margin-top: 90upx;" v-if="list.length>0">
			<!-- #endif -->
			<!-- #ifdef H5 -->
			<view style="text-align: left;padding-bottom: 10rpx;margin-top: 120upx;" v-if="list.length>0">
				<!-- #endif -->
				<view v-for="(item, index) in list" :key="index" class="item">
					<view>
						<!-- <view style="margin-bottom: 8upx;text-align: right;">
						<text v-if="item.type == 1" style="margin-bottom: 8upx;color: #ecd4b4">充值</text>
						<text v-if="item.type == 2" style="margin-bottom: 8upx;color: #ecd4b4">提现</text>
					</view> -->
						<view style="color: #999999;font-size: 28upx;">
							<view style="margin-bottom: 8upx">{{item.title}}</view>
							<!-- <view v-if="item.classify === 2" style="margin-bottom: 8upx"> 返佣类型：直属返佣</view> -->
							<!-- <view v-if="item.classify === 3" style="margin-bottom: 8upx"> 返佣类型：非直属支付</view> -->
							<view style="margin-bottom: 8upx">{{item.content}}</view>
							<view style="margin-bottom: 8upx"> 创建时间：{{item.createTime}}</view>
							<view style="margin-bottom: 8upx;text-align: right;">
								<text v-if="item.type == 1"
									style="color: #FBEA74;font-size: 32upx;font-weight: 600"><text
										class="text-olive">+</text>{{item.money}}柚币</text>
								<text v-if="item.type == 2"
									style="color: #FBEA74;font-size: 32upx;font-weight: 600"><text
										class="text-red">-</text>{{item.money}}柚币</text>
							</view>
						</view>
					</view>
				</view>
			</view>
			<empty v-if="list.length == 0" content="暂无明细"></empty>
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
				list: [],
				page: 1,
				limit: 10,
				tabIndex: 1,
				checkReZhiShu: '否',
				checkReTuanZhang: '否',
				checkReFeiZhiShu: '否',
				scrollTop: false,
				contentText: {
					contentdown: '上拉显示更多',
					contentrefresh: '正在加载...',
					contentnomore: '没有更多数据了'
				},
				tabList: [{
					text: '订单',
					type: 5,
				}, {
					text: '礼物',
					type: 6,
				}],
				type: 5,
				count: 0,
				consortiaId: '',
				userId: ''
			}
		},
		onLoad(option) {
			this.$queue.showLoading("加载中...");
			if (option.userId) {
				this.userId = option.userId
				this.consortiaId = option.consortiaId
				this.getList();
			}

		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		methods: {
			changeList(e) {
				this.page = 1
				this.type = e
				this.getList()
			},
			getList() {
				let data = {
					page: this.page,
					limit: this.limit,
					userId: this.userId,
					consortiaId: this.consortiaId,
					classify: this.type,
				}
				this.$Request.getT('/app/consortia/selectConsortiaPersonMoneyDetails', data).then(res => {
					if (res.code == 0 && res.data) {
						if (this.page == 1) {
							this.list = res.data.records
						} else {
							this.list = [...this.list, ...res.data.records]
						}

						this.count = res.data.total
					}
					uni.hideLoading();
					uni.stopPullDownRefresh();
				});
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
		color: #FFFFFF;
	}
	

	.tui-tab-item-title {
		color: #ffffff;
		font-size: 30rpx;
		height: 80rpx;
		line-height: 80rpx;
		flex-wrap: nowrap;
		white-space: nowrap;
	}

	.tui-tab-item-title-active {
		border-bottom: 1px solid #5E81F9;
		color: #1789FD;
		font-size: 32upx;
		font-weight: bold;
		border-bottom-width: 6upx;
		text-align: center;
	}

	.item {
		background: #291B3D;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		// box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
	}

	.navbar {
		display: flex;
		height: 84rpx;

		// box-shadow: 0 1px 5px rgba(0, 0, 0, .06);
		background: #1E1F31;
		overflow: hidden;
		position: fixed;
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		/* #ifdef H5 */
		top: 44px;
		/* #endif */
		left: 0;
		right: 0;
		z-index: 10;


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
				color: #fff;
				background-color: #362f5b;

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
