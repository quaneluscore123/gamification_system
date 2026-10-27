package com.lifequest.app.ui.navigation

import kotlinx.serialization.Serializable

/*
 * Type-safe routes (Navigation 2.8+): mỗi màn hình là một object/class @Serializable
 * thay vì chuỗi "quests" dễ gõ sai. Màn hình cần tham số thì dùng data class,
 * ví dụ: @Serializable data class QuestDetailRoute(val questId: String).
 */

@Serializable
data object DungeonsRoute

@Serializable
data object QuestsRoute

@Serializable
data object InventoryRoute

@Serializable
data object ShopRoute

@Serializable
data object StatsRoute

/** Các tab ở thanh điều hướng dưới cùng. */
enum class TopLevelDestination(val route: Any, val icon: String, val label: String) {
    DUNGEONS(DungeonsRoute, "🏰", "Dungeons"),
    QUESTS(QuestsRoute, "📜", "Quests"),
    INVENTORY(InventoryRoute, "🎒", "Hành Trang"),
    SHOP(ShopRoute, "🏪", "Cửa Hàng"),
    STATS(StatsRoute, "📊", "Thống kê"),
}
