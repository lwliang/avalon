/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.model.RecordRow;

import java.util.List;

public interface IInvokeMethod {
    Object invokeMethod(String methodName, List<Object> ids, RecordRow row);
}
