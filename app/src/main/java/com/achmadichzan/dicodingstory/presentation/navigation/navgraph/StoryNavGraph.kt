package com.achmadichzan.dicodingstory.presentation.navigation.navgraph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.achmadichzan.dicodingstory.domain.usecase.GetTokenUseCase
import com.achmadichzan.dicodingstory.presentation.intent.StoryIntent
import com.achmadichzan.dicodingstory.presentation.navigation.Route
import com.achmadichzan.dicodingstory.presentation.ui.screen.story.StoryScreen
import com.achmadichzan.dicodingstory.presentation.viewmodel.StoryViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

fun NavGraphBuilder.StoryNavGraph(navController: NavHostController) {
    composable<Route.Story>(
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
        val viewModel = koinViewModel<StoryViewModel>()
        var tokenState by remember { mutableStateOf("") }
        val getTokenUseCase: GetTokenUseCase = getKoin().get()

        LaunchedEffect(Unit) {
            tokenState = getTokenUseCase().toString()
            viewModel.setToken(tokenState)
        }

        val pagingStories = remember(viewModel) {
            viewModel.pagedStories
        }.collectAsLazyPagingItems()

        LaunchedEffect(Unit) {
            viewModel.navigationEvent.collect { event ->
                when (event) {
                    is StoryIntent.Logout -> {
                        navController.navigate(Route.Login) {
                            popUpTo(0) {
                                inclusive = true
                                saveState = false
                            }
                            launchSingleTop = false
                            restoreState = false
                        }
                    }
                    is StoryIntent.AddStory -> {
                        navController.navigate(Route.AddStory) {
                            popUpTo(Route.Story) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    is StoryIntent.OpenDetail -> {
                        navController.navigate(Route.StoryDetail(event.storyId)) {
                            popUpTo(Route.Story) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    is StoryIntent.MapsLocation -> {
                        navController.navigate(Route.MapsLocation) {
                            popUpTo(Route.Story) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }

        StoryScreen(
            pagingStories = pagingStories,
            onIntent = viewModel::onIntent
        )
    }
}