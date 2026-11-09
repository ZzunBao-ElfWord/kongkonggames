<template>
	<view class="content">
		<view class="headtop">
			<me-tabs v-model="tabIndex" nameKey='title' :tabs="tabs" @change="tabChange"></me-tabs>
		</view>

		<view class="part2">
			<Head :list="list" @click="clickItem" className="true"></Head>
		</view>
		<view class="part3" v-if="shangxianSelect != '否'" @click="bindfb">
			<image src="../../../static/images/my/dtfb.png"></image>
		</view>

		<!-- 加载更多提示 -->
		<view class="s-col is-col-24" v-if="list.length > 0">
			<load-more :loadingType="loadingType" :contentText="contentText"></load-more>
		</view>
		<!-- 加载更多提示 -->
		<empty v-if="list.length === 0" content="暂无动态信息,快去发布吧!" show="false"></empty>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"
			:title="title"></popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import Head from '../../../components/Head.vue'
	import empty from '../../../components/empty.vue'
	import meTabs from "@/components/mescroll-uni/me-tabs/me-tabs.vue";
	export default {
		components: {
			empty,
			Head,
			meTabs,
			popup
		},
		data() {
			return {
				popupshow: false,
				content: '',
				id: '',
				title: '',

				shangxianSelect: '否',
				renzheng: 0,
				tabIndex: 0,
				tabBars: [{
						name: '全部',
						id: 'hot',
						state: '-1'
					},
					{
						name: '我的关注',
						id: 'yule',
						state: '1'
					}
				],
				gambitId: 0,
				title: '',
				content: '',
				contentImg: '',
				list: [],
				followUserId: 0,
				isEnable: '否',
				scrollTop: false,
				page: 0,
				size: 10,
				loadingType: 0,
				scrollTop: false,
				contentText: {
					contentdown: '上拉显示更多',
					contentrefresh: '正在加载...',
					contentnomore: '没有更多数据了'
				},
				tabs: [{
					title: '全部',
					status: 0
				}, {
					title: '审核中',
					status: 1
				}, {
					title: '已发布',
					status: 2
				}, {
					title: '已拒绝',
					status: 3
				}],
				status: 0
			}
		},
		onLoad(d) {
			// #ifdef MP-WEIXIN
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			// #endif
			// #ifndef MP-WEIXIN
			this.shangxianSelect = '是';
			// #endif
			let a = this.$queue.getData("isEnable")
			if (a) {
				this.isEnable = a;
			}
		},
		onShow() {
			this.page = 0;
			this.getList();
			this.getRenZheng();
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		methods: {
			// 切换菜单
			tabChange(e) {
				// this.goods = []; // 置空列表,显示加载进度条
				console.log(e)
				this.page = 0;
				this.status = e
				this.getList()
			},
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
			sureT() {
				let that = this;
				that.$queue.showLoading('删除中...');
				that.$Request.postT('/app/gambit/deleteTalk?talkId=' + that.id).then(res => {
					if (res.status == 0) {
						uni.hideLoading();
						that.popupshow = false
						that.page = 0;
						that.getList();

					} else {
						uni.hideLoading();
						that.$queue.showToast(res.msg);
					}
				});
			},
			deleteSave(talkId) {
				this.id = talkId
				this.title = '删除提示'
				this.content = '您确认要删除当前动态吗？'
				this.popupshow = true



			},
			topScrollTap: function() {
				uni.pageScrollTo({
					scrollTop: 0,
					duration: 300
				});
			},
			tabClick(e) {
				this.tabIndex = e;
				this.page = 0;
				this.getList();
			},
			clickItem: function(options) {
				console.log(options)
				if (options.index == 0) {
					uni.navigateTo({
						url: '/package/pages/detail/listDetails?talkId=' + this.list[options.id].talkId
					});
				} else if (options.index == 1) {
					let token = this.$queue.getData('token');
					if (token) {
						//去关注页面
						uni.navigateTo({
							url: '/my/gird/guanzhuDetail?userId=' + this.list[options.id].userId
						});

					} else {
						this.goLoginInfo();
					}
				} else if (options.index == 2) {
					// this.getMessageList2();
				} else if (options.index == 3) {
					this.saveLove(this.list[options.id].talkId);
				} else if (options.index == 4) {
					this.deleteSave(this.list[options.id].talkId);
				} else if (options.index == 5) {
					uni.navigateTo({
						url: '/package/pages/releaseone/releaseone?talkId=' + this.list[options.id].talkId
					})
				}
			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/login'
				});
			},
			saveLove(talkId) {
				let userId = this.$queue.getData('userId');
				let data = {
					talkId: talkId,
					userId: userId
				}
				this.$Request.postJson('/app/gambit/saveGoodTalk', data).then(res => {
					if (res.status == 0) {
						this.page = 0;
						this.getList();

					} else {
						this.$queue.showToast(res.msg);
					}
				});
			},
			getList() {
				this.loadingType = 1;
				uni.showLoading({
					title: '加载中...'
				});
				let userId = this.$queue.getData('userId');
				this.$Request.getT('/app/gambit/selectTalkPage?page=' + this.page + '&size=' + this.size + '&byUserId=' +
					userId + '&userId=' + userId + '&followUserId=' + 0 + '&status=' + this.status).then(res => {
					if (res.status == 0) {
						if (this.page == 0 || res.status) {
							this.list = [];
						}
						res.data.list.forEach(d => {
							if (d.img) {
								d.img = d.img.split(',');
							}
							this.list.push(d);
						})
						if (res.data.list.length === this.size) {
							this.loadingType = 0;
						} else {
							this.loadingType = 3;
						}
					} else {
						this.loadingType = 2;
					}
					uni.hideLoading();
				});
			},
			bindfb() {
				let token = this.$queue.getData('token');
				if (token) {

					uni.navigateTo({
						url: '/package/pages/releaseone/releaseone'
					})
				} else {
					this.goLoginInfo();
				}
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getList();
		},
		onPullDownRefresh: function() {
			this.page = 0;
			this.getList();
		}
	}
</script>

<style lang="less">
	@import '../../../static/less/index.less';
	@import '../../../static/css/index.css';

	::v-deep .textMoney {
		text-align: center;
	}

	.content {
		width: 100%;
	}

	.part3 {
		position: fixed;
		bottom: 150rpx;
		right: 30px;
		z-index: 9999;
	}

	.part3 image {
		width: 175rpx;
		height: 175rpx;
	}

	.part2 {
		margin-top: 95rpx;
	}

	.headtop {
		background: #1A1728;
		position: fixed;
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		/* #ifdef H5 */
		top: 84upx;
		/* #endif */
		left: 0;
		right: 0;
		z-index: 999;
	}
</style>