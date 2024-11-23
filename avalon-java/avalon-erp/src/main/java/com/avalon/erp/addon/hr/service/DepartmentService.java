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
public class DepartmentService extends AbstractService {
    @Override
    public String getServiceName() {
        return "hr.department";
    }

    private final Field jobs = Fields.createOne2many("职位", "hr.job", "departId");
    private final Field companyId = Fields.createMany2one("公司", "hr.company");
    private final Field parentId = Fields.createMany2one("上级部门", "hr.department");
    private final Field leaderJobId = Fields.createMany2one("主管", "hr.job");
}
