<template>
	<view>
		<view v-for="(item, index) in list" :key="index" class="item" @click="goDet(item.content,item.title)">
			<view class="flex justify-between"
				style="color: #F0F0F0;font-size: 30upx;width: 100%;overflow: hidden;text-overflow: ellipsis;white-space:nowrap">
				<view class="flex align-center justify-between" style="width: 100%;">
					<view class="text-bold">{{ item.title }}</view>
					<view style="color: #D7BCFF;font-size: 28upx;text-align: right;">{{ item.createAt }}</view>
				</view>
				<!-- <view v-if="item.isSee == 0"
					style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background:  red;color: #FFF;text-align: center;">
				</view> -->
			</view>
			<view style="color: #D7BCFF;font-size: 28upx;margin-top: 10upx;">{{ item.content }}</view>
		</view>
		<empty v-if="list.length === 0" des="暂无消息" show="false"></empty>
	</view>
</template>

<script>
	import empty from '@/components/empty';
	export default {
		components: {
			empty
		},
		data() {
			return {
				type: 1,
				list: [],
				page: 1,
				limit: 10,
				scrollTop: false,
			}
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
		onLoad(d) {
			if (d.type) {
				this.type = d.type;
				this.loadData();
			}
		},
		methods: {
			//获取消息列表
			loadData() {
				let that = this;
				let token = this.$queue.getData('token');
				if (token) {
					let data = {
						page: this.page,
						limit: this.limit,
						state: this.type
					}
					this.$Request.getT('/app/message/selectMessageByUserId', data).then(res => {
						if (res.code === 0) {
							if (this.page == 1) {
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
	}
</script>

<style lang="scss">
	page {
		background: #111224;
	}

	.item {
		background: linear-gradient(90deg, rgba(234, 134, 189, 0.1) 0%, rgba(162, 112, 248, 0.1) 100%);
		padding: 30rpx;
		margin: 30rpx;
		font-size: 28rpx;
		box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
	}
</style>