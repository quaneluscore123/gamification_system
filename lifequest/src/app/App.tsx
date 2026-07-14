import { useState } from 'react';
import CharacterCard from '../domains/player/presentation/CharacterCard';
import MentorAstra from '../domains/meta/presentation/MentorAstra';
import FloorView from '../domains/world/presentation/FloorView';
import QuestList from '../domains/quest/presentation/QuestList';
import DevConsole from '../domains/meta/presentation/DevConsole';

function App() {
  const [activeTab, setActiveTab] = useState<'WORLD' | 'QUESTS'>('WORLD');

  return (
    <div style={{ width: '100%', paddingBottom: '50px' }}>
      <h3 style={{ marginBottom: '20px', textAlign: 'center', color: 'var(--primary-color)' }}>
        LifeQuest <span style={{ color: 'var(--text-muted)', fontSize: '14px' }}>| The Backend Journey</span>
      </h3>
      
      <MentorAstra />
      <CharacterCard />
      
      <div style={{ display: 'flex', gap: '10px', marginBottom: '15px' }}>
        <button 
          style={{ flex: 1, backgroundColor: activeTab === 'WORLD' ? 'var(--primary-color)' : 'var(--card-bg)' }}
          onClick={() => setActiveTab('WORLD')}
        >
          🏰 Dungeons
        </button>
        <button 
          style={{ flex: 1, backgroundColor: activeTab === 'QUESTS' ? 'var(--primary-color)' : 'var(--card-bg)' }}
          onClick={() => setActiveTab('QUESTS')}
        >
          📜 Quests
        </button>
      </div>

      {activeTab === 'WORLD' ? <FloorView /> : <QuestList />}
      
      <DevConsole />
    </div>
  )
}

export default App;
