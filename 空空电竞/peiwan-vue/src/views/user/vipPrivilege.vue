<template>
  <div>
    <div style="float: right;margin-right:2%;">
      <el-button style='margin: 10px 0;' :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
        icon="document" @click="addNotice">添加会员特权</el-button>
    </div>
    <el-table v-loading="tableDataLoading" :data="tableData.list">
      
      <el-table-column fixed prop="memberId" label="编号" width="50">
      </el-table-column>
      <el-table-column prop="memberImg" label="图片">
        <template slot-scope="scope">
          　　<img :src="scope.row.memberImg" width="60" height="60" />
        </template>
      </el-table-column>
      <el-table-column prop="memberName" label="特权名称">
      </el-table-column>
      <el-table-column prop="sort" label="排序">
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
            @click="amendBanner(scope.$index, scope.row)">修改
          </el-button>
          <el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')" @click="deletes(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center;margin-top: 10px;">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next,jumper"
        :total="tableData.totalCount">
      </el-pagination>
    </div>
    <el-dialog :title="title" :visible.sync="dialogFormVisible" center>
    	<div style="margin-bottom: 10px;display: flex;">
    		<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
    		<div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
    			<el-upload
    				class="avatar-uploader"
    				v-model="memberImg"
    				:action="$http.adornUrl('alioss/upload')"
    				:show-file-list="false"
    				:on-success="handleAvatarSuccess"
    				>
    				<img v-if="memberImg" :src="memberImg" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;"/>
    				<i v-else class="el-icon-plus avatar-uploader-icon"></i>
    			</el-upload>
    		</div>
    	</div>
    	<div style="margin-bottom: 10px;">
    		<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
    		<el-input style="width:50%;" v-model="memberName" placeholder="请输入名称"></el-input>
    	</div>
      <div style="margin-bottom: 10px;">
      	<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
      	<el-input style="width:50%;" v-model="sort" placeholder="请输入排序" onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        page:1,
        limit:10,
        tableDataLoading: true,
        dialogFormVisible:false,
        tableData: [],
        memberName:'',
        memberImg:'',
        memberId:'',
        sort:'',
        title:'添加'
      }
    },
    methods: {
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('member/selectMemberPage'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit
          })
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
            console.log('会员列表',data)
            this.tableDataLoading = false
            let returnData = data.data;
            this.tableData = returnData;
          }

        })
      },
      //上传成功
      handleAvatarSuccess(file) {
      	this.memberImg= file.data
      },
      addNotice(){
      	this.dialogFormVisible = true
      },
      handleCurrentChange(val){
        this.page = val
        this.dataSelect()
      },
      handleSizeChange(val){
        this.limit = val
        this.dataSelect()
      },
      // 添加banner图
      addNoticeTo(){
      	if (this.memberImg == '') {
      	    this.$notify({
      	        title: '提示',
      	        duration: 1800,
      	        message: '请上传图片',
      	        type: 'warning'
      	    });
      	    return
      	}
      	if (this.memberName == '') {
      	    this.$notify({
      	        title: '提示',
      	        duration: 1800,
      	        message: '请输入名称',
      	        type: 'warning'
      	    });
      	    return
      	}
      	this.classify = 1
        if(this.title=='添加'){
          var url = 'member/insertMember'
        }else{
          var url = 'member/updateMember'
        }
      	this.$http({
      		url: this.$http.adornUrl(url),
      		method: 'post',
      		data: this.$http.adornData({
      			'memberImg':this.memberImg,
      			'memberId':this.memberId,
      			'memberName':this.memberName,
      			'sort':this.sort
      		})
      	}).then(({data}) => {
      		this.dialogFormVisible = false
      		this.$message({
      		  message: '操作成功',
      		  type: 'success',
      		  duration: 1500,
      		  onClose: () => {
      		  this.memberImg = ''
      			this.memberId = ''
      			this.memberName=''
      			this.sort = ''
            this.title ='添加'
      		  this.dataSelect()
      		  }
      		})
      	})
      },
      // 修改
      amendBanner(index,rows,a){
      	if(a==1){
      		this.pl = 1
      	}else{
      		this.pl = 0
      	}
      	this.dialogFormVisible = true
      	this.id = rows.id
      	this.memberImg = rows.memberImg
      	this.memberId = rows.memberId
      	this.sort = rows.sort
      	this.memberName = rows.memberName
        this.title = '修改'
      },
      // 修改
      amendNoticeTo(){
      	this.$http({
      	  url: this.$http.adornUrl(`?memberId=${this.memberId}&memberImg=${this.memberImg}&sort=${this.sort}&memberName=${this.memberName}`),
      	  method: 'post',
      	  data: this.$http.adornData({
      	  })
      	}).then(({data}) => {
      		this.dialogFormVisible = false
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
      //删除一级
      deletes(row) {
        let delid = row.memberId
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`member/deleteMemberById/?memberId=${delid}`),
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
                this.dataSelect()
              }
            })
          })
        }).catch(() => {})
      },
    },
    mounted() {
      this.dataSelect()
    }
  }
</script>

<style>
</style>
