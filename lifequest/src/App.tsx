import CharacterCard from './components/CharacterCard';
import MentorAstra from './components/MentorAstra';
import FloorView from './components/FloorView';
import { GameProvider } from './context/GameContext';

// We wrap the inner App with GameProvider in main.tsx, so here we just use the components.
function App() {
  return (
    <div style={{ width: '100%', paddingBottom: '50px' }}>
      <h3 style={{ marginBottom: '20px', textAlign: 'center', color: 'var(--primary-color)' }}>
        LifeQuest <span style={{ color: 'var(--text-muted)', fontSize: '14px' }}>| The Backend Journey</span>
      </h3>
      
      <MentorAstra />
      <CharacterCard />
      <FloorView />
      
    </div>
  )
}

export default App;
