package com.example.androidstarter.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidstarter.data.local.ThemeMode
import com.example.androidstarter.ui.components.ExpandableSettingsCard
import com.example.androidstarter.ui.components.SettingsCard

@Composable
fun SettingsScreen(vm: SettingsViewModel = viewModel()) {
    val themeMode by vm.themeMode.collectAsState()
    var aboutExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(12.dp))

        // 外观
        SettingsCard(icon = Icons.Filled.Palette, title = "外观") {
            ThemeModeOption(
                label = "跟随系统",
                selected = themeMode == ThemeMode.SYSTEM,
                onClick = { vm.setThemeMode(ThemeMode.SYSTEM) },
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            ThemeModeOption(
                label = "浅色",
                selected = themeMode == ThemeMode.LIGHT,
                onClick = { vm.setThemeMode(ThemeMode.LIGHT) },
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            ThemeModeOption(
                label = "深色",
                selected = themeMode == ThemeMode.DARK,
                onClick = { vm.setThemeMode(ThemeMode.DARK) },
            )
        }

        Spacer(Modifier.height(12.dp))

        // 关于
        ExpandableSettingsCard(
            icon = Icons.Filled.Info,
            title = "关于",
            expanded = aboutExpanded,
            onToggle = { aboutExpanded = !aboutExpanded },
        ) {
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                InfoRow("包名", "com.example.androidstarter")
                InfoRow("版本", "1.0 (1)")
                InfoRow("最低 SDK", "Android 7.0 (API 24)")
                InfoRow("目标 SDK", "Android 15 (API 35)")
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ThemeModeOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
