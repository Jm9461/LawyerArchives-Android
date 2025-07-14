package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditSessionBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.utils.CalendarUtils
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast

class EditCourtSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditSessionBinding
    private var originalSessionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalSessionId = intent.getStringExtra("sessionId")
        loadSessionData()

        // Corrected button ID from 'updateSessionButton' to 'saveSessionButton'
        // based on activity_edit_session.xml
        binding.saveSessionButton.setOnClickListener {
            updateSession()
        }
    }

    private fun loadSessionData() {
        originalSessionId?.let { id ->
            val sessionToEdit = XmlManager.loadSessions(this).find { it.id == id }
            sessionToEdit?.let {
                binding.editCaseTitle.setText(it.caseTitle)
                binding.editSessionDate.setText(it.sessionDate)
                binding.editLocation.setText(it.location)
                binding.editDescription.setText(it.description)
            } ?: run {
                Toast.makeText(this, "جلسه دادگاه برای ویرایش یافت نشد.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } ?: run {
            Toast.makeText(this, "شناسه جلسه دادگاه در دسترس نیست.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateSession() {
        val caseTitle = binding.editCaseTitle.text.toString()
        val sessionDate = binding.editSessionDate.text.toString()
        val location = binding.editLocation.text.toString()
        val description = binding.editDescription.text.toString()

        if (caseTitle.isBlank() || sessionDate.isBlank() || location.isBlank() || description.isBlank()) {
            Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            return
        }

        originalSessionId?.let { id ->
            val currentSessions = XmlManager.loadSessions(this).toMutableList()
            val index = currentSessions.indexOfFirst { it.id == id }
            if (index != -1) {
                val updatedSession = CourtSession(
                    id = id,
                    caseId = currentSessions[index].caseId, // Keep original caseId
                    caseTitle = caseTitle,
                    sessionDate = sessionDate,
                    location = location,
                    description = description,
                    isCompleted = currentSessions[index].isCompleted // Keep original status
                )
                currentSessions[index] = updatedSession
                XmlManager.saveSessions(this, currentSessions)
                CalendarUtils.scheduleSessionAlarm(this, updatedSession) // Reschedule alarm for updated session
                Toast.makeText(this, "جلسه دادگاه با موفقیت به‌روزرسانی شد.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "جلسه دادگاه برای به‌روزرسانی یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}