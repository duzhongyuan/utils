/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: FileUtils.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils 
 * @Description: 文件工具类
 * @author: dzy   
 * @date: 2016年12月23日 上午10:32:38 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/** 
 * @ClassName: FileUtils 
 * @Description: 文件工具类
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月23日 上午10:32:38  
 */
public class FileUtils {

	private static LogUtils logger = new LogUtils(FileUtils.class);

	/**
	 * 附加文件内容
	 * @param filePath
	 * @param content
	 * @param charsetName
	 * @return
	 */
	public static boolean appendFileText(String filePath, 
			String content, String charsetName) {
		return writeFileText(filePath, content, charsetName, true);
	}

	/**
	 * 写入文件内容
	 * @param filePath
	 * @param content
	 * @param charsetName
	 * @return
	 */
	public static boolean writeFileText(String filePath, 
			String content, String charsetName) {
		return writeFileText(filePath, content, charsetName, false);
	}

	/**
	 * 写入文件内容
	 * @param filePath
	 * @param content
	 * @param charsetName
	 * @param append
	 * @return
	 */
	public static boolean writeFileText(String filePath, String content, 
			String charsetName, boolean append) {
		OutputStreamWriter writer = null;
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(filePath, append);
			writer = new OutputStreamWriter(stream, charsetName);
			writer.write(content);
			writer.flush();
			return true;
		} catch (Exception e) {
			logger.error("Write file error. filePath=" + filePath, e);
			return false;
		} finally {
			IOUtils.closeQuietly(stream);
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 * 创建文件路径
	 * <p>
	 * 如果文件路径不存在，则创建，否则不创建。
	 * @param path
	 */
	public static void mkdirs(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
}
