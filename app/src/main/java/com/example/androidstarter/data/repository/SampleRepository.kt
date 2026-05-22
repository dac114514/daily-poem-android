package com.example.androidstarter.data.repository

import com.example.androidstarter.data.model.SampleItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SampleRepository {

    fun observeItems(): Flow<List<SampleItem>> = flowOf(
        listOf(
            SampleItem(1, "欢迎卡片", "Material 3 Card · 圆角 12dp · elevation 2dp", 0xFF1976D2),
            SampleItem(2, "底部导航", "NavigationBar + NavigationBarItem", 0xFF1E88E5),
            SampleItem(3, "ViewModel 占位", "StateFlow + collectAsState", 0xFF00ACC1),
            SampleItem(4, "DataStore 偏好", "持久化主题模式选择", 0xFF43A047),
            SampleItem(5, "图标库", "androidx.compose.material:material-icons-extended", 0xFFFB8C00),
            SampleItem(6, "深色适配", "primary #9ECAFF · background #1A1C1E", 0xFF8E24AA),
        )
    )
}
