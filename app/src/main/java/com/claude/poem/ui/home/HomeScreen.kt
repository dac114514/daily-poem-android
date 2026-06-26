package com.claude.poem.ui.home

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.claude.poem.R
import com.claude.poem.ui.components.DpIcon
import com.claude.poem.ui.components.PoemCard

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
                                val shareUri = vm.getShareImageUri(context)
                                if (shareUri != null) {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_STREAM, shareUri)
                                        type = "image/png"
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "分享诗词"))
                                }
                            },
                            modifier = Modifier.size(56.dp),
                        ) {
                            DpIcon(
                                id = R.drawable.ic_share,
                                contentDescription = "分享",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
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
