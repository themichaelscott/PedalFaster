package com.pedalfaster.launcher.prefs.ext

import android.content.SharedPreferences

inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

fun SharedPreferences.saveString(key: String, value: String) {
    val editor = edit()
    editor.putString(key, value)
    editor.apply()
}

fun SharedPreferences.saveLong(key: String, value: Long) {
    val editor = edit()
    editor.putLong(key, value)
    editor.apply()
}

fun SharedPreferences.saveInt(key: String, value: Int) {
    val editor = edit()
    editor.putInt(key, value)
    editor.apply()
}

fun SharedPreferences.saveFloat(key: String, value: Float) {
    val editor = edit()
    editor.putFloat(key, value)
    editor.apply()
}

fun SharedPreferences.saveBoolean(key: String, value: Boolean) {
    val editor = edit()
    editor.putBoolean(key, value)
    editor.apply()
}

fun SharedPreferences.remove(key: String) {
    val editor = edit()
    editor.remove(key)
    editor.apply()
}

fun SharedPreferences.reset() {
    // clear ALL preferences
    val editor = edit()
    editor.clear()
    editor.apply()
}

fun SharedPreferences.registerChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
    registerOnSharedPreferenceChangeListener(listener)
}

fun SharedPreferences.unregisterChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
    unregisterOnSharedPreferenceChangeListener(listener)
}