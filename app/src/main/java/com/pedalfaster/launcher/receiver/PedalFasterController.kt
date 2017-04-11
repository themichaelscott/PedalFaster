package com.pedalfaster.launcher.receiver

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.pedalfaster.launcher.activity.PedalFasterPopupActivity
import com.pedalfaster.launcher.prefs.Prefs
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PedalFasterController
@Inject constructor(private val application: Application, private val prefs: Prefs) {

    // todo - synchronize the map
    private var bluetoothStatusMap: MutableMap<String, BluetoothStatus> = mutableMapOf()

    fun updateBluetooth(deviceAddress: String, status: BluetoothStatus) {
        bluetoothStatusMap.put(deviceAddress, status)
        if (deviceAddress == prefs.activeBluetoothDeviceAddress) {
            notifyOfBluetoothStatus()
        }
    }

    fun notifyOfBluetoothStatus() {
        val bluetoothStatus = getActiveDeviceStatus()
        val intent = Intent(application, PedalFasterPopupActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(PedalFasterPopupActivity.EXTRA_BLUETOOTH_STATUS, bluetoothStatus)
        application.startActivity(intent)
    }

    private fun getActiveDeviceStatus(): BluetoothStatus {
        val bluetoothStatus = bluetoothStatusMap[prefs.activeBluetoothDeviceAddress] ?: BluetoothStatus.UNKNOWN
        Timber.d("Bluetooth status for ${prefs.activeBluetoothDeviceAddress}: $bluetoothStatus")
        return bluetoothStatus
    }
}