package com.lawyer_archives.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.DocumentAdapter
import com.lawyer_archives.databinding.ActivityCaseDocumentsBinding
import com.lawyer_archives.models.Document
import com.lawyer_archives.utils.XmlManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CaseDocumentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseDocumentsBinding
    private lateinit var documentList: MutableList<Document>
    private lateinit var adapter: DocumentAdapter
    private var caseId: String? = null
    private var caseTitle: String? = null

    // Activity Result Launcher for picking files
    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let {
                caseId?.let { id ->
                    copyFileToInternalStorage(it, id)
                }
            }
        }
    }

    // Activity Result Launcher for requesting permissions
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openFilePicker()
            } else {
                Toast.makeText(this, "مجوز دسترسی به حافظه برای افزودن سند لازم است.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        caseId = intent.getStringExtra("caseId")
        caseTitle = intent.getStringExtra("caseTitle")

        // Set title for the activity
        binding.documentsHeader.text = caseTitle ?: "اسناد پرونده" // Changed to documentsHeader

        setupRecyclerView()
        loadDocumentsForCase()

        binding.addDocumentButton.setOnClickListener { // Changed to addDocumentButton
            checkPermissionsAndOpenFilePicker()
        }
    }

    private fun setupRecyclerView() {
        binding.documentsRecyclerView.layoutManager = LinearLayoutManager(this) // Changed to documentsRecyclerView
        documentList = mutableListOf() // Initialize an empty list
        adapter = DocumentAdapter(
            onDocumentClick = { document -> // Changed to onDocumentClick
                openDocument(document)
            },
            onDeleteClick = { document ->
                confirmAndDeleteDocument(document)
            }
        )
        binding.documentsRecyclerView.adapter = adapter // Changed to documentsRecyclerView
    }

    private fun loadDocumentsForCase() {
        // Load all documents, then filter by caseId
        // تغییر: فراخوانی XmlManager.loadDocuments بدون caseId و فیلتر کردن در اینجا
        documentList = XmlManager.loadDocuments(this).filter { it.caseId == caseId }.toMutableList()
        if (documentList.isEmpty()) {
            binding.emptyListMessage.visibility = android.view.View.VISIBLE // Assuming you have an emptyListMessage TextView in your XML
        } else {
            binding.emptyListMessage.visibility = android.view.View.GONE // Assuming you have an emptyListMessage TextView in your XML
        }
        adapter.submitList(documentList.toList())
    }

    private fun checkPermissionsAndOpenFilePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // No explicit READ_EXTERNAL_STORAGE needed for API 33+ for media files picked by system picker
            // For general files, ACTION_OPEN_DOCUMENT is preferred.
            openFilePicker()
        } else {
            // For older Android versions, request READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openFilePicker()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*" // Allow all file types
            // Optionally, specify allowed mime types for common documents
            val mimeTypes = arrayOf(
                "application/pdf",
                "image/jpeg",
                "image/png",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            )
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        pickFileLauncher.launch(intent)
    }

    private fun copyFileToInternalStorage(uri: Uri, caseId: String) {
        val fileName = getFileName(uri)
        val fileExtension = getFileExtension(fileName)
        val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
        val destinationFile = File(filesDir, uniqueFileName) // Save in app's internal storage

        try {
            contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
                FileOutputStream(destinationFile).use { outputStream: FileOutputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Toast.makeText(this, "فایل با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show()
            addDocument(destinationFile.absolutePath, fileName, fileExtension, caseId)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "خطا در ذخیره فایل: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex)
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
        return result ?: "unknown_file"
    }

    private fun getFileExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex != -1 && dotIndex < fileName.length - 1) {
            fileName.substring(dotIndex + 1)
        } else {
            ""
        }
    }

    private fun addDocument(filePath: String, title: String, fileExtension: String, caseId: String) {
        val newDocument = Document(
            id = UUID.randomUUID().toString(),
            caseId = caseId,
            title = title,
            filePath = filePath,
            fileExtension = fileExtension,
            addedDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
        )
        XmlManager.addDocument(this, newDocument) // استفاده از متد addDocument در XmlManager
        loadDocumentsForCase() // Reload all documents for the current case
    }


    private fun openDocument(document: Document) {
        val file = File(document.filePath)
        if (file.exists()) {
            try {
                val uri = androidx.core.content.FileProvider.getUriForFile(
                    this,
                    applicationContext.packageName + ".fileprovider",
                    file
                )
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, getMimeType(document.fileExtension))
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "برنامه‌ای برای باز کردن این فایل یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "فایل پیدا نشد. ممکن است حذف شده باشد.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMimeType(fileExtension: String): String {
        return when (fileExtension.lowercase(Locale.ROOT)) {
            "pdf" -> "application/pdf"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            else -> "*/*"
        }
    }

    private fun confirmAndDeleteDocument(document: Document) {
        AlertDialog.Builder(this)
            .setTitle("حذف سند")
            .setMessage("آیا از حذف سند \"${document.title}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteDocument(document)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteDocument(document: Document) {
        // Delete the physical file first
        val file = File(document.filePath)
        if (file.exists()) {
            file.delete()
            Toast.makeText(this, "فایل حذف شد.", Toast.LENGTH_SHORT).show()
        }

        // Remove from list and save XML
        XmlManager.deleteDocument(this, document.id) // استفاده از متد deleteDocument در XmlManager
        loadDocumentsForCase() // Refresh the list after deletion
        Toast.makeText(this, "سند با موفقیت از لیست حذف شد.", Toast.LENGTH_SHORT).show()
    }

    // اضافه کردن onResume برای بارگذاری مجدد لیست در صورت بازگشت به این اکتیویتی
    override fun onResume() {
        super.onResume()
        loadDocumentsForCase()
    }
}