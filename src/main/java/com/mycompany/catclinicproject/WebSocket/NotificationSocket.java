package com.mycompany.catclinicproject.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/notification/{vetId}")
public class NotificationSocket {

    private static ConcurrentHashMap<Integer, Set<Session>> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("vetId") int vetId) {
        sessions.computeIfAbsent(vetId, k -> new CopyOnWriteArraySet<>()).add(session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("vetId") int vetId) {
        Set<Session> userSessions = sessions.get(vetId);
        if (userSessions != null) {
            userSessions.remove(session);
            if (userSessions.isEmpty()) {
                sessions.remove(vetId);
            }
        }
    }

    public static void sendNotification(int vetId, int notiId, String message, String type) {
        Set<Session> userSessions = sessions.get(vetId);
        if (userSessions != null) {
            String json = String.format("{\"id\":%d,\"message\":\"%s\",\"type\":\"%s\",\"createdAt\":\"%s\"}",
                    notiId, message.replace("\"", "\\\""), type, java.time.LocalDateTime.now());
            for (Session s : userSessions) {
                if (s.isOpen()) {
                    try {
                        s.getBasicRemote().sendText(json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}