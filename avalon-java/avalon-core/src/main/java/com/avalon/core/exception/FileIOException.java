/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;

import java.io.IOException;

public class FileIOException extends IOException implements IAvalonException {

    public FileIOException(String msg) {
        super(msg);
    }

    @Override
    public Integer getCode() {
        return 4000;
    }
}
