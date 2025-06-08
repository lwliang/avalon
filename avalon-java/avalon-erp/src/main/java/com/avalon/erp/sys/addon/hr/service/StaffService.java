/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.hr.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.DelegateInheritMap;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StaffService extends AbstractService {
    @Override
    public String getServiceName() {
        return "hr.staff";
    }

    @Override
    public DelegateInheritMap getDelegateInherit() {
        DelegateInheritMap delegateInheritMap = new DelegateInheritMap();
        delegateInheritMap.put("common.partner", "partnerId");
        return delegateInheritMap;
    }

    @Override
    public String getLabel() {
        return "员工";
    }

    public final Field partnerId = Fields.createMany2one("联系人", "common.partner");
    public final Field code = Fields.createString("员工编码");
    public final Field jobId = Fields.createMany2one("岗位", "hr.job");
    public final Field orgId = Fields.createMany2one("组织", "hr.org");
    public final Field userId = Fields.createMany2one("账号", "base.user");
}
