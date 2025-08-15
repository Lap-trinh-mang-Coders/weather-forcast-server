package com.example.weather_forecast_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WeatherForecastServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherForecastServerApplication.class, args);
    }

}
