import { useContext } from 'react';
import { PlayerContext } from '../application/PlayerProvider';
import { playerService } from '../application/PlayerService';
import './CharacterCard.css';

export default function CharacterCard() {
    const { player } = useContext(PlayerContext);

    const requiredXp = playerService.getRequiredXpForLevel(player.level);
    const xpPercentage = Math.min(100, Math.floor((player.xp / requiredXp) * 100));

    return (
        <div className="character-card">
            <div className="avatar">
                <span className="avatar-icon">👨‍💻</span>
            </div>
            <div className="info">
                <h2 className="name">Astra's Disciple</h2>
                <p className="title">Lv. {player.level} | {player.title}</p>

                <div className="progress-container">
                    <div className="progress-bar" style={{ width: `${xpPercentage}%` }}></div>
                </div>
                <p className="xp-text">XP: {player.xp} / {requiredXp} &nbsp;&nbsp;&nbsp; 💰 Gold: {player.gold}</p>
            </div>
        </div>
    );
}
