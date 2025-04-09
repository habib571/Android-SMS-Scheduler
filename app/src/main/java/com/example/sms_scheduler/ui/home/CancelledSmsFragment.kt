package com.example.sms_scheduler.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sms_scheduler.R

class CancelledSmsFragment : Fragment() {

    companion object {
        fun newInstance() = CancelledSmsFragment()
    }

    private val viewModel: CancelledSmsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cancelled_sms, container, false)
    }
}