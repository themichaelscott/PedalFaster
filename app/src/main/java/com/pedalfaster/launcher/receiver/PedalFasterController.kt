package com.pedalfaster.launcher.receiver

import android.app.Application
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager
import com.pedalfaster.launcher.event.CheckBluetoothStatusEvent
import com.pedalfaster.launcher.prefs.Prefs
import com.pedalfaster.launcher.view.PedalFasterView
import pocketbus.Bus
import pocketbus.Subscribe
import pocketbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PedalFasterController
@Inject constructor(private val application: Application, private val prefs: Prefs, bus: Bus) {


    // todo - change to property
    var showAlert = false

    // todo - synchronize the map
    private var bluetoothStatusMap: MutableMap<String, BluetoothStatus> = mutableMapOf()
    private var windowManager: WindowManager = application.getSystemService(WINDOW_SERVICE) as WindowManager
    private var pedalFasterView: View? = null

    init {
        bus.register(this)
    }

    fun updateBluetooth(deviceAddress: String, status: BluetoothStatus) {
        bluetoothStatusMap.put(deviceAddress, status)
        if (deviceAddress == prefs.activeBluetoothDeviceAddress) {
            notifyOfBluetoothStatus()
        }
    }

    @Subscribe(ThreadMode.MAIN)
    fun handle(event: CheckBluetoothStatusEvent) {
        notifyOfBluetoothStatus()
    }

    fun notifyOfBluetoothStatus() {
        when {
            getActiveDeviceStatus() == BluetoothStatus.CONNECTED -> dismissPedalFasterView()
            showAlert -> showPedalFasterView()
        }
    }

    private fun showPedalFasterView() {
        val windowManagerParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT)
        pedalFasterView = PedalFasterView(application)
        windowManager.addView(pedalFasterView, windowManagerParams)
    }

    fun dismissPedalFasterView() {
        if (pedalFasterView != null) {
            try {
                windowManager.removeView(pedalFasterView)
            } catch (e: IllegalArgumentException) {
                Timber.e(e, "View not attached?  Continue running app...")
            }
        }
    }

    private fun getActiveDeviceStatus(): BluetoothStatus {
        val bluetoothStatus = bluetoothStatusMap[prefs.activeBluetoothDeviceAddress] ?: BluetoothStatus.UNKNOWN
        Timber.d("Bluetooth status for ${prefs.activeBluetoothDeviceAddress}: $bluetoothStatus")
        return bluetoothStatus
    }
}