package com.lawyer_archives.models

// Your existing imports and class definition
import java.util.UUID

data class Meeting(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val clientName: String,
    val description: String,
    val date: String, // مثلا "1404/05/23"
    val time: String, // اضافه شده است، مثلا "15:30"
    val addedDate: String // تاریخ اضافه شدن رکورد به برنامه
)