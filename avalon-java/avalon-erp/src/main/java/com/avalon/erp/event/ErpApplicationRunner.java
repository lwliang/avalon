/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.event;

import com.avalon.core.context.Context;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErpApplicationRunner implements ApplicationListener<ApplicationEvent> {
    private final Context context;

    public ErpApplicationRunner(Context context) {
        this.context = context;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            context.setSystemPrepare(true);
            Record dbRecord = context.getDB();
            for (RecordRow row : dbRecord) {
                String db = row.getString("dataName");
                try {
                    context.init(db);
                    AbstractService serviceBean = context.getServiceBean("base.cron");
                    if (ObjectUtils.isNotNull(serviceBean)) {
                        context.invokeServiceMethod("base.cron", "startAllTask");
                    }
                } catch (Exception ex) {
                    log.error("启动定时任务失败," + db, ex);
                }
            }
        }
    }
}
