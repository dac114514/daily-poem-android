package com.claude.poem.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesRepository(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val BACKGROUND_ENABLED = booleanPreferencesKey("background_enabled")
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        ThemeMode.fromStorage(prefs[Keys.THEME_MODE])
    }

    val backgroundEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.BACKGROUND_ENABLED] ?: true
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = mode.toStorage()
        }
    }

    suspend fun setBackgroundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.BACKGROUND_ENABLED] = enabled
        }
    }
}
