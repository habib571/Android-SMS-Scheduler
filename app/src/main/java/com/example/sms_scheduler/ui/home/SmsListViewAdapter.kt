package com.example.sms_scheduler.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.sms_scheduler.R
import com.google.android.material.button.MaterialButton

class SmsListViewAdapter(
    context: Context,
    private val onCancel: (SmsModel) -> Unit
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
        view.findViewById<MaterialButton>(R.id.cancel_button).setOnClickListener {
            onCancel(sms)
        }
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
