package com.pedalfaster.launcher.activity

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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

        // todo - kill existing pedalFasterView if it exists
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                getOverlayPermission()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun getOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            MaterialDialog.Builder(this)
                    .title("Permission Required")
                    .content("Pedal Faster needs permission to interrupt video playback on your device.")
                    .positiveText("OK")
                    .onPositive { _, _ ->
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName))
                        startActivityForResult(intent, OVERLAY_PERMISSION_CODE)
                    }
                    .build()
                    .show()
        }
    }

    fun launchYouTube() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            getOverlayPermission()
            return
        }
        if (prefs.keepPedalingEnabled && prefs.activeBluetoothDeviceAddress.isBlank()) {
            MaterialDialog.Builder(this)
                    .title("Unable to open YouTube")
                    .content("Apps can't be launched until there is a bluetooth device selected.\nPlease open \"Settings\" to select a device.")
                    .positiveText("OK")
                    .build()
                    .show()
            return
        }
        scheduler.schedulePedalFasterInterruptor()
        val launchYouTubeIntent = packageManager.getLaunchIntentForPackage("com.google.android.youtube")
        startActivity(launchYouTubeIntent)
    }

    fun onSettingsButtonClick(): Boolean {
        val settingIntent = Intent(this, PreferenceActivity::class.java)
        startActivity(settingIntent)
        return true
    }

    override fun onBackPressed() {
        // do nothing - prevents user from leaving launcher
    }

    companion object {
        val OVERLAY_PERMISSION_CODE = 2017
    }

}