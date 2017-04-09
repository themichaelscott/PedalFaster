package com.pedalfaster.launcher.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.prefs.ext.registerChangeListener
import com.pedalfaster.launcher.prefs.ext.unregisterChangeListener
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Timber.d("saveInstanceState is null - ${savedInstanceState == null}")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buildFragment()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerChangeListener(this)
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences.unregisterChangeListener(this)
        super.onPause()
    }

    private fun buildFragment() {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                activity.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        // watch for changes here
    }

    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

}