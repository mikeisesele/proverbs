package com.michael.proverbs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.michael.proverbs.core.ui.extensions.rememberStateWithLifecycle
import com.michael.proverbs.feature.FavouriteProverbsScreen
import com.michael.proverbs.feature.FavouriteProverbsScreenDestination
import com.michael.proverbs.feature.proverbs.presentation.ProverbsScreen
import com.michael.proverbs.feature.proverbs.presentation.ProverbsScreenDestination
import com.michael.proverbs.feature.proverbs.presentation.ProverbsViewModel

@Composable
fun Navigator(navController: NavHostController) {

    val viewModel = viewModel<ProverbsViewModel>()
    val state by rememberStateWithLifecycle(viewModel.state)

    NavHost(navController = navController, startDestination = ProverbsScreenDestination) {

        composable<ProverbsScreenDestination> {
            ProverbsScreen(
                onFavouriteClick = { navController.navigate(FavouriteProverbsScreenDestination) },
                state = state,
                onViewAction = viewModel::onViewAction
            )
        }

        composable<FavouriteProverbsScreenDestination> {
            FavouriteProverbsScreen(
                onBackClick = { navController.popBackStack() }, state = state,
                onViewAction = viewModel::onViewAction
            )
        }
    }
}