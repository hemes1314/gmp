$(document).ready(function() {
// add for crumb start
	initCrumb($('#saveProjectCrumbUl'));
	// add for crumb end
    function GomeProject_CommonBar(config) {
        if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
        this.page = config.laypage || "";
        this.windowButon = config.lay_window || '';
        this.autocomplete_input = config.autoInput || '';
        this.addEmp = config.addEmployee || " ";
        this.project_table = config.tip_dialog || '';
        this.changeLogs = [];
        this.weekDaysPre = 0;
        this.endDate = null;
    }

    GomeProject_CommonBar.prototype = {
        constructor: GomeProject_CommonBar,
        init: function() {
        	
        	this.changeLeftMenu();
            this.bind_complete();
            this.addfun();
            this.listen();
            this.table_control();
            this.getParm();
            this.listen_pre_task();
            this.files_list();
            this.createSelect();
            this.autolisten();
            this.listen_time();
            this.work_Calculation();
            this.form_submit();
            this.file_delete();
            this.delete_li('.delete_li');
            this.importTasks();
           
        },
        changeLeftMenu: function() {
        	var demandId = $("#demandId").val();
        	if(demandId != null && demandId != "") {
        		$(window.parent.frames["iframe_leftbar"].document).find("a").eq(0).addClass("active");
        		$(window.parent.frames["iframe_leftbar"].document).find("a").eq(3).removeClass("active");
        	}
        },
        listen: function() {
            $.event.special.valuechange = {
                teardown: function(namespaces) {
                    $(this).unbind('.valuechange');
                },
                handler: function(e) {
                    $.event.special.valuechange.triggerChanged($(this));
                },

                add: function(obj) {
                    $(this).on('keyup.valuechange cut.valuechange paste.valuechange input.valuechange', obj.selector, $.event.special.valuechange.handler)
                },

                triggerChanged: function(element) {
                    var current = element[0].contentEditable === 'true' ? element.html() : element.val(),
                        previous = typeof element.data('previous') === 'undefined' ? element[0].defaultValue : element.data('previous')
                    if (current !== previous) {
                        element.trigger('valuechange', [element.data('previous')])
                        element.data('previous', current)
                    }
                }
            }
        },
        createSelect: function() {
            var _this = this;
            var option = {
                zIndex: 1000,
                width: 192,
                height: 30,
                reg: true
            }
            if ($('input[name="systemNameArr"]').length) {
                var default_value = $('input[name="systemNameArr"]').val();
                option.defaultValues = default_value;
            }
            $('#system').selectlist_default(option);
            _this.bind_complete(_this.autocomplete_input);
        },
        bind_window_complete: function(name) {
            var _this = this;
            $('body').on('keydown', name, function() {
                _this.bind_complete(name);
            });
        },
        bind_complete: function(name) {
            var _this = this;
            $(name).autocomplete({
                source: function(request, response) {
                    $.ajax({
                        type: 'POST',
                        url: contextPath + '/findUser',
                        dataType: "json",
                        data: JSON.stringify({ userName: request.term }),
                        contentType: "application/json"
                    }).done(function(data) {
                        response($.map(data, function(item) {
                            return { label: item.userId + '(' + item.userName + ',' + item.email + ')', value: item.userId + '(' + item.userName + ')', trans_value: item.id }
                        }));
                    });
                },
                minLength: 1,
                select: function(event, ui) {
                    if ($(this).closest('div').find('input[name="bpId"]').length) {
                        $(this).closest('div').find('input[name="bpId"]').eq(0).val(ui.item.trans_value)
                    };
                    if ($(this).closest('div').find('input[name="keyUserId"]').length) {
                        $(this).closest('div').find('input[name="keyUserId"]').eq(0).val(ui.item.trans_value)
                    }
                    if ($(this).closest('td').find('input[name$=".userId"]').length) {
                        $(this).closest('td').find('input[name$=".userId"]').val(ui.item.trans_value)
                    }
                    if ($(this).closest('div').find('input[name="dialog_hidden"]').length) {
                        $(this).closest('div').find('input[name="dialog_hidden"]').val(ui.item.trans_value)
                    }
                    $(this).attr('title',ui.item.label);
                    $('form').attr('data', ui.item.trans_value);
                },
                _renderMenu: function(ul, items) {
                    var that = this;
                    $.each(items, function(index, item) {
                        that._renderItemData(ul, item);
                    });
                    $(ul).find("li:odd").addClass("odd");
                }
            })
        },
        delete_li: function(name) {
            $('body').on('click', name, function() {
                var index = $(this).closest('li').index();
                $('input[name="relatedUsers[' + index + '].userId"]').remove();
                $('input[name="relatedUsers[' + index + '].roleId"]').remove();
                $(this).closest('li').remove();
            })
        },
        importTasks: function() {
        	$('#importTasksBtn').on('click', function() {
        		$("#file_upload").ajaxSubmit({
                    dataType: 'json', //数据格式为json 
                    beforeSend: function() { //开始上传 
                        //进度条相关
                    },
                    uploadProgress: function(event, position, total, percentComplete) {

                    },
                    success: function(data) { //成功 
                    	alert("导入测试...");
                    },
                    error: function(xhr) { //上传失败 

                    }
                });
            });
        },
        /*表格工期控制*/
        /*增添角色*/
        addfun: function() {
            var _this = this;
            $(_this.addEmp).click(function() {
                _this.bind_window_complete('#employee_');
                var d = dialog({
                    title: '提示',
                    zIndex: 5,
                    content: '<div class="dialog_name"><p class=>角色名称:<input type="text" id="emp_name"></p><p>人&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp员:<input type="text" id="employee_" class="autocomplete_input" ></p><input type="hidden" id="dialog_hidden" name="dialog_hidden" value></div>',
                    okValue: '确定',
                    ok: function() {
                        var name = $('#emp_name').val();
                        var emp = $('#employee_').val();
                        var hid = $("#dialog_hidden").val();
                        if (name == '' || emp == '' || hid == '') {
                        	smoke.alert('请输入有效的人员姓名。')
                            return false;
                        };
                        var userId = $('input[name="dialog_hidden"]').val();
                        var param = {};
                        param.roleName = name;
                        var roleId;
                        $.ajax({
                            type: 'POST',
                            url: contextPath + '/role/save',
                            dataType: "json",
                            data: JSON.stringify(param),
                            contentType: "application/json",
                            async: false
                        }).done(function(data) {
                            roleId = data;
                        });
                        if (!userId) return;
                        var userIds = $('.userIds');
                        var dataMax = 0;
                        for(var i = 0; i < userIds.length; i++){
                        	var data = new Number($(userIds[i]).attr("data"));
                        	if(data > dataMax) {
                        		dataMax = data;
                        	}
                        }
                        dataMax++;
                        var str = '<li><span class="search"><a href="javascript:void(0)" roleId=' + roleId + '>' + name + '</a><input type="text" name="relatedUsers[' + dataMax + '].userName" class="autocomplete_input" value=' + emp + '><a href="javascript:void(0)" class="auto_search"></a></span><span class="delete_li">删除</span></li>';
                        var role_hidden = '<input name="relatedUsers[' + dataMax + '].roleName" type="hidden" value="' + param.roleName + '"/><input name="relatedUsers[' + dataMax + '].roleId" type="hidden" value="' + roleId + '"/>';
                        var user_hidden = '<input name="relatedUsers[' + dataMax + '].userId" class="userIds" data="'+dataMax+'" type="hidden" value="' + userId + '"/>';
                        $('.form-group').eq(0).append(role_hidden);
                        $('.form-group').eq(0).append(user_hidden);
                        $('#employee').append(str);
                        _this.delete_li('.delete_li')
                    },
                    cancelValue: '取消',
                    cancel: function() {}
                }).width(400);
                d.show();
            })
        },
        /*增添角色隐藏域*/
        autolisten: function() {
            var _this = this;
            $('#employee').off('blur').on('blur', '.autocomplete_input', function() {
                var index = $(this).closest('li').index();
                var userId = $('form').attr('data');
                if ($('input[name="relatedUsers[' + index + '].userId"]').length) {
                	if(userId != undefined) {
                		$('input[name="relatedUsers[' + index + '].userId"]').val(userId);
                	}
                } else {
                    var user_hidden = '<input name="relatedUsers[' + index + '].userId" type="hidden" value="' + userId + '"/>';
                    $('.form-group').eq(0).append(user_hidden);
                }
            })
        },
        /*监听前置任务号*/
        listen_pre_task: function() {
            var _this = this;
            $('#project_table').off('blur').on('blur', '.pre_task', function() {
                var $that = $(this);
                var preN = $(this).val();
                var parentNum = $(this).closest('tr').index() + 1;
                var $start = $(this).closest('tr').find('.table_start');
                if ('' == preN){
                	if($start.hasClass("laydate-disabled")){
                		$start.removeClass("laydate-disabled").val('');
                	}
                	$(this).removeClass("input_validation-failed");
                	return false;
                }else{
                	// check
                	 var m = preN.match(/^[1-9]\d*$/);
                     if(!m){
                    	 $(this).addClass("input_validation-failed");
                     }else{
                    	 $(this).removeClass("input_validation-failed");
                     }
                }
                
                if (preN == parentNum) {
                    setTimeout(function() {
                        $that.focus().val('');
                        $start.addClass("laydate-disabled").val('');
                        $that.attr('id', 'tip_dialog' + preN);
                        var d = dialog({
                            content: '不能设置自身任务为前置任务',
                            quickClose: true
                        });
                        d.show(document.getElementById('tip_dialog' + preN));
                    }, 0);
                } else {
                	var preEndDate = $('#project_table tbody').find('tr .table_end').eq(preN - 1).val();
                	if(typeof(preEndDate) != 'undefined' && '' != preEndDate){
                		var nextWorkDate = _this.getNextWorkDay(preEndDate);
                		// 若当前任务存在前置任务，则其开始时间默认为其前置任务结束时间的次日（次工作日）；且不可修改；
                		$(this).closest('tr').find('.table_start').val(nextWorkDate).addClass("laydate-disabled");
                		
                		// 设置前置任务后,结束时间往后递推,排除周末
                		var workPeriod = new Number($(this).closest('tr').find('.workPeriod').val());
                		if(workPeriod != 0) {
                		    var s1 = _this.getDate(nextWorkDate);
                		    var e1 = new Date(s1.getFullYear(),s1.getMonth(),s1.getDate() + (workPeriod-1));
                		    _this.addWorkPeriod(s1, e1, workPeriod, this);
                		}
                	}else{
                		$(this).closest('tr').find('.table_start').val('').removeClass("laydate-disabled");
                	}
                }
            	// 重新计算工时
                setTimeout(function(){
                	var $castDom = $that.closest('tr').find('.workPeriod');
                	var startV = $that.closest('tr').find('.table_start').val();
                	var endV = $that.closest('tr').find('.table_end').val();
                	if((startV != '' && endV != '') && startV <= endV){
                		var cost = _this.getWorkDay('cn', startV, endV);
                    	cost = cost>=0?cost:'';
                    	$castDom.val(cost);
                	}else{
                		$castDom.val('');
                	}
                },100);

            });
        },
        addWorkPeriod:function(s1, e1, w1, obj){
        	var _this = this;
        	var w2 = _this.getWorkDay('cn', _this.toYYMMDD(s1), _this.toYYMMDD(e1));
        	// 计算周末天数
    		var weekDays = _this.getWeekDay('cn', s1, e1);
    		var weekDays2 = weekDays - _this.weekDaysPre;
    		var endDate = new Date(e1.getFullYear(),e1.getMonth(),e1.getDate() + weekDays2);
    		w2 = _this.getWorkDay('cn', _this.toYYMMDD(s1), _this.toYYMMDD(endDate));
    		if(w2 < w1) {
    			_this.weekDaysPre = weekDays;
    			_this.addWorkPeriod(s1,endDate,w1,obj);
    		} else {
    			_this.endDate = endDate;
    			$(obj).closest('tr').find('.table_end').val(_this.toYYMMDD(endDate));
    		}
        },
        /*计算完成百分比*/
        calPer:function(){
        	//A项目包括三项任务为b、c、d；b进度为x%工期为t1天；c进度为y%工期为t2天；d进度为z%工期为t3天；则A的项目进度为=t1/(t1+t2+t3)*x%+t2/(t1+t2+t3)*y%+t3/(t1+t2+t3)*z% ；
            //计算进度
            var num = 0;
            function get_total() {
                var total = 0;
                $(".workPeriod").each(function() {
                    if ($(this).val() != '') {
                        total += Number($(this).val());
                    } else if ($(this).val() == '') {
                    	// 若新增加项，未填写工期时，默认工期为1天
                    	total++;
                    }
                });
                return total;
            };
            //保留两位小数   
            //功能：将浮点数四舍五入，取小数点后2位  
            function toDecimal(x) {  
                var f = parseFloat(x);  
                if (isNaN(f)) {  
                    return;  
                }  
                f = Math.round(x*10)/10;  
                return f;  
            }  
      
            var total = get_total();
            $('#project_table tbody').find('tr').each(function() {
                var innerDay = $(this).find(".workPeriod").val() == '' ? 0 : Number($(this).find(".workPeriod").val());
                var innerPer = $(this).find(".schedule").val() == '' ? 0 : Number($(this).find(".schedule").val());
                
                var per = innerDay / total;
                var result = per * innerPer;
                num += result;
            });
            if(isNaN(num)){
//            	$('#per').val(toDecimal(num) + '%');
            	num = 0;
            }
            
            return toDecimal(num);
        },
        /*监听focus变化*/
        listen_time: function(index) {
            var _this = this;
            
            $('.table_start,.table_end,.schedule,.planTime').off('blur').on('blur',function(e){
            	var _that = this;
            	if($(this).hasClass('planTime')){
            		if($(this).val() == ''){
            			$(this).removeClass("input_validation-failed");
            			return false;
            		}
            	}
               	var $startDom = null;
            	var $endDom = null;
            	var adjustFlg = true;
            	if($(this).hasClass('table_start')){
            		$startDom = $(this);
            		$endDom = $(this).closest('tr').find('.table_end');
            	}else if($(this).hasClass('table_end')){
            		$startDom = $(this).closest('tr').find('.table_start');
            		$endDom = $(this);
            	}else{
            		adjustFlg = false;
            	}
            	var $castDom = $(this).closest('tr').find('.workPeriod');
            	var $tr = $(this).closest('tr');
            	setTimeout(function(){
            		var id = $tr.find('.task_number').text();
            		if($(_that).hasClass('table_end')){
            			var endDate = $endDom.val();
            			$('.pre_task').each(function(i){
                			if(parseInt(id) == parseInt($(this).val())){// 后续项目
                				// set value
                				var $aftTr = $(this).closest('tr');
                				var $aftStartDom = $aftTr.find('.table_start');
                				var $aftEndDom = $aftTr.find('.table_end');
                				var $aftWorkPeriod = $aftTr.find('.workPeriod');
                				
                				if('' == endDate){
                					$aftStartDom.removeClass('laydate-disabled');
                					$aftWorkPeriod.val('');
                				}else{
                					var nextWorkDate = _this.getNextWorkDay(endDate);
                					$aftStartDom.val(nextWorkDate);
                					$aftStartDom.addClass('laydate-disabled');
                				}
                				
                            	if(endDate == '' || $aftEndDom.val() == ''){
                            		$aftStartDom.removeClass("input_validation-failed");
                            		$aftEndDom.removeClass("input_validation-failed");
                            		$aftWorkPeriod.val('');
                            	}else if(endDate <= $aftEndDom.val()){
                            		$aftStartDom.removeClass("input_validation-failed");
                            		$aftEndDom.removeClass("input_validation-failed");
                            		
                                	var cost = _this.getWorkDay('cn', endDate, $aftEndDom.val());
                                	cost = cost>=0?cost:'';
                                	$aftWorkPeriod.val(cost);
                            		
                            	}else{
                            		$aftEndDom.addClass("input_validation-failed");
                            		$aftWorkPeriod.val('');
                            	}
                				
                			}
                		});
            		}
            		
            		if(adjustFlg){
            			var startV = $startDom.val();
                    	var endV = $endDom.val();
                    	
                    	// 清空error状态
                    	if(startV == '' || endV == ''){
                    		$startDom.removeClass("input_validation-failed");
                    		$endDom.removeClass("input_validation-failed");
                    		$castDom.val('');
                    	}else if(startV <= endV){
                    		$startDom.removeClass("input_validation-failed");
                    		$endDom.removeClass("input_validation-failed");
                    		
                        	var cost = _this.getWorkDay('cn', startV, endV);
                        	cost = cost>=0?cost:'';
                        	$castDom.val(cost);
                    		
                    	}else{
                    		$castDom.val('');
                    	}
            		}
            		$('#per').val(_this.calPer() + '%');
            	},300);
 
            });
            
            $('.table_start').each(function() {
                var $that = $(this);
                var index = $that.closest('tr').index();
                var $that_end = $(this).closest('tr').find('.table_end');
//                if (!$(this).attr('id')){
                var start_id = $that.attr('name');
                var end_id = $that_end.attr('name');
                $that.attr('id', start_id);
                $that_end.attr('id', end_id);
                
                var start = {
                    elem: '#' + start_id,
                    format: 'YYYY-MM-DD',
//                    min: laydate.now(),
                    max: '2099-06-16 23:59:59',
                    istime: false,
                    istoday: false,
                    choose: function(datas) {
                        end.min = datas; //开始日选好后，重置结束日的最小日期
                        end.start = datas //将结束日的初始值设定为开始日
                        
                        var start_time = datas;
                        var end_time = $that_end.val();
                        
                        if(end_time != '' && start_time > end_time  ){
                        	$that.addClass("input_validation-failed");
                            setTimeout(function() {
                                var d = dialog({
                                    content: '开始时间 不能晚于结束时间',
                                    quickClose: true
                                });
                                d.show(document.getElementById(start_id));
                            }, 0);
                        }else{
                        	$that.removeClass("input_validation-failed");
                        	$that_end.removeClass("input_validation-failed");
                        }
                        
                        if (end_time != '' &&  start_time != '' && start_time <= end_time) {
                        	var cost = _this.getWorkDay('cn', start_time, end_time);
                        	cost = cost>=0?cost:'';
                        	$that.closest('tr').find('.workPeriod').val(cost);
                        }else{
                        	$that.closest('tr').find('.workPeriod').val('');
                        }
                        
                        return;
                    }
                };
                var end = {
                    elem: '#' + end_id,
                    format: 'YYYY-MM-DD',
//                    min: laydate.now(),
                    max: '2099-06-16 23:59:59',
                    istime: false,
                    istoday: false,
                    choose: function(datas) {
//                            start.max = datas;
                        var start_time = $that.val();
                        var end_time = datas;
                        
                        if(start_time != '' && start_time > end_time  ){
                        	$that_end.addClass("input_validation-failed");
                            setTimeout(function() {
                                var d = dialog({
                                    content: '结束时间 不能早于开始时间',
                                    quickClose: true
                                });
                                d.show(document.getElementById(end_id));
                            }, 0);
                        }else{
                        	$that.removeClass("input_validation-failed");
                        	$that_end.removeClass("input_validation-failed");
                        }
                        
                        if (end_time != '' &&  start_time != '' && start_time <= end_time) {
                        	var cost = _this.getWorkDay('cn', start_time, end_time);
                        	cost = cost>=0?cost:'';
                        	$that.closest('tr').find('.workPeriod').val(cost);
                        }else{
                        	$that.closest('tr').find('.workPeriod').val('');
                        }
                    }
                };
                laydate(start);
                laydate(end);
//                }
            });
            
        },
        /*计算表头工时*/
        work_Calculation: function() {
            var _this = this;
            var start = {
                elem: '#start',
                format: 'YYYY-MM-DD',
//                min: laydate.now(),
                max: '2099-06-16 23:59:59',
                istime: false,
                istoday: false,
                choose: function(datas) {
                    end.min = datas;
                    end.start = datas;
                    last.min = datas;
                    last.start = datas;
                    $('#start').removeClass("input_validation-failed");
                    
                    if(datas == '') {
                    	$('#work_days').val('');
                    	return;
                    }
                    var end_time;
                    if ($('#end').val() != '') {
                        end_time = $('#end').val();
                    } else if($('#plan_date').val() != '') {
                        end_time = $('#plan_date').val();
                    } else {                    	
                    	$('#work_days').val('');
                    	return;
                    }
                    $('#work_days').val(_this.getWorkDay('cn', datas, end_time));
                }
            };
            var end = {
                elem: '#plan_date',
                format: 'YYYY-MM-DD',
//                min: laydate.now(),
                max: '2099-06-16',
                istime: false,
                istoday: false,
                choose: function(datas) {
                    start.max = datas;
                    $('#plan_date').removeClass("input_validation-failed");
                    var start_time = $('#start').val();
                    if(start_time == '') {
                    	$('#work_days').val('');
                    	return;
                    }
                    
                    var end_time;
                    if (datas != '') {
                        end_time = datas;
                    } else if($('#plan_date').val() != '') {
                        end_time = $('#plan_date').val();
                    } else {
                    	$('#work_days').val('');
                    	return;
                    }
                    $('#work_days').val(_this.getWorkDay('cn', start_time, end_time));
                }
            };
            var last = {
                elem: '#end',
                format: 'YYYY-MM-DD ',
//                min: laydate.now(),
                max: '2099-06-16',
                istime: false,
                istoday: false,
                choose: function(datas) {
                    start.max = datas;
                    var start_time = $('#start').val();
                    var end_time = $('#end').val();
                    if (start_time == '') {
                    	$('#work_days').val('');
                    	return;
                    };
                    
                    var end_time;
                    if ($('#end').val() != '') {
                        end_time = $('#end').val();
                    } else if(datas != '') {
                        end_time = datas;
                    } else {
                    	$('#work_days').val('');
                    	return;
                    }
                    $('#work_days').val(_this.getWorkDay('cn', start_time, end_time));
                }
            };
            laydate(start);
            laydate(end);
            laydate(last);
            
            $('#start,#plan_date,#end').off('blur').on('blur',function(e){
            	
            	setTimeout(function(){
            		var start_time = $('#start').val();
            		if (start_time == '') {
                    	$('#work_days').val('');
                    	return;
                    };
            		var end_time;
                    if ($('#end').val() != '') {
                        end_time = $('#end').val();
                    } else if($('#plan_date').val() != '') {
                        end_time = $('#plan_date').val();
                    } else {
                    	$('#work_days').val('');
                    	return;
                    }
                    $('#work_days').val(_this.getWorkDay('cn', start_time, end_time));
            	},200);
            });
        },
        /*表格增删浮层*/
        table_control: function() {
            var _this = this,
                index, lastindex;
            $(_this.project_table).on('click', '.task_number', function() {
                var id;
                if ($(this).attr('id')) {
                    id = $(this).attr('id')
                } else {
                    $(this).attr('id', ('task_number' + $(this).closest('tr').index()));
                    id = 'task_number' + $(this).closest('tr').index()
                }
                index = $(this).closest('tr').index();
                var str = "";
                if(1 == $(_this.project_table).find('.task_number').length){
                	str = "<ol class='tableOp'><li id='add_table'>添加</li></ol>";
                }
                else if(0 == index){
                	str = "<ol class='tableOp'><li id='add_table'>添加</li><li id='del_table'>删除</li><li id='move_down'>下移</li></ol>";
                }else if(index == $(_this.project_table).find('.task_number').length - 1){
                	str = "<ol class='tableOp'><li id='add_table'>添加</li><li id='del_table'>删除</li><li id='move_up'>上移</li></ol>";
                }else{
                	str = "<ol class='tableOp'><li id='add_table'>添加</li><li id='del_table'>删除</li><li id='move_up'>上移</li><li id='move_down'>下移</li></ol>";
                }
                
                
                var d = dialog({
                    content: str,
                    align: 'right top',
                    quickClose: true
                });
                d.show(document.getElementById(id));
                _this.table_move(index + 1,d);
                
            })
        },
        file_delete: function() {
        	$('.file_delete').click(function() {
        		$(this).parent().remove();
            })
        },
        /*表格增删*/
        table_move: function(index, d) {
            var _this = this;
            function exChange(start, end) {
                // 上移下移变更记录 begin wubin
                var tr = $(_this.project_table).find("tr").eq(start);
//            	var taskId = tr.find("input[type='hidden']").eq(0).val();
            	var taskNum = tr.find("input[type='hidden']").eq(1).val();
            	var taskName = tr.find("input[type='text']").eq(0).val();
            	var changeLog = {};
            	changeLog.taskNum = taskNum;
            	changeLog.taskName = taskName;
                if(end > start) {
                	changeLog.move = "down";
                } 
                else {
                	changeLog.move = "up";
                }
                _this.changeLogs.push(changeLog);
                // 上移下移变更记录 end wubin
                
            	$('.pre_task').each(function(i){
            		var txt = $(this).val();
            		if(txt != '' && parseInt(txt) == parseInt(start)){
            			$(this).val(end);
            		}
            		if(txt != '' && parseInt(txt) == parseInt(end)){
            			$(this).val(start);
            		}
            	});
            	
                var temphtml = $(_this.project_table).find('tr').eq(start).clone(true);
                $(_this.project_table).find('tr').eq(start).find('td').each(function(i){
                	$(this).find('input').each(function(j){
                		if($(this).attr('type') == 'text' || $(this).attr('type') == 'hidden'){
                			var $oriDom = $(_this.project_table).find('tr').eq(end).find('td').eq(i).find('input').eq(j);
                			$(this).val($oriDom.val());
                			if($oriDom.hasClass('laydate-disabled')){
                				$(this).addClass('laydate-disabled');
                			}else{
                				$(this).removeClass('laydate-disabled');
                			}
                		}
                	});
                });
                $(_this.project_table).find('tr').eq(end).find('td').each(function(i){
                	$(this).find('input').each(function(j){
                		if($(this).attr('type') == 'text' || $(this).attr('type') == 'hidden'){
                			var $oriDom = $(temphtml).find('td').eq(i).find('input').eq(j);
                			$(this).val($oriDom.val());
                			if($oriDom.hasClass('laydate-disabled')){
                				$(this).addClass('laydate-disabled');
                			}else{
                				$(this).removeClass('laydate-disabled');
                			}
                		}
                	});
                });
            };

            function changeTaskNum() {
                $(_this.project_table).find('tr').each(function() {
                    var real_index = $(this).index();
                    $(this).find('input[name^="relatedTasks"]').each(function() {
                        var name = $(this).attr('name');
                        var replace_name = name.replace(/\d+/g, real_index);		
                        $(this).attr('name', replace_name);
                    })
                })
            }
            
            $('body').off('click', 'li').on('click', 'li', function(e) {
                /*序列化表头*/
                function sortNum(tableId) {
                    $(tableId).find('tbody').find('tr').each(function() {
                    	var this_index = $(this).index();
                    	// 重新设置任务号
                    	$(this).find('.tashNum').val(this_index + 1).closest('td').find('.tashNum_dis').text(this_index + 1);
                    })
                }
                d.close();
                switch (e.target.id) {
                    case 'add_table':
                    	// adjust
                    	$('.pre_task').each(function(i){
                    		var txt = $(this).val();
                    		if(txt != '' && parseInt(txt) > parseInt(index)){
                    			$(this).val( parseInt(txt) + 1);
                    		}
                    	});
                        var str = $('#tr_tpl').html();
                        $(_this.project_table).find('tr').eq(index).after(str);
                        _this.bind_window_complete('.autocomplete_input');
                        setTimeout(function() {
                            _this.listen_time(index);
                        }.bind(_this), 500)
                        break;
                    case 'del_table':
                    	// adjust
                    	$('.pre_task').each(function(i){
                    		var txt = $(this).val();
                    		if(txt != ''){
                    			if(parseInt(txt) == parseInt(index)){
                    				$(this).val('');
                    				$tr = $(this).closest('tr');
                    				if($tr.find('.table_start').hasClass("laydate-disabled")){
                    					$tr.find('.table_start').val('').removeClass("laydate-disabled");
                    				}
                    			}else if(parseInt(txt) > parseInt(index)){
                    				$(this).val(parseInt(txt)>1?parseInt(txt)-1:'');
                    			}
                    		}
                    	});
                        $(_this.project_table).find('tr').eq(index).remove();
                        break;
                    case 'move_up':
                        exChange(index, index - 1);
                        break;
                    case 'move_down':
                        exChange(index, index + 1);
                        break;
                }
                sortNum(_this.project_table);
                changeTaskNum();
                $('#per').val(_this.calPer() + '%');
            })
        },
        getParm: function() {
            function GetQueryString(name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
                var r = window.location.search.substr(1).match(reg);
                if (r != null) return (r[2]);
                return null;
            }
            if (GetQueryString('name')) {
                $('.demand_source').text('相关需求：' + decodeURI(GetQueryString('name')))
            }
        },
        files_list: function() {
            var arr = [];
            $('#upload').on('change', function() {
            	arr.length = 0;
                var upfiles = $(this)[0].files;
                for (var i = 0, len = upfiles.length; i < len; i++) {
                    arr.push(upfiles[i].name)
                }
                var html = arr.join(',');
                if (arr.length >= 4) {
                    var new_arr = arr.slice(0, 4);
                    html = new_arr.join(',') + '....';
                }
                $('#content').html(html).attr('title', arr.join(','));
            })
        },
        // 字符转日期
        getDate : function(strDate) {    
            var date = eval('new Date(' + strDate.replace(/\d+(?=-[^-]+$)/,    
             function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');    
            return date;    
        },
        toYYMMDD : function(now) {  
            var year = now.getFullYear();
            var month =(now.getMonth() + 1).toString();
            var day = (now.getDate()).toString();
            if (month.length == 1) {
                month = "0" + month;
            }
            if (day.length == 1) {
                day = "0" + day;
            }
            var dateTime = year + "-" + month + "-" + day;
            return dateTime;
        },
        // 取得下一个工作日
        getNextWorkDay: function(dateStr){
        	var _this = this;
        	function getNextDay(d){
                d = new Date(d);
                d = +d + 1000*60*60*24;
                d = new Date(d);
                //return d;
                //格式化
                var year = d.getFullYear();
                var month = d.getMonth()+1;
                month = month < 10?'0'+month:month;
                var date = d.getDate() < 10?'0'+d.getDate():d.getDate();
                return year+"-"+month+"-"+date;
            }
        	var nextDay = getNextDay(dateStr);
        	if(!_this.checkWeekDay(nextDay)){
        		nextDay = _this.getNextWorkDay(nextDay);
        	}
        	return nextDay;
        },
        // 判断是否周末
        checkWeekDay: function(dateStr){
        	var d = this.getDate(dateStr);
        	if(d.getDay() == 6 || d.getDay() == 0){
        		return false;
        	}else{
        		return true;
        	}
        },
        // 取得两天之间的工作日（除周末）
        getWorkDay: function(mode, beginDay, endDay) {
            var dateS = this.getDate(beginDay)
            var dateE = this.getDate(endDay);
            var desc = Math.abs ((dateE - dateS) / (60 * 60 * 24 * 1000)) + 1;
           	return desc - this.getWeekDay(mode,dateS,dateE);

        },
        // 取得两天之间的周末
        getWeekDay: function(mode, beginDay, endDay) {
        	var count = 0;
        	$.ajax({  
        		url: contextPath+"/dict/holidayCount",  
        		data:{startDate:this.toYYMMDD(beginDay),endDate:this.toYYMMDD(endDay)},
        		type: "GET", 
        		async: false,
        		success:function(data){
        			count = data;
        		}
        	});  
        	return count;
        },
        form_submit: function() {
        	var _this = this;
            $("#saveForm").submit(function(evt) {
                evt.preventDefault();
                // 判断文件是否为空，为空则把文件移除
                var upload = $("#upload");
                if (upload.val() == undefined || upload.val() == "") {
                    upload.remove();
                }
                var url = contextPath + '/project';
                if($("#proId").val() != undefined && $("#proId").val() != ""){
                	url = url + "/update";
                }
                $("#saveForm").ajaxSubmit({
                    url: url,
                    dataType: "json",
                    type: "POST",
                    uploadProgress: function(event, position, total, percentComplete) {
                    },
                    success: function(data) {
                        if (data.resultData.flag == "0901000001") {
                            
			    	// 保存时不跳转列表页面
                        	var isCommit = $("#isCommit").val();
                        	if(isCommit != "1") {
                        		smoke.alert('保存成功！');
                        	} else {
                        		location.href = data.targetUrl;
                        	}
                        } else {
                            $(".form_file").append(upload);
                        }
                    },
                    error: function(error) {
                        $(".form_file").append(upload);
                    }
                });
            });
            
            submitForm = function(isCommit){
            	// ajax method verify bugid is valid
            	var bugId = $('input[name="bugId"]').val();
            	var proId = $('#proId').val();
            	$.ajax({
                    type: 'POST',
                    url: contextPath + '/project/val/bugId',
                    dataType: "json",
                    data: JSON.stringify({bugId:bugId,proId:proId}),
                    contentType: "application/json",
                    async: false
                }).done(function(data) {
                	if(data == "0901000001") {
                		layer.msg('Bugzilla ID 已使用，请重新输入。')
                		return false;
                	}
                	$("#isCommit").val(isCommit);
                	$("#changeLogs").val(JSON.stringify(_this.changeLogs));
                    $("#saveForm").trigger('submit');
                });
            }
            
            // save
            $('.proSaveBtn').click(function() {
            	$('#start').removeClass("input_validation-failed");
            	$('#plan_date').removeClass("input_validation-failed");
                $('#task_status').removeClass("input_validation-failed");
                $('#importantance').removeClass("input_validation-failed");
                $('#Schedule_status').removeClass("input_validation-failed");
                $('#Execute').removeClass("input_validation-failed");
            	$('#project_table').find('tr').each(function() {
                    var real_index = $(this).index();
                    $(this).find('input[name^="relatedTasks"]').each(function() {
                    	$(this).removeClass("input_validation-failed");
                    })
                })
            	if($('input[name="title"]').val() == "") {
            		layer.msg('标题不能为空！')
            		return false;
            	}
            	var bugId = $('input[name="bugId"]').val();
            	if(bugId == "") {
            		layer.msg('Bugzilla ID不能为空！')
            		return false;
            	}
            	var reg = new RegExp("^[1-9]\d*$");
            	if(!reg.test(bugId)){  
                    layer.msg('Bugzilla ID必须为正整数，且不能以0开头')
                    return false;
                }
            	if($('input[name="bpId"]').val() == "" || $('input[name="bpName"]').val() == "") {
            		layer.msg('请选择项目经理！')
            		return false;
            	}
            	submitForm("0");
            })
            // submit
            $('.proCommitBtn').click(function() {
            	// 涉及系统无法使用校验插件，手动添加样式
            	if($("input[name='systemIds']").val() == "请选择" || $("input[name='systemIds']").val() == "") {
            		$("#system input[type='button']").eq(0).addClass("input_validation-failed");
            	}else{
            		$("#system input[type='button']").eq(0).removeClass("input_validation-failed");
            	}
            	if($('input[name="title"]').val() == "") {
            		layer.msg('标题不能为空！')
            		return false;
            	}
                var bugId = $('input[name="bugId"]').val();
            	if(bugId == "") {
            		layer.msg("Bugzilla ID不能为空！");
            		return false;
            	}
            	var reg = new RegExp("^[1-9][0-9]*$");
            	if(!reg.test(bugId)){  
                    layer.msg('Bugzilla ID必须为正整数，且不能以0开头')
                    return false;
                }
                if($("input[name='systemIds']").val() == "请选择" || $("input[name='systemIds']").val() == "") {
            		layer.msg("请选择涉及系统！");
            		$("#system input[type='button']").eq(0).addClass("input_validation-failed");
            		return false;
            	}
                $("#system input[type='button']").eq(0).removeClass("input_validation-failed");
                if($('input[name="bpId"]').val() == "" || $('input[name="bpName"]').val() == "") {
            		layer.msg('请选择项目经理！')
            		return false;
            	}
                if($('#unitBsId3').val() == "") {
                	layer.msg("请选择业务部门！");
                	return false;
                }
                if($('#task_status').val() == -1) {
                	layer.msg("请选择任务状态！");
                	return false;
                }
                if($('#importantance').val() == -1) {
                	layer.msg("请选择优先级！");
                	return false;
                }
                if($('#Schedule_status').val() == -1) {
                	layer.msg("请选择进度！");
                	return false;
                }
                if($('#Execute').val() == -1) {
                	layer.msg("请选择实施阶段！");
                	return false;
                }
                if($('input[name="startTime"]').val() == "") {
                	layer.msg("请输入开始时间！");
                	return false;
                }
                if($('input[name="planTime"]').val() == "") {
                	layer.msg("请输入计划上线时间！");
                	return false;
                }
                var preTask = $(".pre_task");
                for(var i = 0; i < preTask.length; i++) {
                	var preNum = preTask.eq(i).val();
                	if(preNum == null || preNum == "") continue;
                	var reg = new RegExp("^[1-9]*$");
                	if(!reg.test(preNum)){  
                        layer.msg("前置任务必须为正整数!"); 
                        preTask.eq(i).addClass("input_validation-failed");
                        return false;
                    }
                	if(!(preNum >=1 && preNum <= preTask.length)) {
                		layer.msg("前置任务号必须存在!");  
                		preTask.eq(i).addClass("input_validation-failed");
                        return false;
                	}
                }
                // 若存在已录入的任务项计划工时为空，提交无效
                if(!valPlanTime()) {
                	return false;
                }
                var isSubmit = $(this).attr('submit');
                if (!!isSubmit) {
                	layer.msg('<div style=\"text-align:left;\">请检查项目排期！<br>进度只可填写0-100的整数<br>工时只能输入最多1位小数的数字</div>');
                	return;
                }
                submitForm("1");
            })
        }
    }
    new GomeProject_CommonBar({
        'laypage': 'GomeProjectPage',
        'lay_window': '.auto_search',
        'autoInput': '.autocomplete_input',
        'addEmployee': '.addEmployee',
        'tip_dialog': '#project_table',
    }).init()
});
