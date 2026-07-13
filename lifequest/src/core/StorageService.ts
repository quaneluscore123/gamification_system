import { type PlayerProfile, type PlayerProgress, type QuestProgress, type WorldProgress } from '../domain/models';

export interface IStorageService {
  savePlayerProfile(profile: PlayerProfile): void;
  loadPlayerProfile(): PlayerProfile | null;

  savePlayerProgress(progress: PlayerProgress): void;
  loadPlayerProgress(): PlayerProgress | null;

  saveQuestProgress(progresses: QuestProgress[]): void;
  loadQuestProgress(): QuestProgress[];

  saveWorldProgress(progresses: WorldProgress[]): void;
  loadWorldProgress(): WorldProgress[];

  clearAll(): void;
}

export class LocalStorageService implements IStorageService {
  savePlayerProfile(profile: PlayerProfile): void {
    localStorage.setItem('lq_profile', JSON.stringify(profile));
  }
  loadPlayerProfile(): PlayerProfile | null {
    const data = localStorage.getItem('lq_profile');
    return data ? JSON.parse(data) : null;
  }

  savePlayerProgress(progress: PlayerProgress): void {
    localStorage.setItem('lq_player_prog', JSON.stringify(progress));
  }
  loadPlayerProgress(): PlayerProgress | null {
    const data = localStorage.getItem('lq_player_prog');
    return data ? JSON.parse(data) : null;
  }

  saveQuestProgress(progresses: QuestProgress[]): void {
    localStorage.setItem('lq_quest_prog', JSON.stringify(progresses));
  }
  loadQuestProgress(): QuestProgress[] {
    const data = localStorage.getItem('lq_quest_prog');
    return data ? JSON.parse(data) : [];
  }

  saveWorldProgress(progresses: WorldProgress[]): void {
    localStorage.setItem('lq_world_prog', JSON.stringify(progresses));
  }
  loadWorldProgress(): WorldProgress[] {
    const data = localStorage.getItem('lq_world_prog');
    return data ? JSON.parse(data) : [];
  }

  clearAll(): void {
    localStorage.clear();
  }
}

// Export a singleton instance for frontend use
export const storageService = new LocalStorageService();
