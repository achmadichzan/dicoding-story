package com.achmadichzan.dicodingstory.presentation.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.achmadichzan.dicodingstory.presentation.components.ShimmerEffect
import com.achmadichzan.dicodingstory.presentation.viewmodel.DetailViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: String,
    navController: NavController,
    viewModel: DetailViewModel = koinViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(id) {
        viewModel.loadDetail(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Story Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
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
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        val screenHeight = LocalConfiguration.current.screenHeightDp.dp

                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth(),
                            model = state.story.photoUrl,
                            contentDescription = state.story.name,
                            loading = {
                                ShimmerEffect(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(screenHeight * 0.4f)
                                )
                            },
                            contentScale = ContentScale.FillWidth
                        )

                        Text("Name: ${state.story.name}", fontWeight = FontWeight.Bold)

                        Text("Description: ${state.story.description}")

                        state.story.lat?.let { Text("Lat: $it") }

                        state.story.lon?.let { Text("Lon: $it") }

                        val indonesiaLatLng = LatLng(-2.5489, 118.0149)

                        val storyLatLng = LatLng(
                            state.story.lat ?: indonesiaLatLng.latitude,
                            state.story.lon ?: indonesiaLatLng.longitude
                        )

                        val cameraPositionState = rememberCameraPositionState()

                        LaunchedEffect(storyLatLng) {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(storyLatLng, 8f)
                            )
                        }

                        state.story.lat?.let {
                            state.story.lon?.let {
                                GoogleMap(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(screenHeight * 0.4f),
                                    cameraPositionState = cameraPositionState
                                ) {
                                    Marker(
                                        state = MarkerState(position = storyLatLng),
                                        title = state.story.name,
                                        snippet = state.story.description
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