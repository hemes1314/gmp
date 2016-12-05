package com.gome.gmp.web;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.gome.framework.base.BaseRestController;
import com.gome.framework.util.DateUtil;
import com.gome.gmp.business.GomeGmpDataManageBS;
import com.gome.gmp.business.GomeGmpResUserBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.mybaits.plugins.page.PageModel;
import com.gome.gmp.model.bo.GomeGmpResDatasynBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResDatasynVO;

@RestController
@RequestMapping("/dataManage")
public class GomeGmpDataManageCTL extends BaseRestController {

	@Resource
	private GomeGmpDataManageBS gomeGmpDataManageBS;

	@Resource
	private GomeGmpResUserBS gomeGmpResUserBS;

	/**
	 * 项目资料
	 * 
	 * @return String
	 * @author rl_wanglijie
	 */
	@RequestMapping("/projectData")
	public ModelAndView projectData() {
		return new ModelAndView("/dataManage/projectData");
	}

	/**
	 * 敏捷需求资料
	 * 
	 * @return String
	 * @author rl_wanglijie
	 */
	@RequestMapping("/needData")
	public ModelAndView needData() {
		return new ModelAndView("/dataManage/needData");
	}

	/**
	 * 其他资料
	 * 
	 * @return String
	 * @author rl_wanglijie
	 */
	@RequestMapping("/otherData")
	public ModelAndView otherData() {
		return new ModelAndView("/dataManage/otherData");
	}

	/**
	 * 项目/敏捷
	 ***/
	@RequestMapping(value = "/getProjectData/{pageNum}", method = RequestMethod.POST, produces = "application/json")
	public PageModel getProjectData(@PathVariable Integer pageNum, @Param("strSearch") String strSearch, @Param("proType") Integer proType) {
		BusinessUtil.setPageNum(pageNum);
		GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
		gomeGmpResDatasynVO.setStrSearch(strSearch);
		gomeGmpResDatasynVO.setProType(proType);
		Page<GomeGmpResDatasynVO> projects = gomeGmpDataManageBS.findGomeGmpResDatasynProjectBOByCondition(gomeGmpResDatasynVO);
		List<GomeGmpResDatasynVO> projectsData = gomeGmpDataManageBS.findGomeGmpResDatasynBOByCondition(gomeGmpResDatasynVO);
		for (int i = 0; i < projects.size(); i++) {
			GomeGmpResDatasynVO project = projects.get(i);
			for (int j = 0; j < projectsData.size(); j++) {
				GomeGmpResDatasynVO data = projectsData.get(j);
				if (project.getProId().equals(data.getProId())) {
					project.getDataList().add(data);
				}
			}
		}
		PageModel model = new PageModel();
		model.setPageData(projects);
		return model;
	}

	/**
	 * 其他资料
	 ***/
	@RequestMapping(value = "/getOtherData/{pageNum}", method = RequestMethod.POST, produces = "application/json")
	public PageModel getOtherData(@PathVariable Integer pageNum, @Param("strSearch") String strSearch) {
		BusinessUtil.setPageNum(pageNum);
		GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
		gomeGmpResDatasynVO.setStrSearch(strSearch);
		Page<GomeGmpResDatasynVO> datas = gomeGmpDataManageBS.findGomeGmpResDatasynFileBOByCondition(gomeGmpResDatasynVO);
		PageModel model = new PageModel();
		model.setPageData(datas);
		return model;
	}

	/**
	 * 上传文件
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public GomeGmpResDatasynBO uploadFile(@ModelAttribute GomeGmpResDatasynBO gomeGmpResDatasynBO, @RequestParam("file") CommonsMultipartFile[] files) {
		int saveResult = 0;
		String proType = gomeGmpResDatasynBO.getProType() + "";
		// 上传文件
		List<GomeGmpResDatasynVO> uploadFileInfo = BusinessUtil.uploadFiles(proType, files, getRequest());
		GomeGmpResUserBO userInfo = BusinessUtil.getLoginUser(getRequest());
		for (GomeGmpResDatasynVO uploadFile : uploadFileInfo) {
			// GomeGmpResDatasynBO gomeGmpResDatasynBO = new
			// GomeGmpResDatasynBO();
			gomeGmpResDatasynBO.setFileName(uploadFile.getFileName());
			gomeGmpResDatasynBO.setFilePath(uploadFile.getFilePath());
			gomeGmpResDatasynBO.setPhysicalPath(uploadFile.getPhysicalPath());
			gomeGmpResDatasynBO.setUploadUserId(userInfo.getId().intValue());
			gomeGmpResDatasynBO.setUploadTime(DateUtil.currDate());
			saveResult = gomeGmpDataManageBS.saveGomeGmpResDatasyn(gomeGmpResDatasynBO);
		}
		if (saveResult > 0) {
			return gomeGmpDataManageBS.findNewDatasynBOByUserId(gomeGmpResDatasynBO.getUploadUserId().longValue());
		}
		return null;
	}

	/**
	 * 删除文件
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST, produces = "application/json")
	public Integer deleteFileData(@Param("fileId") Long fileId) {
		Integer res = gomeGmpDataManageBS.deleteGomeGmpResDatasynBOById(fileId);
		return res;
	}

	/**
	 * 下载文件
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST, produces = "application/json")
	public PageModel downloadFile(@Param("fileId") Integer fileId) {
		return null;
	}
}
