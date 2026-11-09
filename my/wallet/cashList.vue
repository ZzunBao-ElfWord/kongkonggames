<template>
	<view style="text-align: left;padding-bottom: 10rpx;">
		<view v-for="(item, index) in list" :key="index" class="item">
			<view>
				<view style="margin-bottom: 8upx;text-align: right;">
					<text style="margin-bottom: 8upx;color: #FBCA0C" v-if="item.state==1"> 提现成功</text>
					<text style="margin-bottom: 8upx;color: #FBCA0C" v-if="item.state==0"> 提现中</text>
					<text style="margin-bottom: 8upx;color: #e10a07" v-if="item.state==-1"> 提现失败</text>
				</view>

				<view style="color: #999999;font-size: 28upx;">
					<view style="margin-bottom: 8upx" v-if="item.classify == 1"> 支付宝账号：{{item.zhifubao}}</view>
					<view style="margin-bottom: 8upx" v-if="item.classify == 1"> 支付宝姓名：{{item.zhifubaoName}}</view>
					<view style="margin-bottom: 8upx" v-if="item.classify == 2"> 微信小程序提现</view>
					<view style="margin-bottom: 8upx" v-if="item.classify == 3"> 微信公众号提现</view>
					<view style="margin-bottom: 8upx"> 提现时间：{{item.createAt}}</view>
					<view style="margin-bottom: 8upx" v-if="item.state==1">成功时间 {{item.outAt}}</view>
					<view style="margin-bottom: 8upx;color: #e10a07" v-if="item.state==-1">{{item.refund}}</view>

					<view style="margin-bottom: 8upx;text-align: right;">
						<text style="color: #FBCA0C;font-size: 32upx;font-weight: 600"> ￥{{item.money}}</text>
					</view>
				</view>

			</view>
		</view>
		<empty v-if="list.length === 0" content="暂无提现记录" show="false"></empty>

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
				limit: 10
			}
		},
		onLoad: function(e) {
			this.$queue.showLoading("加载中...");
			this.getMoney();
		},
		methods: {
			getMoney() {
				let that = this;
				let token = that.$queue.getData("token");
				let userId = that.$queue.getData("userId");
				if (token) {
					//可以提现金额查询预估收入查询
					let data = {
						page: that.page,
						limit: that.limit
					}
					that.$Request.getT("/app/cash/selectPayDetails",data).then(res => {
						if (res.code === 0 && res.data) {
							if(this.page == 1) {
								that.list = res.data.list;
							} else {
								that.list = [...that.list, ...res.data.list]
							}
						}
						uni.stopPullDownRefresh();
						uni.hideLoading();
					});
				}

			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getMoney();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getMoney();
		}

	}
</script>

<style lang='scss'>
	@import "../../static/css/index.css";


	.item {
		background: #101014;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
	}
</style>
