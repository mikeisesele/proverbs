package com.michael.proverbs.feature.proverbs.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.core.ui.extensions.mediumTexStyle
import com.michael.proverbs.feature.proverbs.domain.model.domainmodel.Verse
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity

@Composable
fun ProverbsCardContent(
    modifier: Modifier = Modifier,
    toggleFavorite: (VerseEntity) -> Unit,
    currentRandomVerse: VerseEntity?
) {

    Box(
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxSize()
            .padding(bottom = 38.dp)
    ) {

        Box(
            Modifier
                .offset(y = (-36).dp)
                .padding(24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(450.dp)
        ) {
            Text(
                currentRandomVerse?.verseText.orEmpty(),
                style = boldTexStyle(size = 18, color = Color.Black),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 18.dp),
                textAlign = TextAlign.Center
            )

            currentRandomVerse?.let {


                Box(
                    Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.BottomCenter)
                        .clip(CircleShape)
                        .clickable(role = Role.Button) { toggleFavorite(currentRandomVerse) }
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(28.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Favorite",
                        colorFilter = if (currentRandomVerse?.isFavorite == true) ColorFilter.tint(
                            Color.Red.copy(
                                alpha = 0.5f
                            )
                        ) else ColorFilter.tint(
                            MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}