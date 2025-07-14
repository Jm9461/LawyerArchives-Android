package com.lawyer_archives.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.lawyer_archives.models.CourtSession // مطمئن شوید این import صحیح است
import com.lawyer_archives.receivers.SessionReminderReceiver // مطمئن شوید این import صحیح است
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtils {

    /**
     * Retrieves the current date formatted as "yyyy/MM/dd".
     * @return A string representing the current date.
     */
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Parses a date string formatted as "yyyy/MM/dd" into a Date object.
     * @param dateString The date string to parse.
     * @return A Date object if parsing is successful, otherwise null.
     */
    fun parseDate(dateString: String): Date? {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return try {
            sdf.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Schedules an alarm for a court session using AlarmManager.
     * The alarm will trigger the SessionReminderReceiver.
     *
     * @param context The application context.
     * @param session The CourtSession object for which to schedule the alarm.
     */
    fun scheduleSessionAlarm(context: Context, session: CourtSession) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, SessionReminderReceiver::class.java).apply {
            action = "com.lawyer_archives.ACTION_SESSION_REMINDER"
            // Pass session data to the intent so the receiver can display relevant information
            putExtra("session_id", session.id)
            putExtra("session_case_title", session.caseTitle)
            putExtra("session_date", session.sessionDate)
            putExtra("session_location", session.location)
        }

        // Use a unique request code for each session to avoid overwriting other alarms.
        // Using hashCode of session.id is a simple way to get a unique integer.
        val requestCode = session.id.hashCode()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            // FLAG_IMMUTABLE is required for Android 6.0 (API 23) and above
            // FLAG_UPDATE_CURRENT will update an existing PendingIntent if one with the same request code exists.
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            // Define the date format expected from session.sessionDate.
            // If your date input from editSessionDate is different (e.g., "yyyy-MM-dd HH:mm:ss"), adjust this.
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            val date = dateFormat.parse(session.sessionDate)
            val calendar = Calendar.getInstance().apply {
                if (date != null) {
                    time = date
                    // Optional: Set the reminder a few minutes before the actual session time.
                    // This example sets it 15 minutes before.
                    add(Calendar.MINUTE, -15) // Reminder 15 minutes before the session
                }
            }

            // Check if the calculated alarm time is in the past. If so, don't schedule.
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                Toast.makeText(context, "زمان یادآور در گذشته است. آلارم تنظیم نشد.", Toast.LENGTH_LONG).show()
                return
            }

            // Schedule the alarm based on Android version specifics
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 (API 31) and above
                // For exact alarms on Android 12+, app needs SCHEDULE_EXACT_ALARM permission and user grant.
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                } else {
                    Toast.makeText(context, "برنامه اجازه تنظیم آلارم دقیق را ندارد. لطفاً در تنظیمات اجازه دهید.", Toast.LENGTH_LONG).show()
                    // You might want to direct the user to app settings to grant the permission.
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6.0 (API 23) to Android 11 (API 30)
                // Use setExactAndAllowWhileIdle for exact alarms that fire even in Doze mode.
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else { // Older Android versions (below API 23)
                // Use setExact for exact alarms on older versions.
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
            Toast.makeText(context, "یادآور جلسه برای ${session.caseTitle} در تاریخ ${session.sessionDate} تنظیم شد.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "خطا در تنظیم یادآور: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Cancels an existing alarm for a specific court session.
     *
     * @param context The application context.
     * @param sessionId The ID of the session whose alarm needs to be cancelled.
     */
    fun cancelSessionAlarm(context: Context, sessionId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SessionReminderReceiver::class.java).apply {
            action = "com.lawyer_archives.ACTION_SESSION_REMINDER"
            // It's crucial that the Intent data (action, extras, etc.) matches the Intent
            // that was used to set the alarm, for the PendingIntent to be matched correctly.
            // If you used session_id in extras, it's a good idea to add it here too if not unique by requestCode alone.
        }
        val requestCode = sessionId.hashCode() // Must be the same request code used to set the alarm
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT // Use same flags as setting the alarm
        )
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "یادآور لغو شد.", Toast.LENGTH_SHORT).show()
    }
}
