export interface InventoryItem {
  itemId: string;
  quantity: number;
}

export interface InventoryProgress {
  items: InventoryItem[];
}
