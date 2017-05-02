package com.pedalfaster.launcher.ux.app.enabled

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.adapter.EnabledAppsAdapter
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.domain.pedalfasterapp.PedalfasterApp
import com.pedalfaster.launcher.job.Scheduler
import com.pedalfaster.launcher.receiver.PedalFasterController
import kotlinx.android.synthetic.main.activity_enabled_apps.*
import javax.inject.Inject


class EnabledAppsActivity : AppCompatActivity(), EnabledAppsContract.View {

    @Inject
    lateinit var enabledAppsPresenter: EnabledAppsPresenter
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var pedalFasterController: PedalFasterController

    lateinit var adapter: EnabledAppsAdapter

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enabled_apps)
        setupRecyclerView()
        enabledAppsPresenter.init(this)
    }

    override fun onStart() {
        super.onStart()
        enabledAppsPresenter.register()
        pedalFasterController.showAlert = false
        scheduler.cancelPedalFasterInterrupter()
        enabledAppsPresenter.loadData(getAvailableApps())
    }

    override fun onStop() {
        enabledAppsPresenter.unregister()
        pedalFasterController.showAlert = true
        scheduler.schedulePedalFasterInterrupter()
        super.onStop()
    }

    override fun showData(appList: List<PedalfasterApp>) {
        adapter.list = appList.toMutableList()
        Toast.makeText(this, "Show Data", Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView() {
        adapter = EnabledAppsAdapter().apply {
            itemClickListener = { app, enabled -> enabledAppsPresenter.onClick(app, enabled) }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(pedalfasterApp: PedalfasterApp, enabled: Boolean) {
        Toast.makeText(this, "${pedalfasterApp.appName}, enabled: $enabled", Toast.LENGTH_SHORT).show()
    }

    private fun getAvailableApps(): List<PedalfasterApp> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
                .apply { addCategory(Intent.CATEGORY_LAUNCHER) }
        val resolveInfoList: MutableList<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)
        return resolveInfoList
                .map { buildAppInfo(it.activityInfo) }
                .sortedBy { it.appName }
    }

    private fun buildAppInfo(activityInfo: ActivityInfo): PedalfasterApp {
        return PedalfasterApp()
                .apply {
                    appName = activityInfo.loadLabel(packageManager).toString()
                    packageName = activityInfo.packageName
                    icon = activityInfo.loadIcon(packageManager)
                }
    }

}