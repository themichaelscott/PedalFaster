package com.pedalfaster.launcher.prefs

import android.content.SharedPreferences
import com.pedalfaster.launcher.prefs.ext.saveBoolean
import com.pedalfaster.launcher.prefs.ext.saveLong
import com.pedalfaster.launcher.prefs.ext.saveString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs
@Inject
constructor(
    private val preferences: SharedPreferences
) {

    var keepPedalingEnabled: Boolean
        get() = preferences.getBoolean(PREF_KEEP_PEDALING_ENABLED, true)
        set(enabled) = preferences.saveBoolean(PREF_KEEP_PEDALING_ENABLED, enabled)

    var startupDelayBeforePrompt: Long
        get() = preferences.getLong(PREF_STARTUP_DELAY_BEFORE_PROMPT, DEFAULT_STARTUP_DELAY)
        set(startupDelay) = preferences.saveLong(PREF_STARTUP_DELAY_BEFORE_PROMPT, startupDelay)

    var delayBeforePrompt: Long
        get() = preferences.getLong(PREF_DELAY_BEFORE_PROMPT, DEFAULT_DELAY)
        set(delay) = preferences.saveLong(PREF_DELAY_BEFORE_PROMPT, delay)

    var activeBluetoothDeviceAddress: String
        get() = preferences.getString(PREF_BLUETOOTH_DEVICE_ADDRESS, DEFAULT_BLUETOOTH_DEVICE_ADDRESS) ?: DEFAULT_BLUETOOTH_DEVICE_ADDRESS
        set(bluetoothDeviceAddress) = preferences.saveString(PREF_BLUETOOTH_DEVICE_ADDRESS, bluetoothDeviceAddress)

    var pin: String
        get() = preferences.getString(PREF_PIN, "") ?: ""
        set(pin) = preferences.saveString(PREF_PIN, pin)

    var pinLockExpiration: Long
        get() = preferences.getLong(PREF_PIN_LOCK_EXPIRATION, 0)
        set(long) = preferences.saveLong(PREF_PIN_LOCK_EXPIRATION, long)

    companion object {
        const val PREF_KEEP_PEDALING_ENABLED = "PREF_KEEP_PEDALING_ENABLED"
        const val PREF_STARTUP_DELAY_BEFORE_PROMPT = "PREF_STARTUP_DELAY_BEFORE_PROMPT"
        const val PREF_DELAY_BEFORE_PROMPT = "PREF_DELAY_BEFORE_PROMPT"
        const val PREF_BLUETOOTH_DEVICE_ADDRESS = "PREF_BLUETOOTH_DEVICE_ADDRESS"
        const val PREF_PIN = "PREF_PIN"
        const val PREF_PIN_LOCK_EXPIRATION = "PREF_PIN_LOCK_EXPIRATION"

        const val DEFAULT_STARTUP_DELAY = 60L // in seconds
        const val DEFAULT_DELAY = 2L // in seconds
        const val PIN_LOCKOUT_EXPIRATION_IN_MINUTES = 10L

        private const val DEFAULT_BLUETOOTH_DEVICE_ADDRESS = ""
    }

}