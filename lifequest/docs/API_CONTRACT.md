# API Contracts (Future Spring Boot Backend)

Tài liệu này định nghĩa trước các API endpoints mà Frontend sẽ gọi sau khi chuyển đổi sang kiến trúc Client-Server ở Phase 5.
Hiện tại, `StorageService` (chạy ngầm LocalStorage) đang mô phỏng lại chính xác các Response này.

## 1. Player API

### Lấy thông tin Player
**GET** `/api/v1/player/profile`

**Response:**
```json
{
  "profile": {
    "id": "USR-123",
    "name": "Astra's Disciple",
    "avatar": "default.png"
  },
  "progress": {
    "level": 5,
    "xp": 120,
    "maxXp": 500,
    "gold": 350,
    "legacy": []
  },
  "statistics": {
    "totalQuests": 42,
    "totalHours": 15
  }
}
```

## 2. Quest API

### Lấy danh sách nhiệm vụ hôm nay
**GET** `/api/v1/quests/daily`

**Response:**
```json
[
  {
    "id": "QST-001",
    "title": "Read Spring Security Docs",
    "status": "AVAILABLE",
    "difficulty": "MEDIUM",
    "estimatedTimeMin": 30,
    "tags": ["Spring", "Security"]
  }
]
```

### Hoàn thành Nhiệm vụ & Nhận Thưởng
**POST** `/api/v1/quests/{id}/complete`

**Response:**
```json
{
  "questStatus": "CLAIMED",
  "rewards": {
    "xpGained": 50,
    "goldGained": 20,
    "itemsDropped": []
  },
  "bossImpact": {
    "bossId": "BOSS-1",
    "damageDealt": 100,
    "isDefeated": false
  },
  "playerUpdate": {
    "levelUp": false,
    "newXp": 170
  }
}
```

## 3. World API

### Lấy thông tin World hiện tại
**GET** `/api/v1/worlds/current`

**Response:**
```json
{
  "worldId": "WLD-03",
  "name": "Spring Boot Core",
  "boss": {
    "id": "BOSS-3",
    "name": "CRUD API",
    "maxHp": 1000,
    "currentHp": 800
  },
  "rooms": [
    {
      "id": "RM-01",
      "status": "COMPLETED"
    },
    {
      "id": "RM-02",
      "status": "IN_PROGRESS"
    }
  ]
}
```
