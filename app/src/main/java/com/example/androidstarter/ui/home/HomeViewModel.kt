package com.example.androidstarter.ui.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val featureCards: List<FeatureCard> = listOf(
        FeatureCard("Material 3 卡片", "圆角 12dp · elevation 2dp · 左侧色条", 0xFF1976D2),
        FeatureCard("底部导航 + Navigation Compose", "3 个 Tab，跨页持有路由栈", 0xFF1E88E5),
        FeatureCard("ViewModel + StateFlow", "生命周期感知，Recomposition 友好", 0xFF00ACC1),
        FeatureCard("DataStore 主题持久化", "跟随系统 / 浅色 / 深色 三选一", 0xFF43A047),
    )

    data class FeatureCard(
        val title: String,
        val subtitle: String,
        val accentColor: Long,
    )
}
