package com.michael.proverbs.feature.proverbs.domain.contract

import com.michael.proverbs.feature.proverbs.domain.model.ScreenView
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

interface ProverbsViewAction {
    data class ProcessJsonString(val jsonString: String) : ProverbsViewAction
    data class SelectTab(val selectedTab: ScreenView) : ProverbsViewAction
    data class ToggleFavorite(val verse: VerseEntity) : ProverbsViewAction
}