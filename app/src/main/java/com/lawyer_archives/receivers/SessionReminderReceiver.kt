package com.lawyer_archives.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lawyer_archives.R
import com.lawyer_archives.activities.MainActivity

class SessionReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "یادآوری"
        val description = intent.getStringExtra("description") ?: ""
        val date = intent.getStringExtra("courtDate") ?: intent.getStringExtra("date") ?: "" // Check for both keys
        val time = intent.getStringExtra("courtTime") ?: intent.getStringExtra("time") ?: "" // Check for both keys
        val branch = intent.getStringExtra("courtBranch") ?: ""
        val clientName = intent.getStringExtra("clientName") ?: ""

        // Create an Intent to open the MainActivity when the notification is clicked
        val mainIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Required for Android 12+
        )

        // Build the notification
        val builder = NotificationCompat.Builder(context, "sessionReminderChannel")
            .setSmallIcon(R.drawable.ic_reminder) // Use a suitable icon
            .setContentTitle(title)
            .setContentText("$description - $date ساعت $time ${if (branch.isNotEmpty()) " - شعبه: $branch" else ""} ${if (clientName.isNotEmpty()) " - با: $clientName" else ""}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Remove the notification when clicked

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "sessionReminderChannel",
                "Session Reminders", // User-visible name of the channel
                NotificationManager.IMPORTANCE_HIGH
            )
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Show the notification
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(UUID.randomUUID().hashCode(), builder.build()) // Use a unique ID
    }
}