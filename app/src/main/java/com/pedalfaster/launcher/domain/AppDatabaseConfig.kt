package com.pedalfaster.launcher.domain

import android.app.Application
import org.dbtools.android.domain.AndroidDatabase
import org.dbtools.android.domain.AndroidDatabaseBaseManager
import org.dbtools.android.domain.config.DatabaseConfig
import org.dbtools.android.domain.database.AndroidDatabaseWrapper
import org.dbtools.android.domain.database.DatabaseWrapper
import org.dbtools.android.domain.database.contentvalues.AndroidDBToolsContentValues
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import org.dbtools.android.domain.log.DBToolsAndroidLogger
import org.dbtools.android.domain.log.DBToolsLogger


class AppDatabaseConfig : DatabaseConfig {

    val application: Application

    constructor(application: Application) {
        this.application = application
    }

    override fun identifyDatabases(databaseManager: AndroidDatabaseBaseManager) {
        databaseManager.addDatabase(application, DatabaseManagerConst.MAIN_DATABASE_NAME, DatabaseManager.MAIN_TABLES_VERSION, DatabaseManager.MAIN_VIEWS_VERSION)
    }

    override fun createNewDatabaseWrapper(androidDatabase: AndroidDatabase): DatabaseWrapper<*, *> {
        return AndroidDatabaseWrapper(androidDatabase.path)
    }

    override fun createNewDBToolsContentValues(): DBToolsContentValues<*> {
        return AndroidDBToolsContentValues()
    }

    override fun createNewDBToolsLogger(): DBToolsLogger {
        return DBToolsAndroidLogger()
    }


}