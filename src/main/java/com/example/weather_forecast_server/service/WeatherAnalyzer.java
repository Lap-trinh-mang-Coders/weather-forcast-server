package com.example.weather_forecast_server.service;

import com.example.weather_forecast_server.model.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;

public class WeatherAnalyzer {
    @Autowired
    service.WeatherFetcherService fetcherService;

    WeatherData data  = fetcherService.fetchWeatherData();

}
