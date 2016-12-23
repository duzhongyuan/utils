/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: SocketClient.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils 
 * @Description: Socket客户端
 * @author: dzy   
 * @date: 2016年12月22日 下午5:51:40 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

/** 
 * @ClassName: SocketClient 
 * @Description: Socket客户端
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月22日 下午5:51:40  
 */
public class SocketClient {

private static LogUtils logger = new LogUtils(SocketClient.class);
	
	/** 建立连接的超时时间默认为60秒 **/
	public static final int DEFAULT_CONNECT_TIMEOUT = 60 * 1000;
	/** 接收数据时的等待超时时间默认为60秒 **/
	public static final int DEFAULT_TIMEOUT = 60 * 1000;
	
	/** socket **/
	protected Socket socket = null;
	/** 字符编码 **/
	protected Charset charset = Charset.forName("gbk");

	protected SocketClient() {
		
	}

	public SocketClient(InetSocketAddress socketAddress) 
			throws SocketException, IOException {
		connect(socketAddress, 
				DEFAULT_CONNECT_TIMEOUT, DEFAULT_TIMEOUT);
	}

	public SocketClient(String host, int port) 
			throws SocketException, IOException {
		connect(new InetSocketAddress(Inet4Address.getByName(host), port), 
				DEFAULT_CONNECT_TIMEOUT, DEFAULT_TIMEOUT);
	}

	public SocketClient(String host, int port, int timeout) 
			throws SocketException, IOException {
		connect(new InetSocketAddress(Inet4Address.getByName(host), port), 
				DEFAULT_CONNECT_TIMEOUT, timeout);
	}

	public SocketClient(String host, int port, int connectTimeout, int timeout) 
			throws SocketException, IOException {
		connect(new InetSocketAddress(Inet4Address.getByName(host), port), 
				connectTimeout, timeout);
	}

	public void setCharset(String charsetName) {
		this.charset = Charset.forName(charsetName);
	}

	public Charset getCharset() {
		return this.charset;
	}

	/**
	 * 建立SOCKET连接
	 * 
	 * @param remoteAddress 服务器地址
	 * @param connectTimeout 建立连接的超时时间（单位：毫秒）
	 * @param timeout 接收数据时的等待超时时间（单位：毫秒）
	 * @throws SocketException
	 * @throws IOException
	 */
	protected void connect(InetSocketAddress remoteAddress, int connectTimeout, int timeout) 
			throws SocketException, IOException {
		if (isConnected()) {
			return;
		}
		try {
			socket = new Socket();
			socket.connect(remoteAddress, connectTimeout);
			socket.setSoTimeout(timeout);
		} catch (SocketException e) {
			logger.error("建立SOCKET连接出错！remoteAddress=" + remoteAddress.toString(), e);
			disconnect();
			throw e;
		} catch (IOException e) {
			logger.error("建立SOCKET连接出错！remoteAddress=" + remoteAddress.toString(), e);
			disconnect();
			throw e;
		}
	}

	/**
	 * 是否已经建立SOCKET连接
	 * 
	 * @return
	 */
	public boolean isConnected() {
        if (socket == null) {
        	return false;
        }
        return socket.isConnected();
    }

	/**
	 * 关闭SOCKET连接
	 * 
	 * @throws IOException
	 */
	public void disconnect() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("关闭SOCKET连接出错！", e);
			}
		}
	}

	/**
	 * 获取服务器地址
	 * 
	 * @return
	 */
	public InetAddress getRemoteAddress() {
		if (socket == null) {
			return null;
		}
		return socket.getInetAddress();
	}

	/**
	 * 发送请求数据
	 * 
	 * <p>只发送不接收</p>
	 * 
	 * @param sendContent
	 */
	public void send(String sendContent) {
		BufferedWriter out = null;
		try {
			// 发送请求数据
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
			out.write(sendContent);
			out.flush();
		} catch (IOException e) {
			logger.error("请求失败，远程端异常！", e);
		} finally {
			IOUtils.closeQuietly(out, "关闭SOCKET发送端出错！");
		}
	}

	/**
	 * 发送请求数据
	 * 
	 * <p>发送请求数据后，根据响应报文前22位中最后8位的报文长度接收响应数据</p>
	 * 
	 * @param sendContent
	 * @return 响应报文头 + 响应报文体
	 */
	public String sendAndReceiveByLength(byte[] messageBytes) {
		String receiveContent = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			// 发送请求数据
			out = new BufferedOutputStream(socket.getOutputStream());
//			out.write(sendContent.getBytes(charset));
			out.write(messageBytes);
			out.flush();
			
			// 接收响应数据
			in = new BufferedInputStream(socket.getInputStream());
			// 获取报文头
			byte[] headBuffer = new byte[7];
			try {
				IOUtils.read(in, headBuffer, 0, 7);
			} catch (IOException e) {
				logger.error("读取SOCKET接收端报文头出错！", e);
			}
			// 获取报文体长度
			int bodyLen = 0;
			try {
//				bodyLen = Integer.valueOf(new String(headBuffer, 5, 2));
				byte[] lenBytes = new byte[2];
				System.arraycopy(headBuffer, 5, lenBytes, 0, 2);
				
				bodyLen = ((lenBytes[0] & 0xff) << 8) | (lenBytes[1] & 0xff);
			} catch (NumberFormatException e) {
				logger.error("读取SOCKET接收端报文体长度出错！head=" + new String(headBuffer, charset));
				return null;
			}
			// 获取报文体
			byte[] bodyBuffer = new byte[bodyLen];
			try {
				IOUtils.read(in, bodyBuffer, 0, bodyLen);
			} catch (IOException e) {
				logger.error("读取SOCKET接收端报文体出错！", e);
			}
			receiveContent = new String(bodyBuffer, charset);
		} catch (IOException e) {
			logger.error("请求失败，远程端异常！", e);
		} finally {
			IOUtils.closeQuietly(in, "关闭SOCKET接收端出错！");
			IOUtils.closeQuietly(out, "关闭SOCKET发送端出错！");
		}
		
		return receiveContent;
	}
	
}
