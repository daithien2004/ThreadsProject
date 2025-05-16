📱 Threads Clone App
Ứng dụng clone mạng xã hội Threads, bao gồm các tính năng cơ bản như: đăng bài, theo dõi người dùng, thả tim, bình luận, xác thực OTP qua email, và thông báo thời gian thực.

🧰 Công nghệ sử dụng

📦 Backend (Spring Boot / IntelliJ)
Java Spring Boot

Spring Security

Spring Data JPA

MySQL (Railway)

WebSocket (Real-time notifications).


📱 Android (Frontend)

Java

Retrofit2

RecyclerView

Glide

ViewPager2

TabLayout

Email OTP


⚙️ Cách chạy ứng dụng
**1. 🚀 Backend (Spring Boot)**
✅ Yêu cầu

Java 22

Maven

MySQL (dùng dịch vụ Railway)

**2. 📱 Android App**
   
✅ Yêu cầu

Android Studio (Ladibug trở lên)

Android SDK 35

Thiết bị thật hoặc máy ảo

**3. Cấu hình kết nối database**

Mở file application.properties và cấu hình như sau:
spring.datasource.url=jdbc:mysql://ballast.proxy.rlwy.net:17408/railway
spring.datasource.username=root
spring.datasource.password=vMBhuEMyZUcRyMPGWxPetvcAOMQDkSEk

**Cách kết nối MySql:**

Hostname:ballast.proxy.rlwy.net

Port:17408

username=root

password=vMBhuEMyZUcRyMPGWxPetvcAOMQDkSEk

test connect nếu thành công là đã kết nối tới databse.


**! Nếu chạy trên thiết bị thật, đảm bảo cả máy tính và điện thoại cùng mạng Wi-Fi.**
Sửa lại đường dẫn địa chỉ IP tại:RetrofitClient và SocketManager

