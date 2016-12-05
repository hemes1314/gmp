$(document).ready(function() {
	initCrumb($('#updatedMonitorCrumbUl'));
    function monitor_update(config) {
        if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
        this.tableName = config.name || '';
        this.render_table_name = config.render_table;
    }
    
    function formatDate(number) {
    	if (number >= 1 && number <= 9) {
    		number = "0" + number;
    	}
    	return number;
    }

monitor_update.prototype = {
        init: function() {
            this.createBubble();
            this.createSelect();
            this.bindDom();
            this.bindDatePriker();
            
        },
        createBubble: function() {
            var _this = this;
            $(_this.tableName).on('click', 'td', function() {
            })
        },
        createSelect: function() {
            var _this = this;
            var ajax_option = {
                type: "POST",
                url: contextPath + '/orgManage/getQueryCondition',
                data: JSON.stringify({}),
                dataType: "json",
                contentType: "application/json"
            };
            var op_arr = ['orgIds', 'childOrgIds', 'groupIds'];
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
                    	if(i == 0) {
                        	$('#groupIds').html("").selectlist(obj[i]);
                            $('#groupIds input[type="button"]').eq(0).val("全部");
                    	} 
                        var val = $('input[name="' + item + '"]').val().split(',');
                        var param = {};
                        param.orgIdList = val;
                        ajax_option.data = JSON.stringify(param);
                        $.ajax(ajax_option).done(function(data) {
                            _this.render_select(data, '#' + op_arr[i + 1], obj[i + 1])
                        })
                    };
                }
            });
            $.ajax(ajax_option).done(function(data) {
                _this.render_select(data, '#orgIds', obj[0])
            })
        },
        all_select: function(name) {
            var val_arr = [];
            $(name).find('option').each(function() {
                val_arr.push($(this).val())
            });
            var multi_option = {
                zIndex: 1000,
                width: 100,
                height: 30,
                defalutName: $(name).attr('details')
            };
            multi_option.defaultValues = val_arr.join(',');
            var _this = this;
            multi_option.onChange = function() {
                var param = _this.search_form();
                param.priorityIds = $('input[name="system"]').val().split(',');
                param.scheduleIds = $('input[name="system1"]').val().split(',')
                param.actualizes = $('input[name="system2"]').val().split(',')
                _this.render_table(param);
            }
            $(name).selectlist(multi_option);
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
            doSearch();
        },
	    bindDom: function() {
	        var _this = this;
	        $('.search_data').off('click').on('click', function() {
	        	$('.search_data').removeAttr("detailData");
	        	doSearch();
	        })
	        $('.export_data').off('click').on('click', function() {
	        	exportDatas();
	        })
	    },
	   
	    bindDatePriker: function () {
	    	var _this = this;
	    }
    }
    new monitor_update({ 'name': '.monitor_content', 'render_table': '.monitor_detail' }).init()
});
function monitor_updateBuildCrumb(dom){
	parent.pushNavStack($(dom).text());
}
function doSearch() {
	var param = {};
    $('form').find('input[type="hidden"]').each(function() {
    	param[$(this).attr('name')] = $(this).val().split(',');
    });
    param.startDate = $("#start").val();
    param.endDate = $("#end").val();
    if($("#orgIdsall_select").attr("op") == "no") {
    	param.orgIds.push("-1");
    }
    if($("#childsOrgIdall_select").attr("op") == "no") {
    	param.childOrgIds.push("-1");
    }
    if($("#groupIdsall_select").attr("op") == "no") {
    	param.groupIds.push("-1");
    }
	var ajax_option = {
            type: "POST",
            url:contextPath + '/monitor/getUpdateCountList',
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json"
        };
	ajax_option.type = 'POST';
    $.ajax(ajax_option).done(function(data) {
        doTableHtml(data);
    })
}
function exportDatas() {
	var param = {};
    $('form').find('input[type="hidden"]').each(function() {
    	param[$(this).attr('name')] = $(this).val().split(',');
    });
    param.startDate = $("#start").val();
    param.endDate = $("#end").val();
    
    if($("#orgIdall_select").attr("op") == "no") {
    	param.orgIds.push("-1");
    }
    if($("#childOrgIdall_select").attr("op") == "no") {
    	param.childOrgIds.push("-1");
    }
    if($("#groupIdall_select").attr("op") == "no") {
    	param.groupIds.push("-1");
    }
    var url= contextPath + '/monitor/exportData?startDate=' + $("#start").val() + '&endDate=' + $("#end").val();
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
}

function doTableHtml(data) {
	var detail_str = '';
	if(data.length > 0){
		detail_str += '<thead><tr><td>部门</td><td>未更新</td><td>已更新</td><td>更新占比</td><td>暂停项目</td><td>关闭项目</td><td>更新排名</td></tr></thead>';
		detail_str += '<tbody>';
	}else{
		$('.monitor_detail').hide();
	}
	for (var i = 0; i < data.length; i++) {
		item = data[i];
			detail_str += '<tr>';
			detail_str += '<td>' + item.org_name +'</td>';
			detail_str += '<td data='+  item.org_id  +'#'+'1'   +'><a href="javascript:void(0)">' + item.no_updated_count +'</a></td>';
			detail_str += '<td data='+  item.org_id  +'#'+'2'   +'><a href="javascript:void(0)">' + item.updated_count +'</a></td>';
			detail_str += '<td>' + item.update_pct + '%' +'</td>';
			detail_str += '<td data='+  item.org_id  +'#'+'3'   +'><a href="javascript:void(0)">' + item.updated_pause_count +'</a></td>';
			detail_str += '<td data='+  item.org_id  +'#'+'4'   +'><a href="javascript:void(0)">' + item.updated_close_count +'</a></td>';
			if(i < data.length - 1){
				detail_str += '<td>' + (i + 1) +'</td>';
			} else {
				detail_str += '<td></td>';
			}
			detail_str += '</tr>';
	}
	detail_str += '</tbody>';
   	document.getElementById("monitorTable").innerHTML = detail_str;
}

$('body').on('click','td',function(){
	var data = $(this).attr('data');
	if(data){
		$('#monitorTable tbody td').each(function(){
	    	$(this).css('background-color','');
	    });
		$(this).css('background-color','#FEEBF5');
		$('.search_data').attr('detailData',data);
		data = data.split('#');
		console.log(data);
		if(data[0] != ""){
			doSearchAjax(true,data[0],data[1],'','','','');
		}
	}
})

function doSearchAjax(first,orgIds,countType,task,importance,rate,stage) {
	$('#monitor_detail').attr('orgids',orgIds).attr('countType',countType);
	var url = '';
	switch (countType) {
    case '1' :
    	url = contextPath + '/monitor/getNoUpdatedList/';
    	break;
    case '2' :
    	url = contextPath + '/monitor/getUpdatedList/';
        break;
    case '3' :
    	url = contextPath + '/monitor/getPauseList/';
        break;
    case '4' :
    	url = contextPath + '/monitor/getCloseList/';
        break;
	}
	var param = {};
	var orgIdList = [orgIds];
    param.startDate = $("#start").val();
    param.endDate = $("#end").val();
    param.orgIds = orgIdList;
    //任务状态
    if(task && task.length > 0){
    	param.statusId = task;
    };
    //优先级
    if(importance && importance.length > 0){
    	param.priorityId = importance;
    };
	//进度
    if(rate && rate.length > 0){
    	param.scheduleId = rate;
    };
	//实施阶段
    if(stage && stage.length > 0){
    	param.actualize = stage;
    };
	var ajax_option = {
            type: "POST",
            url:url + "1",
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json"
        };
	ajax_option.type = 'POST';
    $.ajax(ajax_option).done(function(data) {
        doProjectHtml(first,data.resultData);
        doSelect();
        this.dataPages = data.pages;
        laypage({
            cont: 'page',
            pages: data.pages,
            curr: 1,
            jump: function(obj, first) {
                if (!first) {
                    $.ajax({
                        type: "POST",
                        url:url+obj.curr,
                        data: JSON.stringify(param),
                        dataType: "json",
                        contentType: "application/json"
                    }).done(function(data) {
                        this.dataPages = data.pages;
                        doProjectHtml(false,data.resultData);
                        doSelect();
                    })
                }
            }
        });  
    })
}

function doProjectHtml(first,data) {
	var tableHeader = '';
	if(first){
		tableHeader= $("#tableHead_tpl").html();
	}
	$('.monitor_detail').show();
	var html_str = tableHeader + '<tbody id="detailTbody">';
	for (var i = 0; i < data.length; i++) {
		item = data[i];
		html_str += '<tr>';
		html_str += '<td>' + item.pro_id +'</td>';
		html_str += '<td>';
		html_str += '<a href="javascript:void(0)" onclick="monitor_updateBuildCrumb(this);window.location.href=\'';
		html_str += contextPath + '/project/detail/'+ item.pro_id + ';return false\'">'+item.title+'</a>';
		html_str +='</td>';
		html_str += '<td>' + item.statusName +'</td>';
		html_str += '<td>' + item.priorityName +'</td>';
		html_str += '<td>' + item.scheduleName +'</td>';
		html_str += '<td>' + item.actualizeName +'</td>';
		if(!item.task_completion){
			html_str += '<td>' + '0%' +'</td>';
		} else {
			html_str += '<td>' + item.task_completion +'%' +'</td>';
		}
		var planTime = typeof(item.plan_time) == 'undefined'?'--':item.plan_time +'</td>';
		html_str += '<td>' + planTime;
		html_str += '<td>' + item.user_name +'</td>';
		html_str += '</tr>';
	}
	html_str += '</tbody>';
	if(first){
		$('#monitor_detail').empty();
	} else {
		$("#detailTbody").remove();
	}
	$("#monitor_detail").append(html_str);
}

function doSelect(){
    $('#monitor_detail').off('change','select').on('change','select',function(){
        var monitor_detail=$('#monitor_detail');
        var importanceSelect=monitor_detail.find('select[name="importance"] option:selected').attr('project_type'),
            rateSelect=monitor_detail.find('select[name="rate"] option:selected').attr('project_type'),
            stageSelect=monitor_detail.find('select[name="stage"] option:selected').attr('project_type'),
            taskSelect=monitor_detail.find('select[name="task"] option:selected').attr('project_type');
        console.log(importanceSelect+'----'+rateSelect+'----'+stageSelect);
        var orgIds = $('#monitor_detail').attr('orgids');
        var countType = $('#monitor_detail').attr('countType');
        doSearchAjax(false,orgIds,countType,taskSelect,importanceSelect,rateSelect,stageSelect);
    });
}