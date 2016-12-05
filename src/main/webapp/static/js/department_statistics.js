    $(document).ready(function() {
initCrumb($('#unitTjDataCrumbUl'));
        function GomeProject_CommonBar(config) {
            if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
            this.page = config.laypage || "";
            this.searchButton = config.searchButton;
        }

        GomeProject_CommonBar.prototype = {
            constructor: GomeProject_CommonBar,
            init: function() {
                this.chart_ajax();
                this.bindDom();
                $(this.searchButton).click();
                //this.createSelect();
            },
            createSelect: function() {
                var _this = this;
                ['type', 'scheduleId'].forEach(function(item) {
                    _this.all_select('#' + item);
                });
            },
            all_select: function(name) {
                var val_arr = [];
                var _this = this;
                $(name).find('option').each(function() {
                    val_arr.push($(this).val())
                });
                var multi_option = {
                    zIndex: 1000,
                    width: 100,
                    height: 30,
                };
                multi_option.defaultValues = val_arr.join(',');
                $(name).selectlist(multi_option);
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
                var compareData = ['无线开发部', 'ERP系统部', 'EC开发部', 'EC产品部', 'X1', 'X2', '供应链产品部', '平台系统部', 'UED部', 'OA开发组', '虚拟系统部', '搜索部', 'X3', '联盟中心', '金融系统部',
                    '总体规划部', '测试部', '云平台', 'SEO部'
                ];
                var obj = {};
                data.forEach(function(item, i) {
                    obj[item.org_name] = item.org_name;
                });
                var new_arr = [];
                compareData.forEach(function(item, i) {
                    if (!obj[item]) {
                        new_arr.push(item)
                    }
                })
                new_arr.forEach(function(item) {
                    data.push({
                        org_name: item,
                        num: '',
                    })
                })

                function setIndex(obj) {
                    switch (obj.name) {
                        case '无线开发部':
                            obj.sort = 1;
                            break;
                        case 'ERP系统部':
                            obj.sort = 2;
                            break;
                        case 'EC开发部':
                            obj.sort = 3;
                            break;
                        case 'EC产品部':
                            obj.sort = 4;
                            break;
                        case 'X1':
                            obj.sort = 5;
                            break;
                        case 'X2':
                            obj.sort = 6;
                            break;
                        case '供应链产品部':
                            obj.sort = 7;
                            break;
                        case '平台系统部':
                            obj.sort = 8;
                            break;
                        case 'UED部':
                            obj.sort = 9;
                            break;
                        case 'OA开发组':
                            obj.sort = 10;
                            break;
                        case '虚拟系统部':
                            obj.sort = 11;
                            break;
                        case '搜索部':
                            obj.sort = 12;
                            break;
                        case 'X3':
                            obj.sort = 13;
                            break;
                        case '联盟中心':
                            obj.sort = 14;
                            break;
                        case '金融系统部':
                            obj.sort = 15;
                            break;
                        case '总体规划部':
                            obj.sort = 16;
                            break;
                        case '测试部':
                            obj.sort = 17;
                            break;
                        case '云平台':
                            obj.sort = 18;
                            break;
                        case 'SEO部':
                            obj.sort = 19;
                            break;
                    }
                }

                data.forEach(function(item) {
                    setIndex(item)
                })

                data.sort(function(a, b) {
                    return a.sort - b.sort
                });
                var html = '';
                var month = '<tr><td url="javascript:;">部门</td>';
                var proNum = '<tr><td url="javascript:;">数量</td>';
                var url = '';
                //startUptime endUptime proType scheduleId
                var starttime = $('#start').val() || '';
                var endtime =  $('#end').val() || '';
                var scheduleId = param.scheduleId || '';
                var proType = param.proType || '';
                data.forEach(function(item, i) {
                	if(item.num && item.num > 0){
                		 month += '<td url="javascript:;">' + item.org_name + '</td>';
                         url = contextPath + '/datas/toUnit/?startTime=' + starttime + '&endTime=' + endtime + '&scheduleId=' + scheduleId + '&proType=' + proType + '&orgIds=' + item.orgId + '&queryType=2';
                         proNum += '<td url=' + url + '><a href="javascript:void(0)">' + item.num + '项</a></td>';
                         if (i == 9) {
                             month += '</tr>' + proNum + '<tr><td>部门</td>';
                             proNum = '<tr><td>数量</td>';
                         }
                	}
                   
                });
                month += '</tr>';
                proNum += '</tr>';

                $('.data_statistics_table').find('tbody').html(month + proNum);
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
                        var new_data = data.map(function(item, i) {
                            var name = item.org_name;
//                            var reg = /(\d+)/;
//                            
//                            if(name.match(reg) == null){
//                                return { name: "", value: "" }
//                            }else{
//                            	var capture = name.match(reg)[0];
//                                name = name.replace(reg, '');
//                                name = (capture + '_' + name + ':' + item.num);
//                                return { name: name, value: item.num }
//                            }
//                            
                            return { name: name + ':' + item.num, value: item.num }
                            
                        });
                        console.log(new_data);
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
                            series: [{
                                name: '部门项目统计',
                                type: 'pie',
                                selectedMode: 'single',
                                radius: [0, 70],

                                // for funnel
                                x: '20%',
                                width: '40%',
                                funnelAlign: 'right',
                                max: 1548,

                                itemStyle: {
                                    normal: {
                                        label: {
                                            position: 'inner'
                                        },
                                        labelLine: {
                                            show: false
                                        }
                                    }
                                },
                                data: []
                            }, {
                                name: '部门项目统计',
                                type: 'pie',
                                radius: [50, 160],

                                // for funnel
                                x: '60%',
                                width: '35%',
                                funnelAlign: 'left',
                                max: 1048,

                                data: new_data
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
                    param.startTime = getTimestamp($('input[name="startTime"]').val());
                    param.endTime = getTimestamp($('input[name="endTime"]').val());
                    param.proType = $("select[name='proType']").find("option:selected").val();
                    param.scheduleId = $('select[name="scheduleId"]').find("option:selected").val();
                    $.ajax({
                        type: "POST",
                        url: contextPath + '/datas/unitTJ',
                        data: JSON.stringify(param),
                        dataType: "json",
                        contentType: "application/json"
                    }).done(function(json) {
                        _this.creatCharts(json);
                    }).done(function(data) {
                        _this.createTable(data, param);
                    })
                })
            },
            bindDom: function() {
                $('.data_statistics_table').on('click', 'td', function() {
                    var url = $(this).attr('url');
var orgTxt  = $(this).closest('tr').prev().children().eq($(this).index()).html();
                parent.pushNavStack(orgTxt);
                    window.location.href = url;
                })
            }

        };

        new GomeProject_CommonBar({
            'laypage': 'GomeProjectPage',
            'searchButton': '.search_data'
        }).init()
    })
