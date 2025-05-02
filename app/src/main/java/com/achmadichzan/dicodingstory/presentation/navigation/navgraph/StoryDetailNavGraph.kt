package com.achmadichzan.dicodingstory.presentation.navigation.navgraph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.achmadichzan.dicodingstory.presentation.intent.DetailIntent
import com.achmadichzan.dicodingstory.presentation.navigation.Route
import com.achmadichzan.dicodingstory.presentation.navigation.navigateBack
import com.achmadichzan.dicodingstory.presentation.ui.screen.detail.DetailScreen
import com.achmadichzan.dicodingstory.presentation.viewmodel.DetailViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.StoryDetailNavGraph(navController: NavHostController) {
    composable<Route.StoryDetail>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }
    ) { backstackEntry ->
        val viewModel = koinViewModel<DetailViewModel>()
        val id = backstackEntry.arguments?.getString("id") ?: ""
        val state by remember { derivedStateOf { viewModel.state } }

        LaunchedEffect(Unit) {
            viewModel.navigationEvent.collect { event ->
                when (event) {
                    is DetailIntent.GoBack -> {
                        navController.navigateBack()
                    }
                    else -> Unit
                }
            }
        }

        DetailScreen(
            id = id,
            state = state,
            onIntent = viewModel::onIntent
        )
    }
}