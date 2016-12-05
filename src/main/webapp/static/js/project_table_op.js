    function Table_op(name) {
        if (Object.prototype.toString.call(name).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'string') return;
        this.table_name = name || ""
    }
    Table_op.prototype = {
        constructor: Table_op,
        init: function() {
            this.op();
        },
        op: function() {
            var _this = this;
            $(_this.table_name).on('click', 'a', function(e) {
                switch (e.target.className) {
                    case 'edit_project':
                        var name = $(this).closest('tr').find('td').eq(0).text();
                        console.log(name);
                        var href = '1-9new_bulit_Project.html?name=' + name;
                        window.location.href = href;
                        break;
                    case 'close_project':
                        var d = dialog({
                            title: '提示',
                            content: '确定要关闭吗',
                            okValue: '确定',
                            ok: function() {
                                return false;
                            },
                            cancelValue: '取消',
                            cancel: function() {}
                        });
                        d.show();
                }
            })
        }

    }
    new Table_op('.project_info_table').init()

    function GomeProject_CommonBar(config) {
        if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
        this.page = config.laypage || "";
        this.calendar_table = config.work_hour_table || '';
        this.content_table = config.work_content_table || '';
        this.addButton = config.addButton || ''
    }

    GomeProject_CommonBar.prototype = {
        constructor: GomeProject_CommonBar,
        init: function() {
            this.Table_op();
            this.log_op_fn();
            this.button_op()
        },
        Table_op: function() {
            var _this = this;
            $(_this.calendar_table).off('click', 'td').on('click', 'td', function() {
                $(this).addClass('selected');
                var tpl = $('#calender_tab').html();
                $(_this.content_table).show().append(tpl);

            })
        },
        log_op_fn: function() {
            var _this = this;
            $(_this.addButton).on('click', function() {
                var tpl = $('#ul_tpl').html();
                var $div_tpl = $('<div></div>').append(tpl);
                $div_tpl.find('.clockpicker').each(function() {
                    $(this).clockpicker()
                });
                var Head = document.getElementsByTagName('HEAD').item(0);
                var Script = document.createElement("script");
                Script.type = "text/javascript";
                Script.src = "js/jquery.spinner.js";
                Head.appendChild(Script);
                $('.GomeProject_tab-content').append($div_tpl);
                _this.button_op()
            })
        },
        button_op: function() {
            function must(arr, index) {
                arr.forEach(function(item, i) {
                    if (i === index) {
                        arr[i] = item + '#need';;
                    }
                })
            }
            var arr = ['save_button', 'edit_button', 'del_button']
            must(arr, 0);
            arr.forEach(function(item, i) {
                if (item.toString().indexOf('need') > -1) {
                    var _dom = '.' + item.split('#')[0];
                    if ($(_dom).css('display') == 'block') {
                        $(_dom).siblings().css('display', 'none');
                    } else {
                        $(_dom).siblings().css('display', 'block');
                    }
                    $('body').off('click', _dom).on('click', _dom, function() {
                        $(this).toggle().siblings().toggle();
                    })
                }
            })
        }
    };

    new GomeProject_CommonBar({
        'laypage': 'GomeProjectPage',
        'work_hour_table': '#calendar',
        'work_content_table': '#work_hour_content',
        'addButton': '#add_log'
    }).init()
