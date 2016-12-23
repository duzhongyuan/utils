/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: PasswordDecrypter.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils 
 * @Description: <p>用于加解密数据库连接串的用户名密码</p>
 * @author: dzy   
 * @date: 2016年12月23日 上午11:11:05 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/** 
 * <p>用于加解密数据库连接串的用户名密码</p>
 * @ClassName: PasswordDecrypter 
 * @Description: 加密解密器
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月23日 上午11:11:05  
 */
public class PasswordDecrypter {

	private static LogUtils logger = new LogUtils(PasswordDecrypter.class);
	
	private StandardPBEStringEncryptor encrypter;
	private String key;
	
	public PasswordDecrypter(String key){
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * @Title: decrypt 
	 * @Description: 解密
	 * @since: 0.0.1
	 * @param encryptedString	待解密的字符串
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String encryptedString) throws Exception{
		try {
			encrypter = new StandardPBEStringEncryptor();
			encrypter.setPassword(key);
			String encrypted = encrypter.decrypt(encryptedString);
			return encrypted;
		} catch (Exception e) {
			String errMsg = "[CRITICAL ERROR] Error enountered during decryption - " +
					"check decryption algorithm & key match that used for decryption.";
			logger.error(errMsg + " encryptedString=" + encryptedString, e);
			throw new Exception(errMsg, e);
		}
	}

	/**
	 * @Title: encrypt 
	 * @Description: 加密
	 * @since: 0.0.1
	 * @param unEncrypted	待加密的字符串
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String unEncrypted) throws Exception{
		try {
			encrypter = new StandardPBEStringEncryptor();
			encrypter.setPassword(key);
			String encrypted = encrypter.encrypt(unEncrypted);
			return encrypted;
		} catch (Exception e) {
			String errMsg = "[CRITICAL ERROR] Error enountered during decryption - " +
					"check decryption algorithm & key match that used for encryption.";
			logger.error(errMsg + " unEncrypted=" + unEncrypted, e);
			throw new Exception(errMsg, e);
		}
	}
	
}
