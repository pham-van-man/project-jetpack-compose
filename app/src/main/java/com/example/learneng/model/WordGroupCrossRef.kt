package com.example.learneng.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["wordId", "groupId"],
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = ["id"],
            childColumns = ["wordId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Group::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WordGroupCrossRef(
    val wordId: Long,
    val groupId: Long
)