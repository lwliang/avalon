/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.exception;

import com.avalon.core.exception.AvalonException;

/**
 * word文件异常
 */

public class WordFileException extends AvalonException {
    @Override
    public Integer getCode() {
        return 4001;
    }

    public WordFileException(String message) {
        super(message);
    }

    public WordFileException(String mark, String message) {
        super(mark, message);
    }

    public WordFileException(Integer code, String message) {
        super(code, message);
    }

    public WordFileException(Integer code, String mark, String message) {
        super(code, mark, message);
    }
}
