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

/**
 * 职位
 */
@Service
@Slf4j
public class HrJobService extends AbstractService {
    @Override
    public String getServiceName() {
        return "hr.job";
    }

    private final Field departId = Fields.createMany2one("部门", "hr.department");
    private final Field parentId = Fields.createMany2one("上级职位", "hr.job");
}
