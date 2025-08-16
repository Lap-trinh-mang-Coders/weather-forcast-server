package com.example.weather_forecast_server.service;

import com.example.weather_forecast_server.model.WeatherRawResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
public class WeatherFetcherService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WeatherFetcherService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public CompletableFuture<WeatherRawResponse> fetchWeatherAsync(String location) {
        try {
            String apiKey = "7XJYKTF4MUP57J6GETNEPDD5E"; // TODO: để trong config
            String url = String.format(
                    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/%s?unitGroup=metric&key=%s&contentType=json",
                    location, apiKey
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(body -> {
                        try {
                            return objectMapper.readValue(body, WeatherRawResponse.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }
}