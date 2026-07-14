import { useContext } from 'react';
import { QuestContext } from '../application/QuestProvider';
import { dailyQuests, weeklyQuests } from '../domain/questsData';
import './QuestList.css';

export default function QuestList() {
    const { questProgresses, completeQuest } = useContext(QuestContext);

    const isCompleted = (id: string) => {
        const p = questProgresses.find(q => q.questId === id);
        return p?.status === 'COMPLETED' || p?.status === 'CLAIMED';
    };

    return (
        <div className="quest-list">
            <h3>Nhiệm Vụ Hàng Ngày (Daily)</h3>
            <div className="quest-group">
                {dailyQuests.map(quest => (
                    <div key={quest.id} className={`quest-card ${isCompleted(quest.id) ? 'completed' : ''}`}>
                        <div className="quest-info">
                            <h4>{quest.title}</h4>
                            <p className="quest-meta">⏳ {quest.estimatedTimeMin}m | {quest.tags.join(', ')}</p>
                            <p className="quest-rewards">
                                +{quest.rewards.xp} XP | +{quest.rewards.gold} 💰 | ⚔️ {quest.rewards.bossDamage} DMG
                            </p>
                        </div>
                        <button 
                            className="btn-complete" 
                            disabled={isCompleted(quest.id)}
                            onClick={() => completeQuest(quest.id)}
                        >
                            {isCompleted(quest.id) ? 'Đã Xong' : 'Hoàn Thành'}
                        </button>
                    </div>
                ))}
            </div>

            <h3 style={{marginTop: '20px'}}>Nhiệm Vụ Tuần (Weekly)</h3>
            <div className="quest-group">
                {weeklyQuests.map(quest => (
                    <div key={quest.id} className={`quest-card ${isCompleted(quest.id) ? 'completed' : ''}`}>
                        <div className="quest-info">
                            <h4>{quest.title}</h4>
                            <p className="quest-meta">⏳ {quest.estimatedTimeMin}m | {quest.tags.join(', ')}</p>
                            <p className="quest-rewards">
                                +{quest.rewards.xp} XP | +{quest.rewards.gold} 💰 | ⚔️ {quest.rewards.bossDamage} DMG
                            </p>
                        </div>
                        <button 
                            className="btn-complete" 
                            disabled={isCompleted(quest.id)}
                            onClick={() => completeQuest(quest.id)}
                        >
                            {isCompleted(quest.id) ? 'Đã Xong' : 'Hoàn Thành'}
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}
