package com.example.learneng.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WordWithGroup(
    @Embedded val word: Word,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = WordGroupCrossRef::class,
            parentColumn = "wordId",
            entityColumn = "groupId"
        )
    )
    val groups: List<Group>
)

