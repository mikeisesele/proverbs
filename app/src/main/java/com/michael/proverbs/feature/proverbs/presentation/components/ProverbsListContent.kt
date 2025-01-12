package com.michael.proverbs.feature.proverbs.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michael.proverbs.R
import com.michael.proverbs.core.base.model.ImmutableList
import com.michael.proverbs.core.common.customTitleCase
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.core.ui.extensions.clickable
import com.michael.proverbs.core.ui.extensions.mediumTexStyle
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.Verse
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity
import org.jetbrains.kotlin.utils.addToStdlib.applyIf

@Composable
fun ProverbsListContent(
    modifier: Modifier = Modifier,
    selectedChapter: String,
    verses: ImmutableList<VerseEntity>,
    chapters: ImmutableList<String>,
    toggleFavorite: (VerseEntity) -> Unit,
    onChapterClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyColumn(
            modifier = modifier.wrapContentWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (chapters.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.chapters),
                        style = boldTexStyle(size = 10),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
            items(chapters) {
                ChapterComponent(
                    modifier = modifier.fillParentMaxWidth(),
                    selectedChapter = selectedChapter,
                    chapter = it,
                    onChapterClick = onChapterClick
                )
            }
        }
        LazyColumn(
            modifier = modifier.weight(9f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(verses) {
                ProverbListItem(
                    verse = it,
                    toggleFavorite = toggleFavorite,
                )
            }
        }
    }
}
