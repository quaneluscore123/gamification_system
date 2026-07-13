# Game Design

## 1. Core Gameplay Loop
```text
[ Open App ]
      ↓
[ Astra Greeting ] (Câu chào thay đổi theo Level, Story và Streak)
      ↓
[ Today's Event ] (Ví dụ: Lucky Day, Coffee Boost...)
      ↓
[ Quests & World Rooms ] (Danh sách nhiệm vụ xuất hiện)
      ↓
[ Hoàn thành Quest / Room ]
      ↓
[ Nhận XP + Gold ] ➔ [ Boss mất máu ]
      ↓
[ Mở Treasure Chest ] (Nếu thỏa điều kiện số lượng quest)
      ↓
[ Level Up ] ➔ [ Đổi Title & Astra mở khóa Story mới ]
      ↓
[ Auto Save ] (Lưu ngầm LocalStorage)
      ↓
[ Fatigue Check ] (Astra khuyên nghỉ ngơi nếu vượt mốc)
      ↓
[ End ]
```

## 2. Player System
- **Chỉ số**: Level, XP, Gold, Legacy, Title, Avatar, Current World, Current Floor, Current Streak. (Lưu ý: Không dùng chỉ số Energy).
- **Sự phát triển (Progression)**: Khi lên cấp, không mở khóa UI, mà mở khóa **Story** và **Title**.
  - Lv 1: Beginner
  - Lv 20: Junior Backend -> Astra sẽ nói: *"Congratulations. You're no longer just learning Java. You're becoming someone who can build software."*

## 3. Quest System
- **Daily Quest**: Học 20p (+30 XP), Fix 1 bug (+40 XP).
- **Weekly Quest**: Build CRUD API (-500 HP Boss).
- **Main Quest**: Deploy Project (World Clear).
- **Optional Quest**: Xem Tech Conference (Bonus Gold).

## 4. World & Boss System
Hành trình qua các chương sự nghiệp:
- **World 1**: Java Fundamentals ➔ *Boss*: OOP Master
- **World 2**: Database & SQL ➔ *Boss*: Database Design
- **World 3**: Spring Boot Core ➔ *Boss*: CRUD API
- **World 6**: The Final Test ➔ *Boss*: Backend Interview

## 5. Reward System
- **Inventory & Shop**: Có sẵn từ lv 1. Chứa các vật phẩm (XP Potion, Bug Fix Scroll...).
- **Treasure Chest**: Random phần thưởng (Gold, Potion, Rare Badge...).
- **Legacy**: Ghi nhận những cột mốc thực sự thay đổi sự nghiệp.

## 6. Daily Event System
- **Lucky Day**: Gold x2.
- **Coffee Boost**: XP x1.5.
- **Debug Night**: Xuất hiện nhiều Bug Quest hơn.
