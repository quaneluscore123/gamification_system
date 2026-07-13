import { useState } from 'react';
import './DevConsole.css';
import { gameFacade } from '../facade/GameFacade';
import { storageService } from '../core/StorageService';

export default function DevConsole() {
  const [isOpen, setIsOpen] = useState(false);

  const handleCheatXp = () => {
    const progress = storageService.loadPlayerProgress();
    if (progress) {
      gameFacade.claimQuest(
        { id: 'cheat', title: '', type: 'OPTIONAL', difficulty: 'EASY', estimatedTimeMin: 0, tags: [], rewards: { xp: 100, gold: 0, bossDamage: 0 } },
        progress
      );
      // Forcing reload by brutal page refresh since we don't have the context hook attached here yet
      window.location.reload();
    }
  };

  const handleCheatGold = () => {
    const progress = storageService.loadPlayerProgress();
    if (progress) {
      progress.gold += 1000;
      storageService.savePlayerProgress(progress);
      window.location.reload();
    }
  };

  const handleKillBoss = () => {
    const savedWorlds = localStorage.getItem('lifequest_worlds');
    const savedWorldIndex = localStorage.getItem('lifequest_current_world');
    if (savedWorlds && savedWorldIndex !== null) {
      const worlds = JSON.parse(savedWorlds);
      const idx = JSON.parse(savedWorldIndex);

      if (worlds[idx] && worlds[idx].boss) {
        worlds[idx].boss.currentHp = 0;

        // Check next world
        if (idx < worlds.length - 1) {
          localStorage.setItem('lifequest_current_world', JSON.stringify(idx + 1));
        }

        localStorage.setItem('lifequest_worlds', JSON.stringify(worlds));
        window.location.reload();
      }
    }
  };

  const handleReset = () => {
    storageService.clearAll();
    window.location.reload();
  };

  if (!isOpen) {
    return (
      <button className="dev-console-toggle" onClick={() => setIsOpen(true)}>
        🛠️ Dev
      </button>
    );
  }

  return (
    <div className="dev-console-panel">
      <div className="dev-header">
        <h4>Developer Console</h4>
        <button onClick={() => setIsOpen(false)}>X</button>
      </div>
      <div className="dev-actions">
        <button onClick={handleCheatXp}>+100 XP</button>
        <button onClick={handleCheatGold}>+1000 Gold</button>
        <button onClick={handleKillBoss}>Kill Boss</button>
        <button onClick={handleReset} style={{ backgroundColor: 'darkred' }}>Reset Save</button>
      </div>
    </div>
  );
}
