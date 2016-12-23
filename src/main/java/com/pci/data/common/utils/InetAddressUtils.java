/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: InetAddressUtils.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils 
 * @Description: 网络地址工具类
 * @author: dzy   
 * @date: 2016年12月23日 上午10:45:43 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;

/** 
 * @ClassName: InetAddressUtils 
 * @Description: 网络地址工具类
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月23日 上午10:45:43  
 */
public class InetAddressUtils {
	
	private static LogUtils logger = new LogUtils(InetAddressUtils.class);
	
	private static Object lock = new Object();
	private static String localIP = null;

	/**
	 * 获取本机IP地址
	 * @return
	 */
	public static String getLocalHostIP() {
		if (localIP != null) {
			return localIP;
		}
		synchronized (lock) {
			if (localIP == null) {
				localIP = getLocalHostIPv4();
			}
		}
		return localIP;
	}

	/**
	 * 获取本机IPv4地址
	 * @return
	 * 
	 * @since 2.0.0
	 */
	private static String getLocalHostIPv4() {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {
				public String run() throws Exception {
					NetworkInterface networkInterface = null;
					Enumeration<InetAddress> inetAddresses = null;
					InetAddress inetAddress = null;
					Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
					while (networkInterfaces.hasMoreElements()) {
						networkInterface = (NetworkInterface) networkInterfaces.nextElement();
						if (!networkInterface.isLoopback()) {
							inetAddresses = networkInterface.getInetAddresses();
							while (inetAddresses.hasMoreElements()) {
								inetAddress = (InetAddress) inetAddresses.nextElement();
								if (!inetAddress.isLoopbackAddress()) {
									if (inetAddress instanceof Inet4Address) {
										return inetAddress.getHostAddress();
									}
								}
							}
						}
					}
					return InetAddress.getLocalHost().getHostAddress();
				}
			});
		} catch (Exception e) {
			logger.error("Get local host IPv4 error.", e);
		}
		return "127.0.0.1";
	}
	
}
