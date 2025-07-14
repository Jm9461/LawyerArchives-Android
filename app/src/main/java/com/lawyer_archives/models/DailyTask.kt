// LawyerArchives/app/src/main/java/com/lawyer_archives/models/DailyTask.kt
package com.lawyer_archives.models

import java.util.UUID

data class DailyTask(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String,
    var dueDate: String, // You might want to use a Date object or a specific date format
    var relatedClientOrCase: String? = null // Optional:  Link to a client or case
)