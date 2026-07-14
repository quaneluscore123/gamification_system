import { useContext } from 'react';
import { PlayerContext } from '../../player/application/PlayerProvider';
import './MentorAstra.css';

export default function MentorAstra() {
    const { player } = useContext(PlayerContext);

    // Simple reactive text based on level
    const getDialogue = () => {
        if (player.level < 5) return "Welcome to LifeQuest! Complete tasks to gain XP and level up.";
        if (player.level < 10) return "You're getting the hang of it! Keep exploring those Java concepts.";
        if (player.level < 20) return "Impressive progress! The Spring framework awaits you soon.";
        return "You're a true Backend Explorer now! Today's goal: Finish API Authentication.";
    };

    return (
        <div className="mentor-astra">
            <div className="mentor-avatar">
                <span className="mentor-icon">🧙‍♀️</span>
            </div>
            <div className="mentor-dialogue">
                <p><strong>Astra:</strong> {getDialogue()}</p>
            </div>
        </div>
    );
}
