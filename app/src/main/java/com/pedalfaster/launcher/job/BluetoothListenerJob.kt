package com.pedalfaster.launcher.job

import com.evernote.android.job.Job
import timber.log.Timber

import java.util.concurrent.TimeUnit

import javax.inject.Inject

class BluetoothListenerJob
@Inject
constructor(private val bluetoothChecker: BluetoothChecker) : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        Timber.d("BluetoothListenerJob executed")
        bluetoothChecker.check()
        return Job.Result.SUCCESS
    }

    companion object {
        val TAG = "BluetoothListenerJob"

        val EXECUTION_WINDOW_START_NOW = TimeUnit.SECONDS.toMillis(1)
        val EXECUTION_WINDOW_END_NOW = TimeUnit.SECONDS.toMillis(5)
        val EXECUTION_WINDOW_START = TimeUnit.SECONDS.toMillis(5)
        val EXECUTION_WINDOW_END = TimeUnit.SECONDS.toMillis(10)
    }
}
