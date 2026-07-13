import { useContext } from 'react';
import { GameContext } from '../../../app/providers/GameProvider';
import './FloorView.css';

export default function FloorView() {
    const { worlds, currentWorldIndex, completeRoom } = useContext(GameContext);
    const currentWorld = worlds[currentWorldIndex];

    if (!currentWorld) return <div className="floor-view">Loading...</div>;

    const bossHpPercent = Math.max(0, Math.min(100, (currentWorld.boss as any).currentHp / (currentWorld.boss as any).maxHp * 100));

    return (
        <div className="floor-view">
            <h3 className="world-title">World {currentWorldIndex + 1}: {currentWorld.name}</h3>
            <p className="world-desc">{currentWorld.description}</p>

            <div className="boss-container">
                <h4 className="boss-name">⚔️ Boss: {currentWorld.boss.name}</h4>
                <div className="boss-hp-bar">
                    <div className="boss-hp-fill" style={{ width: `${bossHpPercent}%` }}></div>
                </div>
                <p className="boss-hp-text">{(currentWorld.boss as any).currentHp} / {(currentWorld.boss as any).maxHp} HP</p>
            </div>

            <div className="rooms-container">
                <h4>Dungeon Rooms:</h4>
                {currentWorld.rooms.map((room) => (
                    <div key={room.id} className={`room-card ${(room as any).completed ? 'completed' : ''}`}>
                        <div className="room-info">
                            <h5 className="room-name">{room.name}</h5>
                            <p className="room-rewards">
                                +{(room as any).xpReward} XP | +{(room as any).goldReward} 💰
                            </p>
                        </div>
                        <button 
                            className="btn-complete" 
                            disabled={(room as any).completed}
                            onClick={() => completeRoom(currentWorld.id, room.id)}
                        >
                            {(room as any).completed ? 'Complete' : 'Fight'}
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}
