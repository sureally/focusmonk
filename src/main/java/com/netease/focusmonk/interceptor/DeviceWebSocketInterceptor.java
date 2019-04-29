package com.netease.focusmonk.interceptor;

import com.netease.focusmonk.common.SocketMsgCode;
import com.netease.focusmonk.handler.DeviceWebSocketHandler;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * @author hejiecheng
 * @Date 2019-04-29
 */
public class DeviceWebSocketInterceptor implements HandshakeInterceptor {

    /**
     * websocket握手前
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            HttpSession session = request.getSession(false);
            if (session != null) {
                // 检查是否已经有链接
                dealWithConnected(session);
                // 添加自定义属性加入到websocketsession中
                map.put("username", session.getAttribute("username"));
                return true;
            }
        }
        return false;
    }

    /**
     * 处理并判断该账号是否已经登录
     * @param session
     */
    public boolean dealWithConnected(HttpSession session) {

        boolean isConnected = false;

        String username = session.getAttribute("username").toString();
        if (username == null) {
            System.out.println("用户不存在！");
        } else {
            Map<String, WebSocketSession> users = DeviceWebSocketHandler.users;
            if (users.containsKey(username)) {
                WebSocketSession webSocketSession = users.get(username);
                TextMessage textMessage = new TextMessage(SocketMsgCode.FORCE_LOFOUT_MSG.getJson());
                try {
                    webSocketSession.sendMessage(textMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 移除之前的登录信息
                users.remove(username);
                isConnected = true;
            }
        }
        return isConnected;
    }

    /**
     * websocket握手后
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {

    }
}
