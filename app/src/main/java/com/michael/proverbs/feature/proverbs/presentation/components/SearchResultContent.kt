package com.michael.proverbs.feature.proverbs.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

@Composable
fun SearchResultsContent(
    isVisible: Boolean,
    searchQuery: String,
    searchedProverbs: List<VerseEntity>,
    onToggleFavorite: (VerseEntity) -> Unit,
    modifier: Modifier = Modifier,
    emptyProverbText: String,
    noMatchText: String
) {
    // If not visible, skip rendering entirely
    if (!isVisible) return

    // If the list is empty, show either "No proverb searched" or "No matching proverbs found"
    if (searchedProverbs.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (searchQuery.isEmpty()) emptyProverbText else noMatchText,
                style = boldTexStyle(size = 16), // Replace with your typography style if needed
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    } else {
        // Show the list of proverbs
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(searchedProverbs) { proverb ->
                ProverbListItem(
                    verse = proverb,
                    toggleFavorite = {
                        onToggleFavorite(proverb)
                    }
                )
            }
        }
    }
}
