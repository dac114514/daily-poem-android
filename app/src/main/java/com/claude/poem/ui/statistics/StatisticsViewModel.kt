package com.claude.poem.ui.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.claude.poem.data.local.StatsRepository
import com.claude.poem.data.local.StatsState
import com.claude.poem.data.repository.PoemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class WeeklyData(
    val labels: List<String> = listOf("一", "二", "三", "四", "五", "六", "日"),
    val values: List<Int> = emptyList()
)

data class StatisticsState(
    val totalViews: Int = 0,
    val totalFavorites: Int = 0,
    val weeklyData: WeeklyData = WeeklyData(),
    val viewedDates: Set<String> = emptySet(),
    val currentMonth: Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH),
    val currentYear: Int = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
)

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PoemRepository(application)
    private val statsRepo = StatsRepository(application)

    private val _monthNav = MutableStateFlow(
        java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) to
            java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    )

    val state: StateFlow<StatisticsState> = combine(
        statsRepo.statsState,
        repository.getFavoriteCountFlow(),
        _monthNav
    ) { stats, favCount, monthYear ->
        val (month, year) = monthYear
        StatisticsState(
            totalViews = stats.totalViews,
            totalFavorites = favCount,
            weeklyData = WeeklyData(values = stats.weeklyViews),
            viewedDates = stats.viewedDates,
            currentMonth = month,
            currentYear = year,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatisticsState())

    fun previousMonth() {
        val (month, year) = _monthNav.value
        val cal = java.util.Calendar.getInstance()
        cal.set(year, month, 1)
        cal.add(java.util.Calendar.MONTH, -1)
        _monthNav.value = cal.get(java.util.Calendar.MONTH) to cal.get(java.util.Calendar.YEAR)
    }

    fun nextMonth() {
        val (month, year) = _monthNav.value
        val cal = java.util.Calendar.getInstance()
        cal.set(year, month, 1)
        cal.add(java.util.Calendar.MONTH, 1)
        _monthNav.value = cal.get(java.util.Calendar.MONTH) to cal.get(java.util.Calendar.YEAR)
    }

    fun recordPageView() {
        viewModelScope.launch { statsRepo.recordView() }
    }
}
