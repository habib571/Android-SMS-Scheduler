package com.example.sms_scheduler.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sms_scheduler.database.SMSDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PendingSmsViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = SMSDatabaseHelper(application)

    private val _smsList = MutableLiveData<List<SmsModel>>()
    val smsList: LiveData<List<SmsModel>> = _smsList

    init {
        loadPending()
    }
    fun refreshPending() {
        viewModelScope.launch(Dispatchers.IO) {
            _smsList.postValue(dbHelper.getSmsByStatus(SmsStatus.PENDING))
        }
    }

    private fun loadPending() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = dbHelper.getSmsByStatus(SmsStatus.PENDING)
            _smsList.postValue(list)
        }
    }

    fun cancelSms(sms: SmsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.updateSmsStatus(sms.id, SmsStatus.CANCELLED)
            loadPending()
        }
    }


}
