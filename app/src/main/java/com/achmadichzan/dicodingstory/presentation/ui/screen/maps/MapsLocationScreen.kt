package com.achmadichzan.dicodingstory.presentation.ui.screen.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.achmadichzan.dicodingstory.data.local.room.StoryEntity
import com.achmadichzan.dicodingstory.presentation.intent.MapsLocationIntent
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsLocationScreen(
    stories: List<StoryEntity>,
    onIntent: (MapsLocationIntent) -> Unit
) {

    val indonesiaLatLng = LatLng(-2.5489, 118.0149)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(indonesiaLatLng, 4f)
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text(text = "Maps") },
                navigationIcon = {
                    IconButton(onClick = {
                        onIntent(MapsLocationIntent.GoBack)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.safeContent
    ) { innerPadding ->
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