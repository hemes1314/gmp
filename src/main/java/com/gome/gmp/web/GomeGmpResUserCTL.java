package com.gome.gmp.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpResUserBS;
import com.gome.gmp.common.base.Message;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.ws.login.pi.DesUtil;
import com.gome.gmp.ws.login.pi.LoginUtil;

/**
 * 用户信息控制层
 */
@RestController
public class GomeGmpResUserCTL extends BaseRestController {

	@Resource(name = "gomeGmpResUserBS")
	private GomeGmpResUserBS gomeGmpResUserBS;

	/**
	 * 登录页面
	 * 
	 * @return String
	 * @author be_kangfengping
	 */
	@RequestMapping("/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	/**
	 * 主页
	 * 
	 * @return String
	 * @author be_kangfengping
	 */
	@RequestMapping("/main")
	public ModelAndView main() {
		return new ModelAndView("main");
	}

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @param request
	 * @param model
	 * @return String
	 * @author be_kangfengping
	 */
	@RequestMapping(value = "/toLogin", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public Map<String, String> adminis(@Param("username") String username, @Param("password") String password, HttpServletResponse response) {
		Map<String, String> resultMap = new HashMap<String, String>();
		// 管理员用户不进行OC库密码验证
		if (!"admin".equals(username)) {
			boolean isOK = LoginUtil.checkLogin(username, password);
			if (!isOK) {
				resultMap.put("resultData", Constants.FAILD);
				resultMap.put("content", "请输入正确的密码");
				return resultMap;
			}
		} else {
			try {
				password = DesUtil.encrypt(password, "gomedscn");
			} catch (Exception e1) {
				e1.printStackTrace();
				resultMap.put("resultData", Constants.FAILD);
				resultMap.put("content", "请输入正确的密码");
				return resultMap;
			}
			GomeGmpResUserBO user = gomeGmpResUserBS.adminLogin(username, password);
			if (user == null) {
				resultMap.put("resultData", Constants.FAILD);
				resultMap.put("content", "请输入正确的密码");
				return resultMap;
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String lastLog = df.format(new Date());
				user.setLastLogtime(lastLog);
				// 修改数据库中的登录时间
				gomeGmpResUserBS.updateGomeGmpResUserLastLoginTime(user);
				resultMap.put("resultData", Constants.SUCCESS);
				resultMap.put("targetUrl", Constants.PROJECT_PATH + "/layout");
				try {
					Cookie cookie_u = new Cookie("user", URLEncoder.encode(JSON.toJSONString(user), "UTF-8"));
					cookie_u.setPath("/gmp"); // 如果路径为/则为整个tomcat目录有用
					cookie_u.setMaxAge(-1);// 30分 1秒*60=1分*60分=1小时*24=1天*7=7天
					response.addCookie(cookie_u);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return resultMap;
			}
		}
		GomeGmpResUserBO u = gomeGmpResUserBS.testUserName(username);
		if (u == null) {
			GomeGmpResUserBO gomeGmpResUserBO = new GomeGmpResUserBO();
			gomeGmpResUserBO.setPernr("");
			gomeGmpResUserBO.setUserId(username);
			gomeGmpResUserBO.setUserName(username);
			gomeGmpResUserBO.setOrgId("");
			gomeGmpResUserBO.setEmail("");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String lastLog = df.format(new Date());
			gomeGmpResUserBO.setLastLogtime(lastLog);
			gomeGmpResUserBS.saveGomeGmpResUserBO(gomeGmpResUserBO);
		}
		GomeGmpResUserBO user = gomeGmpResUserBS.userLogin(username, password);
		if (user == null) {
			resultMap.put("resultData", Constants.FAILD);
			resultMap.put("content", "请输入正确的用户名");
			return resultMap;
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String lastLog = df.format(new Date());
			user.setLastLogtime(lastLog);
			// 修改数据库中的登录时间
			gomeGmpResUserBS.updateGomeGmpResUserLastLoginTime(user);
			resultMap.put("resultData", Constants.SUCCESS);
			resultMap.put("targetUrl", Constants.PROJECT_PATH + "/layout");
			try {
				Cookie cookie_u = new Cookie("user", URLEncoder.encode(JSON.toJSONString(user), "UTF-8"));
				cookie_u.setPath("/gmp"); // 如果路径为/则为整个tomcat目录有用
				cookie_u.setMaxAge(-1);// 30分 1秒*60=1分*60分=1小时*24=1天*7=7天
				response.addCookie(cookie_u);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return resultMap;
		}
	}

	/**
	 * 根据条件查询用户
	 * @param gomeGmpResUserBO
	 * @return
	 */
	@RequestMapping(value = "/orgManagerMatchingUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message orgManagerMatchingUser(@RequestBody GomeGmpResUserBO gomeGmpResUserBO) {
		Message msg = null;
		String tempUserStr = gomeGmpResUserBO.getUserName();
		List<GomeGmpResUserBO> resUserList = gomeGmpResUserBS.matchingUserByParms(tempUserStr,"orgManager");
		if(resUserList != null && resUserList.size()>0){
			msg = Message.success();
			msg.setData(resUserList);
		}else{
			msg = Message.failure();
		}
		return msg;
	}
	
	/**
	 * 查询用户
	 ***/
	@RequestMapping(value = "/findUser", method = RequestMethod.POST)
	public List<GomeGmpResUserBO> findUser(@RequestBody GomeGmpResUserBO gomeGmpResUserBO) {
		List<GomeGmpResUserBO> bo = gomeGmpResUserBS.matchingUserByParms(gomeGmpResUserBO.getUserName(),"");
		return bo;
	}

	@RequestMapping("/touser")
	public ModelAndView touser() {
		return new ModelAndView("/user/user");
	}

	/**
	 * 退出
	 * 
	 * @param request
	 * @return String
	 * @author be_kangfengping
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletResponse response) {
		Cookie cookie_u = new Cookie("user", "");
		cookie_u.setPath("/gmp"); // 如果路径为/则为整个tomcat目录有用
		cookie_u.setMaxAge(0);//
		response.addCookie(cookie_u);
		return new ModelAndView("login");
	}

	/**
	 * 修改管理员密码
	 ***/
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public Map<String, String> changePassword(@Param("oldpwd") String oldpwd, @Param("newpwd") String newpwd) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			oldpwd = DesUtil.encrypt(oldpwd, "gomedscn");
			newpwd = DesUtil.encrypt(newpwd, "gomedscn");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultData", Constants.FAILD);
			resultMap.put("content", "密码修改失败");
			return resultMap;
		}
		GomeGmpResUserBO user = gomeGmpResUserBS.adminLogin("admin", oldpwd);
		if (user == null) {
			resultMap.put("resultData", Constants.FAILD);
			resultMap.put("content", "请输入正确的密码");
			return resultMap;
		}
		int result = gomeGmpResUserBS.updateAdminPassword(newpwd);
		if (result != 1) {
			resultMap.put("resultData", Constants.FAILD);
			resultMap.put("content", "密码修改失败");
			return resultMap;
		}
		resultMap.put("resultData", Constants.SUCCESS);
		resultMap.put("content", "密码修改成功");
		return resultMap;
	}
}
