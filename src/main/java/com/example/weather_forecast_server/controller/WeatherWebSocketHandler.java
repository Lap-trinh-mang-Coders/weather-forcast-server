package com.example.weather_forecast_server.controller;

import com.example.weather_forecast_server.model.ClientInfo;
import com.example.weather_forecast_server.service.LocationGrouper;
import com.example.weather_forecast_server.service.WeatherFetcherService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WeatherWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, List<ClientInfo>> clientsMap = new ConcurrentHashMap<>();
    private final LocationGrouper locationGrouper;
    private final ObjectMapper objectMapper;
    private final WeatherFetcherService weatherFetcherService;

    public WeatherWebSocketHandler(LocationGrouper locationGrouper, ObjectMapper objectMapper, WeatherFetcherService weatherFetcherService) {
        this.locationGrouper = locationGrouper;
        this.objectMapper = objectMapper;
        this.weatherFetcherService = weatherFetcherService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ObjectNode node = objectMapper.createObjectNode();
        // ✅ Thay đổi "status" thành "type"
        node.put("type", "status");
        node.put("message", "Connected to "+ session.getRemoteAddress().toString());
        session.sendMessage(new TextMessage(node.toString()));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Sau khi ket noi va nhan duoc message tu client, tien hanh lay thong tin location cua client:
        JsonNode node = objectMapper.readTree(message.getPayload());
        String location = node.get("location").asText();

        // Xu li nhom cac location gan nhau:
        String groupedLocation = locationGrouper.groupLocation(location);

        // Tao doi tuong ClientInfo luu thong tin cua client trong 1 phien ket noi:
        ClientInfo client = new ClientInfo(session, location, groupedLocation);

        // Tao Map voi key = groupedLocation, de nhung client co vi tri gan nhau se co cung mot key => gui du lieu den nhieu client gan nhau cung luc
        clientsMap.computeIfAbsent(groupedLocation, key -> new ArrayList<>()).add(client);

        // Gui Data cho client khi co ket noi dau tien:
        weatherFetcherService.fetchWeatherAsync(groupedLocation).thenAccept(weatherData -> {
            try {
                if (weatherData != null) {
                    ObjectNode jsonNode = objectMapper.createObjectNode();
                    jsonNode.put("type", "weather");
                    jsonNode.set("payload", objectMapper.valueToTree(weatherData));

                    session.sendMessage(new TextMessage(jsonNode.toString()));
                    System.out.println("[WebSocket] Sent immediate weather data to client: " + session.getId());
                }
            }  catch (IOException e) {
                System.err.println("Error sending immediate weather data: " + e.getMessage());
            }
        });

        ObjectNode nodeResponse = objectMapper.createObjectNode();
        nodeResponse.put("type", "status");
        nodeResponse.put("message",  "Received grouped Location "+ groupedLocation);

        // ✅ Sửa lỗi sai ở đây: Thay thế .asText() bằng .toString()
        session.sendMessage(new TextMessage(nodeResponse.toString()));

        System.out.println("[WebSocket] Client " + session.getId() + " joined group: " + groupedLocation);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus  status) throws Exception {
        clientsMap.values().forEach(clients -> clients.removeIf(client -> client.getSession().getId().equals(session.getId())));
        System.out.println("[WebSocket] Disconnected: " + session.getId());
    }
    public Map<String, List<ClientInfo>> getClientsMap() {
        return clientsMap;
    }
}
