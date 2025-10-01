package com.lgcns.svcp.prod.config;

import com.lgcns.svcp.prod.interceptor.ApiLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lgcns.svcp.prod.interceptor.UserInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserInterceptor userInterceptor;

    @Autowired
    private ApiLoggingInterceptor apiLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(userInterceptor);
        registry.addInterceptor(apiLoggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/*swagger*/**", "/**/*swagger*", "/**/*api-docs*/**");
    }
    
}
