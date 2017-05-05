package com.pedalfaster.launcher.dagger

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.pedalfaster.launcher.BusRegistry
import com.pedalfaster.launcher.domain.AppDatabaseConfig
import dagger.Module
import dagger.Provides
import org.dbtools.android.domain.config.DatabaseConfig
import pocketbus.Bus
import javax.inject.Singleton

@Module()
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    internal fun provideDatabaseConfig(application: Application): DatabaseConfig {
        return AppDatabaseConfig(application)
    }

    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    fun provideBus(): Bus {
        val bus = Bus.Builder().build()
        bus.setRegistry(BusRegistry())
        return bus
    }
}
