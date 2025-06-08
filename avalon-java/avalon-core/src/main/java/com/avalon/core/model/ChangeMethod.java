package com.avalon.core.model;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/07 10:29
 */
@Data
public class ChangeMethod {
    private String[] fieldValues;
    private Method method;
    private Set<String> fields;

    private void setFieldValues(String[] fieldValues) {
        this.fieldValues = fieldValues;
        fields = Arrays.stream(fieldValues).map(String::trim).collect(Collectors.toSet());
    }

    public ChangeMethod(String[] fieldValues, Method method) {
        setFieldValues(fieldValues);
        this.method = method;
    }

    /**
     * 快速判断 是否存在某字段
     *
     * @param fieldName 字段名
     * @return 是否
     */
    public Boolean containField(String fieldName) {
        return fields.contains(fieldName);
    }
}
