package com.example.weather_forecast_server.model;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class ClientInfo {
    private WebSocketSession session;
    private String originalLocation;
    private String groupedLocation;

    public ClientInfo(WebSocketSession session, String originalLocation, String groupedLocation) {
        this.session = session;
        this.originalLocation = originalLocation;
        this.groupedLocation = groupedLocation;
    }
}
