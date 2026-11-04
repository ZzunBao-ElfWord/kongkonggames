<template>
	<view style="padding-bottom: 40rpx;">
		<view class="myList flex align-center justify-center">
			<view class="myList-box">
				<view class="myList-box-item" v-for="(item,index) in list" :key="index">
					<view class="myList-box-item-top flex align-center">
						<image :src="item.avatar?item.avatar:'../../../static/logo.png'" mode="aspectFill"></image>
						<view class="myList-box-item-top-name">
							{{item.userName?item.userName:''}}
						</view>
					</view>
					<view class="myList-box-item-contont" v-if="item.content">
						{{item.content}}
					</view>
					<view class="myList-box-item-score">
						<u-rate disabled :count="5" inactive-color="#999999" active-color="#AC75FE"
							v-model="item.score"></u-rate>
					</view>
					<view class="myList-box-item-bom flex align-center justify-between">
						<view class="myList-box-item-bom-l">
							{{item.createTime}}
						</view>
						<view class="myList-box-item-bom-r" @click="delItem(item.id,index)">
							删除
						</view>
					</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length==0" content="暂无评论" />
		<!-- 提示弹窗 -->
		<popup v-if="popupshow" :show="popupshow" @closeT="closeT()" @sureT="sureT" :content="content"></popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import empty from '../../../components/empty.vue'
	export default {
		components: {
			popup,
			empty
		},
		data() {
			return {
				content: '确认删除该评论吗？',
				popupshow: false,
				list: [],
				page: 1,
				pages: 1,
				limit: 20,
				id: -1,
				index: -1,
			};
		},
		onLoad() {
			this.getList()
		},
		onPullDownRefresh() {
			this.page = 1
			this.getList()
		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.getList()
			}
		},
		methods: {
			//删除你评论
			delItem(id, index) {
				this.id = id
				this.index = index
				this.popupshow = true
			},
			//确认删除
			sureT() {
				let data = {
					id: this.id
				}
				this.$Request.post('/app/takingComment/deleteOrderTakingComment', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '已删除'
						})
						this.list.splice(this.index, 1) //更新列表
						this.closeT()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//关闭提示弹窗
			closeT() {
				this.id = -1
				this.index = -1
				this.popupshow = false
			},
			getList() {
				let data = {
					page: this.page,
					limit: this.limit,
					userId: uni.getStorageSync('userId')
				}
				this.$Request.getT('/app/takingComment/selectCommentByUserId', data).then(res => {
					uni.hideLoading()
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.pages = res.data.totalPage
						if (this.page == 1) {
							this.list = res.data.list
						} else {
							this.list = [...this.list, ...res.data.list]
						}
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	.myList {
		width: 100%;
		height: auto;

		.myList-box {
			width: 686rpx;
			height: 100%;
		}

		.myList-box-item {
			width: 100%;
			background-color: #291B3D;
			border-radius: 24rpx;
			margin-top: 20rpx;
			padding: 30rpx;
		}

		.myList-box-item-top {
			image {
				width: 60rpx;
				height: 60rpx;
				border-radius: 50%;

			}

			.myList-box-item-top-name {
				margin-left: 20rpx;
			}
		}

		.myList-box-item-contont {
			margin-top: 20rpx;
		}

		.myList-box-item-score {
			margin-top: 20rpx;
		}

		.myList-box-item-bom {
			margin-top: 20rpx;

			.myList-box-item-bom-l {
				color: #999999;
			}

			.myList-box-item-bom-r {
				border: 1px solid #AC75FE;
				color: #AC75FE;
				padding: 6rpx 20rpx;
				border-radius: 24rpx;
				font-size: 26rpx;
			}
		}

	}
</style>