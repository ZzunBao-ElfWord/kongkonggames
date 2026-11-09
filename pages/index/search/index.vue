<template>
	<view class="content">
		<view class="search-box">
			<!-- mSearch组件 如果使用原样式，删除组件元素-->
			<!-- <mSearch class="mSearch-input-box" :mode="2" button="inside" :placeholder="defaultKeyword"
				@search="doSearch(false)" @input="inputChange" @confirm="doSearch(false)" v-model="keyword"></mSearch> -->
			<!-- 原样式 如果使用原样式，恢复下方注销代码 -->

			<!-- <view class="input-box">
				<input type="text" :adjust-position="true" :placeholder="defaultKeyword" @input="inputChange" v-model="keyword" @confirm="doSearch(false)"
				 placeholder-class="placeholder-class" confirm-type="search">
			</view>
			<view class="search-btn" @tap="doSearch(false)">搜索</view> -->
			<u-search style="width: 100%;" placeholder="搜索你想要的电竞教练" :focus="true" v-model="keyword" :show-action="true"
				:animation="true" shape="square" bg-color="#1C1C22" color="#FFFFFF" action-text="取消" @custom="goBack()"
				@search="doSearch(false)" height="82"></u-search>
			<!-- 原样式 end -->
		</view>
		<view class="search-keyword">
			<view class="keyword-list-box" v-if="isShowKeywordList">
				<!-- #ifdef H5 -->
				<ren-dropdown-filter :filterData='filterData' :border="false" :defaultIndex='defaultIndex'
					@onSelected='change' class="u-skeleton-rect"
					style="width: 100%;position: fixed;top: 170upx;z-index: 999;">
				</ren-dropdown-filter>
				<!-- #endif -->
				<!-- #ifndef H5 -->
				<ren-dropdown-filter :filterData='filterData' :border="false" :defaultIndex='defaultIndex'
					@onSelected='change' class="u-skeleton-rect"
					style="width: 100%;position: fixed;top: 85upx;z-index: 999;">
				</ren-dropdown-filter>
				<!-- #endif -->
				<view class="margin-lr-sm padding-bottom" style="margin-top: 180upx;">
					<view class="bgbox" v-for="(item, index) in keywordList" :key="index" @click="goOrder(item)">
						<view style="margin-top: -30rpx;position: relative;bottom: -6rpx;">
							<image :src="item.avatar?item.avatar: '../../../static/logo.png'"
								style="width: 230rpx;height: 260rpx;border-radius: 24rpx"></image>
							<view class="yuyinImg" @click.stop="playVoice(item.voiceIntroduce,item.isPlay,index)"
								v-if="item.voiceIntroduce">
								<view class="flex align-center box">
									<!-- 暂停 -->
									<image v-if="item.isPlay==false" src="../../../static/jc-record/index.png" mode=""
										style="width: 40upx;height: 40upx;"></image>
									<!-- 播放 -->
									<image v-else src="../../../static/jc-record/bof.png" mode=""
										style="width: 40upx;height: 40upx;"></image>
									<image src="../../../static/jc-record/yuyin.png" mode=""
										style="width: 84upx;height: 32upx;margin: 0upx 10upx;"></image>
									<view v-if="item.sec">{{item.sec}}<text>"</text></view>
								</view>
							</view>
						</view>
						<view
							class="flex-sub margin-left text-white flex flex-direction justify-between padding-top-sm">
							<view class="flex justify-between">
								<view class="flex align-center ">
									<view class="margin-right-xs text-lg text-bold" style="display: inline-block;">
										{{item.userName}}
									</view>
									<view v-if="false" class="  margin-right-xs"
										style="display: inline-block;background:linear-gradient(-90deg, #174C6D, #33355F);;padding: 2upx 12upx;color: #6EE3FB;border-radius: 50rpx;">
										<u-icon name="man" color="#6EE3FB" size="24"></u-icon>
										{{item.age?item.age:0}}
									</view>
									<view v-if="false" class="  margin-right-xs"
										style="display: inline-block;background: linear-gradient(-90deg, #4F2B4B, #6735b1);padding: 2upx 12upx;color: #FB85FC;border-radius: 50rpx;">
										<u-icon name="woman" color="#FB85FC" size="24"></u-icon>
										{{item.age?item.age:0}}
									</view>
								</view>
								<view class="">
									<view class="text-sm" v-if="item.onLine==1">
										<view
											style="width: 10upx;height: 10upx;background: #1AD566;border-radius: 10upx;display: inline-block;margin: 0 8upx 4upx 0;">
										</view>
										在线
									</view>
									<view class="text-sm" v-else>
										<view
											style="width: 10upx;height: 10upx;background: #999999;border-radius: 10upx;display: inline-block;margin: 0 8upx 4upx 0;">
										</view>
										离线
									</view>
								</view>
							</view>

							<view class="flex" v-if="item.label ">
								<view v-for="(ite,ind) in item.label" :key="ind">
									<image :src="ite" style="width: 98rpx;height: auto;margin-right: 5rpx;"
										mode="widthFix">
									</image>
								</view>
							</view>
							<view class="flex text-sm" style="color: #FFFFFF;">
								<view class="margin-right" v-if="item.orderScore">评分{{(item.orderScore).toFixed(2)}}
								</view>
								<view class="margin-right" v-else>暂无评分</view>
								<view v-if="item.count">{{item.count}}人已下单</view>
							</view>
							<view class="flex align-center justify-between">
								<view class="gambox">
									<view class="gamebox">
										<image :src="item.gameImg" mode="aspectFill" style="">
										</image>
										<view class="level">{{item.gameName}}</view>
									</view>
									<view class="mytit">{{item.myLevel}}</view>
								</view>
								<view style="color: #FBCA0C;">
									<text class="text-lg text-bold">{{isVip? item.memberMoney :item.money}}
									</text>币/局
								</view>
							</view>
						</view>
					</view>
				</view>
				<!-- <view v-if="keywordList.length == 0">
					暂无数据
				</view> -->
				<empty v-if="keywordList.length == 0"></empty>
			</view>
			<scroll-view class="keyword-box" style="margin-top: 100upx;" v-show="!isShowKeywordList" scroll-y>
				<view class="keyword-block">
					<view class="keyword-list-header">
						<view>热门搜索</view>
						<view>
							<image @tap="hotToggle" :src="'/static/images/index/attention'+forbid+'.png'"></image>
						</view>
					</view>
					<view class="keyword" v-if="forbid==''">
						<view v-if="keyword" v-for="(keyword,index) in hotKeywordList" @tap="doSearch(keyword)"
							:key="index">
							{{keyword}}
						</view>
					</view>
					<view class="hide-hot-tis" v-else>
						<view>当前搜热已隐藏</view>
					</view>
				</view>
				<view class="keyword-block" v-if="oldKeywordList.length>0">
					<view class="keyword-list-header">
						<view>历史记录</view>
						<view>
							<image @tap="oldDelete" src="/static/images/index/delete.png"></image>
						</view>
					</view>
					<view class="keyword">
						<view v-if="keyword" v-for="(keyword,index) in oldKeywordList" @tap="doSearch(keyword)"
							:key="index">
							{{keyword}}
						</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<popup v-if="popupshow" :show="popupshow" @closeT="popupshow = false" @sureT="sureT" :content="content"></popup>
	</view>
</template>

<script>
	import popup from '@/components/popup.vue'
	import empty from '@/components/empty.vue'
	import RenDropdownFilter from '@/components/ren-dropdown-filter/ren-dropdown-filter.vue'
	export default {
		components: {
			RenDropdownFilter,
			empty,
			popup
		},
		data() {
			return {
				popupshow: false,
				content: '',


				juliSelect: '否',
				value1: '',
				value2: '',
				value3: '',
				defaultIndex: [0, 0],
				filterData: [
					[{
							label: '智能排序',
							value: '',
						},
						{
							label: '距离优先',
							value: 3,
						},
						{
							label: '人气优先',
							value: 2,
						},
						{
							label: '同城优先',
							value: 1,
						}
					],
					[{
							label: '不限价格',
							value: '',
						},
						{
							label: '从高到低',
							value: 'desc',
						},
						{
							label: '从低到高',
							value: 'asc',
						}
					],
				],
				defaultKeyword: "",
				keyword: "",
				oldKeywordList: [], //历史记录
				hotKeywordList: [], //热搜
				keywordList: [], //搜索列表
				forbid: '',
				isShowKeywordList: false,
				limit: 10,
				page: 1,
				userId: '',
				isVip: false,
				AUDIO: uni.createInnerAudioContext(),
				musicIndex: '',
			}
		},
		onLoad() {
			this.juliSelect = this.$queue.getData('juliSelect');
			this.userId = uni.getStorageSync('userId')
			if (this.userId) {
				this.getSearchList()
			}
			this.isVip = uni.getStorageSync('isVIP')
			//语音自然播放结束
			this.AUDIO.onEnded((res) => {
				let musicIndexs = this.musicIndex;
				if (musicIndexs != '') {
					this.keywordList[musicIndexs].isPlay = false;
				} else {
					this.keywordList[0].isPlay = false;
				}
			});
		},
		onHide() {
			this.AUDIO.stop();
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.keywordList[musicIndexs].isPlay = false;
			} else if (musicIndexs == 0) {
				this.keywordList[0].isPlay = false;
			}
		},
		methods: {
			// 播放语音
			playVoice(voiceIntroduce, isPlay, index) {
				this.isPlay = this.keywordList[index].isPlay
				this.AUDIO.src = voiceIntroduce;
				if (this.isPlay == true) {
					this.AUDIO.stop();

					this.keywordList[index].isPlay = false
				} else {
					let musicIndexs = this.musicIndex;
					if (musicIndexs != '') {
						this.keywordList[musicIndexs].isPlay = false;
					} else if (musicIndexs == 0) {
						this.keywordList[0].isPlay = false;
					}
					this.musicIndex = index;
					this.AUDIO.stop();

					this.AUDIO.play();
					this.keywordList[index].isPlay = true
				}
				// this.isPlay = !this.isPlay;
			},
			// 筛选
			change(e) {
				this.value1 = e[0][0].value
				this.value3 = e[1][0].value
				this.page = 1;
				this.doSearch(false);
			},
			// 获取搜索历史
			getSearchList() {
				this.$Request.get("/app/Search/selectAppSearchNum").then(res => {
					console.log(res)
					if (res.code == 0) {
						this.oldKeywordList = res.data.userSearchName
						this.hotKeywordList = res.data.allSerchName
					}
				});
			},
			//清除历史搜索
			oldDelete() {

				this.content = '确定清除历史搜索记录？'
				this.popupshow = true
				// let that = this
				// that.$Pmessage({
				// 	showModel: true,
				// 	title: '提示',
				// 	content: '确定清除历史搜索记录？',
				// 	maskcloseAble: false
				// }, {
				// 	emitBtn:()=> { //确认
				// 		console.log('用户点击确认');
				// 		that.$Request.get("/app/Search/deleteAppSearch").then(res => {
				// 			if (res.code == 0) {
				// 				that.getSearchList()
				// 			}
				// 		})
				// 	},
				// 	emitClose:()=> { //取消
				// 		console.log('用户点击取消');
				// 	},
				// });
			},
			sureT() {
				let that = this
				that.$Request.get("/app/Search/deleteAppSearch").then(res => {
					if (res.code == 0) {
						that.popupshow = false
						that.getSearchList()
					}
				})
			},
			setMorKm(m) {
				var n = ''
				if (m) {
					if (m >= 1000) {
						n = (m / 1000).toFixed(0) + 'km'
					} else {
						n = parseInt(m) + 'm'
					}
				} else {
					n = '0m'
				}
				return n
			},
			//执行搜索
			doSearch(keyword) {
				this.keyword = keyword === false ? this.keyword : keyword;
				this.isShowKeywordList = true;
				if (!this.keyword) {
					uni.showToast({
						title: '请输入内容',
						icon: 'none',
						duration: 1000
					})
					return
				}

				let data = {
					like: this.keyword,
					limit: this.limit,
					page: this.page,
					condition: this.value1, //智能优选
					by: this.value3, //价格
					latitude: uni.getStorageSync('latitude') ? uni.getStorageSync('latitude') : '',
					longitude: uni.getStorageSync('longitude') ? uni.getStorageSync('longitude') : '',
				}
				if (this.userId) {
					this.$Request.get("/app/orderTaking/queryTaking", data).then(res => {
						if (res.code == 0) {
							if (this.page == 1) {
								this.keywordList = [];
							}
							res.data.list.forEach(d => {
								d.isPlay = false
								if (d.label) {
									d.label = d.label.split(',')
								}
								d.distance = this.setMorKm(d.distance);
								this.keywordList.push(d);
							});
							// this.keywordList = [...this.keywordList, ...res.data.list]
						}
					});
				} else {
					this.$Request.get("/app/orderTaking/queryTakings", data).then(res => {
						if (res.code == 0) {
							if (this.page == 1) {
								this.keywordList = [];
							}
							res.data.list.forEach(d => {
								d.isPlay = false
								if (d.label) {
									d.label = d.label.split(',')
								}
								this.keywordList.push(d);
							})
						}
					});
				}
			},
			// 点击取消返回首页
			goBack() {
				uni.navigateBack()
			},
			//热门搜索开关
			hotToggle() {
				this.forbid = this.forbid ? '' : '_forbid';
			},
			// 跳转订单
			goOrder(e) {
				if (this.userId) {
					uni.navigateTo({
						url: '/pages/index/game/order?id=' + e.id
					});
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
			},

		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.doSearch(false);
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.doSearch(false);
		},
	}
</script>
<style lang="less">
	::v-deep .textMoney {
		text-align: center;
	}

	.search-box {
		width: 100%;
		/* background-color: rgb(242, 242, 242); */
		padding: 15upx 2.5%;
		display: flex;
		justify-content: space-between;
		position: fixed;
		/* #ifdef H5 */
		top: 80upx;
		/* #endif */
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		/* background-color: #101014; */
		z-index: 999;
	}

	.search-box .mSearch-input-box {
		width: 100%;
	}

	.search-box .input-box {
		width: 85%;
		flex-shrink: 1;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.search-box .search-btn {
		width: 15%;
		margin: 0 0 0 2%;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-shrink: 0;
		font-size: 28upx;
		color: #fff;
		background: linear-gradient(to right, #ff9801, #ff570a);
		border-radius: 60upx;
	}

	.search-box .input-box>input {
		width: 100%;
		height: 60upx;
		font-size: 32upx;
		border: 0;
		border-radius: 60upx;
		-webkit-appearance: none;
		-moz-appearance: none;
		appearance: none;
		padding: 0 3%;
		margin: 0;
		background-color: #ffffff;
	}

	.placeholder-class {
		color: #9e9e9e;
	}

	.search-keyword {
		width: 100%;
		background-color: #050506;
	}

	.keyword-list-box {
		height: calc(100vh - 110upx);
		padding-top: 10upx;
		/* border-radius: 20upx 20upx 0 0; */
		/* background-color: #fff; */
	}

	.keyword-entry-tap {
		background-color: #eee;
	}

	.keyword-entry {
		width: 94%;
		height: 80upx;
		margin: 0 3%;
		font-size: 30upx;
		color: #333;
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: solid 1upx #e7e7e7;
	}

	.keyword-entry image {
		width: 60upx;
		height: 60upx;
	}

	.keyword-entry .keyword-text,
	.keyword-entry .keyword-img {
		height: 80upx;
		display: flex;
		align-items: center;
	}

	.keyword-entry .keyword-text {
		width: 90%;
	}

	.keyword-entry .keyword-img {
		width: 10%;
		justify-content: center;
	}

	.keyword-box {
		height: calc(100vh - 110upx);
		/* border-radius: 20upx 20upx 0 0; */
		background-color: #050506;
	}

	.keyword-box .keyword-block {
		padding: 10upx 0;
	}

	.keyword-box .keyword-block .keyword-list-header {
		width: 94%;
		padding: 10upx 3%;
		font-size: 27upx;
		font-weight: 700;
		color: #FFFFFF;
		display: flex;
		justify-content: space-between;
	}

	.keyword-box .keyword-block .keyword-list-header image {
		width: 40upx;
		height: 40upx;
	}

	.keyword-box .keyword-block .keyword {
		width: 94%;
		padding: 3px 3%;
		display: flex;
		flex-flow: wrap;
		justify-content: flex-start;
	}

	.keyword-box .keyword-block .hide-hot-tis {
		display: flex;
		justify-content: center;
		font-size: 28upx;
		color: #FFFFFF;
	}

	.keyword-box .keyword-block .keyword>view {
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 10upx;
		padding: 0 20upx;
		margin: 10upx 20upx 10upx 0;
		height: 60upx;
		font-size: 28upx;
		background-color: #1C1C22;
		color: #FFFFFF;
	}

	.bgbox {
		background-color: #101014;
		display: flex;
		justify-content: space-between;
		margin-top: 60rpx;
		padding: 0 20rpx 30rpx 20rpx;
		border-radius: 24upx;
		position: relative;
	}

	.yuyinImg {
		width: 230upx;
		height: 60upx;
		text-align: center;
		line-height: 60upx;
		// background: #FFFFFF;
		// border-radius: 16upx;
		color: #FFFFFF;
		border-radius: 0rpx 0rpx 24rpx 24rpx;
		background: #2C2613;
		margin-top: -10rpx;
		position: absolute;
		bottom: 0;
		left: 0rpx;
		right: 0;
	}

	.yuyinImg .box {
		width: 100%;
		text-align: center;
		justify-content: space-between;
		padding: 0 15rpx;
	}

	.gambox {
		width: 68%;
		line-height: 39upx;
		background: linear-gradient(-90deg, #221A06 0%, #4A3808 100%);
		border-radius: 24rpx 0 0 24rpx;
		display: flex;
		align-items: center;

		.gamebox {
			display: flex;
			align-items: center;
			background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/22/9c9581055662f648645300a662645892.png');
			background-size: 100% 100%;
			background-repeat: no-repeat;
			padding-right: 20rpx;
			border-radius: 24rpx 0 0 24rpx;
			position: relative;
			font-size: 24rpx;

			image {
				width: 47rpx;
				height: 43rpx;
				border-radius: 50%;
				position: absolute;
				top: 0;
				left: -10rpx;
			}

			.level {
				margin-left: 44rpx;
				font-size: 24rpx;
			}
		}

		.mytit {
			width: 110rpx;
			margin-left: 10rpx;
			font-size: 24rpx;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			overflow: hidden;

		}
	}
</style>