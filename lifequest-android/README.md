# ⚔️ LifeQuest — Android (Kotlin + Jetpack Compose)

![Android CI](https://github.com/quaneluscore123/gamification_system/actions/workflows/android.yml/badge.svg)

Ứng dụng RPG gamification biến hành trình học Java Backend thành một cuộc phiêu lưu: mỗi module là một **World**,
mỗi World có một **Boss**, mỗi bài học là một **Quest**. Đây là bản Android native, chuyển từ bản web React + TypeScript
trong thư mục [`../lifequest`](../lifequest).

## Tính năng

- **Dungeons**: 4 World (Java → SQL → Spring Boot → DevOps). Hoàn thành room gây sát thương lên Boss; hạ Boss mở khóa World mới.
- **Quests Daily / Weekly**: tự làm mới mỗi ngày / mỗi thứ Hai, có **streak** 🔥 ngày liên tiếp.
- **Quote of the day**: lấy từ REST API, cache để dùng offline.
- **Thống kê**: tổng quest/XP/Gold, Legacy Monument, lịch sử hoàn thành (Room).
- **Shop & Hành Trang**: mua và dùng vật phẩm bằng Gold.
- **Nhắc nhở hằng ngày** bằng WorkManager + notification, chọn giờ trong Cài đặt.
- **Firebase Analytics + Crashlytics** (tùy chọn, bật khi có `google-services.json`).
- **Dev Console**: +XP, +Gold, Kill Boss, Reset, Test Crash.

## Công nghệ

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Kotlin 2.2, Coroutines, Flow |
| UI | Jetpack Compose, Material 3, Navigation Compose (type-safe routes) |
| Kiến trúc | MVVM + UDF, tầng domain thuần Kotlin, Repository pattern |
| DI | Hilt (KSP) |
| Lưu trữ | Room (lịch sử), Preferences DataStore (save game, cài đặt, cache) |
| Mạng | Retrofit 3, OkHttp, kotlinx.serialization |
| Việc nền | WorkManager |
| Giám sát | Firebase Analytics, Crashlytics |
| Test | JUnit 4, kotlinx-coroutines-test, Turbine, fake repositories |
| Build | Gradle 8.13, AGP 8.12, version catalog, R8, minSdk 26 / targetSdk 36 |
| CI | GitHub Actions: test, lint, build APK |

## Kiến trúc

```
app/src/main/java/com/lifequest/app/
├── domain/                 # Kotlin thuần, không import Android → unit test được
│   ├── game/               # GameState, GameEngine (luật chơi, daily reset, streak), GameRepository
│   ├── player/ quest/ world/ shop/ inventory/
│   ├── history/            # QuestLog + HistoryRepository
│   ├── quote/              # Quote + QuoteRepository
│   ├── settings/           # UserSettings, SettingsRepository, ReminderScheduler
│   └── analytics/          # AnalyticsTracker
├── data/                   # Cài đặt các interface ở domain
│   ├── local/              # Room: Entity, Dao, Database
│   ├── remote/             # Retrofit: QuoteApi, DTO
│   ├── analytics/          # Firebase / Logcat tracker
│   └── *Repository.kt      # DataStore, Room, cache-first quote
├── work/                   # WorkManager: ReminderWorker, scheduler, notification
├── di/                     # Hilt modules
├── ui/                     # Compose screens + ViewModels, navigation, theme
├── LifeQuestApp.kt         # @HiltAndroidApp
└── MainActivity.kt         # @AndroidEntryPoint
```

```
UI (Compose) ──action──▶ ViewModel ──▶ GameEngine (hàm thuần) ──▶ state mới
     ▲                      │                                        │
     └──── StateFlow ◀──────┴──── Repository (interface ở domain) ◀──┘
                                       │ Hilt @Binds
                         DataStore · Room · Retrofit · WorkManager · Firebase
```

## Bắt đầu

Yêu cầu: Android Studio (JDK 17), Android SDK 36.

```bash
./gradlew testDebugUnitTest   # 36 unit test
./gradlew assembleDebug       # build APK debug
./gradlew installDebug        # cài lên máy đang kết nối
```

Trên Windows dùng `.\gradlew.bat`.

## Tài liệu

| Tài liệu | Nội dung |
|---|---|
| [docs/RUNNING.md](docs/RUNNING.md) | Chạy trên máy ảo / điện thoại, chỉnh kích thước emulator, các lệnh Gradle |
| [docs/FIREBASE_SETUP.md](docs/FIREBASE_SETUP.md) | Tạo project Firebase, tải `google-services.json`, DebugView, Crashlytics |
| [docs/RELEASE.md](docs/RELEASE.md) | Tạo keystore, build AAB, đưa lên Google Play |
| [docs/LEARNING_GUIDE.md](docs/LEARNING_GUIDE.md) | Đọc code theo từng commit, câu hỏi tự kiểm tra, bài tập |

## Bản React tương ứng với gì trong Kotlin

| React (web) | Kotlin (Android) |
|---|---|
| `PlayerProvider`, `QuestProvider`, ... (Context) | `GameViewModel` + `StateFlow<GameUiState>` |
| `PlayerService`, `QuestService`, ... | `PlayerRules`, `GameEngine` |
| `questsData.ts`, `worldsData.ts`, `shopItems` | `QuestCatalog`, `WorldCatalog`, `ShopCatalog` |
| `LocalStorageService` | `DataStoreGameRepository` |
| `useState` / `useEffect` | `remember` / `LaunchedEffect` |
| `alert()` | `Snackbar` qua `GameEvent` |
| React Router | Navigation Compose |
| Component `.tsx` + `.css` | Hàm `@Composable` + `LifeQuestTheme` |

## Lộ trình

- [x] Navigation Compose, mỗi tab là một route
- [x] DI bằng Hilt
- [x] Room cho lịch sử quest
- [x] Gọi REST API bằng Retrofit + xử lý loading/error/offline
- [x] Reset Daily quest theo ngày, tính streak
- [x] Test ViewModel, CI
- [ ] Màn chi tiết quest (route có tham số), Room migration, HiltWorker: xem bài tập trong [LEARNING_GUIDE](docs/LEARNING_GUIDE.md)
- [ ] Lưu game lên Firestore, đăng nhập Firebase Auth
- [ ] Gọi Spring Boot API của chính mình
