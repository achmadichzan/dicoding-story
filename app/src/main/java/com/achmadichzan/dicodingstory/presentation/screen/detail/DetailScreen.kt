package com.achmadichzan.dicodingstory.presentation.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.achmadichzan.dicodingstory.presentation.viewmodel.DetailViewModel
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.error != null -> Text("Error: ${state.error}", color = Color.Red)
                state.story != null -> {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = state.story.photoUrl,
                            contentDescription = state.story.name,
                            modifier = Modifier.fillMaxWidth().height(200.dp)
                        )
                        Text("Name: ${state.story.name}", fontWeight = FontWeight.Bold)
                        Text("Description: ${state.story.description}")
                        Text("Created At: ${state.story.createdAt}")
                        state.story.lat?.let { Text("Lat: $it") }
                        state.story.lon?.let { Text("Lon: $it") }
                    }
                }
            }
        }
    }
}
