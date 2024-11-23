/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;

import com.avalon.core.field.Field;

/**
 * 判断字段存在的问题 比如必填，长度，数据不符合区间
 */
public class FieldCheckException extends AvalonException {

    private Field field;
    private String serviceName;

    public Integer getCode() {
        return 1000;
    }

    public FieldCheckException(String mark) {
        super(mark);
    }

    public FieldCheckException(Field field, String mark) {
        super(mark);
        this.field = field;
    }

    public FieldCheckException(String serviceName, Field field, String mark) {
        super(mark);
        this.field = field;
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Field getField() {
        return field;
    }
}
