import { type WorldDef, type WorldProgress } from '../domain/models';

export class WorldService {
  
  damageBoss(worldProgress: WorldProgress, damage: number): { updatedWorld: WorldProgress, bossDefeated: boolean } {
    let newHp = worldProgress.bossCurrentHp - damage;
    let bossDefeated = false;

    if (newHp <= 0) {
      newHp = 0;
      bossDefeated = true;
    }

    const updatedWorld = {
      ...worldProgress,
      bossCurrentHp: newHp,
      isCleared: bossDefeated
    };

    return { updatedWorld, bossDefeated };
  }

  // Khởi tạo tiến trình mặc định cho một World nếu chưa có
  initializeWorldProgress(worldDef: WorldDef): WorldProgress {
    return {
      worldId: worldDef.id,
      bossCurrentHp: worldDef.boss.maxHp,
      isCleared: false
    };
  }
}

export const worldService = new WorldService();
