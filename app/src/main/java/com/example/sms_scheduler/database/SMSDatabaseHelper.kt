package com.example.sms_scheduler.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sms_scheduler.ui.home.SmsModel
import com.example.sms_scheduler.ui.home.SmsStatus

class SMSDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME    = "sms_scheduler.db"
        const val DATABASE_VERSION = 2

        const val TABLE_SMS    = "sms"
        const val COLUMN_ID    = "id"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_MESSAGE = "message"
        const val COLUMN_DATE  = "date"
        const val COLUMN_TIME  = "time"
        const val COLUMN_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_SMS (
                $COLUMN_ID       INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PHONE    TEXT,
                $COLUMN_MESSAGE  TEXT,
                $COLUMN_DATE     TEXT,
                $COLUMN_TIME     TEXT,
                $COLUMN_STATUS   TEXT DEFAULT '${SmsStatus.PENDING.name}'
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // add the new status column, with default PENDING for existing rows
            db.execSQL("""
                ALTER TABLE $TABLE_SMS
                ADD COLUMN $COLUMN_STATUS TEXT DEFAULT '${SmsStatus.PENDING.name}';
            """.trimIndent())
        }
    }

    /**
     * Inserts a new SMS into the database.
     * @return true if successful, false otherwise.
     */
    fun insertSMS(
        phone: String,
        message: String,
        date: String,
        time: String,
        status: SmsStatus = SmsStatus.PENDING
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PHONE, phone)
            put(COLUMN_MESSAGE, message)
            put(COLUMN_DATE, date)
            put(COLUMN_TIME, time)
            put(COLUMN_STATUS, status.name)
        }

        val result = db.insert(TABLE_SMS, null, values)
        db.close()
        return result != -1L
    }


      fun getSmsByStatus(status: SmsStatus): List<SmsModel> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SMS,
            null,
            "$COLUMN_STATUS = ?",
            arrayOf(status.name),
            null, null,
            "$COLUMN_DATE || ' ' || $COLUMN_TIME DESC"
        )
        val list = mutableListOf<SmsModel>()
        while (cursor.moveToNext()) {
            val id       = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val phone    = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            val message  = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE))
            val date     = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val time     = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            val statusStr= cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
            list += SmsModel(id, message, phone, "$date $time", SmsStatus.valueOf(statusStr))
        }
        cursor.close()
        return list
    }


    fun updateSmsStatus(id: Int, status: SmsStatus) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_STATUS, status.name)
        }
        db.update(TABLE_SMS, cv, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
    fun getSmsById(id: Int): SmsModel? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SMS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        val sms = if (cursor.moveToFirst()) {
            val phone    = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            val message  = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE))
            val date     = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val time     = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            val statusStr= cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
            SmsModel(id, message, phone, "$date $time", SmsStatus.valueOf(statusStr))
        } else null
        cursor.close()
        return sms
    }
}
