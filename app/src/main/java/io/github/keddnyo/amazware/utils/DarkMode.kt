package io.github.keddnyo.amazware.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class DarkMode {
    fun switch(context: Context): Boolean {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences

        return when (sharedPreferences.getBoolean("dark_mode", false)) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Dark Mode
                true
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Light Mode
                false
            }
        }
    }
}