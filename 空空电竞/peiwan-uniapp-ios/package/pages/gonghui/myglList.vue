<template>
	<view>
		<view class="navbar">
			<view v-for="(item, index) in tabList" :key="index" class="nav-item" :class="{current: type == item.type}"
				@click="changeList(item.type)">
				{{item.text}}
			</view>
		</view>

		<view class="view2" v-if="list.length!=0">
			<view style="display: flex;flex-direction: row;" class="padding">
				<view style="width: 15%;text-align: center;">编号</view>

				<view style="width:25%;text-align: center;">昵称</view>
				<view style="width: 50%;text-align: center;">管理</view>
			</view>
			<view class="flex justify-between align-center padding" v-for="(item, index) in list" :key="index"
				style="margin-bottom: 16upx;">
				<view style="width: 15%;">
					<view style="font-size: 28upx;margin-left: 15upx;margin-top: 6upx;">{{ index + 1 }}
					</view>
				</view>
				<view style="width: 25%;display: flex;flex-direction: row;align-items: center;"
					@tap="goGuanZhuDetail(item.userId)">
					<view
						style="font-size: 28upx;width: 90%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
						{{ item.userName }}
					</view>
					<text v-if="item.isAuthentication==1"
						style="font-size: 22upx;color:#fff;background: #525363;margin-left:10upx;border-radius: 33upx;padding: 5upx 15upx;">已实名</text>
				</view>
				<view class="flex align-center justify-end" style="width: 50%;text-align: center;"
					v-if="item.status==2">
					<view class="btn">已同意</view>
				</view>
				<view class="flex align-center justify-end" style="width: 50%;text-align: center;"
					v-if="item.status==3">
					<view class="btn">已拒绝</view>
				</view>
				<view class="flex align-center justify-between" style="width: 50%;text-align: center;"
					v-if="item.status==1">
					<view class="btn " @click="bindJx(item.consortiaApplyId)">拒绝</view>
					<view class="btn" @click="bindTg(item.consortiaApplyId)">同意</view>
				</view>

			</view>
		</view>
		<!-- 公会拒绝弹框 -->
		<u-popup v-model="show" mode="center" width="640rpx" height="520rpx" border-radius="14" :closeable="true"
			close-icon="close-circle" close-icon-size="50" close-icon-color="#CCCCCC" @close="closePopup()">
			<view style="padding-top: 30rpx;">
				<view class="text-xl text-bold text-center">公会管理</view>
				<view style="padding-top: 50upx;">
					<view class="margin-lr padding-lr-sm" style="background: #1E1F31;">
						<u-input v-model="value" type="textarea" placeholder="请输入拒绝理由" height="220" />
					</view>
					<view class="sure" @click="submit()">拒绝</view>
				</view>
			</view>
		</u-popup>
		<empty v-if="list.length === 0" content="暂无申请记录" show="false"></empty>
		
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
				count: 0,
				page: 1,
				limit: 10,
				tabList: [{
					text: '加入申请',
					type: 1
				}, {
					text: '退出申请',
					type: 2
				}],
				type: 1,
				consortiaId: '',
				value: '',
				show: false,

			}
		},
		onLoad(option) {
			uni.showLoading({
				title: '加载中...'
			});
			this.getlist()
		},
		onShow() {
			this.consortiaId = uni.getStorageSync("consortiaId")
		},
		methods: {
			goGuanZhuDetail(userId) {
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + userId + '&id=' + this.id
				});

			},
			changeList(e) {
				this.type = e
				this.page = 1
				this.getlist()
			},
			//同意加入
			bindTg(consortiaApplyId) {
				let data = {
					status: 1,
					ids: consortiaApplyId,
					content: ''
				}
				this.$Request.postT('/app/consortia/auditConsortiaApply', data).then(res => {
					if (res.code == 0) {
						this.page = 1
						this.getlist()
						uni.showToast({
							title: '公会申请已通过！',
							icon: 'none'
						})
					}

				});
			},
			bindJx(consortiaApplyId) {
				this.consortiaApplyId = consortiaApplyId
				this.show = true
			},
			//拒绝加入
			submit() {
				if (!this.value) {
					uni.showToast({
						title: '请填写拒绝理由',
						icon: 'none'
					})
					return
				}
				let data = {
					status: 2,
					ids: this.consortiaApplyId,
					content: this.value
				}
				this.$Request.postT('/app/consortia/auditConsortiaApply', data).then(res => {
					if (res.code == 0) {
						this.show = false
						this.value = ''
						this.page = 1
						this.getlist()
						uni.showToast({
							title: '公会申请已拒绝！',
							icon: 'none'
						})
					}

				});
			},

			getlist() {
				let data = {
					page: this.page,
					limit: this.limit,
					status: 0,
					classify: this.type,
					consortiaId: uni.getStorageSync("consortiaId")
				}
				this.$Request.getT('/app/consortia/selectConsortiaApplyList', data).then(res => {
					if (res.code == 0) {
						if (this.page == 1) {
							this.list = res.data.list

						} else {
							res.data.list.forEach(d => {
								this.list.push(d);
							});
						}
						this.count = res.data.totalCount
					}
					uni.stopPullDownRefresh();
					uni.hideLoading();
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
				this.getlist();
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getlist();
		}
	}
</script>

<style lang="less">
	.view2 {
		background-color: #1E1F31;
		width: 93%;
		margin-left: 26upx;
		// box-shadow: rgba(183, 183, 183, 0.3) 1px 1px 10px 1px;
		border-radius: 20upx;
		margin-bottom: 50upx;
		/* #ifdef H5 */
		margin-top: 100rpx;
		/* #endif */
		/* #ifndef H5 */
		margin-top: 112rpx;
		/* #endif */
	}

	.btn {
		color: #fff;
		background: #AC75FE;
		border-radius: 10rpx;
		padding: 15rpx 0rpx;
		width: 50%;
		margin-left: 15upx;
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
		z-index: 99;


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
				;
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

	.sure {
		width: 90%;
		height: 80rpx;
		background: linear-gradient(90deg, #E6CCA7 0%, #D0B391 100%);
		color: #5D3D17;
		border-radius: 46rpx;
		text-align: center;
		line-height: 80rpx;
		margin: 60rpx auto;
		letter-spacing: 2rpx;
	}
</style>
