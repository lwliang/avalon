package com.avalon.core.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.RecordRow;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/08 20:27
 */
public interface ICreateService {
    RecordRow create(RecordRow defaultRow) throws AvalonException;
}
