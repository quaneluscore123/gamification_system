# Technical Architecture (Feature-first + DDD)

## 1. Core Architecture Philosophy
Dự án áp dụng **Feature-first + Domain Driven Design (DDD)**. Khác với kiến trúc Clean Architecture truyền thống chia theo kỹ thuật (components, services, context), kiến trúc này nhóm code theo **Nghiệp vụ (Business Domain)**.

Mỗi Domain hoàn toàn độc lập, tự quản lý UI, Logic, và Data của riêng nó. Việc này giúp dự án có khả năng mở rộng vô hạn và là cấu trúc hoàn hảo nhất để chuyển dịch sang Microservices hoặc Spring Boot nhiều module.

## 2. Directory Structure
```text
src/
├── app/                    # Tầng khởi tạo ứng dụng
│   ├── App.tsx             # Root Layout
│   ├── main.tsx            # Entry point
│   ├── providers/          # Global Context Providers
│   └── router/             # Routing (Nếu có)
│
├── shared/                 # Tầng dùng chung
│   ├── components/         # UI Elements dùng chung (Button, Modal...)
│   ├── hooks/              # Custom Hooks chung
│   ├── utils/              # Helper functions
│   ├── config/             # Config chung (VD: gameBalance.ts)
│   └── assets/             # Hình ảnh, Fonts
│
└── domains/                # Tầng Nghiệp vụ cốt lõi (Feature Slices)
    │
    ├── player/             # Domain Player
    │   ├── domain/         # Entities, Types (PlayerProfile, PlayerProgress)
    │   ├── application/    # Use cases, Services (Level up logic)
    │   ├── infrastructure/ # Lưu trữ (LocalStorage)
    │   └── presentation/   # UI Component (CharacterCard, Stats)
    │
    ├── world/              # Domain World & Boss
    │   ├── domain/
    │   ├── application/
    │   ├── infrastructure/
    │   └── presentation/   # FloorView, BossCard
    │
    ├── quest/              # Domain Nhiệm vụ
    ├── inventory/          # Domain Túi đồ
    ├── shop/               # Domain Cửa hàng
    ├── event/              # Domain Sự kiện ngẫu nhiên
    └── meta/               # Domain hệ thống Dev Console, Achievements
```

## 3. Lớp trong mỗi Domain (4-Layer DDD)

1. **`domain/`**: Chứa các Entity Interfaces (vd: `QuestDef`, `QuestProgress`) và các Pure Functions cực kỳ cơ bản. Hoàn toàn KHÔNG phụ thuộc vào React hay LocalStorage.
2. **`application/`**: Chứa các Use Cases (Services) thực thi Logic (vd: `completeQuest`). Gọi đến `domain` và `infrastructure`.
3. **`infrastructure/`**: Thao tác trực tiếp với LocalStorage hoặc REST API (Tương lai). Đây là tầng duy nhất biết dữ liệu được lưu ở đâu.
4. **`presentation/`**: Chứa React Components (`.tsx`) và React Hooks. Đây là tầng duy nhất biết về React. Tầng này sẽ gọi xuống tầng `application/` để thực thi logic.
