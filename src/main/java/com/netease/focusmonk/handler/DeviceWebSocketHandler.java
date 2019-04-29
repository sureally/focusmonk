package com.netease.focusmonk.handler;

import com.netease.focusmonk.common.SocketMsgCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hejiecheng
 * @Date 2019-04-29
 */

public class DeviceWebSocketHandler extends TextWebSocketHandler {

    /**
     * users: 用户列表信息
     * 后续需要对WebsocketSession序列化存入到redis中
     */
    public static final Map<String, WebSocketSession> users = new HashMap<>();

    /**
     * 连接关闭时调用
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        users.remove(getUserInfo(session));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        users.remove(getUserInfo(session));
    }

    /**
     * 成功建立连接后
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = getUserInfo(session);
        if (username != null && !username.isEmpty()) {
            users.put(username, session);
            session.sendMessage(new TextMessage(SocketMsgCode.SUCCESS_CONNECT.getJson()));
        } else {
            session.sendMessage(new TextMessage(SocketMsgCode.FAIL_CONNECT.getJson()));
        }
    }

    /**
     * 处理前端发送的信息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    /**
     * 发送指定信息给相应的用户
     * @param username
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String username, String message) {
        if (users.get(username) == null) {
            return false;
        }

        WebSocketSession webSocketSession = users.get(username);
        if (!webSocketSession.isOpen()) {
            return false;
        }
        try {
            webSocketSession.sendMessage(new TextMessage(message));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getUserInfo(WebSocketSession session) {
        try {
            String username = session.getAttributes().get("username").toString();
            return username;
        } catch (Exception e) {
            return null;
        }
    }
}
