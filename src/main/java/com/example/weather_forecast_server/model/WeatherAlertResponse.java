package com.example.weather_forecast_server.model;

public class WeatherAlertResponse {
    private String type;      // Loại cảnh báo: mưa, bão, lũ lụt, ...
    private String title;     // Tiêu đề cảnh báo
    private String message;   // Nội dung chi tiết cảnh báo
    private String severity;  // Mức độ nghiêm trọng của cảnh báo
    private long timestamp;   // Thời gian tạo cảnh báo

    public WeatherAlertResponse(String type, String title, String message, String severity, long timestamp) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.severity = severity;
        this.timestamp = timestamp;
    }

    // Getters & setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
