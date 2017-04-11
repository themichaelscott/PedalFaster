package com.pedalfaster.launcher.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.job.Scheduler
import com.pedalfaster.launcher.prefs.Prefs
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : FragmentActivity() {

    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var prefs: Prefs

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        settingsButton.setOnLongClickListener { onSettingsButtonClick() }
        youTubeButton.setOnClickListener { launchYouTube() }
    }

    fun launchYouTube() {
        if (prefs.keepPedalingEnabled && prefs.activeBluetoothDeviceAddress.isBlank()) {
            MaterialDialog.Builder(this)
                    .title("Unable to open YouTube")
                    .content("Apps can't be launched until there is a bluetooth device selected.\nPlease open \"Settings\" to select a device.")
                    .positiveText("OK")
                    .build()
                    .show()
            return
        }
        scheduler.scheduleBluetoothListenerJob()
        val launchYouTubeIntent = packageManager.getLaunchIntentForPackage("com.google.android.youtube")
        launchYouTubeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(launchYouTubeIntent)
    }

    fun onSettingsButtonClick(): Boolean {
        val settingIntent = Intent(this, PreferenceActivity::class.java)
        startActivity(settingIntent)
        return true
    }

}