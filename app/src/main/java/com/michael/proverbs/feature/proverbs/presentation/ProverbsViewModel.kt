package com.michael.proverbs.feature.proverbs.presentation

import com.google.gson.Gson
import com.michael.easylog.logInline
import com.michael.proverbs.core.base.contract.BaseViewModel
import com.michael.proverbs.core.base.model.ImmutableList
import com.michael.proverbs.core.base.model.orEmpty
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
    private val gson: Gson,
    dispatcherProvider: DispatcherProvider,
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
            is ProverbsViewAction.RefreshVerse -> getRandomVerse()
            is ProverbsViewAction.NextVerse -> getNextVerse()
            is ProverbsViewAction.PreviousVerse -> getPreviousVerse()
            is ProverbsViewAction.GetFavoriteVerses -> getFavoriteVerses()
            is ProverbsViewAction.SelectChapter -> processChapterClicked(viewAction.chapter)
            is ProverbsViewAction.SearchQuery -> searchProverb(viewAction.query.trim())
            is ProverbsViewAction.SearchFavouriteQuery -> searchFavouriteProverb(viewAction.query.trim())
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

            repository.getChapters()
                .collectBy(
                    onEach = { chaptersEntity ->
                        processProverbs(
                            introData = introData,
                            proverbs = proverb,
                            chaptersEntity = chaptersEntity.toImmutableList()
                        )
                    }
                )
        }
    }

    private fun searchProverb(query: String) {
        val filteredVerses = currentState.chaptersEntity.flatMap { chapter ->
            chapter?.verses.orEmpty().filter { verse ->
                verse.verseText.contains(query, ignoreCase = true)
            }
        }

        val searched = if (query.isNotEmpty()) {
            filteredVerses
        } else {
            emptyList()
        }.toImmutableList()

        updateState {
            it.copy(
                searchedProverbs = searched
            )
        }
    }

     private fun searchFavouriteProverb(query: String) {
        val filteredVerses = currentState.favouriteVerses.filter { verse ->
            verse.verseText.contains(query, ignoreCase = true)
        }

        val searched = if (query.isNotEmpty()) {
            filteredVerses
        } else {
            emptyList()
        }.toImmutableList()

        updateState {
            it.copy(
                searchedFavouriteProverbs = searched
            )
        }
    }

    private fun switchTab(screenView: ScreenView) {
        updateState {
            it.copy(screenView = screenView)
        }
    }

    private fun processProverbs(
        proverbs: ProverbsEntity,
        chaptersEntity: ImmutableList<ChapterEntity?>,
        introData: IntroData?
    ) {
        if (chaptersEntity.isEmpty()) {
            launch {
                repository.addFirstSixVersesOfChapterOne(proverbs.getFirstSixVersesOfChapterOne())

                val mappedProverbs = proverbs.copy(
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

                repository.addProverb(mappedProverbs)
                updateState {
                    it.copy(
                        currentChapterVerses = proverbs.chapters.first().verses.toImmutableList(),
                        chaptersEntity = chaptersEntity,
                        chapterList = proverbs.chapters.map { chapter -> chapter.chapter }
                            .toImmutableList(),
                        currentRandomVerse = proverbs.chapters.random().verses.random(),
                        introData = introData,
                        favouriteVerses = proverbs.chapters.first().verses.filter { it.isFavorite }
                            .toImmutableList()
                    )
                }
            }
        } else {
            val currentChapterVerses = proverbs.chapters.first().verses.toImmutableList()
            prepareProverbsForTheDay(chaptersEntity, introData!!, currentChapterVerses)
        }
    }

    private fun prepareProverbsForTheDay(
        proverbs: ImmutableList<ChapterEntity?>,
        introData: IntroData,
        currentChapterVerses: ImmutableList<VerseEntity>
    ) {
        updateState {
            it.copy(
                currentChapterVerses = currentChapterVerses,
                chaptersEntity = proverbs,
                chapterList = proverbs
                    .map { chapter -> chapter?.chapter.orEmpty() }
                    .sortedBy { it.toInt() }.toImmutableList(),
                currentRandomVerse = proverbs.random()?.verses?.random(),
                introData = introData,
                favouriteVerses = currentChapterVerses.filter { it.isFavorite }.toImmutableList()
            )
        }
    }

    private fun toggleFavorite(verseEntity: VerseEntity) {

        launch {
            val updatedEntity = verseEntity.copy(
                isFavorite = !verseEntity.isFavorite
            )

            val updatedCurrentChapterVerses = currentState.currentChapterVerses.map { verse ->
                if (verse.verseNumber == updatedEntity.verseNumber) {
                    updatedEntity
                } else {
                    verse
                }
            }.toImmutableList()

            repository.toggleFavorite(updatedEntity)
            repository.getFavoriteVerses().collectBy(
                onEach = { favouriteVerses ->
                    updateState {
                        it.copy(
                            favouriteVerses = favouriteVerses.toImmutableList(),
                            currentChapterVerses = updatedCurrentChapterVerses,
                            currentRandomVerse = updatedEntity
                        )
                    }
                }
            )
        }
    }

    private fun getRandomVerse() {
        launch {
            val randomVerse = currentState.chaptersEntity.random()?.verses?.random()
            updateState {
                it.copy(
                    currentRandomVerse = randomVerse
                )
            }
        }
    }

    private fun getNextVerse() {
        launch {
            val chapter = currentState.chaptersEntity.find { it?.chapter == currentState.currentRandomVerse?.chapterNumber }
            val chapterEntity = currentState.chaptersEntity.find { it?.chapter == chapter?.chapter }

            val verseIndex = chapterEntity?.verses?.indexOf(currentState.currentRandomVerse) ?: -1
            val nextVerse = if (verseIndex < (chapterEntity?.verses?.size?.minus(1) ?: 0)) {
                chapterEntity?.verses?.get(verseIndex + 1)
            } else {
                chapterEntity?.verses?.first()
            }
            updateState {
                it.copy(
                    currentRandomVerse = nextVerse
                )
            }
        }
    }

    private fun getPreviousVerse() {
        launch {
            val chapter =
                currentState.chaptersEntity.find { it?.chapter == currentState.currentRandomVerse?.chapterNumber }
            val chapterEntity = currentState.chaptersEntity.find { it?.chapter == chapter?.chapter }
            val verseIndex = chapterEntity?.verses?.indexOf(currentState.currentRandomVerse) ?: -1

            val previousVerse = if (verseIndex > 0) {
                chapterEntity?.verses?.get(verseIndex - 1)
            } else {
                chapterEntity?.verses?.last()
            }

            updateState {
                it.copy(
                    currentRandomVerse = previousVerse
                )
            }
        }
    }

    private fun processChapterClicked(chapter: String) {
        launch {
            val chapterEntity = currentState.chaptersEntity.find { it?.chapter == chapter }
            updateState {
                it.copy(
                    currentChapterVerses = chapterEntity?.verses?.toImmutableList().orEmpty(),
                    selectedChapter = chapter
                )
            }
        }
    }

    private fun getFavoriteVerses() {
        launch {
            repository.getFavoriteVerses().collectBy(
                onEach = { favouriteVerses ->
                    updateState {
                        it.copy(
                            favouriteVerses = favouriteVerses.toImmutableList(),
                        )
                    }
                }
            )
        }
    }
}