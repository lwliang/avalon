/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;

public class AuthenticationException extends AvalonException {

    @Override
    public Integer getCode() {
        return 2000;
    }

    public AuthenticationException(String message) {
        super(message);
    }

    @Override
    public Integer getStatusCode() {
        return 401;
    }
}
