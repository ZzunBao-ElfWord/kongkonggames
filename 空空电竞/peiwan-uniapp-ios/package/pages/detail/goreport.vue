<template>
	<view class="bonus-main">
		<view class="list-item" v-if="matchdata.length>0" :show-arrow="false" v-for="(item, index) in matchdata"
			:key="index">
			<view class="list-item-wrap">
				<view v-if="item.image">
					<image v-for="(imgs,index) in item.image" :key="index" :src="imgs" mode="aspectFill"
						:class="item.image.length>1?'img2':'img1'" @click="saveImg(item.image,index)"></image>
					<!-- <image v-for="imgs in item.image" :src="imgs" :class="item.image.length>1?'img2':'img1'"></image> -->
					<!-- <img :src="item.image" style="width: 100%;height: 400rpx;"> -->
				</view>
				<view style="padding: 20rpx 30rpx;">
					<view class="list-title" style="color: #FFFFFF">投诉内容：{{ item.content }}</view>
					<view class="list-title" style="margin-top: 10upx;color: #FFFFFF;">被投诉人：{{ item.byUserName }}</view>
					<view style="display: flex;justify-content: space-between;margin-top: 10rpx;">
						<view>
							<text style="color: #FFFFFF;">投诉时间：{{ item.createAt }}</text>
						</view>
					</view>
					<view class="list-title" style="margin-top: 10upx;color: #FFFFFF;" v-if="item.status==0">状态：待处理
					</view>
					<view class="list-title" style="margin-top: 10upx;color: #FFFFFF;" v-if="item.status==1">状态：已驳回
						<!-- 	<text @click="disposeDelete(item)"
							style="float: right;background-color: rgb(190, 128, 64);font-size: 28rpx;padding: 5rpx 30rpx;border-radius: 12rpx;">删除</text> -->
					</view>
					<view class="list-title" style="margin-top: 10upx;color: #FFFFFF;" v-if="item.status==2">状态：已封号
						<!-- <text @click="disposeDelete(item)"
							style="float: right;background-color: rgb(190, 128, 64);font-size: 28rpx;padding: 5rpx 30rpx;border-radius: 12rpx;">删除</text> -->
					</view>
					<view class="list-title" style="margin-top: 10upx;color: #FFFFFF;" v-if="item.status==3">状态：已删除
						<!-- <text @click="disposeDelete(item)"
							style="float: right;background-color: rgb(190, 128, 64);font-size: 28rpx;padding: 5rpx 30rpx;border-radius: 12rpx;">删除</text> -->
					</view>
					<view class="list-title" style="margin-top: 10upx;color: #FFFFFF" v-if="item.status==1">
						驳回内容：{{ item.auditContent }}</view>
					<view class="list-title" style="margin-top: 10upx;color: #FFFFFF" v-if="item.status==4">
						处理内容：{{ item.auditContent }}</view>
				</view>
			</view>
		</view>
		<!-- 加载更多提示 -->
		<!-- <view class="s-col is-col-24" v-if="matchdata.length > 0">
			<load-more :loadingType="loadingType" :contentText="contentText"></load-more>
		</view> -->
		<!-- 加载更多提示 -->
		<empty v-if="matchdata.length === 0" des="暂无数据"></empty>
	</view>
</template>

<script>
	import empty from '../../../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				page: 1,
				size: 5,
				count:0,
				
				userId: '',
				type: 3,
				loadingType: 0,
				scrollTop: false,
				matchdata: [],
				contentText: {
					contentdown: '上拉显示更多',
					contentrefresh: '正在加载...',
					contentnomore: '没有更多数据了'
				},
				showFlag: true,
			};
		},
		onLoad() {
			// this.mymatch()
		},
		onShow() {
			if (this.showFlag) {
				this.mymatch();
			}
		},
		methods: {
			mymatch() {
				this.loadingType = 1;
				uni.showLoading({
					title: '加载中...'
				});
				let userId = this.$queue.getData("userId");
				this.$Request.getT(`/app/message/selectMessageByUserId?page=${this.page}&limit=${this.size}&state=3`).then(
					res => {
						if (res.code === 0) {
							if (this.page === 0) {
								this.matchdata = [];
							}
							res.data.list.forEach(d => {
								if (d.image) {
									d.image = d.image.split(',');
								}
								this.matchdata.push(d);
							});
							if (res.data.list.length === this.size) {
								this.loadingType = 0
							} else {
								this.loadingType = 3
							}
							this.count = res.data.totalCount
							uni.hideLoading()
						} else {
							this.loadingType = 2;
							uni.hideLoading();
						}
					});
			},
			// 删除
			// disposeDelete(item) {
			// 	let that = this
			// 	that.$Pmessage({
			// 		showModel: true,
			// 		title: '删除提醒',
			// 		content: '确定要删除此条信息？',
			// 		maskcloseAble: false
			// 	}, {
			// 		emitBtn:()=> { //确认
			// 			that.$Request.getT(`/userReport/delete?id=${item.id}`).then(res => {
			// 				if (res.status === 0) {
			// 					that.$queue.showToast('删除成功');
			// 					that.page = 1
			// 					that.matchdata = [];
			// 					that.mymatch()
			// 				}
			// 			});
			// 		},
			// 		emitClose:()=> { //取消
			// 			console.log('用户点击取消');
			// 		},
			// 	});
				
			// },
			// 查看图片
			saveImg(imgs, index) {
				this.showFlag = false; //不允许再次触发onshow
				console.log(imgs)
				let that = this;
				let imgArr = imgs
				// imgArr.push(imgs);
				// //预览图片
				uni.previewImage({
					urls: imgArr,
					current: imgArr[index]
				});
			},
		},
		onReachBottom: function() {
			if(this.count == this.matchdata.length){
			}else{
				this.page = this.page + 1;
				this.mymatch(this.tabIndex);
			}
			
		}
	};
</script>

<style>
	page {
		background-color: #1A1728;
	}

	.list-item {
		background-color: #291B3D;
		border-radius: 16rpx;
		margin: 0 20rpx 20rpx 20rpx;
	}

	.img1 {
		width: 100%;
		height: 400rpx;
	}

	.img2 {
		width: 210rpx;
		height: 210rpx;
		margin-top: 15rpx;
		margin-right: 5rpx;
		margin-left: 15rpx;
	}
</style>
