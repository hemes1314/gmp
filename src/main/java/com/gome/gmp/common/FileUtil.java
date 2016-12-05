package com.gome.gmp.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;

/**
 * 普通文件读写公共类
 * 
 * @author wubin
 */
public class FileUtil {

	protected static Logger LOG = LoggerFactory.getLogger(FileUtil.class);

	public static List<File> files = null;

	public static String filePath = null;

	public static File file = null;

	private FileUtil() {
		files = new ArrayList<File>();
	}

	/**
	 * 设置文件路径 <功能详细描述>
	 * 
	 * @param filePath
	 * @see [类、类#方法、类#成员]
	 */
	public static void setFilePath(String filePath) {
		FileUtil.filePath = filePath;
		FileUtil.file = new File(filePath);
	}

	/**
	 * 创建文件 不存在则创建新文件,存在则不创建新文件
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public static void createFile() {
		try {
			if (!file.exists()) {
				if (filePath.indexOf("/") != -1) {
					createDir(filePath.substring(0, filePath.lastIndexOf("/")));
				}
				file.createNewFile();
				LOG.debug(filePath + ":创建文件成功。");
			} else {
				LOG.debug(filePath + ":文件已存在。");
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * 创建新文件 存在不存在都废弃原文件创建新文件
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public static void createNewFile() {
		try {
			System.out.println(file.exists());
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * 写入文件 没有此文件，则生成新文件并写入
	 * 
	 * @param String
	 *            lineStr 写入文件的内容
	 * @see [类、类#方法、类#成员]
	 */
	public static void writeFile(String lineStr) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			writer.write(lineStr);
			writer.newLine();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 写入文件 <功能详细描述>
	 * 
	 * @param ArrayList<String>
	 *            linesList 写入文件的内容 按行组成的字符串集合
	 * @see [类、类#方法、类#成员]
	 */
	public static void writeFile(List<String> linesList) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			for (String line : linesList) {
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 读取文件将内容按行读入字符串集合 <功能详细描述>
	 * 
	 * @param filePath
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static ArrayList<String> readFile() {
		ArrayList<String> contentList = new ArrayList<String>();
		if (file.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String tmpStr = null;
				while ((tmpStr = reader.readLine()) != null) {
					contentList.add(tmpStr);
				}
			} catch (FileNotFoundException e) {
				LOG.error(e.getMessage(), e);
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		} else {
			LOG.error(filePath + "：文件不存在或文件路径有误。");
		}
		return contentList;
	}

	/**
	 * 清空文件内容 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public static void contentClear() {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 删除文件 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public static void deleteFile() {
		if (file.exists()) {
			file.delete();
		} else {
			LOG.error(filePath + ":删除文件失败，文件或路径不存在。");
		}
	}

	/**********************************************************************/
	/**
	 * 创建目录
	 * 
	 * @param destDirName("E:/ExcelTEMP/")
	 */
	public static void createDir(String dirPath) {
		File dir = new File(dirPath);
		// 如果目录不存在则创建目录
		if (!dir.exists()) {
			if (!dirPath.endsWith(File.separator)) {
				dirPath = dirPath + File.separator;
			}
			// 创建单个目录
			if (dir.mkdirs()) {
				LOG.debug(dirPath + ":目录创建成功！");
			} else {
				LOG.debug(dirPath + ":目录创建失败！");
			}
		} else {
			LOG.debug(dirPath + ":目录已存在。");
		}
	}

	/**
	 * 删除文件夹 文件夹及以下文件夹及文件
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public static void deleteDir(String dirPath) {
		File dir = new File(dirPath);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File filei : files) {
					if (!filei.isDirectory()) {
						filei.delete();
					}
				}
				for (File filei : files) {
					if (filei.isDirectory()) {
						deleteDir(filei.getPath());
					}
				}
			}
			dir.delete();
		}
		LOG.debug("删除目录成功。");
	}

	/**
	 * 读取某文件夹下的所有文件
	 * 
	 * @param 目录路径
	 *            如f:/word/ return list 读取到的所有文件
	 */
	public static File[] readFileFromDir(String dirPath) {
		File file = new File(dirPath);
		File[] files = file.listFiles();
		return files;
	}

	/**
	 * 获取文件或文件夹大小
	 * 
	 * @param strName
	 *            文件或目录路径
	 * @return
	 */
	public static long getSize(String path) {
		long TotalSize = 0L;
		File f = new File(path);
		if (f.isFile()) {
			return f.length();
		} else {
			if (f.isDirectory()) {
				File[] contents = f.listFiles();
				if (contents != null) {
					for (int i = 0; i < contents.length; i++) {
						if (contents[i].isFile()) {
							TotalSize += contents[i].length();
						} else {
							if (contents[i].isDirectory()) {
								TotalSize += getSize(contents[i].getPath());
							}
						}
					}
				}
			}
		}
		return TotalSize;
	}

	/**
	 * 递归求取目录及子目录文件个数
	 * 
	 * @param 文件个数
	 * @return
	 */
	public static long getListCount(File f) {
		long size = 0;
		File flist[] = f.listFiles();
		if (flist != null) {
			size = flist.length;
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getListCount(flist[i]);
					size--;
				}
			}
		}
		return size;
	}

	/**
	 * 递归取目录及子目录文件
	 * 
	 * @param 文件个数
	 * @return
	 */
	public static void getAllFiles(String root) {
		File file = new File(root);
		File[] subFile = file.listFiles();
		if (subFile != null) {
			for (int i = 0; i < subFile.length; i++) {
				File f = subFile[i];
				if (f.isDirectory()) {
					getAllFiles(subFile[i].getAbsolutePath());
				} else if (f.isFile()) {
					files.add(f);
				}
			}
		}
	}

	/**************************************
	 * 判断文件格式是否合法*start
	 *******************************************/
	/**
	 * 判断单个文件格式是否合法
	 * 
	 * @param allowedExt
	 *            允许的文件类型(文件后缀)
	 * @param fileUrl
	 *            文件名
	 * @return boolean
	 */
	public static boolean checkExt(String[] allowedExt, String fileUrl) {
		boolean isok = false;
		// 获取上传文件的扩展名
		String fileExt = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
		int flag = 0;
		for (; flag < allowedExt.length; flag++) {
			if (fileExt.equals(allowedExt[flag])) {
				break;
			}
		}
		if (flag != allowedExt.length) {
			isok = true;
		}
		return isok;
	}

	/**
	 * 判断多个文件格式是否合法
	 * 
	 * @param allowedExt
	 *            允许的文件类型(文件后缀)
	 * @param fileUrl
	 *            文件名集合
	 * @return boolean
	 */
	public static boolean checkExts(String[] allowedExt, List<String> fileUrls) {
		boolean isok = true;
		for (int i = 0; i < fileUrls.size(); i++) {
			String fileUrl = fileUrls.get(i);
			boolean flag = checkExt(allowedExt, fileUrl);
			if (!flag) {
				isok = false;
			}
		}
		return isok;
	}

	/************************************** 判断文件格式是否合法end *******************************************/
	/**
	 * TEST
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/* //目录、文件操作测试 FilesUtil.createDir("E:/ExcelTEMP/");
		 * //FilesUtil.readFile("f:/word/"); */
		/* //检查文件格式是否合法测试 String[] allowedExt = new String[] { "jpg", "jpeg",
		 * "gif", "bmp", "png" };// 限定文件格式 List<String> ls = new
		 * ArrayList<String>(); ls.add("model1.jpg"); ls.add("model2.gif");
		 * boolean isok = CheckExt.checkExts(allowedExt, ls);
		 * System.out.println(isok); */
		setFilePath("test.txt");
		// createFile();
		// contentClear();
		writeFile("test->" + Math.random());
		ArrayList<String> list = readFile();
		System.out.println(list);
	}
}