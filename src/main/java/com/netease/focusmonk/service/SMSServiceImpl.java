package com.netease.focusmonk.service;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.config.SMSProperties;
import com.netease.focusmonk.utils.CheckSumUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author hejiecheng
 * @Date 2019-05-07
 */
@Service
public class SMSServiceImpl {

    private final RestTemplate restTemplate;

    private final SMSProperties smsProperties;

    private final RedisServiceImpl redisService;

    public SMSServiceImpl(RestTemplate restTemplate, SMSProperties smsProperties, RedisServiceImpl redisService) {
        this.restTemplate = restTemplate;
        this.smsProperties = smsProperties;
        this.redisService = redisService;
    }

    /**
     * 给手机发送验证码
     * @param phone
     * @return
     */
    public boolean sendCode(String phone) {

        HttpHeaders httpHeaders = getHttpHeaders();

        // 请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("mobile", phone);
        body.add("templateid", smsProperties.getCodeModel());
        body.add("codeLen", smsProperties.getCodeLength());

        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(body, httpHeaders);

        // URL
        String url = smsProperties.getSendUrl();

        // 发送请求
        HttpEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        return statusVerify(responseEntity);

    }

    /**
     * 验证验证码
     * @param phone
     * @param code
     * @return
     */
    public boolean verifyCode(String phone, String code) {

        HttpHeaders httpHeaders = getHttpHeaders();

        // 请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("mobile", phone);
        body.add("code", code);

        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(body, httpHeaders);

        // URL
        String url = smsProperties.getVerifyUrl();

        // 发送请求
        HttpEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        return statusVerify(responseEntity);
    }

    /**
     * 验证码请求间隔验证
     * @param phone
     * @return
     */
    public boolean judgeCodePhone(String phone) {
        return redisService.getAndSet("code_" + phone, 5);
    }

    /**
     * 验证码短信请求间隔验证
     * @param phone
     * @return
     */
    public boolean judgeSmsPhone(String phone) {
        return redisService.getAndSet("sms_" + phone, 30);
    }

    /**
     * 获取请求头
     * @return
     */
    private HttpHeaders getHttpHeaders() {
        String appKey = smsProperties.getAppKey();
        String appSecret = smsProperties.getAppSecret();
        String nonce = smsProperties.getNonce();
        String curTime = String.valueOf(System.currentTimeMillis()/1000);

        // 计算checkSum
        String checkSum = CheckSumUtil.getCheckSum(appSecret, nonce, curTime);

        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("AppKey", appKey);
        httpHeaders.add("Nonce", nonce);
        httpHeaders.add("CurTime", curTime);
        httpHeaders.add("CheckSum", checkSum);
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        httpHeaders.add("charset", "utf-8");
        return httpHeaders;
    }

    /**
     * 验证结果
     * @param responseEntity
     * @return
     */
    private boolean statusVerify(HttpEntity<String> responseEntity) {
        if (responseEntity == null) {
            return false;
        }
        if (((ResponseEntity<String>) responseEntity).getStatusCodeValue() != 200) {
            return false;
        } else {
            JSONObject responseBody = JSONObject.parseObject(responseEntity.getBody());
            if ("200".equals(responseBody.getString("code"))) {
                return true;
            } else {
                return false;
            }
        }
    }

}
