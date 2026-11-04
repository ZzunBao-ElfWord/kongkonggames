<template>
	<view class="content">
		<view v-if="dataList.length != 0" class="bg u-flex u-p-l-30 u-p-t-30 u-p-b-10 u-p-r-30" v-for="(item,index) in dataList" :key='index' @tap="goGuanZhuDetail(item.userId)">
			<view class="u-m-r-10">
				<u-avatar :src="item.avatar?item.avatar: '../../static/logo.png'" size="100"></u-avatar>
			</view>
			<view class="u-flex-1 text-white margin-left-xs">
				<view class="u-font-16  text-bold">{{item.userName}}</view>
				<view class="u-font-14 margin-top-sm u-tips-color" @click="goNav('/pages/me/vip/index')">{{item.updateTime?item.updateTime:''}}</view>
			</view>
			<!-- <view>
				<view v-if="item.status == 1" @click="insert(item)" class="round"
					style="color: white;background: #474966;padding: 10upx 24upx;width: 150upx;text-align: center;font-size: 22upx;">
					互相关注</view>
				<view v-if="item.status == 2 && type == 1" @click="insert(item)" class="round"
					style="color: white;background: #474966;padding: 10upx 24upx;width: 150upx;text-align: center;font-size: 22upx;">
					回关</view>
				<view v-if="item.status == 2 && type == 2" @click="insert(item)" class="round"
					style="color: white;background: #474966;padding: 10upx 24upx;width: 150upx;text-align: center;font-size: 22upx;">
					已关注</view>
			</view> -->
		</view>
		
		<empty v-if="dataList.length == 0" ></empty>
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				dataList: [],
				type: 1,
				page: 1,
				limit: 10
			}
		},
		onLoad(e) {
			console.log(e)
			this.$queue.showLoading("加载中...");
			uni.setNavigationBarTitle({
				title: e.name
			})
			this.type = e.type
			if (this.type == 1) {
				this.getFansList()
			} else {
				this.getFollowList()
			}
		},
		methods: {
			goGuanZhuDetail(userId){
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + userId
				});
			},
			// 获取粉丝数量
			getFansList() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$Request.get("/app/userFollow/selectFans", data).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						if(this.page == 1) {
							this.dataList = res.data.list
						} else {
							this.dataList = [...this.dataList, ...res.data.list]
						}
					} else {
						console.log(res.msg)
					}
					uni.stopPullDownRefresh();
				});
			},
			// 获取关注数量
			getFollowList() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$Request.get("/app/userFollow/selectMyFollow", data).then(res => {
					if (res.code == 0) {
						if(this.page == 1) {
							this.dataList = res.data.list
						} else {
							this.dataList = [...this.dataList, ...res.data.list]
						}
					} else {
						console.log(res.msg)
					}
					uni.hideLoading();
					uni.stopPullDownRefresh();
				});
			},
			insert(e) {
				let that = this
				let data = {
					followUserId: e.userId
				}
				that.$Request.get("/app/userFollow/insert", data).then(res => {
					console.log(res)
					if (res.code == 0) {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						setTimeout(function() {
							if (that.type == 1) {
								that.getFansList()
							} else {
								that.getFollowList()
							}
						}, 500)
					}
				});
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			if (e.type == 1) {
				this.getFansList()
			} else {
				this.getFollowList()
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			// this.dataList = []
			if (this.type == 1) {
				this.getFansList()
			} else {
				this.getFollowList()
			}
		},
	}
</script>

<style>

</style>
