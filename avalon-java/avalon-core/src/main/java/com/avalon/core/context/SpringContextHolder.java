/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static GenericWebApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = (GenericWebApplicationContext) applicationContext;
    }


    /**
     * 返回上下文
     */
    public static GenericWebApplicationContext getContext() {
        return SpringContextHolder.applicationContext;
    }
}
