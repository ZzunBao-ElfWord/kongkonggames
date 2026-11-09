<template>
	<view class="content">
		<!-- <view class="box">
			<view class="box-con">
				<view class="box-con-c">
					<view class="box-con-c-item" @click="goNav(item.id,item.gameName)" v-for="(item,index) in typeList"
						:key="index">
						<view class="">
							<image :src="item.gameImg" mode=""></image>
						</view>

						<view class="" style="color: #fff;">
							{{item.gameName}}
						</view>
					</view>
				</view>
			</view>
		</view> -->
		<view class="box" v-for="(item,index) in typeList" :key="index">
			<view class="text-lg text-bold padding-lr" v-if="item.gameType ==1">手游</view>
			<view class="text-lg text-bold padding-lr" v-if="item.gameType ==2">端游</view>
			<view class="text-lg text-bold padding-lr" v-if="item.gameType ==3">其他</view>
			<view class="text-lg text-bold padding-lr" v-if="item.gameType ==4">电影</view>
			<view style="width: 100%;" class="flex flex-wrap">
				<view class="buju" v-for="(game,ind) in item.games" :key="ind">
					<view @click="bindgame(index,ind)">
						<image :src="game.gameImg?game.gameImg:'../../../static/logo.png'"
							style="width: 90upx;height: 90upx;border-radius: 50upx;">
						</image>
						<view>{{game.gameName}}</view>
					</view>

				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				typeList: [],
			};
		},
		onLoad() {
			this.getGrid();
		},
		methods: {
			// 跳转
			bindgame(listIndex, gameIndex) {
				let game = this.typeList[listIndex].games[gameIndex];
				uni.navigateTo({
					url: '/pages/index/game/gameList?gameId=' + game.id + '&name=' + game.gameName
				})
				
			},
			// 获取金刚区分类
			getGrid() {
				let _this = this
				//app/appGame/queryGameName
				this.$Request.get("/app/appGame/selectGameList").then(res => {
					if (res.code == 0) {
						console.log(res.data, '111111111111111')
						_this.typeList = res.data
					}
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #050506;
	}

	.bg {
		background-color: #101014;
	}

	.content {
		width: 100%;
		height: calc(100vh - 88rpx);
		// background-color: #F7F7F7;
	}

	.box {
		// background: linear-gradient(90deg, rgba(234, 134, 189, 0.1) 0%, rgba(162, 112, 248, 0.1) 100%);
		background-color: #101014;
		margin: 30upx;
		border-radius: 8upx;
		padding: 45upx 0upx;

	}

	.buju {
		width: 33%;
		text-align: center;
		margin-top: 60rpx;
	}

	// .box {
	// 	width: 100%;
	// 	display: flex;
	// 	justify-content: center;

	// 	.box-con {
	// 		margin-top: 20rpx;
	// 		width: 686rpx;
	// 		height: 100%;
	// 		background-color: #101014;
	// 		border-radius: 24rpx;
	// 		display: flex;
	// 		justify-content: center;
	// 		align-items: center;
	// 		padding-top: 30rpx;
	// 		padding-bottom: 30rpx;
	// 		flex-wrap: wrap;
	// 		color: #fff;

	// 		.box-con-text {
	// 			font-size: 24rpx;
	// 			color: #999999;
	// 		}

	// 		.box-con-c {
	// 			// width: calc(686rpx - 88rpx);
	// 			width: 100%;
	// 			height: 100%;
	// 			display: flex;
	// 			justify-content: space-between;
	// 			flex-wrap: wrap;

	// 		}

	// 		.box-con-c-item:nth-of-type(1) {
	// 			margin-top: 0;
	// 		}

	// 		.box-con-c-item:nth-of-type(2) {
	// 			margin-top: 0;
	// 		}

	// 		.box-con-c-item:nth-of-type(3) {
	// 			margin-top: 0;
	// 		}

	// 		.box-con-c-item {
	// 			width: 199rpx;
	// 			margin-top: 30rpx;

	// 			// display: flex;
	// 			// justify-content: center;
	// 			// align-items: center;
	// 			// flex-wrap: wrap;
	// 			view {
	// 				width: 100%;
	// 				display: flex;
	// 				justify-content: center;
	// 			}

	// 			// view:nth-of-type(2){
	// 			// 	margin-top: 20rpx;
	// 			// }
	// 			font-size: 24rpx;

	// 			image {
	// 				width: 120rpx;
	// 				height: 120rpx;
	// 			}
	// 		}
	// 	}
	// }
</style>
