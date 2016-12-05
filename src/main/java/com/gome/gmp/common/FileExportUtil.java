package com.gome.gmp.common;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.gome.gmp.common.filter.CommandContext;

public class FileExportUtil {

	private static final Logger logger = Logger.getLogger(FileExportUtil.class);

	/**
	 * 导出excel
	 * 
	 * @param fileName
	 * @param title
	 * @param list
	 * @author wubin
	 */
	public static void getExportExcel(String fileName, List<String> title, List<Map<String, Object>> list) {
		try {
			fileName = new String(fileName.getBytes("gbk"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			logger.info("导出excel表格异常：fileName转码出错", e1);
		}
		HttpServletResponse response = CommandContext.getResponse();
		response.setContentType("application/x-msdownload");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=30");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); // 重点突出
		// 创建一个工作薄
		HSSFWorkbook work = new HSSFWorkbook();
		HSSFSheet sheet = work.createSheet(fileName);
		// 设置表头
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < title.size(); i++) {
			row.createCell(i).setCellValue(title.get(i));
		}
		// 添加数据
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			Map<String, Object> data = list.get(i);
			int j = 0;
			for (String key : data.keySet()) {
				row.createCell(j, HSSFCell.CELL_TYPE_STRING).setCellValue(String.valueOf(data.get(key)));
				j++;
			}
		}
		sheet = null;
		row = null;
		// 让用户下载excel
		downloadFile(CommandContext.getRequest(), CommandContext.getResponse(), work, fileName);
	}

	/**
	 * 下载excel文件
	 * 
	 * @param request
	 * @param response
	 * @param workbook
	 * @param fileName
	 * @author wubin
	 */
	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, HSSFWorkbook workbook, String fileName) {
		OutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			os = response.getOutputStream();
			bos = new BufferedOutputStream(os);
			workbook.write(bos);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		} finally {
			try {
				if (null != bos) {
					bos.close();
					bos = null;
				}
				if (null != os) {
					os.close();
					os = null;
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
	}

	/**
	 * excel数据模板导出
	 * 
	 * @param title
	 * @param colnums
	 * @param list
	 * @param response
	 * @param fileName
	 * @param numberList
	 * @return
	 */
	public static String exportDataToExcelXLSX(String title, String[] colnums, List<String[]> list, HttpServletResponse response, String fileName, List<Integer> numberList) {
		String result = "error";
		OutputStream os = null;
		try {
			fileName = new String(fileName.getBytes("gbk"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			logger.info("导出excel表格异常：fileName转码出错", e1);
		}
		response.setContentType("application/x-msdownload");
		// 允许HTTPS下载文件
		// 页面缓存机制 Public指示响应可被任何缓存区缓存。
		response.setHeader("Pragma", "public");
		// 指定请求和响应遵循的缓存机制 max-age指示客户机可以接收生存期不大于指定时间（以秒为单位）的响应。
		response.setHeader("Cache-Control", "max-age=30");
		response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + ".xlsx\"");
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		Sheet sheet = workbook.createSheet(title);
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		Row row = sheet.createRow(0);
		if (colnums.length > 0) {
			for (int i = 0; i < colnums.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(style);
				cell.setCellValue(colnums[i]);
			}
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(i + 1);
				String[] pStrings = list.get(i);
				for (int j = 0; j < pStrings.length; j++) {
					String vl = pStrings[j];
					if (StringUtils.isEmpty(vl)) {
						vl = "";
					}
					if (numberList != null && numberList.size() > 0) {
						if (numberList.contains(j)) {
							row.createCell((pStrings.length - (pStrings.length - j))).setCellValue(Double.parseDouble(StringUtils.isNotEmpty(vl) ? vl : "0"));
						} else {
							row.createCell((pStrings.length - (pStrings.length - j))).setCellValue(vl);
						}
					} else {
						row.createCell((pStrings.length - (pStrings.length - j))).setCellValue(vl);
					}
				}
			}
		} else {
			logger.info("导出数据错误,数据集合为 0 ");
		}
		// 自适应宽度
		if (colnums.length > 0) {
			for (int i = 0; i < colnums.length; i++) {
				sheet.autoSizeColumn(i, true);
			}
		}
		try {
			os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
			result = "success";
		} catch (Exception e) {
			logger.info("导出excel表格异常：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (Exception e) {
				logger.info("导出excel表格异常，关闭流文件失败。", e);
			}
		}
		return result;
	}
}
