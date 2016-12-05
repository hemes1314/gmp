$(document).ready(function(){
	$("#log").click(function(){
		var username = $("#username").val().trim();
		var password = $("#password").val().trim();
		if(username==""){
			$("#msg").html("用户名不能为空！").show();
		}else if(password==""){
			$("#msg").html("密码不能为空！").show();
		}else if(username!="" && password!=""){
			$.ajax({
				type:'POST',
				url: "/gmp/toLogin",
				data:{
					username:username,
					password:password
				},
				success: function(data){
					if(data.resultData=="nexistence"){
						$("#msg").html("用户名不存在！").show()
					}else if(data.resultData=="fail"){
						$("#msg").html("密码输入错误！").show();
					}else if(data.resultData=="success"){
						location.href=data.targetUrl;
					}
				},
				dataType:"json",
				error:function(){
					$("#msg").html("访问出错了！").show()
				}
			});
		}
	});
});