package com.pedalfaster.launcher.job

import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.pedalfaster.launcher.prefs.Prefs
import timber.log.Timber
import java.util.concurrent.TimeUnit

import javax.inject.Inject

class Scheduler
@Inject constructor(val prefs: Prefs) {

    fun schedulePedalFasterInterrupter() {
        val startupWindow: Long = TimeUnit.SECONDS.toMillis(prefs.startupDelayBeforePrompt)
        val startupWindowBuffer = startupWindow + STARTUP_WINDOW_BUFFER_MS
        JobRequest.Builder(DelayedBluetoothCheckJob.TAG)
                .setUpdateCurrent(true) // if an app is launched again, this will restart the counter for the bluetooth to start
                .setExecutionWindow(startupWindow, startupWindowBuffer)
                .build()
                .schedule()
    }

    fun cancelPedalFasterInterrupter() {
        Timber.d("Bluetooth job cancel requested")
        val canceledJobs = JobManager.instance().cancelAllForTag(DelayedBluetoothCheckJob.TAG)
        Timber.d("Bluetooth jobs cancelled: $canceledJobs")
    }

    companion object {
        private val STARTUP_WINDOW_BUFFER_MS = 5000
    }
}
