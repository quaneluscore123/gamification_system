package com.lifequest.app.domain.world

object WorldCatalog {

    val worlds = listOf(
        WorldDef(
            id = "world_1",
            name = "Java Fundamentals",
            description = "The beginning of the journey.",
            requiredLevel = 1,
            boss = BossDef("boss_1", "OOP Master", 500),
            rooms = listOf(
                RoomDef("w1_r1", "Variables & Data Types", "q_w1_r1"),
                RoomDef("w1_r2", "Loops & Conditions", "q_w1_r2"),
                RoomDef("w1_r3", "Functions & Methods", "q_w1_r3"),
                RoomDef("w1_r4", "Classes & Objects", "q_w1_r4"),
                RoomDef("w1_r5", "Inheritance & Polymorphism", "q_w1_r5"),
            ),
        ),
        WorldDef(
            id = "world_2",
            name = "Database & SQL",
            description = "Learn how to store data permanently.",
            requiredLevel = 5,
            boss = BossDef("boss_2", "Database Design", 800),
            rooms = listOf(
                RoomDef("w2_r1", "SELECT, INSERT, UPDATE, DELETE", "q_w2_r1"),
                RoomDef("w2_r2", "JOINs (INNER, LEFT, RIGHT)", "q_w2_r2"),
                RoomDef("w2_r3", "Indexes & Performance", "q_w2_r3"),
                RoomDef("w2_r4", "Database Normalization", "q_w2_r4"),
            ),
        ),
        WorldDef(
            id = "world_3",
            name = "Spring Boot Core",
            description = "The heart of Java Backend.",
            requiredLevel = 10,
            boss = BossDef("boss_3", "CRUD API", 1000),
            rooms = listOf(
                RoomDef("w3_r1", "Project Setup & IOC Container", "q_w3_r1"),
                RoomDef("w3_r2", "Create API (POST)", "q_w3_r2"),
                RoomDef("w3_r3", "Read API (GET)", "q_w3_r3"),
                RoomDef("w3_r4", "Update & Delete API", "q_w3_r4"),
                RoomDef("w3_r5", "Unit Testing", "q_w3_r5"),
            ),
        ),
    )

    fun initialProgress(world: WorldDef) = WorldProgress(
        worldId = world.id,
        bossCurrentHp = world.boss.maxHp,
        isCleared = false,
    )
}
