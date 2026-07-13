export interface BossDef {
  id: string;
  name: string;
  maxHp: number;
}

export interface RoomDef {
  id: string;
  name: string;
  questId: string;
  xpReward?: number;
  goldReward?: number;
  completed?: boolean;
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
