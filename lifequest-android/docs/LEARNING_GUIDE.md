# Học Android qua LifeQuest

Project được nâng cấp **từng bước, mỗi bước một commit**. Cách học hiệu quả nhất là đọc lại từng commit theo thứ tự:
mỗi commit chỉ thêm một khái niệm, nên diff ngắn và dễ hiểu.

```bash
git log --oneline --reverse 35a0313..HEAD   # danh sách commit
git show 9428a59                            # xem toàn bộ thay đổi của một commit
```

Trong Android Studio: tab **Git** (Alt+9) → **Log** → bấm vào từng commit để xem diff.

---

## Phần 1. Đọc code theo commit

| # | Commit | Học được gì | File chính |
|---|---|---|---|
| 1 | `9428a59` refactor(domain) | **Dependency Rule**: interface ở domain, cài đặt ở data | `domain/game/GameRepository.kt` |
| 2 | `5d3883b` build: KSP + Hilt | Version catalog, plugin, KSP (sinh code lúc biên dịch) | `gradle/libs.versions.toml` |
| 3 | `e8125f7` refactor(di): Hilt | `@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `@Inject`, `@Binds` | `di/DataModule.kt`, `LifeQuestApp.kt` |
| 4 | `adf3162` feat(domain): streak | Hàm thuần nhận `LocalDate` thay vì gọi `now()`; inject `Clock`; viết test cho luật chơi | `GameEngine.startDay`, `DailyResetTest.kt` |
| 5 | `ad297bb` feat(ui): resume | `LifecycleEventEffect(ON_RESUME)` | `LifeQuestRoot.kt` |
| 6 | `d8caf05` feat(data): Room | `@Entity`, `@Dao`, `@Database`, `Flow` từ DAO, mapper Entity ↔ domain, export schema | `data/local/*` |
| 7 | `6e54b96` feat(nav) | Navigation Compose type-safe, `popUpTo/saveState/restoreState`, scope của ViewModel | `ui/navigation/Routes.kt` |
| 8 | `83299da` feat(stats) | `combine` + `stateIn(WhileSubscribed)`, tách Composable stateful/stateless, `@Preview` | `ui/stats/*` |
| 9 | `b43daae` build: Retrofit | `buildConfigField`, quyền `INTERNET` | `app/build.gradle.kts` |
| 10 | `c8d6ed1` feat(network) | Retrofit + kotlinx.serialization, DTO, cache-first, **không nuốt CancellationException** | `data/CachedQuoteRepository.kt` |
| 11 | `ddaffe8` feat(ui): quote | `sealed interface UiState` Loading/Success/Error, hủy request cũ bằng `Job` | `ui/quote/*` |
| 12 | `43a2ba9` feat(settings) | Preferences DataStore, route không nằm ở bottom bar, nút Back | `ui/settings/*` |
| 13 | `36cee41` feat(work) | WorkManager, NotificationChannel, `PendingIntent`, xin quyền runtime | `work/*` |
| 14 | `639e328` feat(firebase) | Áp dụng plugin có điều kiện, Firebase BOM, Analytics/Crashlytics qua interface | `di/AnalyticsModule.kt` |
| 15 | `851ee29` build(release) | Signing config, R8 + shrink resources, giữ bí mật ngoài git | `app/build.gradle.kts` |
| 16 | `46ca7b6` test | Fake vs Mock, `MainDispatcherRule`, `runTest`, Turbine | `src/test/.../ui/*` |
| 17 | `49f137e` ci | GitHub Actions | `.github/workflows/android.yml` |

### Luồng dữ liệu sau khi nâng cấp

```
              ┌──────────── UI (Compose) ─────────────┐
              │ LifeQuestRoot → NavHost → các Screen  │
              └───────┬───────────────────▲───────────┘
            action    │                   │ StateFlow<UiState>
              ┌───────▼───────────────────┴───────────┐
              │ ViewModel (@HiltViewModel)            │
              │ Game / Stats / Quote / Settings       │
              └───────┬───────────────────────────────┘
                      │ gọi interface (port) ở domain
   ┌──────────────────▼─────────────────────────────────────────────┐
   │ domain/  GameEngine (hàm thuần) · GameRepository · HistoryRepo  │
   │          QuoteRepository · SettingsRepository · ReminderScheduler│
   │          AnalyticsTracker            ← KHÔNG import Android     │
   └──────────────────▲─────────────────────────────────────────────┘
                      │ Hilt @Binds: interface → cài đặt
   ┌──────────────────┴─────────────────────────────────────────────┐
   │ data/  DataStore · Room · Retrofit │ work/ WorkManager │ Firebase│
   └────────────────────────────────────────────────────────────────┘
```

---

## Phần 2. Câu hỏi tự kiểm tra (trả lời được thì đã hiểu commit đó)

1. Vì sao `GameRepository` phải nằm ở `domain/`, không phải `data/`? Domain mà import `DataStore` thì hỏng ở đâu?
2. `@Binds` khác `@Provides` thế nào? Vì sao `Clock` và `Retrofit` dùng `@Provides`?
3. Vì sao `GameEngine.completeQuest` nhận `today: LocalDate` thay vì tự gọi `LocalDate.now()`?
4. `GameViewModel` được lấy ở đâu để mọi tab dùng chung? `StatsViewModel` sống bao lâu?
5. `stateIn(..., WhileSubscribed(5_000), ...)`: con số 5000 giải quyết vấn đề gì khi xoay màn hình?
6. Trong `CachedQuoteRepository`, vì sao không viết `catch (e: Exception)`?
7. Bấm "Làm mới" quote 5 lần liên tục thì có 5 request chạy song song không? Dòng code nào xử lý?
8. Vì sao `ReminderWorker` vẫn chạy khi app đã bị tắt hẳn? `ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE` để làm gì?
9. Không có `google-services.json` thì app vẫn build được nhờ đâu? Nếu gọi `FirebaseAnalytics` khi chưa cấu hình thì sao?
10. Vì sao test ViewModel cần `MainDispatcherRule`?

---

## Phần 3. Bẫy đã gặp khi làm project này (rất hay hỏi phỏng vấn)

| Bẫy | Triệu chứng | Cách xử lý |
|---|---|---|
| Nâng Gradle lên 9.7 khi AGP vẫn 8.12 | Sync lỗi `InternalProblems ... removed in Gradle 9.6.0` | Giữ Gradle 8.13 hoặc nâng AGP lên 9.x cùng lúc |
| Tạo ViewModel làm property của class test | `stateIn` không cập nhật, test fail khó hiểu | Tạo ViewModel **trong** test (hoặc `by lazy`), sau khi `MainDispatcherRule` đã chạy |
| Collect `StateFlow` trong `runTest` | Giá trị chưa tới khi assert | `backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { flow.collect {} }` |
| `catch (e: Exception)` trong coroutine | Coroutine không hủy được khi rời màn hình | Bắt exception cụ thể (`IOException`, `HttpException`) hoặc ném lại `CancellationException` |
| R8 ở bản release | Debug chạy, release crash khi parse JSON | Keep rule cho class `@Serializable` (xem `proguard-rules.pro`), luôn test bản release |
| `PendingIntent` thiếu flag | Crash trên Android 12+ | Luôn có `FLAG_IMMUTABLE` (hoặc `FLAG_MUTABLE` nếu thật sự cần) |

---

## Phần 4. Bài tập tự làm

Mỗi bài: tạo branch riêng (`git switch -c bai-1-quest-detail`), code, chạy test, commit theo chuẩn `feat:`/`fix:`/`test:`.

### Bài 1 — Màn hình chi tiết quest (Navigation có tham số) ⭐
**Yêu cầu:** Bấm vào tên một quest ở tab Quests → mở màn hình chi tiết (tiêu đề, độ khó, thời gian, tags, phần thưởng)
có nút Back và nút "Hoàn thành".

**Gợi ý:**
- `@Serializable data class QuestDetailRoute(val questId: String)` trong `Routes.kt`.
- `composable<QuestDetailRoute> { entry -> val route = entry.toRoute<QuestDetailRoute>() ... }`
- Chỉ truyền `questId`, màn hình đích tự tra `QuestCatalog.find(questId)`.
- Ẩn bottom bar giống `SettingsRoute`.

**Hoàn thành khi:** xoay màn hình không mất màn chi tiết; nút Back quay lại đúng tab Quests.

### Bài 2 — Room migration v1 → v2 ⭐
**Yêu cầu:** Lưu thêm `difficulty` (EASY/MEDIUM/HARD) vào lịch sử và hiển thị ở màn Thống kê.

**Gợi ý:**
- Thêm cột vào `QuestLogEntity` với giá trị mặc định: `@ColumnInfo(defaultValue = "EASY") val difficulty: String`.
- `@Database(version = 2, autoMigrations = [AutoMigration(from = 1, to = 2)])`.
- Build xong sẽ sinh `app/schemas/.../2.json`, hãy commit file này.

**Hoàn thành khi:** cài bản cũ, làm vài quest, cài đè bản mới mà **không mất** lịch sử cũ.

### Bài 3 — Chỉ nhắc khi chưa làm quest hôm nay (HiltWorker) ⭐⭐
**Yêu cầu:** `ReminderWorker` không gửi thông báo nếu hôm nay đã hoàn thành quest.

**Gợi ý:**
- Thêm `androidx.hilt:hilt-work` + `androidx.hilt:hilt-compiler` (ksp).
- `@HiltWorker class ReminderWorker @AssistedInject constructor(@Assisted context, @Assisted params, private val repository: GameRepository, private val clock: Clock)`.
- `LifeQuestApp` implement `Configuration.Provider`, inject `HiltWorkerFactory`; tắt initializer mặc định của WorkManager trong Manifest.
- So sánh `state.lastQuestEpochDay` với hôm nay.

**Hoàn thành khi:** test bằng *Background Task Inspector* → *Run now*: làm quest rồi thì không có thông báo.

### Bài 4 — Test cho CachedQuoteRepository ⭐⭐
**Yêu cầu:** Viết unit test cho 3 nhánh: cache hôm nay, gọi API thành công, mất mạng có cache.

**Gợi ý:** Repository đang phụ thuộc trực tiếp `Context` (DataStore) nên khó test. Hãy tách
`interface QuoteCache { suspend fun read(): CachedQuote?; suspend fun write(...) }` với bản DataStore,
rồi trong test dùng `FakeQuoteApi` (ném `IOException` khi cần) + `FakeQuoteCache`.

**Hoàn thành khi:** 3 test xanh, không dùng Robolectric hay thiết bị.

### Bài 5 — Lưu game lên mây (Firebase Auth + Firestore) ⭐⭐⭐
**Yêu cầu:** Đăng nhập ẩn danh, lưu `GameState` lên Firestore, cài app trên máy khác vẫn lấy lại được.

**Gợi ý:**
- Làm [FIREBASE_SETUP.md](FIREBASE_SETUP.md) trước, bật *Anonymous* + *Firestore*.
- `FirestoreGameRepository : GameRepository` lưu document `users/{uid}`.
- Đổi `@Binds` trong `DataModule` là xong, ViewModel không phải sửa: đây chính là lợi ích của Repository + DI.
- Viết **Security Rules** để mỗi user chỉ đọc/ghi được document của mình.

### Bài 6 — Compose UI test ⭐⭐
**Yêu cầu:** Test `StatsContent` hiện đúng số quest và dòng "Chưa có quest nào" khi rỗng.

**Gợi ý:** thêm `androidTestImplementation(platform(libs.androidx.compose.bom))`,
`androidx.compose.ui:ui-test-junit4`, `debugImplementation("androidx.compose.ui:ui-test-manifest")`.
Dùng `createComposeRule()`, `setContent { StatsContent(...) }`, `onNodeWithText("12").assertIsDisplayed()`.
Chạy trên máy thật: `./gradlew connectedDebugAndroidTest`.

### Bài 7 — Nâng cấp thư viện an toàn ⭐
**Yêu cầu:** Lint đang báo nhiều thư viện có bản mới. Nâng từng nhóm (Compose BOM, Lifecycle, Room...), mỗi nhóm một commit.

**Hoàn thành khi:** sau mỗi commit `./gradlew testDebugUnitTest assembleRelease` vẫn xanh và CI xanh.
Ghi lại thư viện nào bắt buộc nâng cùng nhau (ví dụ Kotlin ↔ KSP ↔ Compose compiler).

---

## Phần 5. Khi phỏng vấn được hỏi "Mô tả kiến trúc dự án"

> LifeQuest dùng MVVM với luồng dữ liệu một chiều. Logic game nằm trong `GameEngine`, là các hàm thuần
> `(GameState, input) -> (GameState mới, events)`, nên test bằng JUnit thường. Tầng domain chỉ khai báo interface
> (repository, scheduler, analytics). Tầng data cài đặt chúng bằng DataStore, Room, Retrofit, WorkManager
> và Firebase, rồi Hilt nối hai tầng lại. UI là Jetpack Compose với Navigation type-safe. Mỗi màn hình đọc
> `StateFlow<UiState>` từ ViewModel. Repository gọi API theo kiểu cache-first để app vẫn dùng được khi offline.
> Có unit test cho domain và ViewModel (dùng fake và Turbine), và CI chạy test, lint, build trên GitHub Actions.
