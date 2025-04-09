package com.example.sms_scheduler.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SmsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PendingSmsFragment()
            1 -> SentSmsFragment()
            2 -> CancelledSmsFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }}