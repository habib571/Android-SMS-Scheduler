package com.example.sms_scheduler.ui.home

import android.content.Context
import android.telephony.SmsManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sms_scheduler.database.SMSDatabaseHelper

class SmsSendWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val KEY_SMS_ID = "sms_id"
    }
    override suspend fun doWork(): Result {
        val db = SMSDatabaseHelper(applicationContext)
        val pendingList = db.getPendingSms()
        if (pendingList.isEmpty()) {
            return Result.success()
        }

        val smsManager = SmsManager.getDefault()
        for (sms in pendingList) {
            try {
                smsManager.sendTextMessage(
                    sms.sentTo,
                    null,
                    sms.content,
                    null,
                    null
                )
                db.updateSmsStatus(sms.id, SmsStatus.SENT)
            } catch (e: Exception) {
                db.updateSmsStatus(sms.id, SmsStatus.CANCELLED)
            }
        }

        return Result.success()
    }
}
