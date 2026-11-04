<template>
  <div>
    <div style="float: right;margin-right:2%;">
      <el-button style="margin: 10px 0;" :disabled="!isAuth('materialLink:add')" size="mini" type="primary"
        icon="document" @click="advertNotice">添加金句</el-button>
    </div>
    <el-table v-loading="tableDataLoading" :data="guangaoData.content">
      <el-table-column fixed prop="cateId" label="编号" width="80"></el-table-column>
      <el-table-column prop="content" label="金句名称">
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
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
    <!-- 添加/修改金句 -->
    <el-dialog :title="titles" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">金句名称：</span>
        <el-input style="width:50%;" v-model="content" placeholder="请输入金句名称" type="text"></el-input>
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
        classify:2,
        cateId:'',
        content:'',
        createTime:'',
        titles:'添加金句',
        formLabelWidth: '200px',
        tableDataLoading: false,
        dialogFormVisible1: false,
        dialogFormVisible: false,
        guangaoData: {},
      }
    },
    methods: {

      handleSizeChange(val) {
        this.size = val
        this.guangaoSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.guangaoSelect()
      },
      // 添加话题弹框
      advertNotice() {
        this.titles = '添加金句'
        this.content = ''
        this.cateId = ''
        this.createTime = ''
        this.dialogFormVisible = true
      },
      // 添加话题
      advertNoticeTo() {
        if (this.content == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入金句名称',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(`gambit/saveCate`),
          method: 'post',
          data: this.$http.adornData({
            'content':this.content,
            'cateId':this.cateId,
            'classify':2,
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
              this.guangaoSelect()
            }
          })
        })
      },
      // 点击修改
      advertUpdates(rows) {
        this.titles = '修改金句'
        this.content = rows.content
        this.cateId = rows.cateId
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
            url: this.$http.adornUrl(`gambit/deleteCateById`),
            method: 'post',
            params: this.$http.adornParams({
              'cateId':row.cateId
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
          url: this.$http.adornUrl('gambit/selectCatePage'),
          method: 'get',
          params: this.$http.adornParams({
            'page':this.page-1,
            'size':this.size,
            'classify':2
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.guangaoData = returnData
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
