package com.example.weather_forecast_server.service;

import org.springframework.beans.factory.annotation.Autowired;

public class WeatherAnalyzer {
    @Autowired WeatherFetcherService fetcherService;

    WeatherData data  = fetcherService.fetchWeatherData();

}
