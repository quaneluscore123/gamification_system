# Cấu hình Firebase cho LifeQuest

Code Firebase **đã có sẵn** trong project (Analytics + Crashlytics). Bạn chỉ cần tạo project trên web
và tải về một file cấu hình. Chưa có file thì app vẫn chạy, sự kiện chỉ được ghi ra Logcat.

## Bước 1. Tạo project Firebase

1. Vào **https://console.firebase.google.com** → đăng nhập Google → **Create a project**.
2. Tên project: `LifeQuest` → Continue.
3. **Enable Google Analytics**: bật → chọn *Default Account for Firebase* → **Create project**.

## Bước 2. Thêm app Android

1. Trong trang project, bấm biểu tượng **Android** (Add app).
2. **Android package name**: `com.lifequest.app`
   (phải trùng `applicationId` trong `app/build.gradle.kts`, nếu sai Firebase sẽ không nhận app).
3. **App nickname**: `LifeQuest Android` (tùy ý).
4. **Debug signing certificate SHA-1**: có thể bỏ qua. Chỉ bắt buộc khi làm *Google Sign-In*. Lấy SHA-1 bằng:
   ```bash
   ./gradlew signingReport
   ```
   (copy dòng `SHA1` của variant `debug`).
5. **Register app**.

## Bước 3. Tải google-services.json

1. Bấm **Download google-services.json**.
2. Chép file vào **`lifequest-android/app/google-services.json`** (cùng cấp với `app/build.gradle.kts`).
3. Bỏ qua bước *Add Firebase SDK* trên web (project đã cấu hình sẵn) → **Continue to console**.

> File này đã nằm trong `.gitignore`. Nó không phải bí mật tuyệt đối (API key trong đó bị giới hạn theo package),
> nhưng repo public thì không nên đưa lên.

## Bước 4. Build lại và kiểm tra

1. Android Studio → **Sync Project with Gradle Files** → chạy app.
   `app/build.gradle.kts` thấy file nên tự bật plugin `google-services` và `firebase-crashlytics`.
2. Mở ⚙️ **Cài đặt** → dòng **📈 Analytics** phải là **Firebase**.

## Bước 5. Xem Analytics realtime (DebugView)

Analytics bình thường gom dữ liệu vài giờ mới hiện. Để xem ngay khi đang code:

```bash
adb shell setprop debug.firebase.analytics.app com.lifequest.app
```

Console → **Analytics → DebugView** → làm vài quest trong app → thấy các sự kiện:
`quest_complete`, `level_up`, `boss_defeated`, `item_bought`, `streak_extended`.

Tắt chế độ debug: `adb shell setprop debug.firebase.analytics.app .none.`

## Bước 6. Thử Crashlytics

1. Console → **Crashlytics** → **Enable Crashlytics**.
2. Trong app: 🛠️ **Dev** → **💥 Test Crash** → app sẽ tắt.
3. **Mở lại app** (báo cáo crash được gửi ở lần mở tiếp theo).
4. Chờ 1–5 phút → Console → Crashlytics → thấy `RuntimeException: Test crash from LifeQuest Dev Console`,
   kèm stack trace và mục **Logs** chứa các sự kiện đã log trước khi crash.

Bản release bật R8 nên stack trace bị đổi tên. Plugin Crashlytics tự upload file `mapping.txt`
khi build release để Console hiện lại tên class/hàm gốc.

## Làm tiếp (bài tập nâng cao)

| Tính năng | Bật trên Console | Thêm vào code |
|---|---|---|
| **Authentication** (đăng nhập) | Build → Authentication → Get started → bật *Anonymous* hoặc *Google* | `firebase-auth`, tạo `AuthRepository` trong domain |
| **Cloud Firestore** (lưu save lên mây) | Build → Firestore Database → Create database → *test mode* | `firebase-firestore`, bản `FirestoreGameRepository : GameRepository` |
| **Remote Config** (đổi số liệu game không cần update app) | Run → Remote Config → thêm `cheat_xp`, `xp_multiplier` | `firebase-config`, đọc giá trị thay cho `GameBalance` |
| **Cloud Messaging** (push notification) | Engage → Messaging → gửi thử | `firebase-messaging`, một `FirebaseMessagingService` |

Thêm thư viện vào `libs.versions.toml` **không cần ghi version** vì `firebase-bom` đã chọn sẵn:

```toml
firebase-auth = { group = "com.google.firebase", name = "firebase-auth" }
```

Xem gợi ý chi tiết trong [LEARNING_GUIDE.md](LEARNING_GUIDE.md), bài tập số 5.
