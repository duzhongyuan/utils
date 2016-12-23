/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: CustomStringUtils.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils 
 * @Description: 自定义字符串工具类
 * @author: dzy   
 * @date: 2016年12月22日 下午5:37:12 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/** 
 * @ClassName: CustomStringUtils 
 * @Description: 自定义字符串工具类
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月22日 下午5:37:12  
 */
public class CustomStringUtils {

	/**
	 * @Title: leftFill 
	 * @Description: 长度不足,左补字符
	 * <p>用于要求定长字符串的,长度不足时左补字符串的场景.</p>
	 * @since: 1.0.0
	 * @param str	原字符串
	 * @param ch	长度不足时要补的字符
	 * @param len	定长字段的长度(要补够多长)
	 * @return
	 */
	public static String leftFill(String str, char ch, int len){
		if (null == str) {
			str = "";
		}
		while (str.length() < len) {
			str = ch + str;
		}
		return str;
	}

	/**
	 * @Title: rightFill 
	 * @Description: 长度不足,左补字符
	 * <p>用于要求定长字符串的,长度不足时右补字符串的场景.</p>
	 * @since: 1.0.0
	 * @param str	原字符串
	 * @param ch	长度不足时要补的字符
	 * @param len	定长字段的长度(要补够多长)
	 * @return
	 */
	public static String rightFill(String str, char ch, int len){
		if (null == str) {
			str = "";
		}
		while (str.length() < len) {
			str = str + ch;
		}
		return str;
	}
	
	/**
	 * @Title: append 
	 * @Description: 拼接字符串
	 * @since: 1.0.0
	 * @param objs	需要拼接的对象
	 * @return
	 */
	public static String append(Object... objs) {
		if (null == objs || objs.length == 0) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (Object obj : objs) {
			buffer.append(obj);
		}
		return buffer.toString();
	}
	
	/**
	 * @Title: nullToBlank 
	 * @Description: 如果字符串为空(null)，则返回空字符
	 * @since: 1.0.0
	 * @param str
	 * @return
	 */
	public static String nullToBlank(String str){
		return str == null ? "" : str;
	}
	
	/**
	 * @Title: convertMapToString 
	 * @Description: 将map转换成字符串
	 * @since: 1.0.0
	 * @param map		需要转字符串的Map对象
	 * @param symbol	转成字符串之后键值对之间的间隔符号
	 * @return String,如果symbol为& 则返回格式为:key1=value1&key2=value2...
	 */
	public static String convertMapToString(Map<String, String> map, String symbol){
		if (null == map || map.isEmpty() || StringUtils.isEmpty(symbol)) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (first) {
				buffer.append(entry.getKey()).append("=").append(entry.getValue());
				first = false;
			} else {
				buffer.append(symbol).append(entry.getKey()).append("=").append(entry.getValue());
			}
		}
		return buffer.toString();
		
	}
	
	/**
	 * @Title: convertMapToString 
	 * @Description: 将map转化成字符串，并对map.value进行URL编码（当参数值为null时，则忽略该参数）
	 * @since: 1.0.0
	 * @param map		需要转字符串的Map对象
	 * @param symbol	转成字符串之后键值对之间的间隔符号
	 * @param encoding	对map.value进行编码的编码格式(utf-8,gbk等)
	 * @return
	 */
	public static String convertMapToString(Map<String, String> map, String symbol, String encoding){
		if (map == null || map.isEmpty() || StringUtils.isEmpty(symbol)) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (first) {
				buffer.append(entry.getKey()).append("=").append(urlEncode(entry.getValue(), encoding));
				first = false;
			} else {
				buffer.append(symbol).append(entry.getKey()).append("=").append(urlEncode(entry.getValue(), encoding));
			}
		}
		return buffer.toString();
	}
	
	/**
	 * @Title: urlEncode 
	 * @Description: 对字符串进行URL编码
	 * @since: 1.0.0
	 * @param str		需要编码的字符串
	 * @param encoding	编码格式(utf-8,gbk等)
	 * @return	String 编码后的字符串
	 */
	public static String urlEncode(String str, String encoding) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		try {
			return URLEncoder.encode(str, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println(append("URL encode string error. str=", str, ", encoding=", encoding) + e);
			return str;
		}
	}
	
	/**
	 * @Title: urlDecode 
	 * @Description: 对字符串进行URL解码
	 * @since: 1.0.0
	 * @param str		需要解码的字符串
	 * @param encoding	解码的编码(utf-8,gbk等)
	 * @return
	 */
	public static String urlDecode(String str, String encoding){
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		try {
			return URLDecoder.decode(str, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println(append("URL decode string error. str=", str, ", encoding=", encoding) + e);
			return str;
		}
	}
	
	public static void main(String[] args) {
		String hello = "125";
		hello = leftFill(hello, ' ', 20);
		System.out.println("hello:" + hello);
		
		System.out.println(urlEncode("http://www.baidu.com/hello/你好", "gbk"));
	}
	
}
