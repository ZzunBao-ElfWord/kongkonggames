<template>
  <div>
    <div style="margin:2% 0;display: inline-block;">
      <span>用户昵称:</span>
      <el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称" v-model="nickName">
      </el-input>
    </div>
    <div style="display: inline-block;">
      <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询</el-button>
      <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
    </div>
    <el-table v-loading="tableDataLoading" :data="tableData.list">
      <el-table-column fixed prop="chatConversationId" label="id" width="80">
      </el-table-column>
      <el-table-column prop="userName" label="用户昵称" width="150">
        <template slot-scope="scope">
          <div style="color: #4f9dec;cursor: pointer;"
            @click="updates(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</div>
          <div style="color: #4f9dec;cursor: pointer;"
            @click="updates(scope.row.byUserId)">{{scope.row.byUserName ? scope.row.byUserName : '未绑定'}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="picture" label="用户头像">
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
          <div v-if="scope.row.byAvatar == null || scope.row.byAvatar == ''">
            暂无图片
          </div>
          <div v-else style="margin-top: 5px;">
            <el-popover placement="top-start" title="" trigger="hover">
              <img style="width: 50px; height: 50px" :src="scope.row.byAvatar" alt="" slot="reference">
              <img style="width: 200px; height: 200px" :src="scope.row.byAvatar" alt="">
            </el-popover>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="最新聊天内容">
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
             <img style="width: 200px; height: 200px" :src="'https://'+domain+'/emoji/'+scope.row.content" alt="">           </el-popover>
         </div>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="创建时间" width="160">
      </el-table-column>
      <!-- <el-table-column fixed='right' prop="contentCount" label="状态" width="100">
        <template slot-scope="scope">
          <span style="color: #4f9dec;" v-if="scope.row.contentCount==0 ">未读</span>
          <span v-if="scope.row.contentCount == 1 ">已读</span>
        </template>
      </el-table-column> -->
      <el-table-column fixed='right' label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="complainDetails(scope.row)">聊天内容
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center;margin-top: 10px;">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next"
        :total="tableData.totalCount">
      </el-pagination>
    </div>
    <!-- 聊天内容 -->
    <el-dialog title="聊天内容" :visible.sync="dialogFormVisible" center>
      <div style="margin:2% 0;display: inline-block;">
        <span>内容:</span>
        <el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入聊天内容" v-model="content">
        </el-input>
      </div>
      <div style="display: inline-block;">
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select1">查询</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans1">重置</el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="userData.list">
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
        <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
          :page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
          layout="total,sizes, prev, pager, next" :total="userData.totalCount">
        </el-pagination>
      </div>
    </el-dialog>
    <!-- 处理任务 -->
    <el-dialog title="处理任务" :visible.sync="dialogFormVisible1" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">任务状态：</span>
        <el-radio-group v-model="state">
          <el-radio :label="2">下架</el-radio>
          <el-radio :label="3">不下架</el-radio>
        </el-radio-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="complainTo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        limit: 10,
        page: 1,
        userId: '',
        content: '',
        nickName:'',
        form: {
          id: '',
          content: '',
          picture: '',
          state: ''
        },
        state: '',
        imgs: [],
        formLabelWidth: '200px',
        activeName: 'first',
        tableDataLoading: false,
        dialogFormVisible: false,
        dialogFormVisible1: false,
        tableData: {},
        userData:{},
        chatConversationId:'',
		domain:'',
      }
    },
    methods: {
      handleSizeChange(val) {
        this.limit = val;
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val;
        this.dataSelect()
      },
      handleSizeChange1(val) {
        this.limit = val;
        this.neirongSelect(this.chatConversationId)
      },
      handleCurrentChange1(val) {
        this.page = val;
        this.neirongSelect(this.chatConversationId)
      },
      // 用户详情跳转
      updates(userid) {
        this.$router.push({
          path: '/userDetail',
          query: {
            userId: userid
          }
        })
      },
      //删除任务投诉
      deleteStair(row) {
        let delid = row.id
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`helpClassify/deleteClassifyById/?id=${delid}`),
            method: 'post',
            data: this.$http.adornData({})
          }).then(({
            data
          }) => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.name = '';
                this.classifySelect()
              }
            })
          })
        }).catch(() => {})
      },
      // 查询
      select() {
				this.page = 1
        this.dataSelect()
      },
      // 重置
      cleans() {
				this.page = 1
        this.nickName = ''
        this.dataSelect()
      },
      // 聊天内容
      complainDetails(row) {
        this.chatConversationId = row.chatConversationId
        this.neirongSelect(this.chatConversationId)
        this.dialogFormVisible = true
      },
      select1(){
        this.page = 1
        this.neirongSelect(this.chatConversationId)
      },
      // 重置
      cleans1() {
        this.content = ''
         this.page = 1
        this.neirongSelect(this.chatConversationId)
      },
      // 处理
      amendNoticeTo() {
        this.dialogFormVisible1 = true
      },
      // 处理投诉
      complainTo() {
        this.$http({
          url: this.$http.adornUrl(`/helpComplaint/solveHelpComplaint/${this.form.id}/${this.state}`),
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
                this.dialogFormVisible = false
                this.dialogFormVisible1 = false
                this.dataSelect()
              }
            })
          }
        })
      },
      // 获取聊天会话列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('chat/selectChatConversationPage'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            // 'userId': this.userId
            'nickName': this.nickName
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData
        })
      },
      // 获取聊天内容列表
      neirongSelect(chatConversationId) {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('chat/selectChatContent'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'chatConversationId': this.chatConversationId,
            'content': this.content
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.userData = returnData
        })
      },
	  //删除聊天
	  deleteuser (row) {
	  	// let delid = row.userId
	  	this.$confirm(`确定删除此条信息?`, '提示', {
	  		confirmButtonText: '确定',
	  		cancelButtonText: '取消',
	  		type: 'warning'
	  	}).then(() => {
	  		this.$http({
	  			url: this.$http.adornUrl('chat/deleteChatContentById?chatContentId='+row.chatContentId),
	  			method: 'post',
	  			params: this.$http.adornData({})
	  		}).then(({data}) => {
	  			if(data.code==0){
	  				this.$message({
	  					message: '删除成功',
	  					type: 'success',
	  					duration: 1500,
	  					onClose: () => {
	  						this.neirongSelect(this.chatConversationId)
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
	  	})
	  },
    },
    mounted() {
		this.domain = window.location.hostname;
      this.dataSelect()
    }
  };
</script>

<style>

</style>
