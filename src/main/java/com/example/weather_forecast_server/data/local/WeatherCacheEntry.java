package com.example.weather_forecast_server.data.local;

import com.example.weather_forecast_server.model.WeatherRawResponse;

import java.time.Instant;

public class WeatherCacheEntry {
    private WeatherRawResponse data;
    private Instant timestamp;

    public WeatherCacheEntry(WeatherRawResponse data, Instant timestamp) {
        this.data = data;
        this.timestamp = timestamp;
    }

    public WeatherRawResponse getData() {
        return data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}