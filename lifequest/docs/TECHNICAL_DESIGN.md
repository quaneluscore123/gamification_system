# Technical Design

## 1. Technology Stack
- **Frontend Framework**: React
- **Build Tool**: Vite
- **Styling**: Vanilla CSS (tập trung vào Animation, CSS Variables, Glassmorphism)
- **Data Persistence (MVP)**: LocalStorage API
- **Data Persistence (Tương lai)**: PostgreSQL + Spring Boot

## 2. Component Architecture
Hệ thống Component được chia thành các nhóm rõ ràng:
- `App.tsx` (Root Layout)
- **Core Systems**: `GameContext.tsx` (State Management chính)
- **Dashboard UI**: `CharacterCard`, `MentorAstra`, `Statistics`
- **Dungeon UI**: `FloorView`, `BossCard`, `RoomItem`
- **Feature UI**: `Inventory`, `DynamicShop`, `SkillTree`, `DailyEvent`, `TreasureChest`

## 3. State Management (GameContext)
Trạng thái (State) của toàn bộ trò chơi được giữ trong một Context duy nhất để dễ dàng Save/Load.
- `player`: State chứa số liệu cá nhân (XP, Level, Gold).
- `worlds`: Mảng dữ liệu chứa tiến trình của các World và Boss.
- `quests`: Mảng nhiệm vụ Daily/Weekly được sinh ra mỗi ngày.
- `inventory`: Mảng chứa số lượng item đang sở hữu.

## 4. Save/Load System
- Cơ chế **Auto-save**: Sử dụng `useEffect` trong `GameContext` để lắng nghe mọi thay đổi của State và đồng bộ xuống `localStorage` ngay lập tức. Đảm bảo người dùng không bao giờ mất dữ liệu dù tắt app đột ngột.
