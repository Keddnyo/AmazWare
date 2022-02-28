package io.github.keddnyo.amazware.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class DarkMode {
    fun switch(context: Context) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences
        when (sharedPreferences.getBoolean("dark_mode", false)) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Dark Mode
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Light Mode
            }
        }
    }
}