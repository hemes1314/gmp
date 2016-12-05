$(document).ready(function() {
    function GomeProjectCommonFn(config) {
        if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
        this.leftBar = config.leftBar;
    }

    GomeProjectCommonFn.prototype = {
        init: function() {
            this.layDate();
            this.select_init();
            this.spinner();
            this.limitTextarea();
            this.LeftBarchange();
            return this
        },
        layDate: function() {
        	var start,end;
            $('input[class="laydate-icon"],input[use="laydate"]').each(function() {
                var id = $(this).attr('id');
                var id_arr = [];
                var option = {
                    elem: '#' + id,
                    format: 'YYYY-MM-DD',
                    min: '1990-01-01 23:59:59',
                    istime: true,
                    istoday: true,
                    festival: true//是否显示节日
                };
                if($(this).attr("class").indexOf("now-max")!=-1){
                	option.max = laydate.now();
                }else{
                	option.max = '2099-06-16 23:59:59';
                }
                if (id == 'start') {
                    option.choose = function(datas) {
                        end.min = datas;
                        end.start = datas
                    }
                    start = option;
                } else if (id == 'end') {
                    option.choose = function(datas) {
                        start.max = datas;
                    }
                    end = option;
                };
                laydate(option);
            });
            
            if(start != undefined && end != undefined) {
            	start.choose($("#start").val());
                end.choose($("#end").val());
            }
        },
        select_init: function() {
            var multi_option = {
                zIndex: 1000,
                width: 100,
                height: 30
            };
            $('select[multi="multi"]').each(function() {
                var sel_width = $(this).attr('data-wdith');
                var reg = $(this).attr('reg');
                if (sel_width) {
                    multi_option.width = sel_width
                }
                if (reg) {
                    multi_option.reg = true
                };
                multi_option.defaultValues = 'aaaa_aa';
                $(this).selectlist(multi_option)
            })
        },
        spinner: function() {
            if ($('.clockpicker').length) {
                $('.clockpicker').clockpicker();
            }
        },
        limitTextarea: function() {
            if ($('textarea').length) {
                $('textarea').each(function() {
                    var id = $(this).attr('id');
                    $('#' + id).limitTextarea({
                        maxNumber: 100,
                        position: 'top',
                        onOk: function() {},
                        onOver: function() {}
                    });
                })
            }
        },
        LeftBarchange: function() {
            var _this = this;
            var $leftBar = $(_this.leftBar);
            if ($leftBar.length) {
                $leftBar.on('click', 'li', function() {
                    $(this).find('a').addClass('active').end().siblings().find('a').removeClass('active');
                })
            }
        },
    }

    var CommonFn = new GomeProjectCommonFn({
        'leftBar': '.GomeProjectCommonLeftBarUl',
    }).init();

})



var browserVersion = window.navigator.userAgent.toUpperCase();
var isOpera = false,
    isFireFox = false,
    isChrome = false,
    isSafari = false,
    isIE = false;

function reinitIframe(iframeId, minHeight) {
    try {
        var iframe = document.getElementById(iframeId);
        //iframe.style.height = minHeight + "px";
        var l_frame = document.getElementById("iframe_left");
        var bHeight = 0;
        if (isChrome == false && isSafari == false)
            bHeight = iframe.contentWindow.document.body.scrollHeight;

        var dHeight = 0;
        //var topHeight = 0;
        if (isFireFox == true){
            //dHeight = iframe.contentWindow.document.documentElement.offsetHeight + 2;
        	dHeight = iframe.contentWindow.document.documentElement.offsetHeight;
        }
        else if (isIE == false && isOpera == false){
            dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
        }
        else if (isIE == true && !-[1, ] == false) {} //ie9+
        else
            bHeight += 3;
        
        var height = Math.max(bHeight, dHeight);
        if (height < minHeight) height = minHeight;
        
        var height_content = $(window.frames["iframe_main"].document).find(".GomeProjectContent").height();
        iframe.style.height = height_content + "px";
		l_frame.style.height = height_content + "px";
		$(window.frames["iframe_leftbar"].document).find(".GomeProjectCommonLeftBarUl").height(height_content);
		
		resetDialog();
//        var projectName = $(window.frames["iframe_main"].document).find(".GomeProject_nav_title").text();
//        var tabName = $(window.frames["iframe_main"].document).find(".GomeProject_tab_title").find(".selected a").text();
//        if(projectName == null
//        		|| projectName == undefined
//        		|| projectName.length == 0
//        		|| (projectName.indexOf("项目")==-1
//        		&& projectName.indexOf("创建敏捷")==-1
//        		&& projectName.indexOf("编辑敏捷")==-1)){
//        	if(tabName!=null
//        			&&tabName!=undefined
//        			&&tabName.length != 0
//        			&& tabName.indexOf("日报工时统计")!=-1){
//        		iframe.style.height = height + "px";
//        		l_frame.style.height = height + "px";
//        		$(window.frames["iframe_leftbar"].document).find(".GomeProjectCommonLeftBarUl").height(height);
//        	}else{
//        		iframe.style.height = minHeight + "px";
//        		l_frame.style.height = minHeight + "px";
//        		$(window.frames["iframe_leftbar"].document).find(".GomeProjectCommonLeftBarUl").height(minHeight);
//        	}
//        }else{
//        	iframe.style.height = height + "px";
//        	l_frame.style.height = height + "px";
//        	$('.GomeProjectCommonLeftBarUl').height(height);
//        	$(window.frames["iframe_leftbar"].document).find(".GomeProjectCommonLeftBarUl").height(height);
//        }
        
    } catch (ex) {}
}

Date.prototype.Format = function(fmt)
{ //author: meizz
  var o = {   
    "M+" : this.getMonth()+1,                 //月份
    "d+" : this.getDate(),                    //日
    "h+" : this.getHours(),                   //小时
    "m+" : this.getMinutes(),                 //分
    "s+" : this.getSeconds(),                 //秒
    "q+" : Math.floor((this.getMonth()+3)/3), //季度
    "S"  : this.getMilliseconds()             //毫秒
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}

function startInit(iframeId, minHeight) {
    isOpera = browserVersion.indexOf("OPERA") > -1 ? true : false;
    isFireFox = browserVersion.indexOf("FIREFOX") > -1 ? true : false;
    isChrome = browserVersion.indexOf("CHROME") > -1 ? true : false;
    isSafari = browserVersion.indexOf("SAFARI") > -1 ? true : false;
    if (!!window.ActiveXObject || "ActiveXObject" in window)
        isIE = true;
    window.setInterval("reinitIframe('" + iframeId + "'," + minHeight + ")", 100);
}

startInit('iframe_main', 1000);

function resetDialog(){
	if(typeof(window.frames["iframe_main"]) == "undefined"){
		return;
	}
	var dialog = $(window.frames["iframe_main"].document).find('.ui-popup-focus');
	
	if(typeof($(dialog).css('width')) == "undefined"){
		return;
	}
	if($(dialog).css('width').split('px')[0] < 100){
		return;
	}
	
	$(dialog).css('top',document.body.scrollTop + 120);
}
window.onscroll=resetDialog;
