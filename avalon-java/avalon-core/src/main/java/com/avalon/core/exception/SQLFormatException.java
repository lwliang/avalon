/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;

import java.sql.SQLException;

/**
 * 格式化出错的问题
 */
public class SQLFormatException extends SQLException implements IAvalonException {

    public SQLFormatException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 1001;
    }
}
