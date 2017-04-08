package com.pedalfaster.launcher.job

import android.support.annotation.WorkerThread
import com.pedalfaster.launcher.event.BluetoothConnectedEvent
import org.threeten.bp.LocalDateTime
import pocketbus.Bus
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothChecker
@Inject
constructor() {

    @Inject
    lateinit var bus: Bus

    private val checking = AtomicBoolean(false)

    init {
    }

    @WorkerThread
    fun check(): Boolean {
        if (!checking.compareAndSet(false, true)) {
            Timber.i("Check already in progress...")
            return false
        }
        try {
            return performCheck()
        } finally {
            checking.set(false)
        }
    }

    private fun performCheck(): Boolean {
        Timber.i("Check Started")
        val success = true
        bus.post(BluetoothConnectedEvent(true, LocalDateTime.now()))
        Timber.i("Check Started")
        return success
    }

}
