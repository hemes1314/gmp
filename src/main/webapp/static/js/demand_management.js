
function findneedBuildCrumb(dom){
	parent.pushNavStack($(dom).text());
}

function GomeProject_CommonBar(config) {
    if (Object.prototype.toString.call(config).match(/\[object (\w+)\]/)[1].toLowerCase() !== 'object') return;
    this.leftBar = config.leftBar || {};
    this.autocomplete_input = config.autoInput || {};
    this.page = config.laypage || "";
    this.searchButton = config.searchButton;
}

GomeProject_CommonBar.prototype = {
    constructor: GomeProject_CommonBar,
    init: function() {
        this.LeftBarchange();
        this.CreatePage();
        //this.upload();
        this.bind_complete();
        this.getData();
        $(this.searchButton).click();
        $(window.parent.document).find('body')[0].scrollTop = 0;
    },
    LeftBarchange: function() {
        var _this = this;
        var $leftBar = $(_this.leftBar);
        $leftBar.on('click', 'li', function() {
            $(this).find('a').toggleClass('active').end().siblings().find('a').removeClass('active');
        })
    },
    CreatePage: function() {
        var _this = this;
        if (document.getElementById(_this.page)) {
            laypage({
                cont: document.getElementById(_this.page),
                pages: 100,
                skip: true,
                skin: '#AF0000',
            });
        }

    },
    upload: function() {
        var uploader = new plupload.Uploader({
            runtimes: 'html5,flash,silverlight,html4',
            browse_button: 'pickfiles',
            container: document.getElementById('container'),
            url: 'upload.php',
            flash_swf_url: '../js/Moxie.swf',
            silverlight_xap_url: '../js/Moxie.xap',

            filters: {
                max_file_size: '10mb',
                mime_types: [{
                    title: "images files",
                    extensions: "jpg,gif,png"
                }, {
                    title: "Zip files",
                    extensions: "zip"
                }]
            },

            init: {
                PostInit: function() {
                    document.getElementById('filelist').innerHTML = '';

                    document.getElementById('uploadfiles').onclick = function() {
                        uploader.start();
                        return false;
                    };
                },

                FilesAdded: function(up, files) {
                    plupload.each(files, function(file) {
                        document.getElementById('filelist').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b></div>';
                    });
                },

                UploadProgress: function(up, file) {
                    document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
                },

                Error: function(up, err) {
                    document.getElementById('console').appendChild(document.createTextNode("\nError #" + err.code + ": " + err.message));
                }
            }
        });

        uploader.init();
    },
    bind_complete: function() {
        var states = [
            'Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California',
            'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia',
            'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa',
            'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Maryland',
            'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi',
            'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire',
            'New Jersey', 'New Mexico', 'New York', 'North Carolina',
            'North Dakota', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania',
            'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee',
            'Texas', 'Utah', 'Vermont', 'Virginia', 'Washington',
            'West Virginia', 'Wisconsin', 'Wyoming'
        ];
        var _this = this;
        if ($(_this.autocomplete_input).length) {
            $(_this.autocomplete_input).autocomplete({
                source: [states]
            })
        }
    },
    render_data: function(json) {
    	 String.prototype.temp = function(obj) {
             return this.replace(/\$\w+\$/gi, function(matchs) {
                 //var returns = obj[matchs.replace(/\$/g, "")];

                 var propertyName = matchs.replace(/\$/g, "");
                 var returns = obj[propertyName];
                 // 如果是时间则取日期部分
                 if(propertyName == "planTime"
                	 || propertyName == "createTime"){
                	if(returns != null){
                		returns = returns.split(" ")[0]
                	} 
             	 }
                                 
                 return returns == null || (returns + "") == "undefined" ? "--" : returns;
             });
         };
         var tpl = $('#record_temp').html();
         var html = '';
         if(json.resultData){
        	 json = json.resultData;
         }
         var loginUserId = $("#loginUserId").val();
         var isAdmin = $("#isAdmin").val();
         json.forEach(function(obj) {
        	 //3交付人  4提交人
        	 var userCategory = obj.openStatus;
        	 //4待处理  2已拒绝
        	 var status = obj.states;
        	 var createUserId = obj.createUser;
        	 var payUserId = obj.payUserId;
        	 var prePayUserId = obj.prePayUserId;
        	 
        	 // 添加冒泡
        	 var tplContent = tpl.temp(obj);
//        	 if(payUserId == loginUserId && (status == '4' || status == '5')) {
//        		 tplContent = tplContent.replace("#marker#","<img width='20px' height='20px' src='"+contextPath+"/static/images/marker.jpg'/>");
//        	 }
        	 html += tplContent.replace("#marker#", "");
        	 
        	 html += '<td>';
        	 var content = "";
        	 if(status == '1' || status == '3') {
        		 content += '<a href="'+contextPath+'/project/detail/'+obj.proId+'"  onclick="findneedBuildCrumb(this);" class="detail">查看项目</a></td></tr>';
			 } else if(status == '4' || status == '5' || status == '2') {// 4:待处理，5:已转交，2:已拒绝
				
				 // 交付人，待处理或转交过来的需求均可以受理转交拒绝,拒绝后可以打开
				 if(payUserId == loginUserId) {
					 if(status == '4' || status == '5') {
						 content += '<a href="javascript:void(0)" class="accept">受理</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" class="transfer">转交</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" class="refuse">拒绝</a>';
					 } else if(status == '2') {
						 content += '<a href="javascript:void(0)" onclick="openStatus(this);" class="open">打开</a>';
					 }
				 }
				 // 转交后并且未受理时，前交付人，可以撤销操作
				 if(prePayUserId == loginUserId && status == '5') {
					 content += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="revokeInfo(this);" class="revoke">撤回</a>';
				 }
				 // 创建用户或管理员可以编辑删除
				 if(createUserId == loginUserId || isAdmin == '1') {
					 content += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="'+contextPath+'/demand/toUpdate/'+obj.needId+'" onclick="findneedBuildCrumb(this);" class="edit">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deleteInfo(this);" class="delete">删除</a>';
					 // 当拒绝后创建人也可以重新打开
					 if(createUserId == loginUserId && status == '2') {
						 content +='&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="openStatus(this);" class="open">打开</a>';
					 }
				 }
			 }
        	 if(content == null || content == "") {
        		 content += '--'; 
        	 }
        	 content+='</td></tr>';
        	 
        	 html += content;
         });
        
         return html;
    },
    getData: function() {
    	var _this = this;
		$(_this.searchButton).on('click', function() {
			function getTimestamp(str) {
	            str = str.replace(/-/g, '/');
	            var date = new Date(str);
	            var sTime = date.getTime();
	            return sTime
	        }
	    	
	        var param = {};
	        if($('input[name="startTime"]').val()!=undefined)
        	{
	        	param.startTime = getTimestamp($('input[name="startTime"]').val());
        	}
	        if($('input[name="endTime"]').val()!=undefined)
	    	{
	        	param.endTime = getTimestamp($('input[name="endTime"]').val());
	    	} 
      
	        param.states = $("select[name='states']").find("option:selected").val();
	        param.title = $('input[name="title"]').val();
	        
	        $.ajax({
 	            type: "POST",
 	            url: "list/1",
 	            data: JSON.stringify(param),
 	            dataType: "json",
 	            contentType: "application/json"
 	        }).done(function(data) {
                var page_data = data.resultData;
                $('.project_info_table').show().find('tbody').html(_this.render_data(data));
                laypage({
                    cont: 'page',
                    pages: data.pages,
                    curr: 1,
                    jump: function(obj, first) {
                        
                        if (!first) {
                        	 $.ajax({
                  	            type: "POST",
                  	            url: "list/"+obj.curr,
                  	            data: JSON.stringify(param),
                  	            dataType: "json",
                  	            contentType: "application/json"
                  	        }).done(function(json) {
                  	            $('.project_info_table').find('tbody').html(_this.render_data(json));
                  	        });
                        }
                    }
                });
            })
	        
	        
		});
    }
};

new GomeProject_CommonBar({
    'leftBar': '.GomeProjectCommonLeftBarUl',
    'laypage': 'GomeProjectPage',
    'autoInput': '.autocomplete_input',
    'searchButton': '.searchBtn'
}).init()

function openStatus(obj){
	var tr = $(obj).closest('tr');
	var id = tr.find('td').eq(0).text();
	$.post(contextPath +'/demand/open/'+id,function(){
		$('.searchBtn').click();
	});
}

function deleteInfo(obj){
	if(confirm("确定要删除该条记录吗？")){
		var tr = $(obj).closest('tr');
		var id = tr.find('td').eq(0).text();
		
		$.post(contextPath +'/demand/delete/'+id,function(){
		
			$('.searchBtn').click();
		});
	}
}

function revokeInfo(obj){
	if(confirm("确定撤销吗？")){
		var tr = $(obj).closest('tr');
		var id = tr.find('td').eq(0).text();
		
		$.post(contextPath +'/demand/revoke/'+id,function(){
		
			$('.searchBtn').click();
		});
	}
}
