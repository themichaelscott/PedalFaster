package com.pedalfaster.launcher.fragment

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.text.InputType
import android.view.MenuItem
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.prefs.Prefs
import timber.log.Timber
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var prefs: Prefs

    init {
        Injector.get().inject(this)
    }

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
    }

    override fun onPause() {
        super.onPause()
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

    private fun buildFragment() {
        addPreferencesFromResource(R.xml.preferences)
        setupPreferences()
    }

    private fun setupPreferences() {
        findPreference(Prefs.PREF_STARTUP_DELAY_BEFORE_PROMPT)?.setOnPreferenceClickListener { onStartupDelayPrefClick() }
        updateStartupDelaySummary()
    }

    private fun onStartupDelayPrefClick(): Boolean {
        MaterialDialog.Builder(context)
                .title("Startup Delay")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Seconds to wait", prefs.startupDelayBeforePrompt.toString(), false) { _, input ->
                    Toast.makeText(context, input, Toast.LENGTH_SHORT).show()
                    prefs.startupDelayBeforePrompt = input.toString().toLong()
                    updateStartupDelaySummary()
                }
                .show()
        return true
    }

    private fun updateStartupDelaySummary(): Boolean {
        val startupDelayPref = findPreference(Prefs.PREF_STARTUP_DELAY_BEFORE_PROMPT)
        startupDelayPref?.summary = "Wait ${prefs.startupDelayBeforePrompt} seconds for pedaling once YouTube starts"
        return true
    }

    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

}