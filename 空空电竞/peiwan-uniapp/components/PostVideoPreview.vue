<template>
	<view class="post-video-preview" @tap.stop="play">
		<image v-if="poster" class="post-video-poster" :src="poster" mode="aspectFit" />
		<view class="post-video-center">
			<view class="post-video-triangle"></view>
			<text v-if="duration" class="post-video-duration">{{ durationText }}</text>
		</view>
	</view>
</template>
<script>
import { getPostVideoPreview } from '../utils/post-video-preview.js'
export default {
	props: { item: { type: Object, required: true } },
	data() { return { poster: '', duration: 0 } },
	computed: {
		durationText() {
			const seconds = Math.floor(this.duration)
			return String(Math.floor(seconds / 60)).padStart(2, '0') + ':' + String(seconds % 60).padStart(2, '0')
		}
	},
	mounted() {
		this.poster = this.item.videoCover || this.item.coverUrl || this.item.poster || ''
		this.duration = Number(this.item.duration || this.item.videoDuration) || 0
		this._observer = uni.createIntersectionObserver(this)
		this._observer.relativeToViewport({ top: 200, bottom: 200 }).observe('.post-video-preview', result => {
			if (result.intersectionRatio <= 0) return
			this._observer.disconnect()
			getPostVideoPreview(this.item.videoUrl).then(result => {
				if (this._disposed) return
				if (!this.poster && result.poster) this.poster = result.poster
				if (result.duration) this.duration = result.duration
			}).catch(() => { /* 保留播放入口；封面失败不影响播放。 */ })
		})
	},
	beforeDestroy() { this._disposed = true; if (this._observer) this._observer.disconnect() },
	methods: {
		play() { uni.navigateTo({ url: '/pages/video/post-player?src=' + encodeURIComponent(this.item.videoUrl) }) }
	}
}
</script>
<style scoped>
.post-video-preview { position: relative; width: 100%; height: 320rpx; overflow: hidden; border-radius: 10rpx; background: #000; }
.post-video-poster { position: absolute; width: 100%; height: 100%; top: 0; left: 0; }
.post-video-center { position: absolute; top: 0; left: 0; right: 0; bottom: 0; display: flex; flex-direction: column; align-items: center; justify-content: center; }
.post-video-triangle { width: 0; height: 0; border-top: 22rpx solid transparent; border-bottom: 22rpx solid transparent; border-left: 34rpx solid #fff; margin-left: 8rpx; }
.post-video-duration { margin-top: 20rpx; color: #fff; font-size: 28rpx; text-shadow: 0 1px 3px #000; }
</style>
