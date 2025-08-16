package com.example.weather_forecast_server.service;

import com.example.weather_forecast_server.model.WeatherData;
import com.google.gson.Gson;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public class WeatherBroadcaster {
    private static final Map<String, Set<Session>> clientsByLocation = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();
    // Đăng ký client theo locationKey
    public static void registerClient(String locationKey, Session session) {
        clientsByLocation
                .computeIfAbsent(locationKey, k -> ConcurrentHashMap.newKeySet())
                .add(session);
    }
    // Hủy đăng ký client
    public static void unregisterClient(Session session) {
        clientsByLocation.values().forEach(sessions -> sessions.remove(session));
    }
    // Gửi WeatherData cho tất cả client cùng locationKey
    public static void broadcastToLocation(String locationKey, WeatherData data) {
        Set<Session> sessions = clientsByLocation.get(locationKey);
        if (sessions == null || sessions.isEmpty()) return;
        String json = gson.toJson(data);

        // Sao chép để tránh ConcurrentModificationException
        for (Session session : Set.copyOf(sessions)) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
