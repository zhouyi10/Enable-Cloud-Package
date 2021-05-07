package com.enableets.edu.pakage.framework.ppr.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common tools
 * @author walle_yu@enable-ets.com
 * @since 2020/07/28
 **/
public class Utils {

    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    //html标签替换常量
    private static final String XML_LT  =  "&lt;";
    private static final String XML_LT_REPLACE= "<";
    private static final String XML_GT = "&gt;";
    private static final String XML_GT_REPLACE = ">";
    private static final String XML_NBSP = "&nbsp;";
    private static final String XML_QUOT = "&quot;";
    private static final String XML_QUOT_REPLACE = "\"";
    private static final String XML_APOS = "&apos;";
    private static final String XML_39 = "&#39;";
    private static final String XML_APOS_REPLACE = "\'";
    private static final String XML_AMP = "&amp;";
    private static final String XML_AMP_REPLACE = "&";
    private static final String XML_LINES_1 = "\\/";
    private static final String XML_LINES_2 = "\\\\/";
    private static final String XML_LINES_3 = "/";
    private static final String DUE_TIME = "time";
    private static final String EXPIRE_TIME_IN_MINUTES = "minutes";

    /** 小写字母和数字字符数组  */
    public static final char[] alphaAndDigitArr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 生成随机 num位 文件夹名称
     * @param num 文件夹名称位数
     * @return 文件夹名称
     */
    public static String getRandomDir(int num) {
        Random random = new Random();
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < num; i++) {
            buff.append(alphaAndDigitArr[random.nextInt(36)]);
        }
        return buff.toString();
    }

    /**
     * @param htmlStr 含html标签的str
     * @return 删除Html标签
     */
    public static String unescapeHtmlTags(String htmlStr) {
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        htmlStr = unescapeHtmlTag(htmlStr);
        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 替换html标签
     * @param str 含html标签的str
     * @return String
     */
    private static String unescapeHtmlTag(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String key = str;
        if (key.contains(XML_LT)){
            key  = key.replaceAll(XML_LT, XML_LT_REPLACE);
        }
        if (key.contains(XML_GT)){
            key  = key.replaceAll(XML_GT, XML_GT_REPLACE);
        }
        if (key.contains(XML_NBSP)){
            key  = key.replaceAll(XML_NBSP, " ");
        }
        if (key.contains(XML_QUOT)){
            key  = key.replaceAll(XML_QUOT, XML_QUOT_REPLACE);
        }
        if (key.contains(XML_APOS)||key.contains(XML_39)){
            key  = key.replaceAll(XML_APOS, XML_APOS_REPLACE).replace(XML_39, XML_APOS_REPLACE);
        }
        if (key.contains(XML_AMP)){
            key  = key.replaceAll(XML_AMP, XML_AMP_REPLACE);
        }
        if (key.contains(XML_LINES_1)) {
            key = key.replaceAll(XML_LINES_2, XML_LINES_3);
        }
        return key;
    }

    /**
     * 通过byte大小获得文件大小显示信息
     * @param size
     * @return
     */
    public static String getFileSizeDisplay(Long size){
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

}
