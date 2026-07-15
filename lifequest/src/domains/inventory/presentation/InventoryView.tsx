import { useContext } from 'react';
import { InventoryContext } from '../application/InventoryProvider';
import { shopItems } from '../../shop/domain/models';
import './InventoryView.css';

export default function InventoryView() {
  const { inventory, useItem } = useContext(InventoryContext);

  const handleUseItem = (itemId: string, name: string) => {
    const success = useItem(itemId);
    if (success) {
      alert(`Đã sử dụng ${name} thành công! Hiệu ứng đã được kích hoạt.`);
    } else {
      alert(`Không thể sử dụng ${name} lúc này.`);
    }
  };

  if (inventory.items.length === 0) {
    return (
      <div className="inventory-view empty">
        <p>🎒 Túi đồ của bạn đang trống.</p>
        <p style={{fontSize: '0.9rem', color: 'var(--text-muted)'}}>Hãy làm Quest kiếm Gold và vào Shop để mua sắm nhé!</p>
      </div>
    );
  }

  return (
    <div className="inventory-view">
      <h3>🎒 Hành Trang (Inventory)</h3>
      <div className="inventory-grid">
        {inventory.items.map(invItem => {
          const def = shopItems.find(i => i.id === invItem.itemId);
          if (!def) return null;

          return (
            <div key={invItem.itemId} className="inventory-item-card">
              <div className="item-icon-large">{def.icon}</div>
              <div className="item-info-col">
                <h4>{def.name}</h4>
                <p>Số lượng: <strong style={{color: '#fff'}}>{invItem.quantity}</strong></p>
                <p className="item-effect-desc">{def.description}</p>
              </div>
              <button className="btn-use" onClick={() => handleUseItem(def.id, def.name)}>
                Sử dụng
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
}
