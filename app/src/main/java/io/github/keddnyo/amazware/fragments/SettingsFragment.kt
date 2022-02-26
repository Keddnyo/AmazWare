package io.github.keddnyo.amazware.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import io.github.keddnyo.amazware.BuildConfig
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.ThemeSwitcher

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.settings) // New title

        val theme = findPreference<Preference>("theme")
        val about = findPreference<Preference>("info")
        val cloud = findPreference<Preference>("info2")
        about?.title = getString(R.string.app_name) + " " + "v" + BuildConfig.VERSION_NAME

        about!!.setOnPreferenceClickListener {
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

        /**theme!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, _ ->
                ThemeSwitcher().switch(requireContext(), resources)
                true
            }**/
    }
}