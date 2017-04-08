package com.pedalfaster.launcher.event

import org.threeten.bp.LocalDateTime

class BluetoothConnectedEvent(val running: Boolean, val time: LocalDateTime)