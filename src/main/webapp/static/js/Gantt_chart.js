function Gantt_chart() {

}
Gantt_chart.prototype = {
    init: function() {
        this.init_chart();
        this.toggle();
        var url = document.referrer;
        var array = url.split('/');
        var from = array[array.length - 1];
        
        if(from.indexOf('project') != -1
        		|| from.indexOf('agile') != -1){
        	$('#ulDetail').hide();
        	$('#ulList').show();
        }else{
        	$('#ulDetail').show();
        	$('#ulList').hide();
        }
        $(".breadCrumb").jBreadCrumb({'liCallback':function(index){
        	parent.popNavStack(index);
        }});
    },
    deepClone: function(src) {
        var _this = this;
        var clone = src;
        if (src instanceof Date) {
            clone = new Date(src.getDate());
            return clone;
        }
        if (src instanceof Array) {
            clone = [];
            for (var key in src) {
                clone[key] = _this.deepClone(src[key]);
            }
            return clone;
        }

        // 对于 Object
        if (src instanceof Object) {
            clone = {};
            for (var key in src) {
                if (src.hasOwnProperty(key)) { // 忽略掉继承属性
                    clone[key] = _this.deepClone(src[key]);
                }
            }
            return clone;
        }
        return src;
    },
    flag: true,
    init_chart: function() {
        var _this = this;
        var url_param = window.location.pathname.split('/');
        var id = url_param[url_param.length - 1];
        $.ajax({
            type: "GET",
            url: contextPath + '/project/gantt/find/' + id + '',
            dataType: 'json',
            contentType: "application/json"
        }).done(function(data) {
            _this.createGantt(data)
        }).done(function(json) {
            _this.createTable(json)
        })
    },
    createGantt: function(data) {
        var _this = this;
        var ajax_data = _this.deepClone(data);

        function datetime_to_unix(datetime) {
            var tmp_datetime = datetime.replace(/:/g, '-');
            tmp_datetime = tmp_datetime.replace(/ /g, '-');
            var arr = tmp_datetime.split("-");
            var now = new Date(Date.UTC(arr[0], arr[1] - 1, arr[2], arr[3] - 8, arr[4], arr[5]));
            return parseInt(now.getTime() / 1000);
        };
        var gantt_data = ajax_data.map(function(item, i) {
            return {
                name: item.userName,
                desc: ' ',
                values: [{
                    id: item.id,
                    from: '/Date(' + datetime_to_unix(item.startTime) + '000)/',
                    to: '/Date(' + datetime_to_unix(item.endTime) + '000)/',
                    desc: 'ID' + item.id + '<br/>Name:' + item.taskName,
                    label: (item.userName==null?"未知":item.userName) + ':' + item.schedule + '%' + ':' + item.endTime,
                    customClass: (item.schedule > 100) ? 'ganttRed' : ((item.schedule == 100) ? 'ganttOrange' : 'ganttGreen'),
                    dep: (ajax_data[i + 1] == undefined) ? '' : ajax_data[i + 1]['id']
                }]
            }
        });
        $(".gantt").gantt({
            source: gantt_data,
            navigate: 'scroll',
            scale: 'days',
            maxScale: 'weeks',
            minScale: 'hours',
            months: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            dow: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
        });
        $('.nav-zoomIn').trigger('click');
        $('.nav-zoomIn').trigger('click');
    },
    toggle: function() {
        var _this = this;

        function toggle_right() {
            $(this).toggleClass('toggle_right');
            if (_this.flag) {
                _this.flag = false;
                $(".charts_wrapper").animate({
                    width: '1199px',
                });
            } else {
                $(".charts_wrapper").animate({
                    width: '440px',
                });
                _this.flag = true;
            }
        }
        $('#toggle_button').click(toggle_right)
    },
    createTable: function(data) {
        var _this = this;
        var json = _this.deepClone(data);
        String.prototype.temp = function(obj) {
            return this.replace(/\$\w+\$/gi, function(matchs) {
                var returns = obj[matchs.replace(/\$/g, "")];
                returns = returns == null ? "" : returns;
                return (returns + "") == "undefined" ? "" : returns;
            });
        };

        function tpl(json) {
            var tpl = $('#table_tpl').html();
            var html = '';
            json.forEach(function(obj) {
                html += tpl.temp(obj)
            });
            return html;
        }
        var table_html = tpl(json);
        $('.table_info').find('tbody').html(table_html);
    }
}

new Gantt_chart().init()
