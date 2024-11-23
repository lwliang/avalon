/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.context;

import com.avalon.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AvalonApplicationRunner implements ApplicationRunner {
    @Autowired
    private Context context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String database = context.getApplicationConfig().getDataSource().getDatabase();
        if (StringUtils.isNotEmpty(database)) {
            context.init(database);
        } else {
            context.init(context.getDefaultDatabase());
        }
    }
}
