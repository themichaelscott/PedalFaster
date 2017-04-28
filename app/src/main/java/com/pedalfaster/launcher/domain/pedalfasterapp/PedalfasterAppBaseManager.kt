/*
 * PedalfasterAppBaseManager.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * 
 */



package com.pedalfaster.launcher.domain.pedalfasterapp

import com.pedalfaster.launcher.domain.DatabaseManager
import org.dbtools.android.domain.KotlinAndroidBaseManagerWritable


@Suppress("unused")
@SuppressWarnings("all")
abstract class PedalfasterAppBaseManager (databaseManager: DatabaseManager) : KotlinAndroidBaseManagerWritable<PedalfasterApp>(databaseManager) {

     override val allColumns: Array<String> = PedalfasterAppConst.ALL_COLUMNS
     override val primaryKey = PedalfasterAppConst.PRIMARY_KEY_COLUMN
     override val dropSql = PedalfasterAppConst.DROP_TABLE
     override val createSql = PedalfasterAppConst.CREATE_TABLE
     override val insertSql = PedalfasterAppConst.INSERT_STATEMENT
     override val updateSql = PedalfasterAppConst.UPDATE_STATEMENT

    override fun getDatabaseName() : String {
        return PedalfasterAppConst.DATABASE
    }

    override fun newRecord() : PedalfasterApp {
        return PedalfasterApp()
    }

    override fun getTableName() : String {
        return PedalfasterAppConst.TABLE
    }


}