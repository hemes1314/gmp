;
(function($, window, document) {
    var pluginName = 'selectlist',
        defaults = {
            defaultValues: '', //默认选中值','分割
            zIndex: 2,
            width: 220,
            height: 20,
            triangleSize: 6, //右侧小三角大小
            triangleColor: '#333', //右侧小三角颜色
            border: null, //选择列表边框
            borderRadius: 2, //选择列表圆角
            topPosition: false, //选择列表项在列表框上部显示,默认在下边显示
            showMaxHeight: null, //选择列表显示最大高度
            optionHeight: 34, //选择列表单项高度
            speed: 100, //选择列表框显示动画速度（毫秒）
            optionNumCount: 8, //设置ul默认高度5个option单位高度
            wrapClassName: 'myMulSelect', //包装类别
            onChange: function() {},
            reg: false, //自定义模拟选择列表项change事件 
            defalutName: null
        };

    function SelectList(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    SelectList.prototype = {
        init: function() {
            var that = this, //调用元素本身
                element = this.element, //指的是元素
                //获得元素的内容
                $this = $(element);
            selectHTML = that.renderSelect($this);
            //替换元素内容
            $(element).replaceWith(selectHTML); //替换原素源码
            that.setSelectStyle($this); //设置样式
            that.setSelectEvent($this); //绑定事件
            that.setSelectValues($this); //设置默认值
        },
        //设置值
        setSelectValues: function(element) {
            var that = this,
                $this = $(element),
                defaultValues = that.settings.defaultValues,
                selectID = '#' + that.getSelectID($this),

                selectedValue = that.getSelectedOptionValue($this), //默认首个值 
                selectedOptionText = that.getSelectedOptionText($this); //默认首个值文本

            selectBtn = $(selectID + ' input[type="button"]').eq(0); //按钮对象
            selectValues = $(selectID + ' input[type="hidden"]').eq(0); //值域对象

            if (defaultValues) {
            	//有值为准
            	$(selectValues).val(defaultValues.toString());
            } else {
                //默认值
                defaultValues = selectedValue;
            }
            
            //TODO 获得值对应的所有文本域
            var data_text_arry = [];
            $(selectID + ' ul li').each(function() {
                var thistarget = this;
                var thislival = $(this).attr("data-value");
                //var thislitext = $.trim($(this).text());
                var thislitext = $(this).find("span").eq(1).text();
                if (thislival) {
                    $.each(defaultValues.split(","), function(n, val) {
                        if (thislival == val) {
                            //选中
                            /* $(thistarget).addClass('selected');
                            var checkBoxObj = $(thistarget).find('span').eq(0);
                            if(checkBoxObj){
                                $(checkBoxObj).html("√");
                            } */
                            data_text_arry.push(thislitext);
                        } else {
                            //清空选中样式
                            $(thistarget).removeClass('selected');
                            var checkBoxObj = $(thistarget).find('span').eq(0);
                            if (checkBoxObj) {
                                $(checkBoxObj).html("");
                            }
                        }
                    });
                }
            });
            //默认选中select选中项
            /*  selectedOptionText = that.getSelectedOptionText($this),
             selectedValueTemp = that.getSelectedOptionValue($this);
             selectValues.val(selectedValueTemp);
             data_text_arry.push(selectedOptionText); */
            //设置按钮值
            var btnText = data_text_arry.slice(0, 3);
            if (data_text_arry.length > 3) {
                btnText += "...";
            }
            $(selectBtn).val(btnText);
            $(selectBtn).attr("title", data_text_arry.toString());
        },
        //获得值
        getSelectValues: function() {
            var that = this,
                element = this.element, //指的是元素
                $this = $(element);
            selectID = '#' + that.getSelectID($this),
                selectValues = $(selectID + ' input[type="hidden"]').eq(0); //值域对象
            return selectValues.val();
        },
        //获得选中的键值对象
        getSelectKeyValues: function() {
            var that = this,
                element = this.element, //指的是元素
                $this = $(element);
            selectID = '#' + that.getSelectID($this),
                selectValues = that.getSelectValues(); //值域对象
            var selectKeyValues = [];
            if (selectValues) {
                $(selectID + ' ul li').each(function() {
                    var thislival = $(this).attr("data-value");
                    var thislitext = $(this).text();
                    $.each(selectValues.split(","), function(n, val) {
                        if (thislival == val) {
                            selectKeyValues.push({
                                thislival: thislival,
                                thislitext: thislitext
                            });
                        }
                    });

                });
            }
            return selectKeyValues;
        },
        //重置select域
        resetSelect: function(element) {
            var that = this,
                $this = $(element),
                selectedValue = that.getSelectedOptionValue($this); //默认首个值 
            that.settings.defaultValues = selectedValue;
            that.setSelectValues($this);
        },
        //获取原生选择列表id值
        getSelectID: function(element) {
            var $this = $(element),
                selectID = $this.attr('id');

            if (typeof(selectID) !== 'undefined') {
                return selectID;
            } else {
                selectID = '';
                return selectID;
            }
        },

        //获取原生选择列表name值
        getSelectName: function(element) {
            var that = this,
                $this = $(element),
                selectName = $this.attr('name');

            if (typeof(selectName) !== 'undefined') {
                return selectName;
            } else {
                return that.getSelectID($this);
            }
        },

        //获取原生选择列表class名
        getSelectClassName: function(element) {
            var $this = $(element),
                tempClassNameArray = [],
                classNameArray = [],
                className = $this.attr('class');

            if (typeof(className) !== 'undefined') {
                classNameArray = className.split(' ');
                classNameArray.sort();
                tempClassNameArray = [classNameArray[0]];
                for (var i = 1; i < classNameArray.length; i++) {
                    if (classNameArray[i] !== tempClassNameArray[tempClassNameArray.length - 1]) {
                        tempClassNameArray.push(classNameArray[i]);
                    }
                }
                return tempClassNameArray.join(' ');
            } else {
                className = '';
                return className;
            }
        },

        //获取原生选择列表选中索引值
        getSelectedIndex: function(element) {
            var $this = $(element),
                selectedIndex = $this.get(0).selectedIndex || 0;

            return selectedIndex;
        },

        //获取原生选择列表option的数量
        getOptionCount: function(element) {
            var $this = $(element);

            return $this.children().length;
        },

        //获取原生选择列表option的内容
        getOptionText: function(element) {
            var that = this,
                $this = $(element),
                $option = $this.children(),
                optionText = [],
                selectLength = that.getOptionCount($this);

            for (var i = 0; i < selectLength; i++) {
                optionText[i] = $.trim($option.eq(i).text());
            }
            return optionText;
        },

        //获取原生选择列表选中值
        getSelectedOptionText: function(element) {
            var that = this,
                $this = $(element),
                selectedIndex = that.getSelectedIndex($this),
                optionText = that.getOptionText($this);

            return optionText[selectedIndex];

        },

        //获取原生选择列表选中option的value值
        getSelectedOptionValue: function(element) {
            var that = this,
                $this = $(element),
                selectedIndex = that.getSelectedIndex($this),
                optionValue = that.getOptionValue($this);

            return optionValue[selectedIndex];
        },

        //获取原生选择列表所有option的value,返回数组
        getOptionValue: function(element) {
            var that = this,
                $this = $(element),
                $option = $this.children(),
                optionValue = [],
                selectLength = that.getOptionCount($this);

            for (var i = 0; i < selectLength; i++) {
                optionValue[i] = $option.eq(i).val();
            }
            return optionValue;
        },
        //设置模拟选择列表样式
        setSelectStyle: function(element) {
            var that = this,
                $this = $(element),
                selectID = '#' + that.getSelectID($this),
                selectLength = that.getOptionCount($this);

            //设置模拟选择列表外层样式
            $(selectID).css({
                'z-index': that.setStyleProperty(that.settings.zIndex),
                width: that.setStyleProperty(that.settings.width) - 2 + 'px',
                height: that.setStyleProperty(that.settings.height) + 'px'
            });

            //设置模拟选择列表向下箭头样式
            $(selectID).children('.select-down').css({
//                top: that.setStyleProperty((that.settings.height - that.settings.triangleSize) / 2) + 'px',
//                'border-width': that.setStyleProperty(that.settings.triangleSize) + 'px',
//                'border-color': that.setStyleProperty(that.settings.triangleColor) + ' transparent transparent transparent'
//            
                top: '12px',
                'border-width': '6px 3px 0px 3px',
                'border-color': 'rgb(0, 0, 0) transparent transparent'
            
            });

            //设置模拟选择列表按钮样式
            $(selectID).children('.select-button').css({
                width: function() {
                    if (!that.settings.width) {
                        return;
                    } else {
                        return that.settings.width - 2 + 'px';
                    }
                },
//                height: that.setStyleProperty(that.settings.height) + 'px',
                height: '28px',
                border: that.setStyleProperty(that.settings.border),
//                'border-radius': that.setStyleProperty(that.settings.borderRadius) + 'px'
            });

            //设置模拟选择列表下拉外层样式
            $(selectID).children('.select-list').css({
                width: function() {
                    if (!that.settings.width) {
                        return;
                    } else {
                        return that.settings.width - 2 + 'px';
                    }
                },
                'text-align': 'left;',
                'top': function(index, value) {
                    if (!that.settings.height) {
                        return;
                    } else {
                        if (!that.settings.topPosition) {
                            var topTemp = that.settings.height + 1 + 'px';
                            return topTemp;
                        } else {
                            if (!that.settings.optionHeight) {
                                //计算下拉列表高度
                            } else {
                                return -that.settings.optionHeight * selectLength - 3 + 'px';
                            }
                        }
                    }
                }
            });

            //设置设置模拟选择列表选项外层样式
            $(selectID + ' ul').css({
                //默认展示5个高度
                //'max-height': that.setStyleProperty(that.settings.optionHeight * that.settings.optionNumCount + 10) + that.settings.optionHeight / 2 + 'px',
                'max-height': '200px',
                'line-height': that.setStyleProperty(that.settings.optionHeight) + 'px'
            });

            //设置模拟选择列表选项样式
            $(selectID + ' li').css({
                height: that.setStyleProperty(that.settings.optionHeight) + 'px'
            });
        },
        //检测是否设置某个样式
        setStyleProperty: function(styleProperty) {
            if (!styleProperty) {
                return;
            } else {
                return styleProperty;
            }
        },
        //绑定模拟选择列表一系列事件
        setSelectEvent: function(element) {
            var that = this,
                $this = $(element),
                showSpeed = that.settings.speed,
                border = that.settings.border,
                selectID = '#' + that.getSelectID($this),
                selectName = that.getSelectName($this),
                selectedIndex = that.getSelectedIndex($this),
                selectLength = that.getOptionCount($this),
                selectBtn = $(selectID + ' input[type="button"]').eq(0), //按钮对象
                selectValues = $(selectID + ' input[type="hidden"]').eq(0), //值域对象
                selectItem = $(selectID + ' li');

            $(selectID).unbind('click').click(function(event) {
                //重置所有节点高度，避免重叠
                var zIndex = that.settings.zIndex,
                    wrapClassName = that.settings.wrapClassName;
                $(document).find('.' + wrapClassName).each(function() {
                    $(this).css({
                        "z-index": zIndex
                    });
                });
                //IE
                if (window.event) {
                    window.event.cancelBubble = true; //停止冒泡
                    window.event.returnValue = false; //阻止事件的默认行为
                } else if (event) {
                    //Firefox：
                    event.preventDefault(); // 取消事件的默认行为  
                    event.stopPropagation(); // 阻止事件的传播 
                }
                if (that.settings.zIndex) {
                    var zIndexShow = zIndex + 1;
                    $(this).css({
                        "z-index": zIndexShow
                    });
                };
                if ((event.target.tagName !== 'A') && (event.target.tagName !== 'DIV') && (event.target.tagName !== 'SPAN')) {
                    $(this).children('.select-list').slideToggle(showSpeed);
                }
                if (that.settings.border) {
                    $(this).css({
                        border: border
                    });
                } else {
                    $(this).addClass('focus');
                }
                //选中设置的默认值
                var targetSelectList = $(this);
                var selectValuesInfo = $(selectValues).val();
                if (selectValuesInfo) {
                    $.each(selectValuesInfo.split(","), function(n, val) {
                        targetSelectList.find('ul li').each(function() {
                            var this_li_data_value = $(this).attr("data-value");
                            if (this_li_data_value == val) {
                                $(this).addClass('selected');
                                //选中同步的checkbox样式
                                var checkBoxObj = $(this).find('span').eq(0);
                                if (checkBoxObj) {
                                    $(checkBoxObj).html("√");
                                }
                            }
                        });
                    });
                }
            });

            $(selectID + 'all_select').unbind('click').bind('click', function() {
                if ($(this).attr('op') == 'yes') {
                    $(selectID + '  li:not(.selected)').trigger('click');
                    $(this).attr('op', 'no');
                    var checkBoxObj = $(this).find('span').eq(0);
                    if (checkBoxObj) {
                        $(checkBoxObj).html("√");
                    }
                    // 全选则显示全部  wubin 2016/05/17
                    $(selectID + ' input[type="button"]').eq(0).val("全部");
                } else {
                    $(selectID + ' li.selected').trigger('click');
                    $(this).attr('op', 'yes');
                    var checkBoxObj = $(this).find('span').eq(0);
                    if (checkBoxObj) {
                        $(checkBoxObj).html("");
                    }
                    // 全不选则显示请选择  wubin 2016/05/17
                    $(selectID + ' input[type="button"]').eq(0).val("--请选择--");
                }
            })
            $(selectID + 'Cancel').unbind('click').bind('click', function() {
                $(selectID).children('.select-list').slideToggle(showSpeed);
            })

            $(selectID + 'Confirm').unbind('click').bind('click', function() {
                if ($.isFunction(that.settings.onChange)) {
                    that.settings.onChange();
                    $(selectID).children('.select-list').slideToggle(showSpeed);
                }
            })


            /* $(selectID).bind('focusin','input[type="button"]',function(){
                $('.select-wrapper').children('.select-list').slideUp(showSpeed);
                if($('.select-wrapper').hasClass('focus')){
                    $('.select-wrapper').removeClass('focus');
                }
            });
             */
            //绑定单击选项事件

            selectItem.bind('click', function(event) {
                //IE
                if (window.event) {
                    window.event.cancelBubble = true; //停止冒泡
                    window.event.returnValue = false; //阻止事件的默认行为
                } else if (event) {
                    //Firefox：
                    event.preventDefault(); // 取消事件的默认行为  
                    event.stopPropagation(); // 阻止事件的传播 
                }
                var thisClassInfo = that.getSelectClassName($(this));
                if (thisClassInfo.indexOf("selected") > -1) {
                    $(this).removeClass('selected');
                    var checkBoxObj = $(this).find('span').eq(0);
                    if (checkBoxObj) {
                        $(checkBoxObj).html("");
                    }
                } else {
                    $(this).addClass('selected');
                    var checkBoxObj = $(this).find('span').eq(0);
                    if (checkBoxObj) {
                        $(checkBoxObj).html("√");
                    }
                }

                if ($(this))
                //重新计算值设置值
                var selectedObjs = $(this).parent().parent().find("li.selected");
                var all_value = [];
                $(this).parent().parent().find("li").each(function() {
                    all_value.push($(this).attr('data-value'))
                });
                //console.log(all_value);
                var data_value_arry = [];
                var data_text_arry = [];
                $.each(selectedObjs, function(n, liObj) {
                    //do something here  
                    var data_value = $(liObj).attr("data-value");
                    //var data_text = $.trim($(liObj).text());
                    var data_text = $(liObj).find("span").eq(1).text();
                    if (data_value) {
                        data_value_arry.push(data_value);
                        data_text_arry.push(data_text);
                    }
// modify by wubin 当所有值都选中时不触发全选选中状态 begin 20160520
                    if (data_value_arry.length == all_value.length) {
                        $(selectID + 'all_select').find('span').eq(0).html('√');
                        $(selectID + 'all_select').attr('op', 'no');
                    } else {
                        $(selectID + 'all_select').find('span').eq(0).html('');
                        $(selectID + 'all_select').attr('op', 'yes')
                    }
// modify by wubin end 20160520                    
                    //alert(n+' '+data_value_arry.toString()+"--"+data_text_arry.toString());  
                    //alert(value.html());  
                });
                $(selectValues).val(data_value_arry.toString());
                
                if (data_value_arry.length == all_value.length) {
                    $(selectID + ' input[type="button"]').eq(0).val("全部");
                }else {
                	var btnText = data_text_arry.slice(0, 3);
                    if (data_text_arry.length > 3) {
                        btnText += "...";
                    }
                    $(selectBtn).val(btnText);
                    $(selectBtn).attr("title", data_text_arry.toString());
                }
                
                //定义事件
                /* if($.isFunction(that.settings.onChange)){
                    that.settings.onChange.call(that);
                } */
                return false;
            }).mouseenter(function(event) {
                var target = event.target,
                    realWidth = target.offsetWidth,
                    wrapperWidth = target.scrollWidth,
                    text = $(target).text();
                if (realWidth < wrapperWidth) {
                    $(target).attr("title", text);
                }
            });
            //空白点击自动收缩
            $(document).bind('click', function() {
                $(this).find('.select-list').slideUp(showSpeed);
                if ($('.select-wrapper').hasClass('focus')) {
                    $('.select-wrapper').removeClass('focus');
                }
            });
        },
        //获得替换后选中项的值对象集合
        getSelectedValObj: function(element) {
            var that = this,
                $this = $(element);
            var selectLiObjs = $this.find("li.selected");
        },
        //生成模拟选择列表
        renderSelect: function(element) {
            var that = this,
                $this = $(element),
                defaultValues = that.settings.defaultValues,
                selectID = that.getSelectID($this),
                selectName = that.getSelectName($this),
                selectClassName = that.getSelectClassName($this),
                selectOptionText = that.getOptionText($this),
                selectedOptionText = that.getSelectedOptionText($this),
                selectOptionValue = that.getOptionValue($this),
                selectedIndex = that.getSelectedIndex($this),
                selectedValue = that.getSelectedOptionValue($this),
                selectLength = that.getOptionCount($this);
            that.settings.optionNumCount = selectLength; //设置节点的高度
            selectHTML = "";
            selectListHTML = '';
            //以默认值为准(初始化后设置)//没有默认值，默认选中首个节点

            if (that.settings.reg == true) {
                if (that.settings.defalutName == null) {
                    selectHTML = '<div tabindex="1" reg="[^0]" id="' + selectID + '" class="select-wrapper ' + selectClassName + '"><input type="hidden" name="' + selectName + '" value="" /><i class="icon select-down"></i><input type="button" class="select-button" value=""/><div class="select-list"><div class="all_select" op="no" id="' + selectID + 'all_select"><span style="text-align:center;line-height:10px;display:inline-block;width:10px;height:10px;border:1px solid gray;">√</span>&nbsp;&nbsp;全部</div><ul>';
                } else {
                    selectHTML = '<div tabindex="1" reg="[^0]" id="' + selectID + '" class="select-wrapper ' + selectClassName + '"><input type="hidden" name="' + selectName + '" value="" /><i class="icon select-down"></i><input type="button" class="select-button" value="" /><input class="select-button" value="' + that.settings.defalutName + '" type="button"/><div class="select-list"><div class="all_select" op="no" id="' + selectID + 'all_select"><span style="text-align:center;line-height:10px;display:inline-block;width:10px;height:10px;border:1px solid gray;">√</span>&nbsp;&nbsp;全部</div><ul>';
                }
            } else {
                if (that.settings.defalutName == null) {
                    selectHTML = '<div id="' + selectID + '" class="select-wrapper ' + selectClassName + '"><input type="hidden" name="' + selectName + '" value="" /><i class="icon select-down"></i><input type="button" class="select-button" value=""/><div class="select-list"><div class="all_select" op="no" id="' + selectID + 'all_select"><span style="text-align:center;line-height:10px;display:inline-block;width:10px;height:10px;border:1px solid gray;">√</span>&nbsp;&nbsp;全部</div><ul>';
                } else {
                    selectHTML = '<div id="' + selectID + '" class="select-wrapper ' + selectClassName + '"><input type="hidden" name="' + selectName + '" value="" /><i class="icon select-down"></i><input type="button" class="select-button" value=""  style="display:none"/><input class="select-button" value="' + that.settings.defalutName + '" type="button"/><div class="select-list"><div class="all_select" op="no" id="' + selectID + 'all_select"><span style="text-align:center;line-height:10px;display:inline-block;width:10px;height:10px;border:1px solid gray;">√</span>&nbsp;&nbsp;全部</div><ul>';
                }
            }
            for (var i = 0; i < selectLength; i++) {
                selectListHTML += '<li data-value="' + selectOptionValue[i] + '"><span style="text-align:center;line-height:10px;display:inline-block;width:10px;height:10px;border:1px solid gray;"></span>&nbsp;&nbsp;<span class="data_text">' + selectOptionText[i] + '</span></li>';
            }
            selectHTML += selectListHTML;
            selectHTML += '</ul><div class="select_bottom"><a href="javascript:void(0)" id="' + selectID + 'Confirm">确定</a><a href="javascript:void(0)" id="' + selectID + 'Cancel">取消</a></div></div></div>';
            return selectHTML;
        }
    };
    //select插件
    $.fn[pluginName] = function(options) {
        var plugin_arry = [];
        this.each(function() {
            if (!$.data(this, "plugin_" + pluginName)) {
                $.data(this, "plugin_" + pluginName, new SelectList(this, options));
                //plugin_arry.push($.data(this, "plugin_" + pluginName));
            }
        });
        return this; //返回原始域
        //return plugin_arry;//返回selectjian
    };

    //自定义方法
    $.extend({
        //获得多选值
        'getMulSelectValues': function(element) {
            var selectValues = "";
            var plugin_obj = $.data(element, "plugin_" + pluginName);
            if (plugin_obj) {
                selectValues = plugin_obj.getSelectValues();
            }
            return selectValues;
        },
        //设置多选值
        'setMulSelectValues': function(element, defaultValues) {
            var plugin_obj = $.data(element, "plugin_" + pluginName);
            if (plugin_obj) {
                plugin_obj.settings.defaultValues = defaultValues;
                plugin_obj.setSelectValues(plugin_obj.element);
            }
        },
        //重置多选项为默认值（默认首个项）
        'resetMulSelect': function(element) {
            var plugin_obj = $.data(element, "plugin_" + pluginName);
            if (plugin_obj) {
                plugin_obj.resetSelect(plugin_obj.element);
            }
        }
    });
})(jQuery, window, document);
