// LawyerArchives/app/src/main/java/com/lawyer_archives/activities/DocumentsActivity.kt
package com.lawyer_archives.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.R
import com.lawyer_archives.adapters.DocumentAdapter
import com.lawyer_archives.databinding.ActivityDocumentsBinding
import com.lawyer_archives.models.Document
import com.lawyer_archives.utils.XmlManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class DocumentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentsBinding
    private lateinit var documentList: MutableList<Document>
    private lateinit var adapter: DocumentAdapter
    private var relatedCaseId: String? = null // To link documents to a specific case

    // ActivityResultLauncher for picking files
    private val pickDocumentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                saveDocumentToFile(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        relatedCaseId = intent.getStringExtra("caseId")
        if (relatedCaseId == null) {
            Toast.makeText(this, "شناسه پرونده یافت نشد.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()
        loadDocuments()

        binding.fabAddDocument.setOnClickListener {
            openFilePicker()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewDocuments.layoutManager = LinearLayoutManager(this)
        adapter = DocumentAdapter(
            onOpenClick = { document ->
                openDocument(document)
            },
            onDeleteClick = { document ->
                confirmAndDeleteDocument(document)
            }
        )
        binding.recyclerViewDocuments.adapter = adapter
    }

    private fun loadDocuments() {
        // Load all documents and filter by relatedCaseId
        val allDocuments = XmlManager.loadDocuments(this)
        documentList = allDocuments.filter { it.relatedCaseId == relatedCaseId }.toMutableList()
        adapter.submitList(documentList.toList())
        if (documentList.isEmpty()) {
            Toast.makeText(this, "هیچ سند/مدرکی برای این پرونده وجود ندارد.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*" // Allow all file types
            // Optionally, restrict to specific types if needed:
            // type = "application/pdf"
            // type = "image/jpeg"
            // type = "image/png"
        }
        pickDocumentLauncher.launch(Intent.createChooser(intent, "انتخاب سند"))
    }

    private fun saveDocumentToFile(uri: Uri) {
        val contentResolver = applicationContext.contentResolver
        val fileName = getFileName(uri) ?: "document_${System.currentTimeMillis()}"
        val fileExtension = contentResolver.getType(uri)?.substringAfterLast('/') ?: "unknown"
        val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

        val documentsDir = File(filesDir, "documents")
        if (!documentsDir.exists()) {
            documentsDir.mkdirs()
        }

        val newFile = File(documentsDir, "${UUID.randomUUID()}.${fileExtension}")

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(newFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            val newDocument = Document(
                id = UUID.randomUUID().toString(),
                name = fileName,
                filePath = newFile.absolutePath,
                mimeType = mimeType,
                relatedCaseId = relatedCaseId!!
            )
            documentList.add(newDocument)
            XmlManager.saveDocuments(this, XmlManager.loadDocuments(this).toMutableList().apply { add(newDocument) }) // Save all documents
            adapter.submitList(documentList.toList())
            Toast.makeText(this, "سند با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "خطا در ذخیره سند: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }

    private fun openDocument(document: Document) {
        val file = File(document.filePath)
        if (file.exists()) {
            val uri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, document.mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            try {
                startActivity(Intent.createChooser(intent, "باز کردن سند با"))
            } catch (e: Exception) {
                Toast.makeText(this, "برنامه‌ای برای باز کردن این نوع فایل یافت نشد.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "فایل پیدا نشد.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmAndDeleteDocument(document: Document) {
        AlertDialog.Builder(this)
            .setTitle("حذف سند")
            .setMessage("آیا از حذف سند \"${document.name}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteDocument(document)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteDocument(document: Document) {
        val file = File(document.filePath)
        val deletedFromFileSystem = file.delete()

        if (deletedFromFileSystem) {
            val allDocuments = XmlManager.loadDocuments(this).toMutableList()
            val initialSize = allDocuments.size
            allDocuments.removeIf { it.id == document.id }

            if (allDocuments.size < initialSize) {
                XmlManager.saveDocuments(this, allDocuments)
                loadDocuments() // Reload to refresh the list
                Toast.makeText(this, "سند با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "خطا در حذف سند از لیست.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "خطا در حذف فایل از سیستم ذخیره‌سازی.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadDocuments() // Refresh the list when returning to the activity
    }
}