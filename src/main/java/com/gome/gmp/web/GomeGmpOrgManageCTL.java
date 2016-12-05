package com.gome.gmp.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResUserBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.base.Message;
import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;
import com.gome.gmp.model.vo.GomeGmpResUserVO;

@RestController
@RequestMapping("/orgManage")
public class GomeGmpOrgManageCTL extends BaseRestController {

	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	@Resource
	private GomeGmpResUserBS gomeGmpResUserBS;
	
	/**
	 * 我的账户
	 * 
	 * @return String
	 * @author rl_wanglijie
	 */
	@RequestMapping("/myAccount")
	public ModelAndView myAccount(@ModelAttribute GomeGmpResUserVO gomeGmpResUserVO) {
		GomeGmpResUserBO userInfo = BusinessUtil.getLoginUser(getRequest());
		gomeGmpResUserVO.setUserName(userInfo.getUserName());
		gomeGmpResUserVO.setLastLogtime(userInfo.getLastLogtime());
		gomeGmpResUserVO.setEmail(userInfo.getEmail());
		String groupName ="";
		GomeGmpResOrgBO groupBeen = gomeGmpOrgManageBS.findOrgBOByOrgId(userInfo.getOrgId());
		if (groupBeen != null) {
			groupName = gomeGmpOrgManageBS.getAllLevelOrgName(groupBeen, groupBeen.getOrgName());
		}
		gomeGmpResUserVO.setOrgGroup(groupName);
		return new ModelAndView("/orgManage/myAccount");
	}
	
	/**
	 * 我的组织初始化
	 * 
	 * @return String
	 * @author rl_wanglijie
	 */
	@RequestMapping("/myOrganization")
	public ModelAndView myOrganization(@ModelAttribute GomeGmpResOrgVO gomeGmpResOrgVO) {
		GomeGmpResUserBO userInfo = BusinessUtil.getLoginUser(getRequest());
		List<GomeGmpResOrgVO> orgList = null;
		if (userInfo != null && gomeGmpResOrgVO != null) {
			orgList = gomeGmpOrgManageBS.getMyOrganization(userInfo, gomeGmpResOrgVO);
		}
		gomeGmpResOrgVO.setGomeGmpResOrgVOList(orgList);
		return new ModelAndView("/orgManage/myOrganization");
	}
	
	/**
	 * 取得小组成员
	 ***/
	@RequestMapping(value = "/getTeamMembers", method = RequestMethod.POST)
	public List<GomeGmpResOrgVO> getTeamMembers(@Param("orgId") String orgId) {
		List<GomeGmpResOrgVO> memberList = gomeGmpOrgManageBS.findTeamMembers(orgId,null);
		return memberList;
	}

	/**
	 * 修改组织
	 */
	@RequestMapping(value = "/updateOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message updateOrg(@ModelAttribute GomeGmpResOrgVO viewOrg) {
		Message msg = null;
		if (viewOrg != null) {
			Map<String, Object> updateMap = gomeGmpOrgManageBS.updateOrg(viewOrg, BusinessUtil.getLoginUser(getRequest()));
			if (updateMap != null) {
				int upeOrgRow = (int) updateMap.get("upeOrgRow");
				if (upeOrgRow > 0) {
					msg = Message.success("修改组织成功");
					msg.setData(viewOrg.getOrgId());
				} else {
					msg = Message.failure("修改组织失败");
				}
			} else {
				msg = Message.failure("修改组织失败");
			}
		} else {
			msg = Message.failure("参数错误,修改组织失败");
		}
		return msg;
	}

	/**
	 * 添加组织
	 * 
	 * @param viewOrg
	 * @return
	 */
	@RequestMapping(value = "/addOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message addOrg(@ModelAttribute GomeGmpResOrgVO viewOrg) {
		Message msg = null;
		if (viewOrg != null) {
			Map<String, Object> addMap = gomeGmpOrgManageBS.addOrg(viewOrg);
			if (addMap != null) {
				int addOrgRow = (int) addMap.get("addOrgRow");
				GomeGmpResOrgBO addGmpOrgVo = (GomeGmpResOrgBO) addMap.get("addGmpOrgVo");
				if (addOrgRow > 0) {
					msg = Message.success("添加组织成功");
					msg.setData(addGmpOrgVo.getOrgId());
				} else {
					msg = Message.failure("添加组织失败");
				}
			}
		} else {
			msg = Message.failure("参数错误,添加组织失败");
		}
		return msg;
	}

	/**
	 * 添加成员
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/addMember", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message addMember(@ModelAttribute GomeGmpResOrgVO viewOrg) {
		Message msg = null;
		if(viewOrg!=null && StringUtils.isNoneEmpty(viewOrg.getOrgLeader()) && StringUtils.isNotEmpty(viewOrg.getOrgId())){
			int addUserRow = gomeGmpResUserBS.updateGomeGmpResUser(viewOrg);
			if (addUserRow > 0) {
				msg =  Message.success("添加成员成功");
				List<GomeGmpResOrgVO> memberList = gomeGmpOrgManageBS.findTeamMembers(viewOrg.getOrgId(),viewOrg.getOrgLeader());
				msg.setData(memberList);
			} else {
				msg =  Message.success("添加组员失败");
			}
		}else{
			msg = Message.failure("参数错误,添加组员失败");
		}
		return msg;
	}

	/**
	 * 
	 * 删除组织
	 * 
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = "/deleteOrg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message deleteOrg(@Param("orgId") String orgId) {
		Message msg = null;
		if (StringUtils.isNotBlank(orgId)) {
			int delOrgRow = gomeGmpOrgManageBS.deleteOrg(orgId,BusinessUtil.getLoginUser(getRequest()));
			if (delOrgRow > 0) {
				msg = Message.success("删除组织成功");
			} else {
				msg = Message.failure("删除组织失败");
			}
		} else {
			msg = Message.failure("参数错误,删除组织失败");
		}
		return msg;
	}

	/**
	 * 删除成员
	 */
	@RequestMapping(value = "/deleteMember", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message deleteMember(@Param("id") Long id) {
		Message msg = null;
		if (id != null) {
			int userDelRow = gomeGmpResUserBS.clearGomeGmpResUserOrgIdWithId(id);
			if (userDelRow > 0) {
				msg = Message.success("删除小组组员成功");
			} else {
				msg = Message.failure("删除小组组员失败");
			}
		} else {
			msg = Message.failure("参数错误,删除小组组员失败");
		}
		return msg;
	}

	/**
	 * 获取下级组织列表
	 * 
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = "/getNextLevel", method = RequestMethod.POST)
	public List<GomeGmpResOrgVO> getNextLevel(@Param("orgId") String orgId) {
		List<GomeGmpResOrgVO> childList = gomeGmpOrgManageBS.getLowerLeverOrgByParent(orgId);
		return childList;
	}
	
	/**
	 * 组织结构下拉列表
	 **/
	@SuppressWarnings("unused")
	@RequestMapping(value = "/getOrgFramework", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<GomeGmpResOrgVO> getOrgFramework(@RequestParam(value = "orgIds[]") String[] orgIds) {
		GomeGmpResUserBO userBo = BusinessUtil.getLoginUser(getRequest());
		List<GomeGmpResOrgVO> orgList = gomeGmpOrgManageBS.getOrgFramework(orgIds);
		return orgList;
	}
	
	/**
	 * 组织结构下拉列表查询条件
	 **/
	@RequestMapping(value = "/getQueryCondition", method = RequestMethod.POST)
	public List<GomeGmpResOrgVO> getQueryCondition(@RequestBody GomeGmpResOrgVO gomeGmpResOrgVO) {
		List<GomeGmpResOrgVO> orgList = gomeGmpOrgManageBS.getOrgQueryCondition(gomeGmpResOrgVO.getOrgIdList(),gomeGmpResOrgVO.getQueryType(),BusinessUtil.getLoginUser(getRequest()));
		return orgList;
	}
}
