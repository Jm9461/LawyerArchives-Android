package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditMeetingBinding
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast

class EditMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMeetingBinding
    private var originalMeetingId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalMeetingId = intent.getStringExtra("meetingId")
        loadMeetingData()

        // Corrected button ID from 'updateMeetingButton' to 'saveMeetingButton'
        // based on activity_edit_meeting.xml
        binding.saveMeetingButton.setOnClickListener {
            updateMeeting()
        }
    }

    private fun loadMeetingData() {
        originalMeetingId?.let { id ->
            val meetingToEdit = XmlManager.loadMeetings(this).find { it.id == id }
            meetingToEdit?.let {
                binding.editClientName.setText(it.clientName)
                binding.editMeetingDate.setText(it.meetingDate)
                binding.editTopic.setText(it.topic)
                binding.editDuration.setText(it.duration)
                binding.editLocation.setText(it.location)
            } ?: run {
                Toast.makeText(this, "ملاقات برای ویرایش یافت نشد.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } ?: run {
            Toast.makeText(this, "شناسه ملاقات در دسترس نیست.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateMeeting() {
        val clientName = binding.editClientName.text.toString()
        val meetingDate = binding.editMeetingDate.text.toString()
        val topic = binding.editTopic.text.toString()
        val duration = binding.editDuration.text.toString()
        val location = binding.editLocation.text.toString()

        if (clientName.isBlank() || meetingDate.isBlank() || topic.isBlank() || duration.isBlank() || location.isBlank()) {
            Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            return
        }

        originalMeetingId?.let { id ->
            val currentMeetings = XmlManager.loadMeetings(this).toMutableList()
            val index = currentMeetings.indexOfFirst { it.id == id }
            if (index != -1) {
                val updatedMeeting = Meeting(
                    id = id,
                    clientId = currentMeetings[index].clientId, // Keep original clientId
                    clientName = clientName,
                    meetingDate = meetingDate,
                    topic = topic,
                    duration = duration,
                    location = location
                )
                currentMeetings[index] = updatedMeeting
                XmlManager.saveMeetings(this, currentMeetings)
                Toast.makeText(this, "ملاقات با موفقیت به‌روزرسانی شد.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "ملاقات برای به‌روزرسانی یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}