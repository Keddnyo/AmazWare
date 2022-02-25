package io.github.keddnyo.amazware

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.fragments.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // Fragments list
    private val feedFragment = FeedFragment()
    private val cloudFragment = CloudFragment()
    private val advancedFragment = AdvancedFragment()
    private val telegramFragment = TelegramFragment()
    private val settingsFragment = SettingsFragment()

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this) // Shared Preferences

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Night Mode

        // Permissions
        val permissionCheck = ActivityCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        // Bottom bar logic
        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

        // Select accent color
        when (sharedPreferences.getString("accent_color", "1")) {
            "1" -> {
                theme.applyStyle(R.style.Theme_AmazWare, true)
                bottomNavigation.itemTextColor = this.resources.getColorStateList(R.color.white)
                bottomNavigation.itemIconTintList = this.resources.getColorStateList(R.color.white)
            }
            "2" -> {
                theme.applyStyle(R.style.Red, true)
                when (sharedPreferences.getBoolean("colorize_icons", false)) {
                    true -> {
                        bottomNavigation.itemIconTintList = this.resources.getColorStateList(R.color.red)
                    }
                    false -> {
                        bottomNavigation.itemIconTintList = this.resources.getColorStateList(R.color.white)
                    }
                }
                when (sharedPreferences.getBoolean("colorize_titles", false)) {
                    true -> {
                        bottomNavigation.itemTextColor = this.resources.getColorStateList(R.color.red)
                    }
                    false -> {
                        bottomNavigation.itemTextColor = this.resources.getColorStateList(R.color.white)
                    }
                }
            }
            "3" -> {
                theme.applyStyle(R.style.Green, true)
                when (sharedPreferences.getBoolean("colorize_icons", false)) {
                    true -> {
                        bottomNavigation.itemIconTintList = this.resources.getColorStateList(R.color.green)
                    }
                    false -> {
                        bottomNavigation.itemIconTintList = this.resources.getColorStateList(R.color.white)
                    }
                }
                when (sharedPreferences.getBoolean("colorize_titles", false)) {
                    true -> {
                        bottomNavigation.itemTextColor = this.resources.getColorStateList(R.color.green)
                    }
                    false -> {
                        bottomNavigation.itemTextColor = this.resources.getColorStateList(R.color.white)
                    }
                }
            }
            "4" -> {
                theme.applyStyle(R.style.Blue, true)
                when (sharedPreferences.getBoolean("colorize_icons", false)) {
                    true -> {
                        bottomNavigation.itemIconTintList = this.resources.getColorStateList(R.color.blue)
                    }
                    false -> {
                        bottomNavigation.itemIconTintList = this.resources.getColorStateList(R.color.white)
                    }
                }
                when (sharedPreferences.getBoolean("colorize_titles", false)) {
                    true -> {
                        bottomNavigation.itemTextColor = this.resources.getColorStateList(R.color.blue)
                    }
                    false -> {
                        bottomNavigation.itemTextColor = this.resources.getColorStateList(R.color.white)
                    }
                }
            }
        }

        // Select fragment
        when (sharedPreferences.getString("default_tab", "3")) {
            "1" -> {
                replaceFragment(feedFragment)
                bottomNavigation.selectedItemId = R.id.Feed
            }
            "2" -> {
                replaceFragment(advancedFragment)
                bottomNavigation.selectedItemId = R.id.Extras
            }
            "3" -> {
                replaceFragment(cloudFragment)
                bottomNavigation.selectedItemId = R.id.Explore
            }
            "4" -> {
                replaceFragment(telegramFragment)
                bottomNavigation.selectedItemId = R.id.Telegram
            }
            else -> {
                replaceFragment(feedFragment)
                bottomNavigation.selectedItemId = R.id.Feed
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener {
            try {
                Handler().postDelayed({
                    when (it.itemId) {
                        R.id.Feed -> replaceFragment(feedFragment)
                        R.id.Explore -> replaceFragment(cloudFragment)
                        R.id.Extras -> replaceFragment(advancedFragment)
                        R.id.Telegram -> replaceFragment(telegramFragment)
                        R.id.Settings -> replaceFragment(settingsFragment)
                    }
                }, 500)
            } catch (e: IOException) {
            }
            true
        }
    }

    // Fragment replacing code
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}