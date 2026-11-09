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

//延迟创建 TRTC 原生实例：标准基座不包含 TRTC 原生插件，
//启动时直接 createInstance 会导致应用初始化中断、首页白屏。
//只有用户进入语音业务首次访问 $trtcCloud 时才创建。
let trtcCloud = null
function getTrtcCloud() {
	if (trtcCloud) return trtcCloud
	trtcCloud = TrtcCloud.createInstance()
	return trtcCloud
}
Object.defineProperty(Vue.prototype, '$trtcCloud', {
	configurable: true,
	get: getTrtcCloud
})
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