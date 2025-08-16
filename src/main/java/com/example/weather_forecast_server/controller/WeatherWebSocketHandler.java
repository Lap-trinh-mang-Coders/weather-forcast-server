package com.example.weather_forecast_server.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WeatherWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Tạm thời chỉ echo lại
        session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
    }
}
