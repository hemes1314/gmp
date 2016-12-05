$(document).ready(function() {
    function GomeProject_CommonBar(config) {
        if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
        this.page = config.laypage || "";
        this.windowButon = config.lay_window || '';
        this.autocomplete_input = config.autoInput || '';
        this.addEmp = config.addEmployee || " ";
        this.project_table = config.tip_dialog || '';
    }

    GomeProject_CommonBar.prototype = {
        constructor: GomeProject_CommonBar,
        init: function() {
        	
            this.bind_complete();
            this.files_list();
            this.form_submit();
            this.file_delete();
            this.work_Calculation();
        },
        bind_complete: function() {
        	$('.autocomplete_input:visible').on('keydown',function(){
            	$(this).autocomplete({
            		source: function(request, response) {
            			$.ajax({
            				type: 'POST',
            				url: contextPath + '/findUser',
            				dataType: "json",
            				data: JSON.stringify({ userName: request.term }),
            				contentType: "application/json"
            			}).done(function(data) {
            				response($.map(data, function(item) {
            					return { user: item.userId, value: item.userId + '(' + item.userName + ')', label: item.userId + '(' + item.userName + ',' + item.email +  ')', userId: item.id ,userName:item.userName}
            					}));
            			});
            		},
            		minLength: 1,
            		select: function(event, ui) {
            			$(this).attr('user',ui.item.user);
            			$(this).attr('userid',ui.item.userId);
            			$(this).attr('userName',ui.item.userName);
            		 	$(this).val(ui.item.value);
            		 	$(this).attr('title',ui.item.label);
            		 	$(this).parent().find("input:first").val(ui.item.userId);
            		},
            		_renderMenu: function(ul, items) {
            			var that = this;
            			$.each(items, function(index, item) {
            				that._renderItemData(ul, item);
            			});
            			$(ul).find("li:odd").addClass("odd");
           			}
            	})  	
           })
        },
        work_Calculation: function() {
            var _this = this;
            var start = {
                elem: '#planTime',
                format: 'YYYY-MM-DD',
                min: laydate.now(),
                max: '2099-06-16 23:59:59',
                istime: true,
                istoday: false,
                choose: function(datas) {
//                    end.min = datas;
//                    end.start = datas;
//                    last.min = datas;
//                    last.start = datas;
                    $('#planTime').removeClass("input_validation-failed");
                }
            };
            laydate(start);
        },
        file_delete: function() {
        	$('.file_delete').click(function() {
        		$(this).parent().remove();
            })
        },
        
        files_list: function() {
            var arr = [];
            $('#upload').on('change', function() {
            	arr.length = 0;
                var upfiles = $(this)[0].files;
                for (var i = 0, len = upfiles.length; i < len; i++) {
                    arr.push(upfiles[i].name);
                }
                var html = arr.join(';');
                if (arr.length >= 4) {
                    var new_arr = arr.slice(0, 4);
                    html = new_arr.join(';') + '....';
                }
                $('#content').html(html).attr('title', arr.join(';'));
            })
        },
        form_submit: function() {
            $("#saveForm").submit(function(evt) {
                evt.preventDefault();
                // 判断文件是否为空，为空则把文件移除
                var upload = $("#upload");
                if (upload.val() == undefined || upload.val() == "") {
                    upload.remove();
                }
                var needId = $('#needId').val();
                var url = contextPath + '/demand/save';
                if(needId != null && needId != undefined && needId.length > 0){
                	url = contextPath + '/demand/update';
                }
                
                $("#saveForm").ajaxSubmit({
                    url: url,
                    dataType: "json",
                    type: "POST",
                    uploadProgress: function(event, position, total, percentComplete) {
                    },
                    success: function(data) {
                    	location.href = contextPath + '/demand/tolist';
                    },
                    error: function(error) {
                        $(".form_file").append(upload);
                        console.log("error", error);
                    }
                });
            });
            
            // save
            $('.btn-default').click(function() {
            	var title = $('input[name="title"]').val();
            	if(title == "") {
            		layer.msg('标题不能为空！')
            		return false;
            	}
            	if($('input[name="keyUserId"]').val() == ""
            		|| $('input[name="key_user_name"]').val() == "") {
            		layer.msg('关键用户不能为空！')
            		return false;
            	}
            	if($('#priorityId').val() == "") {
	              	layer.msg('优先级不能为空！')
	              	return false;
            	}
            	if($('input[name="planTime"]').val() == "") {
                	layer.msg('期望上线时间不能为空！')
                	return false;
                }
            	if($('select[name="unitBsId"]').val() == "") {
                 	layer.msg('业务部门不能为空！')
                 	return false;
                }
            	if($('select[name="payUnitId"]').val() == "") {
	              	layer.msg('交付部门不能为空！')
	              	return false;
            	}
            	if($('input[name="payUserId"]').val() == ""
            		|| $('input[name="pay_user_name"]').val() == "") {
            		layer.msg('交付人员不能为空！')
            		return false;
            	}
            	if($('input[name="payUserId"]').val() == $('input[name="createUser"]').val())
        		{
	        		layer.msg('交付人员不能是自己！')
	        		return false;
        		}
                if($('#proRemark').val() == "") {
                	layer.msg('需求简述不能为空！')
                	return false;
                }
                if($('#functionDesc').val() == "") {
                	layer.msg('功能描述不能为空！');
                	return false;
                }
                if($('#needTarget').val() == "") {
                	layer.msg('需求目标不能为空！')
                	return false;
                }
                $("#saveForm").trigger('submit');
            })
        }
    }

    new GomeProject_CommonBar({
        'laypage': 'GomeProjectPage',
        //'lay_window': '.auto_search',
        'autoInput': '.autocomplete_input',
        'addEmployee': '.addEmployee',
        'tip_dialog': '#project_table',
    }).init()
})
