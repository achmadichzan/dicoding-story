package com.achmadichzan.dicodingstory.presentation.ui.screen.story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.ArrowDownward
import androidx.compose.material.icons.twotone.ArrowUpward
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import coil3.imageLoader
import coil3.request.ImageRequest
import com.achmadichzan.dicodingstory.data.local.preferences.ScrollPreferences
import com.achmadichzan.dicodingstory.data.local.preferences.scrollDataStore
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.presentation.intent.StoryIntent
import com.achmadichzan.dicodingstory.presentation.ui.screen.story.component.StoryItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
    pagingStories: LazyPagingItems<StoryDto>,
    onIntent: (StoryIntent) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoggingOut by remember { mutableStateOf(false) }
    var isPullRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()
    val animatable = remember { Animatable(0.5f) }

    LaunchedEffect(key1 = true) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 350,
                easing = FastOutLinearInEasing
            )
        )
    }

    val context = LocalContext.current
    val indexOffset = produceState(initialValue = 0 to 0) {
        context.scrollDataStore.data.firstOrNull()?.let { prefs ->
            value = (prefs[ScrollPreferences.INDEX] ?: 0) to (prefs[ScrollPreferences.OFFSET] ?: 0)
        }
    }
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = indexOffset.value.first,
        initialFirstVisibleItemScrollOffset = indexOffset.value.second
    )
    val showScrollToTopButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    val showScrollToBottomButton by remember {
        derivedStateOf { listState
            .firstVisibleItemIndex < listState
                .layoutInfo.totalItemsCount - listState
                    .layoutInfo.visibleItemsInfo.size }
    }

    val imageLoader = LocalContext.current.imageLoader
    LaunchedEffect(pagingStories) {
        pagingStories.itemSnapshotList.items.take(10).forEach { story ->
            val request = ImageRequest.Builder(context)
                .data(story.photoUrl)
                .build()
            imageLoader.enqueue(request)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            context.scrollDataStore.edit { prefs ->
                prefs[ScrollPreferences.INDEX] = index
                prefs[ScrollPreferences.OFFSET] = offset
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cerita") },
                actions = {
                    IconButton(onClick = dropUnlessResumed {
                        onIntent(StoryIntent.MapsLocation)
                    }
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.LocationOn,
                            contentDescription = "Logout"
                        )
                    }
                    IconButton(onClick = dropUnlessResumed {
                        isLoggingOut = true }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = showScrollToTopButton,
                    enter = slideIn(
                        initialOffset = { IntOffset(0, it.height + 10) },
                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = slideOut(
                        targetOffset = { IntOffset(0, it.height + 10) },
                        animationSpec = tween(durationMillis = 300)
                    ),
                    label = "Scroll to top"
                ) {
                    FilledIconButton(
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.ArrowUpward,
                            contentDescription = "Scroll to top"
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { onIntent(StoryIntent.AddStory) }
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Add,
                        contentDescription = "Add Story"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(
                visible = showScrollToBottomButton,
                enter = slideIn(
                    initialOffset = { IntOffset(0, it.height) },
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOut(
                    targetOffset = { IntOffset(0, it.height) },
                    animationSpec = tween(durationMillis = 300)
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f)
                    .padding(bottom = 16.dp),
                label = "Scroll to bottom"
            ) {
                FilledIconButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.ArrowDownward,
                        contentDescription = "Scroll to bottom"
                    )
                }
            }

            if (isLoggingOut) {
                LogoutDialog(
                    onConfirm = dropUnlessResumed {
                        onIntent(StoryIntent.Logout)
                    },
                    onDismiss = dropUnlessResumed {
                        isLoggingOut = false
                    }
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
                            coroutineScope.launch {
                                delay(300)
                                pagingStories.refresh()
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
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            state = listState,
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(
                                count = pagingStories.itemCount,
                                key = { index -> pagingStories[index]?.id ?: index },
                                contentType = pagingStories.itemContentType { "StoryPagingItems" }
                            ) { index ->
                                val story = pagingStories[index]
                                if (story != null) {
                                    StoryItem(
                                        modifier = Modifier
                                            .graphicsLayer {
                                                this.scaleX = animatable.value
                                                this.scaleY = animatable.value
                                            },
                                        story = story,
                                        onClick = dropUnlessResumed {
                                            onIntent(StoryIntent.OpenDetail(story.id))
                                        }
                                    )
                                }
                            }

                            if (pagingStories.loadState.append is LoadState.Loading) {
                                item {
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