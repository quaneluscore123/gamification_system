import { createContext, useState, useEffect, type ReactNode } from 'react';
import { type PlayerProgress } from '../domain/models';
import { storageService } from '../../../shared/infrastructure/StorageService';
import { playerService } from './PlayerService';

export interface PlayerContextState {
  player: PlayerProgress;
  addXpAndGold: (xp: number, gold: number) => void;
  spendGold: (amount: number) => boolean;
}

export const PlayerContext = createContext<PlayerContextState>({} as PlayerContextState);

export const PlayerProvider = ({ children }: { children: ReactNode }) => {
  const [player, setPlayer] = useState<PlayerProgress>(() => {
    let profile = storageService.loadPlayerProfile();
    let progress = storageService.loadPlayerProgress();
    
    if (!profile) {
      profile = { id: 'usr-1', name: 'Astra\'s Disciple', avatar: '👨‍💻' };
      storageService.savePlayerProfile(profile);
    }
    if (!progress) {
      progress = { level: 1, xp: 0, gold: 0, title: 'Beginner', legacy: [], streak: 0 };
      storageService.savePlayerProgress(progress);
    }
    return progress;
  });

  useEffect(() => {
    storageService.savePlayerProgress(player);
  }, [player]);

  const addXpAndGold = (xp: number, gold: number) => {
    const { newProgress, levelUp } = playerService.addReward(player, xp, gold);
    setPlayer(newProgress);
    if (levelUp) {
      // Future: Trigger story/notification
    }
  };

  const spendGold = (amount: number) => {
    if (player.gold >= amount) {
      setPlayer(prev => ({ ...prev, gold: prev.gold - amount }));
      return true;
    }
    return false;
  };

  return (
    <PlayerContext.Provider value={{ player, addXpAndGold, spendGold }}>
      {children}
    </PlayerContext.Provider>
  );
};
