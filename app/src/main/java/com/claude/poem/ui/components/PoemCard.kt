package com.claude.poem.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.claude.poem.data.model.Poem

@Composable
fun PoemCard(
    poem: Poem,
    modifier: Modifier = Modifier,
    backgroundUrl: String? = null,
) {
    val hasBackground = !backgroundUrl.isNullOrBlank()
    var imageFailed by remember(backgroundUrl) { mutableStateOf(false) }
    val showBackground = hasBackground && !imageFailed

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Box {
                if (showBackground) {
                    AsyncImage(
                        model = backgroundUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                            .alpha(0.08f),
                        contentScale = ContentScale.Crop,
                        onError = { imageFailed = true },
                    )
                }

                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = poem.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = poem.author,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = poem.content,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            lineHeight = 32.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Text(
                    text = poem.dynasty.take(2),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontSize = 72.sp,
                    ),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 8.dp),
                )
            }
        }
    }
}
