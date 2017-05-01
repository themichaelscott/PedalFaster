package com.pedalfaster.launcher.ux.app.enabled

import com.pedalfaster.launcher.domain.pedalfasterapp.PedalfasterApp
import com.pedalfaster.launcher.prefs.Prefs
import timber.log.Timber
import javax.inject.Inject

class EnabledAppsPresenter
@Inject constructor(val prefs: Prefs) {

    private lateinit var view: EnabledAppsContract.View

    fun init(view: EnabledAppsContract.View) {
        this.view = view
    }

    fun register() {

    }

    fun unregister() {

    }

    fun loadData(pedalfasterAppList: List<PedalfasterApp>) {
        view.showData(pedalfasterAppList)
        debug(pedalfasterAppList)
    }

    private fun debug(pedalfasterAppList: List<PedalfasterApp>) {
        for (app in pedalfasterAppList) {
            Timber.d(app.toString())
        }
        Timber.d("Size: ${pedalfasterAppList.size}")
    }

}