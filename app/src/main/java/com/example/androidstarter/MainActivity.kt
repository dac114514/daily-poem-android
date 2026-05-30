package com.example.androidstarter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.androidstarter.data.local.ThemeMode
import com.example.androidstarter.ui.navigation.AppNavHost
import com.example.androidstarter.ui.settings.SettingsViewModel
import com.example.androidstarter.ui.theme.AndroidStarterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot()
        }
    }
}

@Composable
private fun AppRoot(settingsVm: SettingsViewModel = viewModel()) {
    val themeMode by settingsVm.themeMode.collectAsState()
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    AndroidStarterTheme(darkTheme = darkTheme) {
        val navController = rememberNavController()

        AppNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
