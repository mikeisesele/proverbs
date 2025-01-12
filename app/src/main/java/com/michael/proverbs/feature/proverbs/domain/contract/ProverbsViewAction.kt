package com.michael.proverbs.feature.proverbs.domain.contract

import com.michael.proverbs.feature.proverbs.domain.model.ScreenView
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

interface ProverbsViewAction {
    data class ProcessJsonString(val jsonString: String) : ProverbsViewAction
    data class SelectTab(val selectedTab: ScreenView) : ProverbsViewAction
    data class ToggleFavorite(val verse: VerseEntity) : ProverbsViewAction
    data class SelectChapter(val chapter: String) : ProverbsViewAction
    data class SearchQuery(val query: String) : ProverbsViewAction
    data class SearchFavouriteQuery(val query: String) : ProverbsViewAction
    object PreviousVerse : ProverbsViewAction
    object GetFavoriteVerses : ProverbsViewAction
    object NextVerse : ProverbsViewAction
    object RefreshVerse : ProverbsViewAction
}