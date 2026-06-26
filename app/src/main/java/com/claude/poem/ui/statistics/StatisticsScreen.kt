package com.claude.poem.ui.statistics

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.claude.poem.R
import com.claude.poem.ui.components.CalendarHeatmap
import com.claude.poem.ui.components.DpIcon
import com.claude.poem.ui.components.WeekChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit,
    vm: StatisticsViewModel = viewModel(),
) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.recordPageView()
    }

    val monthNames = listOf(
        "一月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "十一月", "十二月"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "数据统计",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        DpIcon(
                            id = R.drawable.ic_arrow_back,
                            contentDescription = "返回",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatCard(
                    id = R.drawable.ic_visibility,
                    label = "总浏览",
                    value = state.totalViews.toString(),
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    id = R.drawable.ic_favorite,
                    label = "总收藏",
                    value = state.totalFavorites.toString(),
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                WeekChart(
                    data = state.weeklyData,
                    modifier = Modifier.padding(20.dp),
                )
            }

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { vm.previousMonth() }) {
                            DpIcon(
                                id = R.drawable.ic_chevron_left,
                                contentDescription = "上个月",
                            )
                        }
                        Text(
                            text = "${monthNames[state.currentMonth]} ${state.currentYear}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                        IconButton(onClick = { vm.nextMonth() }) {
                            DpIcon(
                                id = R.drawable.ic_chevron_right,
                                contentDescription = "下个月",
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    CalendarHeatmap(
                        year = state.currentYear,
                        month = state.currentMonth,
                        viewedDates = state.viewedDates,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "即将上线，敬请期待", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                DpIcon(
                    id = R.drawable.ic_download,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text("生成总结海报", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun StatCard(
    id: Int,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DpIcon(
                id = id,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
