package com.example.weather_forecast_server.model;

import lombok.Data;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class WeatherRawResponse {
    private Double latitude;
    private Double longitude;
    private String timezone;
    private String address;
    private List<WeatherDay> days;

}
