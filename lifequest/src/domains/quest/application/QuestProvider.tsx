import { createContext, useState, useEffect, type ReactNode, useContext } from 'react';
import { type QuestProgress } from '../domain/models';
import { questService } from './QuestService';
import { storageService } from '../../../shared/infrastructure/StorageService';
import { PlayerContext } from '../../player/application/PlayerProvider';
import { WorldContext } from '../../world/application/WorldProvider';

export interface QuestContextState {
  questProgresses: QuestProgress[];
  completeQuest: (questId: string) => void;
  claimQuest: (questId: string) => void;
}

export const QuestContext = createContext<QuestContextState>({} as QuestContextState);

export const QuestProvider = ({ children }: { children: ReactNode }) => {
  const { addXpAndGold } = useContext(PlayerContext);
  const { damageCurrentBoss } = useContext(WorldContext);

  const [questProgresses, setQuestProgresses] = useState<QuestProgress[]>(() => {
    return storageService.loadQuestProgress();
  });

  useEffect(() => {
    storageService.saveQuestProgress(questProgresses);
  }, [questProgresses]);

  const completeQuest = (questId: string) => {
    // If it's a MAIN quest from dungeon, we auto-claim it for fluid gameplay.
    const def = questService.getQuestDef(questId);
    if (!def) return;

    setQuestProgresses(prev => {
      const { newProgressList, isNewlyCompleted } = questService.completeQuest(questId, prev);
      
      if (isNewlyCompleted) {
         // Auto-claim logic (for MVP, we auto-claim all quests to save clicks)
         // 1. Grant Player Rewards
         addXpAndGold(def.rewards.xp, def.rewards.gold);
         
         // 2. Damage Boss if it has bossDamage
         if (def.rewards.bossDamage > 0) {
            damageCurrentBoss(def.rewards.bossDamage);
         }
         
         // Mark as claimed
         return questService.claimQuestReward(questId, newProgressList);
      }
      return newProgressList;
    });
  };

  const claimQuest = (questId: string) => {
      // Manual claim if we want it later
      setQuestProgresses(prev => questService.claimQuestReward(questId, prev));
  };

  return (
    <QuestContext.Provider value={{ questProgresses, completeQuest, claimQuest }}>
      {children}
    </QuestContext.Provider>
  );
};
