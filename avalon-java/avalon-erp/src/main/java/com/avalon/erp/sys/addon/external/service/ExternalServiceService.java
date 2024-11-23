/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.external.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.ExternalService;
import com.avalon.erp.sys.addon.base.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import com.avalon.core.model.Record;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalServiceService extends ServiceService {
    @Override
    public String getServiceName() {
        return "external.service";
    }

    private final Field isExternal = Fields.createBoolean("扩展", true, true);


    public Record getAllExternalService() {
        return select("module, serviceName, label,isExternal",
                Condition.equalCondition(isExternal, true));
    }

    /**
     * 注册服务
     *
     * @param row 记录
     * @return 扩展服务
     */
    public ExternalService register(RecordRow row, AbstractModule module) {
        ExternalService service = getContext().getAvalonApplicationContext().getBean(ExternalService.class);
        service.setServiceName(row.getString("serviceName"));
        service.setModule(module);
        return service;
    }
}
