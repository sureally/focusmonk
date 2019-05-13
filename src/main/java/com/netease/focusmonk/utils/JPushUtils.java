package com.netease.focusmonk.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author zhenghang
 * @date 2019/5/13
 */
public class JPushUtils {
    protected static final Logger LOG = LoggerFactory.getLogger(JPushUtils.class);

    //极光推送配置的应用APP_key
    protected static final String APP_KEY ="d83f8843787ecf30e3d42d8f";
    //极光推送配置的应用master_secret
    protected static final String MASTER_SECRET = "e2f7d05144b36d4ad50ab198";
    //发送的push消息
    public static final String ALERT = "快回来，已积累时长和书卷要消失了";

    public static void SendPush(String registrationId) throws APIConnectionException, APIRequestException {
        ClientConfig clientConfig = ClientConfig.getInstance();
        final JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);
//        String authCode = ServiceHelper.getBasicAuthorization(APP_KEY, MASTER_SECRET);
        // Here you can use NativeHttpClient or NettyHttpClient or ApacheHttpClient.
        // Call setHttpClient to set httpClient,
        // If you don't invoke this method, default httpClient will use NativeHttpClient.

//        ApacheHttpClient httpClient = new ApacheHttpClient(authCode, null, clientConfig);
//        NettyHttpClient httpClient =new NettyHttpClient(authCode, null, clientConfig);
//        jpushClient.getPushClient().setHttpClient(httpClient);
//        final PushPayload payload = buildPushObject_android_and_ios();
//        // For push, all you need do is to build PushPayload object.
//        PushPayload payload = buildPushObject_all_alias_alert();
//        PushPayload pushPayload = buildPushObject_all_all_alert();
        PushPayload pushPayload = buildPushObject_android_cid(registrationId);
        PushResult result = jpushClient.sendPush(pushPayload);
//        try {
//
//            LOG.info("Got result - " + result);
////            System.out.println(result);
//            // 如果使用 NettyHttpClient，需要手动调用 close 方法退出进程
//            // If uses NettyHttpClient, call close when finished sending request, otherwise process will not exit.
//            // jpushClient.close();
//        } catch (APIConnectionException e) {
//            LOG.error("Connection error. Should retry later. ", e);
//            LOG.error("Sendno: " + pushPayload.getSendno());
//
//        } catch (APIRequestException e) {
//            LOG.error("Error response from JPush server. Should review and fix it. ", e);
//            LOG.info("HTTP Status: " + e.getStatus());
//            LOG.info("Error Code: " + e.getErrorCode());
//            LOG.info("Error Message: " + e.getErrorMessage());
//            LOG.info("Msg ID: " + e.getMsgId());
//            LOG.error("Sendno: " + pushPayload.getSendno());
//        }
    }

    //发送给所有平台的所有用户
    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }

    //根据registrationId发送给指定用户
    public static PushPayload buildPushObject_android_cid(String registrationId) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.registrationId(registrationId))
//                .setAudience(Audience.registrationId(list))
                .setNotification(Notification.alert(ALERT))
//                .setCid("cid")
                .build();
    }
}
