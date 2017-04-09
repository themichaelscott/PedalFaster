package com.pedalfaster.launcher.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pedalfaster.launcher.event.BluetoothConnectedEvent
import org.threeten.bp.LocalDateTime
import pocketbus.Bus
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothBroadcastReceiver
@Inject constructor(private val bus: Bus) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        Timber.i("Device status updated: $action, ${device.name}, address: ${device.address}")
        if (device.address != bluetoothAddress) {
            return
        }
        when (action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> bluetoothConnection = true
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> bluetoothConnection = false
        }
        bus.post(BluetoothConnectedEvent(bluetoothConnection, LocalDateTime.now()))
    }

    companion object {
        var bluetoothConnection: Boolean = false
        var bluetoothAddress = "00:18:6B:4D:3D:26"
    }

}