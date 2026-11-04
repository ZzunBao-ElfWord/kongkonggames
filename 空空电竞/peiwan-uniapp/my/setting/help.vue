<template>
	<view style="font-size: 14px;line-height: 26px;padding: 32upx;background: #1E1F31;color: #FFFFFF;" class="home1">
		<view style="color: #FFFFFF;font-size: 28upx;" v-html="content" ></view>
	</view>
</template>

<script>
	export default {
	    data() {
	        return {
				content:''
			}
	    },
		onLoad() {
			this.getGuize();
		},
	    methods: {
			getGuize(){
				this.$Request.getT('/app/common/type/175').then(res =>{
					if(res.code === 0){
						this.content = res.data.value;
					}
				});
			}
		}
	}
</script>

<style>
	
</style>
