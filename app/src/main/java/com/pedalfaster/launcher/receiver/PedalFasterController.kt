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

    fun updateBluetooth(deviceAddress: String, status: BluetoothStatus) {
        if (deviceAddress != prefs.bluetoothDeviceAddress) {
            return
        }
        when (status) {
            BluetoothStatus.CONNECTED -> bluetoothStatus = BluetoothStatus.CONNECTED
            BluetoothStatus.DISCONNECTED -> bluetoothStatus = BluetoothStatus.DISCONNECTED
            else -> {
                Timber.e("Unknown bluetooth status provided for: $deviceAddress")
            }
        }
        notifyOfBluetoothStatus()
    }

    fun notifyOfBluetoothStatus() {
        val intent = Intent(application, PedalFasterPopupActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(PedalFasterPopupActivity.EXTRA_BLUETOOTH_STATUS, bluetoothStatus)
        application.startActivity(intent)
    }

    fun resetBluetoothStatus() {
        bluetoothStatus = BluetoothStatus.UNKNOWN
    }

    companion object {
        // todo - change this to a map and track ALL bluetooth device connections
        // so if user wants to change their bluetooth pairing they can do it without toggling it on and off
        // to make sure it is recognized
        var bluetoothStatus: BluetoothStatus = BluetoothStatus.UNKNOWN
    }

}