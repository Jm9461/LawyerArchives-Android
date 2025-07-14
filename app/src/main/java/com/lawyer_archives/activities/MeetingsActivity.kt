package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.MeetingAdapter
import com.lawyer_archives.databinding.ActivityMeetingsBinding
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast

class MeetingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeetingsBinding
    private lateinit var adapter: MeetingAdapter
    private lateinit var meetingList: MutableList<Meeting>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadMeetings()

        binding.fabAddMeeting.setOnClickListener {
            startActivity(Intent(this, AddMeetingActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMeetings.layoutManager = LinearLayoutManager(this)
        adapter = MeetingAdapter(
            onEditClick = { meeting ->
                val intent = Intent(this, EditMeetingActivity::class.java)
                intent.putExtra("meetingId", meeting.id)
                startActivity(intent)
            },
            onDeleteClick = { meeting ->
                confirmAndDeleteMeeting(meeting) // Changed to call confirmAndDeleteMeeting
            }
        )
        binding.recyclerViewMeetings.adapter = adapter
    }

    private fun loadMeetings() {
        XmlManager.createEmptyXmlFile(this, XmlManager.MEETINGS_FILE) // اضافه شده
        meetingList = XmlManager.loadMeetings(this).toMutableList()
        adapter.submitList(meetingList.toList())
        if (meetingList.isEmpty()) {
            Toast.makeText(this, "هنوز ملاقاتی ثبت نشده است.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmAndDeleteMeeting(meeting: Meeting) {
        AlertDialog.Builder(this)
            .setTitle("حذف ملاقات")
            .setMessage("آیا از حذف ملاقات با موکل \"${meeting.clientName}\" در تاریخ \"${meeting.meetingDate}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteMeeting(meeting)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteMeeting(meeting: Meeting) {
        val initialSize = meetingList.size
        meetingList.remove(meeting)
        if (meetingList.size < initialSize) {
            XmlManager.saveMeetings(this, meetingList)
            Toast.makeText(this, "ملاقات با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "خطا در حذف ملاقات.", Toast.LENGTH_SHORT).show()
        }
        loadMeetings() // بارگذاری مجدد ملاقات‌ها پس از حذف برای تازه سازی لیست
    }

    override fun onResume() {
        super.onResume()
        loadMeetings()
    }
}