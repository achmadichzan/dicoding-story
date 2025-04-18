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
import com.achmadichzan.dicodingstory.domain.usecase.GetTokenUseCase
import com.achmadichzan.dicodingstory.presentation.screen.auth.LoginScreen
import com.achmadichzan.dicodingstory.presentation.screen.auth.RegisterScreen
import com.achmadichzan.dicodingstory.presentation.screen.detail.DetailScreen
import com.achmadichzan.dicodingstory.presentation.screen.story.StoryScreen
import com.achmadichzan.dicodingstory.presentation.util.SessionManager
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
            LoginScreen(navController)
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
            RegisterScreen(navController)
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
            var tokenState by remember { mutableStateOf("") }
            val getTokenUseCase: GetTokenUseCase = getKoin().get()

            LaunchedEffect(Unit) {
                tokenState = getTokenUseCase() ?: ""
            }

            StoryScreen(navController = navController, token = tokenState)
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
            val id = backstackEntry.arguments?.getString("id") ?: ""

            DetailScreen(id = id, navController = navController)
        }
    }
}
