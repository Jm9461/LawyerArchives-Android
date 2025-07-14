package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.SessionAdapter
import com.lawyer_archives.databinding.ActivitySessionsBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast

class SessionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionsBinding
    private lateinit var adapter: SessionAdapter
    private lateinit var sessionList: MutableList<CourtSession>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadSessions()

        binding.fabAddSession.setOnClickListener {
            startActivity(Intent(this, AddCourtSessionActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSessions.layoutManager = LinearLayoutManager(this)
        adapter = SessionAdapter(
            onEditClick = { session ->
                val intent = Intent(this, EditCourtSessionActivity::class.java)
                intent.putExtra("sessionId", session.id)
                startActivity(intent)
            },
            onDeleteClick = { session ->
                confirmAndDeleteSession(session) // Changed to call confirmAndDeleteSession
            }
        )
        binding.recyclerViewSessions.adapter = adapter
    }

    private fun loadSessions() {
        XmlManager.createEmptyXmlFile(this, XmlManager.SESSIONS_FILE) // اضافه شده
        sessionList = XmlManager.loadSessions(this).toMutableList()
        adapter.submitList(sessionList.toList())
        if (sessionList.isEmpty()) {
            Toast.makeText(this, "هنوز جلسه دادگاهی ثبت نشده است.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmAndDeleteSession(session: CourtSession) {
        AlertDialog.Builder(this)
            .setTitle("حذف جلسه دادگاه")
            .setMessage("آیا از حذف جلسه دادگاه مربوط به پرونده \"${session.caseTitle}\" در تاریخ \"${session.sessionDate}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteSession(session)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteSession(session: CourtSession) {
        val initialSize = sessionList.size
        sessionList.remove(session)
        if (sessionList.size < initialSize) {
            XmlManager.saveSessions(this, sessionList)
            Toast.makeText(this, "جلسه دادگاه با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "خطا در حذف جلسه دادگاه.", Toast.LENGTH_SHORT).show()
        }
        loadSessions() // بارگذاری مجدد جلسات پس از حذف برای تازه سازی لیست
    }

    override fun onResume() {
        super.onResume()
        loadSessions()
    }
}