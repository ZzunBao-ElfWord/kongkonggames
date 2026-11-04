<template>
	<div>
		<div style="font-size:18px;margin-bottom: 15px;">
			<a href="#" @click="prev" style="text-decoration:none;font-size: 14px;">
				<icon-svg name="jiantou" style="width: 1.2em;height: 1.2em;position: relative;top: 0.3em;"></icon-svg>
				返回
			</a>
			<span style="display: inline-block;margin: 0 15px;color: #D9D9D9;">|</span>
			<span>公会统计</span>
		</div>
		<!-- <div style="display: inline-block;">
      <span>开始时间：</span>
      <el-date-picker style="width: 250px;margin-left: 10px;" v-model="startTime" align="right" type="datetime"
        default-time="00:00:00" placeholder="选择开始时间">
      </el-date-picker>&nbsp;&nbsp;&nbsp;
      <span>截止时间：</span>
      <el-date-picker style="width: 250px;margin-left: 10px;" v-model="endTime" align="right" type="datetime"
        default-time="23:59:59" placeholder="选择截止时间">
      </el-date-picker>
      <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="timeDate">查询</el-button>
    </div> -->
		<div>
			<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="orderfenxi">
				<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>&nbsp;&nbsp;&nbsp;
			<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info.stockDate" align="right" type="date"
				format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间" @change="animeOrder">
			</el-date-picker>
		</div>
		<div v-loading="tableDataLoading">
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">总收益</div>
							<div class="text_color"><span>{{taskStat.sumMoney ? taskStat.sumMoney : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">订单收益</div>
							<div class="text_color"><span>{{taskStat.ordersMoney ? taskStat.ordersMoney : 0}}</span>元
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">礼物收益</div>
							<div class="text_color"><span>{{taskStat.giftMoney ? taskStat.giftMoney : 0}}</span></div>
						</div>
					</div>
				</el-col>
			</el-row>
			<!-- <span>状态：</span>
    	<el-select v-model = "status" style = "width:150px;margin-left: 10px;" @change = "animeDat2(status)">
    	  <el-option v-for = "item in taskstates" :key = "item.value" :label = "item.label" :value = "item.value"></el-option>
    	</el-select>&nbsp;&nbsp;&nbsp;
    	<div style = "position: relative;display: inline-block;">
    		<span>手机号:</span>
    	  <el-input style = "width: 200px;" @keydown.enter.native = "phoneSelect" placeholder = "请输入手机号" v-model = "phone"></el-input>&nbsp;&nbsp;
    	</div>
      <div style = "position: relative;display: inline-block;">
      	<span>昵称:</span>
        <el-input style = "width: 200px;" @keydown.enter.native = "phoneSelect" placeholder = "请输入昵称" v-model = "userName"></el-input>&nbsp;&nbsp;
      </div>
    	<el-button style = "margin-left:15px;" size = "mini" type = "primary" icon = "document" @click = "phoneSelect">查询</el-button>
    	 <el-button style = "margin-left:15px;" size = "mini" type = "primary" icon = "document" @click = "cleans2">重置</el-button> -->
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData2.records">
			<el-table-column prop="userId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="userName" label="用户名称" width="120">
				<template slot-scope="scope">
					　
					<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary" @click="updates(scope.row)">
						{{scope.row.userName ? scope.row.userName : '未绑定'}}
					</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="avatar" label="用户头像" width="100">
				<template slot-scope="scope">
					　　<img v-if="scope.row.avatar && scope.row.avatar!=''" :src="scope.row.avatar" width="40"
						height="40" />
					<span v-else>暂无图片</span>
				</template>
			</el-table-column>
			<el-table-column prop="money" label="收益">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="180">
			</el-table-column>
			<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="shouyiDels(scope.row)">收益详情
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
				:page-sizes="[10, 20, 30, 40,50,100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
			</el-pagination>
		</div>
		<!-- 收益详情 -->
		<el-dialog title="收益详情" :visible.sync="dialogFormVisible1" center>
			<div>
				<span>申请类型：</span>
				<el-select filterable v-model="classify" style="width:150px;margin-left: 10px;"
					@change="animeDat3(classify)">
					<el-option v-for="item in classifyList" :key="item.value" :label="item.title" :value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<el-table v-loading="tableDataLoading1" :data="shouyiData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="标题" width="180">

				</el-table-column>
				<el-table-column prop="content" label="内容" width="250">

				</el-table-column>
				<el-table-column prop="money" label="类型" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.classify==5">订单</span>
						<span v-if="scope.row.classify==6">礼物</span>
					</template>
				</el-table-column>
				<el-table-column prop="money" label="收益" width="100">
					<template slot-scope="scope">
						<span style="color: green;" v-if="scope.row.type==1"> + {{scope.row.money}}</span>
						<span style="color: red;" v-if="scope.row.type==2"> - {{scope.row.money}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="shouyiData.total">
				</el-pagination>
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
				limit1: 10,
				page1: 1,
				phone: '',
				status: '',

				id: '',
				tableDataLoading: false,
				tableDataLoading1: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: [],
				tableData2: {},

				userName: '',
				taskStat: {},
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
				flag: 1,
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				shouyiData: {},
				classify: 5,
				classifyList: [{
						id: 5,
						title: '订单'
					},
					{
						id: 6,
						title: '礼物'
					}
				],
				userId: '',
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
			timeDate() {
				this.classifySelect()
				this.taskdataSelect()
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
			// 订单分析选择日期
			animeOrder() {
				this.classifySelect()
				this.taskdataSelect()
			},
			// 订单分析年月日
			orderfenxi(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.classifySelect()
					this.taskdataSelect()
				}
				if (vanumber === 2) {
					this.flag = 2
					this.classifySelect()
					this.taskdataSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.classifySelect()
					this.taskdataSelect()
				}
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.limit1 = val
				this.shouyiSelect()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.shouyiSelect()
			},
			handleSizeChange3(val) {
				this.limit = val
				this.taskdataSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.taskdataSelect()
			},


			// 手机号搜索
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.phone = ''
				this.status = ''
				this.userName = ''
				this.page = 1
				this.taskdataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.phone = ''
				this.status = 0
				this.content = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			animeDat2(state) {
				this.page = 1
				this.taskdataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('help/selectStatusHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone,
						'status': this.status,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 获取接单数据列表
			taskdataSelect() {
				this.tableDataLoading = true
				this.consortiaId = this.$route.query.consortiaId
				this.$http({
					url: this.$http.adornUrl('consortia/selectConsortiaPersonMoneyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'consortiaId': this.consortiaId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData

				})
			},
			// 商铺统计
			classifySelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('consortia/selectConsortiaMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'consortiaId': this.consortiaId,
						// 'startTime': this.startTime,
						// 'endTime': this.endTime
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.taskStat = returnData
				})
			},
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 收益详情
			shouyiDels(row) {
				this.classify = 5
				this.userId = row.userId
				this.shouyiSelect()
				this.dialogFormVisible1 = true
			},
			// 收益详情数据
			shouyiSelect() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('consortia/selectConsortiaPersonMoneyDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'userId': this.userId,
						'consortiaId': this.consortiaId,
						'classify': this.classify
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.shouyiData = returnData
				})
			},
			animeDat3() {

				this.page1 = 1
				this.shouyiSelect()
			},
		},
		mounted() {
			this.consortiaId = this.$route.query.consortiaId
			this.classifySelect()
			this.taskdataSelect()

		},
		watch: {
			'$route': 'taskdataSelect'

		}
	}
</script>

<style>
	.box {
		padding: 44px;
		border: 1px solid #eee;
		margin: 15px 10px;
	}

	.box_num {
		font-size: 14px;
		color: #66b1ff;
	}

	.box_num .box_color {
		color: #333;
		font-size: 14px;
		margin-bottom: 15px;
	}

	.box_num div span {
		font-size: 20px;
		margin-left: 5px;
	}

	.text_color {
		color: #4f9dec;
	}

	.text_color span {
		margin-right: 5px;
	}
</style>
