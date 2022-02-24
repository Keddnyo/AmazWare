package io.github.keddnyo.amazware.fragments

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.keddnyo.amazware.BuildConfig
import io.github.keddnyo.amazware.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.settings) // New title

        val about = findPreference<Preference>("info")
        about?.title = getString(R.string.app_name) + " " + "v" + BuildConfig.VERSION_NAME

    }
}