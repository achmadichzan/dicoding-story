package com.achmadichzan.dicodingstory.presentation.navigation.navgraph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.achmadichzan.dicodingstory.presentation.intent.RegisterIntent
import com.achmadichzan.dicodingstory.presentation.navigation.Route
import com.achmadichzan.dicodingstory.presentation.ui.screen.auth.RegisterScreen
import com.achmadichzan.dicodingstory.presentation.viewmodel.RegisterViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.RegisterNavGraph(navController: NavHostController) {
    composable<Route.Register>(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(500)
            )
        }
    ) {
        val viewModel = koinViewModel<RegisterViewModel>()
        val state = viewModel.state

        LaunchedEffect(Unit) {
            viewModel.navigationEvent.collect { event ->
                when (event) {
                    RegisterIntent.NavigateBackToLogin -> {
                        navController.navigate(Route.Login) {
                            popUpTo(Route.Register) {
                                inclusive = true
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }

        RegisterScreen(
            state = state,
            onIntent = viewModel::onIntent
        )
    }
}