package com.example.weather_forecast_server.model;

import lombok.Data;

import java.util.List;

@Data
public class WeatherRawResponse {
    private double latitude;
    private double longitude;
    private String timezone;
    private String address;
    private List<WeatherDay> days;

    @Data
    public static class WeatherDay {
        private String datetime;
        private double temp;
        private double tempmax;
        private double tempmin;
        private String icon;
        private double humidity;
        private double windspeed;
        private double pressure;
        private String sunrise;
        private String sunset;
        private int uvindex;
        private List<WeatherHour> hours;
    }

    @Data
    public static class WeatherHour {
        private String datetime;
        private double temp;
        private double tempmax;
        private double tempmin;
        private String icon;
        private double humidity;
        private double windspeed;
        private double pressure;
        private int uvindex;
    }
}
