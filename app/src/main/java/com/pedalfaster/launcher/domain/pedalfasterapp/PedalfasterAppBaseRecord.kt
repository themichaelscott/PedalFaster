/*
 * PedalfasterAppBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.pedalfaster.launcher.domain.pedalfasterapp

import org.dbtools.android.domain.AndroidBaseRecord
import org.dbtools.android.domain.database.statement.StatementWrapper
import org.dbtools.android.domain.database.contentvalues.DBToolsContentValues
import android.database.Cursor


@Suppress("LeakingThis", "unused", "RemoveEmptySecondaryConstructorBody", "ConvertSecondaryConstructorToPrimary")
@SuppressWarnings("all")
abstract class PedalfasterAppBaseRecord  : AndroidBaseRecord {

     open var id: Long = 0
     open var appName: String = ""
     open var appPath: String = ""
     open var enabled: Boolean = false

    constructor() {
    }

    override fun getIdColumnName() : String {
        return PedalfasterAppConst.C_ID
    }

    override fun getPrimaryKeyId() : Long {
        return id
    }

    override fun setPrimaryKeyId(id: Long) {
        this.id = id
    }

    override fun getAllColumns() : Array<String> {
        return PedalfasterAppConst.ALL_COLUMNS.clone()
    }

    fun getAllColumnsFull() : Array<String> {
        return PedalfasterAppConst.ALL_COLUMNS_FULL.clone()
    }

    override fun getContentValues(values: DBToolsContentValues<*>) {
        values.put(PedalfasterAppConst.C_APP_NAME, appName)
        values.put(PedalfasterAppConst.C_APP_PATH, appPath)
        values.put(PedalfasterAppConst.C_ENABLED, if (enabled) 1L else 0L)
    }

    override fun getValues() : Array<Any?> {
        return arrayOf(
            id,
            appName,
            appPath,
            if (enabled) 1L else 0L)
    }

    open fun copy() : PedalfasterApp {
        val copy = PedalfasterApp()
        copy.id = id
        copy.appName = appName
        copy.appPath = appPath
        copy.enabled = enabled
        return copy
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun bindInsertStatement(statement: StatementWrapper) {
        statement.bindString(1, appName)
        statement.bindString(2, appPath)
        statement.bindLong(3, if (enabled) 1L else 0L)
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun bindUpdateStatement(statement: StatementWrapper) {
        statement.bindString(1, appName)
        statement.bindString(2, appPath)
        statement.bindLong(3, if (enabled) 1L else 0L)
        statement.bindLong(4, id)
    }

    override fun setContent(values: DBToolsContentValues<*>) {
        appName = values.getAsString(PedalfasterAppConst.C_APP_NAME)
        appPath = values.getAsString(PedalfasterAppConst.C_APP_PATH)
        enabled = values.getAsBoolean(PedalfasterAppConst.C_ENABLED)
    }

    override fun setContent(cursor: Cursor) {
        id = cursor.getLong(cursor.getColumnIndexOrThrow(PedalfasterAppConst.C_ID))
        appName = cursor.getString(cursor.getColumnIndexOrThrow(PedalfasterAppConst.C_APP_NAME))
        appPath = cursor.getString(cursor.getColumnIndexOrThrow(PedalfasterAppConst.C_APP_PATH))
        enabled = cursor.getInt(cursor.getColumnIndexOrThrow(PedalfasterAppConst.C_ENABLED)) != 0
    }

    override fun isNewRecord() : Boolean {
        return primaryKeyId <= 0
    }


}