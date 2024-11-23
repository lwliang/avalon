/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RuleService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.rule";
    }
    public final Field domainForce = Fields.createText("domain");
    public final Field permRead = Fields.createBoolean("查询",
            false, true);
    public final Field permWrite = Fields.createBoolean("编辑",
            false, true);
    public final Field permCreate = Fields.createBoolean("新增",
            false, true);
    public final Field permUnlink = Fields.createBoolean("删除",
            false, true);
    public final Field active = Fields.createBoolean("有效", false,
            true);
    public final Field serviceId = Fields.createMany2one("服务",
            "base.service");
}
