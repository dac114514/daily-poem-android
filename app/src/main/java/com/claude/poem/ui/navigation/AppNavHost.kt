package com.claude.poem.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.claude.poem.ui.favorites.FavoritesScreen
import com.claude.poem.ui.home.HomeScreen
import com.claude.poem.ui.settings.SettingsScreen
import com.claude.poem.ui.statistics.StatisticsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier,
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToFavorites = { navController.navigate(Routes.FAVORITES) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateToStatistics = { navController.navigate(Routes.STATISTICS) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Routes.STATISTICS) {
            StatisticsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
