/*
 * PedalfasterAppBaseRecord.kt
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package com.pedalfaster.launcher.domain.pedalfasterapp

import android.database.Cursor


@Suppress("LeakingThis", "unused", "RemoveEmptySecondaryConstructorBody", "ConvertSecondaryConstructorToPrimary")
@SuppressWarnings("all")
object PedalfasterAppConst  {

     const val DATABASE = "main"
     const val TABLE = "PEDALFASTER_APP"
     const val FULL_TABLE = "main.PEDALFASTER_APP"
     const val PRIMARY_KEY_COLUMN = "_id"
     const val C_ID = "_id"
     const val FULL_C_ID = "PEDALFASTER_APP._id"
     const val C_APP_NAME = "APP_NAME"
     const val FULL_C_APP_NAME = "PEDALFASTER_APP.APP_NAME"
     const val C_APP_PATH = "APP_PATH"
     const val FULL_C_APP_PATH = "PEDALFASTER_APP.APP_PATH"
     const val C_ENABLED = "ENABLED"
     const val FULL_C_ENABLED = "PEDALFASTER_APP.ENABLED"
     const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS PEDALFASTER_APP (" + 
        "_id INTEGER PRIMARY KEY  AUTOINCREMENT," + 
        "APP_NAME TEXT NOT NULL," + 
        "APP_PATH TEXT NOT NULL," + 
        "ENABLED INTEGER NOT NULL" + 
        ");" + 
        "" + 
        ""
     const val DROP_TABLE = "DROP TABLE IF EXISTS PEDALFASTER_APP;"
     const val INSERT_STATEMENT = "INSERT INTO PEDALFASTER_APP (APP_NAME,APP_PATH,ENABLED) VALUES (?,?,?)"
     const val UPDATE_STATEMENT = "UPDATE PEDALFASTER_APP SET APP_NAME=?, APP_PATH=?, ENABLED=? WHERE _id = ?"
     val ALL_COLUMNS = arrayOf(
        C_ID,
        C_APP_NAME,
        C_APP_PATH,
        C_ENABLED)
     val ALL_COLUMNS_FULL = arrayOf(
        FULL_C_ID,
        FULL_C_APP_NAME,
        FULL_C_APP_PATH,
        FULL_C_ENABLED)

    fun getId(cursor: Cursor) : Long {
        return cursor.getLong(cursor.getColumnIndexOrThrow(C_ID))
    }

    fun getAppName(cursor: Cursor) : String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_APP_NAME))
    }

    fun getAppPath(cursor: Cursor) : String {
        return cursor.getString(cursor.getColumnIndexOrThrow(C_APP_PATH))
    }

    fun isEnabled(cursor: Cursor) : Boolean {
        return cursor.getInt(cursor.getColumnIndexOrThrow(C_ENABLED)) != 0
    }


}