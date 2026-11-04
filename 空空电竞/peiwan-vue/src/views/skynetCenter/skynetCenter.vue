<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="风险记录" name="first">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;

				<div style="margin:2% 0;display: inline-block;">
					<span>风险类型：</span>
					<el-select filterable v-model="riskType" style="width:150px;margin-left: 10px;" @change="select">
						<el-option v-for="item in platforms" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="riskId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<el-button size="mini" v-if="scope.row.user "
								style="color: #4f9dec;background: #fff;border: none;cursor: pointer;" type="primary"
								:disabled="!isAuth('userList:viewDetails')" @click="updates(scope.row)">
								{{scope.row.user.userName}}
							</el-button>
							<span v-else>未绑定</span>
						</template>
					</el-table-column>
					<el-table-column label="图像" width="100">
						<template slot-scope="scope">
							<img v-if="!scope.row.user" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
							<img v-else :src="scope.row.user.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="150">
						<template slot-scope="scope">
							<el-button size="mini" v-if="scope.row.user "
								style="color: #4f9dec;background: #fff;border: none;" type="primary"
								:disabled="!isAuth('userList:viewDetails')" @click="updates(scope.row)">
								{{scope.row.user.phone}}
							</el-button>
							<span v-else>未绑定</span>
						</template>
					</el-table-column>

					<el-table-column prop="riskType" label="风险类型" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.riskType==1">ip属地风险</span>
							<span v-if="scope.row.riskType==2">动态</span>
							<span v-if="scope.row.riskType==3">动态评论</span>
							<span v-if="scope.row.riskType==4">技能评论</span>
							<span v-if="scope.row.riskType==5">聊天</span>
							<span v-if="scope.row.riskType==6">举报</span>

						</template>
					</el-table-column>
					<el-table-column prop="content" label="触犯原因"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="risk" label="是否标记为风险用户" width="80">
						<template slot-scope="scope">
							<el-switch v-if="scope.row.user" v-model="scope.row.user.risk"
								@change="changeRist(scope.row.user.risk,scope.row.user.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
							<span v-if="scope.row.user&&scope.row.user.risk == 1" style="color: #13ce66;">是</span>
							<span v-else style="color: #ff4949;">否</span>
						</template>
					</el-table-column>
					<el-table-column prop="state" label="状态" width="100">
						<template slot-scope="scope">
							<el-switch v-if="scope.row.user" v-model="scope.row.user.status"
								@change="changeStatus(scope.row.user.status,scope.row.user.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
							<span v-if="scope.row.user&&scope.row.user.status == 1 " style="color: #13ce66;">正常</span>
							<span v-if="scope.row.user&&scope.row.user.status == 2 " style="color: #f56c6c;">禁用</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="risk" label="是否是风险用户" width="80">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.risk" @change="changeRist(scope.row.risk,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column> -->
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="用户风险列表" name="scond">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="selectF" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="selectF" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;

				<div style="margin:2% 0;display: inline-block;">
					<span>发布类型：</span>
					<el-select filterable v-model="riskSort" style="width:150px;margin-left: 10px;" @change="selectF">
						<el-option v-for="item in riskSortforms" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="selectF">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansF">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableDataUser.list">
					<el-table-column fixed prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<el-button size="mini" v-if="scope.row.user"
								style="color: #4f9dec;background: #fff;border: none;" type="primary"
								:disabled="!isAuth('userList:viewDetails')" @click="updates(scope.row)">
								{{scope.row.user.userName}}
							</el-button>
							<span v-else>未绑定</span>
							<!-- <span style="color: #f56c6c;">{{ scope.row.user ? scope.row.user.userName : '未绑定' }}</span> -->
						</template>
					</el-table-column>
					<el-table-column label="图像" width="100">
						<template slot-scope="scope">
							<img v-if="!scope.row.user" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
							<img v-else :src="scope.row.user.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="150">
						<template slot-scope="scope">
							<el-button v-if="scope.row.user" size="mini"
								style="color: #4f9dec;background: #fff;border: none;" type="primary"
								:disabled="!isAuth('userList:viewDetails')" @click="updates(scope.row)">
								{{scope.row.user ? scope.row.user.phone : '未绑定'}}
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="counts" label="总次数"></el-table-column>
					<el-table-column prop="ipCount" label="IP次数"></el-table-column>
					<el-table-column prop="talkCount" label="动态次数"></el-table-column>
					<el-table-column prop="talkCommentCount" label="动态评论次数"></el-table-column>
					<el-table-column prop="serviceCommentCount" label="技能评论次数"></el-table-column>
					<el-table-column prop="chatCount" label="聊天次数"></el-table-column>
					<el-table-column prop="messageCount" label="举报次数"></el-table-column>
					<el-table-column prop="risk" label="是否标记为风险用户" width="80">
						<template slot-scope="scope">
							<el-switch v-if="scope.row.user " v-model="scope.row.user.risk"
								@change="changeRist(scope.row.user.risk,scope.row.user.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
							<span v-if="scope.row.user &&scope.row.user.risk == 1" style="color: #13ce66;">是</span>
							<span v-else style="color: #ff4949;">否</span>
						</template>
					</el-table-column>
					<el-table-column prop="state" label="状态" width="100">
						<template slot-scope="scope">
							<el-switch v-if="scope.row.user " v-model="scope.row.user.status"
								@change="changeStatus(scope.row.user.status,scope.row.user.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
							<span v-if="scope.row.user &&scope.row.user.status == 1 " style="color: #13ce66;">正常</span>
							<span v-if="scope.row.user &&scope.row.user.status == 2 " style="color: #f56c6c;">禁用</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeYh" @current-change="handleCurrentChangeYh"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataUser.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 2,
				state: 'false',
				limit: 10,
				page: 1,
				userName: '',
				nickNam: '',
				phone: '',
				payData: [],
				nickName: '',
				activeName: 'first',
				tableDataLoading: true,
				tableData: {},
				tableDataUser: {},
				platforms: [{
						value: '',
						label: '全部'
					}, {
						value: 1,
						label: 'ip属地风险'
					}, {
						value: 2,
						label: '动态'
					}, {
						value: 3,
						label: '动态评论'
					},
					{
						value: 4,
						label: '技能评论'
					}, {
						value: 5,
						label: '聊天'
					},
					{
						value: 6,
						label: '举报'
					}
				],

				riskType: '',
				riskSort: '',
				riskSortforms: [{
						value: '',
						label: '全部'
					}, {
						value: 0,
						label: '总次数'
					}, {
						value: 1,
						label: 'ip属地风险'
					}, {
						value: 2,
						label: '动态'
					}, {
						value: 3,
						label: '动态评论'
					},
					{
						value: 4,
						label: '技能评论'
					}, {
						value: 5,
						label: '聊天'
					},
					{
						value: 6,
						label: '举报'
					}
				],
			}
		},
		methods: {

			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`risk/updateRiskUser?userId=${userId}`),
					method: 'post',
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
				// this.$router.push({
				// 	path: '/userDetail',
				// 	query: {
				// 		userId: row.superior
				// 	}
				// })
				this.$http({
					url: this.$http.adornUrl('user/selectUserByInvitationCode'),
					method: 'get',
					params: this.$http.adornParams({
						'invitationCode': row.inviterCode
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$router.push({
							path: '/userDetail',
							query: {
								userId: data.data.userEntity.userId
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
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChangeYh(val) {
				this.limit = val
				this.dataSelectUser()
			},
			handleCurrentChangeYh(val) {
				this.page = val
				this.dataSelectUser()
			},
			// tabs切换
			handleClick(tab, event) {
				this.phone = ''
				this.page = 1
				this.userName = ''
				this.riskSort = ''
				this.riskType = ''
				if (tab._props.label == '风险记录') {
					this.dataSelect()
				}
				if (tab._props.label == '用户风险列表') {
					this.dataSelectUser()
				}




			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},

			// 重置
			cleans() {
				this.page = 1
				this.riskType = ''
				this.userName = ''
				this.phone = ''
				this.dataSelect()
			},
			// 查询
			selectF() {
				this.page = 1
				this.dataSelectUser()
			},

			// 重置
			cleansF() {
				this.page = 1
				this.riskSort = ''
				this.userName = ''
				this.phone = ''
				this.dataSelectUser()
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

			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('risk/selectRiskList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': this.userName,
						'riskType': this.riskType,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},

			// 获取用户风险列表
			dataSelectUser() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('risk/selectRiskGroupList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': this.userName,
						'riskSort': this.riskSort,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableDataUser = returnData
				})
			},
			// 是否标记风险用户
			changeRist(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`risk/updateRiskUser?userId=${userId}`),
					method: 'post',
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
			// 状态
			changeStatus(val, userId) {
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
		},
		mounted() {
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
