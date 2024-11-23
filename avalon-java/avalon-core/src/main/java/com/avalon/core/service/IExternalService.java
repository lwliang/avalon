/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.field.Field;

public interface IExternalService {
    void setServiceName(String serviceName);
    void addField(Field field);
}
