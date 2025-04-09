package com.avalon.erp.config;

import com.avalon.core.context.Context;
import com.avalon.core.util.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/25 14:09
 */
@Configuration
public class FeignConfig {
    private final Context context;

    public FeignConfig(Context context) {
        this.context = context;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                String token = context.getToken();
                if (StringUtils.isNotEmpty(token)) {
                    template.header("Token", token);
                }
            }
        };
    }
}
