x<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="技能列表" name="third">
				<div style="display: inline-block;">
					<!-- <span>类型：</span>
					<el-select v-model="classifyT" style="width:150px;margin-left: 10px;" @change="phoneSelect()">
						<el-option v-for="item in classifyList" :key="item.id" :label="item.name" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp; -->
					<span>分类：</span>
					<el-select v-model="classifyIds" filterable style="width:150px;margin-left: 10px;" @change="phoneSelect(status)">
						<el-option v-for="item in homeData1" :key="item.value" :label="item.gameName" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
					<span>状态：</span>
					<el-select v-model="statusId" style="width:150px;margin-left: 10px;" @change="phoneSelect(status)">
						<el-option v-for="item in statuss" :key="item.value" :label="item.title" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
					<!-- <div style="position: relative;display: inline-block;">
						<span>用户手机号:</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入手机号"
							v-model="myPhone">
						</el-input>&nbsp;&nbsp;
					</div> -->
					<div style="position: relative;display: inline-block;">
						<span>用户名称：</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入用户名称"
							v-model="search">
						</el-input>&nbsp;&nbsp;
					</div>
					<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="phoneSelect">查询
					</el-button>
					<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleans2">重置
					</el-button>&nbsp;&nbsp;
					<el-button style="margin: 10px 0;" :disabled="!isAuth('locality:add')" size="mini" type="primary"
						icon="document" @click="choiaddNotice()">添加技能</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
					<!-- <el-button style="margin: 10px 0;"
						:disabled="!isAuth('locality:delete') || multipleSelection.length <= 0 " size="mini"
						type="danger" icon="document" @click="choideletes()">批量删除</el-button> -->
					<el-button style="margin:10px;" size="mini" type="success" plain icon="document" @click="biaoqian">标签图
					</el-button>
				</div>
				<div style="float: right;margin-right:2%;">
				</div>
				<el-table v-loading="tableDataLoading" :data="choicenData.list">
					<!--@selection-change="handleSelectionChange" <el-table-column type="selection">
					</el-table-column> -->
					<el-table-column prop="id" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="isRecommend" label="首页推荐" width="100">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isRecommend"
								@change="change2(scope.row.isRecommend,scope.row)" :active-value="openValue1"
								:disabled="!isAuth('locality:update')" :inactive-value="closeValue1"
								active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="userName" label="发布人" fixed="left">
						<template slot-scope="scope">
							　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="homepageImg" label="主页图" width="400">
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
							<!-- <audio :id="'myAudio1'+scope.row.id" controls>
							  <source src="https://www.w3school.com.cn/i/audio/song.mp3" type="audio/mpeg">
							  您的浏览器不支持 audio 元素。
							</audio> -->
							<!-- <audio :id="'myAudio'+scope.row.id" controls duration="none" >
							  <source :src="scope.row.voiceIntroduce" type="audio/mpeg">
							  您的浏览器不支持 audio 元素。
							</audio> -->
							<audio :id="'myAudio'+scope.row.id" v-if="scope.row.voiceIntroduce!=''&&scope.row.voiceIntroduce!=null&&scope.row.voiceIntroduce!='null'" duration
								:src="scope.row.voiceIntroduce" type="audio/wav" controls preload="metadata"></audio>
							<span v-else>暂无语音</span>
							
						</template>
					</el-table-column>
					<el-table-column prop="details" label="个人简介" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">{{scope.row.details}}</div>
								<div style="width: 300px;">{{scope.row.details}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="classify" label="游戏类型" width="150">
						<template slot-scope="scope">
							<span v-if="scope.row.classify==1">线上</span>
							<span v-if="scope.row.classify==2">线下</span>
						</template>
					</el-table-column> -->
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
					<el-table-column prop="isWangZhes" label="标签图" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.label">
								<div v-for="(item,index) in scope.row.label.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
							
						</template>
					</el-table-column>
					<!-- <el-table-column prop="city" label="城市" width="200">
					</el-table-column> -->
					<el-table-column prop="count" label="接单人数" width="140">
						<template slot-scope="scope">
							<span v-if="scope.row.count">{{scope.row.count}}</span>
							<span v-else>0</span>
						</template>
					</el-table-column>
					<el-table-column prop="rewardCount" label="打赏次数" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.rewardCount">{{scope.row.rewardCount}}</span>
							<span v-else>0</span>
						</template>
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
							<el-switch v-model="scope.row.onLine"
								:disabled="!isAuth('locality:update')" @change="changeOnline(scope.row.onLine,scope.row)"
								:active-value="openValue3" :inactive-value="closeValue3" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
							
						</template>
					</el-table-column>
					<el-table-column prop="status" label="状态" width="100" fixed='right'>
						<template slot-scope="scope">
							<span style="color: #4f9dec;" v-if="scope.row.status === 1 ">待审核</span>
							<el-switch v-if="scope.row.status ==0 || scope.row.status ==2" v-model="scope.row.status"
								:disabled="!isAuth('locality:update')" @change="change3(scope.row.state,scope.row)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
							<!-- <span style="color: #4f9dec;" v-if="scope.row.status === 1 ">上架</span>
							<span style="color: #4f9dec;" v-if="scope.row.status === 2 ">下架</span> -->
							<span style="color: #4f9dec;" v-if="scope.row.status === 3 ">驳回</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="240" fixed='right' align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" v-if="scope.row.status === 1 "
								@click="shenhe( scope.row)">审核</el-button>
							<el-button size="mini" type="primary" v-if="scope.row.status != 1 "
								@click="plCompile(scope.row)">评论
							</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('locality:update')"
								@click="choiCompile(scope.$index, scope.row)">修改
							</el-button>

							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="choidelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;float:right">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[5,10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="choicenData.totalCount">
					</el-pagination>
				</div>
				<!-- 添加信息弹框 -->
				<el-dialog :title="renwuTitles" customClass="customWidth" :visible.sync="dialogFormVisible5" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">陪练游戏：</span>
						<el-select v-model="gameId" filterable style="width:200px;" @change="onChang">
							<el-option v-for="(item,index) in homeData" :key="index" :label="item.gameName"
								:value="item.id">
							</el-option>
						</el-select>
					</div>
					<!-- <div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">片区：</span>
						<el-select v-model="campusName" style="width:200px;" @change="onChang3">
							<el-option v-for="(item,index) in homeData2" :key="index" :label="item.campusName"
								:value="item.campusId">
							</el-option>
						</el-select>
					</div> -->
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">发布人：</span>
						<el-input v-model="nickName1" @focus="userselect" style="width:200px;" placeholder="请选择发布人">
						</el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">段位：</span>
						<el-input v-model="myLevel" style="width:200px;" placeholder="请输入段位"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">接单段位：</span>
						<el-input v-model="orderLevel" style="width:200px;" placeholder="请输入段位"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">接单时间：</span>
						<!-- <el-input v-model="orderTakingTime" style="width:200px;" placeholder="请输入接单时间"></el-input> -->
						<span class="block">
							<!-- <el-time-picker is-range v-model="orderTakingTime" range-separator="至"
								start-placeholder="开始时间" end-placeholder="结束时间" placeholder="选择时间范围"
								value-format="HH-mm-ss" @change="changeaa" default-value="Date">
							</el-time-picker> -->
							<el-time-picker v-model="value1" :picker-options="pickeroptions" placeholder="任意时间点"
								value-format="HH:mm" @change="changeaa">
							</el-time-picker> ~
							<el-time-picker v-model="value2" :picker-options="pickeroptions" placeholder="任意时间点"
								value-format="HH:mm" @change="changeaa1">
							</el-time-picker>
						</span>
					</div>
					<div style="margin-bottom: 10px;display:flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">标签图：</span>
						<div class="imgs" v-for="(item,index) in isWangZhe" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="dels1(index)"></i>
							</span>
						</div>
						<div class="imgs" style="width: 50%;">
							<el-button type="primary" plain @click="isWangZheBtn()">添加标签</el-button>
							<!-- <el-upload :action="$http.adornUrlUp('alioss/upload')"
								list-type="picture-card" :show-file-list="false" :on-success="handleRemove1"
								v-if="isWangZhe.length<5">
								<i class="el-icon-plus"></i>
							</el-upload>
							<el-dialog :visible.sync="dialogVisible">
								<img width="100%" :src="dialogImageUrl" alt="">
							</el-dialog> -->
						</div>
					</div>
					<!-- <div style="margin-bottom: 10px;display:flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">主页图：</span>
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="homepageImg"
								:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
								:on-success="handleAvatarSuccess5" :on-progress="onprogress">
								<img v-if="homepageImg!=''&&percentage==100" :src="homepageImg" class="avatar"
									style="border-radius: 6px;width: 148px;height: 148px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
								<el-progress v-if="percentage>0&&percentage<100" type="circle" :percentage="percentage">
								</el-progress>
							</el-upload>
						</div>
					</div> -->
					<div style="margin-bottom: 10px;display:flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">主页图：</span>
						<div class="imgs" v-for="(item,index) in homepageImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="dels(index)"></i>
							</span>
						</div>
						<div class="imgs" style="width: 50%;">
							<el-upload :action="$http.adornUrlUp('alioss/upload')"
								list-type="picture-card" :show-file-list="false" :on-success="handleRemove"
								v-if="homepageImg.length<5">
								<i class="el-icon-plus"></i>
							</el-upload>
							<el-dialog :visible.sync="dialogVisible">
								<img width="100%" :src="dialogImageUrl" alt="">
							</el-dialog>
						</div>
					</div>
					<div style="margin-bottom: 10px;display:flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">语音上传：</span>
						<div style="width: 50%;">
							<el-upload class="upload-demo" v-model="voiceIntroduce"
								:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
								:on-success="handleSuccess" :before-upload="beforeAvatarUpload"
								:on-progress="handlePreview">
								<el-button size="small" type="primary" v-loading="tableDataLoading4">点击上传</el-button>
								<el-input v-if="voiceIntroduce!=''" v-model="voiceIntroduce" style="width:300px;">
								</el-input>
							</el-upload>
						</div>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">接单大区：</span>
						<el-input v-model="orderTakingArea" style="width:200px;" placeholder="请输入接单大区"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">发布价格：</span>
						<el-input v-model="oldMoney" type="number" style="width:200px;" min="0" placeholder="请输入发布价格"
							onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">普通用户接单价格：</span>
						<el-input v-model="money" type="number" style="width:200px;" min="0" placeholder="请输入普通用户接单价格"
							onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">会员接单价格：</span>
						<el-input v-model="memberMoney" type="number" style="width:200px;" min="0"
							placeholder="请输入会员接单价格" onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')">
						</el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">单位：</span>
						<el-radio-group v-model="unit">
							<el-radio label="单">单</el-radio>
							<el-radio label="局">局</el-radio>
							<el-radio label="小时">小时</el-radio>
							<el-radio label="天">天</el-radio>
						</el-radio-group>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
						<el-radio-group v-model="status">
							<el-radio :label="0">上架</el-radio>
							<el-radio :label="2">下架</el-radio>
						</el-radio-group>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">个人简介：</span>
						<el-input v-model="details" style="width:45%;" placeholder="请输入个人简介" min="0"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
						<el-input v-model="latitude" style="width:45%;" placeholder="请输入纬度"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
						<el-input v-model="longitude" style="width:45%;" placeholder="请输入经度"></el-input>
					</div>
					<div>
						<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
					</div>
					<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
						<el-button @click="prev">取 消</el-button>
						<el-button type="primary" @click="addmissionNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
			</el-tab-pane>
			<!-- 游戏分类 -->
			<el-tab-pane label="游戏分类" name="first">
				<div style="width: 100%;text-align: right;">
					<el-button style="margin:10px;" size="mini" type="primary" icon="document"
						:disabled="!isAuth('locality:add')" @click="classAdd(0)">添加
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="createTime" label="游戏类型" width="160">
						<template slot-scope="scope">
							<span v-if="scope.row.gameType==1">手游</span>
							<span v-if="scope.row.gameType==2">端游</span>
							<span v-if="scope.row.gameType==3">其他</span>
							<span v-if="scope.row.gameType==4">电影</span>
						</template>
					</el-table-column>
					<el-table-column prop="gameName" label="游戏名称">
					</el-table-column>
					<el-table-column prop="gameImg" label="游戏图片">
						<template slot-scope="scope">
							　　<img v-if="scope.row.gameImg &&scope.row.gameImg!=''" :src="scope.row.gameImg" width="40"
								height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.id,scope.row.status)"
								:disabled="!isAuth('locality:update')" :active-value="openValue2"
								:inactive-value="closeValue2" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="240" fixed='right' align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('locality:update')"
								@click="classAdd(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="classdelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
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
				<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.records">
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
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!--    图片展示-->
			<el-dialog title="图片" :visible.sync="dialogVisible" width="800px">
				<span v-if="imageUrl.length>0" v-for="(item, index) in imageUrl" :key="index" style="">
					<!-- <el-image style = "width: 100px; height: 100px" :src = "item" :preview-src-list="imageUrl"></el-image>-->
					<img :src="item" alt="" style="width: 45%; height: 100%;margin: 15px;display:inline-block;">
				</span>
				<span v-else>暂无图片</span>
			</el-dialog>
			<!-- 技能审核弹框 -->
			<el-dialog title="技能审核" :visible.sync="dialogFormVisible8" center>
				<el-radio-group v-model="radio" style="width: 100%;margin-bottom: 30px;margin-left: 200px;">
					<el-radio :label="0">通过</el-radio>
					<el-radio :label="3">驳回</el-radio>
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
			<!-- 添加、修改游戏分类 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisible9" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">游戏类型：</span>
					<el-radio-group v-model="gameType">
						<el-radio :label="1">手游</el-radio>
						<el-radio :label="2">端游</el-radio>
						<el-radio :label="3">其他</el-radio>
						<el-radio :label="4">电影</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">游戏名称：</span>
					<el-input style="width:50%;" v-model="gameName" type="text" placeholder="请输入游戏名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">游戏图片：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="gameImg"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess1" :on-progress="onprogress2">
							<!-- <el-progress type="circle" :percentage="100" status="success"></el-progress> -->
							<img v-if="gameImg!=''&&percentage2==100" :src="gameImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<el-progress v-if="percentage2>0&&percentage2<100" type="circle" :percentage="percentage2">
							</el-progress>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible9 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 评论列表 -->
			<el-dialog title="评论列表" :visible.sync="dialogFormVisible10" center>
				<el-table v-loading="tableDataLoading2" :data="pinglunData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="评论用户" width="120">
						<template slot-scope="scope">
							　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像" width="100">
						<template slot-scope="scope">
							　　<img v-if="scope.row.avatar && scope.row.avatar!=''" :src="scope.row.avatar" width="40"
								height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="评论内容">
					</el-table-column>
					<el-table-column prop="createTime" label="评论时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="pinglundelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size2" :current-page="page2"
						layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 标签图 -->
			<el-dialog title="标签图" :visible.sync="dialogFormVisibleBq" center width="80%">
				<div style="text-align: right;">
					<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="addSelect"  v-if="formBq==1">添加
					</el-button>
					<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="dialogFormVisibleBq=false"  v-if="formBq==2">确认
					</el-button>
				</div>
				<div>
					<div v-for="(item,index) in biaoqianData.list" class="biaoqianCla" v-if="formBq==1">
						<el-button type="info" size="mini" icon="el-icon-delete" circle style="position: absolute;right: 0;padding:5px;margin-right: 5px;" @click="deleteBq(item)"></el-button>
						<!-- <i class="el-icon-delete"></i> -->
						<img :src="item.image" style="width: 70px;height: 100%;padding-top:20%;"/>
					</div>
					
					<el-checkbox-group v-model="isWangZhe" v-if="formBq==2">
					    <el-checkbox v-for="(item,index) in biaoqianData.list" :key="index" border :label="item.image"  @click="xuanze(item)" style="margin-top: 10px;height: auto;">
							<div class="biaoqianCla">
								<!-- <i class="el-icon-delete"></i> -->
								<img :src="item.image" style="width: 70px;height: 100%;padding-top:20%;"/>
							</div>
						</el-checkbox>
					  </el-checkbox-group>
					
					
					
					<!-- <div v-for="(item,index) in biaoqianData.list" class="biaoqianCla"  v-if="formBq==2" @click="xuanze(item)">
						<img :src="item.image" style="width: 200px;height: 100%;padding-top:20%;"/>
					</div> -->
				</div>
				<div style="text-align: center;">
					<el-pagination @size-change="handleSizeChangeBq" @current-change="handleCurrentChangeBq"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
						layout="total,sizes, prev, pager, next,jumper" :total="biaoqianData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 标签图 -->
			<el-dialog title="添加标签图" :visible.sync="dialogFormVisibleBqadd" center>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">标签图：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="image"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccessBq">
							<!-- <el-progress type="circle" :percentage="100" status="success"></el-progress> -->
							<img v-if="image!=''" :src="image" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleBqadd = false">取 消</el-button>
					<el-button type="primary" @click="refusetoBq()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from './quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				dialogWidth: 0,
				size: 5,
				page: 1,
				size1: 10,
				page1: 1,
				size2: 10,
				page2: 1,
				state: '',
				limit: 10,
				classify: 6,
				classifys: 6,
				openValue: 0,
				closeValue: 2,
				openValue1: '0',
				closeValue1: '1',
				openValue2: 0,
				closeValue2: 1,
				openValue3: 1,
				closeValue3: 2,
				title: '',
				type: '',
				name: '',
				keyword: '',
				describes: '',
				checkBoxData: [], //多选框选择的值
				method: 'false',
				formLabelWidth: '200px',
				activeName: 'third',
				tableDataLoading: true,
				tableDataLoading2:false,
				tableDataLoading4: false,
				dialogFormVisible3: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				dialogFormVisible10: false,
				dialogImageUrl: [],
				homeData: [],
				homeData1: [{
					id: 0,
					name: '全部'
				}],
				classifyT: '',
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
				choicenData: [],
				pinglunData: {},
				renwuId: '',
				url: '',
				imageUrl: '',
				id: '',
				userId: '',
				search: '',
				myPhone: '',
				classifyIds: '',
				statusId: '',
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
				// 发布信息
				myLevel: '', //我的段位
				orderLevel: '', //接单段位
				userIdss: '', //用户id
				orderTakingArea: '', //接单大区
				homepageImg: [], //封面图
				percentage: 0, //进度条
				percentage2: 0, //进度条
				voiceIntroduce: '', //语音介绍
				gameId: '', //游戏id
				oldMoney: '', //发布价格
				money: '', //普通用户价格
				memberMoney: '', //会员价格
				latitude: '', //纬度
				longitude: '', //经度
				city: '', //市
				orderTakingTime: '',
				status: '', //状态0待审核  1上架  2下架 3驳回 (修改添加不传)
				phone: '', //电话
				phone1: '',
				pickeroptions: {
					selectableRange: '00:00:01 - 23:59:59'
				},
				value1: '',
				value2: '',

				options: regionData,
				storeAddress: [],
				storeAddre: '请选择城市',
				province: '', //省
				district: '', //区
				ids: '', //信息id
				nickName: '',
				nickName1: '',
				nickName2: '',
				unit: '',
				userIds: {}, //用户列表
				bq: '', //输入的标签名
				missions: 0,
				shenheId: 0, //审核id
				contents: '', //驳回理由
				dialogVisible: false,
				radio: 0,
				multipleSelection: [],
				campus: '',
				campus1: '',
				campusName: '',
				homeData2: [],
				quillOption: quillConfig,
				statusIdd: 1,
				tableData: {},
				titles: '添加游戏分类',
				renwuTitles: '添加技能',
				gameName: '',
				gameIds: '',
				gameImg: '',
				gameStatus: '',
				details: '', //个人简介
				address:'',
				
				dialogFormVisibleBq:false,
				pageB:1,
				limitB:50,
				biaoqianData:{},
				dialogFormVisibleBqadd:false,
				image:'',
				formBq:1,
				isWangZhe: [],
				gameType: 1,
			}
		},
		methods: {
			//查看照片
			refund(e) {
				console.log(e)
				this.imageUrl = []
				if (e.img != null) {
					let img = e.img.split(',')
					if (img.length != 0) {
						this.imageUrl = img
					}
				}
				this.dialogVisible = true
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			handleSizeChange(val) {
				this.limit = val
				this.classSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.classSelect()
			},
			handleSizeChange1(val) {
				this.size = val
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
				this.pinglunSelect(this.renwuId)
			},
			handleCurrentChange3(val) {
				this.page2 = val
				this.pinglunSelect(this.renwuId)
			},
			handleSizeChangeBq(val) {
				this.limitB = val
				this.biaoqianSelect()
			},
			handleCurrentChangeBq(val) {
				this.pageB = val
				this.biaoqianSelect()
			},
			handleClick(tab, event) {
				this.campus = ''
				this.campus1 = ''
				this.page = 1
				if (tab._props.label == '技能列表') {
					this.type = 2
					this.InformationSelect()
				}
				if (tab._props.label == '游戏分类') {

					this.classSelect()
				}
			},

			//添加技能
			choiaddNotice() {
				this.campus = ''
				this.campus1 = ''
				this.renwuTitles = '添加技能'
				this.dialogFormVisible5 = true
				this.percentage = 0
				this.gameId = ''
				this.myLevel = ''
				this.orderTakingTime = ''
				this.value1 = ''
				this.value2 = ''
				this.orderLevel = ''
				this.orderTakingArea = ''
				this.homepageImg = []
				this.oldMoney = ''
				this.details = ''
				this.money = ''
				this.memberMoney = ''
				this.latitude = ''
				this.unit = ''
				this.longitude = ''
				this.voiceIntroduce = ''
				this.userIdss = ''
				this.city = ''
				this.nickName1 = ''
				this.nickName2 = ''
				this.phone = ''
				this.getMyLocation()
				console.log('this.homepageImg', this.homepageImg)
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.status = ''
				this.statusId = ''
				this.classifyT = ''
				this.search = ''
				this.classifyIds = ''
				this.page = 1
				console.log('this.phone', this.myPhone)
				this.InformationSelect()
			},
			// 获取游戏分类启用列表
			homeSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`game/queryGame`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
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
				this.tableDataLoading = true
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl(`orderTaking/queryAllOrderTaking`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userName': this.search,
						'gameId': this.classifyIds,
						'status': this.statusId,
						'classify': this.classifyT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						data.data.list[i].homepageImgs = data.data.list[i].homepageImg.split(',')
					}
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
			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage2 = parseInt(event.percent)
			},
			// 详情图片上传
			handleRemove(file, ) {
				this.homepageImg.push(file.data);
			},
			// 删除详情图
			dels(index) {
				console.log(this.homepageImg)
				this.homepageImg.splice(index, 1);

			},
			handleSuccess(file) {
				console.log(file)
				this.voiceIntroduce = file.data;
				console.log(file.data, this.voiceIntroduce)
				this.tableDataLoading4 = false
			},
			handlePreview(file) {
				this.tableDataLoading4 = true
			},
			// 验证只能上传音频
			beforeAvatarUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				const extension = testmsg === 'mp3'
				if (!extension) {
					this.$message({
						message: "上传文件只能是mp3格式！",
						type: 'error'
					})
				}
				return extension;
			},
			// 删除详情图
			// dels(index) {
			// 	this.img.splice(index, 1);
			// 	console.log(this.img)
			// },
			// 获取分类id
			onChang(e) {
				console.log(e)
				this.gameId = e
			},
			// 获取社区
			onChang3(e) {
				console.log(e)
				this.campus1 = e
			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				this.form.city = value[1]
				if (this.city == '市辖区') {
					this.city = this.province
				}
				if (this.form.city == '市辖区') {
					this.city = this.province
				}
				console.log(this.$refs['cascaderAddr'].currentLabels)
			},
			//定位获得当前位置信息
			getMyLocation() {

				var geolocation = new qq.maps.Geolocation("REDACTED_CONFIGURE_LOCALLY", "恋语陪伴");

				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				// this.latitude = position.lat;
				// this.longitude = position.lng;
				// this.city = position.city;
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}

				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度

					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng.getLng() + '&key=REDACTED_CONFIGURE_LOCALLY&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response, response.result.address_component.city)
						that.address = response.result.formatted_addresses.recommend
						that.city = response.result.address_component.city
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					if (!marker) {
						marker = new qq.maps.Marker({
							map: map,
							position: event.latLng
						});
					} else {
						marker.setPosition(event.latLng)
					}

					// markersArray.push(marker);

				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
				console.log('-----------------',that.city,)
			},
			// 地图定位
			select() {
				console.log(this.city,)

				// if (this.city == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请在地图上定位获取经纬度',
				// 		type: 'warning'
				// 	});
				// 	return
				// } else {
					var add = this.province + this.city + this.district + this.address
					let that = this
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + add +
						'&key=REDACTED_CONFIGURE_LOCALLY&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						// handle success
						if (response.message == '查询无结果') {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '详细地址输入有误，请重新输入',
								type: 'warning'
							});
							return
						}
						console.log('response', response)
						that.longitude = response.result.location.lng; // 经度
						that.latitude = response.result.location.lat; // 纬度
						that.city = response.result.address_components.city;
						// that.address = response.result.address_components.province + response.result.address_components
						// 	.city + response.result.address_components.district + response.result.title
						that.setMap()
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
				// }

			},
			// 获取用户列表弹框
			userselect() {
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
				this.$http({
					url: this.$http.adornUrl('userCertification/queryUserCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'name': this.nickName2,
						'phone': this.phone1
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
				this.isWangZhe.splice(index, 1);
				console.log(this.labels)
			},
			// 确定添加
			addmissionNoticeTo() {
				console.log(this.userIdss)
				if (this.gameId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择游戏',
						type: 'warning'
					});
					return
				}
				if (this.userId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择发布人',
						type: 'warning'
					});
					return
				}
				if (this.myLevel == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入我的段位',
						type: 'warning'
					});
					return
				}
				// if (this.orderLevel == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入接单段位',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.value1 == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择开始时间',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.value2 == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择结束时间',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.orderTakingArea == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入接单大区',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.homepageImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传技能封面图',
						type: 'warning'
					});
					return
				}
				// if (this.voiceIntroduce == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传语音介绍',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// var reg = /^1[3|4|5|7|8][0-9]{9}$/; //验证规则
				if (this.oldMoney == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入发布价格',
						type: 'warning'
					});
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入普通用户接单价格',
						type: 'warning'
					});
					return
				}
				if (this.memberMoney == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入会员接单价格',
						type: 'warning'
					});
					return
				}
				if (this.unit == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择单位',
						type: 'warning'
					});
					return
				}
				if (this.city == '') {
					// this.select()
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请在地图上定位获取城市',
						type: 'warning'
					});
					return
				}
				if (this.latitude == ''||this.longitude=='') {
					// this.select()
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请在地图上定位获取经纬度',
						type: 'warning'
					});
					return
				}
				

				let urls = ''
				if (this.missions == 0) {
					urls = 'orderTaking/insertOrderTaking'
				} else {
					urls = 'orderTaking/updateTakingOrders'
				}
				var times = this.value1 + '~' + this.value2
				let that = this

				setTimeout(function() {
					that.$http({
						url: that.$http.adornUrl(urls),
						method: 'get',
						// data: that.$http.adornData({
						params: that.$http.adornParams({
							'gameId': that.gameId,
							'myLevel': that.myLevel,
							'orderLevel': that.orderLevel,
							'orderTakingArea': that.orderTakingArea,
							'orderTakingTime': times,
							'oldMoney': that.oldMoney,
							'money': that.money,
							'memberMoney': that.memberMoney,
							'unit': that.unit,
							'voiceIntroduce': that.voiceIntroduce,
							'homepageImg': that.homepageImg.toString(),
							'userId': that.userIdss,
							'classify': 1,
							'latitude': that.latitude,
							'longitude': that.longitude,
							'city': that.city,
							'id': that.ids,
							'details': that.details,
							'status':that.status,
							'label':that.isWangZhe.toString()
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
									that.prev()
									that.campus = ''
									that.InformationSelect()
								}
							})
						} else {
							this.$notify({
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
				this.gameId = ''
				this.myLevel = ''
				this.orderTakingTime = ''
				this.value1 = ''
				this.value2 = ''
				this.orderLevel = ''
				this.orderTakingArea = ''
				this.homepageImg = []
				this.oldMoney = ''
				this.money = ''
				this.memberMoney = ''
				this.unit = ''
				this.latitude = ''
				this.longitude = ''
				this.voiceIntroduce = ''
				this.userIdss = ''
				this.city = ''
				this.nickName1 = ''
				this.nickName2 = ''
				this.phone = ''
				this.details = ''
				this.isWangZhe = []
			},
			// 审核
			shenhe(row) {
				this.shenheId = row.id
				this.dialogFormVisible8 = true
			},
			// 提交审核
			refuseto(row) {
				if (this.radio == 3 && this.contents == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入驳回理由',
						type: 'warning'
					})
					return
				} else {
					let ids = this.shenheId
					let status = this.radio
					let content = this.contents
					this.$http({
						url: this.$http.adornUrl(`orderTaking/auditorOrderTaking`),
						method: 'get',
						params: this.$http.adornParams({
							'id': ids,
							'status': status,
							'content': content
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
								this.InformationSelect()
							}
						})
					})
				}
			},
			// 修改信息弹框
			choiCompile(index, row) {
				console.log(index, row)
				this.labels = []
				this.missions = 1
				this.renwuTitles = '修改技能'
				if (row) {
					this.gameId = row.gameId
					this.myLevel = row.myLevel
					this.orderLevel = row.orderLevel
					this.orderTakingArea = row.orderTakingArea
					this.orderTakingTime = row.orderTakingTime
					var timess = row.orderTakingTime.split('~')
					this.value1 = timess[0]
					this.value2 = timess[1]
					this.oldMoney = row.oldMoney
					this.money = row.money
					this.memberMoney = row.memberMoney
					this.voiceIntroduce = row.voiceIntroduce
					this.homepageImg = row.homepageImg.split(',')
					this.userIdss = row.userIdss
					this.latitude = row.latitude
					this.longitude = row.longitude
					this.city = row.city
					this.classify = row.classify
					this.unit = row.unit
					this.ids = row.id
					this.nickName1 = row.userName
					this.userIdss = row.userId
					this.status = Number(row.status)
					this.percentage = 100
					this.details = row.details
					if(row.label){
						this.isWangZhe = row.label.split(',')
					}
					console.log('row.homepageImg', row.homepageImg)
				}

				this.dialogFormVisible5 = true
				this.getMyLocation()
			},
			// 查看评论
			plCompile(row) {
				console.log(row)
				this.renwuId = row.id
				this.dialogFormVisible10 = true
				this.pinglunSelect(row.id)
			},
			// 删除信息
			choidelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orderTaking/deleteOrderTaking?id=${delid}`),
						method: 'get',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.InformationSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 筛选信息
			animeDat2(state) {
				this.InformationSelect()
			},
			// 筛选信息
			animeDat4(state) {
				this.InformationSelect()
			},
			animeDat3(state) {
				this.InformationSelect()
			},
			// 多选
			handleSelectionChange(val) {

				var arr = []
				for (var i in val) {
					arr.push(val[i].id)
				}
				this.multipleSelection = arr;
				console.log('val', val, this.multipleSelection)
			},
			// 批量删除
			choideletes() {
				console.log(this.multipleSelection)
				let delid = this.multipleSelection.toString()
				this.$confirm(`确定删除信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`information/deleteInformationById?ids=${delid}`),
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
								this.InformationSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 技能推荐
			change2(val, row) {
				this.$http({
					url: this.$http.adornUrl(`orderTaking/updateOrderTakingRecommend/${row.id}`),
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
							this.homeSelect()
						}
					})
				})
			},
			// 技能上下架
			change3(val, row) {
				this.$http({
					url: this.$http.adornUrl(`orderTaking/updateOrderTakingStatus/${row.id}`),
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
							this.homeSelect()
						}
					})
				})
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
			changeaa(e) {
				console.log('eeee', e)
				this.value1 = e
			},
			changeaa1(e) {
				console.log('eeee', e)
				this.value2 = e
			},
			// 获取游戏分类数据
			classSelect() {
				this.$http({
					url: this.$http.adornUrl('game/queryGameName'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}


				})
			},
			// 添加游戏分类
			classAdd(row) {
				console.log('row', row)
				if (row != 0) {
					this.titles = '修改游戏分类'
					this.gameName = row.gameName
					this.gameIds = row.id
					this.gameImg = row.gameImg
					this.gameStatus = row.status
					this.gameType = row.gameType
					this.percentage2 = 100
				} else {
					this.titles = '添加游戏分类'
					this.gameName = ''
					this.gameIds = ''
					this.gameImg = ''
					this.gameStatus = ''
					this.gameType = ''
				}
				this.dialogFormVisible9 = true

			},
			// 启用与否
			change(id, status) {
				this.$http({
					url: this.$http.adornUrl(`game/enableGameName`),
					method: 'get',
					params: this.$http.adornParams({
						'id': id,
						'status': status
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classSelect()
						}
					})
				})
			},
			// 提交修改、添加游戏分类
			refuseto1() {
				if (this.gameType == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择游戏类型',
						type: 'warning'
					})
					return
				}
				if (this.gameName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入游戏名称',
						type: 'warning'
					})
					return
				} else if (this.gameImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传游戏图片',
						type: 'warning'
					})
					return gameImg
				} else {
					if (this.titles == '添加游戏分类') {
						var urls = 'game/addGameName'
					} else {
						var urls = 'game/updateGameName'
					}
					this.$http({
						url: this.$http.adornUrl(urls),
						method: 'get',
						params: this.$http.adornParams({
							'gameName': this.gameName,
							'status': this.gameStatus,
							'gameImg': this.gameImg,
							'id': this.gameIds,
							'gameType': this.gameType,
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible9 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.gameName = ''
								this.classSelect()
							}
						})
					})
				}
			},
			// s删除游戏分类
			classdelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`game/deleteGameName?id=${delid}`),
						method: 'get',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.classSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 用户评论列表
			pinglunSelect(id) {
				this.tableDataLoading2 = true
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl('takingComment/selectOrderTakingComment'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'id': id
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading2 = false
					let returnData = data.data
					this.pinglunData = returnData


				})
			},
			// 删除评论信息
			pinglundelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`takingComment/deleteOrderTakingComment/${delid}`),
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
								this.pinglunSelect(this.renwuId)
							}
						})
					})
				}).catch(() => {})
			},
			// 修改在线状态
			changeOnline(val, row) {
				this.$http({
					url: this.$http.adornUrl('user/updateOnLine'),
					method: 'post',
					params: this.$http.adornParams({
						'userId':row.userId,
						'onLine':val
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
								this.InformationSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.InformationSelect()
							}
						})
						
					}
					
				})
			},
			// 标签图弹框
			biaoqian(){
				this.formBq = 1
				this.pageB = 1
				this.dialogFormVisibleBq = true
				this.biaoqianSelect()
			},
			biaoqianSelect(){
				this.$http({
					url: this.$http.adornUrl(`message/selectMessageByType`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'state': 8,
						'platform': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.biaoqianData = returnData
				
				})
			},
			// 添加弹框
			addSelect(){
				this.image = ''
				this.dialogFormVisibleBqadd = true
			},
			handleAvatarSuccessBq(file) {
				this.image = file.data;
			},
			// 确认添加标签
			refusetoBq() {
				if (this.image == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传标签图',
						type: 'warning'
					})
					return image
				} 
				this.$http({
					url: this.$http.adornUrl('message/add'),
					method: 'post',
					data: this.$http.adornData({
						'image': this.image,
						'state': 8,
				
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
								this.dialogFormVisibleBqadd = false
								this.biaoqianSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.biaoqianSelect()
							}
						})
					}
				})
			},
			// 删除标签
			deleteBq(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('message/deleteMessageById'),
						method: 'post',
						params: this.$http.adornParams({
							'id':row.id
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.biaoqianSelect()
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
				}).catch(() => {})
			},
			// 服务添加标签图
			isWangZheBtn(){
				this.formBq = 2
				this.dialogFormVisibleBq = true
				this.biaoqianSelect()
			},
			// 服务选择标签
			xuanze(row){
				this.isWangZhe.push(row.image)
			},
		},
		mounted() {
			this.InformationSelect()
			this.homeSelect()
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
	.biaoqianCla{
		position: relative;
		    width: 90px;
		    height: 75px;
		display:inline-block;
		padding: 10px;
		box-shadow: 0px 0px 7px #999;
		margin: 5px;
	}
</style>
