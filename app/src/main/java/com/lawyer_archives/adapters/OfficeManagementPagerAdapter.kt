// LawyerArchives/app/src/main/java/com/lawyer_archives/adapters/OfficeManagementPagerAdapter.kt
package com.lawyer_archives.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lawyer_archives.fragments.DailyTasksFragment
import com.lawyer_archives.fragments.ClientCaseTasksFragment

class OfficeManagementPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // Two tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DailyTasksFragment()
            1 -> ClientCaseTasksFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}