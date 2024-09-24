package com.michael.proverbs.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.ProverbsModel
import com.michael.proverbs.feature.proverbs.domain.model.entity.ChapterEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.IntroData
import com.michael.proverbs.feature.proverbs.domain.model.entity.ProverbsEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

const val DB_NAME = "proverbs_database"

@Database(entities = [ProverbsEntity::class, VerseEntity::class, ChapterEntity::class, IntroData::class], version = 2, exportSchema = false)
@TypeConverters(ProverbsTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun proverbsDao(): ProverbsDao

    companion object {
        const val DATABASE_NAME = DB_NAME
    }
}
