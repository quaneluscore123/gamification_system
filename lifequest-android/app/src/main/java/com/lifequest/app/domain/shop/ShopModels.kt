package com.lifequest.app.domain.shop

enum class ItemEffectType { GRANT_XP, DAMAGE_BOSS, GRANT_GOLD }

data class ItemEffect(
    val type: ItemEffectType,
    val value: Int,
)

data class ItemDef(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val effect: ItemEffect,
    val icon: String,
)

object ShopCatalog {

    val items = listOf(
        ItemDef(
            id = "itm_potion_xp",
            name = "Focus Potion",
            description = "Tăng sự tập trung, nhận ngay 100 XP.",
            price = 50,
            effect = ItemEffect(ItemEffectType.GRANT_XP, 100),
            icon = "🧪",
        ),
        ItemDef(
            id = "itm_sword_clean_code",
            name = "Clean Code Sword",
            description = "Gây 200 Sát thương thẳng vào Boss hiện tại.",
            price = 150,
            effect = ItemEffect(ItemEffectType.DAMAGE_BOSS, 200),
            icon = "🗡️",
        ),
        ItemDef(
            id = "itm_lucky_coin",
            name = "Lucky Coin",
            description = "Nhận ngẫu nhiên 300 Vàng (Mở ra lãi hoặc lỗ).",
            price = 200,
            effect = ItemEffect(ItemEffectType.GRANT_GOLD, 300),
            icon = "🪙",
        ),
    )

    private val byId = items.associateBy { it.id }

    fun find(itemId: String): ItemDef? = byId[itemId]
}
