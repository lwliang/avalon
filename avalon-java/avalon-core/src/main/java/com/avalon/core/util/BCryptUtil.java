/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import java.util.UUID;

public class BCryptUtil {
    /**
     * 获取uuid唯一值
     *
     * @return
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static String[] chars = new String[]{"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * 获取唯一值作为用户的
     * 本算法利用62个可打印字符，通过随机生成32位UUID，由于UUID都为十六进制，所以将UUID分为8组，
     * 每4个为一组，然后通过模62操作，结果作为索引取出字符。
     * 这样重复率大大降低。经测试，在生成一千万个数据也没有出现重复，完全满足大部分需求。
     *
     * @return
     */
    public static String shortUUID() {
        String uuid = simpleUUID();
        StringBuilder shortBuffer = new StringBuilder();
        for (int i = 0; i < 8; i++) { //分为8组
            String str = uuid.substring(i * 4, i * 4 + 4); //每组4位
            int x = Integer.parseInt(str, 16); //输出str在16进制下的表示
            shortBuffer.append(chars[x % 0x22]); //用该16进制数取模62（十六进制表示为314（14即E）），结果作为索引取出字符
        }
        return shortBuffer.toString();//生成8位字符
    }
}
