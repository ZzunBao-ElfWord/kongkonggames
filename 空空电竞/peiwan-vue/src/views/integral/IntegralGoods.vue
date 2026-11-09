<template>
	<div>
		<div style="float: right;margin-right:2%;">
			<el-button style='margin: 10px 0;' :disabled="!isAuth('vipPrivilege:add')" size="mini" type="primary"
				icon="document" @click="addNotice">添加</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.list">

			<el-table-column fixed prop="payClassifyId" label="编号" width="50">
			</el-table-column>
			<!-- <el-table-column prop="memberImg" label="图片">
        <template slot-scope="scope">
          　　<img :src="scope.row.memberImg" width="60" height="60" />
        </template>
      </el-table-column> -->
			<el-table-column prop="price" label="售价">
			</el-table-column>
			<el-table-column prop="money" label="金币数量">
			</el-table-column>
			<el-table-column prop="sort" label="排序">
			</el-table-column>
			<el-table-column prop="giveMoney" label="赠送金币">
			</el-table-column>
			<el-table-column label="操作" width="180">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('vipPrivilege:update')"
						@click="amendBanner(scope.row)">修改
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('vipPrivilege:delete')"
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
			<!-- <div style="margin-bottom: 10px;display: flex;">
    		<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
    		<div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
    			<el-upload
    				class="avatar-uploader"
    				v-model="memberImg"
    				:action="$http.adornUrl('alioss/upload')"
    				:show-file-list="false"
    				:on-success="handleAvatarSuccess"
    				>
    				<img v-if="memberImg" :src="memberImg" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;"/>
    				<i v-else class="el-icon-plus avatar-uploader-icon"></i>
    			</el-upload>
    		</div>
    	</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">售价：</span>
				<el-input style="width:50%;" v-model="price" placeholder="请输入售价"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">金币数量：</span>
				<el-input style="width:50%;" v-model="money" placeholder="请输入金币数量"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">赠送金币：</span>
				<el-input style="width:50%;" v-model="giveMoney" placeholder="请输入金币数量"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" placeholder="请输入排序"
					onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
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
				price: '',
				memberImg: '',
				memberId: '',
				money: '',
				giveMoney: '',
				sort: '',
				payClassifyId: '',
				title: '添加'
			}
		},
		methods: {
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('payClassify/selectPayClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit
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
				this.memberImg = file.data
			},
			addNotice() {
        this.money = ''
        this.memberId = ''
        this.price = ''
        this.giveMoney = ''
        this.sort = ''
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
				if (this.price === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入售价',
						type: 'warning'
					});
					return
				}
				if (this.money === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金币数量',
						type: 'warning'
					});
					return
				}
				if (this.giveMoney === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入赠送金币',
						type: 'warning'
					});
					return
				}
				if (this.title == '添加') {
					var url = 'payClassify/insertPayClassify'
				} else {
					var url = 'payClassify/updatePayClassify'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'money': this.money,
						'memberId': this.memberId,
						'price': this.price,
						'sort': this.sort,
						'giveMoney': this.giveMoney,
						'payClassifyId': this.payClassifyId,
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
            	message: data.msg,
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
				this.payClassifyId = rows.payClassifyId
				this.money = rows.money
				this.sort = rows.sort
				this.giveMoney = rows.giveMoney
				this.price = rows.price
				this.title = '修改'
			},
			//删除一级
			deletes(row) {
				let delid = row.payClassifyId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`payClassify/deletePayClassify/?payClassifyId=${delid}`),
						method: 'post',
						params: this.$http.adornParams({})
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
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style>
</style>
