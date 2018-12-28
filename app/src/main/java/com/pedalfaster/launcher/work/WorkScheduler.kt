package com.pedalfaster.launcher.work

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.pedalfaster.launcher.prefs.Prefs
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkScheduler
@Inject
constructor(
    private val prefs: Prefs,
    private val workManager: WorkManager
) {

    fun schedulePedalFasterInterrupter() {
        val workRequest = OneTimeWorkRequest.Builder(DelayedBluetoothCheckWorker::class.java)
            .addTag(DelayedBluetoothCheckWorker.TAG)
            .setInitialDelay(prefs.startupDelayBeforePrompt, TimeUnit.SECONDS)
            .build()
        workManager.enqueue(workRequest)
    }

    fun cancelPedalFasterInterrupter() {
        Timber.d("Bluetooth work cancel requested")
        val canceledWork = workManager.cancelAllWorkByTag(DelayedBluetoothCheckWorker.TAG)
        Timber.d("Bluetooth work cancelled: %s", canceledWork)
    }

}
