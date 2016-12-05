function GomeProject_CommonBar(config) {
         if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
         this.page = config.laypage || "";
         this.calendar_table = config.work_hour_table || '';
         this.content_table = config.work_content_table || '';
         this.addButton = config.addButton || '';
         this.resultData=[];
         this.onOff=true; //默认都保存完了
     }

     GomeProject_CommonBar.prototype = {
         constructor: GomeProject_CommonBar,
         init: function() {
        	 var _this=this;
             this.Table_op();
             this.appendDailyDialog();
             this.initAjax(_this.page,true);
         },
         initAjax:function(page,isInit){
            var _this=this;
            var ajax_option = {
                type: "POST",
                url:contextPath + '/daily/getDailyData/' + page,
                dataType: "json"
            };
            $.ajax(ajax_option).done(function(data) {
               _this.dataPages = data.pages;
               _this.afterAjax(data,isInit,1);
               laypage({
                   cont: 'page',
                   pages: _this.dataPages,
                   curr: 1,
                   jump: function(obj, first) {
                       if (!first) {
                           $.ajax({
                        	   type: "POST",
                               url:contextPath + '/daily/getDailyData/' + obj.curr,
                               dataType: "json"
                           }).done(function(data) {
                        	   _this.onOff=true;
                               _this.dataPages = data.pages;
                               _this.afterAjax(data,isInit,obj.curr);
                           })
                       }
                   }
               });  
            });
         },
         afterAjax:function(data,isInit,page){
            var _this=this;
            if(data.resultCode == "0"){ //服务器错误
            	 smoke.alert(data.resultData.message, {ok: "确定"});
                 return false;
             }
            _this.resultData=$.extend(true, [], data.resultData);
            if(page == 1){
            	 //如果不存在数据新增一条新数据
                if(!_this.resultData.length){
                    //创建一条数据
                    var data={"id":"","type":1,"startTime":"","endTime":"","workContent":"","hours":0.5,"proId":0,"createUser":null,"createTime":null};
                    _this.onOff=false;
                    _this.resultData.push(data);
                }else{
                    _this.onOff=true;
                }
            }
            _this.renderAjax(_this.resultData,isInit,page);
        },Table_op: function() {
             var _this = this;
             $(_this.calendar_table).off('click', 'td').on('click', 'td', function() {
                 $(this).addClass('selected');
                 var tpl = $('#calender_tab').html();
                 $(_this.content_table).show().append(tpl);
             })
         },
         appendDailyDialog: function() {
            var _this = this;
            //添加日报
             $(_this.addButton).on('click', function() {
                var name=$(this).text();
                var ajax_option = {
                    type: "POST",
                    url:contextPath + '/daily/getDailyData/' + 1,
                    dataType: "json"
                };
                $.ajax(ajax_option).done(function(data) {
                   _this.resultData = $.extend(true, [], data.resultData);//继承数据
                   _this.dataPages = data.pages;
                   var newlyObj = getMaxObjByType(_this.resultData,"create");
                   var newlyStartTime = newlyObj.startTime;
                   var newlyCreateTime = newlyObj.createTime;
                   var nowTime = laydate.now(0, "YYYY-MM-DD hh:mm:ss");
                   //添加时的开始时间 
                   var plugStartData,plugHours,plugEndDate;
                   if(_this.resultData.length>0){
                	   //今天是否有写日报,当前日期与最近创建日期比较
                	   if(nowTime.split(" ")[0] == newlyCreateTime.split(" ")[0]){//今天写了日报
                    	   if(newlyObj.type!="3"){//公休类型
                    		   //1，日期=>当日已填写的日期相同，
                    		   plugStartData = newlyObj.endTime;
                    		   //2，工时=>按上一条填写的工时
                    		   plugHours = newlyObj.hours
                    	   }else{
                    		   plugStartData = _this.resultData[0].endTime;
                    	   }
                	   }else{//今日没写日报
                		   if(nowTime.split(" ")[0]==_this.resultData[0].endTime){
                			   plugStartData = _this.resultData[0].endTime;
                		   }else{
                			   //根据日期分组 之后  再根据开始时间排序
                			   var nearlyStartGroupObjs =  groupDailyDatasByStartType(_this.resultData,"nearly");
                			   var nearlyObj = getMaxObjByType(nearlyStartGroupObjs,"start");
                			   plugStartData = nowTime.split(" ")[0] +" "+nearlyObj.startTime.split(" ")[1];
                		   }
                		   plugHours = 0.5;
                	   }
	               	   plugEndDate = getDailyEndTime(plugStartData,plugHours);
                   }
                   
                   var data={"id":"","type":1,"startTime":plugStartData||"","endTime":plugEndDate||"","workContent":"","hours":plugHours||0.5,"proId":0,"createUser":null,"createTime":null};
                    _this.onOff=false;
                    _this.resultData.unshift(data);
                    _this.renderAjax(_this.resultData,false,1);
                });  
             })
         },
         renderAjax:function(dailyList,isInit,page){
            var _this = this;
        	var ajax_sysDate = {
                type: "POST",
                url:contextPath + '/daily/getSysDate',
                dataType: "json"
             };
            $.ajax(ajax_sysDate).done(function(data) {
            	if(data!=null && data.code=='success'){
            		var sysNowTime  = data["data"];
//                	//当前时间
                	var sysToday = new Date(sysNowTime);
                	var nowDateMs = sysToday.getTime();
            		var sysNowDate = sysNowTime.split(" ")[0];
            		var todayMs = new Date(sysNowDate.replace(/-/g,"/")).getTime();
            		var defaultStartDate = sysNowDate+' 08:00';
            		var daily_content='';
            		if(page == 1 && isInit && _this.onOff){
                        if(isInit){
                        	var firstDaily = dailyList[0];
                        	//判断是否有今天的日报
                            if(firstDaily.startTime.split(" ")[0] != sysNowDate){//没有今天的日报
                 			    var nearlyStartGroupObjs = groupDailyDatasByStartType(dailyList,"nearly");
                			    var nearlyObj = getMaxObjByType(nearlyStartGroupObjs,"start");
                			    var plugStartData = sysNowTime.split(" ")[0] +" "+nearlyObj.startTime.split(" ")[1];
                			    var plugStartTime = plugStartData||defaultStartDate;
                            	var data={"id":"","type":1,"startTime":plugStartTime,"endTime":"","workContent":"","hours":0.5,"proId":0,"createUser":null,"createTime":null};
                                _this.onOff=false;
                                _this.resultData.unshift(data);
                            }
                            dailyList = _this.resultData;
                        }
            		}
                    for (var index = 0; index < dailyList.length; index++) {
                        var tpl = $('#ul_tpl').html();
                        daily_content+=tpl;
                    }
                    var contentMain = $('.GomeProject_tab_content_main');
                    contentMain.find('.log_ul').remove();
                    contentMain.prepend(daily_content);
                    var log_ul= $('.GomeProject_tab_content_main .log_ul');
                    
                    for (var index = 0; index < dailyList.length; index++) {
                    	var daily = dailyList[index];
                    	var dailyId = daily.id;
                    	var dailyType = daily.type;
                    	var dailyStartTime = daily.startTime.substring(0,16)||defaultStartDate;
                    	var dailyEndTime = daily.endTime.substring(0,16);
                    	var dailyHours = daily.hours||"0.5";
                    	var dailyContent = daily.workContent;
                    	var dailyProId = daily.proId;
                    	var dailyProTaskId = daily.proTaskId;
                    	var dailyProTaskName = daily.proTaskName;
                    	var dailyProTaskSchedule = daily.proTaskSchedule;
                    	var dailyCreateTime = daily.createTime;
                    	var start_endtime = getDailyEndTime(dailyStartTime,dailyHours); //根据开始时间算结束时间
                    	var endTimeShow = dailyEndTime||start_endtime;
                    	log_ul.eq(index).find('.laydate-icon').val(dailyStartTime);
                    	log_ul.eq(index).find('.spinner input').val(dailyHours.toFixed(2));
                    	log_ul.eq(index).find('.form-control').val(endTimeShow.split(' ')[1]);
                    	log_ul.eq(index).find('.form-control').attr('endtime',endTimeShow);
                    	log_ul.eq(index).find('.edit_box').html(dailyContent);
                    	var options_project = log_ul.eq(index).find('select[name="project"] option');
                    	var options_relateditems = log_ul.eq(index).find('select[name="relateditems"] option');
                    	for(var i=0;i<options_project.length;i++){
                    		if(Number(options_project.eq(i).attr('project_type'))==dailyType){
                    			options_project.eq(i).attr('selected','selected');
                    		}
                    	}
                    	for(var i=0;i<options_relateditems.length;i++){
                    		if(Number(options_relateditems.eq(i).attr('proid'))==dailyProId){
                    			options_relateditems.eq(i).attr('selected','selected');
                    		}
                    	}
                    	if(dailyProTaskId!=null){
                    		var str = '<option schedule="'+ dailyProTaskSchedule+'"  value="' + dailyProTaskId + '">' + dailyProTaskName + '</option>';
                    		log_ul.eq(index).find('select[name="proTaskId"]').html(str);
                    		log_ul.eq(index).find(':text[name="proTaskSchedule"]').val(dailyProTaskSchedule);
	    					log_ul.eq(index).find(".proTaskBox").css("display","inline-block");
                    		log_ul.eq(index).find(".proTaskBox").show("slow");
                    	}
                    	var startTimeAry = dailyStartTime.split(' ')[0].split('-');
                    	var endTimeAry = endTimeShow.split(' ')[0].split('-');
                    	//日报开始时间
                    	var startTimeMs = new Date(startTimeAry[0],startTimeAry[1]-1,startTimeAry[2]).getTime();
                    	//日报结束时间
                    	var endTimeMs = new Date(endTimeAry[0],endTimeAry[1]-1,endTimeAry[2]).getTime();
                    	
                    	//日报创建时间
                    	var createMs = dailyCreateTime == null?nowDateMs:new Date(dailyCreateTime).getTime();
                    	//日报最小时间
                    	var minDayMs = todayMs - 3 * 24*60*60*1000;
                    	if(sysToday.getDay()>0 && sysToday.getDay()<4){//如果非周四、周五则再往前推2天时间
                    		minDayMs = minDayMs - 2*24*60*60*1000;
                    	}
                    	//日报明天时间
                    	var tomorrowMs = todayMs + 24*60*60*1000;
                    	if(startTimeMs >= minDayMs && endTimeMs == startTimeMs && typeof dailyId=="number"){
                    		log_ul.eq(index).find('.del_button').show();
                    		log_ul.eq(index).find('.edit_button').show();
                    		log_ul.eq(index).find('.save_button').hide();
                    		log_ul.eq(index).find('.edit_button_dis').hide();
                    		log_ul.eq(index).find('.del_button_dis').hide();
                    	} else if(typeof dailyId!="number") {
                    		log_ul.eq(index).find('.del_button').show();
                    		log_ul.eq(index).find('.save_button').show();
                    		log_ul.eq(index).find('.edit_button').hide();
                    		log_ul.eq(index).find('.edit_button_dis').hide();
                    		log_ul.eq(index).find('.del_button_dis').hide();
                    		log_ul.eq(index).find('.opacity').hide();
                    	}else if(nowDateMs > startTimeMs && typeof dailyId=="number"){
                    		log_ul.eq(index).find('.del_button').hide();
                    		log_ul.eq(index).find('.edit_button').hide();
                    		log_ul.eq(index).find('.save_button').hide();
                    		log_ul.eq(index).find('.edit_button_dis').show();
                    		log_ul.eq(index).find('.del_button_dis').show();
                    	}
                    	log_ul.eq(index).find('.del_button').attr('delId',dailyId);
                    	log_ul.eq(index).find('.save_button').attr('saveId',dailyId);
                    	log_ul.eq(index).attr('index',index);
                    }
                    _this.button_op(sysNowTime);
            	}
            }); 
         },
         button_op: function(sysNowTime){
            var _this=this;
            //默认情况下计算时间,加减工时加载
	           $('.GomeProject_tab_content_main select[name="project"]').each(function(){
	                var maxtime=Number($(this).find("option:selected").attr('maxtime'));
	                $(this).parents('.log_ul').find('.spinner input').attr('data-max',maxtime);
	                 _this.showDailyHours($('.spinner'));
	           });
            //切换项目类型时
            $('body').off('change' ,'select[name="project"]').on('change' ,'select[name="project"]',function(){
                var maxtime = Number($(this).find("option:selected").attr('maxtime'));
                $(this).parents('.log_ul').find('.spinner input').attr('data-max',maxtime);
                var projectType = $(this).parents('.log_ul').find('select[name="project"] option:selected').attr('project_type');
            	var spinnerVal = Number($(this).parents('.log_ul').find('.spinner input').val());
            	if(spinnerVal>maxtime){
            		$(this).parents('.log_ul').find('.spinner input').val(maxtime+".00");
            	}
                _this.showDailyHours($('.spinner'));
                _this.endTimeVal($(this));
                if(projectType!="1" && projectType!="2"){
                	$(this).parents('.log_ul').find(".relatedProBox").hide();
                }else{
                	$(this).parents('.log_ul').find(".relatedProBox").show();
                }
                $(this).parents('.log_ul').find(".proTaskBox").hide();
                $(this).parent().parent().find(".related_project_select option[value='']").attr("selected", true); 
                $(this).parent().parent().find(".related_project_select option[value='']").siblings().attr("selected", false); 
            });
            //切换关联项目时
            $('body').off('change' ,'select[name="relateditems"]').on('change' ,'select[name="relateditems"]',function(){
            	 var thisObj = $(this);
            	 var proId = thisObj.parents('.log_ul').find('select[name="relateditems"] option:selected').attr('proid');
            	 if(proId!=""){
            		 $.post(
            			contextPath + '/task/getProTasks',
        		    	{proId:proId},
        		    	function (data){
    		    			if(data!=null && data.code=='success'){
    		    				var tasks = data["data"];
    		    				if(tasks!=null && tasks.length>0){
    		    					var str = '';
    		    					var firstProSchedule ="";
    		    					tasks.forEach(function(item,i) {
    		    						if(i==0){
    		    							firstProSchedule = item.schedule;
    		    						}
		    					        str += '<option schedule="'+item.schedule+'"  value="' + item.id + '">' + item.taskName + '</option>';
		    					    });
    		    					thisObj.parent().parent().next(".proTaskBox").find("select[name='proTaskId']").html(str);
    		    					thisObj.parent().parent().next(".proTaskBox").find(":text[name='proTaskSchedule']").val(firstProSchedule);
    		    					thisObj.parent().parent().next(".proTaskBox").css("display","inline-block");
    		    					thisObj.parent().parent().next(".proTaskBox").show();
    		    				}else{
    		    					 thisObj.parent().parent().next(".proTaskBox").hide();
    		    				}
    		    			}else{
    		    				 thisObj.parent().parent().next(".proTaskBox").hide();
    		    			}
    		    		}
        		    );
            	 }else{
            		 thisObj.parent().parent().next(".proTaskBox").hide();
            	 }
            });
            //切换项目任务时
            $('body').off('change' ,'select[name="proTaskId"]').on('change' ,'select[name="proTaskId"]',function(){
            	 var schedule = $(this).parents('.log_ul').find('select[name="proTaskId"] option:selected').attr('schedule');
            	 if(schedule!=null && schedule!=""){
            		 $(this).parent().siblings(".taskSchedule").find(":text[name='proTaskSchedule']").val(schedule);
            	 }else{
            		 $(this).parent().siblings(".taskSchedule").find(":text[name='proTaskSchedule']").val("0");
            	 }
            });
            //修改按钮
            $('body').off('click' ,'.edit_button').on('click' ,'.edit_button',function(){
            	var log_ul = $(this).parents('.log_ul');
            	var dailyStartTimeObj = log_ul.find('.laydate-icon');
            	var dailyStartTime = dailyStartTimeObj.val();
            	var dailyId = $(this).siblings(".save_button").attr('saveId');
            	var dailyList = _this.resultData;
            	var groupDailyObjs = groupDailyDatasByStartType(dailyList,dailyStartTime.split(" ")[0]);
            	var dailyObj = getMaxObjByType(groupDailyObjs,"start");
            	var proTaskBoxDisplay =  log_ul.find('.proTaskBox').css("display");
            	if(typeof proTaskBoxDisplay=="string" && proTaskBoxDisplay!="none"){
            		log_ul.find('select[name="relateditems"]').attr("disabled",true);
            		log_ul.find('select[name="proTaskId"]').attr("disabled",true);
            		log_ul.find(':text[name="proTaskSchedule"]').attr("disabled",true);
            	}
            	if(dailyId != dailyObj.id){//不为开始时间的那一天的第一条日报 
            		//不能修改日期  只能修改工时
            		dailyStartTimeObj.attr("disabled",true);
            	}
        		$(this).parents('.li_op').find('.del_button').show();
        		$(this).parents('.li_op').find('.save_button').show();
        		$(this).parents('.li_op').find('.edit_button').hide();
        		$(this).parents('.log_ul').find('.opacity').hide();
        		_this.onOff=false;
            });
            
            //删除按钮
            $('body').off('click' ,'.del_button').on('click' ,'.del_button',function(){
                var obj = $(this);
            	smoke.confirm("确定要删除该条日报？", function (e) {
                    if (e) {
                    	 _this.delDaily(obj); //删除数据
            		}
            	}, {ok:"确定", cancel:"取消", reverseButtons: true});
            });
            //保存按钮
            $('body').off('click' ,'.save_button').on('click' ,'.save_button',function(){
                var log_ul = $(this).parents('.log_ul');
                var dailyId = $(this).attr('saveId');
                var dailyStartTime = log_ul.find('.laydate-icon').val();
                var dailyEndTime = log_ul.find('.form-control').attr('endtime');
                var dailyHours = log_ul.find('.spinner input').val();
                var dailyType = log_ul.find('select[name="project"] option:selected').attr('project_type');
                var dailyProid = log_ul.find('select[name="relateditems"] option:selected').attr('proId');
                var dailyContent = log_ul.find('.edit_box').html();
                var proTaskId = log_ul.find('select[name="proTaskId"] option:selected').val();
                var proTaskName = log_ul.find('select[name="proTaskId"] option:selected').text();
                var proTaskSchedule = log_ul.find(":text[name='proTaskSchedule']").val();
                if(!dailyStartTime){
                	smoke.alert('日报开始日期不能为空', {ok: "确定"});
                	return false;
                }
                var regTaskSchedule = /^(100|[1-9]\d|\d)$/;
                var proTaskBoxDisplay =  log_ul.find('.proTaskBox').css("display");
            	if(typeof proTaskBoxDisplay=="string" && proTaskBoxDisplay!="none"){
            		if(!regTaskSchedule.test(proTaskSchedule)){
            			smoke.alert('任务进度只可填写0-100的整数', {ok: "确定"});
            			return false;
            		}
            	}
                var startTime = new Date(dailyStartTime);
                var offDuty = new Date(startTime.getFullYear() + '/' + (startTime.getMonth() + 1) + '/' + startTime.getDate()+ ' 18:30:00');
                var maxHours = Number(log_ul.find('.spinner input').attr('data-max'));//最大工时
                if(dailyEndTime.split(' ')[0].replace(/-/g,'').replace(/\//g,'')!=dailyStartTime.split(' ')[0].replace(/-/g,'').replace(/\//g,'')){ //不是同一天
                   smoke.alert('开始日期与结束日期已跨天，请修改此日报开始日期或工时', {ok: "确定"});
                   return false;
               }
               if(maxHours == 8 && new Date(dailyEndTime).getTime() > offDuty.getTime()){ //公休
            	   smoke.alert('公休结束时间不得超过18:30', {ok: "确定"});
                   return false;
               }
               var url = '';
               var isCreate = false;
               var dailyParams = {
                   type:dailyType,
                   startTime:dailyStartTime.replace(/\//g,'-') + ':00',
                   endTime:dailyEndTime.replace(/\//g,'-') + ':00',
                   workContent:dailyContent,
                   hours:dailyHours,
                   proId:dailyProid,
                   proTaskId:proTaskId,
                   proTaskName:proTaskName,
                   proTaskSchedule:proTaskSchedule
               };
               if(dailyId){
                   url = contextPath + '/daily/updateDaily';
                   dailyParams.id = dailyId;
               } else {
                   url = contextPath + '/daily/addDaily';
                   isCreate= true;
               }
               var ajax_option = {
                       type:"POST",
                       url:url,
                       data:dailyParams,
                       dataType:"json"
                 };
               $.ajax(ajax_option).done(function(data) {
	               	if(data!=null && data.code=='success'){
                       log_ul.find('.opacity').show();
                       log_ul.find('.del_button').show();
                       log_ul.find('.edit_button').show();
                       log_ul.find('.save_button').hide();
                       log_ul.find('.edit_button_dis').hide();
                       log_ul.find('.del_button_dis').hide();
                       if(isCreate){
                         var resultData = data["data"];
                   		 log_ul.find('.del_button').attr('delId',resultData.id);
                   		 log_ul.find('.save_button').attr('saveId',resultData.id);
                  	 	}else{
                  	 		window.location.href='getDaily';
                  	 	} 
	               	}else{
	               		smoke.alert(data.result, {ok: "确定"});
	               	}
                });
            });
        
            //日期控件调用
            $('body').off('click' ,'.laydate-icon').on('click' ,'.laydate-icon',function(elem,i){
            	  var _thisLaydate = $(this);
                  var log_ul = _thisLaydate.parents('.log_ul');
                  var dailyType = log_ul.find('select[name="project"] option:selected').attr('project_type');//日报类型
                  var dailyId = log_ul.find(".save_button").attr('saveId');
                  var sysNowDate = sysNowTime.split(" ")[0];
                  var nowDate = sysNowDate==laydate.now().split(" ")[0]?sysNowDate:laydate.now().split(" ")[0];
                  var today = new Date(nowDate.replace(/-/g,"/"));
                  var todayMs = today.getTime();
                  var minDay,maxDay;
                  if(dailyId){//修改
                	  var dailyStartTime = log_ul.find('.laydate-icon').val();
                	  var dailyStartDate = dailyStartTime.split(" ")[0];
                	  var dailyStartDateMS = new Date(dailyStartDate.replace(/-/g,"/")).getTime();
                	  if(dailyStartDateMS<todayMs){
                		  minDay = maxDay = new Date(dailyStartDateMS).Format("yyyy/MM/dd hh");
                	  }else if(dailyStartDateMS==todayMs){
                		  minDay = new Date(todayMs).Format("yyyy/MM/dd hh");
                		  maxDay = new Date(sysNowTime).Format("yyyy/MM/dd hh");
                	  }
                  }else{//添加
                	  var minDayMs = todayMs- 3 *24*60*60*1000;
                	  if(today.getDay()>0 && today.getDay()<4){//如果非周四、周五则再往前推2天时间
                		  minDayMs = minDayMs-2 *24*60*60*1000;
                	  }
                	  minDay = dailyType!="3"?new Date(minDayMs).Format("yyyy/MM/dd hh:mm"):"";
                	  maxDay = dailyType!="3"?new Date(sysNowTime).Format("yyyy/MM/dd hh"):"";
                  }
            	  var start = {
                          elem: '', //不传的时候只针对当前点中的元素
                          format: 'YYYY-MM-DD hh:mm',
                          min: minDay, //设定最小日期为当前日期
                          max: maxDay, //最大日期
                          istime: true,
                          istoday: true,
                          festival: true, //是否显示节日
                          isclear: false, //是否显示清空
                          choose: function(datas){ //选择日期完毕的回调
	                        	if(dailyType!="3"){
	                        		var selectedTime = new Date(datas.replace(/-/g,"/")).getTime();
	                        		var nowTime = new Date().getTime();
	                        		if(selectedTime > nowTime){
	                        			smoke.alert('选择的时间不能超过当前系统时间', {ok: "确定"});
	                        			return;
	                        		}
	                        	}
                              //计算结束日期
                              _this.endTimeVal(log_ul.find('select[name="project"]'));
                          }
                  };
            	  laydate(start);
            });
         },delDaily:function(object){
            var _this=this;
            var dailyId = object.attr('delId');
            var index=object.parents('.log_ul').attr('index');
            if(!dailyId){ //删除新增的不需要ajax请求
                _this.delDailyDom(object,index);
                return false;
            }
            var ajax_option = {
                    type: "POST",
                    url: contextPath + '/daily/deleteDaily/' + object.attr('delId'),
                    dataType: "json",
                };
            $.ajax(ajax_option).done(function(data) {
                if(data == 1){
                    _this.delDailyDom(object,index);
                }else{
                    smoke.alert("未成功删除", {ok: "确定"});
                }  
             })
        },delDailyDom:function(object,index){ //删除数据的操作;
            var _this=this;
            //删除数据和DOM节点
            _this.resultData.splice(index,1);
            object.parents('.log_ul').remove();
            //是否有保存按钮存在
            var save_len=$('.save_button:visible').length;
            if(!save_len){
                _this.onOff=true;
            }
            //如果不存在数据新增一条新数据
            if(!_this.resultData.length){
                //创建一条数据
                var data={"id":"","type":1,"startTime":"","endTime":"","workContent":"","hours":0.5,"proId":0,"createUser":null,"createTime":null};
                _this.onOff=false;
                _this.resultData.push(data);
            }else{
                var save_button_len=$('.save_button:visible').length;
                if(save_button_len>0){
                    _this.onOff=false;
                }else{
                    _this.onOff=true; 
                }
            }
             _this.renderAjax(_this.resultData,false,0);
        },onOffFn:function(name){
            var _this=this;
             if(!_this.onOff){
            	smoke.alert('您有未保存的数据，不能'+name, {ok: "确定"});
                return false;
            }else{
                return true;
            }
         },endTimeVal:function(thisObj){
        	var log_ul = thisObj.parents('.log_ul');
        	var dailyStartTime = log_ul.find('.laydate-icon').val();
            if(!dailyStartTime){
            	smoke.alert('日报开始日期不能为空', {ok: "确定"});
                return false;
            }
            var dailyHours = Number(log_ul.find('.spinner input').val());
            var dailyEndTime = getDailyEndTime(dailyStartTime,dailyHours);
            if(dailyEndTime.replace(/-/g,"/").split(" ")[0]!=dailyStartTime.replace(/-/g,"/").split(" ")[0]){ //不是同一天
            	smoke.alert('日报开日期和结束日期已跨天，请修改项目日期', {ok: "确定"});
                return false;
            }
            var dailyEndTimeObj = log_ul.find('.form-control');
            dailyEndTimeObj.val(dailyEndTime.split(" ")[1]);
            dailyEndTimeObj.attr('endtime',dailyEndTime);
         },
         showDailyHours:function(hoursDivObjs){
             var _this = this;
             hoursDivObjs.each(function(){
                var hoursDivObj = $(this);
                var minusObj = hoursDivObj.find('a[data-spin="down"]');
                var plusObj = hoursDivObj.find('a[data-spin="up"]');
                var hoursInput = hoursDivObj.find('input');
                var hoursVal = Number(hoursInput.val());
                var maxHours = Number(hoursInput.attr('data-max'));
                var stepHours = Number(hoursInput.attr('data-step'));
                var projectElem = hoursDivObj.parents('.log_ul').find('select[name="project"]');
                hoursDivObj.onoff=true; //开关
                plusObj.off('click').on('click',function(){
                    if(hoursDivObj.onoff){
                    	hoursVal += stepHours;
                    }
                    if(hoursVal <= maxHours){
                        $(this).parent().find('input').val(hoursVal.toFixed(2));
                    }else{
                    	hoursVal = maxHours;
                    }
                    showDailyHours_isSameDay();
                    _this.endTimeVal(projectElem);
                })
                minusObj.off('click').on('click',function(){
                	hoursVal -= stepHours;
                    if(hoursVal >= stepHours){
                        $(this).parent().find('input').val(hoursVal.toFixed(2));
                    }else{
                    	hoursVal = stepHours;
                    }
                    showDailyHours_isSameDay();
                    _this.endTimeVal(projectElem);
                })
                
                //是否同一天的加减限制
                function showDailyHours_isSameDay(){ //obj.spinner onoff:开关
                	var logUl = hoursDivObj.parents('.log_ul');
                    var dailyStartTime = logUl.find('.laydate-icon').val();
                    var dailyHours = Number(logUl.find('.spinner input').val());
                    var dailyEndTime = getDailyEndTime(dailyStartTime,dailyHours);
                    if(dailyEndTime.replace(/-/g,'/').split(" ")[0]!=dailyStartTime.replace(/-/g,'/').split(" ")[0]){ //不是同一天
                    	hoursDivObj.onoff=false;
                    }else{
                    	hoursDivObj.onoff=true;
                    }
                }
             })
         }
     };
     var commonBar  = new GomeProject_CommonBar({
         'laypage': '1',
         'work_hour_table': '#calendar',
         'work_content_table': '#work_hour_content',
         'addButton': '#add_log'
     });
     commonBar.init();

     
function getMaxObjByType(reslutObjs,sortType){
	var dailyObjDatas = clone(reslutObjs);
	var tempObj;
	var sortRule;
	var sortNextRule;
	if(dailyObjDatas!=null && dailyObjDatas.length>0){
		for(var i=0;i<dailyObjDatas.length-1;i++){
			for(var j=0;j<dailyObjDatas.length-1-i;j++){
				if(sortType=="create"){
					sortRule = dailyObjDatas[j].createTime;
					sortNextRule = dailyObjDatas[j+1].createTime;
					if(new Date(sortRule).getTime() < new Date(sortNextRule).getTime() ){
						tempObj = dailyObjDatas[j];
						dailyObjDatas[j] = dailyObjDatas[j+1];
						dailyObjDatas[j+1] = tempObj;
					}
				}else if(sortType=="start"){
					sortRule = dailyObjDatas[j].startTime;
					sortNextRule = dailyObjDatas[j+1].startTime;
					if(new Date(sortRule).getTime() > new Date(sortNextRule).getTime() ){
						tempObj = dailyObjDatas[j];
						dailyObjDatas[j] = dailyObjDatas[j+1];
						dailyObjDatas[j+1] = tempObj;
					}
				}
			}
		}
		var resultObj = dailyObjDatas[0];
		return resultObj;
	}
}

//克隆对象,改变对象传引用,改为传值
function clone(obj) {
    if (null == obj || "object" != typeof obj) return obj;
    if (obj instanceof Date) {
        var copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }
    if (obj instanceof Array) {
        var copy = [];
        for (var i = 0;i < obj.length; ++i) {
            copy[i] = clone(obj[i]);
        }
        return copy;
    }
    if (obj instanceof Object) {
        var copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
        }
        return copy;
    }
    throw new Error("Unable to copy obj! Its type isn't supported.");
}

/**
 * 对数组对象进行分组
 * @param aryObjs
 * @returns
 */
function groupDailyDatasByStartType(aryObjs,startType){
	var dailyObjs = clone(aryObjs);
	var map = {};
	var dest = [];
	for(var i = 0; i < dailyObjs.length; i++){
	    var daily = dailyObjs[i];
	    var mapKey = daily.startTime.split(" ")[0];
	    if(!map[mapKey]){
	        dest.push({
	            key: mapKey,
	            data: [daily]
	        });
	        map[mapKey] = daily;
	    }else{
	        for(var j = 0; j < dest.length;j++){
	            var tempDaily = dest[j];
	            if(tempDaily.key == daily.startTime.split(" ")[0]){
	            	tempDaily.data.push(daily);
	                break;
	            }
	        }
	    }
	}
	var resultObjs;
	if(startType=="nearly"){
		//获取最近的一组日报开始时间的对象
		resultObjs = dest[0].data;
	}else if(startType=="all"){
		resultObjs = dest;
	}else{
    	for(var i=0;i<dest.length;i++){
    		if(startType==dest[i].key){
    			resultObjs = dest[i].data;
    			break;
    		}
    	}
	}
	return resultObjs;
}

/** 根据开始时间和工时算结束时间 */
function getDailyEndTime(dailyStartTime,dailyHours) {
	dailyHours = dailyHours||0.5;
	var dailyStartTimeMs = new Date(dailyStartTime.replace(/-/g,"/")).getTime();
	var dailyEndTimeMs = dailyStartTimeMs + dailyHours*60*60*1000;
	var startDate = new Date(dailyStartTimeMs);
	var dayNoonTimeMs = new Date(startDate.getFullYear() + '/' + (startDate.getMonth() + 1) + '/' + startDate.getDate()+ ' 12:30:00').getTime();//午休时间
	if(dailyStartTimeMs <= dayNoonTimeMs && dayNoonTimeMs < dailyEndTimeMs){
		dailyEndTimeMs += 60*60*1000;
	}
	var endDate = new Date(dailyEndTimeMs);
	var Y = endDate.getFullYear() + '/';
	var M = addZero(endDate.getMonth()+1)+ '/';
	var D = addZero(endDate.getDate()) + ' ';
	var h = addZero(endDate.getHours()) + ':';
	var m = addZero(endDate.getMinutes()) ;
	var dailyEndTime = Y+M+D+h+m;
	return dailyEndTime;
}
   
function addZero(num){
    return num<10?'0'+num:num;
}