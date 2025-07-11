package com.avalon.erp.util;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/06/07 11:29
 */
public class PinyinUtil {
    public static String getFirstLetters(String chinese) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chinese.length(); i++) {
            char c = chinese.charAt(i);
            // 判断是不是汉字
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
                if (pinyins != null && pinyins.length > 0) {
                    sb.append(Character.toUpperCase(pinyins[0].charAt(0)));
                }
            } else if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toUpperCase(c));
            }
        }
        return sb.toString();
    }
}
