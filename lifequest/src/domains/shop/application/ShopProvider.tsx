import { createContext, useContext, type ReactNode } from 'react';
import { type ItemDef, shopItems } from '../domain/models';
import { PlayerContext } from '../../player/application/PlayerProvider';
import { InventoryContext } from '../../inventory/application/InventoryProvider';

export interface ShopContextState {
  availableItems: ItemDef[];
  buyItem: (itemId: string) => boolean;
}

export const ShopContext = createContext<ShopContextState>({} as ShopContextState);

export const ShopProvider = ({ children }: { children: ReactNode }) => {
  const { spendGold } = useContext(PlayerContext);
  const { addItem } = useContext(InventoryContext);

  const availableItems = shopItems;

  const buyItem = (itemId: string): boolean => {
    const itemDef = availableItems.find(i => i.id === itemId);
    if (!itemDef) return false;

    // Trừ gold
    const success = spendGold(itemDef.price);
    if (success) {
      // Thêm vào túi đồ
      addItem(itemId, 1);
      return true;
    }
    return false;
  };

  return (
    <ShopContext.Provider value={{ availableItems, buyItem }}>
      {children}
    </ShopContext.Provider>
  );
};
