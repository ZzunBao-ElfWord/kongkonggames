<template>
  <div>
    <div style = "margin:2% 0;display: inline-block;">
      <span>是否推荐：</span>
      <el-select v-model = "isChoicenesType" style = "width:150px;" @change = "selectTrigger(isChoicenesType)">
        <el-option v-for = "item in isChoicenesTypeNum" :key = "item.value" :label = "item.label" :value = "item.value">
        </el-option>
      </el-select>&nbsp;&nbsp;&nbsp;&nbsp;
      <span>状态：</span>
      <el-select v-model = "type" style = "width:150px;" @change = "selectTrigger(type)">
        <el-option v-for = "item in typeNum" :key = "item.value" :label = "item.label" :value = "item.value">
        </el-option>
      </el-select>&nbsp;&nbsp;&nbsp;&nbsp; 
    </div>
    <div style = "display: inline-block;">
      <el-button style = "margin-left:15px;" size = "mini" type = "primary" icon = "document" @click = "select">查询</el-button>
      <el-button style = "margin-left:15px;" size = "mini" type = "primary" icon = "document" @click = "cleans">重置</el-button>
    </div>
    <div style="float: right;margin-right:2%;">
      <el-button style="margin: 10px 0;" :disabled="!isAuth('materialLink:add')" size="mini" type="primary"
        icon="document" @click="advertNotice">添加话题</el-button>
    </div>
    <el-table v-loading="tableDataLoading" :data="guangaoData.content">
      <el-table-column fixed prop="gambitId" label="编号" width="80"></el-table-column>
      <el-table-column prop="title" label="标题名称">
      </el-table-column>
      <el-table-column prop="titleImg" label="标题图片" width="100">
        <template slot-scope="scope">
          <img :src="scope.row.titleImg" alt="" width="80px" height="80">
        </template>
      </el-table-column>
      <el-table-column prop="content" label="内容">
      </el-table-column>
      <el-table-column prop="imgUrl" label="内容图片" width="100">
        <template slot-scope="scope">
          <img :src="scope.row.contentImg" alt="" width="80px" height="80">
        </template>
      </el-table-column>
      <el-table-column prop="imgUrl" label="是否推荐" width="150" align="center">
        <template slot-scope="scope">
         <el-switch v-model = "scope.row.isChoiceness" @change = "change(scope.row.isChoiceness,scope.row.gambitId)" :active-value = "openValue"
                    :inactive-value = "closeValue" active-color = "#13ce66" inactive-color = "#ff4949">
         </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="imgUrl" label="状态" width="150" align="center">
        <template slot-scope="scope">
          <el-switch v-model = "scope.row.status" @change = "change1(scope.row.status,scope.row.gambitId)" :active-value = "openValue1"
                     :inactive-value = "closeValue1" active-color = "#13ce66" inactive-color = "#ff4949">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="counts" label="谈论次数"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="talkAbout(scope.row)">动态列表</el-button>
          <el-button style="margin: 10px 0;" size="mini" type="primary" :disabled="!isAuth('materialLink:update')"
            @click="advertUpdates(scope.row)">修改</el-button>
          <el-button size="mini" type="danger" :disabled="!isAuth('materialLink:delete')"
            @click="advertdeletes(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style = "text-align: center;margin-top: 10px;">
      <el-pagination @size-change = "handleSizeChange" @current-change = "handleCurrentChange" :page-sizes = "[5, 10, 15, 20]"
                     :page-size = "size" :current-page = "page" layout = "total,sizes, prev, pager, next,jumper"
                     :total = "guangaoData.totalElements">
      </el-pagination>
    </div>
    <!-- 添加话题 -->
    <el-dialog title="添加话题" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
        <el-input style="width:50%;" v-model="title" placeholder="请输入标题" type="text"></el-input>
      </div>
      <div style="margin-bottom: 10px;display: flex;">
        <span style="width: 200px;display: inline-block;text-align: right;">话题图片：</span>
        <div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
          <el-upload class="avatar-uploader" v-model="titleImg" :action="$http.adornUrl('alioss/upload')"
            :show-file-list="false" :on-success="handleAvatarSuccess">
            <img v-if="titleImg" :src="titleImg" class="avatar" style="border-radius: 6px;width:148px;height: 148px;" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">内容：</span>
        <el-input style="width:50%;" v-model="content" placeholder="请输入标题" type="text"></el-input>
      </div>
      <div style="margin-bottom: 10px;display: flex;">
        <span style="width: 200px;display: inline-block;text-align: right;">内容图片：</span>
        <div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
          <el-upload class="avatar-uploader" v-model="contentImg" :action="$http.adornUrl('alioss/upload')"
            :show-file-list="false" :on-success="handleAvatarSuccess1">
            <img v-if="contentImg" :src="contentImg" class="avatar" style="border-radius: 6px;width:148px;height: 148px;" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">是否推荐：</span>
        <el-radio-group v-model="isChoiceness">
            <el-radio :label="1">推荐</el-radio>
            <el-radio :label="2">普通</el-radio>
        </el-radio-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="advertNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        page:1,
        size:10,
        title:'', //标题
        titleImg: '', //标题图片
        isChoiceness: '',//是否推荐 1 精选 2 普通 0 全部
        status:'',//状态
        content:'',//内容
        contentImg:'',//内容图片
        gambitId:'',//话题id
        createTime:'',//创建时间
        isChoicenesType:0,
        isChoicenesTypeNum:[
          {
            value:0,
            label:'全部'
          },
          {
            value:1,
            label:'精选'
          },
          {
            value:2,
            label:'普通'
          },
        ],
        type: 0,
        typeNum:[
          {
            value:0,
            label:'全部'
          },
          {
            value:1,
            label:'正常'
          },
          {
            value:2,
            label:'下架'
          },
        ],
        openValue: 1,
        closeValue: 2,
        openValue1: 1,
        closeValue1: 2,
        tableDataLoading: false,
        dialogFormVisible1: false,
        dialogFormVisible: false,
        guangaoData: {},
      }
    },
    methods: {
      handleAvatarSuccess(file) {
        this.titleImg = file.data
      },
      handleAvatarSuccess1(file) {
        this.contentImg = file.data
      },
      handleSizeChange(val) {
        this.size = val
        this.guangaoSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.guangaoSelect()
      },
      // 刷新
      refresh() {
        this.choicenSelect()
        this.handpick()
      },
      // 添加话题弹框
      advertNotice() {
        this.content = ''
        this.contentImg = ''
        this.gambitId = ''
        this.isChoiceness = 0
        this.status = ''
        this.title = ''
        this.titleImg = ''
        this.createTime = ''
        this.dialogFormVisible = true
      },
      // 添加话题
      advertNoticeTo() {
        if (this.title == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入标题',
            type: 'warning'
          })
          return
        }
        if (this.titleImg == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传标题图片',
            type: 'warning'
          })
          return
        }
        if (this.content == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入内容',
            type: 'warning'
          })
          return
        }
        if (this.contentImg == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传内容图片',
            type: 'warning'
          })
          return
        }
        if (this.isChoiceness == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择是否推荐',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(`gambit/saveGambit`),
          method: 'post',
          data: this.$http.adornData({
            'content':this.content,
            'contentImg':this.contentImg,
            'gambitId':this.gambitId,
            'isChoiceness':this.isChoiceness,
            'status':this.status,
            'title':this.title,
            'titleImg':this.titleImg,
            'createTime':this.createTime
          })
        }).then(({
          data
        }) => {
          this.dialogFormVisible = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.title = ''
              this.titleImg = ''
              this.guangaoSelect()
            }
          })
        })
      },
      // 谈论列表
      talkAbout(row) {
        this.$router.push({
          path: '/dynamic',
          query: {
            gambitId: row.gambitId
          }
        })
      },
      // 点击修改
      advertUpdates(rows) {
        this.content = rows.content
        this.contentImg = rows.contentImg
        this.gambitId = rows.gambitId
        this.isChoiceness = rows.isChoiceness
        this.status = rows.status
        this.title = rows.title
        this.titleImg = rows.titleImg
        this.createTime = rows.createTime
        this.dialogFormVisible = true
      },

      // 删除广告
      advertdeletes(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`gambit/deleteGambitById`),
            method: 'post',
            params: this.$http.adornParams({
              'gambitId':row.gambitId
            })
          }).then(({
            data
          }) => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.guangaoSelect()
              }
            })
          })
        })
      },
      // 获取话题数据
      guangaoSelect() {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('gambit/selectGambitPage'),
          method: 'get',
          params: this.$http.adornParams({
            'page':this.page-1,
            'size':this.size,
            'isChoiceness':this.isChoicenesType,
            'status':this.type
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.guangaoData = returnData
        })
      },
      selectTrigger(type){
        this.guangaoSelect()
      },
      change (val, id) {
        this.$http({
          url: this.$http.adornUrl(`gambit/updateGambitIsChoiceness`),
          method: 'post',
           params: this.$http.adornParams({
            'gambitId':id
          })
        }).then(({data}) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.guangaoSelect()
            }
          })
        })
      },
      change1 (val, id) {
        this.$http({
          url: this.$http.adornUrl(`gambit/updateGambitStatus`),
          method: 'post',
           params: this.$http.adornParams({
            'gambitId':id
          })
        }).then(({data}) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.guangaoSelect()
            }
          })
        })
      },
    },
    mounted() {
      this.guangaoSelect()
    }
  }
</script>

<style>
  .customWidth {
    width: 80% !important;
  }
</style>
