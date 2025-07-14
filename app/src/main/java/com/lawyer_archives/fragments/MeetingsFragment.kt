package com.lawyer_archives.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.babak.persiancalender.PersianCalenderView
import com.lawyer_archives.activities.AddCourtSessionActivity
import com.lawyer_archives.activities.AddMeetingActivity
import com.lawyer_archives.adapters.EventAdapter
import com.lawyer_archives.databinding.FragmentMeetingsBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.utils.XmlManager

class MeetingsFragment : Fragment() {

    private var _binding: FragmentMeetingsBinding? = null
    private val binding get() = _binding!!
    private var isFabMenuOpen = false

    private lateinit var eventAdapter: EventAdapter
    private var allSessions: List<CourtSession> = emptyList()
    private var allMeetings: List<Meeting> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendar()
        setupFabMenu()
        setupRecyclerView()
        loadEvents()
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangedListener { year, month, day ->
            val selectedDateStr = "$year/${String.format("%02d", month)}/${String.format("%02d", day)}"
            displayEventsForDate(selectedDateStr)
        }

        // نمایش رویدادهای امروز به صورت پیش فرض
        displayEventsForDate(binding.calendarView.persianShortDate)
    }

    private fun setupFabMenu() {
        binding.fabMain.setOnClickListener {
            if (isFabMenuOpen) {
                hideFabMenu()
            } else {
                showFabMenu()
            }
        }

        binding.fabAddSession.setOnClickListener {
            startActivity(Intent(requireContext(), AddCourtSessionActivity::class.java))
            hideFabMenu()
        }

        binding.fabAddMeeting.setOnClickListener {
            startActivity(Intent(requireContext(), AddMeetingActivity::class.java))
            hideFabMenu()
        }
    }

    private fun showFabMenu() {
        isFabMenuOpen = true
        binding.fabAddSession.show()
        binding.fabAddMeeting.show()
        binding.textAddSession.visibility = View.VISIBLE
        binding.textAddMeeting.visibility = View.VISIBLE
    }

    private fun hideFabMenu() {
        isFabMenuOpen = false
        binding.fabAddSession.hide()
        binding.fabAddMeeting.hide()
        binding.textAddSession.visibility = View.GONE
        binding.textAddMeeting.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter()
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = eventAdapter
    }

    private fun loadEvents() {
        allSessions = XmlManager.loadSessions(requireContext())
        allMeetings = XmlManager.loadMeetings(requireContext())

        // نمایش رویدادهای تاریخ انتخاب شده فعلی (یا امروز)
        displayEventsForDate(binding.calendarView.persianShortDate)

        highlightDaysWithEvents()
    }

    private fun displayEventsForDate(persianDateStr: String) {
        val eventsForSelectedDate = mutableListOf<Any>()

        allSessions.filter { it.courtDate == persianDateStr }
            .forEach { eventsForSelectedDate.add(it) }

        allMeetings.filter { it.date == persianDateStr }
            .forEach { eventsForSelectedDate.add(it) }

        eventAdapter.submitList(eventsForSelectedDate.toList())
    }

    private fun highlightDaysWithEvents() {
        val datesWithEvents = mutableSetOf<String>()

        allSessions.map { it.courtDate }.toCollection(datesWithEvents)
        allMeetings.map { it.date }.toCollection(datesWithEvents)

        for (dateStr in datesWithEvents) {
            val parts = dateStr.split("/")
            if (parts.size == 3) {
                try {
                    val year = parts[0].toInt()
                    val month = parts[1].toInt()
                    val day = parts[2].toInt()
                    // این کتابخانه متد مستقیمی برای mark کردن ندارد
                    // اما با انتخاب روزها، می توانیم به صورت بصری آن ها را متمایز کنیم
                    // این بخش نیاز به بررسی بیشتر مستندات کتابخانه دارد
                } catch (e: Exception) {
                    // Handle parsing error
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
