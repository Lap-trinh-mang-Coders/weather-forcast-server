package com.example.weather_forecast_server.service;

import org.springframework.stereotype.Service;

@Service
public class LocationGrouper {
    public String groupLocation(String originalLocation) {
        String[] parts = originalLocation.split(",");
        Double lat = Math.round(Double.parseDouble(parts[0]) * 100.0) / 100.0;
        Double lng = Math.round(Double.parseDouble(parts[1]) * 100.0) / 100.0;
        return lat + "," + lng;
    }
}
