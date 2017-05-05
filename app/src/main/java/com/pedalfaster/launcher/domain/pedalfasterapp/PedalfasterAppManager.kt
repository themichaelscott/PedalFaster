package com.pedalfaster.launcher.domain.pedalfasterapp

import com.pedalfaster.launcher.domain.DatabaseManager
import org.dbtools.query.sql.SQLQueryBuilder
import javax.inject.Inject

@javax.inject.Singleton
class PedalfasterAppManager @Inject constructor(databaseManager: DatabaseManager) : PedalfasterAppBaseManager(databaseManager) {

    fun findByPackageName(packageName: String): PedalfasterApp? {
        return findBySelection("${PedalfasterAppConst.C_PACKAGE_NAME}=?", SQLQueryBuilder.toSelectionArgs(packageName))
    }

}