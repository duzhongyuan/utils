/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	POSP系统
 *
 * @Title: FtpUtils.java 
 * @Prject: FinancialPlatform_ParamDownload
 * @Package: com.pci.FinancialPlatform.tools 
 * @Description: FTP工具类
 * @author: dzy   
 * @date: 2016年11月8日 下午8:37:03 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


/** 
 * @ClassName: FtpUtils 
 * @Description: FTP工具类
 * @since: 1.0.0
 * @author: dzy
 * @date: 2016年11月8日 下午8:37:03  
 */
public class FtpUtils {

	private static LogUtils logger = new LogUtils(FtpUtils.class);
	
	/**
	 * @Title: downloadFile 
	 * @Description: 从FTP服务器下载文件 
	 * @since: 1.0.0
	 * @param logSeq		流水号
	 * @param host 			FTP服务器地址
	 * @param port 			FTP服务器端口
	 * @param username 		FTP登录账号
	 * @param password 		FTP登录密码
	 * @param remotePath	FTP服务器上的相对路径
	 * @param fileName 		要下载的文件名
	 * @param localFile 	下载后保存到本地的文件
	 * @return
	 */
	public static boolean downloadFile(String logSeq, 
			String host, int port, String username, String password, 
			String remotePath, String fileName, File localFile) {
		return downloadFile(logSeq, host, port, username, password, 
				remotePath, fileName, localFile, true);
	}
	
	/**
	 * @Title: downloadFile 
	 * @Description: 从FTP服务器下载文件
	 * @since: 1.0.0
	 * @param logSeq		流水号
	 * @param host 			FTP服务器地址
	 * @param port 			FTP服务器端口
	 * @param username 		FTP登录账号
	 * @param password 		FTP登录密码
	 * @param remotePath 	FTP服务器上的相对路径
	 * @param fileName 		要下载的文件名
	 * @param localFile 	下载后保存到本地的文件
	 * @param isPassvieMode 是否采用被动模式
	 * @return
	 */
	public static boolean downloadFile(String logSeq, 
			String host, int port, String username, String password, 
			String remotePath, String fileName, File localFile, boolean isPassvieMode) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		
		try {
			// 连接FTP
			try {
				ftp.connect(host, port);
			} catch (SocketException e) {
				logger.error(logSeq, CustomStringUtils.append("无法连接到FTP服务器", host, ":", port), e);
				return result;
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("无法连接到FTP服务器", host, ":", port), e);
				return result;
			}
			
			// 登录FTP
			try {
				boolean loginResult = ftp.login(username, password);
				if (!loginResult) {
					logger.error(logSeq, CustomStringUtils.append("用户名", username, 
							"或密码******错误无法登陆到FTP服务器", host, ":", port));
					return result;
				}
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("用户", username, "无法登陆到FTP服务器", host, ":", port), e);
				return result;
			}
			
			// 采用被动模式
			if (isPassvieMode) {
				ftp.enterLocalPassiveMode();
			}
			
			// 转移到FTP服务器目录
			try {
				boolean changeResult = ftp.changeWorkingDirectory(remotePath);
				if (!changeResult) {
					logger.error(logSeq, "无法转移到FTP服务器目录" + remotePath);
					return result;
				}
			} catch (IOException e) {
				logger.error(logSeq, "转移到FTP服务器目录" + remotePath + "出错！");
				return result;
			}
			
			// 下载文件
			try {
				// 列出FTP服务器目录下的文件
				FTPFile[] fs = ftp.listFiles();
		        for (FTPFile ff : fs) {
		        	if (ff.getName().equals(fileName)) {
		        		logger.info(logSeq, CustomStringUtils.append("从FTP服务器", host, ":", port, "成功找到目标文件", fileName));
		        		OutputStream os = null;
		        		try {
							os = new FileOutputStream(localFile);
							ftp.retrieveFile(ff.getName(), os);
							result = true;
							logger.info(logSeq, CustomStringUtils.append("从FTP服务器", host, ":", port, "成功下载目标文件并保存到本地文件", localFile));
						} catch (Exception e) {
							logger.error(logSeq, CustomStringUtils.append("从FTP服务器", host, ":", port, "下载文件", fileName, "出错!"), e);
						} finally {
							IOUtils.closeQuietly(os);
						}
		        		break;
		        	}
		        }
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("从FTP服务器", host, ":", port, "下载文件", fileName, "出错!"), e);
			}
			
			// 退出FTP
			try {
				ftp.logout();
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("用户", username, "无法退出FTP服务器", host, ":", port), e);
			}
		} finally {
			disconnect(logSeq, ftp);
		}
		
	    return result;
	}
	
	/**
	 * @Title: uploadFile 
	 * @Description: 上传文件到FTP服务器
	 * @since: 1.0.0
	 * @param logSeq		流水号
	 * @param host 			FTP服务器地址
	 * @param port 			FTP服务器端口
	 * @param username 		FTP登录账号
	 * @param password 		FTP登录密码
	 * @param remotePath 	FTP服务器上的相对路径
	 * @param localFile 	要上传的本地文件
	 * @return
	 */
	public static boolean uploadFile(String logSeq, 
			String host, int port, String username, String password, 
			String remotePath, File localFile) {
		return uploadFile(logSeq, host, port, username, 
				password, remotePath, localFile, true);
	}
	
	/**
	 * @Title: uploadFile 
	 * @Description: 上传文件到FTP服务器
	 * @since: 1.0.0
	 * @param logSeq		流水号
	 * @param host 			FTP服务器地址
	 * @param port 			FTP服务器端口
	 * @param username 		FTP登录账号
	 * @param password 		FTP登录密码
	 * @param remotePath 	FTP服务器上的相对路径
	 * @param localFile 	要上传的本地文件
	 * @param isPassvieMode 是否采用被动模式
	 * @return
	 */
	public static boolean uploadFile(String logSeq, 
			String host, int port, String username, String password, 
			String remotePath, File localFile, boolean isPassvieMode) {  
	    boolean result = false;  
	    FTPClient ftp = new FTPClient();  
        
	    try {
			// 连接FTP
			try {
				ftp.connect(host, port);
			} catch (SocketException e) {
				logger.error(logSeq, CustomStringUtils.append("无法连接到FTP服务器", host, ":", port), e);
				return result;
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("无法连接到FTP服务器", host, ":", port), e);
				return result;
			}
			
			// 登录FTP
			try {
				boolean loginResult = ftp.login(username, password);
				if (!loginResult) {
					logger.error(logSeq, CustomStringUtils.append("用户名", username, 
							"或密码******错误无法登陆到FTP服务器", host, ":", port));
					return result;
				}
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("用户", username, "无法登陆到FTP服务器", host, ":", port), e);
				return result;
			}
			
			// 采用被动模式
			if (isPassvieMode) {
				ftp.enterLocalPassiveMode();
			}
			
			// 转移到FTP服务器目录
			try {
				boolean changeResult = ftp.changeWorkingDirectory(remotePath);
				if (!changeResult) {
					logger.error(logSeq, "无法转移到FTP服务器目录" + remotePath);
					return result;
				}
			} catch (IOException e) {
				logger.error(logSeq, "转移到FTP服务器目录" + remotePath + "出错！");
				return false;
			}
			
			// 上传文件
			logger.info(logSeq, CustomStringUtils.append("准备将本地文件", localFile, 
					"上传到FTP服务器", host, ":", port, "目录", remotePath));
			InputStream is = null;
    		try {
    			is = new FileInputStream(localFile);
    			// 使用二进制类型传输文件（解决上传zip文件后文件损坏问题）
    			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
    			ftp.storeFile(localFile.getName(), is);
    			result = true;
    			logger.info(logSeq, CustomStringUtils.append("成功将本地文件", localFile, 
    					"上传到FTP服务器", host, ":", port, "目录", remotePath));
			} catch (Exception e) {
				logger.error(logSeq, CustomStringUtils.append("将本地文件", localFile, 
						"上传到FTP服务器", host, ":", port, "目录", remotePath, "出错!"), e);
			} finally {
				IOUtils.closeQuietly(is);
			}
			
			// 退出FTP
			try {
				ftp.logout();
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("用户", username, "无法退出FTP服务器", host, ":", port), e);
			}
		} finally {
			disconnect(logSeq, ftp);
		}
		
	    return result;
	}
	
	/**
	 * @Title: listFile 
	 * @Description: 列出FTP某个目录下所有的文件名
	 * @since: 1.0.0
	 * @param logSeq		流水号
	 * @param host 			FTP服务器地址
	 * @param port 			FTP服务器端口
	 * @param username 		FTP登录账号
	 * @param password 		FTP登录密码
	 * @param remotePath 	FTP服务器上的相对路径
	 * @return
	 */
	public static List<String> listFile(String logSeq, 
			String host, int port, String username, String password, 
			String remotePath){
		return listFile(logSeq, host, port, username, password, remotePath, true);
	}
	
	/**
	 * @Title: listFile 
	 * @Description: 列出FTP某个目录下所有的文件名
	 * @since: 1.0.0
	 * @param logSeq		流水号
	 * @param host 			FTP服务器地址
	 * @param port 			FTP服务器端口
	 * @param username 		FTP登录账号
	 * @param password 		FTP登录密码
	 * @param remotePath 	FTP服务器上的相对路径
	 * @param isPassvieMode	是否开启被动模式
	 * @return
	 */
	public static List<String> listFile(String logSeq, 
			String host, int port, String username, String password, 
			String remotePath, boolean isPassvieMode){
		List<String> fileList = null;
		FTPClient ftp = new FTPClient();
		
		try {
			// 连接FTP
			try {
				ftp.connect(host, port);
			} catch (SocketException e) {
				logger.error(logSeq, CustomStringUtils.append("无法连接到FTP服务器", host, ":", port), e);
				return fileList;
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("无法连接到FTP服务器", host, ":", port), e);
				return fileList;
			}
			
			// 登录FTP
			try {
				boolean loginResult = ftp.login(username, password);
				if (!loginResult) {
					logger.error(logSeq, CustomStringUtils.append("用户名", username, 
							"或密码******错误无法登陆到FTP服务器", host, ":", port));
					return fileList;
				}
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("用户", username, "无法登陆到FTP服务器", host, ":", port), e);
				return fileList;
			}
			
			// 采用被动模式
			if (isPassvieMode) {
				ftp.enterLocalPassiveMode();
			}
			
			// 转移到FTP服务器目录
			try {
				boolean changeResult = ftp.changeWorkingDirectory(remotePath);
				if (!changeResult) {
					logger.error(logSeq, "无法切换到FTP服务器目录" + remotePath);
					return fileList;
				}
			} catch (IOException e) {
				logger.error(logSeq, "切换到FTP服务器目录:[" + remotePath + "]出错！");
				return fileList;
			}
			
			// 列出该目录下所有文件名称
			try {
				
				// 列出FTP服务器目录下的文件
				String[] fileNameArray = ftp.listNames();
				
				//将数组转成list
				if (null != fileNameArray) {
					fileList = Arrays.asList(fileNameArray);
				} else {
					logger.info(logSeq, "FTP服务器目录:[" + remotePath + "]下文件列表为空");
				}
				
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("从FTP服务器:[", host, ":", port, "]的目录[", remotePath, "]出错!"), e);
			}
			
			// 退出FTP
			try {
				ftp.logout();
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("用户", username, "无法退出FTP服务器", host, ":", port), e);
			}
		} finally {
			disconnect(logSeq, ftp);
		}
		
		return fileList;
	}
	
	/**
	 * @Title: moveFile 
	 * @Description: FTP上移动文件
	 * @since: 1.0.0
	 * @param logSeq		流水号
	 * @param host 			FTP服务器地址
	 * @param port 			FTP服务器端口
	 * @param username 		FTP登录账号
	 * @param password 		FTP登录密码
	 * @param from			移动前的文件路径
	 * @param to			移动后的文件路径
	 * @return
	 */
	public static boolean moveFile(String logSeq, 
			String host, int port, String username, String password, 
			String from, String to){
		return moveFile(logSeq, host, port, username, password, from, to, true);
	}
	
	/**
	 * @Title: moveFile 
	 * @Description: FTP上移动文件
	 * @since: 1.0.0
	 * @param logSeq		流水号
	 * @param host 			FTP服务器地址
	 * @param port 			FTP服务器端口
	 * @param username 		FTP登录账号
	 * @param password 		FTP登录密码
	 * @param from			移动前的文件路径
	 * @param to			移动后的文件路径
	 * @param isPassvieMode	是否开启被动模式
	 * @return
	 */
	public static boolean moveFile(String logSeq, 
			String host, int port, String username, String password, 
			String from, String to, boolean isPassvieMode){
	    boolean result = false;  
	    FTPClient ftp = new FTPClient();  
        
	    try {
			// 连接FTP
			try {
				ftp.connect(host, port);
			} catch (SocketException e) {
				logger.error(logSeq, CustomStringUtils.append("无法连接到FTP服务器", host, ":", port), e);
				return result;
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("无法连接到FTP服务器", host, ":", port), e);
				return result;
			}
			
			// 登录FTP
			try {
				boolean loginResult = ftp.login(username, password);
				if (!loginResult) {
					logger.error(logSeq, CustomStringUtils.append("用户名", username, 
							"或密码******错误无法登陆到FTP服务器", host, ":", port));
					return result;
				}
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("用户", username, "无法登陆到FTP服务器", host, ":", port), e);
				return result;
			}
			
			// 采用被动模式
			if (isPassvieMode) {
				ftp.enterLocalPassiveMode();
			}
			
			// 移动文件
			logger.info(logSeq, CustomStringUtils.append("准备将FTP服务器:[", host, ":", port, "]上的[", from, "]移动到[", to, "]"));
			
    		try {
    			
    			result = ftp.rename(from, to);
    			
    			if (result) {
    				logger.info(logSeq, CustomStringUtils.append("成功的将FTP服务器:[", host, ":", port, "]上的[", from, "]移动到[", to, "]"));
				} else{
					logger.info(logSeq, CustomStringUtils.append("将FTP服务器:[", host, ":", port, "]上的[", from, "]移动到[", to, "]移动失败"));
				}
    			
			} catch (Exception e) {
				logger.error(logSeq, CustomStringUtils.append("将FTP服务器:[", host, ":", port, "]上的[", from, 
						"]移动到[", to, "]移动出错!"), e);
			}
			
			// 退出FTP
			try {
				ftp.logout();
			} catch (IOException e) {
				logger.error(logSeq, CustomStringUtils.append("用户", username, "无法退出FTP服务器", host, ":", port), e);
			}
		} finally {
			disconnect(logSeq, ftp);
		}
		
		return result;
	}
	
	/**
	 * @Title: disconnect 
	 * @Description: 关闭FTP连接
	 * @since: 1.0.0
	 * @param logSeq	流水号
	 * @param ftp		FTP连接对象
	 */
	private static void disconnect(String logSeq, FTPClient ftp) {
		if (null != ftp) {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
					logger.error(logSeq, "关闭FTP连接" + ftp.getRemoteAddress() + "出错!", e);
				}
			} else {
				logger.info(logSeq, "FTP连接:[" + ftp.getRemoteAddress() + "]已经被关闭!");
			}
		} else {
			logger.info(logSeq, "FTP连接对象为null!");
		}
	}
}
