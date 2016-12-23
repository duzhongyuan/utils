/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	常用工具类项目
 *
 * @Title: AmountUtils.java 
 * @Prject: utils
 * @Package: com.pci.data.common.utils 
 * @Description: 金额工具类
 * @author: dzy   
 * @date: 2016年12月22日 下午5:44:20 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

/** 
 * @ClassName: AmountUtils 
 * @Description: 金额工具类
 * @since: 0.0.1
 * @author: dzy
 * @date: 2016年12月22日 下午5:44:20  
 */
public class AmountUtils {
	/**
	 * 人民币金额:100分
	 */
	private static final BigDecimal CNY_AMOUNT_FEN_100 = new BigDecimal(100);
	
	/**
	 * @Title: fromFenToYuan 
	 * @Description: 人民币金额单位转换:将分转换成元
	 * @since: 1.0.0
	 * @param amount	人民币金额(单位:分)
	 * @param n			小数点后保留的有效位数,n>=2
	 * @return
	 * @throws Exception
	 */
	public static String fromFenToYuan(String amount, int n) throws Exception {
		if (StringUtils.isEmpty(amount)) {
			return null;
		}
		if (n < 2) {
			return new BigDecimal(amount).divide(CNY_AMOUNT_FEN_100).toString();
		}
		return new BigDecimal(amount).divide(CNY_AMOUNT_FEN_100).setScale(n).toString();
	}
	
	/**
	 * @Title: fromYuanToFen 
	 * @Description: 人民币金额单位转换：将元转换成分
	 * @since: 1.0.0
	 * @param amount	人民币金额(单位:元)
	 * @return
	 */
	public static String fromYuanToFen(String amount) throws Exception {
		if (StringUtils.isEmpty(amount)) {
			return null;
		}
		return String.valueOf(BigDecimal.valueOf(Double.parseDouble(amount)).multiply(CNY_AMOUNT_FEN_100).longValue());
	}
	
	/**
	 * @Title: fromYuanToFen 
	 * @Description: 人民币金额单位转换：将元转换成分
	 * @since: 1.0.0
	 * @param amount	人民币金额(单位:元)
	 * @return
	 * @throws Exception
	 */
	public static long fromYuanToFen(double amount) throws Exception {
		return BigDecimal.valueOf(amount).multiply(CNY_AMOUNT_FEN_100).longValue();
	}
	
	/**
	 * @Title: formatAmount 
	 * @Description: 金额格式化
	 * <p>将".角分"转换成"0.角分"</p>
	 * @since: 1.0.0
	 * @param amount
	 * @return
	 */
	public static String formatAmount(String amount){
		if (StringUtils.isEmpty(amount)) {
			return amount;
		}
		if (amount.indexOf('.') == 0) {
			return "0" + amount;
		}
		return amount;
	}
	
}
