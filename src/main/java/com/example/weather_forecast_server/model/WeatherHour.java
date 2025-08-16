package com.example.weather_forecast_server.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class WeatherHour {
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
