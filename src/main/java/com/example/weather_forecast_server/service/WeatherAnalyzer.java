package com.example.weather_forecast_server.service;

import org.springframework.beans.factory.annotation.Autowired;

public class WeatherAnalyzer {

    WeatherData data  = fetcherService.fetchWeatherData();

}

// Duc Analyzer nhan tham so WeatherRawResponse => phan tich => them canh bao thoi tiet vao thuoc tinh WeatherData.alerts => return WeatherData da co alerts

