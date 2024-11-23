/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import java.util.Base64;

public class Base64Utils {
    /**
     * Base64 加密
     *
     * @param data 明文
     * @return
     */
    public static String encoded(String data) {
        byte[] bytes = data.getBytes();
        return Base64.getEncoder().encodeToString(bytes);

    }

    /**
     * Base64 解密
     *
     * @param base64Str
     * @return
     */
    public static String decode(String base64Str) {

        byte[] decoded = Base64.getDecoder().decode(base64Str);

        return new String(decoded);
    }
}
