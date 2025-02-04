package com.example.learneng.db

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class DatabaseBuilder(private val context: Context) {
    private var instance: AppDatabase? = null
    private val migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS `group_table`")
            db.execSQL("CREATE TABLE `group_table` (`name` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
            db.execSQL("DROP TABLE IF EXISTS `WordGroupCrossRef`")
            db.execSQL(
                """
            CREATE TABLE `WordGroupCrossRef` (
                `wordId` INTEGER NOT NULL,
                `groupId` INTEGER NOT NULL,
                PRIMARY KEY(`wordId`, `groupId`),
                FOREIGN KEY(`groupId`) REFERENCES `group_table`(`id`) ON DELETE CASCADE,
                FOREIGN KEY(`wordId`) REFERENCES `word_table`(`id`) ON DELETE CASCADE
            )
            """
            )
        }
    }

    fun getDatabase(): AppDatabase {
        return instance ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).addMigrations(migration).build()
            this.instance = instance
            instance
        }
    }
}