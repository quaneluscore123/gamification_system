import { createContext, useState, useEffect, ReactNode } from 'react';
import { type World, initialWorlds, type Room } from '../data/worldsData';

export interface PlayerStats {
  level: number;
  xp: number;
  maxXp: number;
  gold: number;
  legacy: string[];
}

export interface GameState {
  player: PlayerStats;
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
    maxXp: 100,
    gold: 0,
    legacy: []
  },
  worlds: initialWorlds,
  currentWorldIndex: 0,
  completeRoom: () => { },
  gainGold: () => { },
  spendGold: () => false,
};

export const GameContext = createContext<GameState>(defaultState);

export const GameProvider = ({ children }: { children: ReactNode }) => {
  const [player, setPlayer] = useState<PlayerStats>(defaultState.player);
  const [worlds, setWorlds] = useState<World[]>(defaultState.worlds);
  const [currentWorldIndex, setCurrentWorldIndex] = useState(0);

  // Load from local storage on mount
  useEffect(() => {
    const savedPlayer = localStorage.getItem('lifequest_player');
    const savedWorlds = localStorage.getItem('lifequest_worlds');
    const savedWorldIndex = localStorage.getItem('lifequest_current_world');

    if (savedPlayer) setPlayer(JSON.parse(savedPlayer));
    if (savedWorlds) setWorlds(JSON.parse(savedWorlds));
    if (savedWorldIndex) setCurrentWorldIndex(JSON.parse(savedWorldIndex));
  }, []);

  // Auto-save whenever state changes
  useEffect(() => {
    localStorage.setItem('lifequest_player', JSON.stringify(player));
    localStorage.setItem('lifequest_worlds', JSON.stringify(worlds));
    localStorage.setItem('lifequest_current_world', JSON.stringify(currentWorldIndex));
  }, [player, worlds, currentWorldIndex]);

  const gainXp = (amount: number) => {
    setPlayer(prev => {
      let newXp = prev.xp + amount;
      let newLevel = prev.level;
      let newMaxXp = prev.maxXp;

      // Level up logic
      while (newXp >= newMaxXp) {
        newXp -= newMaxXp;
        newLevel += 1;
        newMaxXp = newLevel * 100; // Formula: level * 100

        // Trigger Level up animation / sound here later
      }

      return { ...prev, xp: newXp, level: newLevel, maxXp: newMaxXp };
    });
  };

  const gainGold = (amount: number) => {
    setPlayer(prev => ({ ...prev, gold: prev.gold + amount }));
  };

  const spendGold = (amount: number): boolean => {
    if (player.gold >= amount) {
      setPlayer(prev => ({ ...prev, gold: prev.gold - amount }));
      return true;
    }
    return false;
  };

  const completeRoom = (worldId: string, roomId: string) => {
    setWorlds(prevWorlds => {
      const newWorlds = [...prevWorlds];
      const worldIndex = newWorlds.findIndex(w => w.id === worldId);
      if (worldIndex === -1) return prevWorlds;

      const world = { ...newWorlds[worldIndex] };
      const roomIndex = world.rooms.findIndex(r => r.id === roomId);
      if (roomIndex === -1) return prevWorlds;

      const room = { ...world.rooms[roomIndex] };

      // Prevent completing twice
      if (room.completed) return prevWorlds;

      // Mark completed
      room.completed = true;
      world.rooms[roomIndex] = room;

      // Damage Boss
      const newBoss = { ...world.boss };
      newBoss.currentHp = Math.max(0, newBoss.currentHp - room.damageToBoss);
      world.boss = newBoss;

      newWorlds[worldIndex] = world;

      // Give Rewards
      gainXp(room.xpReward);
      gainGold(room.goldReward);

      // Check Boss Defeated (World Cleared)
      if (newBoss.currentHp === 0) {
        // Add Legacy
        setPlayer(prev => ({
          ...prev,
          legacy: [...prev.legacy, `Defeated ${newBoss.name} in ${world.name}`]
        }));

        // Move to next world if exists
        if (worldIndex < newWorlds.length - 1) {
          setCurrentWorldIndex(worldIndex + 1);
        }
      }

      return newWorlds;
    });
  };

  return (
    <GameContext.Provider value={{ player, worlds, currentWorldIndex, completeRoom, gainGold, spendGold }}>
      {children}
    </GameContext.Provider>
  );
};
