import { type PlayerProfile, type PlayerProgress } from '../../domains/player/domain/models';
import { type QuestProgress } from '../../domains/quest/domain/models';
import { type WorldProgress } from '../../domains/world/domain/models';
import { type InventoryProgress } from '../../domains/inventory/domain/models';

// MVP Storage Implementation using LocalStorage
export interface IStorageService {
  savePlayerProfile(profile: PlayerProfile): void;
  loadPlayerProfile(): PlayerProfile | null;
  savePlayerProgress(progress: PlayerProgress): void;
  loadPlayerProgress(): PlayerProgress | null;
  saveQuestProgress(progress: QuestProgress[]): void;
  loadQuestProgress(): QuestProgress[];
  saveWorldProgress(progress: WorldProgress[]): void;
  loadWorldProgress(): WorldProgress[];
  saveInventoryProgress(progress: InventoryProgress): void;
  loadInventoryProgress(): InventoryProgress | null;
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
  saveQuestProgress(progress: QuestProgress[]): void {
    localStorage.setItem('lq_quest_prog', JSON.stringify(progress));
  }
  loadQuestProgress(): QuestProgress[] {
    const data = localStorage.getItem('lq_quest_prog');
    return data ? JSON.parse(data) : [];
  }
  saveWorldProgress(progress: WorldProgress[]): void {
    localStorage.setItem('lq_world_prog', JSON.stringify(progress));
  }
  loadWorldProgress(): WorldProgress[] {
    const data = localStorage.getItem('lq_world_prog');
    return data ? JSON.parse(data) : [];
  }
  saveInventoryProgress(progress: InventoryProgress): void {
    localStorage.setItem('lq_inventory_prog', JSON.stringify(progress));
  }
  loadInventoryProgress(): InventoryProgress | null {
    const data = localStorage.getItem('lq_inventory_prog');
    return data ? JSON.parse(data) : null;
  }
  clearAll(): void {
    localStorage.removeItem('lq_profile');
    localStorage.removeItem('lq_player_prog');
    localStorage.removeItem('lq_quest_prog');
    localStorage.removeItem('lq_world_prog');
    localStorage.removeItem('lq_inventory_prog');
    localStorage.removeItem('lifequest_worlds');
    localStorage.removeItem('lifequest_current_world');
  }
}

export const storageService = new LocalStorageService();
