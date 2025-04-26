package com.achmadichzan.dicodingstory.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.achmadichzan.dicodingstory.domain.usecase.GetTokenUseCase
import com.achmadichzan.dicodingstory.presentation.screen.addstory.AddStoryScreen
import com.achmadichzan.dicodingstory.presentation.screen.auth.LoginScreen
import com.achmadichzan.dicodingstory.presentation.screen.auth.RegisterScreen
import com.achmadichzan.dicodingstory.presentation.screen.detail.DetailScreen
import com.achmadichzan.dicodingstory.presentation.screen.story.StoryScreen
import com.achmadichzan.dicodingstory.presentation.util.SessionManager
import com.achmadichzan.dicodingstory.presentation.util.StoryIntent
import com.achmadichzan.dicodingstory.presentation.viewmodel.LoginViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.RegisterViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.StoryViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@Composable
fun NavMain(
    navController: NavHostController = rememberNavController()
) {
    val cachedToken = SessionManager.token

    val startDestination = when {
        cachedToken.isNullOrBlank() -> Route.Login
        else -> Route.Story
    }

    NavHost(navController = navController, startDestination = startDestination) {
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

            LoginScreen(
                navController = navController,
                state = state,
                onIntent = viewModel::onIntent
            )
        }
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

            RegisterScreen(
                navController = navController,
                state = state,
                onIntent = viewModel::onIntent
            )
        }
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

            val pagingStories = viewModel.getPagedStories().collectAsLazyPagingItems()

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        is StoryIntent.Logout -> {
                            navController.navigate(Route.Login) {
                                popUpTo(0)
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                        is StoryIntent.AddStory -> {
                            navController.navigate(Route.AddStory) {
                                popUpTo(Route.Story) { inclusive = false }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        is StoryIntent.OpenDetail -> {
                            navController.navigate(Route.StoryDetail(event.storyId)) {
                                popUpTo(Route.Story) { inclusive = false }
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
            val id = backstackEntry.arguments?.getString("id").toString()

            DetailScreen(id = id, navController = navController)
        }

        composable<Route.AddStory> {
            AddStoryScreen(navController = navController)
        }
    }
}
