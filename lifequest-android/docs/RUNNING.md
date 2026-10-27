# Chạy LifeQuest trên máy ảo và điện thoại thật

## 1. Mở project

1. Android Studio → **File → Open** → chọn thư mục `lifequest-android` (thư mục có `settings.gradle.kts`).
2. Chờ **Gradle Sync** xong (thanh tiến độ dưới cùng).

> ⚠️ Nếu Android Studio gợi ý nâng cấp Gradle lên 9.x, **đừng nhận**: AGP 8.x không chạy được với Gradle 9.6+
> (lỗi `InternalProblems ... removed in Gradle 9.6.0`). Wrapper của project cố định ở Gradle 8.13.
> Muốn lên Gradle 9 thì phải nâng AGP lên 9.x cùng lúc.

## 2. Máy ảo (Emulator)

### Tạo máy ảo
**Device Manager** (View → Tool Windows → Device Manager) → **+** → **Create Virtual Device** → chọn máy → system image API 35/36 x86_64 → Finish.

### Máy ảo quá to so với màn hình laptop
Chọn một trong các cách sau:

| Cách | Làm thế nào |
|---|---|
| **Kéo góc cửa sổ** | Cửa sổ emulator riêng: kéo góc dưới bên phải để thu nhỏ, màn hình máy ảo tự co theo. |
| **Nhúng vào Android Studio** | **File → Settings → Tools → Emulator** → tick **Launch in the Running Devices tool window**. Máy ảo hiện trong panel *Running Devices*, tự co vừa panel; dùng nút zoom trên thanh công cụ của panel để phóng to/thu nhỏ. |
| **Chọn máy nhỏ hơn** | Tạo máy ảo mới với profile **Small Phone** hoặc **Medium Phone** (độ phân giải thấp hơn Pixel 8/9). |
| **Đổi máy của AVD có sẵn** | Device Manager → ⋮ → **Edit** → đổi *Device* sang máy nhỏ hơn. |

### Máy ảo không khởi động / màn hình đen
- Device Manager → ⋮ → **Edit** → *Graphics acceleration*: **Software** → *Boot option*: **Cold boot**.
- Laptop 2 card đồ họa: **Settings → System → Display → Graphics** → thêm
  `%LOCALAPPDATA%\Android\Sdk\emulator\qemu\windows-x86_64\qemu-system-x86_64.exe` → **High performance**.
- Vẫn lỗi: Device Manager → ⋮ → **Wipe Data**.

## 3. Điện thoại thật

1. **Cài đặt → Giới thiệu điện thoại → Phiên bản** → chạm **Số phiên bản** 7 lần.
2. **Tùy chọn nhà phát triển** → bật **Gỡ lỗi USB** (máy Realme/OPPO: bật thêm **Cài đặt qua USB**, **Tắt giám sát quyền**).
3. Cắm cáp có truyền dữ liệu → chọn **Truyền tệp** → bấm **Cho phép** gỡ lỗi USB.
4. Kiểm tra: `adb devices` phải thấy dòng `<mã máy>  device`.

## 4. Chạy app

- Chọn thiết bị trên thanh công cụ → bấm ▶ **Run** (Shift+F10).
- Hoặc dòng lệnh: `.\gradlew.bat installDebug`
- App lỗi/crash: tab **Logcat**, lọc `package:mine`, tìm `FATAL EXCEPTION`.

## 5. Các lệnh Gradle hay dùng

```bash
./gradlew testDebugUnitTest   # unit test (domain + ViewModel)
./gradlew lintDebug           # kiểm tra code bằng Android Lint
./gradlew assembleDebug       # APK debug  → app/build/outputs/apk/debug/
./gradlew assembleRelease     # APK release (R8 + ký) → app/build/outputs/apk/release/
./gradlew bundleRelease       # AAB để upload Google Play → app/build/outputs/bundle/release/
```

Trên Windows dùng `.\gradlew.bat` thay cho `./gradlew`.

## 6. Thử các tính năng

| Tính năng | Cách thử |
|---|---|
| Quote of the day | Tab **Quests**. Tắt Wi-Fi rồi bấm *Làm mới*: hiện quote cũ kèm chữ `(offline)`. |
| Daily reset + streak | Hoàn thành quest hôm nay, đổi ngày hệ thống sang mai rồi mở lại app. |
| Thống kê (Room) | Tab **Thống kê**: tổng quest/XP/Gold, lịch sử gần đây. Xem bảng bằng **App Inspection → Database Inspector**. |
| Nhắc nhở (WorkManager) | ⚙️ → bật nhắc → chọn giờ. Xem lịch bằng **App Inspection → Background Task Inspector**. |
| Analytics | Logcat lọc `Analytics` (chưa có Firebase) hoặc Firebase DebugView (xem [FIREBASE_SETUP.md](FIREBASE_SETUP.md)). |
| Crashlytics | 🛠️ Dev → **Test Crash**, mở lại app, xem trên Firebase Console. |
