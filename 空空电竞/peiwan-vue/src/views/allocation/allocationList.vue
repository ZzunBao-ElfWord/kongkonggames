<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="全局系统配置" name="first">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<!-- <el-tab-pane label="首页配置" name="thirdly">
				<el-table
					v-loading="tableDataLoading"
					:data="tableData">
					<el-table-column
					  fixed
						prop="type"
					  label="编号"
					  align="center"
					  width="80">
					</el-table-column>
					<el-table-column
					  prop="min"
					  label="类型">
					</el-table-column>
					<el-table-column
					  prop="value"
					  label="是否启用"
					  width="500">
					</el-table-column>
					<el-table-column
						prop="createAt"
						label="创建时间">
					</el-table-column>
					<el-table-column
						label="操作"
						prop="id"
						width="120">
						<template slot-scope="scope">
							<el-button
							  size="mini"
							  type="primary"
								:disabled="!isAuth('allocationList:update')"
							  @click="amendWhether(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				 修改弹框
				<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
					<el-form :model="form2">
					  <el-form-item label="配置类型：" :label-width="formLabelWidth" >
						<el-input v-model="form2.min" style="width:65%;" readonly ></el-input>
					  </el-form-item>
					  <el-form-item label="是否启用：" :label-width="formLabelWidth" >
						<el-select v-model="form2.value" placeholder="请选择类型" style="width:65%;">
						  <el-option v-for="item in values" :key="item.value" :label="item.label" :value="item.label">
						  </el-option>
						</el-select>
					  </el-form-item>
					</el-form>
				  <div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="WhetherNoticeTo()">确 定</el-button>
				  </div>
				</el-dialog>
			</el-tab-pane> -->
			<el-tab-pane label="微信配置" name="fifth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="支付宝配置" name="sixth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="短信配置" name="seventh">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="新人优惠券配置" name="youhuiquan">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								<div>
									<div v-if="scope.row.couponList">
										<span v-for="(item,index) in scope.row.couponList">
											<span v-if="item">{{item.couponName}};</span>
											<span v-else> - </span>
										</span>
									</div>
									
								</div>
								<!-- <div v-else>
									<span>{{scope.row.value}}</span>
								</div> -->
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="协议配置" name="xieyi">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="APP消息推送配置" name="app">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="文件上传配置" name="wenjian">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="佣金配置" name="yongjin">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="服务费配置" name="fuwufei">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="开关配置" name="kaiguan">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="图片配置" name="image">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
		</el-tabs>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="配置类型：" :label-width="formLabelWidth">
					<el-input v-model="form.min" style="width:65%;" readonly></el-input>
				</el-form-item>
				<el-form-item label="内容：" :label-width="formLabelWidth">
					<quill-editor v-if="form.conditionFrom=='xieyi' " ref="myTextEditor" v-model="form.value"
						:options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
					<div v-else-if="form.conditionFrom=='coupon'">
						<div
							style="height: 150px;width: 72%;border: 1px solid #efefef;border-radius: 6px;padding: 10px;">
							<span v-for="(item,index) in couponList" :key="index"
								style="border: 1px;margin-right: 5px;">{{item.couponName}} <i
									class="el-icon-circle-close" @click="del(index,item)"></i> ;</span>
					
						</div>
						<el-button type="primary" @click="yhqbtn()" style="margin: 5px;">选择优惠券</el-button>
					</div>
					<el-upload class="avatar-uploader" v-else-if="form.conditionFrom=='image'" v-model="form.value"
						:action="$http.adornUrl('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="form.value" :src="form.value" class="avatar"
							style="border-radius: 6px;width:148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
					<div v-else-if="form.conditionFrom=='kaiguan'">
						<div>
							<el-radio-group v-model="form.value">
								<el-radio label="是">是</el-radio>
								<el-radio label="否">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<el-input v-else v-model="form.value" style="width:65%;"></el-input>
					<!-- <el-input v-model="form.value" style="width:65%;"></el-input> -->
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="优惠券列表" :visible.sync="dialogFormVisibleYhq" center>
			<div style="display: inline-block;float: right;">
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh">刷新
				</el-button>
			</div>
			<el-table v-loading="tableDataLoadingY" :data="tableDataYhq.content">
				<el-table-column prop="couponId" label="编号" width="80">
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
				<el-table-column label="操作" width="150" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="discuss(scope.$index,scope.row)">确认选择
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableDataYhq.totalElements">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../mission/quill-config.js'
	export default {
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				openValue: '是',
				closeValue: '否',
				limit: 10,
				page: 0,
				min: '',
				value: '',
				id: '',
				condition: 'xitong',
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				form: {
					id: '',
					min: '',
					value: '',
					type: '',
					couponList:[],
				},
				form2: {
					id: '',
					min: '',
					value: '',
					type: '',
				},
				values: [{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					}
				],
				tableData: [],
				checkBoxData: [], //多选框选择的值
				quillOption: quillConfig,
				dialogFormVisibleYhq: false,
				tableDataYhq:{},
				tableDataLoadingY:false,
				tableDataLoading5:false,
				couponList:[],
				
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.yhqSelect();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.yhqSelect();
			},
			handleClick(tab, event) {
				if (tab._props.label == '全局系统配置') {
					this.condition = 'xitong'
					this.dataSelect()
				}
				if (tab._props.label == '首页配置') {
					this.condition = 'shouye'
					this.dataSelect()
				}
				if (tab._props.label == '微信配置') {
					this.condition = 'weixin'
					this.dataSelect()
				}
				if (tab._props.label == '支付宝配置') {
					this.condition = 'zhifubao'
					this.dataSelect()
				}
				if (tab._props.label == '短信配置') {
					this.condition = 'duanxin'
					this.dataSelect()
				}
				if (tab._props.label == '任务系统配置') {
					this.condition = 'renwu'
					this.dataSelect()
				}
				if (tab._props.label == '协议配置') {
					this.condition = 'xieyi'
					this.dataSelect()
				}
				if (tab._props.label == 'APP消息推送配置') {
					this.condition = 'push'
					this.dataSelect()
				}
				if (tab._props.label == '文件上传配置') {
					this.condition = 'oss'
					this.dataSelect()
				}
				if (tab._props.label == '佣金配置') {
					this.condition = 'yongjin'
					this.dataSelect()
				}
				if (tab._props.label == '服务费配置') {
					this.condition = 'fuwufei'
					this.dataSelect()
				}
				if (tab._props.label == '开关配置') {
					this.condition = 'kaiguan'
					this.dataSelect()
				}
				if (tab._props.label == '图片配置') {
					this.condition = 'image'
					this.dataSelect()
				}
				if (tab._props.label == '新人优惠券配置') {
					this.condition = 'coupon'
					this.dataSelect()
				}
				

			},
			// 修改弹框
			amend(index, rows) {
				
				this.form.id = rows.id;
				this.form.type = rows.type
				this.form.min = rows.min;
				this.form.value = rows.value;
				this.form.max = rows.max;
				this.form.createAt = rows.createAt;
				this.form.conditionFrom = rows.conditionFrom
				if (rows.couponList) {
					if(rows.couponList.length>0){
						var rowsValue=JSON.parse(JSON.stringify(rows.couponList));
							this.couponList = rowsValue
						
					}else{
						this.couponList = []
					}
				}else{
					this.couponList = []
					
				}
				this.dialogFormVisible = true;
			},
			// 修改
			amendNoticeTo() {
				if (this.form.conditionFrom=='coupon') {
					this.values = []
					this.form.couponList = this.couponList
					for (var i in this.form.couponList) {
						this.values.push(this.form.couponList[i].couponId)
					}
					this.form.value = this.values.toString()
				}
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'type': this.form.type,
						'value': this.form.value,
						'min': this.form.min,
						'max': this.form.max,
						'createAt': this.form.createAt,
						'conditionFrom': this.form.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 修改首页
			amendWhether(index, rows) {
				this.dialogFormVisible1 = true;
				this.form2.id = rows.id;
				this.form2.type = rows.type
				this.form2.min = rows.min;
				this.form2.value = rows.value;
				this.form2.max = rows.max;
				this.form2.createAt = rows.createAt;
				this.form2.conditionFrom = rows.conditionFrom
			},
			WhetherNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form2.id,
						'type': this.form2.type,
						'value': this.form2.value,
						'min': this.form2.min,
						'max': this.form2.max,
						'createAt': this.form2.createAt,
						'conditionFrom': this.form2.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`common/type/condition/${this.condition}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
			handleAvatarSuccess1(file) {
				this.form.value = file.data;
			},
			// 点击获取优惠券
			yhqbtn() {
				console.log('this.couponList', this.form.couponList)
			
				this.page = 1
				this.yhqSelect()
				this.dialogFormVisibleYhq = true
			},
			// 优惠券列表数据
			yhqSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCoupon/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page-1,
						'size': this.limit
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableDataYhq = returnData
					}
				})
			},
			refresh() {
				this.page1 = 1
				this.yhqSelect()
			},
			// 确认选择
			discuss(index, row) {
				// this.values.push(row.couponId)
				this.couponList.push(row)
				this.dialogFormVisibleYhq = false
				
			},
			del(index, row) {
				console.log('-------------',index)
				// this.values.splice(index,1)
				this.couponList.splice(index,1)
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped="scoped">
	.eit {
		height: 120px;
	}
</style>