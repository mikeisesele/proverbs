package com.michael.proverbs.feature.proverbs.domain.contract

import com.michael.proverbs.core.base.contract.BaseState
import com.michael.proverbs.core.base.model.ImmutableList
import com.michael.proverbs.core.base.model.MessageState
import com.michael.proverbs.core.base.model.emptyImmutableList
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.ProverbsModel
import com.michael.proverbs.feature.proverbs.domain.model.ScreenView
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.Verse
import com.michael.proverbs.feature.proverbs.domain.model.entity.IntroData
import com.michael.proverbs.feature.proverbs.domain.model.entity.ProverbsEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

data class ProverbsState(
    override val isLoading: Boolean, override val errorState: MessageState?,
    val proverbsEntity: ProverbsEntity?,
    val chapterList: ImmutableList<String>,
    val currentChapterVerses: ImmutableList<VerseEntity>,
    val selectedChapter: String?,
    val screenView: ScreenView,
    val currentRandomVerse: VerseEntity?,
    val introData: IntroData?
) : BaseState {
    companion object {
        val initialState = ProverbsState(
            isLoading = false, errorState = null,
            selectedChapter = null,
            proverbsEntity = null,
            chapterList = emptyImmutableList(),
            currentChapterVerses = emptyImmutableList(),
            screenView = ScreenView.CARD_VIEW,
            currentRandomVerse = null,
            introData = null
        )
    }
}
