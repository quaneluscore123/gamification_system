package com.lifequest.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Khi đổi cấu trúc bảng: tăng `version` và viết Migration (hoặc AutoMigration),
 * nếu không app của người dùng cũ sẽ crash khi mở database.
 * exportSchema = true lưu file JSON mô tả schema vào app/schemas để so sánh giữa các version.
 */
@Database(entities = [QuestLogEntity::class], version = 1, exportSchema = true)
abstract class LifeQuestDatabase : RoomDatabase() {
    abstract fun questLogDao(): QuestLogDao

    companion object {
        const val NAME = "lifequest.db"
    }
}
