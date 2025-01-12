package com.michael.proverbs.feature.proverbs.domain

import com.michael.proverbs.core.common.sentenceCase
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.Chapter
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.ProverbsModel
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.Verse
import com.michael.proverbs.feature.proverbs.domain.model.entity.ChapterEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.IntroData
import com.michael.proverbs.feature.proverbs.domain.model.entity.ProverbsEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

fun ProverbsModel.toProverbsEntity(): ProverbsEntity {

    val chapters = chapters.map { it.toChapterEntity() }

    return ProverbsEntity(
        book = book,
        chapters = chapters,
    )
}

fun Chapter.toChapterEntity(): ChapterEntity {
    val verses = verses.map { it.toVerseEntity(this.chapter) }
    return ChapterEntity(
        chapter = chapter,
        verses = verses
    )
}

fun Verse.toVerseEntity(chapter: String): VerseEntity =
    VerseEntity(
        verseText = text.sentenceCase(),
        verseNumber = verse,
        chapterNumber = chapter
    )


fun ProverbsEntity.getFirstSixVersesOfChapterOne(): IntroData {
    // Find chapter one
    val chapterOne = chapters.find { it.chapter == "1" }

    // Return the first six verses, or an empty list if chapter is not found or has less than 6 verses
    return IntroData(
        title = listOf("Proverbs, Chapter One, verse 1 - 6"),
        description = chapterOne?. verses?.take(6)?.map { it.verseText } ?: emptyList(),
        image = 0
    )
}