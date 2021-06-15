package com.enableets.edu.pakage.framework.ppr.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析answerXml 工具
 * @author walle_yu@enable-ets.com
 * @since 2018年6月4日
 */
public class XmlUtils {

	/**
	 * 获取answer xml 单个属性值
	 * @date 2021/02/23 10:47
	 * @since caleb_liu@enable-ets.com
	 * @param answerXml : answerXml
	 * @param propertyName : 属性名称
	 * @return java.lang.String
	 */
	public static String getAnswerXmlPropertyValue(String answerXml, String propertyName) {
		String re = "(" + propertyName + ")(\\s*)(=)(\\s*)([\'||\"])[^\'||^\"]+";
		Pattern tagPatternCompile = Pattern.compile(re);
		Matcher matcher = tagPatternCompile.matcher(answerXml);
		String value = "";
		if (matcher.find()) {
			String[] split = matcher.group().trim().split("=");
			if (split.length == 2) {
				value = split[1].replaceAll("\'","").replaceAll("\"","").trim();
				return value;
			}
		}
		return value;
	}
}
