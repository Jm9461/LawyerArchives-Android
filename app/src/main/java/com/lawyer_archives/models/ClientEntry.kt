// LawyerArchives/app/src/main/java/com/lawyer_archives/models/ClientEntry.kt
package com.lawyer_archives.models

import java.util.UUID

// یک رابط برای نمایش ویژگی‌های مشترک برای همه انواع موکلین
interface ClientEntry {
    val id: String
    val name: String // یک فیلد نام مشترک (مثلاً نام کامل برای حقیقی، نام شرکت برای حقوقی)
    val phoneNumber: String // شماره موبایل یا تلفن اصلی
    val addedDate: String
}