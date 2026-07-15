export type ItemEffectType = 'GRANT_XP' | 'DAMAGE_BOSS' | 'GRANT_GOLD';

export interface ItemEffect {
  type: ItemEffectType;
  value: number;
}

export interface ItemDef {
  id: string;
  name: string;
  description: string;
  price: number;
  effect: ItemEffect;
  icon: string;
}

// Data giả lập cửa hàng (Seed Data)
export const shopItems: ItemDef[] = [
  {
    id: "itm_potion_xp",
    name: "Focus Potion",
    description: "Tăng sự tập trung, nhận ngay 100 XP.",
    price: 50,
    effect: { type: 'GRANT_XP', value: 100 },
    icon: "🧪"
  },
  {
    id: "itm_sword_clean_code",
    name: "Clean Code Sword",
    description: "Gây 200 Sát thương thẳng vào Boss hiện tại.",
    price: 150,
    effect: { type: 'DAMAGE_BOSS', value: 200 },
    icon: "🗡️"
  },
  {
    id: "itm_lucky_coin",
    name: "Lucky Coin",
    description: "Nhận ngẫu nhiên 300 Vàng (Mở ra lãi hoặc lỗ).",
    price: 200,
    effect: { type: 'GRANT_GOLD', value: 300 },
    icon: "🪙"
  }
];
