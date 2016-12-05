$(document).ready(function() {
	// add for crumb start
	initCrumb($('#demandDetailCrumbUl'));
	// add for crumb end
});

function demandDetailBuildCrumb(dom){
	parent.pushNavStack($(dom).text());
}

function demand_infodetail(name) {
    this.name = name
}

demand_infodetail.prototype = {
    init: function() {
        this.detail_control()
    },
    detail_control: function() {
    	var _id = $('#nid').val();
        var _this = this;
        $('body').on('click', 'a', function(e) {
            switch (e.target.className) {
                case 'refuse':
                    var content = $('#refuse').html();
                    var url = contextPath + '/demand/deny';
                    var d = top.dialog({
                    	fixed: true,
                        content: content,
                        okValue: '确定',
                        ok: function() {
                        	var deny = $(window.parent.document).find("#deny option:selected").text();
                        	var reason = $(window.parent.document).find('.dialog_editbox').text();
                        	$.post(url,{'needId':_id,'deny':deny,'reason':reason},function(){
                        		location.reload();
                        	});
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: '提示'
                    }).width(400).height(240);
                    d.showModal();
                    break;
                case 'transfer':
                    var content = $('#transfer').html();
                    var url = contextPath + '/demand/transfer';
                    var d = top.dialog({
                    	fixed: true,
                        content: content,
                        okValue: '确定',
                        ok: function() {
                        	var userName = $(window.parent.document).find('#userSelect').attr('username');
                        	var userId = $(window.parent.document).find('#userSelect').attr('userid');
                        	$.post(url,{'needId':_id,'payUserName':userName,"payUserId":userId},function(){
                        		location.reload();
                        	});
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: '提示'
                    }).width(300).height(130);
                    d.showModal();
                    _this.bind_window_complete();
                    break;
                case 'accept':
                    var content = $('#accept').html();
                	var title = $('#ntitle').text();
                    var d = top.dialog({
                    	fixed: true,
                        content: content,
                        okValue: '确定',
                        ok: function() {
                        	var type = $(window.parent.document).find('#type').val();
                        	location.href=contextPath+"/project/needToSave?proType="+ type + "&demandId=" + _id + "&demandName=" + title;
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: '提示'
                    }).width(300).height(130);
                    d.showModal();
                    break;
            }
            
        })
    },
    bind_window_complete: function() {
    	$('.autocomplete_input:visible',parent.document).on('keydown',function(){
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

new demand_infodetail('#control_detail').init()
