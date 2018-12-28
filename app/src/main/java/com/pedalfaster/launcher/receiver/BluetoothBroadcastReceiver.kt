package com.pedalfaster.launcher.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothBroadcastReceiver
@Inject
constructor(
    private val pedalFasterController: PedalFasterController
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        Timber.i("Device status updated: $action, ${device.name}, address: ${device.address}")

        when (action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> pedalFasterController.updateBluetooth(device.address, BluetoothStatus.CONNECTED)
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> pedalFasterController.updateBluetooth(device.address, BluetoothStatus.DISCONNECTED)
        }
    }
}