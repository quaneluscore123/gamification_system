package com.lifequest.app.domain.inventory

import kotlinx.serialization.Serializable

@Serializable
data class InventoryItem(
    val itemId: String,
    val quantity: Int,
)

object InventoryRules {

    fun add(items: List<InventoryItem>, itemId: String, quantity: Int = 1): List<InventoryItem> =
        if (items.any { it.itemId == itemId }) {
            items.map { if (it.itemId == itemId) it.copy(quantity = it.quantity + quantity) else it }
        } else {
            items + InventoryItem(itemId, quantity)
        }

    fun remove(items: List<InventoryItem>, itemId: String, quantity: Int = 1): List<InventoryItem> =
        items.mapNotNull {
            when {
                it.itemId != itemId -> it
                it.quantity - quantity <= 0 -> null
                else -> it.copy(quantity = it.quantity - quantity)
            }
        }

    fun has(items: List<InventoryItem>, itemId: String, quantity: Int = 1): Boolean =
        (items.find { it.itemId == itemId }?.quantity ?: 0) >= quantity
}
