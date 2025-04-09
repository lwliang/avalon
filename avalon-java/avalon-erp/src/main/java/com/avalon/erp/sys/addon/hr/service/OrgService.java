/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.hr.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractTreeService;
import com.avalon.erp.sys.addon.base.model.enums.OrgTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrgService extends AbstractTreeService {
    @Override
    public String getServiceName() {
        return "hr.org";
    }

    @Override
    public String getLabel() {
        return "组织";
    }

    public final Field code = Fields.createString("组织编码");
    public final Field type = Fields.createSelection("组织类型",
            OrgTypeEnum.class, OrgTypeEnum.department);
    public final Field chargeUserId = Fields.createMany2one("负责人", "hr.staff");
}
