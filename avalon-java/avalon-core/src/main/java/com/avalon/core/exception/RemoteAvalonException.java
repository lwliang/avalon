/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;

public class RemoteAvalonException extends AvalonException{
    @Override
    public Integer getCode() {
        return 5000;
    }

    public RemoteAvalonException(String message) {
        super(message);
    }
}
