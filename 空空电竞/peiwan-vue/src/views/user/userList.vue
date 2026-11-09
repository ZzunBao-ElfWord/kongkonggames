<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="全部用户" name="first">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>邀请人邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请人邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" default-time="00:00:00" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					</div>
					<div style="display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" default-time="23:59:59" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
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
					<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="rate" label="陪玩官佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'rate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="公会订单抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaOrdersRate?scope.row.consortiaOrdersRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaOrdersRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label=" 公会礼物抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaGiftRate?scope.row.consortiaGiftRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaGiftRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">已实名</span>
							<span v-else>未实名</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是陪玩官">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态" >
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>

							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<el-button size="mini" type="primary" @click="updates1(scope.row)">{{scope.row.isAuthentication==1?'取消陪玩官':'设置陪玩官'}}</el-button>
							<el-button size="mini" type="primary" @click="updates3(scope.row)">设置1个月会员</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="普通用户" name="second">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>邀请人邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请人邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" default-time="00:00:00" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					</div>
					<div style="display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" default-time="23:59:59" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
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
					<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="rate" label="陪玩官佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'rate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="公会订单抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaOrdersRate?scope.row.consortiaOrdersRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaOrdersRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label=" 公会礼物抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaGiftRate?scope.row.consortiaGiftRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaGiftRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">已实名</span>
							<span v-else>未实名</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是陪玩官">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
							<el-button size="mini" type="primary" @click="updates1(scope.row)">{{scope.row.isAuthentication==1?'取消陪玩官':'设置陪玩官'}}</el-button>
							<el-button size="mini" type="primary" @click="updates3(scope.row)">设置1个月会员</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="会员用户" name="third">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>邀请人邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请人邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" default-time="00:00:00" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					</div>
					<div style="display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" default-time="23:59:59" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
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
					<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="rate" label="陪玩官佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'rate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="公会订单抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaOrdersRate?scope.row.consortiaOrdersRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaOrdersRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label=" 公会礼物抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaGiftRate?scope.row.consortiaGiftRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaGiftRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">已实名</span>
							<span v-else>未实名</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是陪玩官">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
							<el-button size="mini" type="primary" @click="updates1(scope.row)">{{scope.row.isAuthentication==1?'取消陪玩官':'设置陪玩官'}}</el-button>
							<el-button size="mini" type="primary" @click="updates3(scope.row)">设置1个月会员</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="陪玩官用户" name="four">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>邀请人邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请人邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" default-time="00:00:00" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					</div>
					<div style="display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" default-time="23:59:59" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
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
					<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="rate" label="陪玩官佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'rate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="公会订单抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaOrdersRate?scope.row.consortiaOrdersRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaOrdersRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label=" 公会礼物抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaGiftRate?scope.row.consortiaGiftRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaGiftRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">已实名</span>
							<span v-else>未实名</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="isVip" label="是否是陪玩官">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
							<el-button size="mini" type="primary" @click="updates1(scope.row)">{{scope.row.isAuthentication==1?'取消陪玩官':'设置陪玩官'}}</el-button>
							<el-button size="mini" type="primary" @click="updates3(scope.row)">设置1个月会员</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="团长用户" name="tuanzhang">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>邀请人邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请人邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				<!-- <div style="margin:2% 0;display: inline-block;">
					<span>邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请码"
						v-model="invitationCode"></el-input>
				</div>&emsp;&emsp; -->
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" default-time="00:00:00" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" default-time="23:59:59" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="150">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="rate" label="陪玩官佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'rate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="公会订单抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaOrdersRate?scope.row.consortiaOrdersRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaOrdersRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label=" 公会礼物抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaGiftRate?scope.row.consortiaGiftRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaGiftRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">已实名</span>
							<span v-else>未实名</span>
						</template>
					</el-table-column>
					<el-table-column prop="consortiaName" label="公会名称" width="160"></el-table-column>
					<!-- <el-table-column prop="consortiaNo" label="公会编号" width="100"></el-table-column> -->
					<!-- <el-table-column prop="member" label="会员等级" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.member?scope.row.member:'1'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;" @click="xiugai1(scope.row)">修改
							</el-button>
							</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="isVip" label="是否是陪玩官">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="isSafetyMoney" label="是否是缴纳保证金">
						<template slot-scope="scope">
							<span v-if="scope.row.isSafetyMoney==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary" @click="updates2(scope.row)">
								{{scope.row.inviterCode ? scope.row.inviterCode : '未绑定'}}
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
							<el-button size="mini" type="primary" @click="updates1(scope.row)">{{scope.row.isAuthentication==1?'取消陪玩官':'设置陪玩官'}}</el-button>
							<el-button size="mini" type="primary" @click="updates3(scope.row)">设置1个月会员</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="会长用户" name="gonghui">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>邀请人邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请人邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				<!-- <div style="margin:2% 0;display: inline-block;">
					<span>邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请码"
						v-model="invitationCode"></el-input>
				</div>&emsp;&emsp; -->
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" default-time="00:00:00" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 250px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" default-time="23:59:59" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="150">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="rate" label="陪玩官佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'rate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例"  width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="公会订单抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaOrdersRate?scope.row.consortiaOrdersRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaOrdersRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label=" 公会礼物抽成比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.consortiaGiftRate?scope.row.consortiaGiftRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'consortiaGiftRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">已实名</span>
							<span v-else>未实名</span>
						</template>
					</el-table-column>
					<el-table-column prop="consortiaName" label="公会名称" width="160"></el-table-column>
					<!-- <el-table-column prop="consortiaNo" label="公会编号" width="100"></el-table-column> -->
					<!-- <el-table-column prop="member" label="会员等级" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.member?scope.row.member:'1'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;" @click="xiugai1(scope.row)">修改
							</el-button>
							</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="isVip" label="是否是陪玩官">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="isSafetyMoney" label="是否是缴纳保证金">
						<template slot-scope="scope">
							<span v-if="scope.row.isSafetyMoney==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary" @click="updates2(scope.row)">
								{{scope.row.inviterCode ? scope.row.inviterCode : '未绑定'}}
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
							<el-button size="mini" type="primary" @click="updates1(scope.row)">{{scope.row.isAuthentication==1?'取消陪玩官':'设置陪玩官'}}</el-button>
							<el-button size="mini" type="primary" @click="updates3(scope.row)">设置1个月会员</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 修改比例 -->
			<el-dialog :title="titleBl" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">比例：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						:placeholder="titleBl"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 2,
				state: 'false',
				limit: 10,
				page: 1,
				userName: '',
				inviterCode: '',
				isAuthentication:-1,
				nickNam: '',
				phone: '',
				platform: 'all',
				isRelation: -1,
				isTuan: -1,
				value: '',
				member: -1,
				payData: [],
				nickName: '',
				grade: '',
				name: '',
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible2:false,
				tableData: [],
				platforms: [{
						value: 'all',
						label: '全部'
					}, {
						value: 'wap',
						label: '网站'
					}, {
						value: 'app',
						label: 'APP'
					}, {
						value: 'weixin',
						label: '微信公众号'
					},
					{
						value: 'mp',
						label: '微信小程序'
					}
				],
				campus: '',
				homeData1: [],
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				proportion:'',
				texts:'',
				userIdss:'',
				titleBl: '修改佣金比例',
				invitationCode:'',
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime1() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			getNowTime2() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// tabs切换
			handleClick(tab, event) {
				this.page = 1
				this.limit = 10
				this.startTime = ''
				this.endTime = ''
				this.phone = ''
				if (tab._props.label == '全部用户') {
					this.member = -1
					this.isAuthentication = -1
					this.dataSelect()
				}
				if (tab._props.label == '普通用户') {
					this.member = 0
					this.isAuthentication = 0
					this.dataSelect()
				}
				if (tab._props.label == '会员用户') {
					this.member = 1
					this.isAuthentication = -1
					this.dataSelect()
				}
				if (tab._props.label == '陪玩官用户') {
					this.member = -1
					this.isAuthentication = 1
					this.dataSelect()
				}
				if (tab._props.label == '团长用户') {
					this.member = -1
					this.isAuthentication = 2
					this.dataSelect()
				}
				if (tab._props.label == '会长用户') {
					this.member = -1
					this.isAuthentication = 3
					this.dataSelect()
				}

			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
					data: this.$http.adornData({})
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
			// 详情跳转
			updates2(row) {
				// this.$router.push({
				// 	path: '/userDetail',
				// 	query: {
				// 		userId: row.superior
				// 	}
				// })
				this.$http({
					url: this.$http.adornUrl('user/selectUserByInvitationCode'),
					method: 'get',
					params: this.$http.adornParams({
						'invitationCode': row.inviterCode
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$router.push({
							path: '/userDetail',
							query: {
								userId: data.data.userEntity.userId
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
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.paySelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.paySelect()
			},
			// select自动选择
			selectTrigger() {
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			select2() {
				this.paySelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.startTime = ''
				this.endTime = ''
				this.userName = ''
				this.inviterCode = ''
				this.phone = ''
				this.dataSelect()
			},
			//删除用户
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteUserByUserId/${delid}`),
						method: 'post',
						params: this.$http.adornData({})
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
				})
			},
			// 设置陪玩官
			updates1(row) {
				this.$confirm(`确定进行此操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserIsAuthentication'),
						method: 'post',
						data: this.$http.adornData({
							'userId':row.userId
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
				})
			},
			// 设置会员
			updates3(row) {
				this.$confirm(`确定要设置1个月会员?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserVip'),
						method: 'post',
						params: this.$http.adornParams({
							'userId':row.userId
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
				})
			},
			// 获取社区数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpCampus/selectCampusList'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page':this.page,
						// 'limit':this.limit,
						// 'content':this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData1 = returnData
				})
			},
			// 筛选信息
			animeDat2(state) {
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'member': this.member,
						'userName': this.userName,
						'inviterCode': this.inviterCode,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'isAuthentication':this.isAuthentication,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			xiugai(row,text){
				this.titleBl = '修改佣金比例'
				if(text=='rate'){
					this.proportion = row.rate
				}
				if(text=='zhiRate'){
					this.proportion = row.zhiRate
				}
				if(text=='feiRate'){
					this.proportion = row.feiRate
				}
				if (text == 'consortiaOrdersRate') {
					this.titleBl = '公会订单抽成比例'
					this.proportion = row.consortiaOrdersRate
				}
				if (text == 'consortiaGiftRate') {
					this.titleBl = '公会礼物抽成比例'
					this.proportion = row.consortiaGiftRate
				}
				this.texts = text
				this.userIdss = row.userId
				this.dialogFormVisible2 = true
			},
			StairNoticeTo2(){
				var datas = {}
				if(this.texts =='rate'){
					datas.rate = this.proportion
				}
				if(this.texts =='zhiRate'){
					datas.zhiRate = this.proportion
				}
				if(this.texts =='feiRate'){
					datas.feiRate = this.proportion
				}
				if (this.texts == 'consortiaOrdersRate') {
					datas.consortiaOrdersRate = this.proportion
				}
				if (this.texts == 'consortiaGiftRate') {
					datas.consortiaGiftRate = this.proportion
				}
				datas.userId = this.userIdss
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					console.log('data',data)
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dialogFormVisible2 = false
							this.dataSelect()
						}
					})
				})
			},
		},
		mounted() {
			// this.homeSelect()
			this.dataSelect()
		}
	}
</script>

<style scoped="scoped">
	.el-button+.el-button {
		margin-left: 0 !important;
		margin-top: 5px !important;
	}
</style>
