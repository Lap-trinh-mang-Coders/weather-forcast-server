package com.example.weather_forecast_server.service;

import com.example.weather_forecast_server.model.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.weather_forecast_server.model.WeatherRawResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherAnalyzer {

    @Autowired WeatherFetcherService fetcherService;

    public String analyzeHourlyWeather(WeatherData weatherData){
        Optional<WeatherRawResponse> weatherOpt = fetcherService.fetchWeather();

        if (weatherOpt.isEmpty()){
            return"Khong lay duoc du lieu";
        }

        WeatherRawResponse weatherRawResponse = weatherOpt.get();

        List<String> alerts = new ArrayList<>();
        weatherData.setAlerts(alerts);

        //Du lieu ngay
        if (WeatherRawResponse.getDays() == null || WeatherRawResponse.getDays().isEmpty()){
            alerts.add("Khong lay duoc du lieu ngay.");
            weatherData.setHasAlert(true);
            return String.join("/n ", alerts);
        }


        //Du lieu gio
        List<WeatherRawResponse> hours = WeatherRawResponse.getDays().get(0).getHours();
        if (hours == null || hours.isEmpty()){
            alerts.add("Khong lay duoc du lieu gio.");
            weatherData.setHasAlert(true);
            return String.join("/n ", alerts);
        }

        StringBuilder results = new StringBuilder("Weather Info");

        for (WeatherRawResponse h : hours){
            String description = String.format("", h.getDatetime(), h.getTemp(), h.getHumid(), h.getPrecip(), h.getWind());

            //Phan tich canh bao
            if (h.getTemp > 37.5){
                alerts.add(h.getDatetime() + ": Nhiet do cao.");
            } else if (h.getTemp < 26.5){
                alerts.add(h.getDatetime() + ": Nhiet do thap.");
            }

            if (h.getHumid > 75){
                alerts.add(h.getHumid() + ": Do am cao.");
            }

            if (h.getPrecip > 0){
                alerts.add(h.getPrecip() + ": Troi sap mua.");
            }

            if (h.getWind > 40){
                alrerts.add(h.getWind() + ": Troi hom nay gio lon.");
            }
            results.append(description).append("\n");
        }

        weatherData.setHasAlert(!alerts.isEmpty());

        if  (weatherData.getHasAlert()){
            results.append("").append(String.join("\n", alerts));
        }
        return results.toString();
    }
}
