package com.pedalfaster.launcher.job

import com.evernote.android.job.Job
import com.pedalfaster.launcher.event.CheckBluetoothStatusEvent
import pocketbus.Bus
import timber.log.Timber
import javax.inject.Inject

class BluetoothListenerJob
@Inject constructor(private val bus: Bus) : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        Timber.d("BluetoothListenerJob executed")
        bus.post(CheckBluetoothStatusEvent())
        return Job.Result.SUCCESS
    }

    companion object {
        val TAG = "BluetoothListenerJob"
    }
}
