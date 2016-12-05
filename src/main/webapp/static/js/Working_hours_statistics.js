// add for crumb start
$(document).ready(function(){
	console.log(';aaaa');
	initCrumb($('#findReadOnly_pjCrumbUl'));
});
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
        String.prototype.temp = function(obj) {
            return this.replace(/\$\w+\$/gi, function(matchs) {
            	var propertyName = matchs.replace(/\$/g, "");
                var returns = obj[matchs.replace(/\$/g, "")];
                // 如果是时间则取日期部分
                if(propertyName == "planTime" && returns != null){
            		returns = returns.split(" ")[0]
            	}
                return returns == null || (returns + "") == "undefined" ? "--" : returns;
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
        var _this = this;
        $.ajax({
            type: "POST",
            url: contextPath + '/project/find/readOnly/1',
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
                    if (!first) {
                        $.ajax({
                            type: "POST",
                            url: contextPath + '/project/find/readOnly/' + obj.curr,
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
