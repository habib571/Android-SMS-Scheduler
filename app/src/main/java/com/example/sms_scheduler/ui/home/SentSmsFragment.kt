package com.example.sms_scheduler.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sms_scheduler.R
import com.example.sms_scheduler.databinding.FragmentSentSmsBinding

class SentSmsFragment : Fragment() {

    companion object {
        fun newInstance() = SentSmsFragment()
    }

    private var _binding: FragmentSentSmsBinding? = null
    private val binding get() = _binding!!

    private val viewModel:  SentSmsViewModel by viewModels()
    private lateinit var adapter: SentListviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSentSmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter  =SentListviewAdapter(requireContext())
        binding.listView.adapter = adapter

        viewModel.smsList.observe(viewLifecycleOwner) { smsList ->
            adapter.updateList(smsList)
            adapter.notifyDataSetChanged()
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.refreshSent()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}