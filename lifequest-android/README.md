# ⚔️ LifeQuest — Android (Kotlin + Jetpack Compose)

Ứng dụng RPG gamification biến hành trình học Java Backend thành một cuộc phiêu lưu: mỗi module là một **World**, mỗi World có một **Boss**, mỗi bài học là một **Quest**. Đây là bản Android native, chuyển từ bản web React + TypeScript trong thư mục [`../lifequest`](../lifequest).

## Tính năng

- **Dungeons**: 3 World (Java Fundamentals ➔ Database & SQL ➔ Spring Boot Core). Hoàn thành room sẽ gây sát thương lên Boss, hạ Boss thì mở khóa World tiếp theo.
- **Quests**: nhiệm vụ Daily / Weekly, có thưởng XP, Gold và sát thương lên Boss.
- **Player progression**: lên level (XP cần cho mỗi level tăng ×1.5), danh hiệu (Beginner ➔ Backend Engineer), ghi lại Legacy khi hạ Boss.
- **Shop & Hành Trang**: mua vật phẩm bằng Gold, dùng để nhận XP, Gold hoặc gây sát thương lên Boss.
- **Lưu tự động** bằng DataStore, không mất tiến trình khi tắt app.
- **Dev Console** để test nhanh (+XP, +Gold, Kill Boss, Reset).

## Công nghệ

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Kotlin 2.2 |
| UI | Jetpack Compose + Material 3 |
| Kiến trúc | MVVM + tầng domain thuần Kotlin (theo DDD của bản web) |
| State | `StateFlow` + `collectAsStateWithLifecycle` |
| Lưu trữ | Jetpack DataStore + kotlinx.serialization (JSON) |
| DI | Thủ công qua `AppContainer` |
| Test | JUnit 4 cho domain logic |
| Build | Gradle Kotlin DSL + version catalog, AGP 8.12, minSdk 26 / targetSdk 36 |

## Kiến trúc

```
app/src/main/java/com/lifequest/app/
├── domain/                 # Kotlin thuần, không phụ thuộc Android → unit test được
│   ├── game/               # GameState, GameEngine (luật chơi), GameBalance
│   ├── player/             # PlayerProgress, PlayerRules (XP, level, title)
│   ├── quest/              # QuestDef, QuestCatalog
│   ├── world/              # WorldDef, WorldCatalog
│   ├── shop/               # ItemDef, ShopCatalog
│   └── inventory/          # InventoryItem, InventoryRules
├── data/                   # GameRepository + bản cài đặt DataStore
├── ui/                     # GameViewModel, các màn hình Compose, theme
├── LifeQuestApp.kt         # Application + AppContainer (DI)
└── MainActivity.kt
```

Luồng dữ liệu một chiều:

```
UI (Compose) --action--> GameViewModel --GameEngine.xxx(state)--> GameState mới
     ^                         |                                       |
     └──── StateFlow ──────────┘<──────────────────────────────────────┘
                               └── repository.save(state) → DataStore
```

`GameEngine` gồm các hàm thuần `(GameState) -> GameOutcome(state mới, sự kiện)`. Bản web chia logic qua 5 Context Provider gọi chéo nhau; bản Android gom về một state bất biến nên tránh được lỗi state cũ (stale state) và test dễ hơn.

### Bản React tương ứng với gì trong Kotlin

| React (web) | Kotlin (Android) |
|---|---|
| `PlayerProvider`, `QuestProvider`, ... (Context) | `GameViewModel` + `StateFlow<GameUiState>` |
| `PlayerService`, `QuestService`, ... | `PlayerRules`, `GameEngine` |
| `questsData.ts`, `worldsData.ts`, `shopItems` | `QuestCatalog`, `WorldCatalog`, `ShopCatalog` |
| `LocalStorageService` | `DataStoreGameRepository` |
| `useState` / `useEffect` | `remember` / `LaunchedEffect` |
| `alert()` | `Snackbar` qua `GameEvent` |
| Component `.tsx` + `.css` | Hàm `@Composable` + `LifeQuestTheme` |

## Chạy dự án

Yêu cầu: JDK 17, Android SDK (platform 36). Cách dễ nhất là mở thư mục này bằng **Android Studio**.

```bash
# Chạy unit test
./gradlew testDebugUnitTest

# Build APK debug → app/build/outputs/apk/debug/app-debug.apk
./gradlew assembleDebug

# Cài lên điện thoại/emulator đang kết nối
./gradlew installDebug

# Build APK release → app/build/outputs/apk/release/app-release.apk
./gradlew assembleRelease
```

Trên Windows dùng `gradlew.bat` thay cho `./gradlew` nếu chạy trong CMD/PowerShell.

> APK release hiện được ký bằng debug key để cài thử trên máy thật. Muốn đưa lên Google Play cần tạo keystore riêng.

## Lộ trình tiếp theo

- [ ] Thêm Navigation Compose, mỗi tab là một route riêng
- [ ] Chuyển DI sang Hilt
- [ ] Chuyển lưu trữ sang Room (bảng quest, world, inventory)
- [ ] Gọi Spring Boot REST API bằng Retrofit + xử lý loading/error/offline
- [ ] Reset Daily quest theo ngày, tính streak
- [ ] Viết UI test cho Compose và test cho ViewModel
