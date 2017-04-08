package com.pedalfaster.launcher.prefs

import android.content.SharedPreferences
import com.pedalfaster.launcher.prefs.ext.saveInt

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject
constructor(val preferences: SharedPreferences) {

    var pin: Int
        get() = preferences.getInt(PREF_PIN, 0)
        set(pin) = preferences.saveInt(PREF_PIN, pin)

    companion object {
        val PREF_PIN = "PREF_PIN"
    }

}