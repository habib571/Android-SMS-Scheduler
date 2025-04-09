package com.example.sms_scheduler.ui.home

data class SmsModel(
    val id: Int,
    val content: String,
    val sentTo: String,
    val dateTime: String
)
