package com.achmadichzan.dicodingstory.presentation.screen.story.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.presentation.components.ShimmerEffect

@Composable
fun StoryItem(modifier: Modifier = Modifier, story: StoryDto, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(story.photoUrl)
                .crossfade(true)
                .size(Size.ORIGINAL)
                .build(),
        )
        val painterState by painter.state.collectAsState()

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = story.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            when (painterState) {
                is AsyncImagePainter.State.Loading -> {
                    ShimmerEffect(
                        modifier = Modifier.fillMaxWidth()
                            .height(200.dp)
                    )
                }
                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = story.name,
                        modifier = Modifier.fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.FillWidth,
                    )
                }
                is AsyncImagePainter.State.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "Error loading image")
                    }
                }
                else -> Unit
            }

            Text(
                text = story.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Created at: ${story.createdAt}",
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
