import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App.tsx';
import '../shared/ui/index.css';
import { PlayerProvider } from '../domains/player/application/PlayerProvider.tsx';
import { WorldProvider } from '../domains/world/application/WorldProvider.tsx';
import { QuestProvider } from '../domains/quest/application/QuestProvider.tsx';
import { InventoryProvider } from '../domains/inventory/application/InventoryProvider.tsx';
import { ShopProvider } from '../domains/shop/application/ShopProvider.tsx';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <PlayerProvider>
      <WorldProvider>
        <QuestProvider>
          <InventoryProvider>
            <ShopProvider>
              <App />
            </ShopProvider>
          </InventoryProvider>
        </QuestProvider>
      </WorldProvider>
    </PlayerProvider>
  </StrictMode>,
);
