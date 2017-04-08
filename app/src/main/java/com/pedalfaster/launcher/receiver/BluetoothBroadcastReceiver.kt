package com.pedalfaster.launcher.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothBroadcastReceiver @Inject
constructor() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

        // todo - implement actions to be taken
        when (action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> Timber.i("Device connected: ${device.name}, address: ${device.address}")
            BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED -> Timber.i("Device is about to disconnect: ${device.name}, address: ${device.address}")
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> Timber.i("Device has disconnected: ${device.name}, address: ${device.address}")
        }
    }

}