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
import com.pedalfaster.launcher.receiver.PedalFasterController
import kotlinx.android.synthetic.main.activity_home.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import timber.log.Timber
import javax.inject.Inject

class HomeActivity : FragmentActivity() {

    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var pedalFasterController: PedalFasterController

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        settingsButton.setOnClickListener { onSettingsButtonClick() }
        youTubeButton.setOnClickListener { launchYouTube() }
    }

    override fun onResume() {
        super.onResume()
        // kill existing pedalFasterView if it exists
        pedalFasterController.dismissPedalFasterView()
        pedalFasterController.showAlert = false // sets flag to turn off pedal faster alerts (until youtube is launched again)
        scheduler.cancelBluetoothListenerJob()
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            OVERLAY_PERMISSION_CODE -> if (!Settings.canDrawOverlays(this)) {
                getOverlayPermission()
            }
            PinActivity.REQUEST_CODE -> {
                when (resultCode) {
                    PinActivity.SUCCESS -> {
                        Timber.d("PIN Success - launching PreferenceActivity")
                        val settingIntent = Intent(this, PreferenceActivity::class.java)
                        startActivity(settingIntent)
                    }
                    PinActivity.FAIL -> {
                        Timber.d("PIN FAILED")
                        prefs.pinLockExpiration = LocalDateTime.now()
                                .plusMinutes(Prefs.PIN_LOCKOUT_EXPIRATION_IN_MINUTES)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()

                        MaterialDialog.Builder(this)
                                .title("Preferences Locked")
                                .content("You have been locked out of preferences for ${Prefs.PIN_LOCKOUT_EXPIRATION_IN_MINUTES} minutes.")
                                .positiveText("OK")
                                .build()
                                .show()
                    }
                    else -> {
                        // do nothing
                    }
                }
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
        val launchYouTubeIntent = packageManager.getLaunchIntentForPackage(YOUTUBE_PACKAGE)
        launchYouTubeIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivity(launchYouTubeIntent)
        pedalFasterController.showAlert = true
    }

    fun onSettingsButtonClick() {
        if (System.currentTimeMillis() > prefs.pinLockExpiration) {
            val intent = Intent(this, PinActivity::class.java)
            startActivityForResult(intent, PinActivity.REQUEST_CODE)
        } else {
            MaterialDialog.Builder(this)
                    .title("Preferences Locked")
                    .content("You have exceeded the maximum number of pin attempts. Please try again later.")
                    .positiveText("OK")
                    .build()
                    .show()
        }
    }

    override fun onBackPressed() {
        // do nothing - prevents user from leaving launcher
    }

    companion object {
        val OVERLAY_PERMISSION_CODE = 2017
        val YOUTUBE_PACKAGE = "com.google.android.youtube"
    }

}