package com.michael.proverbs.feature.proverbs.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.core.ui.extensions.clickable

@Composable
fun ChapterComponent(
    modifier: Modifier = Modifier,
    selectedChapter: String,
    chapter: String,
    onChapterClick: (String) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .clickable(onClick = { onChapterClick(chapter) })
            .widthIn(min = if (selectedChapter == chapter) 55.dp else 50.dp, max = 55.dp)
            .padding(vertical = 2.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (selectedChapter == chapter) {
                Color.Black.copy(alpha = 0.5f)
            } else {
                Color.White
            }
        ),
        border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(Color.Black))
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                text = chapter,
                style = boldTexStyle(
                    size = 16, color = if (selectedChapter == chapter) {
                        Color.White
                    } else {
                        Color.Black
                    }
                ),
            )
        }
    }
}
