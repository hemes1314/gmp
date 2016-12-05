// 校验计划工时
function valPlanTime() {
	
	// 计划工时不能为空
    var planTimes = $(".planTime");
    planTimes.removeClass("input_validation-failed");
    for(var i = 0; i < planTimes.length; i++) {
    	var planTime = planTimes.eq(i).val();
    	if(valIsInputted(i) && (planTime == null || $.trim(planTime) == "")) {
    		layer.msg("请输入计划工时!"); 
    		planTimes.eq(i).addClass("input_validation-failed");
            return false;
    	}
    }
    return true;
}

// 验证本行是否已输入排期
function valIsInputted(num) {
	var taskInputs = $("#project_table tbody tr").eq(num).find("input[type='text']");
	for(var j = 0; j < taskInputs.length; j++) {
		var input = taskInputs.eq(j);
		if(input.attr("class") != "planTime" && input.val() != "") {
			return true;
		}
	}
	return false;
}