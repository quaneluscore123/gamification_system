package com.lifequest.app.domain.quest

import com.lifequest.app.domain.quest.Difficulty.EASY
import com.lifequest.app.domain.quest.Difficulty.HARD
import com.lifequest.app.domain.quest.Difficulty.MEDIUM
import com.lifequest.app.domain.quest.QuestType.DAILY
import com.lifequest.app.domain.quest.QuestType.MAIN
import com.lifequest.app.domain.quest.QuestType.WEEKLY

object QuestCatalog {

    val daily = listOf(
        QuestDef("dq_1", "Write 50 lines of code", DAILY, EASY, 30, listOf("coding", "habit"), QuestRewards(50, 10, 10)),
        QuestDef("dq_2", "Read 1 technical article", DAILY, EASY, 15, listOf("learning"), QuestRewards(30, 5, 0)),
        QuestDef("dq_3", "Review Flashcards / Concepts", DAILY, MEDIUM, 20, listOf("memory"), QuestRewards(50, 15, 20)),
    )

    val weekly = listOf(
        QuestDef("wq_1", "Complete a Mini-Project", WEEKLY, HARD, 180, listOf("project", "milestone"), QuestRewards(300, 100, 150)),
        QuestDef("wq_2", "Learn a new Framework/Library", WEEKLY, HARD, 120, listOf("learning"), QuestRewards(200, 80, 100)),
    )

    // Main quest gắn với các phòng trong World
    val main = listOf(
        // World 1
        QuestDef("q_w1_r1", "Variables & Data Types", MAIN, EASY, 60, listOf("java", "core"), QuestRewards(50, 20, 100)),
        QuestDef("q_w1_r2", "Loops & Conditions", MAIN, EASY, 60, listOf("java", "core"), QuestRewards(50, 20, 100)),
        QuestDef("q_w1_r3", "Functions & Methods", MAIN, MEDIUM, 90, listOf("java", "core"), QuestRewards(50, 20, 100)),
        QuestDef("q_w1_r4", "Classes & Objects", MAIN, MEDIUM, 120, listOf("java", "oop"), QuestRewards(50, 20, 100)),
        QuestDef("q_w1_r5", "Inheritance & Polymorphism", MAIN, HARD, 150, listOf("java", "oop"), QuestRewards(100, 50, 100)),

        // World 2
        QuestDef("q_w2_r1", "SELECT, INSERT, UPDATE, DELETE", MAIN, EASY, 60, listOf("sql"), QuestRewards(60, 30, 200)),
        QuestDef("q_w2_r2", "JOINs (INNER, LEFT, RIGHT)", MAIN, MEDIUM, 90, listOf("sql"), QuestRewards(80, 40, 200)),
        QuestDef("q_w2_r3", "Indexes & Performance", MAIN, HARD, 120, listOf("sql"), QuestRewards(80, 40, 200)),
        QuestDef("q_w2_r4", "Database Normalization", MAIN, HARD, 180, listOf("sql", "design"), QuestRewards(100, 50, 200)),

        // World 3
        QuestDef("q_w3_r1", "Project Setup & IOC Container", MAIN, MEDIUM, 120, listOf("spring"), QuestRewards(80, 40, 200)),
        QuestDef("q_w3_r2", "Create API (POST)", MAIN, MEDIUM, 90, listOf("spring", "api"), QuestRewards(80, 40, 200)),
        QuestDef("q_w3_r3", "Read API (GET)", MAIN, MEDIUM, 90, listOf("spring", "api"), QuestRewards(80, 40, 200)),
        QuestDef("q_w3_r4", "Update & Delete API", MAIN, MEDIUM, 90, listOf("spring", "api"), QuestRewards(80, 40, 200)),
        QuestDef("q_w3_r5", "Unit Testing", MAIN, HARD, 150, listOf("spring", "test"), QuestRewards(150, 100, 200)),

        // World 4
        QuestDef("q_w4_r1", "Dockerfile Basics", MAIN, EASY, 120, listOf("docker"), QuestRewards(80, 40, 300)),
        QuestDef("q_w4_r2", "Docker Compose", MAIN, MEDIUM, 90, listOf("docker"), QuestRewards(80, 40, 300)),
        QuestDef("q_w4_r3", "CI with GitHub Actions", MAIN, HARD, 90, listOf("docker", "deploy"), QuestRewards(80, 40, 300)),
        QuestDef("q_w4_r4", "Deploy to Cloud", MAIN, HARD, 90, listOf("docker", "deploy"), QuestRewards(80, 40, 300)),
    )

    val all = daily + weekly + main

    private val byId = all.associateBy { it.id }

    fun find(questId: String): QuestDef? = byId[questId]
}
