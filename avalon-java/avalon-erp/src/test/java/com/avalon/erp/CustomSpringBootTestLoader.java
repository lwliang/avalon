/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp;

import com.avalon.core.context.AvalonApplicationContextFactory;
import com.avalon.erp.ErpApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;

public class CustomSpringBootTestLoader extends SpringBootContextLoader {
    @Override
    protected SpringApplication getSpringApplication() {
        SpringApplication springApplication = new SpringApplication(ErpApplication.class);
        springApplication.setApplicationContextFactory(AvalonApplicationContextFactory.getInstance());
        return springApplication;
    }
}
