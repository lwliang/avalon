/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.SelectionHashMap;

import java.lang.reflect.Method;

public class EnumUtils {
    public static SelectionHashMap getSelectionMapFromEnum(Class<? extends Enum> clz) throws AvalonException {
        try {
            // 2.得到所有枚举常量
            Object[] objects = clz.getEnumConstants();
            Method getName = clz.getMethod("getName");
            SelectionHashMap selectionHashMap = new SelectionHashMap();
            selectionHashMap.setType(clz);
            for (Object obj : objects) {
                selectionHashMap.put(obj.toString(), getName.invoke(obj).toString());
            }
            return selectionHashMap;
        } catch (Exception e) {
            throw new AvalonException(e.getMessage());
        }
    }

    public static SelectionHashMap getSelectionMapFromEnum(String className) throws AvalonException {
        Class<Enum> clz = null;
        try {
            clz = (Class<Enum>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new AvalonException(e.getMessage());
        }
        return getSelectionMapFromEnum(clz);
    }
}
