package com.netease.focusmonk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * 配置并注入resttemplate
 * @Author hejiecheng
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        // requestFactory.setConnectTimeout(1000);
        // requestFactory.setReadTimeout(1000);

        StringHttpMessageConverter messageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(messageConverter);
        return restTemplate;
    }

}
