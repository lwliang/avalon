/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file.config;


import com.avalon.file.interceptor.AvalonHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class FileAppConfig extends WebMvcConfigurationSupport {

    @Autowired
    private AvalonHandlerInterceptor avalonHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(avalonHandlerInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/file/down/**")
                .excludePathPatterns("/service/file/down")
                .excludePathPatterns("/test");
    }
}
