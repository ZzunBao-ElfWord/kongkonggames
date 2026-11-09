import Vue from 'vue'
import App from './App'

import HttpRequest from './common/httpRequest'
import HttpCache from './common/cache'
import queue from './common/queue'
// #ifdef APP
// 引入腾讯trtc插件
import TrtcCloud from "@/TrtcCloud/lib/index"; // 将 TrtcCloud 引入到代码中
import {
	TRTCAppScene,
	TRTCRoleType
} from '@/TrtcCloud/lib/TrtcDefines';
import {
	TRTCAudioQuality
} from '@/TrtcCloud/lib/TrtcDefines';

//创建一个trtc的单例
const trtcCloud = TrtcCloud.createInstance()
// 将trtcCloud单利添加到Vue的原型上
Vue.prototype.$trtcCloud = trtcCloud;
// 将TRTCAppScene添加到Vue的原型上
Vue.prototype.$TRTCAppScene = TRTCAppScene;
// 将TRTCRoleType添加到Vue的原型上
Vue.prototype.$TRTCRoleType = TRTCRoleType;
// 将TRTCAudioQuality添加到Vue的原型上
Vue.prototype.$TRTCAudioQuality = TRTCAudioQuality;
// #endif


Vue.config.productionTip = false
Vue.prototype.$Request = HttpRequest;
Vue.prototype.$queue = queue;

Vue.prototype.$Sysconf = HttpRequest.config;
Vue.prototype.$SysCache = HttpCache;




App.mpType = 'app'

// 引入全局uView
import uView from "uview-ui";
import jsfunRecord from "./components/jc-record/jc-record.vue";

import loadMore from "@/components/uni-load-more/uni-load-more.vue";
Vue.component('loadMore', loadMore)
Vue.use(uView);

import popup from '@/components/popup.vue';
// vue.component('popup',popup)

//全局注册弹窗组件
import {
	Pmessage
} from '@/components/massage/message.js'
//js调用
Vue.prototype.$Pmessage = Pmessage

const app = new Vue({
	...App
})

app.$mount()