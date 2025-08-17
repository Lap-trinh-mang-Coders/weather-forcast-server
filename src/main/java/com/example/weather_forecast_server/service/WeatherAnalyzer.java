package com.example.weather_forecast_server.service;

import com.example.weather_forecast_server.model.WeatherAlertResponse;
import com.example.weather_forecast_server.model.WeatherRawResponse;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherAnalyzer {

    public List<WeatherAlertResponse> analyze(WeatherRawResponse raw) {
        List<WeatherAlertResponse> alerts = new ArrayList<>();

        try {
            List<?> days = raw.getDays();
            if (days != null && !days.isEmpty()) {
                Object today = days.get(0); // WeatherDay object

                String datetime = (String) getValue(today, "getDatetime");
                Double temp = (Double) getValue(today, "getTemp");
                Double tempMax = (Double) getValue(today, "getTempmax");
                Double tempMin = (Double) getValue(today, "getTempmin");
                Double humidity = (Double) getValue(today, "getHumidity");
                Double pressure = (Double) getValue(today, "getPressure");
                Double windspeed = (Double) getValue(today, "getWindspeed");
                String sunrise = (String) getValue(today, "getSunrise");
                String sunset = (String) getValue(today, "getSunset");
                Integer uvIndex = (Integer) getValue(today, "getUvindex");

                LocalDate date = LocalDate.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                long now = System.currentTimeMillis();

                // RULE cảnh báo
                if (windspeed != null && windspeed > 50) {
                    alerts.add(new WeatherAlertResponse(
                            "Wind",
                            "Gió mạnh",
                            "Gió mạnh (>50km/h) - hạn chế ra ngoài.",
                            "Severe",
                            now
                    ));
                }
                if (uvIndex != null && uvIndex > 7) {
                    alerts.add(new WeatherAlertResponse(
                            "UV",
                            "Chỉ số UV cao",
                            "Chỉ số UV cao (>7) - cần chống nắng.",
                            "High",
                            now
                    ));
                }
                if (tempMax != null && tempMax > 38) {
                    alerts.add(new WeatherAlertResponse(
                            "Heat",
                            "Nắng nóng",
                            "Nhiệt độ cao (>38°C) - nguy cơ sốc nhiệt.",
                            "Extreme",
                            now
                    ));
                }
                if (tempMin != null && tempMin < 5) {
                    alerts.add(new WeatherAlertResponse(
                            "Cold",
                            "Trời lạnh",
                            "Nhiệt độ thấp (<5°C) - nguy cơ hạ thân nhiệt.",
                            "Moderate",
                            now
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alerts;
    }

    // Hàm gọi getter qua Reflection
    private Object getValue(Object obj, String methodName) {
        try {
            Method method = obj.getClass().getMethod(methodName);
            return method.invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }
}
