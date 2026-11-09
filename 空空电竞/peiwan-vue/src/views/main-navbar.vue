<template>
  <nav class="site-navbar" :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand">

        <a class="site-navbar__brand-lg" href="javascript:;">{{entryName}}</a>
        <a class="site-navbar__brand-mini" href="javascript:;">
          <img src="~@/assets/img/avatar.png" style="width: 30px;height: 30px; border-radius: 50%;">
        </a>
      </h1>
    </div>
    <div class="site-navbar__body clearfix">
      <el-menu class="site-navbar__menu" mode="horizontal">
        <el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
          <icon-svg name="zhedie"></icon-svg>
        </el-menu-item>
      </el-menu>
      <el-menu class="site-navbar__menu site-navbar__menu--right" mode="horizontal">
        <el-menu-item index="1" style="position: relative;" @click="$router.push({ name: 'vueMchat' })">
          <template slot="title">
            <span class="left-number" v-if="chatNum">{{chatNum}}</span>
            <icon-svg name="tixing" class="el-icon-setting" style="font-size: 28px;"></icon-svg>
            <!-- 消息提示 -->
            <audio id="audio" controls  preload hidden="hidden" autoplay="autoplay" v-if="chatNum>0">
              <source src="https://pw.xianmxkj.com/file/uploadPath/2022/01/19/0753211f78d718d44ee6372e33eae9ee.mp3" type="audio/mpeg"></audio>
          </template>
        </el-menu-item>
        <el-menu-item class="site-navbar__avatar" index="3">
          <el-dropdown :show-timeout="0" placement="bottom">
            <span class="el-dropdown-link">
              <span style="margin-right: 10px;">{{ username ? username : '' }} <span
                  v-if="username">,欢迎您登录</span></span>
              <img src="~@/assets/img/avatar.png" :alt="userName">
            </span>
            <el-dropdown-menu slot="dropdown">
              <!-- <el-dropdown-item @click.native="updatePasswordHandle()">修改密码</el-dropdown-item> -->
              <el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 弹窗, 修改密码 -->
    <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
  </nav>
</template>

<script>
  import UpdatePassword from './main-navbar-update-password'
  import {
    clearLoginInfo
  } from '@/utils'
	import { entryName } from '@/utils/httpRequest'
  export default {
    data() {
      return {
        updatePassowrdVisible: false,
        username: '',
        intervalId: 0,
        storeId: 0,
        isshow: false,
        chatNum: 0,
		errCount: 0,
		code:0,
		entryName:'',
      }
    },
    components: {
      UpdatePassword
    },
    computed: {
      navbarLayoutType: {
        get() {
          return this.$store.state.common.navbarLayoutType
        }
      },
      sidebarFold: {
        get() {
          return this.$store.state.common.sidebarFold
        },
        set(val) {
          this.$store.commit('common/updateSidebarFold', val)
        }
      },
      mainTabs: {
        get() {
          return this.$store.state.common.mainTabs
        },
        set(val) {
          this.$store.commit('common/updateMainTabs', val)
        }
      },
      userName: {
        get() {
          return this.$store.state.user.name
        }
      },
    },
    methods: {
      // 修改密码
      updatePasswordHandle() {
        this.updatePassowrdVisible = true
        this.$nextTick(() => {
          this.$refs.updatePassowrd.init()
        })
      },
      // 退出
      logoutHandle() {
        this.$confirm(`确定进行[退出]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$cookie.set('token', '')
          this.$cookie.set('userId', '')
          // sessionStorage.clear()
          clearInterval(this.chatNum2)
          clearInterval(this.chatNum)
          //router.options.isAddDynamicMenuRoutes = false;
          this.$router.push({
            name: 'login'
          })

        }).catch(() => {})
      },

      // 获取用户信息
      userSelect() {
        let userId = this.$cookie.get('userId')
        this.$http({
          url: this.$http.adornUrl(`sys/user/info`),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          let returnData = data.user;
          // if (returnData == null) {
          //   this.$cookie.set('token', '')
          //   this.$cookie.set('userId', '')
          //   sessionStorage.clear()
          //   this.$notify({
          //     title: '提示',
          //     duration: 1800,
          //     message: '用户信息失效，退出登录',
          //     type: 'warning'
          //   });
          //   this.$router.push({
          //     path: '/login'
          //   });
          // } else {
            this.username = returnData.username
          // }
        })
      },
      // 获取聊天消息
      chatSelect() {
        this.$http({
          url: this.$http.adornUrl('chats/count'),
          method: 'get',
          params: this.$http.adornParams({
            'storeId': this.storeId
          })
        }).then(({
          data
        }) => {
			this.code = data.code
          // let returnData = data.data;
          // this.chatNum = returnData
          // if (this.chatNum == 0) {
          //   this.isshow = false
          // } else {
          //   // this.chatAudio();
          //   // let audio = new Audio()
          //   // audio.src = require('../assets/img/notify.mp3')
          //   // audio.play();
          //   //如果不是当前页面，标题栏闪动+消息提示
          //   if (document.hidden) {
          //     var options = {
          //       body: '您有新的未读消息，请及时处理',
          //       silent: true
          //     }
          //     var notification = new Notification('消息通知', options);
          //     notification.onclick = function() {
          //       window.open(window.location.href, '_blank');
          //     }
          //     //标题栏闪动
          //     var defaultTitle = document.title;
          //     if (that.isReceive) {
          //       return;
          //     } else {
          //       that.isReceive = true;
          //     }
          //     that.timer = setInterval(function() {
          //       var title = document.title;
          //       if (document.hidden && that.isReceive) {
          //         if (/你有新消息/.test(title) == false) {
          //           document.title = '【你有新消息】';
          //         } else {
          //           document.title = defaultTitle;
          //         }
          //       } else {
          //         that.isReceive = false;
          //         document.title = defaultTitle;
          //       }
          //     }, 500);
          //   }
          //   this.isshow = true
          // }
		  console.log('this.chatNum',data.data)
		  if (data.data > 0) {
		    if(this.errCount!=data.data){
		      this.$notify.info({
		        title: '消息',
		        dangerouslyUseHTMLString: true,
		        message: '您有' + data.data + '条未读消息。',
		        offset: 80,
		        duration: 2500,
		        type: "warning"
		      });
		      this.chatNum = data.data
		      this.errCount = data.data
		    }
		  }else{
		    this.chatNum = 0
		    this.errCount = 0
		  }
		  if (this.chatNum == 0) {
		    this.isshow = false
		  } else {
		    this.isshow = true
		  }

        })
      },
      //申请浏览器通知权限，具体参见上面文档链接
      notification() {
        let that = this;
        if (!("Notification" in window)) {
          console.log("浏览器不支持消息通知");
          return;
        }
        Notification.requestPermission(function(permission) {});
      },
      // 获取聊天消息
      chatSelect2() {
        this.$http({
          url: this.$http.adornUrl('chats/count'),
          method: 'get',
          params: this.$http.adornParams({
            'storeId': this.storeId
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          if (data.data > 0) {
            this.$notify.info({
              title: '消息',
              dangerouslyUseHTMLString: true,
              message: '您有' + data.data + '条未读消息。',
              offset: 80,
              duration: 2500,
              type: "warning"
            });
          }
        })
      },
	  aplayAudio() {
	    console.log('语音提示')
	    document.getElementById('audio').play()

	  },
    },
    mounted() {
			this.entryName = entryName
      this.userSelect()
      this.chatSelect()
      this.chatSelect2()
      this.notification();
			var  menuList = sessionStorage.getItem('menuList')
			 				menuList = JSON.parse(menuList)
			 			if(menuList.length>0){
			 				console.log('-=============',menuList)
			 				if(menuList[0].menuId!=33){
			 					
			 					 this.$router.replace({
			 						name: 'welcome'
			 					})
			 				}
			 			}

    },
    created() {
      this.chatNum = setInterval(() => {
		  var token = this.$cookie.get('token')
		if(token!==''){
			this.chatSelect()
		}else{
			clearInterval(this.chatNum)
		}
      }, 6000)
    },
    beforeDestroy() {
      clearInterval(this.chatNum)
    },
	watch: {
	  'chatNum': function() {
	    if(this.chatNum>0){
	      this.aplayAudio()
	    }
	  },
	}
  }
</script>
<style scoped="scoped">
  .site-navbar__avatar .el-dropdown-link>img {
    height: 36px;
  }

  .left-number {
    position: absolute;
    top: 0;
    right: 20px;
    width: 18px;
    height: 18px;
    line-height: 18px;
    border-radius: 50%;
    background: #ff4949;
    color: #ffffff;
    text-align: center;
    font-size: 10px;
    font-family: PingFang SC, PingFang SC-Regular;
    font-weight: 400;
    margin-top: 5px;
  }
</style>
