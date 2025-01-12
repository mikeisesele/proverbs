package com.michael.proverbs.feature.proverbs.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.michael.proverbs.core.common.customTitleCase
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.core.ui.extensions.clickable
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity


@Composable
fun ProverbListItem(
    verse: VerseEntity,
    toggleFavorite: (VerseEntity) -> Unit,
) {

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
        ),
        border =  CardDefaults.outlinedCardBorder().copy(
            brush = SolidColor(Color.Black)
        )
    ) {

        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                "Proverbs ${verse.chapterNumber} vs. ${verse.verseNumber}",
                style = boldTexStyle(size = 16),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 16.dp, bottom = 2.dp)
            )
        }

        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                verse.verseText.customTitleCase(),
                style = boldTexStyle(size = 16),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            )
        }

        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Icon(
                Icons.Rounded.Favorite,
                contentDescription = "Checked icon",
                tint = if (verse.isFavorite) {
                    Color.Red.copy(alpha = 0.5f)
                } else {
                    Color.Black.copy(alpha = 0.5f)
                },
                modifier = Modifier
                    .clickable(onClick = { toggleFavorite(verse) })
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp),
            )
        }
    }
}