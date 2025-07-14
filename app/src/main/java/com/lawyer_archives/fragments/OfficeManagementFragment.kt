// LawyerArchives/app/src/main/java/com/lawyer_archives/fragments/OfficeManagementFragment.kt
package com.lawyer_archives.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.lawyer_archives.activities.AddDailyTaskActivity
import com.lawyer_archives.adapters.OfficeManagementPagerAdapter
import com.lawyer_archives.databinding.FragmentOfficeManagementBinding

class OfficeManagementFragment : Fragment() {

    private var _binding: FragmentOfficeManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfficeManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPagerAndTabs()

        binding.fabAddTask.setOnClickListener {
            val currentTabPosition = binding.tabLayout.selectedTabPosition
            val intent = Intent(requireContext(), AddDailyTaskActivity::class.java).apply {
                putExtra("tabPosition", currentTabPosition) // Pass tab info to activity
            }
            startActivity(intent)
        }
    }

    private fun setupViewPagerAndTabs() {
        val pagerAdapter = OfficeManagementPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab?.position ?: 0
                updateFabIcon(tab?.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                updateFabIcon(position)
            }
        })
    }

    private fun updateFabIcon(position: Int?) {
        //  Adjust FAB icon/behavior based on which tab is active.
        //  This is a placeholder; you'll likely want different icons/actions.
        binding.fabAddTask.setImageResource(
            if (position == 0) android.R.drawable.ic_menu_add // Daily Tasks
            else android.R.drawable.ic_menu_add // Client/Case Tasks
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}