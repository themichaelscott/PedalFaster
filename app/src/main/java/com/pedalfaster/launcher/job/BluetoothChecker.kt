package com.pedalfaster.launcher.job

import android.app.Application
import android.support.annotation.WorkerThread
import pocketbus.Bus
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothChecker
@Inject
constructor(val bus: Bus, val application: Application) {

    private val checking = AtomicBoolean(false)

    init {

    }

    @WorkerThread
    fun check(): Boolean {
        if (!checking.compareAndSet(false, true)) {
            Timber.i("Check already in progress...")
            return false
        }
        try {
            return performCheck()
        } finally {
            checking.set(false)
        }
    }
    private fun performCheck(): Boolean {
        Timber.i("Check Started")
        val success = true
        Timber.i("Check Ended")
        return success
    }

//    private fun performCheckOld(): Boolean {
//        Timber.i("Check Started")
//        val success = true
////        val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
////        val connectedDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT)
////        Timber.i("Connected device count: ${connectedDevices.size}")
////        connectedDevices.forEach { Timber.i("Name: ${it.name}, Address: ${it.address})") }
//
//        val defaultAdapter = BluetoothAdapter.getDefaultAdapter()
//        val remoteDevice: BluetoothDevice = defaultAdapter.getRemoteDevice("00:18:6B:4D:3D:26")
////        val uuid = UUID.randomUUID()
////        val uuid = remoteDevice.fetchUuidsWithSdp()
////        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // still doesn't work
//        val uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66") // still doesn't work
//        Timber.i("UUID: $uuid")
//        val bluetoothSocket = remoteDevice.createRfcommSocketToServiceRecord(uuid)
//        try {
//            bluetoothSocket.connect()
//        } catch (e: Exception) {
//            Timber.e(e)
//        } finally {
//            Timber.i("name ${remoteDevice.name} is connected ${bluetoothSocket.isConnected}")
//            if (bluetoothSocket.isConnected) {
//                bluetoothSocket.close()
//            }
//        }
////        Timber.i("GATT connection state ${bluetoothManager.getConnectionState(remoteDevice, BluetoothProfile.GATT)}")
////        Timber.i("GATT_SERVER connection state ${bluetoothManager.getConnectionState(remoteDevice, BluetoothProfile.GATT_SERVER)}")
//
////        val connectionState: Int =
////        bus.post(BluetoothConnectedEvent(connectionState == BluetoothProfile.STATE_CONNECTED, LocalDateTime.now()))
//        Timber.i("Check Ended")
//        return success
//    }

}
