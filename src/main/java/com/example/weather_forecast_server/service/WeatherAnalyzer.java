package com.example.weather_forecast_server.service;

import com.example.weather_forecast_server.model.WeatherData;
import com.example.weather_forecast_server.model.WeatherRawResponse;
import com.example.weather_forecast_server.model.WeatherDay;
import com.example.weather_forecast_server.model.WeatherHour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherAnalyzer {

    @Autowired
    WeatherFetcherService fetcherService;

    public String analyzeHourlyWeather(WeatherData weatherData) {
        String location = weatherData.getLocation();
        if (location == null || location.isEmpty()) {
            return "⚠️ Bạn chưa nhập địa điểm!";
        }

        WeatherRawResponse weatherRawResponse = fetcherService.fetchWeatherAsync(location).join();
        if (weatherRawResponse == null) {
            return "Không lấy được dữ liệu thời tiết.";
        }

        List<String> alerts = new ArrayList<>();
        weatherData.setAlerts(alerts);

        List<WeatherDay> days = weatherRawResponse.getDays();
        if (days == null || days.isEmpty()) {
            alerts.add("Không lấy được dữ liệu ngày.");
            weatherData.setHasAlert(true);
            return String.join("\n", alerts);
        }

        List<WeatherHour> hours = days.get(0).getHours();
        if (hours == null || hours.isEmpty()) {
            alerts.add("Không lấy được dữ liệu giờ.");
            weatherData.setHasAlert(true);
            return String.join("\n", alerts);
        }

        StringBuilder results = new StringBuilder("THÔNG TIN THỜI TIẾT tại " + location + ":\n");

        for (WeatherHour h : hours) {
            String description = String.format(
                    "⏰ %s - 🌡️ %.1f°C - 💧 %.1f%% - 💨 %.1f km/h - 🔆 UV: %d",
                    h.getDatetime(), h.getTemp(), h.getHumidity(), h.getWindspeed(), h.getUvindex()
            );

            if (h.getTemp() != null && h.getTemp() > 37.5) {
                alerts.add(h.getDatetime() + ": Nhiệt độ cao.");
            } else if (h.getTemp() != null && h.getTemp() < 26.5) {
                alerts.add(h.getDatetime() + ": Nhiệt độ thấp.");
            }

            if (h.getHumidity() != null && h.getHumidity() > 75) {
                alerts.add(h.getDatetime() + ": Độ ẩm cao.");
            }

            if (h.getWindspeed() != null && h.getWindspeed() > 40) {
                alerts.add(h.getDatetime() + ": Gió lớn.");
            }

            results.append(description).append("\n");
        }

        weatherData.setHasAlert(!alerts.isEmpty());

        if (weatherData.getHasAlert()) {
            results.append("\n⚠️ CẢNH BÁO:\n");
            results.append(String.join("\n", alerts));
        }

        return results.toString();
    }
}
