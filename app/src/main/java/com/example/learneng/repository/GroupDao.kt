package com.example.learneng.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.learneng.model.Group
import com.example.learneng.model.GroupWithWord

@Dao
interface GroupDao {
    @Insert
    suspend fun insert(group: Group): Long

    @Query("SELECT * FROM group_table WHERE name LIKE :q ORDER BY id DESC")
    suspend fun findGroups(q: String = "%%"): List<GroupWithWord>

    @Query("SELECT * FROM group_table WHERE id = :groupId")
    suspend fun findGroupById(groupId: Long): Group?

    @Delete
    suspend fun delete(group: Group)

    @Update
    suspend fun update(group: Group)
}