<template>
	<view>
		<u-modal v-model="showModel" :content="content" :title="title" :show-cancel-button='showCancel'
			@cancel="handleClose" @confirm="handleBtn" :confirm-text='confirmText' :cancel-text='cancelText'
			:mask-close-able='maskcloseAble'></u-modal>
		
	</view>
</template>

<script>
	export default {
		name: "massage",
		data() {
			return {
				showModel: false, //是否显示
				title: '提示', //标题
				content: "", //内容
				showCancel: true, //是否显示取消按钮
				confirmText: '确认', //确认文字
				cancelText: '取消', //取消文字
				maskcloseAble: true, //是否允许点击遮罩关闭Modal
			
			};
		},
		methods: {
			emitBtn(){},
			emitClose(){},
			handleClose() {
				this.$el.parentNode.removeChild(this.$el);
				this.emitClose();
			},
			handleBtn() {
				this.$el.parentNode.removeChild(this.$el);
				this.emitBtn();
			},
		}
	}
</script>

<style lang="scss">

</style>
