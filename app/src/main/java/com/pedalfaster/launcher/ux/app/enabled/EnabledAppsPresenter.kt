package com.pedalfaster.launcher.ux.app.enabled

import com.pedalfaster.launcher.prefs.Prefs
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

    fun loadData() {
        view.showData()
    }

}