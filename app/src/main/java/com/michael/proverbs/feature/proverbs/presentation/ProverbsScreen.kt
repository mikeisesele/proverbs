package com.michael.proverbs.feature.proverbs.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import com.bugfender.sdk.ui.FeedbackActivity.REQUEST_CODE
import com.michael.kompanion.utils.kompanionAllNotNull
import com.michael.kompanion.utils.kompanionIfAllNotNull
import com.michael.proverbs.R
import com.michael.proverbs.core.common.displayToast
import com.michael.proverbs.core.common.readFromRawResource
import com.michael.proverbs.core.ui.extensions.applyIf
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.core.ui.extensions.clickable
import com.michael.proverbs.core.ui.theme.Dimens
import com.michael.proverbs.feature.proverbs.domain.contract.ProverbsState
import com.michael.proverbs.feature.proverbs.domain.contract.ProverbsViewAction
import com.michael.proverbs.feature.proverbs.domain.model.ScreenView
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity
import com.michael.proverbs.feature.proverbs.presentation.components.ProverbsCardContent
import com.michael.proverbs.feature.proverbs.presentation.components.ProverbsListContent
import com.michael.proverbs.feature.proverbs.presentation.components.SearchBarAnimated
import com.michael.proverbs.feature.proverbs.presentation.components.SearchResultsContent
import com.michael.proverbs.feature.proverbs.presentation.components.TabRowComponent
import kotlinx.serialization.Serializable
import java.io.File
import java.io.FileOutputStream
import kotlin.reflect.KFunction1

@Serializable
data class ProverbsScreenDestination(
    val title: String?,
    val chapter: String?,
    val verse: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProverbsScreen(
    modifier: Modifier = Modifier,
    onFavouriteClick: () -> Unit,
    closeApp: () -> Unit,
    state: ProverbsState,
    onViewAction: KFunction1<ProverbsViewAction, Unit>,
    args: ProverbsScreenDestination
) {


    val context = LocalContext.current
    val view = LocalView.current
    var searchBarComponentVisible by remember {
        mutableStateOf(false)
    }
    var pendingIntentHandled by remember {
        mutableStateOf(false)
    }

    BackHandler {
        if (searchBarComponentVisible) {
            searchBarComponentVisible = false
            onViewAction(ProverbsViewAction.SearchQuery(""))
        } else {
            closeApp()
        }
    }

    LaunchedEffect(key1 = args) {
        if (kompanionAllNotNull(args.title, args.verse, args.chapter)) {
            onViewAction(ProverbsViewAction.SelectTab(ScreenView.CARD_VIEW))
        }
    }

    LaunchedEffect(Unit) {
        val proverbsJsonString = context.readFromRawResource(R.raw.proverbs)
        onViewAction(ProverbsViewAction.ProcessJsonString(proverbsJsonString))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            var text = stringResource(R.string.proverbs)
            state.currentRandomVerse?.verseNumber?.let {
                text = if (state.screenView == ScreenView.CARD_VIEW) {
                    "Proverbs ${state.currentRandomVerse.chapterNumber}: verse ${state.currentRandomVerse.verseNumber}"
                } else {
                    stringResource(R.string.book_of_proverbs)
                }
            }

            TopAppBar(title = {
                if (!searchBarComponentVisible) {
                    Text(
                        text = text,
                        style = boldTexStyle(size = 16),
                        color = Color.White
                    )
                }
            }, actions = {
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

                Icon(
                    modifier = Modifier
                        .padding(horizontal = Dimens.PaddingHalf)
                        .clickable(onClick = onFavouriteClick),
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Favourite",
                    tint = Color.White
                )
            })
        },
        bottomBar = {
            if (state.screenView == ScreenView.CARD_VIEW) {
                BottomAppBar(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BottomBarItem(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(R.string.previous),
                            icon = Icons.Outlined.KeyboardArrowLeft,
                            onTabSelected = {
                                onViewAction(ProverbsViewAction.PreviousVerse)
                            }
                        )

                        BottomBarItem(
                            modifier = Modifier.padding(end = 16.dp),
                            text = stringResource(R.string.next),
                            icon = Icons.Outlined.KeyboardArrowRight,
                            onTabSelected = {
                                onViewAction(ProverbsViewAction.NextVerse)
                            }
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBarAnimated(
                isVisible = searchBarComponentVisible,
                searchQuery = state.searchQuery,
                onSearchQueryChange = {
                    onViewAction(ProverbsViewAction.SearchQuery(it))
                },
                onCloseClick = {
                    searchBarComponentVisible = false
                    onViewAction(ProverbsViewAction.SearchQuery(""))
                }
            )

            if (!searchBarComponentVisible) {
                TabRowComponent(
                    selectedTab = state.screenView,
                    onTabSelected = {
                        onViewAction(ProverbsViewAction.SelectTab(it))
                    }
                )
            }

            if (searchBarComponentVisible) {
                SearchResultsContent(
                    isVisible = searchBarComponentVisible,
                    searchQuery = state.searchQuery,
                    searchedProverbs = state.searchedProverbs,
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
                            displayToast(context, context.getString(R.string.added_to_favorites))
                        }

                        // Dispatch the toggle action to your ViewModel or parent
                        onViewAction(ProverbsViewAction.ToggleFavorite(proverb))
                    },
                    modifier = Modifier.weight(9f)
                )
            } else {
                when (state.screenView) {
                    ScreenView.CARD_VIEW -> {
                        val data = if (kompanionAllNotNull(
                                args.title,
                                args.verse,
                                args.chapter
                            ) && !pendingIntentHandled
                        ) {
                            pendingIntentHandled = true
                            VerseEntity(
                                chapterNumber = args.chapter.orEmpty(),
                                verseNumber = args.verse.orEmpty(),
                                verseText = args.title.orEmpty(),
                                isFavorite = false
                            )

                        } else {
                            state.currentRandomVerse
                        }
                        ProverbsCardContent(
                            modifier = modifier,
                            currentRandomVerse = data,
                            toggleFavorite = {
                                onViewAction(ProverbsViewAction.ToggleFavorite(it))
                            },
                            shareVerse = {
                                captureAndShareScreenshot(context, view)
                            },
                            refreshVerse = {
                                onViewAction(ProverbsViewAction.RefreshVerse)
                            }
                        )
                    }

                    ScreenView.LIST_VIEW -> {
                        ProverbsListContent(
                            modifier = modifier,
                            selectedChapter = state.selectedChapter,
                            verses = state.currentChapterVerses,
                            chapters = state.chapterList,
                            toggleFavorite = { verse ->
                                onViewAction(ProverbsViewAction.ToggleFavorite(verse))
                            },
                            onChapterClick = { chapter ->
                                onViewAction(ProverbsViewAction.SelectChapter(chapter))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    text: String,
    onTabSelected: () -> Unit,
    icon: ImageVector
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .clickable(onTabSelected),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(30.dp) ,
            imageVector = icon,
            contentDescription = text,
            colorFilter = ColorFilter.tint(
                Color.Black
            )
        )

        Text(text = text, style = boldTexStyle(size = 12), color = Color.Black.copy(alpha = 0.7f))
    }
}

// Function to capture and save the screenshot
fun captureAndShareScreenshot(context: Context, view: View) {

    val activity = context as? Activity

    if (checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        if (activity != null) {
            requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        }
    }


    val bitmap: Bitmap = view.drawToBitmap()

    // Save the bitmap to a file or share
    val file = File(context.cacheDir, "screenshot.png")
    val fos = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    fos.flush()
    fos.close()

    shareImage(file, context)
}

fun shareImage(file: File, context: Context) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, uri)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}
