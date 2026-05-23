package com.example.androidstarter.ui.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.androidstarter.ui.components.SettingsCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisplayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(12.dp))

        // Typography
        SettingsCard(icon = Icons.Filled.FormatSize, title = "字体排版") {
            val styles = listOf(
                "Display Large" to MaterialTheme.typography.displayLarge,
                "Headline Large" to MaterialTheme.typography.headlineLarge,
                "Title Large" to MaterialTheme.typography.titleLarge,
                "Title Medium" to MaterialTheme.typography.titleMedium,
                "Body Large" to MaterialTheme.typography.bodyLarge,
                "Body Medium" to MaterialTheme.typography.bodyMedium,
                "Label Large" to MaterialTheme.typography.labelLarge,
                "Label Small" to MaterialTheme.typography.labelSmall,
            )
            styles.forEach { (name, style) ->
                Text(
                    text = name,
                    style = style,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // 色板
        SettingsCard(icon = Icons.Filled.Brush, title = "颜色色板") {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val colors = listOf(
                    "Primary" to MaterialTheme.colorScheme.primary,
                    "Secondary" to MaterialTheme.colorScheme.secondary,
                    "Tertiary" to MaterialTheme.colorScheme.tertiary,
                    "Error" to MaterialTheme.colorScheme.error,
                    "Surface" to MaterialTheme.colorScheme.surface,
                    "Background" to MaterialTheme.colorScheme.background,
                    "Surface\nVariant" to MaterialTheme.colorScheme.surfaceVariant,
                    "Outline" to MaterialTheme.colorScheme.outline,
                )
                colors.forEach { (name, color) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color),
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = name,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // 卡片示例
        SettingsCard(icon = Icons.Filled.Dashboard, title = "卡片示例") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Text(
                    text = "这是 primaryContainer 颜色的卡片",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(16.dp),
                )
            }
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            ) {
                Text(
                    text = "这是 secondaryContainer 颜色的卡片",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // 分割线示例
        SettingsCard(icon = Icons.Filled.ListAlt, title = "分割线") {
            Text("上方有分割线的文字", style = MaterialTheme.typography.bodyMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            Text("下方有分割线的文字", style = MaterialTheme.typography.bodyMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            Text("再次分割", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(12.dp))

        // 图标网格
        SettingsCard(icon = Icons.Filled.GridOn, title = "图标网格") {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                listOf(
                    Icons.Filled.Favorite to "收藏",
                    Icons.Filled.Home to "首页",
                    Icons.Filled.Settings to "设置",
                    Icons.Filled.ListAlt to "列表",
                    Icons.Filled.Info to "信息",
                    Icons.Filled.Star to "星标",
                ).forEach { (icon, label) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(24.dp),
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
