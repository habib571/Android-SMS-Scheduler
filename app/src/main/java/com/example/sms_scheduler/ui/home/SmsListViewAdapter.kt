package com.example.sms_scheduler.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.sms_scheduler.R

class SmsListViewAdapter(context: Context, smsList: List<SmsModel>) :
    ArrayAdapter<SmsModel>(context, 0, smsList) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Reuse the view if possible; otherwise inflate a new one.
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.sms_list_item, parent, false)

        val sms = getItem(position)

        // Bind data to views.
        val tvSmsContent = view.findViewById<TextView>(R.id.tvSmsContent)
        val tvSentTo = view.findViewById<TextView>(R.id.tvSentTo)
        val tvDateTime = view.findViewById<TextView>(R.id.tvDateTime)

        tvSmsContent.text = sms?.content ?: ""
        tvSentTo.text = "Sent To: ${sms?.sentTo ?: ""}"
        tvDateTime.text = "Date/Time: ${sms?.dateTime ?: ""}"

        return view
    }
}