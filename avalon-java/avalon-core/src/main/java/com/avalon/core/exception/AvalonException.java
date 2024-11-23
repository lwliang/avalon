/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;

import com.avalon.core.util.StringUtils;

public class AvalonException extends RuntimeException implements IAvalonException {
    private String mark;//多语言的标记 暂时不用
    private Integer code = 500;//编码

    public AvalonException(String message) {
        this("", message);
    }

    public AvalonException(String message, Exception e) {
        super(message, e);
    }

    public AvalonException(String mark, String message) {
        super(message);
        this.mark = mark;
    }

    public AvalonException(Integer code, String message) {
        this(code, "", message);
    }

    public AvalonException(Integer code, String mark, String message) {
        super(StringUtils.isEmpty(mark) ? message : mark);
        this.mark = mark;
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
