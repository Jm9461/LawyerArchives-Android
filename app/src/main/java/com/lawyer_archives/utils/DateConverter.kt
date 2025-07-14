package com.lawyer_archives.utils

import com.alirezaashrafi.persiancalendar.models.GregorianDate
import com.alirezaashrafi.persiancalendar.models.PersianDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateConverter {

    private val persianDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale("fa", "IR"))
    private val gregorianDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)


    fun convertPersianToGregorian(persianDateString: String): Date? {
        // Example: "1404/05/23" -> Gregorian Date
        val parts = persianDateString.split("/")
        if (parts.size == 3) {
            try {
                val year = parts[0].toInt()
                val month = parts[1].toInt()
                val day = parts[2].toInt()
                val persianDate = PersianDate(year, month, day)
                return persianDate.toGregorian().time
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun convertGregorianToPersian(gregorianDate: Date): String {
        val gregorian = GregorianDate(gregorianDate)
        val persian = gregorian.toPersian()
        return "${persian.year}/${String.format("%02d", persian.month)}/${String.format("%02d", persian.day)}"
    }

    fun getTodayGregorianDate(): Date {
        return Date()
    }

    // Helper to format Date objects for display or internal comparison if needed
    fun formatDateForComparison(date: Date): String {
        return gregorianDateFormat.format(date) // Using a consistent format for comparison
    }
}