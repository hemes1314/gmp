function createPage(config) {
    if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
    this.pageName = config.name;
}

createPage.prototype = {
    init: function() {
        this.createP()
    },
    getParam: function() {
        var url_param = window.location.pathname.split('/');
        var id = url_param[url_param.length - 1];
        return id;
    },
    render_data: function(json) {
        String.prototype.temp = function(obj) {
            return this.replace(/\$\w+\$/gi, function(matchs) {
                var returns = obj[matchs.replace(/\$/g, "")];
                return (returns + "") == "undefined" ? "" : returns;
            });
        };
        var tpl = $('#record_temp').html();
        var html = '';
        json.forEach(function(obj) {
            html += tpl.temp(obj)
        });
        return html;
    },
    createP: function() {
        var _this = this;
        var url = contextPath + '/log/find/' + _this.getParam() + '/';
        $.ajax({
            type: "GET",
            url: url + '1',
            dataType: 'json',
            contentType: "application/json"
        }).done(function(data, status, jqXHR) {
            var page_data = data.resultData;
            $('.record_content').html(_this.render_data(data.resultData));
            laypage({
                cont: _this.pageName,
                pages: data.pages,
                curr: 1,
                jump: function(obj, first) {
                    if (!first) {
                        $.ajax({
                            type: "GET",
                            url: url + obj.curr,
                            dataType: 'json',
                            contentType: "application/json"
                        }).done(function(data) {
                            $('.record_content').html(_this.render_data(data.resultData));
                        })
                    }
                }
            });
        })
    }
}

new createPage({ 'name': 'page' }).init()
