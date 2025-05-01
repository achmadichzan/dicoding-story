package com.achmadichzan.dicodingstory.presentation.ui.screen.story.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.BrokenImage
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.presentation.component.ShimmerEffect
import com.achmadichzan.dicodingstory.presentation.util.formatIndonesianDate

@Composable
fun StoryItem(modifier: Modifier = Modifier, story: StoryDto, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(story.photoUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .size(Size(360, 640))
                .crossfade(true)
                .build(),
        )
        val painterState by painter.state.collectAsState()

        Column(verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = story.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formatIndonesianDate(story.createdAt),
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
            }

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
                            .wrapContentHeight(),
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
                is AsyncImagePainter.State.Empty -> {
                    Image(
                        imageVector = Icons.TwoTone.BrokenImage,
                        contentDescription = "Error loading image",
                        modifier = Modifier.fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                    )) {
                        append(story.name)
                    }
                    append(" ")
                    append(story.description)
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
            )
        }
    }
}