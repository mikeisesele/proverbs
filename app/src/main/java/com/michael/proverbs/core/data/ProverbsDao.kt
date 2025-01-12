package com.michael.proverbs.core.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.michael.proverbs.feature.proverbs.domain.model.entity.ChapterEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.IntroData
import com.michael.proverbs.feature.proverbs.domain.model.entity.ProverbsEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProverbsDao {


    // Insert a new entry (whole Proverbs book with chapters and verses)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntroProverbs(introData: IntroData)

    // Query to retrieve the intro proverbs (verses 1-5 of Chapter 1)
    @Query("SELECT * FROM intro_proverbs")
    suspend fun getIntroProverbs(): IntroData

    // Insert a new entry (whole Proverbs book with chapters and verses)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(proverbs: ChapterEntity) : Long

    // Get all chapters and their verses from the book of Proverbs
    @Query("SELECT * FROM chapters_table")
    suspend fun getAllChapters(): List<ChapterEntity>

    // Get a specific chapter and its verses by chapter number
    @Query("SELECT * FROM chapters_table WHERE :chapterNumber is chapter")
    suspend fun getChapterById(chapterNumber: String): ChapterEntity

    // Delete all entries (e.g., to reset the table)
    @Query("DELETE FROM chapters_table")
    suspend fun deleteAll()

    // delete a specific chapter
    @Query("DELETE FROM chapters_table WHERE :chapterNumber is chapter")
    suspend fun deleteChapter(chapterNumber: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerse(event: VerseEntity)

    // Query to retrieve all favorite verses
    @Query("SELECT * FROM verse WHERE isFavorite = 1")
    suspend fun getFavoriteVerses(): List<VerseEntity>
}
