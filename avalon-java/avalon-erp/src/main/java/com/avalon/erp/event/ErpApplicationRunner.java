/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.event;

import com.avalon.core.context.Context;
import com.avalon.core.model.Record;
import com.avalon.erp.sys.addon.external.service.ExternalFieldService;
import com.avalon.erp.sys.addon.external.service.ExternalModuleService;
import com.avalon.erp.sys.addon.external.service.ExternalServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErpApplicationRunner implements ApplicationListener<ApplicationEvent> {
    @Autowired
    private Context context;

    @Autowired
    private ExternalFieldService externalFieldService;

    @Autowired
    private ExternalModuleService externalModuleService;

    @Autowired
    private ExternalServiceService externalServiceService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
//            Record allModels = externalModuleService.getAllModes();
//            allModels.forEach(model -> {
//
//            });
            context.setSystemPrepare(true);
        }
    }
}
