/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.field.Field;
import com.avalon.core.model.FieldHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service("external_service")
@Scope("prototype")
public class ExternalService extends AbstractService implements IExternalService {
    private String serviceName;

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public void addField(Field field) {
        FieldHashMap fieldMap = getFieldMap();
        fieldMap.put(field.getName(), field);
    }

    @PostConstruct
    public void postConstruct() {
        setFieldMap(new FieldHashMap());
    }
}
