<template>
	<div>
		<div style="margin:2% 0;display: inline-block;">
			<span>用户昵称:</span>
			<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
				v-model="userName"></el-input>
		</div>&emsp;&emsp;
		<div style="display: inline-block;">
			<span>开始时间：</span>
			<el-date-picker style="width: 250px;margin-left: 10px;" v-model="startTime" align="right" type="datetime"
				default-time="00:00:00" placeholder="选择开始时间">
			</el-date-picker>&nbsp;&nbsp;&nbsp;
			<span>截止时间：</span>
			<el-date-picker style="width: 250px;margin-left: 10px;" v-model="endTime" align="right" type="datetime"
				default-time="23:59:59" placeholder="选择截止时间">
			</el-date-picker>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
		</div>
		<div style="display: inline-block;">
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.list">
			<el-table-column fixed prop="userName" label="昵称" width="150">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="isAuthentication" label="性别">
				<template slot-scope="scope">
					<span v-if="scope.row.sex==1">男</span>
					<span v-else-if="scope.row.sex==2">女</span>
					<span v-else>未设置</span>
				</template>
			</el-table-column>
			<el-table-column label="图像">
				<template slot-scope="scope">
					<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
					<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="手机号">
				<template slot-scope="scope">
					<span style="color: #4f9dec;cursor: pointer;">
						{{ scope.row.phone ? scope.row.phone : '未绑定' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
			<el-table-column prop="isAuthentication" label="是否实名">
				<template slot-scope="scope">
					<span v-if="scope.row.isAuthentication==1">已实名</span>
					<span v-else>未实名</span>
				</template>
			</el-table-column>
			<el-table-column prop="isVip" label="是否是会员">
				<template slot-scope="scope">
					<span v-if="scope.row.isVip==1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column>
			<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
			<el-table-column label="邀请人邀请码">
				<template slot-scope="scope">
					<span style="color: #4f9dec;cursor: pointer;">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="zhiFuBaoName" label="支付宝名称">
				<template slot-scope="scope">
					<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="zhiFuBao" label="支付宝账号">
				<template slot-scope="scope">
					<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="wxImg" label="收款二维码" width="150">
				<template slot-scope="scope">
					<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
						暂无收款二维码
					</div>
					<div v-else style="display:flex;flex-wrap: wrap;">
						<div style="margin: 2px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt="" slot="reference">
								<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
							</el-popover>
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
			<el-table-column prop="state " label="状态">
				<template slot-scope="scope">
					<span style="color: #13ce66;" v-if="scope.row.status==1">正常</span>
					<span style="color: #ff4949;" v-else>禁用</span>
					<!-- <el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
						:active-value="openValue" :disabled="!isAuth('userList:delete')" :inactive-value="closeValue"
						active-color="#13ce66" inactive-color="#ff4949">
					</el-switch> -->
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
			</el-pagination>
		</div>

	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 2,
				state: 'false',
				limit: 5,
				page: 1,
				userName: '',
				inviterCode: '',
				nickNam: '',
				platform: 'all',
				isRelation: -1,
				isTuan: -1,
				value: '',
				member: -1,
				payData: [],
				nickName: '',
				grade: '',
				name: '',
				activeName: 'first',
				tableDataLoading: true,
				tableData: [],
				platforms: [{
						value: 'all',
						label: '全部'
					}, {
						value: 'wap',
						label: '网站'
					}, {
						value: 'app',
						label: 'APP'
					}, {
						value: 'weixin',
						label: '微信公众号'
					},
					{
						value: 'mp',
						label: '微信小程序'
					}
				],
				campus: '',
				homeData1: [],
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime1() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			getNowTime2() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// tabs切换
			handleClick(tab, event) {
				this.page = 1
				this.limit = 10
				this.startTime = ''
				this.endTime = ''
				if (tab._props.label == '全部用户') {
					this.member = -1
					this.dataSelect()
				}
				if (tab._props.label == '普通用户') {
					this.member = 0
					this.dataSelect()
				}
				if (tab._props.label == '会员用户') {
					this.member = 1
					this.dataSelect()
				}
			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 详情跳转
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.superior
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.paySelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.paySelect()
			},
			// select自动选择
			selectTrigger() {
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			select2() {
				this.paySelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.startTime = ''
				this.endTime = ''
				this.userName = ''
				this.inviterCode = ''
				this.dataSelect()
			},
			//删除用户
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteUserByUserId/${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
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
				})
			},
			// 封号
			updates1(row) {

			},
			// 获取社区数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpCampus/selectCampusList'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page':this.page,
						// 'limit':this.limit,
						// 'content':this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData1 = returnData
				})
			},
			// 筛选信息
			animeDat2(state) {
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'member': this.member,
						'userName': this.userName,
						'inviterCode': this.inviterCode,
						'startTime': this.startTime,
						'endTime': this.endTime
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			}
		},
		mounted() {
			// this.homeSelect()
			this.dataSelect()
		}
	}
</script>

<style scoped="scoped">
	.el-button+.el-button {
		margin-left: 0 !important;
		margin-top: 5px !important;
	}
</style>
