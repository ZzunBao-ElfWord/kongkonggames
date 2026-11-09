<template>
	<div>
		<div class="record_date">
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">总用户数</div>
							<div class="text_color"><span>{{tableData.totalUsers ? tableData.totalUsers : 0}}</span>人
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">今日新增</div>
							<div class="text_color"><span>{{tableData.newToday ? tableData.newToday : 0}}</span>人</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本月新增</div>
							<div class="text_color"><span>{{tableData.newMonth ? tableData.newMonth : 0}}</span>人</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本年新增</div>
							<div class="text_color"><span>{{tableData.newYear ? tableData.newYear : 0}}</span>人</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">总充值 </div>
							<div class="text_color">
								<span>{{tableData.totalRevenue ? tableData.totalRevenue : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">今日充值</div>
							<div class="text_color">
								<span>{{tableData.todayRevenue ? tableData.todayRevenue : 0 }}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本月充值 </div>
							<div class="text_color">
								<span>{{tableData.monthRevenue ? tableData.monthRevenue : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本年充值</div>
							<div class="text_color"><span>{{tableData.yearRevenue ? tableData.yearRevenue : 0}}</span>元
							</div>
						</div>
					</div>
				</el-col>

			</el-row>
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="orderfenxi">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeOrder">
				</el-date-picker>
			</div>
			<el-tabs v-model="activeName" @tab-click="handleClick" style="margin-left: 10px;">
				<el-tab-pane label="订单分析" name="first">
					<el-row>
						
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总订单数量</div>
									<div class="text_color">
										<span>{{taskStat.ordersSumCount +taskStat.yueSumCount}}</span>单</div>
								</div>
							</div>
						</el-col>
						
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">约玩订单总数量</div>
									<div class="text_color">
										<span>{{taskStat.yueSumCount ? taskStat.yueSumCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">技能订单总数量</div>
									<div class="text_color">
										<span>{{taskStat.ordersSumCount ? taskStat.ordersSumCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待支付约玩订单数量</div>
									<div class="text_color">
										<span>{{taskStat.yueDZFCount ? taskStat.yueDZFCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待服务约玩订单数量</div>
									<div class="text_color">
										<span>{{taskStat.yueDFWCount ? taskStat.yueDFWCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">进行中约玩订单数量</div>
									<div class="text_color">
										<span>{{taskStat.yueJXZCount ? taskStat.yueJXZCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已完成约玩订单数量</div>
									<div class="text_color">
										<span>{{taskStat.yueYWCCount ? taskStat.yueYWCCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已结束约玩订单数量</div>
									<div class="text_color">
										<span>{{taskStat.yueJSZCount ? taskStat.yueJSZCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col> -->
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已退款约玩订单数量</div>
									<div class="text_color">
										<span>{{taskStat.yueYTKCount ? taskStat.yueYTKCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待支付技能订单数量</div>
									<div class="text_color">
										<span>{{taskStat.ordersDZFCount ? taskStat.ordersDZFCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待服务技能订单数量</div>
									<div class="text_color">
										<span>{{taskStat.ordersDFWCount ? taskStat.ordersDFWCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">进行中技能订单数量</div>
									<div class="text_color">
										<span>{{taskStat.ordersJXZCount ? taskStat.ordersJXZCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已完成技能订单数量</div>
									<div class="text_color">
										<span>{{taskStat.ordersYWCCount ? taskStat.ordersYWCCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已结束技能订单数量</div>
									<div class="text_color">
										<span>{{taskStat.ordersJSZCount ? taskStat.ordersJSZCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col> -->
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已退款技能订单数量</div>
									<div class="text_color">
										<span>{{taskStat.ordersYTKCount ? taskStat.ordersYTKCount : 0}}</span>单</div>
								</div>
							</div>
						</el-col>
						
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="收益统计" name="five">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总收益</div>
									<div class="text_color"><span>{{shouyiData.sumMoney ? shouyiData.sumMoney : 0 }}</span>元</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">平台总收益</div>
									<div class="text_color"><span>{{shouyiData.pingMoney ? shouyiData.pingMoney : 0 }}</span>元</div>
								</div>
							</div>
						</el-col>
						
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">礼物收益</div>
									<div class="text_color"><span>{{shouyiData.sumGiftMoney ? shouyiData.sumGiftMoney : 0}}</span>元</div>
								</div>
							</div>
						</el-col> 
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">订单收益</div>
									<div class="text_color"><span>{{shouyiData.sumOrdersMoney ? shouyiData.sumOrdersMoney : 0}}</span>元</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">平台礼物收益</div>
									<div class="text_color"><span>{{shouyiData.pingGiftMoney ? shouyiData.pingGiftMoney : 0}}</span>元</div>
								</div>
							</div>
						</el-col> 
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">平台订单收益</div>
									<div class="text_color"><span>{{shouyiData.pingOrdersMoney ? shouyiData.pingOrdersMoney : 0}}</span>元</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="收益收入分析" name="fourth">
					<el-table v-loading="tableDataLoading" :data="tableData2.list">
						<el-table-column prop="orderId" label="编号">
						</el-table-column>
						<el-table-column prop="nickName1" label="用户名">
							<template slot-scope="scope">
								<div>
									<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary" :disabled="!isAuth('userList:viewDetails')" @click="updates1(scope.row.userId)">
										{{scope.row.userName ? scope.row.userName : '未绑定'}}
									</el-button>
								</div>
								<!-- <span style="color: #4f9dec;cursor: pointer;"
									@click="updates1(scope.row.userId)">{{scope.row.userName ? scope.row.userName : 'null'}}</span> -->
							</template>
						</el-table-column>
						<el-table-column prop="payMoney" label="支付金额">
						</el-table-column>
						<el-table-column prop="rate" label="陪玩官收入">
						</el-table-column>
						<el-table-column prop="zhiRate" label="一级收入">
							<template slot-scope="scope">
								<!-- <span style="color: #4f9dec;cursor: pointer;"
									@click="updates1(scope.row.zhiUserId)">{{scope.row.zhiRate ? scope.row.zhiRate : '0'}}</span> -->
									<div>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;" type="primary" :disabled="!isAuth('userList:viewDetails')" @click="updates1(scope.row.zhiUserId)">
											{{scope.row.zhiRate ? scope.row.zhiRate : '0'}}
										</el-button>
									</div>
							</template>
						</el-table-column>
						<el-table-column prop="pingRate" label="平台收入">
							<!-- <template slot-scope="scope">
								{{ parseFloat(scope.row.payMoney-scope.row.money).toFixed(2)}}
							</template> -->
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间">
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;float:right">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData2.totalCount">
						</el-pagination>
					</div>
				</el-tab-pane>
				<el-tab-pane label="用户分析" name="third">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总用户统计</div>
									<div class="text_color"><span>{{userStat.userCount ? userStat.userCount : 0 }}</span>人</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">手机号用户</div>
									<div class="text_color"><span>{{userStat.userPhoneCount ? userStat.userPhoneCount : 0}}</span>人</div>
								</div>
							</div>
						</el-col>
						
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">APP渠道用户统计</div>
									<div class="text_color"><span>{{userStat.appCount ? userStat.appCount : 0}}</span>人</div>
								</div>
							</div>
						</el-col> 
						
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">小程序渠道用户统计</div>
									<div class="text_color"><span>{{userStat.wxCount ? userStat.wxCount : 0}}</span>人</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">QQ渠道用户统计</div>
									<div class="text_color"><span>{{userStat.qq ? userStat.qq : 0}}</span>人</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信渠道用户统计</div>
									<div class="text_color"><span>{{userStat.wx ? userStat.wx : 0}}</span>人</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="充值统计" name="sixth">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">充值总金额</div>
									<div class="text_color"><span>{{rechgeData.sumMoney ? rechgeData.sumMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">小程序充值金额</div>
									<div class="text_color">
										<span>{{rechgeData.weiXinXCXMoney ? rechgeData.weiXinXCXMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">APP充值金额</div>
									<div class="text_color">
										<span>{{rechgeData.appMoney ? rechgeData.appMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信充值金额</div>
									<div class="text_color">
										<span>{{rechgeData.weiXinMoney ? rechgeData.weiXinMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付宝充值金额</div>
									<div class="text_color">
										<span>{{rechgeData.zhiFuBaoMoney ? rechgeData.zhiFuBaoMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">系统充值</div>
									<div class="text_color">
										<span>{{rechgeData.xiTongMoney ? rechgeData.xiTongMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">退款金额</div>
									<div class="text_color">
										<span>{{rechgeData.refundMoney ? rechgeData.refundMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						
					</el-row>
				</el-tab-pane>
			</el-tabs>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 5,
				page: 1,
				tableData: {},
				tableData2: {
					list: [],
					totalCount: 0
				},
				colonelData: [],
				commodityData: [],
				userjfData: [],
				incomeData: [],
				activeName: 'first',
				tableDataLoading: true,
				flag: 1,
				usersumData: {},
				taskStat: {},
				data: '',
				time: '',
				platform: '',
				type: '',
				balance: -1,
				flag: 1,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				userStat:{},
				phoneStat: 0,
				notphoneStat: 0, //未绑定手机号
				appStat: 0,
				weixinStat: 0,
				mpStat:0,
				wapStat: 0,
				rechgeData: {},
				shouyiData: {},
			}
		},
		mounted() {
			this.colonel()
			this.taskData()
			this._getData() //用户统计
			// this.incomeSelect()//收入
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			updates1(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId2
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val;
				this.colonel();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.colonel();
			},
			// 用户分析选择日期
			animeDat() {
				this._getData()
			},
			// 订单分析选择日期
			animeOrder() {
				this.taskData()
				this.colonel()
				this.userData()
				this.rechSelect()
				this.shouyiSelect()
			},
			// 订单分析年月日
			orderfenxi(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.taskData()
					this.colonel()
					this.userData()
					this.rechSelect()
					this.shouyiSelect()
				}
				if (vanumber === 2) {
					this.flag = 2
					this.taskData()
					this.colonel()
					this.userData()
					this.rechSelect()
					this.shouyiSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.taskData()
					this.colonel()
					this.userData()
					this.rechSelect()
					this.shouyiSelect()
				}
			},
			// Tabs点击事件
			handleClick(tab, event) {
				if (tab._props.label == '订单分析') {
					this.taskData()
				}
				if (tab._props.label == '收益统计') {
					this.shouyiSelect()
				}
				
				if (tab._props.label == '收益收入分析') {
					this.colonel()
				}
				if (tab._props.label == '用户分析') {
					this.info.stockDate = this.getNowTime()
					this.userData()
					
				}
				if (tab._props.label == '充值统计') {
					this.info.stockDate = this.getNowTime()
					this.rechSelect()
				}
			},
			//处理默认选中当前日期
			getNowTime() {
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
			// 用户统计
			_getData() {
				this.$http({
					url: this.$http.adornUrl('user/homeMessage'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data;
						this.tableData = returnData
					}

				})
			},
			// 收入统计
			//  incomeSelect(){
			//  this.$http({
			// url: this.$http.adornUrl('statistical/income'),
			// method: 'get',
			// params: this.$http.adornParams({
			// })
			//  }).then(({data}) => {
			//   if(data.code == 0){
			// 	  let returnData = data.data;
			// 	  this.incomeData = returnData
			//   }
			//  })
			//  },
			//订单统计分析
			taskData() {
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersCount'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.taskStat = returnData
				})
			},
			// 收益收入分析
			colonel() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orderTaking/incomeAnalysis'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						let returnData = data.data
						this.tableData2 = returnData
					}
				})
			},
			// 用户分析
						userData(platform, type) {
							this.$http({
								url: this.$http.adornUrl('user/userPlatform'),
								method: 'get',
								params: this.$http.adornParams({
									'data': this.info.stockDate,
									'type': this.flag,
								})
							}).then(({
								data
							}) => {
								let returnData = data.data;
								this.userStat = returnData
								
							})
			},
			// 充值统计
			rechSelect() {
				this.$http({
					url: this.$http.adornUrl('cash/payMember'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.rechgeData = returnData
					}
				})
			},
			// 收益统计 
			shouyiSelect() {
				this.$http({
					url: this.$http.adornUrl('orders/selectMoneyCount'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.shouyiData = returnData
					}
				})
			},
		}
	}
</script>

<style scoped>
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
