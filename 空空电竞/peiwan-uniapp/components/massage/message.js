import Vue from 'vue'
import Massage from './massage.vue'

const MessageConstructor = Vue.extend(Massage); //注册实例

export const Pmessage = (options,methods = {}) => {
  const instance = new MessageConstructor(
    {
  //   el: document.createElement('view'),
    data: options,//数据
    methods //方法
  }
  ).$mount();//挂载实例
  document.body.appendChild(instance.$el);
}