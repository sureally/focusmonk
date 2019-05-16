package com.netease.focusmonk.config;

import com.netease.focusmonk.interceptor.JWTInterceptor;
import com.netease.focusmonk.utils.StringToDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @author hejiecheng
 * @Date 2019-05-05
 * 拦截器配置
 */
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

    private final static String[] EXCLUDE = {
            "/SDSController/login",
            "/SDSController/sendSMS"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(JWTInterceptor()).excludePathPatterns(Arrays.asList(EXCLUDE));
    }

    @Bean
    public JWTInterceptor JWTInterceptor() {
        return new JWTInterceptor();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());
    }
}