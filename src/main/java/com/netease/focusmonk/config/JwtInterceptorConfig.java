package com.netease.focusmonk.config;

import com.netease.focusmonk.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JwtInterceptorConfig
 * @Author konghaifeng
 * @Date 2019/5/10 14:28
 **/
@Configuration
public class JwtInterceptorConfig {

    @Bean
    public FilterRegistrationBean jwtFilter() {

        final FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JwtFilter());

        //添加需要拦截的url
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/RoomController/enter");
        urlPatterns.add("/RoomController/exit");
        urlPatterns.add("/addTaskDetail");

        int urlNum = urlPatterns.size();

        registrationBean.addUrlPatterns(urlPatterns.toArray(new String[urlNum]));

        return registrationBean;
    }
}
