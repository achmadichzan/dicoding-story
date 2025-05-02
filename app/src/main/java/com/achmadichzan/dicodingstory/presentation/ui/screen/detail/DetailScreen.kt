package com.achmadichzan.dicodingstory.presentation.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.SubcomposeAsyncImage
import com.achmadichzan.dicodingstory.presentation.component.ShimmerEffect
import com.achmadichzan.dicodingstory.presentation.intent.DetailIntent
import com.achmadichzan.dicodingstory.presentation.state.DetailState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: String,
    state: DetailState,
    onIntent: (DetailIntent) -> Unit,
) {
    var isMapShowing by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        onIntent(DetailIntent.LoadDetail(id))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = state.story?.name ?: "Detail Story",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                ) },
                navigationIcon = {
                    IconButton(onClick = {
                        onIntent(DetailIntent.GoBack)
                    }) {
                        Icon(
                            imageVector = Icons.TwoTone.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.error != null -> Text(
                    text = "Error: ${state.error}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )

                state.story != null -> {
                    val scrollState = rememberScrollState()

                    val scale = remember { Animatable(1f) }
                    val rotation = remember { Animatable(0f) }
                    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
                    val coroutineScope = rememberCoroutineScope()

                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
                    ) {
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxWidth()
                                .zIndex(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            val maxWidth = this.constraints.maxWidth.toFloat()
                            val maxHeight = this.constraints.maxHeight.toFloat()

                            SubcomposeAsyncImage(
                                model = state.story.photoUrl,
                                contentDescription = state.story.name,
                                contentScale = ContentScale.FillWidth,
                                loading = {
                                    ShimmerEffect(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(screenHeight * 0.4f)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .pointerInput(Unit) {
                                        awaitEachGesture {
                                            var zooming = false

                                            do {
                                                val event = awaitPointerEvent()
                                                val zoomChange = event.calculateZoom()
                                                val rotationChange = event.calculateRotation()
                                                val pan = event.calculatePan()
                                                if (zoomChange != 1f || rotationChange != 0f || pan != Offset.Zero) {
                                                    coroutineScope.launch {
                                                        zooming = true
                                                        val newScale = (scale.value * zoomChange).coerceIn(1f, 5f)
                                                        scale.snapTo(newScale)
                                                        rotation.snapTo(rotation.value + rotationChange)
                                                        val extraWidth = (scale.value - 1) * maxWidth
                                                        val extraHeight = (scale.value - 1) * maxHeight
                                                        val maxX = extraWidth / 2
                                                        val maxY = extraHeight / 2
                                                        val newOffset = Offset(
                                                            (offset.value.x + scale.value * pan.x).coerceIn(-maxX, maxX),
                                                            (offset.value.y + scale.value * pan.y).coerceIn(-maxY, maxY)
                                                        )
                                                        offset.snapTo(newOffset)
                                                    }
                                                }
                                            } while (event.changes.any { it.pressed })

                                            if (zooming) {
                                                coroutineScope.launch {
                                                    scale.animateTo(1f)
                                                    rotation.animateTo(0f)
                                                    offset.animateTo(Offset.Zero)
                                                }
                                            }
                                        }
                                    }
                                    .graphicsLayer {
                                        scaleX = scale.value
                                        scaleY = scale.value
                                        rotationZ = rotation.value
                                        translationX = offset.value.x
                                        translationY = offset.value.y
                                    }
                            )
                        }

                        Text(
                            text = state.story.name,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = state.story.description,
                            textAlign = TextAlign.Justify
                        )

                        state.story.lat?.let { Text("Lat: $it") }

                        state.story.lon?.let { Text("Lon: $it") }


                        val cameraPositionState = rememberCameraPositionState()

                        val lat = state.story.lat
                        val lon = state.story.lon

                        if (lat != null && lon != null) {
                            FilledTonalIconToggleButton(
                                checked = !isMapShowing,
                                onCheckedChange = {
                                    isMapShowing = !it
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Show map")
                            }

                            val storyLatLng = LatLng(
                                state.story.lat,
                                state.story.lon
                            )

                            AnimatedVisibility(
                                visible = isMapShowing,
                                enter = slideInVertically(
                                    animationSpec = tween(durationMillis = 300),
                                    initialOffsetY = { -(it + 10) / 2 }
                                ),
                                exit = slideOutVertically(
                                    animationSpec = tween(durationMillis = 300),
                                    targetOffsetY = { -(it + 10) / 2 }
                                ),
                                label = "Map"
                            ) {
                                GoogleMap(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(screenHeight * 0.4f),
                                    cameraPositionState = cameraPositionState,
                                    onMapLoaded = {
                                        cameraPositionState.move(
                                            CameraUpdateFactory.newLatLngZoom(storyLatLng, 8f)
                                        )
                                    }
                                ) {
                                    Marker(
                                        state = MarkerState(position = storyLatLng),
                                        alpha = 0.8f,
                                        title = state.story.name,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}