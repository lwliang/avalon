/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.config;

import com.avalon.erp.interceptor.AvalonHandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class ErpAppConfig extends WebMvcConfigurationSupport {

    @Autowired
    private AvalonHandlerInterceptor avalonHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(avalonHandlerInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/design/**")
                .excludePathPatterns("/db/**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/index")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/validate")
                .excludePathPatterns("/error")
                .excludePathPatterns("/wx/**")
                .excludePathPatterns("/module/**")
                .excludePathPatterns("/electric/**")
                .excludePathPatterns("/bank/**")
                .excludePathPatterns("/school/**")
                .excludePathPatterns("/test");

    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/templates/static/")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/templates/favicon.ico");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();

        // 生成json时，将所有Long转换成String
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(0, jackson2HttpMessageConverter);
    }
}
