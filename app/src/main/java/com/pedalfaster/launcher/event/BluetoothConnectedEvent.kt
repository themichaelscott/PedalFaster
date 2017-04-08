package com.pedalfaster.launcher.event

import org.threeten.bp.LocalDateTime

class BluetoothConnectedEvent(val connected: Boolean, val time: LocalDateTime)