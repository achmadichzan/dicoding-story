package com.achmadichzan.dicodingstory.presentation.screen.story.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.presentation.components.ShimmerEffect

@Composable
fun StoryItem(story: StoryDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = story.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxWidth()
                    .height(200.dp),
                model = story.photoUrl,
                contentDescription = story.name,
                loading = {
                    ShimmerEffect(
                        modifier = Modifier.fillMaxWidth()
                            .height(200.dp)
                    )
                },
                contentScale = ContentScale.FillWidth
            )

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
