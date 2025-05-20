package com.example.sms_scheduler.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.sms_scheduler.R

class SentListviewAdapter(
    context: Context,
) : ArrayAdapter<SmsModel>(context, 0, mutableListOf()) {

    private val smsList = mutableListOf<SmsModel>()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.sms_list_item, parent, false)
        val sms = smsList[position]
        view.findViewById<TextView>(R.id.tvSmsContent).text = sms.content
        view.findViewById<TextView>(R.id.tvSentTo).text = "Sent To: ${sms.sentTo}"
        view.findViewById<TextView>(R.id.tvDateTime).text = sms.dateTime
        return view
    }

    override fun getCount() = smsList.size
    override fun getItem(position: Int) = smsList[position]

    fun updateList(newList: List<SmsModel>) {
        smsList.clear()
        smsList.addAll(newList)
        notifyDataSetChanged()
    }
}
