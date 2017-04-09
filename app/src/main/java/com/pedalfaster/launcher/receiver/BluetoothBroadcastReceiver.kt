package com.pedalfaster.launcher.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pedalfaster.launcher.event.BluetoothConnectedEvent
import com.pedalfaster.launcher.prefs.Prefs
import org.threeten.bp.LocalDateTime
import pocketbus.Bus
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothBroadcastReceiver
@Inject constructor(private val bus: Bus, private val prefs: Prefs) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        Timber.i("Device status updated: $action, ${device.name}, address: ${device.address}")
        if (device.address != prefs.bluetoothDeviceAddress) {
            return
        }
        when (action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> bluetoothConnection = true
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> bluetoothConnection = false
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