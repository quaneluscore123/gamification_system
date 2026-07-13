import { createContext, useState, useEffect, type ReactNode } from 'react';
import { initialWorlds } from '../../domains/world/domain/worldsData';
import { type WorldDef } from '../../domains/world/domain/models';
import { type PlayerProgress } from '../../domains/player/domain/models';
import { storageService } from '../../shared/infrastructure/StorageService';
import { playerService } from '../../domains/player/application/PlayerService';

// Legacy Types (Dần dần sẽ được thay thế bởi Domain Models)
export interface GameState {
  player: PlayerProgress;
  worlds: WorldDef[];
  currentWorldIndex: number;
  completeRoom: (worldId: string, roomId: string) => void;
  gainGold: (amount: number) => void;
  spendGold: (amount: number) => boolean;
}

const defaultState: GameState = {
  player: {
    level: 1,
    xp: 0,
    gold: 0,
    title: 'Beginner',
    legacy: [],
    streak: 0
  },
  worlds: initialWorlds,
  currentWorldIndex: 0,
  completeRoom: () => { },
  gainGold: () => { },
  spendGold: () => false,
};

export const GameContext = createContext<GameState>(defaultState);

export const GameProvider = ({ children }: { children: ReactNode }) => {
  // Load synchronously to prevent initial state from overwriting saved data
  const [player, setPlayer] = useState<PlayerProgress>(() => {
    let profile = storageService.loadPlayerProfile();
    let progress = storageService.loadPlayerProgress();
    
    if (!profile) {
      profile = { id: 'usr-1', name: 'Astra\'s Disciple', avatar: '👨‍💻' };
      storageService.savePlayerProfile(profile);
    }
    if (!progress) {
      progress = defaultState.player;
      storageService.savePlayerProgress(progress);
    }
    return progress || defaultState.player;
  });

  const [worlds, setWorlds] = useState<WorldDef[]>(() => {
    const savedWorlds = localStorage.getItem('lifequest_worlds');
    return savedWorlds ? JSON.parse(savedWorlds) : defaultState.worlds;
  });

  const [currentWorldIndex, setCurrentWorldIndex] = useState<number>(() => {
    const savedWorldIndex = localStorage.getItem('lifequest_current_world');
    return savedWorldIndex ? JSON.parse(savedWorldIndex) : defaultState.currentWorldIndex;
  });

  // Save Player via StorageService, others via old way temporarily
  useEffect(() => {
    storageService.savePlayerProgress(player);
    localStorage.setItem('lifequest_worlds', JSON.stringify(worlds));
    localStorage.setItem('lifequest_current_world', JSON.stringify(currentWorldIndex));
  }, [player, worlds, currentWorldIndex]);

  // Handle Room Completion
  const completeRoom = (worldId: string, roomId: string) => {
    setWorlds(prevWorlds => {
      const worldIndex = prevWorlds.findIndex(w => w.id === worldId);
      if (worldIndex === -1) return prevWorlds;

      const world = prevWorlds[worldIndex];
      const roomIndex = world.rooms.findIndex(r => r.id === roomId);
      if (roomIndex === -1) return prevWorlds;

      const room = { ...world.rooms[roomIndex] };

      // Any room object from initialWorlds acts as a RoomDef mixed with progress right now
      // This will be refactored when QuestService is fully implemented.
      if ((room as any).completed) return prevWorlds;
      (room as any).completed = true;

      // Tính thưởng
      const { newProgress, levelUp } = playerService.addReward(player, (room as any).xpReward, (room as any).goldReward);
      setPlayer(newProgress);

      if (levelUp) {
        // Có thể add logic hiển thị popup ở đây
      }

      // Xử lý Boss cũ (Sẽ refactor sang WorldService sau)
      const newBoss = { ...world.boss, currentHp: (world.boss as any).currentHp };
      let dmg = 100;
      if ((room as any).xpReward > 100) dmg = 200;
      newBoss.currentHp -= dmg;

      if (newBoss.currentHp < 0) newBoss.currentHp = 0;

      const newRooms = [...world.rooms];
      newRooms[roomIndex] = room;

      const newWorld = {
        ...world,
        rooms: newRooms,
        boss: newBoss
      };

      const newWorlds = [...prevWorlds];
      newWorlds[worldIndex] = newWorld;

      if (newBoss.currentHp === 0) {
        setPlayer(prev => ({
          ...prev,
          legacy: [...prev.legacy, `Defeated ${newBoss.name} in ${world.name}`]
        }));
        if (worldIndex < newWorlds.length - 1) {
          setCurrentWorldIndex(worldIndex + 1);
        }
      }

      return newWorlds;
    });
  };

  const gainGold = (amount: number) => {
    setPlayer(prev => ({ ...prev, gold: prev.gold + amount }));
  };

  const spendGold = (amount: number) => {
    if (player.gold >= amount) {
      setPlayer(prev => ({ ...prev, gold: prev.gold - amount }));
      return true;
    }
    return false;
  };

  return (
    <GameContext.Provider value={{
      player,
      worlds,
      currentWorldIndex,
      completeRoom,
      gainGold,
      spendGold
    }}>
      {children}
    </GameContext.Provider>
  );
};
