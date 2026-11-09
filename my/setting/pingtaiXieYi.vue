<template>
	<view style="line-height: 26px;padding: 32upx;background: #050506;" class="home1">
		<view style="color: #FFFFFF;font-size: 28upx;" v-html="content"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				content: '',
				id:''
			}
		},
		onLoad(d) {
			if (d.id) {
				this.id = d.id;
			}

			if (d.name) {
				uni.setNavigationBarTitle({
					title: '' + d.name
				})
			}

			this.getGuize();
		},
		methods: {
			getGuize() {
				this.$Request.getT('/app/common/type/' + this.id).then(res => {
					if (res.code === 0) {
						this.content = res.data.value;
					}
				});
			}
		}
	}
</script>

<style>
	page {
		background-color: #050506;
	}
</style>