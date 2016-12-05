$(document).ready(function() {
	
	var unitBsIds = ["unitBsId1","unitBsId2","unitBsId3"];
	var payUnitIds = ["payUnitId1","payUnitId2","payUnitId3"];
	
	changeEvent(unitBsIds, "unitBsIdAll");
	changeEvent(payUnitIds, "payUnitIdAll");
	
	/**
	 * 级联事件绑定
	 * unitIds:三级部门下拉列表表单id组成的数组
	 * unitIdAll:编辑时部门加载默认值的隐藏域id，
	 * 			表单值格式：中心orgId-部门OrgId-子部门OrgId(01-0101-010101)
	 */
	function changeEvent(unitIds, unitIdAll) {
		if($("#"+unitIds[0]) != undefined && $("#"+unitIds[1]) != undefined && $("#"+unitIds[2]) != undefined) {
			
			var unitIdAll = $("#"+unitIdAll).val();
			var unitIdAllArr = null;
			if(unitIdAll != undefined) {
				unitIdAllArr = unitIdAll.split("-");
				if(unitIdAllArr.length > 0) {
					$("#"+unitIds[0]).find("option[value='"+unitIdAllArr[0]+"']").attr("selected",true);
				}
			}
			$("#"+unitIds[0]).change(function(){
				loadNextLevelOrg($(this).val(), unitIds[1], 2, unitIdAllArr);
			});
			
			$("#"+unitIds[1]).change(function(){
				loadNextLevelOrg($(this).val(), unitIds[2], 3, unitIdAllArr);
			});
			
			if(unitIdAll != undefined) {
				$("#"+unitIds[0]).trigger("change");
			}
		}
	}
	
	function loadNextLevelOrg(orgIdVal, targetOrgId, level, orgAllArr) {
		if(orgIdVal!=null && orgIdVal!=""){
			$.post(contextPath +'/orgManage/getNextLevel?orgId='+orgIdVal,function(data){
				var emptyStrArr = ["选择中心","选择部门","选择子部门"];
				if(data == "") {
					$('#'+targetOrgId).html('<option value="">'+emptyStrArr[level-1]+'</option>');
				}else{
					var opts = "";
					for(var i = 0; i < data.length; i++) {
						var item = data[i];
						opts += '<option value="' + item.orgId + '">' + item.orgName + '</option>';
					}
					$('#'+targetOrgId).html(opts);
					if(orgAllArr != null) {
						$("#"+targetOrgId).find("option[value='"+orgAllArr[level-1]+"']").attr("selected",true);
					}
				}
				// 触发change事件
				$("#"+targetOrgId).trigger("change");
			});
		}
	}

});

