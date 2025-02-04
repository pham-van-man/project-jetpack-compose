package com.example.learneng.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.learneng.model.Group
import com.example.learneng.model.Word
import com.example.learneng.model.WordGroupCrossRef
import com.example.learneng.repository.GroupDao
import com.example.learneng.repository.WordDao

@Database(
    entities = [Word::class, Group::class, WordGroupCrossRef::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun groupDao(): GroupDao
}