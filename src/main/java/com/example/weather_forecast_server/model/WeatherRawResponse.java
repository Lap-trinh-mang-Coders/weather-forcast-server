package com.example.weather_forecast_server.model;

import lombok.Data;

import java.util.List;

@Data
public class WeatherRawResponse {
    private int queryCost;
    private double latitude;
    private double longitude;
    private String resolvedAddress;
    private String address;
    private String timezone;
    private double tzoffset;
    private String description;
    private List<DayWeather> days;
    private List<WeatherAlert> alerts;

    @Data
    public static class DayWeather {
        private String datetime;
        private Double tempmax;
        private Double tempmin;
        private Double temp;
        private Double humidity;
        private Double windspeed;
        private Double pressure;
        private String sunrise;
        private String sunset;
        private Integer uvindex;
        private String conditions;
        private String description;
    }

    @Data
    public static class WeatherAlert {
        private String event;
        private String headline;
        private String description;
        private String onset;
        private String ends;
        private String severity;
    }
}
