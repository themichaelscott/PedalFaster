package com.pedalfaster.launcher.job

import com.evernote.android.job.Job
import com.pedalfaster.launcher.receiver.PedalFasterController
import timber.log.Timber
import javax.inject.Inject

class BluetoothListenerJob
@Inject constructor(private val pedalFasterController: PedalFasterController) : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        Timber.d("BluetoothListenerJob executed")
        pedalFasterController.notifyOfBluetoothStatus()
        return Job.Result.SUCCESS
    }

    companion object {
        val TAG = "BluetoothListenerJob"
    }
}
