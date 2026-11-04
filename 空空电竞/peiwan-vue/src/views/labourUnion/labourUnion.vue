<template>
	<div>
		<!-- <el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="技能列表" name="third"> -->

		<div style="position: relative;display: inline-block;">
			<span>会长名称：</span>
			<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入会长名称"
				v-model="userNames">
			</el-input>&nbsp;&nbsp;
		</div>
		<div style="position: relative;display: inline-block;">
			<span>公会名称：</span>
			<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入公会名称"
				v-model="consortiaNames">
			</el-input>&nbsp;&nbsp;
		</div>
		<div style="position: relative;display: inline-block;">
			<span>电话：</span>
			<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入电话" v-model="search">
			</el-input>&nbsp;&nbsp;
		</div>
		<div style="display: inline-block;">
			<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="phoneSelect">查询
			</el-button>
			<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleans2">重置
			</el-button>&nbsp;&nbsp;
			<el-button style="margin: 10px 0;" :disabled="!isAuth('labourUnion:add')" size="mini" type="primary"
				icon="document" @click="choiaddNotice()">添加公会</el-button>&nbsp;&nbsp;&nbsp;&nbsp;

		</div>
		<el-table v-loading="tableDataLoading" :data="choicenData.records">

			<el-table-column prop="consortiaId" label="ID" width="80">
			</el-table-column>
			<el-table-column prop="consortiaNo" label="公会编号" width="100" fixed="left">
			</el-table-column>
			<el-table-column prop="userName" label="会长名称" width="150" fixed="left">
				<template slot-scope="scope">
					<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
						 @click="updatesvideo(scope.row)">
						{{scope.row.userName ? scope.row.userName : '未绑定'}}
					</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="电话" width="150">
			</el-table-column>
			<el-table-column prop="consortiaName" label="公会名称" width="150" fixed="left">
			</el-table-column>
			<el-table-column prop="homepageImg" label="主页图" width="200">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<img style="width: 50px; height: 50px" :src="scope.row.consortiaImg" alt="" slot="reference">
						<img style="width: 300px; height: auto" :src="scope.row.consortiaImg" alt="">
					</el-popover>
				</template>
			</el-table-column>
			<el-table-column prop="consortiaContent" label="公会规则" width="250">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.consortiaContent}}</div>
						<div style="width: 300px;">{{scope.row.consortiaContent}}</div>
					</el-popover>

				</template>
			</el-table-column>
			<el-table-column prop="sunMoney" label="总收益" width="100">
			</el-table-column>
			<el-table-column prop="ordersMoney" label="订单收益" width="100">
			</el-table-column>
			<el-table-column prop="giftMoney" label="礼物收益" width="100">
			</el-table-column>
			<el-table-column prop="counts" label="公会人数" width="100">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="180">
			</el-table-column>
			<el-table-column label="操作" prop="id" width="200" fixed='right' align="center">
				<template slot-scope="scope">

					<el-button size="mini" type="primary" v-if="scope.row.status != 1 " @click="plCompile(scope.row)"
						style="margin: 3px;">
						陪陪列表
					</el-button>
					<el-button size="mini" type="primary" v-if="scope.row.status != 1 " @click="syCompile(scope.row)"
						style="margin: 3px;">
						收益统计
					</el-button>
					<el-button size="mini" type="primary" v-if="scope.row.status != 1 " @click="spCompile(scope.row)"
						style="margin: 3px;">
						申请管理
					</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('labourUnion:update')"
						@click="choiCompile(scope.$index, scope.row)" style="margin: 3px;">修改信息
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('labourUnion:delete')"
						@click="choidelete(scope.row)" style="margin: 3px;">删除
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;float:right">
			<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="choicenData.total">
			</el-pagination>
		</div>
		<!-- 添加信息弹框 -->
		<el-dialog :title="renwuTitles" customClass="customWidth" :visible.sync="dialogFormVisible5" center>


			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">公会会长：</span>
				<el-input v-model="nickName1" @focus="userselect" style="width:45%;" placeholder="请选择公会会长">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">公会名称：</span>
				<el-input v-model="consortiaName" style="width:45%;" placeholder="请输入公会名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">公会编号：</span>
				<el-input v-model="consortiaNo" style="width:45%;" placeholder="请输入公会编号" min="0"
					:disabled="consortiaId!=''" onkeyup="value=value.replace(/[^\d]/g,'')"></el-input>
			</div>


			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">主页图：</span>
				<div class="imgs" v-for="(item,index) in homepageImg" :key="index">
					<img width="100%" class="images" height="100%" :src="item" alt="">
					<span class="dels">
						<i class="el-icon-delete" @click="dels(index)"></i>
					</span>
				</div>
				<div class="imgs" style="width: 50%;">
					<el-upload :action="$http.adornUrl('alioss/upload')" list-type="picture-card"
						:show-file-list="false" :on-success="handleRemove" v-if="homepageImg.length<1">
						<i class="el-icon-plus"></i>
					</el-upload>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="dialogImageUrl" alt="">
					</el-dialog>
				</div>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">公会规则：</span>
				<el-input v-model="consortiaContent" style="width:45%;" type="textarea" placeholder="请输入公会规则" min="0">
				</el-input>
			</div>

			<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
				<el-button @click="prev">取 消</el-button>
				<el-button type="primary" @click="addmissionNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center>
			<div style="margin:2% 0;display: inline-block;">
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list">
				<el-table-column fixed prop="nickName" width="220" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" width="220" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" width="220" label="创建时间">
				</el-table-column>
				</el-table-column>
				<el-table-column label="操作" fixed='right' width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 技能审核弹框 -->
		<el-dialog title="技能审核" :visible.sync="dialogFormVisible8" center>
			<el-radio-group v-model="radio" style="width: 100%;margin-bottom: 30px;margin-left: 200px;">
				<el-radio :label="1">通过</el-radio>
				<el-radio :label="2">驳回</el-radio>
			</el-radio-group>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核理由：</span>
				<el-input style="width:50%;" v-model="contents" type="text" placeholder="请输入审核理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible8 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 陪陪列表 -->
		<el-dialog title="陪陪列表" :visible.sync="dialogFormVisible10" center width="80%">
			<el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document"
				@click="transferClcikTk(3)" :disabled="checkBoxData.length <= 0 || !isAuth('labourUnion:updateYj')">
				批量修改陪玩官佣金
			</el-button>
			<el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document"
				@click="transferClcikTk(1)" :disabled="checkBoxData.length <= 0 || !isAuth('labourUnion:updateYj')">
				批量修改一级佣金
			</el-button>
			<el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document"
				@click="transferClcikTk(2)" :disabled="checkBoxData.length <= 0 || !isAuth('labourUnion:updateYj')">
				批量修改二级佣金
			</el-button>
			<el-table v-loading="tableDataLoading1" :data="pinglunData.records" @selection-change="changeFun">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column prop="userId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
					<template slot-scope="scope">
						　
						<!-- <span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row)"
							v-text="scope.row.userName"></span> -->
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
							 @click="updatesvideo(scope.row)">
							{{scope.row.userName ? scope.row.userName : '未绑定'}}
						</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="avatar" label="用户头像">
					<template slot-scope="scope">
						　　<img v-if="scope.row.avatar && scope.row.avatar!=''" :src="scope.row.avatar" width="40"
							height="40" />
						<span v-else>暂无图片</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="电话">
				</el-table-column>
				<el-table-column prop="invitationCode" label="邀请码">
				</el-table-column>
				<el-table-column prop="createTime" label="是否陪陪">
					<template slot-scope="scope">
						<span v-if="scope.row.isAuthentication==1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="rate" label="陪玩官佣金比例" width="150">
					<template slot-scope="scope">
						<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="zhiRate" label="一级佣金比例"  width="150">
					<template slot-scope="scope">
						<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="feiRate" label="二级佣金比例"  width="150">
					<template slot-scope="scope">
						<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="isAuthentication" label="是否认证">
					<template slot-scope="scope">
						<span v-if="scope.row.isAuthentication==1">已实名</span>
						<span v-else>未实名</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" @click="xiangqing(scope.row)" style="margin: 3px;">收益详情</el-button> -->
						<el-button size="mini" type="danger" :disabled="!isAuth('labourUnion:tichu')"
							style="margin: 3px;" @click="tichudelete(scope.row)" v-if="ghhzUserId!=scope.row.userId">
							踢出公会
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size2" :current-page="page2"
					layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.total">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 申请列表 -->
		<el-dialog title="申请列表" :visible.sync="dialogFormVisible2" center>
			<div style="display: inline-block;">
				<span>申请类型：</span>
				<el-select filterable v-model="classify" style="width:150px;margin-left: 10px;"
					@change="animeDat3(classify)">
					<el-option v-for="item in classifyList" :key="item.value" :label="item.title" :value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;">
				<span>状态：</span>
				<el-select v-model="status1" style="width:150px;margin-left: 10px;" @change="animeDat3(status1)">
					<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>

			<el-table v-loading="tableDataLoading1" :data="shenqingList.list">
				<el-table-column prop="consortiaApplyId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户" width="120">
					<template slot-scope="scope">
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
							 @click="updatesvideo(scope.row)">
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
				<el-table-column prop="createTime" label="申请类型" width="180">
					<template slot-scope="scope">
						<span v-if="scope.row.classify==1">入会申请</span>
						<span v-if="scope.row.classify==2">退会申请</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态" width="180">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1">待审核</span>
						<span v-if="scope.row.status==2">同意</span>
						<span v-if="scope.row.status==3">拒绝</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('labourUnion:shenhe')"
							@click="shenhe(scope.row)" v-if="scope.row.status==1">审核
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeSp" @current-change="handleCurrentChangeSp"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size2" :current-page="page2"
					layout="total,sizes, prev, pager, next,jumper" :total="shenqingList.totalCount">
				</el-pagination>
			</div>
		</el-dialog>

		<!-- 收益详情列表 -->
		<el-dialog title="收益详情" :visible.sync="dialogFormVisible4" center>
			<div>
				<span>申请类型：</span>
				<el-select filterable v-model="classifyLw" style="width:150px;margin-left: 10px;"
					@change="animeDatSy(classifyLw)">
					<el-option v-for="item in classifyLwList" :key="item.value" :label="item.title" :value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>

			<el-table v-loading="tableDataLoading2" :data="shouyiList.records">
				<el-table-column prop="consortiaApplyId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户" width="120">
					<template slot-scope="scope">
						<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary"
							 @click="updatesvideo(scope.row)">
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
				<el-table-column prop="createTime" label="申请类型" width="180">
					<template slot-scope="scope">
						<span v-if="scope.row.classify==1">入会申请</span>
						<span v-if="scope.row.classify==2">退会申请</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态" width="180">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1">待审核</span>
						<span v-if="scope.row.status==2">同意</span>
						<span v-if="scope.row.status==3">拒绝</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('labourUnion:shenhe')"
							@click="shenhe(scope.row)">审核
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeSy" @current-change="handleCurrentChangeSy"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size3" :current-page="page3"
					layout="total,sizes, prev, pager, next,jumper" :total="shouyiList.total">
				</el-pagination>
			</div>
		</el-dialog>
		
		<!-- 修改佣金比例 弹框 -->
		<el-dialog title="修改佣金比例" :visible.sync="dialogFormVisibleYjbl" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">{{types==1?'一级':types==2?'二级':'陪玩官'}}佣金比例：</span>
				<el-input style="width:50%;" v-model="contents" type="text" placeholder="请输入佣金比例"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleYjbl = false">取 消</el-button>
				<el-button type="primary" @click="transferClcik()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- </el-tabs> -->
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../locality/quill-config.js'
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				dialogWidth: 0,
				size: 10,
				page: 1,
				size1: 10,
				page1: 1,
				size2: 10,
				page2: 1,
				size3: 10,
				page3: 1,
				state: '',
				limit: 10,
				classifyList: [{
						id: 1,
						title: '入会申请'
					},
					{
						id: 2,
						title: '退会申请'
					}
				],
				classifyLw: 2,
				classifyLwList: [{
						id: 1,
						title: '订单收益'
					},
					{
						id: 2,
						title: '礼物收益'
					}
				],
				title: '',
				type: '',
				activeName: 'third',
				tableDataLoading: true,
				tableDataLoading1: false,
				tableDataLoading2: false,
				tableDataLoading4: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible8: false,
				dialogFormVisible10: false,
				choicenData: {},
				pinglunData: {},
				shenqingList: {},
				id: '',
				userId: '',
				search: '',
				statusId: '',
				// 发布信息
				consortiaNames: '',
				consortiaName: '',
				consortiaId: '',
				userIdss: '', //用户id
				consortiaNo: '',
				homepageImg: [], //封面图
				consortiaContent: '', //介绍
				percentage: 0, //进度条
				consortiaIds: '',
				classify: 1,
				userNames: '',

				status: '', //状态0待审核  1上架  2下架 3驳回 (修改添加不传)
				phone: '', //电话
				phone1: '',

				nickName: '',
				nickName1: '',
				nickName2: '',
				userIds: {}, //用户列表
				bq: '', //输入的标签名
				missions: 0,
				shenheId: 0, //审核id
				contents: '', //驳回理由
				dialogVisible: false,
				radio: 0,
				campusName: '',
				tableData: {},
				dialogImageUrl: [],
				shouyiList: {},
				renwuTitles: '添加公会',
				status1: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '待审核'
					},
					{
						value: 2,
						label: '同意'
					},
					{
						value: 3,
						label: '拒绝'
					}
				],
				ghhzUserId: '',
				checkBoxData: [], //多选框选择的值
				dialogFormVisibleYjbl:false,
				types:1,

			}
		},
		methods: {

			handleSizeChange(val) {
				this.limit = val
				this.classSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.classSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleSizeChange3(val) {
				this.size2 = val
				this.pinglunSelect()
			},
			handleCurrentChange3(val) {
				this.page2 = val
				this.pinglunSelect()
			},
			handleSizeChangeSp(val) {
				this.size2 = val
				this.shengqingSelect(this.consortiaIds)
			},
			handleCurrentChangeSp(val) {
				this.page2 = val
				this.shengqingSelect(this.consortiaIds)
			},
			handleSizeChangeSy(val) {
				this.size3 = val
				this.shouyiSelect(this.consortiaIds)
			},
			handleCurrentChangeSy(val) {
				this.page3 = val
				this.shouyiSelect(this.consortiaIds)
			},
			handleClick(tab, event) {
				this.page = 1
				this.InformationSelect()
			},

			//添加技能
			choiaddNotice() {
				this.renwuTitles = '添加公会'
				this.dialogFormVisible5 = true
				this.consortiaId = ''
				this.consortiaName = ''

				this.consortiaContent = ''
				this.homepageImg = []
				this.consortiaNo = ''
				this.userIdss = ''
				this.nickName1 = ''

			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.userNames = ''
				this.consortiaNames = ''
				this.search = ''
				this.page = 1
				this.InformationSelect()
			},

			// 信息数据
			InformationSelect() {
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl(`consortia/selectConsortiaList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'consortiaName': this.consortiaNames,
						'userName': this.userNames,
						'phone': this.search
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false

					let returnData = data.data
					this.choicenData = returnData



				})
			},
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.homepageImg = file.data;
			},
			onprogress(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage = parseInt(event.percent)
			},
			// 封面图片上传
			handleAvatarSuccess1(file) {
				this.gameImg = file.data;
			},

			// 删除
			clear(index) {
				this.songList.splice(index, 1);
			},

			// 获取用户列表弹框
			userselect() {
				this.page1 = 1
				this.dialogFormVisible3 = true
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				let phone = -1
				if (this.phone) {
					phone = this.phone
				}
				let nickName = -1
				if (this.nickName) {
					nickName = this.nickName
				}
				// user/selectUserList?page=1&limit=10&member=-1&userName=&inviterCode=&startTime=&endTime=&isAuthentication=-1&phone=
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'userName': this.nickName2,
						'phone': this.phone1,
						'member':-1,
						'inviterCode':'',
						'isAuthentication':'',
						'endTime':'',
						'startTime':'',
						
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.tableDataLoading4 = true

				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				this.phone = row.phone
				// if (this.nickName == '' || this.nickName == null) {
				// 	this.nickName = row.phone
				// }
				this.dialogFormVisible3 = false
			},
			// 添加标签
			btnTj() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签名',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.labels.push(this.bq)
					this.bq = ''
				}

			},
			// 删除标签
			dels1(index) {
				this.labels.splice(index, 1);
				console.log(this.labels)
			},
			// 确定添加
			addmissionNoticeTo() {
				if (this.userIdss == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择发布人',
						type: 'warning'
					});
					return
				}
				if (this.consortiaName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入公会名称',
						type: 'warning'
					});
					return
				}
				if (this.consortiaNo == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入公会编号',
						type: 'warning'
					});
					return
				}
				console.log('this.consortiaNo.length', this.consortiaNo.length)
				if (this.consortiaNo.length != 4) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入4位数的公会编号',
						type: 'warning'
					});
					return
				}

				if (this.homepageImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传公会主页图',
						type: 'warning'
					});
					return
				}
				if (this.consortiaContent == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入公会规则',
						type: 'warning'
					});
					return
				}

				let urls = ''
				if (this.renwuTitles == '添加公会') {
					urls = 'consortia/insertConsortia'
				} else {
					urls = 'consortia/updateConsortia'
				}
				let that = this

				setTimeout(function() {
					that.$http({
						url: that.$http.adornUrl(urls),
						method: 'post',
						data: that.$http.adornData({
							// params: that.$http.adornParams({
							'consortiaId': that.consortiaId,
							'consortiaName': that.consortiaName,
							'consortiaImg': that.homepageImg.toString(),
							'userId': that.userIdss,
							'consortiaNo': that.consortiaNo,
							'consortiaContent': that.consortiaContent
						})
					}).then(({
						data
					}) => {
						console.log('提交结果', data)
						if (data.code == 0) {
							that.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									that.dialogFormVisible5 = false
									that.InformationSelect()
								}
							})
						} else {
							that.$message({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'warning'
							});
						}


					})
				}, 1000)
			},
			// 关闭添加信息弹框
			prev() {
				this.dialogFormVisible5 = false
				this.consortiaId = ''
				this.consortiaName = ''
				this.orderTakingArea = ''
				this.homepageImg = []
				this.userIdss = ''
				this.nickName1 = ''
				this.nickName2 = ''
				this.phone = ''
				this.consortiaContent = ''
				this.consortiaNo = ''
			},
			// 审核
			shenhe(row) {
				this.shenheId = row.consortiaApplyId
				this.dialogFormVisible8 = true
			},
			// 提交审核
			refuseto(row) {
				if (this.radio == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					})
					return
				}
				if (this.radio == 2) {
					if (this.contents == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入驳回理由',
							type: 'warning'
						})
						return
					}

				}
				let ids = this.shenheId
				let status = this.radio
				let content = this.contents
				this.$http({
					url: this.$http.adornUrl('consortia/auditConsortiaApply'),
					method: 'post',
					params: this.$http.adornParams({
						'ids': ids,
						'status': status,
						'content': content,
						// 'classify':this.classify
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible8 = false
					this.$message({
						message: '审核完成',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.radio = 0
							this.contents = ''
							this.shengqingSelect(this.consortiaIds)
						}
					})
				})
			},
			// 修改信息弹框
			choiCompile(index, row) {
				console.log(index, row)
				this.missions = 1
				this.renwuTitles = '修改公会'
				if (row) {
					this.consortiaId = row.consortiaId
					this.consortiaName = row.consortiaName

					this.homepageImg = row.consortiaImg.split(',')

					this.consortiaNo = row.consortiaNo
					this.nickName1 = row.userName
					this.userIdss = row.userId
					this.consortiaContent = row.consortiaContent

				}

				this.dialogFormVisible5 = true
			},

			// 查看陪陪
			plCompile(row) {
				this.ghhzUserId = row.userId
				this.consortiaIds = row.consortiaId
				this.dialogFormVisible10 = true
				this.pinglunSelect(row.consortiaId)
			},

			// 删除信息
			choidelete(row) {
				let delid = row.consortiaId
				this.$confirm(`确定删除此公会?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('consortia/deleteConsortia'),
						method: 'post',
						params: this.$http.adornParams({
							'consortiaId': delid
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.InformationSelect()
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


			// 详情跳转
			updatesvideo(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},

			// 陪陪列表
			pinglunSelect(id) {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('consortia/selectConsortiaPersonMoneyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'consortiaId': this.consortiaIds,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.pinglunData = returnData


				})
			},
			// 提出公会
			tichudelete(row) {
				let delid = row.userId
				this.$confirm(`确定踢出公会?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('consortia/outConsortiaPerson'),
						method: 'post',
						params: this.$http.adornParams({
							'userId': row.userId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.pinglunSelect()
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
			// 轮播图片上传
			handleRemove(file) {
				console.log(file, this.homepageImg)
				this.homepageImg.push(file.data);
			},
			handleDownload(file) {

				console.log(file, this.homepageImg)
			},
			// 删除轮播图
			dels(index) {
				this.homepageImg.splice(index, 1);
				console.log(this.homepageImg)
			},
			// 标签图片上传
			handleRemove1(file) {
				console.log(file, this.isWangZhe)
				this.isWangZhe.push(file.data);
			},
			handleDownload1(file) {

				console.log(file, this.isWangZhe)
			},
			// 删除轮播图
			dels1(index) {
				this.isWangZhe.splice(index, 1);
				console.log(this.isWangZhe)
			},
			// 申请管理
			spCompile(row) {
				this.classify = 1
				this.status1 = ''
				this.consortiaIds = row.consortiaId
				this.shengqingSelect(row.consortiaId)
				this.dialogFormVisible2 = true
			},
			animeDat3() {

				this.page2 = 1
				this.shengqingSelect(this.consortiaIds)
			},
			shengqingSelect(consortiaId) {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('consortia/selectConsortiaApplyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page2,
						'limit': this.size2,
						'status': this.status1,
						'consortiaId': consortiaId,
						'classify': this.classify
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.shenqingList = returnData
				})
			},
			// 收益统计
			syCompile(row) {
				this.$router.push({
					path: '/profitList',
					query: {
						consortiaId: row.consortiaId
					}
				})
			},
			// // 用户收益详情
			// xiangqing(row){
			// 	console.log('row',row)
			// 	this.userId = row.userId
			// 	this.consortiaIds = row.consortiaId
			// 	this.shouyiSelect(row.consortiaId)
			// 	this.dialogFormVisible4 = true
			// },
			// // 收益详情
			// shouyiSelect(consortiaId){
			// 	this.tableDataLoading2 = true
			// 	this.$http({
			// 		url: this.$http.adornUrl('consortia/selectConsortiaPersonMoneyDetails'),
			// 		method: 'get',
			// 		params: this.$http.adornParams({
			// 			'page': this.page3,
			// 			'limit': this.size3,
			// 			'userId': this.userId,
			// 			'consortiaId': consortiaId,
			// 			'classify':this.classifyLw
			// 		})
			// 	}).then(({
			// 		data
			// 	}) => {
			// 		this.tableDataLoading2 = false
			// 		let returnData = data.data
			// 		this.shouyiList = returnData
			// 	})
			// },
			// animeDatSy(){
			// 	this.page3 = 1
			// 	this.shouyiSelect(this.consortiaIds)
			// }
			// 批量选择
			changeFun(val) {
				this.checkBoxData = val
			},
			// 修改弹框
			transferClcikTk(id) {
				this.types = id
				this.contents = ''
				this.dialogFormVisibleYjbl = true
			},
			// 批量操作
			transferClcik(id) {
				
			if (this.contents === '') {
				this.$notify({
					title: '提示',
					duration: 1800,
					message: '佣金比例不能为空',
					type: 'warning'
				})
				return
			}
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.userId
				})
				var flag=true;
				for (var i in ids) {
					if(this.types==1){
						var datas = {
							zhiRate:this.contents,
							userId:ids[i]
						}
					}
					if(this.types==2){
						var datas = {
							feiRate:this.contents,
							userId:ids[i]
						}
					}
					if(this.types==3){
						var datas = {
							rate:this.contents,
							userId:ids[i]
						}
					}
					
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData(datas)
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							if(i==(ids.length-1) && flag){
								this.dialogFormVisibleYjbl = false
								this.$notify({
									title: '提示',
									duration: 1800,
									message: '操作成功',
									type: 'success'
								})
								this.pinglunSelect()
								flag=false
							}
						}else{
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'error'
							})
							return
						}
					})
				}
				
			},
		},
		mounted() {
			this.InformationSelect()
		}
	}
</script>

<style>
	.customWidth {
		width: 80% !important;
	}

	.adver_main.box {
		display: block;
		max-width: 100%;
		text-align: center;
		border: 1px dotted rgba(67, 79, 103, .4);
	}

	.cards {
		padding: 0 8px;
		margin-bottom: 15px;
	}

	.adver_main.box a {
		display: flex;
		justify-content: center;
		height: 150px;
		line-height: 150px;
		text-decoration: none
	}

	.bannerManin {
		border: 1px solid #e8e8e8;
		font-size: 14px;
		padding: 0 24px;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 113px;
		color: rgba(0, 0, 0, .65);
	}

	.bannerManin span {
		display: inline-block;
		margin-left: 5px;
	}

	.bannerManin img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
	}

	.bannerbtn {
		display: flex;
		border-top: none !important;
		border: 1px solid #e8e8e8;
		padding: 11px;
		font-size: 14px;
		color: #3E8EF7;
	}

	.bannerbtn a {
		flex: 1;
		text-align: center;
		color: #3E8EF7 !important;
		text-decoration: none;
	}

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

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}
</style>
