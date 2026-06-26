package com.claude.poem.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val Context.statsDataStore: DataStore<Preferences> by preferencesDataStore(name = "stats")

data class StatsState(
    val totalViews: Int = 0,
    val weeklyViews: List<Int> = List(7) { 0 },
    val viewedDates: Set<String> = emptySet(),
)

class StatsRepository(private val context: Context) {

    private object Keys {
        val TOTAL_VIEWS = longPreferencesKey("total_views")
        val VIEWED_DATES = stringSetPreferencesKey("viewed_dates")
        val WEEKLY_WEEK_YEAR = longPreferencesKey("weekly_week_year")
        val WEEKLY_DATA = stringPreferencesKey("weekly_data")
    }

    val statsState: Flow<StatsState> = context.statsDataStore.data.map { prefs ->
        val totalViews = prefs[Keys.TOTAL_VIEWS]?.toInt() ?: 0
        val viewedDates = prefs[Keys.VIEWED_DATES] ?: emptySet()
        val weekly = readWeekly(prefs[Keys.WEEKLY_DATA], prefs[Keys.WEEKLY_WEEK_YEAR] ?: 0L)
        StatsState(totalViews, weekly, viewedDates)
    }

    suspend fun recordView() {
        val cal = Calendar.getInstance()
        val todayKey = dayKey(cal)
        val weekYear = weekKey(cal)
        val dayOfWeek = ((cal.get(Calendar.DAY_OF_WEEK) + 5) % 7)

        context.statsDataStore.edit { prefs ->
            val currentTotal = prefs[Keys.TOTAL_VIEWS] ?: 0L
            prefs[Keys.TOTAL_VIEWS] = currentTotal + 1L

            val dates = prefs[Keys.VIEWED_DATES] ?: emptySet()
            prefs[Keys.VIEWED_DATES] = dates + todayKey

            val savedWeek = prefs[Keys.WEEKLY_WEEK_YEAR] ?: 0L
            val current = if (savedWeek == weekYear) {
                prefs[Keys.WEEKLY_DATA]?.let { readWeeklyList(it) } ?: List(7) { 0 }
            } else {
                List(7) { 0 }
            }
            val updated = current.toMutableList().also { it[dayOfWeek] = it[dayOfWeek] + 1 }
            prefs[Keys.WEEKLY_DATA] = updated.joinToString(",")
            prefs[Keys.WEEKLY_WEEK_YEAR] = weekYear
        }
    }

    private fun dayKey(cal: Calendar): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)

    private fun weekKey(cal: Calendar): Long {
        val c = cal.clone() as Calendar
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return c.get(Calendar.YEAR) * 100L + c.get(Calendar.WEEK_OF_YEAR)
    }

    private fun readWeekly(raw: String?, savedWeek: Long): List<Int> {
        val cal = Calendar.getInstance()
        val currentWeek = weekKey(cal)
        if (raw == null || savedWeek != currentWeek) return List(7) { 0 }
        return readWeeklyList(raw)
    }

    private fun readWeeklyList(raw: String): List<Int> {
        val parts = raw.split(",")
        if (parts.size != 7) return List(7) { 0 }
        return parts.mapNotNull { it.toIntOrNull() }.let {
            if (it.size == 7) it else List(7) { 0 }
        }
    }

    fun totalViewsFlow(): Flow<Int> = context.statsDataStore.data.map { prefs ->
        (prefs[Keys.TOTAL_VIEWS] ?: 0L).toInt()
    }

    fun viewedDatesFlow(): Flow<Set<String>> = context.statsDataStore.data.map { prefs ->
        prefs[Keys.VIEWED_DATES] ?: emptySet()
    }
}
