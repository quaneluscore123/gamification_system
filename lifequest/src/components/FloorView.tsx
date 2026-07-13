import { useContext } from 'react';
import { GameContext } from '../context/GameContext';
import './FloorView.css';

export default function FloorView() {
  const { worlds, currentWorldIndex, completeRoom } = useContext(GameContext);
  
  if (currentWorldIndex >= worlds.length) {
      return (
          <div className="floor-container">
              <h2>All Worlds Cleared!</h2>
              <p>You are now a true Java Backend Master.</p>
          </div>
      );
  }

  const currentWorld = worlds[currentWorldIndex];
  const allRoomsCompleted = currentWorld.rooms.every(r => r.completed);

  return (
    <div className="floor-container">
      <div className="floor-header">
        <h3 className="world-title">{currentWorld.name}</h3>
        <p className="world-desc">{currentWorld.description}</p>
      </div>

      <div className="boss-status">
         <h4>⚔ Boss: {currentWorld.boss.name}</h4>
         <div className="boss-hp-container">
            <div 
               className="boss-hp-bar" 
               style={{ width: `${(currentWorld.boss.currentHp / currentWorld.boss.maxHp) * 100}%` }}
            ></div>
         </div>
         <p className="boss-hp-text">{currentWorld.boss.currentHp} / {currentWorld.boss.maxHp} HP</p>
      </div>

      <div className="room-list">
         <h4>Dungeon Rooms:</h4>
         {currentWorld.rooms.map((room) => (
             <div key={room.id} className={`room-item ${room.completed ? 'completed' : ''}`}>
                 <div className="room-info">
                     <span className="room-name">{room.name}</span>
                     <span className="room-rewards">+{room.xpReward} XP | +{room.goldReward} 💰</span>
                 </div>
                 {!room.completed ? (
                     <button className="complete-btn" onClick={() => completeRoom(currentWorld.id, room.id)}>
                         Complete
                     </button>
                 ) : (
                     <span className="done-mark">✔ Done</span>
                 )}
             </div>
         ))}
      </div>

      {allRoomsCompleted && currentWorld.boss.currentHp === 0 && (
          <div className="world-clear">
              <h3>🎉 WORLD CLEARED!</h3>
              <p>Prepare for the next chapter.</p>
          </div>
      )}
    </div>
  );
}
