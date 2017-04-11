package com.pedalfaster.launcher.prefs

import android.content.SharedPreferences
import com.pedalfaster.launcher.prefs.ext.saveBoolean
import com.pedalfaster.launcher.prefs.ext.saveLong
import com.pedalfaster.launcher.prefs.ext.saveString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject
constructor(private val preferences: SharedPreferences) {

    var keepPedalingEnabled: Boolean
        get() = preferences.getBoolean(PREF_KEEP_PEDALING_ENABLED, true)
        set(enabled) = preferences.saveBoolean(PREF_KEEP_PEDALING_ENABLED, enabled)

    var startupDelayBeforePrompt: Long
        get() = preferences.getLong(PREF_STARTUP_DELAY_BEFORE_PROMPT, DEFAULT_STARTUP_WINDOW)
        set(startupWindow) = preferences.saveLong(PREF_STARTUP_DELAY_BEFORE_PROMPT, startupWindow)

    var activeBluetoothDeviceAddress: String
        get() = preferences.getString(PREF_BLUETOOTH_DEVICE_ADDRESS, DEFAULT_BLUETOOTH_DEVICE_ADDRESS)
        set(bluetoothDeviceAddress) = preferences.saveString(PREF_BLUETOOTH_DEVICE_ADDRESS, bluetoothDeviceAddress)

    companion object {
        val PREF_KEEP_PEDALING_ENABLED = "PREF_KEEP_PEDALING_ENABLED"
        val PREF_STARTUP_DELAY_BEFORE_PROMPT = "PREF_STARTUP_DELAY_BEFORE_PROMPT"
        val PREF_BLUETOOTH_DEVICE_ADDRESS = "PREF_BLUETOOTH_DEVICE_ADDRESS"

        val DEFAULT_STARTUP_WINDOW = 60L // in seconds

        private val DEFAULT_BLUETOOTH_DEVICE_ADDRESS = ""
    }

}