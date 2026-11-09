<template>
	<div>
		<div style="margin:2% 0;display: inline-block;">
			<span>状态：</span>
			<el-select v-model="status" placeholder="请选择状态" style="width:150px;" @change="selectTrigger(status)">
				<el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<!-- <div style = "margin:2% 0;display: inline-block;">
      <span>话题名称：</span>
      <el-input style = "width: 150px;" @keydown.enter.native = "select" clearable placeholder = "请输入话题名称" v-model = "gtitle"></el-input>
      &nbsp;&nbsp;&nbsp;&nbsp;
    </div> -->
	<div style="margin:5px;display: inline-block;">
		<span>发布用户：</span>
		<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入发布用户"
			v-model="nickNameT"></el-input>
		&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div style="margin:5px;display: inline-block;">
		<span>发布内容：</span>
		<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入发布内容"
			v-model="contentT"></el-input>
		&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
		<div style="display: inline-block;">
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="guangaoData.records">
			<el-table-column fixed prop="talkId" label="编号" width="80"></el-table-column>
			<el-table-column prop="nickName" label="发布用户" width="150">
				<template slot-scope="scope">
					<span style="color: #4f9dec;cursor: pointer;"
						@click="updates(scope.row)">{{ scope.row.nickName?scope.row.nickName:'未绑定' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="imageUrl" label="用户头像" width="100">
				<template slot-scope="scope">
					<img :src="scope.row.imageUrl" alt="" width="60px" height="60">
				</template>
			</el-table-column>
			<el-table-column prop="content" label="发布内容" width="300">
				<template slot-scope="scope">
				<el-popover placement="top-start" title="" trigger="hover">
					<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">{{scope.row.content}}</div>
					<div style="width: 300px;">{{scope.row.content}}</div>
				</el-popover>
				</template>
			</el-table-column>
			<el-table-column prop="imgUrl" label="发布图片" width="210">
				<template slot-scope="scope">
					<div v-if="scope.row.img">
						<div v-for="(item,index) in scope.row.img.split(',')"
							style="display: inline-block;width: 60px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin-bottom: 10px;" :src="item" alt=""
									slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
					<div v-else>暂无图片</div>
				</template>
			</el-table-column>
			<el-table-column prop="videoUrl" label="发布视频" width="210">
				<template slot-scope="scope">
					<div>
						<video style="width: 100px;height: 100px;" v-if="scope.row.videoUrl" :src="scope.row.videoUrl"
							type="audio/wav" controls="controls"></video>

						<span v-else>暂无视频</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="commentCount" label="评论次数" width="80"></el-table-column>
			<el-table-column prop="goodTalkCount" label="点赞次数" width="80"></el-table-column>
			<!-- <el-table-column prop="shareNum" label="转发次数" width="80"></el-table-column> -->
			<el-table-column prop="status" label="状态" width="80" fixed="right">
				<template slot-scope="scope">
					<span v-if="scope.row.status=='1'">待审核</span>
					<span v-if="scope.row.status=='2'">已发布</span>
					<span v-if="scope.row.status=='3'">已下架</span>
				</template>
			</el-table-column>
			<el-table-column prop="auditContent" label="审核/下架理由" width="180"></el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
			<el-table-column label="操作" width="150" align="center" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('dynamic:shenhe')"
						v-if="scope.row.status=='1'" style="margin: 5px;" @click="advertshenhe(scope.row)">审核
					</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('dynamic:shenhe')"
						v-if="scope.row.status=='2'" style="margin: 5px;" @click="advertshenhe(scope.row,3)">下架
					</el-button>
					<el-button size="mini" type="primary" @click="talkAbout(scope.row)" style="margin: 5px;">评论列表
					</el-button>
					<el-button size="mini" type="warning" v-if="scope.row.isDelete!=1"
						:disabled="!isAuth('dynamic:delete')" style="margin: 5px;" @click="advertdeletes1(scope.row)">
						删除动态</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('dynamic:delete')" style="margin: 5px;"
						@click="advertdeletes(scope.row)">删除数据</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="guangaoData.total">
			</el-pagination>
		</div>
		<!-- 评论列表 -->
		<el-dialog title="评论列表" :visible.sync="dialogFormVisible1" center>
			<el-table v-loading="tableDataLoading" :data="pinglunData.content">
				<el-table-column fixed prop="commentId" label="编号" width="80"></el-table-column>
				<el-table-column prop="nickName" label="评论用户" width="100">
				</el-table-column>
				<el-table-column prop="imageUrl" label="用户头像" width="100">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" alt="" width="80px" height="80">
					</template>
				</el-table-column>
				<el-table-column prop="content" label="评论内容">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('dynamic:delete')"
							@click="pldeletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.totalElements">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 审核弹框 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible2" center>
			<div style="margin-bottom: 10px;" v-if="titles=='审核'">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="radio">
					<el-radio :label="2">通过</el-radio>
					<el-radio :label="3">驳回</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入理由">
				</el-input>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				size: 10,
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				guangaoData: {},
				pinglunData: {},
				talkId: '',
				gtitle: '',
				nickNameT:'',
				contentT:'',
				gambitId: '',
				status: 0,
				statusList: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '待审核'
					},
					{
						value: 2,
						label: '已发布'
					},
					{
						value: 3,
						label: '已下架'
					},
				],
				content: '',
				radio: '',
				titles: '审核'
			}
		},
		methods: {
			handleAvatarSuccess(file) {
				this.imgUrl = file.data
				this.linkUrl = this.imgUrl
			},
			handleAvatarSuccess1(file) {
				this.form.imgUrl = file.data
				this.form.linkUrl = this.form.imgUrl
			},
			handleSizeChange(val) {
				this.size = val
				this.guangaoSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.guangaoSelect()
			},
			handleSizeChange1(val) {
				this.size = val
				this.pinglunSelect(this.talkId)
			},
			handleCurrentChange1(val) {
				this.page = val
				this.pinglunSelect(this.talkId)
			},
			// 刷新
			refresh() {
				this.choicenSelect()
				this.handpick()
			},
			// 添加话题弹框
			advertNotice() {
				this.dialogFormVisible = true
			},
			// 添加话题
			advertNoticeTo() {
				if (this.linkUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入跳转链接',
						type: 'warning'
					})
					return
				}
				if (this.imgUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传话题图',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl(
						`banner/update/-1?url=${this.linkUrl}&image=${this.imgUrl}&type=${this.type}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.imgUrl = ''
							this.linkUrl = ''
							this.guangaoSelect()
						}
					})
				})
			},
			// 谈论列表
			talkAbout(row) {
				this.talkId = row.talkId
				this.pinglunSelect(this.talkId)
				this.dialogFormVisible1 = true
			},
			// 点击修改
			advertUpdates(rows) {
				this.form.id = rows.id
				this.form.imgUrl = rows.image_url
				this.form.linkUrl = rows.url
				this.dialogFormVisible1 = true
			},
			// 确定修改
			amendadvertTo() {
				if (this.form.linkUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入跳转链接',
						type: 'warning'
					})
					return
				}
				if (this.form.imgUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传话题图',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl(
						`banner/update/${this.form.id}?url=${this.form.linkUrl}&image=${this.form.imgUrl}&type=${this.type}`
					),
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
							this.guangaoSelect()
							this.dialogFormVisible1 = false
						}
					})
				})
			},
			// 删除数据
			advertdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteTalkById`),
						method: 'post',
						params: this.$http.adornParams({
							'talkId': row.talkId
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.guangaoSelect()
							}
						})
					})
				})
			},
			advertdeletes1(row) {
				this.$confirm(`确定删除此条动态?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteTalk`),
						method: 'post',
						params: this.$http.adornParams({
							'talkId': row.talkId
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.guangaoSelect()
							}
						})
					})
				})
			},
			// 删除评论
			pldeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteCommentById`),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.pinglunSelect(this.talkId)
							}
						})
					})
				})
			},
			// 获取话题数据
			guangaoSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('gambit/selectTalkPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'gambitId': this.gambitId,
						'isDelete': 1,
						'status': this.status,
						'nickName': this.nickNameT,
						'content': this.contentT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data.page
					this.guangaoData = returnData
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
			// 获取评论数据
			pinglunSelect(talkId) {
				let gambitId = this.$route.query.gambitId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('gambit/selectCommentPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'talkId': talkId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.pinglunData = returnData
				})
			},
			selectTrigger(status) {
				this.guangaoSelect()
			},
			select() {
				this.guangaoSelect()
			},
			cleans() {
				this.gambitId = ''
				this.gtitle = ''
				this.status = 0
				this.nickNameT= ''
				this.contentT= ''
				this.guangaoSelect()
			},
			// 审核
			advertshenhe(row, status) {
				if (status) {
					this.radio = status
					this.content == ''
					this.titles = '下架'
				} else {
					this.radio = ''
					this.content == ''
					this.titles = '审核'
				}
				this.talkId = row.talkId
				this.dialogFormVisible2 = true
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
				if (this.radio === 3) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入理由',
							type: 'warning'
						});
						return
					}
				}


				this.$http({
					url: this.$http.adornUrl('gambit/auditTalkStatus'),
					method: 'post',
					params: this.$http.adornParams({
						'talkId': this.talkId,
						'status': this.radio,
						'auditContent': this.content
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.radio = ''
								this.guangaoSelect()
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
		},
		mounted() {
			let gambitId = this.$route.query.gambitId
			this.gambitId = gambitId
			this.guangaoSelect()
		}
	}
</script>

<style>
	.customWidth {
		width: 80% !important;
	}
</style>
