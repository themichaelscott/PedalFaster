package com.pedalfaster.launcher.ux.app.enabled

import com.pedalfaster.launcher.domain.pedalfasterapp.PedalfasterApp

class EnabledAppsContract {
    interface View {
        fun showData(appList: List<PedalfasterApp>)
    }
}