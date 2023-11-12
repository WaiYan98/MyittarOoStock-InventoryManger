package com.example.myittaroostockinventorymanger.ui.batch

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.databinding.FragmentBatchBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Calendar

class BatchFragment : Fragment() {

    private lateinit var binding: FragmentBatchBinding
    private val viewModel: BatchFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager2.adapter = PagerAdapter(this)

        var expiredTab: TabLayout.Tab = binding.tabLayout.newTab()
        var outOfStockTab: TabLayout.Tab =  binding.tabLayout.newTab()


        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->

            when (position) {
                0 -> {
                    tab.text = "All"
                    tab.icon = ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_batch)
                }
                1 -> {
                    tab.text = "Out Of Date"
                    tab.icon = ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_batch)
                    expiredTab = tab
                }

                2 -> {
                    tab.text = "Out Of Stock"
                    tab.icon = ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_batch)
                    outOfStockTab = tab
                }
            }
        }.attach()

        //currentDate+10days
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, 10)
        val currDateAfter10Days = cal.time.time


        //check expired batch qty
        viewModel.getExpiredBatchQty(currDateAfter10Days)
            .observe(viewLifecycleOwner) {
                Log.d("expired", "onViewCreated: $it")
                expiredTab.orCreateBadge
                    .number = it.toInt()

            }

        //check outOfStock batch qty
        viewModel.getOutOfStockBatchQty(10)
            .observe(viewLifecycleOwner) {
                outOfStockTab.orCreateBadge
                    .number = it.toInt()
            }
    }

}