<template>
	<view class="content">
		<view v-if="dataList.length != 0" class="bg u-flex u-p-l-30 u-p-t-30 u-p-b-10 u-p-r-30"
			v-for="(item,index) in dataList" :key='index' @longpress="delData(item)"
			@tap="goGuanZhuDetail(item.userId)">
			<view class="u-m-r-10">
				<u-avatar :src="item.avatar?item.avatar: '../../static/logo.png'" size="100"></u-avatar>
			</view>
			<view class="u-flex-1 text-white margin-left-xs">
				<view class="u-font-16  text-bold">{{item.userName}}</view>
				<view class="u-font-14 margin-top-sm u-tips-color" @click="goNav('/pages/me/vip/index')">
					{{item.updateTime}}访问了你
				</view>
			</view>
		</view>
		<empty v-if="dataList.length == 0"></empty>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import empty from '../../components/empty.vue'
	export default {
		components: {
			empty,
			popup
		},
		data() {
			return {
				popupshow: false,
				content: '',
				id: '',


				dataList: [],
				page: 1,
				limit: 15
			}
		},
		onLoad(e) {
			// uni.setNavigationBarTitle({
			// 	title: e.name
			// })
			this.$queue.showLoading("加载中...");
			this.getVisitorList()

		},
		methods: {
			goGuanZhuDetail(userId) {
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + userId
				});
			},
			// 访客
			getVisitorList() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$Request.get("/app/userBrowse/myVisitor", data).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						if (this.page == 1) {
							this.dataList = res.data.list
						} else {
							this.dataList = [...this.dataList, ...res.data.list]
						}
					} else {
						console.log(res.msg)
					}
					uni.stopPullDownRefresh();
				})
			},
			sureT() {
				let that = this
				let data = {
					id: that.id
				}
				that.$Request.post("/app/userBrowse/deleteMyVisitor", data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '删除成功！',
							icon: 'none'
						})
						that.page = 1
						that.getVisitorList()
						that.popupshow = false
					}
				})
			},
			// 删除
			delData(e) {
				this.id = e.id
				this.content = '确定删除吗？'
				this.popupshow = true

			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getVisitorList()
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getVisitorList()
		},
	}
</script>

<style lang="less">
	::v-deep .textMoney {
		text-align: center;
	}
</style>