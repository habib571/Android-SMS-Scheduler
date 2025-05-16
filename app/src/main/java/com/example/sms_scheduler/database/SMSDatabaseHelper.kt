package com.example.sms_scheduler.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SMSDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "sms_scheduler.db"
        const val DATABASE_VERSION = 1

        const val TABLE_SMS = "sms"
        const val COLUMN_ID = "id"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_MESSAGE = "message"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_SMS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PHONE TEXT,
                $COLUMN_MESSAGE TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_TIME TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SMS")
        onCreate(db)
    }

    fun insertSMS(phone: String, message: String, date: String, time: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PHONE, phone)
            put(COLUMN_MESSAGE, message)
            put(COLUMN_DATE, date)
            put(COLUMN_TIME, time)
        }

        val result = db.insert(TABLE_SMS, null, values)
        db.close()
        return result != -1L
    }
}
