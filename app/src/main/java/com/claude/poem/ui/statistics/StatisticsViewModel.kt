package com.claude.poem.ui.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.claude.poem.data.repository.PoemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

data class WeeklyData(
    val labels: List<String> = listOf("一", "二", "三", "四", "五", "六", "日"),
    val values: List<Int> = emptyList()
)

data class StatisticsState(
    val totalViews: Int = 12458,
    val totalFavorites: Int = 0,
    val weeklyData: WeeklyData = WeeklyData(
        values = listOf(23, 45, 38, 62, 51, 78, 34)
    ),
    val currentMonth: Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH),
    val currentYear: Int = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
)

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PoemRepository(application)

    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    init {
        loadFavoritesCount()
    }

    private fun loadFavoritesCount() {
        viewModelScope.launch {
            val count = repository.getFavoriteCount()
            _state.value = _state.value.copy(totalFavorites = count)
        }
    }

    fun previousMonth() {
        val current = _state.value
        val cal = java.util.Calendar.getInstance()
        cal.set(current.currentYear, current.currentMonth, 1)
        cal.add(java.util.Calendar.MONTH, -1)
        _state.value = current.copy(
            currentMonth = cal.get(java.util.Calendar.MONTH),
            currentYear = cal.get(java.util.Calendar.YEAR)
        )
    }

    fun nextMonth() {
        val current = _state.value
        val cal = java.util.Calendar.getInstance()
        cal.set(current.currentYear, current.currentMonth, 1)
        cal.add(java.util.Calendar.MONTH, 1)
        _state.value = current.copy(
            currentMonth = cal.get(java.util.Calendar.MONTH),
            currentYear = cal.get(java.util.Calendar.YEAR)
        )
    }

    fun recordPageView() {
        _state.value = _state.value.copy(totalViews = _state.value.totalViews + 1)
    }
}
