// add for crumb start
$(document).ready(function(){
	initCrumb($('#findReadOnlyCrumbUl'));
});
function findReadOnlyBuildCrumb(dom){
	parent.pushNavStack($(dom).text());
}
// add for crumb end
function createPage(config) {
    if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
    this.pageName = config.name;
}

createPage.prototype = {
    init: function() {
        this.createP();

    },
    render_data: function(json) {
        String.prototype.temp = function(obj) {
            return this.replace(/\$\w+\$/gi, function(matchs) {
            
                var returns = obj[matchs.replace(/\$/g, "")];
             
                return returns == null || (returns + "") == "undefined" ? "" : returns.split(" ")[0];
            });
        };
        var tpl = $('#view_task').html();
        var html = '';
        json.forEach(function(obj) {
        
            html += tpl.temp(obj)
        });
        return html;
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

    	
    	
        var judgeurl = window.location.search;
        var queryType=getUrlParam('queryType');
    	if(queryType != null) {
    		$(".GomeProject_tab_title").find("span").eq(queryType).addClass("selected");
    	}else{
    		$(".GomeProject_tab_title").find("span").eq(0).addClass("selected");
    	}
        if (judgeurl.indexOf('month') > 0) {
            param = Object.create(null);
            param.month = getUrlParam('month');
            getUrlParam('orgIds') ? (param.orgIds = getUrlParam('orgIds').split(',')) : '';
            getUrlParam('childOrgIds') ? (param.childOrgIds = getUrlParam('childOrgIds').split(',')) : '';
            getUrlParam('groupIds') ? (param.groupIds = getUrlParam('groupIds').split(',')) : '';
            param.proType = getUrlParam('proType');
            url = contextPath + '/datas/onlineTJNum/1';
        } else if (judgeurl.indexOf('skiptotal') > 0) {
            param = Object.create(null);
            param.year = getUrlParam('year');
            url = contextPath + '/datas/proTJNum/1';
        } else if (judgeurl.indexOf('skipProcessing') > 0) {
            param = Object.create(null);
            param.year = getUrlParam('year');
            url = contextPath + '/datas/jxzTJNum/1';
        } else if (judgeurl.indexOf('scheduleIdUnit') > 0) {
            param = Object.create(null);
            param.year = getUrlParam('year');
            param.scheduleId = getUrlParam('scheduleIdUnit');
            url = contextPath + '/datas/ztTJNum/1';
        } else if (judgeurl.indexOf('startTimeJX') > 0) {
        	param = Object.create(null);
        	param.startTime = Date.parse(getUrlParam('startTimeJX'));
            param.endTime = Date.parse(getUrlParam('endTime'));
            param.scheduleId = getUrlParam('scheduleId');
        	if(param.scheduleId == '99'){
        		//param.scheduleId = {};
        	}else{
        		param.scheduleId = param.scheduleId;
        	}
        	param.scheduleId = parseInt(param.scheduleId);
        	param.orgIds = getUrlParam('orgIds').split(',');
        	url = contextPath + '/datas/unitJxzTJNum/1';
        } else if (judgeurl.indexOf('startTimeSX') > 0) {
        	param = Object.create(null);
        	param.startTime = Date.parse(getUrlParam('startTimeSX'));
            param.endTime = Date.parse(getUrlParam('endTime'));
            param.scheduleId = getUrlParam('scheduleId');
            param.scheduleId = parseInt(param.scheduleId);
            param.orgIds = getUrlParam('orgIds').split(',');
        	url = contextPath + '/datas/unitYsxTJNum/1';
        } else if (judgeurl.indexOf('startTimeFX') > 0
        		|| judgeurl.indexOf('startTimeYQ') > 0) {
        	param = Object.create(null);
        	param.startTime = Date.parse(getUrlParam('startTimeFX'));
        	if(param.startTime == null || isNaN(param.startTime)){
        		param.startTime = Date.parse(getUrlParam('startTimeYQ'));
        	}
            param.endTime = Date.parse(getUrlParam('endTime'));
            param.scheduleId = getUrlParam('scheduleId');
            param.scheduleId = parseInt(param.scheduleId);
            param.orgIds = getUrlParam('orgIds').split(',');
        	url = contextPath + '/datas/unitztTJNum/1';
        } else if (judgeurl.indexOf('ztfb_scheduleId') > 0) {
        	param = Object.create(null);
        	param.scheduleId = getUrlParam('ztfb_scheduleId');
        	param.startTime = Date.parse(getUrlParam('startTime'));
            param.endTime = Date.parse(getUrlParam('endTime'));
        	url = contextPath + '/datas/bzZtTJNum/1';
        } else if (judgeurl.indexOf('bzpro_scheduleId') > 0) {
        	param = Object.create(null);
        	var scheduleId = getUrlParam('bzpro_scheduleId');
        	if(scheduleId == '99'){
        		//param.scheduleId = {};
        	}else{
        		param.scheduleId = parseInt(scheduleId);
        	}
        	
        	url = contextPath + '/datas/bzProTJNum/1';
        }  else if (judgeurl.indexOf('startTime') > 0) {
            param = Object.create(null);
            param.startTime = Date.parse(getUrlParam('startTime'));
            param.endTime = Date.parse(getUrlParam('endTime'));
            param.proType = getUrlParam('proType');
            param.scheduleId = getUrlParam('scheduleId');
            param.orgIds = getUrlParam('orgIds').split(',');
            url = contextPath + '/datas/unitTJNum/1';
        }else if (judgeurl.indexOf('xzJhsx_scheduleId') > 0) {
        	param = Object.create(null);
        	var scheduleId = getUrlParam('xzJhsx_scheduleId');
        	if(scheduleId == '99'){
        		//param.scheduleId = {};
        	}else{
        		param.scheduleId = parseInt(scheduleId);
        	}
        	url = contextPath + '/datas/xzJhsxTJNum/1'
        	} else if (judgeurl.indexOf('bzfx_scheduleId') > 0) {
        	param = Object.create(null);
        	var scheduleId = getUrlParam('bzfx_scheduleId');
        	if(scheduleId == '99'){
        		//param.scheduleId = {};
        	}else{
        		param.scheduleId = parseInt(scheduleId);
        	}
        	url = contextPath + '/datas/bzFxTJNum/1'
        	} else if (judgeurl.indexOf('bzjxz_scheduleId') > 0) {
        	param = Object.create(null);
        	var scheduleId = getUrlParam('bzjxz_scheduleId');
        	if(scheduleId == '99'){
        		//param.scheduleId = {};
        	}else{
        		param.scheduleId = parseInt(scheduleId);
        	}
        	url = contextPath + '/datas/bzjxzTJNum/1';
        }else if (judgeurl.indexOf('scheduleId') > 0) {
        	param = Object.create(null);
        	var scheduleId = getUrlParam('scheduleId');
        	if(scheduleId == '99'){
        		//param.scheduleId = {};
        	}else{
        		param.scheduleId = parseInt(scheduleId);
        	}
        	url = contextPath + '/datas/bzXzTJNum/1';
        }
        var _this = this;
        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(param),
            dataType: 'json',
            contentType: "application/json"
        }).done(function(data, status, jqXHR) {
            var page_data = data.resultData;
            $('.view_project_list').find('tbody').html(_this.render_data(data.resultData));
            $('.export_data').attr('href', (contextPath + '/project/find/export/' + '1'));
            laypage({
                cont: _this.pageName,
                pages: data.pages,
                curr: 1,
                jump: function(obj, first) {
                    $('.export_data').attr('href', (contextPath + '/project/find/export/' + obj.curr));
                    var urlArray = url.split('/');
                    var urlTemp = '/' + urlArray[urlArray.length - 3] + '/' + urlArray[urlArray.length - 2] + '/'
                    if (!first) {
                        $.ajax({
                            type: "POST",
                            url: contextPath + urlTemp + obj.curr,
                            data: JSON.stringify(param),
                            dataType: 'json',
                            contentType: "application/json"
                        }).done(function(data) {
                            $('.view_project_list').find('tbody').html(_this.render_data(data.resultData))
                        })
                    }
                }
            });
        })
    }
}

new createPage({ 'name': 'GomeProjectPage' }).init()
