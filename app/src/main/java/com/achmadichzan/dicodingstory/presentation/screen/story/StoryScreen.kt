package com.achmadichzan.dicodingstory.presentation.screen.story

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.achmadichzan.dicodingstory.R
import com.achmadichzan.dicodingstory.domain.usecase.ClearTokenUseCase
import com.achmadichzan.dicodingstory.presentation.navigation.Route
import com.achmadichzan.dicodingstory.presentation.screen.story.components.StoryItem
import com.achmadichzan.dicodingstory.presentation.util.SessionManager
import com.achmadichzan.dicodingstory.presentation.viewmodel.StoryViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
    navController: NavHostController,
    viewModel: StoryViewModel = koinViewModel(),
    token: String,
) {
    val state = viewModel.state
    val clearTokenUseCase: ClearTokenUseCase = getKoin().get()
    val scope = rememberCoroutineScope()
    var isLoggingOut by remember { mutableStateOf(false) }

    val pagingStories = viewModel.getPagedStories(token).collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.loadStories(token)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Stories") },
                actions = {
                    IconButton(onClick = {
                            isLoggingOut = !isLoggingOut
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.AddStory) {
                        popUpTo(Route.AddStory) {
                            inclusive = false
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_24),
                    contentDescription = "Add Story"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.CenterStart
        ) {

            if (isLoggingOut) {
                AlertDialog(
                    onDismissRequest = { isLoggingOut = false },
                    title = { Text("Keluar") },
                    text = { Text("Yakin ingin keluar?") },
                    confirmButton = {
                        TextButton( onClick = {
                            scope.launch {
                                clearTokenUseCase()
                                SessionManager.token = null
                                navController.navigate(Route.Login) {
                                    popUpTo(0)
                                    launchSingleTop = true
                                    restoreState = false
                                }
                            }

                        }) {
                            Text("Keluar")
                        }

                    },
                    dismissButton = {
                        TextButton(onClick = { isLoggingOut = false }) {
                            Text("Batalkan")
                        }
                    }
                )
            }
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.error != null -> Text(
                    text = "Error: ${state.error}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        count = pagingStories.itemCount,
                        key = { index -> pagingStories[index]?.id ?: index },
                        contentType = pagingStories.itemContentType { "StoryPagingItems" }
                    ) { index ->
                        pagingStories[index]?.let { story ->
                            StoryItem(
                                story = story,
                                onClick = {
                                    navController.navigate(Route.StoryDetail(story.id)) {
                                        popUpTo(Route.Story) {
                                            inclusive = false
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }

                    pagingStories.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
                            }
                            loadState.append is LoadState.Loading -> {
                                item { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
                            }
                            loadState.refresh is LoadState.Error -> {
                                item { Text("Error: ${(loadState.refresh as LoadState.Error).error.localizedMessage}") }
                            }
                        }
                    }
                }
            }
        }
    }
}