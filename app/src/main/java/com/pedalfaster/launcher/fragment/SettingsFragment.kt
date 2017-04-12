package com.pedalfaster.launcher.fragment

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.text.InputType
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.prefs.Prefs
import com.pedalfaster.launcher.receiver.PedalFasterController
import timber.log.Timber
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var pedalFasterController: PedalFasterController

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
        // startup delay
        findPreference(Prefs.PREF_STARTUP_DELAY_BEFORE_PROMPT)?.setOnPreferenceClickListener { onStartupDelayPrefClick() }
        updateStartupDelaySummary()

        // bluetooth device
        findPreference(Prefs.PREF_BLUETOOTH_DEVICE_ADDRESS)?.setOnPreferenceClickListener { onBluetoothPrefClick() }
        updateBluetoothDeviceSummary()

    }

    private fun onStartupDelayPrefClick(): Boolean {
        MaterialDialog.Builder(context)
                .title(getString(R.string.prefs_startup_delay_title))
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(getString(R.string.prefs_startup_delay_coaching_message), prefs.startupDelayBeforePrompt.toString(), false) { _, input ->
                    prefs.startupDelayBeforePrompt = input.toString().toLong()
                    updateStartupDelaySummary()
                }
                .show()
        return true
    }

    private fun updateStartupDelaySummary(): Boolean {
        val startupDelayPref = findPreference(Prefs.PREF_STARTUP_DELAY_BEFORE_PROMPT)
        startupDelayPref?.summary = getString(R.string.prefs_startup_delay_message, prefs.startupDelayBeforePrompt)
        return true
    }

    private fun onBluetoothPrefClick(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val bondedDevicesList = bluetoothAdapter.bondedDevices
        val deviceAddressList = bondedDevicesList.map { it.address }
        val deviceNameDisplayList = bondedDevicesList.map { "${it.name}\n${it.address}" }

        if (deviceNameDisplayList.isEmpty()) {
            MaterialDialog.Builder(context)
                    .title(getString(R.string.prefs_no_bluetooth_devices_title))
                    .content(getString(R.string.prefs_no_bluetooth_devices_message))
                    .positiveText(R.string.ok)
                    .build()
                    .show()
            return true
        }

        var selectedPosition = -1
        deviceAddressList.forEachIndexed { index, s ->
            if (s == prefs.activeBluetoothDeviceAddress) {
                selectedPosition = index
                return@forEachIndexed
            }
        }

        MaterialDialog.Builder(context)
                .title(getString(R.string.prefs_bluetooth_devices_title))
                .items(deviceNameDisplayList)
                .itemsCallbackSingleChoice(selectedPosition) { _, _, which, _ ->
                    if (which >= 0 && which < deviceAddressList.size) {
                        prefs.activeBluetoothDeviceAddress = deviceAddressList[which]
                        updateBluetoothDeviceSummary()
                    }
                    return@itemsCallbackSingleChoice true
                }
                .positiveText(R.string.ok)
                .show()
        return true
    }

    private fun updateBluetoothDeviceSummary(): Boolean {
        val bluetoothDevicePref = findPreference(Prefs.PREF_BLUETOOTH_DEVICE_ADDRESS)
        val bluetoothAddress = prefs.activeBluetoothDeviceAddress
        if (bluetoothAddress.isBlank() || !BluetoothAdapter.checkBluetoothAddress(bluetoothAddress)) {
            bluetoothDevicePref?.summary = getString(R.string.prefs_selected_bluetooth_empty)
        } else {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothAddress)
            bluetoothDevicePref?.summary = bluetoothDevice.name
        }
        return true
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

}