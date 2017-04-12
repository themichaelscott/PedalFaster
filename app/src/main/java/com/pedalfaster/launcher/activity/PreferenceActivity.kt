package com.pedalfaster.launcher.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.fragment.SettingsFragment

class PreferenceActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)

        title = getString(R.string.prefs_title)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commit()
        }
    }
}