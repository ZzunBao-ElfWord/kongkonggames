<template>
  <div>
    <div>
      <div style="margin:1% 0;display: inline-block;float: right;">
        <el-button size="mini" style="margin-right: 10px;" type="primary" :disabled="!isAuth('memberDetails:add')" icon="document" @click="addNotice">添加会员
        </el-button>
      </div>
    </div>
    <el-table v-loading="tableDataLoading" :data="configData.list">
      <el-table-column prop="id" label="编号" width="80">
      </el-table-column>
      <el-table-column prop="vipName" label="会员名称">
      </el-table-column>
      <el-table-column prop="money" label="会员价格">
      </el-table-column>
    <!--  <el-table-column prop="award" label="邀请赏金" width="150">
      </el-table-column> -->
      <el-table-column prop="vipNameType" label="会员等级" width="150">
        <template slot-scope="scope">
          <span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.vipNameType == 0">月</span>
          <span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.vipNameType == 1">季</span>
		  <span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.vipNameType == 2">年</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed='right' width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" :disabled="!isAuth('memberDetails:update')" @click="amendone(scope.$index, scope.row)">编辑
          </el-button>
          <el-button size="mini" type="danger" :disabled="!isAuth('memberDetails:delete')" @click="deletes(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center;margin-top: 10px;">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="[5, 10, 15, 20]"
        :page-size="size" :current-page="page" layout="total,sizes, prev, pager, next,jumper" :total="configData.totalCount">
      </el-pagination>
    </div>
    <el-dialog title="添加会员" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 20px;">
        <span style="width: 200px;display: inline-block;text-align: right;">会员类型：</span>
        <el-radio-group v-model="vipNameType">
          <el-radio :label="0">月会员</el-radio>
          <el-radio :label="1">季会员</el-radio>
		  <el-radio :label="2">年会员</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 20px;">
        <span style="width: 200px;display: inline-block;text-align: right;">会员名称：</span>
        <el-input style="width:50%;" v-model="vipName" placeholder="请输入会员名称"></el-input>
      </div>
      <div style="margin-bottom: 20px;">
        <span style="width: 200px;display: inline-block;text-align: right;">会员价格：</span>
        <el-input style="width:50%;" v-model="money" type="number" placeholder="请输入会员价格" min="0" onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
      </div>
      <!-- <div style="margin-bottom: 20px;">
        <span style="width: 200px;display: inline-block;text-align: right;">邀请赏金：</span>
		<el-input style="width:50%;" v-model="award" type="number" placeholder="请输入邀请赏金" min="0" onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
      </div> -->
			<!-- <el-input-number v-model="award" style="width:50%;" :precision="2" :step="0.1" :min="0"
			  placeholder="请输入邀请赏金"></el-input-number> -->



      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 修改配置弹框 -->
    <el-dialog title="编辑会员配置" :visible.sync="dialogFormVisible1" center>
      <el-form :model="form">
		  <el-form-item label="是否有专属标识：" :label-width="formLabelWidth">
		    <el-radio-group v-model="form.vipNameType">
		      <el-radio :label="0">月会员</el-radio>
		      <el-radio :label="1">季会员</el-radio>
		      <el-radio :label="2">年会员</el-radio>
		    </el-radio-group>
		  </el-form-item>
        <el-form-item label="会员名称：" :label-width="formLabelWidth">
          <el-input v-model="form.vipName" style="width:65%;"></el-input>
        </el-form-item>
        <el-form-item label="会员价格：" :label-width="formLabelWidth">
          <el-input v-model="form.money" type="number" style="width:65%;" min="0" onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
        </el-form-item>
		<!-- <el-form-item label="邀请赏金：" :label-width="formLabelWidth">
		  <el-input v-model="form.award" style="width:65%;" type="number" min="0" onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"
		    placeholder="请输入邀请赏金"></el-input>
		</el-form-item> -->
        <!-- <el-form-item label="邀请赏金：" :label-width="formLabelWidth">
          <el-input-number v-model="form.award" style="width:65%;" :precision="2" :step="0.1" :min="0"
            placeholder="请输入邀请赏金 例0.1"></el-input-number>
        </el-form-item> -->



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="amendoneNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        size: 10,
        page: 1,
        totalnum: 0,
        totalnum2: 0,
        ids: '',
        content: '',
        activeName: 'first',
        tableDataLoading: false,
        dialogFormVisible: false,
        dialogFormVisible1: false,
        dialogFormVisible2: false,
        dialogFormVisible3: false,
        memberGrade: '',
        vipName: '',
        money: '',
        award: '',
        vipNameType: 0,
        formLabelWidth: '200px',
        checkBoxData: [], //多选框选择的值
        tableData: [],
        configData: {},
        form: {
          id: '',
          vipName: '',
          money: 0.00,
          award: 0.00,
          vipNameType: 0,
        },
        form2: {
          picture: '',
        },
      }
    },
    methods: {
      changeInput() {
        var pattern = /^[1-9][1-9]*$/ // 正整数的正则表达式
        // 不符合正整数时
        if (!pattern.test(this.memberGrade)) {
          // input 框绑定的内容为空
          this.memberGrade = ''
        }
      },
      deletes(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('vipDetails/deleteVipDetails'),
            method: 'post',
            params: this.$http.adornParams({
              'id': row.id
            })
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
      handleAvatarSuccess1(file) {
        this.memberIcon = file.data;
      },
      handleAvatarSuccess2(file) {
        this.memberImg = file.data;
      },
      handleAvatarSuccess3(file) {
        this.form.memberIcon = file.data;
      },
      handleAvatarSuccess4(file) {
        this.form.memberImg = file.data;
      },
      handleSizeChange(val) {
        this.size = val
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.dataSelect()
      },
      // 图片预览
      lookMember(rows) {
        this.dialogFormVisible2 = true
        this.form2.picture = rows.picture
      },
      selectTrigger() {
        this.page = 1
        this.size = 5
        this.dataSelect()
      },
      addNotice() {
        this.dialogFormVisible = true
      },
      //添加会员配置
      releasNoticeTo() {

        if (this.vipName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入会员名称',
            type: 'warning'
          });
          return
        }

        if (this.money == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入会员价格',
            type: 'warning'
          });
          return
        }
        // if (this.award == '') {
        //   this.$notify({
        //     title: '提示',
        //     duration: 1800,
        //     message: '请输入邀请赏金',
        //     type: 'warning'
        //   });
        //   return
        // }
        this.$http({
          url: this.$http.adornUrl('vipDetails/insertVipDetails'),
          method: 'post',
          data: this.$http.adornData({
            'vipName': this.vipName,
            'money': this.money,
            // 'award': this.award,
            'vipNameType': this.vipNameType,
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
              this.vipName = ''
              this.money = ''
              this.award = ''
              this.vipNameType = 0
              this.dataSelect()
            }
          })
        })
      },
      // 修改配置弹框
      amendone(index, rows) {
        this.dialogFormVisible1 = true
        this.form.id = rows.id
        this.form.vipName = rows.vipName
        this.form.money = rows.money
        this.form.award = rows.award
        this.form.vipNameType = rows.vipNameType

      },
      // 修改配置
      amendoneNoticeTo() {

        if (this.form.vipName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入会员名称',
            type: 'warning'
          });
          return
        }
        if (this.form.money == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入会员价格',
            type: 'warning'
          });
          return
        }

        // if (this.form.award == '') {
        //   this.$notify({
        //     title: '提示',
        //     duration: 1800,
        //     message: '请输入邀请赏金',
        //     type: 'warning'
        //   });
        //   return
        // }

        this.$http({
          url: this.$http.adornUrl('vipDetails/updateVipDetails'),
          method: 'post',
          data: this.$http.adornData({
            'id': this.form.id,
            'vipName': this.form.vipName,
            'money': this.form.money,
            // 'award': this.form.award,
            'vipNameType': this.form.vipNameType,
          })
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
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
            this.$message.error(data.msg)
          }
        })
      },
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        let page = this.page
        this.$http({
          url: this.$http.adornUrl('vipDetails/selectVipDetailsList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'limit': this.size,
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.configData = returnData
          this.totalnum2 = returnData.totalElements
        })
      },
    },
    mounted() {
      this.dataSelect()
    }
  }
</script>

<style>
</style>
