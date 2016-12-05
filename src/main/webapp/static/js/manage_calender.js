$(document).ready(function() { 
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strMonth = year + '-' + month;
	doAjax(strMonth);
})  
function doAjax(strMonth) {
	var orgIds = $("#search_from input[name='orgIds']").val();
	if(orgIds!="" && typeof orgIds == "undefined"){
		orgIds = $("#search_from #orgIds").find("option:selected").val();
	}
	var childOrgIds = $("#search_from input[name='childOrgIds']").val();
	var groupIds = $("#search_from input[name='groupIds']").val();
	if((orgIds=="" || typeof orgIds == 'undefined' ) && (typeof childOrgIds == 'undefined' || childOrgIds=="") && (typeof groupIds == 'undefined' || groupIds=="")){
    	smoke.alert("请选取组织部门进行日历查询", {ok: "确定"});
    	return;
    }
	var ajax_option = {
            type: "POST",
            url:contextPath + '/calendar/getProInfoForCalendar',
            data: {
            	strMonth: strMonth,
            	orgIds:orgIds,
            	childOrgIds:childOrgIds,
            	groupIds:groupIds
            },
            dataType: "json"
        };
	ajax_option.type = 'POST';
    $.ajax(ajax_option).done(function(data) {
        calender.init(data,strMonth);
    })
}
function CreatCalender(config) {
        this.calendar_table = config.work_hour_table || '';
        this.content_table = config.work_content_table || '';
    }
    CreatCalender.prototype = {
        constructor: CreatCalender,
        create: function() {
            this.getDayObj();
            this.setDate();
            this.setYear();
            this.bind_dom();
            this.Table_op()
        },
        currentMonth: '',
        currentYear: '',
        today_str: '',
        getDayObj: function() {
            var _this = this;
            var today = new Date();
            var thisDate = today.getDate();
            var thisMonth = today.getMonth();
            var thisYear = today.getFullYear();
            var thisDay = today.getDay();
            _this.currentYear = thisYear;
            _this.currentMonth = thisMonth;
            _this.today_str = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
            var Month_InChinese = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
            var events = [];
        },
        setYear: function() {
            var _this = this;
            var selectYear = $('#selectYear');
            selectYear.empty();
            for(var i = 10;i > -2;i--){
            	var optionYear = _this.currentYear - i;
            	if(optionYear == _this.currentYear){
            		selectYear.append('<option value="'+optionYear+'" selected>'+ optionYear + '</option>');
            	} else {
            		selectYear.append('<option value="'+optionYear+'">'+ optionYear + '</option>');
            	}
            }
        },
        setDate: function() {
            var _this = this;
            var month = 1 + _this.currentMonth;
            document.getElementsByClassName('calendar-title-date')[0].innerHTML =  month + "月";
        },
        init: function(data,strMonth) {
            var _this = this;
            var currentYear = _this.currentYear;
            var currentMonth = _this.currentMonth;
            document.getElementById("detail").innerHTML = '';
            var firstDayOfTheMonth = new Date(currentYear, currentMonth, 1);

            //上月日期
            var recentArr = [];
            var recentFirstDay = new Date(currentYear, currentMonth, 1).getDay();
            var recentMonthDate = new Date(currentYear, currentMonth, 0).getDate();
            var lastMonth = new Date(currentYear, currentMonth, 0).getDate();
            if (recentFirstDay == 0) {
                var temp = 7;
            } else {
                var temp = recentFirstDay;
            }
            for (var i = temp + 1; i > 1; i--) {
                recentArr.push({
                    'date': lastMonth - i + 2 + 'disable',
                })
            }
            //得到当月的最大天数
            var tempDate = new Date(currentYear, currentMonth + 1, 0);
            var numOfDays = tempDate.getDate();
            var content = data;
            //增加要显示的天数
            for (var j = 1; j <= numOfDays; j++) {
                recentArr.push({
                    'date': j,
                    'realdate': currentYear + '-' + (currentMonth + 1) + '-' + j
                })
            }
            var len = 42 - recentArr.length;
            for (var i = 1; i <= len; i++) {
                recentArr.push({
                    'date': i + 'disable',
                })
            }
            recentArr.forEach(function(p_item, i) {
                var real_date = p_item['realdate'];
                content.forEach(function(item, i) {
                    if (item.time == real_date) {
                        p_item['content'] = item.name
                    }
                })
            })
            var calender_str = '<thead><tr><th>星期日</th><th>星期一</th><th>星期二</th><th>星期三</th><th>星期四</th><th>星期五</th><th>星期六</th></tr></thead>';
            for (var k = 0; k < recentArr.length; k++) {
                if ((k != 0) && k % 7 == 0) {
                    calender_str += '</tr><tr>'
                }
                if (recentArr[k]['realdate'] == _this.today_str) {
                    calender_str += '<td class="today_selected">'
                } else {
                    calender_str += '<td>'
                }
                var date_info = recentArr[k]['date'].toString();
                if (date_info.indexOf('disable') > 0) {
                    calender_str += '<a class="calendar_date disable">' + recentArr[k]['date'].replace(/disable/, '') + '</a></td>';
                } else {
                    if (recentArr[k]['content']) {
                        calender_str += '<a class="calendar_date" data=' + recentArr[k]['realdate'] + '>' + recentArr[k]['date'];
                        if (recentArr[k]['content'].length == 1 ) {
                        	calender_str += '<div class="bubble top">' + recentArr[k]['content'][0] + '</div>';
                        }else if (recentArr[k]['content'].length == 2 ) {
                        	calender_str += '<div class="bubble top">' + recentArr[k]['content'][0] + '<br>' + recentArr[k]['content'][1] + '</div>';
                        }
                        calender_str += '</a></td>';
                    } else {
                        calender_str += '<a class="calendar_date" data=' + recentArr[k]['realdate'] + '>' + recentArr[k]['date'] + '</a></td>';
                    }

                }
            }
            document.getElementById("calendar").innerHTML = calender_str;
            var todayYMD = _this.today_str.split('-');
            var searchYM = strMonth.split('-');
            if(todayYMD[0] == searchYM[0] && todayYMD[1] == searchYM[1] ){
            	doDetailAjax(_this.today_str);
            }
        },
        bind_dom: function() {
            var _this = this;
            $('.nextMonth').on('click', function() {
                _this.nextMonth(_this)
            });
            $('.lastMonth').on('click', function() {
                _this.lastMonth(_this)
            });
            $('#selectYear').change(function() {
            	_this.changeYear(_this)
            });
        },
        changeYear: function(that) {
        	that.currentYear = $('#selectYear').val();
    		var monthStr = $('#selectYear').siblings(".calendar-title").find(".calendar-title-date").html();
    		that.currentMonth = parseInt(monthStr);
    		var strMonth = that.currentYear + '-' + that.currentMonth;
            doAjax(strMonth);
        },
        lastMonth: function(that) {
            if (that.currentMonth == 0) {
                that.currentMonth = 11;
            } else { that.currentMonth-- };
            that.setDate();
            var strMonth = that.currentYear + '-' + (that.currentMonth +1);
            doAjax(strMonth);
        },
        nextMonth: function(that) {
            if (that.currentMonth == 11) {
                that.currentMonth = 0;
            } else {
                ++that.currentMonth
            };
            that.setDate();
            var strMonth = that.currentYear + '-' + (that.currentMonth + 1);
            doAjax(strMonth);
        },
        Table_op: function() {
            var _this = this;
            $(_this.calendar_table).off('click', 'td').on('click', 'td', function() {
            	$('.click_selected').removeClass('click_selected');
            	$('.today_selected').removeClass('today_selected');
            	$(this).addClass('click_selected');
            	var thisTdChildEleA = $(this).find("a").eq(0);
            	var thisTdChildEleAClass = thisTdChildEleA.attr("class");
            	if(thisTdChildEleAClass!="" && thisTdChildEleAClass.indexOf("disable")==-1 && thisTdChildEleA.children().is("div")){
            		var strDate = thisTdChildEleA.attr('data');
            		doDetailAjax(strDate);
            	}else{
            		doDetailHtml([]);
            	}
            })
        },
    }
    function doDetailAjax(strDate) {
    	var orgIds = $("#search_from input[name='orgIds']").val();
    	var childOrgIds = $("#search_from input[name='childOrgIds']").val();
    	var groupIds = $("#search_from input[name='groupIds']").val();
    	var ajax_option = {
                type: "POST",
                url:contextPath + '/calendar/getDetailInfoForCalendar',
                data: {
                	strDate: strDate,
                	orgIds:orgIds,
                	childOrgIds:childOrgIds,
                	groupIds:groupIds
                },
                dataType: "json"
            };
    	ajax_option.type = 'POST';
        $.ajax(ajax_option).done(function(data) {
        	doDetailHtml(data);
        })
    }
    function doDetailHtml(data) {
    	var detail_str = "";
    	var actionStateTemp = "";
    	var index = 1;
    	for (var i = 0; i < data.length; i++) {
    		item = data[i];
    		var actionState = item.actionState;
            var proId = item.proId;
            var title = item.title;
            if(actionStateTemp != actionState ){
            	if(actionStateTemp != ""){
            		detail_str += '</div>';
            	}
            	actionStateTemp = actionState;
            	index = 1;
            	if(i == 0 ){
        			detail_str += '<div class="infogroup first_group">';
        		} else {
        			detail_str += '<div class="infogroup">';
        		}
            	detail_str += '<div>' + actionStateTemp +'</div>' ;
            }
    		detail_str += '<p>';
    		detail_str += '<a href="javascript:void(0)" onclick="window.location.href=\'';
    		detail_str += contextPath + '/project/detail/'+ proId+ ';return false\'">'+  index +'、' + title +'</a></p>';
    		if(i == data.length - 1 ){
    			detail_str += '</div>';
    		}
    		index += 1;
        }
    	   
       	document.getElementById("detail").innerHTML = detail_str;
    }
    var calender = new CreatCalender({
        'work_hour_table': '#calendar',
        'work_content_table': '#work_hour_content',
    });
    calender.create();

    function importantDay() {
        var month, day, year;
        for (var i = 0; i < events.length; i++) {
            year = parseInt(events[i].date.substring(0, 4));
            month = parseInt(events[i].date.substring(4, 6));
            day = parseInt(events[i].date.substring(6, 8));
            if (year == currentYear && month == currentMonth + 1) {
                var tds = document.getElementsByTagName('td');
                var len = tds.length;
                for (var j = 0; j < len; j++)
                    if (parseInt(tds[j].innerHTML) == day) {
                        tds[j].innerHTML = "<a href='#'>" + day + "</a>";
                        tds[j].style = 'color:#333; border:1px solid gray; ';
                    }
            }
        }
    }

    function sortEvent() {
        var len = events.length;
        for (var i = 0; i < len - 1; i++) {
            for (var j = i + 1; j < len; j++) {
                if (parseInt(events[i].date) > parseInt(events[j].date))
                    swap(events[i], events[j]);
            }
        }
        function swap(a, b) {
            var temp = {
                date: "",
                event: ""
            };
            temp.date = a.date;
            temp.event = a.event;
            a.date = b.date;
            a.event = b.event;
            b.date = temp.date;
            b.content = temp.content;
        }
    }

    function setEvent() {
        var IntToday = parseInt("" + thisYear + (1 + thisMonth) + thisDate);
        var len = events.length;
        for (var i = 0; i < len; i++) {
            if (parseInt(events[i].date) >= IntToday) {
                document.getElementsByClassName('recentEvent-Day')[0].innerHTML = events[i].date.substring(6, 8);
                document.getElementsByClassName('recentEvent-Month')[0].innerHTML = Month_InChinese[parseInt(events[i].date.substring(4, 6)) - 1];
                document.getElementsByClassName('recentEvent-content')[0].innerHTML = '<p><a href="">' + events[i].event + ' >>></a></p>';
                return;
            }
        }
    }
    
var orgOpAry = ['orgIds', 'childOrgIds', 'groupIds'];
var orgObjs = {};
orgOpAry.forEach(function(item,i) {
	var _this = this;
	var thisObj = "#"+item;
	var val_arr = [];
    $(thisObj).find('option').each(function() {
        val_arr.push($(this).val());
    });
	var multi_option = {
		zIndex: 100,
		width: 150,
		height: 28,
		defaultValues:val_arr.join(',')
	};
	orgObjs[i] = JSON.parse(JSON.stringify(multi_option));
	orgObjs[i].onChange = function() {
		if(i < 2){
			var orgIds = $('input[name="' + item + '"]').val().split(',');
			if(orgIds!=""){
				$.post(
					contextPath + '/orgManage/getOrgFramework',
					{orgIds:orgIds},
					function (data){
						render_select(data, '#' + orgOpAry[i + 1], orgObjs[i + 1]);
					}
				);
			}
		}
		CreatCalender.prototype.changeYear(_this);
	}
	var initShow = $(thisObj).attr('initShow');
	if(initShow!=null && initShow=="1"){
		$(thisObj).selectlist(orgObjs[i]);
		var attrName = orgOpAry[i];
		$("#"+attrName+" input[type='button']").eq(0).val("全部");
	}
});

function render_select(datas,orgEle, multiOption){
    var str = '';
    var val_arr = [];
    datas.forEach(function(item) {
        val_arr.push(item.orgId);
        str += '<option value="' + item.orgId + '">' + item.orgName + '</option>';
    });
    multiOption.defaultValues = val_arr.join(',');
    $(orgEle).html(str).selectlist(multiOption);
    $(orgEle + ' input[type="button"]').eq(0).val("全部");
}