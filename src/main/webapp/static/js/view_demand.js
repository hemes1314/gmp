function viewDemandBuildCrumb(dom){
	var orgNm = $(dom).closest('tr').children().eq(0).text();
	parent.pushNavStack(orgNm);
	console.log(parent.NavStack);
}
$(document)
		.ready(
				function() {
if($('#pageId').val() == 'commitPeople'){
						initCrumb($('#commitPeopleCrumbUl'));
					}else if($('#pageId').val() == 'commitNeed'){
						initCrumb($('#commitNeedCrumbUl'));
					}
					function GomeProject_CommonBar(config) {
						if (Object.prototype.toString.call(config).match(
								/\[object (\w+)\]/)[1].toLowerCase() !== 'object')
							return;
						this.page = config.laypage || "";
						this.searchButton = config.searchButton;
					}

					GomeProject_CommonBar.prototype = {
						constructor : GomeProject_CommonBar,
						init : function() {
							this.bindDom();
							this.createSelect();
							this.getParam();
							$(".breadCrumb").jBreadCrumb({'liCallback':function(){
					        	parent.popNavStack();
					        }});
						},
						getParam : function() {
							var _this = this;
							var param;
							var url_param;

							function getTimestamp(str) {
								str = str.replace(/-/g, '/');
								var date = new Date(str);
								var sTime = date.getTime();
								return sTime
							}

							function getUrlParam(name) {
								var reg = new RegExp("(^|&)" + name
										+ "=([^&]*)(&|$)");
								var r = window.location.search.substr(1).match(
										reg);
								if (r != null)
									return unescape(r[2]);
								return null;
							}
							;
							var judge_url = window.location.href;
							if (judge_url.indexOf('starttime') > 0) {
								param = {};
								url_param = {};
								param.startTime = getTimestamp(getUrlParam('starttime'));
								param.endTime = getTimestamp(getUrlParam('endtime'));
								/*
								 * if(getUrlParam('orgId')!=null){ param.orgId =
								 * getUrlParam('orgId').split(','); }
								 */

								if (getUrlParam('orgIds') != null
										&& getUrlParam('orgIds') != undefined
										&& getUrlParam('orgIds').length > 0
										) {
									param.orgIds = getUrlParam('orgIds').split(
											',');
								}

								url_param.startTime = getUrlParam('starttime');
								url_param.endTime = getUrlParam('endtime');

								/*
								 * if(getUrlParam('orgId')!=null){
								 * url_param.orgId =
								 * getUrlParam('orgId').split(','); }
								 */
								if (getUrlParam('orgIds') != null
										&& getUrlParam('orgIds') != undefined
										&& getUrlParam('orgIds').length > 0
										) {
									url_param.orgIds = getUrlParam('orgIds')
											.split(',');
								}

								var url = contextPath + '/datas/tjPeopleInfo';

								_this.getData(param, url_param, url);
								
							} else {
								_this.chart_ajax();
							}
						},
						render_data : function(json, url_param) {
							
							String.prototype.temp = function(obj) {
								return this
										.replace(
												/\$\w+\$/gi,
												function(matchs) {
													// var returns =
													// obj[matchs.replace(/\$/g,
													// "")];
													var propertyName = matchs
															.replace(/\$/g, "");
													var returns = obj[propertyName];
													// 如果是时间则取日期部分
													if (propertyName == "planTime"
															|| propertyName == "createTime") {
														if (returns != null) {
															returns = returns
																	.split(" ")[0]
														}
													}
													return returns == null
															|| (returns + "") == "undefined" ? ""
															: returns;

												});
							};
							
							var tpl = $('#record_temp').html();
							var html = '';
							var judge_url = window.location.href;
							var url;

							
							if (judge_url.indexOf('starttime') > 0) {
								// url = contextPath +
								// '/datas/toNeedList/?starttime=' +
								// url_param.startTime + '&endtime=' +
								// url_param.endTime + '&orgId=' +
								// url_param.orgId;
								url = contextPath
										+ '/datas/toNeedList/?starttime='
										+ url_param.startTime + '&endtime='
										+ url_param.endTime;
							} else {
								// url = contextPath +
								// '/datas/toPeopleList/?starttime=' +
								// url_param.startTime + '&endtime=' +
								// url_param.endTime + '&orgId=' +
								// url_param.orgId;
								url = contextPath
										+ '/datas/toPeopleList/?starttime='
										+ url_param.startTime + '&endtime='
										+ url_param.endTime;
							}
							var temUrl = url;
							var jsonCount = json.length;
							var index = 0;
							var allUrl = url;
							json.forEach(function(obj) {
								url = temUrl;
								index = index + 1;
							
								html += tpl.temp(obj);
								if (judge_url.indexOf('starttime') > 0) {
									url += '&createUser='
											+ obj.create_user;
								}
                                
								if (obj.orgId == null
										|| obj.orgId == undefined) {
									url = url
											+ '&orgIds='
											+ (url_param.orgIds ? url_param.orgIds
													: "");
								} else {
									url = url
											+ '&orgIds='
											+ (obj.orgId ? obj.orgId
													: "");
								}
								html += '<td><a onclick="viewDemandBuildCrumb(this);" href=' + url+ '>查看</a></td>';
								html += '</tr>';
							});
							return html;
						},
						createSelect : function() {
							var _this = this;
							var ajax_option = {
								type : "POST",
								url : contextPath + '/orgManage/getQueryCondition',
								data : JSON.stringify({}),
								dataType : "json",
								contentType : "application/json"
							};
							var multi_option = {
								zIndex : 1000,
								width : 150,
								height : 30,
							};
							$.ajax(ajax_option).done(
									function(data) {
										_this.render_select(data, '#orgIds',
												multi_option)
									})
						},
						render_select : function(json, name, option) {
							var data = json;
							var str = '';
							var val_arr = [];
							data.forEach(function(item) {
								val_arr.push(item.orgId);
								str += '<option value="' + item.orgId + '">'
										+ item.orgName + '</option>';
							});
							option.defaultValues = val_arr.join(',');
							
							// 重要，勿去！
							if($(name).length<=0) return;
							
							$(name).html(str).selectlist(option);
							$(name + ' input[type="button"]').eq(0).val("全部");
							var _this = this;
							var param = {};
							var url_param = {};

							function getTimestamp(str) {
								str = str.replace(/-/g, '/');
								var date = new Date(str);
								var sTime = date.getTime();
								return sTime
							}
							param.startTimes = $('input[name="startTime"]')
									.val();
							param.endTimes = $('input[name="endTime"]').val();

							if($('input[name="orgIds"]').val() != null &&
							  $('input[name="orgIds"]').val() != undefined &&
							  $('input[name="orgIds"]').val().length > 0 &&
							  $('input[name="orgIds"]').val().split(',') != null &&
							 $('input[name="orgIds"]').val().split(',') !=
							  undefined &&
							  $('input[name="orgIds"]').val().split(',').length >
							  0){ param.orgIds =
							  $('input[name="orgIds"]').val().split(','); }
							  
							  else { param.orgIds ={};
							   }
							
							url_param.startTime = $('input[name="startTime"]')
									.val();
							url_param.endTime = $('input[name="endTime"]')
									.val();
							
							if ($('input[name="orgIds"]').val() != null
									&& $('input[name="orgIds"]').val() != undefined
									&& $('input[name="orgIds"]').val().length > 0
									&& $('input[name="orgIds"]').val()
											.split(',') != null
									&& $('input[name="orgIds"]').val()
											.split(',') != undefined
									&& $('input[name="orgIds"]').val()
											.split(',').length > 0) {
								url_param.orgIds = $('input[name="orgIds"]')
										.val().split(',');
							} else {
								url_param.orgIds = {};

							}
							var url = contextPath + '/datas/tjDemand';
							_this.getData(param, url_param, url)
						},
						chart_ajax : function() {
							var _this = this;
							$(_this.searchButton)
									.on(
											'click',
											function() {
												var param = {};
												var url_param = {};

												function getTimestamp(str) {
													str = str
															.replace(/-/g, '/');
													var date = new Date(str);
													var sTime = date.getTime();
													return sTime
												}
												param.startTimes = $(
														'input[name="startTime"]')
														.val();
												param.endTimes = $(
														'input[name="endTime"]')
														.val();
											

												if ($('input[name="orgIds"]')
														.val() != null
														&& $(
																'input[name="orgIds"]')
																.val() != undefined
														&& $(
																'input[name="orgIds"]')
																.val().length > 0
														) {
													
													param.orgIds = $(
															'input[name="orgIds"]')
															.val().split(',');
												} else {
													param.orgIds = {};

												}
												url_param.startTime = $(
														'input[name="startTime"]')
														.val();
												url_param.endTime = $(
														'input[name="endTime"]')
														.val();

												if ($('input[name="orgIds"]')
														.val() != null
														&& $(
																'input[name="orgIds"]')
																.val() != undefined
														&& $(
																'input[name="orgIds"]')
																.val().length > 0
														) {
													url_param.orgIds = $(
															'input[name="orgIds"]')
															.val().split(',');
												} else {
													url_param.orgIds = {};

												}

												var url = contextPath
														+ '/datas/tjDemand';
												_this.getData(param, url_param,
														url)
											})
						},
						getData : function(param, url_param, url) {
							var _this = this;
							$.ajax({
								type : "POST",
								url : url,
								data : JSON.stringify(param),
								dataType : "json",
								contentType : "application/json"
							}).done(
									
									function(json) {
										$('.demand_statistics_table').find(
												'tbody').html(
												_this.render_data(json,
														url_param))
									}).then(function() {
								_this.export(param);
							})
						},
						export : function(param) {
							$('.export_data').attr(
									'href',
									contextPath + '/datas/exportD?startTimes='
											+ param.startTimes + '&endTimes='
											+ param.endTimes + '&orgIds='
											+ param.orgIds)
						},
						bindDom : function() {
							$('.demand_statistics_table').on('click', 'td',
									function() {
										var url = $(this).attr('url');
										if (url == null || url == undefined) {
											return;
										}
										window.location.href = url;
									})
						}
					};
					new GomeProject_CommonBar({
						'laypage' : 'GomeProjectPage',
						'searchButton' : '.search_data'
					}).init()
				})