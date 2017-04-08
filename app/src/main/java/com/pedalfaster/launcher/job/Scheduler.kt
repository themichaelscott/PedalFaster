package com.pedalfaster.launcher.job

import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest

import javax.inject.Inject

class Scheduler @Inject
constructor()// Dagger Constructor
{

    fun scheduleBluetoothListenerJob() {
        scheduleBluetoothListenerJob(false)
    }

    fun scheduleBluetoothListenerJobNow() {
        scheduleBluetoothListenerJob(true)
    }

    private fun scheduleBluetoothListenerJob(now: Boolean) {
        val startExec = if (now) BluetoothListenerJob.EXECUTION_WINDOW_START_NOW else BluetoothListenerJob.EXECUTION_WINDOW_START
        val endExec = if (now) BluetoothListenerJob.EXECUTION_WINDOW_END_NOW else BluetoothListenerJob.EXECUTION_WINDOW_END

        JobRequest.Builder(BluetoothListenerJob.TAG)
                .setUpdateCurrent(true)
                .setRequirementsEnforced(true)
                .setExecutionWindow(startExec, endExec)
                .build()
                .schedule()
    }


    fun cancelBluetoothListenerJob() {
        JobManager.instance().cancelAllForTag(BluetoothListenerJob.TAG)
    }
}
