import { useContext } from 'react';
import { WorldContext } from '../application/WorldProvider';
import { QuestContext } from '../../quest/application/QuestProvider';
import { questService } from '../../quest/application/QuestService';
import './FloorView.css';

export default function FloorView() {
    const { worldDefs, worldProgresses, currentWorldIndex } = useContext(WorldContext);
    const { completeQuest, questProgresses } = useContext(QuestContext);

    const currentWorldDef = worldDefs[currentWorldIndex];
    const currentWorldProgress = worldProgresses[currentWorldIndex];

    if (!currentWorldDef || !currentWorldProgress) return <div className="floor-view">Loading...</div>;

    const bossHpPercent = Math.max(0, Math.min(100, currentWorldProgress.bossCurrentHp / currentWorldDef.boss.maxHp * 100));

    const isRoomCompleted = (questId: string) => {
        const p = questProgresses.find(q => q.questId === questId);
        return p?.status === 'COMPLETED' || p?.status === 'CLAIMED';
    };

    return (
        <div className="floor-view">
            <h3 className="world-title">World {currentWorldIndex + 1}: {currentWorldDef.name}</h3>
            <p className="world-desc">{currentWorldDef.description}</p>

            <div className="boss-container">
                <h4 className="boss-name">⚔️ Boss: {currentWorldDef.boss.name}</h4>
                <div className="boss-hp-bar">
                    <div className="boss-hp-fill" style={{ width: `${bossHpPercent}%` }}></div>
                </div>
                <p className="boss-hp-text">{currentWorldProgress.bossCurrentHp} / {currentWorldDef.boss.maxHp} HP</p>
            </div>

            <div className="rooms-container">
                <h4>Dungeon Rooms:</h4>
                {currentWorldDef.rooms.map((room) => {
                    const questDef = questService.getQuestDef(room.questId);
                    const completed = isRoomCompleted(room.questId);
                    
                    return (
                        <div key={room.id} className={`room-card ${completed ? 'completed' : ''}`}>
                            <div className="room-info">
                                <h5 className="room-name">{room.name}</h5>
                                <p className="room-rewards">
                                    +{questDef?.rewards.xp || 0} XP | +{questDef?.rewards.gold || 0} 💰
                                </p>
                            </div>
                            <button 
                                className="btn-complete" 
                                disabled={completed || currentWorldProgress.isCleared}
                                onClick={() => completeQuest(room.questId)}
                            >
                                {completed ? 'Cleared' : 'Fight'}
                            </button>
                        </div>
                    );
                })}
            </div>
            {currentWorldProgress.isCleared && (
                <div style={{ marginTop: '15px', color: '#059669', fontWeight: 'bold', textAlign: 'center' }}>
                    🎉 Vượt ải thành công! Nhấn sang tab Worlds hoặc Quest để đi tiếp.
                </div>
            )}
        </div>
    );
}
