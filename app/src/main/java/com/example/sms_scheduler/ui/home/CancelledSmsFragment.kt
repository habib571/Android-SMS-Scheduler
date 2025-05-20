package com.example.sms_scheduler.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sms_scheduler.R
import com.example.sms_scheduler.databinding.FragmentCancelledSmsBinding

class CancelledSmsFragment : Fragment() {

    companion object {
        fun newInstance() = CancelledSmsFragment()
    }

    private var _binding: FragmentCancelledSmsBinding? = null
    private val binding get() = _binding!!

    private val viewModel:  CancelledSmsViewModel by viewModels()
    private lateinit var adapter: CancelledListviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCancelledSmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CancelledListviewAdapter(requireContext())
        binding.listView.adapter = adapter

        viewModel.smsList.observe(viewLifecycleOwner) { smsList ->
            adapter.updateList(smsList)
            adapter.notifyDataSetChanged()
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.refreshCancelled()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}