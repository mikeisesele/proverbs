package com.michael.proverbs.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michael.proverbs.core.ui.theme.Dimens

@Composable
fun WhiteSurroundingBox(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .padding(Dimens.PaddingHalf),
    ) {
        content()
    }
}
