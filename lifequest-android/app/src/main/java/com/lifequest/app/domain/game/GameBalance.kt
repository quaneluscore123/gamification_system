package com.lifequest.app.domain.game

object GameBalance {
    // Player
    const val BASE_XP_REQUIREMENT = 100 // XP cần để lên Level 2
    const val XP_MULTIPLIER_PER_LEVEL = 1.5 // Mỗi level khó hơn bao nhiêu (Level 3 cần 150)

    // Dev console
    const val CHEAT_XP = 100
    const val CHEAT_GOLD = 1000
}
