package com.pedalfaster.launcher.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.event.BluetoothConnectedEvent
import com.pedalfaster.launcher.job.Scheduler
import com.pedalfaster.launcher.prefs.Prefs
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
    @Inject
    lateinit var prefs: Prefs

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bus.register(this)
        setContentView(R.layout.activity_home)
        settingsButton.setOnLongClickListener { onSettingsButtonClick() }
        youTubeButton.setOnClickListener { launchYouTube() }
    }

    override fun onDestroy() {
        bus.unregister(this)
        super.onDestroy()
    }

    fun launchYouTube() {
        if (prefs.keepPedalingEnabled && prefs.bluetoothDeviceAddress.isBlank()) {
            MaterialDialog.Builder(this)
                    .title("Unable to open YouTube")
                    .content("Apps can't be launched until there is a bluetooth device selected.\nPlease open \"Settings\" to select a device.")
                    .positiveText("OK")
                    .build()
                    .show()
            return
        }
        val launchYouTube = packageManager.getLaunchIntentForPackage("com.google.android.youtube")
        scheduler.scheduleBluetoothListenerJob()
        startActivity(launchYouTube)
    }

    fun onSettingsButtonClick(): Boolean {
        val settingIntent = Intent(this, PreferenceActivity::class.java)
        startActivity(settingIntent)
        return true
    }

    @Subscribe(ThreadMode.MAIN)
    fun handle(event: BluetoothConnectedEvent) {
        Timber.d("Event received - connected: ${event.connected}, ${event.time}")
        Timber.d("keepPedalingEnabled = ${prefs.keepPedalingEnabled}")
        scheduler.cancelBluetoothListenerJob()
        if (!prefs.keepPedalingEnabled) {
            return
        }
        when {
            event.connected -> {
                val keepPedalingDialog = supportFragmentManager.findFragmentByTag(KeepPedalingDialogFragment.TAG)
                if (keepPedalingDialog is KeepPedalingDialogFragment) {
                    keepPedalingDialog.dismiss()
                }
            }
            else -> {
                // todo - now that it is using a dialog fragment, we can't pop a message when youtube launches because
                // onSaveInstanceState has been called when user leaves HomeActivity
                // because of this show() throws an exception
                KeepPedalingDialogFragment()
                        .show(supportFragmentManager, KeepPedalingDialogFragment.TAG)
            }
        }
    }

    class KeepPedalingDialogFragment : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return MaterialDialog.Builder(context)
                    .content("Keep pedaling") // todo - replace with icon of bicycle
                    .cancelable(false)
                    .tag(TAG)
                    .build()
        }

        companion object {
            val TAG: String = KeepPedalingDialogFragment::class.java.simpleName
        }
    }
}