package com.michael.proverbs.core.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.Chapter
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.Verse
import com.michael.proverbs.feature.proverbs.domain.model.entity.ChapterEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

class ProverbsTypeConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromChapterList(value: List<ChapterEntity>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toChapterList(value: String): List<ChapterEntity> {
        val listType = object : TypeToken<List<ChapterEntity>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromVerseList(value: List<VerseEntity>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toVerseList(value: String): List<VerseEntity> {
        val listType = object : TypeToken<List<VerseEntity>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return gson.toJson(list)
    }
}
