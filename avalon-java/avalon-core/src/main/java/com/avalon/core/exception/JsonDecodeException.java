/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;

public class JsonDecodeException extends AvalonException {

    @Override
    public Integer getCode() {
        return 3000;
    }

    public JsonDecodeException(String message) {
        super(message);
    }
}
