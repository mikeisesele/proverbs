package com.michael.proverbs.feature.proverbs.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michael.proverbs.core.common.toPx
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.core.ui.extensions.mediumTexStyle
import com.michael.proverbs.feature.proverbs.domain.model.ScreenView
import kotlinx.coroutines.launch

@Composable
fun TabRowComponent(
    modifier: Modifier = Modifier, onTabSelected: (ScreenView) -> Unit, selectedTab: ScreenView
) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val tabWidths = remember { mutableStateMapOf<ScreenView, Dp>() }

        ScreenView.values().toList().forEach { screenView ->
            TabComponent(selected = selectedTab == screenView, onClick = {
                onTabSelected(screenView)
                coroutineScope.launch {
                    val tabWidth = tabWidths[screenView]?.toPx(density) ?: 0f

                    val position = tabWidths.entries.filter { it.key.ordinal <= screenView.ordinal }
                        .sumOf { it.value.toPx(density).toDouble() }.toFloat()

                    coroutineScope.launch {
                        scrollState.animateScrollTo(position.toInt() - (tabWidth / 2).toInt())
                    }

                }
            }) {
                Text(text = screenView.title, style = boldTexStyle(
                    size = 18,
                    color = if (selectedTab == screenView) Color.Black.copy(
                        alpha = 0.8f
                    ) else Color.Black.copy(alpha = 0.4f)
                ), modifier = Modifier.onSizeChanged { size ->
                    tabWidths[screenView] = with(density) { size.width.toDp() }
                })
            }
        }
    }
}

@Composable
fun TabComponent(selected: Boolean, onClick: () -> Unit, content: @Composable () -> Unit) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
            .clickable(role = Role.Tab, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 10.dp), contentAlignment = Alignment.Center
    ) {
        content()
    }
}
