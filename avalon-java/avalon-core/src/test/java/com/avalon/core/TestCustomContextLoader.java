package com.avalon.core;

import com.avalon.core.context.AvalonApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/02/12 23:40
 */
public class TestCustomContextLoader extends SpringBootContextLoader {
    @Override
    protected SpringApplication getSpringApplication() {
        SpringApplication springApplication = new SpringApplication(CoreApplication.class);
        springApplication.setApplicationContextFactory(AvalonApplicationContextFactory.getInstance());
        return springApplication;
    }
}
