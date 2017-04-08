package com.pedalfaster.launcher.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.job.AppJobCreator
import com.pedalfaster.launcher.job.Scheduler
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : FragmentActivity() {

    @Inject
    lateinit var appJobCreator: AppJobCreator
    @Inject
    lateinit var scheduler: Scheduler

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        settingsButton.setOnClickListener { onSettingsButtonClick() }
        youTubeButton.setOnClickListener { launchYouTube() }
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

}