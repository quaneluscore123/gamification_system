import { createContext, useState, useEffect, type ReactNode } from 'react';
import { initialWorlds } from '../domain/worldsData';
import { type WorldDef, type WorldProgress } from '../domain/models';
import { storageService } from '../../../shared/infrastructure/StorageService';
import { worldService } from './WorldService';

export interface WorldContextState {
  worldDefs: WorldDef[];
  worldProgresses: WorldProgress[];
  currentWorldIndex: number;
  damageCurrentBoss: (damage: number) => void;
  nextWorld: () => void;
}

export const WorldContext = createContext<WorldContextState>({} as WorldContextState);

export const WorldProvider = ({ children }: { children: ReactNode }) => {
  const worldDefs = initialWorlds;
  
  const [worldProgresses, setWorldProgresses] = useState<WorldProgress[]>(() => {
    let saved = storageService.loadWorldProgress();
    if (saved.length === 0) {
      // Initialize world 1
      saved = [worldService.initializeWorldProgress(worldDefs[0])];
      storageService.saveWorldProgress(saved);
    }
    return saved;
  });

  const [currentWorldIndex, setCurrentWorldIndex] = useState<number>(() => {
    const savedIdx = localStorage.getItem('lifequest_current_world');
    return savedIdx ? JSON.parse(savedIdx) : 0;
  });

  useEffect(() => {
    storageService.saveWorldProgress(worldProgresses);
    localStorage.setItem('lifequest_current_world', JSON.stringify(currentWorldIndex));
  }, [worldProgresses, currentWorldIndex]);

  const damageCurrentBoss = (damage: number) => {
    setWorldProgresses(prev => {
      const currentProgress = prev[currentWorldIndex];
      if (!currentProgress) return prev;
      
      const { updatedWorld, bossDefeated } = worldService.damageBoss(currentProgress, damage);
      
      const newList = [...prev];
      newList[currentWorldIndex] = updatedWorld;
      
      if (bossDefeated && currentWorldIndex < worldDefs.length - 1) {
        // Boss defeated! We don't auto-advance index here to let UI show victory, 
        // but for MVP we can just initialize the next world progress.
        const nextWorldDef = worldDefs[currentWorldIndex + 1];
        if (!newList.find(w => w.worldId === nextWorldDef.id)) {
           newList.push(worldService.initializeWorldProgress(nextWorldDef));
        }
      }
      return newList;
    });
  };

  const nextWorld = () => {
    if (currentWorldIndex < worldDefs.length - 1) {
      setCurrentWorldIndex(currentWorldIndex + 1);
    }
  };

  return (
    <WorldContext.Provider value={{ worldDefs, worldProgresses, currentWorldIndex, damageCurrentBoss, nextWorld }}>
      {children}
    </WorldContext.Provider>
  );
};
