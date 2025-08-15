package com.example.weather_forecast_server.service;


import com.example.weather_forecast_server.data.local.WeatherCacheEntry;
import com.example.weather_forecast_server.model.WeatherRawResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherFetcherService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    private final Map<String, WeatherCacheEntry> cache = new ConcurrentHashMap<>();
    private final Duration cacheDuration = Duration.ofMinutes(10);


    public WeatherFetcherService(@Value("${visual.crossing.api.key}") String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.apiKey = apiKey;
    }

    // ✅ Hàm chính để lấy dữ liệu thời tiết
    public CompletableFuture<WeatherRawResponse> fetchWeatherAsync(String location) {
        WeatherCacheEntry cached = cache.get(location);

        // ✅ Kiểm tra cache có còn hiệu lực không
        if (cached != null && Instant.now().isBefore(cached.getTimestamp().plus(cacheDuration))) {
            System.out.println("[Cache] Dùng lại dữ liệu cho: " + location);
            return CompletableFuture.completedFuture(cached.getData());
        }

        // ✅ Nếu không có cache hoặc cache hết hạn → gọi API có retry
        return callApiWithRetry(location).thenApply(data -> {
            if (data != null) {
                cache.put(location, new WeatherCacheEntry(data, Instant.now()));
                System.out.println("[Cache] Đã lưu dữ liệu mới cho: " + location);
            }
            return data;
        });
    }

    // ✅ Hàm gọi API có retry
    private CompletableFuture<WeatherRawResponse> callApiWithRetry(String location) {
        int maxRetries = 3;
        int delayMillis = 1000;

        CompletableFuture<WeatherRawResponse> future = new CompletableFuture<>();
        attemptApiCall(location, maxRetries, delayMillis, future);
        return future;
    }

    // ✅ Hàm nội bộ để thử lại khi lỗi
    private void attemptApiCall(String location, int retriesLeft, int delayMillis, CompletableFuture<WeatherRawResponse> future) {
        String url = String.format(
                "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/%s?unitGroup=metric&key=%s&contentType=json",
                location, apiKey
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        return objectMapper.readValue(body, WeatherRawResponse.class);
                    } catch (Exception e) {
                        System.err.println("[Retry] Lỗi parse JSON: " + e.getMessage());
                        return null;
                    }
                })
                .whenComplete((result, error) -> {
                    if (result != null) {
                        future.complete(result);
                    } else if (retriesLeft > 0) {
                        System.out.println("[Retry] Thử lại lần nữa cho: " + location);
                        try {
                            Thread.sleep(delayMillis);
                        } catch (InterruptedException ignored) {}
                        attemptApiCall(location, retriesLeft - 1, delayMillis, future);
                    } else {
                        System.err.println("[Retry] Hết số lần thử cho: " + location);
                        future.complete(null);
                    }
                });
    }
}

