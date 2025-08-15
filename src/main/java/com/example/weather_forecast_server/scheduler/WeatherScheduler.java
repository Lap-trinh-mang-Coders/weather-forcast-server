package com.example.weather_forecast_server.scheduler;

import com.example.weather_forecast_server.controller.WeatherWebSocketHandler;
import com.example.weather_forecast_server.model.ClientInfo;
import com.example.weather_forecast_server.model.WeatherRawResponse;
import com.example.weather_forecast_server.service.WeatherFetcherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.util.List;
import java.util.Map;

@Component
public class WeatherScheduler {

    private final WeatherFetcherService fetcherService;
    private final WeatherWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public WeatherScheduler(WeatherFetcherService fetcherService,
                            WeatherWebSocketHandler webSocketHandler,
                            ObjectMapper objectMapper) {
        this.fetcherService = fetcherService;
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000) // Mỗi 5 phút
    public void broadcastWeatherToClients() {
        Map<String, List<ClientInfo>> clientsMap = webSocketHandler.getClientsMap();

        for (Map.Entry<String, List<ClientInfo>> entry : clientsMap.entrySet()) {
            String groupedLocation = entry.getKey();
            List<ClientInfo> clients = entry.getValue();

            fetcherService.fetchWeatherAsync(groupedLocation).thenAccept(weatherData -> {
                if (weatherData != null) {
                    try {
                        String json = objectMapper.writeValueAsString(weatherData);
                        for (ClientInfo client : clients) {
                            client.getSession().sendMessage(new TextMessage(json));
                        }
                        System.out.println("[Scheduler] Sent weather to group: " + groupedLocation);
                    } catch (Exception e) {
                        System.err.println("[Scheduler] Failed to send to group: " + groupedLocation);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("[Scheduler] Failed to fetch weather for: " + groupedLocation);
                }
            });
        }
    }
}