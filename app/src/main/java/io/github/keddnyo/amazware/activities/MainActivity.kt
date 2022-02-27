package io.github.keddnyo.amazware.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.fragments.*
import io.github.keddnyo.amazware.fragments.utils.Theme
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // Fragments list
    private val feedFragment = Feed()
    private val exploreFragment = Explore()
    private val advancedFragment = Extras()
    private val telegramFragment = Telegram()
    private val settingsFragment = Settings()

    @SuppressLint("UseCompatLoadingForColorStateLists", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set dark mode
        Theme().switch(this, resources)
        selectFragment()

        val sp =
            PreferenceManager.getDefaultSharedPreferences(this) // Shared Preferences

        // Permissions
        val permissionCheck = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
            )
        }

        // Bottom bar logic
        val bottomNavigation =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener {
            try {
                Handler().postDelayed({
                    when (it.itemId) {
                        R.id.Feed -> replaceFragment(feedFragment)
                        R.id.Extras -> replaceFragment(advancedFragment)
                        R.id.Explore -> replaceFragment(exploreFragment)
                        R.id.Telegram -> replaceFragment(telegramFragment)
                        R.id.Settings -> replaceFragment(settingsFragment)
                    }
                }, 500)
            } catch (e: IOException) {
            }
            true
        }

        // Select accent color
        when (sp.getString("accent_color", "1")) {
            "1" -> {
                theme.applyStyle(R.style.Theme_AmazWare, true)
                bottomNavigation.itemIconTintList =
                    this.resources.getColorStateList(R.color.secondary)
                bottomNavigation.itemTextColor = this.resources.getColorStateList(R.color.secondary)
            }
            "2" -> {
                theme.applyStyle(R.style.Red, true)
                when (sp.getBoolean("colorize_icons", false)) {
                    true -> {
                        bottomNavigation.itemIconTintList =
                            this.resources.getColorStateList(R.color.red)
                    }
                    false -> {
                        bottomNavigation.itemIconTintList =
                            this.resources.getColorStateList(R.color.secondary)
                    }
                }
                when (sp.getBoolean("colorize_titles", false)) {
                    true -> {
                        bottomNavigation.itemTextColor =
                            this.resources.getColorStateList(R.color.red)
                    }
                    false -> {
                        bottomNavigation.itemTextColor =
                            this.resources.getColorStateList(R.color.secondary)
                    }
                }
            }
            "3" -> {
                theme.applyStyle(R.style.Green, true)
                when (sp.getBoolean("colorize_icons", false)) {
                    true -> {
                        bottomNavigation.itemIconTintList =
                            this.resources.getColorStateList(R.color.green)
                    }
                    false -> {
                        bottomNavigation.itemIconTintList =
                            this.resources.getColorStateList(R.color.secondary)
                    }
                }
                when (sp.getBoolean("colorize_titles", false)) {
                    true -> {
                        bottomNavigation.itemTextColor =
                            this.resources.getColorStateList(R.color.green)
                    }
                    false -> {
                        bottomNavigation.itemTextColor =
                            this.resources.getColorStateList(R.color.secondary)
                    }
                }
            }
            "4" -> {
                theme.applyStyle(R.style.Blue, true)
                when (sp.getBoolean("colorize_icons", false)) {
                    true -> {
                        bottomNavigation.itemIconTintList =
                            this.resources.getColorStateList(R.color.blue)
                    }
                    false -> {
                        bottomNavigation.itemIconTintList =
                            this.resources.getColorStateList(R.color.secondary)
                    }
                }
                when (sp.getBoolean("colorize_titles", false)) {
                    true -> {
                        bottomNavigation.itemTextColor =
                            this.resources.getColorStateList(R.color.blue)
                    }
                    false -> {
                        bottomNavigation.itemTextColor =
                            this.resources.getColorStateList(R.color.secondary)
                    }
                }
            }
        }
    }

    // Fragment replacing code
    private fun replaceFragment(fragment: Fragment) {
        runOnUiThread {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()

        }
    }

    private fun selectFragment() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this) // Shared Preferences
        val bottomNavigation =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

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
                replaceFragment(exploreFragment)
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
    }

    override fun onBackPressed() {
        selectFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                val builder = AlertDialog.Builder(this)
                    .setTitle(R.string.exit)
                    .setMessage(getString(R.string.exit_message))
                builder.setPositiveButton(android.R.string.yes) { _: DialogInterface?, _: Int ->
                    finish()
                }
                builder.setNegativeButton(android.R.string.no) { _: DialogInterface?, _: Int ->
                    DialogInterface.BUTTON_NEGATIVE
                }
                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}