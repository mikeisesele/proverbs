package com.michael.proverbs.feature

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.michael.easylog.logInline
import com.michael.proverbs.R
import com.michael.proverbs.core.common.customTitleCase
import com.michael.proverbs.core.common.displayToast
import com.michael.proverbs.core.common.toPx
import com.michael.proverbs.core.ui.extensions.applyIf
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.core.ui.extensions.clickable
import com.michael.proverbs.core.ui.theme.Dimens
import com.michael.proverbs.feature.proverbs.domain.contract.ProverbsState
import com.michael.proverbs.feature.proverbs.domain.contract.ProverbsViewAction
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity
import com.michael.proverbs.feature.proverbs.presentation.components.SearchBarAnimated
import com.michael.proverbs.feature.proverbs.presentation.components.SearchResultsContent
import kotlinx.serialization.Serializable
import kotlin.reflect.KFunction1

@Serializable
object FavouriteProverbsScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteProverbsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    state: ProverbsState,
    onViewAction: KFunction1<ProverbsViewAction, Unit>
) {
    val context = LocalContext.current

    var searchBarComponentVisible by remember {
        mutableStateOf(false)
    }

    BackHandler {
        if (searchBarComponentVisible) {
            searchBarComponentVisible = false
            onViewAction(ProverbsViewAction.SearchQuery(""))
        } else {
            onBackClick.invoke()
        }
    }

    LaunchedEffect(Unit) { onViewAction(ProverbsViewAction.GetFavoriteVerses) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favourite_proverbs),
                        style = boldTexStyle(size = 16),
                        color = Color.White
                    )
                },
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = Dimens.PaddingHalf)
                            .clickable(onClick = {
                                searchBarComponentVisible = !searchBarComponentVisible
                            }),
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            )
        },

        ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state.searchedFavouriteProverbs.isEmpty() && state.favouriteVerses.isEmpty() && !searchBarComponentVisible ) {
                Text(text = "No Favourite Verses")
            } else {
                if (searchBarComponentVisible) {
                    Column(
                        modifier = Modifier.wrapContentHeight().padding(it),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SearchBarAnimated(
                            modifier = modifier.padding(top = 16.dp),
                            isVisible = searchBarComponentVisible,
                            searchQuery = state.searchFavouritesQuery,
                            onSearchQueryChange = {
                                onViewAction(ProverbsViewAction.SearchFavouriteQuery(it))
                            },
                            onCloseClick = {
                                searchBarComponentVisible = false
                                onViewAction(ProverbsViewAction.SearchFavouriteQuery(""))
                            }
                        )

                        SearchResultsContent(
                            isVisible = searchBarComponentVisible && state.searchedFavouriteProverbs.isNotEmpty(),
                            searchQuery = state.searchFavouritesQuery,
                            searchedProverbs = state.searchedFavouriteProverbs,
                            emptyProverbText = stringResource(R.string.no_proverb_searched),
                            noMatchText = stringResource(R.string.no_matching_proverbs_found),
                            onToggleFavorite = { proverb ->
                                // Show toast depending on the current favorite status
                                if (proverb.isFavorite) {
                                    displayToast(
                                        context,
                                        context.getString(R.string.removed_from_favorites)
                                    )
                                } else {
                                    displayToast(
                                        context,
                                        context.getString(R.string.added_to_favorites)
                                    )
                                }

                                // Dispatch the toggle action to your ViewModel or parent
                                onViewAction(ProverbsViewAction.ToggleFavorite(proverb))
                            },
                            modifier = Modifier.weight(9f)
                        )
                    }
                }

                if (state.searchedFavouriteProverbs.isEmpty()) {
                    LazyColumn(
                        modifier = modifier
                            .background(Color.White)
                            .fillMaxSize()
                            .applyIf(!searchBarComponentVisible) {
                              padding(it)
                            },
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        item {
                            Spacer(modifier = Modifier.padding(top = 8.dp))
                        }
                        items(state.favouriteVerses) { verse ->
                            FavoriteProverbListItem(
                                modifier = modifier
                                    .fillParentMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 2.dp),
                                verse = verse,
                                toggleFavorite = {
                                    onViewAction(
                                        ProverbsViewAction.ToggleFavorite(
                                            verse
                                        )
                                    )
                                },
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FavoriteProverbListItem(
    modifier: Modifier = Modifier,
    verse: VerseEntity,
    toggleFavorite: (VerseEntity) -> Unit,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
        ),
        border = if (verse.isFavorite) {
            CardDefaults.outlinedCardBorder()
                .copy(brush = SolidColor(Color.Black))
        } else {
            CardDefaults.outlinedCardBorder()
        },
    ) {

        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                stringResource(R.string.proverbs_verse, verse.chapterNumber, verse.verseNumber),
                style = boldTexStyle(size = 12),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = verse.verseText.customTitleCase(),
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
                contentDescription = stringResource(R.string.checked_icon),
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
