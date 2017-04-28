package com.pedalfaster.launcher.ux.app.enabled

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import javax.inject.Inject

class EnabledAppsActivity : AppCompatActivity(), EnabledAppsContract.View {

    @Inject
    lateinit var enabledAppsPresenter: EnabledAppsPresenter

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enabled_apps)
        enabledAppsPresenter.init(this)
        enabledAppsPresenter.loadData()
    }

    override fun onStart() {
        super.onStart()
        enabledAppsPresenter.register()
    }

    override fun onStop() {
        enabledAppsPresenter.unregister()
        super.onStop()
    }

    override fun showData() {
        Toast.makeText(this, "Show Data", Toast.LENGTH_SHORT).show()
    }
}
