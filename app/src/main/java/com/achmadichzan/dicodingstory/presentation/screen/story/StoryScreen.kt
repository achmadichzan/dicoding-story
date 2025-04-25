package com.achmadichzan.dicodingstory.presentation.screen.story

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import com.achmadichzan.dicodingstory.R
import com.achmadichzan.dicodingstory.domain.usecase.ClearTokenUseCase
import com.achmadichzan.dicodingstory.presentation.navigation.Route
import com.achmadichzan.dicodingstory.presentation.screen.story.components.StoryItem
import com.achmadichzan.dicodingstory.presentation.util.SessionManager
import com.achmadichzan.dicodingstory.presentation.viewmodel.StoryViewModel
import kotlinx.coroutines.delay
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
    val scope = rememberCoroutineScope()
    var isLoggingOut by remember { mutableStateOf(false) }

    val clearTokenUseCase: ClearTokenUseCase = getKoin().get()
    val pagingStories = viewModel.getPagedStories(token).collectAsLazyPagingItems()

    var isPullRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Stories") },
                actions = {
                    IconButton(onClick = { isLoggingOut = true }) {
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
                        popUpTo(Route.Story) { inclusive = false }
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
                .padding(innerPadding)
        ) {
            if (isLoggingOut) {
                LogoutDialog(
                    onConfirm = {
                        scope.launch {
                            clearTokenUseCase()
                            SessionManager.token = null
                            navController.navigate(Route.Login) {
                                popUpTo(0)
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                    },
                    onDismiss = { isLoggingOut = false }
                )
            }

            when (pagingStories.loadState.refresh) {
                is LoadState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                else -> {
                    PullToRefreshBox(
                        isRefreshing = isPullRefreshing,
                        onRefresh = {
                            isPullRefreshing = true
                            scope.launch {
                                pagingStories.refresh()
                                delay(300)
                                isPullRefreshing = false
                            }
                        },
                        state = pullRefreshState,
                        indicator = {
                            Indicator(
                                modifier = Modifier.align(Alignment.TopCenter),
                                isRefreshing = isPullRefreshing,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                state = pullRefreshState
                            )
                        }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(
                                count = pagingStories.itemCount,
                                key = { index -> pagingStories[index]?.id ?: index },
                                contentType = pagingStories.itemContentType { "StoryPagingItems" }
                            ) { index ->
                                pagingStories[index]?.let { story ->
                                    StoryItem(
                                        modifier = Modifier.animateItem(
                                            placementSpec = spring(
                                                dampingRatio = Spring.DampingRatioNoBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        ),
                                        story = story,
                                        onClick = {
                                            navController.navigate(Route.StoryDetail(story.id)) {
                                                popUpTo(Route.Story) { inclusive = false }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }

                            item {
                                if (pagingStories.loadState.append is LoadState.Loading) {
                                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text("Keluar") },
        text = { Text("Yakin ingin keluar?") },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Keluar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batalkan")
            }
        }
    )
}