package com.pedalfaster.launcher.receiver

import android.app.Application
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager
import com.pedalfaster.launcher.prefs.Prefs
import com.pedalfaster.launcher.view.PedalFasterView
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PedalFasterController
@Inject constructor(private val application: Application, private val prefs: Prefs) {

    // todo - synchronize the map
    private var bluetoothStatusMap: MutableMap<String, BluetoothStatus> = mutableMapOf()
    var windowManager: WindowManager = application.getSystemService(WINDOW_SERVICE) as WindowManager
    var pedalFasterView: View? = null

    fun updateBluetooth(deviceAddress: String, status: BluetoothStatus) {
        bluetoothStatusMap.put(deviceAddress, status)
        if (deviceAddress == prefs.activeBluetoothDeviceAddress) {
            notifyOfBluetoothStatus()
        }
    }

    fun notifyOfBluetoothStatus() {
        if (getActiveDeviceStatus() == BluetoothStatus.CONNECTED) {
            dismissPedalFasterView()
        } else {
            val windowManagerParams = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT)
            pedalFasterView = PedalFasterView(application)
            windowManager.addView(pedalFasterView, windowManagerParams)
        }
    }

    fun dismissPedalFasterView() {
        if (pedalFasterView != null) {
            try {
                windowManager.removeView(pedalFasterView)
            } catch (e: IllegalArgumentException) {
                Timber.e(e, "View not attached?  Continue running app")
            }
        }
    }

    private fun getActiveDeviceStatus(): BluetoothStatus {
        val bluetoothStatus = bluetoothStatusMap[prefs.activeBluetoothDeviceAddress] ?: BluetoothStatus.UNKNOWN
        Timber.d("Bluetooth status for ${prefs.activeBluetoothDeviceAddress}: $bluetoothStatus")
        return bluetoothStatus
    }
}