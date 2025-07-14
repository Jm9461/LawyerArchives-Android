// LawyerArchives/app/src/main/java/com/lawyer_archives/models/RealClient.kt
package com.lawyer_archives.models

import java.util.UUID

data class RealClient(
    override val id: String = UUID.randomUUID().toString(),
    val fullName: String, // نام و نام خانوادگی
    val fatherName: String, // نام پدر
    val idCardNumber: String, // شماره شناسنامه
    val nationalId: String, // شماره ملی
    val birthDate: String, // تاریخ تولد (شمسی)
    val birthPlace: String, // محل تولد
    val address: String, // آدرس
    val phone: String, // تلفن
    override val phoneNumber: String, // موبایل
    val occupation: String, // شغل
    val email: String, // ایمیل
    val postalCode: String, // کد پستی
    val description: String, // توضیحات
    val referredCasesCount: Int = 0, // تعداد پرونده‌های ارجاعی
    override val addedDate: String // تاریخ اضافه شدن
) : ClientEntry {
    override val name: String
        get() = fullName
}