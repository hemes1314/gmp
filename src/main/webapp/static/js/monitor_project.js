$(document).ready(function() {
    function monitor_project(config) {
        if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
        this.tableName = config.name || '';
        this.render_table_name = config.render_table;
    }

    monitor_project.prototype = {
        init: function() {
            this.createBubble();
            this.createSelect();
            $(".search").click();
        },
        createBubble: function() {
            var _this = this;
            $(_this.tableName).on('click', 'td', function() {
                if (!($(this).find('a').length == 0)) {
                    $('.monitor_detail').find('span').css('left', ($(this).offset().left - 130));
                    $('.procontr td').each(function(){
                    	$(this).css('background-color','');
                    });
                    $(this).css('background-color','#FEEBF5');
                    _this.render_table()
                }
            })
        },
        render_table: function(JSON) {
            var _this = this;
            var tpl = '';
            $(_this.render_table_name).show().find('table').find('tbody').append(tpl);
        },
        createSelect: function() {
            var _this = this;
            var ajax_option = {
                type: "POST",
                url: contextPath + '/orgManage/getQueryCondition',
                data: JSON.stringify({}),
                dataType: "json",
                async : false,
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
                _this.render_select(data, '#orgIds', obj[0]);
            })
        },
        all_select: function(name) {
            var val_arr = [];
            $(name).find('option').each(function() {
                val_arr.push($(this).val())
            });
            var multi_option = {
                zIndex: 1000,
                width: 100,
                height: 30,
                defalutName: $(name).attr('details')
            };
            multi_option.defaultValues = val_arr.join(',');
            var _this = this;
            multi_option.onChange = function() {
                var param = _this.search_form();
                param.taskStatus = $('input[name="system3"]').val().split(',');
                param.priorityIds = $('input[name="system"]').val().split(',');
                param.scheduleIds = $('input[name="system1"]').val().split(',')
                param.actualizes = $('input[name="system2"]').val().split(',')
                _this.render_table(param);
            }
            $(name).selectlist(multi_option);
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
    }
    new monitor_project({ 'name': '.monitor_content', 'render_table': '.monitor_detail' }).init()
})