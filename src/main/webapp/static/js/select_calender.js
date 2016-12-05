$(document).ready(function() { 
	doAjax(null);
})

function doAjax(date) {
	var ajax_option = {
            type: "POST",
            url:contextPath + '/daily/getDailyHoursSchedule',
            data: {targetDate:date},
            dataType: "json"
        };
    $.ajax(ajax_option).done(function(data) {
        calender.init(data);
    })
}

function formatDate(number) {
	if (number >= 1 && number <= 9) {
		number = "0" + number;
	}
	return number;
}
function CreatCalender(config) {
        this.calendar_table = config.work_hour_table || '';
        this.content_table = config.work_content_table || '';
    }
    CreatCalender.prototype = {
        constructor: CreatCalender,
        create: function(data,dateRange) {
            this.getDayObj();
            this.setDate();
            this.setYear();
            this.bind_dom();
            this.Table_op()
        },
        currentMonth: '',
        currentYear: '',
        getDayObj: function() {
            var _this = this;
            var today = new Date();
            var thisDate = today.getDate();
            var thisMonth = today.getMonth();
            var thisYear = today.getFullYear();
            var thisDay = today.getDay();
            _this.currentYear = thisYear;
            _this.currentMonth = thisMonth;
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
            document.getElementsByClassName('calendar-title-date')[0].innerHTML = month + "月";
        },
        init: function(data) {
        	if(data!=null && data.code=='success'){
        		var datas = data["data"];
        		var sysTime = datas.sysDate;
        		var dateRange = datas.dateRange;
        		var dataList = datas.dataList;
        		var today = new Date(sysTime);
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
        		var strHTML = "<tr><th>日</th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th>六</th></tr>";
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
        				calender_str += '<a class="calendar_date disable" data="'+recentArr[k].readDate+'">' + recentArr[k].date.replace(/disable/, '') + '</a><a class="calendar_content">' + recentArr[k].content + '</a></td>';
        			} else {
        				calender_str += '<a class="calendar_date" data="'+recentArr[k].readDate+'">' + recentArr[k].date + '</a><a class="calendar_content">' + recentArr[k].content + '</a></td>';
        			}
        		}
        		strHTML += "</tr>";
        		document.getElementById("calendar").innerHTML = calender_str;
        		document.getElementById("work_hour_content").innerHTML = '';
        	}
        },
        bind_dom: function() {
            var _this = this;
            $('.nextMonth').on('click', function() {
                _this.nextMonth(_this)
            })
            $('.lastMonth').on('click', function() {
                _this.lastMonth(_this)
            })
            $('#selectYear').change(function() {
            	_this.changeYear(_this)
            })
        },
        changeYear: function(that) {
        	that.currentYear = $('#selectYear').val();
            var date = new Date(that.currentYear,that.currentMonth,1).Format("yyyy-MM-dd");
            doAjax(date);
        },
        lastMonth: function(that) {
            if (that.currentMonth == 0) {
                that.currentMonth = 11;
            } else { that.currentMonth-- };
            that.setDate();
            var date = new Date(that.currentYear,that.currentMonth,1).Format("yyyy-MM-dd");
            doAjax(date);
        },
        nextMonth: function(that) {
            if (that.currentMonth == 11) {
                that.currentMonth = 0;
            } else {
                ++that.currentMonth
            };
            that.setDate();
            var date = new Date(that.currentYear,that.currentMonth,1).Format("yyyy-MM-dd");
            doAjax(date);
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
            	doDetailAjax(strDateList);
            })
        },
    }
    
    
    function doDetailAjax(strDateList) {
    	if(strDateList.length > 0){
        	var ajax_option = {
                    type: "POST",
                    url:contextPath + '/daily/getDailyDetailInfo',
                    data: {
                    	dailyDates:strDateList
                    },
                    dataType: "json"
                };
        	ajax_option.type = 'POST';
            $.ajax(ajax_option).done(function(data) {
            	$('#work_hour_content').show();
            	doDetailHtml(data);
            })
    	} else {
    		doDetailHtml(strDateList);
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
                document.getElementsByClassName('recentEvent-Month')[0].innerHTML =
                    Month_InChinese[parseInt(events[i].date.substring(4, 6)) - 1];
                document.getElementsByClassName('recentEvent-content')[0].innerHTML =
                    '<p><a href="">' + events[i].event + ' >>></a></p>';
                return;
            }
        }
    }
