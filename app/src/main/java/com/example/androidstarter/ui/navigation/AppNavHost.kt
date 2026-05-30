package com.example.androidstarter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidstarter.ui.favorites.FavoritesScreen
import com.example.androidstarter.ui.home.HomeScreen
import com.example.androidstarter.ui.settings.SettingsScreen
import com.example.androidstarter.ui.statistics.StatisticsScreen

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
            )
        }
        composable(Routes.STATISTICS) {
            StatisticsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
