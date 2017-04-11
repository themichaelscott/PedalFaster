package com.pedalfaster.launcher.receiver

import com.pedalfaster.launcher.event.BluetoothConnectedEvent
import com.pedalfaster.launcher.prefs.Prefs
import org.threeten.bp.LocalDateTime
import pocketbus.Bus
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PedalFasterController
@Inject constructor(private val bus: Bus, private val prefs: Prefs) {

    fun updateBluetooth(deviceAddress: String, status: BluetoothStatus) {
        if (deviceAddress != prefs.bluetoothDeviceAddress) {
            return
        }
        when (status) {
            BluetoothStatus.CONNECTED -> bluetoothConnection = true
            BluetoothStatus.DISCONNECTED -> bluetoothConnection = false
            else -> {
                Timber.e("Unknown bluetooth status provided for: $deviceAddress")
            }
        }
        bus.post(BluetoothConnectedEvent(bluetoothConnection, LocalDateTime.now()))
    }

    companion object {
        // todo - change this to a map and track ALL bluetooth device connections
        // so if user wants to change their bluetooth pairing they can do it without toggling it on and off
        // to make sure it is recognized
        var bluetoothConnection: Boolean = false
    }

}