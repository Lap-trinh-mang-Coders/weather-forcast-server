package com.example.weather_forecast_server.service;

import com.example.weather_forecast_server.model.WeatherRawResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherFetcherService {

    @Value("${visualcrossing.api.key}")
    private String apiKey;

    private static final String API_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherFetcherService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public WeatherRawResponse fetchWeather(String location) {
        try {
            // 1. Gọi API Visual Crossing
            String url = UriComponentsBuilder.fromHttpUrl(API_URL + location)
                    .queryParam("unitGroup", "metric")
                    .queryParam("key", apiKey)
                    .queryParam("contentType", "json")
                    .toUriString();

            String jsonResponse = restTemplate.getForObject(url, String.class);

            // 2. Parse JSON → WeatherRawResponse
            return objectMapper.readValue(jsonResponse, WeatherRawResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy dữ liệu thời tiết từ Visual Crossing", e);
        }
    }
}
