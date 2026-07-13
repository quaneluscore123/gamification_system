import { createContext, useState, useEffect, type ReactNode } from 'react';
import { type World, initialWorlds } from '../data/worldsData';
import { type PlayerProgress } from '../domain/models';
import { storageService } from '../core/StorageService';
import { gameFacade } from '../facade/GameFacade';

// Legacy Types (Dần dần sẽ được thay thế bởi Domain Models)
export interface GameState {
  player: PlayerProgress;
  worlds: World[];
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
    const { progress } = gameFacade.loadGame();
    return progress || defaultState.player;
  });

  const [worlds, setWorlds] = useState<World[]>(() => {
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

      if (room.completed) return prevWorlds;

      room.completed = true;

      // Tính thưởng
      const { updatedProgress, leveledUp } = gameFacade.claimQuest(
        { id: room.id, title: room.name, type: 'MAIN', difficulty: 'MEDIUM', estimatedTimeMin: 15, tags: [], rewards: { xp: room.xpReward, gold: room.goldReward, bossDamage: 0 } },
        player
      );
      setPlayer(updatedProgress);

      if (leveledUp) {
        // Có thể add logic hiển thị popup ở đây
      }

      // Xử lý Boss cũ (Sẽ refactor sang WorldService sau)
      const newBoss = { ...world.boss };
      let dmg = 100;
      if (room.xpReward > 100) dmg = 200;
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
