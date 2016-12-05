function DataOp(obj) {
    this.accordion = obj.accordion || "";
    this.title_add_icon = obj.title_add_icon || '';
    this.jsonData = [];
}


DataOp.prototype = {
    init: function() {
        this.accordionFn();
        this.file_op();
        this.title_add_file();
        this.bindDom();
        if($('.searchOther').length == 0){
        	this.render_file_content('getProjectData');
        }else{
        	this.render_file_content('getOtherData');
        }
        
    },
    render_file_content: function(getProjectData) {
        var _this = this;
        var param = {};
        $('.data_title_').find('input').each(function() {
            param[$(this).attr('name')] = $(this).val();
        });
        var ajax_option = {
            type: "POST",
            url: contextPath + '/dataManage/' + getProjectData + '/1',
            data: param,
            dataType: "json",
        };
        $.ajax(ajax_option).done(function(data) {
            _this.jsonData = $.extend(true, [], data.resultData); //继承数据
            _this.dataPages = data.pages;
            _this.createContent(_this.jsonData);
            laypage({
                cont: 'page',
                pages: _this.dataPages,
                curr: 1,
                jump: function(obj, first) {
                    if (!first) {
                        $.ajax({
                            type: "POST",
                            url: contextPath + '/dataManage/' + getProjectData + '/' + obj.curr,
                            data: param,
                            dataType: "json",
                        }).done(function(data) {
                            _this.jsonData = $.extend(true, [], data.resultData); //继承数据
                            _this.dataPages = data.pages;
                            _this.createContent(_this.jsonData);
                        })
                    }
                }
            });
        })
    },
    bindDom: function() {
        var _this = this;
        $('.searchBtn').off('click').on('click', function() {
            _this.render_file_content('getProjectData');
        })
        $('.searchOther').off('click').on('click', function() {
            _this.render_file_content('getOtherData');
        })
    },
    createContent: function(data) {
    	var userCookie = JSON.parse($.cookie("user"));
    	var param = {};
    	$('.data_title_').find('input').each(function() {
            param[$(this).attr('name')] = $(this).val();
        });
        html = '<li class="title"><span class="file_name"> 文件名称</span><span class="flie_time"> 更新时间</span><span class="file_person"> 上传人</span><span class="file_op">操作</span></li>';
        data.forEach(function(item, i) {
            html += '<li attr_index="attr_index' + i + '" class="attr_index' + i + '">';
            if (item.dataList && item.dataList.length > 0) { //项目资料或敏捷需求资料
                html += '<p>';
                html += '<span class="file_name" ><a href="javascript:void(0)" class="add_toggle"></a><span style="display:block;text-align:left;margin-left:2.5em;">' + item.title + '</span></span><span class="flie_time">' + item.strUploadTime + '</span><span class="file_person">' + '' + '</span><span class="file_op"><span class="file_op"> <a href="javascript:void(0)" class="add_file" proId="' + item.proId + '" parents_title="' + item.title + '" >添加</a></span></p><ul>';
                item.dataList.forEach(function(item, j) {
                	  html += '<li attr_index="attr_index' + j + '_' + i + '"><span class="file_name"><a href="javascript:void(0)" class="add_toggle"></a><div>' + item.fileName + '</div></span><span class="flie_time">' + item.strUploadTime + '</span><span class="file_person">' + item.userName + '</span><span class="file_op"><span class="file_op"><a href="' + contextPath + '/' + item.id + '/download" target="downloadIframe">下载</a>'
                	  if(userCookie.id == item.uploadUserId){
                		  html += '&nbsp;&nbsp;<a href="javascript:void(0)" class="del_file" attr_id="' + item.id + '"  index="' + j + '" parents_index="' + i + '">删除</a>'
                	  }
                	  html += '</span></li>';
                })
                html += '</ul>'
            } else { //其他资料
                html += '<span class="file_name"><a href="javascript:void(0)"></a><div>' + item.fileName + '</div></span><span class="flie_time">' + item.strUploadTime + '</span><span class="file_person">' + item.userName + '</span><span class="file_op"><span class="file_op"><a href="' + contextPath + '/'+ item.id + '/download" target="downloadIframe">下载</a>';
                if(userCookie.id == item.uploadUserId){
                	html +=  '&nbsp;&nbsp;<a href="javascript:void(0)" class="del_file" attr_id="' + item.id + '" index="' + i + '">删除</a>'
                }
               
                html += '</span>';
            }
            html += '</li>';
        });
        $('.data_accordion').html(html)
    },
    accordionFn: function() {
        var _this = this;
        $(_this.accordion).on('click', '.add_toggle', function() {
            $(this).toggleClass('reduce_toggle').closest('p').next().slideToggle().end().closest('li').siblings('li').find('a').removeClass('reduce_toggle').end().find('ul').slideUp();
        })
    },
    file_op: function() {
        var _this = this;
        $(_this.accordion).off('click', 'a').on('click', 'a', function(e) {
        	
        	
            switch ((e.target.className)) {
                case 'add_file':
                    var parents_title = $(this).attr('parents_title'),
                        proId = $(this).attr('proid'),
                        content = $('#file_tpl').html().replace(/\{name\}/, parents_title);
                    var d = dialog({
                        content: content,
                        okValue: '上传',
                        ok: function() {
                        	var param = {};
                        	$('#file_upload input[name="proId"]').attr("value",proId);
                        	 $('.data_title').find('input').each(function() {
                                 if ($(this).attr('name') == "proType"){
                                	 param.proType = $(this).val();
                                 } 
                             });
                            param.name = $('.ui-dialog-content .input_file_name').val();
                            param.title = parents_title;
                            _this.addFile(param, _this.createContent);
                        },
                        cancelValue: '取消',
                        cancel: function() {},
                        title: '提示'
                    }).width(400).height(300);
                    d.show();
                    _this.getFileName()
                    break;
                case 'del_file':
                    var attr_id = $(this).attr('attr_id'); //删除的属性标识
                    var obj = $(this);
                    var d = dialog({
                        title: '提示',
                        content: '确定要删除吗',
                        okValue: '确定',
                        ok: function() {
                            var param = {};
                            param.attr_id = attr_id;
                            param.index = obj.attr('index');
                            param.parents_index = obj.attr('parents_index');

                            _this.delFile(param, obj, _this.createContent); //删除数据
                        },
                        cancelValue: '取消',
                        cancel: function() {}
                    });
                    d.show();
                    break;
            }
        })
    },
    getFileName: function(name) {

        $('body').off('change').on('change', '.modal_file', function() {
            $('.file_error_tips').html('');
            var str = $(this).val();
            var arr = str.split('\\');
            var filename = arr[arr.length - 1];
            $('.input_file_name').val(filename)
        });
    },
    title_add_file: function() {
        var _this = this;
        $(_this.title_add_icon).on('click', function() {
            var $content = $('<div></div>').append($('#file_tpl').html());
            $content.find('p').eq(0).remove();
            var d = dialog({
                content: $content,
                okValue: '上传',
                ok: function() {
                    var param = {};
                    param.name = $('.input_file_name').val();
                    param.title = null;
                    _this.addFile(param, _this.createContent);
                },
                cancelValue: '取消',
                cancel: function() {},
                title: '提示'
            }).width(400).height(200);
            d.show();
            _this.getFileName()
        })
    },
    addFile: function(param, callback) {
        var _this = this;
        //点击确定的上传操作
        function suffix(file_name) { //获取文件后缀名
            var result = /\.[^\.]+/.exec(file_name);
            return result;
        }
        var suffix = suffix(param.name),
            file_type_data = ['.xls', '.xlsx', '.doc', '.docx','.txt', '.pdf', '.jpg', '.gif', '.jpe', '.jpeg', '.jpg', '.png', '.zip','.html'],
            file_type_onoff = false; //开关，文件格式默认错误

        for (var i = 0; i < file_type_data.length; i++) {
            if (suffix == file_type_data[i]) { //有配置的文件格式
                file_type_onoff = true;
                break;
            }
        }
        if (param.name && file_type_onoff) { //如果格式正确

            $("#file_upload").ajaxSubmit({
                dataType: 'json', //数据格式为json 
                beforeSend: function() { //开始上传 
                    //进度条相关
                },
                uploadProgress: function(event, position, total, percentComplete) {

                },
                success: function(data) { //成功 
                	
                    var userName=JSON.parse($.cookie("user")).userName;
                    var otherPage = $('#other_file_page').length;
                    //为data新增属性
                    data.userName=userName;
                    data.title=param.title;
                    data.strUploadTime=data.updateTime.split(' ')[0];

                    if (!otherPage) { //项目资料、敏捷需求资料
                         //添加数据
                         for (var i = 0; i < _this.jsonData.length; i++) {
                             if (_this.jsonData[i].proId == data.proId) {
                                 _this.jsonData[i].dataList.unshift(data);
                                 _this.jsonData[i].strUploadTime = data.strUploadTime;
                                 var moveData = _this.jsonData.splice(i, 1);
                                 _this.jsonData.unshift(moveData[0]);
                             }
                         }
                         callback(_this.jsonData);
                         $('.attr_index0').find('.file_name a').addClass('reduce_toggle');
                         $('.attr_index0').find('ul').show();

                    } else { //其他资料
                         _this.jsonData.unshift(data);
                         callback(_this.jsonData);
                    }
                },
                error: function(xhr) { //上传失败 

                }
            });
        } else if (param.name && !file_type_onoff) {
            $('.file_error_tips').html('文件格式不正确');
            throw new Error('文件格式不正确');
        } else {
            $('.file_error_tips').html('文件不能为空');
            throw new Error('文件不能为空');
        }

    },
    delFile: function(param, obj, callback) { //删除数据
        _this = this;
        var ajax_option = {
                type: "POST",
                url: contextPath + '/dataManage/deleteFile',
                data: {fileId:param.attr_id},
                dataType: "json"
        };


        $.ajax(ajax_option).done(function(data) {
            //成功后的处理
            var liLen = obj.closest('ul').find('li').length;
            var otherpage = $('#other_file_page').length;

            if (liLen > 0 && !otherpage) { //项目资料 敏捷需求资料

                for (var i = 0; i < _this.jsonData.length; i++) {
                    if (param.parents_index == i) {
                        _this.jsonData[i].dataList.splice(param.index, 1); //删除数据
                        var parents_index = param.parents_index--;
                    }
                }

                parents_index = parents_index < 0 ? 0 : parents_index;

                if (_this.jsonData[parents_index].dataList && _this.jsonData[parents_index].dataList[0]) {
                    _this.jsonData[parents_index].strUploadTime = _this.jsonData[parents_index].dataList[0].strUploadTime; //重新赋值给时间
                }
                if (_this.jsonData[parents_index].dataList.length == 0) { //没有子项时，删除
                    _this.jsonData.splice(parents_index, 1);
                    callback(_this.jsonData);
                }else{ //还有子项的时候，展示
                    callback(_this.jsonData);
                    $('.attr_index'+parents_index).find('p .file_name a').addClass('reduce_toggle');
                    $('.attr_index'+parents_index).find('ul').show();
                }

            } else if (otherpage) { //其他资料
                _this.jsonData.splice(param.index, 1); //删除数据
                callback(_this.jsonData);
            }
        })
    }

}



new DataOp({ 'accordion': '.data_accordion', 'title_add_icon': '.button' }).init()
