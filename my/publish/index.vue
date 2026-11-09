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
			<view class="margin-sm padding-sm bg radius " v-for="(item,index) in goods" :key='index'
				@click="goNav('/pages/index/game/myOrder?id=' + item.id + '&userId=' + userId)">
				<view class="flex justify-between  padding-bottom-sm" style="border-bottom: 1rpx solid #2A2A32;">
					<view class="text-mauve" v-if="item.status==0">进行中</view>
					<view class="text-mauve" v-if="item.status==1">待审核</view>
					<view class="text-mauve" v-if="item.status==2">已下架</view>
					<view class="text-mauve" v-if="item.status==3">已拒绝</view>
					<view class="text-white">{{item.updateTime}}</view>
				</view>
				<view class=" u-flex u-p-t-30">
					<!-- <view class="u-m-r-10">
						<u-avatar :src="avatar" mode="square" size="100"></u-avatar>
					</view> -->
					<view class="u-flex-1 text-white margin-left-xs">
						<view class="u-font-16  text-bold flex justify-between">
							<view>{{item.gameName}}</view>
							<view style="color: #FBCA0C;">{{item.oldMoney}}币/局</view>
						</view>
						<view class="u-font-14 u-tips-color margin-top">
							<view class="text-white">接单时间：{{item.orderTakingTime}}</view>
						</view>
						<view class="u-font-14 u-tips-color margin-top " v-if="item.content && item.status == 3">
							<view class="text-red">拒绝理由：{{item.content}}</view>
						</view>
					</view>
				</view>

				<view class="flex justify-end u-p-t-20">
					<u-button v-if="item.status == 1" class="margin-right-sm" :custom-style="customStyle"
						@click="updateStatus(item,2)" shape="circle" :plain="true" hover-class="none">取消审核</u-button>
					<u-button v-if="item.status == 0" class="margin-right-sm" :custom-style="customStyle"
						@click="updateStatus(item,2)" shape="circle" :plain="true" hover-class="none">立即下架</u-button>
					<u-button v-if="item.status == 2 || item.status == 3" class="margin-right-sm"
						:custom-style="customStyle" @click="delStatus(item,1)" shape="circle" :plain="true"
						hover-class="none">删除</u-button>
					<u-button v-if="item.status == 2" class="margin-right-sm" :custom-style="customStyle"
						hover-class="none" @click="updateStatus(item,1)" shape="circle" :plain="true">重新上架</u-button>
					<u-button v-if="item.status != 0" :custom-style="customStyle" shape="circle" :plain="true"
						hover-class="none" @click="update(item.id)">重新编辑</u-button>
				</view>
			</view>
			<u-gap height="140" bg-color="#050506"></u-gap>
			<empty v-if="goods.length == 0"></empty>

		</mescroll-body>
		<view v-if="shangxianSelect!='否'" class="padding-lr-sm"
			style="position: fixed;bottom: 40upx;width: 100%;z-index: 999;">
			<u-button class="" :custom-style="customStyle1" :hair-line="false" hover-class="none"
				@click="goNav('/my/publish/editor',2)">
				<u-icon name="plus" color="#fff" size="30" style="margin-right: 10upx;"></u-icon>
				发布
			</u-button>
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
	</view>
</template>

<script>
	import MescrollMixin from "@/components/mescroll-uni/components/mescroll-uni/mescroll-mixins.js";
	import mescrollBody from "@/components/mescroll-uni/components/mescroll-body/mescroll-body.vue";
	import meTabs from "@/components/mescroll-uni/me-tabs/me-tabs.vue";
	import empty from '../../components/empty.vue'
	import popup from '@/components/popup.vue'
	export default {
		mixins: [MescrollMixin], // 使用mixin
		components: {
			mescrollBody,
			meTabs,
			// orderList
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
					title: '进行中',
					status: '0'
				}, {
					title: '待审核',
					status: '1'
				}, {
					title: '已下架',
					status: '2'
				}, {
					title: '已拒绝',
					status: '3'
				}, ],
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
				},
				customStyle1: {
					backgroundColor: "#FBCA0C",
					border: 0,
					color: "#FFF",
					height: '98rpx'
				},
				renzheng: 0,
				shangxianSelect: '否',
			}
		},
		onLoad() {
			// #ifdef MP-WEIXIN
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			// #endif
			// #ifndef MP-WEIXIN
			this.shangxianSelect = '是';
			// #endif
			this.$queue.showLoading("加载中...");
			this.userId = uni.getStorageSync('userId')
			this.nickName = uni.getStorageSync('nickName')
			this.getRenZheng()
		},
		onShow() {
			this.mescroll.resetUpScroll()
		},
		methods: {
			getRenZheng() {
				this.$Request.get("/app/userCertification/queryInsert").then(res => {
					if (res.code == 0) {
						// 0审核中 1通过 2拒绝 
						if (res.data == null) {
							this.renzheng = 0 //未实名
						} else if (res.data.status == 0) {
							this.renzheng = 1 //审核中
						} else if (res.data.status == 1) {
							this.renzheng = 2 //已实名
						} else if (res.data.status == 2) {
							this.renzheng = 3 //已拒绝
						}
					}
				});
			},
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
				this.$Request.get('/app/orderTaking/selectMyRelease', data).then(res => {
					uni.hideLoading();
					this.mescroll.endBySize(res.data.list.length, res.data.totalCount)
					if (page.num == 1) this.goods = []; //如果是第一页需手动制空列表

					this.goods = [...this.goods, ...res.data.list]; //追加新数据

					this.mescroll.endSuccess(res.data.list.length); // 隐藏加载状态栏
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
			// 下架
			updateStatus(e, status) {
				let data = {
					id: e.id,
					status: status
				}
				this.$Request.get('/app/orderTaking/updateTakingStatus', data).then(res => {
					if (res.code == 0) {
						this.mescroll.resetUpScroll()
					}
				})
			},
			// 重新编辑
			update(e) {
				uni.navigateTo({
					url: '/my/publish/editor?id=' + e
				})
			},
			sureT() {
				let that = this
				if (that.popupIndex == 1) {
					let data = {
						id: that.id
					}
					that.$Request.get('/app/orderTaking/deleteTaking', data).then(res => {
						uni.showToast({
							title: "删除成功"
						})
						that.popupshow = false
						that.mescroll.resetUpScroll()
					})
				} else if (that.popupIndex == 2) {
					uni.navigateTo({
						url: "/my/renzheng/index"
					})
				}
			},
			//删除
			delStatus(e, index) {
				this.popupIndex = index
				this.id = e.id
				this.content = '确定删除吗？'
				this.popupshow = true

			},
			// 完善需求
			edit(item) {
				uni.navigateTo({
					url: '/pages/order/release?type=hasEdit&id=' + item.id + '&content=' + item.content +
						'&site=' + item.site +
						'&phone=' + item.phone + '&deliveryTime=' + item.deliveryTime + '&classifyId=' + item
						.classifyId +
						'&classifyName=' + item.classifyName + '&userType=' + item.userType + '&commission=' + item
						.commission + '&image=' + item.image + '&address=' + item.address + '&latitude=' + item
						.latitude + '&longitude=' + item.longitude
				})
			},
			clickItem: function(e) {
				console.log('点击', e)
				uni.navigateTo({
					url: '/pages/index/game/order?id=' + e.orderTakingId + '&userId=' + e.userId
				});
			},
			goNav(e, index) {
				if (this.renzheng != 2) {
					this.popupIndex = index
					this.content = '您还未认证电竞教练,请先认证'
					this.popupshow = true

					return
				}
				uni.navigateTo({
					url: e
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

	.bg {
		background: #101014;
	}
</style>