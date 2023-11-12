package com.example.myittaroostockinventorymanger.ui.batch

import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> AllBatchFragment()
            1 -> OutOfDateBatchFragment()
            2 -> OutOfStockBatchFragment()
            else -> Fragment()
        }
    }
}