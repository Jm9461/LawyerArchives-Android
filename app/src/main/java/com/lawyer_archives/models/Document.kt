// LawyerArchives/app/src/main/java/com/lawyer_archives/models/Document.kt
package com.lawyer_archives.models

import java.util.UUID

data class Document(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var filePath: String, // Absolute path to the file
    var mimeType: String, // e.g., "application/pdf", "image/jpeg"
    var relatedCaseId: String // ID of the case this document belongs to
)