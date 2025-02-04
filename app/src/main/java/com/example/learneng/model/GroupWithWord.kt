package com.example.learneng.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class GroupWithWord(
    @Embedded val group: Group,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = WordGroupCrossRef::class,
            parentColumn = "groupId",
            entityColumn = "wordId"
        )
    )
    val words: List<Word>
)

