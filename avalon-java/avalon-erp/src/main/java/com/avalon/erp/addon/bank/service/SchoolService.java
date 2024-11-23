/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchoolService extends AbstractService {
    @Override
    public String getServiceName() {
        return "bank.school";
    }

    @Override
    public String getLabel() {
        return "报名学校";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("学校", true);
    }

    public final Field courseIds = Fields.createOne2many("课程", "bank.school.course", "schoolId");
}
