/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;

public interface IAvalonException {

    Integer getCode();

    String getMessage();

    default Integer getStatusCode() {
        return 500;
    }
}
