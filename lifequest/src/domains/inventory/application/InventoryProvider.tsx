import { createContext, useState, useEffect, type ReactNode, useContext } from 'react';
import { type InventoryProgress } from '../domain/models';
import { inventoryService } from './InventoryService';
import { storageService } from '../../../shared/infrastructure/StorageService';
import { shopItems } from '../../shop/domain/models';
import { PlayerContext } from '../../player/application/PlayerProvider';
import { WorldContext } from '../../world/application/WorldProvider';

export interface InventoryContextState {
  inventory: InventoryProgress;
  addItem: (itemId: string, quantity?: number) => void;
  removeItem: (itemId: string, quantity?: number) => void;
  useItem: (itemId: string) => boolean;
}

export const InventoryContext = createContext<InventoryContextState>({} as InventoryContextState);

export const InventoryProvider = ({ children }: { children: ReactNode }) => {
  const { addXpAndGold } = useContext(PlayerContext);
  const { damageCurrentBoss } = useContext(WorldContext);

  const [inventory, setInventory] = useState<InventoryProgress>(() => {
    const saved = storageService.loadInventoryProgress();
    if (saved) return saved;
    // Initial empty inventory
    const initial: InventoryProgress = { items: [] };
    storageService.saveInventoryProgress(initial);
    return initial;
  });

  useEffect(() => {
    storageService.saveInventoryProgress(inventory);
  }, [inventory]);

  const addItem = (itemId: string, quantity: number = 1) => {
    setInventory(prev => inventoryService.addItem(prev, itemId, quantity));
  };

  const removeItem = (itemId: string, quantity: number = 1) => {
    setInventory(prev => inventoryService.removeItem(prev, itemId, quantity));
  };

  const useItem = (itemId: string): boolean => {
    // Kiểm tra xem có item trong túi không
    if (!inventoryService.hasItem(inventory, itemId, 1)) return false;

    const itemDef = shopItems.find(i => i.id === itemId);
    if (!itemDef) return false;

    // Kích hoạt hiệu ứng
    const effect = itemDef.effect;
    if (effect.type === 'GRANT_XP') {
      addXpAndGold(effect.value, 0);
    } else if (effect.type === 'GRANT_GOLD') {
      addXpAndGold(0, effect.value);
    } else if (effect.type === 'DAMAGE_BOSS') {
      damageCurrentBoss(effect.value);
    }

    // Xóa item khỏi túi đồ
    removeItem(itemId, 1);
    return true;
  };

  return (
    <InventoryContext.Provider value={{ inventory, addItem, removeItem, useItem }}>
      {children}
    </InventoryContext.Provider>
  );
};
