package com.michael.template.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.michael.template.fake.FakeModel

const val DB_NAME = "app_database"

@Database(entities = [FakeModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = DB_NAME
    }
}
