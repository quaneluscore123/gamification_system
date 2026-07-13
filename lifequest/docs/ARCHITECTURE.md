# Technical Architecture (Domain Driven Design)

## 1. Core Architecture Philosophy
Dự án áp dụng **Domain Driven Design (DDD)** ở mức đơn giản ngay trên Frontend. Các module (player, quest, reward, world) hoàn toàn độc lập.
Khi chuyển sang Spring Boot ở Phase 5, Domain Logic sẽ được giữ nguyên, chỉ thay thế Storage Layer và Transport Layer. 

Kiến trúc này đảm bảo ứng dụng có thể scale (mở rộng) dễ dàng và là một "điểm cộng tuyệt đối" khi trả lời phỏng vấn.

## 2. Directory Structure (Clean Architecture)
```text
src/
├── config/              # gameBalance.ts (Constants)
├── core/                # Các Base Services & Interfaces
│   └── StorageService.ts# Dependency Inversion (LocalStorage -> API sau này)
├── domain/              # Domain Entities (Immutable Definition)
│   ├── models/          # PlayerProfile, BossDef, QuestDef
│   └── progress/        # WorldProgress, QuestProgress (Mutable)
├── services/            # Domain Services (Chứa Business Logic)
│   ├── PlayerService.ts
│   ├── QuestService.ts
│   ├── RewardService.ts
│   └── WorldService.ts
├── facade/              # GameFacade (Điều phối UI và Services)
│   └── GameFacade.ts
├── contexts/            # React Context (Chia nhỏ theo Domain)
│   ├── PlayerContext.tsx
│   └── WorldContext.tsx
├── components/          # UI Components
└── App.tsx
```

## 3. Data Models (Immutable vs Progress)

### Nguyên tắc
Tách biệt rõ ràng giữa **Định nghĩa (Definition)** và **Tiến trình (Progress)**.
- `BossDef` không chứa máu hiện tại.
- `QuestDef` không chứa trạng thái hoàn thành.

### Ví dụ
```typescript
// Immutable Definition
interface BossDef {
  id: string;
  name: string;
  maxHp: number;
  difficulty: 'EASY' | 'NORMAL' | 'HARD';
  baseReward: Reward;
}

// Mutable Progress
interface WorldProgress {
  worldId: string;
  bossCurrentHp: number;
  completedRooms: string[];
}

// Quest Status
enum QuestStatus {
  LOCKED,
  AVAILABLE,
  IN_PROGRESS,
  COMPLETED,
  CLAIMED
}
```

## 4. Design Patterns Applied
- **Facade Pattern (`GameFacade`)**: UI không bao giờ gọi thẳng Service. UI gọi `GameFacade.completeQuest()`, Facade sẽ tự điều phối `QuestService` kiểm tra logic, gọi `RewardService` tính thưởng, và gọi `PlayerService` cộng điểm.
- **Dependency Inversion (`StorageService`)**: Các Service lưu dữ liệu thông qua interface `StorageService`. Hiện tại nó implement `LocalStorage`, tương lai sẽ đổi sang `RestApiStorage` mà không cần sửa Core Logic.
