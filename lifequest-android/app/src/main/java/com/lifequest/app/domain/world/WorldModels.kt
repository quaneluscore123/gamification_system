package com.lifequest.app.domain.world

import kotlinx.serialization.Serializable

data class BossDef(
    val id: String,
    val name: String,
    val maxHp: Int,
)

data class RoomDef(
    val id: String,
    val name: String,
    val questId: String,
)

data class WorldDef(
    val id: String,
    val name: String,
    val description: String,
    val requiredLevel: Int,
    val boss: BossDef,
    val rooms: List<RoomDef>,
)

@Serializable
data class WorldProgress(
    val worldId: String,
    val bossCurrentHp: Int,
    val isCleared: Boolean,
)
