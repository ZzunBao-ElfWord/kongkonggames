<template>
	<div>
		<div style="margin:2% 0;display: inline-block;">
			<span>房间名称：</span>
			<el-input style="width: 160px;" @keydown.enter.native="select" clearable placeholder="请输入房间名称"
				v-model="storeName"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div style="margin:2% 0;display: inline-block;">
			<span>创建用户昵称：</span>
			<el-input style="width: 160px;" @keydown.enter.native="select" clearable placeholder="请输入创建用户昵称"
				v-model="userNameT"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div style="display: inline-block;">
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
				@click="select">查询</el-button>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
				@click="cleans">重置</el-button>
			<!-- <el-button style='margin-left:15px;' size="mini" type="warning" icon="document"
				@click="cleansJb">举报记录</el-button> -->
		</div>
		<div style="display: inline-block;">
			<el-button style='margin-right:15px;position: absolute;right: 20px;top: 42px;' size="mini" type="warning" icon="document"
				@click="cleansJb">举报记录</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.list">
			<el-table-column fixed="left" prop="roomId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="userName" label="创建用户昵称" fixed="left">
				<template slot-scope="scope">
					　<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)"
						v-text="scope.row.userName"></span>
				</template>
			</el-table-column>
			<el-table-column prop="roomType" label="类型" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.roomType==1">娱乐</span>
					<span v-if="scope.row.roomType==2">陪玩</span>
				</template>
			</el-table-column>
			<el-table-column prop="roomName" fixed="left" label="房间名称" width="120">
			</el-table-column>
			<el-table-column prop="roomImg" label="封面图片">
				<template slot-scope="scope">
					<span v-if="scope.row.roomImg == null">暂无图片</span>
					<img v-else :src="scope.row.roomImg" alt="" width="40" height="40">
				</template>
			</el-table-column>

			<el-table-column prop="gameName" label="游戏类型">
			</el-table-column>
			<el-table-column prop="playingMethod" label="游戏玩法">
			</el-table-column>
			<el-table-column prop="announcement" label="房间公告">
			</el-table-column>
			<el-table-column prop="roomNo" label="房间编号">
			</el-table-column>
			<el-table-column prop="status" label="状态">
				<template slot-scope="scope">
					<span v-if="scope.row.status==1" style="color:#13ce66">开启</span>
					<span v-if="scope.row.status==2" style="color:#ff4949">关闭</span>
					<!-- <el-switch v-model="scope.row.status" @change="change(scope.row.id,scope.row.status)"
						:disabled="!isAuth('locality:update')" :active-value="openValue2" :inactive-value="closeValue2"
						active-color="#13ce66" inactive-color="#ff4949">
					</el-switch> -->
				</template>
			</el-table-column>
			<!-- <el-table-column prop="tearoomCoverImage" label="房间轮播图" width="200">
      	<template slot-scope="scope">
      		<div v-if="scope.row.tearoomCoverImage">
      			<span v-for="(item,index) in scope.row.tearoomCoverImage.split(',')">
      				<el-popover placement="top-start" title="" trigger="hover">
      					<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
      					<img style="width: 300px; height: 300px" :src="item" alt="">
      				</el-popover>
      			</span>
      		</div>
      	</template>
      </el-table-column> -->
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column label="操作" width="120" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="updatesJiankong(scope.row)">房间监控
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
			</el-pagination>
		</div>
		<!-- 房间监控 -->
		<el-dialog title="房间监控" :visible.sync="dialogFormVisibleJk" @close="closeJiankong()" center width="1000px">
			<!-- <chat-room :dataRoom="dataRoom"></chat-room> -->
			<div style="position: relative;">
				<div style="font-size: 18px;">
					<span>房间名：{{dataRoom.roomName}}</span>
				</div>
				<div>
					<span>ID:{{dataRoom.roomNo}} | 在线：<span style="color: orangered;cursor: pointer;"
							@click="peopleBtn()">{{chatListNum}} </span> 人 </span>
					<!-- 贵宾:<span style="color: red;">24</span>人 -->
					<el-button size="mini" type="primary" plain round style="padding: 2px 8px"
						@click="ggBtn()">房间公告</el-button>
					<el-button size="mini" type="warning" plain round style="padding: 2px 8px"
						@click="wfBtn()">房间玩法</el-button>
				</div>
				<el-button icon="document" v-if="dataRoom.status==1" size="mini" plain style='margin:5px;position: absolute;right: 20px;top: 0px;' type="danger"
					@click="deleteRoom()" :disabled="!isAuth('chattingRoom:jiesan')">解散房间
				</el-button>
				<div>
					<!-- 房主 -->
					<el-row style="margin: 15px 0;">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">
										<img :src="fangzhuData.avatar" class="imageName" />
										<img src="../../assets/img/fz.png" class="imageName"
											style="position: absolute;top: -8px;left: -8px;width: 65px;height: 65px;" />
									</div>
									<div class="text_color">{{fangzhuData.userName}}</div>
									<div>
										<img src="../../assets/img/lw.png" class="imageName"
											style="width: 13px;height: 13px;border-radius: 0;" />
									{{fangzhuData.giftCount?fangzhuData.giftCount:0}}
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
					<!-- 连麦列表 -->
					<div style="font-size: 16px;">连麦列表</div>
					<el-row style="margin: 15px 0;">
						<el-col :span="3" class="cards" v-for="(item,index) in chatListSm" :key="index">
							<div class="box">
								<div class="box_num">
									<div class="box_color">
										<img :src="item.avatar" style="width: 70px;height: 70px;" :class="userIds==item.userId?'images':''" />
										<i class="el-icon-phone-outline" v-if="userIds==item.userId" style="position: absolute;bottom: -8px;right: 40px;color: #0eb0e1;"></i>
									</div>
									<div class="text_color">{{item.userName}}</div>
									<div>
										<img src="../../assets/img/lw.png" class="imageName"
											style="width: 13px;height: 13px;border-radius: 0;" />
										{{item.giftCount?item.giftCount:0}}
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</div>
			</div>
			<!-- 底部聊天 -->
			<div class="whole-wrapper">
				<!-- 中间的对话-->
				<div class="center_whole_wrapper">
					<div class="center_wrapper">
						<!-- 中间的聊天框，尝试模拟数据 -->
						<div class="center_wrapper_center" id="ele" style="overflow-y: auto" ref="dialogue_box">
							<!-- <div class="item loading">{{loadText}}<span>{{"第" + pageNum + "页"}}</span></div> -->
							<!-- <div v-for="(item, index) in dialogueList" :key="index" :class="center_wrapper_center_item"> -->
								<div v-for="(item, index) in dialogueList" :key="index" :class="
								  item.userId != 0? center_wrapper_center_item: center_wrapper_center_item_reserve">
								<div class="left-item-head" v-if="item.userId === 0">
									<img v-if="item.avatar !== null" :src="item.avatar" />
									<img v-else-if="item.avatar == null" src="../../assets/img/head.png" />
									<img v-else src="../../assets/img/head.png" />
								</div>
								<div class="left-item-head" v-else>
									<img v-if="item.avatar !== null" :src="item.avatar" />
									<img v-else-if="item.avatar == null" src="../../assets/img/head.png" />
									<img v-else src="../../assets/img/head.png" />
								</div>

								<!-- 文字 -->
								<div class="center-item-info_wrapper" v-if="item.messageType == 1">
									<div class="center-item-tip">

										<div class="center-item-info">
											{{ item.content }}
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<!-- 图片 -->
								<div class="center-item-info_wrapper" v-if="item.messageType == 2">
									<div class="center-item-tip">
										<div>
											<el-popover placement="top-start" title="" trigger="hover">
												<img style="width: 100px; height: 100px" :src="item.content" alt=""
													slot="reference" />
												<img style="width: 300px; height: 300px" :src="item.content" alt="" />
											</el-popover>
											<!-- <img :src="item.content" alt="" width="100" height="100">					 -->
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<!-- 语音 -->
								<div class="center-item-info_wrapper" v-if="item.messageType == 3">
									<div class="center-item-tip">
										<div>
											<audio :id="'myAudio'+item.roomChatId" controls duration="none">
												<source :src="item.content" type="audio/mpeg">
												您的浏览器不支持 audio 元素。
											</audio>
											<!-- <img :src="item.content" alt="" width="100" height="100">					 -->
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<!-- 表情包 -->
								<div class="center-item-info_wrapper" v-if="item.messageType == 4">
									<div class="center-item-tip">
										<div>
											<img :src="item.content" alt="" width="50" height="50">
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<!-- 礼物 -->
								<div class="center-item-info_wrapper" v-if="item.messageType == 5">
									<div class="center-item-tip">
										<div>
											<img :src="item.content" alt="" width="50" height="50">
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<!-- 订单 -->
								<div class="center-item-info_wrapper" v-if="item.messageType == 6">
									<div class="center-item-tip">
										<div>
											<img :src="item.content" alt="" width="50" height="50">
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<!-- 进入直播间 -->
								<div class="center-item-info_wrapper" v-if="item.messageType == 7">
									<div class="center-item-tip">
										<div>
											欢迎 <span style="color: rgb(207 131 131);">{{item.userName}}</span> 进入房间~
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
							</div>
							<!-- <a id="mao2"></a> -->
						</div>
						<!-- 下面的输入框，发送 -->
						<div class="center_wrapper_right">
							<div class="icon-list">
								<!-- 表情 -->
								<div class="icon-item" title="表情">
									<el-popover v-model="emojiShow" placement="top-start" width="400" trigger="click"
										class="emoBox">
										<div class="emotionList">
											<a href="javascript:void(0);" @click="getEmo(index)"
												v-for="(item,index) in faceList" :key="index"
												class="emotionItem">{{item}}</a>
										</div>
										<el-button style="background: transparent;border: none;" class="emotionSelect"
											icon="iconfont icon-biaoqing" slot="reference">😁</el-button>
									</el-popover>
								</div>
								<!-- 上传文件 -->
								<div class="icon-item">
									<el-upload class="avatar-uploader" v-model="coverImg"
										:action="$http.adornUrl('alioss/upload')" :show-file-list="false"
										:on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
										<div class="icon-item" title="发送文件">
											<img class="icon-item-img" src="~@/assets/img/wenjian.png" />
										</div>
									</el-upload>
								</div>
							</div>
							<el-input type="textarea" id="textarea" :rows="2" placeholder="请输入内容" v-model="content"
								@keyup.enter.native="sendTextarea(1)"></el-input>
							<el-button type="primary" style="font-size: 12px" v-if="content" @click="sendTextarea(1)">发送
							</el-button>
							<el-button type="primary" style="font-size: 12px" v-else disabled
								@click="sendTextarea(1)">发送
							</el-button>
						</div>
					</div>
				</div>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="closeJiankong()">关闭弹框</el-button>
			</div>
		</el-dialog>
		<!-- 公告 玩法 弹框 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisibleshebieG" center :modal="false" width="500px"
			top="20vh">
			<div style="margin-bottom: 10px;">
				<el-input type="textarea" id="textarea" :rows="5" placeholder="请输入" v-model="contentGg"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleshebieG = false">取 消</el-button>
				<el-button type="primary" @click="disposeGg()">修 改</el-button>
			</div>
		</el-dialog>
		<el-dialog title="在线用户" :visible.sync="dialogFormVisibleU" center width="85%" :modal="false">
			<el-table v-loading="tableDataLoading" :data="chatList" height="600px">
				<el-table-column label="编号" prop="roomUserId" width="80" fixed="left">
				</el-table-column>
				<el-table-column label="用户昵称" prop="realName">
					<template slot-scope="scope">
						<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">{{
								scope.row.userName ? scope.row.userName : '未绑定'
							}}</span>
					</template>
				</el-table-column>
				<el-table-column label="用户头像" prop="avatar" width="100">
					<template slot-scope="scope">
						<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
							暂无图片
						</div>
						<div v-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img slot="reference" :src="scope.row.avatar" alt="" style="width: 50px; height: 50px">
								<img :src="scope.row.avatar" alt="" style="width: 200px; height: auto">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column label="用户手机号" prop="phone">
					<template slot-scope="scope">
						<span v-if="scope.row.phone">{{ scope.row.phone }}</span>
					</template>
				</el-table-column>
				<el-table-column label="性别" prop="sex" width="80">
					<template slot-scope="scope">
						<span v-if="scope.row.sex == 1">男</span>
						<span v-else>女</span>
					</template>
				</el-table-column>
				<el-table-column label="礼物数量" prop="giftCount" width="100">
				</el-table-column>
				<el-table-column label="是否上麦" prop="isSeat" width="80">
					<template slot-scope="scope">
						<span v-if="scope.row.isSeat == 1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column label="上麦编号" prop="seatNo" width="100">
				</el-table-column>
				<el-table-column label="状态" prop="status" width="80">
					<template slot-scope="scope">
						<span v-if="scope.row.status == 1">在线</span>
						<span v-if="scope.row.status == 2">离开</span>
					</template>
				</el-table-column>
				<el-table-column label="进入时间" prop="createTime">
				</el-table-column>
				<el-table-column fixed='right' label="操作" width="150">
					<template slot-scope="scope">
						<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
							@click="deleteBtn(scope.row)" :disabled="!isAuth('chattingRoom:tiren')">踢出房间
						</el-button>

					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination :current-page="pageU" :page-size="limitU" :page-sizes="[10, 20, 30, 50, 100]"
					:total="chatListNum" layout="total,sizes, prev, pager, next,jumper" @size-change="handleSizeChangeU"
					@current-change="handleCurrentChangeU">
				</el-pagination>
			</div>
		</el-dialog>
		<el-dialog title="举报列表" :visible.sync="dialogFormVisibleJb" center width="80%">

				<el-table v-loading="tableDataLoading" :data="tableDataJb.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="标题">
					</el-table-column>
					<el-table-column prop="content" label="内容">
					</el-table-column>
					<el-table-column prop="userName" label="举报用户">
						<template slot-scope="scope">
							<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
								 @click="updates(scope.row.userId)">
								{{scope.row.userName ? scope.row.userName : '未绑定'}}
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="直播间名称">
						<template slot-scope="scope">
							<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
								 @click="JiankongBtn(scope.row.platformId)">
								{{scope.row.platform ? scope.row.platform : '未绑定'}}
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="image" label="图片">
						<template slot-scope="scope">
							<div v-if="scope.row.image">
								<div v-for="(item,index) in scope.row.image.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeJ" @current-change="handleCurrentChangeJ"
						:page-sizes="[5, 10, 20, 50, 100]" :page-size="limitU" :current-page="pageU"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataJb.totalCount">
					</el-pagination>
				</div>
		</el-dialog>
	</div>
</template>

<script>
	import TRTC from 'trtc-sdk-v5';
	const trtc = TRTC.create();
	// import RoomContainer from '@/TUIRoom/index';
	import ChatRoom from './chatRoom';
	import TUIRoomEngine, {
		TUIRoomEvents
	} from '@tencentcloud/tuiroom-engine-js';
	export default {
		components: {
			// RoomContainer,
			ChatRoom
		},
		data() {
			return {
				primary_equipment_name: '',
				primary_equipment_sn: '',
				equipment_gate_name: '',
				equipment_gate_sn: '',
				primary_argument_one: '',
				primary_argument_two: '',
				primary_argument_thd: '',
				gate_argument_one: '',
				gate_argument_two: '',
				gate_argument_thd: '',
				type: 0,
				size: 10,
				page: 1,
				storeName: '',
				userNameT: '',
				form: {
					storeName: '',
					logo: '',
					shipAddress: '',
					refundContact: '',
					refundMobile: '',
					shoptime: '',
				},
				isshop: true,
				formLabelWidth: '200px',
				tableDataLoading: true,
				dialogFormVisible: false,
				dialogFormVisibleshebie: false,
				tableData: [],
				tearoomId: '',
				bianhaoList: [],
				innerCode: '',
				dialogFormVisibleJk: false,
				equipment_sn: '',
				jiankongUrl: '',
				openValue2: 1,
				closeValue2: 2,
				dataRoom: {},
				userList: [],
				// 语音房弹框------
				chatList: [],
				chatListSm: [],
				chatListNum: 0,
				page: 1,
				limit: 10,
				storeId: 0,
				targetId: "",
				userName: "",
				userId: "",
				chatId: "",
				type: "",
				current: 0,
				coverImg: "",
				sendType: "2",
				// 表情包列表
				faceList: [],
				// 发送文字
				content: "",
				// 模拟对话
				name: "",
				userHead: "",
				numlist: {},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				emojiShow: false,
				dialogueInfo: {},
				dialogueList: [],
				loadText: "加载中...",
				pageNum: 1,
				loadText1: "加载中...",
				pageNum1: 1,
				center_wrapper_center_item: "center_wrapper_center_item",
				center_wrapper_center_item_reserve: "center_wrapper_center_item_reserve",
				dialogFormVisibleshebieG: false,
				contentGg: '',
				titles: '',
				fangzhuData: {},
				dialogFormVisibleU: false,
				tableData: {},
				pageU:1,
				limitU:10,
				tableDataJb:{},
				dialogFormVisibleJb:false,
				userIds:'',

			}
		},
		methods: {
			// 商户详情跳转
			merchDetails(rows) {
				this.$router.push({
					path: '/devicedetails',
					query: {
						merchantId: rows.merchant_id,
						storeId: rows.tearoomId,
						storeName: rows.store_name,
						equipment_sn: rows.innerCode
					}
				});
			},
			// 详情跳转
			updates(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},

			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChangeJ(val) {
				this.limitU = val
				this.dataSelectJb()
			},
			handleCurrentChangeJ(val) {
				this.pageU = val
				this.dataSelectJb()
			},
			// select自动选择
			selectTrigger() {
				this.dataSelect()
			},
			// 查询
			select() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.storeName = ''
				this.userNameT = ''
				this.dataSelect()
			},

			// 图标上传
			handleAvatarSuccess(file) {
				this.form.logo = file.data;
			},

			// 查看
			merchLook(row) {
				console.log(row)
				this.dialogFormVisible = true
				this.form.storeName = row.store_name
				this.form.logo = row.img
				this.form.shipAddress = row.store_address
				this.form.refundContact = row.user_info.nickname
				this.form.refundMobile = row.store_phone
				this.form.shoptime = row.store_time
			},
			updatesshebei(rows) {
				console.log(rows)
				this.shebeiSelect()
				this.dialogFormVisibleshebie = true
				this.tearoomId = rows.tearoomId
				this.innerCode = rows.innerCode
			},
			disposeNoticeToshiebei() {
				this.$http({
					url: this.$http.adornUrl('tearoom/bindInnerCode'),
					method: 'post',
					data: this.$http.adornData({
						// 'type': this.type,
						'tearoomId': this.tearoomId,
						'innerCode': this.innerCode,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
								this.dialogFormVisibleshebie = false
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}


				})
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('room/selectRoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userName': this.userNameT,
						'roomName': this.storeName,
						'status': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 获取设备列表
			shebeiSelect() {
				this.$http({
					url: this.$http.adornUrl('room/selectGroup'),
					method: 'get',
					params: this.$http.adornParams({
						'innerCode': this.equipment_sn,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.bianhaoList = returnData.result
				})
			},
			// 监控弹框
			updatesJiankong(row) {
				console.log(row, 'RoomContainer', row.roomId)
				this.dataRoom = row
				this.equipment_sn = row.roomId
				var sdkAppIds = Number(this.adkAppId)
				this.$http({
					url: this.$http.adornUrl('room/selectSign'),
					method: 'get',
					params: this.$http.adornParams({
						'roomId': row.roomNo,
						'userId':1,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						var that = this

						try {
							// 用户打开麦克风
							trtc.on(TRTC.EVENT.REMOTE_AUDIO_AVAILABLE, datas => {
							  console.log('--------打开麦克风: ' + JSON.stringify(datas) );
							   const userId = datas.userId;
							   this.userIds = datas.userId;
							});
							// 用户关闭麦克风
							trtc.on(TRTC.EVENT.REMOTE_AUDIO_UNAVAILABLE, event => {
								 console.log('--------关闭麦克风: ' + JSON.stringify(event) );
							  const userId = event.userId;
							});
							const roomEngine = trtc.enterRoom({
								strRoomId: row.roomNo,
								scene: 'live',
								sdkAppId: sdkAppIds,
								userId: '1',
								userSig: data.data,
								role:'audience',
							});
							console.log('that.adkAppId', sdkAppIds);



						} catch (error) {
							console.error('进房失败 ' + error);
						}
						 // console.log('----------------: ',trtc.on());

					}
				})
				this.loadEmojis();
				this.initWebSocket();
				this.dataSelectUser()
				this.dataSelectSm()
				this.clickItem2();
				this.dialogFormVisibleJk = true

			},
			closeJiankong() {
				this.dialogFormVisibleJk = false
				trtc.exitRoom();
				trtc.destroy();
			},

			// 获取adkAppId
			xianshi() {
				this.$http({
					url: this.$http.adornUrl('app/common/type/814'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.adkAppId = data.data.value
					}
				})
			},
			// ----语音房模块---------
			initWebSocket() {

				this.websock = new WebSocket(
					this.$http.adornWss('roomChatSocket/0/'+this.dataRoom.roomId)
					// 'ws://192.168.0.132:8182/pwyl-api/roomChatSocket/0/' + this.dataRoom.roomId
				); //这个连接ws://固定，后面的根据自己的IP和端口进行改变，我设置监听的就是8081
				// this.websock = new WebSocket("ws://yungameadmin.xianmxkj.com/ws/websocket/0");
				this.websock.onmessage = this.websocketonmessage;
				this.websock.onerror = this.websocketonerror;
				this.websock.onopen = this.websocketonopen;
				this.websock.onclose = this.websocketclose;
			},
			websocketonopen() {
				// 连接建立之后执行send方法发送数据，这个和自己的后端沟通好需要传什么数据，我的是要进行token验证
				console.log("WebSocket连接成功");
			},
			websocketonerror() {
				//连接错误
				console.log("WebSocket连接失败");
			},
			websocketonmessage(e) {
				// 数据接收
				let msg = JSON.parse(e.data);
				// this.dialogueList.push(msg);
				console.log(msg);
				if(msg.type==1||msg.type==5){
					this.dataSelectUser();
				}else if(msg.type==4||msg.type==7||msg.type==9){
					this.dataSelectSm();
				}else if(msg.type==4||msg.type==7){

				}

				let ele = document.getElementById("ele");
				this.dataSelectUser();
				this.$nextTick(() => {
					ele.scrollTop = ele.scrollHeight;
				});
			},
			websocketclose(e) {
				// 关闭连接
				console.log("已关闭连接", e);
			},
			//加载表情，存放到表情列表中
			loadEmojis() {
				const appData = require("@/assets/img/emojis.json");
				for (let i in appData) {
					this.faceList.push(appData[i].char);
				}
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hour = now.getHours();
				var minute = now.getMinutes();
				var second = now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hour}:${minute}:${second}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},

			// 获取用户点击之后的标签 ，存放到输入框内
			getEmo(index) {
				var textArea = document.getElementById("textarea");

				function changeSelectedText(obj, str) {
					if (window.getSelection) {
						// 非IE浏览器
						textArea.setRangeText(str);
						// 在未选中文本的情况下，重新设置光标位置
						textArea.selectionStart += str.length;
						textArea.focus();
					} else if (document.selection) {
						// IE浏览器
						obj.focus();
						var sel = document.selection.createRange();
						sel.text = str;
					}
				}
				changeSelectedText(textArea, this.faceList[index]);
				this.description = textArea.value; // 要同步data中的数据
				// console.log(this.faceList[index]);
				this.emojiShow = false;
				return;
			},
			clickItem2() {
				this.$http({
					url: this.$http.adornUrl("room/selectRoomChatList"),
					method: "get",
					params: this.$http.adornParams({
						'page': 1,
						'limit': 100,
						'roomId': this.dataRoom.roomId,
					}),
				}).then(({
					data
				}) => {
					console.log('````````````````````', data.data)
					let returnData = data.data;
					this.dialogueList = returnData.list.reverse();
					let ele = document.getElementById("ele");
					this.$nextTick(() => {
						ele.scrollTop = ele.scrollHeight;
					});
				});
			},
			// 点击发送
			sendTextarea(type) {

				let data = {
					content: this.content,
					type: type,
					messageType:type,
					createTime: this.info.stockDate,
					sendType: this.sendType,
					userId: 0, //this.userId
					storeId: this.storeId,
					chatId: this.chatId,
				};
				console.log('data', data, )
				this.numlist = data;
				data = JSON.stringify(data);
				this.websock.send(data);
				this.content = "";
				this.dialogueList.push(this.numlist);
				let ele = document.getElementById("ele");
				this.$nextTick(() => {
					ele.scrollTop = ele.scrollHeight;
				});
				this.initWebSocket();
				this.dataSelectUser();
				// this.clickItem2()
			},

			// 上传成功
			handleAvatarSuccess(file, fileList) {
				this.coverImg = file.data;
				this.content = this.coverImg;
				this.sendTextarea(2);
			},
			// 获取左边实时聊天用户列表
			dataSelectUser() {
				this.tableDataLoading = true;

				this.$http({
					url: this.$http.adornUrl('room/selectRoomUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'roomId': this.dataRoom.roomId,
						'status': 1,
					})
				}).then(({
					data
				}) => {

					this.tableDataLoading = false;
					let returnData = data.data;
					this.chatList = returnData.list;
					this.chatListNum = returnData.totalCount
					console.log('-----------', returnData.list)
				});
			},

			// 获取上麦人员
			dataSelectSm() {
				this.$http({
					url: this.$http.adornUrl('room/selectRoomUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'roomId': this.dataRoom.roomId,
						'status': 1,
						'isSeat': 1,
					})
				}).then(({
					data
				}) => {

					let returnData = data.data;
					this.chatListSm = returnData.list;
					this.dataSelectFjxx()
				});
			},
			// 获取房主信息
			dataSelectFjxx() {

				this.$http({
					url: this.$http.adornUrl('room/selectCreateRoomUser'),
					method: 'get',
					params: this.$http.adornParams({
						'roomId': this.dataRoom.roomId,
					})
				}).then(({
					data
				}) => {

					let returnData = data.data;
					this.fangzhuData = returnData;
					if (this.chatListSm.length > 0) {
						for (var i in this.chatListSm) {
							if (this.fangzhuData.userId == this.chatListSm[i].userId) {
								this.fangzhuData.giftCount = this.chatListSm[i].giftCount
							}
						}
					}
				});
			},
			// 验证只能上传音频
			beforeAvatarUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				var extension;
				if (testmsg == 'png' || testmsg == 'jpg' || testmsg == 'jpeg' || testmsg == 'gif') {
					extension = testmsg
				}
				// const extension = testmsg==='png'
				if (!extension) {
					this.$message({
						message: "只能上传图片！",
						type: 'error'
					})
				}
				return extension;
			},
			// 公告弹框
			ggBtn() {
				console.log()
				this.titles = '公告'
				this.contentGg = this.dataRoom.announcement
				this.dialogFormVisibleshebieG = true

			},
			// 玩法弹框
			wfBtn() {
				this.titles = '玩法'
				this.contentGg = this.dataRoom.playingMethod
				this.dialogFormVisibleshebieG = true
			},
			// 修改公告、玩法
			disposeGg() {
				console.log('修改啦')
				if(this.contentGg==''){
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入拒绝理由',
							type: 'warning'
						})
						return
				}

				if(this.titles == '公告'){
					this.dataRoom.announcement = this.contentGg
					var datas = {
						roomId:this.dataRoom.roomId,
						announcement:this.contentGg
					}
				}
				if(this.titles == '玩法'){
					this.dataRoom.playingMethod = this.contentGg
					var datas = {
						roomId:this.dataRoom.roomId,
						playingMethod:this.contentGg
					}
				}
				this.$http({
					url: this.$http.adornUrl('room/updateRoom'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success'
						});
						this.dialogFormVisibleshebieG = false

					} else {
						this.$message.error(data.msg);
					}
				})
			},
			// 查看在线用户
			peopleBtn() {
				this.page = 1
				this.dataSelectUser()
				this.dialogFormVisibleU = true
			},
			handleSizeChangeU(val) {
				this.limitU = val
				this.dataSelectUser()
			},
			handleCurrentChangeU(val) {
				this.pageU = val
				this.dataSelectUser()
			},
			// 踢出房间
			deleteBtn(row){
				this.$confirm('确定要将该用户踢出房间吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('room/roomRemoveUser'),
						method: 'get',
						params: this.$http.adornParams({
							'userId': row.userId,
							'roomNo': this.dataRoom.roomNo,
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success'
							});
							this.sendTextarea(14);
							this.dataSelectUser()
						} else {
							this.$message.error(data.msg);
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消'
					});
				});
			},
			// 解散房间
			deleteRoom(){
				this.$confirm('确定要解散房间吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('room/dismissRoom'),
						method: 'get',
						params: this.$http.adornParams({
							'roomNo': this.dataRoom.roomNo,
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.dialogFormVisibleJk = false
							this.$message({
								message: '操作成功',
								type: 'success'
							});
							this.sendTextarea(18);

							this.dataSelect()
						} else {
							this.$message.error(data.msg);
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消'
					});
				});
			},
			// ----语音房模块---------
			// 举报记录
			cleansJb() {
				this.dataSelectJb()
				this.dialogFormVisibleJb = true

			},
			// 获取举报记录
			dataSelectJb() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`message/selectMessageByType`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'state': 100,
						'platform': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataJb = returnData

				})
			},
			// 跳转直播间
			JiankongBtn(roomId){
				this.$http({
					url: this.$http.adornUrl('room/selectRoomDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'roomId': roomId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.updatesJiankong(data.data)
					} else {
						this.$message.error(data.msg);
					}
				})
			},
		},
		mounted() {

			this.xianshi()
			this.dataSelect()
		},
		destroyed() {
			window.clearInterval(this.timer);
			if (this.websock) {
				this.websock.close(); // 页面销毁后断开websocket连接
			}
		},
	}
</script>

<style scoped="scoped">
	.el-button+.el-button {
		margin-left: 0 !important;
		margin-top: 5px !important;
	}
</style>
<style scoped>
	.whole-wrapper {
		display: flex;
		background: #fff;
		height: 450px;
		position: relative;
	}

	.top-group {
		padding-bottom: 30px;
		padding-top: 10px;
	}

	/* 左边部分 */
	.left_whole_wrapper {
		display: flex;
		flex-direction: column;
		width: 24%;
	}

	.left_wrapper {
		flex: 1;
		background-color: #ebe9e8;
	}

	.left-item {
		display: flex;
		justify-content: space-between;
		padding: 18px 24px;
		background-color: #e9e7e6;
	}

	.left-item.blue {
		background-color: #c3c3c4;
	}

	.active {
		background: #ebebeb;
	}

	.left-item-left {
		display: flex;
	}

	.left-item-head>img {
		width: 48px;
		height: 48px;
	}

	.left-item-info {
		margin-left: 12px;
		max-width: 135px;
		display: flex;
		flex-direction: column;
		justify-content: space-around;
	}

	.left-item-info_title {
		font-size: 14px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		text-align: left;
		color: #292929;
		/* 文字超出处理 */
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.left-item-info_content {
		font-size: 14px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		text-align: left;
		color: #909090;
		margin-top: 4px;
		/* 文字超出处理 */
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.left-item-time {
		display: flex;
		flex-direction: column;
		text-align: right;
		align-items: flex-end;
	}

	.left-item-time-time {
		font-size: 14px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		text-align: right;
		color: #909090;
	}

	.left-item-time-number {
		width: 20px;
		height: 20px;
		line-height: 20px;
		border-radius: 50%;
		background: #ff4949;
		color: #ffffff;
		text-align: center;
		font-size: 12px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		margin-top: 5px;
	}

	.search {
		padding: 10px 24px;
		/* height: 5%; */
		background-color: #eeebe8;
	}

	/* 中间部分*/
	.center_whole_wrapper {
		flex: 1;
		background: #f5f5f5;
	}

	.center_wrapper {
		height: 100%;
		display: flex;
		flex-direction: column;
	}

	.center_wrapper_top {
		display: flex;
		align-items: center;
		padding: 30px 24px;
		flex: 1;
		position: relative;
	}

	.center_wrapper_top_name {
		font-size: 18px;
		font-family: PingFang SC, PingFang SC-Bold;
		font-weight: 700;
		text-align: left;
		color: #292929;
	}

	.center_wrapper_top_number {
		font-size: 14px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		text-align: left;
		color: #292929;
		margin-left: 12px;
	}

	.center_wrapper_center {
		flex: 10;
		padding: 30px 24px 0px 24px;
		border-top: 1px solid #ececec;
		border-bottom: 1px solid #ececec;
	}

	.center_wrapper_center_item {
		display: flex;
		margin-bottom: 32px;
	}

	.center_wrapper_right {
		flex: 4;
		background-color: #ffffff;
		padding: 20px 20px;
		position: relative;
		border-right: 1px solid #ececec;
		border-bottom: 1px solid #ececec;
	}

	.center-item-info_wrapper {
		margin: 0px 16px;
	}

	.center-item-tip {
		display: flex;
		align-items: center;
	}

	.error-tip>img {
		width: 20px;
		margin: 0 10px;
	}

	.center_wrapper_center_item .center-item-info {
		background: #ffffff;
		border-radius: 3px;
	}

	.center_wrapper_center_item_reserve .center-item-info {
		background: #9eea6a;
		border-radius: 3px;
	}

	.center-item-info {
		max-width: 336px;
		font-size: 14px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		text-align: left;
		color: #333333;
		line-height: 22px;
		padding: 16px;
	}

	.center-item-time {
		font-size: 12px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		text-align: left;
		color: #909090;
		line-height: 22px;
		margin-top: 12px;
	}

	/* 反向样式开始*/
	.center_wrapper_center_item_reserve {
		display: flex;
		flex-direction: row-reverse;
		margin-bottom: 32px;
	}

	.center_wrapper_center_item_reserve .center-item-time {
		text-align: right;
	}

	.center_wrapper_center_item_reserve {
		display: flex;
		flex-direction: row-reverse;
		margin-bottom: 32px;
	}

	.center_wrapper_center_item_reserve .center-item-tip {
		display: flex;
		flex-direction: row-reverse;
		align-items: center;
	}

	.center_wrapper_center_item_reserve .error-tip>img {
		width: 20px;
		margin: 0 10px;
	}

	/* 反向样式结束*/

	/* 右边样式 */
	.right_whole_wrapper {
		background: #fff;
	}

	.right_wrapper {
		flex: 1;
	}

	.right_wrapper_center {
		border: none;
		color: #909090;
	}

	.center-item-name {
		font-size: 12px;
	}

	.center-item-info {
		word-wrap: bre省钱兄-word;
		word-bre省钱兄: bre省钱兄-all;
	}

	.center-item-info_img {
		width: 150px;
		/* height: 100px; */
	}

	.center-item-info_img>img {
		width: 100%;
		height: 100%;
	}

	/* 几个图标 */
	.icon-list {
		display: flex;
	}

	.icon-list>div:not(:first-child) {
		margin-left: 15px;
	}

	.icon-item-img {
		width: 25px;
	}

	/* 修改组件样式 */
	.whole-wrapper>>>.el-input__inner {
		border-radius: 5px;
	}

	.whole-wrapper>>>.el-textarea__inner {
		min-height: 80px;
		resize: none;
		border: none;
		background: #ffffff;
		padding: 5px 0px;
	}

	.whole-wrapper>>>.el-button--primary {
		width: 100px;
		height: 40px;
		position: absolute;
		bottom: 20px;
		right: 20px;
	}

	.loading {
		text-align: center;
		color: #909090;
	}

	.loading span {
		margin-left: 10px;
	}

	.center_wrapper_center_item_reserve,
	.center_wrapper_center_item {
		position: relative;
	}

	.center_wrapper_center_item_reserve .center-item-info::after {
		content: "";
		position: absolute;
		right: 55px;
		top: 12px;
		width: 0;
		height: 0;
		border: 5px solid #9eea6a;
		border-top-color: transparent;
		border-right-color: transparent;
		border-bottom-color: transparent;
	}

	.center_wrapper_center_item .center-item-info::after {
		content: "";
		position: absolute;
		left: 55px;
		top: 12px;
		width: 0;
		height: 0;
		border: 5px solid #ffffff;
		border-top-color: transparent;
		border-left-color: transparent;
		border-bottom-color: transparent;
	}
</style>
<style lang="scss">
	/* el-popover是和app同级的，所以scoped的局部属性设置了无效 */
	/* 需要设置全局style */
	.el-popover {
		height: 200px;
		width: 400px;
		overflow: scroll;
		overflow-x: auto;
		font-size: 20px;
	}

	.box {
		// padding: 44px;
		// border: 1px solid #eee;
		// margin: 15px 10px;
	}

	.box_num {
		font-size: 14px;
		color: #66b1ff;
	}

	.box_num .box_color {
		color: #333;
		font-size: 14px;
		margin-bottom: 15px;
		position: relative;
	}

	.box_num div span {
		font-size: 20px;
		margin-left: 5px;
	}

	.text_color {
		color: #999;
	}

	.text_color span {
		margin-right: 5px;
	}

	.imageName {
		width: 50px;
		height: 50px;
		border-radius: 50%;
	}
	.images{
		// box-shadow: 0px 0px 8px 1PX #9c80ef;
		    border: 2px solid #9c80ef;
	}
</style>
