/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: PCIHttpClientImpl3.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils.http 
 * @Description: HTTP客户端3.1 (HttpClient Version 3.1)
 * @author: dzy   
 * @date: 2016年12月22日 下午6:21:06 
 * @version: V1.0   
 */
package com.pci.data.common.utils.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;

import com.pci.data.common.utils.CustomConstants;
import com.pci.data.common.utils.CustomStringUtils;
import com.pci.data.common.utils.IOUtils;
import com.pci.data.common.utils.LogUtils;


/** 
 * @ClassName: PCIHttpClientImpl3 
 * @Description: HTTP客户端3.x (HttpClient Version 3.1)
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月22日 下午6:21:06  
 */
public class PCIHttpClientImpl3 implements PCIHttpClient {

	private static LogUtils logger = new LogUtils(PCIHttpClientImpl3.class);
	
	/** 是否允许循环重定向 **/
	private static final String HTTP_PROTOCOL_ALLOW_CIRCULAR_REDIRECTS = "http.protocol.allow-circular-redirects";
	
	@Override
	public String sendGetAndResponseAsString(String url, 
			int timeout) throws IOException {
		return sendGetAndResponseAsString(url, null, null, CustomConstants.CHARSET_UTF8, timeout);
	}

	@Override
	public String sendGetAndResponseAsString(String url, 
			Map<String, String> headers, int timeout) throws IOException {
		return sendGetAndResponseAsString(url, headers, null, CustomConstants.CHARSET_UTF8, timeout);
	}

	@Override
	public String sendGetAndResponseAsString(String url, 
			Map<String, String> headers, Map<String, String> params, 
			String charsetName, int timeout) throws IOException {
		byte[] buffer = sendGetAndResponseAsByteArray(url, headers, params, charsetName, timeout);
		if (buffer == null) {
			return null;
		}
		return new String(buffer, charsetName);
	}

	@Override
	public byte[] sendGetAndResponseAsByteArray(String url, 
			int timeout) throws IOException {
		return sendGetAndResponseAsByteArray(url, null, null, null, timeout);
	}

	@Override
	public byte[] sendGetAndResponseAsByteArray(String url, 
			Map<String, String> headers, int timeout) throws IOException {
		return sendGetAndResponseAsByteArray(url, headers, null, null, timeout);
	}

	@Override
	public byte[] sendGetAndResponseAsByteArray(String url, 
			Map<String, String> headers, Map<String, String> params, 
			String charsetName, int timeout) throws IOException {
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			sendGet(url, headers, params, charsetName, timeout, output);
			return output.toByteArray();
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	@Override
	public InputStream sendGetAndResponseAsStream(String url, 
			Map<String, String> headers, Map<String, String> params, 
			String charsetName, int timeout) throws IOException {
		byte[] buffer = sendGetAndResponseAsByteArray(url, headers, params, charsetName, timeout);
		if (buffer == null) {
			return null;
		}
		return new ByteArrayInputStream(buffer);
	}

	@Override
	public void sendGet(String url, Map<String, String> headers, 
			Map<String, String> params, String charsetName, int timeout, 
			OutputStream output) throws IOException {
		if (StringUtils.isEmpty(url)) {
			logger.error("The url can not be null.");
			throw new IllegalArgumentException("The url can not be null.");
		}
		sendAndResponseAsStream(new GetMethod(formatURL(url, params, charsetName)), 
				headers, timeout, output);
	}

	@Override
	public String sendPostAndResponseAsString(String url, 
			Map<String, String> params, String charsetName, int timeout) throws IOException {
		return sendPostAndResponseAsString(url, null, params, null, charsetName, timeout);
	}

	@Override
	public String sendPostAndResponseAsString(String url, 
			Map<String, String> headers, Map<String, String> params, 
			int timeout, String charsetName) throws IOException {
		return sendPostAndResponseAsString(url, headers, params, null, charsetName, timeout);
	}

	@Override
	public String sendPostAndResponseAsString(String url, 
			Map<String, String> headers, Object stringOrStream, 
			String charsetName, int timeout) throws IOException {
		return sendPostAndResponseAsString(url, headers, null, stringOrStream, charsetName, timeout);
	}

	@Override
	public String sendPostAndResponseAsString(String url, 
			Map<String, String> headers, Map<String, String> params, 
			Object stringOrStream, String charsetName, int timeout) throws IOException {
		byte[] buffer = sendPostAndResponseAsByteArray(
				url, headers, params, stringOrStream, charsetName, timeout);
		if (buffer == null) {
			return null;
		}
		return new String(buffer, charsetName);
	}

	@Override
	public byte[] sendPostAndResponseAsByteArray(String url, 
			Map<String, String> params, String charsetName, int timeout) throws IOException {
		return sendPostAndResponseAsByteArray(url, null, params, null, charsetName, timeout);
	}

	@Override
	public byte[] sendPostAndResponseAsByteArray(String url, 
			Map<String, String> headers, Map<String, String> params, 
			int timeout, String charsetName) throws IOException {
		return sendPostAndResponseAsByteArray(url, headers, params, null, charsetName, timeout);
	}

	@Override
	public byte[] sendPostAndResponseAsByteArray(String url, 
			Map<String, String> headers, Object stringOrStream, 
			String charsetName, int timeout) throws IOException {
		return sendPostAndResponseAsByteArray(url, headers, null, stringOrStream, charsetName, timeout);
	}

	@Override
	public byte[] sendPostAndResponseAsByteArray(String url, 
			Map<String, String> headers, Map<String, String> params, 
			Object stringOrStream, String charsetName, int timeout) throws IOException {
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			sendPost(url, headers, params, stringOrStream, charsetName, timeout, output);
			return output.toByteArray();
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	@Override
	public InputStream sendPostAndResponseAsStream(String url, 
			Map<String, String> headers, Map<String, String> params, 
			Object stringOrStream, String charsetName, int timeout) throws IOException {
		byte[] buffer = sendPostAndResponseAsByteArray(
				url, headers, params, stringOrStream, charsetName, timeout);
		if (buffer == null) {
			return null;
		}
		return new ByteArrayInputStream(buffer);
	}

	@Override
	public void sendPost(String url, Map<String, String> headers, 
			Map<String, String> params, Object stringOrStream, 
			String charsetName, int timeout, OutputStream output) throws IOException {
		if (StringUtils.isEmpty(url)) {
			logger.error("The url can not be null.");
			throw new IllegalArgumentException("The url can not be null.");
		}
		
		PostMethod method = new PostMethod(url);
		
		// 设置请求头信息
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		if (!headers.containsKey(CONTENT_TYPE)) {
			headers.put(CONTENT_TYPE, DEFAULT_CONTENTTYPE);
		}
		
		// 设置请求参数
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				method.addParameter(entry.getKey(), entry.getValue());
			}
		}
		
		// 设置请求体内容
		if (stringOrStream != null) {
			RequestEntity entity = null;
			if (stringOrStream instanceof InputStream) {
				entity = new InputStreamRequestEntity((InputStream) stringOrStream);
			} else {
				entity = new StringRequestEntity(stringOrStream.toString(), headers.get(CONTENT_TYPE), charsetName);
			}
			method.setRequestEntity(entity);
		}
		
		// 发送请求数据，并接收响应数据
		sendAndResponseAsStream(method, headers, timeout, output);
	}

	/**
	 * 发送请求数据，并接收响应数据
	 * @param method 请求方式
	 * @param headers 请求头信息
	 * @param timeout 超时时间
	 * @param output 响应内容（输出流）
	 * @throws IOException
	 */
	private void sendAndResponseAsStream(HttpMethod method, 
			Map<String, String> headers, int timeout, OutputStream output) throws IOException {
		// 设置请求方式参数
		HttpMethodParams methodParams = method.getParams();
		if (methodParams == null) {
			methodParams = new HttpMethodParams();
			method.setParams(methodParams);
		}
		methodParams.setSoTimeout(timeout);
		
		// 设置请求头信息
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		if (!headers.containsKey(USER_AGENT)) {
			headers.put(USER_AGENT, DEFAULT_USERAGENT);
		}
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			method.setRequestHeader(entry.getKey(), entry.getValue());
		}
		
		SimpleHttpConnectionManager httpConnectionManager = new SimpleHttpConnectionManager();
		httpConnectionManager.getParams().setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
		HttpClient httpClient = new HttpClient(httpConnectionManager);
		
		// 设置是否允许循环重定向（重定向到相同路径）
		httpClient.getParams().setBooleanParameter(HTTP_PROTOCOL_ALLOW_CIRCULAR_REDIRECTS, true);
		
		// 发送请求
		InputStream input = null;
		try {
			int status = httpClient.executeMethod(method);
			if (status != HttpStatus.SC_OK) {
				String errorMsg = CustomStringUtils.append("The remote service[", 
						method.getURI().getURI(), "] respose an error status:", status);
				logger.error(errorMsg);
				if (status >= 500 && status < 600) {
					throw new IOException(errorMsg);
				}
			}
			input = method.getResponseBodyAsStream();
			if (input == null) {
				String errorMsg = CustomStringUtils.append("The remote service[", 
						method.getURI().getURI(), "] respose body is null. status:", status);
				logger.error(errorMsg);
				throw new IOException(errorMsg);
			}
			IOUtils.copy(input, output);
		} catch (IOException e) {
			logger.error("Access to the remote service[" + method.getURI().getURI() + "] error.", e);
			throw e;
		} finally {
			IOUtils.closeQuietly(input);
			method.releaseConnection();
			httpConnectionManager.shutdown();
		}
	}

	/**
	 * 格式化请求URL
	 * @param url
	 * @param params
	 * @param charsetName
	 * @return
	 */
	private String formatURL(String url, 
			Map<String, String> params, String charsetName) {
		if (params == null || params.isEmpty()) {
			return url;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(url);
		if (url.indexOf("?") > 0) {
			buffer.append("&");
		} else {
			buffer.append("?");
		}
		buffer.append(CustomStringUtils.convertMapToString(params, charsetName));
		return buffer.toString();
	}

	/** 
	 * @see cn.com.bestpay.pgw.tools.http.PGWHttpClient#sendGet(java.lang.String, java.util.Map, java.util.Map, java.lang.String, int)
	 */
	@Override
	public PCIHttpResponse sendGet(String url, Map<String, String> headers,
			Map<String, String> params, String charsetName, int timeout)
			throws IOException {
		if (StringUtils.isEmpty(url)) {
			logger.error("The url can not be null.");
			throw new IllegalArgumentException("The url can not be null.");
		}
		return sendAndResponseAsStream(
				new GetMethod(formatURL(url, params, charsetName)), headers, timeout);
	}

	/** 
	 * @see cn.com.bestpay.pgw.tools.http.PGWHttpClient#sendPost(java.lang.String, java.util.Map, java.util.Map, java.lang.Object, java.lang.String, int)
	 */
	@Override
	public PCIHttpResponse sendPost(String url, Map<String, String> headers,
			Map<String, String> params, Object stringOrStream,
			String charsetName, int timeout) throws IOException {
		if (StringUtils.isEmpty(url)) {
			logger.error("The url can not be null.");
			throw new IllegalArgumentException("The url can not be null.");
		}
		
		PostMethod method = new PostMethod(url);
		
		// 设置请求头信息
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		if (!headers.containsKey(CONTENT_TYPE)) {
			headers.put(CONTENT_TYPE, DEFAULT_CONTENTTYPE);
		}
		
		// 设置请求参数
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				method.addParameter(entry.getKey(), entry.getValue());
			}
		}
		
		// 设置请求体内容
		if (stringOrStream != null) {
			RequestEntity entity = null;
			if (stringOrStream instanceof InputStream) {
				entity = new InputStreamRequestEntity((InputStream) stringOrStream);
			} else {
				entity = new StringRequestEntity(stringOrStream.toString(), headers.get(CONTENT_TYPE), charsetName);
			}
			method.setRequestEntity(entity);
		}
		
		// 发送请求数据，并接收响应数据
		return sendAndResponseAsStream(method, headers, timeout);
	}

	/**
	 * 发送请求数据，并接收响应数据
	 * 
	 * @param method 请求方式
	 * @param headers 请求头信息
	 * @param timeout 超时时间
	 * @return
	 * @throws IOException
	 * 
	 * @since 0.0.1
	 */
	private PCIHttpResponse sendAndResponseAsStream(HttpMethod method, 
			Map<String, String> headers, int timeout) throws IOException {
		// 设置请求方式参数
		HttpMethodParams methodParams = method.getParams();
		if (methodParams == null) {
			methodParams = new HttpMethodParams();
			method.setParams(methodParams);
		}
		methodParams.setSoTimeout(timeout);
		
		// 设置请求头信息
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		if (!headers.containsKey(USER_AGENT)) {
			headers.put(USER_AGENT, DEFAULT_USERAGENT);
		}
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			method.setRequestHeader(entry.getKey(), entry.getValue());
		}
		
		SimpleHttpConnectionManager httpConnectionManager = new SimpleHttpConnectionManager();
		httpConnectionManager.getParams().setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
		HttpClient httpClient = new HttpClient(httpConnectionManager);
		
		// 设置是否允许循环重定向（重定向到相同路径）
		httpClient.getParams().setBooleanParameter(HTTP_PROTOCOL_ALLOW_CIRCULAR_REDIRECTS, true);
		
		// 发送请求
		InputStream responseBodyInputStream = null;
		ByteArrayOutputStream responseBodyOutputStream = null;
		try {
			int status = httpClient.executeMethod(method);
			if (status != HttpStatus.SC_OK) {
				String errorMsg = CustomStringUtils.append("The remote service[", 
						method.getURI().getURI(), "] respose an error status:", status);
				logger.error(errorMsg);
				if (status >= 500 && status < 600) {
					throw new IOException(errorMsg);
				}
			}
			
			// 获取响应头
			Map<String, String> responseHeaders = null;
			Header[] httpHeaders = method.getResponseHeaders();
			if (httpHeaders != null && httpHeaders.length > 0) {
				responseHeaders = new HashMap<String, String>(httpHeaders.length);
				for (Header header : httpHeaders) {
					responseHeaders.put(header.getName(), header.getValue());
				}
			}
			
			// 获取响应体
			byte[] responseBody = null;
			responseBodyInputStream = method.getResponseBodyAsStream();
			if (responseBodyInputStream != null) {
				responseBodyOutputStream = new ByteArrayOutputStream();
				IOUtils.copy(responseBodyInputStream, responseBodyOutputStream);
				responseBody = responseBodyOutputStream.toByteArray();
			} else {
				logger.warn(CustomStringUtils.append("The remote service[", 
						method.getURI().getURI(), "] respose body is null. status:", status));
			}
			
			return new PCIHttpResponse(responseHeaders, responseBody);
		} catch (IOException e) {
			logger.error("Access to the remote service[" + method.getURI().getURI() + "] error.", e);
			throw e;
		} finally {
			IOUtils.closeQuietly(responseBodyInputStream);
			IOUtils.closeQuietly(responseBodyOutputStream);
			method.releaseConnection();
			httpConnectionManager.shutdown();
		}
	}

}
