<template>
	<view>
		<view class="list flex align-center justify-center">
			<view class="list-box">
				<view class="list-box-item flex align-center">
					<view class="list-box-item-i" style="color: #FBCA0C;font-weight: bold;">
						主播昵称
					</view>
					<view class="list-box-item-i" style="color: #FBCA0C;font-weight: bold;">
						违规原因
					</view>
					<view class="list-box-item-i" style="color: #FBCA0C;font-weight: bold;">
						处罚结果
					</view>
					<view class="list-box-item-i" style="color: #FBCA0C;font-weight: bold;">
						时间
					</view>
				</view>
				<view :style="{background:isEvenOrOdd(index)?'rgba(87,92,106,0.1)':''}"
					class="list-box-item flex align-center" v-for="(item,index) in list" :key="index">
					<view class="list-box-item-i">
						{{item.userName}}
					</view>
					<view class="list-box-item-i">
						{{item.auditContent}}
					</view>
					<view class="list-box-item-i" style="color: #FBCA0C;font-weight: bold;">
						{{item.auditType==1?'封禁直播间':(item.auditType==2?'封禁账号':'警告')}}
					</view>
					<view class="list-box-item-i">
						{{item.auditTime}}
					</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length==0" />
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
				page: 1,
				pages: 1,
				limit: 15,
				list: []
			}
		},
		onShow() {
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
			getList() {
				let data = {
					page: this.page,
					limit: this.limit,
					status: 2
				}
				this.$Request.getT('/app/roomAudit/selectRoomAuditList', data).then(res => {
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
			//判断奇偶
			isEvenOrOdd(num) {
				return num % 2 === 0;
			},
		}
	}
</script>

<style lang="scss">
	.list {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.list-box {
			width: 686rpx;
			height: 100%;
			background-color: rgba(87, 92, 106, 0.1);
			border-radius: 16rpx;
		}

		.list-box-item {
			width: 100%;

			.list-box-item-i {
				width: calc(100% / 4);
				text-align: center;
				color: #ffffff;
				padding: 30rpx 10rpx;
			}
		}
	}
</style>