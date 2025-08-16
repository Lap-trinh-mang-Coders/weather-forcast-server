package com.example.weather_forecast_server.service;

import com.example.weather_forecast_server.controller.WeatherWebSocketHandler;
import com.example.weather_forecast_server.model.ClientInfo;
import com.example.weather_forecast_server.model.WeatherRawResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Component
public class WeatherBroadcaster {
    private final WeatherWebSocketHandler handler;
    private final ObjectMapper objectMapper;

    public WeatherBroadcaster(final WeatherWebSocketHandler weatherWebSocketHandler, final ObjectMapper objectMapper) {
        this.handler = weatherWebSocketHandler;
        this.objectMapper = objectMapper;
    }

    public void broadcastToGroup(String groupedLocation, Object payload, String type) {
        List<ClientInfo> clients = handler.getClientsMap().getOrDefault(groupedLocation, List.of());
        ObjectNode node = objectMapper.createObjectNode();
        node.put("type", type);
        node.set("payload", objectMapper.valueToTree(payload));
        String json;
        try {
            json = objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            System.out.println("Processing Json Error:  " + e.getMessage());
            return;
        }
        TextMessage message = new TextMessage(json);

        for (ClientInfo client : clients) {
            try {
                WebSocketSession session = client.getSession();
                session.sendMessage(message);
            } catch (IOException e) {
                System.out.println("Send Message Error:  " + e.getMessage());
                return;
            }
        }
    }

    public void broadcastRawData(WeatherRawResponse rawResponse) {
        broadcastToGroup(rawResponse.getAddress(), rawResponse, "weather");
    }
}
