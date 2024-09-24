package com.michael.proverbs.fake

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FakeModel(
    @PrimaryKey
    val id: Long,
    val name: String,
)
