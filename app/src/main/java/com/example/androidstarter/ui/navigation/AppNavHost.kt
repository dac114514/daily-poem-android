package com.example.androidstarter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidstarter.ui.home.HomeScreen
import com.example.androidstarter.ui.list.ListScreen
import com.example.androidstarter.ui.settings.SettingsScreen

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
        composable(Routes.HOME) { HomeScreen() }
        composable(Routes.LIST) { ListScreen() }
        composable(Routes.SETTINGS) { SettingsScreen() }
    }
}
