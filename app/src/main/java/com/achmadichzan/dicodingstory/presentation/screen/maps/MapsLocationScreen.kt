package com.achmadichzan.dicodingstory.presentation.screen.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.achmadichzan.dicodingstory.presentation.viewmodel.MapsLocationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapsLocationScreen(viewModel: MapsLocationViewModel = koinViewModel()) {
    val stories by viewModel.uiState.collectAsStateWithLifecycle()

    val indonesiaLatLng = LatLng(-2.5489, 118.0149)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(indonesiaLatLng, 4f)
    }

    Scaffold(contentWindowInsets = WindowInsets.safeContent) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                stories.forEach { story ->
                    val lat = story.lat
                    val lon = story.lon

                    if (lat != null && lon != null) {
                        Marker(
                            state = MarkerState(position = LatLng(lat, lon)),
                            title = story.name,
                            snippet = story.description
                        )
                    }
                }
            }
        }
    }
}
