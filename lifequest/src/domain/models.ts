// Player
export interface PlayerProfile {
  id: string;
  name: string;
  avatar: string;
}

export interface PlayerProgress {
  level: number;
  xp: number;
  gold: number;
  title: string;
  legacy: string[];
  streak: number;
}

// Quest
export type QuestStatus = 'LOCKED' | 'AVAILABLE' | 'IN_PROGRESS' | 'COMPLETED' | 'CLAIMED';

export interface QuestDef {
  id: string;
  title: string;
  type: 'DAILY' | 'WEEKLY' | 'MAIN' | 'OPTIONAL';
  difficulty: 'EASY' | 'MEDIUM' | 'HARD' | 'BOSS';
  estimatedTimeMin: number;
  tags: string[];
  rewards: {
    xp: number;
    gold: number;
    bossDamage: number;
  };
}

export interface QuestProgress {
  questId: string;
  status: QuestStatus;
}

// World & Boss
export interface BossDef {
  id: string;
  name: string;
  maxHp: number;
}

export interface RoomDef {
  id: string;
  name: string;
  questId: string; // Liên kết Room với 1 Quest cụ thể
}

export interface WorldDef {
  id: string;
  name: string;
  description: string;
  requiredLevel: number;
  boss: BossDef;
  rooms: RoomDef[];
}

export interface WorldProgress {
  worldId: string;
  bossCurrentHp: number;
  isCleared: boolean;
}
