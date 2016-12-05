$(document).ready(function() {
	$(".explainImgLabel").each(function() {
		var key = $(this).prev(".explainKey").val();
		var declare = getExplainInfo(key);
		$(this).attr("title",declare.title);
	});
});

$(".explainImgLabel").click(function(){
	var key = $(this).prev(".explainKey").val();
	var declare = getExplainInfo(key);
	smoke.alert(declare.msg, {ok: "确定"});
	$(".gmTips_title").html("");
	$(".gmTips_title").html(declare.title);
});

