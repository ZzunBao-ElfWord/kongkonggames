<template>
	<div>
		<div style="position: relative;display: inline-block;">
			<span>状态：</span>
			<el-select v-model="status1" style="width:150px;margin-left: 10px;" @change="phoneSelect(status1)">
				<el-option v-for="item in statuss" :key="item.value" :label="item.title" :value="item.id">
				</el-option>
			</el-select>&nbsp;&nbsp;&nbsp;
		</div>
		<div style="position: relative;display: inline-block;">
			<span>名称：</span>
			<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入名称" v-model="search">
			</el-input>&nbsp;&nbsp;
		</div>
		<div style="margin-right:2%;display: inline-block;">
			<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="phoneSelect">查询
			</el-button>
			<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleans2">重置
			</el-button>&nbsp;&nbsp;
			<el-button style='margin: 10px 0;' :disabled="!isAuth('rewardGift:add')" size="mini" type="primary"
				icon="document" @click="addNotice">添加礼物</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.list">
			<el-table-column fixed prop="giftId" label="编号" width="50">
			</el-table-column>
			<el-table-column prop="giftImg" label="图片">
				<template slot-scope="scope">
					<img :src="scope.row.giftImg" width="60" height="60" />
				</template>
			</el-table-column>
			<el-table-column prop="giftGif" label="GIF图片">
				<template slot-scope="scope">
					<div v-if="scope.row.giftGif">
						<div v-for="(item,index) in scope.row.giftGif.split(',')" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="giftName" label="名称">
			</el-table-column>
			<el-table-column prop="giftMoney" label="金币">
			</el-table-column>
			<el-table-column prop="sort" label="排序">
			</el-table-column>
			<el-table-column prop="status" label="状态">
				<template slot-scope="scope">
					<span style="color: #008200;" v-if="scope.row.status==1">正常</span>
					<span v-if="scope.row.status==2">下架</span>
				</template>
			</el-table-column>
			<el-table-column label="操作" width="180">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('rewardGift:update')"
						@click="amendBanner(scope.row)">修改
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('rewardGift:delete')"
						@click="deletes(scope.row)">删除
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
			</el-pagination>
		</div>
		<el-dialog :title="title" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="giftImg"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess">
						<img v-if="giftImg" :src="giftImg" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">GIF图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="giftGif"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess2">
						<img v-if="giftGif" :src="giftGif" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="giftName" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">价格：</span>
				<el-input style="width:50%;" v-model="giftMoney" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" placeholder="请输入排序"
					onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="status">
					<el-radio :label="1">正常</el-radio>
					<el-radio :label="2">下架</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: [],
				giftName: '',
				giftMoney: '',
				giftImg: '',
				giftGif:'',
				giftId: '',
				sort: '',
				status: 1,
				status1: '',
				title: '添加',
				statuss: [{
						id: '',
						title: '全部'
					},
					{
						id: 1,
						title: '正常'
					},
					{
						id: 2,
						title: '下架'
					}
				],
				search: '',
			}
		},
		methods: {
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('gift/selectGiftPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': this.status1,
						'giftName': this.search
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('会员列表', data)
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.giftImg = file.data
			},
			//上传成功
			handleAvatarSuccess2(file) {
				this.giftGif = file.data
			},
			addNotice() {
				this.giftImg = ''
				this.giftId = ''
				this.giftName = ''
				this.status = 1
				this.giftMoney = ''
				this.sort = ''
				this.giftGif = ''
				this.title = '添加'
				this.dialogFormVisible = true
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			// 添加banner图
			addNoticeTo() {
				if (this.giftImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					});
					return
				}
				if (this.giftGif == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传GIF图片',
						type: 'warning'
					});
					return
				}
				
				if (this.giftName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				if (this.giftMoney == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入价格',
						type: 'warning'
					});
					return
				}

				this.classify = 1
				if (this.title == '添加') {
					var url = 'gift/insertGift'
				} else {
					var url = 'gift/updateGift'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'giftImg': this.giftImg,
						'giftId': this.giftId,
						'giftName': this.giftName,
						'giftMoney': this.giftMoney,
						'sort': this.sort,
						'status': this.status,
						'giftGif':this.giftGif

					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								
								this.dataSelect()
							}
						})
					}else{
						this.$message({
							message: data,msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}
					
				})
			},
			// 修改
			amendBanner(rows) {
				
				this.dialogFormVisible = true
				this.giftId = rows.giftId
				this.giftImg = rows.giftImg
				this.giftMoney = rows.giftMoney
				this.sort = rows.sort
				this.giftName = rows.giftName
				this.status = rows.status
				this.giftMoney = rows.giftMoney
				this.giftGif = rows.giftGif
				this.title = '修改'
			},
			//删除一级
			deletes(row) {
				let delid = row.giftId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('gift/deleteGift'),
						method: 'post',
						params: this.$http.adornParams({
							'giftId': delid
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans2() {
				this.status1 = ''
				this.search = ''
				this.page = 1
				this.dataSelect()
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style>
</style>
