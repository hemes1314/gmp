$(document).ready(function() {
	// add for crumb start
	initCrumb($('#proMonitorCrumbUl'));
	// add for crumb end
});
function proMonitorBuildCrumb(dom){
	parent.pushNavStack($(dom).text());
}
function createPage(config) {
	if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1]
			.toLowerCase() !== 'object')
		return;
	this.pageName = config.name;
}

function structureParms(thisObj,queryType){
	var param = {};
	if(queryType!="" && queryType=="detail"){
		var thisId = thisObj.attr("id");
		param.monitorType = thisId.substring(0,thisId.indexOf("_"));
		param.proType = thisId.substring(thisId.indexOf("_")+1,thisId.length);
	}else if(queryType!="" && queryType=="changes"){
		var thisId = thisObj.attr("id");
		param.actualize = $('#actualize').val();
		param.priorityId = $('#priorityId').val();
		param.statusId = $('#statusId').val();
		param.scheduleId = $('#scheduleId').val();
		param.proType = $('#proType').val();
		$('#'+thisId+'Select').val(thisObj.val());
	}else if(queryType!="" && queryType=="export"){
		param.actualize = $('#actualizeSelect').val();
		param.priorityId = $('#priorityIdSelect').val();
		param.statusId = $('#statusIdSelect').val();
		param.scheduleId = $('#scheduleIdSelect').val();
		param.proType = $('#proType').val();
	}
	param.year = $("select[name='year']").find("option:selected").val();
	$('form').find('input[type="hidden"]').each(function() {
		param[$(this).attr('name')] = $(this).val().split(',');
	});
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
}

createPage.prototype = {
	init : function() {
		this.bindEvent();
	},
	render_data : function(json) {
		var proItem = json[0];
		var mjItem = json[1];
		if (proItem == null && mjItem == null) {
			$('.monitor_content').empty();
		}
		if (proItem != null) {
			var allPro = proItem.allPro == null ? "" : proItem.allPro;
			$('#allPro_1').html('<a href="javascript:void(0)">'+allPro+'</a>');
			var nearOnline = proItem.nearOnline == null ? "" : proItem.nearOnline;
			$('#nearOnline_1').html('<a href="javascript:void(0)">'+nearOnline+'</a>');
			var soonOnline = proItem.soonOnline == null ? "" : proItem.soonOnline;
			$('#soonOnline_1').html('<a href="javascript:void(0)">'+soonOnline+'</a>');
			var riskPro = proItem.riskPro == null ? "" : proItem.riskPro;
			$('#riskPro_1').html('<a href="javascript:void(0)">'+riskPro+'</a>');
			var delayPro = proItem.delayPro == null ? "" : proItem.delayPro;
			$('#delayPro_1').html('<a href="javascript:void(0)">'+delayPro+'</a>');
			var newlyPro = proItem.newlyPro == null ? "" : proItem.newlyPro;
			$('#newlyPro_1').html('<a href="javascript:void(0)">'+newlyPro+'</a>');
		} else {
			$('#pro').hide();
		}
		if (mjItem != null) {
			var allPro = mjItem.allPro == null ? "" : mjItem.allPro;
			$('#allPro_2').html('<a href="javascript:void(0)">'+allPro+'</a>');
			var nearOnline = mjItem.nearOnline == null ? "" : mjItem.nearOnline;
			$('#nearOnline_2').html('<a href="javascript:void(0)">'+nearOnline+'</a>');
			var soonOnline = mjItem.soonOnline == null ? "" : mjItem.soonOnline;
			$('#soonOnline_2').html('<a href="javascript:void(0)">'+soonOnline+'</a>');
			var riskPro = mjItem.riskPro == null ? "" : mjItem.riskPro;
			$('#riskPro_2').html('<a href="javascript:void(0)">'+riskPro+'</a>');
			var delayPro = mjItem.delayPro == null ? "" : mjItem.delayPro;
			$('#delayPro_2').html('<a href="javascript:void(0)">'+delayPro+'</a>');
			var newlyPro = mjItem.newlyPro == null ? "" : mjItem.newlyPro;
			$('#newlyPro_2').html('<a href="javascript:void(0)">'+newlyPro+'</a>');
		} else {
			$('#minpro').hide();
		}
	},

	renderItems : function(data) {
		var html = "";
		for (var i = 0; i < data.length; i++) {
			html += "<tr>";
			html += '<td>' + data[i].pro_id + '</td>';
			html += '<td>';
			html += '<a href="javascript:void(0)" onclick="proMonitorBuildCrumb(this);window.location.href=\'';
			html += contextPath + '/project/detail/' + data[i].pro_id + ';return false\'">' + data[i].title + '</a>';
			html += '</td>';
			if (data[i].status_name != null) {
				html += '<td>' + data[i].status_name + '</td>';
			} else {
				html += '<td></td>';
			}
			if (data[i].priority_name != null) {
				html += '<td>' + data[i].priority_name + '</td>';
			} else {
				html += '<td></td>';
			}
			if (data[i].schedule_name != null) {
				html += '<td>' + data[i].schedule_name + '</td>';
			} else {
				html += '<td></td>';
			}
			if (data[i].actualize_name != null) {
				html += '<td>' + data[i].actualize_name + '</td>';
			} else {
				html += '<td></td>';
			}
			if (data[i].percentage != null) {
				html += '<td>' + data[i].percentage + '</td>';
			} else {
				html += '<td></td>';
			}
			if (data[i].plan_time != null) {
				html += '<td>' + data[i].plan_time.split(" ")[0] + '</td>';
			} else {
				html += '<td>--</td>';
			}
			if (data[i].plan_time != null) {
				html += '<td>' + data[i].bp_name + '</td>';
			} else {
				html += '<td>--</td>';
			}
			html += '</tr>';
		}
		$('.monitor_detail').find('tbody').html(html);
	},
	bindEvent : function() {
		var _this = this;
		$('#monitor_detail span.monitor_type_count').on('click', function() {
			var param = structureParms($(this),"detail");
			$('#monitorType').val(param.monitorType);
			$('#proType').val(param.proType);
			$.when($.ajax({
				type : "POST",
				url : contextPath + '/proMonitor/monitor/'+param.monitorType,
				data : JSON.stringify(param),
				dataType : 'json',
				contentType : "application/json"
			})).done(function(data) {
				$('#priorityId')[0].selectedIndex = 0;
				$('#scheduleId')[0].selectedIndex = 0;
				$('#actualize')[0].selectedIndex = 0;
				$('#statusId')[0].selectedIndex = 0;
				_this.renderItems(data);
			})
		});
		
		$('.search').on('click', function() {
			var param = structureParms($(this),"");
			$.when($.ajax({
				type : "POST",
				url : contextPath + '/proMonitor/findPro',
				data : JSON.stringify(param),
				dataType : 'json',
				contentType : "application/json"
			})).done(function(data) {
				$('.monitor_detail').hide();
				_this.render_data(data);
			})
		})

		$('#exportBtn').on('click',function() {
			var param = structureParms($(this),"");
			var url = "orgIds=" + param.orgIds;
			if (param.groupIds != null) {
				url += "&groupIds=" + param.groupIds;
			}
			if (param.childOrgIds != null) {
				url += "&childOrgIds=" + param.childOrgIds;
			}
			if (param.year != null) {
				url += "&year=" + param.year;
			}
			var monitorType = $('#monitorType').val();
			if (monitorType != null && monitorType.length > 0) {
				var param = structureParms($(this),"export");
				url += "&statusId=" + param.statusId;
				url += "&priorityId=" + param.priorityId;
				url += "&scheduleId=" + param.scheduleId;
				url += "&actualize=" + param.actualize;
				url += "&proType=" + param.proType;
				url = contextPath + '/proMonitor/detailExport/'+ monitorType + '?' + url;
			}else{
				url = contextPath + '/proMonitor/export?' + url;
			}
			console.log(url);
			$(this).attr('href', url);
		});

		$('#monitor_detail_id .monitor_search_selects').on('change',function() {
			var param = structureParms($(this),"changes");
			$.when(
					$.ajax({
						type : "POST",
						url : contextPath + '/proMonitor/monitor/'+ $('#monitorType').val(),
						data : JSON.stringify(param),
						dataType : 'json',
						contentType : "application/json"
					})).done(function(data) {
				_this.renderItems(data);
			})
		});
	},
}

new createPage({
	'name' : 'GomeProjectPage'
}).init()
