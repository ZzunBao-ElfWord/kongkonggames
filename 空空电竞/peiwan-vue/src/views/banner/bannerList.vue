<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="轮播图" name="first">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="名称"></el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:disabled="!isAuth('bannerList:update')" :active-value="openValue"
							:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			<!-- 添加弹框 -->
			<!-- <el-dialog title="添加" :visible.sync="dialogFormVisible" center>
				<div style="margin-bottom: 10px;display: flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="imageUrl"
							:action="$http.adornUrl('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess">
							<img v-if="imageUrl" :src="imageUrl" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
					<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
					<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
					<el-input style="width:50%;" v-model="describes" placeholder="请输入描述"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
				</div>
			</el-dialog> -->
		</el-tab-pane>
		<el-tab-pane label="首页分类" name="second">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice2">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="名称">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			<!-- 添加弹框 -->
			<el-dialog title="添加" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;display: flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="imageUrl" :action="$http.adornUrl('alioss/upload')"
							:show-file-list="false" :on-success="handleAvatarSuccess">
							<img v-if="imageUrl" :src="imageUrl" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
					<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
					<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
					<el-input style="width:50%;" v-model="describes" placeholder="请输入描述"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="addNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
		<el-tab-pane label="背景图" name="fourthly">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice5">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="名称">
				</el-table-column>
				<el-table-column prop="describes" label="活动描述">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>

		</el-tab-pane>
		<el-tab-pane label="动态轮播图" name="hdBanner">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="名称"></el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:disabled="!isAuth('bannerList:update')" :active-value="openValue"
							:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>

		</el-tab-pane>
		<el-tab-pane label="首页配置" name="shouye">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice5">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="名称">
				</el-table-column>
				<el-table-column prop="describes" label="活动描述">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="我的页面配置" name="wode">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice5">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="名称">
				</el-table-column>
				<el-table-column prop="describes" label="活动描述">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="约玩页面配置" name="yuewan">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice5">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="名称">
				</el-table-column>
				<el-table-column prop="describes" label="活动描述">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<!-- 动态轮播图、首页、我的页面、约玩页面等配置 添加弹框 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible5" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="imageUrl" :action="$http.adornUrl('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess">
						<img v-if="imageUrl" :src="imageUrl" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
				<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="describes" placeholder="请输入描述"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入排序"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible5 = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo5()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加弹框 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="imageUrl" :action="$http.adornUrl('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess">
						<img v-if="imageUrl" :src="imageUrl" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
				<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="describes" placeholder="请输入描述"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入排序"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="图片：" :label-width="formLabelWidth">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="imageUrl" :action="$http.adornUrl('alioss/upload')"
							:show-file-list="false" :on-success="handleAvatarSuccess2">
							<img v-if="form.imageUrl" :src="form.imageUrl" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="链接：" :label-width="formLabelWidth">
					<el-input v-model="form.url" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="名称：" :label-width="formLabelWidth">
					<el-input v-model="form.name" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="备注：" :label-width="formLabelWidth">
					<el-input v-model="form.describes" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="排序：" :label-width="formLabelWidth">
					<el-input v-model="form.sort" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				classify: 1,
				openValue: 1,
				closeValue: 2,
				name: '',
				imageUrl: '',
				url: '',
				sort: '',
				state: -1,
				describes: '',
				hideUpload: false,
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				tableData: [],
				choicenData: [],
				choicenData2: [],
				checkBoxData: [], //多选框选择的值
				keyword: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				states: [{
						label: '显示',
						value: 1
					},
					{
						label: '隐藏',
						value: 2
					}
				],
				form: {
					id: '',
					name: '',
					imageUrl: '',
					url: '',
					classify: '',
					createTime: '',
					sort: '',
					state: '',
					describes: ''

				}
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
				var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
				var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '轮播图') {
					this.page = 1
					this.limit = 10
					this.classify = 1
					this.dataSelect()
				}
				if (tab._props.label == '首页分类') {
					this.page = 1
					this.limit = 10
					this.classify = 2
					this.dataSelect()
				}
				if (tab._props.label == '活动') {
					this.page = 1
					this.limit = 10
					this.classify = 3
					this.dataSelect()
				}
				if (tab._props.label == '精品服务') {
					this.page = 1
					this.limit = 10
					this.classify = 4
					this.dataSelect()
				}
				if (tab._props.label == '背景图') {
					this.page = 1
					this.limit = 10
					this.classify = 5
					this.dataSelect()
				}
				if (tab._props.label == '动态轮播图') {
					this.page = 1
					this.limit = 10
					this.classify = 6
					this.dataSelect()
				}

				if (tab._props.label == '首页配置') {
					this.page = 1
					this.limit = 10
					this.classify = 7
					this.dataSelect()
				}
				if (tab._props.label == '我的页面配置') {
					this.page = 1
					this.limit = 10
					this.classify = 8
					this.dataSelect()
				}
				if (tab._props.label == '约玩页面配置') {
					this.page = 1
					this.limit = 10
					this.classify = 9
					this.dataSelect()
				}
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.imageUrl = file.data
			},
			handleAvatarSuccess2(file2) {
				this.form.imageUrl = file2.data
				console.log('file2.data', file2.data)
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange3(val) {
				this.limit = val
				this.choicenSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.choicenSelect()
			},
			// 是否启用
			change(val, row) {
				this.$http({
					url: this.$http.adornUrl(`banner/updateBannerStateById?id=${row.id}`),
					method: 'post',
					data: this.$http.adornData({})
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
			},
			//添加banner图
			addNotice() {
				this.imageUrl = ''
				this.state = ''
				this.url = ''
				this.name = ''
				this.describes = ''
				this.sort = ''
				this.dialogFormVisible = true
			},
			// 添加banner图
			addNoticeTo() {
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				// this.classify = 1
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {

								this.dataSelect()
							}
						})
					}

				})
			},
			// 修改
			amendBanner(index, rows) {
				this.dialogFormVisible1 = true
				this.form.id = rows.id
				this.form.imageUrl = rows.imageUrl
				this.form.url = rows.url
				this.form.sort = rows.sort
				this.form.state = rows.state
				this.form.name = rows.name
				this.form.describes = rows.describes
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(
						'banner/updateBannerById'
					),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'createTime': this.info.stockDate,
						'imageUrl': this.form.imageUrl,
						'url': this.form.url,
						'state': this.form.state,
						'sort': this.form.sort,
						'name': this.form.name,
						'describes': this.form.describes
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
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
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			//添加任务分类
			addNotice2() {
				this.imageUrl = ''
				this.state = ''
				this.url = ''
				this.name = ''
				this.describes = ''
				this.sort = ''
				this.dialogFormVisible2 = true
			},
			// 添加添加任务分类
			addNoticeTo2() {
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				this.classify = 2
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible2 = false
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
			},
			//添加活动
			addNotice3() {
				this.imageUrl = ''
				this.state = ''
				this.url = ''
				this.name = ''
				this.describes = ''
				this.sort = ''
				this.dialogFormVisible3 = true
			},
			// 添加添加活动
			addNoticeTo3() {
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				this.classify = 3
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible3 = false
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
			},
			//添加精品服务
			addNotice4() {
				this.imageUrl = ''
				this.state = ''
				this.url = ''
				this.name = ''
				this.describes = ''
				this.sort = ''
				this.dialogFormVisible4 = true
			},
			// 添加精品服务
			addNoticeTo4() {
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				this.classify = 4
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible4 = false
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
			},
			//添加活动背景图
			addNotice5() {
				if (this.tableData.length == 1) {
					this.$message({
						title: '提示',
						duration: 1800,
						message: '活动背景图只能添加一张',
						type: 'warning'
					})
				} else {
					this.imageUrl = ''
					this.state = ''
					this.url = ''
					this.name = ''
					this.describes = ''
					this.sort = ''
					this.dialogFormVisible5 = true
				}
			},
			// 添加背景图
			addNoticeTo5() {
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						// type: 'success',
						message: '请上传图片',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				this.classify = 5
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible5 = false
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
			},
			// 删除banner图
			deletes(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`banner/deleteBannerById?ids=${delid}`),
						method: 'get',
						data: this.$http.adornData({})
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
				}).catch(() => {})

			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('banner/selectBannerPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'classify': this.classify,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
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
	.customWidth {
		width: 80% !important;
	}
</style>