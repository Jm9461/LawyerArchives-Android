// LawyerArchives/app/src/main/java/com/lawyer_archives/models/Case.kt
package com.lawyer_archives.models

import java.util.UUID

data class Case(
    val id: String = UUID.randomUUID().toString(),
    var title: String, // عنوان پرونده (از لیست انتخاب می‌شود)
    var formationDate: String, // تاریخ تشکیل پرونده (تقویم)
    var clientName: String, // نام موکل (از لیست موکلین ثبت شده)
    var clientRole: String, // سمت موکل (از لیست انتخاب می‌شود)
    var caseSubject: String, // موضوع پرونده (نوشتاری)
    var status: String, // وضعیت پرونده (از لیست انتخاب می‌شود)
    var process: String, // روند پرونده (از لیست انتخاب می‌شود)
    var caseNumber: String, // شماره پرونده (نوشتاری)
    var archiveNumber: String, // شماره بایگانی (نوشتاری)
    var cityJudiciary: String, // دادگستری شهرستان (از لیست انتخاب می‌شود)
    var courtLevelAndType: String, // درجه و نوع دادگاه (از لیست انتخاب می‌شود)
    var opponentInfo: String, // اطلاعات طرف دعوا (نوشتاری)
    var powerOfAttorneyNumber: String, // شماره وکالت‌نامه (نوشتاری)
    val addedDate: String // تاریخ اضافه شدن (معمولاً همان تاریخ تشکیل پرونده یا زمان ثبت)
)