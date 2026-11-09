<template>
	<view>
		<!-- 背景图 -->
		<image src="../static/voice.png" class="bgImg" mode="aspectFill"></image>
		<!-- 自定义导航栏 -->
		<u-navbar :custom-back="customBacks" h5-nav-height="0" back-icon-color="#ffffff" :border-bottom="false"
			:is-back="true" :title="title" :background="background">
			<view class="slot-wrap">
				<div class="title flex align-center">
					{{roomInfo.roomName}}
					<!-- <view class="title-btn">
						<u-icon name="star" color="#ffffff" size="22"></u-icon>
						收藏
					</view> -->
				</div>
				<div class="info">
					<text class="info-id">ID:{{roomInfo.roomNo}}</text>
					<text class="info-line">|</text>
					<text class="info-online">在线：{{assemble}}人</text>
					<!-- <text class="info-vipNum">贵宾:24人</text> -->
				</div>

			</view>
			<view class="" slot="right">
				<view class="flex align-center" style="margin-right: 30rpx;" @click="showUsers = true">
					<view class="userList-l flex align-center">
						<view class="userList-l-item" v-if="userLIst[0]">
							<image class="userList-l-item-t" src="../static/di1.png" mode=""></image>
							<image class="userList-l-item-a"
								:src="userLIst[0].avatar?userLIst[0].avatar:'../../static/logo.png'" mode="">
							</image>
						</view>
						<view class="userList-l-item" v-if="userLIst[1]">
							<image class="userList-l-item-t" src="../static/di2.png" mode=""></image>
							<image class="userList-l-item-a"
								:src="userLIst[1].avatar?userLIst[1].avatar:'../../static/logo.png'" mode="">
							</image>
						</view>
						<view class="userList-l-item" v-if="userLIst[2]">
							<image class="userList-l-item-t" src="../static/di3.png" mode=""></image>
							<image class="userList-l-item-a"
								:src="userLIst[2].avatar?userLIst[2].avatar:'../../static/logo.png'" mode="">
							</image>
						</view>
					</view>
					<view class="userList-c flex align-center">
						{{assemble}}
					</view>
					<view class="userList-r flex align-center">
						···
					</view>
				</view>
			</view>
		</u-navbar>
		<!-- 主体内容 -->
		<view
			:style="{top:(screenHeight+statusBarHeight) + 'px',height:'calc(100vh - '+(screenHeight+statusBarHeight)+'px - 118rpx - 40rpx)'}"
			class="content flex align-center justify-center">
			<view class="content-box">
				<!-- 观众前三预览 -->
				<view class="userList flex align-center justify-between">
					<!-- 公告、玩法、房间列表 -->
					<view class="notice flex align-center justify-between">
						<view class="notice-l flex align-center">
							<view @click="showNotice = true" class="notice-l-item flex align-center justify-center">
								<image src="../static/gong.png" style="width: 28rpx;height: 28rpx;margin-right: 10rpx;"
									mode=""></image>
								房间公告
							</view>
							<view @click="showPlayer = true" class="notice-l-item flex align-center justify-center">
								<image src="../static/wf.png" style="width: 28rpx;height: 28rpx;margin-right: 10rpx;"
									mode=""></image>
								房间玩法
							</view>
							<view @click="goGs()" class="notice-l-item flex align-center justify-center">
								<image src="../../static/images/my/yuliaojg.png"
									style="width: 28rpx;height: 20rpx;margin-right: 10rpx;" mode=""></image>
								违规公示
							</view>
						</view>

					</view>


				</view>

				<!-- 上麦区 -->
				<view class="upMike">
					<!-- 房主 -->
					<view class="upMike-master flex align-center justify-center flex-wrap">
						<view @click="goUserInfoTo(homeownerInfo.userId)"
							class="upMike-master-imgs flex align-center justify-center">
							<image class="upMike-master-imgs-a"
								:src="homeownerInfo.avatar?homeownerInfo.avatar:'../../static/logo.png'" mode="">
							</image>
							<image class="upMike-master-imgs-z" src="../static/zhuchi.png" mode=""></image>
							<!-- <image class="upMike-master-imgs-s" src="../../static/images/my/audioStatus.gif"
								mode="aspectFill">
							</image> -->
							<block v-if="isAudio">
								<view class="upMike-master-imgs1"></view>
								<view class="upMike-master-imgs1 upMike-master-imgs2"></view>
							</block>

						</view>

						<view class="upMike-master-name">
							{{homeownerInfo.userName?homeownerInfo.userName:'匿名'}}
						</view>
						<view class="upMike-master-gift flex align-center justify-center">
							<view class="upMike-master-gifts flex align-center justify-center">
								<image style="width: 20rpx;height: 20rpx;margin-right: 4rpx;" src="../static/gift.png"
									mode=""></image>
								{{zongNum}}
							</view>

						</view>
					</view>
					<!-- 嘉宾 -->
					<view class="upMike-guest flex align-center justify-between flex-wrap">
						<view @click="userLianmai(item,index)" class="upMike-guest-item" v-for="(item,index) in guest"
							:key="index">
							<view v-if="item.userId" class="upMike-guest-item-img">
								<image class="upMike-guest-item-img" :src="item.avatar" mode=""></image>
							</view>
							<view v-else class="upMike-guest-item-imgs flex align-center justify-center">
								<u-icon name="plus" color="#FFD85D" size="38"></u-icon>
							</view>
							<view class="upMike-guest-item-name flex align-center justify-center">
								<view v-if="item.userId" style="background-color:#FBCA0C;"
									class="upMike-guest-item-names flex align-center justify-center">{{index+1}}
								</view>
								<view class="upMike-guest-item-namet">
									{{item.userId?item.name:'嘉宾位'}}
								</view>
							</view>
							<view class="upMike-guest-item-gift flex align-center justify-center">
								<view class="upMike-guest-item-gifts flex align-center justify-center">
									<image style="width: 20rpx;height: 20rpx;margin-right: 4rpx;"
										src="../static/gift.png" mode=""></image>
									{{item.userId?item.num:0}}
								</view>
							</view>
							<block v-if="item.isAudio">
								<view class="upMike-guest-audio"></view>
								<view class="upMike-guest-audio upMike-guest-audio2"></view>
							</block>
						</view>
					</view>
					<!-- 礼物记录 -->
					<view class="upMike-giftList flex align-center justify-center">
						<view class="upMike-giftLists flex align-center justify-between">
							<view class="upMike-giftLists-l flex align-center flex align-center justify-center">
								<image src="../static/giftlist.png"
									style="width: 22rpx;height: 28rpx;margin-right: 10rpx;" mode=""></image>
								送礼记录
							</view>
							<view class="upMike-giftLists-r flex align-center">
								<image src="../static/gifts.png" style="width: 28rpx;height: 26rpx;" mode="">
								</image>
								<view class="upMike-giftLists-rs">
									<u-notice-bar :volume-icon="false" color="#FFE793" bg-color="transparent"
										padding="4rpx 0 0 0" style="height: 100%;" font-size="22" mode="vertical"
										:list="list"></u-notice-bar>
								</view>
							</view>
						</view>
					</view>
				</view>
				<!-- 中间聊天消息列表区 -->
				<view class="msgList">
					<scroll-view :scroll-into-view="goBottomId" scroll-y="true" style="width: 100%;height: 100%;">
						<!-- 公告/提醒 -->
						<view class="msgList-gg flex justify-between">
							<view class="msgList-gg-l">
								<view class="msgList-gg-txt" v-if="roomInfo.announcement">
									{{roomInfo.announcement}}
								</view>
								<view v-if="roomInfo.playingMethod" class="msgList-gg-txt" style="margin-top: 20rpx;">
									玩法
								</view>
								<view class="msgList-gg-txt" v-if="roomInfo.playingMethod">
									{{roomInfo.playingMethod}}
								</view>
							</view>
							<!-- <view class="msgList-gg-r">
								<view class="msgList-gg-txt">
									<image src="../static/ggt.png" mode=""></image>
								</view>
								<view class="msgList-gg-txt" style="margin-top: 20rpx;">
									需求：歌曲表演
								</view>
								<view class="msgList-gg-txt">
									性别：不限
								</view>
								<view class="msgList-gg-txt">
									角色：不限
								</view>
								<view class="msgList-gg-txt">
									备注：不限
								</view>

							</view> -->
						</view>
						<!-- 进入房间提示 -->
						<!-- <view class="msgList-tips">
							小木齐进入直播间
						</view> -->
						<!-- 提示 -->
						<view class="msgList-ts" v-if="ggContent">
							{{ggContent}}
						</view>
						<view class="" :id="'msg'+item.roomChatId" v-for="(item,index) in messageList" :key="index">
							<!-- 聊天记录 -->
							<view v-if="Number(item.userId)==0" class="msgList-item flex">
								<image src="../../static/logo.png" mode=""></image>
								<view class="msgList-item-info">
									<view class="msgList-item-info-name">
										超管
									</view>
									<view class="msgList-item-info-con">
										{{item.content}}
									</view>
								</view>
							</view>
							<block v-if="Number(item.userId)!=0">
								<view v-if="item.messageType=='1'" class="msgList-item flex">
									<image @click="goUserInfoTo(item.userId)"
										:src="item.avatar?item.avatar:'../../static/logo.png'" mode=""></image>
									<view class="msgList-item-info">
										<view class="msgList-item-info-name">
											{{item.userName?item.userName:'匿名用户'}}
										</view>
										<view class="msgList-item-info-con">
											{{item.content}}
										</view>
									</view>
								</view>
								<!-- 礼物记录 -->
								<view v-if="item.messageType=='5'" class="msgList-item flex">
									<image @click="goUserInfoTo(item.userId)"
										:src="item.avatar?item.avatar:'../../static/logo.png'" mode=""></image>
									<view class="msgList-item-info">
										<view class="msgList-item-info-name">
											{{item.userName?item.userName:'匿名用户'}}
										</view>
										<view class="msgList-item-info-con flex">
											<view class="msgList-item-info-con-l">
												<view>
													赠送
												</view>
												<view>
													{{item.content.giftName}}<span></span>
												</view>
											</view>
											<image :src="item.content.giftImg" mode=""></image>
										</view>
									</view>
								</view>
							</block>

							<!-- 进入房间提示 -->
							<view v-if="item.messageType=='7'" class="msgList-tips flex align-center">
								<image v-if="item.userId == homeownerInfo.userId" src="../static/zhuchis.png" mode="">
								</image>
								{{item.content}}
							</view>
						</view>

					</scroll-view>
				</view>
				<!-- 底部消息区域 -->
				<!-- <view class="mssage flex align-center justify-around">
					<view class="mssage-item messages flex align-center justify-center">
						<u-search confirmTypes="send" color="#ffffff" bg-color="#2C243B" action-text="确认"
							:clearabled="false" search-icon="" @search="sendMsgs()" :show-action="false"
							placeholder="请输入" v-model="content"></u-search>
					</view>
					<view class="mssage-item" @click="showgift = true">
						<image src="../static/gGift.png" mode=""></image>
					</view>
					<view class="mssage-item" @click="showMsg = true">
						<image src="../static/mgmsg.png" mode=""></image>
					</view>
					<view @click="openOrClose()" class="mssage-item">
						<image v-if="isOpenAudio" src="../static/audioclose.png" mode=""></image>
						<image v-else src="../static/audioclosex.png" mode=""></image>
					</view>
					<view class="mssage-item" @click="sendMsg = true">
						<image src="../static/moreset.png" mode=""></image>
					</view>
				</view> -->
				<view class="mssage-container">
					<view v-if="!isInputExpanded" class="mssage-collapsed flex align-center justify-center"
						@click.stop="handleInputClick">
						<image src="../static/moreset.png" mode=""></image>
					</view>

					<view v-else class="mssage-expanded" @click.stop>
						<view class="mssage flex align-center justify-around">
							<view class="mssage-item messages flex align-center justify-center">
								<u-search confirmTypes="send" color="#ffffff" bg-color="#1C1C22" action-text="确认"
									:clearabled="false" search-icon="" @search="sendMsgs()" :show-action="false"
									placeholder="请输入" v-model="content"></u-search>
							</view>
							<view class="mssage-item" @click="showgift = true">
								<image src="../static/gGift.png" mode=""></image>
							</view>
							<view class="mssage-item" @click="showMsg = true">
								<image src="../static/mgmsg.png" mode=""></image>
							</view>
							<view @click="openOrClose()" class="mssage-item">
								<image v-if="isOpenAudio" src="../static/audioclose.png" mode=""></image>
								<image v-else src="../static/audioclosex.png" mode=""></image>
							</view>
							<view class="mssage-item" @click="sendMsg = true">
								<image src="../static/moreset.png" mode=""></image>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>


		<!-- 消息弹窗 -->
		<u-popup :scrollY="false" v-model="showMsg" mode="bottom" border-radius="24" :closeable="true">
			<view class="msgTitile">
				消息
			</view>
			<view class="myMsg">
				<scroll-view scroll-y="true" class="myMsg">
					<msgIndex @isGo="isGo()" />
				</scroll-view>
			</view>
		</u-popup>
		<!-- 举报弹窗 -->
		<u-popup :scrollY="false" @close="closeSendJb()" v-model="sendMsg" mode="bottom" border-radius="24"
			:closeable="true">
			<view style="background-color: #050506;">
				<view class="msgTitile">
					举报
				</view>
				<view class="jubao">
					<view class="jubao-item flex align-center">
						标题：<u-input class="jubao-title-inp" v-model="sendFrom.title" placeholder="请输入标题" type="text"
							:border="false" />
					</view>
					<view class="jubao-item flex align-center">
						内容：<u-input class="jubao-title-inp" v-model="sendFrom.content" placeholder="请输入内容"
							type="textarea" :border="false" />
					</view>
					<view class="jubao-item flex align-center" style="margin-bottom:0;">
						图片：
					</view>
					<view class="jubao-item" style="padding-left: 0rpx;">
						<shmily-drag-image :list.sync="sendImgList" :number="6"></shmily-drag-image>
					</view>
					<view @click="jubaoUp()" class="jubao-btn flex align-center justify-center">
						立即举报
					</view>
				</view>
			</view>

		</u-popup>
		<!-- 房间公告 -->
		<u-popup :scrollY="false" v-model="showNotice" width="60%" mode="center" border-radius="24" :closeable="true">
			<view class="notices">
				<view class="notices-title">
					房间公告
				</view>
				<view class="notices-content">
					{{roomInfo.announcement?roomInfo.announcement:'暂无公告'}}
				</view>
			</view>
		</u-popup>
		<!-- 房间玩法 -->
		<u-popup :scrollY="false" v-model="showPlayer" width="60%" mode="center" border-radius="24" :closeable="true">
			<view class="notices">
				<view class="notices-title">
					房间玩法
				</view>
				<view class="notices-content">
					{{roomInfo.playingMethod?roomInfo.playingMethod:'暂无公告'}}
				</view>
			</view>
		</u-popup>
		<!-- 显示所有人员的列表 -->
		<u-popup v-model="showUsers" mode="right" width="80%" :safe-area-inset-bottom="false">
			<view class="allUserList flex align-center justify-center">
				<view class="allUserList-box">
					<view class="allUserList-search" :style="{marginTop:(screenHeight+statusBarHeight)+'px'}">
						<u-search placeholder="请输入用户昵称" v-model="searchName" @search="searchUserList()"
							@custom="searchUserList()"></u-search>
					</view>
					<view class="allUserList-list"
						:style="{height:'calc(100vh - '+(screenHeight+statusBarHeight+44)+'px)'}">
						<scroll-view @scrolltolower="getMoreUserList()" scroll-y="true"
							style="width: 100%;height: 100%;padding: 20rpx 0;">
							<view class="allUserList-list-item flex align-center justify-between"
								v-for="(item,index) in userLIst" :key="index">
								<view @click.stop="goUserInfoTo(item.userId)"
									class="allUserList-list-item-l flex align-center">
									<image :src="item.avatar?item.avatar:'../../static/logo.png'" mode=""></image>
									<view class="allUserList-list-item-name">
										{{item.userName}}
									</view>
								</view>
								<view class="flex align-center">
									<view @click.stop="inviteUserUp(item)" class="allUserList-list-item-up"
										v-if="item.isSeat!=1 && userId == homeownerInfo.userId">
										上麦
									</view>
									<view @click.stop="isBanUsers(item)" class="allUserList-list-item-up"
										v-if="userId == homeownerInfo.userId && userId!=item.userId">
										封禁
									</view>
									<view class="" v-if="item.userId == homeownerInfo.userId">
										主持
									</view>
								</view>

							</view>
						</scroll-view>

					</view>
				</view>
				<!-- 关闭房间退出按钮 -->
				<view class="allUserList-close" @click="closeRoom()">
					<image src="../../static/images/msg/closeRoom.png" mode="widthFix"></image>
				</view>
			</view>
		</u-popup>
		<!-- 礼物弹框 -->
		<u-popup v-model="showgift" mode="bottom" border-radius="14" :closeable="false">
			<view class="popupbox">
				<view class="padding-top popupboxbgs">
					<scroll-view scroll-x="true" style="white-space: nowrap;width: 100%;">
						<view class="giftUserItem">
							<view class="giftUserItems flex" @click="selectUserGift(homeownerInfo.userId)">
								<view :class="giftUserCurr==homeownerInfo.userId?'selectGiftUser':''"
									class="giftUserItemsW flex align-center justify-center">
									<image :src="homeownerInfo.avatar" mode=""></image>
								</view>
								<view
									class="giftUserItemsNo flex align-center justify-center"
									style="width: 70rpx;font-size: 20rpx;border-radius: 20rpx;background: linear-gradient(90deg, #D9A800 0%, #FBCA0C 100%);">
									主持
								</view>
							</view>
						</view>
						<view class="giftUserItem" @click="selectUserGift(item.userId)" v-for="(item,index) in guest"
							:key="index">
							<view class="giftUserItems flex" v-if="item.userId">
								<view :class="giftUserCurr==item.userId?'selectGiftUser':''"
									class="giftUserItemsW flex align-center justify-center">
									<image :src="item.avatar" mode=""></image>
								</view>

								<view style="background-color:#FBCA0C;"
									class="giftUserItemsNo flex align-center justify-center">
									{{index+1}}
								</view>
							</view>
						</view>

					</scroll-view>
				</view>
				<view class="flex justify-between align-center" style="margin-top: 40rpx;padding: 0 30rpx;">
					<view class="flex align-center">
						<image src="../../static/images/index/jinbi.png" style="width: 38upx;height: 38upx;">
						</image>
						<view class="margin-left-xs" style="font-size: 30rpx;">{{money}}</view>
					</view>
					<view class="popupbtn" @click="goNavs('/my/wallet/index?index=1')">充值</view>
				</view>
				<!-- 分类轮播 -->
				<view class="category" v-if="giftList.length>0">
					<view class="box">
						<swiper class="swiper" duration="300" :style="{ height: categoryHeight }">
							<swiper-item v-for="(nav, index5) in giftList" :key="index5">
								<view class="category-list flex align-center justify-around">
									<view class="iconss popbox" v-for="(item,ind) in nav" :key="ind"
										@click="binddaS(item)" :class="TabCurr == item.giftId?'active':''">
										<image mode="widthFix" :src="item.giftImg" style="height: 90upx;width: 90upx">
										</image>
										<view :class="TabCurr == item.giftId?'actives':''">{{ item.giftName }}</view>
										<view class="text-sm flex align-center justify-center margin-top-xs">
											<view>{{item.giftMoney}}</view>
											<image style="margin:0upx 6upx;width: 20rpx;height: 20rpx;"
												src="../../static/images/index/jinbi.png"></image>
										</view>
										<view v-if="TabCurr == item.giftId" class="zengsong"
											@click="giveGift(item.giftId,item)">赠送
										</view>
									</view>
									<view class="iconss popbox" style="height: 0;"></view>
									<view class="iconss popbox" style="height: 0;"></view>
									<view class="iconss popbox" style="height: 0;"></view>
								</view>
							</swiper-item>
						</swiper>
					</view>
				</view>
			</view>
		</u-popup>
		<image v-if="isShowGifGift" :src="giftGif" class="giftEffect" mode="widthFix"></image>
		<!-- 自定义弹框 -->
		<popup v-if="popupshow" :show="popupshow" :closeName="closeName" :isStartTime="isStartTime"
			:maskCloseAble="false" :confirmName="confirmName" @closeT="closeT" @sureT="sureT" :content="contentPop">
		</popup>

	</view>
</template>

<script>
	// #ifdef H5
	import TRTC from 'trtc-sdk-v5';
	// #endif
	const RECONNECT_INTERVAL = 3000; // 延迟3秒后进行重连
	const HEARTBEAT_INTERVAL = 2000; // 每20秒发送一次心跳消息
	//引入消息组件
	import msgIndex from '../../components/msgIndex/msgIndex.vue'
	import configdata from '../../common/config.js'
	import shmilyDragImage from '../components/shmily-drag-image/shmily-drag-image.vue'
	//自定义弹框
	import popup from '@/components/popup.vue'
	export default {
		components: {
			msgIndex,
			shmilyDragImage,
			popup
		},
		data() {
			return {
				giftGif: "", //特效礼物路径
				isShowGifGift: false, //是否显示礼物效果
				popuCurrent: 1,
				confirmName: '确认', //弹框确认按钮名称
				closeName: '取消', //弹框取消按钮名称
				contentPop: '', //弹框内容
				popupshow: false, //是否显示自定义弹框
				sendImgList: [],
				sendFrom: { //举报表单
					title: '', //标题
					content: '', //内容
					image: '', //举报图片
					state: 100,
					userId: uni.getStorageSync('userId'),
					platformId: '', //roomId
					userName: '', //用户名称
					platform: '', //直播间名称
				},
				sendMsg: false,
				showMsg: false,
				myCustomStyle: {
					background: '#1C1C22',
					padding: '0 10rpx',
					borderRadius: '40rpx'
				},
				msgList: [],
				list: [], //送礼物的列表公告
				guest: [{
					userId: '',
					avatar: '', //用户头像
					name: '',
					num: 0, //礼物数量
					sex: 1, //性别
					roomUserId: '',
					isAudio: false, //是否正在说话
				}, {
					userId: '',
					avatar: '', //用户头像
					name: '',
					num: 0, //礼物数量
					sex: 1, //性别
					roomUserId: '',
					isAudio: false, //是否正在说话
				}, {
					userId: '',
					avatar: '', //用户头像
					name: '',
					num: 0, //礼物数量
					sex: 2, //性别
					roomUserId: '',
					isAudio: false, //是否正在说话
				}, {
					userId: '',
					avatar: '', //用户头像
					name: '',
					num: 0, //礼物数量
					sex: 1, //性别
					roomUserId: '',
					isAudio: false, //是否正在说话
				}, {
					userId: '',
					avatar: '', //用户头像
					name: '',
					num: 0, //礼物数量
					sex: 1, //性别
					roomUserId: '',
					isAudio: false, //是否正在说话
				}, {
					userId: '',
					avatar: '', //用户头像
					name: '',
					num: 0, //礼物数量
					sex: 1, //性别
					roomUserId: '',
					isAudio: false, //是否正在说话
				}, {
					userId: '',
					avatar: '', //用户头像
					name: '',
					num: 0, //礼物数量
					sex: 1, //性别
					roomUserId: '',
					isAudio: false, //是否正在说话
				}, {
					userId: '',
					avatar: '', //用户头像
					name: '',
					num: 0, //礼物数量
					sex: 1, //性别
					roomUserId: '',
					isAudio: false, //是否正在说话
				}], //嘉宾数量
				title: '', //自定义导航栏的内容需要设为空字符串否则在小程序环境下会显示true
				background: {
					backgroundColor: 'transparent'
				},
				screenHeight: 44, //导航栏高度
				statusBarHeight: 0, //状态栏高度
				msgInfo: {}, //聊天对象信息
				strRoomId: '', //房间号id（字符串类型）
				role: '',
				sdkAppId: 1600017348,
				roomId: '',
				userSig: '', //用户凭证
				roomUserId: '',
				roomInfo: {}, //房间信息
				userLIst: [], //房间所有人列表
				page: 1,
				pages: 1,
				limit: 20,
				searchName: '', //搜索的用户名
				homeownerInfo: {}, //房主信息
				showUsers: false, //显示所有人员列表
				assemble: 0, //总人数
				showNotice: false, //是否显示房间公告
				showPlayer: false, //是否显示房间玩法
				userId: uni.getStorageSync('userId'), //自己的userid
				content: '', //聊天内容
				messageList: [], //聊天列表
				goBottomId: '', //要滚动到的位置
				showgift: false, //是否显示礼物弹框
				giftList: [], //礼物列表
				TabCurr: 0,
				categoryHeight: '300rpx',
				money: 0, //余额
				giftUserCurr: -1, //选择送礼物的人
				zongNum: 0, //主持收到的礼物
				myUserInfo: {}, //个人信息
				isOpenAudio: false, //是否开启麦克风
				ggContent: '', //官方公告
				onBackPressFlag: true,
				byUserId: '', //他人的userId
				byNum: 0,
				isHomeAudio: true, //房主正在说话
				byRoomUserId: '', //房主的roomUserId
				isAudio: false, //房主是否正在说话
				isOpen: false, //scoket是否正在链接
				socketTimer: null, //心跳定时器
				isStartTime: false, //是否开启弹窗倒计时
				trtc: null, //trtc实例
				isInputExpanded: false
			};
		},
		created() {
			let sysInfo = uni.getSystemInfoSync()
			// #ifdef MP-WEIXIN
			this.statusBarHeight = sysInfo.statusBarHeight
			// #endif
			// #ifdef APP
			this.statusBarHeight = sysInfo.statusBarHeight
			// #endif
		},
		onLoad(option) {
			this.getGgContent()
			this.trtc = TRTC.create();
			if (option.strRoomId) {
				//获取个人信息
				this.getUserInfo()
				// 远端用户进房事件。
				this.trtc.on(TRTC.EVENT.REMOTE_USER_ENTER, event => {
					const userId = event.userId;
					// 开启音量回调，并设置每 1000ms 触发一次事件
					this.trtc.enableAudioVolumeEvaluation(300);
				});
				//监听用户音量变化
				this.trtc.on(TRTC.EVENT.AUDIO_VOLUME, event => {
					if (event.result.length > 0) {
						event.result.map(item => {
							if (item.volume > 20) { //该用户正在说话
								//当前用户是否是房主
								if (item.userId == '' || item.userId == this.homeownerInfo.userId) {
									this.isAudio = true
								} else {
									let indexs = this.guest.findIndex(ite => ite.userId == item.userId)
									if (indexs != -1) { //找到该用户的位置了
										this.guest[indexs].isAudio = true
									}
								}
							} else { //用户没有说话了
								//判断当前用户是不是房主
								if (item.userId == '' || item.userId == this.homeownerInfo.userId) {
									//是房主
									this.isAudio = false
								} else {
									//别人在说话的时候
									let indexs = this.guest.findIndex(ite => ite.userId == item.userId)
									if (indexs != -1) { //找到该用户的位置了
										this.guest[indexs].isAudio = false
									}
								}
							}
						})
					} else { //没人在说话,把所有人的说话状态设置为false
						this.isAudio = false
						this.guest.map(ite => {
							ite.isAudio = false
						})
					}


				});
				//监听进房失败原因
				this.trtc.on(TRTC.EVENT.ERROR, error => {
					console.error('trtc error observed: ' + error);
					this.contentPop = '房间异常，请重试'
					this.popuCurrent = 1
					this.closeName = '取消'
					this.confirmName = '确认'
					this.popupshow = true
				});

				this.sdkAppId = uni.getStorageSync('sdkAppId')
				this.strRoomId = option.strRoomId //房间id
				this.role = option.role //用户角色
				this.roomId = option.roomId //房间id
				this.requestJoinRoom()
				if (option.isBack == true) {
					// this.requestJoinRoom()
				} else {
					this.$Request.getT('/app/room/selectSign?roomId=' + this.roomId).then(res => {
						if (res.code == 0) {
							this.userSig = res.data
							//进入房间
							this.initAudio()
						}
					})
				}
				this.userId = uni.getStorageSync('userId')
				if (this.userId && this.roomId) {
					this.connect()
				}
			}

		},
		onShow() {
			//如果之前是开启麦克风的（处理在上麦状态后，切换到别的应用，再切回来不能说话的问题）
			if (this.isOpenAudio) {
				//那么切回来的时候再开启一下
				// this.$trtcCloud.startLocalAudio(this.$TRTCAudioQuality.TRTCAudioQualityDefault);
				// this.isOpenAudio = true
				this.startLocalAudio()
			}
			//获取我的余额
			this.getMoney()
			if (this.userId && this.roomId) {
				// this.connect()
				//获取聊天记录
				this.getChatList()
			}
		},
		onBackPress(e) {
			console.log(e, '返回回调')
			if (e.from == 'backbutton') { //只有系统返回才触发
				console.log(e, '触发了')
				let roomInfo = {
					strRoomId: this.strRoomId,
					role: 'TRTCRoleAnchor',
					roomId: this.roomId,
					avatar: this.homeownerInfo.avatar
				}
				//关闭scoket连接
				uni.closeSocket()
				this.isOpen = false
				clearInterval(this.socketTimer)
				this.socketTimer = null
				uni.setStorageSync('startRoom', roomInfo)
			}
		},
		onUnload() {
			console.log('scoket已关闭888888')
			uni.closeSocket()
			this.isOpen = false
			clearInterval(this.socketTimer)
			this.socketTimer = null
		},
		watch: {
			//监听聊天消息记录的变化
			messageList() {
				//根据最新的消息来设置需要滚动到的位置
				//在dom更新后设置
				this.$nextTick(() => {
					if (this.messageList.length > 0) {
						this.goBottomId = 'msg' + this.messageList[this.messageList.length - 1].roomChatId
						//重新设置滚动的位置后更新dom
						this.$forceUpdate()
					}

				})
			},
			//监听礼物动效变化
			isShowGifGift() {
				if (this.isShowGifGift) {
					setTimeout(() => {
						this.isShowGifGift = false
					}, 3000)
				}
			},
		},
		// 在 mounted 中添加全局点击监听
		mounted() {
			// 点击页面其他地方收起输入框
			document.addEventListener('click', this.handleOutsideClick);
		},
		// 在 beforeDestroy 中移除监听
		beforeDestroy() {
			document.removeEventListener('click', this.handleOutsideClick);
		},
		methods: {
			handleInputClick(e) {
				this.isInputExpanded = true;
			},

			handleOutsideClick(e) {
				if (this.isInputExpanded) {
					this.isInputExpanded = false;
				}
			},
			//封禁用户
			isBanUsers(item) {
				//关闭所有用户弹窗
				this.showUsers = false
				//判断这个人是否在麦上
				let indexs = this.guest.findIndex(ite => ite.userId == item.userId)
				console.log(indexs)
				if (indexs != -1) { //在麦上
					//提示语
					this.contentPop = item.userName + '正在麦上，是否踢下麦并封禁该用户？'
				} else { //不在麦上
					//提示语
					this.contentPop = '是否封禁' + item.userName + '？'
				}
				this.byUserId = item.userId
				this.popuCurrent = 9
				this.popupshow = true
			},
			//关闭/退出房间
			closeRoom() {
				if (this.homeownerInfo.userId == this.userId) { //是主持
					this.contentPop = '你是主持，如果退出，该房间将解散，确认退出吗？'
					this.popuCurrent = 2
					this.closeName = '取消'
					this.confirmName = '确认'
					this.popupshow = true
				} else {
					let that = this
					//不是主持，直接退出
					let data = {
						roomId: this.roomId,
						status: 2
					}
					this.$Request.postJson('/app/room/updateRoomUser', data).then(res => {
						if (res.code == 0) {
							let upData = {
								type: 14,
							}
							upData = JSON.stringify(upData);
							uni.sendSocketMessage({
								data: upData,
								success(res) {
									uni.closeSocket()
									that.isOpen = false
									clearInterval(that.socketTimer)
									that.socketTimer = null
									// that.$trtcCloud.exitRoom();
									that.unloadRoom()
									uni.hideLoading()
									uni.navigateBack()
								},
								fail(err) {
									uni.hideLoading()
									// console.log(err);
								}
							})
						} else {
							uni.hideLoading()
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				}
			},
			//自定义返回逻辑
			customBacks() {
				let roomInfo = {
					strRoomId: this.strRoomId,
					role: 'TRTCRoleAnchor',
					roomId: this.roomId,
					avatar: this.homeownerInfo.avatar
				}
				//关闭scoket连接
				uni.closeSocket()
				this.isOpen = false
				clearInterval(this.socketTimer)
				this.socketTimer = null
				uni.setStorageSync('startRoom', roomInfo)
				// uni.$emit('startRoom', roomInfo)
				uni.navigateBack()
			},
			//去公示页面
			goGs() {
				uni.navigateTo({
					url: '/my/publicity/publicity'
				})
			},
			//如果跳转别的scoket页面后，把本页面scoket连接状态设置为false，让返回后重连
			isGo(data) {
				console.log('走这儿了，需要重连')
				this.isOpen = false
				clearInterval(this.socketTimer)
				this.socketTimer = null
				//把scoket也关闭了
				uni.closeSocket()
			},
			async unloadRoom() {
				await this.trtc.exitRoom();
				// 退房成功后，若后续无需使用 trtc 实例，则可以调用 trtc.destroy 方法销毁实例，及时释放相关资源。销毁后的 trtc 实例无法继续使用，需要重新创建新的实例。
				this.trtc.destroy();
			},
			//服务端解散房间
			dismissRoom() {
				let data = {
					roomNo: this.strRoomId
				}
				this.$Request.getT('/app/room/dismissRoom', data).then(res => {
					if (res.code == 0) {
						//退出trtc房间
						this.unloadRoom()
						uni.navigateBack()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//确认弹框回调
			sureT() {
				let that = this
				let upData = {}
				switch (this.popuCurrent) {
					case 1: //房间异常，请重试//退出房间
						that.onBackPressFlag = false
						uni.navigateBack()
						break;
					case 2: //你是主持，如果退出，该房间将解散，确认退出吗？
						let data = {
							roomId: that.roomId,
							status: 2
						}
						console.log('走这里了吗？')
						uni.showLoading({
							title: '关闭中...'
						})
						that.$Request.postJson('/app/room/updateRoomUser', data).then(res => {
							if (res.code == 0) {
								let upData = {
									type: 16,
								}
								upData = JSON.stringify(upData);
								uni.sendSocketMessage({
									data: upData,
									success(res) {
										uni.hideLoading()
										//关闭scoket连接
										uni.closeSocket()
										//链接状态设置为false
										that.isOpen = false
										//取消心跳机制
										clearInterval(that.socketTimer)
										that.socketTimer = null
										//调用解散房间的接口
										that.dismissRoom()
									},
									fail(err) {
										uni.hideLoading()
										// console.log(err);
									}
								})
							} else {
								uni.hideLoading()
								uni.showToast({
									title: res.msg,
									icon: 'none'
								})
							}
						})

						break;
					case 3: //邀请上麦
						upData = {
							type: 10,
							userId: this.byUserId,
							num: Number(this.byNum) + 1,
						}
						upData = JSON.stringify(upData)
						uni.sendSocketMessage({
							data: upData,
							success(res) {

							},
							fail(err) {
								// console.log(err);
							}
						})
						break;
					case 4: //强制下麦提醒
						upData = {
							type: 8,
							userId: this.byUserId,
						}
						upData = JSON.stringify(upData);
						uni.sendSocketMessage({
							data: upData,
							success(res) {

							},
							fail(err) {
								// console.log(err);
							}
						})
						break;
					case 5: //主动下麦提醒
						upData = {
							type: 6,
							userId: this.userId,
						}
						upData = JSON.stringify(upData);
						uni.sendSocketMessage({
							data: upData,
							success(res) {

							},
							fail(err) {
								// console.log(err);
							}
						})
						break;
					case 6: //申请上麦
						this.userUpMicrophone(Number(this.byNum) + 1)
						break;
					case 7: //上麦提醒
						upData = {
							type: 3,
							status: 1,
							userId: this.byUserId,
							num: this.byNum
						}
						upData = JSON.stringify(upData);
						uni.sendSocketMessage({
							data: upData,
							success(res) {

							},
							fail(err) {
								// console.log(err);
							}
						})
						break;
					case 8: //上麦邀请
						//查找一下是否有空麦？
						let indexFind = this.guest.findIndex(item => item.userId == '')
						if (indexFind != -1) { //还有空着的麦，可以上
							upData = {
								type: 12,
								status: 1,
								num: indexFind + 1
							}
							upData = JSON.stringify(upData);
							uni.sendSocketMessage({
								data: upData,
								success(res) {},
								fail(err) {
									// console.log(err);
								}
							})
						} else { //已经没有麦了，不能上了
							uni.showToast({
								title: '暂无可使用麦序',
								icon: 'none'
							})
						}
						break;
					case 9: //封禁用户
						upData = {
							type: 20,
							userId: this.byUserId,
						}
						upData = JSON.stringify(upData);
						uni.sendSocketMessage({
							data: upData,
							success(res) {

							},
							fail(err) {
								// console.log(err);
							}
						})
						break;
					default:
						break;
				}
				//关闭弹框
				this.popupshow = false
				this.isStartTime = false
			},
			//生成一个10-100之间的随机数
			generateRandomNumber() {
				return Math.floor(Math.random() * (100 - 10 + 1)) + 10;
			},
			//取消/关闭弹框
			closeT() {
				let that = this
				let upData = {}
				switch (this.popuCurrent) {
					case 1:
						that.onBackPressFlag = false
						uni.navigateBack()
						break;
					case 2:
						that.onBackPressFlag = true
						break;
					case 7: //上麦提醒
						upData = {
							type: 3,
							status: 2,
							userId: this.byUserId,
							num: this.byNum
						}
						upData = JSON.stringify(upData);
						uni.sendSocketMessage({
							data: upData,
							success(res) {

							},
							fail(err) {
								// console.log(err);
							}
						})
						break;
					case 8: //上麦邀请
						upData = {
							type: 12,
							status: 2,
							num: this.byNum
						}
						upData = JSON.stringify(upData);
						uni.sendSocketMessage({
							data: upData,
							success(res) {

							},
							fail(err) {
								// console.log(err);
							}
						})
						break;
					default:
						break;
				}
				this.popupshow = false
				this.isStartTime = false
				setTimeout(() => {
					this.popuCurrent = 1
					this.confirmName = '确认' //弹框确认按钮名称
					this.closeName = '取消' //弹框取消按钮名称
					this.contentPop = '' //弹框内容
					this.byUserId = ''
					this.byNum = ''
				}, 260)
			},
			//清空举报内容
			closeSendJb() {
				this.sendImgList = []
				this.sendFrom = { //举报表单
					title: '', //标题
					content: '', //内容
					image: '', //举报图片
					state: 100,
					userId: uni.getStorageSync('userId'),
					platformId: '', //roomId
					userName: '', //用户名称
					platform: '', //直播间名称
				}
				this.sendMsg = false
			},
			//立即举报
			jubaoUp() {
				if (!this.sendFrom.title) {
					uni.showToast({
						title: '请输入标题',
						icon: 'none'
					})
					return
				}
				if (!this.sendFrom.content) {
					uni.showToast({
						title: '请输入内容',
						icon: 'none'
					})
					return
				}
				let data = {
					title: this.sendFrom.title,
					content: this.sendFrom.content,
					image: this.sendImgList.length > 0 ? this.sendImgList.join(',') : '',
					state: 100,
					userId: uni.getStorageSync('userId'),
					platformId: this.roomId,
					userName: this.myUserInfo.userName,
					platform: this.roomInfo.roomName
				}
				this.$Request.postJson('/app/message/insertMessage', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '提交成功',
							icon: 'none'
						})
						this.closeSendJb()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//上传举报图片
			uploudRoomImg() {
				let that = this
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album'], //从相册选择
					success: function(res) {
						uni.showLoading({
							title: '上传中...'
						});
						let token = uni.getStorageSync('token');
						let userId = uni.getStorageSync('userId');
						uni.uploadFile({
							// url: 'https://pw.xianmxkj.com/pwyl-api/alioss/upload', //真实的接口地址
							url: that.config("APIHOST1") + '/alioss/upload', //真实的接口地址
							filePath: res.tempFilePaths[0],
							header: {
								token: token
							},
							name: 'file',
							success: uploadFileRes => {
								let url = JSON.parse(uploadFileRes.data);
								// console.log(url.data, '111111111')
								that.sendImgList.push(url.data)
								uni.hideLoading();
							},
							fail() {
								uni.hideLoading()
								uni.showToast({
									title: '上传失败，请重试！',
									icon: 'none'
								})
							}
						});
					}
				});
			},
			//去用户详情
			goUserInfoTo(userId) {
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + userId
				})
			},
			getGgContent() {
				this.$Request.getT('/app/common/type/817').then(res => {
					if (res.code == 0) {
						if (res.data && res.data.value) {
							this.ggContent = res.data.value
						}
					}
				});
			},
			//关闭或开启麦克风
			openOrClose() {
				let that = this
				//先判断是不是房主
				if (this.homeownerInfo.userId == this.userId) {
					//判断当前麦克风是否开启
					if (this.isOpenAudio) { //如果开启则关闭
						// this.$trtcCloud.stopLocalAudio();
						// this.isOpenAudio = false
						this.stopLocalAudio()
						uni.showToast({
							title: '麦克风已关闭',
							icon: 'none'
						})
					} else {
						//如果关闭则开启
						// this.$trtcCloud.startLocalAudio(this.$TRTCAudioQuality.TRTCAudioQualityDefault);
						// this.isOpenAudio = true
						this.startLocalAudio()
						uni.showToast({
							title: '麦克风已打开',
							icon: 'none'
						})
					}
				} else {
					//如果不是房主，判断是否是麦上的人
					let indexs = this.guest.findIndex(item => item.userId == this.userId)
					if (indexs != -1) {
						//判断当前麦克风是否开启
						if (this.isOpenAudio) { //如果开启则关闭
							// this.$trtcCloud.stopLocalAudio();
							// this.isOpenAudio = false
							this.stopLocalAudio()
							uni.showToast({
								title: '麦克风已关闭',
								icon: 'none'
							})
						} else {
							//如果关闭则开启
							// this.$trtcCloud.startLocalAudio(this.$TRTCAudioQuality.TRTCAudioQualityDefault);
							// this.isOpenAudio = true
							this.startLocalAudio()
							uni.showToast({
								title: '麦克风已打开',
								icon: 'none'
							})
						}
					} else {
						uni.showToast({
							title: '你还不在麦上，无法开启或关闭麦克风',
							icon: 'none'
						})
					}
				}
			},
			//获取送礼物列表
			getGiveGiftList() {
				let data = {
					page: 1,
					limit: 10,
					roomId: this.roomId
				}
				this.$Request.getT('/app/gift/selectGiftRoomList', data).then(res => {
					if (res.code == 0) {
						this.list = []
						if (res.data.list.length > 0) {
							res.data.list.map(item => {
								let content = item.sendUserName + '赠送了' + item.giftName
								this.list.push(content)
							})
						} else {
							this.list.push('暂无记录')
						}

					}
				})
			},
			//选择送礼物的人
			selectUserGift(userId) {
				this.giftUserCurr = userId
			},
			//去充值
			goNavs(url) {
				uni.navigateTo({
					url: url
				})
			},
			// 我的余额
			getMoney() {
				this.$Request.get("/app/userMoney/selectMyMoney").then(res => {
					if (res.code == 0 && res.data) {
						this.money = res.data.money
					}
				});
			},
			//赠送礼物
			giveGift(giftId, item) {
				if (this.giftUserCurr == -1) {
					uni.showToast({
						title: '请选择打赏的对象',
						icon: 'none'
					})
					return
				}
				let roomUserId = ''

				if (this.giftUserCurr == this.homeownerInfo.userId) {
					//送给房主的
					roomUserId = this.byRoomUserId
				} else {
					//送给别人的
					roomUserId = this.guest[this.guest.findIndex(item => item.userId == this.giftUserCurr)].roomUserId
				}
				let data = {
					giftId: giftId,
					userId: this.giftUserCurr,
					roomUserId: roomUserId,
					roomId: this.roomId,
				}
				let that = this
				this.$Request.postJson('/app/gift/sendGift', data).then(res => {
					if (res.code == 0) {
						// uni.showToast({
						// 	title: '打赏成功',
						// 	icon: 'none'
						// })
						let data = {
							messageType: 5,
							type: 1,
							content: item
						}
						data = JSON.stringify(data)
						uni.sendSocketMessage({
							data: data,
							success(res) {
								console.log('走了吗？')
								that.showgift = false
								that.getMoney()
								that.getRoomUserList()
								that.getChatList()
							},
							fail(err) {
								// console.log(err);
							}
						})

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						setTimeout(() => {
							uni.navigateTo({
								url: '/my/wallet/index?index=1'
							})
						}, 1000)

					}
				})
			},
			// 切换礼物
			binddaS(item) {
				this.TabCurr = item.giftId
			},
			// 传进数组和指定个数，进行拆分
			chunk: function(array, size) {
				//获取数组的长度，如果你传入的不是数组，那么获取到的就是undefined
				const length = array.length
				//判断不是数组，或者size没有设置，size小于1，就返回空数组
				if (!length || !size || size < 1) {
					return []
				}
				//核心部分
				let index = 0 //用来表示切割元素的范围start
				let resIndex = 0 //用来递增表示输出数组的下标

				//根据length和size算出输出数组的长度，并且创建它。
				let result = new Array(Math.ceil(length / size))
				//进行循环
				while (index < length) {
					//循环过程中设置result[0]和result[1]的值。该值根据array.slice切割得到。
					result[resIndex++] = array.slice(index, (index += size))
				}
				//输出新数组
				return result
			},
			// 获取礼物列表
			getgift() {
				this.$Request.getT("/app/gift/selectGiftList", {
					status: 1
				}).then(res => {
					if (res.code == 0) {
						if (res.data.length > 0) {
							var datanew = this.chunk(res.data, 8)
							this.giftList = datanew;
							// console.log('giftList的数据', datanew)
							if (res.data.length > 4) {
								this.categoryHeight = "480rpx"
							} else {
								this.categoryHeight = "240rpx"
							}
						} else {
							var datanew = this.chunk(this.giftLists, 8)
							this.giftList = datanew;
						}
					}
				});
			},
			//主播邀请用户上麦
			inviteUserUp(item) {
				let that = this
				let num = this.guest.findIndex(item => item.userId == '')
				if (num == -1) {
					uni.showToast({
						title: '上麦人员已满',
						icon: 'none'
					})
				} else {
					that.showUsers = false
					that.contentPop = '是否邀请' + item.userName + '上麦？'
					that.byUserId = item.userId
					that.byNum = num
					console.log(num, '当前麦序')
					that.popuCurrent = 3
					that.popupshow = true
				}

			},
			//用户发起上麦
			userUpMicrophone(num) {
				let data = {
					type: 2,
					num: num
				}
				data = JSON.stringify(data)
				uni.sendSocketMessage({
					data: data,
					success(res) {

					},
					fail(err) {
						// console.log(err);
					}
				})
			},
			//用户发起连麦申请
			userLianmai(item, index) {
				let that = this
				//如果麦上有用户，那么就跳转到他的
				if (item.userId) { //跳转到该用户的用户详情
					if (that.homeownerInfo.userId == that.userId) {
						that.contentPop = '是否强制' + item.name + '下麦？'
						that.byUserId = item.userId
						that.popuCurrent = 4
						that.closeName = '取消'
						that.confirmName = '确认'
						that.popupshow = true
					} else if (item.userId == that.userId) {
						that.contentPop = '是否下麦？'
						that.popuCurrent = 5
						that.closeName = '取消'
						that.confirmName = '确认'
						that.popupshow = true
					} else {
						this.goUserInfoTo(item.userId)
					}
				} else { //发起上麦请求
					if (that.homeownerInfo.userId == that.userId) {
						that.showUsers = true
					} else {
						//如果不是房主，判断是否是麦上的人
						let indexs = this.guest.findIndex(item => item.userId == this.userId)
						if (indexs == -1) {
							that.contentPop = '是否申请上麦？'
							that.byNum = index
							that.closeName = '取消'
							that.confirmName = '确认'
							that.popuCurrent = 6
							that.popupshow = true
						}

					}

				}
			},
			//获取聊天记录
			getChatList() {
				let data = {
					roomId: this.roomId,
					page: 1,
					limit: 999
				}
				this.$Request.getT('/app/room/selectRoomChatList', data).then(res => {
					if (res.code == 0) {
						res.data.list.map(item => {
							if (item.content) {
								try {
									item.content = JSON.parse(item.content)
								} catch (e) {
									item.content = item.content
								}

							}
						})
						this.messageList = res.data.list.reverse()
						//获取一下送礼记录
						this.getGiveGiftList()
						//获取一下在线人数
						this.getUserList()
					}
				})
			},
			//发送消息
			sendMsgs() {
				let that = this;
				if (!that.content) {
					uni.showToast({
						title: '请输入聊天内容',
						icon: 'none'
					})
					return
				}
				if (this.$queue.getChatSearchKeys(this.content)) {
					uni.showToast({
						title: "输入内容带有非法关键字请重新输入",
						mask: false,
						duration: 1500,
						icon: "none"
					});
					this.$Request.postT('/app/risk/insertRisk?riskType=5&content=' + this.content).then(res => {

					})
					return;
				}
				let data = {
					content: that.content,
					messageType: 1,
					type: 1,
				}
				data = JSON.stringify(data);
				uni.sendSocketMessage({
					data: data,
					success(res) {
						console.log('发送成功44444')
						setTimeout(() => {
							that.getChatList();
						}, 100);
					},
					fail(err) {
						// console.log(err);
					}
				})
				this.content = '';
				this.isInputExpanded = false;
			},
			// 发送心跳包
			startHeartbeat() {
				// HeartBeat 这里是和后端约定好的值，我们发送给后端，后端再把这个值返给我们
				// 目的就是让后端知道我们一直在
				const heartbeatMsg = 'HeartBeat';
				const sendHeartbeat = () => {
					if (this.isOpen) {
						// console.log('发送HeartBeat')
						uni.sendSocketMessage({
							data: heartbeatMsg,
						}).catch(error => {
							console.log('发送心跳消息失败:', error);
							//发送消息失败则进行重新链接
							this.connect()
						});
					}
				};
				this.socketTimer = setInterval(sendHeartbeat, HEARTBEAT_INTERVAL);
			},
			// 开始重连
			reconnect() {
				if (!this.isOpen) {
					clearTimeout(this.socketTimer);
					this.socketTimer = setTimeout(() => {
						console.log('开始重连...');
						this.connect();
					}, RECONNECT_INTERVAL);
				}
			},
			//连接scoket
			connect() {
				let that = this

				uni.connectSocket({
					url: that.config("APIHOST5") + `/roomChatSocket/${that.userId}/${that.roomId}`,
					data() {
						return {
							msg: 'Hello'
						}
					},
					header: {
						'content-type': 'application/json'
					},
					method: 'GET',
					success(res) {
						// 这里是接口调用成功的回调，不是连接成功的回调，请注意
					},
					fail(err) {
						// 这里是接口调用失败的回调，不是连接失败的回调，请注意
					}
				})
				//连接打开
				uni.onSocketOpen((res) => {
					console.log('链接打开1111111')
					that.isOpen = true;
					that.startHeartbeat()
					that.$Request.getT('/app/user/selectUserById').then(res => {
						if (res.code == 0) {
							this.myUserInfo = res.data
							if (that.isOpen == true && that.myUserInfo
								.userName) {
								let upData = {
									messageType: 7,
									type: 1,
									content: that.myUserInfo.userName + '进入房间'
								}
								upData = JSON.stringify(upData);
								uni.sendSocketMessage({
									data: upData,
									success(res) {
										// console.log(upData, '发送成功')
									},
									fail(err) {
										// console.log(err);
									}
								})
							}
						}
					})
					// console.log(that.connected + '-' + that.connecting + '-' + that.myUserInfo.userName)

				})
				//连接异常
				uni.onSocketError((err) => {
					//如果连接异常那么把状态设置为false
					console.log('链接异常1111111')
					that.isOpen = false;
					//并尝试进行重连
					that.reconnect();
				})
				//连接关闭
				uni.onSocketClose((res) => {
					console.log('链接关闭1111111')
					that.isOpen = false;
					//链接关闭的时候也尝试进行重连
					// that.reconnect();
				})
				//接收消息
				uni.onSocketMessage((res) => {
					// console.log('收到消息1111111')
					// console.log(JSON.parse(res.data), 'scoket接收到的消息')
					if (res.data == 'HeartBeat') { //如果是心跳回推则不进行处理
						return
					}
					let getRes = JSON.parse(res.data)
					if (Number(getRes.type) == 21) { //封禁消息的通知
						if (getRes.user.userId == this.userId) { //判断是不是自己
							//是自己，则进行封禁逻辑（关闭scoket并退出房间）
							uni.showToast({
								title: '你已被封禁',
								icon: 'none'
							})
							setTimeout(() => {
								that.closeRoom()
							}, 1000)
							//走退房逻辑

						}
						//给所有人更新列表

					}
					if (Number(getRes.messageType) == 5) { //如果是礼物
						if (getRes.content.giftGif) {
							//如果有礼物特效则显示礼物特效
							that.giftGif = getRes.content.giftGif
							that.isShowGifGift = true
						}
					}
					if (Number(getRes.type) == 2) { //用户请求上麦
						if (that.userId == that.homeownerInfo.userId) { //如果是房主，则弹出请求弹框
							if (that.popuCurrent == 7 && that.popupshow == true) {
								//判断一下是否已经有上麦请求了，如果有则不在进行弹窗了直接return
								return
							}
							that.contentPop = getRes.user.userName + '请求上' + getRes.num + '号麦，是否同意？'
							that.byUserId = getRes.user.userId
							that.byNum = getRes.num
							that.popuCurrent = 7
							that.closeName = '拒绝'
							that.confirmName = '同意'
							that.isStartTime = true
							that.popupshow = true
						}
					} else if (Number(getRes.type) == 4) { //有上麦成功的人
						that.getRoomUserList()
						//如果上麦成功的人是自己，那么就打开麦克风权限
						if (getRes.user.userId == that.userId) {
							//切换用户角色为主播类型
							// that.$trtcCloud.switchRole(that.$TRTCRoleType.TRTCRoleAnchor)
							that.switchRole(TRTC.TYPE.ROLE_ANCHOR)
							//上麦后，开启麦克风
							// that.$trtcCloud.startLocalAudio(that.$TRTCAudioQuality.TRTCAudioQualityDefault);
							// that.isOpenAudio = true
							that.startLocalAudio()
						}
					} else if (Number(getRes.type) == 5) {
						uni.showToast({
							title: '主持已拒绝你的上麦请求！',
							icon: 'none'
						})
					} else if (Number(getRes.type) == 7 || Number(getRes.type) == 9) { //有用户下麦
						that.getRoomUserList()
						//如果下麦成功的人是自己，那么就打开麦克风权限
						if (getRes.user.userId == that.userId) {
							//切换用户角色为观众类型
							// that.$trtcCloud.switchRole(that.$TRTCRoleType.TRTCRoleAudience)
							that.switchRole(TRTC.TYPE.ROLE_AUDIENCE)
							//下麦后，关闭麦克风
							// this.$trtcCloud.stopLocalAudio();
							// that.isOpenAudio = false
							that.stopLocalAudio()
						}
					} else if (Number(getRes.type) == 11) {
						that.contentPop = '主持邀请你上' + getRes.num + '号麦，是否同意？'
						that.byNum = getRes.num
						console.log(that.byNum, '返回的麦序')
						that.popuCurrent = 8
						that.closeName = '拒绝'
						that.confirmName = '同意'
						that.isStartTime = true
						that.popupshow = true
					} else if (Number(getRes.type) == 13) {
						uni.showToast({
							title: '你的上麦邀请已被拒绝',
							icon: 'none'
						})
					} else if (Number(getRes.type) == 15) {
						that.getUserList()
					} else if (Number(getRes.type) == 17) {
						//不是房主的时候再提醒
						if (that.userId != that.homeownerInfo.userId) {
							that.contentPop = '该房间已解散，确认退出房间'
							that.popuCurrent = 1
							that.closeName = '取消'
							that.confirmName = '确认'
							that.popupshow = true
						}
					} else if (Number(getRes.type) == 19) {
						//19是管理端解散房间，通知所有人退出房间
						that.contentPop = '该房间已解散，确认退出房间'
						that.popuCurrent = 1
						that.closeName = '取消'
						that.confirmName = '确认'
						that.popupshow = true
					}
					// homeownerInfo.userId
					//每次接收到消息的时候刷新一下聊天列表
					that.getChatList()
					that.getRoomUserList()
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
			//获取房主信息
			getHomeownerInfo() {
				let data = {
					roomId: this.roomId
				}
				this.$Request.getT('/app/room/selectCreateRoomUser', data).then(res => {
					if (res.code == 0) {
						// console.log(res.data, '房主信息')
						this.homeownerInfo = res.data
						if (this.userId == this.homeownerInfo.userId) {
							//如果是房主，默认开启麦克风权限
							console.log('如果是房主，默认开启麦克风权限')
							this.startLocalAudio()
						}
					}
				})
			},
			//角色切换
			async switchRole(event) {
				await this.trtc.switchRole(event);

			},
			//开启麦克风
			async startLocalAudio() {
				//开启麦克风
				await this.trtc.startLocalAudio();
				this.isOpenAudio = true
			},
			//关闭麦克风
			async stopLocalAudio() {
				//关闭麦克风
				await this.trtc.stopLocalAudio();
				this.isOpenAudio = false
			},
			//分页
			getMoreUserList() {
				if (this.page < this.pages) {
					this.page += 1
					this.getUserList()
				}
			},
			searchUserList() {
				this.page = 1
				this.getUserList()
			},
			//获取房间所有人的列表
			getUserList() {
				let data = {
					page: this.page,
					limit: this.limit,
					userName: this.searchName,
					roomId: this.roomId,
					status: 1
				}
				this.$Request.getT('/app/room/selectRoomUserList', data).then(res => {
					if (res.code == 0) {
						// console.log(res, '所有人的列表')
						this.assemble = res.data.totalCount
						this.pages = res.data.totalPage
						if (this.page == 1) {
							this.userLIst = res.data.list
						} else {
							this.userList = [...this.userList, ...res.data.list]
						}
					}
				})
			},
			//获取房间信息
			getRoomInfo() {
				let data = {
					roomId: this.roomId
				}
				this.$Request.getT('/app/room/selectRoomDetails', data).then(res => {
					if (res.code == 0) {
						this.roomInfo = res.data
						// console.log(this.roomInfo, '房间信息')
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						uni.navigateBack()
					}
				})
			},
			//获取房间上麦人员列表
			getRoomUserList() {
				let data = {
					page: 1,
					limit: 8,
					roomId: this.roomId,
					isSeat: 1,
					status: 1
				}
				this.$Request.getT('/app/room/selectRoomUserList', data).then(res => {
					if (res.code == 0) {
						// console.log(res, '上麦人员')
						// guest
						this.guest = [{
							userId: '',
							avatar: '', //用户头像
							name: '',
							num: 0, //礼物数量
							sex: 1, //性别
							roomUserId: '',
						}, {
							userId: '',
							avatar: '', //用户头像
							name: '',
							num: 0, //礼物数量
							sex: 1, //性别
							roomUserId: '',
						}, {
							userId: '',
							avatar: '', //用户头像
							name: '',
							num: 0, //礼物数量
							sex: 1, //性别
							roomUserId: '',
						}, {
							userId: '',
							avatar: '', //用户头像
							name: '',
							num: 0, //礼物数量
							sex: 1, //性别
							roomUserId: '',
						}, {
							userId: '',
							avatar: '', //用户头像
							name: '',
							num: 0, //礼物数量
							sex: 1, //性别
							roomUserId: '',
						}, {
							userId: '',
							avatar: '', //用户头像
							name: '',
							num: 0, //礼物数量
							sex: 1, //性别
							roomUserId: '',
						}, {
							userId: '',
							avatar: '', //用户头像
							name: '',
							num: 0, //礼物数量
							sex: 1, //性别
							roomUserId: '',
						}, {
							userId: '',
							avatar: '', //用户头像
							name: '',
							num: 0, //礼物数量
							sex: 1, //性别
							roomUserId: '',
						}] //嘉宾数量
						this.zongNum = 0
						if (res.data.list.length > 0) {
							res.data.list.map((item, index) => {
								if (item.seatNo != 0) {
									switch (item.seatNo) {
										case 1: //一号麦
										case 2: //二号麦
										case 3: //三号麦
										case 4: //四号麦
										case 5: //五号麦
										case 6: //六号麦
										case 7: //七号麦
										case 8: //八号麦
											this.guest[item.seatNo - 1].userId = item.userId
											this.guest[item.seatNo - 1].avatar = item.avatar
											this.guest[item.seatNo - 1].name = item.userName
											this.guest[item.seatNo - 1].sex = item.sex
											this.guest[item.seatNo - 1].num = item.giftCount ? item
												.giftCount : 0
											this.guest[item.seatNo - 1].roomUserId = item.roomUserId
											break;
										default:
											break;
									}
								} else {
									this.zongNum = item.giftCount ? item.giftCount : 0
									this.byRoomUserId = item.roomUserId
								}

							})
							// console.log(res.data.list, '麦上的人员列表')
						}
					}
				})
			},
			//获取个人信息
			getUserInfo() {
				this.$Request.getT('/app/user/selectUserById').then(res => {
					if (res.code == 0) {
						this.myUserInfo = res.data
					}
				})
			},
			// 进入房间的的接口
			requestJoinRoom() {
				let data = {
					roomId: this.roomId
				}
				this.$Request.postJson('/app/room/insertRoomUser', data).then(res => {
					if (res.code == 0) {
						this.roomUserId = res.data.roomUserId
						// uni.showToast({
						// 	title: '进入房间成功',
						// 	icon: 'none'
						// })
						//获取房间信息
						this.getRoomInfo()
						//获取房主信息
						this.getHomeownerInfo()
						//获取所有人员列表
						this.getUserList()
						//获取上麦人员列表
						this.getRoomUserList()
						//获取礼物列表
						this.getgift()

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//退出房间的接口
			requestExiteRoom() {
				let data = {
					roomUserId: this.roomUserId,
					status: 2
				}
				// console.log(data, '退房参数')
				let that = this
				this.$Request.postJson('/app/room/updateRoomUser', data).then(res => {
					if (res.code == 0) {
						// console.log('走这儿了，退房接口')
						if (this.userId == this.homeownerInfo.userId) { //房主退房
							let upData = {
								type: 16,
							}
							upData = JSON.stringify(upData);
							uni.sendSocketMessage({
								data: upData,
								success(res) {
									//关闭scoket连接
									uni.closeSocket()
									that.isOpen = false
									clearInterval(that.socketTimer)
									that.socketTimer = null
								},
								fail(err) {
									// console.log(err);
								}
							})
						} else { //普通人退房
							let upData = {
								type: 14,
							}
							upData = JSON.stringify(upData);
							uni.sendSocketMessage({
								data: upData,
								success(res) {
									// console.log('走退房，关闭了scoket')
									//关闭scoket连接
									uni.closeSocket()
									that.isOpen = false
									clearInterval(that.socketTimer)
									that.socketTimer = null
								},
								fail(err) {
									// console.log(err);
								}
							})
						}
						//关闭scoket连接
						uni.closeSocket()
						that.isOpen = false
						clearInterval(that.socketTimer)
						that.socketTimer = null
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						//关闭scoket连接
						uni.closeSocket()
						that.isOpen = false
						clearInterval(that.socketTimer)
						that.socketTimer = null
					}
				})
			},
			//监听进房的回调
			/**
			 * 初始化TrtcCloud
			 * 进入房间
			 */
			async initAudio() {
				try {
					let data = {
						strRoomId: this.strRoomId, //房间id 这里需要区分数字与字符串，比如 ’123‘和123不是同一个房间
						roomId: 0, //房间id
						scene: 'rtc', //rtc实时通话模式 live在线直播模式
						sdkAppId: Number(this.sdkAppId),
						userId: uni.getStorageSync('userId') + '',
						userSig: this.userSig,
						// role: 'anchor', //anchor:“主播”角色，可以推流和拉流。 audience:“观众”角色，只能拉流观看，无法推流。
						role: this.role == 'TRTCRoleAnchor' ? 'anchor' : 'audience'
					}
					await this.trtc.enterRoom(data);
					console.log('进房成功');
				} catch (error) {
					console.error('进房失败 ' + error);
				}
			},
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #050506;
	}

	.giftEffect {
		width: 500rpx;
		position: fixed;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		z-index: 9999;
	}

	.jubao {
		background-color: #050506;
		padding: 20rpx 40rpx;
	}

	.jubao-item {
		margin-bottom: 20rpx;
		padding-left: 20rpx;
	}

	.jubao-item-up {
		width: 200rpx;
		height: 200rpx;
		border-radius: 12rpx;

		image {
			width: 100%;
			height: 100%;
			border-radius: 18rpx;
		}
	}

	.jubao-title-inp {
		background: linear-gradient(90deg, #1C1C22 0%, #101014 100%);
		padding: 0 20rpx !important;
		border-radius: 18rpx;
	}

	.jubao-btn {
		width: calc(100% - 100rpx);
		height: 88rpx;
		background-color: #FBCA0C;
		border-radius: 44rpx;
		color: #0A0A0C;
		margin: 0 auto;
		margin-bottom: 40rpx;
	}

	.popupbox {
		// background: #101014;
		background: linear-gradient(to right bottom, #1C1C22, #101014);

		// padding: 0upx 20upx;
	}

	.popupboxbgs {
		padding: 0 20rpx;
		padding-bottom: 16rpx;
		border-radius: 14rpx 14rpx 0 0;
		background: linear-gradient(110deg, rgba(255, 142, 206, 0.1), rgba(171, 116, 255, 0.1));

	}

	.giftUserItem {
		display: inline-block;
		padding: 10rpx 0;
	}

	.selectGiftUser {
		border: 1px solid #FBCA0C;
	}

	.giftUserItems {
		position: relative;
		margin-right: 20rpx;

		.giftUserItemsW {
			width: 75rpx;
			height: 75rpx;
			border-radius: 50%;


			image {
				width: 70rpx;
				height: 70rpx;
				border-radius: 50%;
			}
		}


		.giftUserItemsNo {
			width: 30rpx;
			height: 30rpx;
			position: absolute;
			border-radius: 50%;
			left: 50%;
			bottom: -10rpx;
			transform: translate(-50%, 0);

		}
	}

	.popupbtn {
		background: linear-gradient(-90deg, #FBCA0C 0%, #D9A800 100%);
		color: #0A0A0C;
		border-radius: 40upx;
		padding: 15upx 45upx;
		/* margin-top: 40upx; */
	}

	.popbox {
		/* width: 25%; */
		width: 160rpx;
		height: 200rpx;
		text-align: center;
		margin-bottom: 30rpx;
	}

	.actives {
		display: none;
	}

	.active {
		background: linear-gradient(to right bottom, rgba(243, 134, 189, 0.1), rgba(164, 112, 248, 0.1));
		// background: #32224a;
		// background-image: url('https://pw.xianmxkj.com/file/uploadPath/2023/03/21/1118a7b01f1c09f652baa505da9994fd.png');
		// background-repeat: no-repeat;
		// background-size: 100% 100%;
		border-radius: 15upx;
	}

	.zengsong {
		width: 100%;
		/* height: 56px; */
		background: linear-gradient(-90deg, #FBCA0C 0%, #D9A800 100%);
		border-radius: 0px 0px 15upx 15upx;
		padding: 10upx 0;
		margin: 24rpx 0px;

	}

	.category {
		width: 100%;

		.box {
			width: 100%;
			border-radius: 20upx;
			// background-color: #ffffff;

			.dots {
				display: flex;
				justify-content: center;
				height: 15upx;
				width: 100%;

				view {
					width: 30upx;
					height: 5upx;
					background-color: rgba(0, 0, 0, 0.2);

					&.active {
						background-color: #ff570a;
					}
				}
			}

			.swiper {
				width: 100%;
				padding: 10upx 0;

				.uni-swiper-dot {
					width: 20upx;
				}

				.category-list {
					width: 100%;
					height: auto;
					display: flex;
					padding: 10upx 0;
					flex-flow: wrap;

					.iconss {

						image {
							width: 70%;
							height: 13.3vw;
						}

						view {
							// width: 100%;
							// display: flex;
							// justify-content: center;
						}

						.remen,
						.lijian {
							width: 60upx;
							height: 30upx;
							position: absolute;
							top: 0;
							right: 0;
						}
					}
				}
			}
		}
	}

	.notices {
		width: 100%;
		color: #FFFFFF;
		background-color: #050506;

		.notices-title {
			width: 100%;
			text-align: center;
			padding-top: 30rpx;
			font-weight: bold;
		}

		.notices-content {
			min-height: 200rpx;
			padding: 20rpx;
		}
	}

	.allUserList {
		width: 100%;
		height: 100vh;
		background-color: #050506;
		position: relative;

		.allUserList-close {
			position: absolute;
			bottom: 100rpx;
			right: 30rpx;

			image {
				width: 150rpx;
			}
		}

		.allUserList-box {
			width: calc(100% - 60rpx);
			height: 100%;
		}

		.allUserList-list {
			width: 100%;
			overflow: hidden;
		}

		.allUserList-list-item {
			margin-bottom: 20rpx;

			.allUserList-list-item-l {
				image {
					width: 60rpx;
					height: 60rpx;
					border-radius: 50%;
					margin-right: 20rpx;
				}
			}

			.allUserList-list-item-name {
				width: 280rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
				-o-text-overflow: ellipsis;

			}

			.allUserList-list-item-up {
				background-color: rgba(164, 112, 248, 0.4);
				padding: 4rpx 10rpx;
				border-radius: 20rpx;
				margin-left: 10rpx;
			}
		}
	}

	.msgTitile {
		color: #FFFFFF;
		font-weight: bold;
		background-color: #050506;
		padding: 20rpx 30rpx 0 40rpx;
		// height: 100rpx;
		font-size: 40rpx;
	}

	.myMsg {
		width: 100%;
		height: 700rpx;
		background-color: #050506;
		padding: 20rpx 0;
	}

	.bgImg {
		width: 100%;
		height: 100vh;
		position: fixed;
		top: 0;
		left: 0;
	}

	.title {
		font-size: 26rpx;
		font-weight: 500;

		.title-btn {
			padding: 2rpx 10rpx;
			background: linear-gradient(90deg, #FBCA0C 0%, #D9A800 100%);
			border-radius: 24rpx;
			border: 1px solid #FFFFFF;
			margin-left: 10px;
			font-size: 24rpx;
		}
	}

	.info {
		font-size: 22rpx;
		margin-top: 4rpx;

		.info-line {
			padding: 0 10rpx;
		}

		.info-vipNum {
			color: #FFBD56;
			margin-left: 16rpx;
		}
	}

	.content {
		width: 100%;
		position: fixed;
		left: 0;

		.content-box {
			width: 706rpx;
			height: 100%;
		}
	}

	.userList-l {
		// margin-right: -30rpx;

		.userList-l-item {
			width: 69rpx;
			height: 65rpx;
			border-radius: 50%;
			margin-left: 10rpx;
			position: relative;
			margin-right: -30rpx;

			.userList-l-item-t {
				width: 69rpx;
				height: 65rpx;
				border-radius: 50%;
			}

			.userList-l-item-a {
				position: absolute;
				top: 55%;
				left: 59%;
				transform: translate(-50%, -50%);
				width: 46rpx;
				height: 46rpx;
				border-radius: 50%;
				// display: none;
			}

		}

	}

	.userList-c {
		height: 54rpx;
		background-color: rgba(0, 0, 0, 0.3);
		border-radius: 23rpx;
		font-size: 22rpx;
		padding: 0 10rpx 0 40rpx;
		margin-right: 10rpx;
		margin-top: 10rpx;
	}

	.userList-r {
		font-size: 40rpx;
		font-weight: bold;
	}

	.userList {
		width: 100%;
		height: 55rpx;




	}

	.notice {
		width: 100%;

		.notice-l {
			width: auto;
			height: 100%;

			.notice-l-item {
				background-color: rgba(164, 112, 248, 0.4);
				margin-right: 20rpx;
				font-size: 24rpx;
				padding: 8rpx 16rpx;
				border-radius: 24rpx;
			}
		}

		.notice-r {
			width: auto;
			position: absolute;
			right: 0;
			font-size: 24rpx;
			background: linear-gradient(90deg, rgba(243, 134, 189, 0.4) 0%, rgba(164, 112, 248, 0.4) 100%);
			border-radius: 24rpx 0rpx 0rpx 24rpx;
			padding: 8rpx 16rpx;
		}
	}

	@keyframes ansmos {
		0% {
			width: 0;
			height: 0;
			left: 105rpx;
			top: 105rpx;
			opacity: 0;
		}

		50% {
			width: 180rpx;
			height: 180rpx;
			left: 15rpx;
			top: 16rpx;
			opacity: 0.7;
		}

		100% {
			width: 210rpx;
			height: 210rpx;
			left: 0;
			top: 0;
			opacity: 0;
		}
	}

	@keyframes ansmosAudio {
		0% {
			width: 0;
			height: 0;
			left: 70rpx;
			top: 50rpx;
			opacity: 0;
		}

		50% {
			width: 135rpx;
			height: 135rpx;
			left: 8rpx;
			top: -13rpx;
			opacity: 0.7;
		}

		100% {
			width: 140rpx;
			height: 140rpx;
			left: 6rpx;
			top: -15rpx;
			opacity: 0;
		}
	}

	.upMike {
		margin-top: 20rpx;

		.upMike-master {
			.upMike-master-imgs {
				width: 210rpx;
				height: 210rpx;
				border-radius: 50%;
				position: relative;
				// background-color: red;

				.upMike-master-imgs1 {
					border: 6rpx solid #D9A800;
					position: absolute;
					border-radius: 50%;
					// opacity: 0;
					animation: 2s ansmos infinite;
				}

				.upMike-master-imgs2 {
					animation-delay: 1s !important;
				}

				.upMike-master-imgs-a {
					width: 120rpx;
					height: 120rpx;
					border-radius: 50%;
					position: absolute;
					top: 50%;
					left: 50%;
					transform: translate(-50%, -50%);
					z-index: 2;
				}

				.upMike-master-imgs-z {
					width: 154rpx;
					height: 154rpx;
					border-radius: 50%;
					position: absolute;
					top: 50%;
					left: 50%;
					transform: translate(-50%, -50%);
					z-index: 3;
				}



				.upMike-master-imgs-s {
					width: 180rpx;
					height: 180rpx;
					border-radius: 50%;
					position: absolute;
					// top: 0;
					// left: 0;
					z-index: 1;
				}
			}

			.upMike-master-name {
				width: 100%;
				text-align: center;
				font-size: 28rpx;
				font-weight: bold;
			}

			.upMike-master-gift {
				width: 100%;
				text-align: center;

				.upMike-master-gifts {
					background-color: rgba(0, 0, 0, 0.3);
					border-radius: 15rpx;
					padding: 4rpx 10rpx;
					color: #CCCCCC;
					font-size: 22rpx;
				}
			}
		}

		.upMike-guest {
			.upMike-guest-item {
				width: 150rpx;
				// text-align: center;
				margin-top: 20rpx;
				position: relative;

				.upMike-guest-audio {
					position: absolute;
					border: 6rpx solid #D9A800;
					border-radius: 50%;
					animation: 3s ansmosAudio infinite;
					z-index: 1;

				}

				.upMike-guest-audio2 {
					animation-delay: 1.5s !important;
				}

				.upMike-guest-item-img {
					width: 110rpx;
					height: 110rpx;
					border-radius: 50%;
					margin: 0 auto;
					z-index: 100;
				}

				.upMike-guest-item-imgs {
					width: 110rpx;
					height: 110rpx;
					border-radius: 50%;
					border: 1px dashed #FFD85D;
					margin: 0 auto;
				}

				.upMike-guest-item-name {
					font-size: 24rpx;
					margin-top: 10rpx;

					.upMike-guest-item-names {
						width: 26rpx;
						height: 26rpx;
						border-radius: 50%;
						background-color: #FBCA0C;
						margin-right: 10rpx;
					}

					.upMike-guest-item-namet {
						max-width: 110rpx;
						overflow: hidden;
						white-space: nowrap;
						text-overflow: ellipsis;
						-o-text-overflow: ellipsis;
					}


				}

				.upMike-guest-item-gift {
					margin-top: 10rpx;
				}

				.upMike-guest-item-gifts {
					background-color: rgba(0, 0, 0, 0.3);
					border-radius: 15rpx;
					padding: 4rpx 10rpx;
					color: #CCCCCC;
					font-size: 22rpx;
				}
			}
		}

		.upMike-giftList {
			width: 100%;
			margin-top: 20rpx;

			.upMike-giftLists {
				width: 490rpx;
				height: 40rpx;
				background: rgba(255, 224, 136, 0.17);
				border: 1px solid rgba(255, 224, 121, 0.58);
				border-radius: 20rpx;
			}

			.upMike-giftLists-l {
				width: 152rpx;
				height: 100%;
				font-size: 22rpx;
				color: #FFE793;
				background: rgba(255, 224, 136, 0.17);
				border: 1px solid rgba(255, 224, 121, 0.58);
				border-radius: 20rpx;
			}

			.upMike-giftLists-r {
				width: 308rpx;
				height: 100%;

				.upMike-giftLists-rs {
					width: calc(100% - 40rpx);
					height: 100%;
				}
			}
		}
	}

	.msgList {
		width: 100%;
		height: calc(100% - 840rpx);
		margin-top: 20rpx;
		overflow: hidden;

		.msgList-gg-l {
			background-color: rgba(94, 58, 120, 0.4);
			border-radius: 16rpx;
			padding: 20rpx;
			font-size: 28rpx;

		}

		.msgList-gg-r {
			height: fit-content;
			background-color: rgba(94, 58, 120, 0.4);
			border-radius: 16rpx;
			padding: 20rpx;
			font-size: 28rpx;
		}

		.msgList-gg-txt {
			margin-top: 5rpx;

			image {
				width: 78rpx;
				height: 20rpx;
			}
		}

		.msgList-tips {
			width: fit-content;
			height: fit-content;
			margin-top: 20rpx;
			background-color: rgba(94, 58, 120, 0.4);
			border-radius: 16rpx;
			padding: 20rpx;
			font-size: 28rpx;

			image {
				width: 60rpx;
				height: 30rpx;
				margin-right: 10rpx;
			}
		}

		.msgList-ts {
			width: fit-content;
			height: fit-content;
			margin-top: 20rpx;
			background-color: rgba(94, 58, 120, 0.4);
			border-radius: 16rpx;
			padding: 20rpx;
			font-size: 28rpx;
			color: #FFE795;

		}

		.msgList-item {
			margin-top: 20rpx;

			image {
				width: 74rpx;
				height: 74rpx;
				border-radius: 50%;
			}

			.msgList-item-info {
				margin-left: 18rpx;
				font-size: 28rpx;

				.msgList-item-info-con {
					background-color: rgba(94, 58, 120, 0.4);
					padding: 20rpx;
					border-radius: 16rpx;
					margin-top: 6rpx;

					image {
						width: 90rpx;
						height: 90rpx;
						margin-left: 20rpx;
					}
				}
			}
		}
	}

	// 修改样式部分
	.mssage-container {
		width: 706rpx;
		position: fixed;
		bottom: 28rpx;
		left: 50%;
		transform: translateX(-50%);
		z-index: 999;
		height: 110rpx;
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}

	.mssage {
		width: 100%;
		height: 110rpx;
		border: 1px solid #FBCA0C;
		background: linear-gradient(90deg, #221A06 0%, #101014 99%);
		border-radius: 32rpx;
		transition: all 0.3s ease;
		position: absolute;
		right: 0;
		transform-origin: right center;
	}

	.mssage-expanded {
		width: 100%;
		height: 110rpx;
		position: relative;
	}

	.mssage-collapsed {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: linear-gradient(90deg, #221A06 0%, #101014 99%);
		border: 1px solid #FBCA0C;
		z-index: 10;
		position: relative;

		image {
			width: 80rpx;
			height: 80rpx;
		}
	}

	.messages {
		width: 190rpx;
		height: 80rpx;
	}

	.mssage-item {
		height: 80rpx;
		margin: 0 10rpx;

		image {
			width: 80rpx;
			height: 80rpx;
		}
	}

	// .mssage {
	// 	width: 706rpx;
	// 	height: 110rpx;
	// 	border: 1px solid #FBCA0C;
	// 	background: linear-gradient(90deg, #221A06 0%, #101014 99%);
	// 	border-radius: 32rpx;
	// 	// margin-top: 20rpx;
	// 	position: fixed;
	// 	bottom: 28rpx;

	// 	.messages {
	// 		width: 190rpx;
	// 		height: 80rpx;
	// 	}

	// 	.mssage-item {
	// 		height: 80rpx;

	// 		image {
	// 			width: 80rpx;
	// 			height: 80rpx;
	// 		}
	// 	}

	// }
</style>