package com.lawyer_archives.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lawyer_archives.utils.XmlManager
import com.lawyer_archives.utils.CalendarUtils

/**
 * BootCompletedReceiver is a BroadcastReceiver that listens for the BOOT_COMPLETED action.
 * When the device finishes booting, this receiver is triggered to reschedule all
 * previously set court session alarms. This is necessary because alarms are cancelled
 * when the device is rebooted.
 */
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check if the received intent is indeed the BOOT_COMPLETED action
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // It's good practice to run heavy operations on a background thread,
            // but for simple XML loading and alarm scheduling, it might be acceptable
            // directly in onReceive if the number of sessions is small.
            // For larger datasets, consider using WorkManager or a Service.

            // Load all existing court sessions from XML
            val sessions = XmlManager.loadSessions(context)

            // Reschedule alarms for each session
            for (session in sessions) {
                // Only reschedule if the session is not completed
                if (!session.isCompleted) {
                    CalendarUtils.scheduleSessionAlarm(context, session)
                }
            }
        }
    }
}