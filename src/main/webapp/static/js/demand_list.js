// add for crumb start
$(document).ready(function(){
	initCrumb($('#findReadNeedCrumbUl'));
});
function findReadNeedBuildCrumb(dom){
	parent.pushNavStack($(dom).text());
}
// add for crumb end
function createPage(config) {
    if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
    this.pageName = config.name;
}

createPage.prototype = {
    init: function() {
        this.createP()
    },
    render_data: function(json) {
    	/*console.log("json:")
    	 console.log(json);*/
    
        var html = "";
        json.forEach(function(item, i) {
        	html += '<tr><td> <a onclick="findReadNeedBuildCrumb(this);" href="'+contextPath+'/demand/toDetail/'+item.needId+'" class="detail">' + item.title + '</a></td>' + '<td>' + (item.priorityName==undefined ? '--' : item.priorityName) + '</td>' + '<td>' + (item.createTime==undefined ? '--' : item.createTime.split(' ')[0]) + '</td>' + '<td>' + (item.planTime == undefined ? '--' : item.planTime.split(' ')[0]) + '</td>' + '<td>' + (item.statusName==undefined ? '--' : item.statusName) + '</td></tr>';
        })
        $('.demand_show_table').find('tbody').html(html)
    },
    createP: function() {
        var url;
        var param;

        function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        }
    	function getTimestamp(str) {
            str = str.replace(/-/g, '/');
            var date = new Date(str);
            var sTime = date.getTime();
            return sTime
        }
        param = Object.create(null);
        param.startTime = getUrlParam('starttime');
        param.endTime = getUrlParam('endtime');
//        param.proType = getUrlParam('orgId');
        if($('#createUser').val() != null
        		&& $('#createUser').val() != undefined
        		&& $('#createUser').val().length > 0){
        	param.createUser = $('#createUser').val();
        }
      
        if($('input[name="orgIds"]').val() != null
        		&& $('input[name="orgIds"]').val() != undefined
        		&& $('input[name="orgIds"]').val().length > 0
        		&& $('input[name="orgIds"]').val().split(',') != null
        		&& $('input[name="orgIds"]').val().split(',') != undefined
        		&& $('input[name="orgIds"]').val().split(',').length > 0){
        	param.orgIds = $('input[name="orgIds"]').val().split(',');
        }
        //param.createUser = $('#createUser').val();
        //param.orgId = $('input[name="orgId"]').val().split(',');
        url = contextPath + '/datas/tjPeopleList/1?startTime='+param.startTime+'&endTime='+ param.endTime +'&createUser=' + (param.createUser?param.createUser:"")+ '&orgIds=' + (param.orgIds?param.orgIds:"");
      // alert(url)
        var _this = this;
        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(param),
            dataType: 'json',
            contentType: "application/json"
        }).done(function(data, status, jqXHR) {
            var page_data = data.resultData;
            $('.demand_show_table').find('tbody').html(_this.render_data(page_data));
            laypage({
                cont: _this.pageName,
                pages: data.pages,
                curr: 1,
                jump: function(obj, first) {
                    if (!first) {
                        $.ajax({
                       
                            type: "POST",
                            url: contextPath + '/datas/tjPeopleList/'+obj.curr+'?createUser=' + (param.createUser?param.createUser:"")+ '&orgIds=' + (param.orgIds?param.orgIds:""),
                            data: JSON.stringify(param),
                            dataType: 'json',
                            contentType: "application/json"
                        }).done(function(data) {
                        	 var page_data = data.resultData;
                            $('.demand_show_table').find('tbody').html(_this.render_data(page_data))
                        })
                    }
                }
            });
        })
    }
}

new createPage({ 'name': 'GomeProjectPage' }).init()
