package com.pedalfaster.launcher.prefs

import android.content.SharedPreferences
import com.pedalfaster.launcher.prefs.ext.saveLong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject
constructor(private val preferences: SharedPreferences) {

    var startupWindow: Long
        get() = preferences.getLong(PREF_STARTUP_WINDOW, DEFAULT_STARTUP_WINDOW)
        set(startupWindow) = preferences.saveLong(PREF_STARTUP_WINDOW, startupWindow)

    companion object {
        val PREF_STARTUP_WINDOW = "PREF_STARTUP_WINDOW"

        val DEFAULT_STARTUP_WINDOW = 60L
    }

}