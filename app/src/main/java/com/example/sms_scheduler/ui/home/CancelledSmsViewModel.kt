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

class CancelledSmsViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = SMSDatabaseHelper(application)

    private val _smsList = MutableLiveData<List<SmsModel>>()
    val smsList: LiveData<List<SmsModel>> = _smsList

    init {
        loadCancelled()
    }

    fun refreshCancelled() {
        viewModelScope.launch(Dispatchers.IO) {
            _smsList.postValue(dbHelper.getSmsByStatus(SmsStatus.CANCELLED))
        }
    }

    private fun loadCancelled() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = dbHelper.getSmsByStatus(SmsStatus.CANCELLED)
            _smsList.postValue(list)
        }
    }
}




