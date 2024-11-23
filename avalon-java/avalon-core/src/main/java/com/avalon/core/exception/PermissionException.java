/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;

public class PermissionException extends AvalonException{
    public PermissionException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return 403;
    }
}
