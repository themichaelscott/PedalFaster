package com.pedalfaster.launcher.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.prefs.Prefs
import com.pedalfaster.launcher.receiver.InterruptionType
import com.pedalfaster.launcher.receiver.PedalFasterController
import com.pedalfaster.launcher.work.WorkScheduler
import kotlinx.android.synthetic.main.activity_pin.*
import me.eugeniomarletti.extras.bundle.BundleExtra
import me.eugeniomarletti.extras.bundle.base.Int
import me.eugeniomarletti.extras.bundle.base.String
import timber.log.Timber
import javax.inject.Inject

class PinActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var pedalFasterController: PedalFasterController
    @Inject
    lateinit var scheduler: WorkScheduler

    private var newPin = DEFAULT_PIN
    private var attemptsRemaining = DEFAULT_PIN_ATTEMPTS

    init {
        Injector.get().inject(this)
    }

    private val pinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            handlePin(pin)
        }

        override fun onEmpty() {} // do nothing
        override fun onPinChange(pinLength: Int, intermediatePin: String) {} // do nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)
        savedInstanceState?.let {
            with(SaveStateOptions) {
                newPin = it.newPin
                attemptsRemaining = it.attemptsRemaining
            }
        }
        pinLockView.attachIndicatorDots(indicatorDots)
        pinLockView.setPinLockListener(pinLockListener)
        indicatorDots.indicatorType = IndicatorDots.IndicatorType.FIXED
        if (prefs.pin.isBlank()) {
            updateMessage(getString(R.string.pin_create))
        } else {
            updateMessage(getString(R.string.pin_enter))
        }
    }

    override fun onResume() {
        super.onResume()
        scheduler.cancelPedalFasterInterrupter()
        pedalFasterController.showAlert = false
    }

    override fun onPause() {
        scheduler.schedulePedalFasterInterrupter(InterruptionType.LAUNCHED)
        pedalFasterController.showAlert = true
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(SaveStateOptions) {
            outState.newPin = newPin
            outState.attemptsRemaining = attemptsRemaining
        }

    }

    private fun resetPinFields() {
        pinLockView.resetPinLockView()
    }

    private fun updateMessage(message: String) {
        pinMessageTextView.text = message
    }

    private fun handlePin(pin: String) {
        Timber.d("Pin entered: $pin")
        when {
            prefs.pin.isNotBlank() -> {
                attemptsRemaining--
                when {
                    prefs.pin == pin -> {
                        Timber.d("PIN success")
                        updateMessage(getString(R.string.pin_success))
                        setResult(SUCCESS)
                        finish()
                    }
                    attemptsRemaining > 0 -> {
                        Timber.d("PIN fail")
                        updateMessage(getString(R.string.pin_fail))
                    }
                    else -> {
                        Timber.d("Too many tries")
                        updateMessage(getString(R.string.pin_max_attempts_exceeded))
                        setResult(FAIL)
                        finish()
                    }
                }
            }
            newPin.isBlank() -> {
                newPin = pin
                Timber.d("PIN1 saved")
                updateMessage(getString(R.string.pin_reenter))
            }
            newPin == pin -> {
                // saves the PIN
                Timber.d("new pin saved")
                prefs.pin = pin
                updateMessage(getString(R.string.pin_accepted))
                setResult(SUCCESS)
                finish()
            }
            else -> {
                Timber.d("pins didn't match")
                updateMessage(getString(R.string.pin_setup_no_match))
                newPin = "" // clears the pin
            }
        }
        resetPinFields()
    }

    object SaveStateOptions {
        var Bundle.newPin by BundleExtra.String(defaultValue = DEFAULT_PIN)
        var Bundle.attemptsRemaining by BundleExtra.Int(defaultValue = DEFAULT_PIN_ATTEMPTS)
    }

    companion object {
        const val REQUEST_CODE = 1234 // the kind of combination an idiot would have on his luggage
        const val SUCCESS = 1
        const val FAIL = 2
        const val DEFAULT_PIN = ""
        const val DEFAULT_PIN_ATTEMPTS = 3
    }
}