/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: PCIHttpResponse.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils.http 
 * @Description: HTTP响应类
 * @author: dzy   
 * @date: 2016年12月22日 下午6:14:58 
 * @version: V1.0   
 */
package com.pci.data.common.utils.http;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import com.pci.data.common.utils.CustomConstants;

/** 
 * @ClassName: PCIHttpResponse 
 * @Description: HTTP响应类
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月22日 下午6:14:58  
 */
public class PCIHttpResponse {
	private Map<String, String> headers; // 响应头
	private byte[] body; // 响应体

	public PCIHttpResponse() {
		
	}

	public PCIHttpResponse(Map<String, String> headers, byte[] body) {
		this.headers = headers;
		this.body = body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	/**
	 * 获取响应头字符串
	 * 
	 * @return
	 */
	public String getHeadersString() {
		if (headers == null) {
			return null;
		}
		Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
		if (!iterator.hasNext()) {
			return "{}";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		Map.Entry<String, String> entry = null;
		for (;;) {
			entry = iterator.next();
			buffer.append(entry.getKey()).append("=").append(entry.getValue());
			if (iterator.hasNext()) {
				buffer.append(", ");
			} else {
				return buffer.append("}").toString();
			}
		}
	}

	/**
	 * 获取响应体字符串
	 * 
	 * <p>默认使用UTF-8编码转换</p>
	 * 
	 * @return
	 */
	public String getBodyString() {
		if (body == null) {
			return null;
		}
		if (body.length == 0) {
			return "";
		}
		return new String(body, Charset.forName(CustomConstants.CHARSET_UTF8));
	}

	/**
	 * 判断响应头是否为空
	 * @return
	 */
	public boolean isEmptyHeaders() {
		return headers == null || headers.isEmpty();
	}

	/**
	 * 判断响应体是否为空
	 * @return
	 */
	public boolean isEmptyBody() {
		return body == null || body.length == 0;
	}

	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer().append("[Headers=").append(getHeadersString())
				.append(", Body=").append(getBodyString()).append("]").toString();
	}
	
}
