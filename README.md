# 🌤️ Weather Alert WebSocket Server

## 🧠 Mô tả dự án
Truy cập phần trình bày báo cáo: https://www.youtube.com/watch?v=6GyEI6j1rEI

Dự án Java Spring Boot này là một WebSocket server **lấy dữ liệu thời tiết từ Visual Crossing API**, phân tích thời tiết xấu theo quy tắc riêng, và **gửi cảnh báo thời tiết tới các client Kotlin app** qua WebSocket.

Mục tiêu là xây dựng một layer trung gian tiết kiệm quota API và dễ mở rộng.

---

## 🔧 Công nghệ sử dụng

- Java **21**
- Spring Boot **3.x**
- Maven
- WebSocket
- Scheduled Tasks (`@Scheduled`)
- RestTemplate / WebClient

---

## 🗂️ Kiến trúc hệ thống

```text
[Visual Crossing API]
        ↓ (5 phút/lần)
 [WeatherFetcherService]
        ↓
 [WeatherAnalyzer] ← (Phân tích điều kiện thời tiết xấu)
        ↓
 [WeatherBroadcaster]
        ↓
 [WebSocketHandler] → nhiều client (Kotlin App)
# weather-forcast-server
