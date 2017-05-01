package com.pedalfaster.launcher.ux.app.enabled

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.domain.pedalfasterapp.PedalfasterApp
import com.pedalfaster.launcher.job.Scheduler
import com.pedalfaster.launcher.receiver.PedalFasterController
import javax.inject.Inject


class EnabledAppsActivity : AppCompatActivity(), EnabledAppsContract.View {

    @Inject
    lateinit var enabledAppsPresenter: EnabledAppsPresenter
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var pedalFasterController: PedalFasterController

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enabled_apps)
        enabledAppsPresenter.init(this)
        enabledAppsPresenter.loadData(getAvailableApps())
    }

    override fun onStart() {
        super.onStart()
        enabledAppsPresenter.register()
        pedalFasterController.showAlert = false // sets flag to turn off pedal faster alerts (until youtube is launched again)
        scheduler.cancelPedalFasterInterrupter()
    }

    override fun onStop() {
        enabledAppsPresenter.unregister()
        super.onStop()
    }

    override fun showData(appList: List<PedalfasterApp>) {
        Toast.makeText(this, "Show Data", Toast.LENGTH_SHORT).show()
    }

    fun getAvailableApps(): List<PedalfasterApp> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
                .apply { addCategory(Intent.CATEGORY_LAUNCHER) }
        val resolveInfoList: MutableList<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)
        return resolveInfoList
                .map { buildAppInfo(it.activityInfo) }
                .sortedBy { it.appName }
    }

    fun buildAppInfo(activityInfo: ActivityInfo): PedalfasterApp {
        return PedalfasterApp()
                .apply {
                    appName = activityInfo.loadLabel(packageManager).toString()
                    packageName = activityInfo.packageName
                    icon = activityInfo.loadIcon(packageManager)
                }
    }

}