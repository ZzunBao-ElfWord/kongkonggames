<template name="Head">
	<view class="head">
		<view class="head_box" v-for="(item,index) in list" :key="item.talkId || item.id || index">
			<view>
				<!-- <u-gap height="20" bg-color="#f2f2f2"></u-gap> -->
				<!-- <view style="height: 20rpx;background: #f2f2f2;"></view> -->
				<view class="head_left">
					<view style="width: 80%;display: flex;" @tap="clickItem(1,index)">
						<view class="head_img">
							<image :src="item.imageUrl ? item.imageUrl : '/static/logo.png'"></image>
						</view>
						<view style="margin-left: 20rpx;">
							<view class="head_name">{{item.nickName}}</view>
							<view class="head_data">{{item.createTime}}</view>
						</view>
					</view>
					<view v-if="item.status == 1" style="width: 20%;text-align: right;font-size: 28upx;color: #AC75FE;">
						审核中
					</view>
					<view v-if="item.status == 1" style="width: 20%;text-align: right;font-size: 28upx;"
						@click="clickItem(5,index)">
						编辑
					</view>
					<view style="width: 20%;text-align: right;font-size: 28upx;color: #AC75FE;"
						v-if="className&&item.status == 2||item.status == 3||item.status == 1"
						@tap="clickItem(4,index)">
						删除
					</view>
				</view>

				<view class="head_conten" @tap="clickItem(0,index)">{{item.content}}</view>

				<view v-if="item.videoUrl" class="margin-lr margin-top-sm">
					<!-- #ifdef APP-PLUS -->
					<PostVideoPreview :item="item" />
					<!-- #endif -->
					<!-- #ifndef APP-PLUS -->
					<video :src="item.videoUrl" style="width: 100%;height: 320rpx;border-radius: 10rpx;"></video>
					<!-- #endif -->
				</view>
				<view class="head_image" @tap="clickItem(0,index)">
					<image v-for="imgs in item.img" :src="imgs" :class="item.img.length>1?'img2':'img1'"
						mode="aspectFill"></image>
				</view>
				<!-- <view class="head_image" @tap="clickItem(0,index)">
					<image v-for="imgs in item.img" :src="imgs" :class="item.img.length>1?'img2':'img1'"
						mode="aspectFit"></image>
				</view> -->
			</view>
			<!-- 分享 评论 点赞  -->
			<view class="user_mk">
				<view class="mk1" @tap="clickItem(3,index)">
					<view class="mk_image">
						<image src="../static/pw/xin.png"></image>
						<!-- <image src="../static/pw/xin.png" v-if="item.goodTalkByUserCount != 1"></image>
						<image src="../static/pw/xinon.png" v-if="item.goodTalkByUserCount == 1"></image> -->
					</view>
					<view class="mk_num">{{item.goodTalkCount ? item.goodTalkCount : 0}}</view>
				</view>
				<view class="mk2" @tap="clickItem(0,index)">
					<view class="mk_image">
						<image src="../static/pw/msg.png"></image>
						<!-- <image src="../static/pw/msg.png" v-if="item.commentByUserCount < 1"></image>
						<image src="../static/pw/msgon.png" v-if="item.commentByUserCount > 0"></image> -->
					</view>
					<view class="mk_num">{{item.commentCount ? item.commentCount : 0}}</view>
				</view>
				<view class="mk3" @tap="clickItem(0,index)">
					<view class="mk_image">
						<image src="../static/pw/shang.png"></image>
						<!-- <image src="../static/pw/shang.png"
							v-if="item.rewardByUserCount < 1"></image>
						<image src="../static/pw/shangon.png"
							v-if="item.rewardByUserCount > 0"></image> -->
					</view>
					<view class="mk_num">{{item.rewardCount ? item.rewardCount : 0}}</view>
				</view>
			</view>
			<!-- 用户评论 -->
			<view class="user_pl" v-if="item.commentPage&&item.commentPage.content">
				<view class="pl" v-for="ite in item.commentPage.content">
					<view class="pl_name">
						{{ite.nickName}}：
					</view>
					<view class="pl_name">{{ite.content}}</view>
				</view>
			</view>
			<view class="padding-left" style="color: red;" v-if="item.status==3&&item.auditContent">
				拒绝理由：{{item.auditContent}}
			</view>
			<!-- 展开更多 -->
			<!-- <view class="gengduo" v-for="item in item.gengduo" :key="item.id">
				{{item.name}}
				<image :src="item.icon"></image>
			</view> -->
		</view>
	</view>
</template>

<script>
	import PostVideoPreview from './PostVideoPreview.vue'
	export default {
		components: { PostVideoPreview },
		props: {
			//背景颜色
			list: {
				type: Array,
				default: []
			},
			className: {
				type: Boolean,
				default: false
			}
		},
		methods: {
			clickItem(ind, item) {
				this.$emit('click', {
					index: ind,
					id: item
				});
			}
		}
	}
</script>

<style>
	page {
		background-color: #1A1728;
	}

	.head {
		width: 100%;
		margin-bottom: 50rpx;
		color: #FFFFFF;
		background: #1A1728
	}

	.head_box {
		/* width: 95%; */
		/* background: #372853; */
		background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/22/9762a6ea960f4016d8a0bbe43a14b3dd.png');
		background-size: 100% 100%;
		background-repeat: no-repeat;
		padding-top: 5rpx;
		padding-bottom: 30rpx;
		/* margin: 20rpx; */
		border-radius: 20rpx;
		margin: 20rpx 30rpx;
		/* margin-bottom: 20upx; */
	}

	.head_left {
		width: 90%;
		margin: 0 auto;
		margin-top: 30rpx;
		display: flex;
	}

	.head_img image {
		width: 90rpx;
		height: 90rpx;
		border-radius: 100rpx;
	}

	.head_name {
		color: #FFFFFF;
		margin-top: 10rpx;
		font-size: 28rpx;
		font-weight: bold;
	}

	.head_data {
		margin-top: 15rpx;
		color: #D7BCFF;
		font-size: 24rpx;
	}

	.head_conten {
		width: 90%;
		margin: 0 auto;
		letter-spacing: 2rpx;
		font-size: 28rpx;
		margin-top: 20rpx;
	}

	.head_image {
		width: 95%;
		margin: 0 auto;
		margin-top: 20rpx;
	}

	.img1 {
		width: 420rpx;
		height: 320rpx;
		border-radius: 8rpx;
	}

	.img2 {
		width: 202rpx;
		height: 202rpx;
		margin-top: 15rpx;
		margin-right: 5rpx;
		margin-left: 15rpx;
	}

	/* 分享 评论 点赞 */
	.user_mk {
		width: 90%;
		margin: 0 auto;
		display: flex;
		margin-top: 20rpx;
	}

	.mk1 {
		flex: 1;
		height: 80rpx;
		display: flex;
		justify-content: left;
		align-items: center;
	}

	.mk2 {
		flex: 1;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.mk3 {
		flex: 1;
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}

	.mk_image {
		width: 40rpx;
		height: 45rpx;
	}

	.mk_image image {
		width: 40rpx;
		height: 40rpx;
	}

	.mk_num {
		color: #999999;
		margin-left: 10rpx;
		margin-top: -8rpx;
	}

	/* 用户评论 */
	.user_pl {
		width: 100%;
		margin-top: 20rpx;
	}

	.pl_name {
		font-size: 24rpx;
		font-family: PingFang SC Bold, PingFang SC Bold-Bold;
		font-weight: 700;
		text-align: left;
		color: #D7BCFF;
		line-height: 32rpx;
	}

	.pl {
		width: 90%;
		margin: 0 auto;
		display: flex;
		justify-content: left;
		align-items: center;
		height: 50rpx;
		font-size: 24rpx;
		color: #999999;
	}

	.gengduo {
		height: 80rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 20rpx;
		color: #999999;
		letter-spacing: 2rpx;
	}

	.gengduo image {
		width: 16rpx;
		height: 10rpx;
	}
</style>
