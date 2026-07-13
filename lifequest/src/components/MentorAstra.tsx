import { useContext } from 'react';
import { GameContext } from '../context/GameContext';
import './MentorAstra.css';

export default function MentorAstra() {
  const { player } = useContext(GameContext);

  // Mentor dialogue logic based on game state
  let expression = "🙂";
  let dialogue = "Today's goal: Finish JWT Authentication. You're almost ready for your first interview.";

  if (player.level > 1) {
      dialogue = `You've reached level ${player.level}! Keep pushing your limits.`;
      expression = "😄";
  }

  return (
    <div className="mentor-container">
      <div className="mentor-avatar">
        {expression}
      </div>
      <div className="mentor-dialogue">
        <span className="mentor-name">Astra:</span>
        <p>"{dialogue}"</p>
      </div>
    </div>
  );
}
