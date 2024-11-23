/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.field.Field;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FieldHashMap extends HashMap<String, Field> {

    @Override
    public Field put(String fieldName, Field field) {
        Method setName = null;
        Method getName = null;
        Class<?> cls = field.getClass();
        while (cls != Object.class) {//将变量名设置为字段名
            try {
                if (ObjectUtils.isNull(setName)) {
                    setName = cls.getDeclaredMethod("setName", String.class);
                }
                if (ObjectUtils.isNull(getName)) {
                    getName = cls.getDeclaredMethod("getName");
                }
                if (ObjectUtils.isNotNull(getName) && ObjectUtils.isNotNull(setName)) {
                    break;
                }
            } catch (NoSuchMethodException e) {
                // log.debug("setName报错->" + e.getMessage());
                cls = cls.getSuperclass();
            }
        }

        try {
            Object getNameField = null;
            if (ObjectUtils.isNotNull(getName)) {
                getName.setAccessible(true);
                getNameField = getName.invoke(field);
            }
            if (ObjectUtils.isEmpty(getNameField) && ObjectUtils.isNotNull(setName)) {
                setName.setAccessible(true);
                setName.invoke(field, fieldName);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            // log.debug("setName调用报错->" + e.getMessage());
        }

        super.put(fieldName, field);
        return field;
    }

    public FieldHashMap() {
        super();
    }

    public FieldHashMap(Map<String, Field> fieldMap) {
        super();

        this.putAll(fieldMap);
    }
}
