package com.example.sms_scheduler.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PendingSmsViewModel : ViewModel() {
    private val _smsList = MutableLiveData<List<SmsModel>>().apply {
        value = listOf(
            SmsModel(1, "Your appointment is scheduled for tomorrow at 10AM", "123-456-7890", "2025-04-09 09:30"),
            SmsModel(2, "Don't forget your meeting at 2PM", "987-654-3210", "2025-04-09 11:00"),
            SmsModel(3, "Your package has been shipped", "555-123-4567", "2025-04-09 14:45"), SmsModel(1, "Your appointment is scheduled for tomorrow at 10AM", "123-456-7890", "2025-04-09 09:30"),
            SmsModel(2, "Don't forget your meeting at 2PM", "987-654-3210", "2025-04-09 11:00"),
           SmsModel(3, "Your package has been shipped", "555-123-4567", "2025-04-09 14:45") ,
            SmsModel(1, "Your appointment is scheduled for tomorrow at 10AM", "123-456-7890", "2025-04-09 09:30"),
            SmsModel(2, "Don't forget your meeting at 2PM", "987-654-3210", "2025-04-09 11:00"),
            SmsModel(3, "Your package has been shipped", "555-123-4567", "2025-04-09 14:45")

        )
    }
    val smsList: LiveData<List<SmsModel>>
        get() = _smsList


}