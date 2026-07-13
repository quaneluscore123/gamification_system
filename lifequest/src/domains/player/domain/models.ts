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
