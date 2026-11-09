<template>
	<div>
		<div style="position: relative;display: inline-block;margin: 3px;">
			<span>赠送用户：</span>
			<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入赠送用户" v-model="sendUserName">
			</el-input>&nbsp;&nbsp;
		</div>
		<div style="position: relative;display: inline-block;margin: 3px;">
			<span>赠送用户手机号：</span>
			<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入赠送用户手机号" v-model="sendPhone">
			</el-input>&nbsp;&nbsp;
		</div>
		<div style="position: relative;display: inline-block;margin: 3px;">
			<span>接收用户：</span>
			<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入接收用户" v-model="userName">
			</el-input>&nbsp;&nbsp;
		</div>
		<div style="position: relative;display: inline-block;margin: 3px;">
			<span>接收用户手机号：</span>
			<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入接收用户手机号" v-model="phone">
			</el-input>&nbsp;&nbsp;
		</div>
		<div style="position: relative;display: inline-block;margin: 3px;">
			<span>礼物名称：</span>
			<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入礼物名称"
				v-model="giftName"></el-input>&nbsp;&nbsp;
		</div>

		<div style="margin-right:2%;display: inline-block;margin: 3px;">
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.list">

			<el-table-column fixed prop="giftId" label="编号" width="50">
			</el-table-column>
			<el-table-column fixed prop="sendUserName" label="赠送用户">
				<template slot-scope="scope">
					<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.sendUserId)">
						{{ scope.row.sendUserName ? scope.row.sendUserName : '未绑定' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="sendPhone" label="赠送用户手机号">
			</el-table-column>
			<el-table-column fixed prop="userName" label="接收用户">
				<template slot-scope="scope">
					<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">
						{{ scope.row.userName ? scope.row.userName : '未绑定' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="接收用户手机号">
			</el-table-column>
			<el-table-column prop="giftImg" label="图片">
				<template slot-scope="scope">
					　　<img :src="scope.row.giftImg" width="60" height="60" />
				</template>
			</el-table-column>
			<el-table-column prop="giftName" label="名称">
			</el-table-column>
			<el-table-column prop="giftMoney" label="礼物金额">
			</el-table-column>
			<el-table-column prop="userMoney" label="陪陪佣金">
				<template slot-scope="scope">
					<span>{{scope.row.userMoney?scope.row.userMoney:'0'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="consortiaRate" label="会长佣金">
				<template slot-scope="scope">
					<el-button size="mini" style="color: #DD0000;background: #fff;border: none;" type="primary"  @click="updates(scope.row.consortiaUserId)">
						{{scope.row.consortiaRate ? scope.row.consortiaRate : '0'}}
					</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="pingMoney" label="平台金额">
				<template slot-scope="scope">
					<span>{{scope.row.pingMoney?scope.row.pingMoney:'0'}}</span>
			
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间">
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
			</el-pagination>
		</div>
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
				tableData: {},
				memberName: '',
				memberImg: '',
				memberId: '',
				sort: '',
				title: '添加',
				userName:'',
				phone:'',
				sendUserName:'',
				sendPhone:'',
				giftName:''
			}
		},
		methods: {
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('gift/selectGiftUser'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': this.userName,
						'phone': this.phone,
						'sendUserName': this.sendUserName,
						'sendPhone': this.sendPhone,
						'giftName':this.giftName
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

			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.phone = ''
				this.userName = ''
				this.sendUserName = ''
				this.sendPhone = ''
				this.giftName = ''
				this.dataSelect()
			},
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style>
</style>
