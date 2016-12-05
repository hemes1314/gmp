$(document).ready(function() {
initCrumb($('#scheduleStatisticsCrumbUl'));
    function ShowWorkHoursContent(config) {
        if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
        this.pageName = config.name;
        this.searchButton = config.searchButton;
        this.pageType = $("#pageType").val();
    }
    
    function formatDate(date) {
    	if (date >= 1 && date <= 9) {
    		date = "0" + date;
    	}
    	return date;
    }
    
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
    
    ShowWorkHoursContent.prototype = {
        constructor: ShowWorkHoursContent,
        init: function() {
            this.bindDatePriker();
            this.createSelect();
            this.bindDom();
            this.table_a_op();
            this.checkParam();
        },
        checkParam: function() {
            var param = {};
            var _this = this;
            var orgId = getUrlParam('orgId');
            if (typeof orgId == "string") {
                param.startDate = getUrlParam('startDate');
                param.endDate = getUrlParam('endDate');
                var arr = [];
                arr.push(orgId)
                param.orgIds = arr;
                _this.getData(param,'/hourCount/subOrgHourCount');
            } else {
                param.startDate = $('#start').val();
                param.endDate = $('#end').val();
                $('form').find('input[type="hidden"]').each(function() {
                    param[$(this).attr('name')] = $(this).val().split(',')
                });
            }
            if($("#orgIdsall_select").attr("op") == "no") {
            	param.orgIds.push("-1");
            }
            if($("#childOrgIdsall_select").attr("op") == "no") {
            	param.childOrgIds.push("-1");
            }
            if($("#groupIdsall_select").attr("op") == "no") {
            	param.groupIds.push("-1");
            }
            return param;
        },
        params: {},
        createSelect: function() {
            var _this = this;
            var ajax_option = {
                type: "POST",
                url: contextPath + '/orgManage/getQueryCondition',
                data:JSON.stringify({queryType:"permission"}),
                dataType: "json",
                contentType: "application/json"
            };
            var userOrgLevel = $("#userOrgLevel").val();
            var firstOrgObj;
            var op_arr = ['orgIds', 'childOrgIds', 'groupIds'];
            firstOrgObj =  "#orgIds";
            if(userOrgLevel!=""){
            	if(userOrgLevel>2){
            		var op_arr = ['childOrgIds', 'groupIds'];
            		firstOrgObj =  "#childOrgIds";
            	}
            }
            var multi_option = {
                zIndex: 1000,
                width: 150,
                height: 30,
            };
            var obj = {};
            op_arr.forEach(function(item, i) {
                obj[i] = JSON.parse(JSON.stringify(multi_option));
                if (i < 2) {
                    obj[i].onChange = function() {
                        var val = $('input[name="' + item + '"]').val().split(',');
                        var param = {};
                        param.orgIdList = val;
                        param.queryType = "permission";
                        ajax_option.data = JSON.stringify(param);
                        $.ajax(ajax_option).done(function(data) {
                            _this.render_select(data, '#' + op_arr[i + 1], obj[i + 1])
                        })
                    };
                }
            });
            if ($(firstOrgObj).length) {
                $.ajax(ajax_option).done(function(data) {
                    _this.render_select(data, firstOrgObj, obj[0]);
                }).done(function(){
                	$(_this.searchButton).click();
                });
            }
        },
        render_select: function(json, name, option) {
            var data = json;
            var str = '';
            var val_arr = [];
            data.forEach(function(item) {
                val_arr.push(item.orgId);
                str += '<option value="' + item.orgId + '">' + item.orgName + '</option>';
            });
            option.defaultValues = val_arr.join(',');
            $(name).html(str).selectlist(option);
            $(name + ' input[type="button"]').eq(0).val("全部");
        },
        bindDom: function() {
            var _this = this;
            if ($('.search_data').length) {
                $('.search_data').off('click').on('click', function() {
                    var param = _this.checkParam();
                    if(param.orgIds=="" && (typeof param.childOrgIds == 'undefined' || param.childOrgIds=="") && (typeof param.groupIds == 'undefined' || param.groupIds=="")){
                    	smoke.alert("请选取组织部门进行工时统计查询", {ok: "确定"});
                    	return;
                    }
                    _this.params.startDate = param.startDate;
                    _this.params.endDate = param.endDate;
                    _this.getData(param,'/hourCount/orgHourCount');
                });
            }
            if ($('.export_data').length) {
                $('.export_data').off('click').on('click', function() {
                    var param = _this.checkParam();
                    _this.params.startDate = param.startDate;
                    _this.params.endDate = param.endDate;
                    _this.exportData(param);
                });
            }
        },
        table_a_op: function() {
            var _this = this;
            var startDate = (_this.params.startDate == 'undefined' || !startDate) ? getUrlParam('startDate') : (_this.params.startDate);
            var endDate = (_this.params.endDate == 'undefined' ||  !endDate) ? getUrlParam('endDate') : (_this.params.startDate);
            $("#hours_count_tab").on('click', 'a', function() {
            	if(startDate == 'undefined' || !startDate){
            		startDate = $(this).attr('startDate');
            	};
            	if(endDate == 'undefined' || !endDate){
            		endDate = $(this).attr('endDate');
            	};
            	var url;
            	var orgId = $(this).attr('data');
            	if(typeof orgId!="undefined"){
            		var orgStrLen = orgId.length;
            		if(orgStrLen == 4 || orgStrLen == 6){
            			url = contextPath + '/hourCount/scheduleStatisticsSub' + '?orgId=' + orgId + '&startDate=' + startDate + '&endDate=' + endDate;
            			if(orgStrLen == 4) {
            				parent.pushNavStack("子部门工时统计");
            			}
            			if(orgStrLen == 6) {
            				parent.pushNavStack("小组工时统计");
            			}
            		} else if (orgStrLen == 8){
            			url = contextPath + '/hourCount/scheduleStatisticsMember' + '?orgId=' + orgId + '&startDate=' + startDate + '&endDate=' + endDate;
            			parent.pushNavStack("个人工时统计");
            		}
            		window.location.href = url;
            	}else{
            		var userTid = $(this).attr('userTid');
            		$("#dailyUserId").val(userTid);
            		$("#userDailyHours_div").show("slow");
            		var currentMonthStr = $(".calendar-title-date").html();
            		var currentMonth = currentMonthStr.substring(0,currentMonthStr.lastIndexOf("月"));
                    var date = new Date($('#selectYear').val(),currentMonth-1,1).Format("yyyy-MM-dd");
            		getDailyHoursSchedule(date);
            	}
            })
        },
        getData: function(param,url) {
        	if(param.orgIds[0]!=null){
    			if(url == '/hourCount/subOrgHourCount' && param.orgIds[0].length == 8){
    				url = '/hourCount/memberHourCount';
    			}
    			var _this = this;
    			$.ajax({
    				type: "POST",
    				url: contextPath + url,
    				data: JSON.stringify(param),
    				dataType: "json",
    				contentType: "application/json"
    			}).done(function(json) {
    				String.prototype.temp = function(obj) {
    					return this.replace(/\$\w+\$/gi, function(matchs) {
    						var returns = obj[matchs.replace(/\$/g, "")];
    						return (returns + "") == "undefined" ? "" : returns;
    					});
    				};
    				var tpl = $('#work_hours_tpl').html();
    				var html = '';
    				json.forEach(function(obj) {
    					html += tpl.temp(obj)
    				});
    				$('.work_hours_table').find('tbody').html(html)
    				$('.showNext').attr('startDate',param.startDate).attr('endDate',param.endDate);
    				if(param.groupId != undefined && param.groupId.length != 0){
    					$('#personCount').text('小组人数');
    				}else if(param.childOrgId != undefined && param.childOrgId.length != 0){
    					$('#personCount').text('子部门人数');
    				}else{
    					$('#personCount').text('部门人数');
    				}
    			})
        	}
        },
        exportData: function(param) {
        	 var url= contextPath + '/hourCount/exportData?startDate=' + $("#start").val() + '&endDate=' + $("#end").val();
        	    if(param.orgIds){
        	    	url += '&orgIds=' + param.orgIds;
        	    }
        	    if(param.childOrgIds){
        	    	url += '&childOrgIds=' + param.childOrgIds;
        	    }
        	    if(param.groupIds){
        	    	url += '&groupIds=' + param.groupIds;
        	    }
        	    
        	    window.location.href=url;
        },
        bindDatePriker: function () {
	    	var _this = this;
	    }
    }
    new ShowWorkHoursContent({ 'name': 'XXXbutton','searchButton': '.search_data' }).init();
});