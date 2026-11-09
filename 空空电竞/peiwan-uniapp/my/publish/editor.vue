<template>
	<view class="padding" v-if="shangxianSelect!='否'">
		<view class="text-white padding bg radius">
			<u-form :model="form" ref="uForm" label-position="top" :label-style='lableStyle'>
				<!-- <u-form-item label="陪玩类型">
					<u-radio-group v-model="form.classify" @change="radioGroupChange" :width="radioCheckWidth" :wrap="radioCheckWrap">
						<u-radio shape="circle" v-for="(item, index) in radioList" :key="index" :name="item.disabled">{{ item.name }}</u-radio>
					</u-radio-group>
				</u-form-item> -->
				<u-form-item label="陪玩分类">
					<u-input v-model="game" disabled placeholder="可商议" @click="show = true" />
				</u-form-item>
				<u-form-item label="我的段位">
					<u-input v-model="form.myLevel" placeholder="请填写（必填）" />
				</u-form-item>
				<u-form-item label="接单段位">
					<u-input v-model="form.orderLevel" placeholder="请填写（选填）" />
				</u-form-item>
				<u-form-item label="接单时间">
					<view class="flex text-white">
						<view @click="isShowTime = true">{{startTime}}</view>
						<view class="margin-lr">~</view>
						<view @click="isEndTime = true">{{endTime}}</view>
					</view>
				</u-form-item>
				<u-form-item label="接单大区">
					<u-input v-model="form.orderTakingArea" placeholder="请填写（选填）" />
				</u-form-item>
				<u-form-item label="接单价格">
					<u-input v-model="form.money" type="number" placeholder="请填写（必填）" />
				</u-form-item>
				<u-form-item label="接单单位">
					<u-radio-group v-model="form.unit" @change="jiedanChange">
						<u-radio shape="circle" active-color="#AC75FE" v-for="(item, index) in jiedanList" :key="index"
							:name="item.name">
							{{ item.name }}
						</u-radio>
					</u-radio-group>
				</u-form-item>
			</u-form>
		</view>


		<view class="text-white padding bg radius margin-top">
			<u-form :model="form" ref="uForm" label-position="top" :label-style='lableStyle'>
				<u-form-item label="语音介绍" :border-bottom='false'>
					<textarea class="text-grey" disabled style="height: 150rpx;"
						placeholder="录制一段自我介绍的语音(介绍自己擅长位置，英雄，游戏成就 等皆可)"></textarea>
				</u-form-item>
			</u-form>
			<jsfun-record :maxTime="60" :minTime="5" @okClick="saveRecord" @okClickdata="okClickdata" @start='select'
				v-if="status == 1">
				<view class="flex justify-between text-center margin-top">
					<view class="padding-top">
						<!-- <image style="width: 120rpx;height: 120rpx;" src="../static/reset.png"></image> -->
					</view>
					<view>
						<view>
							<image style="width: 184rpx;height: 184rpx;" src="../static/luyin.png"></image>
						</view>
					</view>
					<view class="padding-top">
						<!-- <image style="width: 120rpx;height: 120rpx;" src="../static/confirm.png"></image> -->
					</view>
				</view>
				<view class="flex justify-between text-center">
					<view style="width: 120rpx;"></view>

					<view style="width: 184rpx;">点击录制</view>

					<view style="width: 120rpx;"></view>
				</view>
			</jsfun-record>
			<view class="flex justify-between" v-if="status == 2">
				<view class="u-rela" style="width: 360rpx;" @click="playVoice">
					<image src="../static/15.png" style="width: 360rpx;height: 78rpx;"></image>
					<view class="flex justify-between u-abso"
						style="width: 326rpx;height: 58rpx;line-height: 58rpx;top:10rpx;left: 0;right: 0;margin: auto;">
						<image v-if="isPlay" src="../static/stop1.png" style="width: 58rpx;height: 58rpx;"></image>
						<image v-else src="../static/play1.png" style="width: 58rpx;height: 58rpx;"></image>
						<image src="../static/yinpin.png" style="width: 185rpx;height: 58rpx;"></image>
						<view>{{recordLength?recordLength+'S':'未知'}}</view>
					</view>
				</view>
				<u-icon @click="clearAudio()" name="close" size="40"></u-icon>
			</view>
		</view>


		<view class="text-white padding bg radius margin-tb">
			<view class="text-lg">上传主页图(可添加技能图/个人照片)</view>
			<view class="flex flex-wrap">
				<view v-if="homepageImg.length">
					<view class="margin-top flex margin-right-sm flex-wrap">
						<view class="flex" style="width: 200rpx;height: 200rpx;margin-right: 2rpx;position: relative;"
							v-for="(image,index) in homepageImg" :key="index">
							<image :src="image" style="width: 100%;height: 100%;"></image>
							<view style="z-index: 9;position: absolute;top: -15rpx;right: -15rpx;"
								@click="removeImg(index)">
								<u-icon name="close-circle-fill" color="#AC75FE" size="50rpx"></u-icon>
							</view>

						</view>
					</view>
				</view>
				<view class="margin-top" @click="addImages()" v-if="homepageImg.length<5">
					<view style="width: 200rpx;height: 200rpx;background: #362f5b;border-radius: 14upx;"
						class="flex justify-center align-center">
						<view>
							<view class="text-center">
								<image src="../static/photo.png" style="width: 65rpx;height: 55rpx;">
								</image>
							</view>
							<view class="text-center text-white">添加图片</view>
						</view>
					</view>
				</view>
			</view>
			<view class="margin-top">
				<view class="text-lg">个人简介</view>
				<view class="padding-lr-sm padding-tb-xs margin-top-sm"
					style="background: #362f5b;border-radius: 14upx;">
					<u-input v-model="form.details" type="textarea" placeholder="请介绍您的技能水平、技术特长、个人风格等（至少50 个字）"
						height="180" maxlength="300" />
				</view>
			</view>
			<!-- <view class="margin-top" v-if="homepageImg.length>0" v-for="(item,index) in homepageImg" :key="index" @click="addImage(item.index)"
				style="border: 2rpx dashed #484B74; width: 100%;height: 320rpx;position: relative;">
				<image :src="item" style="width: 100%;height: 320rpx;"></image>
			</view>
			<view class="margin-top"
				style="border: 2rpx dashed #484B74; width: 100%;height: 320rpx;position: relative;" v-if="homepageImg.length<5">
				<view style="text-align: center;margin: 80rpx auto 0;" @click="addImages" >
					<image src="../static/photo.png" mode="widthFix" style="width: 73rpx;"></image>
					<view class="text-sm text-gray margin-top-sm">添加图片(可添加技能图/个人照片)</view>
				</view>
			</view> -->
		</view>
		<view class="padding-lr-lg flex align-center" style="padding: 0rpx 30rpx;">
			<u-checkbox active-color="#AC75FE" shape="circle" v-model="checked"></u-checkbox>
			<view @click="checked = !checked" class="text-sm flex align-center flex-wrap"
				style="color: #5F6285;width: 600rpx;margin-left: 10rpx;">
				同意并遵守
				<view style="color: #AC75FE;" @click.stop="goNav('/my/setting/pingtaiXieYi?name=平台行为守则&id=822')">
					《平台行为守则》
				</view>
				<view style="color: #AC75FE;" @click.stop="goNav('/my/setting/pingtaiXieYi?name=平台服务规范&id=823')">
					《平台服务规范》</view>
				<view style="color: #AC75FE;" @click.stop="goNav('/my/setting/pingtaiXieYi?name=游戏上分服务保障&id=824')">
					《游戏上分服务保障》</view>
			</view>
		</view>
		<u-select v-model="show" mode="single-column" :list="gameList" @confirm="selConfirm"></u-select>

		<u-picker v-model="isShowTime" mode="time" zIndex='999999' :params="params" @confirm="statusChange"></u-picker>
		<u-picker v-model="isEndTime" mode="time" zIndex='999999' :params="params" @confirm="endChange"></u-picker>

		<u-button v-if="id" @click="update" class="margin-top" :custom-style="customStyle" shape="circle"
			:hair-line="false" hover-class="none">发布</u-button>
		<u-button v-else @click="submit" class="margin-top" :custom-style="customStyle" shape="circle"
			:hair-line="false" hover-class="none">发布</u-button>

		<!-- <u-action-sheet :list="actionSheetList" v-model="actionSheetShow" @click="actionSheetCallback"></u-action-sheet> -->
	</view>
	<view v-else>
		功能开发中，请等待
	</view>
</template>

<script>
	import configdata from '@/common/config.js';
	import jsfunRecord from '../components/jc-record/jc-record.vue'

	export default {
		components: {
			jsfunRecord
		},
		data() {
			return {
				checked: false,
				time: '',
				startTime: '开始时间（必填）',
				startHour: '',
				endTime: '结束时间（必填）',
				endHour: '',
				game: '',
				isShowTime: false,
				isEndTime: false,
				params: {
					year: false,
					month: false,
					day: false,
					hour: true,
					minute: true,
				},
				form: {
					classify: 1,
					// game: '',
					gameId: '',
					myLevel: '',
					orderLevel: '',
					orderTakingTime: '',
					orderTakingArea: '',
					money: '',
					voiceIntroduce: '',
					sec: 0,
					homepageImg: '',
					longitude: '',
					latitude: '',
					city: '',
					id: 0,
					details: '',
					unit: '单',
					label: ''
				},
				radioList: [{
						name: '线上',
						checked: true,
						disabled: 1
					},
					{
						name: '线下',
						checked: false,
						disabled: 2
					}
				],
				jiedanList: [{
						name: '单',
						checked: true,
						disabled: 1
					},
					{
						name: '局',
						checked: false,
						disabled: 2
					},
					{
						name: '小时',
						checked: false,
						disabled: 3
					},
					{
						name: '天',
						checked: false,
						disabled: 4
					}
				],
				lableStyle: {
					color: '#FFFFFF',
					fontSize: '32rpx'
				},
				customStyle: {
					backgroundColor: '#AC75FE',
					color: '#FFFFFF',
					border: 0
				},
				status: 1,

				//录音相关参数
				// #ifndef H5
				//H5不能录音
				// #endif

				recording: false,
				willStop: false,

				recordLength: 0, //录音时长

				show: false,
				gameList: [],

				number: 0,
				homepageImg: [],
				id: '',
				AUDIO: uni.createInnerAudioContext(),
				isPlay: false,
				shangxianSelect: '否',
			}
		},
		onLoad(option) {
			// #ifdef MP-WEIXIN
			this.shangxianSelect = this.$queue.getData('shangxianSelect');
			// #endif
			// #ifndef MP-WEIXIN
			this.shangxianSelect = '是';
			// #endif
			this.getGameList()
			if (option.id) {
				this.id = option.id
				this.form.id = option.id
			}
			this.form.city = uni.getStorageSync('city')
			this.form.latitude = uni.getStorageSync('latitude')
			this.form.longitude = uni.getStorageSync('longitude')
			this.getData(this.id)

		},
		onShow() {
			// this.getData(this.id)
		},
		onReady() {
			this.AUDIO.onEnded(function(res) {
				this.isPlay = false;
			});
		},
		onUnload() {
			if (this.isPlay) {
				this.AUDIO.stop();
			}
			this.isPlay = !this.isPlay;
		},
		methods: {
			goNav(url) {
				uni.navigateTo({
					url: url
				})
			},
			okClickdata(data) {
				let sec = data % 60;
				// #ifdef H5
				this.form.sec = data
				this.recordLength = data
				// #endif
			},
			// 详情图删除
			removeImg(index) {
				this.homepageImg.splice(index, 1)
			},
			playVoice() {
				console.log(this.isPlay)
				this.AUDIO.src = this.form.voiceIntroduce;
				if (this.isPlay) {
					this.AUDIO.stop();
				} else {
					this.AUDIO.play();
				}
				this.isPlay = !this.isPlay;
			},
			// radio选择发生变化
			radioGroupChange(e) {
				this.form.classify = e;
			},
			jiedanChange(e) {
				this.form.unit = e;
			},
			saveRecord(recordPath) {
				console.log("===音频文件地址：" + recordPath + "===")
				uni.showLoading({
					title: '录音上传中...'
				})
				console.log(this.config("APIHOST1") + '/alioss/uploadMusic')
				let that = this
				uni.uploadFile({
					url: this.config("APIHOST1") + '/alioss/uploadMusic', //仅为示例，非真实的接口地址

					// #ifdef H5
					file: recordPath,
					// #endif
					// #ifndef H5
					filePath: recordPath,
					// #endif
					name: 'file',
					success: (uploadFileRes) => {
						console.log(JSON.parse(uploadFileRes.data))
						that.form.voiceIntroduce = JSON.parse(uploadFileRes.data).data.url
						// #ifndef H5
						that.form.sec = JSON.parse(uploadFileRes.data).data.sec
						that.recordLength = JSON.parse(uploadFileRes.data).data.sec
						// #endif
						uni.hideLoading()
					},
					fail: (err) => {
						console.log(err)
						uni.hideLoading()
					}
				});
			},
			select(e) {
				this.status = e
			},
			statusChange(e) {
				this.startHour = e.hour
				this.startTime = e.hour + ':' + e.minute
			},
			endChange(e) {
				// if (this.startHour >= e.hour) {
				// 	uni.showToast({
				// 		title: '结束时间必须大于开始时间一个小时以上',
				// 		icon: "none"
				// 	})
				// 	return
				// }
				this.endHour = e.hour

				this.endTime = e.hour + ':' + e.minute
				// this.form.orderTakingTime = this.startTime + '~' + this.endTime
			},
			// 图片上传
			addImages(index) {
				let that = this
				// let arr = []
				let count = 5 - that.homepageImg.length
				// console.log(count,'countcountcountcount')
				uni.chooseImage({
					count: count,
					sourceType: ['album', 'camera'],
					success: res => {
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							that.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								url: that.config("APIHOST1") + '/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									that.homepageImg.push(JSON.parse(uploadFileRes.data).data)
									uni.hideLoading();
								}
							});
						}
					}
				})
			},

			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
					if (name2.length > 1) {
						info = configdata[name2[0]][name2[1]] || null;
					} else {
						info = configdata[name] || null;
					}
					if (info == null) {
						let web_config = cache.get("web_config");
						if (web_config) {
							if (name2.length > 1) {
								info = web_config[name2[0]][name2[1]] || null;
							} else {
								info = web_config[name] || null;
							}
						}
					}
				}
				return info;
			},
			// 发布
			submit() {
				this.form.homepageImg = this.homepageImg
				this.form.homepageImg = this.form.homepageImg.toString()
				console.log(this.form)
				if (!this.form.gameId) {
					uni.showToast({
						title: '请选择陪练游戏',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.form.myLevel) {
					uni.showToast({
						title: '请填写我的段位',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.form.money) {
					uni.showToast({
						title: '请填写接单价格',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.form.homepageImg) {
					uni.showToast({
						title: '请上传主页图',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (this.startTime == '开始时间（必填）') {
					uni.showToast({
						title: '请选择开始时间',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (this.endTime == '结束时间（必填）') {
					uni.showToast({
						title: '请选择结束时间',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (this.startHour >= this.endHour) {
					uni.showToast({
						title: '结束时间必须大于开始时间一个小时以上',
						icon: "none"
					})
					return
				}
				if (!this.checked) {
					uni.showToast({
						title: '请阅读并同意协议',
						icon: 'none'
					})
					return
				}
				this.form.orderTakingTime = this.startTime + '~' + this.endTime
				this.$Request.get("/app/orderTaking/insertOrderTaking", this.form).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
			// 重新编辑
			update() {
				this.form.homepageImg = this.homepageImg
				this.form.homepageImg = this.form.homepageImg.toString()
				console.log(this.form)
				if (!this.form.gameId) {
					uni.showToast({
						title: '请选择陪练游戏',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.form.myLevel) {
					uni.showToast({
						title: '请填写我的段位',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.form.money) {
					uni.showToast({
						title: '请填写接单价格',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.form.homepageImg) {
					uni.showToast({
						title: '请上传主页图',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (this.startTime == '开始时间（必填）') {
					uni.showToast({
						title: '请选择开始时间',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (this.endTime == '结束时间（必填）') {
					uni.showToast({
						title: '请选择结束时间',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (this.startHour >= this.endHour) {
					uni.showToast({
						title: '结束时间必须大于开始时间一个小时以上',
						icon: "none"
					})
					return
				}
				if (!this.checked) {
					uni.showToast({
						title: '请阅读并同意协议',
						icon: 'none'
					})
					return
				}
				this.form.orderTakingTime = this.startTime + '~' + this.endTime
				this.$Request.get("/app/orderTaking/updateTakingOrder", this.form).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '编辑成功',
							icon: 'none'
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					}
				});
			},
			// 获取游戏列表
			getGameList() {
				this.$Request.get("/app/appGame/queryGameName").then(res => {
					if (res.code == 0) {
						res.data.forEach(res => {
							res.label = res.gameName
							res.value = res.id
						})
						this.gameList = res.data
					}
				});
			},
			// 选择游戏
			selConfirm(e) {
				// console.log(e);
				this.game = e[0].label
				this.form.gameId = e[0].value
			},
			getData(e) {
				let data = {
					id: e
				}
				this.$Request.get("/app/orderTaking/queryTakingOrder", data).then(res => {
					if (res.code == 0) {
						this.form.unit = res.data.unit
						this.form.classify = res.data.classify
						this.game = res.data.game.gameName
						this.form.gameId = res.data.gameId
						this.form.myLevel = res.data.myLevel
						this.form.orderLevel = res.data.orderLevel
						this.form.orderTakingTime = res.data.orderTakingTime ? res.data.orderTakingTime : ''
						this.form.orderTakingArea = res.data.orderTakingArea
						this.form.money = res.data.oldMoney
						this.form.voiceIntroduce = res.data.voiceIntroduce
						this.recordLength = res.data.sec ? res.data.sec : ''
						if (res.data.homepageImg) {
							this.homepageImg = res.data.homepageImg.split(',')
						}
						this.form.label = res.data.label
						this.form.details = res.data.details
						this.startTime = this.form.orderTakingTime.split('~')[0] ? this.form.orderTakingTime.split(
							'~')[0] : '开始时间（必填）'
						this.endTime = this.form.orderTakingTime.split('~')[1] ? this.form.orderTakingTime.split(
							'~')[1] : '结束时间（必填）'
						let data = this.startTime.split(':');
						this.startHour = data[0];
						let data1 = this.endTime.split(':');
						this.endHour = data1[0];
						// console.log(this.startTime.split(':'));
						if (res.data.voiceIntroduce) {
							this.status = 2
						} else {
							this.status = 1
						}
					}
				});
			},

			// 清除录音
			clearAudio() {
				this.status = 1
				this.recordLength = 0
				this.form.voiceIntroduce = ''
			},


		}
	}
</script>

<style>
	::v-deep .u-checkbox__label {
		display: none;
	}

	.u-input__textarea {
		color: #ffffff !important;
	}

	/* ::v-deep .u-border-bottom:after{
		border: 0rpx solid #34244f;
	} */
</style>
