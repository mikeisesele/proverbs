package com.michael.proverbs.feature.proverbs.presentation

import com.google.gson.Gson
import com.michael.easylog.logInlineNullable
import com.michael.proverbs.core.base.contract.BaseViewModel
import com.michael.proverbs.core.base.model.toImmutableList
import com.michael.proverbs.core.ui.extensions.collectBy
import com.michael.proverbs.feature.proverbs.data.ProverbsRepository
import com.michael.proverbs.feature.proverbs.domain.contract.ProverbsState
import com.michael.proverbs.feature.proverbs.domain.contract.ProverbsViewAction
import com.michael.proverbs.feature.proverbs.domain.getFirstSixVersesOfChapterOne
import com.michael.proverbs.feature.proverbs.domain.model.ScreenView
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.ProverbsModel
import com.michael.proverbs.feature.proverbs.domain.model.entity.ChapterEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.IntroData
import com.michael.proverbs.feature.proverbs.domain.model.entity.ProverbsEntity
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity
import com.michael.proverbs.feature.proverbs.domain.toProverbsEntity
import com.nb.benefitspro.core.base.providers.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProverbsViewModel @Inject constructor(
    private val gson: Gson, dispatcherProvider: DispatcherProvider,
    private val repository: ProverbsRepository
) : BaseViewModel<ProverbsState, ProverbsViewAction>(
    ProverbsState.initialState,
    dispatcherProvider
) {


    override fun onViewAction(viewAction: ProverbsViewAction) {
        when (viewAction) {
            is ProverbsViewAction.ProcessJsonString -> processJsonString(viewAction.jsonString)
            is ProverbsViewAction.SelectTab -> switchTab(viewAction.selectedTab)
            is ProverbsViewAction.ToggleFavorite -> toggleFavorite(viewAction.verse)
        }
    }


    private fun processJsonString(jsonString: String) {

        val myData = gson.fromJson(jsonString, ProverbsModel::class.java)

        launch {
            prepareData(myData.toProverbsEntity())
        }

    }

    private fun prepareData(proverb: ProverbsEntity) {
        launch {
            val introData = repository.getIntroProverb()

            repository.getProverbs()
                .collectBy(
                onEach = { savedProverbsEntity ->
                    savedProverbsEntity.logInlineNullable("savedProverbsEntity")
                    processProverbs(introData = introData, proverbs = proverb, savedProverbsEntity = savedProverbsEntity)
                }
            )
        }
    }

    private fun switchTab(screenView: ScreenView) {
        updateState {
            it.copy(
                screenView = screenView
            )
        }
    }

    private fun processProverbs(
        proverbs: ProverbsEntity,
        savedProverbsEntity: List<ChapterEntity?>,
        introData: IntroData?
    ) {
        if (savedProverbsEntity.isEmpty()) {
            launch {
                repository.addFirstSixVersesOfChapterOne(proverbs.getFirstSixVersesOfChapterOne())
                repository.addProverb(
                    proverbs.copy(
                        chapters = proverbs.chapters.map { chapter ->
                            if (chapter.chapter == "1") {
                                // If it's chapter 1, drop the first 6 verses
                                chapter.copy(verses = chapter.verses.drop(6))
                            } else {
                                // Leave other chapters as they are
                                chapter
                            }
                        }
                    )
                )
                updateState {
                    it.copy(
                        chapterList = proverbs.chapters.map { chapter -> chapter.chapter }.toImmutableList(),
                        currentRandomVerse = proverbs.chapters.random().verses.random(),
                        introData = introData
                    )
                }
            }
        } else {
            prepareProverbsForTheDay(savedProverbsEntity, introData!!)
        }
    }


    private fun prepareProverbsForTheDay(proverbs: List<ChapterEntity?>, introData: IntroData) {
        updateState {
            it.copy(
                chapterList = proverbs.map { chapter -> chapter?.chapter.orEmpty() }
                    .toImmutableList(),
                currentRandomVerse = proverbs.random()?.verses?.random(),
                introData = introData
            )
        }
    }

    private fun toggleFavorite(verseEntity: VerseEntity) {
        launch {
            val updatedEntity = verseEntity.copy(isFavorite = !verseEntity.isFavorite)
            repository.toggleFavorite(updatedEntity)
            updateState {
                it.copy(
                    currentRandomVerse = updatedEntity
                )
            }
        }
    }
}