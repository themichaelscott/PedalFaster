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

    var pin: String
        get() = preferences.getString(PREF_PIN, "")
        set(pin) = preferences.saveString(PREF_PIN, pin)

    var pinLockExpiration: Long
        get() = preferences.getLong(PREF_PIN_LOCK_EXPIRATION, 0)
        set(long) = preferences.saveLong(PREF_PIN_LOCK_EXPIRATION, long)

    companion object {
        val PREF_KEEP_PEDALING_ENABLED = "PREF_KEEP_PEDALING_ENABLED"
        val PREF_STARTUP_DELAY_BEFORE_PROMPT = "PREF_STARTUP_DELAY_BEFORE_PROMPT"
        val PREF_BLUETOOTH_DEVICE_ADDRESS = "PREF_BLUETOOTH_DEVICE_ADDRESS"
        val PREF_ENABLED_APPS = "PREF_ENABLED_APPS"
        val PREF_PIN = "PREF_PIN"
        val PREF_PIN_LOCK_EXPIRATION = "PREF_PIN_LOCK_EXPIRATION"

        val DEFAULT_STARTUP_WINDOW = 60L // in seconds
        val PIN_LOCKOUT_EXPIRATION_IN_MINUTES = 10L

        private val DEFAULT_BLUETOOTH_DEVICE_ADDRESS = ""
    }

}