package com.netease.focusmonk.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author hejiecheng
 * @Date 2019-05-07
 */
@Data
@Component
public class SMSProperties {

    @Value("${sms.send.url}")
    private String sendUrl;

    @Value("${sms.verify.url}")
    private String verifyUrl;

    @Value("${sms.app.key}")
    private String appKey;

    @Value("${sms.app.secret}")
    private String appSecret;

    @Value("${sms.nonce}")
    private String nonce;

    @Value("${sms.code.length}")
    private String codeLength;

    @Value("${sms.code.model}")
    private String codeModel;

}
