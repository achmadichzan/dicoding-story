package com.achmadichzan.dicodingstory.presentation.navigation.navgraph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.achmadichzan.dicodingstory.presentation.intent.MapsLocationIntent
import com.achmadichzan.dicodingstory.presentation.navigation.Route
import com.achmadichzan.dicodingstory.presentation.navigation.navigateBack
import com.achmadichzan.dicodingstory.presentation.ui.screen.maps.MapsLocationScreen
import com.achmadichzan.dicodingstory.presentation.viewmodel.MapsLocationViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.MapsLocationNavGraph(navController: NavHostController) {
    composable<Route.MapsLocation> {
        val viewModel = koinViewModel<MapsLocationViewModel>()
        val stories by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.navigationEvent.collect { event ->
                when (event) {
                    MapsLocationIntent.GoBack -> {
                        navController.navigateBack()
                    }
                }
            }
        }

        MapsLocationScreen(
            stories = stories,
            onIntent = viewModel::onIntent
        )
    }
}