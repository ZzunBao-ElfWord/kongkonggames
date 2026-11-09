<template>
	<div>
		<div style="display: inline-block;">
			<div style="position: relative;display: inline-block;margin: 3px;">
				<span>状态：</span>
				<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="animeDat2(status)">
					<el-option v-for="item in homeData1" :key="item.value" :label="item.name" :value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin: 3px;">
				<span>用户昵称：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入用户昵称" v-model="minMoney">
				</el-input>
			</div>
			<div style="position: relative;display: inline-block;margin: 3px;">
				<span> 手机号：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入手机号" v-model="maxMoney">
				</el-input>&nbsp;&nbsp;
			</div>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.list">
			<el-table-column prop="fastOrderId" label="编号" width="80" fixed="left">
			</el-table-column>
			<el-table-column prop="userName" label="发布用户">
				<template slot-scope="scope">
					　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row.userId)">
						{{scope.row.userName?scope.row.userName:'未绑定'}}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="userName" label="接单用户">
				<template slot-scope="scope">
					　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row.ordersUserId)">
					{{scope.row.ordersUserName?scope.row.ordersUserName:'未绑定'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="classify" label="要求性别" width="150">
				<template slot-scope="scope">
					<span v-if="scope.row.sex==1">男</span>
					<span v-else-if="scope.row.sex==2">女</span>
					<span v-else>不限</span>
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="手机号" width="150">
			</el-table-column>
			<!-- <el-table-column prop="serviceType" label="技能类型" width="150">
			</el-table-column> -->
			<el-table-column prop="gameName" label="游戏名称" width="150">
			</el-table-column>
			<el-table-column prop="money" label="价格区间" width="100">
			</el-table-column>
			<!-- <el-table-column prop="skill" label="技能水平" width="100">
			</el-table-column>
			<el-table-column prop="hero" label="擅长英雄" width="100">
			</el-table-column>
			<el-table-column prop="location" label="期望擅长位置" width="100">
			</el-table-column> -->
			<el-table-column prop="fastMoney" label="发布价格" width="150">
			</el-table-column>
			<el-table-column prop="myLevel" label="当前段位" width="100">
			</el-table-column>
			<el-table-column prop="mySystem" label="玩家系统" width="100">
			</el-table-column>
			<el-table-column prop="myArea" label="玩家大区" width="100">
			</el-table-column>
			<el-table-column prop="num" label="约玩局数" width="100">
			</el-table-column>
			<el-table-column prop="orderTakingTime" label="约玩时间" width="200">
			</el-table-column>
			<el-table-column prop="content" label="陪玩接单文字" width="150">
			</el-table-column>
			<el-table-column prop="voice" label="陪玩接单语音"  width="350">
				<template slot-scope="scope">
					<audio v-if="scope.row.voice!=''&&scope.row.voice!=null" :src="scope.row.voice" type="audio/wav"
						controls="controls"></audio>
					<span v-else>暂无语音</span>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="musicStyle" label="擅长曲风" width="100">
			</el-table-column>
			<el-table-column prop="musicLevel" label="音乐水平" width="200">
			</el-table-column> -->
			<el-table-column prop="remark" label="备注" width="250">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="180">
			</el-table-column>
			<el-table-column prop="status" label="状态" fixed="right">
				<template slot-scope="scope">
					<span style="color: #E87D0D;" v-if="scope.row.status==1">发起中</span>
					<span style="color: #009900;" v-if="scope.row.status==2">匹配成功</span>
					<span style="color: #999;" v-if="scope.row.status==3">取消约玩</span>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" fixed="right" label="操作" width="100">
				<template slot-scope="scope">
					<el-button size="mini" style="margin: 2px;" type="primary" :disabled="!isAuth('matching:quxiao')"
						v-if="scope.row.status==1" @click="cancelOrder(scope.row,2)">取消</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
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
				couponName: '',
				homeData1: [{
						id: 0,
						name: '全部'
					},
					{
						id: 1,
						name: '发起中'
					},
					{
						id: 2,
						name: '匹配成功'
					},
					{
						id: 3,
						name: '取消约玩'
					}
				],
				status: 0,
				maxMoney: '',
				minMoney: '',
				times: '',
				sex: 0, //1男 2女 不限0
				sexList: [{
						value: 0,
						label: '不限'
					},
					{
						value: 1,
						label: '男'
					}, {
						value: 2,
						label: '女'
					}
				],
				info: {
					stockDate: this.getNowTime(), //日期
				},
			}
		},
		methods: {
			// 详情跳转
			updatesvideo(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},
			//处理默认选中当前日期
			getNowTime() {
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
						'remek': ''
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
						'remek': this.content
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
				this.status = ''
				this.couponName = ''
				this.userName = ''
				this.maxMoney = ''
				this.minMoney = ''
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				// if(this.times){}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('fastOrder/selectFastOrderPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': this.status,
						'userName': this.minMoney,
						'phone': this.maxMoney

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 获取游戏分类启用列表
			homeSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`game/queryGame`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.homeData1 = [{
						id: '',
						gameName: '全部'
					}]
					if (data.data) {
						for (var i in data.data) {
							this.homeData1.push(data.data[i])
							data.data[i].state = Number(data.data[i].state)
						}
					}
					this.homeData = data.data
				})
			},
			// 筛选信息
			animeDat2(state) {
				this.dataSelect()
			},
			// 订单分析选择日期
			animeOrder() {
				this.dataSelect()
			},
			cancelOrder(row, status) {
				let content = '确定取消此订单'

				this.$confirm(content, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('fastOrder/cancelFastOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'fastOrderId': row.fastOrderId
						})
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
				})
			},
		},
		mounted() {
			this.dataSelect()
			// this.homeSelect()
		}
	};
</script>

<style>

</style>
