$(document).ready(function() { 
	
	var userCookie = JSON.parse($.cookie("user"));
	init(userCookie);
	$("[userid$='"+(userCookie.id)+"']").click();
})  

function split( val ) {
   return val.split( /;\s*/ );
}

function extractLast( term ) {
    return split( term ).pop();
}

function uniQueue(array){
	var arr=[];
	var m;
	while(array.length>0){
    	m=array[0];
    	arr.push(m);
    	array=$.grep(array,function(n,i){
    		return n==m;
    	},true);
	}
	return arr;
}

/**
 * 组织人员管理自动完成匹配用户
 */
function orgPersonAutocomplete(){
	 $('.autocomplete_input:visible').bind( "keydown", function( event ) {
         if ( event.keyCode === $.ui.keyCode.TAB &&  $( this ).data( "instance" ).menu.active ) {
               event.preventDefault();
             }
     })
    .autocomplete({
        source: function( request, response ) {
	            $.ajax({
	                type: 'POST',
	                 url:  contextPath + '/orgManagerMatchingUser',
	                 dataType: "json",
	                 contentType: "application/json",
	                 data: JSON.stringify({userName: extractLast(request.term)}),
	                 success: function(data) {
	                 	if(data!=null && data.code=='success'){
	                 		var matchDatas = data["data"];
	                 		response(
	                 				$.map(matchDatas, function(item) {
	                 					var labelStr = item.userId + '(' + item.userName + ')';
	                 					return { user: item.userId,userId: item.id ,userName:item.userName,label:labelStr}
	                 				})
	                 		);
	                 	}
	                 }
	            });
	        }, 
	        search: function() {
	            var term = extractLast(this.value);
	            if ( term.length < 1 ) {
	              return false;
	            }
	         },
	        focus: function() {
	          // 防止在获得焦点时插入值
	          return false;
	        },
	        select: function(event, ui) {
	          var terms = split(this.value);
	          // 移除当前输入
	          terms.pop();
	          // 添加被选项
	          terms.push(ui.item.value);
	          // 添加占位符，在结尾添加逗号+空格
	          terms.push("");
	          this.value = terms.join(";");
	          var attrUser = $(this).attr("user");
	          if(typeof attrUser=="undefined" || (typeof attrUser=="string" && $.trim(attrUser)=="")){
	        	  $(this).attr('user',ui.item.user);
	          }else{
	        	  $(this).attr('user',$(this).attr("user")+";"+ui.item.user);
	          }
	   		  var attrUserId = $(this).attr("userid");
	   		  if(typeof attrUserId=="undefined" || (typeof attrUserId=="string" && $.trim(attrUserId)=="")){
	   			  $(this).attr('userid',ui.item.userId);
	          }else{
	        	  $(this).attr('userid',$(this).attr("userid")+";"+ui.item.userId);
	          }
	   		  var attrUserName = $(this).attr("userName");
	   		  if(typeof attrUserName=="undefined" || (typeof attrUserName=="string" && $.trim(attrUserName)=="")){
	   			  $(this).attr('userName',ui.item.userName);
	          }else{
	        	  $(this).attr('userName',$(this).attr("userName")+";"+ui.item.userName);
	          }
	   		  var hideInfoVal = $("#"+ui.item.user).val();
	   		  if(typeof hideInfoVal == "undefined"){
	   			  $(this).after("<input type='hidden' id='"+ui.item.user+"' value='"+ui.item.userId+"' userName='"+ui.item.userName+"'/>");
	   		  }
		     return false;
	      }
	 });
}

function init(userCookie) {
	var loginOrgId = userCookie.orgId;
	var loginUserId = userCookie.id;
	var authority = userCookie.authority;
	//点击组织
    $('body').find('.org_op_ul').each(function() {
        var li_op = $(this).find('li');
        var orgLevel = $(this).attr('orglevel');
        if(orgLevel != "5"){
        li_op.each(function() {
            if ($(this).attr('class') != 'title') {
            	var orgId = $(this).find('.org_op_p').attr('orgid');
            	var userId = $(this).find('.org_op_p_preson').attr('userid');
            	if(loginOrgId!= null && loginOrgId != undefined){
            		if((orgId!=loginOrgId ||(orgId==loginOrgId && userId.indexOf(loginUserId) < 0)) && loginOrgId.indexOf(orgId) == 0){
                		$(this).addClass('cur');
                	}
            	}
                //修改按钮
                $(this).off('click').on('click',function(e){
                	if(e.target.nodeName =='A'){
                		return;
                	}
                	if($(this).attr('class') != 'cur'){
                		$(this).siblings().removeClass('cur');
                		$(this).addClass('cur');
                		//权限
                		checkAuthority($(this));
                		//删除子节点
                		$(this).parent().nextAll().find('li:not(.title)').remove();
                  		var team = $(this).attr('team');
                		if(orgLevel == "4"){
                			serchMemberAjax($(this),orgId);
                		} else {
                			serchChildAjax($(this),orgId);
                		}
                	}
                })
            }else {
            	if(authority== "1"){
            		if (orgLevel == "1"){
            			$(this).find('a').show();
            		} else {
            			if($(this).siblings().length > 0){
            				$(this).find('a').show();
            			}
            		}
            		
            	}
            }
        });

        }
    });
}
function checkAuthority(object) {
	var leaderId = object.find('.org_op_p_preson').attr('userid');
	var userCookie = JSON.parse($.cookie("user"));
	var loginId = userCookie.id;
	var authority = userCookie.authority;
	if(leaderId.indexOf(loginId)>-1 || authority == "1"){
		object.parent().next().nextAll().find('a').each(function(){
			$(this).hide();
		});
		object.parent().next().find('a').each(function(){
			$(this).show();
			if($(this).text()==='完成'){
				$(this).text('编辑');
			}
		});
		return;
	}
	var hasAuthority = false;
	object.parent().prevAll().each(function() {
		$(this).find('li').each(function(){
			if($(this).attr('class') == 'cur'){
        		var loginId = JSON.parse($.cookie("user")).id;
        		var leaderId = $(this).find('.org_op_p_preson').attr('userid');
        		if(loginId == leaderId){
        			hasAuthority = true;
        		}
			}
		})
	});
	if(hasAuthority == true){
		object.parent().next().nextAll().find('a').each(function(){
			$(this).hide();
		});
		object.parent().next().find('a').each(function(){
			$(this).show();
			if($(this).text()==='完成'){
				$(this).text('编辑');
			}
		});
	} else {
		object.parent().nextAll().find('a').hide();
	}
}

function serchChildAjax(object,orgId) {
	var ajax_option = {
            type: "POST",
            url:contextPath + '/orgManage/getNextLevel',
            data:{
            	orgId:orgId
            },
            dataType: "json"
        };
    $.ajax(ajax_option).done(function(data) {
        for (var index = 0; index < data.length; index++) {
        	var item = data[index];
        	var strHtml = '<li>';
        	strHtml += '<p>' +item.orgName+'</p>';
        	if(item.leaderId == null){
        		strHtml += '<p class="org_op_p_preson" user="" userId ="">'  +  '&nbsp;' +'</p>';
        	}else {
        		strHtml += '<p class="org_op_p_preson" user="'+item.leaderId+    '"userId ="' +item.orgLeader+'">'  +  item.leaderName +'</p>';
        	}
        	strHtml += '<p class="org_op_p" orgId ="'+ item.orgId+'">';
        	strHtml += '<span>'+ item.orgName +'</span>';
        	strHtml += '<span>' + (item.leaderName == null ? "" : item.leaderName);
        	strHtml += '</span> <a href="javascript:void(0)" class="org_depart_edit">修改</a><a href="javascript:void(0)" class="org_del">删除</a></p>'
        	strHtml += '</li>';
        	object.parent().next().append(strHtml);
        }
        var nextLevel = object.parent().next().attr('orglevel');
        if(nextLevel == "5"){
        	return;
        }
        var li_op = object.parent().next().find('li');
        li_op.each(function() {
            if ($(this).attr('class') != 'title') {
            	var orgId = $(this).find('.org_op_p').attr('orgid');
            	     //修改按钮
                    $(this).off('click').on('click',function(e){
                    	if(e.target.nodeName =='A'){
                    		return;
                    	}
                    	if($(this).attr('class') != 'cur'){
                    		$(this).siblings().removeClass('cur');
                    		$(this).addClass('cur');
                    		//权限
                    		checkAuthority($(this));
                    		//删除子节点
                    		$(this).parent().nextAll().find('li:not(.title)').remove();
                    		var team = $(this).attr('team');
                    		if(nextLevel == "4"){
                    			serchMemberAjax($(this),orgId);
                    		} else {
                    			serchChildAjax($(this),orgId);
                    		}
                    	}
                    }) 
            };
        }); 
    })
}

function serchMemberAjax(object,orgId) {
	var ajax_option = {
            type: "POST",
            url:contextPath + '/orgManage/getTeamMembers',
            data:{
            	orgId:orgId
            },
            dataType: "json"
        };
    $.ajax(ajax_option).done(function(data) {
        for (var index = 0; index < data.length; index++) {
        	var item = data[index];
        	var strHtml = '<li style="height:50px" orglevel = "5">';
        	if(item.userId == null){
        		strHtml += '<p class="org_op_p_preson"  user="" userId =""  style="display: block;margin-top: 12px;">'  +  '&nbsp;' +'</p>';
        	} else {
        		strHtml += '<p class="org_op_p_preson"  user="'+item.userId+    '" userId ="' +item.id+'"  style="display: block;margin-top: 12px;">'  +  item.userName +'</p>';	
        	}
        	strHtml += '<p class="org_op_p"  orgId = "" style="display: none;"><span style="height:35px;line-height:50px;">'+ item.userName+'</span><a href="javascript:void(0)" class="org_del">删除</a></p>';
        	strHtml += '</li>';
        	object.parent().next().append(strHtml);
        }
    })
}

function org_op(name) {
    if (Object.prototype.toString.call(name).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'string') return;
    this.panel = name || ""
}

org_op.prototype = {
    constructor: org_op,
    init: function() {
        this.op();
    },
    op: function() {
        var _this = this;
        $(_this.panel).on('click', 'a', function(e) {
            var that = this;
            e = arguments.callee.caller.arguments[0]||window.event;
            switch ((e.target.className)) {
                case 'org_add':
                	var $div = $('<div></div>').append($('#org_tpl').html());
                    //判断添加人员
                      if($(this).parents('.org_op_ul').attr('orglevel') == '5'){
                      	$div.find('p').eq(0).hide();
                      } 
                      var parentId = '';
                      var parentUl = $(this).parents('.org_op_ul').prev();
                      if(parentUl){
                    	  parentId = parentUl.find('.cur').find('.org_op_p').attr('orgid');
                      };
                      if(!parentId){
                    	  parentId = '';
                      }
                      $div.find('input').eq(0).attr('parentid',parentId);
                    var flagTitle = $(this).prev("span").html();
                    var d = dialog({
                        content: $div,
                        okValue: '确定',
                        ok: function() {
                        	var orgName = $('.dialog_org:visible').find('.depart_name').val();
                        	var orgParent = $('.dialog_org:visible').find('.depart_name').attr('parentid');
                        	var orgLevel = 1;
                        	if(orgParent){
                        		orgLevel = orgParent.length / 2 + 1;
                        	} else {
                        		orgParent = '';
                        	}
                        	var selectUserVal = $('.dialog_org:visible').find('#userSelect').val();
                        	var orgLeader = null;
                        	var leaderId = null;
                        	var orgLeaderName = null;
                        	if(typeof selectUserVal=="string" && $.trim(selectUserVal)!=''){
                            	var userVals = uniQueue(selectUserVal.split(";"));
                            	var flagLeaderId = "";
                            	var flagOrgLeader = "";
                            	var flagOrgLeaderName = "";
                            	for(var i=0;i<userVals.length;i++){
                            		if(userVals[i]!=""){
                            			var userObjIds = userVals[i].split("(");
                            			var userTid = $("#"+userObjIds[0]).val();
                            			if(typeof userTid == 'undefined'){
                            				continue;
                            			}
                            			flagOrgLeader += userTid +";";
                            			flagOrgLeaderName += $("#"+userObjIds[0]).attr('userName') + ";";
                            			flagLeaderId += userObjIds[0]+";";
                            		}
                            	}
                            	orgLeader = flagOrgLeader.substr(0,flagOrgLeader.lastIndexOf(";"));
                            	$('.dialog_org:visible').find('#userSelect').attr('userid',orgLeader);
                            	orgLeaderName = flagOrgLeaderName.substr(0,flagOrgLeaderName.lastIndexOf(";")); 
                            	$('.dialog_org:visible').find('#userSelect').attr('userName',orgLeaderName);
                            	leaderId = flagLeaderId.substr(0,flagLeaderId.lastIndexOf(";"));
                            	$('.dialog_org:visible').find('#userSelect').attr('user',leaderId);
                        	}
                        	if(!orgName){
                        		$('.dialog_org:visible').find('.error_tips').html('请输入'+flagTitle+'名称！');
                        		return false;
                        	}
                        	data = {
                        			orgName:orgName,
                        			orgParent:orgParent,
                        			orgLevel:orgLevel,
                        			orgLeader:orgLeader,
                        			orgLeaderName:orgLeaderName,
                        			leaderId:leaderId
                        	};
                        	addOrgAjax(data);
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: "添加"+flagTitle+"内容"
                    }).width(400);
                    d.showModal();
                    orgPersonAutocomplete();
                    break;
                case 'member_add':
                	var $div = $('<div></div>').append($('#org_tpl').html());
                    //判断添加人员
                      if($(this).parents('.org_op_ul').attr('orglevel') == '5'){
                      	$div.find('p').eq(0).hide();
                      } 
                      var parentId = '';
                      var parentUl = $(this).parents('.org_op_ul').prev();
                      if(parentUl){
                    	  parentId = parentUl.find('.cur').find('.org_op_p').attr('orgid');
                      };
                      if(!parentId){
                    	  parentId = '';
                      }
                      $div.find('input').eq(0).attr('parentid',parentId);
                      var flagTitle = $(this).prev("span").html();
                    var d = dialog({
                        content: $div,
                        okValue: '确定',
                        ok: function() {
                            var userName = $('.dialog_org:visible').find('#userSelect').attr('userName');
                            var user = $('.dialog_org:visible').find('#userSelect').attr('user');
                        	var orgId = $('.dialog_org:visible').find('.depart_name').attr('parentid');
                        	var userId = $('.dialog_org:visible').find('#userSelect').attr('userid');
                        	if(!userId){
                        		$('.dialog_org:visible').find('.error_tips').html('请输入小组成员');
                        		return false;
                        	}
                        	
                        	data = {
                        			orgId:orgId,
                        			userId:userId,
                        			userName:userName,
                        			user:user,
                        			orgLevel:"5"
                        	};
                        	addMemberAjax(parentUl.find('.cur'),data);

                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: "添加"+flagTitle+"内容"
                    }).width(400);
                    d.showModal();
                    orgPersonAutocomplete();
                    break;
                case 'org_edit':
                    $(this).text() === '编辑' ? ($(this).text('完成') && $(this).closest('ul').find('li').find('p').toggle()) : ($(this).text('编辑') && $(this).closest('ul').find('li').find('p').toggle())
                    e.stopPropagation();
                    e.preventDefault();
                    break;
                case 'org_depart_edit':
                    var param = {};
                    $(that).closest('p').siblings().each(function() {
                    	if($(this).attr('class')=='org_op_p_preson'){
                    		param.userId = $(this).attr('userid');
                    		param.userName = $(this).text();
                    		param.user = $(this).attr('user');
                    	}else {
                    		param.orgId = $(that).closest('p').attr('orgid');
                    		param.orgName = $(this).text();
                    	}
                    });
                    var $div = $('<div></div>').append($('#org_tpl').html());
                    $div.find('input').each(function(i) {
                    	if($(this).attr('class')=='depart_name'){
                    		$(this).attr('orgid',param.orgId);
                   		 	$(this).val(param.orgName);
                    	}else {
                    		$(this).attr('userid',param.userId);
                    		$(this).attr('user',param.user);
                    		$(this).attr('userName',param.userName);
                    		if(typeof param.userName=="string" && $.trim(param.userName)==""){
                    			$(this).val("");
                    		}else {
                    			var userIdStrs = param.userId.split(";");
                    			var userStrs = param.user.split(";");
                    			var userNameStrs = param.userName.split(";");
                    			var showVal = "";
                    			for(var i=0;i<userStrs.length;i++){
                    				showVal += userStrs[i]+'('+userNameStrs[i]+');'
                    				$(this).after("<input type='hidden' id='"+userStrs[i]+"' value='"+userIdStrs[i]+"' userName='"+userNameStrs[i]+"'/>");
                    			}
                    			$(this).val(showVal);
                    		}
                    	}
                    });
                    //判断添加人员
                    if($(this).parents('.org_op_ul').attr('orglevel') == '5'){
                    	$div.find('p').eq(0).hide();
                    }
                    var orgStr = $(this).parent("p").siblings("p:eq(0)").html();
                    var levelStr = $(this).parent("p").parent("li").siblings(".title").find("span").html();
                    var d = dialog({
                        content: $div,
                        okValue: '确定',
                        ok: function() {
                        	var selectUserVal = $('.dialog_org:visible').find('#userSelect').val();
                        	var orgLeader = null;
                        	var leaderId = null;
                        	var orgLeaderName = null;
                        	if(typeof selectUserVal=="string" && $.trim(selectUserVal)!=''){
                            	var userVals = uniQueue(selectUserVal.split(";"));
                            	var flagLeaderId = "";
                            	var flagOrgLeader = "";
                            	var flagOrgLeaderName = "";
                            	for(var i=0;i<userVals.length;i++){
                            		if(userVals[i]!=""){
                            			var userObjIds = userVals[i].split("(");
                            			var userTid = $("#"+userObjIds[0]).val();
                            			if(typeof userTid == 'undefined'){
                            				continue;
                            			}
                            			flagOrgLeader += userTid +";";
                            			flagOrgLeaderName += $("#"+userObjIds[0]).attr('userName') + ";";
                            			flagLeaderId += userObjIds[0]+";";
                            		}
                            	}
                            	orgLeader = flagOrgLeader.substr(0,flagOrgLeader.lastIndexOf(";"));
                            	$('.dialog_org:visible').find('#userSelect').attr('userid',orgLeader);
                            	orgLeaderName = flagOrgLeaderName.substr(0,flagOrgLeaderName.lastIndexOf(";")); 
                            	$('.dialog_org:visible').find('#userSelect').attr('userName',orgLeaderName);
                            	leaderId = flagLeaderId.substr(0,flagLeaderId.lastIndexOf(";"));
                            	$('.dialog_org:visible').find('#userSelect').attr('user',leaderId);
                        	}
                        	var orgName = $('.dialog_org:visible').find('.depart_name').val();
                        	var orgId = $('.dialog_org:visible').find('.depart_name').attr('orgid');
                        	if(!orgName){
                        		$('.dialog_org:visible').find('.error_tips').html('请输入所修改的'+flagTitle+'名称！');
                        		return false;
                        	}
                        	data = {
                        			orgName:orgName,
                        			orgLeader:orgLeader,
                        			orgLeaderName:orgLeaderName,
                        			leaderId:leaderId,
                        			orgId:orgId
                        	};
                        	editOrgAjax(data,that);
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: '修改('+orgStr+')'+levelStr+'内容'
                    }).width(400);
                    d.showModal();
                    orgPersonAutocomplete();
                    break;
                case 'org_del':
                    var $that = $(this);
                    var d = dialog({
                        title: '删除内容',
                        content: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;确定要删除吗',
                        okValue: '确定',
                        ok: function() {
                            var li = $(that).closest('li')
                            var orgId = li.find('.org_op_p').attr('orgid');
                            var id = li.find('.org_op_p_preson').attr('userid');
                            delOrgAjax(li,orgId,id);
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: '删除内容'
                    });
                    d.showModal().width(250);
                    e.stopPropagation();
                    break;
            }
            e.stopPropagation();
        })
    }
}
function addOrgAjax(ajaxParms) {
	var ajax_option = {
            type: "POST",
            url:contextPath + '/orgManage/addOrg',
            data:ajaxParms,
            dataType: "json"
        };
    $.ajax(ajax_option).done(function(data) {
    	if(data!=null && data.code=='success'){
     		var orgId = data["data"];
	   		smoke.alert(data.result, {ok: "确定"}, function(){
	    		var newOrg = $('#panel_tpl').html();
	     		var ul = $('.org_op_ul[orglevel = "' + ajaxParms.orgLevel + '"]');
	     		ul.append(newOrg);
	     		var allLi = ul.find('li');
	     		var newLi = allLi.eq(allLi.length - 1);
	     		newLi.find('p').eq(0).html(ajaxParms.orgName);
	     		newLi.find('.org_op_p_preson').attr('user',ajaxParms.leaderId).attr('userid',ajaxParms.orgLeader).html(ajaxParms.orgLeaderName);
	     		if(ajaxParms.orgLeaderName == null){
	    			newLi.find('.org_op_p_preson').html('&nbsp;');
	    		} else {
	    			newLi.find('.org_op_p_preson').html(ajaxParms.orgLeaderName);
	    		}
	     	 	newLi.find('.org_op_p').attr('orgid',orgId);
	        	newLi.find('.org_op_p').find('span').eq(0).html(ajaxParms.orgName);
	        	newLi.find('.org_op_p').find('span').eq(1).html(ajaxParms.orgLeaderName);
	          	if(ul.find('.title .org_edit').text() === '完成'){
	        		newLi.find('p').toggle();
	        	}
	      	  //修改按钮
	        	newLi.off('click').on('click',function(e){
	            	if(e.target.nodeName =='A'){
	            		return;
	            	}
	            	if($(this).attr('class') != 'cur'){
	            		$(this).siblings().removeClass('cur');
	            		$(this).addClass('cur');
	            		//权限
	            		checkAuthority($(this));
	            		//删除子节点
	            		$(this).parent().nextAll().find('li:not(.title)').remove();
	            		var team = $(this).attr('team');
	            		if(ajaxParms.orgLevel == "4"){
	            			serchMemberAjax($(this),orgId);
	            		} else {
	            			serchChildAjax($(this),orgId);
	            		}
	            	}
	            });
	   		});
    	}else{
    		layer.msg(data.result);
    	}
    });
}
function addMemberAjax(object,memberParms) {
	var param = {};
	param.orgLeader = memberParms.userId;
	param.orgId = memberParms.orgId;
	param.userId = memberParms.user;
	var ajax_option = {
            type: "POST",
            url:contextPath + '/orgManage/addMember',
            data:param,
            dataType: "json"
       };
    $.ajax(ajax_option).done(function(data) {
    	if(data!=null && data.code=='success'){
    		var members = data["data"];
    		smoke.alert(data.result, {ok: "确定"}, function(){
    			for (var index = 0; index < members.length; index++) {
    	        	var item = members[index];
    	        	var strHtml = '<li style="height:50px" orglevel = "5">';
    	        	if(item.userId == null){
    	        		strHtml += '<p class="org_op_p_preson"  user="" userId =""  style="display: block;margin-top: 12px;">'  +  '&nbsp;' +'</p>';
    	        	} else {
    	        		strHtml += '<p class="org_op_p_preson"  user="'+item.userId+    '" userId ="' +item.id+'"  style="display: block;margin-top: 12px;">'  +  item.userName +'</p>';	
    	        	}
    	        	strHtml += '<p class="org_op_p"  orgId = "" style="display: none;"><span style="height:35px;line-height:50px;">'+ item.userName+'</span><a href="javascript:void(0)" class="org_del">删除</a></p>';
    	        	strHtml += '</li>';
    	        	object.parent().next().append(strHtml);
    	        }
    		});
    	}else{
    		layer.msg(data.result);
    	}
    })
}

function delOrgAjax(object,orgId,id) {
	var url = '';
	var data = {};
	if(orgId != ""){
		data.orgId = orgId;
		url = contextPath + '/orgManage/deleteOrg';
	} else {
		data.id = id;
		url = contextPath + '/orgManage/deleteMember';
	}
	var ajax_option = {
            type: "POST",
            url:url,
            data: data,
            dataType: "json"
        };
    $.ajax(ajax_option).done(function(data) {
    	if(data!=null && data.code=='success'){
    		smoke.alert(data.result, {ok: "确定"}, function(){
    			if(object.attr('class') == 'cur'){
    				object.parent().nextAll().find('li:not(.title)').remove();
    				object.parent().nextAll().find('li.title').find('a').hide();
    			};
    			object.remove(); 
    		});
    	}else{
    		layer.msg(data.result);
    	}
    })
}

function editOrgAjax(ajaxParms,object) {
	var ajax_option = {
            type: "POST",
            url:contextPath + '/orgManage/updateOrg',
            data:ajaxParms,
            dataType: "json"
        };
    $.ajax(ajax_option).done(function(data) {
    	if(data!=null && data.code=='success'){
    		smoke.alert(data.result, {ok: "确定"}, function(){
    			$(object).parent().parent().find('.org_op_p_preson').attr('user',ajaxParms.leaderId).attr('userid',ajaxParms.orgLeader);
        		if(ajaxParms.orgLeaderName == null){
        			$(object).parent().parent().find('.org_op_p_preson').html('');
        		} else {
        			$(object).parent().parent().find('.org_op_p_preson').html(ajaxParms.orgLeaderName);
        		}
        		$(object).parent().find('span').eq(0).html(ajaxParms.orgName);  
        		$(object).parent().find('span').eq(1).html(ajaxParms.orgLeaderName);  
    		});
    	}else {
    		layer.msg(data.result);
    	}
    })
}
new org_op('.org_op_ul').init();
