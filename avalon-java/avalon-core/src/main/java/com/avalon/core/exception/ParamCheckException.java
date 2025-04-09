package com.avalon.core.exception;

import com.avalon.core.field.Field;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/11 10:27
 */

/**
 * 检查接口参数
 */
public class ParamCheckException extends AvalonException {
    public Integer getCode() {
        return 1500;
    }

    public ParamCheckException(String mark) {
        super(mark);
    }
}