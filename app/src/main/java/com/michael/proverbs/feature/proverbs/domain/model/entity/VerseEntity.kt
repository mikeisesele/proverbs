package com.michael.proverbs.feature.proverbs.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verse")
data class VerseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val verseNumber: String,
    val verseText: String,
    val isFavorite: Boolean = false,
    val chapterNumber: String
)
