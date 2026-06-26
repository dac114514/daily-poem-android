package com.claude.poem.ui.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.claude.poem.R
import com.claude.poem.ui.components.DpIcon
import com.claude.poem.ui.components.PoemCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToFavorites: () -> Unit,
    onNavigateToSettings: () -> Unit,
    vm: HomeViewModel = viewModel(),
) {
    val poem by vm.currentPoem.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var sharePreview by remember { mutableStateOf<SharePreview?>(null) }
    var isPreparingShare by remember { mutableStateOf(false) }

    sharePreview?.let { preview ->
        SharePreviewDialog(
            bitmap = preview.bitmap,
            onShare = {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, preview.uri)
                    type = "image/png"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, "分享诗词"))
                vm.cleanupShareImage(context)
                sharePreview = null
            },
            onDismiss = {
                vm.cleanupShareImage(context)
                sharePreview = null
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "每日诗文",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToFavorites) {
                        DpIcon(
                            id = R.drawable.ic_favorite,
                            contentDescription = "收藏列表",
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        DpIcon(
                            id = R.drawable.ic_settings,
                            contentDescription = "设置",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            val currentPoem = poem
            if (isLoading && currentPoem == null) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                )
            } else if (currentPoem != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    PoemCard(
                        poem = currentPoem,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 360.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedIconButton(
                            onClick = {
                                if (isPreparingShare) return@OutlinedIconButton
                                isPreparingShare = true
                                scope.launch {
                                    val preview = withContext(Dispatchers.IO) {
                                        vm.prepareSharePreview(context)
                                    }
                                    isPreparingShare = false
                                    if (preview != null) sharePreview = preview
                                }
                            },
                            enabled = !isPreparingShare,
                            modifier = Modifier.size(56.dp),
                        ) {
                            if (isPreparingShare) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    strokeWidth = 2.dp,
                                )
                            } else {
                                DpIcon(
                                    id = R.drawable.ic_share,
                                    contentDescription = "分享",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }

                        OutlinedIconButton(
                            onClick = { vm.toggleFavorite() },
                            modifier = Modifier.size(56.dp),
                        ) {
                            DpIcon(
                                id = if (currentPoem.isFavorite) R.drawable.ic_favorite
                                else R.drawable.ic_favorite_border,
                                contentDescription = if (currentPoem.isFavorite) "取消收藏" else "收藏",
                                tint = if (currentPoem.isFavorite) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                            )
                        }

                        FilledIconButton(
                            onClick = { vm.refreshPoem() },
                            modifier = Modifier.size(56.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                        ) {
                            DpIcon(
                                id = R.drawable.ic_skip_next,
                                contentDescription = "下一首",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun SharePreviewDialog(
    bitmap: android.graphics.Bitmap,
    onShare: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("分享诗词", fontWeight = FontWeight.Bold) },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onShare) {
                Text("分享")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        },
    )
}
