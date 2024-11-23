/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file;

import com.avalon.core.context.AvalonApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = "com.avalon")
public class FileProgram {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FileProgram.class);
        springApplication.setApplicationContextFactory(AvalonApplicationContextFactory.getInstance());
        springApplication.run(args);
    }
}
