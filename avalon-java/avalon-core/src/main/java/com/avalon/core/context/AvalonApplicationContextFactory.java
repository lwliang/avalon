/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.context;

import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class AvalonApplicationContextFactory implements ApplicationContextFactory {
    private static final AvalonApplicationContextFactory instance = new AvalonApplicationContextFactory();

    public static AvalonApplicationContextFactory getInstance() {
        return instance;
    }

    @Override
    public ConfigurableApplicationContext create(WebApplicationType webApplicationType) {
        try {
            if (webApplicationType == WebApplicationType.REACTIVE) {
                return new AnnotationConfigReactiveWebServerApplicationContext();
            }
            return AvalonApplicationContext.getInstance();
        } catch (Exception var2) {
            throw new IllegalStateException("Unable create a default ApplicationContext instance, you may need a custom ApplicationContextFactory", var2);
        }
    }
}
