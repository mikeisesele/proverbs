package com.michael.proverbs.feature.proverbs.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

@Composable
fun ProverbsCardContent(
    modifier: Modifier = Modifier,
    toggleFavorite: (VerseEntity) -> Unit,
    currentRandomVerse: VerseEntity?,
    shareVerse: (VerseEntity) -> Unit,
    refreshVerse: () -> Unit,
//    onChapterSelected: (Chapter) -> Unit
) {

    Box(
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxSize()
            .padding(bottom = 38.dp)
    ) {

        Box(
            Modifier
                .padding(24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray.copy(alpha = 0.5f))
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(450.dp)
        ) {


            currentRandomVerse?.let {
                Text(
                    it.verseText,
                    style = boldTexStyle(size = 18, color = Color.Black.copy(alpha = 0.8f)),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 18.dp),
                    textAlign = TextAlign.Center
                )
                Box(
                    Modifier
                        .padding(bottom = 18.dp, end = 20.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .clickable(onClick =  { toggleFavorite(currentRandomVerse) } )
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(24.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Favorite",
                        colorFilter = if (currentRandomVerse.isFavorite) ColorFilter.tint(
                            Color.Red.copy(
                                alpha = 0.5f
                            )
                        ) else ColorFilter.tint(
                            Color.Black
                        )
                    )
                }

                Box(
                    Modifier
                        .padding(bottom = 18.dp, start = 20.dp)
                        .align(Alignment.BottomStart)
                        .clip(CircleShape)
                        .clickable(onClick = {shareVerse(currentRandomVerse)})
                        .background( Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(24.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Favorite",
                        colorFilter = ColorFilter.tint(
                            Color.Black
                        )
                    )
                }
                Text(
                    text = "chapter ${currentRandomVerse.chapterNumber}:${currentRandomVerse.verseNumber}",
                    style = boldTexStyle(size = 14, color = Color.Black.copy(alpha = 0.8f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .padding(top = 16.dp, start = 18.dp),
                    textAlign = TextAlign.Start
                )
                Icon(
                    imageVector = Icons.Outlined.Refresh, contentDescription = "Refresh",
                    modifier = Modifier
                        .clickable(onClick = refreshVerse)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp, end = 18.dp)
                        .size(56.dp),
                    tint = Color.Black.copy(alpha = 0.5f)
                )

            }
        }
    }
}