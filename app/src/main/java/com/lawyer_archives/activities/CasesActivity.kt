// LawyerArchives/app/src/main/java/com/lawyer_archives/activities/CasesActivity.kt
// (Inside your existing CasesActivity.kt file)

package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.adapters.CaseAdapter
import com.lawyer_archives.databinding.ActivityCasesBinding
import com.lawyer_archives.models.Case
import com.lawyer_archives.utils.XmlManager
import androidx.recyclerview.widget.LinearLayoutManager


class CasesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCasesBinding
    private lateinit var caseList: MutableList<Case>
    private lateinit var adapter: CaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadCases() // Initial load of cases

        binding.addCaseButton.setOnClickListener {
            val intent = Intent(this, AddCaseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.casesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CaseAdapter(
            onEditClick = { case: Case -> // Explicitly specifying type
                val intent = Intent(this, EditCaseActivity::class.java).apply {
                    putExtra("caseId", case.id)
                }
                startActivity(intent)
            },
            onDeleteClick = { case ->
                confirmAndDeleteCase(case)
            },
            onDocumentsClick = { case -> // New lambda for documents
                val intent = Intent(this, DocumentsActivity::class.java).apply {
                    putExtra("caseId", case.id)
                }
                startActivity(intent)
            }
        )
        binding.casesRecyclerView.adapter = adapter
    }

    private fun loadCases() {
        caseList = XmlManager.loadCases(this).toMutableList() // Convert to mutable list if needed for remove operation
        adapter.submitList(caseList.toList()) // Submit a copy to the adapter
        if (caseList.isEmpty()) {
            Toast.makeText(this, "هنوز پرونده‌ای ثبت نشده است.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmAndDeleteCase(case: Case) {
        AlertDialog.Builder(this)
            .setTitle("حذف پرونده")
            .setMessage("آیا از حذف پرونده \"${case.title}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteCase(case)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteCase(case: Case) {
        val initialSize = caseList.size
        caseList.remove(case) // Remove from local list
        if (caseList.size < initialSize) { // Check if removal was successful
            XmlManager.saveCases(this, caseList) // Save the updated list to XML
            XmlManager.deleteDocumentsForCase(this, case.id) // Delete associated documents
            Toast.makeText(this, "پرونده با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "خطا در حذف پرونده.", Toast.LENGTH_SHORT).show()
        }
        loadCases() // Reload cases after deletion to refresh the list from XML
    }

    override fun onResume() {
        super.onResume()
        loadCases() // Refresh list when returning from Add/Edit activities
    }
}