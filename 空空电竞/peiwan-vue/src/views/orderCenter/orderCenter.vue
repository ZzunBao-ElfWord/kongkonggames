<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="陪玩订单" name="first">
			<div style="display: inline-block;">
				<span>订单类型：</span>
				<el-select v-model="type" style="width:150px;margin-left: 10px;" @change="animeDat(campus)">
					<el-option v-for="item in typeNum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<span>状态：</span>
				<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="animeDat(status)">
					<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				<div style="position: relative;display: inline-block;">
					<span>订单用户：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单用户"
						v-model="ordersUserName"></el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;">
					<span>接单陪玩用户：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入接单陪玩用户"
						v-model="userName"></el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;">
					<span>订单编号：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单编号"
						v-model="ordersNoT"></el-input>&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="datetime"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="exportBtn">导出
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" plain icon="document" @click="orderAdd">新增订单
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="ordersId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="ordersUserName" label="订单用户">
					<template slot-scope="scope">
						<span style="color: #008000;cursor: pointer;"
							@click="updates(scope.row.ordersUserId)">{{ scope.row.ordersUserName }}</span>
					</template>
				</el-table-column>
				<el-table-column label="订单用户图像">
					<template slot-scope="scope">
						<img v-if="scope.row.ordersUserAvatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
							height="40">
						<img v-else :src="scope.row.ordersUserAvatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="接单陪玩用户">
					<template slot-scope="scope">
						<span style="color: #008000;cursor: pointer;"
							@click="updates(scope.row.userId)">{{ scope.row.userName }}</span>
					</template>
				</el-table-column>
				<el-table-column label="接单用户图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
							height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="ordersNo" label="订单编号">
				</el-table-column>
				<el-table-column prop="unit" label="单位"></el-table-column>
				<el-table-column prop="money" label="单价"></el-table-column>
				<el-table-column prop="orderNumber" label="数量"></el-table-column>
				<el-table-column prop="couponMoney" label="优惠券金额">
					<template slot-scope="scope" >
						<span style="color: #DD0000;">{{scope.row.couponMoney?scope.row.couponMoney:'0'}}</span>
					</template>
				</el-table-column>
				
				<el-table-column prop="payMoney" label="支付金额">
					<template slot-scope="scope" >
						<span style="color: #DD0000;">{{scope.row.payMoney?scope.row.payMoney:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderType" label="订单类型">
					<template slot-scope="scope">
						<span v-if="scope.row.orderType==1">技能订单</span>
						<span v-if="scope.row.orderType==2">约玩订单</span>
					</template>
				</el-table-column>
				<el-table-column prop="zhiRate" label="一级佣金">
					<template slot-scope="scope">
						<span style="color: #DD0000;cursor: pointer;"
							@click="updates(scope.row.zhiUserId)">{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="feiRate" label="二级佣金">
					<template slot-scope="scope">
						<span style="color: #DD0000;cursor: pointer;"
							@click="updates(scope.row.feiUserId)">{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="rate" label="陪玩官佣金">
					<template slot-scope="scope">
						<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="consortiaRate" label="会长佣金">
					<template slot-scope="scope">
						<el-button size="mini" style="color: #DD0000;background: #fff;border: none;" type="primary"  @click="updates(scope.row.consortiaUserId)">
							{{scope.row.consortiaRate ? scope.row.consortiaRate : '0'}}
						</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="pingRate" label="平台金额">
					<template slot-scope="scope">
						<span>{{scope.row.pingRate?scope.row.pingRate:'0'}}</span>
					</template>
				</el-table-column>
				
				<el-table-column prop="remarks" label="备注">
				</el-table-column>
				<el-table-column prop="state" label="订单状态">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.state==0">待支付</span>
						<span style="color: #E87D0D;" v-if="scope.row.state==1">待服务</span>
						<span style="color: #009900;" v-if="scope.row.state==2">已完成</span>
						<span style="color: #999;" v-if="scope.row.state==3">已退款</span>
						<span style="color: #999;" v-if="scope.row.state==4">进行中</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="createTime" fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" style="margin: 2px;" type="primary" :disabled="!isAuth('orderCenter:accomplish')" v-if="scope.row.state==1" @click="cancelOrder(scope.row,2)">完成</el-button>
						<el-button size="mini" style="margin: 2px;" type="warning" :disabled="!isAuth('orderCenter:refund')" v-if="scope.row.state==1" @click="cancelOrder(scope.row,3)">退款</el-button>
						<el-button size="mini" style="margin: 2px;" type="danger" :disabled="!isAuth('orderCenter:delete')" @click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="color: #B94A48;font-size: 20px;margin-top: 10px;display: inline-block;">本页累计交易统计：{{totalMoney.toFixed(2)}}元</div>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="会员订单" name="huiyuan">
			<div style="display: inline-block;">
				<span>状态：</span>
				<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="animeDat(status)">
					<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				<div style="position: relative;display: inline-block;">
					<span>订单用户：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单用户"
						v-model="ordersUserName"></el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;">
					<span>接单陪玩用户：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入接单陪玩用户"
						v-model="userName"></el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;">
					<span>订单编号：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单编号"
						v-model="ordersNoT"></el-input>&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="datetime"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="exportBtn">导出
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="ordersId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="ordersUserName" label="订单用户">
					<template slot-scope="scope">
						<span style="color: #008000;cursor: pointer;"
							@click="updates(scope.row.ordersUserId)">{{ scope.row.ordersUserName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="接单陪玩用户">
					<template slot-scope="scope">
						<span style="color: #008000;cursor: pointer;"
							@click="updates(scope.row.userId)">{{ scope.row.userName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersNo" label="订单编号">
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额">
				</el-table-column>
				<el-table-column prop="ordersType" label="订单类型">
					<template slot-scope="scope">
						<span v-if="scope.row.ordersType==1">技能订单</span>
						<span v-if="scope.row.ordersType==2">会员订单</span>
						<span v-if="scope.row.ordersType==3">约玩订单</span>
					</template>
				</el-table-column>
				<el-table-column prop="zhiRate" label="一级佣金">
					<template slot-scope="scope">
						<span style="color: #DD0000;cursor: pointer;"
							@click="updates(scope.row.zhiUserId)">{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="feiRate" label="二级佣金">
					<template slot-scope="scope">
						<span style="color: #DD0000;cursor: pointer;"
							@click="updates(scope.row.feiUserId)">{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="rate" label="陪玩官佣金">
					<template slot-scope="scope">
						<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="consortiaRate" label="会长佣金">
					<template slot-scope="scope">
						<el-button size="mini" style="color: #DD0000;background: #fff;border: none;" type="primary"  @click="updates(scope.row.consortiaUserId)">
							{{scope.row.consortiaRate ? scope.row.consortiaRate : '0'}}
						</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="pingRate" label="平台金额">
					<template slot-scope="scope">
						<span>{{scope.row.pingRate?scope.row.pingRate:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="remarks" label="备注">
				</el-table-column>
				<el-table-column prop="state" label="订单状态">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.state==0">待支付</span>
						<span style="color: #E87D0D;" v-if="scope.row.state==1">待服务</span>
						<span style="color: #009900;" v-if="scope.row.state==2">已完成</span>
						<span style="color: #999;" v-if="scope.row.state==3">已退款</span>
						<span style="color: #999;" v-if="scope.row.state==4">进行中</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="createTime" fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" style="margin: 2px;" type="primary" :disabled="!isAuth('orderCenter:accomplish')" v-if="scope.row.state==1" @click="cancelOrder(scope.row,2)">完成</el-button>
						<el-button size="mini" style="margin: 2px;" type="warning" :disabled="!isAuth('orderCenter:refund')" v-if="scope.row.state==1" @click="cancelOrder(scope.row,3)">退款</el-button>
						<el-button size="mini" style="margin: 2px;" type="danger" :disabled="!isAuth('orderCenter:delete')" @click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="color: #B94A48;font-size: 20px;margin-top: 10px;display: inline-block;">本页累计交易统计：{{totalMoney.toFixed(2)}}元</div>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="订单统计" name="third">
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat2">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeSelect2">
				</el-date-picker>
			</div>
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">直属金额</div>
							<div class="text_color">
								<span>{{withdrawData.sumMoney}}</span>元
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">非直属金额</div>
							<div class="text_color">
								<span>{{withdrawData.countMoney}}</span>笔
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">陪玩官金额</div>
							<div class="text_color">
								<span>{{withdrawData.stayMoney}}</span>笔
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">平台金额</div>
							<div class="text_color">
								<span>{{withdrawData.stayMoney}}</span>笔
							</div>
						</div>
					</div>
				</el-col>
			</el-row>
		</el-tab-pane> -->
		<!-- 添加信息弹框 -->
		<el-dialog title="添加订单" customClass="customWidth" :visible.sync="dialogFormVisibleO" center>
			
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">下单人：</span>
				<el-input v-model="nickName1" @focus="userselect" style="width:50%;" placeholder="请选择下单人">
				</el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">订单类型：</span>
				<el-radio-group v-model="orderType">
					<el-radio :label="1">技能订单</el-radio>
					<el-radio :label="2">约玩订单</el-radio>
				</el-radio-group>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">下单服务：</span>
				<el-input v-model="fuwuName" @focus="fuwuselect" style="width:50%;" placeholder="请选择下单服务">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">数量：</span>
				<el-input-number size="small" v-model="orderNumber" @change="orderNumberBtn" :min="1"></el-input-number>
				<!-- <el-input v-model="" style="width:50%;" placeholder="请输入订单号"></el-input> -->
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务单价：</span>
				<!-- <el-input v-model="orderTakingMoney" style="width:50%;" placeholder="请输入服务单价"></el-input> -->
				<span style="color: red;font-weight: 600;font-size: 18;">{{orderTakingMoney}}</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input v-model="remarks" style="width:50%;" type="textarea" :rows="2" placeholder="请输入备注"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">支付金额：</span>
				<span style="color: red;font-weight: 600;font-size: 18;">{{payMoney}}</span>
				<!-- <el-input v-model="payMoney" style="width:50%;" placeholder="请输入支付金额"></el-input> -->
			</div>
			
			
			<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
				<el-button @click="dialogFormVisibleO=false">取 消</el-button>
				<el-button type="primary" @click="addmissionNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisibleU" center width="80%">
			<div style="margin:2% 0;display: inline-block;">
				<span>手机号</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input>&nbsp;&nbsp;
				<span>昵称</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>&nbsp;&nbsp;
				<span>邀请人邀请码</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入邀请人邀请码"
					v-model="inviterCode"></el-input>
				<span>是否会员：</span>
				<el-select v-model="isVipT" style="width:150px;margin-left: 10px;" @change="userclick()">
					<el-option v-for="item in isVipType" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table width="780px" v-loading="tableDataLoadingU" :data="userIds.list">
				<el-table-column prop="userId" width="80" label="编号">
				</el-table-column>
				<el-table-column prop="nickName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column label="邀请人邀请码">
					<template slot-scope="scope">
						<span >{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="isVip" label="是否是会员">
					<template slot-scope="scope">
						<span v-if="scope.row.isVip==1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="createTime" label="创建时间">
				</el-table-column> -->
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="sizeU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalPage">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 服务弹框 -->
		<el-dialog title="服务列表" :visible.sync="dialogFormVisibleF" center width="80%">
			<div style="display: inline-block;">
				<span>游戏类型：</span>
				<el-select v-model="classify" style="width:150px;margin-left: 10px;" @change="phoneSelect(classify)">
					<el-option v-for="item in classifyList" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<span>游戏名称：</span>
				<el-select v-model="classifyIds" style="width:150px;margin-left: 10px;" @change="phoneSelect(status)">
					<el-option v-for="item in homeData1" :key="item.value" :label="item.gameName" :value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<!-- <span>状态：</span>
				<el-select v-model="statusId" style="width:150px;margin-left: 10px;" @change="phoneSelect(status)">
					<el-option v-for="item in statuss" :key="item.value" :label="item.title" :value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp; -->
				<div style="position: relative;display: inline-block;">
					<span>发布人：</span>
					<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入发布人"
						v-model="search">
					</el-input>&nbsp;&nbsp;
				</div>
				<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="phoneSelect">查询
				</el-button>
				<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleans2">重置
				</el-button>
			</div>
			<el-table v-loading="tableDataLoadingF" :data="choicenData.list">
				<el-table-column prop="id" label="编号" width="80" fixed="left">
				</el-table-column>
				<el-table-column prop="userName" label="发布人" fixed="left">
					<template slot-scope="scope">
						　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row)"
							v-text="scope.row.userName"></span>
					</template>
				</el-table-column>
				<el-table-column prop="classify" label="游戏类型" width="150">
					<template slot-scope="scope">
						<span v-if="scope.row.classify==1">线上</span>
						<span v-if="scope.row.classify==2">线下</span>
					</template>
				</el-table-column>
				<el-table-column prop="gameName" label="游戏名称" width="150">
				</el-table-column>
				<el-table-column prop="myLevel" label="段位" width="100">
				</el-table-column>
				<el-table-column prop="orderLevel" label="可接段位" width="100">
				</el-table-column>
				<el-table-column prop="orderTakingArea" label="接单区服" width="200">
				</el-table-column>
				<el-table-column prop="oldMoney" label="发布价格" width="100">
				</el-table-column>
				<el-table-column prop="money" label="普通用户价格" width="100">
				</el-table-column>
				<el-table-column prop="memberMoney" label="会员价格" width="100">
				</el-table-column>
				<el-table-column prop="unit" label="单位" width="100">
				</el-table-column>
				<el-table-column prop="orderTakingTime" label="接单时间" width="200">
				</el-table-column>
				<el-table-column prop="city" label="城市" width="200">
				</el-table-column>
				<el-table-column prop="orderScore" label="评分" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.orderScore">{{scope.row.orderScore}}</span>
						<span v-else>0</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="在线状态" width="100" fixed='right'>
					<template slot-scope="scope">
						<span style="color: #4f9dec;" v-if="scope.row.onLine == 1 ">在线</span>
						<span style="color: #ff4949;" v-else>离线</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态" width="100" fixed='right'>
					<template slot-scope="scope">
						<span style="color: #E87D0D;" v-if="scope.row.status === 1 ">待审核</span>
						<span style="color: #4f9dec;" v-if="scope.row.status === 0 ">上架</span>
						<span style="color: #ff4949;" v-if="scope.row.status === 2 ">下架</span>
						<span style="color: #999;" v-if="scope.row.status === 3 ">驳回</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('locality:update')"
							@click="choiCompile(scope.row)">选择
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[5,10, 20, 30, 50, 100]" :page-size="sizeF" :current-page="pageF"
					layout="total,sizes, prev, pager, next,jumper" :total="choicenData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 2,
				status: '',
				userName: '',
				ordersUserName:'',
				type: '',
				id: '',
				statesnum: [
					
					{
							value: '',
							label: '全部'
						},
						{
							value: 0,
							label: '待支付'
						},
						{
							value: 1,
							label: '待服务'
						},
						{
							value: 2,
							label: '已完成'
						},
						{
							value: 3,
							label: '已退款'
						},
						{
							value: 4,
							label: '进行中'
						}
				],
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '技能订单'
					},
					{
						value: 2,
						label: '约玩订单'
					},
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
				flag: '',
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
				totalMoney:0,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
				ordersType:1,
				ordersNoT:'',
				// 新增订单数据
				dialogFormVisibleO:false,
				dialogFormVisibleU: false,
				tableDataLoadingU: false,
				
				nickName2: '',
				inviterCode:'',
				phone1: '',
				
				userIds: {}, //用户列表
				sizeU: 10,
				pageU: 1,
				
				dialogFormVisibleF:false,
				tableDataLoadingF:false,
				classify: '',
				classifyList: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '线上'
					},
					{
						id: 2,
						name: '线下'
					}
				],
				classifyIds: '',
				homeData1: [{
					id: 0,
					name: '全部'
				}],
				search: '',
				statusId: 0,
				statuss: [{
						id: '',
						title: '全部'
					},
					{
						id: 0,
						title: '进行中'
					},
					{
						id: 1,
						title: '待审核'
					},
					{
						id: 2,
						title: '已下架'
					},
					{
						id: 3,
						title: '拒绝'
					}
				],
				sizeF: 10,
				pageF: 1,
				choicenData:{},
				orderType:1,
				ordersNo:'',
				
				fuwuName:'',
				orderNumber:1,
				payMoney:0,
				orderTakingMoney:0,
				ptMoney:0,
				hyMoney:0,
				orderTakingId:'',
				remarks:'',
				isVip:'',
				userIdss:'',
				nickName1: '',
				isVipT:-1,
				isVipType: [{
						value: -1,
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 0,
						label: '否'
					},
				],
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
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// 详情跳转
			updataDetails(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
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
			handleSizeChange2(val) {
				this.sizeU = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.pageU = val
				this.userClass()
			},
			handleSizeChange1(val) {
				this.sizeF = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.pageF = val
				this.InformationSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '陪玩订单') {
					this.page = 1
					this.limit = 10
					this.ordersType = 1
					this.type = ''
					this.dataSelect()
				}
				if (tab._props.label == '会员订单') {
					this.page = 1
					this.limit = 10
					this.ordersType = 2
					this.type = ''
					this.dataSelect()
				}

			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible2 = true
			},

			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.totalMoney = 0
				var endTime=this.endTime
				if(this.endTime!=''){
					endTime=this.endTime+" 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('orders/queryOrders'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'orderType': this.type,
						'ordersType':this.ordersType,
						'name': this.userName,
						'orderName':this.ordersUserName,
						'ordersNo':this.ordersNoT,
						'status': this.status,
						'startTime': this.startTime,
						'endTime': endTime
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for(var i in data.data.list){
					  if(data.data.list[i].payMoney){
					  	this.totalMoney= this.totalMoney+data.data.list[i].payMoney
					  }
					}
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 详情跳转
			updates(userId) {
				if(userId){
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.userName = ''
				this.ordersUserName = ''
				this.ordersNoT = ''
				this.status = ''
				this.type = ''
				this.startTime = ''
				this.endTime = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			// 删除订单
			deletes(row) {
				let delid = row.ordersId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/deleteOrders?id=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
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
				})
			},
		cancelOrder(row,status) {
				let delid = row.ordersId
				let content=''
				if(status==2){
					content="确定完成此订单？"
				}else{
					content="确定退款此订单？"
				}
				this.$confirm(content, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/cancelOrder?id=${delid}&status=${status}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				})
			},
			// 导出
			exportBtn() {
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				 var endTime=this.endTime
				 if(this.endTime!=''){
				 	endTime=this.endTime+" 23:59:59"
				 }
				this.$http({
					url: this.$http.adornUrl('orders/ordersListExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'orderType': this.type,
						'ordersType':this.ordersType,
						'name': this.userName,
						'orderName':this.ordersUserName,
						'ordersNo':this.ordersNoT,
						'status': this.status,
						'startTime': this.startTime,
						'endTime': endTime
					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 新增订单弹框
			orderAdd(){
				this.fuwuName = ''
				this.orderNumber = 1
				this.payMoney = 0
				this.orderTakingMoney = 0
				this.ptMoney = 0
				this.hyNoney = 0
				this.orderTakingId = ''
				this.remarks = ''
				this.isVip = ''
				this.userIdss = ''
				this.nickName1 =  ''
				
				this.dialogFormVisibleO = true
			},
			// 获取用户列表弹框
			userselect() {
				this.phone1 = ''
				this.nickName2 = ''
				this.inviterCode = ''
				this.isVipT = -1
				this.isVip = ''
				this.pageU = 1
				this.dialogFormVisibleU = true
				
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				
				this.tableDataLoadingU = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.sizeU,
						'member': this.isVipT,
						'userName': this.nickName2,
						'inviterCode': this.inviterCode,
						'startTime': '',
						'endTime': '',
						'isAuthentication':-1,
						'phone': this.phone1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingU = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.pageU = 1
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.inviterCode = ''
				this.isVipT = -1
				this.isVip = ''
				this.pageU = 1
				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				if(row.isVip==1){
					this.isVip = 1
					this.orderTakingMoney = this.hyNoney
					this.payMoney = this.hyNoney
				}else{
					this.isVip = ''
					this.orderTakingMoney = this.ptMoney
					this.payMoney = this.ptMoney
				}
				this.orderNumber = 1
				// this.payMoney = 0
				// this.orderTakingMoney = 0
				// this.orderTakingId = ''
				this.remarks = ''
				this.dialogFormVisibleU = false
			},
			// 服务弹框
			fuwuselect(){
				this.status = ''
				this.statusId = 0
				this.classify = ''
				this.search = ''
				this.classifyIds = ''
				this.pageF = 1
				this.homeSelect() 
				this.InformationSelect()
				this.dialogFormVisibleF = true
			},
			// 获取游戏分类
			homeSelect() {
				this.$http({
					url: this.$http.adornUrl(`game/queryGame`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.homeData1 = [{
						id: '',
						gameName: '全部'
					}]
					if (data.data) {
						for (var i in data.data) {
							this.homeData1.push(data.data[i])
							data.data[i].state = Number(data.data[i].state)
						}
					}
					this.homeData = data.data
				})
			},
			// 信息数据
			InformationSelect() {
				this.tableDataLoadingF = true
				this.$http({
					url: this.$http.adornUrl(`orderTaking/queryAllOrderTaking`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageF,
						'limit': this.sizeF,
						'userName': this.search,
						'gameId': this.classifyIds,
						'status': this.statusId,
						'classify': this.classify
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingF = false
					let returnData = data.data
					this.choicenData = returnData
			
				})
			},
			// 查询
			phoneSelect() {
				this.pageF = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.status = ''
				this.statusId = 0
				this.classify = ''
				this.search = ''
				this.classifyIds = ''
				this.pageF = 1
				this.InformationSelect()
			},
			// 选择服务
			choiCompile(row){
				this.orderTakingId = row.id
				this.fuwuName = row.gameName+'/'+row.myLevel
				if(this.isVip == 1){
					this.orderTakingMoney = row.memberMoney
					this.payMoney = row.memberMoney
				}else{
					this.orderTakingMoney = row.money
					this.payMoney = row.money
				}
				this.ptMoney = row.money
				this.hyNoney = row.memberMoney
				this.dialogFormVisibleF = false
			},
			// 修改数量
			orderNumberBtn(){
				if (this.orderNumber === ''||this.orderNumber==null) {
					
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '数量不能为空',
						type: 'warning'
					});
					return
				}
				this.payMoney = (Number(this.orderTakingMoney)*this.orderNumber).toFixed(2)
			},
			// 确认添加
			addmissionNoticeTo(){
				if (this.userIdss === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择下单人',
						type: 'warning'
					});
					return
				}
				if (this.orderTakingId === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择下单服务',
						type: 'warning'
					});
					return
				}
				if (this.orderNumber === ''||this.orderNumber==null) {
					
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '数量不能为空',
						type: 'warning'
					});
					return
				}
				
				this.$http({
					url: this.$http.adornUrl('orders/insertOrders'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'orderTakingId': this.orderTakingId,
						'orderTakingMoney': this.orderTakingMoney,
						'userId': this.userIdss,
						'payMoney':this.payMoney,
						'remarks': this.remarks,
						'orderNumber':this.orderNumber,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
								this.dialogFormVisibleO = false
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				
				})
			}

		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>
	.box {
		padding: 44px;
		border: 1px solid #eee;
		margin: 15px 10px;
	}

	.box_num {
		font-size: 14px;
		color: #66b1ff;
	}

	.box_num .box_color {
		color: #333;
		font-size: 14px;
		margin-bottom: 15px;
	}

	.box_num div span {
		font-size: 20px;
		margin-left: 5px;
	}

	.text_color {
		color: #4f9dec;
	}

	.text_color span {
		margin-right: 5px;
	}
</style>
