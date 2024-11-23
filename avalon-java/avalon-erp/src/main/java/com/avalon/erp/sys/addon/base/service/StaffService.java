/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StaffService extends PartnerService {
    @Override
    public String getServiceName() {
        return "base.staff";
    }

    @Override
    public String getLabel() {
        return "员工";
    }
    public final Field code = Fields.createString("员工编码");
    public final Field jobId = Fields.createMany2one("岗位", "base.job");
    public final Field orgId = Fields.createMany2one("组织", "base.org");
}
