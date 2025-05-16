package com.example.sms_scheduler.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PendingSmsViewModel : ViewModel() {
    private val _smsList = MutableLiveData<List<SmsModel>>().apply {
        value = listOf(
            SmsModel(1, "Your appointment is scheduled for tomorrow at 10AM", "123-456-7890", "2025-04-09 09:30", SmsStatus.PENDING),
            SmsModel(2, "Don't forget your meeting at 2PM", "987-654-3210", "2025-04-09 11:00", SmsStatus.PENDING),
            SmsModel(3, "Your package has been shipped", "555-123-4567", "2025-04-09 14:45", SmsStatus.PENDING),
            SmsModel(4, "Lunch with client at 1PM", "123-123-1234", "2025-04-09 12:00", SmsStatus.PENDING)
        )
    }

    val smsList: LiveData<List<SmsModel>>
        get() = _smsList

    fun cancelSms(smsModel: SmsModel) {
        _smsList.value = _smsList.value?.filter { it.id != smsModel.id }
    }
}
