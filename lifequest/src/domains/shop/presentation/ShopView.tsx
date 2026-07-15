import { useContext } from 'react';
import { ShopContext } from '../application/ShopProvider';
import { PlayerContext } from '../../player/application/PlayerProvider';
import './ShopView.css';

export default function ShopView() {
  const { availableItems, buyItem } = useContext(ShopContext);
  const { player } = useContext(PlayerContext);

  const handleBuy = (itemId: string, price: number) => {
    if (player.gold < price) {
        alert('Bạn không đủ Vàng!');
        return;
    }
    const success = buyItem(itemId);
    if (success) {
        alert('Mua vật phẩm thành công! Đã chuyển vào Inventory.');
    }
  };

  return (
    <div className="shop-view">
      <div className="shop-header">
        <h3>Cửa Hàng (Shop)</h3>
        <p className="gold-display">Your Gold: <strong>{player.gold} 💰</strong></p>
      </div>

      <div className="shop-items-grid">
        {availableItems.map(item => (
          <div key={item.id} className="shop-item-card">
            <div className="item-icon">{item.icon}</div>
            <div className="item-details">
              <h4>{item.name}</h4>
              <p>{item.description}</p>
            </div>
            <button 
              className="btn-buy" 
              onClick={() => handleBuy(item.id, item.price)}
              disabled={player.gold < item.price}
            >
              {item.price} 💰 Mua
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
