/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core;

import com.avalon.core.context.AvalonApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class CoreApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CoreApplication.class);
        springApplication.setApplicationContextFactory(AvalonApplicationContextFactory.getInstance());
        springApplication.run(args);
    }
}
