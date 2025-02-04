package com.example.learneng.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.learneng.model.Word
import com.example.learneng.model.WordGroupCrossRef

@Dao
interface WordDao {
    @Insert
    suspend fun insert(word: Word): Long

    @Query("SELECT * FROM word_table WHERE `key` LIKE :q OR ipa LIKE :q OR value LIKE :q OR type LIKE :q ORDER BY id DESC")
    suspend fun findWords(q: String = "%%"): List<Word>

    @Query("SELECT * FROM word_table WHERE id = :wordId")
    suspend fun finWordById(wordId: Long): Word?

    @Delete
    suspend fun delete(word: Word)

    @Update
    suspend fun update(word: Word)

    @Insert
    suspend fun insertGroup(word: WordGroupCrossRef): Long
}