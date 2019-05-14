package com.netease.focusmonk.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @ClassName StringToDateConverter
 * @Author konghaifeng
 * @Date 2019/5/11 12:55
 **/
public class StringToDateConverter implements Converter<String, Date> {

    private static final String dateFormat      = "yyyy/MM/dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy/MM/dd";

    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = source.trim();

        try {
            source = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException("日期绑定异常");
        }

        try {
            if (source.contains("/")) {
                SimpleDateFormat formatter;
                if (source.contains(":")) {
                    formatter = new SimpleDateFormat(dateFormat);
                } else {
                    formatter = new SimpleDateFormat(shortDateFormat);
                }
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                return formatter.parse(source);
            } else if (source.matches("^\\d+$")) {
                Long lDate = new Long(source);
                return new Date(lDate);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to Date fail", source));
        }
        throw new RuntimeException(String.format("parser %s to Date fail", source));
    }
}