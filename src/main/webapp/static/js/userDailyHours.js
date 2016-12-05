
function getDailyHoursSchedule(date) {
    $('#work_hour_content').hide("slow");
	var userTid = $("#dailyUserId").val();
	var ajax_option = {
            type: "POST",
            url:contextPath + '/daily/getDailyHoursSchedule',
            data: {targetDate:date,userTid:userTid},
            dataType: "json"
        };
    $.ajax(ajax_option).done(function(data) {
    	if(data!=null && data.code=='success'){
    		var datas = data["data"];
    		var sysTime = datas.sysDate;
    		var dateRange = datas.dateRange;
    		var dataList = datas.dataList;
    		var todayDateMs = new Date(sysTime.split(" ")[0].replace(/-/g,"/")).getTime();
    		var tomorrowMs = todayDateMs+24*60*60*1000;
    		var startDateStr = dateRange.split(';')[0];
    		var endDateStr = dateRange.split(';')[1];
    		var startRange = startDateStr.split('-');
            var endRange = endDateStr.split('-');
            var lastMonthEndSunday = new Date(startDateStr.replace(/-/g,"/"));
    		var currentYear = lastMonthEndSunday.getFullYear();
    		var currentMonth = lastMonthEndSunday.getMonth()+1;
    		var firstDayOfTheMonth = new Date(currentYear, currentMonth, 1);
            //上月日期
            var recentArr = [];
            var recentFirstDay = new Date(currentYear, currentMonth, 1).getDay();
            var recentMonthDate = new Date(currentYear, currentMonth, 0).getDate(); 
            var lastMonth = new Date(currentYear, currentMonth, 0).getDate();
            var temp;
            if (recentFirstDay == 0) {
                temp = 7;
            } else {
                temp = recentFirstDay;
            }
            for (var i = temp + 1; i > 1; i--) {
            	var content = '0';
            	for (var index = 0; index < dataList.length; index++) {
            		var actionDate = dataList[index].actionDate;
            		var actionDate1 = startRange[0] + '-' + parseInt(startRange[1]) + '-' + (lastMonth - i + 2);
            		if(actionDate == actionDate1){
            			content = dataList[index].hours;
            			break;
            		}
            	}
            	var actionTime = new Date(startRange[0],(startRange[1] -1),(lastMonth - i + 2)).getTime();
            	if (tomorrowMs > actionTime){
            		recentArr.push({
                        'date': lastMonth - i + 2 + 'disable',
                        'content': content + 'h',
                        'readDate' :startRange[0] + '-' + startRange[1] + '-' + (lastMonth - i + 2)
                    })
            	}else {
            		recentArr.push({
                        'date': lastMonth - i + 2 + 'disable',
                        'content': '-',
                        'readDate' :startRange[0] + '-' + startRange[1] + '-' + (lastMonth - i + 2)
                    })
            	}
            }
            //得到当月的最大天数
            var tempDate = new Date(currentYear, currentMonth + 1, 0);
            var numOfDays = tempDate.getDate();
            //增加要显示的天数
            for (var j = 1; j <= numOfDays; j++) {
            	var content = '0';
            	for (var index = 0; index < dataList.length; index++) {
            		var actionDate = dataList[index].actionDate;
            		var actionDate1 = currentYear + '-' + (currentMonth + 1) + '-' + j;
            		if(actionDate == actionDate1){
            			content = dataList[index].hours;
            			break;
            		}
            	}
            	var actionTime = new Date(currentYear,currentMonth,j).getTime();
            	if (tomorrowMs > actionTime){
            		if((tomorrowMs - 24*60*60*1000) <= actionTime && content == "0"){
            			recentArr.push({
                            'date': j,
                            'content': '-',
                            'readDate' :currentYear + '-' + (currentMonth + 1) + '-' + j
                        })
            		} else {
	        			 recentArr.push({
	                         'date': j,
	                         'content': content + 'h',
	                         'readDate' :currentYear + '-' + (currentMonth + 1) + '-' + j
	                     })
            		}
            	}else {
            		 recentArr.push({
                         'date': j,
                         'content': '-',
                         'readDate' :currentYear + '-' + (currentMonth + 1) + '-' + j
                     })
            	}
            }
            var len = 42 - recentArr.length;
            for (var i = 1; i <= len; i++) {
            	var content = '0';
            	for (var index = 0; index < dataList.length; index++) {
            		var actionDate = dataList[index].actionDate;
            		var actionDate1 = endRange[0] + '-' + endRange[1] + '-' + i;
            		if(actionDate == actionDate1){
            			content = dataList[index].hours;
            			break;
            		}
            	}
            	var actionTime = new Date(endRange[0],(endRange[1] - 1),i).getTime();
            	if (tomorrowMs > actionTime){
        		   recentArr.push({
                       'date': i + 'disable',
                       'content': content + 'h',
                       'readDate' :endRange[0] + '-' + endRange[1] + '-' + i
                   })
            	}else {
        		   recentArr.push({
                       'date': i + 'disable',
                       'content': '-',
                       'readDate' :endRange[0] + '-' + endRange[1] + '-' + i
                   })
            	}
            }
            var calender_str = '';
            for (var k = 0; k < recentArr.length; k++) {
                if ((k != 0) && k % 7 == 0) {
                    calender_str += '</tr><tr>'
                }
                calender_str += '<td>'
                var date_info = recentArr[k]['date'].toString();
                if (date_info.indexOf('disable') > 0) {
                    calender_str += '<a class="calendar_date disable" userTid="'+userTid+'" data="'+recentArr[k].readDate+'">' + recentArr[k].date.replace(/disable/, '') + '</a><a class="calendar_content">' + recentArr[k].content + '</a></td>';
                } else {
                    calender_str += '<a class="calendar_date" userTid="'+userTid+'"  data="'+recentArr[k].readDate+'">' + recentArr[k].date + '</a><a class="calendar_content">' + recentArr[k].content + '</a></td>';
                }
            }
            document.getElementById("calendar").innerHTML = "";
            document.getElementById("calendar").innerHTML = calender_str;
    	}
    })
}

function CreatCalender(config) {
    this.calendar_table = config.work_hour_table || '#calendar';
    this.content_table = config.work_content_table || '#work_hour_content';
}

CreatCalender.prototype = {
    constructor: CreatCalender,
    create: function(data,dateRange) {
        this.getDayObj();
        this.setDate();
        this.setYear();
        this.bind_dom();
        this.Table_op();
    },
    currentMonth: '',
    currentYear: '',
    getDayObj: function() {
        var _this = this;
        var today = new Date();
        _this.currentYear = today.getFullYear();
        _this.currentMonth = today.getMonth();
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
        document.getElementsByClassName('calendar-title-date')[0].innerHTML = month + "月";
    },
    bind_dom: function() {
        var _this = this;
        $('.nextMonth').on('click', function() {
            _this.nextMonth(_this);
        });
        $('.lastMonth').on('click', function() {
            _this.lastMonth(_this);
        });
        $('#selectYear').change(function() {
        	_this.changeYear(_this);
        });
    },
    changeYear: function(that) {
    	that.currentYear = $('#selectYear').val();
        var date = new Date(that.currentYear,that.currentMonth,1).Format("yyyy-MM-dd");
        getDailyHoursSchedule(date);
    },
    lastMonth: function(that) {
        if (that.currentMonth == 0) {
            that.currentMonth = 11;
        } else { that.currentMonth-- };
        that.setDate();
        var date = new Date(that.currentYear,that.currentMonth,1).Format("yyyy-MM-dd");
        getDailyHoursSchedule(date);
    },
    nextMonth: function(that) {
        if (that.currentMonth == 11) {
            that.currentMonth = 0;
        } else {
            ++that.currentMonth
        };
        that.setDate();
        var date = new Date(that.currentYear,that.currentMonth,1).Format("yyyy-MM-dd");
        getDailyHoursSchedule(date);
    },
    Table_op: function() {
        var _this = this;
        $(_this.calendar_table).off('click', 'td').on('click', 'td', function(e) {
        	if($(this).attr('class') == 'selected'){
        		$(this).removeClass('selected');
        	} else {
        		$(this).addClass('selected');
        	}
        	var tdList = $(_this.calendar_table).find('.selected');
        	var strDateList = "";
        	for (var i=0; i< tdList.length;i++){
        		strDateList += $(tdList[i]).find("a").eq(0).attr('data');
        		if(i != tdList.length-1 ){
        			strDateList +=  ",";
        		}
        	}
        	var userTid = $(tdList[0]).find("a").eq(0).attr('userTid');
        	if(strDateList.length > 0){
            	var ajax_option = {
                        type: "POST",
                        url:contextPath + '/daily/getDailyDetailInfo',
                        data: {
                        	dailyDates:strDateList,
                        	userTid:userTid
                        },
                        dataType: "json"
                    };
                $.ajax(ajax_option).done(function(data) {
                	$('#work_hour_content').show("slow");
                	doDetailHtml(data);
                })
        	} else {
        		doDetailHtml(strDateList);
        	}
        })
    }
}
 
function doDetailHtml(data) {
	var detail_str = '';
	if(data.length > 0){
		detail_str += '<thead><tr><td>日期</td><td>类型</td><td>工作内容</td><td>关联需求</td><td>工时统计</td><td>更新时间</td><td>备注</td></tr></thead>';
		detail_str += '<tbody id="calender_tab">';
	}
	for (var i = 0; i < data.length; i++) {
		item = data[i];
		var childList = data[i].gomeGmpResDailyDetailVOList;
		detail_str += '<tr>';
		detail_str += '<td rowspan="' + childList.length + '"> ' + item.dailyDate + '</td>';
		for (var j = 0; j < childList.length; j++){
			var childItem = childList[j];
			if(j != 0){
				detail_str += '<tr>';
			}
			detail_str += '<td>'+childItem.itemName+'</td>';
			detail_str += '<td>'+childItem.startPoint + '-'+ childItem.endPoint +' '+ childItem.workContent +'</td>';
			detail_str += '<td>'+childItem.title+'</td>';
			detail_str += '<td>'+childItem.hours+ 'h' + '</td>';
			detail_str += '<td>'+childItem.updatePoint+'</td>';
			detail_str += '<td></td>';
			if(j != 0){
				detail_str += '</tr>';
			}
		}
		detail_str += '</tr>';
    }
	if(data.length > 0){
		detail_str += '</tbody>'
	}
   	document.getElementById("work_hour_content").innerHTML = detail_str;
	if($('#work_hour_content tbody').length > 0){
    	setTimeout(function(){
    		$('#page')[0].scrollIntoView();
    	},100);
	}
}

new CreatCalender({'work_hour_table': '#calendar','work_content_table': '#work_hour_content'}).create();