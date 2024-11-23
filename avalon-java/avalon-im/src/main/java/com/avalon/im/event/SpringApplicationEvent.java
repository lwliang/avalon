/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.event;

import com.avalon.core.context.Context;
import com.avalon.core.util.StringUtils;
import com.avalon.im.service.NettyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class SpringApplicationEvent implements ApplicationListener<ApplicationEvent> {
    private final Context context;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public SpringApplicationEvent(Context context,
                                  @Qualifier("taskExecutor") ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.context = context;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
            NettyService.start(threadPoolExecutor);
        }
    }
}
