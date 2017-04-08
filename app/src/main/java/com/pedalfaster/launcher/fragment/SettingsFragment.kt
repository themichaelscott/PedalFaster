package com.pedalfaster.launcher.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.prefs.Prefs
import com.pedalfaster.launcher.prefs.ext.registerChangeListener
import com.pedalfaster.launcher.prefs.ext.unregisterChangeListener
import pocketknife.PocketKnife
import pocketknife.SaveState
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    @SaveState
    var devEnvironmentEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Timber.d("saveInstanceState is null - ${savedInstanceState == null}")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PocketKnife.restoreInstanceState(this, savedInstanceState)
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        PocketKnife.saveInstanceState(this, outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SELECT_APPS -> {
            }
        }// do nothing
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
        if (key == Prefs.PREF_PIN) {
            // do something here
        }
    }

    private fun addPreferenceChangeListener(preferenceName: String) {
        val pref = findPreference(preferenceName)
        if (pref != null) {
            pref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, `object` -> this@SettingsFragment.doSomething() }
        }
    }

    private fun doSomething(): Boolean {
        return false
    }

    companion object {

        val DEV_MODE_REQUEST = 1111

        private val SELECT_APPS = 10

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

}