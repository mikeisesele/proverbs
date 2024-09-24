package com.michael.proverbs.feature.proverbs.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michael.proverbs.core.base.model.ImmutableList
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.Verse
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

@Composable
fun ProverbsListContent(
    modifier: Modifier = Modifier,
    verses: ImmutableList<VerseEntity>,
    chapters: ImmutableList<String>,
    ) {
    LazyColumn (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        items(chapters) {
//            ProverbItem(chapters, verses)
//        }
    }
}

@Composable
fun ProverbItem(chapter: ImmutableList<String>, verses: ImmutableList<VerseEntity>) {
//    Text(text = chapter)
//    Spacer()
//    verses.forEach {
//        Text(text = it.verse)
//    }

}