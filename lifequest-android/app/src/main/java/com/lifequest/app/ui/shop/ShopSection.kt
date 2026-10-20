package com.lifequest.app.ui.shop

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.domain.shop.ShopCatalog
import com.lifequest.app.ui.components.ActionRow
import com.lifequest.app.ui.components.SectionTitle

fun LazyListScope.shopSection(game: GameState, onBuy: (itemId: String) -> Unit) {
    item(key = "shop_title") { SectionTitle("🏪 Cửa Hàng — Vàng của bạn: ${game.player.gold} 💰") }

    items(ShopCatalog.items, key = { it.id }) { item ->
        ActionRow(
            icon = item.icon,
            title = item.name,
            detail = item.description,
            subtitle = "${item.price} 💰",
            actionLabel = "Mua",
            enabled = game.player.gold >= item.price,
            onAction = { onBuy(item.id) },
        )
    }
}
