/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobService extends AbstractTreeService {
    @Override
    public String getServiceName() {
        return "base.job";
    }

    @Override
    public String getLabel() {
        return "岗位";
    }

    public final Field code = Fields.createString("岗位编码");
    public final Field orgId = Fields.createMany2one("组织", "base.org");
}
