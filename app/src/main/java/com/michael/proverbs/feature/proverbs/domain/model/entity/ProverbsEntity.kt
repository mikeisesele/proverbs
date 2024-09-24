package com.michael.proverbs.feature.proverbs.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proverbs_table")
data class ProverbsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val book: String,
    val chapters: List<ChapterEntity>,
    val hasFavorite: Boolean = false
)
