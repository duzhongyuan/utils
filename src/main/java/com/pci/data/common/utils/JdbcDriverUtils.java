/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: JdbcDriverUtils.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils 
 * @Description: JDBC驱动工具类
 * @author: dzy   
 * @date: 2016年12月23日 上午11:01:59 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/** 
 * @ClassName: JdbcDriverUtils 
 * @Description: JDBC驱动工具类
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月23日 上午11:01:59  
 */
public class JdbcDriverUtils {

	private static LogUtils logger = new LogUtils(JdbcDriverUtils.class);
	
	/**
	 * @Title: deregisterJdbcDrivers 
	 * @Description: 注销JDBC驱动
	 * @since: 0.0.1
	 */
	public static void deregisterJdbcDrivers(){
		final Enumeration<Driver> drivers = DriverManager.getDrivers();
		if (drivers == null) {
			logger.warn("Can not find the JDBC drivers.");
			return;
		}
		Driver driver = null;
		while (drivers.hasMoreElements()) {
			driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				logger.info("Deregister JDBC driver '" + driver + "' successful.");
			} catch (SQLException e) {
				logger.error("Deregister JDBC driver '" + driver + "' error.", e);
			}
		}
	}
	
}
