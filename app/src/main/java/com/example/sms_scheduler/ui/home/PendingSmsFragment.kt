package com.example.sms_scheduler.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sms_scheduler.databinding.FragmentPendingSmsBinding

class PendingSmsFragment : Fragment() {

    companion object {
        fun newInstance() = PendingSmsFragment()
    }

    private var _binding: FragmentPendingSmsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PendingSmsViewModel by viewModels()
    private lateinit var adapter: SmsListViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPendingSmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SmsListViewAdapter(requireContext()) { smsToCancel ->
            viewModel.cancelSms(smsToCancel)
        }
        binding.listView.adapter = adapter

        viewModel.smsList.observe(viewLifecycleOwner) { smsList ->
            adapter.updateList(smsList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
