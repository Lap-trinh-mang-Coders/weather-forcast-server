package com.example.weather_forecast_server.model;

import lombok.Data;

import java.util.List;

@Data
public class WeatherRawResponse {
    private Double latitude;
    private Double longitude;
    private String timezone;
    private String address;
    private List<WeatherDay> days;
}

@Data
class WeatherDay {
    private String datetime;
    private Double temp;
    private Double tempmax;
    private Double tempmin;
    private String icon;
    private Double humidity;
    private Double pressure;
    private Double windspeed;
    private String sunrise;
    private String sunset;
    private Integer uvindex;
    private List<WeatherHour> hours;
}

@Data
class WeatherHour {
    private String datetime;
    private Double temp;
    private Double tempmax;
    private Double tempmin;
    private String icon;
    private Double humidity;
    private Double pressure;
    private Double windspeed;
    private Integer uvindex;
}
