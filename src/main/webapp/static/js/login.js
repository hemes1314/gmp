$(function(){ 
	// set login page is top window
	if(top.location!=self.location){
		top.location = self.location;
	}
}); 

function CookieOp(config) {
    if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'array') return;
    this.arr = config;
}

CookieOp.prototype = {
    constructor: CookieOp,
    init: function() {
        this.readCookie();
        this.bindDom();
        this.valid();
        this._resetInputBg();
    },
    _resetInputBg:function(){
    	var tl = setInterval(function(){
    		if($('#username').length > 0){
    			$('#valid_area .inner').css('background-color',$('#username').css('background-color'));
    		}else{
    			clearInterval(tl);
    		}
    		$('#valid_area .inner').css('background-color',$('#username').css('background-color'));
    	},50);
    },
    readCookie: function() {
        var arr = this.arr;
/*        if ($.cookie(arr[0]) == "true") {
            arr.forEach(function(item) {
                $('#' + item).is('input[type="checkbox"]') ? $('#' + item).prop("checked", true) : $('#' + item).val($.cookie(item));
            })
        }*/
    },
    SaveCookie: function(arr) {
        if ($('#' + arr[0]).prop('checked')) {
            arr.forEach(function(item, i) {
                $.cookie(item, ($('#' + item).val() == 'on' ? true : $('#' + item).val()), { expires: 7 })
            })
        } else {
            arr.forEach(function(item) {
                $.cookie(item, ($('#' + item).val() == 'on' ? false : $('#' + item).val()), { expires: -1 })
            })
        }
    },
    bindDom: function() {
        var _this = this;
        var arr = _this.arr;
        $('#' + arr[0]).on('click', function() {
            _this.SaveCookie(arr)
        })
    },
    valid: function() {
        $('#valid_area').validator({
            valid: function(form) {
                $.ajax({
                    url: "/gmp/toLogin",
                    type: 'post',
                    data: {
                        username: $('#username').val(),
                        password: $('#password').val()
                    },
                    dataType: 'json',
                }).done(function(data) {
                    if (data.resultData == 0901000001) {
                        window.location.href = data.targetUrl
                    } else if (data.resultData == 0901000000) {
                    	$('.msg-box').remove();
                        if (data.content == "请输入正确的密码") {
                            if ($('.msg-box').text() == '') {
                                var str = '<span class="msg-box n-right" style="" for="username"><span role="alert" class="msg-wrap n-error"><span class="n-icon"></span><span class="n-msg">请输入正确的密码</span></span></span>';
                                $('#password').after(str)
                            }
                        } else if (data.content == "请输入正确的用户名") {
                            if ($('.msg-box').text() == '') {
                                var str = '<span class="msg-box n-right" style="" for="username"><span role="alert" class="msg-wrap n-error"><span class="n-icon"></span><span class="n-msg">请输入正确的用户名</span></span></span>';
                                $('#username').after(str)
                            }

                        }
                    }
                });
            },
            fields: {
                'username': {
                    rule: "required;",
                    msg: { required: "请输入用户名" },
                },
                'password': {
                    rule: "required;",
                    msg: { required: "请输入密码" },
                }
            }
        }).on("click", "i.login_button", function(e) {
            $(e.delegateTarget).trigger("validate");
        }).on('keydown', function(e) {
            var curKey = e.which;
            if (curKey == 13) {
                $(this).trigger("validate");
            }else{
            	var self = this;
            	setTimeout(function(){
            		$(self).find('.inner').css('background-color',$('#username').css('background-color'));
            	},0);
            }
        }).change(function(e){
        	var self = this;
        	setTimeout(function(){
        		$(self).find('.inner').css('background-color',$('#username').css('background-color'));
        	},0);
        	
        });
    }
}

new CookieOp(['rmbUser', 'username', 'password']).init()
