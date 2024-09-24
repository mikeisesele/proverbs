package com.michael.proverbs.feature.proverbs.data

import com.michael.easylog.logInline
import com.michael.proverbs.core.data.ProverbsDao
import com.michael.proverbs.core.ui.extensions.asFlow
import com.michael.proverbs.feature.proverbs.domain.model.entity.ChapterEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.IntroData
import com.michael.proverbs.feature.proverbs.domain.model.entity.ProverbsEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class ProverbsRepository @Inject constructor(
    private val proverbDao: ProverbsDao
) {
    private val favoriteMutex = Mutex()


    suspend fun getProverbs(): Flow<List<ChapterEntity?>> = proverbDao.getAllChapters().asFlow()

    suspend fun getProverb(chapter: String) = proverbDao.getChapterById(chapter).asFlow()

    suspend fun getIntroProverb() = proverbDao.getIntroProverbs()
    suspend fun addFirstSixVersesOfChapterOne(introData: IntroData) =
        proverbDao.insertIntroProverbs(introData).asFlow()

    suspend fun addProverb(proverb: ProverbsEntity) {
        proverb.chapters.forEach {
          proverbDao.insertChapter(it)
        }
    }

    suspend fun toggleFavorite(verseEntity: VerseEntity) {

        favoriteMutex.withLock {

            val chapterEntity = proverbDao.getChapterById(verseEntity.chapterNumber)

            proverbDao.getAllChapters().first().let { proverbsEntity ->
                chapterEntity.verses.map { verse ->
                    // Update the verses in the current chapter
                    if (verse.verseNumber == verseEntity.verseNumber && verse.chapterNumber == verseEntity.chapterNumber) {
                        verseEntity // Toggle favorite status
                    } else {
                        verse
                    }
                }.let { updatedVerses ->
                    // Return a new ProverbsEntity with the updated chapters
                    proverbDao.insertChapter(proverbsEntity.copy(verses = updatedVerses))

                }
            }
            proverbDao.insertVerse(verseEntity)
        }
    }
}
