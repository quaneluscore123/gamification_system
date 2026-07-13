import { type PlayerProgress } from '../domain/models';
import { GameBalance } from '../../../shared/config/gameBalance';

export class PlayerService {

  addReward(progress: PlayerProgress, xp: number, gold: number): { newProgress: PlayerProgress, levelUp: boolean } {
    let newXp = progress.xp + xp;
    let newLevel = progress.level;
    let levelUp = false;

    let requiredXp = this.getRequiredXpForLevel(newLevel);

    while (newXp >= requiredXp) {
      newXp -= requiredXp;
      newLevel++;
      levelUp = true;
      requiredXp = this.getRequiredXpForLevel(newLevel);
    }

    const newTitle = this.getTitleForLevel(newLevel);

    return {
      newProgress: {
        ...progress,
        xp: newXp,
        level: newLevel,
        title: newTitle,
        gold: progress.gold + gold
      },
      levelUp
    };
  }

  getRequiredXpForLevel(level: number) {
    return Math.floor(GameBalance.BASE_XP_REQUIREMENT * Math.pow(GameBalance.XP_MULTIPLIER_PER_LEVEL, level - 1));
  }

  getTitleForLevel(level: number) {
    if (level >= 50) return 'Backend Engineer';
    if (level >= 30) return 'Spring Warrior';
    if (level >= 20) return 'Junior Backend';
    if (level >= 10) return 'Backend Explorer';
    if (level >= 5) return 'Java Apprentice';
    return 'Beginner';
  }
}

export const playerService = new PlayerService();
