package com.pedalfaster.launcher.domain.pedalfasterapp

import android.graphics.drawable.Drawable

class PedalfasterApp : PedalfasterAppBaseRecord() {
    var icon: Drawable? = null

    override fun toString(): String {
        return """appName: $appName
            packageName: $packageName"""
    }
}