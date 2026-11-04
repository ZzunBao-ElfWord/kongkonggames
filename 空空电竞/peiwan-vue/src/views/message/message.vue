<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="用户反馈" name="first">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="联系方式">
				</el-table-column>
				<el-table-column prop="content" label="内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 20, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="投诉消息" name="second">
			<span>投诉类型：</span>
			<el-select v-model="platform1" style="width:150px;margin-left: 10px;" @change="animeDat4(platform1)">
				<el-option v-for="item in platformList" :key="item.id" :label="item.name" :value="item.id">
				</el-option>
			</el-select>&nbsp;&nbsp;&nbsp;
			<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="primary"
				icon="document" @click="magNotice">消息推送</el-button>
			<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="danger"
				icon="document" @click="magNoticeDx">短信推送</el-button>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="投诉用户">
					<template slot-scope="scope">
						<!-- <div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.userId)">
							{{scope.row.userName}}
						</div> -->
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
							:disabled="!isAuth('userList:viewDetails')" @click="updataDetails(scope.row.userId)">
							{{scope.row.userName ? scope.row.userName : '未绑定'}}
						</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="被投诉用户">
					<template slot-scope="scope">
						<!-- <div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.byUserId)">
							{{scope.row.byUserName}}
						</div> -->
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
							:disabled="!isAuth('userList:viewDetails')" @click="updataDetails(scope.row.byUserId)">
							{{scope.row.byUserName ? scope.row.byUserName : '未绑定'}}
						</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="platform" label="投诉类型">
					<template slot-scope="scope">
						<div v-if="scope.row.platform==1">聊天投诉</div>
						<div v-if="scope.row.platform==2">用户投诉</div>
						<div v-if="scope.row.platform==3">动态投诉</div>
						<div v-if="scope.row.platform==4">服务投诉</div>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="title" label="投诉标题">
				</el-table-column> -->
				<el-table-column prop="content" label="投诉内容" width="200">
				</el-table-column>
				<el-table-column prop="image" label="图片" width="150">
					<template slot-scope="scope">
						<!-- <img :src="scope.row.goodsCover" alt="" width="60" height="60"> -->
						<div v-for="(item,index) in scope.row.image" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待审核</div>
						<div v-else>已审核</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="处理结果">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待处理</div>
						<div v-if="scope.row.status==1">不处理</div>
						<div v-if="scope.row.status==2">已封号</div>
						<div v-if="scope.row.status==3">已删除</div>
						<div v-if="scope.row.status==4">已处理</div>
					</template>
				</el-table-column>
				<el-table-column prop="auditContent" label="审核内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="scope.row.status==0"
							@click="refuseClick(scope.row)">处理
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 20, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="用户消息" name="fourth">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="primary"
					icon="document" @click="magNotice">消息推送</el-button>
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="danger"
					icon="document" @click="magNoticeDx">短信推送</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button> -->
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
							:disabled="!isAuth('userList:viewDetails')" @click="updataDetails(scope.row.userId)">
							用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 20, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="订单消息" name="fiveth">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="primary"
					icon="document" @click="magNotice">消息推送</el-button>
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="danger"
					icon="document" @click="magNoticeDx">短信推送</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button> -->
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
							:disabled="!isAuth('userList:viewDetails')" @click="updataDetails(scope.row.userId)">
							用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 20, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="约玩消息" name="seven">
			<!-- <div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="primary"
					icon="document" @click="magNotice">消息推送</el-button>
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="danger"
					icon="document" @click="magNoticeDx">短信推送</el-button>
			</div> -->
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button> -->
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
							:disabled="!isAuth('userList:viewDetails')" @click="updataDetails(scope.row.userId)">
							用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 20, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="官方公告" name="six">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="primary"
					icon="document" @click="magNotice">消息推送</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button> -->
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
							:disabled="!isAuth('userList:viewDetails')" @click="updataDetails(scope.row.userId)">
							用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 20, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="消息推送" name="seventh">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini"  type="primary" icon="document" @click="magNotice">消息推送</el-button>
			</div>
			<el-table
			v-loading="tableDataLoading"
				:data="tableData.list">
			<el-table-column
				fixed
			 prop="id"
			 label="编号"
			 width="80">
			 </el-table-column>
			 <el-table-column
				prop="userName"
				label="用户名称">
				</el-table-column>
				<el-table-column
				 prop="title"
				 label="消息标题">
				 </el-table-column>
				<el-table-column
				prop="content"
				label="消息内容"
				width="280">
				</el-table-column>
				<el-table-column
				prop="createAt"
				label="创建时间">
				</el-table-column>
				<el-table-column
					label="操作"
				width="120">
					<template slot-scope="scope">
						<el-button
							size="mini"
							type="primary"
							@click="updataDetails(scope.row)">用户详情
						</el-button>
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary" :disabled="!isAuth('userList:viewDetails')" @click="updataDetails(scope.row.userId)">
							用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination
					@size-change="handleSizeChange"
					@current-change="handleCurrentChange"
					:page-sizes="[5, 10, 20, 50, 100]"
					:page-size="limit"
					:current-page="page"
					layout="total,sizes, prev, pager, next,jumper"
					:total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane> -->
		<!-- 消息推送 -->
		<el-dialog title="消息推送" :visible.sync="dialogFormVisible2" center>
			<div style="margin-bottom: 10px;" v-if="state==6">
				<span style="width: 200px;display: inline-block;text-align: right;">选择：</span>
				<el-select v-model="flag" placeholder="请选择类型" style="width:50%">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin-bottom: 10px;" v-if="flag==1">
				<span style="width: 200px;display: inline-block;text-align: right;">用户手机号：</span>
				<el-input style="width: 50%;" v-model="phone" placeholder="请输入用户手机号"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="state==6">
				<span style="width: 200px;display: inline-block;text-align: right;">消息标题：</span>
				<span>官网公告</span>
			</div>
			<div style="margin-bottom: 10px;" v-else>
				<span style="width: 200px;display: inline-block;text-align: right;">消息标题：</span>
				<el-input style="width: 50%;" v-model="title" placeholder="请输入消息标题"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">消息内容：</span>
				<el-input style="width: 50%;" type="textarea" rows="4" v-model="content" placeholder="请输入消息内容">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="magNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="投诉处理" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="radio">
					<el-radio :label="0">通过</el-radio>
					<el-radio :label="1">驳回</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="radio===1">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">驳回理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入驳回理由">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="radio===0">
				<span style="width: 200px;display: inline-block;text-align: right;">处理结果：</span>
				<el-radio-group v-model="clStatus">
					<el-radio :label="0">封号</el-radio>
					<el-radio :label="1" v-if="platform==3||platform==4">删除</el-radio>
					<el-radio :label="2">其他</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="clStatus===2">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">处理说明：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入处理说明">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 短信推送 -->
		<el-dialog title="短信推送" :visible.sync="dialogFormVisibleDx" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">用户手机号：</span>
				<el-input style="width: 50%;" v-model="phone" placeholder="请输入用户手机号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">短信标题：</span>
				<span v-if="state==4">您好，您有一个订单请尽快处理</span>
				<span v-if="state==5||state==3">您好，您的账号存在违规</span>
				<!-- <el-input style="width: 50%;" v-model="title" placeholder="请输入消息标题"></el-input> -->
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">原因：</span>
				<el-input style="width: 50%;" type="textarea" rows="4" v-model="content" placeholder="请输入原因"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleDx = false">取 消</el-button>
				<el-button type="primary" @click="magNoticeToDx()">确 定</el-button>
			</div>
		</el-dialog>
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				limit: 5,
				page: 1,
				state: 2,
				url: '',
				title: '',
				type: '',
				id: '',
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
				],
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				phone: '',
				content: '',
				flag: 1,
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				tableData: [],
				radio: '',
				clStatus: '',
				messageId: '',
				platform: '',
				platform1: '',
				platformList: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '聊天投诉'
					},
					{
						id: 2,
						name: '用户投诉'
					},
					{
						id: 3,
						name: '动态投诉'
					},
					{
						id: 4,
						name: '服务投诉'
					}
				],
				dialogFormVisibleDx: false,

			}
		},
		methods: {
			// 详情跳转
			updataDetails(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				});
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '用户反馈') {
					this.page = 1
					this.limit = 5
					this.state = 2
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '投诉消息') {
					this.page = 1
					this.limit = 5
					this.state = 3
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '用户消息') {
					this.page = 1
					this.limit = 5
					this.state = 5
					this.dataSelect()
				}
				if (tab._props.label == '订单消息') {
					this.page = 1
					this.limit = 5
					this.state = 4
					this.dataSelect()
				}
				if (tab._props.label == '消息推送') {
					this.page = 1
					this.limit = 5
					this.state = 8
					this.dataSelect()
				}

				if (tab._props.label == '官方公告') {
					this.page = 1
					this.limit = 5
					this.state = 6
					this.dataSelect()
				}
				if (tab._props.label == '约玩消息') {
					this.page = 1
					this.limit = 5
					this.state = 7
					this.dataSelect()
				}

			},
			// 消息推送
			magNotice() {
				this.title = ''
				this.content = ''
				this.phone = ''
				this.flag = 1
				this.dialogFormVisible2 = true
			},
			// 消息推送
			magNoticeTo() {
				if (this.state == 6) {
					this.title = '官方公告'
					if (this.flag == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择类型',
							type: 'warning'
						});
						return
					}

				}

				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					});
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				if (this.flag == 1) {
					if (this.phone == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入用户手机号',
							type: 'warning'
						});
						return
					}
				}
				if (this.state == 3) {
					var state = 5
				} else {
					var state = this.state
				}
				this.$http({
					url: this.$http.adornUrl('cash/sendMsg'),
					method: 'get',
					params: this.$http.adornParams({
						'title': this.title,
						'content': this.content,
						'phone': this.phone,
						'flag': this.flag,
						'state': state,

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
								
								this.dialogFormVisible2 = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.title = ''
								this.content = ''
								this.phone = ''
								this.flag = 1
								this.dataSelect()
							}
						})
					}
				})
				// }
				// if (this.flag == 2) {
				// 	this.$http({
				// 		url: this.$http.adornUrl('cash/sendMsg'),
				// 		method: 'get',
				// 		params: this.$http.adornParams({
				// 			'title': this.title,
				// 			'content': this.content,
				// 			'flag': this.flag,
				// 			'state': this.state,
				// 		})
				// 	}).then(({
				// 		data
				// 	}) => {

				// 		if (data.code == 0) {
				// 			this.$message({
				// 				message: '操作成功',
				// 				type: 'success',
				// 				duration: 1500,
				// 				onClose: () => {
				// 					this.title = ''
				// 					this.content = ''
				// 					this.flag = ''
				// 					this.dialogFormVisible2 = false
				// 					this.dataSelect()
				// 				}
				// 			})
				// 		}else{
				// 			this.$message({
				// 				message: data.msg,
				// 				type: 'error',
				// 				duration: 1500,
				// 				onClose: () => {
				// 					this.title = ''
				// 					this.content = ''
				// 					this.phone = ''
				// 					this.flag = ''
				// 					this.dataSelect()
				// 				}
				// 			})
				// 		}
				// 	})
				// }
			},
			// 短信推送
			magNoticeDx() {
				console.log('this.state', this.state)
				this.dialogFormVisibleDx = true
			},
			// 短信推送
			magNoticeToDx() {


				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				if (this.phone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入用户手机号',
						type: 'warning'
					});
					return
				}
				if (this.state == 4) {
					var states = 'orders'
				}
				if (this.state == 5 || this.state == 3) {
					var states = 'weiGui'
				}
				this.$http({
					url: this.$http.adornUrl('cash/sendSms'),
					method: 'post',
					params: this.$http.adornParams({
						'content': this.content,
						'phone': this.phone,
						'state': states,

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
								this.title = ''
								this.content = ''
								this.phone = ''
								this.flag = ''
								this.dialogFormVisibleDx = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
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
					url: this.$http.adornUrl(`message/selectMessageByType`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'platform': this.platform1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (this.state == 2) {
						for (var i in data.data.list) {
							var a = data.data.list[i].content.toString()
							console.log('aaaaa', a)
							if (a == '我要注销账号！') {
								data.data.list[i].content = a
							} else {
								var b = JSON.parse(a)
								if (data.data.list[i].content) {
									data.data.list[i].content = b.content
								}
							}

						}
					}
					for (var i in data.data.list) {
						if (data.data.list[i].image) {
							data.data.list[i].image = data.data.list[i].image.split(',')
						}

					}

					let returnData = data.data;
					this.tableData = returnData

				})
			},
			//审核弹框
			refuseClick(rows) {
				this.radio = ''
				this.clStatus = ''
				this.content = ''
				this.messageId = rows.id
				this.platform = rows.platform
				this.dialogFormVisible = true
			},
			//提交审核
			refuseto() {
				if (this.radio === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}
				if (this.radio === 1) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入拒绝理由',
							type: 'warning'
						});
						return
					}
				}
				if (this.radio === 0) {
					if (this.clStatus === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择处理结果',
							type: 'warning'
						});
						return
					}
				}
				if (this.radio === 0) {
					if (this.clStatus === 2) {
						if (this.content == '') {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '请输入处理原因',
								type: 'warning'
							});
							return
						}
					}
				}

				var status = ''
				if (this.radio === 1) {
					status = 1
				} else {
					if (this.clStatus === 0) {
						status = 2
					}
					if (this.clStatus === 1) {
						status = 3
					}
					if (this.clStatus === 2) {
						status = 4
					}
				}

				this.$http({
					url: this.$http.adornUrl('message/auditMessage'),
					method: 'post',
					params: this.$http.adornParams({
						'messageId': this.messageId,
						'status': status,
						'auditContent': this.content
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = -1
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
			},
			animeDat4() {
				this.dataSelect()
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>

</style>
