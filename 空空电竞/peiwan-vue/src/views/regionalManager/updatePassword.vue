<template>
  <div>
    <div>

      <div style="margin-bottom: 20px;">
        <span style="width: 200px;display: inline-block;text-align: right;">原密码：</span>
        <el-input style="width:50%;" v-model="password" type="password" placeholder="请输入原密码"></el-input>
      </div>
      <div style="margin-bottom: 20px;">
        <span style="width: 200px;display: inline-block;text-align: right;">新密码：</span>
        <el-input style="width:50%;" v-model="newPassword" type="password" placeholder="请输入新密码" ></el-input>
      </div>
	  <div style="margin-bottom: 20px;">
	    <span style="width: 200px;display: inline-block;text-align: right;">确认密码：</span>
	    <el-input style="width:50%;" v-model="qurrenPassword" type="password" placeholder="请输入确认密码" ></el-input>
	  </div>
      <div slot="footer" class="dialog-footer" style="margin-left: 200px;">
        <el-button type="primary" @click="releasNoticeTo()" :disabled="!isAuth('updatePasswordRm:update')">确 定</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        password:'',
		newPassword:'',
		qurrenPassword:'',
      }
    },
    methods: {

      //添加会员配置
      releasNoticeTo() {

        if (this.password == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入原密码',
            type: 'warning'
          });
          return
        }

        if (this.newPassword == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入新密码',
            type: 'warning'
          });
          return
        }
		if(this.qurrenPassword==''){
			this.$notify({
			  title: '提示',
			  duration: 1800,
			  message: '请输入确认密码',
			  type: 'warning'
			});
			return
		}else{
			if (this.newPassword != this.qurrenPassword) {
			  this.$notify({
			    title: '提示',
			    duration: 1800,
			    message: '新密码与确认密码不一致，请重新输入',
			    type: 'warning'
			  });
			  return
			}
		}

        this.$http({
          url: this.$http.adornUrl('sys/user/password'),
          method: 'post',
          data: this.$http.adornData({
            'password': this.password,
            'newPassword': this.newPassword
          })
        }).then(({
          data
        }) => {
          if(data.code==0){
			  var that  = this
			  that.$message({
			    message: '操作成功',
			    type: 'success',
			    duration: 1500,
			    onClose: () => {
			      that.password = ''
			      that.newPassword = ''
				  that.qurrenPassword = ''
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
      },

    },
    mounted() {
    }
  }
</script>

<style>
</style>
