package com.achmadichzan.dicodingstory.presentation.navigation.navgraph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.achmadichzan.dicodingstory.presentation.intent.LoginIntent
import com.achmadichzan.dicodingstory.presentation.navigation.Route
import com.achmadichzan.dicodingstory.presentation.ui.screen.auth.LoginScreen
import com.achmadichzan.dicodingstory.presentation.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.LoginNavGraph(navController: NavHostController) {
    composable<Route.Login>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        }
    ) {
        val viewModel = koinViewModel<LoginViewModel>()
        val state = viewModel.state

        LaunchedEffect(Unit) {
            viewModel.navigationEvent.collect { event ->
                when (event) {
                    LoginIntent.NavigateToStory -> {
                        navController.navigate(Route.Story) {
                            popUpTo(0)
                        }
                    }
                    LoginIntent.NavigateToRegister -> {
                        navController.navigate(Route.Register) {
                            popUpTo(Route.Login) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    else -> Unit
                }
            }
        }

        LoginScreen(
            state = state,
            onIntent = viewModel::onIntent
        )
    }
}