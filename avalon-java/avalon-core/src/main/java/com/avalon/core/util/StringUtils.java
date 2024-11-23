/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static final String EMPTY = "";

    public static Boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    public static Boolean isEmpty(Object str) {
        return org.springframework.util.ObjectUtils.isEmpty(str);
    }

    public static String prefixPadZero(String src, Integer length) {
        return String.format("%" + length + "s", src).replace(" ", "0");
    }

    public static String join(List<String> strings) {
        return String.join(",", strings);
    }

    public static String joinField(List<String> strings) {
        return String.join(".", strings);
    }

    public static int getCount(String str, String tag) {
        if (StringUtils.isEmpty(str)) return 0;
        return str.split(tag).length - 1;
    }

    /**
     * 去除两边所有空格，并且中间多个空格合并为一个
     *
     * @param str
     * @return
     */
    public static String trim(String str) {
        return str.trim().replaceAll("\\s+", " ");
    }

    /**
     * description: 随机获取字符串
     * version: 1.0
     * date: 2022/4/27 16:09
     * author: AN
     *
     * @param length 长度
     * @return java.lang.String
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * description: 补零返回字符串
     * version: 1.0
     * date: 2022/5/14 14:17
     * author: AN
     *
     * @param length 长度
     * @param value  原始值
     * @return java.lang.String
     */
    public static String makeUpZero(Integer length, Integer value) {
        return String.format("%0" + length + "d", value);
    }

    /**
     * 分割字符串
     *
     * @param str
     * @param tag
     * @return
     */
    public static List<String> split(String str, String tag) {
        return List.of(str.split(tag));
    }


    /**
     * 将list<string> 转 list<object>
     *
     * @param list
     * @return
     */
    public static List<Object> convertToObject(List list) {
        return list;
    }

    /**
     * 判断是否为整数
     *
     * @param str 源字符串
     * @return
     */
    public static boolean isInt(String str) {
        Pattern pattern = Pattern.compile("\\d*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
