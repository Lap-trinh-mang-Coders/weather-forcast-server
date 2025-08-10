package com.example.weather_forecast_server.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WeatherData {
    private String locationName;
    private String location;
    private String condition;
    private Double currentTemp;
    private Double maxTemp;
    private Double minTemp;
    private String dayOfWeek;
    private LocalDate date;
    private Double humidity;
    private Double pressure;
    private Double windSpeed;
    private String sunrise;
    private String sunset;
    private Integer uvIndex;
    private Boolean hasAlert = false;
    private List<String> alerts = null;
}
