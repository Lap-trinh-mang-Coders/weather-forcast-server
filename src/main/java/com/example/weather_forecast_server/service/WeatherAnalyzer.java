package com.example.weather_forecast_server.service;

import com.example.weather_forecast_server.model.WeatherAlertResponse;
import com.example.weather_forecast_server.model.WeatherRawResponse;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherAnalyzer {

    public List<WeatherAlertResponse> analyze(WeatherRawResponse raw) {
        List<WeatherAlertResponse> alerts = new ArrayList<>();

        try {
            List<?> days = raw.getDays();
            if (days != null && !days.isEmpty()) {
                Object today = days.get(0); 

                // Lấy list giờ từ WeatherDay
                List<?> hours = (List<?>) getValue(today, "getHours");

                if (hours != null && !hours.isEmpty()) {
                    for (Object hour : hours) {
                        String datetime = (String) getValue(hour, "getDatetime");
                        Double temp = (Double) getValue(hour, "getTemp");
                        Double tempMax = (Double) getValue(hour, "getTempmax");
                        Double tempMin = (Double) getValue(hour, "getTempmin");
                        Double humidity = (Double) getValue(hour, "getHumidity");
                        Double pressure = (Double) getValue(hour, "getPressure");
                        Double windspeed = (Double) getValue(hour, "getWindspeed");
                        Integer uvIndex = (Integer) getValue(hour, "getUvindex");
                        String icon = (String) getValue(hour, "getIcon");

                        LocalDateTime dateTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                        long timestamp = System.currentTimeMillis();

                        // RULE cảnh báo theo giờ
                        if (windspeed != null && windspeed > 50) {
                            alerts.add(new WeatherAlertResponse(
                                    "Wind",
                                    "Gió mạnh",
                                    "Gió mạnh (>50km/h) lúc " + dateTime.getHour() + "h - hạn chế ra ngoài.",
                                    "Severe",
                                    timestamp
                            ));
                        }
                        if (uvIndex != null && uvIndex > 7) {
                            alerts.add(new WeatherAlertResponse(
                                    "UV",
                                    "Chỉ số UV cao",
                                    "Chỉ số UV cao (>7) lúc " + dateTime.getHour() + "h - cần chống nắng.",
                                    "High",
                                    timestamp
                            ));
                        }
                        if (tempMax != null && tempMax > 38) {
                            alerts.add(new WeatherAlertResponse(
                                    "Heat",
                                    "Nắng nóng",
                                    "Nhiệt độ cao (>38°C) lúc " + dateTime.getHour() + "h - nguy cơ sốc nhiệt.",
                                    "Extreme",
                                    timestamp
                            ));
                        }
                        if (tempMin != null && tempMin < 5) {
                            alerts.add(new WeatherAlertResponse(
                                    "Cold",
                                    "Trời lạnh",
                                    "Nhiệt độ thấp (<5°C) lúc " + dateTime.getHour() + "h - nguy cơ hạ thân nhiệt.",
                                    "Moderate",
                                    timestamp
                            ));
                        }
                        if (icon != null && icon.toLowerCase().contains("rain")) {
                            alerts.add(new WeatherAlertResponse(
                                    "Rain",
                                    "Có mưa",
                                    "Trời mưa lúc " + dateTime.getHour() + "h - nên mang theo áo mưa.",
                                    "Moderate",
                                    timestamp
                            ));
                        }
                    }
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
