<template>
	<div>
		<div style="display: inline-block;">
			<span>状态：</span>
			<el-select v-model="statusT" style="width:150px;margin-left: 10px;" @change="select()">
				<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>&nbsp;&nbsp;
			<div style="position: relative;display: inline-block;">
				<span>姓名：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入姓名" v-model="userName">
				</el-input>&nbsp;&nbsp;
			</div>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
			<!-- <el-button style='margin:0 0 20px 20px;' v-if="isAuth('autonym:pass')" size="mini" type="primary"
				icon="document" @click="passClick()" :disabled="checkBoxData.length <= 0">通过</el-button> -->
		</div>
		<el-table  v-loading="tableDataLoading" :data="tableData.list">
			<!-- @selection-change="changeFun" <el-table-column type="selection" fixed>
			</el-table-column> -->
			<el-table-column prop="id" label="编号" width="80" fixed="left">
			</el-table-column>
			<!-- <el-table-column prop="classify" label="类型" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.classify==null || scope.row.classify==1 ">企业</span>
					<span v-else>个人</span>
				</template>
			</el-table-column> -->
			<el-table-column prop="name" label="用户昵称" width="100"  fixed="left">
				<template slot-scope="scope">
					<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)" v-if="scope.row.userEntity">
						{{scope.row.userEntity.userName ? scope.row.userEntity.userName : '未绑定'}}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="name" label="姓名" width="100" fixed="left">
				<template slot-scope="scope">
					<span style="color: #4f9dec;cursor: pointer;"
						@click="updates(scope.row)">{{scope.row.name ? scope.row.name : '未绑定'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="电话" width="150">
				<template slot-scope="scope">
					<span v-if="scope.row.userEntity">{{scope.row.userEntity.phone }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="idNumber" label="身份证号码" width="200">
			</el-table-column>
			<el-table-column prop="cardFront" label="身份证正面" width="150">
				<template slot-scope="scope">
					<div v-if="scope.row.front == null || scope.row.front == ''">
						暂无图片
					</div>
					<div v-if="scope.row.front" style="display:flex;flex-wrap: wrap;">
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="scope.row.front" alt="" slot="reference">
							<img style="width: 200px; height: 200px" :src="scope.row.front" alt="">
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="cardBack" label="身份证反面" width="150">
				<template slot-scope="scope">
					<div v-if="scope.row.back == null || scope.row.back == ''">
						暂无图片
					</div>
					<div v-if="scope.row.back" style="display:flex;flex-wrap: wrap;">
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="scope.row.back" alt="" slot="reference">
							<img style="width: 200px; height: 200px" :src="scope.row.back" alt="">
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="cardBack" label="技能图" width="400">
				<template slot-scope="scope">
					<div v-if="scope.row.skillImg == null || scope.row.skillImg == ''">
						暂无图片
					</div>
					<div v-if="scope.row.skillImg" style="display:flex;flex-wrap: wrap;">
						<div v-for="(item,index) in scope.row.skillImg.split(',')">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 200px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
						
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="cardBack" label="生活照" width="400">
				<template slot-scope="scope">
					<div v-if="scope.row.lifePhoto == null || scope.row.lifePhoto == ''">
						暂无图片
					</div>
					<div v-if="scope.row.lifePhoto" style="display:flex;flex-wrap: wrap;">
						<div v-for="(item,index) in scope.row.lifePhoto.split(',')">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 200px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
						
					</div>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="serviceType" label="服务类型" width="160">
			</el-table-column> -->
			<el-table-column prop="details" label="个人简介" width="250">
				<template slot-scope="scope">
				  <el-popover placement="top-start" title="" trigger="hover">
				  	<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">{{scope.row.details}}</div>
				  	<div style="width: 300px;">{{scope.row.details}}</div>
				  </el-popover>
				</template>
			</el-table-column>
			<el-table-column prop="remek" label="审核内容" width="160">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column fixed='right' prop="status" label="状态" width="160">
				<template slot-scope="scope">
					<span v-if="scope.row.status == 0">待审核</span>
					<span v-if="scope.row.status == 1">审核成功</span>
					<span v-if="scope.row.status == 2">已拒绝</span>
				</template>
			</el-table-column>
			<el-table-column fixed='right' label="操作" width="160">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="scope.row.status !== 0 || !isAuth('autonym:jujue')"
						@click="refuseClick(scope.row)" style="margin: 3px;">
						拒绝
					</el-button>
					<el-button size="mini" type="primary" :disabled="scope.row.status !== 0 || !isAuth('autonym:tongguo')"
						@click="passClicks(scope.row)" style="margin: 3px;">通过
					</el-button>
					<el-button size="mini" type="warning" :disabled="!isAuth('autonym:update')"
						@click="updateClicks(scope.row)" style="margin: 3px;">修改
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('autonym:delete')"
						@click="deleteClicks(scope.row)" style="margin: 3px;">删除
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[5,10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
			</el-pagination>
		</div>
		<!-- 任务拒绝弹框 -->
		<el-dialog title="审核拒绝" :visible.sync="dialogFormVisible" center>
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
		<el-dialog title="修改认证信息" :visible.sync="dialogFormVisibleR" center width="80%">
			
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">项目类型：</span>
				<el-select v-model="classifyIds" style="width:50%;margin-left: 10px;">
					<el-option v-for="item in fwData" :key="item.id" :label="item.value" :value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
				<el-input style="width:50%;" v-model="name" type="text" placeholder="请输入姓名"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">电话：</span>
				<el-input style="width:50%;" v-model="phone" type="text" placeholder="请输入电话"></el-input>
			</div> -->
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">性别：</span>
				<el-radio-group v-model="sex">
					<el-radio :label="'1'">男</el-radio>
					<el-radio :label="'2'">女</el-radio>
				</el-radio-group>
			</div> -->
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">生日：</span>
				<el-date-picker style="width: 50%;" v-model="birth" align="right"
					type="date"  format="yyyy年MM月dd日" value-format="yyyy年MM月dd日" placeholder="选择日期">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">个人简介：</span>
				<el-input style="width:50%;" v-model="details" type="textarea" :rows="3" placeholder="请输入个人简介"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证号：</span>
				<el-input style="width:50%;" v-model="idNumber" type="text" placeholder="请输入身份证号"></el-input>
			</div>
			
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
				<div style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="avatar"
						:action="$http.adornUrl('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess5">
						<img v-if="avatar" :src="avatar" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证正面图：</span>
				<div class="imgs" style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="front"
						:action="$http.adornUrl('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="front" :src="front" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d;border: 1px dashed #ccc;width: 148px;line-height: 148px;border-radius: 5px;"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证反面图：</span>
				<div class="imgs" style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="back"
						:action="$http.adornUrl('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess2">
						<img v-if="back" :src="back" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d;border: 1px dashed #ccc;width: 148px;line-height: 148px;border-radius: 5px;"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;display:flex;flex-direction: row;flex-wrap: wrap;">
				<span style="width: 200px;display: inline-block;text-align: right;">技能图：</span>
				<div class="imgs" v-for="(item,index) in skillImg" :key="index">
					<img width="100%" class="images" height="100%" :src="item" alt="">
					<span class="dels">
						<i class="el-icon-delete" @click="clear1(index)"></i>
					</span>
				</div>
				<div class="imgs" style="">
					<el-upload :action="$http.adornUrl('alioss/upload')"
						list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess3"
						:on-progress="onprogress1">
						<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
							:percentage="percentage1"></el-progress>
						<i v-else class="el-icon-plus"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;display:flex;flex-direction: row;flex-wrap: wrap;">
				<span style="width: 200px;display: inline-block;text-align: right;">生活照：</span>
				<div class="imgs" v-for="(item,index) in lifePhoto" :key="index">
					<img width="100%" class="images" height="100%" :src="item" alt="">
					<span class="dels">
						<i class="el-icon-delete" @click="clear2(index)"></i>
					</span>
				</div>
				<div class="imgs" style="">
					<el-upload :action="$http.adornUrl('alioss/upload')"
						list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess4"
						:on-progress="onprogress2">
						<el-progress v-if="percentage2>0 && percentage2<100" type="circle"
							:percentage="percentage2"></el-progress>
						<i v-else class="el-icon-plus"></i>
					</el-upload>
				</div>
			</div>
			
		
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleR = false">取 消</el-button>
				<el-button type="primary" @click="refusetoUp()">确 定</el-button>
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
				statusT:'',
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
				// 修改信息
				birth:'',
				classifyIds:'',
				phone: '',
				name: '',
				sex:'',
				idNumber: '',
				front: '',
				back: '',
				avatar:'',
				details:'',
				userId: '',
				dialogFormVisibleR: false,
				userIds:'',
				percentage1: 0,
				percentage2:0,
				skillImg:[],
				lifePhoto:[],
				info: {
					stockDate: this.getNowTime(), //日期
				},
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
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
				this.statusT = ''
				this.userName = ''
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('userCertification/queryCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': this.statusT,
						'name': this.userName
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 删除认证
			deleteClicks(row){
				this.$confirm(`确定要删除此条信息?删除后无法恢复！`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('userCertification/deleteCertification'),
						method: 'post',
						params: this.$http.adornParams({
							'id':row.id
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
								}
							})
						}
						
					})
				}).catch(() => {})
			},
			// 修改弹框
			updateClicks(row){
				this.birth = row.birth
				this.ids = row.id
				this.classifyIds = row.classifyId
				this.name = row.name
				this.sex = row.sex
				this.details = row.details
				this.percentage1 = 0
				this.percentage2 = 0
				this.skillImg = row.skillImg.split(',')
				this.lifePhoto = row.lifePhoto.split(',')
				this.phone = row.userEntity.phone
				this.idNumber = row.idNumber
				this.front = row.front
				this.avatar = row.avatar
				this.back = row.back
				this.userId = row.userId
				this.dialogFormVisibleR = true
			},
			// 确认吸怪
			refusetoUp() {
				
				this.$http({
					url: this.$http.adornUrl('userCertification/updateCertification'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.userId,
						// 'classifyId': this.classifyIds,
						'name': this.name,
						// 'sex':this.sex,
						// 'address':this.address,
						'lifePhoto':this.lifePhoto.toString(),
						'skillImg':this.skillImg.toString(),
						// 'phone':this.phone,
						'details':this.details,
						'idNumber': this.idNumber,
						'front': this.front,
						// 'avatar':this.avatar,
						'back': this.back,
						'birth': this.birth,
						'id':this.ids
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
								this.dialogFormVisibleR = false
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
			// 上传
			handleAvatarSuccess1(file, fileList) {
				this.front = file.data
			},
			handleAvatarSuccess2(file, fileList) {
				this.back = file.data
			},
			handleAvatarSuccess5(file, fileList) {
				this.avatar = file.data
			},
			handleUploadSuccess3(file, fileList) {
				this.skillImg.push(file.data)
				console.log('this.contentImg', this.skillImg)
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			handleUploadSuccess4(file, fileList) {
				this.lifePhoto.push(file.data)
				console.log('this.contentImg', this.lifePhoto)
			},
			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage2 = parseInt(event.percent)
			},
			// 删除
			clear1(index) {
				this.skillImg.splice(index, 1);
			},
			clear2(index) {
				this.lifePhoto.splice(index, 1);
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>
.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}
</style>
