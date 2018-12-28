package com.pedalfaster.launcher.activity

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
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
        scheduler.cancelPedalFasterInterrupter()
    }

    override fun onPause() {
        // any time we navigate from the launcher, we will interrupt
        scheduler.schedulePedalFasterInterrupter()
        pedalFasterController.showAlert = true
        super.onPause()
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
                            .title(getString(R.string.prefs_locked_title))
                            .content(getString(R.string.prefs_locked_message, Prefs.PIN_LOCKOUT_EXPIRATION_IN_MINUTES))
                            .positiveText(getString(R.string.ok))
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
                .title(R.string.permission_required_title)
                .content(R.string.permission_required_overlay_message)
                .positiveText(R.string.ok)
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
                .title(R.string.bluetooth_required_title)
                .content(R.string.bluetooth_required_message)
                .positiveText(R.string.ok)
                .build()
                .show()
            return
        }
        if (!pedalFasterController.isPedalFasterTheDefaultLauncher()) {
            MaterialDialog.Builder(this)
                .content(getString(R.string.required_to_be_default_launcher_message))
                .positiveText(R.string.ok)
                .build()
                .show()
            return
        }
        // TODO - exclude from recents isn't working as expected on a v21 device - works find on a 7.x
        val launchYouTubeIntent = packageManager.getLaunchIntentForPackage(YOUTUBE_PACKAGE)
        launchYouTubeIntent?.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivity(launchYouTubeIntent)
    }

    fun onSettingsButtonClick() {
        if (System.currentTimeMillis() > prefs.pinLockExpiration) {
            val intent = Intent(this, PinActivity::class.java)
            startActivityForResult(intent, PinActivity.REQUEST_CODE)
        } else {
            MaterialDialog.Builder(this)
                .title(R.string.prefs_locked_title)
                .content(R.string.prefs_locked_message_try_again)
                .positiveText(R.string.ok)
                .build()
                .show()
        }
    }

    override fun onBackPressed() {
        // do nothing - prevents user from leaving launcher
    }

    companion object {
        const val OVERLAY_PERMISSION_CODE = 2017
        const val YOUTUBE_PACKAGE = "com.google.android.youtube"
    }

}