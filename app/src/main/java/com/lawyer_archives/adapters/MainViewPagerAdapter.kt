package com.lawyer_archives.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lawyer_archives.fragments.CasesFragment
import com.lawyer_archives.fragments.ClientsFragment
import com.lawyer_archives.fragments.JudicialCalculationsFragment
import com.lawyer_archives.fragments.LegalDeadlinesFragment
import com.lawyer_archives.fragments.MeetingsFragment
import com.lawyer_archives.fragments.OfficeManagementFragment
import com.lawyer_archives.fragments.PublicCalculationsFragment
import com.lawyer_archives.fragments.DocumentCalculationsFragment
import com.lawyer_archives.fragments.UnanimityOfProcedureFragment
import com.lawyer_archives.fragments.ConsultativeOpinionsFragment
import com.lawyer_archives.fragments.JudicialProceduresFragment
import com.lawyer_archives.fragments.LawBankFragment
import com.lawyer_archives.fragments.LegalNeedsFragment
import com.lawyer_archives.fragments.JudicialLocatorFragment
import com.lawyer_archives.fragments.RegistrationServicesFragment


class MainViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 15 // 8 قبلی + 7 جدید

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MeetingsFragment() // جلسات
            1 -> ClientsFragment() // موکلین
            2 -> CasesFragment() // پرونده‌ها
            3 -> OfficeManagementFragment() // مدیریت دفتر وکالت
            4 -> JudicialCalculationsFragment() // محاسبات قضایی
            5 -> DocumentCalculationsFragment() // محاسبات اسنادی
            6 -> PublicCalculationsFragment() // محاسبات عمومی
            7 -> LegalDeadlinesFragment() // مواعد قانونی
            8 -> UnanimityOfProcedureFragment() // آراء وحدت رویه
            9 -> ConsultativeOpinionsFragment() // نظریه های مشورتی
            10 -> JudicialProceduresFragment() // رویه های قضایی
            11 -> LawBankFragment() // بانک قوانین
            12 -> LegalNeedsFragment() // نیازهای حقوقی
            13 -> JudicialLocatorFragment() // مکان یاب قضایی
            14 -> RegistrationServicesFragment() // خدمات ثبتی
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}