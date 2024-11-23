/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import java.util.ArrayList;
import java.util.List;

public class ObjectUtils {
    public static Boolean isNull(Object obj) {
        return obj == null;
    }

    public static Boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static Boolean isEmpty(Object obj) {
        return org.springframework.util.ObjectUtils.isEmpty(obj);
    }

    public static Boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static List<Object> object2List(Object id) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(id);
        return objects;
    }

    public static List<Object> toList(Object... value) {
        return List.of(value);
    }

    public static String toString(List<Object> list) {
        StringBuilder stringBuffer = new StringBuilder();
        for (Object obj : list) {
            stringBuffer.append(obj.toString());
        }
        return stringBuffer.toString();
    }
}
