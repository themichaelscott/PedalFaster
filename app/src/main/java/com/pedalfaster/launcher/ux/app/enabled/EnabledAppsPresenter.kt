package com.pedalfaster.launcher.ux.app.enabled

import com.pedalfaster.launcher.domain.pedalfasterapp.PedalfasterApp
import com.pedalfaster.launcher.domain.pedalfasterapp.PedalfasterAppManager
import com.pedalfaster.launcher.prefs.Prefs
import timber.log.Timber
import javax.inject.Inject

class EnabledAppsPresenter
@Inject constructor(val prefs: Prefs, val pedalfasterAppManager: PedalfasterAppManager) {

    private lateinit var view: EnabledAppsContract.View

    fun init(view: EnabledAppsContract.View) {
        this.view = view
    }

    fun register() {

    }

    fun unregister() {

    }

    fun loadData(pedalfasterAppList: List<PedalfasterApp>) {
        pedalfasterAppList.forEach {
            val app = pedalfasterAppManager.findByPackageName(it.packageName)
            it.enabled = app?.enabled ?: false
        }
        view.showData(pedalfasterAppList)
        debug(pedalfasterAppList)
    }

    private fun debug(pedalfasterAppList: List<PedalfasterApp>) {
        for (app in pedalfasterAppList) {
            Timber.d(app.toString())
        }
        Timber.d("Size: ${pedalfasterAppList.size}")
    }

    fun onClick(clickedPedalfasterApp: PedalfasterApp, enabled: Boolean) {
        var pedalfasterApp = pedalfasterAppManager.findByPackageName(clickedPedalfasterApp.packageName)
        if (pedalfasterApp == null) {
            pedalfasterApp = clickedPedalfasterApp
        }
        pedalfasterApp.enabled = enabled
        pedalfasterAppManager.save(pedalfasterApp)
    }

}