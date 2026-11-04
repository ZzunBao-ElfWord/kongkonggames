<template>
	<div>
		<div style="display: inline-block;font-size:18px;margin-bottom: 15px;">
			<a href="#" @click="prev" style="text-decoration:none;font-size: 14px;">
				<icon-svg name="jiantou" style="width: 1.2em;height: 1.2em;position: relative;top: 0.3em;"></icon-svg>
				返回
			</a>
			<span style="display: inline-block;margin: 0 15px;color: #D9D9D9;">|</span>
			<span>用户详情</span>
		</div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="用户详情" name="first">
				<div class="detailtabel">
					<div class="table_main">
						<table>
							<tbody>
								<tr>
									<th>本月接单数量</th>
									<td>{{ tablenum.takeOrdersCount ? `${tablenum.takeOrdersCount}笔` : '0笔' }}</td>
									<th>本月充值金额</th>
									<td>{{ tablemoney.consume ? `${tablemoney.consume}元` : '0元' }}</td>
									<th>本月邀请数量</th>
									<td class="border-rt">{{ tablemoney.count ? `${tablemoney.count}人` : '0人' }}
									</td>
								</tr>
								<tr>
									<th>本月订单数量</th>
									<td>{{ tablemoney.myOrdersCount ? `${tablemoney.myOrdersCount}笔` : '0笔' }}</td>
									<th>本月提现金额</th>
									<td>{{ tablemoney.income ? `${tablemoney.income}元` : '0元' }}</td>
									<th>openId</th>
									<td class="border-rt">{{ tableData.openId ? tableData.openId : '未绑定' }}</td>
								</tr>
								<tr>
									<th>陪玩官佣金比例</th>
									<td>{{ tableData.rate ? `${tableData.rate}` : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="xiugai(tableData,'rate')">修改</el-button>
										</span>
									</td>
									<th>一级佣金比例</th>
									<td>{{ tableData.zhiRate ? `${tableData.zhiRate}` : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="xiugai(tableData,'zhiRate')">修改</el-button>
									</td>
									<th>二级佣金比例</th>
									<td class="border-rt">{{ tableData.feiRate ? tableData.feiRate : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="xiugai(tableData.feiRate,'feiRate')">修改</el-button>
									</td>
									<!-- <th>保证金</th>
									<td class="border-rt">
										{{ baozhengjinData.safetyMoney ? `${baozhengjinData.safetyMoney }` : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatebzj')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="rechargenone(tableData.userId,3)">修改</el-button>
										( <span
											style="color: red;">{{tableData.isSafetyMoney==1?'已交保证金':'未交保证金'}}</span> )
									</td> -->
								</tr>
								<tr>
									<th>公会订单抽成比例</th>
									<td>{{ tableData.consortiaOrdersRate ? `${tableData.consortiaOrdersRate}` : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="xiugai(tableData,'consortiaOrdersRate')">修改</el-button>
										</span>
									</td>
									<th>公会礼物抽成比例</th>
									<td>{{ tableData.consortiaGiftRate ? `${tableData.consortiaGiftRate}` : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="xiugai(tableData,'consortiaGiftRate')">修改</el-button>
									</td>
									<th>余额</th>
									<td class="border-rt" style="color: rgb(245, 108, 108)">
										{{ tablemoney.money ? `${tablemoney.money}` : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="rechargenone(2)">修改</el-button>
									</td>
								</tr>

								<tr>
									<th>公会编号</th>
									<td>
										{{tableData.consortiaNo}}
										<el-button size="mini" :disabled="!isAuth('userList:update')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="xiugai(tableData,'consortiaNo')">修改</el-button>
									</td>
									<th>公会名称</th>
									<td>{{tableData.consortiaName}}

									</td>
									<th>可提现余额</th>
									<td class="border-rt"style="color: rgb(245, 108, 108)">
										{{ tablemoney.earningsMoney ? `${tablemoney.earningsMoney}` : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="rechargenone(1)">修改</el-button>
									</td>
									<!-- <th>接单总数</th>
									<td class="border-rt">{{tablenum.ordersCount}}</td> -->


								</tr>
								<tr>
									<th>邀请人邀请码</th>
									<td>{{ tableData.inviterCode }}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="inviterCodeChange(tableData.userId,tableData.inviterCode)">修改
										</el-button>
									</td>
									<th>收款二维码</th>
									<td>
										<div v-if="tableData.wxImg == null || tableData.wxImg == ''">
											暂无收款二维码
										</div>
										<div v-else style="display:flex;flex-wrap: wrap;">
											<div style="margin: 2px;">
												<el-popover placement="top-start" title="" trigger="hover">
													<img style="width: 50px; height: 50px" :src="tableData.wxImg" alt=""
														slot="reference">
													<img style="width: 300px; height: auto" :src="tableData.wxImg"
														alt="">
												</el-popover>
											</div>
										</div>
									</td>
									<th>图像</th>
									<td class="border-rt">
										<img v-if="tableData.avatar==null" src="~@/assets/img/avatar.png" alt=""
											width="80" height="80">
										<img v-else :src="tableData.avatar" width="80" height="80" />
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="avatarChange(tableData.userId,tableData.avatar)">修改</el-button>
									</td>
								</tr>
								<tr>
									<th>微信名称</th>
									<td>
										{{ tableData.userName }}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="nameChange(tableData.userId,tableData.userName)">修改</el-button>
									</td>
									<th>手机号</th>
									<td>{{ tableData.phone ? tableData.phone : '未绑定' }}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="phoneChange(tableData.userId,tableData.phone)">修改</el-button>
									</td>
									<th>支付宝姓名</th>
									<td class="border-rt">{{ tableData.zhiFuBaoName ? tableData.zhiFuBaoName : '未绑定' }}
									</td>
								</tr>
								<tr>
									<th>邀请码</th>
									<td>{{ tableData.invitationCode }}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="yqmChange(tableData.userId,tableData.invitationCode)">修改</el-button>
									</td>
									<th>支付宝账号</th>
									<td>{{ tableData.zhiFuBao ? tableData.zhiFuBao : '未绑定' }}</td>
									<th>用户状态</th>
									<td class=" border-rt">
										<span v-if="tableData.status == 1 ">正常</span>
										<span v-if="tableData.status == 2 " style="color: #f56c6c;">禁用</span>
										<el-button size="mini" :disabled="!isAuth('userList:updateStatus')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="stateChange(tableData.userId)">更改状态</el-button>
									</td>
								</tr>
								<tr>
									<th>是否实名</th>
									<td>
										<span v-if="tableData.isAuthentication==1">已实名</span>
										<span v-else>未实名</span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')&&tableData.isAuthentication==1"
											@click="shimingChange(tableData.userId,tableData.isAuthentication)">修改
										</el-button>
									</td>
									<th>性别</th>
									<td>
										<span v-if="tableData.sex==0">未设置</span>
										<span v-if="tableData.sex==1">男</span>
										<span v-if="tableData.sex==2">女</span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="sexChange(tableData.userId,tableData.sex)">修改</el-button>
									</td>
									<th>年龄</th>
									<td class="border-rt">
										<span v-if="tableData.age">{{tableData.age}}</span>
										<span v-else>未设置</span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChange(tableData.userId,tableData.age)">修改</el-button>
									</td>
								</tr>
								<tr>
									<th>是否是陪玩官</th>
									<td>
										<span>{{tableData.isAuthentication==1?'是':'否'}}</span>
									</td>
									<th>姓名</th>
									<td>
										<span
											v-if="tablenum.userCertification">{{tablenum.userCertification.name}}</span>
										<span v-else> - </span>
									</td>
									<th>身份证号</th>
									<td class="border-rt">
										<span v-if="tablenum.userCertification">
											{{tablenum.userCertification.idNumber}}
										</span>
										<span v-else> - </span>
									</td>
								</tr>
								<tr>
									<th>身份证正面</th>
									<td>
										<div style="margin: 2px;">
											<span v-if="tablenum.userCertification">
												<el-popover placement="top-start" title="" trigger="hover">
													<img style="width: 50px; height: 50px"
														:src="tablenum.userCertification.front" alt="" slot="reference">
													<img style="width: 300px; height: auto"
														:src="tablenum.userCertification.front" alt="">
												</el-popover>
											</span>
											<span v-else> - </span>
										</div>
									</td>
									<th>身份证反面</th>
									<td>
										<div style="margin: 2px;">
											<span v-if="tablenum.userCertification">
												<el-popover placement="top-start" title="" trigger="hover">
													<img style="width: 50px; height: 50px"
														:src="tablenum.userCertification.back" alt="" slot="reference">
													<img style="width: 300px; height: auto"
														:src="tablenum.userCertification.back" alt="">
												</el-popover>
											</span>
											<span v-else> - </span>
										</div>
									</td>
									<th>技能图片</th>
									<td class="border-rt">
										<div style="margin: 2px;"
											v-if="tablenum.userCertification && tablenum.userCertification.skillImg">
											<div v-for="(item,index) in tablenum.userCertification.skillImg.split(',')"
												style="display: inline-block;margin: 5px;">
												<el-popover placement="top-start" title="" trigger="hover">
													<img style="width: 50px; height: 50px" :src="item" alt=""
														slot="reference">
													<img style="width: 300px; height: auto" :src="item" alt="">
												</el-popover>
											</div>

										</div>
										<span v-else> - </span>
									</td>
								</tr>
								<tr>
									<th>创建时间</th>
									<td>{{ tableData.createTime }}</td>
									<!-- <th>认证类型</th>
									<td>
										<span v-if="tablenum.userCertification">
											{{tablenum.userCertification.serviceType}}
										</span>
										<span v-else> - </span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											v-if="tablenum.userCertification"
											@click="renzhengChange(tableData.userId,tablenum.userCertification.serviceType,1)">
											修改</el-button>
									</td> -->
									<th>个人简介</th>
									<td>
										<div v-if="tablenum.userCertification"
											style="word-wrap: break-word;word-break: break-all;max-width: 500px;">
											{{tablenum.userCertification.details}}
										</div>
										<span v-else> - </span>
										<!-- <el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											v-if="tablenum.userCertification"
											@click="renzhengChange(tableData.userId,tablenum.userCertification.details,2)">
											修改</el-button> -->
									</td>

									<th>生活图片</th>
									<td class="border-rt">
										<div style="margin: 2px;"
											v-if="tablenum.userCertification&&tablenum.userCertification.lifePhoto">
											<div v-for="(item,index) in tablenum.userCertification.lifePhoto.split(',')"
												style="display: inline-block;margin: 5px;">
												<el-popover placement="top-start" title="" trigger="hover">
													<img style="width: 50px; height: 50px" :src="item" alt=""
														slot="reference">
													<img style="width: 300px; height: auto" :src="item" alt="">
												</el-popover>
											</div>

										</div>
										<span v-else> - </span>
									</td>
								</tr>
								<tr>
									<th>渠道来源</th>
									<td>
										{{tableData.platform}}
									</td>
									<th>是否开启青少年模式</th>
									<td>
										<span>{{tableData.babyModel==1?'是':'否'}}</span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="changeQsnMm(tableData,1)"
											:disabled="!isAuth('userList:updateStatus')" v-if="tableData.babyModel==1">
											关闭</el-button>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="changeQsnMm(tableData,2)"
											:disabled="!isAuth('userList:updateStatus')" v-else>开启</el-button>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="changeQsnMm(tableData,3)" :disabled="!isAuth('userList:update')">
											修改密码</el-button>
									</td>
									<th>是否是会员</th>
									<td class="border-rt">
										<span v-if="tableData.isVip==1">是</span>
										<span v-else>否</span>
									</td>
								</tr>
								<tr>
									<th class="border-bt">自动回复</th>
									<td class="border-bt">
										<span>{{tableData.msg?tableData.msg:'暂无'}}</span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="msgChange(tableData.userId,tableData.msg)">修改</el-button>
									</td>
									<!-- <th>是否是会员</th>
									<td>
										<span v-if="tableData.isVip==1">是</span>
										<span v-else>否</span>
									</td> -->
									<th class="border-bt">会员到期时间</th>
									<td class="border-bt">{{tableData.vipEndTime}}
									</td>
									<th class="border-bt">认证时间</th>
									<td class="border-bt border-rt">
										<span v-if="tablenum.userCertification">
											{{tablenum.userCertification.updateTime}}
										</span>
										<span v-else> - </span>
									</td>
								</tr>
								<!-- <tr>
									<th class="border-bt">陪玩技能评分</th>
									<td class="border-bt">
										<div v-if="tablenum.score">{{tablenum.score}}</div>
										<div v-else>暂无评分</div>
									</td>
									<th class="border-bt">平台人工评分</th>

									<td class="border-bt">
										<span>{{tableData.score?tableData.score:'0'}}</span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="updatePinFen(tableData.userId,4)">修改</el-button>
									</td>
									<th class="border-bt"></th>
									<td class="border-bt border-rt">
										
									</td>
								</tr> -->
							</tbody>
						</table>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="提现记录" name="sixth">
				<el-table v-loading="tableDataLoading" :data="withdrawData.list">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates(scope.row)">{{scope.row.zhifubao ? scope.row.zhifubao : 'null'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{scope.row.zhifubaoName ? scope.row.zhifubaoName : 'null'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="classify" label="提现方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.classify == 1">支付宝</span>
							<span v-if="scope.row.classify == 2">微信小程序</span>
							<span v-if="scope.row.classify == 3">微信公众号</span>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="提现金额" width="100">
					</el-table-column>
					<el-table-column prop="state" label="状态" width="100" fixed="right">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 0">待转账</span>
							<span v-if="scope.row.state == 1">已转账</span>
							<span v-if="scope.row.state == -1">已退款</span>
						</template>
					</el-table-column>
					<el-table-column prop="refund" label="退款原因" width="220">
					</el-table-column>
					<el-table-column prop="createAt" label="申请时间" width="170">
					</el-table-column>
					<el-table-column prop="outAt" label="转账/退款时间" width="180">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="withdrawData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="钱包明细" name="eighth">
				<el-table v-loading="tableDataLoading" :data="walletData.records">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
							<span style="color: #006600;" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="walletData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="可提现余额钱包明细" name="eighthK">
				<el-table v-loading="tableDataLoading" :data="walletData.records">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
							<span style="color: #006600;" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="walletData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="充值明细" name="seventh">
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="date"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="timeDate2">
						查询</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="rechargeData.list">
					<el-table-column prop="id" label="编号" width="50"></el-table-column>
					<el-table-column prop="orderId" label="充值订单号" width="200"></el-table-column>
					<el-table-column prop="money" label="充值金额"></el-table-column>
					<el-table-column prop="userId" label="会员编号"></el-table-column>
					<el-table-column label="分类">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 1">app微信</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 2">微信公众号</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 3">微信小程序</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 4">支付宝</span>
						</template>
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 0">待支付</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 1">支付成功</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 2">支付失败</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="170">
					</el-table-column>
					<el-table-column prop="payTime" label="支付时间" width="170">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="rechargeData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>

			<el-tab-pane label="我的足迹" name="message">
				<el-table v-loading="tableDataLoading" :data="userData.list">
					<el-table-column prop="id" label="编号"></el-table-column>
					<el-table-column prop="userName" label="发布用户名" width="120">
						<template slot-scope="scope">
							<span style="color: #f56c6c;cursor:pointer"
								@click="updates(scope.row)">{{ scope.row.userName }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="发布用户头像" width="120">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="orderNo" label="发布游戏名称" width="160">
						<template slot-scope="scope">
							<span v-if="scope.row.orderTaking">{{scope.row.orderTaking.game.gameName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="homepageImg" label="主页图" width="200">
						<template slot-scope="scope">
							　　<div v-if="scope.row.homepageImgs">
								<div v-for="(item,index) in scope.row.homepageImgs" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
							<!-- <img v-if="scope.row.homepageImg &&scope.row.homepageImg!=''" :src="scope.row.homepageImg"
								width="40" height="40" />
							<span v-else>暂无图片</span> -->

						</template>
					</el-table-column>
					<el-table-column prop="phone" label="段位" width="150">
						<template slot-scope="scope">
							<span v-if="scope.row.orderTaking">{{scope.row.orderTaking.myLevel}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="site" label="接单段位">
						<template slot-scope="scope">
							<span v-if="scope.row.orderTaking">{{scope.row.orderTaking.orderLevel}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="deliveryTime" label="价格" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.orderTaking">{{scope.row.orderTaking.money}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="userType" label="评分" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.orderTaking">{{scope.row.orderTaking.orderScore}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="commission" label="服务人数" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.orderTaking">{{scope.row.orderTaking.count}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="updateTime" label="浏览时间" width="160">
						<!-- <template slot-scope="scope">
							<span v-if="scope.row.updateTime">{{scope.row.createTime}}</span>
						</template> -->
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的访客" name="fangke">
				<el-table v-loading="tableDataLoading" :data="fangkeData.list">
					<el-table-column prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column prop="userName" label="用户名">
						<template slot-scope="scope">
							<span style="color: #f56c6c;cursor:pointer"
								@click="updates(scope.row)">{{ scope.row.userName }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<!-- <el-table-column prop="status" label="是否关注">
						<template slot-scope = "scope">
							<el-switch v-model = "scope.row.state" @change = "change(scope.row.state,scope.row.userId)" :active-value = "openValue"
									   :inactive-value = "closeValue" active-color = "#13ce66" inactive-color = "#ff4949">
							</el-switch>
						</template>
					</el-table-column> -->
					<el-table-column prop="updateTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser1(scope.row)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange7" @current-change="handleCurrentChange7"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="fangkeData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的粉丝" name="fensi">
				<el-table v-loading="tableDataLoading" :data="fensiData.list">
					<el-table-column prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column prop="userName" label="用户名">
						<template slot-scope="scope">
							<span style="color: #f56c6c;cursor:pointer"
								@click="updates(scope.row)">{{ scope.row.userName }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="status" label="是否关注" align="center">
						<template slot-scope="scope">
							<div v-if='scope.row.status==1' style="color: #0074D9;">相互关注<span
									style="color: #999;margin-left: 10px;cursor:pointer"
									@click="select(scope.row)">取消关注</span></div>
							<div v-if='scope.row.status==2' style="color: #0074D9;cursor:pointer"
								@click="select(scope.row)">关注TA</div>
						</template>
					</el-table-column>
					<el-table-column prop="updateTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" v-if='scope.row.status==1'
								:disabled="!isAuth('userList:delete')" @click="select(scope.row)">取消关注</el-button>
							<el-button size="mini" type="danger" v-if='scope.row.status==2'
								:disabled="!isAuth('userList:delete')" @click="select(scope.row)">关注TA</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange8" @current-change="handleCurrentChange8"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="fensiData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的关注" name="guanzhu">
				<el-table v-loading="tableDataLoading" :data="guanzhuData.list">
					<el-table-column prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column prop="userName" label="用户名">
						<template slot-scope="scope">
							<span style="color: #f56c6c;cursor:pointer"
								@click="updates(scope.row)">{{ scope.row.userName }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="status" label="是否关注" align="center">
						<template slot-scope="scope">
							<div v-if='scope.row.status==1' style="color: #0074D9;">相互关注</div>
							<div v-if='scope.row.status==2' style="color: #0074D9;cursor:pointer">已关注</div>
						</template>
					</el-table-column>
					<el-table-column prop="updateTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="select(scope.row)">取消关注</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange9" @current-change="handleCurrentChange9"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="guanzhuData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的订单" name="dingdan">
				<el-table v-loading="tableDataLoading" :data="dingdanData.list">
					<el-table-column prop="ordersId" label="编号" width="80"></el-table-column>
					<el-table-column prop="ordersNo" label="订单编号"></el-table-column>
					<el-table-column prop="payMoney" label="佣金" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column fixed="right" prop="state" label="状态" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.state === 0 ">待确认</span>
							<span v-if="scope.row.state === 1 ">进行中</span>
							<span v-if="scope.row.state === 2 ">已完成</span>
							<span v-if="scope.row.state === 3 ">已取消</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange10" @current-change="handleCurrentChange10"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="dingdanData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的发布" name="fabu">
				<el-table v-loading="tableDataLoading" :data="takeData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="isRecommend" label="首页推荐" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.isRecommend==0">推荐</span>
							<span v-if="scope.row.isRecommend==1">不推荐</span>
							<!-- <el-switch v-model="scope.row.isRecommend"
								@change="change2(scope.row.isRecommend,scope.row)" :active-value="openValue1"
								:disabled="!isAuth('locality:update')" :inactive-value="closeValue1"
								active-color="#13ce66" inactive-color="#ff4949">
							</el-switch> -->
						</template>
					</el-table-column>
					<el-table-column prop="userName" label="发布人">
						<template slot-scope="scope">
							　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="homepageImg" label="主页图" width="200">
						<template slot-scope="scope">
							　　
							<!-- <img v-if="scope.row.homepageImg &&scope.row.homepageImg!=''" :src="scope.row.homepageImg"
								width="40" height="40" />
							<span v-else>暂无图片</span> -->
							<div v-for="(item,index) in scope.row.homepageImgs" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="voiceIntroduce" label="语音介绍" width="350">
						<template slot-scope="scope">
							<!-- <audio v-if="scope.row.voiceIntroduce!=''&&scope.row.voiceIntroduce!=null"
								:src="scope.row.voiceIntroduce" type="audio/wav" controls="controls"></audio> -->
							<audio :id="'myAudio'+scope.row.id" v-if="scope.row.voiceIntroduce!=''&&scope.row.voiceIntroduce!=null&&scope.row.voiceIntroduce!='null'" duration
								:src="scope.row.voiceIntroduce" type="audio/wav" controls preload="metadata"></audio>
							<span v-else>暂无语音</span>
						</template>
					</el-table-column>
					<el-table-column prop="details" label="个人简介" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">{{scope.row.details}}</div>
								<div style="width: 300px;">{{scope.row.details}}</div>
							</el-popover>
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
					<el-table-column prop="count" label="接单人数" width="140">
					</el-table-column>
					<el-table-column prop="orderScore" label="评分" width="100">
					</el-table-column>
					<el-table-column prop="status" label="状态" width="100" fixed="right">
						<template slot-scope="scope">
							<span style="color: #4f9dec;" v-if="scope.row.status === 0 ">进行中</span>
							<span style="color: red;" v-if="scope.row.status === 1 ">待审核</span>
							<span style="color: #999;" v-if="scope.row.status === 2 ">已下架</span>
							<span style="color: #999;" v-if="scope.row.status === 3 ">驳回</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange5" @current-change="handleCurrentChange5"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="takeData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的接单" name="jiedan">
				<el-table v-loading="tableDataLoading" :data="jiedanData.list">
					<el-table-column prop="ordersId" label="编号" width="80"></el-table-column>
					<el-table-column prop="ordersNo" label="订单编号"></el-table-column>
					<el-table-column prop="ordersNo" label="发布人">
						<template slot-scope="scope">
							<span style="color: #f56c6c;cursor:pointer"
								@click="updates(scope.row)">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersNo" label="发布人头像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="gameName" label="游戏名称"></el-table-column>
					<el-table-column prop="gameImg" label="游戏图片">
						<template slot-scope="scope">
							<img :src="scope.row.gameImg" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="unit" label="单位"></el-table-column>
					<el-table-column prop="orderNumber" label="数量"></el-table-column>
					<el-table-column prop="payMoney" label="支付佣金" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column fixed="right" prop="state" label="状态" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.state === 0 ">待确认</span>
							<span v-if="scope.row.state === 1 ">进行中</span>
							<span v-if="scope.row.state === 2 ">已完成</span>
							<span v-if="scope.row.state === 3 ">已取消</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange6" @current-change="handleCurrentChange6"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="jiedanData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="聊天记录" name="ltjl">
				<el-table v-loading="tableDataLoading" :data="liaotianData.list">
					<el-table-column fixed prop="chatConversationId" label="id" width="80">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称" width="150">
						<template slot-scope="scope">
							<div style="color: #4f9dec;cursor: pointer;">
								{{scope.row.userName ? scope.row.userName : '未绑定'}}
							</div>
							<div style="color: #4f9dec;cursor: pointer;" @click="updates1(scope.row.byUserId)">
								{{scope.row.byUserName ? scope.row.byUserName : '未绑定'}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="picture" label="用户头像">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
								暂无图片
							</div>
							<div v-else>
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
								</el-popover>
							</div>
							<div v-if="scope.row.byAvatar == null || scope.row.byAvatar == ''">
								暂无图片
							</div>
							<div v-else style="margin-top: 5px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.byAvatar" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.byAvatar" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="最新聊天内容">
						<template slot-scope="scope">
							<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
							<div v-if="scope.row.messageType==2">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.content" alt="">
								</el-popover>
							</div>
							<div v-if="scope.row.messageType==3">
								<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
							</div>
							<div v-if="scope.row.messageType==4">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px"
:src="'https://'+domain+'/emoji/'+scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px"
:src="'https://'+domain+'/emoji/'+scope.row.content" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="updateTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed='right' prop="contentCount" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color: #4f9dec;" v-if="scope.row.contentCount==0 ">未读</span>
							<span v-if="scope.row.contentCount == 1 ">已读</span>
						</template>
					</el-table-column>
					<el-table-column fixed='right' label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="complainDetails(scope.row)">聊天内容
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange11" @current-change="handleCurrentChange11"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="liaotianData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 我的常用语 -->
			<el-tab-pane label="我的常用语" name="guanjianci">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="add">添加
				</el-button>
				<el-table v-loading="tableDataLoading" :data="guanjianciData.list">
					<el-table-column prop="id" label="编号" width="80"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="createAt" label="操作" width="180">
						<template slot-scope="scope">
							<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
								@click="updateGjc(scope.row)">编辑
							</el-button>
							<el-button style="margin-left:15px;" size="mini" type="danger" icon="document"
								@click="deletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange13" @current-change="handleCurrentChange13"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="guanjianciData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 我的团队 -->
			<el-tab-pane label="我的团队" name="tuandui">
				<div>
					<span>等级：</span>
					<el-select v-model="types" style="width:150px;margin-left: 10px;" @change="animeDat(types)">
						<el-option v-for="item in typesList" :key="item.type" :label="item.name" :value="item.type">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>
				<el-table v-loading="tableDataLoading" :data="tuanduiData.list">
					<el-table-column fixed prop="userId" label="id" width="80">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称" width="150">
						<template slot-scope="scope">
							<div style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{scope.row.userName ? scope.row.userName : '未绑定'}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<div style="margin-top: 5px;" v-if="scope.row.avatar">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
								</el-popover>
							</div>
							<div v-else><img style="width: 50px; height: 50px" src="~@/assets/img/avatar.png" alt=""
									slot="reference"></div>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="金币">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange14" @current-change="handleCurrentChange14"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tuanduiData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 一级收入 -->
			<!-- <el-tab-pane label="一级收入" name="zhishu">

				<el-table v-loading="tableDataLoading" :data="tuanduiData.list">
				  <el-table-column fixed prop="ordersId" label="id" width="80">
				  </el-table-column>
				  <el-table-column prop="nickName" label="用户昵称" width="150">
				    <template slot-scope="scope">
				      <div style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
				        {{scope.row.userName ? scope.row.userName : '未绑定'}}</div>
				    </template>
				  </el-table-column>
				  <el-table-column prop="avatar" label="用户头像">
				    <template slot-scope="scope">
				      <div style="margin-top: 5px;" v-if="scope.row.avatar">
				        <el-popover placement="top-start" title="" trigger="hover">
				          <img style="width: 50px; height: 50px" :src="scope.row.avatar" alt="" slot="reference">
				          <img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
				        </el-popover>
				      </div>
					  <div v-else><img style="width: 50px; height: 50px" src = "~@/assets/img/avatar.png" alt="" slot="reference"></div>
				    </template>
				  </el-table-column>
				  <el-table-column prop="gameName" label="游戏名称">
				  </el-table-column>
				  <el-table-column prop="ordersNo" label="订单编号"></el-table-column>
				  <el-table-column prop="payMoney" label="佣金" width="160"></el-table-column>
				  <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				  <el-table-column fixed="right" prop="state" label="状态" width="100">
				  	<template slot-scope="scope">
				  		<span v-if="scope.row.state === 0 ">待确认</span>
				  		<span v-if="scope.row.state === 1 ">进行中</span>
				  		<span v-if="scope.row.state === 2 ">已完成</span>
				  		<span v-if="scope.row.state === 3 ">已取消</span>
				  	</template>
				  </el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange15" @current-change="handleCurrentChange15"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tuanduiData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 二级收入 -->
			<!-- <el-tab-pane label="二级收入" name="feizhishu">

				<el-table v-loading="tableDataLoading" :data="tuanduiData.list">
				  <el-table-column fixed prop="ordersId" label="id" width="80">
				  </el-table-column>
				  <el-table-column prop="nickName" label="用户昵称" width="150">
				    <template slot-scope="scope">
				      <div style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
				        {{scope.row.userName ? scope.row.userName : '未绑定'}}</div>
				    </template>
				  </el-table-column>
				  <el-table-column prop="avatar" label="用户头像">
				    <template slot-scope="scope">
				      <div style="margin-top: 5px;" v-if="scope.row.avatar">
				        <el-popover placement="top-start" title="" trigger="hover">
				          <img style="width: 50px; height: 50px" :src="scope.row.avatar" alt="" slot="reference">
				          <img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
				        </el-popover>
				      </div>
				  					  <div v-else><img style="width: 50px; height: 50px" src = "~@/assets/img/avatar.png" alt="" slot="reference"></div>
				    </template>
				  </el-table-column>
				  <el-table-column prop="gameName" label="游戏名称">
				  </el-table-column>
				  <el-table-column prop="ordersNo" label="订单编号"></el-table-column>
				  <el-table-column prop="payMoney" label="佣金" width="160"></el-table-column>
				  <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				  <el-table-column fixed="right" prop="state" label="状态" width="100">
				  	<template slot-scope="scope">
				  		<span v-if="scope.row.state === 0 ">待确认</span>
				  		<span v-if="scope.row.state === 1 ">进行中</span>
				  		<span v-if="scope.row.state === 2 ">已完成</span>
				  		<span v-if="scope.row.state === 3 ">已取消</span>
				  	</template>
				  </el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange15" @current-change="handleCurrentChange15"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tuanduiData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 我的动态 -->
			<el-tab-pane label="我的动态" name="dongtai">
				<el-table v-loading="tableDataLoading" :data="dongtaiData.records">
					<el-table-column fixed prop="talkId" label="编号" width="80"></el-table-column>
					<el-table-column prop="content" label="发布内容">
					</el-table-column>
					<el-table-column prop="imgUrl" label="发布图片" width="210">
						<template slot-scope="scope">
							<div v-if="scope.row.img">
								<div v-for="(item,index) in scope.row.img.split(',')"
									style="display: inline-block;width: 60px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px;margin-bottom: 10px;" :src="item" alt=""
											slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>

						</template>
					</el-table-column>
					<el-table-column prop="commentCount" label="评论次数" width="80"></el-table-column>
					<el-table-column prop="goodTalkCount" label="点赞次数" width="80"></el-table-column>
					<el-table-column prop="shareNum" label="转发次数" width="80"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
					<el-table-column label="操作" width="150" align="center" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="talkAbout(scope.row)" style="margin: 5px;">
								评论列表</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('dynamic:delete')"
								style="margin: 5px;" @click="advertdeletes(scope.row)">删除评论</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange18" @current-change="handleCurrentChange18"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="dongtaiData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 我的点赞 -->
			<!-- <el-tab-pane label="我的点赞" name="dianzan">
			  <el-table v-loading="tableDataLoading" :data="dianzanData.list">
			    <el-table-column fixed prop="talkId" label="编号" width="80"></el-table-column>
			    <el-table-column prop="content" label="发布内容">
			    </el-table-column>
			    <el-table-column prop="imgUrl" label="发布图片" width="210">
			    	<template slot-scope="scope">
			    		<div v-for="(item,index) in scope.row.img.split(',')"
			    			style="display: inline-block;width: 60px;">
			    	<el-popover placement="top-start" title="" trigger="hover">
			    				<img style="width: 50px; height: 50px;margin-bottom: 10px;" :src="item" alt=""
			    					slot="reference">
			    				<img style="width: 300px; height: auto" :src="item" alt="">
			    			</el-popover>
			    		</div>
			    	</template>
			    </el-table-column>
			    <el-table-column prop="commentCount" label="评论次数" width="80"></el-table-column>
			    <el-table-column prop="goodTalkCount" label="点赞次数" width="80"></el-table-column>
			    <el-table-column prop="shareNum" label="转发次数" width="80"></el-table-column>
			    <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
			  </el-table>
			  <div style="text-align: center;margin-top: 10px;">
			    <el-pagination @size-change="handleSizeChange16" @current-change="handleCurrentChange16"
			      :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next"
			      :total="dianzanData.totalCount">
			    </el-pagination>
			  </div>
			</el-tab-pane> -->
			<!-- 我的评论 -->
			<!-- <el-tab-pane label="我的评论" name="pinglun">
			  <el-table v-loading="tableDataLoading" :data="pinglunData.list">
			    <el-table-column fixed prop="id" label="编号" width="80">
			    </el-table-column>
			    <el-table-column prop="content" label="积分明细">
			    </el-table-column>
			    <el-table-column label="积分" width="80">
			      <template slot-scope="scope">
			        <span style="color: #f56c6c;">{{scope.row.num}}</span>
			      </template>
			    </el-table-column>
			    <el-table-column prop="createTime" label="创建时间" width="180">
			    </el-table-column>
			  </el-table>
			  <div style="text-align: center;margin-top: 10px;">
			    <el-pagination @size-change="handleSizeChange17" @current-change="handleCurrentChange17"
			      :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next"
			      :total="pinglunData.totalCount">
			    </el-pagination>
			  </div>
			</el-tab-pane> -->
			<!-- 我的打赏 -->
			<el-tab-pane label="我的打赏" name="dashang">
				<el-table v-loading="tableDataLoading1" :data="dashangData.list">
					<el-table-column fixed prop="giftUserId" label="编号" width="50">
					</el-table-column>
					<el-table-column fixed prop="sendUserName" label="接收用户">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.sendUserId)">
								{{ scope.row.sendUserName ? scope.row.sendUserName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="sendPhone" label="接收用户手机号">
					</el-table-column>
					<el-table-column prop="giftImg" label="图片">
						<template slot-scope="scope">
							　　<img :src="scope.row.giftImg" width="60" height="60" />
						</template>
					</el-table-column>
					<el-table-column prop="giftName" label="名称">
					</el-table-column>
					<el-table-column prop="giftMoney" label="金币">
						<template slot-scope="scope">
							<span style="color: red;">{{scope.row.giftMoney}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeDs" @current-change="handleCurrentChangeDs"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="dashangData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 我的投诉 -->
			<el-tab-pane label="我的投诉" name="tousu">
				<el-table v-loading="tableDataLoading" :data="tousuData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="被投诉用户">
						<template slot-scope="scope">
							<div style="color: #0066CC;cursor: pointer;" @click="updates1(scope.row.byUserId)">
								{{scope.row.byUserName}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="投诉类型">
						<template slot-scope="scope">
							<div v-if="scope.row.platform==1">聊天投诉</div>
							<div v-if="scope.row.platform==2">用户投诉</div>
							<div v-if="scope.row.platform==3">动态投诉</div>
							<div v-if="scope.row.platform==4">技能投诉</div>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="title" label="投诉标题">
					</el-table-column> -->
					<el-table-column prop="content" label="投诉内容" width="200">
					</el-table-column>
					<el-table-column prop="image" label="图片" width="150">
						<template slot-scope="scope">
							<!-- <img :src="scope.row.goodsCover" alt="" width="60" height="60"> -->
							<div v-for="(item,index) in scope.row.image" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<div v-if="scope.row.status==0">待审核</div>
							<div v-else>已审核</div>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="处理结果">
						<template slot-scope="scope">
							<div v-if="scope.row.status==0">待处理</div>
							<div v-if="scope.row.status==1">不处理</div>
							<div v-if="scope.row.status==2">已封号</div>
							<div v-if="scope.row.status==3">已删除</div>
						</template>
					</el-table-column>
					<el-table-column prop="auditContent" label="审核内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange23" @current-change="handleCurrentChange23"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tousuData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的消息" name="xiaoxi">
				<el-table v-loading="tableDataLoading" :data="tousuData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange23" @current-change="handleCurrentChange23"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tousuData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的订单消息" name="dingdanxiaoxi">
				<el-table v-loading="tableDataLoading" :data="tousuData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange23" @current-change="handleCurrentChange23"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tousuData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的礼物" name="liwu">
				<el-table v-loading="tableDataLoading" :data="liwuData.list">
					<el-table-column fixed prop="giftId" label="编号" width="50">
					</el-table-column>
					<el-table-column fixed prop="userName" label="赠送用户">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="赠送用户手机号">
					</el-table-column>
					<el-table-column prop="giftImg" label="图片">
						<template slot-scope="scope">
							　　<img :src="scope.row.giftImg" width="60" height="60" />
						</template>
					</el-table-column>
					<el-table-column prop="giftName" label="名称">
					</el-table-column>
					<el-table-column prop="giftMoney" label="金币">
						<template slot-scope="scope">
							<span style="color: red;">{{scope.row.giftMoney}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeLw" @current-change="handleCurrentChangeLw"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="liwuData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 我的约玩记录 -->
			<el-tab-pane label="我的约玩记录" name="yuewanjilu">
				<el-table v-loading="tableDataLoading" :data="yuewanData.list">
					<el-table-column prop="fastOrderId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="userName" label="发布用户">
						<template slot-scope="scope">
							　<span style="color: #4f9dec;cursor: pointer;" @click="updates1(scope.row.userId)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="classify" label="要求性别" width="150">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>不限</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="150">
					</el-table-column>
					<!-- <el-table-column prop="serviceType" label="服务类型" width="150">
					</el-table-column> -->
					<el-table-column prop="gameName" label="游戏名称" width="150">
					</el-table-column>
					<el-table-column prop="money" label="价格区间" width="100">
					</el-table-column>
					<!-- <el-table-column prop="skill" label="技能水平" width="100">
					</el-table-column>
					<el-table-column prop="hero" label="擅长英雄" width="100">
					</el-table-column>
					<el-table-column prop="location" label="期望擅长位置" width="100">
					</el-table-column> -->
					<el-table-column prop="fastMoney" label="发布价格" width="150">
					</el-table-column>
					<el-table-column prop="myLevel" label="当前段位" width="100">
					</el-table-column>
					<el-table-column prop="mySystem" label="玩家系统" width="100">
					</el-table-column>
					<el-table-column prop="myArea" label="玩家大区" width="100">
					</el-table-column>
					<el-table-column prop="num" label="约玩局数" width="100">
					</el-table-column>
					<el-table-column prop="orderTakingTime" label="约玩时间" width="200">
					</el-table-column>
					<el-table-column prop="content" label="陪玩接单文字" width="150">
					</el-table-column>
					<el-table-column prop="voice" label="陪玩接单语音" width="350">
						<template slot-scope="scope">
							<audio v-if="scope.row.voice!=''&&scope.row.voice!=null" :src="scope.row.voice"
								type="audio/wav" controls="controls"></audio>
							<span v-else>暂无语音</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="musicStyle" label="擅长曲风" width="100">
					</el-table-column>
					<el-table-column prop="musicLevel" label="音乐水平" width="200">
					</el-table-column> -->
					<el-table-column prop="remark" label="备注" width="100">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column prop="status" label="状态" fixed="right">
						<template slot-scope="scope">
							<span style="color: #E87D0D;" v-if="scope.row.status==1">发起中</span>
							<span style="color: #009900;" v-if="scope.row.status==2">匹配成功</span>
							<span style="color: #999;" v-if="scope.row.status==3">取消约玩</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeYw" @current-change="handleCurrentChangeYw"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="yuewanData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 我的服务评论 -->
			<el-tab-pane label="我的技能评论" name="pinglunjilu">
				<el-table v-loading="tableDataLoading" :data="pinglunjiluData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="content" label="评价内容">
					</el-table-column>
					<el-table-column prop="score" label="评分">
						<template slot-scope="scope">
							<span>{{scope.row.score?scope.row.score:'暂无评分'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="评价时间" width="180">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangePljl" @current-change="handleCurrentChangePljl"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="pinglunjiluData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 我的动态评论 -->
			<el-tab-pane label="我的动态评论" name="dongtaipinglun">
				<el-table v-loading="tableDataLoading" :data="pinglunjiluData.records">
					<el-table-column prop="commentId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="content" label="评论内容">
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
					</el-table-column>

					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangedtpl" @current-change="handleCurrentChangedtpl"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="pinglunjiluData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 违规记录 -->
			<el-tab-pane label="违规记录" name="jubao">
				<el-table v-loading="tableDataLoading" :data="jubaoData.list">
					<el-table-column fixed="left" prop="roomAuditId" label="编号" width="80">
					</el-table-column>
					<el-table-column label="处罚分类" prop="auditType" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.auditType == 1">封禁直播间</span>
							<span v-if="scope.row.auditType == 2">封号</span>
							<span v-if="scope.row.auditType == 3">警告</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="roomUserName" label="被举报用户昵称" fixed="left">
						<template slot-scope="scope">
							　<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.roomUserId)"
								v-text="scope.row.roomUserName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="roomPhone" label="被举报用户电话" width="80">
					</el-table-column> -->
					<el-table-column prop="roomNo" label="房间编号" width="80">
					</el-table-column>
					<el-table-column prop="roomName" fixed="left" label="房间名称" width="120">
					</el-table-column>
					<el-table-column prop="url" label="音频地址" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">{{scope.row.url}}</div>
								<div style="width: 500px;">{{scope.row.url}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="audioText" label="音频内容" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">{{scope.row.audioText}}</div>
								<div style="width: 500px;">{{scope.row.audioText}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="auditContent" label="出发内容" width="200">
					</el-table-column>
					<el-table-column label="建议" prop="suggestion" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.suggestion == 'Block'">建议屏蔽</span>
							<span v-if="scope.row.suggestion == 'Review'">建议人工复审</span>
							<span v-if="scope.row.suggestion == 'Pass'">建议通过</span>
						</template>
					</el-table-column>
					<el-table-column label="状态" prop="status" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 1||scope.row.status==null">待处理</span>
							<span v-if="scope.row.status == 2">已处理</span>
							<span v-if="scope.row.status == 3">已忽略</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="auditTime" label="审核时间" width="160">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangedtJb" @current-change="handleCurrentChangedtJb"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="jubaoData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 我的被举报 -->
			<!-- <el-tab-pane label="我的被举报" name="beijubao">
				<el-table v-loading="tableDataLoading" :data="jubaoData.list">
					<el-table-column fixed="left" prop="roomAuditId" label="编号" width="80">
					</el-table-column>
					<el-table-column label="处罚分类" prop="auditType" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.auditType == 1">封禁直播间</span>
							<span v-if="scope.row.auditType == 2">封号</span>
							<span v-if="scope.row.auditType == 3">警告</span>
						</template>
					</el-table-column>
					<el-table-column prop="userName" label="举报用户昵称" fixed="left">
						<template slot-scope="scope">
							　<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="举报用户电话" width="80">
					</el-table-column>
					<el-table-column prop="roomNo" label="房间编号" width="80">
					</el-table-column>
					<el-table-column prop="roomName" fixed="left" label="房间名称" width="120">
					</el-table-column>
					<el-table-column prop="url" label="音频地址" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">{{scope.row.url}}</div>
								<div style="width: 500px;">{{scope.row.url}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="audioText" label="音频内容" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">{{scope.row.audioText}}</div>
								<div style="width: 500px;">{{scope.row.audioText}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="auditContent" label="出发内容" width="200">
					</el-table-column>
					<el-table-column label="建议" prop="suggestion" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.suggestion == 'Block'">建议屏蔽</span>
							<span v-if="scope.row.suggestion == 'Review'">建议人工复审</span>
							<span v-if="scope.row.suggestion == 'Pass'">建议通过</span>
						</template>
					</el-table-column>
					<el-table-column label="状态" prop="status" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 1||scope.row.status==null">待处理</span>
							<span v-if="scope.row.status == 2">已处理</span>
							<span v-if="scope.row.status == 3">已忽略</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="auditTime" label="审核时间" width="160">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangedtJb" @current-change="handleCurrentChangedtJb"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="jubaoData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 不可提现添加金额 -->
			<el-dialog title="修改金额" :visible.sync="dialogFormVisible1" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改年龄 -->
			<el-dialog title="修改年龄" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">年龄：</span>
					<el-input style="width:50%;" v-model="age" type="number" :min="0" :controls="false"
						placeholder="请输入年龄"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo4()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改手机号 -->
			<el-dialog title="修改手机号" :visible.sync="dialogFormVisible6" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
					<el-input style="width:50%;" v-model="phones" :controls="false" placeholder="请输入手机号"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible6 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo6()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改年龄 -->
			<el-dialog title="修改自动回复" :visible.sync="dialogFormVisible5" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">自动回复：</span>
					<el-input style="width:50%;" v-model="msg" type="text" placeholder="请输入自动回复"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible5 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo3()">确 定</el-button>
				</div>
			</el-dialog>

			<!-- 聊天内容 -->
			<el-dialog title="聊天内容" :visible.sync="dialogFormVisible4" center>
				<el-table v-loading="tableDataLoading1" :data="pinglunDatas.content">
					<el-table-column prop="commentId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称">
						<template slot-scope="scope">
							<div style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{scope.row.nickName ? scope.row.nickName : '未绑定'}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="imageUrl" label="用户头像">
						<template slot-scope="scope">
							<div v-if="scope.row.imageUrl == null || scope.row.imageUrl == ''">
								暂无图片
							</div>
							<div v-else>
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.imageUrl" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.imageUrl" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="信息内容" width="280">
						<!-- <template slot-scope="scope">
							<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
							<div v-if="scope.row.messageType==2">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.content" alt="">
								</el-popover>
							</div>
							<div v-if="scope.row.messageType==3">
								<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
							</div>
							<div v-if="scope.row.messageType==4">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px"
:src="'https://'+domain+'/emoji/'+scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px"
:src="'https://'+domain+'/emoji/'+scope.row.content" alt="">
								</el-popover>
							</div>
						</template> -->
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange12" @current-change="handleCurrentChange12"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next" :total="pinglunDatas.totalElements">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 聊天内容 -->
			    <el-dialog title="聊天内容" :visible.sync="dialogFormVisible44" center>
			      <div style="margin:2% 0;display: inline-block;">
			        <span>内容:</span>
			        <el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入聊天内容" v-model="content">
			        </el-input>
			      </div>
			      <div style="display: inline-block;">
			        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select1">查询</el-button>
			        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans1">重置</el-button>
			      </div>
			      <el-table v-loading="tableDataLoading1" :data="huihuaData.list">
			        <el-table-column prop="chatContentId" label="编号" width="80">
			        </el-table-column>
			        <el-table-column prop="userName" label="用户昵称">
			          <template slot-scope="scope">
			            <div style="color: #4f9dec;cursor: pointer;"
			              @click="updates(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</div>
			          </template>
			        </el-table-column>
			        <el-table-column prop="title" label="用户头像">
			          <template slot-scope="scope">
			            <div v-if="scope.row.avatar == null || scope.row.avatar == ''">
			              暂无图片
			            </div>
			            <div v-else>
			              <el-popover placement="top-start" title="" trigger="hover">
			                <img style="width: 50px; height: 50px" :src="scope.row.avatar" alt="" slot="reference">
			                <img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
			              </el-popover>
			            </div>
			          </template>
			
			        </el-table-column>
			        <el-table-column prop="content" label="信息内容" width="280">
			          <template slot-scope="scope">
			            <div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
			            <div  v-if="scope.row.messageType==2">
			              <el-popover placement="top-start" title="" trigger="hover">
			                <img style="width: 50px; height: 50px" :src="scope.row.content" alt="" slot="reference">
			                <img style="width: 200px; height: 200px" :src="scope.row.content" alt="">
			              </el-popover>
			            </div>
			            <div v-if="scope.row.messageType==3">
			              <audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
			            </div>
						<div v-if="scope.row.messageType==4">
						  <el-popover placement="top-start" title="" trigger="hover">
			<img style="width: 50px; height: 50px" :src="'https://'+domain+'/emoji/'+scope.row.content" alt="" slot="reference">
						    <img style="width: 200px; height: 200px" :src="'https://'+domain+'/emoji/'+scope.row.content" alt="">			  </el-popover>
						</div>
			
			            <!-- <div  v-if="scope.row.messageType==4">
			              <el-popover placement="top-start" title="" trigger="hover">
			<img style="width: 50px; height: 50px" :src="'https://'+domain+'/emoji/'+scope.row.content" alt="" slot="reference">
			                <img style="width: 200px; height: 200px" :src="'https://'+domain+'/emoji/'+scope.row.content" alt="">              </el-popover>
			            </div> -->
			
			          </template>
			        </el-table-column>
			        <el-table-column prop="createTime" label="创建时间" width="150">
			        </el-table-column>
					<el-table-column prop="createTime" label="操作" width="100" fixed="right">
						<template slot-scope="scope">
							<el-button size = "mini" type = "danger" :disabled = "!isAuth('userList:delete')" @click = "deleteuser(scope.row)">删除</el-button>
						</template>
					</el-table-column>
			
			      </el-table>
			      <div style="text-align: center;margin-top: 10px;">
			        <el-pagination @size-change="handleSizeChange21" @current-change="handleCurrentChange21"
			          :page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
			          layout="total,sizes, prev, pager, next" :total="huihuaData.totalCount">
			        </el-pagination>
			      </div>
			    </el-dialog>
			<!-- 添加/修改常用语 -->
			<el-dialog :title="title2" :visible.sync="dialogFormVisible3" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">常用语：</span>
					<el-input style="width:50%;" v-model="contents" type="text" placeholder="请输入常用语"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="addGuanjianci()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改比例 -->
			<el-dialog :title="titleBl" :visible.sync="dialogFormVisible9" center>
				<div style="margin-bottom: 10px;">

					<span style="width: 200px;display: inline-block;text-align: right;"
						v-if="titleBl=='公会编号'">公会编号：</span>
					<span style="width: 200px;display: inline-block;text-align: right;" v-else>比例：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						:placeholder="titleBl"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible9 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToY()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改邀请人邀请码 -->
			<el-dialog title="修改邀请人邀请码" :visible.sync="dialogFormVisible8" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请人邀请码：</span>
					<el-input style="width:50%;" v-model="phones" :controls="false" placeholder="请输入邀请人邀请码"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible8 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo7()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改邀请码 -->
			<el-dialog title="修改邀请码" :visible.sync="dialogFormVisibleYqm" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请码：</span>
					<el-input style="width:50%;" v-model="age" type="text" :controls="false" placeholder="请输入邀请码">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleYqm = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToYqm()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改认证类型 -->
			<el-dialog :title="titleLx" :visible.sync="dialogFormVisible7" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">认证类型：</span>
					<el-input style="width:50%;" v-model="msg" type="text" placeholder="请输入认证类型"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible7 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToRz()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改头像 -->
			<el-dialog title="修改头像" :visible.sync="dialogFormVisibleTx" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="phones"
							:action="$http.adornUrl('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess5">
							<img v-if="phones" :src="phones" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleTx = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToTx()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改昵称 -->
			<el-dialog title="修改昵称" :visible.sync="dialogFormVisibleName" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">昵称：</span>
					<el-input style="width:50%;" v-model="phones" type="text" placeholder="请输入昵称"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleName = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToName()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog title="修改认证信息" :visible.sync="dialogFormVisibleR" center>

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
					<el-input style="width:50%;" v-model="details" type="textarea" :rows="3" placeholder="请输入个人简介">
					</el-input>
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
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
						<el-upload class="avatar-uploader" v-model="front"
							:action="$http.adornUrl('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="front" :src="front" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">身份证反面图：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
						<el-upload class="avatar-uploader" v-model="back"
							:action="$http.adornUrl('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="back" :src="back" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
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
			<!-- 修改青少年模式密码 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleQsn" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">青少年模式密码：</span>
					<el-input style="width:50%;" v-model="contents" type="number" placeholder="请输入青少年模式密码"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleQsn = false">取 消</el-button>
					<el-button type="primary" @click="mmGuanjianci()">确 定</el-button>
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
				closeValue: 0,
				state: 0,
				limit: 10,
				page: 1,
				limit1: 10,
				page1: 1,
				userId: '',
				money: '',
				title: '',
				content: '',
				phone: '',
				startTime: '',
				endTime: '',
				invitationCode: '',
				feiDatamin: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				tablejifen: '',
				autonymData: {},
				tablemoney: {},
				tablenum: {},
				tableData: {},
				userData: {},
				fangkeData: {},
				tableDatamin: [],
				withdrawData: {},
				walletData: {},
				rechargeData: {},
				sendData: [],
				takeData: {},
				jiedanData: {},
				dingdanData: {},
				fensiData: {},
				guanzhuData: {},
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				tableDataLoading: true,
				tableDataLoading1: true,
				relation_id: '',
				relationId: '',
				tkstatus: '',
				invitation: '',
				nowMonthOver: '',
				lastMonthOver: '',
				nowMonthPaid: '',
				lastMonthPaid: '',
				nowMonthOrderNumber: '',
				nowDayOrderNumber: '',
				hasCash: '',
				search: '',
				classifyIds: 0,
				statusId: -1,
				myPhone: '',
				type: '',
				age: '',
				msg: '',
				phones: '',
				chatConversationId: '',
				contents: '',
				guanjianciId: '',
				title2: '添加关键词',
				liaotianData: {},
				huihuaData: {},
				guanjianciData: {},
				tuanduiData: {},
				userId: '',
				tousuData: {},
				dongtaiData: {},
				dianzanData: {},
				pinglunData: {},
				pinglunDatas: {},
				dashangData: {},
				guanzhuData: {},
				types: 1,
				typesList: [{
						type: 1,
						name: '一级'
					},
					{
						type: 2,
						name: '二级'
					}
				],
				states: 3,
				liwuData: {},
				yuewanData: {},
				texts: '',
				proportion: '',
				userIdss: '',
				titleBl: '修改佣金比例',
				titleLx: '修改认证类型',
				pinglunjiluData: {},
				jubaoData:{},
				dialogFormVisibleTx: false,
				dialogFormVisibleYqm: false,
				dialogFormVisibleName: false,
				// 修改信息
				birth: '',
				classifyIds: '',
				phone: '',
				name: '',
				sex: '',
				idNumber: '',
				front: '',
				back: '',
				avatar: '',
				details: '',
				userId: '',
				dialogFormVisibleR: false,
				userIds: '',
				percentage1: 0,
				percentage2: 0,
				skillImg: [],
				lifePhoto: [],
				info: {
					stockDate: this.getNowTime(), //日期
				},
				heimingdanData: {},
				titles: '',
				dialogFormVisibleQsn: false,
				domain:'',
				dialogFormVisible44: false,

			}
		},
		// watch:{
		// 	userId(name){
		// 		this.userId = this.$route.query.userId
		// 		console.log('this.userId',this.userId)
		// 	}
		// },
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
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
			updates1(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
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
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '用户详情') {
					this.dataSelect()
					// this.autonymSelect()
				}

				if (tab._props.label == '提现记录') {
					this.limit = 10,
						this.page = 1,
						this.withdrawSelect()
				}
				if (tab._props.label == '钱包明细') {
					this.limit = 10
					this.page = 1
					this.types = ''
					this.walletSelect()
				}
				if (tab._props.label == '可提现余额钱包明细') {
					this.limit = 10
					this.page = 1
					this.types = 10
					this.walletSelect()
				}
				if (tab._props.label == '充值明细') {
					this.limit = 10
					this.page = 1
					this.rechargeSelect()
				}
				if (tab._props.label == '我的接单') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.jiedanSelect()
				}
				if (tab._props.label == '我的发布') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.takeSelect()
				}

				if (tab._props.label == '我的订单') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.dingdanSelect()
				}
				if (tab._props.label == '我的足迹') {
					this.limit = 10
					this.page = 1
					this.state = 8
					this.flag = 1
					this.userSelect()
				}
				if (tab._props.label == '我的粉丝') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.fensiSelect()
				}
				if (tab._props.label == '我的关注') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.guanzhuSelect()
				}
				if (tab._props.label == '我的访客') {
					this.limit = 10
					this.page = 1
					this.state = 8
					this.flag = 1
					this.fangkeSelect()
				}
				if (tab._props.label == '聊天记录') {
					this.limit = 10
					this.page = 1
					this.liaotianSelect()
				}
				if (tab._props.label == '我的常用语') {
					this.limit = 10
					this.page = 1
					this.guanjianciSelect()
				}
				if (tab._props.label == '我的团队') {
					this.limit = 10
					this.page = 1
					this.types = 1
					this.tuanduiSelect()
				}
				if (tab._props.label == '一级收入') {
					this.limit = 10
					this.page = 1
					this.types = 1
					this.tuanduiSelect1()
				}
				if (tab._props.label == '二级收入') {
					this.limit = 10
					this.page = 1
					this.types = 2
					this.tuanduiSelect1()
				}

				if (tab._props.label == '实名认证') {
					this.autonymSelect()
				}
				if (tab._props.label == '我的投诉') {
					this.limit = 10
					this.page = 1
					this.states = 3
					this.tousuSelect()
				}
				if (tab._props.label == '我的消息') {
					this.limit = 10
					this.page = 1
					this.states = 5
					this.tousuSelect()
				}
				if (tab._props.label == '我的订单消息') {
					this.limit = 10
					this.page = 1
					this.states = 4
					this.tousuSelect()
				}

				if (tab._props.label == '我的动态') {
					this.limit = 10,
						this.page = 1,
						this.dongtaiSelect()
				}
				if (tab._props.label == '我的点赞') {
					this.limit = 10,
						this.page = 1,
						this.dianzanSelect()
				}
				if (tab._props.label == '我的评论') {
					this.limit = 10,
						this.page = 1,
						this.pinglunSelect()
				}
				if (tab._props.label == '我的打赏') {
					this.limit = 10,
						this.page = 1,
						this.dashangSelect()
				}
				if (tab._props.label == '我的优惠券') {
					this.limit = 10,
						this.page = 1,
						this.yhqSelect()
				}
				if (tab._props.label == '我的礼物') {
					this.limit = 10,
						this.page = 1,
						this.lySelect()
				}
				if (tab._props.label == '我的约玩记录') {
					this.limit = 10
					this.page = 1
					this.yuewanSelect()
				}
				if (tab._props.label == '我的技能评论') {
					this.limit = 10
					this.page = 1
					this.pinglunjiluSelect()
				}
				if (tab._props.label == '我的动态评论') {
					this.limit = 10
					this.page = 1
					this.dongtaiplSelect()
				}
				if (tab._props.label == '违规记录') {
					this.limit = 10
					this.page = 1
					this.types = 1
					this.jubaoSelect()
				}
				if (tab._props.label == '我的被举报') {
					this.limit = 10
					this.page = 1
					this.types = 2
					this.jubaoSelect()
				}
				
			},
			handleSizeChange(val) {
				this.limit = val
				this.teamSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.teamSelect()
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
				this.limit = val
				this.withdrawSelect()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.withdrawSelect()
			},
			handleSizeChange3(val) {
				this.limit = val
				this.walletSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.walletSelect()
			},
			handleSizeChange4(val) {
				this.limit = val
				this.rechargeSelect()
			},
			handleCurrentChange4(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleSizeChange5(val) {
				this.limit = val
				this.sendSelect()
			},
			handleCurrentChange5(val) {
				this.page = val
				this.sendSelect()
			},
			handleSizeChange6(val) {
				this.limit = val
				this.jiedanSelect()
			},
			handleCurrentChange6(val) {
				this.page = val
				this.jiedanSelect()
			},
			handleSizeChange10(val) {
				this.limit = val
				this.dingdanSelect()
			},
			handleCurrentChange10(val) {
				this.page = val
				this.dingdanSelect()
			},
			handleSizeChange7(val) {
				this.limit = val
				this.feiSelect()
			},
			handleCurrentChange7(val) {
				this.page = val
				this.feiSelect()
			},
			handleSizeChange8(val) {
				this.limit = val
				this.fensiSelect()
			},
			handleCurrentChange8(val) {
				this.page = val
				this.fensiSelect()
			},
			handleSizeChange9(val) {
				this.limit = val
				this.guanzhuSelect()
			},
			handleCurrentChange9(val) {
				this.page = val
				this.guanzhuSelect()
			},
			handleSizeChange11(val) {
				this.limit = val
				this.liaotianSelect()
			},
			handleCurrentChange11(val) {
				this.page = val
				this.liaotianSelect()
			},
			handleSizeChange12(val) {
				this.limit1 = val
				this.pinglunSelects(this.talkId)
			},
			handleCurrentChange12(val) {
				this.page1 = val
				this.pinglunSelects(this.talkId)
			},
			handleSizeChange21(val) {
				this.limit1 = val
				this.neirongSelect(this.chatConversationId)
			},
			handleCurrentChange21(val) {
				this.page1 = val
				this.neirongSelect(this.chatConversationId)
			},
			handleSizeChange13(val) {
				this.limit = val
				this.guanjianciSelect()
			},
			handleCurrentChange13(val) {
				this.page = val
				this.guanjianciSelect()
			},
			handleSizeChange14(val) {
				this.limit = val
				this.tuanduiSelect()
			},
			handleCurrentChange14(val) {
				this.page = val
				this.tuanduiSelect()
			},
			handleSizeChange15(val) {
				this.limit = val
				this.tuanduiSelect1()
			},
			handleCurrentChange15(val) {
				this.page = val
				this.tuanduiSelect1()
			},

			handleSizeChange23(val) {
				this.limit = val
				this.tousuSelect()
			},
			handleCurrentChange23(val) {
				this.page = val
				this.tousuSelect()
			},
			handleSizeChange18(val) {
				this.limit = val
				this.dongtaiSelect()
			},
			handleCurrentChange18(val) {
				this.page = val
				this.dongtaiSelect()
			},
			handleSizeChange16(val) {
				this.limit = val
				this.dianzanSelect()
			},
			handleCurrentChange16(val) {
				this.page = val
				this.dianzanSelect()
			},
			handleSizeChange17(val) {
				this.limit = val
				this.pinglunSelect()
			},
			handleCurrentChange17(val) {
				this.page = val
				this.pinglunSelect()
			},
			handleSizeChangeDs(val) {
				this.limit = val
				this.dashangSelect()
			},
			handleCurrentChangeDs(val) {
				this.page = val
				this.dashangSelect()
			},
			handleSizeChangeLw(val) {
				this.limit = val
				this.lySelect()
			},
			handleCurrentChangeLw(val) {
				this.page = val
				this.lySelect()
			},
			handleSizeChangeYw(val) {
				this.limit = val
				this.yuewanSelect()
			},
			handleCurrentChangeYw(val) {
				this.page = val
				this.yuewanSelect()
			},
			handleSizeChangePljl(val) {
				this.limit = val
				this.pinglunjiluSelect()
			},
			handleCurrentChangePljl(val) {
				this.page = val
				this.pinglunjiluSelect()
			},
			handleSizeChangedtpl(val) {
				this.limit = val
				this.dongtaiplSelect()
			},
			handleCurrentChangedtpl(val) {
				this.page = val
				this.dongtaiplSelect()
			},
			
			handleSizeChangedtJb(val) {
				this.limit = val
				this.jubaoSelect()
			},
			handleCurrentChangedtJb(val) {
				this.page = val
				this.jubaoSelect()
			},
			timeDate2() {
				this.rechargeSelect()
			},
			// 可提现金额
			withdraw(id, val) {
				if (val == 0) {
					this.$message({
						message: '可提现金额为0元',
						type: 'error',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				} else {
					this.$confirm(`确定要推送提现消息吗?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl(`user/notification/${id}`),
							method: 'post',
							data: this.$http.adornData({})
						}).then(({
							data
						}) => {
							this.$message({
								message: '推送成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						})
					}).catch(() => {})
				}

			},
			// 不可提现金额充值
			rechargenone(index) {
				this.types = index
				this.dialogFormVisible1 = true
			},
			StairNoticeTo1() {
				let userId = this.$route.query.userId
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改类型',
						type: 'warning'
					})
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				if(this.types == 2 ){
					this.$http({
						url: this.$http.adornUrl(`user/addCannotMoney/${userId}/${this.money}/${this.type}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.type = ''
									this.money = ''
									this.dataSelect()
								}
							})
							this.dialogFormVisible1 = false
						} else {
							this.$message.error(data.msg)
						}
					
					})
				}else {
					this.$http({
						url: this.$http.adornUrl('user/updateEarningsMoney'),
						method: 'post',
						params: this.$http.adornParams({
							'userId':userId,
							'money':this.money,
							'type':this.type
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.type = ''
									this.money = ''
									this.dataSelect()
								}
							})
							this.dialogFormVisible1 = false
						} else {
							this.$message.error(data.msg)
						}
					
					})
				}
				
			},
			StairNoticeTo4() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'age': this.age
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
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
			// 更改状态
			stateChange(userId) {
				this.$confirm(`确定要更改用户状态吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
						method: 'get',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 修改性别
			sexChange(userId, sex) {
				var sexs = 1;
				if (sex == 1) {
					sexs = 2
				}
				if (sex == 2) {
					sexs = 1
				}
				this.$confirm(`确定要更改用户性别吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'sex': sexs
						})
					}).then(({
						data
					}) => {
						console.log('data', data)
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			ageChange(userId, age) {
				this.age = age
				this.dialogFormVisible2 = true
			},
			// 获取数据列表
			dataSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl(`user/${userId}`),
					method: 'get',
					params: this.$http.adornParams({
						// 'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data
						this.tablenum = returnData
						this.tablemoney = returnData
						this.tableData = returnData.userEntity
						this.phone = returnData.userEntity.phone
						this.invitationCode = returnData.userEntity.invitationCode
					}
				})
			},
			// 获取团队成员
			teamSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/selectInviteByUserIdLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableDatamin = returnData
				})
			},
			// 非直属
			feiSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectInvitationCodeByUserIdLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'invitationCode': this.invitationCode
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.feiDatamin = returnData
				})
			},
			// 我的足迹数据
			userSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userBrowse/myBrowse'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].orderTaking.homepageImg != '') {
							data.data.list[i].homepageImgs = data.data.list[i].orderTaking.homepageImg.split(',')
						}

					}
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 获取用户提现记录
			withdrawSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('/cash/selectPayDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			// 获取钱包明细
			walletSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('Details/queryUserMoneyDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'classify':this.types,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.walletData = returnData
				})
			},
			// 获取充值明细
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRechargeByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'endTime': this.endTime,
						'startTime': this.startTime,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {

						let returnData = data.data
						this.rechargeData = returnData
					}
					if (data.code == 500) {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 2500,
							onClose: () => {
								this.tableDataLoading = false
							}
						})
					}
				})
			},
			// 获取接单
			sendSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('help/selectRunHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': this.state,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.sendData = returnData
				})
			},
			// 我的发布
			takeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('orderTaking/queryAllOrderTaking'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].homepageImg) {
							data.data.list[i].homepageImgs = data.data.list[i].homepageImg.split(',')
						}

					}
					let returnData = data.data
					this.takeData = returnData
				})
			},
			// 我的接单
			jiedanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('orders/selectMyTakeOrders'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.jiedanData = returnData
				})
			},
			// 我的订单
			dingdanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('orders/queryOrders'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.dingdanData = returnData
				})
			},
			// 实名认证
			autonymSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('student/selectStudentByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.autonymData = returnData
				})
			},
			// 信息数据
			InformationSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl(`information/selectInformationList`),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
						'page': this.page,
						'limit': this.limit,
						'search': this.search,
						'classify': this.classifyIds,
						'status': this.statusId,
						'phone': this.myPhone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false

					for (var i in data.data.records) {
						if (data.data.records[i].img) {
							data.data.records[i].imgs = data.data.records[i].img.split(',')
						}

					}
					let returnData = data.data
					this.userData = returnData


				})
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.status = -1
				this.classify = 0
				this.phone = ''
				this.search = ''
				this.page = 1
				this.InformationSelect()
			},
			// 我的访客
			fangkeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userBrowse/myVisitor'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fangkeData = returnData
				})
			},
			// 我的粉丝
			fensiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userFollow/selectFans'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fensiData = returnData
				})
			},
			// 我的关注
			guanzhuSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userFollow/selectMyFollow'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.guanzhuData = returnData
				})
			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStateById?userId=${userId}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.fangkeSelect()
						}
					})
				})
			},
			//删除足迹
			deleteuser(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('userBrowse/deleteMyBrowse?id=' + delid),
						method: 'post',
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.userSelect()
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
			// 删除我的访客
			deleteuser1(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('userBrowse/deleteMyVisitor?id=' + delid),
						method: 'post',
						params: this.$http.adornData({
							'id': delid
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
									this.fangkeSelect()
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
			// 关注/取关
			select(row) {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userFollow/insert?userId=' + userId + '&followUserId=' + row.userId),
					method: 'post',
					params: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.fensiSelect()
								this.guanzhuSelect()
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
			// 我的聊天会话
			liaotianSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.liaotianData = returnData;
				})
			},
			// 聊天内容
			complainDetails(row) {
				this.chatConversationId = row.chatConversationId
				this.neirongSelect(this.chatConversationId)
				this.dialogFormVisible44 = true
			},
			select1(){
				this.page1 = 1
				this.neirongSelect(this.chatConversationId)
			},
			cleans1(){
				this.content = ''
				this.page1 = 1
				this.neirongSelect(this.chatConversationId)
			},
			// 获取聊天内容列表
			neirongSelect(chatConversationId) {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('chat/selectChatContent'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'chatConversationId': this.chatConversationId,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.huihuaData = returnData
					console.log('this.huihuaData ', this.huihuaData)
				})
			},
			// 我的常用语
			guanjianciSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/page/20/' + this.page + '/' + this.limit),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.guanjianciData = returnData
				})
			},
			// 添加关键词
			add() {
				this.title2 = '添加关键词'
				this.contents = ''
				this.guanjianciId = ''
				this.dialogFormVisible3 = true
			},
			// 修改关键词
			updateGjc(row) {
				this.title2 = '修改关键词'
				this.contents = row.content
				this.guanjianciId = row.id
				this.dialogFormVisible3 = true
			},
			addGuanjianci() {
				var datas = {}
				let userId = this.$route.query.userId
				if (this.guanjianciId == '') {
					var url = 'message/add'
					datas.state = 20
					datas.userId = userId
					datas.content = this.contents
				}
				if (this.guanjianciId != '') {
					var url = 'message/update'
					datas.state = 20
					datas.userId = userId
					datas.content = this.contents
					datas.id = this.guanjianciId
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible3 = false
								this.guanjianciSelect()
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
			deletes(row) {
				let userId = this.$route.query.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`message/delete/${row.id}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.guanjianciSelect()
							}
						})
					})
				})
			},
			// 修改自动回复
			msgChange(userId, msg) {
				this.msg = msg
				this.dialogFormVisible5 = true
			},
			// 更改自动回复
			StairNoticeTo3() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'msg': this.msg
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dialogFormVisible5 = false
							this.dataSelect()
						}
					})
				})
			},
			// 我的tuandui
			tuanduiSelect() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectTeamUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tuanduiData = returnData
				})
			},
			// 等级选择事件
			animeDat(state) {
				this.page = 1
				this.tuanduiSelect()
			},
			// 我的团队收入
			tuanduiSelect1() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectTeamOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tuanduiData = returnData
				})
			},
			// 我的投诉
			tousuSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.states,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].image) {
							data.data.list[i].image = data.data.list[i].image.split(',')
						}

					}
					let returnData = data.data;
					this.tousuData = returnData;
				})
			},
			// 我的动态
			dongtaiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('gambit/selectTalkPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.limit,
						'userId': userId,
						'status': '',
						'byUserId': '',
						'followUserId': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dongtaiData = returnData.page;
				})
			},
			// 谈论列表
			talkAbout(row) {
				this.talkId = row.talkId
				this.pinglunSelects(row.talkId)
				this.dialogFormVisible4 = true
			},
			// 获取动态评论数据
			pinglunSelects(talkId) {
				let gambitId = this.$route.query.gambitId
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('gambit/selectCommentPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.limit,
						'talkId': talkId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.pinglunDatas = returnData
				})
			},
			// 删除动态
			advertdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteTalkById`),
						method: 'post',
						params: this.$http.adornParams({
							'talkId': row.talkId
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dongtaiSelect()
							}
						})
					})
				})
			},
			// 删除评论
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
								this.pinglunSelects(this.talkId)
							}
						})
					})
				})
			},
			// 我的点赞
			dianzanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dianzanData = returnData;
				})
			},
			// 我的评论
			pinglunSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.pinglunData = returnData;
				})
			},
			// 我的打赏
			dashangSelect() {
				this.tableDataLoading1 = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('gift/selectGiftUser'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.dashangData = returnData;
				})
			},
			// 我的礼物
			lySelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('gift/selectGiftUser'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'sendUserId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.liwuData = returnData;
				})
			},
			// 我的优惠券
			yhqSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/userList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.limit,
						'userId': userId,
						'type': 0
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dashangData = returnData;
				})
			},
			// 修改手机号
			phoneChange(userId, phone) {
				this.phones = phone
				this.dialogFormVisible6 = true
			},
			StairNoticeTo6() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'phone': this.phones
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible6 = false
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
			// 我的约玩记录
			yuewanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('fastOrder/selectFastOrderPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'status': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.yuewanData = returnData;
				})
			},
			// 修改邀请人邀请码
			inviterCodeChange(userId, inviterCode) {
				this.phones = inviterCode
				this.dialogFormVisible8 = true
			},
			StairNoticeTo7() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserIntInviterCode'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': userId,
						'inviterCode': this.phones
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible8 = false
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
			xiugai(rate, text) {
				this.titleBl = '修改佣金比例'
				if (text == 'rate') {
					this.proportion = rate.rate
				}
				if (text == 'zhiRate') {
					this.proportion = rate.proportion
				}
				if (text == 'feiRate') {
					this.proportion = rate.proportion
				}
				if (text == 'consortiaOrdersRate') {
					this.titleBl = '公会订单抽成比例'
					this.proportion = rate.consortiaOrdersRate
				}
				if (text == 'consortiaGiftRate') {
					this.titleBl = '公会礼物抽成比例'
					this.proportion = rate.consortiaGiftRate
				}
				if (text == 'consortiaNo') {
					this.titleBl = '公会编号'
					this.proportion = rate.consortiaNo
				}
				this.texts = text
				this.userIdss = this.$route.query.userId
				this.dialogFormVisible9 = true
			},
			StairNoticeToY() {
				var datas = {}
				if (this.texts == 'rate') {
					datas.rate = this.proportion
				}
				if (this.texts == 'zhiRate') {
					datas.zhiRate = this.proportion
				}
				if (this.texts == 'feiRate') {
					datas.feiRate = this.proportion
				}
				if (this.texts == 'consortiaOrdersRate') {
					datas.consortiaOrdersRate = this.proportion
				}
				if (this.texts == 'consortiaGiftRate') {
					datas.consortiaGiftRate = this.proportion
				}
				if (this.texts == 'consortiaNo') {
					datas.consortiaNo = this.proportion
					datas.userId = this.userIdss
					this.$http({
						url: this.$http.adornUrl('consortia/updateUserConsortia'),
						method: 'post',
						params: this.$http.adornParams(datas)
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dialogFormVisible9 = false
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
				} else {
					datas.userId = this.userIdss
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData(datas)
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dialogFormVisible9 = false
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
				}
			},
			// 我的技能评论
			pinglunjiluSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('/takingComment/selectOrderTakingComment'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.pinglunjiluData = returnData;
				})
			},
			// 我的动态评论
			dongtaiplSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('app/gambit/selectCommentPageAdmin'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.pinglunjiluData = returnData;
				})
			},
			// 违规记录
			jubaoSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				var datas = {
					'page': this.page,
					'limit': this.limit,
					'userName': '',
					'roomName': '',
					'status': '',
					'roomNo':'',
					'roomUserName':'',
					'roomPhone':'',
					'phone':'',
				}
				if(this.types==1){
					datas.userId = userId
				}else{
					datas.roomUserId = userId
				}
				this.$http({
					url: this.$http.adornUrl('roomAudit/selectRoomAuditList'),
					method: 'get',
					params: this.$http.adornParams(datas)
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.jubaoData = returnData;
				})
			},
			
			handleAvatarSuccess5(file, fileList) {
				this.phones = file.data
			},
			// 修改头像
			avatarChange(userId, avatar) {
				this.phones = avatar
				this.dialogFormVisibleTx = true
			},
			StairNoticeToTx() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'avatar': this.phones
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleTx = false
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
			// 修改昵称
			nameChange(userId, userName) {
				this.phones = userName
				this.dialogFormVisibleName = true
			},
			StairNoticeToName() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'userName': this.phones
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleName = false
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
			// 修改邀请码弹框
			yqmChange(userId, age) {
				this.age = age
				this.dialogFormVisibleYqm = true
			},
			// 修改邀请码
			StairNoticeToYqm() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'invitationCode': this.age
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleYqm = false
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
			// 修改实名状态
			shimingChange(userId, isAuthentication, ) {

				if (isAuthentication == 1) {
					this.$confirm(`确定要更改实名状态吗?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl('user/updateUserIsAuthentication'),
							method: 'post',
							data: this.$http.adornData({
								'userId': userId,
							})
						}).then(({
							data
						}) => {
							console.log('data', data)
							if (data.code == 0) {
								this.$message({
									message: '修改成功',
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
				} else {
					this.birth = ''
					this.ids = ''
					this.classifyIds = ''
					this.name = ''
					this.sex = ''
					this.details = ''
					this.percentage1 = 0
					this.percentage2 = 0
					this.skillImg = []
					this.lifePhoto = []
					this.phone = ''
					this.idNumber = ''
					this.front = ''
					this.avatar = ''
					this.back = ''
					this.userId = userId
					this.dialogFormVisibleR = true
				}

			},
			// 确认修改
			refusetoUp() {

				this.$http({
					url: this.$http.adornUrl('user/updateUserIsAuthentication'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.userId,
						// 'classifyId': this.classifyIds,
						'name': this.name,
						// 'sex':this.sex,
						// 'address':this.address,
						'lifePhoto': this.lifePhoto.toString(),
						'skillImg': this.skillImg.toString(),
						// 'phone':this.phone,
						'details': this.details,
						'idNumber': this.idNumber,
						'front': this.front,
						// 'avatar':this.avatar,
						'back': this.back,
						'birth': this.birth,
						'id': this.ids
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
			// handleAvatarSuccess5(file, fileList) {
			// 	this.avatar = file.data
			// },
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
			// 修改青少年模式密码
			changeQsnMm(row, index) {
				this.contents = ''
				if (index == 1) {
					let userId = this.$route.query.userId
					this.titles = '关闭青少年模式'
					this.$confirm(`确定关闭青少年模式?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl('user/closeBaby'),
							method: 'post',
							params: this.$http.adornParams({
								'userId': userId,
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
				}
				if (index == 2) {
					this.titles = '开启青少年模式'
					this.dialogFormVisibleQsn = true
				}
				if (index == 3) {
					this.titles = '修改青少年模式密码'
					this.dialogFormVisibleQsn = true
				}
			
			
			},
			// 确认修改青少年模式密码
			mmGuanjianci() {
				let userId = this.$route.query.userId
				if (this.contents == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入密码',
						type: 'warning'
					});
					return
				}
			
				if (this.titles == '修改青少年模式密码') {
					if (this.contents >= 10000) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入四位有效数字',
							type: 'warning'
						});
						return
					}
					var url = 'user/updateBabyPwd'
				}
				if (this.titles == '开启青少年模式') {
					var url = 'user/setBabyPwd'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					params: this.$http.adornParams({
						'userId': userId,
						'babyPwd': this.contents
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
								this.dialogFormVisibleQsn = false
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
		},
		mounted() {
			this.domain = window.location.hostname;
			this.dataSelect()
		},
	}
</script>

<style scoped="scoped">
	.detailtabel h2 {
		margin-top: 0;
	}

	.detailtabel .table_main table {
		width: 100%;
	}

	.detailtabel .table_main {
		border: 1px solid #e8e8e8;
	}

	.detailtabel table tr {
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table tr th {
		background-color: #fafafa;
		padding: 16px 24px;
		border-right: 1px solid #e8e8e8;
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table tr td {
		padding: 16px 24px;
		border-right: 1px solid #e8e8e8;
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table th {
		color: rgba(0, 0, 0, .85);
		font-weight: 400;
		font-size: 14px;
		line-height: 1.5;
	}

	.border-rt {
		border-right: none !important;
	}

	.border-bt {
		border-bottom: none !important;
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
</style>
