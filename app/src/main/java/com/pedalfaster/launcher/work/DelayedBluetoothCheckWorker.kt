package com.pedalfaster.launcher.work

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.event.TriggerBluetoothCheckEvent
import pocketbus.Bus
import timber.log.Timber
import javax.inject.Inject

class DelayedBluetoothCheckWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    @Inject
    lateinit var bus: Bus

    init {
        Injector.get().inject(this)
    }

    @WorkerThread
    override fun doWork(): Result {
        Timber.d("DelayedBluetoothCheckWorker executed")
        bus.post(TriggerBluetoothCheckEvent())
        return Result.SUCCESS
    }

    companion object {
        const val TAG = "DelayedBluetoothCheckWorker"
    }
}
