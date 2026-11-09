<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="技能评论" name="first">

				<div style="margin:6px 0;display: inline-block;">
					<span>用户昵称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:6px 0;display: inline-block;">
					<span>手机号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:6px 0;display: inline-block;">
					<span>评论内容/关键字：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" clearable placeholder="请输入评论内容/关键字"
						v-model="content"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh">刷新
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="头像">
						<template slot-scope="scope">
							<img :src="scope.row.avatar" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="content" label="评论内容">
					</el-table-column>
					<!-- <el-table-column label="评论图片" width="150">
				<template slot-scope="scope">
					<div v-if="scope.row.img == null || scope.row.img == ''">
						暂无图片
					</div>
					<div v-else-if="scope.row.img.includes(',')" style="display:flex;flex-wrap: wrap;">
						<div v-for="item in scope.row.img.split(',')" style="margin: 2px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="item" alt="">
							</el-popover>
						</div>
					</div>
					<div v-else>
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="scope.row.img" alt="" slot="reference">
							<img style="width: 200px; height: 200px" :src="scope.row.img" alt="">
						</el-popover>
					</div>
				</template>
			</el-table-column> -->
					<el-table-column prop="score" label="评分">
						<template slot-scope="scope">
							<span>{{scope.row.score?scope.row.score:'暂无评分'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('comment:delete')"
								@click="deletes(scope.row)">删除
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
			</el-tab-pane>

			<el-tab-pane label="动态评论" name="scond">

				<div style="margin:6px 0;display: inline-block;">
					<span>用户昵称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="selectDt" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:6px 0;display: inline-block;">
					<span>手机号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="selectDt" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:6px 0;display: inline-block;">
					<span>评论内容/关键字：</span>
					<el-input style="width: 200px;" @keydown.enter.native="selectDt" clearable placeholder="请输入评论内容/关键字"
						v-model="content"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="selectDt">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleansDt">重置
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refreshDt">
						刷新
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="commentId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates(scope.row)">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="imageUrl" label="头像">
						<template slot-scope="scope">
							<img :src="scope.row.imageUrl" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="content" label="评论内容">
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
					</el-table-column>
					<el-table-column prop="score" label="评分">
						<template slot-scope="scope">
							<span>{{scope.row.score?scope.row.score:'暂无评分'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('comment:delete')"
								@click="pldeletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeDt" @current-change="handleCurrentChangeDt"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.total">
					</el-pagination>
				</div>
			</el-tab-pane>

			<!-- 回复 -->
			<el-dialog title="回复" :visible.sync="dialogFormVisible" center>
				<el-form :model="form">
					<el-form-item label="回复内容：" :label-width="formLabelWidth">
						<el-input v-model="form.reply" type="textarea" :rows="4" style="width:65%;"
							placeholder="请输入回复内容">
						</el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				goodsId: '',
				form: {
					commentId: '',
					reply: ''
				},
				scoreType: 0,
				scoreTypes: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '好评'
					},
					{
						value: 2,
						label: '中评'
					},
					{
						value: 3,
						label: '差评'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				content: '',
				title: '',
				userName: '',
				phone: '',
				activeName: 'first',

			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChangeDt(val) {
				this.size = val;
				this.dataSelectDt()
			},
			handleCurrentChangeDt(val) {
				this.page = val;
				this.dataSelectDt()
			},
			// tabs切换
			handleClick(tab, event) {
				this.phone = ''
				this.page = 1
				this.userName = ''
				if (tab._props.label == '技能评论') {
					this.dataSelect()
				}
				if (tab._props.label == '动态评论') {
					this.dataSelectDt()
				}
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			//搜索
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.userName = ''
				this.phone = ''
				this.content = ''
				this.page = 1
				this.dataSelect()
			},
			// 刷新
			refreshDt() {
				this.dataSelectDt()
			},
			//搜索
			selectDt() {
				this.page = 1
				this.dataSelectDt()
			},
			// 重置
			cleansDt() {
				this.userName = ''
				this.phone = ''
				this.content = ''
				this.page = 1
				this.dataSelectDt()
			},
			// 回复
			replyClick(row) {
				this.dialogFormVisible = true
				this.form.commentId = row.commentId
				this.form.reply = row.reply
			},
			replyNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('/selfGoodsComment/reply'),
					method: 'get',
					params: this.$http.adornParams({
						'commentId': this.form.commentId,
						'reply': this.form.reply,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '回复成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
					this.dialogFormVisible = false
				})
			},
			// 删除评论
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`takingComment/deleteOrderTakingComment/${row.id}`),
						method: 'post',
						params: this.$http.adornParams({})
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
				}).catch(() => {})
			},
			// 获取技能评论数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('takingComment/selectOrderTakingComment'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userName': this.userName,
						'content': this.content,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 获取动态评论
			dataSelectDt() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('app/gambit/selectCommentPageAdmin'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userName': this.userName,
						'content': this.content,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
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
			// 商品跳转
			toGoods(title) {
				this.$router.push({
					path: '/shopAdmin1',
					query: {
						title: title
					}
				})
			},
			// 删除动态评论
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
								this.dataSelectDt()
							}
						})
					})
				})
			},
		},
		activated() {
			this.dataSelect()
		}
	};
</script>

<style>
</style>
