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
