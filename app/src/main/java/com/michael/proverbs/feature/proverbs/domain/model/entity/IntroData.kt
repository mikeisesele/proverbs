package com.michael.proverbs.feature.proverbs.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "intro_proverbs")
data class IntroData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: List<String>,
    val description: List<String>,
    val image: Int = 0
)
