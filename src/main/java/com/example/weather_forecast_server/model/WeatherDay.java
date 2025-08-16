package com.example.weather_forecast_server.model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Data
public class WeatherDay {
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
