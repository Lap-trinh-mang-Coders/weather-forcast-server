package com.example.weather_forecast_server.controller;

import com.example.weather_forecast_server.model.WeatherData;
import com.example.weather_forecast_server.service.WeatherAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherAnalyzer weatherAnalyzer;

    @Autowired
    public WeatherController(WeatherAnalyzer weatherAnalyzer) {
        this.weatherAnalyzer = weatherAnalyzer;
    }

    @PostMapping
    public String getHourlyWeather(@RequestBody WeatherData weatherData) {
        return weatherAnalyzer.analyzeHourlyWeather(weatherData);
    }
}
