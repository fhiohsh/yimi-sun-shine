package com.sky.WeSocket;

import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/**
 * @author: Joey
 * @Description: WebSocket服务 固定建立
 * @date:2024/5/3 23:36
 */
@Component
@ServerEndpoint("/ws/{type}/{sid}")
public class WebSocketServer {

    //存放会话对象
    private static Map<String, Session> sessionMap = new HashMap();

    // 存放后台管理端会话对象
    private static Map<String, Session> adminSessionMap = new HashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid, @PathParam("type") String type) {
//        System.out.println("客户端：" + sid + "建立连接");
        sessionMap.put(sid, session);

        if ("admin".equals(type)) {
            System.out.println("管理端：" + sid + "建立连接");
            adminSessionMap.put(sid, session);
        } else {
            System.out.println("客户端：" + sid + "建立连接");
            sessionMap.put(sid, session);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message,  @PathParam("type") String type, @PathParam("sid") String sid) {
        System.out.println("收到来自客户端：" + sid + "的信息:" + message);
        if (!"admin".equals(type)) { // 如果不是后台管理端发来的消息
            sendToAdminClient(message); // 将消息转发给所有后台管理端
}
    }

    public void sendToAdminClient(String message) {
        Collection<Session> sessions = adminSessionMap.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭调用的方法
     *
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("type") String type, @PathParam("sid") String sid) {
//        System.out.println("连接断开:" + sid);
//        sessionMap.remove(sid);
        System.out.println("连接断开:" + sid);
        if ("admin".equals(type)) {
            adminSessionMap.remove(sid);
        } else {
            sessionMap.remove(sid);
        }
    }

    /**
     * 群发
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
