import { type InventoryProgress } from '../domain/models';

export class InventoryService {
  
  addItem(progress: InventoryProgress, itemId: string, quantity: number = 1): InventoryProgress {
    const items = [...progress.items];
    const existingIndex = items.findIndex(i => i.itemId === itemId);
    
    if (existingIndex >= 0) {
      items[existingIndex] = { ...items[existingIndex], quantity: items[existingIndex].quantity + quantity };
    } else {
      items.push({ itemId, quantity });
    }
    
    return { ...progress, items };
  }

  removeItem(progress: InventoryProgress, itemId: string, quantity: number = 1): InventoryProgress {
    let items = [...progress.items];
    const existingIndex = items.findIndex(i => i.itemId === itemId);
    
    if (existingIndex >= 0) {
      const newQuantity = items[existingIndex].quantity - quantity;
      if (newQuantity <= 0) {
        items = items.filter(i => i.itemId !== itemId);
      } else {
        items[existingIndex] = { ...items[existingIndex], quantity: newQuantity };
      }
    }
    
    return { ...progress, items };
  }

  hasItem(progress: InventoryProgress, itemId: string, quantity: number = 1): boolean {
    const item = progress.items.find(i => i.itemId === itemId);
    return item ? item.quantity >= quantity : false;
  }
}

export const inventoryService = new InventoryService();
