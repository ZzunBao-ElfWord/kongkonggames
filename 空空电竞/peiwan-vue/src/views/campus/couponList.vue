<template>
	<div>
		<div style="display: inline-block;">
			<div style="position: relative;display: inline-block;">
				<span>优惠券名称：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入优惠券名称" v-model="couponName">
				</el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;">
				<span>用户名：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入用户名" v-model="userName">
				</el-input>&nbsp;&nbsp;
			</div>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
			<!-- <el-button style='margin:0 0 20px 20px;' v-if="isAuth('autonym:pass')" size="mini" type="primary"
				icon="document" @click="passClick()" :disabled="checkBoxData.length <= 0">通过</el-button> -->
		</div>
		<el-table  v-loading="tableDataLoading" :data="tableData.content">
			<!-- @selection-change="changeFun" <el-table-column type="selection" fixed>
			</el-table-column> -->
			<el-table-column prop="couponUserId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="nickName" label="用户名" width="100">
				<template slot-scope="scope">
					<span style="color: #4f9dec;cursor: pointer;"
						@click="updates(scope.row)">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="couponName" label="满减券名称" >
			</el-table-column>
			<el-table-column prop="lessMoney" label="满减券面值">
			</el-table-column>
			<el-table-column prop="minMoney" label="满减券最低消费" >
			</el-table-column>
			<el-table-column label="状态">
				<template slot-scope="scope">
					<span style="color: #999999;" v-if="scope.row.status == 1">待使用</span>
					<span style="color: #3E8EF7;" v-if="scope.row.status == 2">已使用</span>
					<span style="color: #AA1111;" v-if="scope.row.status == 3">已过期</span>
				</template>
			</el-table-column>
			<el-table-column prop="failureTime" label="到期时间">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间">
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalElements">
			</el-pagination>
		</div>
		<!-- 任务拒绝弹框 -->
		<el-dialog title="任务拒绝" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入拒绝理由">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				content: '',
				status: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					}, {
						value: 1,
						label: '审核成功'
					}, {
						value: 2,
						label: '拒绝'
					}
				],
				userName: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				checkBoxData: [], //多选框选择的值
				couponName:'',
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
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
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			//任务通过
			passClick(id) {
				this.status = 1
				this.content = '同意'
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
					url: this.$http.adornUrl(
						`student/auditStudentAuthentication/${ids}/${this.status}/${this.content}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					}
				})
			},
			//单条任务通过
			passClicks(row) {
				this.status = 1
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl(`userCertification/auditorUserCertification`),
					method: 'post',
					params: this.$http.adornParams({
						'status': this.status,
						'userId': row.userId,
						'remek':''
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
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					}

				})
			},
			//任务拒绝
			refuseClick(rows) {
				this.status = 2
				this.helpTakeId = rows.userId
				this.content = ''
				this.dialogFormVisible = true
			},
			refuseto() {
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl(`userCertification/auditorUserCertification`),
					method: 'post',
					params: this.$http.adornParams({
						'status': this.status,
						'userId': this.helpTakeId,
						'remek':this.content
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.content = ''
							this.status = ''
							this.dataSelect()
						}
					})
				})
			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.couponName = ''
				this.userName = ''
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page-1,
						'size': this.limit,
						'couponName': this.couponName,
						'nickName': this.userName
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>

</style>
