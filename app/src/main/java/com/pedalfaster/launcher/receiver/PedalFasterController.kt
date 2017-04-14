package com.pedalfaster.launcher.receiver

import android.app.Application
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.PixelFormat
import android.view.WindowManager
import com.pedalfaster.launcher.R
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

    var showAlert = true

    private var bluetoothStatusMap: MutableMap<String, BluetoothStatus> = mutableMapOf()
    private var windowManager: WindowManager = application.getSystemService(WINDOW_SERVICE) as WindowManager
    private val pedalFasterView = PedalFasterView(application)

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

    @Synchronized
    fun notifyOfBluetoothStatus() {
        when {
            !isPedalFasterTheDefaultLauncher() -> dismissPedalFasterView()
            !prefs.keepPedalingEnabled -> dismissPedalFasterView() // if keep pedaling is disabled, then make sure to dismiss any windows
            getActiveDeviceStatus() == BluetoothStatus.CONNECTED -> dismissPedalFasterView()
            showAlert -> showPedalFasterView()
        }
    }

    private fun showPedalFasterView() {
        val windowManagerParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT)
        try {
            windowManager.addView(pedalFasterView, windowManagerParams)
        } catch (e: IllegalStateException) {
            // todo - prevent this with concurrency checks
//            Timber.e("View already added.  Continue running app...")
        }
    }

    fun dismissPedalFasterView() {
        try {
            windowManager.removeView(pedalFasterView)
        } catch (e: IllegalArgumentException) {
            // todo - prevent this with concurrency checks
//            Timber.e("View not attached?  Continue running app...")
        }
    }

    private fun getActiveDeviceStatus(): BluetoothStatus {
        val bluetoothStatus = bluetoothStatusMap[prefs.activeBluetoothDeviceAddress] ?: BluetoothStatus.UNKNOWN
        Timber.d("Bluetooth status for ${prefs.activeBluetoothDeviceAddress}: $bluetoothStatus")
        return bluetoothStatus
    }

    private fun isPedalFasterTheDefaultLauncher(): Boolean {
        val packageManager = application.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.addCategory(Intent.CATEGORY_DEFAULT)

        val info = packageManager.resolveActivity(intent, 0)
        val applicationLabel = packageManager.getApplicationLabel(info.activityInfo.applicationInfo)
        Timber.d("Current default launcher - $applicationLabel")
        val pedalFasterActive = applicationLabel == application.getString(R.string.app_name)
        Timber.d("Pedal faster is active - $pedalFasterActive")
        return pedalFasterActive
    }
}