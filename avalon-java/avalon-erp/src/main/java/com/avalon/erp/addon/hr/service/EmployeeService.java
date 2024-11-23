/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.hr.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeService extends AbstractService {
    @Override
    public String getServiceName() {
        return "hr.employee";
    }

    private final Field phone = Fields.createString("电话");
    private final Field loginId = Fields.createMany2one("登陆id", "base.user");
}
