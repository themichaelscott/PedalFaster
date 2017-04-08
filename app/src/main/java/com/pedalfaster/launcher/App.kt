package com.pedalfaster.launcher

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.evernote.android.job.JobManager
import com.pedalfaster.launcher.dagger.AppModule
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.job.AppJobCreator
import com.pedalfaster.launcher.logging.DebugTree
import com.pedalfaster.launcher.logging.ReleaseTree
import com.pedalfaster.launcher.prefs.Prefs
import timber.log.Timber
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var appJobCreator: AppJobCreator

    init {
        Injector.init(AppModule(this))
    }

    override fun onCreate() {
        super.onCreate()
        Injector.get().inject(this)
        setupLogging()
        JobManager.create(this).addJobCreator(appJobCreator)

    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        if (filesDir != null) {
            MultiDex.install(this)
        } else {
            // During app install it might have experienced "INSTALL_FAILED_DEXOPT" (reinstall is the only known work-around)
            // https://code.google.com/p/android/issues/detail?id=8886
            val message = getString(R.string.app_name) + " is in a bad state, please uninstall/reinstall"
            Timber.e(message)
        }
    }

    companion object {

        val DEFAULT_TAG_PREFIX = "pedal."

        private val tempAuthenticated = false
        private val developmentMode = "debug".equals(BuildConfig.BUILD_TYPE, ignoreCase = true) // true to disable crash reports, analytics, etc..
        private val devEnvironmentEnabled = false // enable developer settings/preferences
        private val lastAppUseTS: Long = 0
    }


}
