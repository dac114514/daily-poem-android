package com.example.androidstarter.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidstarter.ui.components.ScreenHeader
import com.example.androidstarter.ui.components.SettingsCard

@Composable
fun WidgetScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(24.dp))
        ScreenHeader("组件", "Material 3 控件展示")

        Spacer(Modifier.height(24.dp))

        // 按钮
        SettingsCard(icon = Icons.Filled.TouchApp, title = "按钮") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { }) { Text("Filled") }
                FilledTonalButton(onClick = { }) { Text("Tonal") }
                OutlinedButton(onClick = { }) { Text("Outlined") }
                TextButton(onClick = { }) { Text("Text") }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Favorite, contentDescription = null)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                }
                Text(
                    text = "IconButton 示例",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 文本输入
        var text by remember { mutableStateOf("") }
        SettingsCard(icon = Icons.Filled.Input, title = "文本输入") {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("输入内容") },
                placeholder = { Text("在这里打字...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.height(16.dp))

        // 开关
        var switchChecked by remember { mutableStateOf(false) }
        SettingsCard(icon = Icons.Filled.ToggleOn, title = "开关") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Wi-Fi", modifier = Modifier.weight(1f))
                Switch(checked = switchChecked, onCheckedChange = { switchChecked = it })
            }
        }

        Spacer(Modifier.height(16.dp))

        // 复选框
        var checkA by remember { mutableStateOf(false) }
        var checkB by remember { mutableStateOf(true) }
        var checkC by remember { mutableStateOf(false) }
        SettingsCard(icon = Icons.Filled.CheckCircle, title = "复选框") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checkA, onCheckedChange = { checkA = it })
                Text("选项 A", modifier = Modifier.padding(start = 4.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checkB, onCheckedChange = { checkB = it })
                Text("选项 B", modifier = Modifier.padding(start = 4.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checkC, onCheckedChange = { checkC = it })
                Text("选项 C", modifier = Modifier.padding(start = 4.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        // 单选按钮
        var selectedRadio by remember { mutableStateOf(0) }
        SettingsCard(icon = Icons.Filled.RadioButtonChecked, title = "单选按钮") {
            listOf("小型", "中型", "大型").forEachIndexed { index, label ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    RadioButton(selected = selectedRadio == index, onClick = { selectedRadio = index })
                    Text(label, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 滑块
        var sliderValue by remember { mutableFloatStateOf(0.5f) }
        SettingsCard(icon = Icons.Filled.Dialpad, title = "滑块") {
            Text(
                text = "数值: ${(sliderValue * 100).toInt()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Slider(value = sliderValue, onValueChange = { sliderValue = it })
        }

        Spacer(Modifier.height(16.dp))

        // 芯片
        var selectedChip by remember { mutableStateOf(false) }
        SettingsCard(icon = Icons.Filled.AutoAwesome, title = "芯片") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = { }, label = { Text("辅助") })
                FilterChip(
                    selected = selectedChip,
                    onClick = { selectedChip = !selectedChip },
                    label = { Text("筛选") },
                )
                InputChip(
                    selected = selectedChip,
                    onClick = { selectedChip = !selectedChip },
                    label = { Text("输入") },
                )
                SuggestionChip(onClick = { }, label = { Text("建议") })
            }
        }

        Spacer(Modifier.height(16.dp))

        // 进度指示器
        SettingsCard(icon = Icons.Filled.Favorite, title = "进度指示器") {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
        }

        Spacer(Modifier.height(32.dp))
    }
}
