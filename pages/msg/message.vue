<template>
	<view class="content">
		<view class="navbar">
			<view v-for="(item, index) in tabList" :key="index" class="nav-item"
				:class="{ current: tabFromIndex === item.state }" @click="tabClicks(item.state)">
				{{ item.text }}
			</view>
		</view>
		<view v-for="(item, index) in list" :key="index" class="item" @click="goDet(item.content,item.title)" >
			<view class="flex justify-between"
				style="color: #F0F0F0;font-size: 30upx;width: 100%;overflow: hidden;text-overflow: ellipsis;white-space:nowrap">
				<view>{{ item.title }}</view>
				<view v-if="item.isSee == 0"
					style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
				</view>
			</view>
			<view style="color: #999999;font-size: 28upx;margin-top: 10upx;">{{ item.content }}</view>

			<view style="margin-top: 10upx;color: #999999;font-size: 28upx;text-align: right;">{{ item.createAt }}
			</view>
		</view>
		<!-- <view v-if="list.length === 0" style="background: #1c1b20;text-align: center;padding-top: 140upx;color: #FFFFFF;">暂无消息</view> -->
		<empty v-if="list.length === 0" des="暂无消息" show="false"></empty>
	</view>
</template>

<script>
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	import empty from '@/components/empty';

	export default {
		components: {
			uniLoadMore,
			empty
		},
		data() {
			return {
				tabFromIndex: 5,
				tabCurrentIndex: 0,
				fromInfo: 5,
				list: [],
				page: 1,
				limit: 10,
				scrollTop: false,
				tabList: [{
						state: 5,
						text: '用户消息',
						totalElements: 0
					},
					{
						state: 4,
						text: '订单消息',
						totalElements: 0
					}
				]
			};
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.loadData();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.loadData();
		},
		onLoad(options) {
			this.$queue.showLoading("加载中...")
			this.loadData();
		},
		methods: {
			goDet(e,title) {
				console.log(e.indexOf('下单'))
				if(e.indexOf('下单')!=-1) {
					uni.navigateTo({
						url: '/my/order/index'
					})
				} else if (e.indexOf('接单')!=-1) {
					uni.navigateTo({
						url: '/my/takeOrder/index'
					})
				}else if (e.indexOf('订单审核通过')!=-1) {
					uni.navigateTo({
						url: '/my/publish/index'
					})
				}else if(title.indexOf('加入公会成功')!=-1){
					uni.navigateTo({
						url: '/package/pages/play/gonghui'
					});
				}else if(title.indexOf('加入公会申请失败')!=-1){
					uni.navigateTo({
						url: '/package/pages/play/gonghui'
					});
				}else if(title.indexOf('退出公会成功')!=-1){
					uni.navigateTo({
						url: '/package/pages/play/gonghui'
					});
				}else if(title.indexOf('退出公会失败')!=-1){
					uni.navigateTo({
						url: '/package/pages/play/gonghui'
					});
				}else if(title.indexOf('退出公会通知')!=-1){
					uni.navigateTo({
						url: '/package/pages/play/gonghui'
					});
				}else if(title.indexOf('退会申请待审核')!=-1||title.indexOf('公会申请待审核')!=-1){
					uni.navigateTo({
						url: '/package/pages/gonghui/myglList'
					});
				}
			},
			//顶部渠道点击
			tabClicks(index) {
				this.list = [];
				this.page = 1;
				this.tabFromIndex = index;
				this.$queue.showLoading("加载中...")
				this.loadData();
			},
			//获取消息列表
			loadData() {
				let that = this;
				let number = 10;
				let token = this.$queue.getData('token');
				if (token) {
					let data = {
						page: this.page,
						limit: this.limit,
						state: this.tabFromIndex
					}
					this.$Request.getT('/app/message/selectMessageByUserId', data).then(res => {
						if (res.code === 0) {
							if(this.page == 1) {
								this.list = res.data.list
							} else {
								res.data.list.forEach(d => {
									this.list.push(d);
								});
							}
						}
						uni.hideLoading();
						uni.stopPullDownRefresh();
					});
				}
			}
		}
	};
</script>

<style lang="scss">
	page,
	page {
		background: #050506;
	}

	.content {
		background: #050506;
		height: 100%;
	}

	.swiper-box {
		height: calc(100% - 40px);
	}

	.list-scroll-content {
		height: 100%;
	}

	.navbar {
		display: flex;
		height: 40px;
		padding: 0 5px;
		background: #050506;
		box-shadow: 0 1px 5px rgba(0, 0, 0, 0.06);
		position: relative;
		z-index: 10;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;
			color: #FFFFFF;
			position: relative;

			&.current {
				color: #FBCA0C;

				&:after {
					content: '';
					position: absolute;
					left: 50%;
					bottom: 0;
					transform: translateX(-50%);
					width: 44px;
					height: 0;
					border-bottom: 2px solid #FBCA0C;
				}
			}
		}
	}

	.uni-swiper-item {
		height: auto;
	}

	.item {
		background: #101014;
		padding: 16rpx;
		margin: 16rpx;
		font-size: 28rpx;
		box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
	}


	/* load-more */
	.uni-load-more {
		display: flex;
		flex-direction: row;
		height: 40px;
		align-items: center;
		justify-content: center;
	}

	.uni-load-more__text {
		font-size: 14px;
		color: #999;
	}

	.uni-load-more__img {
		height: 24px;
		width: 24px;
		margin-right: 10px;
	}

	.uni-load-more__img>view {
		position: absolute;
	}

	.uni-load-more__img>view view {
		width: 6px;
		height: 2px;
		border-top-left-radius: 1px;
		border-bottom-left-radius: 1px;
		background: #999;
		position: absolute;
		opacity: 0.2;
		transform-origin: 50%;
		animation: load 1.56s ease infinite;
	}

	.uni-load-more__img>view view:nth-child(1) {
		transform: rotate(90deg);
		top: 2px;
		left: 9px;
	}

	.uni-load-more__img>view view:nth-child(2) {
		transform: rotate(180deg);
		top: 11px;
		right: 0;
	}

	.uni-load-more__img>view view:nth-child(3) {
		transform: rotate(270deg);
		bottom: 2px;
		left: 9px;
	}

	.uni-load-more__img>view view:nth-child(4) {
		top: 11px;
		left: 0;
	}

	.load1,
	.load2,
	.load3 {
		height: 24px;
		width: 24px;
	}

	.load2 {
		transform: rotate(30deg);
	}

	.load3 {
		transform: rotate(60deg);
	}

	.load1 view:nth-child(1) {
		animation-delay: 0s;
	}

	.load2 view:nth-child(1) {
		animation-delay: 0.13s;
	}

	.load3 view:nth-child(1) {
		animation-delay: 0.26s;
	}

	.load1 view:nth-child(2) {
		animation-delay: 0.39s;
	}

	.load2 view:nth-child(2) {
		animation-delay: 0.52s;
	}

	.load3 view:nth-child(2) {
		animation-delay: 0.65s;
	}

	.load1 view:nth-child(3) {
		animation-delay: 0.78s;
	}

	.load2 view:nth-child(3) {
		animation-delay: 0.91s;
	}

	.load3 view:nth-child(3) {
		animation-delay: 1.04s;
	}

	.load1 view:nth-child(4) {
		animation-delay: 1.17s;
	}

	.load2 view:nth-child(4) {
		animation-delay: 1.3s;
	}

	.load3 view:nth-child(4) {
		animation-delay: 1.43s;
	}

	@-webkit-keyframes load {
		0% {
			opacity: 1;
		}

		100% {
			opacity: 0.2;
		}
	}
</style>
