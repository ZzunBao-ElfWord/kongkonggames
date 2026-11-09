<!-- 菜单悬浮的原理: 通过给菜单添加position:sticky实现, 用法超简单, 仅APP端的低端机不兼容 https://caniuse.com/#feat=css-sticky -->
<template>
	<view>
		<!-- 对于mescroll-body: 需设置:sticky="true", 此应避免在mescroll-body标签前面加其他非定位的元素, 否则下拉区域会被挤出, 无法会隐藏.-->
		<!-- 对于mescroll-uni: 则无需设置:sticky="true", 无其他限制和要求 -->

		<!-- sticky吸顶悬浮的菜单, 父元素必须是 mescroll -->
		<view class="sticky-tabs">
			<me-tabs v-model="tabIndex" nameKey='title' :tabs="tabs" @change="tabChange"></me-tabs>
		</view>
		<mescroll-body :sticky="true" ref="mescrollRef" @init="mescrollInit" @down="downCallback" @up="upCallback">
			<!-- 数据列表 -->
			<view v-if="goods.length > 0" class="margin-sm padding-sm bg radius" v-for="(item,index) in goods"
				:key='index' @click="clickItem(item)">
				<view class="flex justify-between">
					<view class="text-mauve">{{item.statusName}}</view>
					<view class="text-white">{{item.updateTime}}</view>
				</view>
				<view class=" u-flex u-p-t-30">
					<view class="u-m-r-10">
						<u-avatar :src="item.avatar" mode="square" size="100">
						</u-avatar>
					</view>
					<view class="u-flex-1 text-white margin-left-xs">
						<view class="u-font-18  text-bold">{{item.userName}}</view>
						<view class="u-font-14 margin-top-xs u-tips-color flex justify-between">
							<view class="text-white">{{item.gameName}}/{{item.orderNumber}}局</view>
							<view class="text-white">
								实付：<text class="text-lg">{{item.payMoney}}币</text>
							</view>
						</view>
					</view>
				</view>
				<view class="flex justify-end u-p-t-20">
					<u-button v-if="item.state == 0" :custom-style="customStyle" shape="circle" :plain="true"
						hover-class="none" @click="cancelOrder(item,2)">取消订单</u-button>
					<u-button v-if="item.state == 0" :custom-style="customStyle" shape="circle" :plain="true"
						hover-class="none" @click="goNav('/my/order/pay?id='+item.ordersId+'&isTrue=0')">去支付</u-button>
					<u-button v-if="item.state == 1||item.state == 4||item.state == 2" :custom-style="customStyle"
						hover-class="none" shape="circle" :plain="true"
						@click="goNav('/my/order/pay?id='+item.ordersId+'&isTrue=1')">查看详情
					</u-button>
					<u-button v-if="item.state == 4" :custom-style="customStyle" shape="circle" :plain="true"
						hover-class="none" @click="cancel(item,1)">订单完成</u-button>
					<u-button v-if="item.state == 2 && item.commentCount == 0" :custom-style="customStyle"
						hover-class="none" shape="circle" :plain="true"
						@click="goNav('/pages/index/game/orderDet?id=' + item.orderTakingId + '&num='+ item.orderNumber)">
						再来一单
					</u-button>
					<u-button v-if="item.state == 2 && item.commentCount == 0" :custom-style="customStyle"
						shape="circle" :plain="true" hover-class="none"
						@click="goNav('/my/order/feedback?id='+item.orderTakingId+ '&ordersId='+item.ordersId)">去评价
					</u-button>
					<u-button v-if="item.state == 3" :custom-style="customStyle" shape="circle" :plain="true"
						hover-class="none" @click="delOrder(item,4)">删除订单</u-button>
					<u-button v-if="item.state == 1" :custom-style="customStyle" shape="circle" :plain="true"
						hover-class="none" @click="cancel(item,3)">取消订单</u-button>
					<u-button v-if="item.state == 4" :custom-style="customStyle" shape="circle" hover-class="none"
						@click="goMsg(item)">
						联系TA</u-button>
				</view>
			</view>
			<empty v-if="goods.length == 0"></empty>

			<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content">
			</popup>
		</mescroll-body>
	</view>
</template>

<script>
	import MescrollMixin from "@/components/mescroll-uni/components/mescroll-uni/mescroll-mixins.js";
	import mescrollBody from "@/components/mescroll-uni/components/mescroll-body/mescroll-body.vue";
	import meTabs from "@/components/mescroll-uni/me-tabs/me-tabs.vue";
	import empty from '@/components/empty.vue'
	import popup from '@/components/popup.vue'
	export default {
		mixins: [MescrollMixin], // 使用mixin
		components: {
			mescrollBody,
			meTabs,
			empty,
			popup
		},
		data() {
			return {
				popupshow: false,
				content: '',
				id: '',
				popupIndex: '',

				goods: [], // 数据列表
				tabs: [{
					title: '全部',
					status: ''
				}, {
					title: '待付款',
					status: '0'
				}, {
					title: '待服务',
					status: '1'
				}, {
					title: '进行中',
					status: '4'
				}, {
					title: '已完成',
					status: '2'
				}],
				tabIndex: 0, // tab下标

				page: 1,
				limit: 10,
				userId: 0,
				status: 1,
				nickName: '',
				avatar: '',
				customStyle: {
					color: '#FBCA0C',
					backgroundColor: '#101014',
					border: "8rpx",
					width: '180rpx',
					height: '54rpx',
					margin: "0 0 0 20rpx"
				}
			}
		},
		onLoad(option) {
			console.log(option)
			if (option.index) {
				this.tabIndex = option.index
			} else {
				this.tabIndex = 0
			}

			this.$queue.showLoading("加载中...");
			this.userId = uni.getStorageSync('userId')
			this.nickName = uni.getStorageSync('nickName')
			this.mescroll.resetUpScroll()
		},
		onShow() {
			this.mescroll.resetUpScroll()
		},
		methods: {
			/*下拉刷新的回调 */
			downCallback() {
				// 这里加载你想下拉刷新的数据, 比如刷新轮播数据
				// loadSwiper();
				// 下拉刷新的回调,默认重置上拉加载列表为第一页 (自动执行 page.num=1, 再触发upCallback方法 )
				this.mescroll.resetUpScroll()
			},
			/*上拉加载的回调: 其中page.num:当前页 从1开始, page.size:每页数据条数,默认10 */
			upCallback(page) {
				let curTab = this.tabs[this.tabIndex].status

				let data = {
					status: curTab,
					page: page.num,
					limit: page.size
				}
				this.$Request.get('/app/orders/selectMyOrder', data).then(res => {
					this.mescroll.endBySize(res.data.list.length, res.data.totalCount)
					if (page.num == 1) this.goods = []; //如果是第一页需手动制空列表
					this.goods = [...this.goods, ...res.data.list]; //追加新数据
					this.goods.forEach(ret => {
						switch (ret.state) {
							case '0':
								ret.statusName = '待付款'
								break;
							case '1':
								ret.statusName = '待服务'
								break;
							case '4':
								ret.statusName = '进行中'
								break;
							case '2':
								ret.statusName = '已完成'
								break;
							case '3':
								ret.statusName = '已取消'
								break;
						}
					})
					this.mescroll.endSuccess(res.data.list.length); // 隐藏加载状态栏
					uni.hideLoading();
				}).catch(() => {
					//联网失败, 结束加载
					this.mescroll.endErr();
				});
			},
			// 切换菜单
			tabChange() {
				this.goods = []; // 置空列表,显示加载进度条
				this.mescroll.resetUpScroll()
			},
			sureT() {
				let that = this
				if (that.popupIndex == 2) { //取消订单
					//检查订单状态
					that.$Request.get('/app/orders/queryOrders', {
						id: that.id
					}).then(res => {
						if (res.code == 0) {
							that.popupshow = false
							let order = res.data
							switch (order.state) {
								case '1': //进行中
									that.changeOrderState(that.id, '3');
									break;
								case '0': //进行中
									that.changeOrderState(that.id, '3');
									break;
								case '2': //完成了
									that.$queue.showToast('订单已完成啦')
									that.mescroll.resetUpScroll()
									break;
								case '3': //取消了
									that.$queue.showToast('订单已被取消')
									that.mescroll.resetUpScroll()
									break;
							}

						}
					})
				} else if (that.popupIndex == 1 || that.popupIndex == 3) {
					//检查订单状态
					that.$Request.get('/app/orders/queryOrders', {
						id: that.id
					}).then(ret => {
						if (ret.code == 0) {
							that.popupshow = false
							console.log(ret, '彻底')
							let order = ret.data
							console.log(order.state, '彻底')
							switch (order.state) {
								case '1': //待服务
									that.changeOrderState(that.id, '3');
									break;
								case '4': //进行中
									that.changeOrderState(that.id, '2');
									break;
								case '2': //完成了
									that.$queue.showToast('很抱歉订单已完成啦，无法操作')
									that.mescroll.resetUpScroll()
									break;
								case '3': //取消了
									that.$queue.showToast('很抱歉订单已被取消，无法操作')
									// that.changeOrderState(that.id, '3');
									that.mescroll.resetUpScroll()
									break;
								default:
									that.mescroll.resetUpScroll()
									break;
							}

						}
					})
				} else if (that.popupIndex == 4) {
					let data = {
						id: that.id,
					}
					that.$Request.get('/app/orders/deleteOrder', data).then(res => {
						if (res.code == 0) {
							that.id = ''
							that.popupshow = false
							uni.showToast({
								title: "删除成功"
							})
							that.mescroll.resetUpScroll()
						}
					})
				}
			},
			// 取消订单
			cancelOrder(e, index) {
				this.id = e.ordersId
				this.popupIndex = index
				this.content = '确认取消订单吗?'
				this.popupshow = true

			},
			// 完成订单
			cancel(e, index) {
				this.id = e.ordersId
				this.popupIndex = index
				let data = ''
				if (index == 1) {
					data = "确认完成订单吗?"
				} else if (index == 3) {
					data = "确认取消订单吗?"
				}
				this.content = data
				this.popupshow = true

			},
			//统一处理订单状态
			changeOrderState(id, status) {
				let that = this;
				let data = {
					id,
					status
				}
				that.$Request.get('/app/orders/cancelOrder', data).then(res => {
					if (res.code == 0) {
						that.mescroll.resetUpScroll()
					} else {
						that.$queue.showToast(res.msg)
					}
				})
			},
			//删除
			delOrder(e, index) {
				this.id = e.ordersId
				this.popupIndex = index
				this.content = '确定删除订单吗?'
				this.popupshow = true

			},
			clickItem: function(e) {
				console.log('点击', e)
				uni.navigateTo({
					url: '/my/order/pay?id=' + e.ordersId + '&isTrue=1'
				})
				// uni.navigateTo({
				// 	url: '/pages/index/game/order?id=' + e.orderTakingId + '&userId=' + e.userId
				// });
			},
			goMsg(options) {
				if (options.userId) {
					let data = {
						userId: this.userId,
						focusedUserId: options.ordersUserId
					}
					this.$Request.postJson('/app/chat/insertChatConversation ', data).then(res => {
						if (res.code == 0) {
							let id = this.userId == res.data.userId ? res.data.focusedUserId : this.userId
							uni.navigateTo({
								url: '/pages/msg/chat?chatConversationId=' + res.data.chatConversationId +
									'&byUserId=' + id + '&byNickName=' + options.userName
							})
						}
					})
				} else {
					uni.showToast({
						title: '服务订单已下架',
						icon: 'none'
					})
				}

			},
			goNav(url) {
				uni.navigateTo({
					url
				})
			}
		}
	}
</script>

<style lang="scss">
	::v-deep .textMoney {
		text-align: center;
	}

	/*
	sticky生效条件：
	1、父元素不能overflow:hidden或者overflow:auto属性。(mescroll-body设置:sticky="true"即可, mescroll-uni本身没有设置overflow)
	2、必须指定top、bottom、left、right4个值之一，否则只会处于相对定位
	3、父元素的高度不能低于sticky元素的高度
	4、sticky元素仅在其父元素内生效,所以父元素必须是 mescroll
	*/
	.sticky-tabs {
		z-index: 990;
		position: sticky;
		top: var(--window-top);
		// background-color: #fff;
	}

	// 使用mescroll-uni,则top为0
	.mescroll-uni,
	::v-deep .mescroll-uni {
		.sticky-tabs {
			top: 0;
		}
	}

	.demo-tip {
		padding: 18upx;
		font-size: 24upx;
		text-align: center;
	}
</style>