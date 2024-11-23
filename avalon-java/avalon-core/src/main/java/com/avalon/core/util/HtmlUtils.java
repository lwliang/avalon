/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

public class HtmlUtils {

    /**
     * html的标签特殊字符转换成普通字符
     *
     * @param html
     * @return
     */
    public static String htmlEscape(String html) {
        return org.springframework.web.util.HtmlUtils.htmlEscape(html);
    }


    /**
     * html的特殊字符转换成普通数字
     *
     * @param html
     * @return
     */
    public static String htmlEscapeDecimal(String html) {
        return org.springframework.web.util.HtmlUtils.htmlEscapeDecimal(html);
    }

    /**
     * html的特殊字符转换成符合Intel HEX文件的字符串
     *
     * @param html
     * @return
     */
    public static String htmlEscapeHex(String html) {
        return org.springframework.web.util.HtmlUtils.htmlEscapeHex(html);
    }

    /**
     * html字符串转text字符串
     *
     * @param input
     * @param length
     * @return
     */
    public static String splitAndFilterString(String input, int length) {
        if (input == null || input.trim().equals("")) {
            return "";
        }
        // 去掉所有html元素,
        String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
                "<[^>]*>", "");
        str = str.replaceAll("[(/>)<]", "");
        int len = str.length();
        if (len <= length) {
            return str;
        } else {
            str = str.substring(0, length);
        }
        return str;
    }
}
