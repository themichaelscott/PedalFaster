package com.pedalfaster.launcher.job

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class AppJobCreator @Inject
constructor(private val delayedBluetoothCheckJobProvider: Provider<DelayedBluetoothCheckJob>) : JobCreator {

    override fun create(tag: String): Job? {
        when (tag) {
            DelayedBluetoothCheckJob.TAG -> {
                return delayedBluetoothCheckJobProvider.get()
            }
            else -> {
                Timber.e("Cannot find job for tag [$tag]. Be sure to add creation to AppJobCreator")
                return null
            }
        }
    }

    companion object {
        private val TAG = AppJobCreator::class.java.simpleName
    }
}
