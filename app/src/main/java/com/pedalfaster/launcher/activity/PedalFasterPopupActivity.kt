package com.pedalfaster.launcher.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.receiver.BluetoothStatus
import pocketknife.BindExtra
import pocketknife.PocketKnife

class PedalFasterPopupActivity : FragmentActivity() {

    @BindExtra
    var bluetoothStatus: BluetoothStatus = BluetoothStatus.UNKNOWN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        PocketKnife.bindExtras(this)
        handleLaunch()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        PocketKnife.bindExtras(this, intent)
        handleLaunch()
    }

    override fun onBackPressed() {
        // don't allow the user to exit this activity
    }

    fun handleLaunch() {
        when(bluetoothStatus) {
            BluetoothStatus.CONNECTED -> {
                finish()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
            BluetoothStatus.DISCONNECTED,
            BluetoothStatus.UNKNOWN -> {
                setContentView(R.layout.activity_pedal_faster_popup)
            }
        }
    }

    companion object {
        val EXTRA_BLUETOOTH_STATUS = "EXTRA_BLUETOOTH_STATUS"
    }
}