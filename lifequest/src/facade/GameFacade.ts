import { type PlayerProgress, type QuestDef, type WorldProgress, type WorldDef } from '../domain/models';
import { storageService } from '../core/StorageService';
import { playerService } from '../services/player/PlayerService';

// Facade to handle UI actions and coordinate Domain Services
export class GameFacade {

  // Initialization
  loadGame() {
    let profile = storageService.loadPlayerProfile();
    let progress = storageService.loadPlayerProgress();

    // First time setup
    if (!profile) {
      profile = { id: 'usr-1', name: 'Astra\'s Disciple', avatar: '👨‍💻' };
      storageService.savePlayerProfile(profile);
    }
    if (!progress) {
      progress = { level: 1, xp: 0, gold: 0, title: 'Beginner', legacy: [], streak: 0 };
      storageService.savePlayerProgress(progress);
    }

    return { profile, progress };
  }

  // Claim Quest Reward
  claimQuest(questDef: QuestDef, currentProgress: PlayerProgress): { updatedProgress: PlayerProgress, leveledUp: boolean } {
    // 1. Calculate Reward (Simplification: using static rewards from definition)
    const { xp, gold } = questDef.rewards;

    // 2. Add to Player via PlayerService
    const { newProgress, levelUp } = playerService.addReward(currentProgress, xp, gold);

    // 3. Save to DB (StorageService)
    storageService.savePlayerProgress(newProgress);

    return { updatedProgress: newProgress, leveledUp: levelUp };
  }

  // Hit Boss (Main Quests)
  hitBoss(_worldDef: WorldDef, worldProgress: WorldProgress, damage: number): { updatedWorld: WorldProgress, isDefeated: boolean } {
    let newHp = worldProgress.bossCurrentHp - damage;
    let isDefeated = false;

    if (newHp <= 0) {
      newHp = 0;
      isDefeated = true;
    }

    const updated = { ...worldProgress, bossCurrentHp: newHp, isCleared: isDefeated };

    // Note: In full DDD, we would fetch array of all world progress, update this one, and save.
    // For MVP facade simplification, returning the object to Context to handle array replacement.
    return { updatedWorld: updated, isDefeated };
  }
}

export const gameFacade = new GameFacade();
