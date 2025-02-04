package com.example.learneng.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val key: String,
    val ipa: String,
    val value: String,
    val type: String
)