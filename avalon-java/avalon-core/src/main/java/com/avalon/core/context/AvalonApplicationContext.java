/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.context;

import com.avalon.core.orm.DefaultORMMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

/**
 * 自定义的ApplicationContext实现类
 */
public class AvalonApplicationContext extends AnnotationConfigServletWebServerApplicationContext {
    @Getter
    private final DefaultORMMapper defaultORM = new DefaultORMMapper(); // 默认所有模块代码
    @Getter
    private static AvalonApplicationContext instance = new AvalonApplicationContext();

    protected AvalonApplicationContext() {
//        log.debug("application context constructor");
    }

    private Logger log = LoggerFactory.getLogger(AvalonApplicationContext.class);

    @Override
    protected void initPropertySources() {
        super.initPropertySources();
//        log.debug("execute override initPropertySources");
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        super.postProcessBeanFactory(beanFactory);
//        log.debug("execute override postProcessBeanFactory");
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
//        log.debug("execute override onRefresh");
    }

}
