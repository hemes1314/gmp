$(function() {
    var xOffset = -20;
    var yOffset = 20;
    $("[reg],[url]:not([reg]),[tip]").hover(
        function(e) {
            if ($(this).attr('tip') != undefined) {
                var top = (e.pageY + yOffset);
                var left = (e.pageX + xOffset);
                $('body').append('<p id="vtip"><img id="vtipArrow" src=' + contextPath + '/static/images/vtip_arrow.png>' + $(this).attr('tip') + '</p>');
                $('p#vtip').css("top", top + "px").css("left", left + "px");
                $('p#vtip').bgiframe();
            }
        },
        function() {
            if ($(this).attr('tip') != undefined) {
                $("p#vtip").remove();
            }
        }
    ).mousemove(
        function(e) {
            if ($(this).attr('tip') != undefined) {
                var top = (e.pageY + yOffset);
                var left = (e.pageX + xOffset);
                $("p#vtip").css("top", top + "px").css("left", left + "px");
            }
        }
    ).blur(function() {
        if ($(this).attr("url") != undefined) {
            ajax_validate($(this));
        } else if ($(this).attr("reg") != undefined) {
            validate($(this));
            console.log('do')
        }
    });


    $('[type="ajax"]').click(function() {
        var isSubmit = true;
        var $that = $(this);
        $('form').find("[reg],[url]:not([reg])").each(function() {
            if ($(this).attr("reg") == undefined) {
                if (!ajax_validate($(this))) {
                    isSubmit = false;
                }
            } else {
                if (!validate($(this))) {
                    isSubmit = false;
                }
            }
        });
        if (typeof(isExtendsValidate) != "undefined") {
            if (isSubmit && isExtendsValidate) {
                return extendsValidate();
            }
        }
        if (!isSubmit) {
            $that.attr('submit', 'sub_false')
        } else {
            $that.attr('submit', '')
        }
        return isSubmit;
    })
});

function mbStringLength(s) {
    var totalLength = 0;
    var i;
    var charCode;
    for (i = 0; i < s.length; i++) {
        charCode = s.charCodeAt(i);
        if (charCode < 0x007f) {
            totalLength = totalLength + 1;
        } else if ((0x0080 <= charCode) && (charCode <= 0x07ff)) {
            totalLength += 2;
        } else if ((0x0800 <= charCode) && (charCode <= 0xffff)) {
            totalLength += 3;
        }
    }
    return totalLength;
}

function validate(obj) {
    var reg = obj.attr('reg');
    var multi_reg = obj.attr('multi_reg');
    if (reg) {
        if (/maxLen/.test(reg)) {
            //var len = mbStringLength(obj.text());
            var len = obj.text().length;
            var max = reg.split('=')[1];
            if (len >= 500) {
                change_error_style(obj, "add");
                obj.text(obj.text().substring(0, 500));
                change_tip(obj, null, "remove");
                return false;
            } else {
                console.log('no');
                change_error_style(obj, "remove");
                change_tip(obj, null, "remove");
                return true;
            }
        } else {
            if (reg == 'required') {
                var objValue = obj.val();
                if (objValue === '') {
                    change_error_style(obj, "add");
                    change_tip(obj, null, "remove");
                    return false;
                } else {
                    change_error_style(obj, "remove");
                    change_tip(obj, null, "remove");
                    return true;
                }
            } else {
            	// 如果不填则校验通过 wubin 2016/05/17 begin
            	if(obj.val() == null || obj.val() == "") {
            		return true;
            	}
            	// 如果不填则校验通过 wubin 2016/05/17 end
                reg = new RegExp(obj.attr("reg"));
                var objValue = obj.val() || (obj.find('input[type="hidden"]').val());
                if (!reg.test(objValue)) {
                    change_error_style(obj, "add");
                    change_tip(obj, null, "remove");
                    return false;
                } else {
                    if (obj.attr("url") == undefined) {
                        change_error_style(obj, "remove");
                        change_tip(obj, null, "remove");
                        return true;
                    } else {
                        return ajax_validate(obj);
                    }
                }
            }

        }
    }
}

function ajax_validate(obj) {
    var url_str = obj.attr("url");
    if (url_str.indexOf("?") != -1) {
        url_str = url_str + "&" + obj.attr("name") + "=" + obj.val();
    } else {
        url_str = url_str + "?" + obj.attr("name") + "=" + obj.val();
    }
    var feed_back = $.ajax({ url: url_str, cache: false, async: false }).responseText;
    feed_back = feed_back.replace(/(^\s*)|(\s*$)/g, "");
    if (feed_back == 'success') {
        change_error_style(obj, "remove");
        change_tip(obj, null, "remove");
        return true;
    } else {
        change_error_style(obj, "add");
        change_tip(obj, feed_back, "add");
        return false;
    }
}

function change_tip(obj, msg, action_type) {

    if (obj.attr("tip") == undefined) {
        obj.attr("is_tip_null", "yes");
    }
    if (action_type == "add") {
        if (obj.attr("is_tip_null") == "yes") {
            obj.attr("tip", msg);
        } else {
            if (msg != null) {
                if (obj.attr("tip_bak") == undefined) {
                    obj.attr("tip_bak", obj.attr("tip"));
                }
                obj.attr("tip", msg);
            }
        }
    } else {
        if (obj.attr("is_tip_null") == "yes") {
            obj.removeAttr("tip");
            obj.removeAttr("tip_bak");
        } else {
            obj.attr("tip", obj.attr("tip_bak"));
            obj.removeAttr("tip_bak");
        }
    }
}

function change_error_style(obj, action_type) {
    if (action_type == "add") {
        obj.addClass("input_validation-failed");
    } else {
        obj.removeClass("input_validation-failed");
    }
}

$.fn.validate_callback = function(msg, action_type, options) {
    this.each(function() {
        if (action_type == "failed") {
            change_error_style($(this), "add");
            change_tip($(this), msg, "add");
        } else {
            change_error_style($(this), "remove");
            change_tip($(this), null, "remove");
        }
    });
};
