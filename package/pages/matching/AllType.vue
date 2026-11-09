<template>
	<view >
		<view class="box flex flex-wrap " >
			<view class="bb" v-for="(item,index) in list" :key="index"
				@click="bindgame(item.id,item.gameImg,item.gameName)">
				<image :src="item.gameImg?item.gameImg:'../../../static/logo.png'"
					style="width: 90upx;height: 90upx;border-radius: 50upx;">
				</image>
				<view>{{item.gameName}}</view>
			</view>
		</view>
	</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: []
			}
		},
		onLoad() {
			this.getAllType()

		},
		onShow() {

		},
		methods: {
			getAllType() {
				this.$Request.getT('/app/appGame/queryGameName').then(res => {
					console.log(res)
					if (res.code == 0) {
						this.list = res.data
					}
				});
			},
			bindgame(id, gameImg, gameName) {
				// console.log(templateType, '类型')
				uni.navigateTo({
					url: '/package/pages/matching/matching?id=' + id + '&gameName=' + gameName + '&gameImg=' +
						gameImg
				})
				// if (templateType == 1) {
				// 	uni.navigateTo({
				// 		url: '/package/pages/matching/matching?id=' + id + '&gameName=' + gameName + '&gameImg=' +
				// 			gameImg
				// 	})
				// } else if (templateType == 2) {
				// 	uni.navigateTo({
				// 		url: '/package/pages/matching/index?id=' + id + '&gameName=' + gameName + '&gameImg=' +
				// 			gameImg
				// 	})
				// }
			}
		}
	}
</script>

<style>
	page {
		background: #050506;
	}

	.box {
		background-color: #101014;
		margin: 30upx;
		border-radius: 8upx;
		padding: 45upx 30upx;

	}
	.bb{
		width: 33%;
		text-align: center;
		margin-bottom: 30upx;
	}
</style>
