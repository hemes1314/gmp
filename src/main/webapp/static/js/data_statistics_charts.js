function statusBuildCrumb(dom){
	var titleTxt = $(dom).closest('tr').prev().children().eq($(dom).closest('td').index()).text();
	if($(dom).text() != '0项'){
		parent.pushNavStack(titleTxt);
	};
}
    $(document).ready(function() {
initCrumb($('#statusCrumbUl'));
        function GomeProject_CommonBar(config) {
            if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
            this.page = config.laypage || "";
            this.searchButton = config.searchButton;
        }

        GomeProject_CommonBar.prototype = {
            constructor: GomeProject_CommonBar,
            init: function() {
                this.createSelect();
                this.chart_ajax();
            },
            auto_click: function() {
                $('.search_data')[0].click();
            },
            createSelect: function() {
                var _this = this;
                var ajax_option = {
                    type: "POST",
                    url: contextPath + '/orgManage/getQueryCondition',
                    data: JSON.stringify({}),
                    dataType: "json",
                    contentType: "application/json"
                };
                var op_arr = ['orgId', 'childOrgId', 'groupId'];
                var multi_option = {
                    zIndex: 1000,
                    width: 150,
                    height: 30,
                };
                var obj = {};
                op_arr.forEach(function(item, i) {
                    obj[i] = JSON.parse(JSON.stringify(multi_option));
                    if (i < 2) {
                        obj[i].onChange = function() {
                        	// add by wubin 20160525 begin
                        	if(i == 0) {
                            	$('#groupId').html("").selectlist(obj[i]);
                                $('#groupId input[type="button"]').eq(0).val("全部");
                        	} 
                        	// add by wubin 20160525 end
                            var val = $('input[name="' + item + '"]').val().split(',');
                            var param = {};
                            param.orgIdList = val;
                            ajax_option.data = JSON.stringify(param);
                            $.ajax(ajax_option).done(function(data) {
                                _this.render_select(data, '#' + op_arr[i + 1], obj[i + 1])
                            })
                        };
                    }
                });
                $.ajax(ajax_option).done(function(data) {
                    _this.render_select(data, '#orgId', obj[0]);
                    _this.auto_click();
                })
            },
            render_select: function(json, name, option) {
                var data = json;
                var str = '';
                var val_arr = [];
                data.forEach(function(item) {
                    val_arr.push(item.orgId);
                    str += '<option value="' + item.orgId + '">' + item.orgName + '</option>';
                });
                option.defaultValues = val_arr.join(',');
                $(name).html(str).selectlist(option);
                // 默认显示全部  wubin 2016/05/18
                $(name + ' input[type="button"]').eq(0).val("全部");
            },
            CreatePage: function() {
                var _this = this;
                if (document.getElementById(_this.page)) {
                    laypage({
                        cont: document.getElementById(_this.page),
                        pages: 100,
                        skip: true,
                        skin: '#AF0000',
                    });
                }

            },
            createTable: function(json) {
                function deepClone(src) {
                    var clone = src;
                    if (src instanceof Date) {
                        clone = new Date(src.getDate());
                        return clone;
                    }
                    if (src instanceof Array) {
                        clone = [];
                        for (var key in src) {
                            clone[key] = deepClone(src[key]);
                        }
                        return clone;
                    }
                    // 对于 Object
                    if (src instanceof Object) {
                        clone = {};
                        for (var key in src) {
                            if (src.hasOwnProperty(key)) { // 忽略掉继承属性
                                clone[key] = deepClone(src[key]);
                            }
                        }
                        return clone;
                    }

                    return src;
                }

                var data = deepClone(json);
                var compareData = ['未启动','正常','提前','延期风险','已延期','已上线','待上线','已取消','内部上线','暂停','暂缓','待排期','部分上线'];
                var obj = {};
                data.forEach(function(item, i) {
                    obj[item.name] = item.name;
                });
                console.log(obj);
                var new_arr = [];
                compareData.forEach(function(item, i) {
                    if (!obj[item]) {
                        new_arr.push(item)
                    }
                })
                new_arr.forEach(function(item) {
                    data.push({
                        id: '',
                        name: item,
                        value: '',
                        targetUrl: ''
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
                        case '已上线':
                            obj.sort = 6;
                        case '待上线':
                            obj.sort = 7;
                            break;
                        case '已取消':
                            obj.sort = 8;
                            break;
                        case '内部上线':
                            obj.sort = 9;
                            break;
                        case '暂停':
                            obj.sort = 10;
                            break;
                        case '暂缓':
                            obj.sort = 11;
                            break;
                        case '待排期':
                            obj.sort = 12;
                            break;
                        case '部分上线':
                        	obj.sort = 13;
                        	break;
                    }
                }

                data.forEach(function(item) {
                    geTsort(item)
                })

                data.sort(function(a, b) {
                    return a.sort - b.sort
                });
                var desc = '<tr><td>状态</td>';
                var num = '<tr><td>数量</td>';
                data.forEach(function(item, i) {
                    desc += '<td>' + item.name + '</td>';
                    num += '<td><a onclick="statusBuildCrumb(this);" href="' + (item.value == '' ? 'javascript:void(0)' : contextPath + '/project/readOnly/' + item.id) + '">' + (item.value == '' ? 0 : item.value) + '项</a></td>';
                    if (i == 9) {
                        desc += '</tr>' + num + '</tr><tr><td>状态</td>';
                        num = '<tr><td>数量</td>';
                    }
                })
                var tds = "";
                for(var i = 0; i < 20-compareData.length; i++) {
                	tds += '<td></td>';
                }
                tds+="</tr>";
                desc += tds;
                var html = (desc + num) + tds;
                $('.data_statistics_table').find('table').html(html)
            },

            creatCharts: function(json) {
                function deepClone(src) {
                    var clone = src;

                    if (src instanceof Date) {
                        clone = new Date(src.getDate());
                        return clone;
                    }
                    if (src instanceof Array) {
                        clone = [];
                        for (var key in src) {
                            clone[key] = deepClone(src[key]);
                        }
                        return clone;
                    }

                    // 对于 Object
                    if (src instanceof Object) {
                        clone = {};
                        for (var key in src) {
                            if (src.hasOwnProperty(key)) { // 忽略掉继承属性
                                clone[key] = deepClone(src[key]);
                            }
                        }
                        return clone;
                    }

                    return src;
                }
                var data = deepClone(json);
                // 路径配置
                require.config({
                    paths: {
                        echarts: contextPath + '/static/js'
                    }
                });
                // 使用
                require(
                    [
                        'echarts',
                        'echarts/chart/pie'
                    ],
                    function(ec) {
                        var myChart = ec.init(document.getElementById('main'));
                        data.map(function(item, i) {
                            delete item.id;
                            delete item.targetUrl;
                            return item.name = item.name + ':' + item.value
                        });

                        var option = {
                            tooltip: {
                                trigger: 'item',
                                formatter: "{a} <br/>{b} : {c} ({d}%)"
                            },
                            toolbox: {
                                show: false,
                                feature: {
                                    mark: {
                                        show: true
                                    },
                                    dataView: {
                                        show: true,
                                        readOnly: false
                                    },
                                    magicType: {
                                        show: true,
                                        type: ['pie', 'funnel']
                                    },
                                    restore: {
                                        show: true
                                    },
                                    saveAsimages: {
                                        show: true
                                    }
                                }
                            },
                            calculable: false,
                            series: [
//                            {
//                                name: '状态分布统计',
//                                type: 'pie',
//                                selectedMode: 'single',
//                                radius: [0, 70],
//
//                                // for funnel
//                                x: '20%',
//                                width: '40%',
//                                funnelAlign: 'right',
//                                max: 1548,
//
//                                itemStyle: {
//                                    normal: {
//                                        label: {
//                                            position: 'inner'
//                                        },
//                                        labelLine: {
//                                            show: false
//                                        }
//                                    }
//                                },
//                                data: []
//                            }, 
                            {
                                name: '状态分布统计',
                                type: 'pie',
                                radius: [50, 160],
                                // for funnel
                                x: '60%',
                                width: '35%',
                                funnelAlign: 'left',
                                max: 1048,
                                data: data
                            }]
                        };

                        // 为echarts对象加载数据 
                        myChart.setOption(option);
                    }
                );
            },
            chart_ajax: function() {
                var _this = this;
                $(_this.searchButton).on('click', function() {
                    function getTimestamp(str) {
                        str = str.replace(/-/g, '/');
                        var date = new Date(str);
                        var sTime = date.getTime();
                        return sTime
                    }
                    var param = {};
//                    param.startCreateTime = getTimestamp($('input[name="startCreateTime"]').val());
//                    param.endCreateTime = getTimestamp($('input[name="endCreateTime"]').val());
                    param.proType = $("select[name='proType']").find("option:selected").val();
                    param.orgIds = $('input[name="orgId"]').val().split(",");
                    var childOrgIdVal = $('input[name="childOrgId"]').val();
                    var groupIdVal = $('input[name="groupId"]').val();
                    param.childOrgIds = childOrgIdVal==undefined?null:childOrgIdVal.split(",");
                    param.groupIds = groupIdVal==undefined?null:groupIdVal.split(",");
                    
                    if($("#orgIdall_select").attr("op") == "no") {
                    	param.orgIds.push("-1");
                    }
                    if($("#childOrgIdall_select").attr("op") == "no") {
                    	param.childOrgIds.push("-1");
                    }
                    if($("#groupIdall_select").attr("op") == "no") {
                    	param.groupIds.push("-1");
                    }
                    
                    $.ajax({
                        type: "POST",
                        url: contextPath + '/tj/status/data',
                        data: JSON.stringify(param),
                        dataType: "json",
                        contentType: "application/json"
                    }).done(function(json) {
                        console.log(JSON.stringify(json))
                        _this.creatCharts(json);
                    }).done(function(data) {
                        _this.createTable(data);
                    })
                })
            }

        };

        new GomeProject_CommonBar({
            'laypage': 'GomeProjectPage',
            'searchButton': '.search_data'
        }).init()
    })
