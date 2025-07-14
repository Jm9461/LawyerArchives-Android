package com.lawyer_archives.models

// Your existing imports and class definition
import java.util.UUID

data class CourtSession(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val clientName: String,
    val courtDate: String, // مثلا "1404/05/23"
    val courtTime: String, // اضافه شده است، مثلا "10:00"
    val courtBranch: String,
    val status: String,
    val addedDate: String // تاریخ اضافه شدن رکورد به برنامه
)