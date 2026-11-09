<template>
  <div>
    <el-table v-loading="tableDataLoading" :data="guangaoData.content">
      <el-table-column fixed prop="cateTalkId" label="编号" width="80"></el-table-column>
      <el-table-column prop="nickName" label="发布用户" width="150">
        <template slot-scope="scope">
          <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">{{ scope.row.nickName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" width="180">
      </el-table-column>
      <el-table-column prop="content" label="内容">
        <template slot-scope="scope">
          <div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">{{scope.row.content}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="img" label="图片" width="100">
        <template slot-scope="scope">
          <img :src="scope.row.img" alt="" width="80px" height="80">
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
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
  </div>
</template>

<script>
  export default {
    data() {
      return {
        size:10,
        page:1,
        imgUrl: '',
        linkUrl: '',
        type: 3,
        form: {
          id: '',
          imgUrl: '',
          linkUrl: '',
          createAt: ''
        },
        formLabelWidth: '200px',
        tableDataLoading: false,
        dialogFormVisible1: false,
        dialogFormVisible: false,
        guangaoData: {},
      }
    },
    methods: {
      handleAvatarSuccess(file) {
        this.imgUrl = file.data
        this.linkUrl = this.imgUrl
      },
      handleAvatarSuccess1(file) {
        this.form.imgUrl = file.data
        this.form.linkUrl = this.form.imgUrl
      },
      handleSizeChange(val) {
        this.size = val
        this.guangaoSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.guangaoSelect()
      },
      handleSizeChange1(val) {
        this.size = val
        this.guangaoSelect()
      },
      handleCurrentChange1(val) {
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
        this.dialogFormVisible = true
      },
      // 添加话题
      advertNoticeTo() {
        if (this.linkUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入跳转链接',
            type: 'warning'
          })
          return
        }
        if (this.imgUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传话题图',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(`banner/update/-1?url=${this.linkUrl}&image=${this.imgUrl}&type=${this.type}`),
          method: 'post',
          data: this.$http.adornData({})
        }).then(({
          data
        }) => {
          this.dialogFormVisible = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.imgUrl = ''
              this.linkUrl = ''
              this.guangaoSelect()
            }
          })
        })
      },
      // 谈论列表
      talkAbout(row) {
        this.dialogFormVisible1 = true
      },
      // 点击修改
      advertUpdates(rows) {
        this.form.id = rows.id
        this.form.imgUrl = rows.image_url
        this.form.linkUrl = rows.url
        this.dialogFormVisible1 = true
      },
      // 确定修改
      amendadvertTo() {
        if (this.form.linkUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入跳转链接',
            type: 'warning'
          })
          return
        }
        if (this.form.imgUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传话题图',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(
              `banner/update/${this.form.id}?url=${this.form.linkUrl}&image=${this.form.imgUrl}&type=${this.type}`
              ),
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
              this.guangaoSelect()
              this.dialogFormVisible1 = false
            }
          })
        })
      },
      // 删除广告
      advertdeletes(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`gambit/deleteCateTalkById`),
            method: 'post',
            params: this.$http.adornParams({
              'cateTalkId':row.cateTalkId
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
          url: this.$http.adornUrl('gambit/selectCateTalkPage'),
          method: 'get',
          params: this.$http.adornParams({
            'page':this.page-1,
            'size':this.size
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.guangaoData = returnData
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
