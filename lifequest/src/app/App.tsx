import CharacterCard from '../domains/player/presentation/CharacterCard';
import MentorAstra from '../domains/meta/presentation/MentorAstra';
import FloorView from '../domains/world/presentation/FloorView';
import DevConsole from '../domains/meta/presentation/DevConsole';

function App() {
  return (
    <div style={{ width: '100%', paddingBottom: '50px' }}>
      <h3 style={{ marginBottom: '20px', textAlign: 'center', color: 'var(--primary-color)' }}>
        LifeQuest <span style={{ color: 'var(--text-muted)', fontSize: '14px' }}>| The Backend Journey</span>
      </h3>
      
      <MentorAstra />
      <CharacterCard />
      <FloorView />
      <DevConsole />
    </div>
  )
}

export default App;
