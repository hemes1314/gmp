package com.gome.gmp.web;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gome.framework.AppContext;
import com.gome.framework.Env;
import com.gome.framework.base.BaseRestController;
import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.gmp.business.GomeGmpDataManageBS;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.dao.GomeGmpResLogDAO;
import com.gome.gmp.model.bo.GomeGmpResDatasynBO;
import com.gome.gmp.model.vo.GomeGmpResLogVO;

/**
 * 下载
 * 
 * @author wubin
 */
@RestController
public class FileDownloadCTL extends BaseRestController {

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Resource
	private GomeGmpResLogDAO gomeGmpResLogDAO;

	@Resource
	private GomeGmpDataManageBS gomeGmpDataManageBS;

	/**
	 * 下载
	 * 
	 * @param proId
	 * @param filePath
	 * @param fileName
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author wubin
	 */
	@RequestMapping(value = "/{fileId}/download", method = RequestMethod.GET)
	public ModelAndView download(@PathVariable String fileId, HttpServletResponse response) throws Exception {
		GomeGmpResDatasynBO dataSyn = gomeGmpDataManageBS.findGomeGmpResDatasynBOById(Long.valueOf(fileId));
		String proType = String.valueOf(dataSyn.getProType());
		String filePath = dataSyn.getFilePath();
		String fileName = dataSyn.getFileName();
		String proId = dataSyn.getProId();
		// 获取上传路径
		Env env = AppContext.getEnv();
		String path = null;
		if (Constants.UPLOAD_PRO_TYPE_PROJECT.equals(proType)) {
			path = env.getProperty(Constants.UPLOAD_PATH_KEY_PROJECT);
		} else if (Constants.UPLOAD_PRO_TYPE_AGILE.equals(proType)) {
			path = env.getProperty(Constants.UPLOAD_PATH_KEY_AGILE);
		} else if (Constants.UPLOAD_PRO_TYPE_DEMAND.equals(proType)) {
			path = env.getProperty(Constants.UPLOAD_PATH_KEY_DEMAND);
		} else {
			path = env.getProperty(Constants.UPLOAD_PATH_KEY_OTHERS);
		}
		path = path.replaceAll("//", "/");
		String downLoadPath = path + filePath;
		LOG.info("下载文件:" + downLoadPath);
		try {
			// BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			// download(response, bis, fileName);
			try {
				if (LOG.isDebugEnabled()) {
					// LOG.debug("fileName encoding is " + BusinessUtil.getEncoding(fileName));
				}
				// 字符串在java内存中总是按unicode编码存储的,将字符串所表示的字符按照charset编码,并以字节方式表示。
				// 将字节数组按照charset编码,使用ISO-8859-1编码
				// 由于中文操作系统的编码为GBK或gb2312,所以使用gbk编码
				fileName = new String(fileName.getBytes("gbk"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e1) {
				LOG.info("导出excel表格异常：fileName转码出错", e1);
			}
			response.setContentType("application/x-msdownload");
			// 允许HTTPS下载文件
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");// 关键：文件名加上双引号
			ServletOutputStream sos = null;
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(downLoadPath));
			try {
				sos = response.getOutputStream();
				int i = -1;
				byte[] buf = new byte[8192];
				while ((i = is.read(buf)) != -1) {
					sos.write(buf, 0, i);
					sos.flush();
				}
				sos.flush();
				sos.close();
			} catch (Exception e) {
				LOG.info("导出异常：" + e.getMessage(), e);
			} finally {
				try {
					if (sos != null) {
						sos.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (Exception e) {
					LOG.info("导出异常，关闭流文件失败。", e);
				}
			}
			// 记录日志
			if ("1".equalsIgnoreCase(proType) || "2".equals(proType)) {
				GomeGmpResLogVO log = new GomeGmpResLogVO();
				log.setProId(proId);
				log.setOperateType("下载");
				log.setContent("下载了资料\"" + fileName + "\"");
				gomeGmpResLogDAO.saveGomeGmpResLogBO(log);
			}
			return null;
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage(), e);
			return new ModelAndView("/error_filedownload");
		}
	}
}
