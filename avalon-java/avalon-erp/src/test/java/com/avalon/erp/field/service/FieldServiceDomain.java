/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.field.service;

import com.avalon.core.field.BigDecimalField;
import com.avalon.core.field.Field;
import com.avalon.core.field.IntegerField;
import com.avalon.core.field.StringField;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FieldServiceDomain extends AbstractService {
    @Override
    public String getServiceName() {
        return "field.test";
    }

    private final Field age = IntegerField.Builder.getInstance().setLabel("年龄").build();
    private final Field firstName = StringField.Builder.getInstance().setLabel("学名").build();
    private final Field salary = BigDecimalField.Builder.getInstance().setLabel("薪水").build();
}
