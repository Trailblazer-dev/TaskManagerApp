package com.example.taskmanagerapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var isDarkTheme = mutableStateOf(prefs.getBoolean(DARK_THEME_KEY, false))
        private set

    fun toggleTheme() {
        val newTheme = !isDarkTheme.value
        isDarkTheme.value = newTheme
        prefs.edit().putBoolean(DARK_THEME_KEY, newTheme).apply()
    }

    companion object {
        private const val DARK_THEME_KEY = "darkMode"
    }
}
