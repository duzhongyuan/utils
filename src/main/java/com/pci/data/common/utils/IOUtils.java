/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: IOUtils.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils 
 * @Description: IO工具类
 * @author: dzy   
 * @date: 2016年12月22日 下午5:48:32 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: IOUtils 
 * @Description: IO工具类
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月22日 下午5:48:32  
 */
public class IOUtils {
	private static LogUtils logger = new LogUtils(IOUtils.class);
	
	/** 默认的缓冲区大小 **/
	private static final int DEFAULT_BUFFER_SIZE = 4096;

	/**
	 * 将字节输入流转换成字节数组
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			copy(input, output);
			return output.toByteArray();
		} finally {
			closeQuietly(output);
		}
	}

	/**
	 * 将字符输入流转换成字节数组
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(Reader reader) throws IOException {
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			copy(reader, output);
			return output.toByteArray();
		} finally {
			closeQuietly(output);
		}
	}

	/**
	 * 将字符输入流转换成字节数组
	 * @param reader
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(Reader reader, String encoding) throws IOException {
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			copy(reader, output, encoding);
			return output.toByteArray();
		} finally {
			closeQuietly(output);
		}
	}

	/**
	 * 将字节输入流转换成字符串
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String toString(InputStream input) throws IOException {
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			copy(input, writer);
			return writer.toString();
		} finally {
			closeQuietly(writer);
		}
	}

	/**
	 * 将字节输入流转换成字符串
	 * @param input
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String toString(InputStream input, String encoding) throws IOException {
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			copy(input, writer, encoding);
			return writer.toString();
		} finally {
			closeQuietly(writer);
		}
	}

	/**
	 * 将字符输入流转换成字符串
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static String toString(Reader reader) throws IOException {
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			copy(reader, writer);
			return writer.toString();
		} finally {
			closeQuietly(writer);
		}
	}

	/**
	 * 按行读取字节输入流
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(InputStream input) throws IOException {
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(input);
			return readLines(reader);
		} finally {
			closeQuietly(reader);
		}
	}

	/**
	 * 按行读取字节输入流
	 * @param input
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(InputStream input, String encoding) throws IOException {
		if (encoding == null) {
			return readLines(input);
		}
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(input, encoding);
			return readLines(reader);
		} finally {
			closeQuietly(reader);
		}
	}

	/**
	 * 按行读取字符输入流
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(Reader reader) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(reader);
			List<String> lines = new ArrayList<String>();
			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
			return lines;
		} finally {
			closeQuietly(br);
		}
	}

	/**
	 * 阻塞地读取字节输入流
	 * @param input
	 * @param buffer
	 * @param off
	 * @param length
	 * @throws IOException
	 */
	public static void read(InputStream input, 
			byte[] buffer, int off, int length) throws IOException {
		while (off < length) {
			off = off + input.read(buffer, off, length - off);
			if (off < 0) {
				throw new IOException("读取时出错[readLen=" + off + "]");
			}
		}
	}

	/**
	 * 将字节输入流拷贝到字符输出流
	 * @param input
	 * @param writer
	 * @throws IOException
	 */
	public static void copy(InputStream input, Writer writer) throws IOException {
//		InputStreamReader reader = new InputStreamReader(input);
//		copy(reader, writer);
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(input);
			copy(reader, writer);
		} finally {
			closeQuietly(reader);
		}
	}

	/**
	 * 将字节输入流拷贝到字符输出流
	 * @param input
	 * @param writer
	 * @param encoding
	 * @throws IOException
	 */
	public static void copy(InputStream input, Writer writer, String encoding) throws IOException {
//		if (encoding == null) {
//			copy(input, writer);
//		} else {
//			InputStreamReader reader = new InputStreamReader(input, encoding);
//			copy(reader, writer);
//		}
		if (encoding == null) {
			copy(input, writer);
		} else {
			InputStreamReader reader = null;
			try {
				reader = new InputStreamReader(input, encoding);
				copy(reader, writer);
			} finally {
				closeQuietly(reader);
			}
		}
	}

	/**
	 * 将字节输入流拷贝到字节输出流
	 * @param input
	 * @param output
	 * @return 字节数
	 * @throws IOException
	 */
	public static int copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		output.flush();
		return count;
	}

	/**
	 * 将字符输入流拷贝到字节输出流
	 * @param reader
	 * @param output
	 * @throws IOException
	 */
	public static void copy(Reader reader, OutputStream output) throws IOException {
//		OutputStreamWriter writer = new OutputStreamWriter(output);
//		copy(reader, writer);
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(output);
			copy(reader, writer);
		} finally {
			closeQuietly(writer);
		}
	}

	/**
	 * 将字符输入流拷贝到字节输出流
	 * @param reader
	 * @param output
	 * @param encoding
	 * @throws IOException
	 */
	public static void copy(Reader reader, OutputStream output, String encoding)
			throws IOException {
//		if (encoding == null) {
//			copy(reader, output);
//		} else {
//			OutputStreamWriter writer = new OutputStreamWriter(output, encoding);
//			copy(reader, writer);
//		}
		if (encoding == null) {
			copy(reader, output);
		} else {
			OutputStreamWriter writer = null;
			try {
				writer = new OutputStreamWriter(output, encoding);
				copy(reader, writer);
			} finally {
				closeQuietly(writer);
			}
		}
	}

	/**
	 * 将字符输入流拷贝到字符输出流
	 * @param reader
	 * @param writer
	 * @return 字符数
	 * @throws IOException
	 */
	public static int copy(Reader reader, Writer writer) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = reader.read(buffer))) {
			writer.write(buffer, 0, n);
			count += n;
		}
		writer.flush();
		return count;
	}

	/**
	 * 关闭IO流
	 * @param close
	 */
	public static void closeQuietly(Closeable close) {
		closeQuietly(close, "关闭IO流出错！");
	}

	/**
	 * 关闭IO流
	 * @param close
	 * @param errorMsg
	 */
	public static void closeQuietly(Closeable close, String errorMsg) {
		if (close != null) {
			try {
				close.close();
			} catch (IOException e) {
				logger.error(errorMsg, e);
			}
		}
	}

}
