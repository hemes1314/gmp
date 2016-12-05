function findProjectBuildCrumb(dom){
	parent.pushNavStack($(dom).text());
}

// 显示甘特图
function showGantt(proId,proName,proType) {
	var url = contextPath + '/project/gantt/' + proType + '/' + proId;
	var proTypeStr = proType!=""&&proType=="1"?"项目":"敏捷需求";
	parent.dialog({
        url: url,
        title: "【"+proName+"】"+proTypeStr+'排期甘特图',
        width: 1200,
        height: 380
    }).showModal();
}

function showWorkHour(proId, proType, finishHour) {
	location.href = contextPath + '/tj/workHour/' + proType + '/' + proId;
}

$(".delete_project").click(function(){
	var thisObj = $(this);
	var proId = thisObj.parent("td").siblings(".pro_id").text();
	smoke.confirm("确定要删除该项目？", function (e) {
        if (e) {
	    	$.post(
				contextPath + '/project/deletePro',
	    		{proId:proId},
	    		function (data){
	    			if(data!=null && data.code=='success'){
	    				gotoPage();
	    			}
	    		}
		    );
		}
	}, {ok:"确定", cancel:"取消", reverseButtons: true});
});

$(".close_project").click(function(){
	var thisObj = $(this);
	var proId = thisObj.parent("td").siblings(".pro_id").text();
	smoke.confirm("确定要关闭该项目？", function (e) {
		if (e) {
			$.post(
				contextPath + '/project/closePro',
				{proId:proId},
				function (data){
					if(data!=null && data.code=='success'){
						thisObj.hide().closest('tr').find('td').eq(2).text('关闭的');
					}
				}
			);
		}
	}, {ok:"确定", cancel:"取消", reverseButtons: true});
});

['taskStatus', 'priorityIds', 'scheduleIds', 'actualizes','bpIds'].forEach(function(item) {
	 var thisObj = "#"+item;
	 var val_arr = [];
   $(thisObj).find('option').each(function() {
  	 val_arr.push($(this).val());
   });
    multi_option = {
		 zIndex: 10,
       width: 100,
       height: 28,
       defalutName: $(thisObj).attr('details'),
       defaultValues: val_arr.join(',')
    };
	  if(item == "actualizes"){
		   multi_option.width = 160;
	  }
    var _this = this;
    multi_option.onChange = function() {
       var param = _this.search_form();
       $('#search_form').submit();
    }
    $(thisObj).selectlist(multi_option);
    $(thisObj + ' input[type="button"]').eq(0).val("全部");
});

var orgOpArys = ['orgIds', 'childOrgIds', 'groupIds'];
var orgObjs = {};
orgOpArys.forEach(function(item,i) {
	var thisObj = "#"+item;
	var val_arr = [];
    $(thisObj).find('option').each(function() {
        val_arr.push($(this).val());
    });
	var multi_option = {
		zIndex: 100,
		width: 150,
		height: 28,
		defaultValues:val_arr.join(',')
	};
	orgObjs[i] = JSON.parse(JSON.stringify(multi_option));
	if(i < 2){
		var thisOrgQueryVal = $("#qPagerForm").find("input[name='"+item+"']").val();
		if(typeof thisOrgQueryVal !="undefined" ){
			getOrgFramework(item,'#' + orgOpArys[i + 1],orgObjs[i + 1],"query");
		}
		orgObjs[i].onChange = function() {
			getOrgFramework(item,'#' + orgOpArys[i + 1],orgObjs[i + 1],"change");
		}
	}
	var initShow = $(thisObj).attr('initShow');
	if(initShow!=null && initShow=="1"){
		$(thisObj).selectlist(orgObjs[i]);
		var attrName = orgOpArys[i];
		$("#"+attrName+" input[type='button']").eq(0).val("全部");
	}
});

function getOrgFramework(thisOrg,nextOrg,nextOrgMulti,dataType){
	var orgIds = $('input[name="' + thisOrg + '"]').val().split(',');
	if(orgIds!=""){
		$.post(
			contextPath + '/orgManage/getOrgFramework',
			{orgIds:orgIds},
			function (data){
				render_select(data, nextOrg, nextOrgMulti);
				if(dataType=="query"){
					showQueryCondition(nextOrg.substring(nextOrg.indexOf("#")+1,nextOrg.length));
				}
			}
		);
	}
}

function render_select(datas,orgEle,multiOption){
    var str = '';
    var val_arr = [];
    datas.forEach(function(item) {
        val_arr.push(item.orgId);
        str += '<option value="' + item.orgId + '">' + item.orgName + '</option>';
    });
    if(typeof multiOption == "undefined"){
    	var multiOption = {
			zIndex: 100,
			width: 150,
			height: 28
    	};
    }
    multiOption.defaultValues = val_arr.join(',');
    $(orgEle).html(str).selectlist(multiOption);
    $(orgEle + ' input[type="button"]').eq(0).val("全部");
}

function search_form(){
    var param = {};
    $('form').eq(0).find('input[type="text"],input[type="hidden"]').each(function() {
    	if($(this)[0].type == 'hidden'){
    		if($(this).attr('name') == 'proType'){
    			param[$(this).attr('name')] = $(this).val();
    		} else {
    			var arr = [];
    			if($(this).val() != null && $(this).val() != "") {
    				arr = $(this).val().split(",");
    			}
    			if($(this).attr('name') == 'taskStatus' || $(this).attr('name') == 'priorityIds' || $(this).attr('name') == 'scheduleIds' || $(this).attr('name') =='actualizes' || $(this).attr('name') =='bpIds'){
    				if($("#"+$(this).attr('name')+"all_select").attr("op") != "no") {
    					param[$(this).attr('name')] = $('table.detail thead input[name="'+$(this).attr('name')+'"]').val().split(',');
    					$("#queryHideBox").find("input[name='"+$(this).attr('name')+"']").val(param[$(this).attr('name')]);
    				}
    			}else{
    				param[$(this).attr('name')] = arr;
    			}
    		}
    	} else {
    		if($(this).attr('name') == 'startTime' || $(this).attr('name') == 'endTime'){
    			param[$(this).attr('name')] = getTimestamp($(this).val());
    		} else {
    			param[$(this).attr('name')] = $(this).val();
    		}
    	}
    });
    var unitBsVal = $("input[name=unitBsId] option:selected").val();
    if(unitBsVal!=null && unitBsVal!="-1"){
    	param.unitBsId = unitBsVal;
    }
    return param;
}

function getTimestamp(str) {
    str = str.replace(/-/g, '/');
    var date = new Date(str);
    var sTime = date.getTime();
    return sTime
}

$("#searchBtn").click(function(){
	 var params = search_form();
	 $('#search_form').submit();
});

['orgIds', 'childOrgIds', 'groupIds','taskStatus','priorityIds','scheduleIds','actualizes','bpIds'].forEach(function(item) {
	showQueryCondition(item);
});

function showQueryCondition(item){
	var thisObj = $("#"+item);
	var thisObjVal = $("#qPagerForm").find("input[name='"+item+"']").val();
	if(typeof thisObjVal !="undefined" ){
		var thisObjSelectedAyrs = $("#qPagerForm").find("input[name='"+item+"']").val().split(",");
		var thisObjChoiceLen = thisObj.find("ul li").length;
		var tempShowName = "";
		var tempObjVal = "";
		for (var i = 0; i < thisObjSelectedAyrs.length; i++) {
			var thisSelectedName =  thisObj.find("ul li[data-value='"+thisObjSelectedAyrs[i]+"']").find("span.data_text").html();
			if(typeof thisSelectedName !="undefined" && thisSelectedName!=""){
				tempShowName += thisSelectedName + ",";
				tempObjVal += thisObjSelectedAyrs[i] + ",";
				thisObj.find("ul li[data-value='"+thisObjSelectedAyrs[i]+"']").attr("class","selected");
			}
		}
		if(tempShowName!=""){
			var showName = tempShowName.substring(0,tempShowName.lastIndexOf(","));
			var selectOrgVal = tempObjVal.substring(0,tempObjVal.lastIndexOf(","));
			if(item=="orgIds" || item=="childOrgIds" || item=="groupIds" ){
				if(thisObjSelectedAyrs.length==thisObjChoiceLen){
					thisObj.find(".select-button").val("全部");
				}else{
					thisObj.find(".select-button").val(showName);
					thisObj.find(".select-button").attr("title",showName);
				}
			}
			thisObj.find("input[name='"+item+"']").val(selectOrgVal);
			$("#qPagerForm").find("input[name='"+item+"']").val(selectOrgVal);
			
			if(thisObjSelectedAyrs.length==thisObjChoiceLen){
				thisObj.find("div.all_select").attr("op","no");
			}else{
				thisObj.find("div.all_select").attr("op","yes");
				thisObj.find("div.all_select").find("span").html("");
			}
		}
	}
}