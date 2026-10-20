package com.lifequest.app.ui.inventory

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.domain.shop.ShopCatalog
import com.lifequest.app.ui.components.ActionRow
import com.lifequest.app.ui.components.GameCard
import com.lifequest.app.ui.components.SectionTitle
import com.lifequest.app.ui.theme.LqColors

fun LazyListScope.inventorySection(game: GameState, onUse: (itemId: String) -> Unit) {
    item(key = "inventory_title") { SectionTitle("🎒 Hành Trang (Inventory)") }

    if (game.inventory.isEmpty()) {
        item(key = "inventory_empty") {
            GameCard {
                Text("🎒 Túi đồ của bạn đang trống.", style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Hãy làm Quest kiếm Gold và vào Shop để mua sắm nhé!",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = LqColors.TextMuted,
                )
            }
        }
        return
    }

    items(game.inventory, key = { it.itemId }) { invItem ->
        val def = ShopCatalog.find(invItem.itemId) ?: return@items
        ActionRow(
            icon = def.icon,
            title = def.name,
            detail = def.description,
            subtitle = "Số lượng: ${invItem.quantity}",
            actionLabel = "Sử dụng",
            enabled = true,
            onAction = { onUse(def.id) },
        )
    }
}
