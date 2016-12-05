    $(document).ready(function() {
initCrumb($('#onLineDataCrumbUl'));
        function GomeProject_CommonBar(config) {
            if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
            this.page = config.laypage || "";
            this.searchButton = config.searchButton;
        }

        GomeProject_CommonBar.prototype = {
            constructor: GomeProject_CommonBar,
            init: function() {
                this.chart_ajax();
                this.createSelect();
                this.changeSelect();
                this.bindDom();
            },
            autoClick: function() {
                var _this = this;
                $(_this.searchButton)[0].click();
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
                var op_arr = ['orgIds', 'childOrgIds', 'groupIds'];
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
                    _this.render_select(data, '#orgIds', obj[0])
                }).then(function() {
                    _this.autoClick();
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
            createTable: function(json, param) {
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
                var year = data[0].onlineYear;
                var compareData = [];
                for (var i = 1; i < 13; i++) {
                    compareData.push(year + '-' + (i < 10 ? ('0' + i) : i))
                }
                var obj = {};
                data.forEach(function(item, i) {
                    obj[item.onlineMonth] = item.onlineMonth;
                });
                var new_arr = [];
                compareData.forEach(function(item, i) {
                    if (!obj[item]) {
                        new_arr.push(item)
                    }
                })
                new_arr.forEach(function(item) {
                    data.push({
                        onlineMonth: item,
                        cnt: '0',
                        onlineYear: year
                    })
                })
                data.sort(function(a, b) {
                    return a.onlineMonth.split('-')[1] - b.onlineMonth.split('-')[1]
                });
                var html = '';
                var month = '<tr><td>月份</td>';
                var proNum = '<tr><td>数量</td>';
                var url = '';
                var orgIds = param.orgIds.join(',') || '';
                /*if($("#orgIdall_select").attr("op") == "no") {
                	orgId+=",-1";
                }
                if($("#childOrgIdall_select").attr("op") == "no") {
                	childOrgId+=",-1";
                }
                if($("#groupIdall_select").attr("op") == "no") {
                	//param.groupId.push("-1");
                	groupId+=",-1";
                }*/
                var childOrgIds = param.childOrgIds ? param.childOrgIds.join(',') : '';
                var groupIds = param.groupIds ? param.groupIds.join(',') : '';
                var proType = param.proType;
               
                data.forEach(function(item, i) {
                    month += '<td>' + item.onlineMonth + '</td>';
                    url = contextPath + '/datas/toUnit/?month=' + item.onlineMonth + '&orgIds=' + orgIds + '&childOrgIds=' + childOrgIds + '&groupIds=' + groupIds + '&proType=' + proType + '&queryType=1';
                    proNum += '<td url=' + url + '><a href="javascript:void(0)">' + item.cnt + '项</a></td>';
                    if (i == 5) {
                        month += '</tr>' + proNum + '<tr><td>月份</td>';
                        proNum = '<tr><td>数量</td>';
                    }
                })
                month += '</tr>';
                proNum += '</tr>';
               
                $('.data_statistics_project_num_table').find('tbody').html(month + proNum)
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
                var x_data = data.map(function(item) {
                    return item.onlineMonth
                });
                var charts_data = data.map(function(item) {
                    return item.cnt
                });
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
                        'echarts/chart/pie',
                        'echarts/chart/bar'
                    ],
                    function(ec) {
                        // 基于准备好的dom，初始化echarts图表
                        var myChart = ec.init(document.getElementById('main'));
                        var option = {
                            title: {
                                text: '',
                                sublink: '',
                                x: '50'
                            },
                            legend: {
                                selectedMode: false,
                                data: ['项目上线分布统计']
                            },
                            calculable: true,
                            grid: {
                                borderWidth: 0,
                                y: 80,
                                y2: 60
                            },
                            xAxis: [{
                                type: 'category',
                                splitLine: {
                                    show: false
                                },
                                data: x_data
                            }],
                            yAxis: [{
                                type: 'value',
                                boundaryGap: [0, 0.6],
                   
                                max:1
                            }],
                            series: [{
                                name: '项目上线分布统计',
                                type: 'bar',
                                stack: 'sum',
                                barCategoryGap: '50%',
                                itemStyle: {
                                    normal: {
                                        color: '#8085E8',
                                        barBorderColor: '#8085E8',
                                        barBorderWidth: 0,
                                        barBorderRadius: 0,
                                        label: {
                                            show: true,
                                            position: 'insideTop'
                                        }
                                    }
                                },
                                data: charts_data
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
                    var param = {};
                    param.proType = $("select[name='proType']").find("option:selected").val();
                    param.year = $("select[name='year']").find("option:selected").val();
                    $('form').find('input[type="hidden"]').each(function() {
                        param[$(this).attr('name')] = $(this).val().split(',');
                    });
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
                        url: contextPath + '/datas/onlineTJ',
                        data: JSON.stringify(param),
                        dataType: "json",
                        contentType: "application/json"
                    }).done(function(json) {
                        _this.creatCharts(json)
                    }).done(function(data) {
                        _this.createTable(data, param);
                    }).then(function() {
                        _this.export(param);
                    })
                })
            },
             
            export: function(param) {
                console.log(param);
                $('.export_data').attr('href', contextPath + '/datas/onlineTJExport?year=' + param.year + '&proType=' + param.proType + '&orgIds=' + param.orgIds)
            },
            changeSelect: function() {
                var NowYear = new Date().getFullYear()
                $('select[name="year"]').find('option').each(function() {
                    if ($(this).val() == NowYear) {
                        $(this).prop("selected", true)
                    }
                })
            },
            bindDom: function() {
                $('.data_statistics_project_num_table').on('click', 'td', function() {
                    var url = $(this).attr('url');
                    if(typeof(url) == 'undefined'){
                    	return;
                    }
 // add crumb start
                var monthTxt  = $(this).closest('tr').prev().children().eq($(this).index()).html();
                parent.pushNavStack(monthTxt);
                // add crumb end
                    window.location.href = url;
                })
            }

        };

        new GomeProject_CommonBar({
            'laypage': 'GomeProjectPage',
            'searchButton': '.search_data'
        }).init()
    })
