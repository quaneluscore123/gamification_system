import './CharacterCard.css';

export default function CharacterCard() {
    return (
        <div className="character-card">
            <div className="avatar">
                {/* Tạm dùng Emoji làm Avatar nhé */}
                <span className="avatar-icon">👨‍💻</span>
            </div>
            <div className="info">
                <h2 className="name">Astra's Disciple</h2>
                <p className="title">Lv. 12 | Spring Boot Novice</p>

                <div className="progress-container">
                    <div className="progress-bar" style={{ width: '60%' }}></div>
                </div>
                <p className="xp-text">XP: 600 / 1000</p>
            </div>
        </div>
    );
}
