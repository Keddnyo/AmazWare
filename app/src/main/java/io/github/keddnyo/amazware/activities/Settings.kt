package io.github.keddnyo.amazware.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.keddnyo.amazware.BuildConfig
import io.github.keddnyo.amazware.R

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = getString(R.string.settings) // New title
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onResume() {
            super.onResume()

            val about = findPreference<Preference>("info")
            val cloud = findPreference<Preference>("info2")
            about!!.title = getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME

            about.setOnPreferenceClickListener {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Keddnyo"))
                )
                true
            }
            cloud!!.setOnPreferenceClickListener {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://4pda.to/forum/index.php?showuser=243484"))
                )
                true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}