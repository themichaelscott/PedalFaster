package com.pedalfaster.launcher.dagger

import android.app.Application
import com.pedalfaster.launcher.App
import com.pedalfaster.launcher.activity.HomeActivity
import com.pedalfaster.launcher.fragment.SettingsFragment
import com.pedalfaster.launcher.receiver.PedalFasterController
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun application(): Application
    fun inject(target: App)
    fun inject(target: SettingsFragment)
    fun inject(target: HomeActivity)
    fun inject(target: PedalFasterController)
}
