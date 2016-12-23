/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: PCIHttpClient.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils.http 
 * @Description: HTTP客户端接口
 * @author: dzy   
 * @date: 2016年12月22日 下午6:12:18 
 * @version: V1.0   
 */
package com.pci.data.common.utils.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/** 
 * @ClassName: PCIHttpClient 
 * @Description: HTTP客户端接口
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月22日 下午6:12:18  
 */
public interface PCIHttpClient {
	/** 请求头信息:Content-Type **/
	public static final String CONTENT_TYPE = "Content-Type";
	/** 请求头信息:User-Agent **/
	public static final String USER_AGENT = "User-Agent";
	/** 默认的内容类型 **/
	public static final String DEFAULT_CONTENTTYPE = "application/x-www-form-urlencoded;charset=UTF-8";
	/** 默认的用户代理（浏览器类型） **/
	public static final String DEFAULT_USERAGENT = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; SLCC1; .NET CLR 2.0.50727; .NET CLR 3.0.04506; InfoPath.1; .NET CLR 1.1.4322; CIBA)";
	/** 默认的读取超时时间（单位毫秒） **/
	public static final int DEFAULT_TIMEOUT = 180000;
	/** 默认的连接超时时间（单位毫秒） **/
	public static final int DEFAULT_CONNECTION_TIMEOUT = 60000;

	/**
	 * 发送GET方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字符串）
	 * @throws IOException
	 */
	public String sendGetAndResponseAsString(String url, 
			int timeout) throws IOException;

	/**
	 * 发送GET方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字符串）
	 * @throws IOException
	 */
	public String sendGetAndResponseAsString(String url, 
			Map<String, String> headers, int timeout) throws IOException;

	/**
	 * 发送GET方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字符串）
	 * @throws IOException
	 */
	public String sendGetAndResponseAsString(String url, 
			Map<String, String> headers, Map<String, String> params, 
			String charsetName, int timeout) throws IOException;

	/**
	 * 发送GET方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字节数组）
	 * @throws IOException
	 */
	public byte[] sendGetAndResponseAsByteArray(String url, 
			int timeout) throws IOException;

	/**
	 * 发送GET方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字节数组）
	 * @throws IOException
	 */
	public byte[] sendGetAndResponseAsByteArray(String url, 
			Map<String, String> headers, int timeout) throws IOException;

	/**
	 * 发送GET方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字节数组）
	 * @throws IOException
	 */
	public byte[] sendGetAndResponseAsByteArray(String url, 
			Map<String, String> headers, Map<String, String> params, 
			String charsetName, int timeout) throws IOException;

	/**
	 * 发送GET方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（输入流）
	 * @throws IOException
	 */
	public InputStream sendGetAndResponseAsStream(String url, 
			Map<String, String> headers, Map<String, String> params, 
			String charsetName, int timeout) throws IOException;

	/**
	 * 发送GET方式请求
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @param output 响应内容（输出流）
	 * @throws IOException
	 */
	public void sendGet(String url, Map<String, String> headers, 
			Map<String, String> params, String charsetName, int timeout, 
			OutputStream output) throws IOException;

	/**
	 * 发送POST方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字符串）
	 * @throws IOException
	 */
	public String sendPostAndResponseAsString(String url, 
			Map<String, String> params, String charsetName, int timeout) throws IOException;

	/**
	 * 发送POST方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param timeout 超时时间（单位毫秒）
	 * @param charsetName 字符编码
	 * @return 响应内容（字符串）
	 * @throws IOException
	 */
	public String sendPostAndResponseAsString(String url, 
			Map<String, String> headers, Map<String, String> params, 
			int timeout, String charsetName) throws IOException;

	/**
	 * 发送POST方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param stringOrStream 请求体内容（字符串或者输入流）
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字符串）
	 * @throws IOException
	 */
	public String sendPostAndResponseAsString(String url, 
			Map<String, String> headers, Object stringOrStream, 
			String charsetName, int timeout) throws IOException;

	/**
	 * 发送POST方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param stringOrStream 请求体内容（字符串或者输入流）
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字符串）
	 * @throws IOException
	 */
	public String sendPostAndResponseAsString(String url, 
			Map<String, String> headers, Map<String, String> params, 
			Object stringOrStream, String charsetName, int timeout) throws IOException;

	/**
	 * 发送POST方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字节数组）
	 * @throws IOException
	 */
	public byte[] sendPostAndResponseAsByteArray(String url, 
			Map<String, String> params, String charsetName, int timeout) throws IOException;

	/**
	 * 发送POST方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param timeout 超时时间（单位毫秒）
	 * @param charsetName 字符编码
	 * @return 响应内容（字节数组）
	 * @throws IOException
	 */
	public byte[] sendPostAndResponseAsByteArray(String url, 
			Map<String, String> headers, Map<String, String> params, 
			int timeout, String charsetName) throws IOException;

	/**
	 * 发送POST方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param stringOrStream 请求体内容（字符串或者输入流）
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字节数组）
	 * @throws IOException
	 */
	public byte[] sendPostAndResponseAsByteArray(String url, 
			Map<String, String> headers, Object stringOrStream, 
			String charsetName, int timeout) throws IOException;

	/**
	 * 发送POST方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param stringOrStream 请求体内容（字符串或者输入流）
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（字节数组）
	 * @throws IOException
	 */
	public byte[] sendPostAndResponseAsByteArray(String url, 
			Map<String, String> headers, Map<String, String> params, 
			Object stringOrStream, String charsetName, int timeout) throws IOException;

	/**
	 * 发送POST方式请求数据，并接收响应数据
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param stringOrStream 请求体内容（字符串或者输入流）
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return 响应内容（输入流）
	 * @throws IOException
	 */
	public InputStream sendPostAndResponseAsStream(String url, 
			Map<String, String> headers, Map<String, String> params, 
			Object stringOrStream, String charsetName, int timeout) throws IOException;

	/**
	 * 发送POST方式请求
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param stringOrStream 请求体内容（字符串或者输入流）
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @param output 响应内容（输出流）
	 * @throws IOException
	 */
	public void sendPost(String url, Map<String, String> headers, 
			Map<String, String> params, Object stringOrStream, 
			String charsetName, int timeout, OutputStream output) throws IOException;

	/**
	 * 发送GET方式请求
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return
	 * @throws IOException
	 * 
	 * @since 0.0.1
	 */
	public PCIHttpResponse sendGet(String url, Map<String, String> headers, 
			Map<String, String> params, String charsetName, int timeout) throws IOException;

	/**
	 * 发送POST方式请求
	 * 
	 * @param url 请求地址
	 * @param headers 请求头信息
	 * @param params 请求参数
	 * @param stringOrStream 请求体内容（字符串或者输入流）
	 * @param charsetName 字符编码
	 * @param timeout 超时时间（单位毫秒）
	 * @return
	 * @throws IOException
	 * 
	 * @since 0.0.1
	 */
	public PCIHttpResponse sendPost(String url, Map<String, String> headers, 
			Map<String, String> params, Object stringOrStream, 
			String charsetName, int timeout) throws IOException;
	
}
