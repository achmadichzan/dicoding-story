package com.achmadichzan.dicodingstory.presentation.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.achmadichzan.dicodingstory.domain.usecase.GetTokenUseCase
import com.achmadichzan.dicodingstory.presentation.intent.AddStoryIntent
import com.achmadichzan.dicodingstory.presentation.intent.DetailIntent
import com.achmadichzan.dicodingstory.presentation.intent.LoginIntent
import com.achmadichzan.dicodingstory.presentation.intent.MapsLocationIntent
import com.achmadichzan.dicodingstory.presentation.intent.RegisterIntent
import com.achmadichzan.dicodingstory.presentation.intent.StoryIntent
import com.achmadichzan.dicodingstory.presentation.ui.screen.addstory.AddStoryScreen
import com.achmadichzan.dicodingstory.presentation.ui.screen.auth.LoginScreen
import com.achmadichzan.dicodingstory.presentation.ui.screen.auth.RegisterScreen
import com.achmadichzan.dicodingstory.presentation.ui.screen.detail.DetailScreen
import com.achmadichzan.dicodingstory.presentation.ui.screen.maps.MapsLocationScreen
import com.achmadichzan.dicodingstory.presentation.ui.screen.story.StoryScreen
import com.achmadichzan.dicodingstory.presentation.viewmodel.DetailViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.LoginViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.MapsLocationViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.RegisterViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.StoryViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.UploadViewModel
import com.google.android.gms.location.LocationServices
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@Composable
fun NavMain(token: String?) {

    val navController: NavHostController = rememberNavController()
    val startDestination = if (token.isNullOrBlank()) Route.Login else Route.Story

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
                                popUpTo(Route.Register) { inclusive = true }
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
                                popUpTo(Route.Story) { inclusive = false }
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

        composable<Route.AddStory> {
            val viewModel = koinViewModel<UploadViewModel>()
            val state = viewModel.state
            val context = getKoin().get<Context>()

            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { intent ->
                    when (intent) {
                        AddStoryIntent.GoBack -> navController.navigateBack()
                        else -> Unit
                    }
                }
            }

            @SuppressLint("MissingPermission")
            AddStoryScreen(
                state = state,
                onUpload = { file, desc ->
                    if (state.isLocationEnabled) {
                        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                            val lat = location?.latitude
                            val lon = location?.longitude
                            viewModel.uploadStory(file, desc, lat, lon)
                        }
                    } else {
                        viewModel.uploadStory(file, desc, null, null)
                    }
                },
                onIntent = viewModel::onIntent
            )
        }

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
}