package com.michael.proverbs.feature.proverbs.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.michael.proverbs.R
import com.michael.proverbs.core.ui.extensions.clickable
import com.michael.proverbs.core.ui.theme.Dimens

private const val UNFOCUSED_ALPHA = 0.4f

@Composable
fun SearchBarComponent(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    searchQuery: String,
    text: String = "Search by word",
    color: Color = MaterialTheme.colorScheme.onPrimary,
    applyBottomPadding: Boolean = false
) {
    var queryValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = searchQuery,
                selection = TextRange(searchQuery.length),
            ),
        )
    }

    OutlinedTextField(
        textStyle = TextStyle(
            color = Color.Black
        ),
        value = queryValue,
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .height(20.dp),
                imageVector = Icons.Default.Search,
                contentDescription = "",
                tint = color
            )
        },
        trailingIcon = {
            if (queryValue.text.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .height(18.dp)
                        .clickable(
                            onClick = {
                                queryValue = TextFieldValue("")
                                onValueChange("")
                            }
                        ),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    tint = color
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .padding(start = Dimens.PaddingDefault)
            .fillMaxWidth()
            .border(
                width = 1.dp, // Custom border width
                color = color, // Border color
                shape = RoundedCornerShape(Dimens.RadiusTriple) // Custom rounded corners
            )
            .clip(RoundedCornerShape(Dimens.RadiusTriple)), // Clip shape to match the border
        placeholder = {
            Text(
                text = text,
                color = color.copy(alpha = UNFOCUSED_ALPHA)
            )
        },
        shape = RoundedCornerShape(Dimens.RadiusTriple),
        onValueChange = {
            queryValue = it
            onValueChange(it.text)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions()
    )
}


@Composable
fun SearchBarAnimated(
    isVisible: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)) + scaleIn(initialScale = 0.9f),
        exit = fadeOut(animationSpec = tween(durationMillis = 300)) + scaleOut(targetScale = 0.9f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Your custom SearchBarComponent
            SearchBarComponent(
                modifier = Modifier.weight(0.8f),
                searchQuery = searchQuery,
                onValueChange = onSearchQueryChange,
            )

            Icon(
                modifier = Modifier
                    .padding(horizontal = Dimens.PaddingHalf)
                    .weight(0.1f)
                    .clickable { onCloseClick() },
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
