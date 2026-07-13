# Development Roadmap

## Phase 1: Gameplay Foundation (MVP Core)
Mục tiêu: Đảm bảo vòng lặp cốt lõi chạy mượt mà, lưu được dữ liệu và có hệ thống phân rã code tốt.
- Thiết lập React + Vite + Design Tokens.
- Khởi tạo kiến trúc `src/game/` để tách biệt Logic và Context.
- Thiết lập file `config/gameBalance.ts`.
- **Player Progression System**: Quản lý XP, Level, Gold, Legacy, Title.
- **Quest System**: Khởi tạo Daily, Weekly, Main (có Tag, Difficulty, Estimated Time).
- **Reward System**: Logic tính toán XP, Gold, sát thương Boss.
- Xây dựng UI: `FloorView`, `BossCard`, `QuestList`.
- Tích hợp Dev Console để test game.

## Phase 2: Gameplay Expansion (Reward/Meta)
Mục tiêu: Khiến trò chơi thú vị hơn mỗi ngày bằng hệ thống phần thưởng ngẫu nhiên.
- **Inventory System**: Thiết kế cơ sở dữ liệu vật phẩm, tính năng sử dụng Item.
- **Dynamic Shop**: Cửa hàng động lưu trữ bằng LocalStorage.
- **Daily Event**: Tung xúc xắc mỗi ngày (Lucky Day, Fog...).
- **Treasure Chest**: Logic mở rương và rớt đồ ngẫu nhiên.

## Phase 3: Career Journey (Tracking)
Mục tiêu: Giúp người chơi theo dõi hành trình tự học một cách trực quan.
- **Skill Tree**: Bản đồ kỹ năng dạng mạng lưới (Non-linear).
- **Story System**: Unlock câu chuyện khi lên cấp.
- **Statistics Board**: Thống kê số lượng Quest đã hoàn thành, tổng XP, số giờ code.
- **Achievements & Legacy Monument**: Cột mốc vinh danh.

## Phase 4: Polish (Hoàn thiện)
Mục tiêu: Nâng cấp trải nghiệm hình ảnh, âm thanh.
- Particle effects (Hạt, pháo hoa khi Level Up).
- Screen shake khi chém Boss.
- Âm thanh (Sound effects) cho các tương tác bấm.
- Responsive cho giao diện mobile.

## Phase 5: The Big Pivot (Spring Boot Integration)
Mục tiêu: Nâng cấp dự án thành một Fullstack Portfolio hoàn chỉnh.
- Tạo Project Spring Boot.
- Phân rã các Service từ `src/game/` sang Backend.
- Xây dựng Schema PostgreSQL (Players, Quests, Worlds, Inventories).
- Tích hợp JWT Authentication.
- Deploy lên môi trường Cloud.
