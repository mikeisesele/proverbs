package com.michael.proverbs.feature.proverbs.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michael.easylog.logInline
import com.michael.easylog.logInlineNullable
import com.michael.proverbs.R
import com.michael.proverbs.core.common.readFromRawResource
import com.michael.proverbs.core.ui.extensions.rememberStateWithLifecycle
import com.michael.proverbs.feature.proverbs.domain.contract.ProverbsViewAction
import com.michael.proverbs.feature.proverbs.domain.model.ScreenView
import com.michael.proverbs.feature.proverbs.presentation.components.ProverbsCardContent
import com.michael.proverbs.feature.proverbs.presentation.components.ProverbsListContent
import com.michael.proverbs.feature.proverbs.presentation.components.TabRowComponent

@Composable
fun ProverbsScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val viewModel = viewModel<ProverbsViewModel>()
    val state by rememberStateWithLifecycle(viewModel.state)

    LaunchedEffect(Unit) {
        val proverbsJsonString = context.readFromRawResource(R.raw.proverbs)
        viewModel.onViewAction(ProverbsViewAction.ProcessJsonString(proverbsJsonString))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRowComponent(
            selectedTab = state.screenView,
            onTabSelected = {
                viewModel.onViewAction(ProverbsViewAction.SelectTab(it))
            }
        )

        when (state.screenView) {
            ScreenView.CARD_VIEW -> {
                ProverbsCardContent(
                    modifier = modifier,
                    currentRandomVerse = state.currentRandomVerse,
                    toggleFavorite = {
                        viewModel.onViewAction(ProverbsViewAction.ToggleFavorite(it))
                    }
                )
            }

            ScreenView.LIST_VIEW -> {
                ProverbsListContent(
                    modifier = modifier,
                    verses = state.currentChapterVerses,
                    chapters = state.chapterList
                )
            }
        }
    }
}