package com.pedalfaster.launcher

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import androidx.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen
import com.pedalfaster.launcher.dagger.AppModule
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.logging.DebugTree
import com.pedalfaster.launcher.logging.ReleaseTree
import com.pedalfaster.launcher.prefs.Prefs
import com.pedalfaster.launcher.receiver.BluetoothBroadcastReceiver
import pocketbus.Registry
import timber.log.Timber
import javax.inject.Inject

@Registry
class App : Application() {

    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var bluetoothBroadcastReceiver: BluetoothBroadcastReceiver

    init {
        Injector.init(AppModule(this))
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Injector.get().inject(this)
        setupLogging()

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }
        registerReceiver(bluetoothBroadcastReceiver, filter)
    }

    private fun setupLogging() {
        when {
            BuildConfig.DEBUG -> Timber.plant(DebugTree())
            else -> Timber.plant(ReleaseTree())
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        when {
            filesDir != null -> MultiDex.install(this)
            else -> {
                // During app install it might have experienced "INSTALL_FAILED_DEXOPT" (reinstall is the only known work-around)
                // https://code.google.com/p/android/issues/detail?id=8886
                val message = getString(R.string.app_name) + " is in a bad state, please uninstall/reinstall"
                Timber.e(message)
            }
        }
    }

    companion object {

        const val DEFAULT_TAG_PREFIX = "pedal."

        private const val tempAuthenticated = false
        private val developmentMode = "debug".equals(BuildConfig.BUILD_TYPE, ignoreCase = true) // true to disable crash reports, analytics, etc..
        private const val devEnvironmentEnabled = false // enable developer settings/preferences
        private const val lastAppUseTS: Long = 0
    }


}
