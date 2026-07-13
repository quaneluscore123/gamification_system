import { type PlayerProfile, type PlayerProgress } from '../../domains/player/domain/models';

// MVP Storage Implementation using LocalStorage
export interface IStorageService {
  savePlayerProfile(profile: PlayerProfile): void;
  loadPlayerProfile(): PlayerProfile | null;
  savePlayerProgress(progress: PlayerProgress): void;
  loadPlayerProgress(): PlayerProgress | null;
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
  clearAll(): void {
    localStorage.removeItem('lq_profile');
    localStorage.removeItem('lq_player_prog');
    localStorage.removeItem('lifequest_worlds');
    localStorage.removeItem('lifequest_current_world');
  }
}

export const storageService = new LocalStorageService();
