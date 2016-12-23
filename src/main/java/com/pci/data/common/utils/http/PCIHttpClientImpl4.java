/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: PCIHttpClientImpl4.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils.http 
 * @Description: HTTP客户端4.5 (HttpClient Version 4.5)
 * @author: dzy   
 * @date: 2016年12月23日 上午8:54:46 
 * @version: V1.0   
 */
package com.pci.data.common.utils.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import com.pci.data.common.utils.CustomConstants;
import com.pci.data.common.utils.CustomStringUtils;
import com.pci.data.common.utils.IOUtils;
import com.pci.data.common.utils.LogUtils;

/** 
 * @ClassName: PCIHttpClientImpl4 
 * @Description: HTTP客户端4.5 (HttpClient Version 4.5)
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月23日 上午8:54:46  
 */
public class PCIHttpClientImpl4 implements PCIHttpClient {

private static LogUtils logger = new LogUtils(PCIHttpClientImpl4.class);
	
	private SSLConnectionSocketFactory sslSocketFactory = null;

	/**
	 * 构造方法
	 * 
	 * <p>允许信任任何证书策略和允许任何域名验证</p>
	 */
	public PCIHttpClientImpl4() {
		this(false, false, true, true, null, null, null);
	}

	/**
	 * 构造方法
	 * 
	 * @param allowAnyTrustMaterial 是否允许信任任何证书策略
	 * @param allowAnyHostnameVerifier 是否允许任何域名验证
	 */
	public PCIHttpClientImpl4(boolean allowAnyTrustMaterial, boolean allowAnyHostnameVerifier) {
		this(false, false, allowAnyTrustMaterial, allowAnyHostnameVerifier, null, null, null);
	}

	/**
	 * 构造方法
	 * 
	 * @param loadKeyMaterial 是否加载密钥
	 * @param loadTrustMaterial 是否加载信任证书
	 * @param keystoreFilePath 密钥库文件路径
	 * @param storePassword 密钥库密码
	 * @param keyPassword 私钥密码
	 */
	public PCIHttpClientImpl4(boolean loadKeyMaterial, boolean loadTrustMaterial, 
			String keystoreFilePath, String storePassword, String keyPassword) {
		this(loadKeyMaterial, loadTrustMaterial, false, true, keystoreFilePath, storePassword, keyPassword);
	}

	/**
	 * 构造方法
	 * 
	 * @param loadKeyMaterial 是否加载密钥
	 * @param loadTrustMaterial 是否加载信任证书（若allowAnyTrustMaterial=true，则loadTrustMaterial无效）
	 * @param allowAnyTrustMaterial 是否允许信任任何证书策略
	 * @param allowAnyHostnameVerifier 是否允许任何域名验证
	 * @param keystoreFilePath 密钥库文件路径
	 * @param storePassword 密钥库密码
	 * @param keyPassword 私钥密码
	 */
	public PCIHttpClientImpl4(boolean loadKeyMaterial, boolean loadTrustMaterial, 
			boolean allowAnyTrustMaterial, boolean allowAnyHostnameVerifier, 
			String keystoreFilePath, String storePassword, String keyPassword) {
		try {
			// Create SSLContext
			SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
			if (loadKeyMaterial) {
				sslContextBuilder.loadKeyMaterial(
						new File(keystoreFilePath), storePassword.toCharArray(), keyPassword.toCharArray());
			}
			if (allowAnyTrustMaterial) {
				sslContextBuilder.loadTrustMaterial(null, new AnyTrustStrategy());
			} else if (loadTrustMaterial) {
				sslContextBuilder.loadTrustMaterial(
						new File(keystoreFilePath), storePassword.toCharArray(), new TrustSelfSignedStrategy());
			}
			SSLContext sslContext = sslContextBuilder.build();
			
			// Create SSLConnectionSocketFactory
			if (allowAnyHostnameVerifier) {
				sslSocketFactory = new SSLConnectionSocketFactory(sslContext, new AnyHostnameVerifier());
			} else {
				sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error occurred: NoSuchAlgorithmException", e);
		} catch (KeyStoreException e) {
			logger.error("Error occurred: KeyStoreException", e);
		} catch (UnrecoverableKeyException e) {
			logger.error("Error occurred: UnrecoverableKeyException", e);
		} catch (CertificateException e) {
			logger.error("Error occurred: CertificateException", e);
		} catch (IOException e) {
			logger.error("Error occurred: IOException", e);
		} catch (KeyManagementException e) {
			logger.error("Error occurred: KeyManagementException", e);
		}
	}

	/**
	 * 信任任何证书策略
	 */
	private static class AnyTrustStrategy implements TrustStrategy {
		@Override
		public boolean isTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			return true;
		}
	}

	/**
	 * 允许任何域名验证
	 */
	private static class AnyHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

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
		sendAndResponseAsStream(new HttpGet(createURI(url, params, charsetName)), 
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
		
		// post请求方式
		HttpPost method = new HttpPost(createURI(url, params, charsetName));
		
		// 设置请求头信息
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		if (!headers.containsKey(CONTENT_TYPE)) {
			headers.put(CONTENT_TYPE, DEFAULT_CONTENTTYPE);
		}
		
		// 设置请求体内容
		if (stringOrStream != null) {
			HttpEntity entity = null;
			if (stringOrStream instanceof InputStream) {
				entity = new InputStreamEntity((InputStream) stringOrStream);
			} else {
				entity = new StringEntity(stringOrStream.toString(), 
						ContentType.create(CONTENT_TYPE, charsetName));
			}
			method.setEntity(entity);
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
	private void sendAndResponseAsStream(HttpRequestBase method, 
			Map<String, String> headers, int timeout, OutputStream output) throws IOException {
		// 设置请求配置信息
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT) //连接超时时间
				.setSocketTimeout(timeout) //读取超时时间
				.setCircularRedirectsAllowed(true) //设置是否允许循环重定向（重定向到相同路径）
				.build();
		method.setConfig(config);
		
		// 设置请求头信息
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		if (!headers.containsKey(USER_AGENT)) {
			headers.put(USER_AGENT, DEFAULT_USERAGENT);
		}
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			method.setHeader(entry.getKey(), entry.getValue());
		}
		
		// 发送请求
		CloseableHttpClient httpClient = createHttpClient(method.getURI());
		CloseableHttpResponse httpResponse = null;
		InputStream input = null;
		try {
			httpResponse = httpClient.execute(method);
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				String errorMsg = CustomStringUtils.append("The remote service[", 
						method.getURI(), "] respose an error status:", status);
				logger.error(errorMsg);
				if (status >= 500 && status < 600) {
					throw new IOException(errorMsg);
				}
			}
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity == null) {
				String errorMsg = CustomStringUtils.append("The remote service[", 
						method.getURI(), "] respose entity is null. status:", status);
				logger.error(errorMsg);
				throw new IOException(errorMsg);
			}
			input = httpEntity.getContent();
			if (input == null) {
				String errorMsg = CustomStringUtils.append("The remote service[", 
						method.getURI(), "] respose entity content is null. status:", status);
				logger.error(errorMsg);
				throw new IOException(errorMsg);
			}
			IOUtils.copy(input, output);
		} catch (IOException e) {
			logger.error("Access to the remote service[" + method.getURI() + "] error.", e);
			throw e;
		} finally {
			IOUtils.closeQuietly(input);
//			method.releaseConnection();
			IOUtils.closeQuietly(httpResponse);
			IOUtils.closeQuietly(httpClient);
		}
	}

	/**
	 * 创建HttpClient对象
	 * @param uri
	 * @return
	 */
	private CloseableHttpClient createHttpClient(URI uri) {
		if ("https".equalsIgnoreCase(uri.getScheme()) && sslSocketFactory != null) {
			return HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
		} else {
			return HttpClients.createDefault();
		}
	}

	/**
	 * 创建请求URI
	 * @param url
	 * @param params
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	private URI createURI(String url, 
			Map<String, String> params, String charsetName) throws IOException {
		if (params == null || params.isEmpty()) {
			return URI.create(url);
		}
		// 设置请求参数
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(params.size());
		for (Map.Entry<String, String> entry : params.entrySet()) {
			parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		try {
			return new URIBuilder(url)
				.addParameters(parameters)
				.setCharset(Charset.forName(charsetName))
				.build();
		} catch (URISyntaxException e) {
			String errorMsg = "Build request URI error, the url is [" + url + "].";
			logger.error(errorMsg, e);
			throw new IOException(errorMsg, e);
		}
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
				new HttpGet(createURI(url, params, charsetName)), headers, timeout);
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
		
		// post请求方式
		HttpPost method = new HttpPost(createURI(url, params, charsetName));
		
		// 设置请求头信息
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		if (!headers.containsKey(CONTENT_TYPE)) {
			headers.put(CONTENT_TYPE, DEFAULT_CONTENTTYPE);
		}
		
		// 设置请求体内容
		if (stringOrStream != null) {
			HttpEntity entity = null;
			if (stringOrStream instanceof InputStream) {
				entity = new InputStreamEntity((InputStream) stringOrStream);
			} else {
				entity = new StringEntity(stringOrStream.toString(), 
						ContentType.create(CONTENT_TYPE, charsetName));
			}
			method.setEntity(entity);
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
	 * @since 2.0.0
	 */
	private PCIHttpResponse sendAndResponseAsStream(HttpRequestBase method, 
			Map<String, String> headers, int timeout) throws IOException {
		// 设置请求配置信息
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT) //连接超时时间
				.setSocketTimeout(timeout) //读取超时时间
				.setCircularRedirectsAllowed(true) //设置是否允许循环重定向（重定向到相同路径）
				.build();
		method.setConfig(config);
		
		// 设置请求头信息
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		if (!headers.containsKey(USER_AGENT)) {
			headers.put(USER_AGENT, DEFAULT_USERAGENT);
		}
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			method.setHeader(entry.getKey(), entry.getValue());
		}
		
		// 发送请求
		CloseableHttpClient httpClient = createHttpClient(method.getURI());
		CloseableHttpResponse httpResponse = null;
		InputStream responseBodyInputStream = null;
		ByteArrayOutputStream responseBodyOutputStream = null;
		try {
			httpResponse = httpClient.execute(method);
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				String errorMsg = CustomStringUtils.append("The remote service[", 
						method.getURI(), "] respose an error status:", status);
				logger.error(errorMsg);
				if (status >= 500 && status < 600) {
					throw new IOException(errorMsg);
				}
			}
			
			// 获取响应头
			Map<String, String> responseHeaders = null;
			Header[] httpHeaders = httpResponse.getAllHeaders();
			if (httpHeaders != null && httpHeaders.length > 0) {
				responseHeaders = new HashMap<String, String>(httpHeaders.length);
				for (Header header : httpHeaders) {
					responseHeaders.put(header.getName(), header.getValue());
				}
			}
			
			// 获取响应体
			byte[] responseBody = null;
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				responseBodyInputStream = httpEntity.getContent();
				if (responseBodyInputStream != null) {
					responseBodyOutputStream = new ByteArrayOutputStream();
					IOUtils.copy(responseBodyInputStream, responseBodyOutputStream);
					responseBody = responseBodyOutputStream.toByteArray();
				} else {
					logger.warn(CustomStringUtils.append("The remote service[", 
							method.getURI(), "] respose entity content is null. status:", status));
				}
			} else {
				logger.warn(CustomStringUtils.append("The remote service[", 
						method.getURI(), "] respose entity is null. status:", status));
			}
			
			return new PCIHttpResponse(responseHeaders, responseBody);
		} catch (IOException e) {
			logger.error("Access to the remote service[" + method.getURI() + "] error.", e);
			throw e;
		} finally {
			IOUtils.closeQuietly(responseBodyInputStream);
			IOUtils.closeQuietly(responseBodyOutputStream);
//			method.releaseConnection();
			IOUtils.closeQuietly(httpResponse);
			IOUtils.closeQuietly(httpClient);
		}
	}
}
