package com.michael.proverbs.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.michael.proverbs.feature.proverbs.model.ProverbsModel

const val DB_NAME = "proverbs_database"

@Database(entities = [ProverbsModel::class], version = 1, exportSchema = false)
@TypeConverters(ProverbsTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = DB_NAME
    }
}
