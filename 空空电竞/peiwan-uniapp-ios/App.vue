<script>
	export default {
		onLaunch: function() {
			let that = this;
			//预登录
			// #ifdef MP-WEIXIN
			if (!uni.getStorageSync('token')) {
				uni.login({
					provider: 'weixin',
					success: function(loginRes) {
						console.log(loginRes, '************')
						let data = {
							code: loginRes.code,
						}
						that.$Request.get('/app/Login/wxLogin', data).then(res => {
							if (res.code == 0) {
								uni.setStorageSync('openId', res.data.open_id)
								uni.setStorageSync('unionId', res.data.unionId)
								uni.setStorageSync('sessionkey', res.data.session_key)
								let sendData = {
									openId: uni.getStorageSync('openId'),
									unionId: uni.getStorageSync('unionId'),
									userName: '游客',
									avatar: '',
									sex: '1' //性别
								};
								let phoneNum = false
								uni.setStorageSync('sendDataList', sendData)
								that.$Request.getT('/app/common/type/188').then(ret => {
									if (ret.code == 0) {
										if (ret.data && ret.data.value && ret.data.value ==
											'是') {
											phoneNum = true;
											let flag = res.data.flag;
											if (flag == '1' && phoneNum) { //需要授权手机号则走手机号授权
												uni.setStorageSync('weixinPhone', true)
											} else { //不需要手机号则则直接走授权登录
												uni.setStorageSync('weixinPhone', false)
											}
										} else {
											uni.setStorageSync('weixinPhone', false)
										}
									}
								});

							}
						})

					}
				});
			}
			// #endif

			setInterval(d => { //定时器，定时去调取聊天未读消息
				let userId = uni.getStorageSync('userId')
				if (uni.getStorageSync('token')) {
					this.$Request.getMsg('/app/chat/selectChatCount').then(res => {
						if (res.code === 0) {
							let chatCount = res.data.chatCount
							let messageCount = res.data.messageCount
							let systemMessageCount = res.data.systemMessageCount
							let yueMessageCount = res.data.yueMessageCount
							uni.setStorageSync('messageCount', messageCount)
							uni.setStorageSync('systemMessageCount', systemMessageCount)
							uni.setStorageSync('yueMessageCount', yueMessageCount)

							let num = chatCount + messageCount + systemMessageCount + yueMessageCount

							this.$Request.getMsg('/app/chats/userCount?userId=' + userId).then(res => {
								if (res.status === 0) {
									res.data = res.data ? res.data : 0;
									uni.setStorageSync('adminmessageCount', res.data)
									num = parseInt(parseInt(num) + parseInt(res.data))
									if (num == 0) {
										uni.removeTabBarBadge({
											index: 3
										})
										return;
									}
									uni.setTabBarBadge({
										index: 3,
										text: num + ""
									})
								}
							});
						}
					});
				} else {
					//退出后清空一下徽标
					uni.removeTabBarBadge({
						index: 3
					})
				}
			}, 3000);

			// setInterval(d => { //定时器，定时去调取订单未读消息
			// 	let userId = uni.getStorageSync('userId')
			// 	if (userId) {
			// 		this.$Request.getT('/app/orders/selectMyOrdersCount').then(res => {
			// 			if (res.code === 0) {
			// 				let orderCount = res.data
			// 				uni.setStorageSync('orderCount', orderCount)
			// 				let ordeNum = res.data.count0 + res.data.count1 + res.data.count2 + res
			// 					.data.count4
			// 			}
			// 		});
			// 	}
			// }, 3000);


			// #ifdef MP-WEIXIN
			// 小程序更新
			const updateManager = uni.getUpdateManager();
			// 请求完新版本信息的回调
			updateManager.onCheckForUpdate(function(res) {
				if (res.hasUpdate) {
					updateManager.onUpdateReady(function(data) {
						// 新版本下载好，重启小程序
						updateManager.applyUpdate();
					})
				}
			})

			// 新版本下载失败
			updateManager.onUpdateFailed(function(res) {
				uni.showModal({
					title: '提示',
					content: '检查到有新版本，但下载失败，请检查网络设置',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							updateManager.applyUpdate();
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});

			})

			// #endif

			//#ifdef APP-PLUS
			// APP检测更新 具体打包流程可以参考：https://ask.dcloud.net.cn/article/35667
			plus.screen.lockOrientation('portrait-primary'); //竖屏正方向锁定
			//获取是否热更新过
			const updated = uni.getStorageSync('updated'); // 尝试读取storage

			if (updated.completed === true) {
				// 如果上次刚更新过
				// 删除安装包及安装记录
				console.log('安装记录被删除，更新成功');
				uni.removeSavedFile({
					filePath: updated.packgePath,
					success: res => {
						uni.removeStorageSync('updated');
					}
				});
			} else if (updated.completed === false) {
				uni.removeStorageSync('updated');
				plus.runtime.install(updated.packgePath, {
					force: true
				});
				uni.setStorage({
					key: 'updated',
					data: {
						completed: true,
						packgePath: updated.packgePath
					},
					success: res => {
						console.log('成功安装上次的更新，应用需要重启才能继续完成');
					}
				});
				uni.showModal({
					title: '温馨提示',
					content: '应用将重启以完成更新',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							plus.runtime.restart();
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});


			} else {
				//获取当前系统版本信息
				plus.runtime.getProperty(plus.runtime.appid, widgetInfo => {
					//请求后台接口 解析数据 对比版本
					that.$Request.getT('/app/user/selectNewApp').then(res => {
						console.log("/app/user/selectNewApp___:" + JSON.stringify(res));
						res = res.data[0];
						if (res.wgtUrl && widgetInfo.version < res.version) {
							let downloadLink = '';
							let androidLink = res.androidWgtUrl;
							let iosLink = res.iosWgtUrl;
							let ready = false;
							//校验是是不是热更新
							if (res.wgtUrl.match(RegExp(/.wgt/))) {
								// 判断系统类型
								if (plus.os.name.toLowerCase() === 'android') {
									console.log('安卓系统');
									if (androidLink && androidLink !== '#') {
										// 我这里默认#也是没有地址，请根据业务自行修改
										console.log('发现下载地址');
										// 安卓：创建下载任务
										if (androidLink.match(RegExp(/.wgt/))) {
											console.log('确认wgt热更新包');
											downloadLink = androidLink;
											ready = true;
										} else {
											console.log('安卓推荐.wgt强制更新，.apk的强制更新请您自行修改程序');
										}
									} else {
										console.log('下载地址是空的，无法继续');
									}
								} else {
									console.log('苹果系统');
									if (iosLink && iosLink !== '#') {
										// 我这里默认#也是没有地址，请根据业务自行修改
										console.log('发现下载地址');
										// 苹果(A)：进行热更新（如果iosLink是wgt更新包的下载地址）判断文件名中是否含有.wgt
										if (iosLink.match(RegExp(/.wgt/))) {
											console.log('确认wgt热更新包');
											downloadLink = iosLink;
											ready = true;
										} else {
											console.log('苹果只支持.wgt强制更新');
										}
									} else {
										console.log('下载地址是空的，无法继续');
									}
								}
								if (ready) {
									console.log('任务开始');
									let downloadTask = uni.downloadFile({
										url: downloadLink,
										success: res => {
											if (res.statusCode === 200) {
												// 保存下载的安装包
												console.log('保存安装包');
												uni.saveFile({
													tempFilePath: res.tempFilePath,
													success: res => {
														const packgePath = res
															.savedFilePath;
														// 保存更新记录到stroage，下次启动app时安装更新
														uni.setStorage({
															key: 'updated',
															data: {
																completed: false,
																packgePath: packgePath
															},
															success: () => {
																console
																	.log(
																		'成功保存记录'
																	);
															}
														});
														// 任务完成，关闭下载任务
														console.log(
															'任务完成，关闭下载任务，下一次启动应用时将安装更新'
														);
														downloadTask.abort();
														downloadTask = null;
													}
												});
											}
										}
									});
								} else {
									console.log('下载地址未准备，无法开启下载任务');
								}
							} else {
								//不是热更新是在线更新 校验是否强制升级
								if (res.method == 'true') {
									uni.showModal({
										confirmText: '立即更新',
										showCancel: false,
										title: '发现新版本',
										content: res.des,
										success: function(res) {
											if (res.confirm) {
												console.log('用户点击确定');
												that.$queue.showLoading('下载中...');
												if (uni.getSystemInfoSync().platform ==
													'android') {
													uni.downloadFile({
														url: androidLink,
														success: downloadResult => {
															if (downloadResult
																.statusCode ===
																200) {
																plus.runtime
																	.install(
																		downloadResult
																		.tempFilePath, {
																			force: false
																		},
																		d => {
																			console
																				.log(
																					'install success...'
																				);
																			plus.runtime
																				.restart();
																		},
																		e => {
																			console
																				.error(
																					'install fail...'
																				);
																		}
																	);
															}
														}
													});
												}
												if (uni.getSystemInfoSync().platform ==
													'ios') {
													plus.runtime.openURL(iosLink, function(
														res) {});
												}
											} else if (res.cancel) {
												console.log('用户点击取消');
											}
										}
									});

								} else {
									uni.showModal({
										// title: '温馨提示',
										// content: '应用将重启以完成更新',
										showModel: true,
										title: '发现新版本',
										confirmText: '立即更新',
										cancelText: '下次更新',
										content: res.des,

										success: function(res) {
											if (res.confirm) {
												console.log('用户点击确定');
												that.$queue.showLoading('下载中...');
												if (uni.getSystemInfoSync().platform ==
													'android') {
													uni.downloadFile({
														url: androidLink,
														success: downloadResult => {
															if (downloadResult
																.statusCode ===
																200) {
																plus.runtime
																	.install(
																		downloadResult
																		.tempFilePath, {
																			force: false
																		},
																		d => {
																			console
																				.log(
																					'install success...'
																				);
																			plus.runtime
																				.restart();
																		},
																		e => {
																			console
																				.error(
																					'install fail...'
																				);
																		}
																	);
															}
														}
													});
												}
												if (uni.getSystemInfoSync().platform ==
													'ios') {
													plus.runtime.openURL(iosLink, function(
														res) {});
												}
											} else if (res.cancel) {
												console.log('用户点击取消');
											}
										}
									});

								}
							}
						}
					});
				});
			}

			//#endif
		},
		onShow: function() {
			let that = this;
			//客服方式 1企业微信 2系统 3二维码客服
			that.$Request.getT('/app/common/type/618').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('SelKeFu', res.data.value)
					}
				}
			});
			//sdkAppId 814
			that.$Request.getT('/app/common/type/814').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('sdkAppId', res.data.value)
					}
				}
			});
			//客服AppId 617
			that.$Request.getT('/app/common/type/617').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('SelKeFuAppId', res.data.value)
					}
				}
			});
			//客服链接 616
			that.$Request.getT('/app/common/type/616').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('SelKeFuLink', res.data.value)
					}
				}
			});
			//关键词过滤  602
			that.$Request.getT('/app/common/type/602').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('chatSearchKeys', res.data.value)
					}
				}
			});
			//快速派单首页图片 
			that.$Request.getT('/app/common/type/308').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('yuewanImage', res.data.value)
					}
				}
			});
			//快速派单是否付费
			that.$Request.getT('/app/common/type/278').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('yuewanFabu', res.data.value)
					}
				}
			});
			//快速派单单次派单付费金额
			that.$Request.getT('/app/common/type/279').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('fabuPrice', res.data.value)
					}
				}
			});
			//快速派单每日免费次数
			that.$Request.getT('/app/common/type/280').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('fabuNumber', res.data.value)
					}
				}
			});
			// 会员开关
			that.$Request.getT('/app/common/type/300').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						// #ifdef MP-WEIXIN
						that.$queue.setData('huiyuanSelect', res.data.value)
						// #endif
						// #ifndef MP-WEIXIN
						that.$queue.setData('huiyuanSelect', '是')
						// #endif
					}
				}
			});
			// // 小程序是否上线审核
			// that.$Request.getT('/app/common/type/262').then(res => {
			// 	if (res.code == 0) {
			// 		if (res.data && res.data.value) {
			// 			// #ifdef MP-WEIXIN
			// 			that.$queue.setData('shangxianSelect', res.data.value)
			// 			// #endif
			// 			// #ifndef MP-WEIXIN
			// 			that.$queue.setData('shangxianSelect', '是')
			// 			// #endif
			// 		}
			// 	}
			// });
			// // 余额是否上线审核
			// that.$Request.getT('/app/common/type/301').then(res => {
			// 	if (res.code == 0) {
			// 		if (res.data && res.data.value) {
			// 			// #ifdef MP-WEIXIN
			// 			that.$queue.setData('yueSelect', res.data.value)
			// 			// #endif
			// 			// #ifndef MP-WEIXIN
			// 			that.$queue.setData('yueSelect', '是')
			// 			// #endif
			// 		}
			// 	}
			// });


			// 是否开启用户端会员
			that.$Request.getT('/app/common/type/310').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						// #ifdef MP-WEIXIN
						that.$queue.setData('huiyuanSelect', res.data.value)
						// #endif
						// #ifndef MP-WEIXIN
						that.$queue.setData('huiyuanSelect', '是')
						// #endif
					}
				}
			});
			// 余额开关
			that.$Request.getT('/app/common/type/311').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						// #ifdef MP-WEIXIN
						that.$queue.setData('yueSelect', res.data.value)
						// #endif
						// #ifndef MP-WEIXIN
						that.$queue.setData('yueSelect', '是')
						// #endif
					}
				}
			});
			//是否上线开关
			that.$Request.getT('/app/common/type/312').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						// #ifdef MP-WEIXIN
						that.$queue.setData('shangxianSelect', res.data.value)
						// #endif
						// #ifndef MP-WEIXIN
						that.$queue.setData('shangxianSelect', '是')
						// #endif
					}
				}
			});
			//打赏费率 202
			that.$Request.getT('/app/common/type/202').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('dashangFeilv', res.data.value)
					}
				}
			});
			//打赏最低额度 203
			that.$Request.getT('/app/common/type/203').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('dashangZuidi', res.data.value)
					}
				}
			});
			//打赏最高额度 204
			that.$Request.getT('/app/common/type/204').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('dashangZuiGao', res.data.value)
					}
				}
			});
			//是否显示距离
			that.$Request.getT('/app/common/type/205').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('juliSelect', res.data.value)
					}
				}
			});
			//是否显示优惠券
			that.$Request.getT('/app/common/type/210').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.$queue.setData('yhqSelect', res.data.value)
					}
				}
			});
			//陪玩认证是否需要实名信息	729
			that.$Request.getT('/app/common/type/729').then(res => {
				if (res.code == 0) {
					if (res.data) {
						that.$queue.setData('shimingSelect', res.data.value)
					}
				}
			});
			//是否开启客服充值 827
			that.$Request.getT('/app/common/type/827').then(res => {
				if (res.code == 0) {
					if (res.data) {
						uni.setStorageSync('isOpenKfPay', res.data.value)
					}
				}
			});
			//表情
			that.$Request.getT('/app/common/type/702').then(res => {
				if (res.code == 0) {
					if (res.data) {
						that.$queue.setData('emojiNumber', res.data.value)
					}
				}
			});

			// #ifdef APP-PLUS
			let sysPhone = 1;
			if (plus.os.name.toLowerCase() === 'android') {
				sysPhone = 1;
			} else {
				sysPhone = 2;
			}
			let clientid = plus.push.getClientInfo().clientid;

			let userId = this.$queue.getData('userId');
			if (userId) {
				this.$Request.postT('/app/user/updateClientId?clientId=' + clientid + '&userId=' + userId +
					'&sysPhone=' + sysPhone).then(
					res => {});
			}

			//#endif
			//#ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				let openid = uni.getStorageSync('openid');
				let userId = uni.getStorageSync('userId');
				let that = this;
				if (!openid) {
					if (window.location.href.indexOf('?code=') !== -1 || window.location.href.indexOf('&code=') !==
						-1) {
						let code;
						if (window.location.href.indexOf('?code=') !== -1) {
							code = window.location.href.split('?code=')[1].split('&')[0];
						} else {
							code = window.location.href.split('&code=')[1].split('&')[0];
						}
						this.$Request.get('/app/Login/getOpenId?code=' + code).then(ret => {
							uni.setStorageSync('openid', ret.data)

							this.$Request.get('/app/Login/openid/login?openId=' + ret.data).then(res => {

								this.$queue.setData("userId", res.user.userId);
								this.$queue.setData("token", res.token);
								this.$queue.setData("phone", res.user.phone);
								this.$queue.setData("userName", res.user.userName);
								this.$queue.setData("avatar", res.user.avatar);
								this.$queue.setData("invitationCode", res.user.invitationCode);
							});
						});
					} else {
						this.$Request.get('/app/common/type/108').then(res => {
							if (res.data.value == '是') {
								window.location.href =
									'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' +
									that.$queue.getWxAppid() +
									'&redirect_uri=' +
									window.location.href.split('#')[0] +
									'&response_type=code&scope=snsapi_userinfo#wechat_redirect';
							}
						});
					}
				}
				if (userId && openid) {
					this.$Request.get('/app/Login/bindOpenId?userId=' + userId + '&openId=' + openid).then(res => {});
				}
			}


			//#endif
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	@import "uview-ui/index.scss";
	@import 'components/colorui/main.css';
	@import 'components/colorui/icon.css';

	page {
		background-color: #1A1728;
		color: #FFFFFF;
	}

	.bg {
		background-color: #291B3D;
	}
</style>
