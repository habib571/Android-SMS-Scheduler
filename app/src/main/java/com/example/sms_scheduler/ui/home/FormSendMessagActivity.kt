package com.example.sms_scheduler.ui.home

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.sms_scheduler.R
import com.example.sms_scheduler.database.SMSDatabaseHelper

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FormSendMessageActivity : AppCompatActivity() {

    companion object {
        private const val SMS_REQUEST_CODE = 1001
    }

    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var messageEditText: EditText

    private val viewModel: PendingSmsViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_send_messag)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dateEditText    = findViewById(R.id.date_edit_text)
        timeEditText    = findViewById(R.id.time_edit_text)
        phoneEditText   = findViewById(R.id.phone_edit_text)
        messageEditText = findViewById(R.id.message_edit_text)

        val calendar = Calendar.getInstance()

        dateEditText.setOnClickListener { showDatePicker(calendar) }
        timeEditText.setOnClickListener { showTimePicker(calendar) }

        findViewById<Button>(R.id.save_button).setOnClickListener {
            ensureSmsPermission { planifyThenSave(calendar) }
        }
    }

    private fun showDatePicker(calendar: Calendar) {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                dateEditText.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(calendar: Calendar) {
        TimePickerDialog(
            this,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                timeEditText.setText(SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(calendar.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun ensureSmsPermission(onGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
            PackageManager.PERMISSION_GRANTED) {
            onGranted()
        } else {
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), SMS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_REQUEST_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            findViewById<Button>(R.id.save_button).performClick()
        } else {
            Toast.makeText(this, "SMS permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun planifyThenSave(calendar: Calendar) {
        val phone   = phoneEditText.text.toString().trim()
        val content = messageEditText.text.toString().trim()
        val date    = dateEditText.text.toString().trim()
        val time    = timeEditText.text.toString().trim()
        if (phone.isEmpty() || content.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }
        val dbHelper = SMSDatabaseHelper(this)
        val saved    = dbHelper.insertSMS(phone, content, date, time)
        if (!saved) {
            Toast.makeText(this, "Failed to save SMS", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf         = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val scheduledMs = sdf.parse("$date $time")?.time ?: System.currentTimeMillis()
        val delay       = (scheduledMs - System.currentTimeMillis()).coerceAtLeast(0L)
        val workData    = workDataOf(SmsSendWorker.KEY_SMS_ID to saved)
        val request     = OneTimeWorkRequestBuilder<SmsSendWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workData)
            .build()
        WorkManager.getInstance(this).enqueue(request)

        Toast.makeText(this, "SMS scheduled and saved", Toast.LENGTH_SHORT).show()
        finish()
    }
}
