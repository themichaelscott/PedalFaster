package com.pedalfaster.launcher.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.event.BluetoothConnectedEvent
import com.pedalfaster.launcher.job.Scheduler
import kotlinx.android.synthetic.main.activity_home.*
import pocketbus.Bus
import pocketbus.Subscribe
import pocketbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

class HomeActivity : FragmentActivity() {

    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var bus: Bus

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bus.register(this)
        setContentView(R.layout.activity_home)
        settingsButton.setOnClickListener { onSettingsButtonClick() }
        youTubeButton.setOnClickListener { launchYouTube() }
    }

    override fun onDestroy() {
        bus.unregister(this)
        super.onDestroy()
    }

    fun launchYouTube() {
        val launchYouTube = packageManager.getLaunchIntentForPackage("com.google.android.youtube")
        scheduler.scheduleBluetoothListenerJob()
        startActivity(launchYouTube)
    }

    fun onSettingsButtonClick() {
        val settingIntent = Intent(this, PreferenceActivity::class.java)
        startActivity(settingIntent)
    }

    @Subscribe(ThreadMode.MAIN)
    fun handle(event: BluetoothConnectedEvent) {
        val message = "Event received - connected: ${event.connected}, ${event.time}"
        Timber.d(message)
        scheduler.cancelBluetoothListenerJob()
        when {
            event.connected -> {
                // find dialog by tag and dismiss it if it exists
            }
            else -> MaterialDialog.Builder(this)
                    .content(message)
                    .cancelable(false)
                    .tag(TAG)
                    .build()
                    .show()
        }
    }

    companion object {
        private val TAG = "HomeActivity"
    }
}