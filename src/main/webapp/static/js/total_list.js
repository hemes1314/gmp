// add for crumb start
$(document).ready(function(){
	initCrumb($('#totalDateCrumbUl'));
});
function totalDateBuildCrumb(dom){
	var org_name = $(dom).closest('tr').children().eq($(dom).closest('td').index()>4?5:0).html();
	var status = $('.department_data .title').eq($(dom).closest('td').index() + 1).text();
	parent.pushNavStack(org_name+'('+status+')');
}

//add for crumb end
function createPage(config) {
    if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
    this.pageName = config.name;
}

createPage.prototype = {
    init: function() {
    	this.bindStartEndDate();
    	this.bindEvent();
        this.createP();
        this.bindDom();
$(".breadCrumb").jBreadCrumb({easing:'swing'});
        
    },
    render_data: function(json, param) {
        var html = "<tr>";
        var obj = json[0][0];
        var scheduleId;
        /* 已上线： 5 风险： 3 延期： 4 暂停： 9 未启动：0*/
        /*obj:{sums: 16, jxz: 13, ysx: 4, fx: 1, yq: 2, zt: 0, wqd: 0}*/
        for (var i in obj) {
            switch (i) {
                case 'sums':
                	html += '<td>年度项目统计</td><td><a onclick="parent.pushNavStack('+ "'年度项目统计'" +');" href=' + contextPath + "/datas/toUnit/?skiptotal=total&year=" + param.year + '>' + obj[i] + '项</a></td>';
                    break;
                case 'jxz':
                    html += '<td>进行中</td><td><a onclick="parent.pushNavStack('+ "'进行中'" +');"  href=' + contextPath + "/datas/toUnit/?skipProcessing=pro&year=" + param.year + '>' + obj[i] + '项</a></td>';
                    break;
                case 'ysx':
                    scheduleId = '5';
                    html += '<td>已上线项目</td><td><a onclick="parent.pushNavStack('+ "'已上线项目'" +');"  href=' + contextPath + "/datas/toUnit/?scheduleIdUnit=5&year=" + param.year + '>' + obj[i] + '项</a></td>';
                    break;
                case 'fx':
                    html += '<td>风险项目</td><td><a onclick="parent.pushNavStack('+ "'风险项目'" +');" href=' + contextPath + "/datas/toUnit/?scheduleIdUnit=3&year=" + param.year + '>' + obj[i] + '项</a></td></td></tr><tr>';
                    break;
                case 'yq':
                    html += '<td>延期项目</td><td><a onclick="parent.pushNavStack('+ "'延期项目'" +');" href=' + contextPath + "/datas/toUnit/?scheduleIdUnit=4&year=" + param.year + '>' + obj[i] + '项</a></td></td>';
                    break;
                case 'zt':
                    html += '<td>暂停项目</td><td><a onclick="parent.pushNavStack('+ "'暂停项目'" +');" href=' + contextPath + "/datas/toUnit/?scheduleIdUnit=9&year=" + param.year + '>' + obj[i] + '项</a></td></td>';
                    
                    break;
                case 'wqd':
                    html += '<td>未启动项目</td><td><a onclick="parent.pushNavStack('+ "'未启动项目'" +');" href=' + contextPath + "/datas/toUnit/?scheduleIdUnit=0&year=" + param.year + '>' + obj[i] + '项</a></td></td><td></td><td></td><tr>';
                    break;
            }
            $('.total_data').find('tbody').html(html)
        }
    },
    render_pro: function(json) {
        var html = "<tr>";
        var obj = json[0][0];
        /* 已上线： 5 风险： 3 延期： 4 暂停： 9 未启动：0*/
        /*{"ysx":0,"jxz":0,"xz":0,"fx":0,"zt":0,"xzjh":0}*/
        for (var i in obj) {
            switch (i) {
                case 'ysx':
                    html += '<td rowspan="2">本周项目进度</td><td>本周已上线</td><td><a onclick="parent.pushNavStack('+ "'本周已上线'" +');"  href=' + contextPath + "/datas/toUnit/?bzpro_scheduleId=5" + '>' + obj[i] + '项</a></td>';
                    break;
                case 'jxz':
                    html += '<td>本周进行中</td><td><a onclick="parent.pushNavStack('+ "'本周进行中'" +');" href=' + contextPath + "/datas/toUnit/?bzjxz_scheduleId=99" + '>' + obj[i] + '项</a></td>';
                    break;
                case 'xz':
                    html += '<td>本周新增</td><td><a onclick="parent.pushNavStack('+ "'本周新增'" +');" href=' + contextPath + "/datas/toUnit/?scheduleId=99" + '>' + obj[i] + '项</a></td></tr><tr>';
                    break;
                case 'fx':
                    html += '<td>本周风险项目</td><td><a onclick="parent.pushNavStack('+ "'本周风险项目'" +');" href=' + contextPath + "/datas/toUnit/?bzfx_scheduleId=3" + '>' + obj[i] + '项</a></td>';
                    break;
                case 'zt':
                    html += '<td>本周暂停项目</td><td><a onclick="parent.pushNavStack('+ "'本周暂停项目'" +');" href=' + contextPath + "/datas/toUnit/?bzpro_scheduleId=9" + '>' + obj[i] + '项</a></td>';
                    break;
                case 'xzjh':
                    html += '<td>下周计划上线</td><td><a onclick="parent.pushNavStack('+ "'下周计划上线'" +');" href=' + contextPath + "/datas/toUnit/?xzJhsx_scheduleId=99" + '>' + obj[i] + '项</a></td></tr>';
                    break;
            }
        }
        return html;

    },
    /*项目排期*/
    render_state: function(json) {
        var data = json[0];
        var compareData = ['未启动', '正常', '提前', '延期风险', '已延期', '待上线', '已取消', '内部上线', '暂停', '暂缓', '待排期', '部分上线'];
        var obj = {};
        data.forEach(function(item, i) {
            obj[item.name] = item.name;
        });
        var new_arr = [];
        compareData.forEach(function(item, i) {
            if (!obj[item]) {
                new_arr.push(item)
            }
        })
        new_arr.forEach(function(item) {
            data.push({
                name: item,
                value: '',
            })
        })

        function geTsort(obj) {
            switch (obj.name) {
                case '未启动':
                    obj.sort = 1;
                    break;
                case '正常':
                    obj.sort = 2;
                    break;
                case '提前':
                    obj.sort = 3;
                    break;
                case '延期风险':
                    obj.sort = 4;
                    break;
                case '已延期':
                    obj.sort = 5;
                    break;
                case '待上线':
                    obj.sort = 6;
                    break;
                case '已取消':
                    obj.sort = 7;
                    break;
                case '内部上线':
                    obj.sort = 8;
                    break;
                case '暂停':
                    obj.sort = 9;
                    break;
                case '暂缓':
                    obj.sort = 10;
                    break;
                case '待排期':
                    obj.sort = 11;
                    break;
                case '部分上线':
                    obj.sort = 12;
                    break;
            }
        }

        data.forEach(function(item) {
            geTsort(item)
        })

        data.sort(function(a, b) {
            return a.sort - b.sort
        });

        var newData = [];
        data.forEach(function(item) {
            if (item.name != '已上线') {
                newData.push(item)
            }
        })
        var html = "<tr>";
        $('#start').val();
        var startTime = $('#start').val();
        var endTime = $('#end').val();
        
        newData.forEach(function(item, i) {
            var sort = i + 1;
            var id = i;
            if(id>=5) {
            	id=id+1;
            } 
            var value = item.num;
           	if(value == null || value == undefined){
           		value = '0';
         	}
	        if (sort == 1) {
	            html += '<td rowspan="4">项目状态分布</td><td>' + item.name + '</td>' + '<td><a onclick="parent.pushNavStack('+ "'" +item.name+ "'" +');" href=' + contextPath + "/datas/toUnit/?startTime="+startTime+"&endTime="+endTime+"&ztfb_scheduleId=0" + '>' + value + '项</a></td>'
	        } else {
	            html += '<td>' + item.name + '</td>' + '<td><a onclick="parent.pushNavStack('+ "'" +item.name+ "'" +');" href=' + contextPath + "/datas/toUnit/?startTime="+startTime+"&endTime="+endTime+"&ztfb_scheduleId=" + id +'>' + value + '项</a></td>';
	            if (sort % 3 == 0) {
	                html += '</tr><tr>'
	            }
	        }
        })
        return html
    },
    renderUnit: function(data) {
        var json = data[0];
        var startTime = $('#start1').val();
        var endTime = $('#end1').val();
        var height = Math.ceil(json.length / 2) + 1;
        var html = '<tr><td rowspan="' + height + '" class="title">部门数据统计</td><td class="title">部门名称</td><td class="title">进行中</td><td class="title">已上线</td><td class="title">风险</td><td class="title">延期</td><td class="title">部门名称</td><td class="title">进行中</td><td class="title">已上线</td><td class="title">风险</td><td class="title">延期</td></tr><tr>';
        json.forEach(function(item, i) {
            html += '<td class="totalListOrgNm">' + item.org_name + '</td>' + '<td><a  onclick="totalDateBuildCrumb(this);" href=' + contextPath + "/datas/toUnit/?startTimeJX="+startTime+"&endTime="+endTime+"&scheduleId=99"+"&orgIds="+item.orgId+">" + item.jxz + '项</a></td>' + '<td><a  onclick="totalDateBuildCrumb(this);" href=' + contextPath + "/datas/toUnit/?startTimeSX="+startTime+"&endTime="+endTime+"&scheduleId=5"+"&orgIds="+item.orgId+">" + item.ysx + '项</a></td>' + '<td><a  onclick="totalDateBuildCrumb(this);" href=' + contextPath + "/datas/toUnit/?startTimeFX="+startTime+"&endTime="+endTime+"&scheduleId=3"+"&orgIds="+item.orgId+">" + item.fx + '项</a></td>' + '<td><a  onclick="totalDateBuildCrumb(this);" href=' + contextPath + "/datas/toUnit/?startTimeYQ="+startTime+"&endTime="+endTime+"&scheduleId=4"+"&orgIds="+item.orgId+">" + item.yq + '项</a></td>';
            if ( i& 1 && i != json.length - 1) {
                html += '</tr><tr>'
            }
        })
        if (json.length % 2 != 0) {
            html += '<td></td><td></td><td></td><td></td><td></td></tr>'
        } else {
            html += '</tr>'
        }
        $('.department_data').find('tbody').html(html)
    },
    createP: function() {
        var url;
        var _this = this;
        var param = { year: $('#years').find('option:selected').text() };
        _this.get_Total_data(param)
    },
    bindEvent: function() {
    	var _this = this;
    	var start = {
                elem: '#start',
                format: 'YYYY-MM-DD',
                min: '',
                max: '',
                istime: false,
                istoday: false,
                choose: function(datas) {
                	end.min = datas;
                    end.start = datas
                	var year = { year: datas };
                	_this.get_Total_data(year)
                }
            };
        	laydate(start);
        	var start1 = {
                    elem: '#start1',
                    format: 'YYYY-MM-DD',
                    min: '',
                    max: '',
                    istime: false,
                    istoday: false,
                    choose: function(datas) {
                    	end1.min = datas;
                        end1.start = datas
                    	var year = { year: datas };
                    	_this.get_Total_data(year);
                    }
                };
            laydate(start1);
            var end = {
                    elem: '#end',
                    format: 'YYYY-MM-DD',
                    min: '',
                    max: '',
                    istime: false,
                    istoday: false,
                    choose: function(datas) {
                    	start.max = datas;
                    	var year = { year: datas };
                    	_this.get_Total_data(year)
                    }
                };
            laydate(end);
        	
            var end1 = {
                    elem: '#end1',
                    format: 'YYYY-MM-DD',
                    min: '',
                    max: '',
                    istime: false,
                    istoday: false,
                    choose: function(datas) {
                    	start1.max = datas;
                    	var year = { year: datas };
                    	_this.get_Total_data(year)
                    }
                };
            laydate(end1);
            
            
            start.choose($("#start").val());
            end.choose($("#end").val());
            start1.choose($("#start1").val());
            end1.choose($("#end1").val());
    },
    
    bindStartEndDate: function(){
    	var currentDate = new Date();
    	var preWeekDate = new Date(currentDate.getTime() - 6*24*60*60*1000);  //前七天
    	var premounth = preWeekDate.getMonth() + 1;
    	if(premounth.toString().length == 1){
    		premounth = '0' + premounth;
    	}
    	var curmounth = currentDate.getMonth() + 1;
    	if(curmounth.toString().length == 1){
    		curmounth = '0' + curmounth;
    	}
    	var preDate = preWeekDate.getDate() ;
    	if(preDate.toString().length == 1){
    		preDate = '0' + preDate;
    	}
    	var curDate = currentDate.getDate() ;
    	if(curDate.toString().length == 1){
    		curDate = '0' + curDate;
    	}
    	$('#start').val(preWeekDate.getFullYear() + '-' + premounth + '-' + preDate);
    	$('#end').val(currentDate.getFullYear() + '-' + curmounth + '-' + curDate);
    	$('#start1').val(preWeekDate.getFullYear() + '-' + premounth + '-' + preDate);
    	$('#end1').val(currentDate.getFullYear() + '-' + curmounth + '-' + curDate);
    	$('#start').attr('placeholder',preWeekDate.getFullYear() + '-' + premounth + '-' + preWeekDate.getDate());
    	$('#end').attr('placeholder',currentDate.getFullYear() + '-' + curmounth + '-' + currentDate.getDate());
    	$('#start1').attr('placeholder',preWeekDate.getFullYear() + '-' + premounth + '-' + preWeekDate.getDate());
    	$('#end1').attr('placeholder',currentDate.getFullYear() + '-' + curmounth + '-' + currentDate.getDate());
    	
    },
    get_Total_data: function(param) {
        var _this = this;
        var start = $('#start').val();
        var end = $('#end').val();
        var start1 = $('#start1').val();
        var end1 = $('#end1').val();
        $.when($.ajax({
                type: "POST",
                url: contextPath + '/datas/proTJ',
                data: JSON.stringify(param),
                dataType: 'json',
                contentType: "application/json"
            }),
            $.ajax({
                type: "POST",
                url: contextPath + '/datas/bzProTJ',
                data: JSON.stringify({}),
                dataType: 'json',
                contentType: "application/json"
            }),
            $.ajax({
                type: "POST",
                url: contextPath + '/datas/ztProTJ',
                data: JSON.stringify({}),
                dataType: 'json',
                contentType: "application/json"
            }),
            $.ajax({
                type: "POST",
                url: contextPath + '/datas/unitProTJ',
                data: JSON.stringify({ startTime: Date.parse(start1), endTime: Date.parse(end1) }),
                dataType: 'json',
                contentType: "application/json"
            })
        ).done(function(data, data1, data2, data3) {
            _this.render_data(data, param);
            $('.week_data').find('tbody').html(_this.render_pro(data1, param) + _this.render_state(data2, param));
           
            _this.renderUnit(data3);
        })
    },
    bindSelect: function() {
        var _this = this;
        $('#createTime').change(function() {
            var $that = $(this);
            var year = { year: $that.find('option:selected').text() };
            _this.get_Total_data(year)
        })
    }
}
new createPage({ 'name': 'GomeProjectPage' }).init()
