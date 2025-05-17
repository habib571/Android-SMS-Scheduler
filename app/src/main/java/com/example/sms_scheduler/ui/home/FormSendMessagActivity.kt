package com.example.sms_scheduler.ui.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sms_scheduler.R
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.sms_scheduler.database.SMSDatabaseHelper

class FormSendMessagActivity : AppCompatActivity() {

    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private val viewModel: PendingSmsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_send_messag)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        dateEditText = findViewById(R.id.date_edit_text)
        timeEditText = findViewById(R.id.time_edit_text)

        val calendar = Calendar.getInstance()

        // Date picker
        dateEditText.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    dateEditText.setText(formatter.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Time picker
        timeEditText.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                    timeEditText.setText(formatter.format(calendar.time))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24-hour format
            )
            timePicker.show()
        }
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            val phone = findViewById<EditText>(R.id.phone_edit_text).text.toString()
            val message = findViewById<EditText>(R.id.message_edit_text).text.toString()
            val date = dateEditText.text.toString()
            val time = timeEditText.text.toString()

            val dbHelper = SMSDatabaseHelper(this)
            val newSms = SmsModel(
                id       = 0,  // auto‐generated in DB
                content  = message,
                sentTo   = phone,
                dateTime = date,
                status   = SmsStatus.PENDING
            )

            val success = dbHelper.insertSMS(phone, message, date, time)

            if (success) {
                Toast.makeText(this, "SMS enregistré avec succès !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
