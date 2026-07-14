import { type QuestDef, type QuestProgress, type QuestStatus } from '../domain/models';
import { allQuests } from '../domain/questsData';

export class QuestService {
  getQuestDef(questId: string): QuestDef | undefined {
    return allQuests.find(q => q.id === questId);
  }

  // Returns true if the status changed, indicating we should award rewards
  completeQuest(questId: string, currentProgress: QuestProgress[]): { newProgressList: QuestProgress[], isNewlyCompleted: boolean } {
    const existing = currentProgress.find(p => p.questId === questId);
    
    if (existing) {
      if (existing.status === 'COMPLETED' || existing.status === 'CLAIMED') {
        return { newProgressList: currentProgress, isNewlyCompleted: false };
      }
      
      const updatedList = currentProgress.map(p => 
        p.questId === questId ? { ...p, status: 'COMPLETED' as QuestStatus } : p
      );
      
      return { newProgressList: updatedList, isNewlyCompleted: true };
    }
    
    // If not found in progress list, create it as COMPLETED
    const newList = [...currentProgress, { questId, status: 'COMPLETED' as QuestStatus }];
    return { newProgressList: newList, isNewlyCompleted: true };
  }

  claimQuestReward(questId: string, currentProgress: QuestProgress[]): QuestProgress[] {
    return currentProgress.map(p => 
      p.questId === questId ? { ...p, status: 'CLAIMED' as QuestStatus } : p
    );
  }
}

export const questService = new QuestService();
