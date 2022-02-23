package io.github.keddnyo.amazware

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import io.github.keddnyo.amazware.fragments.CloudFragment
import io.github.keddnyo.amazware.fragments.FeedFragment
import io.github.keddnyo.amazware.fragments.ManualFragment
import io.github.keddnyo.amazware.fragments.TelegramFragment
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val feedFragment = FeedFragment()
    private val cloudFragment = CloudFragment()
    private val manualFragment = ManualFragment()
    private val telegramFragment = TelegramFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        val permissionCheck = ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        replaceFragment(cloudFragment)
        bottomNavigation.selectedItemId = R.id.Cloud
        bottomNavigation.setOnNavigationItemSelectedListener {
            try {
                Handler().postDelayed({
                    when (it.itemId) {
                        R.id.Feed -> replaceFragment(feedFragment)
                        R.id.Cloud -> replaceFragment(cloudFragment)
                        R.id.Manual -> replaceFragment(manualFragment)
                        R.id.Telegram -> replaceFragment(telegramFragment)
                    }
                }, 500)
            } catch (e: IOException) {
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Info -> {
                val aboutDialog = AlertDialog.Builder(this)
                aboutDialog.setTitle(getString(R.string.app_name)+" "+BuildConfig.VERSION_NAME)
                aboutDialog.setMessage(getString(R.string.app_credits)+"\n"+getString(R.string.logic_credits)+"\n"+getString(R.string.support_credits))
                aboutDialog.setPositiveButton(getString(R.string.exit_title)) { _, _ ->
                    finish()
                }
                aboutDialog.setNegativeButton(getString(R.string.back)) { dialog, _ ->
                    dialog.dismiss()
                }
                aboutDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}