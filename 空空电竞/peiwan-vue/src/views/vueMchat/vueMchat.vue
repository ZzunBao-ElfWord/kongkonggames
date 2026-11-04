<template>
	<div>
		<!-- <div class="top-group">
        <el-button type="primary">查看全部</el-button>
    </div> -->
		<div class="whole-wrapper">
			<!-- 左边的聊天列表 -->
			<div class="left_whole_wrapper">
				<!-- 搜索框 -->
				<div class="search">
					<el-input placeholder="搜索联系人" suffix-icon="el-icon-search" v-model="userName" clearable
						@change="changeSearch" @input="searchChange"></el-input>
				</div>
				<!-- 聊天列表 -->
				<div class="left_wrapper" style="overflow-y: auto" v-if="chatList">
					<div class="left-item" style="position: relative; cursor: pointer"
						v-bind:class="{ blue: index == current }" v-for="(item, index) in chatList" :key="index"
						@click="clickItem(item, index)">
						<div class="left-item-left">
							<div class="left-item-head" v-if="item.userHead == null || item.userHead == ''">
								<img src="~@/assets/img/head.png" />
							</div>
							<div class="left-item-head" v-else><img :src="item.userHead" /></div>
							<div class="left-item-info">
								<div class="left-item-info_title">{{ item.userName }}</div>
								<div class="left-item-info_content">{{ item.content }}</div>
							</div>
						</div>
						<div class="left-item-time">
							<div class="left-item-time-time" v-if="item.contentTime == null"></div>
							<div class="left-item-time-time" v-else>
								{{ item.contentTime.substring(11, 16) }}
							</div>
							<div class="left-item-time-number" style="position: absolute; left: 59px; top: 8px"
								v-if="item.storeCount">
								{{ item.storeCount }}
							</div>
							<div class="" @click="deletes(item)" style="position: relative; top: 6px"
								v-if="isAuth('vueMchat:delete')">
								<img src="~@/assets/img/delete.png" alt="" width="25" height="25" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 中间的对话-->
			<div class="center_whole_wrapper">
				<div class="center_wrapper">
					<!-- 上面的表头 -->
					<div class="center_wrapper_top">
						<div class="center_wrapper_top_name">{{ name }}</div>
						<el-button v-if="name" size="mini"
							style="top: 30px; right: 20px; border-radius: 4px; position: absolute"
							@click="updates(userId)">用户详情
						</el-button>
					</div>
					<!-- 中间的聊天框，尝试模拟数据 -->
					<div class="center_wrapper_center" id="ele" style="overflow-y: auto" ref="dialogue_box">
						<!-- <div class="item loading">{{loadText}}<span>{{"第" + pageNum + "页"}}</span></div> -->
						<div v-for="(item, index) in dialogueList" :key="index" :class="
                item.sendType == '1'
                  ? center_wrapper_center_item
                  : center_wrapper_center_item_reserve
              ">
							<div class="left-item-head">
								<img v-if="item.sendType == '1' && userHead !== null" :src="userHead" />
								<img v-else-if="item.sendType == '1' && userHead == null"
									src="~@/assets/img/head.png" />
								<img v-else src="~@/assets/img/avatar.png" />
							</div>
							<!-- 文字 -->
							<div class="center-item-info_wrapper" v-if="item.type == 1">
								<div class="center-item-tip">
									<div class="center-item-info">
										{{ item.content }}
									</div>
								</div>
								<div class="center-item-time">{{ item.createTime }}</div>
							</div>
							<!-- 图片 -->
							<div class="center-item-info_wrapper" v-if="item.type == 2">
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
							<!-- 订单 -->
							<div class="center-item-info_wrapper" v-if="item.type == 3">
								<div class="center-item-tip" style="background-color: #fff;cursor: pointer;padding: 10px;border-radius: 4px;">
									<div class="" @click="orderDetails(item.content.split(',')[2])">
										<div style="display: flex">
											<img :src="item.content.split(',')[0]" alt="" width="40" height="40" />
											<div style="
                          width: 145px;
                          font-size: 13px;
                          line-height: 20px;
                          margin-left: 5px;
                          overflow: hidden;
                          text-overflow: ellipsis;
                          display: -webkit-box;
                          -webkit-line-clamp: 2;
                          -webkit-box-orient: vertical;
                        ">
												{{ item.content.split(",")[1] }}
											</div>
										</div>
										<p class="hoverp" style="font-size: 13px; color: #999999">
											订单编号: {{ item.content.split(",")[3] }}
										</p>
										<p style="font-size: 13px; color: #999999">
											创建时间:{{ item.content.split(",")[4] }}
										</p>
									</div>
								</div>
								<div class="center-item-time">{{ item.createTime }}</div>
							</div>
							<!-- 商品 -->
							<div class="center-item-info_wrapper" v-if="item.type == 4">
								<div class="center-item-tip"
									style="background-color: #fff; padding: 10px; border-radius: 4px">
									<div class="">
										<img :src="item.content.split(',')[0]" alt="" width="160" height="120" />
										<p style="width: 160px; line-height: 20px; margin-bottom: 0">
											{{ item.content.split(",")[1] }}
										</p>
										<p style="color: #f56c6c; margin-bottom: 0">
											¥{{ item.content.split(",")[2] }}
										</p>
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
							<!-- <div class="icon-item" title="表情">
                            <el-popover v-model="emojiShow" placement="top-start" width="400" trigger="click" class="emoBox">
                                <div class="emotionList">
                                    <a href="javascript:void(0);" @click="getEmo(index)" v-for="(item,index) in faceList" :key="index" class="emotionItem">{{item}}</a>
                                </div>
                                <el-button
                                style="background: transparent;border: none;"
                                class="emotionSelect"
                                icon="iconfont icon-biaoqing"
                                slot="reference"
                                >😁</el-button>
                            </el-popover>
                        </div> -->
							<!-- 上传文件 -->
							<div class="icon-item">
								<el-upload class="avatar-uploader" v-model="coverImg"
									:action="$http.adornUrl('alioss/upload')"
									:show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
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
						<el-button type="primary" style="font-size: 12px" v-else disabled @click="sendTextarea(1)">发送
						</el-button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				chatList: [],
				page: 1,
				size: 5,
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
			};
		},
		created() {
			this.loadEmojis();
		},
		watch: {
			$route: function(to, from) {
				if (to.name == "vueMchat") {
					let ele = document.getElementById("ele");
					this.$nextTick(() => {
						ele.scrollTop = ele.scrollHeight;
					});
				}
			},
		},
		activated() {
			let {
				userId,
				userName
			} = this.$route.query;
			if (userId && userName) {
				this.userName = userName;
				this.current = 0;
			} else {
				this.userName = "";
			}
			this.dataSelect();
			this.initWebSocket();
		},
		methods: {
			initWebSocket() {
 const domain = window.location.hostname;
        // this.websock = new WebSocket("wss://"+domain+"/wss/websocket/0");
				this.websock = new WebSocket(
				this.$http.adornWss("websocket/0"));				// "ws://192.168.1.17:8180/pwyl-api/websocket/0"); //这个连接ws://固定，后面的根据自己的IP和端口进行改变，我设置监听的就是8081
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
				this.dialogueList.push(msg);
				console.log(msg);

				let ele = document.getElementById("ele");
				this.dataSelect();
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
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: "/userDetail",
					query: {
						userId: userId
					}
				});
			},
			// 订单详情跳转
			orderDetails(id) {
				this.$router.push({
					path: "/orderDetails",
					query: {
						id: id
					}
				});
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
			changeSearch(e) {
				if (!e) {
					this.userName = ""
					this.dataSelect();
				}
			},
			searchChange() {
				this.dataSelect();
			},
			// 点击聊天列表的某一项
			clickItem(item, index) {
				this.current = index;
				this.userId = item.userId;
				this.name = item.userName;
				this.userHead = item.userHead;
				this.chatId = item.chatId;
				this.chatList[index].storeCount = 0
				this.clickItem2();
				this.initWebSocket();
			},


			clickItem2() {
				this.$http({
					url: this.$http.adornUrl("chatsContent/storeList"),
					method: "get",
					params: this.$http.adornParams({
						chatId: this.chatId,
					}),
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.dialogueList = returnData;
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
					createTime: this.info.stockDate,
					sendType: this.sendType,
					userId: this.userId,
					storeId: this.storeId,
					chatId: this.chatId,
				};
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
				this.dataSelect();
				// this.clickItem2()
			},
			//删除会话
			deletes(item) {
				this.$confirm(`确定删除聊天记录，删除后不能恢复?`, "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						this.$http({
							url: this.$http.adornUrl(`chats/delete/?id=${item.chatId}`),
							method: "get",
							params: this.$http.adornParams({}),
						}).then(({
							data
						}) => {
							this.$message({
								message: "删除成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.dataSelect();
								},
							});
						});
					})
					.catch(() => {});
			},
			// 上传成功
			handleAvatarSuccess(file, fileList) {
				this.coverImg = file.data;
				this.content = this.coverImg;
				this.sendTextarea(2);
			},
			// 获取左边实时聊天用户列表
			dataSelect() {

				this.tableDataLoading = true;
				this.$http({
					url: this.$http.adornUrl("chats/list"),
					method: "get",
					params: this.$http.adornParams({
						userName: this.userName,
						storeId: this.storeId,
					}),
				}).then(({
					data
				}) => {
					this.tableDataLoading = false;
					let returnData = data.data;
					this.chatList = returnData;
					this.clickItem(this.chatList[this.current], this.current);
				});
			},
			// 验证只能上传音频
			beforeAvatarUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				var extension;
				if (testmsg == 'png'||testmsg == 'jpg'||testmsg == 'jpeg'||testmsg == 'gif'){
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
		},
		mounted() {
			// this.timer = window.setInterval(() => {
			// setTimeout(() => {
			// 	this.dataSelect()
			// },0)
			// },2000)
		},
		destroyed() {
			window.clearInterval(this.timer);
			if (this.websock) {
				this.websock.close(); // 页面销毁后断开websocket连接
			}
		},
	};
</script>
<style scoped>
	.whole-wrapper {
		display: flex;
		background: #fff;
		height: 600px;
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
		padding: 46px 24px;
		height: 5%;
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
</style>
