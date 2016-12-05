function demand_detail(name) {
    this.name = name
}

demand_detail.prototype = {
    init: function() {
        this.detail_control()
    },
    detail_control: function() {
        var _this = this;
        $('body').on('click', 'a', function(e) {
            switch (e.target.className) {
                case 'refuse':
                	var tr = $(e.target).closest('tr');
                	var id = tr.find('td').eq(0).text();
                    var content = $('#refuse').html();
                    var d = dialog({
                        content: content,
                        okValue: '确定',
                        ok: function() {
                        	var deny = $('#deny').find("option:selected").text();
                        	var reason = $('.dialog_editbox').text();
                        	$.post('deny',{'needId':id,'deny':deny,'reason':reason},function(){
                        		$('.search_data').click();
                        		location.reload();
                        	});
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: '提示'
                    }).width(400).height(240);
                    d.show();
                    break;
                case 'transfer':
                    var content = $('#transfer').html();
                    var tr = $(e.target).closest('tr');
                	var id = tr.find('td').eq(0).text();
                    var d = dialog({
                        content: content,
                        okValue: '确定',
                        ok: function() {
                        	var deny = $('#deny').find("option:selected").text();
                        	var reason = $('.dialog_editbox').text();
                        	var userName = $('#userSelect').attr('userid');
                        	$.post('transfer',{'needId':id,'payUserId':userName},function(){
                        		$('.search_data').click();
                        		location.reload();
                        	});
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: '提示'
                    }).width(300).height(130);
                    d.show();
                    _this.bind_window_complete();
                    break;
                case 'accept':
                    var content = $('#accept').html();
                    var tr = $(e.target).closest('tr');
                	var id = tr.find('td').eq(0).text();
                	var title = tr.find('td').eq(1).text();
                    var d = dialog({
                        content: content,
                        okValue: '确定',
                        ok: function() {
                        	var type = $('#type').val();
                        	location.href=contextPath+"/project/needToSave?proType="+ type + "&demandId=" + id;
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: '提示'
                    }).width(300).height(130);
                    d.show();
                    break;
            }
        })
    },
    bind_window_complete: function() {
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
        		},
        		_renderMenu: function(ul, items) {
        			var that = this;
        			$.each(items, function(index, item) {
        				that._renderItemData(ul, item);
        			});
        			$(ul).find("li:odd").addClass("odd");
       			}
        		});  	
       });
    }
}

new demand_detail('#control_detail').init()
