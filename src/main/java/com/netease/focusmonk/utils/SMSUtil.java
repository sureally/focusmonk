package com.netease.focusmonk.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author hejiecheng
 * @Date 2019-04-30
 */
@Slf4j
public class SMSUtil {

    private static final String USERNAME = "focusmonk";
    private static final String PASSWORD = md5("Hejiecheng3820");
    private static final String URL = "http://api.smsbao.com/sms";
    private static final String PRE = "【专注小和尚】您的验证码是";
    private static final String END = ",５分钟内有效。若非本人操作请忽略此消息。";
    private static final String SUCCESS_CODE = "0";

    public static boolean sendSMS(String phone, String code) {
        StringBuffer httpArg = new StringBuffer();
        httpArg.append("u=").append(USERNAME).append("&");
        httpArg.append("p=").append(PASSWORD).append("&");
        httpArg.append("m=").append(phone).append("&");
        httpArg.append("c=").append(encodeUrlString(PRE + code + END, "UTF-8"));

        String result = request(URL, httpArg.toString());
        log.info("发送验证码的结果：" + result);
        return SUCCESS_CODE.equals(result);
    }

    private static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            java.net.URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = reader.readLine();
            if (strRead != null) {
                sbf.append(strRead);
                while ((strRead = reader.readLine()) != null) {
                    sbf.append("\n");
                    sbf.append(strRead);
                }
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 对密码进行md5编码
     * @param plainText
     * @return
     */
    private static String md5(String plainText) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    private static String encodeUrlString(String str, String charset) {
        String strret = null;
        if (str == null) {
            return str;
        }
        try {
            strret = java.net.URLEncoder.encode(str, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strret;
    }

}
