<template>
	<div>
		<div style="display: inline-block;float: right;">
			<el-button style='margin-left:15px;' :disabled="!isAuth('coupon:add')" size="mini" type="primary"
				icon="document" @click="addCoupon">添加</el-button>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh" plain>刷新
			</el-button>
			<el-button style='margin-left:15px;' :disabled="!isAuth('coupon:add')" size="mini" type="primary"
				icon="document" @click="songCoupon">赠送优惠券</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.content">
			<el-table-column prop="couponId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="couponName" label="满减券名称" width="150">
			</el-table-column>
			<!-- <el-table-column
			   prop="type"
			   label="满减券类型"
					width="100">
			   <template slot-scope="scope">
					<span  v-if="scope.row.type==1">通用券</span>
			   		<span style="color: #3E8EF7;" v-if="scope.row.type==2">商品券</span>
			   </template>
			</el-table-column> -->
			<el-table-column prop="lessMoney" label="满减券面值">
			</el-table-column>
			<el-table-column prop="minMoney" label="满减券最低消费" width="150">
			</el-table-column>
			<el-table-column prop="validDay" label="满减券有效期限(天)">
			</el-table-column>
			<el-table-column prop="sort" label="排序">
			</el-table-column>
			<el-table-column label="状态">
				<template slot-scope="scope">
					<span style="color: #3E8EF7;" v-if="scope.row.status == 1">开启</span>
					<span v-if="scope.row.status == 2">关闭</span>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="180">
			</el-table-column>
			<el-table-column label="操作" width="120">
				<template slot-scope="scope">
					<!-- <el-button size="mini" type="primary" :disabled="!isAuth('coupon:issue')"
						@click="discuss(scope.$index,scope.row)">发布
					</el-button> -->
					<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
						@click="shopUpdates(scope.$index,scope.row)">修改
					</el-button>
					<!-- <el-button
	  		        size="mini"
	  		        type="danger"
                :disabled="!isAuth('coupon:delete')"
	  		        @click="shopdeletes(scope.row)">删除
	  		      </el-button> -->
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalElements">
			</el-pagination>
		</div>
		<!-- 添加满减券-->
		<el-dialog title="添加满减券" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">满减券名称：</span>
				<el-input style="width:50%;" v-model="couponName" placeholder="请输入满减券名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">满减券面值：</span>
				<el-input style="width:50%;" v-model="lessMoney" type="number" :min="0" placeholder="请输入满减券面值">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">满减券最低消费：</span>
				<el-input style="width:50%;" v-model="minMoney" type="number" :min="0" placeholder="请输入满减券最低消费">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">满减券有效期限(天)：</span>
				<el-input style="width:50%;" v-model="validDay" type="number" :min="1" placeholder="请输入满减券最低消费">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" :min="0" placeholder="请输入排序号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="status">
					<el-radio :label="1">开启</el-radio>
					<el-radio :label="2">关闭</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="couponNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改满减券 -->
		<el-dialog title="修改满减券" :visible.sync="dialogFormVisible3" center>
			<el-form :model="form">
				<el-form-item label="满减券名称：" :label-width="formLabelWidth">
					<el-input v-model="form.couponName" style="width:65%;" placeholder="请输入满减券名称"></el-input>
				</el-form-item>
				<el-form-item label="满减券面值：" :label-width="formLabelWidth">
					<el-input v-model="form.lessMoney" style="width:65%;" placeholder="请输入满减券面值"></el-input>
				</el-form-item>
				<el-form-item label="满减券最低消费：" :label-width="formLabelWidth">
					<el-input v-model="form.minMoney" style="width:65%;" placeholder="请输入满减券最低消费"></el-input>
				</el-form-item>

				<el-form-item label="满减券有效期限(天)：" :label-width="formLabelWidth">
					<el-input v-model="form.validDay" style="width:65%;" placeholder="请输入满减券有效期限(天)"></el-input>
				</el-form-item>
				<el-form-item label="排序：" :label-width="formLabelWidth">
					<el-input v-model="form.sort" style="width:65%;" placeholder="请输入排序号"></el-input>
				</el-form-item>
				<el-form-item label="状态：" :label-width="formLabelWidth">
					<el-radio-group v-model="form.status">
						<el-radio :label="1">开启</el-radio>
						<el-radio :label="2">关闭</el-radio>
					</el-radio-group>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 发布满减券-->
		<el-dialog title="发布满减券" :visible.sync="dialogFormVisible2" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">满减券ID：</span>
				<el-input style="width:50%;" v-model="couponId" disabled></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">满减券名称：</span>
				<el-input style="width:50%;" v-model="couponName1" disabled></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">领取开启时间：</span>
				<el-date-picker style="width:200px;" v-model="startTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择开始时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">领取结束时间：</span>
				<el-date-picker style="width:200px;" v-model="endTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择结束时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">发布数量：</span>
				<el-input style="width:50%;" v-model="issueNumber" type="number" :min="0" placeholder="请输入发布数量">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">是否限量：</span>
				<el-radio-group v-model="isLimit">
					<el-radio :label="1">限量</el-radio>
					<el-radio :label="2">不限量</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="status">
					<el-radio :label="1">开启</el-radio>
					<el-radio :label="2">关闭</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="discussNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 选择商品弹框 -->
		<el-dialog title="商品列表" customClass="customWidth" :visible.sync="dialogFormVisible1" center>
			<div>
				<div style="margin:2% 0;display: inline-block;">
					<span>商品标题：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
						v-model="title"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="shopData.content">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="coverImg" label="商品图片">
						<template slot-scope="scope">
							<img :src="scope.row.coverImg" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题" width="200">
					</el-table-column>
					<el-table-column prop="originalPrice" label="商品原价">
					</el-table-column>
					<el-table-column prop="price" label="商品价格">
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价格">
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="selectClick(scope.$index,scope.row)">选择
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="shopData.totalElements">
					</el-pagination>
				</div>
			</div>
		</el-dialog>
		<!-- 赠送优惠券-->
		<el-dialog title="赠送优惠券" :visible.sync="dialogFormVisible0" center>
			<div>
				<span style="width: 200px;display: inline-block;text-align: right;">赠送类型：</span>
				<el-radio-group v-model="types">
					<el-radio :label="1">指定用户</el-radio>
					<el-radio :label="2">所有用户</el-radio>
				</el-radio-group>
			</div>
			<div v-if="types==1" style="margin: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择用户：</span>
				<el-tag :key="tag" v-for="(tag,j) in userNameList" closable :disable-transitions="false"
					@close="couponIdClose1(tag,j)">
					{{tag}}
				</el-tag>
				<el-button class="button-new-tag" size="small" @click="couponIdBtn1(couponIds)">添加</el-button>
			</div>
			<div style="margin: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择优惠券：</span>
				<el-tag :key="tag" v-for="(tag,j) in couponIdList" closable :disable-transitions="false"
					@close="couponIdClose(tag,j)">
					{{tag}}
				</el-tag>
				<el-button class="button-new-tag" size="small" @click="couponIdBtn(couponIds)">添加</el-button>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible0 = false">取 消</el-button>
				<el-button type="primary" @click="songNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible5" center width="80%">
			<div style="display: inline-block;margin-top: 5px;">
				<span>邀请人邀请码：</span>
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入邀请人邀请码"
					v-model="inviterCode">
				</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<span>手机号：</span>
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入手机号"
					v-model="phone">
				</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<span>昵称：</span>
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入昵称"
					v-model="nickName">
				</el-input>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<span>是否是会员：</span>
				<el-select v-model="member" style="width:150px;margin-left: 10px;" @change="select1(member)">
					<el-option v-for="item in memberList" :key="item.value" :label="item.title" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select1">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans1">重置
				</el-button>
			</div>
			<el-button style="margin-left:15px;" :disabled="xzList.length <= 0 " size="mini" type="primary"
				icon="document" @click="closes1()">确定</el-button>
			<el-table v-loading="tableDataLoading1" @selection-change="changeFun1" :data="userData.list">
				<el-table-column  type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="userId" label="id" width="80">
				</el-table-column>
				<el-table-column fixed prop="userName" label="昵称" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;"
							@click="updates(scope.row)">{{ scope.row.userName ? scope.row.userName : '未设置' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
							{{ scope.row.phone ? scope.row.phone : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="邀请人邀请码">
					<template slot-scope="scope">
						<span>{{ scope.row.inviterCode ? scope.row.inviterCode : '-' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="isVip" label="是否是会员">
					<template slot-scope="scope">
						<span v-if="scope.row.isVip==1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 优惠券列表 -->
		<el-dialog title="优惠券列表" :visible.sync="dialogFormVisible4" center>
			<el-button style="margin-left:15px;" :disabled="xzList.length <= 0 " size="mini" type="primary"
				icon="document" @click="closes2()">确定</el-button>
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column  type="selection" fixed>
				</el-table-column>
				<el-table-column prop="couponName" label="优惠券名称" width="150">
				</el-table-column>
				<el-table-column prop="type" label="优惠券类型" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.type==1">通用券</span>
						<span style="color: #3E8EF7;" v-if="scope.row.type==2">商品券</span>
					</template>
				</el-table-column>
				<el-table-column prop="lessMoney" label="优惠券面值" width="100">
				</el-table-column>
				<el-table-column prop="minMoney" label="优惠券最低消费" width="150">
				</el-table-column>
				<el-table-column prop="validDay" label="优惠券有效期限" width="150">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column label="状态">
					<template slot-scope="scope">
						<span style="color: #3E8EF7;" v-if="scope.row.status == 1">开启</span>
						<span v-if="scope.row.status == 2">关闭</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="150">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				size1: 10,
				page1: 1,
				type: 1,
				title: '',
				typeId: '',
				couponName: '',
				lessMoney: 0,
				minMoney: 0,
				validDay: 1,
				sort: 0,
				status: 1,
				goodsIds: '',
				goodsImages: '',
				issueNumber: 0,
				remainNumber: '',
				isLimit: 1,
				form: {
					couponId: '',
					type: '',
					couponName: '',
					lessMoney: '',
					goodsIds: '',
					goodsImages: '',
					minMoney: '',
					validDay: '',
					sort: '',
					status: '',
					createTime: ''
				},
				coverImg: '',
				couponName1: '',
				couponId: '',
				startTime: '',
				endTime: '',
				checkBoxData: [], //多选框选择的值
				formLabelWidth: '200px',
				tableDataLoading: false,
				tableDataLoading1: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible0: false,
				dialogFormVisible5: false,
				dialogFormVisible4: false,
				tableData: [],
				shopData: [],
				couponIds: [],
				couponIdList: [],
				userIdList: [],
				userNameList: [],
				isCoupon: 1,
				userData: [],
				types: '',
				phone: '',
				nickName: '',
				member: -1,
				memberList: [{
						value: -1,
						title: '全部'
					},
					{
						value: 0,
						title: '否'
					},
					{
						value: 1,
						title: '是'
					},
				],
				inviterCode: '',
				xzList:[],
			}
		},

		methods: {
			// 通用券/商品券
			couponClick(val) {
				if (val = 1) {
					this.goodsIds = ''
					this.goodsImages = ''
					this.form.goodsIds = ''
					this.form.goodsImages = ''
				}
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.shopSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val;
				this.dataSelect1()
			},
			handleCurrentChange2(val) {
				this.page1 = val;
				this.dataSelect1()
			},
			// 添加满减券弹框
			addCoupon() {
				this.type = 1
				this.couponName = ''
				this.goodsImages = ''
				this.lessMoney = 0
				this.validDay = 1
				this.minMoney = 0
				this.sort = 0
				this.status = 1
				this.dialogFormVisible = true
			},
			// 添加满减券
			couponNoticeTo() {
				if (this.couponName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入满减券名称',
						type: 'warning'
					});
					return
				}
				// if (this.type == 2 && this.goodsImages == '') {
				//     this.$notify({
				//         title: '提示',
				//         duration: 1800,
				//         message: '请选择商品',
				//         type: 'warning'
				//     });
				//     return
				// } 
				if (Number(this.minMoney) <= Number(this.lessMoney)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '最低消费金额不能低于满减券金额',
						type: 'warning'
					});
					return
				}
				// if (this.minMoney == this.lessMoney) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '满减券金额不能高于最低消费金额',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				this.$http({
					url: this.$http.adornUrl('selfCoupon/save'),
					method: 'post',
					data: this.$http.adornData({
						'couponName': this.couponName,
						'goodsIds': this.goodsIds,
						'goodsImages': this.goodsImages,
						'lessMoney': this.lessMoney,
						'minMoney': this.minMoney,
						'sort': this.sort,
						'status': this.status,
						'type': this.type,
						'validDay': this.validDay,
					})
				}).then(({
					data
				}) => {
					if(data.status==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisible = false
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
			// 修改满减券
			shopUpdates(index, row) {
				this.dialogFormVisible3 = true
				this.form.type = row.type
				this.form.couponId = row.couponId
				this.form.couponName = row.couponName
				this.form.lessMoney = row.lessMoney
				this.form.minMoney = row.minMoney
				this.form.sort = row.sort
				this.form.status = row.status
				this.form.validDay = row.validDay
				this.form.createTime = row.createTime
				this.form.goodsIds = row.goodsIds
				this.form.goodsImages = row.goodsImages
			},
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('selfCoupon/update'),
					method: 'post',
					data: this.$http.adornData({
						'couponId': this.form.couponId,
						'couponName': this.form.couponName,
						'goodsIds': this.form.goodsIds,
						'goodsImages': this.form.goodsImages,
						'lessMoney': this.form.lessMoney,
						'minMoney': this.form.minMoney,
						'sort': this.form.sort,
						'status': this.form.status,
						'type': this.form.type,
						'validDay': this.form.validDay,
						'createTime': this.form.createTime,
					})
				}).then(({
					data
				}) => {
					if(data.status==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisible3 = false
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
			// 发布满减券弹框
			discuss(index, row) {
				this.dialogFormVisible2 = true
				this.couponId = row.couponId
				this.couponName1 = row.couponName
				this.goodsIds = row.goodsIds
				this.type = row.type
			},
			// 发布满减券
			discussNoticeTo() {
				if (this.startTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择满减券领取时间',
						type: 'warning'
					});
					return
				}
				if (this.endTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择满减券结束时间',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('selfCouponIssue/save'),
					method: 'post',
					data: this.$http.adornData({
						'couponName': this.couponName1,
						'couponId': this.couponId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'issueNumber': this.issueNumber,
						'remainNumber': this.issueNumber,
						'isLimit': this.isLimit,
						'status': this.status,
						'type': this.type,
						'goodsIds': this.goodsIds
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.startTime = ''
								this.endTime = ''
								this.isLimit = 1
								this.status = 1
								this.issueNumber = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible2 = false
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
			// 删除满减券
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('selfCoupon/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.couponId
						})
					}).then(({
						data
					}) => {
						if (data && data.status == 0) {
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
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}
					})
				}).catch(() => {})
			},
			//获取满减券数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCoupon/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			//搜索
			select() {
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.shopSelect()
			},
			// 删除
			clear(index) {
				this.imgs.splice(index, 1);
				this.ids.splice(index, 1)
			},
			// 选择商品
			shopClick() {
				this.dialogFormVisible1 = true
				this.shopSelect()
			},
			// 获取商品数据
			shopSelect() {
				let page = this.page - 1
				let status = 0
				this.title = ''
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('goods/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'title': this.title,
						'type': this.typeId,
						'status': status
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.shopData = returnData
				})
			},
			// 选择商品
			selectClick(index, row) {
				console.log(row)
				this.$http({
					url: this.$http.adornUrl('goods/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id': row.id
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible1 = false
					let returnData = data.data;
					this.goodsIds = returnData.id
					this.goodsImages = returnData.coverImg
					this.form.goodsIds = returnData.id
					this.form.goodsImages = returnData.coverImg
				})
			},
			// 赠送优惠券
			songCoupon() {
				this.userIdList = []
				this.couponIds = []
				this.userNameList = []
				this.couponIdList = []
				this.types = ''
				this.dialogFormVisible0 = true
			},
			// 赠送优惠券
			songNoticeTo() {

				if (this.types == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择赠送类型',
						type: 'warning'
					});
					return
				}
				if (this.types == 1) {
					if (this.userIdList.length == 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择用户',
							type: 'warning'
						});
						return
					}
				}

				if (this.couponIds.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择卡券',
						type: 'warning'
					});
					return
				}
				var userIdList = this.userIdList.toString()
				var couponIds = this.couponIds.toString()
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/sendUserCoupon'),
					method: 'post',
					params: this.$http.adornParams({
						'userIds': userIdList,
						'couponIds': couponIds,
						'type': this.types
					})
				}).then(({
					data
				}) => {
					if (data.code == 0 || data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.userIdList = []
								this.couponIds = []
								this.userNameList = []
								this.couponIdList = []
								this.types = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible0 = false
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

			couponIdClose2(index, ietm) {
				console.log('index', index, 'ietm', ietm, this.couponIdList.length)
				if (this.couponIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				} else {
					this.couponIdList.splice(index, 1)
					this.couponIds.splice(index, 1)
				}
			},
			// 获取用户数据列表
			dataSelect1() {
				// let phone = -1
				// if (this.phone) {
				// 	phone = this.phone
				// }
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'member': this.member,
						'userName': this.nickName,
						'inviterCode': this.inviterCode,
						'startTime': '',
						'endTime': '',
						'isAuthentication': -1,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			select1() {
				this.page = 1
				this.dataSelect1()
			},
			// 重置
			cleans1() {
				this.page = 1
				this.phone = ''
				this.member = -1
				this.nickName = ''
				this.inviterCode = ''
				this.dataSelect1()
			},
			// 确定选中
			queding(row) {
				this.userName = row.userName
				this.userId = row.userId
				this.dialogFormVisible2 = false
			},
			// 打开用户列表
			couponIdBtn1(rows) {
				this.dataSelect1()

				this.dialogFormVisible5 = true
			},
			// 打开优惠券列表
			couponIdBtn(rows) {
				this.dataSelect()
				this.dialogFormVisible4 = true
			},
			// 删除优惠券
			couponIdClose(ietm,index) {
				console.log('index', index, 'ietm', ietm, this.couponIdList.length)
				if (this.couponIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				} else {
					this.couponIdList.splice(index, 1)
					this.couponIds.splice(index, 1)
				}
			},
			// 删除用户
			couponIdClose1(ietm,index) {
				console.log('index', index, 'ietm', ietm, this.userIdList.length)
				if (this.userIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一个用户',
						type: 'warning'
					});
					return
				} else {
					this.userIdList.splice(index, 1)
					this.userNameList.splice(index, 1)
				}
			},
			couponIdClose2(ietm,index) {
				console.log('index', index, 'ietm', ietm, this.couponIdList.length)
				if (this.couponIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				} else {
					this.couponIdList.splice(index, 1)
					this.couponIds.splice(index, 1)
				}
			},
			// 多选用户
			changeFun1(val) {
				console.log('多选', val)
				this.xzList = val
			},
			// 批量开启
			closes1(id) {
				this.isCoupon = 1
				// var userIdList = []
				// var userNameList = []
				for (var i in this.xzList) {
					this.userIdList.push(this.xzList[i].userId)
					this.userNameList.push(this.xzList[i].userName)
				}
				// this.userIdList = userIdList
				// this.userNameList = userNameList
				this.dialogFormVisible5 = false
			},
			// 多选券
			changeFun(val) {
				console.log('多选', val)
				this.xzList = val
			
			},
			// 批量开启
			closes2(id) {
				this.isCoupon = 1
				// var couponIds = []
				// var couponIdList = []
				for (var i in this.xzList) {
					this.couponIds.push(this.xzList[i].couponId)
					this.couponIdList.push(this.xzList[i].couponName)
				}
				// this.couponIds = couponIds
				// this.couponIdList = couponIdList
				this.dialogFormVisible4 = false
			
			},
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>

</style>
