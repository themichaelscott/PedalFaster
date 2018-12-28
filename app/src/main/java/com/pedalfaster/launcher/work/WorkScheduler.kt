package com.pedalfaster.launcher.work

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.pedalfaster.launcher.prefs.Prefs
import com.pedalfaster.launcher.receiver.InterruptionType
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkScheduler
@Inject
constructor(
    private val prefs: Prefs,
    private val workManager: WorkManager
) {

    fun schedulePedalFasterInterrupter(interruptionType: InterruptionType) {
        val delay = when (interruptionType) {
            InterruptionType.LAUNCHED -> prefs.startupDelayBeforePrompt
            InterruptionType.DISCONNECTED -> TOLERANCE
        }
        val workRequest = OneTimeWorkRequest.Builder(DelayedBluetoothCheckWorker::class.java)
            .addTag(DelayedBluetoothCheckWorker.TAG)
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .build()
        workManager.beginUniqueWork(
            DelayedBluetoothCheckWorker.TAG,
            ExistingWorkPolicy.REPLACE,
            workRequest
        ).enqueue()
    }

    fun cancelPedalFasterInterrupter() {
        Timber.d("Bluetooth work cancel requested")
        val canceledWork = workManager.cancelUniqueWork(DelayedBluetoothCheckWorker.TAG)
        Timber.d("Bluetooth work cancelled: %s", canceledWork)
    }

    companion object {
        const val TOLERANCE = 1L // seconds
    }
}
