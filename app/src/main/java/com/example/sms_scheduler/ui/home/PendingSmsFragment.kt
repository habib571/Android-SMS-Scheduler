package com.example.sms_scheduler.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import com.example.sms_scheduler.R
import com.example.sms_scheduler.databinding.FragmentPendingSmsBinding

class PendingSmsFragment : Fragment() {

    companion object {
        fun newInstance() = PendingSmsFragment()
    }


    private var _binding: FragmentPendingSmsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PendingSmsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPendingSmsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.smsList.observe(viewLifecycleOwner) {
            smsList ->
            val adapter = SmsListViewAdapter(requireContext(), smsList)
            binding.listView.adapter = adapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}