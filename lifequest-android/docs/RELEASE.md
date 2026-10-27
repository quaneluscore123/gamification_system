# Phát hành LifeQuest lên Google Play

## 1. Tạo upload keystore (làm một lần duy nhất)

**Cách A: Android Studio**
**Build → Generate Signed App Bundle / APK → Android App Bundle → Create new...**
- Key store path: `lifequest-android/lifequest-upload.jks` (đã nằm trong `.gitignore`)
- Alias: `upload`, đặt mật khẩu, Validity 25 năm, điền tên.

**Cách B: dòng lệnh** (keytool đi kèm JDK):

```bash
keytool -genkeypair -v -keystore lifequest-upload.jks -alias upload -keyalg RSA -keysize 2048 -validity 9125
```

> 🔐 **Sao lưu file `.jks` và mật khẩu** (ví dụ Google Drive riêng tư + trình quản lý mật khẩu).
> Mất upload key thì phải xin Google reset; nếu không bật Play App Signing thì mất luôn quyền cập nhật app.

## 2. Khai báo keystore cho Gradle

```bash
cp keystore.properties.example keystore.properties
```

Điền mật khẩu thật vào `keystore.properties` (file này **không** được commit). Từ giờ `assembleRelease` /
`bundleRelease` tự ký bằng key thật; xóa file đi thì quay lại ký bằng debug key.

## 3. Trước mỗi lần phát hành

- [ ] Tăng `versionCode` (+1 mỗi lần upload) và `versionName` trong `app/build.gradle.kts`.
- [ ] `./gradlew testDebugUnitTest lintDebug`
- [ ] Cài **bản release** lên máy thật và bấm thử mọi màn hình. R8 có thể làm hỏng những gì bản debug không bị.
- [ ] Đã thêm `google-services.json` nếu muốn có Crashlytics trên bản release.

## 4. Build file AAB

```bash
./gradlew bundleRelease
```

File kết quả: `app/build/outputs/bundle/release/app-release.aab`.

## 5. Google Play Console

1. **https://play.google.com/console** → đăng ký tài khoản developer (phí một lần 25 USD, cần xác minh danh tính).
2. **Create app** → tên, ngôn ngữ, *App* / *Free*.
3. ⚠️ `applicationId` là **vĩnh viễn**. `com.lifequest.app` có thể đã có người dùng; nên đổi sang dạng
   `io.github.<username>.lifequest` **trước** lần upload đầu tiên (đổi cả package name trên Firebase).
4. Điền phần **App content**: Privacy policy (có thể đăng bằng GitHub Pages), Data safety (app có dùng
   Firebase Analytics/Crashlytics → khai báo *App activity* và *Crash logs*), Content rating, Target audience.
5. **Store listing**: icon 512×512, feature graphic 1024×500, ít nhất 2 ảnh chụp màn hình điện thoại.
6. **Testing → Internal testing** → Create release → upload `.aab` → bật **Play App Signing** (mặc định).
7. Tài khoản cá nhân mới phải chạy **Closed testing** với một số tester trong một khoảng thời gian
   (thường là 12 người trong 14 ngày, nhưng con số này có thể thay đổi, hãy xem yêu cầu hiện tại trong Console)
   trước khi được mở **Production**.

## 6. Tự động hóa (bài tập)

Thêm job vào `.github/workflows/android.yml` để build AAB đã ký khi tạo tag `v*`:
lưu file `.jks` dạng base64 trong **GitHub Secrets**, giải mã trong workflow, tạo `keystore.properties` từ secrets,
rồi chạy `./gradlew bundleRelease` và upload artifact.
