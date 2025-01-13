package com.sachin.urlshortener.config;

import com.sachin.urlshortener.interceptor.IpInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipInterceptor()).addPathPatterns("/**"); // Add your path patterns here
    }

    @Bean
    public IpInterceptor ipInterceptor() {
        return new IpInterceptor();
    }
}
