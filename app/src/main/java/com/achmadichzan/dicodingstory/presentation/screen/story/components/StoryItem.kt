package com.achmadichzan.dicodingstory.presentation.screen.story.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.presentation.components.ShimmerEffect

@Composable
fun StoryItem(story: StoryDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxWidth()
                    .height(200.dp),
                model = story.photoUrl,
                contentDescription = story.name,
                loading = {
                    ShimmerEffect(modifier = Modifier.fillMaxWidth()
                        .height(200.dp))
                }
            )
            Text(text = story.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = story.description)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Created at: ${story.createdAt}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
