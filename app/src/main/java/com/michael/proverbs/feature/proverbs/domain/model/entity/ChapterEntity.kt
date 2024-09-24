package com.michael.proverbs.feature.proverbs.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapters_table")
data class ChapterEntity(

    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    val chapter: String,
    val verses: List<VerseEntity>
)
