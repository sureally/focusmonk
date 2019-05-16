package com.netease.focusmonk.config;

import com.netease.focusmonk.handler.DeviceWebSocketHandler;
import com.netease.focusmonk.interceptor.DeviceWebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author hejiecheng
 * @Date 2019-04-29
 * websocket配置
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(doDeviceWebSocketHandler(), "/websocket")
                .addInterceptors(new DeviceWebSocketInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public DeviceWebSocketHandler doDeviceWebSocketHandler() {
        return new DeviceWebSocketHandler();
    }
}
