/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: LogUtils.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils 
 * @Description: 日志工具类
 * @author: dzy   
 * @date: 2016年12月22日 下午5:41:59 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/** 
 * @ClassName: LogUtils 
 * @Description: 日志工具类
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月22日 下午5:41:59  
 */
public class LogUtils {

	private Logger logger;

	public LogUtils(Class<?> clazz) {
		this.logger = Logger.getLogger(clazz);
	}

	public static LogUtils getLogger(Class<?> clazz) {
		return new LogUtils(clazz);
	}

	public static void config(String configFilePath) {
		DOMConfigurator.configure(configFilePath);
	}

	private String getPrefixStr(String logSeq) {
		return new StringBuffer().append("log_seq[").append(logSeq).append("]: ").toString();
	}

	public void debug(String message) {
		this.logger.debug(message);
	}

	public void debug(String message, Throwable t) {
		this.logger.debug(message, t);
	}

	public void debug(String logSeq, String message) {
		this.logger.debug(this.getPrefixStr(logSeq) + message);
	}

	public void debug(String logSeq, String message, Throwable t) {
		this.logger.debug(this.getPrefixStr(logSeq) + message, t);
	}

	public void info(String message) {
		this.logger.info(message);
	}

	public void info(String message, Throwable t) {
		this.logger.info(message, t);
	}

	public void info(String logSeq, String message) {
		this.logger.info(this.getPrefixStr(logSeq) + message);
	}

	public void info(String logSeq, String message, Throwable t) {
		this.logger.info(this.getPrefixStr(logSeq) + message, t);
	}

	public void warn(String message) {
		this.logger.warn(message);
	}

	public void warn(String message, Throwable t) {
		this.logger.warn(message, t);
	}

	public void warn(String logSeq, String message) {
		this.logger.warn(this.getPrefixStr(logSeq) + message);
	}

	public void warn(String logSeq, String message, Throwable t) {
		this.logger.warn(this.getPrefixStr(logSeq) + message, t);
	}

	public void error(String message) {
		this.logger.error(message);
	}

	public void error(String message, Throwable t) {
		this.logger.error(message, t);
	}

	public void error(String logSeq, String message) {
		this.logger.error(this.getPrefixStr(logSeq) + message);
	}

	public void error(String logSeq, String message, Throwable t) {
		this.logger.error(this.getPrefixStr(logSeq) + message, t);
	}

	public boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return this.logger.isInfoEnabled();
	}
	
}
