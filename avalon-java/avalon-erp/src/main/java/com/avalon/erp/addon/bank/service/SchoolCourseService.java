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
public class SchoolCourseService extends AbstractService {
    @Override
    public String getServiceName() {
        return "bank.school.course";
    }

    @Override
    public String getLabel() {
        return "课程";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("课程名称", true);
    }

    public final Field schoolId = Fields.createMany2one("学校", "bank.school");
    public final Field collectInfo = Fields.createBoolean("是否收集收集&地址");
    public final Field amount = Fields.createBigDecimal("金额(元)");
}
