package com.example.sms_scheduler.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sms_scheduler.database.SMSDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SentSmsViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = SMSDatabaseHelper(application)

    private val _smsList = MutableLiveData<List<SmsModel>>()
    val smsList: LiveData<List<SmsModel>> = _smsList

    init {
        loadSent()
    }

    fun refreshSent() {
        viewModelScope.launch(Dispatchers.IO) {
            _smsList.postValue(dbHelper.getSmsByStatus(SmsStatus.SENT))
        }
    }

    private fun loadSent() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = dbHelper.getSmsByStatus(SmsStatus.SENT)
            _smsList.postValue(list)
        }
    }


}